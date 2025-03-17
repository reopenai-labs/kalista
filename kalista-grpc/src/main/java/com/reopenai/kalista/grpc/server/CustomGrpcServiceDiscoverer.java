package com.reopenai.kalista.grpc.server;

import com.reopenai.kalista.grpc.client.annotation.GrpcStub;
import com.reopenai.kalista.grpc.common.GrpcDefinitionUtil;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import com.reopenai.kalista.grpc.server.invoker.GrpcServerInvokeFactory;
import com.reopenai.kalista.grpc.server.test.EchoServiceGrpc;
import io.grpc.*;
import io.grpc.stub.ServerCalls;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.grpc.internal.ApplicationContextBeanLookupUtils;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.service.DefaultGrpcServiceDiscoverer;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.grpc.server.service.GrpcServiceConfigurer;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen Huang
 */
public class CustomGrpcServiceDiscoverer extends DefaultGrpcServiceDiscoverer implements EnvironmentAware {

    private final ApplicationContext applicationContext;

    private Environment environment;

    private List<ServerInterceptor> globalInterceptors;

    private final GrpcServerInvokeFactory grpcServerInvokeFactory;

    public CustomGrpcServiceDiscoverer(GrpcServiceConfigurer serviceConfigurer,
                                       ApplicationContext applicationContext,
                                       GrpcServerInvokeFactory grpcServerInvokeFactory) {
        super(serviceConfigurer, applicationContext);
        this.applicationContext = applicationContext;
        this.grpcServerInvokeFactory = grpcServerInvokeFactory;
    }

    @Override
    public List<ServerServiceDefinition> findServices() {
        List<ServerServiceDefinition> definitions = new ArrayList<>(super.findServices());
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(GrpcService.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            if (bean instanceof EchoServiceGrpc.EchoServiceImplBase) {
                continue;
            }
            Class<?> realClass = ClassUtils.getUserClass(bean);
            GrpcService grpcService = realClass.getAnnotation(GrpcService.class);
            Class<?>[] superInterfaces = ClassUtils.getAllInterfaces(bean);
            GrpcDefinitionUtil.Checker checker = new GrpcDefinitionUtil.Checker();
            for (Class<?> superInterface : superInterfaces) {
                GrpcStub stub = superInterface.getAnnotation(GrpcStub.class);
                if (stub != null) {
                    ServiceDescriptor serviceDescriptor = GrpcDefinitionUtil.buildServiceDescriptor(superInterface, this.environment);
                    ServerServiceDefinition.Builder builder = ServerServiceDefinition.builder(serviceDescriptor);
                    for (Method method : superInterface.getDeclaredMethods()) {
                        if (GrpcDefinitionUtil.methodMatcher(method)) {
                            checker.check(method);
                            GrpcMethodDetail methodDetail = new GrpcMethodDetail(this.environment, method);
                            ServerCalls.UnaryMethod<byte[], byte[]> unaryMethod = grpcServerInvokeFactory.create(bean, methodDetail);
                            ServerCallHandler<byte[], byte[]> handler = ServerCalls.asyncUnaryCall(unaryMethod);
                            builder.addMethod(methodDetail.getMethodDescriptor(), handler);
                        }
                    }
                    definitions.add(bindInterceptors(builder.build(), grpcService));
                }
            }
        }
        return definitions;
    }

    private List<ServerInterceptor> findGlobalInterceptors() {
        if (this.globalInterceptors == null) {
            this.globalInterceptors = ApplicationContextBeanLookupUtils.getBeansWithAnnotation(this.applicationContext,
                    ServerInterceptor.class, GlobalServerInterceptor.class);
        }
        return this.globalInterceptors;
    }

    private ServerServiceDefinition bindInterceptors(ServerServiceDefinition serviceDefinition, GrpcService serviceConfig) {
        List<ServerInterceptor> interceptors = new ArrayList<>(findGlobalInterceptors());

        for (Class<? extends ServerInterceptor> interceptorClass : serviceConfig.interceptors()) {
            ServerInterceptor serverInterceptor;
            if (this.applicationContext.getBeanNamesForType(interceptorClass).length > 0) {
                serverInterceptor = this.applicationContext.getBean(interceptorClass);
            } else {
                try {
                    serverInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            interceptors.add(serverInterceptor);
        }
        for (String interceptorName : serviceConfig.interceptorNames()) {
            interceptors.add(this.applicationContext.getBean(interceptorName, ServerInterceptor.class));
        }
        return ServerInterceptors.interceptForward(serviceDefinition, interceptors);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
