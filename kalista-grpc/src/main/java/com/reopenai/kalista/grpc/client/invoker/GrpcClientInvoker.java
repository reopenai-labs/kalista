package com.reopenai.kalista.grpc.client.invoker;

import com.reopenai.kalista.grpc.common.GrpcMethodDetail;

/**
 * Created by Allen Huang
 */
public interface GrpcClientInvoker {

    Object invoke(Object[] arguments);

    GrpcMethodDetail getMethodDetail();

}
