package com.reopenai.kalista.grpc.serialization;

import java.lang.reflect.Type;

/**
 * Created by Allen Huang
 */
public abstract class BaseRpcSerialization implements RpcSerialization {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public byte[] serializer(Object data) {
        if (data == null) {
            return EMPTY_BYTE_ARRAY;
        }
        return doSerializer(data);
    }

    protected abstract byte[] doSerializer(Object data);

    @Override
    public Object deserializer(byte[] data, Type dataType) {
        if (data == null || data.length == 0) {
            return null;
        }
        return doDeserializer(data, dataType);
    }

    protected abstract Object doDeserializer(byte[] data, Type dataType);

}
