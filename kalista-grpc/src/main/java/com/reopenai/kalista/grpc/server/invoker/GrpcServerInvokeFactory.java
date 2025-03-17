package com.reopenai.kalista.grpc.server.invoker;

import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import io.grpc.stub.ServerCalls;

/**
 * Created by Allen Huang
 */
public interface GrpcServerInvokeFactory {

    ServerCalls.UnaryMethod<byte[], byte[]> create(Object bean, GrpcMethodDetail methodDetail);

}
