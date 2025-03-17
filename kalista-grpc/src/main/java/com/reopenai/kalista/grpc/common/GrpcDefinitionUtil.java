package com.reopenai.kalista.grpc.common;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.grpc.client.annotation.GrpcStub;
import com.reopenai.kalista.grpc.common.marshaller.ByteArrayMarshaller;
import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * gRPC服务描述工具类
 *
 * @author Allen Huang
 */
public final class GrpcDefinitionUtil {

    /**
     * gRPC MethodDescriptor和Java Method映射关系的缓存
     */
    private static final Map<Method, MethodDescriptor<byte[], byte[]>> methodDescriptors = new HashMap<>(2);

    /**
     * 缓存服务的描述
     */
    private static final Map<Class<?>, ServiceDescriptor> serviceDescriptors = new HashMap<>(2);

    /**
     * 将一个Java方法解析成gRPC MethodDescriptor
     *
     * @param serviceName 方法的包名
     * @param method      目标方法名
     * @return MethodDescriptor实例
     */
    public static MethodDescriptor<byte[], byte[]> buildMethodDescriptor(String serviceName, Method method) {
        MethodDescriptor<byte[], byte[]> methodDescriptor = methodDescriptors.get(method);
        if (methodDescriptor == null) {
            synchronized (methodDescriptors) {
                methodDescriptor = methodDescriptors.get(method);
                if (methodDescriptor == null) {
                    methodDescriptor = MethodDescriptor.<byte[], byte[]>newBuilder()
                            .setType(MethodDescriptor.MethodType.UNARY)
                            .setFullMethodName(String.join("/", serviceName, method.getName()))
                            .setSampledToLocalTracing(true)
                            .setRequestMarshaller(ByteArrayMarshaller.INSTANCE)
                            .setResponseMarshaller(ByteArrayMarshaller.INSTANCE)
                            .build();
                    methodDescriptors.put(method, methodDescriptor);
                }
            }
        }
        return methodDescriptor;
    }

    /**
     * 获取一个服务的描述信息
     *
     * @param type 服务的类型
     * @return 服务的gRPC描述
     */
    public static ServiceDescriptor buildServiceDescriptor(Class<?> type, Environment environment) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("RPC定义类必须是一个接口.");
        }
        ServiceDescriptor descriptor = serviceDescriptors.get(type);
        if (descriptor == null) {
            synchronized (serviceDescriptors) {
                descriptor = serviceDescriptors.get(type);
                if (descriptor == null) {
                    String serviceName = parseServiceName(type, environment);
                    ServiceDescriptor.Builder builder = ServiceDescriptor.newBuilder(serviceName);
                    for (Class<?> parent = type; parent != null; parent = parent.getSuperclass()) {
                        for (Method method : parent.getDeclaredMethods()) {
                            if (Modifier.isAbstract(method.getModifiers())) {
                                builder.addMethod(buildMethodDescriptor(serviceName, method));
                            }
                        }
                    }
                    descriptor = builder.build();
                    serviceDescriptors.put(type, descriptor);
                }
            }
        }
        return descriptor;
    }

    public static String parseServiceName(Class<?> type, Environment environment) {
        GrpcStub stub = type.getAnnotation(GrpcStub.class);
        if (stub != null) {
            String packages = stub.packages();
            if (StrUtil.isNotBlank(packages)) {
                packages = environment.resolvePlaceholders(packages);
            } else {
                packages = type.getPackageName();
            }
            String name = stub.name();
            if (StrUtil.isNotBlank(name)) {
                name = environment.resolvePlaceholders(name);
            } else {
                name = type.getSimpleName();
            }
            return String.join(".", packages, name);
        }
        return String.join(".", type.getPackageName(), type.getSimpleName());
    }

    public static boolean methodMatcher(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return false;
        }
        if (!declaringClass.isAnnotationPresent(GrpcStub.class)) {
            return false;
        }
        return Modifier.isAbstract(method.getModifiers());
    }

    public static final class Checker {

        private final Set<String> registeredMethods = new HashSet<>();

        public void check(Method method) {
            String name = method.getName();
            if (method.getParameterCount() > 1) {
                throw new IllegalArgumentException(String.format("创建gRPC接口失败,gRPC的参数不能超过1个.see: %s#%s", method.getDeclaringClass().getName(), name));
            }
            if (registeredMethods.contains(name)) {
                throw new IllegalArgumentException(String.format("创建gRPC接口失败,方法名重复.see: %s#%s", method.getDeclaringClass().getName(), name));
            }
            registeredMethods.add(name);
        }

    }

}
