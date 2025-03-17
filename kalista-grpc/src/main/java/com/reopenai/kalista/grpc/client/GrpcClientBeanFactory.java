package com.reopenai.kalista.grpc.client;

import com.reopenai.kalista.grpc.client.annotation.GrpcStub;
import com.reopenai.kalista.grpc.client.invoker.GrpcClientInvoker;
import com.reopenai.kalista.grpc.client.invoker.GrpcClientInvokerFactory;
import com.reopenai.kalista.grpc.common.GrpcDefinitionUtil;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.env.Environment;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class GrpcClientBeanFactory implements FactoryBean<Object>, ApplicationContextAware, EnvironmentAware {

    private final Class<?> targetType;

    private final ClassLoader classLoader;

    private Environment environment;

    private GrpcChannelFactory channelFactory;

    private Collection<GrpcClientInvokerFactory> invokerFactories;

    public GrpcClientBeanFactory(String beanClassName, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.targetType = ClassUtils.resolveClassName(beanClassName, classLoader);
    }

    @Override
    public Object getObject() throws Exception {
        Logger logger = LoggerFactory.getLogger(this.targetType);
        Class<?>[] proxiedInterfaces = ClassUtils.getAllInterfacesForClass(this.targetType);
        Map<Method, GrpcClientInvoker> invokers = this.createInvokers();
        GrpcClientProxy proxy = new GrpcClientProxy(logger, invokers);
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, proxy);
    }

    public Map<Method, GrpcClientInvoker> createInvokers() {
        GrpcStub stub = this.targetType.getAnnotation(GrpcStub.class);
        ManagedChannel channel = channelFactory.createChannel(environment.resolvePlaceholders(stub.service()));

        Set<Method> methods = MethodIntrospector.selectMethods(this.targetType, GrpcDefinitionUtil::methodMatcher);
        Map<Method, GrpcClientInvoker> invokers = new HashMap<>(methods.size());
        GrpcDefinitionUtil.Checker checker = new GrpcDefinitionUtil.Checker();
        for (Method method : methods) {
            checker.check(method);
            GrpcClientInvoker invoker = null;
            GrpcMethodDetail methodDetail = new GrpcMethodDetail(this.environment, method);
            for (GrpcClientInvokerFactory invokerFactory : invokerFactories) {
                invoker = invokerFactory.create(channel, methodDetail);
                if (invoker != null) {
                    invokers.put(method, invoker);
                    break;
                }
            }
            if (invoker == null) {
                throw new BeanCreationException("gRPC Client当前不支持此方法: " + method.getDeclaringClass().getSimpleName() + "." + method.getName());
            }
        }
        return invokers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.channelFactory = applicationContext.getBean(GrpcChannelFactory.class);
        this.invokerFactories = applicationContext.getBeansOfType(GrpcClientInvokerFactory.class).values();
    }

    @Override
    public Class<?> getObjectType() {
        return targetType;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
