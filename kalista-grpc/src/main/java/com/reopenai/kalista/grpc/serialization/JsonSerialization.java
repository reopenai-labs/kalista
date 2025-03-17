package com.reopenai.kalista.grpc.serialization;

import com.reopenai.kalista.core.serialization.jackson.JsonUtil;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Created by Allen Huang
 */
public class JsonSerialization extends BaseRpcSerialization {

    @Override
    protected byte[] doSerializer(Object data) {
        return JsonUtil.toJSONString(data).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected Object doDeserializer(byte[] data, Type dataType) {
        String payload = new String(data, StandardCharsets.UTF_8);
        return JsonUtil.parseObject(payload, dataType);
    }

    @Override
    public String supportType() {
        return "json";
    }
}
