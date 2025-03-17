package com.reopenai.kalista.grpc.client.interceptor;

import com.reopenai.kalista.grpc.common.metadata.GrpcContextKey;
import io.grpc.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.grpc.client.GlobalClientInterceptor;

/**
 * Created by Allen Huang
 */
@GlobalClientInterceptor
public class ContextClientInterceptor implements ClientInterceptor, EnvironmentAware {

    private String serviceName;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(method, callOptions)) {
            public void start(ClientCall.Listener<RespT> responseListener, io.grpc.Metadata headers) {
                for (GrpcContextKey contextKey : GrpcContextKey.values()) {
                    if (GrpcContextKey.REFERRER_SERVICE == contextKey) {
                        headers.put(contextKey.getMetadataKey(), serviceName);
                    } else {
                        String value = contextKey.getContextKey().get();
                        if (value != null) {
                            headers.put(contextKey.getMetadataKey(), value);
                        }
                    }
                }
                super.start(responseListener, headers);
            }
        };
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.serviceName = environment.resolveRequiredPlaceholders("${spring.application.name:unknown}");
    }

}
