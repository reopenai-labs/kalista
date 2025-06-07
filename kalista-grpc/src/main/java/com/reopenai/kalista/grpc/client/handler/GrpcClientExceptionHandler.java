package com.reopenai.kalista.grpc.client.handler;

import com.reopenai.kalista.core.lang.exception.SystemException;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;

/**
 * Created by Allen Huang
 */
public interface GrpcClientExceptionHandler {

    SystemException handle(GrpcMethodDetail methodDetail, Throwable cause);

}
