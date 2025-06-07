package com.reopenai.kalista.grpc.server.invoker;

import cn.hutool.core.util.IdUtil;
import com.reopenai.kalista.base.constants.EmptyConstants;
import com.reopenai.kalista.base.constants.SystemConstants;
import com.reopenai.kalista.core.bench.BenchMarker;
import com.reopenai.kalista.core.bench.BenchMarkers;
import com.reopenai.kalista.core.lang.exception.SystemException;
import com.reopenai.kalista.grpc.common.GrpcMethodDetail;
import com.reopenai.kalista.grpc.common.metadata.GrpcContextKey;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class BlockingGrpcServerInvoker implements ServerCalls.UnaryMethod<byte[], byte[]> {

    private final Object bean;

    private final GrpcMethodDetail methodDetail;

    private final RpcSerialization serialization;

    private final GrpcExceptionHandler exceptionHandler;

    @Override
    public void invoke(byte[] bytes, StreamObserver<byte[]> streamObserver) {
        BenchMarker benchMarker = BenchMarkers.current();
        benchMarker.mark("inter");
        String requestId = Optional.ofNullable(GrpcContextKey.REQUEST_ID.getContextKey().get())
                .orElseGet(IdUtil::nanoId);
        MDC.put(SystemConstants.REQUEST_ID, requestId);
        StringBuilder builder = new StringBuilder();
        builder.append("gRPC Request: [endpoint=").append(methodDetail.getEndpoint())
                .append("]#[referrer=").append(GrpcContextKey.REFERRER_SERVICE.getContextKey().get())
                .append("]#[requestId=").append(requestId).append(']');
        try {
            Object[] arguments;
            Parameter parameter = methodDetail.getParameter();
            if (parameter == null) {
                arguments = EmptyConstants.EMPTY_OBJECT_ARRAY;
            } else if (bytes.length == 0) {
                arguments = new Object[]{null};
            } else {
                Object deserializer = serialization.deserializer(bytes, parameter.getParameterizedType());
                arguments = new Object[]{deserializer};
            }
            Object result = methodDetail.getMethod().invoke(bean, arguments);
            byte[] serializer = serialization.serializer(result);
            streamObserver.onNext(serializer);
            streamObserver.onCompleted();
        } catch (Throwable e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            if (cause instanceof SystemException err) {
                builder.append("#[errorCode=").append(err.getErrorCode().getValue())
                        .append("]#[errorParams=").append(Arrays.toString(err.getArgs())).append("]");
            }
            builder.append("#[errMsg=").append(cause.getMessage()).append("]");
            methodDetail.getLogger().error("{}", builder, cause);
            streamObserver.onError(exceptionHandler.handleException(cause));
        } finally {
            benchMarker.mark("outer");
            builder.append(benchMarker.getResult());
            methodDetail.getLogger().info(builder.toString());
        }
    }

}
