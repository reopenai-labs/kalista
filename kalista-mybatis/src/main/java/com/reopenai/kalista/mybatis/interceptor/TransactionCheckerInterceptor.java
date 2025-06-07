package com.reopenai.kalista.mybatis.interceptor;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 事务检查拦截器，所有的写操作都需要有事务
 *
 * @author Allen Huang
 */
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class TransactionCheckerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (SqlCommandType.SELECT != ms.getSqlCommandType() && !TransactionSynchronizationManager.isSynchronizationActive()) {
            log.error("[MyBatis]写操作必须包含事物.当前操作被拒绝.method={}", ms.getId());
            throw new SystemException(ErrorCode.Builtin.SERVER_ERROR);
        }
        return invocation.proceed();
    }

}
