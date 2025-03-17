package com.reopenai.kalista.grpc.server.interceptor;

import com.reopenai.kalista.grpc.common.metadata.GrpcContextKey;
import io.grpc.*;

/**
 * Created by Allen Huang
 */
public class ContextServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        Context context = Context.current();
        for (GrpcContextKey value : GrpcContextKey.values()) {
            String payload = metadata.get(value.getMetadataKey());
            context = context.withValue(value.getContextKey(), payload);
        }
        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

}
