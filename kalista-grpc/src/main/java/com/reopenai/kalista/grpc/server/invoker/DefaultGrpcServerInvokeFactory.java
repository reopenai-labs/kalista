package com.reopenai.kalista.grpc.server.invoker;

import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import io.grpc.stub.ServerCalls;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class DefaultGrpcServerInvokeFactory implements GrpcServerInvokeFactory {

    private final List<RpcSerialization> serializations;

    private final GrpcExceptionHandler exceptionHandler;

    @Override
    public ServerCalls.UnaryMethod<byte[], byte[]> create(Object bean, GrpcMethodDetail methodDetail) {
        String protocol = methodDetail.getProtocol();
        RpcSerialization serialization = getSerialization(protocol);
        if (Mono.class.isAssignableFrom(methodDetail.getReturnClass())) {
            return new ReactorGrpcServerInvoker(bean, methodDetail, serialization, exceptionHandler);
        }
        return new BlockingGrpcServerInvoker(bean, methodDetail, serialization, exceptionHandler);
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
