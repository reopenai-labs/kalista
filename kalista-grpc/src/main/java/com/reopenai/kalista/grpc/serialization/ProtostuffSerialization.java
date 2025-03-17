package com.reopenai.kalista.grpc.serialization;

import com.reopenai.kalista.core.serialization.protostuff.ProtostuffUtil;

import java.lang.reflect.Type;

/**
 * Created by Allen Huang
 */
public class ProtostuffSerialization extends BaseRpcSerialization {

    @Override
    protected byte[] doSerializer(Object data) {
        return ProtostuffUtil.serialize(data);
    }

    @Override
    protected Object doDeserializer(byte[] data, Type dataType) {
        return ProtostuffUtil.deserialize(data, (Class<?>) dataType);
    }

    @Override
    public String supportType() {
        return "protostuff";
    }

}
