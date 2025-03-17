package com.reopenai.kalista.grpc.common;

import com.reopenai.kalista.core.bench.BenchMarker;
import com.reopenai.kalista.grpc.client.annotation.GrpcStub;
import io.grpc.MethodDescriptor;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static com.reopenai.kalista.grpc.common.GrpcDefinitionUtil.buildMethodDescriptor;
import static com.reopenai.kalista.grpc.common.GrpcDefinitionUtil.parseServiceName;

/**
 * Created by Allen Huang
 */
@Getter
@Builder(builderClassName = "Builder")
@RequiredArgsConstructor
public final class GrpcMethodDetail {

    private final Method method;

    private final String methodName;

    private final Parameter parameter;

    private final boolean validParameter;

    private final Class<?> parameterClass;

    private final Type returnType;

    private final boolean returnVoid;

    private final Class<?> returnClass;

    private final Logger logger;

    private final String serviceName;

    private final String endpoint;

    private final String benchFlag;

    private final String protocol;

    private final MethodDescriptor<byte[], byte[]> methodDescriptor;

    public GrpcMethodDetail(Environment environment, Method method) {
        this(environment, method.getDeclaringClass(), method);
    }

    public GrpcMethodDetail(Environment environment, Class<?> targetType, Method method) {
        this(method, buildMethodDescriptor(parseServiceName(targetType, environment), method),
                environment.resolvePlaceholders(targetType.getAnnotation(GrpcStub.class).protocol()));
    }

    public GrpcMethodDetail(Method method, MethodDescriptor<byte[], byte[]> methodDescriptor, String protocol) {
        this.method = method;
        this.methodName = method.getName();
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            this.parameter = parameters[0];
            this.parameterClass = this.parameter.getType();
            this.validParameter = this.parameter.isAnnotationPresent(Valid.class);
        } else {
            this.parameter = null;
            this.parameterClass = null;
            this.validParameter = false;
        }
        this.protocol = protocol;
        this.returnClass = method.getReturnType();
        this.returnType = method.getGenericReturnType();
        this.returnVoid = this.returnClass.equals(void.class) || this.returnClass.equals(Void.class);
        this.logger = LoggerFactory.getLogger(method.getDeclaringClass());
        this.benchFlag = BenchMarker.parseMethodFlag(method);
        this.serviceName = method.getDeclaringClass().getName();
        this.endpoint = String.join("#", methodDescriptor.getServiceName(), method.getName());
        this.methodDescriptor = methodDescriptor;
    }

    public boolean hasParameter() {
        return parameter != null;
    }

    public static GrpcMethodDetail.Builder from(GrpcMethodDetail methodDetail) {
        return new Builder()
                .method(methodDetail.method)
                .methodName(methodDetail.methodName)
                .parameter(methodDetail.parameter)
                .validParameter(methodDetail.validParameter)
                .parameterClass(methodDetail.parameterClass)
                .returnType(methodDetail.returnType)
                .returnVoid(methodDetail.returnVoid)
                .returnClass(methodDetail.returnClass)
                .logger(methodDetail.logger)
                .serviceName(methodDetail.serviceName)
                .endpoint(methodDetail.endpoint)
                .benchFlag(methodDetail.benchFlag)
                .protocol(methodDetail.protocol)
                .methodDescriptor(methodDetail.methodDescriptor);
    }

}
