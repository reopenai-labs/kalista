package com.reopenai.kalista.grpc.client.invoker;

import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import io.grpc.Channel;
import org.springframework.core.Ordered;

/**
 * Created by Allen Huang
 */
public interface GrpcClientInvokerFactory extends Ordered {

    GrpcClientInvoker create(Channel channel, GrpcMethodDetail methodDetail);

    @Override
    default int getOrder() {
        return Integer.MIN_VALUE;
    }

}
