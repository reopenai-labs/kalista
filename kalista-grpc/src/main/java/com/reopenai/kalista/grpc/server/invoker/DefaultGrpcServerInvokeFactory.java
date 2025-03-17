package com.reopenai.kalista.grpc.server.invoker;

import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import io.grpc.stub.ServerCalls;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanCreationException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class DefaultGrpcServerInvokeFactory implements GrpcServerInvokeFactory {

    private final List<RpcSerialization> serializations;

    @Override
    public ServerCalls.UnaryMethod<byte[], byte[]> create(Object bean, GrpcMethodDetail methodDetail) {
        String protocol = methodDetail.getProtocol();
        RpcSerialization serialization = getSerialization(protocol);
        if (Mono.class.isAssignableFrom(methodDetail.getReturnClass())) {
            return new ReactorGrpcServerInvoker(bean, methodDetail, serialization);
        }
        return new BlockingGrpcServerInvoker(bean, methodDetail, serialization);
    }

    private RpcSerialization getSerialization(String protocol) {
        for (RpcSerialization serialization : serializations) {
            if (serialization.supportType().equals(protocol)) {
                return serialization;
            }
        }
        throw new BeanCreationException("No RpcSerialization found for protocol " + protocol);
    }
}
