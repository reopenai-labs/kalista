package com.reopenai.kalista.grpc.client;

import com.reopenai.kalista.core.lang.reflect.ProxyUtil;
import com.reopenai.kalista.grpc.client.invoker.GrpcClientInvoker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class GrpcClientProxy implements InvocationHandler {

    private final Logger logger;

    private final Map<Method, GrpcClientInvoker> invokers;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return method.invoke(this, args);
        }
        if (!Modifier.isAbstract(method.getModifiers())) {
            return ProxyUtil.invokeDefaultMethod(proxy, method, args);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[gRPC]requesting.args:{}", Arrays.asList(args));
        }
        GrpcClientInvoker invoker = invokers.get(method);
        if (invoker != null) {
            return invoker.invoke(args);
        }
        throw new IllegalStateException("Unexpected method invocation: " + method);
    }

}
