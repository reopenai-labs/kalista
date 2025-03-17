package com.reopenai.kalista.grpc.client.handler;

import com.reopenai.kalista.grpc.common.GrpcException;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;

/**
 * Created by Allen Huang
 */
public interface GrpcClientExceptionHandler {

    GrpcException handle(GrpcMethodDetail methodDetail, Throwable cause);

}
