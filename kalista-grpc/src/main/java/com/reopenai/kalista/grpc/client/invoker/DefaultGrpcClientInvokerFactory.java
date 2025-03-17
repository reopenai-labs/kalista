package com.reopenai.kalista.grpc.client.invoker;

import com.reopenai.kalista.grpc.client.handler.GrpcClientExceptionHandler;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanCreationException;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class DefaultGrpcClientInvokerFactory implements GrpcClientInvokerFactory {

    protected final Map<String, RpcSerialization> serializationMap;

    protected final GrpcClientExceptionHandler grpcClientExceptionHandler;

    @Override
    public GrpcClientInvoker create(Channel channel, GrpcMethodDetail methodDetail) {
        RpcSerialization serialization = serializationMap.get(methodDetail.getProtocol());
        if (serialization == null) {
            throw new BeanCreationException("No serialization found for protocol " + methodDetail.getProtocol() + ",method=" + methodDetail.getEndpoint());
        }
        if (Mono.class.isAssignableFrom(methodDetail.getReturnClass())) {
            GrpcMethodDetail.Builder builder = GrpcMethodDetail.from(methodDetail);
            ParameterizedType returnType = ((ParameterizedType) methodDetail.getReturnType());
            Type realReturnType = returnType.getActualTypeArguments()[0];
            builder.returnType(realReturnType);
            builder.returnClass((Class<?>) realReturnType);
            return new MonoGrpcClientInvoker(channel, builder.build(), serialization, grpcClientExceptionHandler);
        }
        return new BlockingGrpcClientInvoker(channel, methodDetail, serialization, grpcClientExceptionHandler);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
