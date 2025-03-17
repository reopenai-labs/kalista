package com.reopenai.kalista.lock.advisor;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.core.lang.reflect.MethodDescription;
import com.reopenai.kalista.lock.annotation.DistributedLock;
import com.reopenai.kalista.lock.api.DistributedLockFactory;
import com.reopenai.kalista.lock.api.DistributedLocker;
import com.reopenai.kalista.lock.api.LockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAdvice implements MethodInterceptor {

    private final DistributedLockFactory lockFactory;

    private final Environment environment;

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private String applicationName;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        Method method = invocation.getMethod();
        DistributedLock locker = method.getAnnotation(DistributedLock.class);
        // 使用SpEL解析锁的名称
        String name = parseLockName(invocation.getThis(), method, locker.value(), args);
        String applicationName = getApplicationName();
        if (locker.includePrefix() && StrUtil.isNotBlank(applicationName)) {
            name = String.format("LOCK:%S:%s", applicationName, name);
            if (log.isDebugEnabled()) {
                log.debug("[DistributedLock]生成的锁名将包含服务前缀.");
            }
        } else {
            name = "LOCK:" + name;
        }
        DistributedLocker lockInstance = tryLock(locker.type(), name, locker.timeout(), locker.leaseTime());
        try {
            if (log.isDebugEnabled()) {
                log.debug("[DistributedLock]已获取到分布式锁.当前锁名称:{},持有的锁数量:{},过期剩余时间:{}", lockInstance.getName(), lockInstance.getHoldCount(), lockInstance.remainTimeToLive());
            }
            return invocation.proceed();
        } finally {
            lockInstance.unlock();
            if (log.isDebugEnabled()) {
                log.debug("[DistributedLock]已成功释放分布式锁.锁名称{},释放后持有锁数量:{}", lockInstance.getName(), lockInstance.getHoldCount());
            }
        }
    }

    private DistributedLocker tryLock(LockType type, String name, long timeout, long leaseTime) {
        return switch (type) {
            case FAIR -> lockFactory.lockFairLock(name, timeout, leaseTime);
            case READ_LOCK -> lockFactory.lockReadLock(name, timeout, leaseTime);
            case WRITE_LOCK -> lockFactory.lockWriteLock(name, timeout, leaseTime);
            case REENTRANT -> lockFactory.lockReentrantLock(name, timeout, leaseTime);
        };
    }

    private String parseLockName(Object target, Method originMethod, String name, Object[] args) {
        String lockName = name;
        if (StrUtil.isBlank(name)) {
            log.warn("[DistributedLock]未显示指定锁名称，将使用方法名称作为锁的key.注意: 这是不合适的行为，请显示的指定锁的Key");
            Class<?> declaringClass = originMethod.getDeclaringClass();
            lockName = String.join("#", declaringClass.getName(), originMethod.getName());
        } else {
            Expression expression = parser.parseExpression(lockName);

            MethodDescription methodDescription = new MethodDescription();
            methodDescription.setArgs(args);
            methodDescription.setTarget(target);
            methodDescription.setMethod(originMethod);
            methodDescription.setMethodName(originMethod.getName());
            methodDescription.setTargetClass(originMethod.getDeclaringClass());

            EvaluationContext context = new StandardEvaluationContext(methodDescription);
            lockName = expression.getValue(context, String.class);
            if (log.isDebugEnabled()) {
                log.debug("[DistributedLock]获取锁的SpEL表达式:{},解析后的锁名称:{}", name, lockName);
            }
        }
        return lockName;
    }

    public String getApplicationName() {
        if (applicationName == null) {
            applicationName = environment.getProperty("spring.application.name");
        }
        return applicationName;
    }
}
