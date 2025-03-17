package com.reopenai.kalista.grpc.server.invoker;

import cn.hutool.core.lang.reflect.MethodHandleUtil;
import com.reopenai.kalista.base.constants.EmptyConstants;
import com.reopenai.kalista.core.bench.BenchMarker;
import com.reopenai.kalista.grpc.common.metadata.GrpcContextKey;
import com.reopenai.kalista.grpc.serialization.RpcSerialization;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Allen Huang
 */
public class ReactiveGrpcServerInvoker implements ServerCalls.UnaryMethod<byte[], byte[]> {

    private final String benchFlag;

    private final Logger logger;

    private final Type parameterType;

    private final MethodHandle methodHandle;

    private final RpcSerialization serialization;

    public ReactiveGrpcServerInvoker(Object bean, Method method, RpcSerialization serialization) {
        this.serialization = serialization;
        this.benchFlag = BenchMarker.parseMethodFlag(method);
        this.logger = LoggerFactory.getLogger(method.getDeclaringClass());
        this.methodHandle = findMethodHandle(method).bindTo(bean);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length == 0) {
            this.parameterType = null;
        } else {
            this.parameterType = genericParameterTypes[0];
        }
    }

    @Override
    public void invoke(byte[] bytes, StreamObserver<byte[]> streamObserver) {
        Map<Object, Object> context = buildContext();
        BenchMarker marker = new BenchMarker();
        context.put(BenchMarker.class, marker);
        StreamObserverAdapter adapter = new StreamObserverAdapter(streamObserver, marker, benchFlag, logger);
        invokeWithArguments(bytes)
                .map(serialization::serializer)
                .doOnError(adapter::onError)
                .doOnSubscribe(adapter::doOnSubscribe)
                .contextWrite(Context.of(context))
                .contextCapture()
                .subscribe(adapter::onNext, adapter::onError, adapter::onCompleted);
    }

    private Map<Object, Object> buildContext() {
        Map<Object, Object> context = new HashMap<>();
        for (GrpcContextKey key : GrpcContextKey.values()) {
            String value = key.getContextKey().get();
            if (value != null) {
                context.put(key.getAppKey(), key.getDecode().apply(value));
            }
        }
        return context;
    }

    @RequiredArgsConstructor
    private static class StreamObserverAdapter implements StreamObserver<byte[]> {

        private final StreamObserver<byte[]> delegate;

        private final BenchMarker marker;

        private final String benchFlag;

        private final Logger logger;

        private boolean published;

        @Override
        public void onNext(byte[] bytes) {
            delegate.onNext(bytes);
            published = true;
        }

        @Override
        public void onError(Throwable throwable) {
            delegate.onError(throwable);
        }

        @Override
        public void onCompleted() {
            if (!published) {
                delegate.onNext(EmptyConstants.EMPTY_BYTE_ARRAY);
            }
            delegate.onCompleted();
            marker.mark(benchFlag);
            logger.info("gRPC Call: {}", marker.getResult());
        }

        public void doOnSubscribe(Subscription subscription) {
            marker.mark(benchFlag);
        }

    }

    private Mono<Object> invokeWithArguments(byte[] bytes) {
        List<Object> arguments;
        if (parameterType == null) {
            arguments = Collections.emptyList();
        } else if (bytes.length == 0) {
            arguments = new ArrayList<>(1);
        } else {
            Object deserializer = serialization.deserializer(bytes, parameterType);
            arguments = List.of(deserializer);
        }
        try {
            return (Mono<Object>) this.methodHandle.invokeWithArguments(arguments);
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }

    private MethodHandle findMethodHandle(Method method) {
        MethodHandles.Lookup lookup = MethodHandleUtil.lookup(method.getDeclaringClass());
        try {
            return lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new BeanCreationException("Cannot created gRPC service Bean.", e);
        }
    }

}
