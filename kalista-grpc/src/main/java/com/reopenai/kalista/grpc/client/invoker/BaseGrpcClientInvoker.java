package com.reopenai.kalista.grpc.client.invoker;

import com.reopenai.kalista.grpc.serialization.RpcSerialization;

import java.lang.reflect.Type;

/**
 * Created by Allen Huang
 */
public abstract class BaseGrpcClientInvoker implements GrpcClientInvoker {

    protected byte[] serializerArguments(Object[] arguments) {
        Object params = arguments == null ? null : arguments.length == 0 ? null : arguments[0];
        return getRpcSerialization().serializer(params);
    }

    protected Object deserializerResult(byte[] data) {
        Type returnType = this.getMethodDetail().getReturnType();
        return getRpcSerialization().deserializer(data, returnType);
    }

    public abstract RpcSerialization getRpcSerialization();
}
