package com.reopenai.kalista.grpc;

import com.reopenai.kalista.grpc.client.GrpcClientRegister;
import com.reopenai.kalista.grpc.client.handler.DefaultGrpcClientExceptionHandler;
import com.reopenai.kalista.grpc.client.handler.GrpcClientExceptionHandler;
import com.reopenai.kalista.grpc.client.interceptor.ContextClientInterceptor;
import com.reopenai.kalista.grpc.client.invoker.DefaultGrpcClientInvokerFactory;
import com.reopenai.kalista.grpc.client.invoker.GrpcClientInvokerFactory;
import com.reopenai.kalista.grpc.serialization.JsonSerialization;
import com.reopenai.kalista.grpc.serialization.ProtostuffSerialization;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import com.reopenai.kalista.grpc.server.CustomGrpcServiceDiscoverer;
import com.reopenai.kalista.grpc.server.handler.DefaultGrpcExceptionHandler;
import com.reopenai.kalista.grpc.server.interceptor.ContextServerInterceptor;
import com.reopenai.kalista.grpc.server.invoker.DefaultGrpcServerInvokeFactory;
import com.reopenai.kalista.grpc.server.invoker.GrpcServerInvokeFactory;
import com.reopenai.kalista.grpc.server.test.EchoService;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.grpc.autoconfigure.server.GrpcServerFactoryAutoConfiguration;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.server.ServerBuilderCustomizer;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import org.springframework.grpc.server.service.GrpcServiceConfigurer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Allen Huang
 */
@AutoConfiguration
@Import(GrpcClientRegister.class)
@AutoConfigureBefore(GrpcServerFactoryAutoConfiguration.class)
public class GrpcExtAutoConfiguration {

    @Bean
    public EchoService echoService() {
        return new EchoService();
    }

    @Bean
    public RpcSerialization protostuffGrpcSerialization() {
        return new ProtostuffSerialization();
    }

    @Bean
    public RpcSerialization jsonGrpcSerialization() {
        return new JsonSerialization();
    }

    @Configuration
    public static class GrpcClientExtConfiguration {

        @Bean
        @ConditionalOnMissingBean(GrpcClientInvokerFactory.class)
        public GrpcClientExceptionHandler grpcClientExceptionHandler() {
            return new DefaultGrpcClientExceptionHandler();
        }

        @Bean
        public ContextClientInterceptor contextClientInterceptor() {
            return new ContextClientInterceptor();
        }

        @Bean
        public DefaultGrpcClientInvokerFactory defaultGrpcClientInvokerFactory(List<RpcSerialization> serializations,
                                                                               GrpcClientExceptionHandler exceptionHandler) {
            Map<String, RpcSerialization> serializationMap = serializations
                    .stream()
                    .collect(Collectors.toMap(RpcSerialization::supportType, Function.identity()));
            return new DefaultGrpcClientInvokerFactory(serializationMap, exceptionHandler);
        }

        @Bean
        @ConditionalOnBooleanProperty("spring.threads.virtual.enabled")
        public GrpcChannelBuilderCustomizer<NettyChannelBuilder> grpcChannelBuilderCustomizer() {
            return (authority, builder) -> builder.executor(Executors.newVirtualThreadPerTaskExecutor());
        }

    }

    @Configuration
    @AutoConfigureBefore(GrpcServerFactoryAutoConfiguration.class)
    @ConditionalOnProperty(value = "spring.grpc.server.enabled", havingValue = "true", matchIfMissing = true)
    public static class GrpcServerExtConfiguration {

        @Bean
        public GrpcServerInvokeFactory grpcServerInvokeFactory(List<RpcSerialization> serializations) {
            return new DefaultGrpcServerInvokeFactory(serializations);
        }

        @Bean
        public CustomGrpcServiceDiscoverer grpcServiceDiscoverer(GrpcServiceConfigurer grpcServiceConfigurer,
                                                                 ApplicationContext applicationContext,
                                                                 GrpcServerInvokeFactory grpcServerInvokeFactory) {
            return new CustomGrpcServiceDiscoverer(grpcServiceConfigurer, applicationContext, grpcServerInvokeFactory);
        }

        @Bean
        public GrpcExceptionHandler defaultGrpcExceptionHandler() {
            return new DefaultGrpcExceptionHandler();
        }

        @Bean
        public ContextServerInterceptor contextServerInterceptor() {
            return new ContextServerInterceptor();
        }

        @Bean
        @ConditionalOnBooleanProperty("spring.threads.virtual.enabled")
        public ServerBuilderCustomizer<NettyServerBuilder> serverBuilderCustomizer() {
            return builder -> builder.executor(Executors.newVirtualThreadPerTaskExecutor());
        }

    }

}
