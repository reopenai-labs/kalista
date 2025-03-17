package com.reopenai.kalista.r2dbc.builtin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.reopenai.kalista.core.lang.reflect.TypeReference;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Allen Huang
 */
@Getter
@JsonSerialize(using = JsonField.JsonFieldSerializer.class)
public class JsonField {

    private final String payload;

    public JsonField(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("JsonField`s payload is required.");
        } else if (data instanceof String str) {
            payload = str;
        } else if (data instanceof byte[] buff) {
            payload = new String(buff, StandardCharsets.UTF_8);
        } else {
            payload = JsonUtil.toJSONString(data);
        }
    }

    public Map<String, Object> asMap() {
        return JsonUtil.parseMap(payload);
    }

    public <T> List<T> asList() {
        return JsonUtil.parseArray(payload);
    }

    public <T> Set<T> asSet() {
        return new HashSet<>(asList());
    }

    public <T> T asObject(Class<T> clazz) {
        return JsonUtil.parseObject(payload, clazz);
    }

    public <T> T asObject(TypeReference<T> typeReference) {
        return JsonUtil.parseObject(payload, typeReference);
    }

    public <T> T asObject(Type type) {
        return JsonUtil.parseObject(payload, type);
    }

    @Override
    public String toString() {
        return payload;
    }

    static class JsonFieldSerializer extends JsonSerializer<JsonField> {

        @Override
        public void serialize(JsonField jsonField, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeRawValue(jsonField.getPayload());
        }

    }

}
