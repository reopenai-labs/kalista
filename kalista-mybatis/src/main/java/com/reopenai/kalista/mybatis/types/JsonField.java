package com.reopenai.kalista.mybatis.types;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.reopenai.kalista.core.lang.reflect.TypeReference;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Json字符串
 *
 * @author Allen Huang
 */
@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = JsonField.JsonFieldDeserializer.class)
@JsonSerialize(using = JsonField.JsonFieldSerializer.class)
public class JsonField {

    private final String json;

    public JsonField(Object bean) {
        if (bean instanceof String) {
            this.json = ((String) bean);
        } else {
            this.json = JsonUtil.toJSONString(bean);
        }
    }

    /**
     * 将Json对象转换成Java对象
     *
     * @param type 目标类型
     * @return 转换后的实例
     */
    @SuppressWarnings("unchecked")
    public <T> T parseObject(Class<T> type) {
        if (type == String.class) {
            return (T) json;
        }
        return JsonUtil.parseObject(json, type);
    }

    /**
     * 将Json对象转换成Java对象
     *
     * @param type 目标类型
     * @return 转换后的实例
     */
    @SuppressWarnings("unchecked")
    public <T> T parseObject(Type type) {
        if (type == String.class) {
            return (T) json;
        }
        return JsonUtil.parseObject(json, type);
    }

    /**
     * 将Json对象转换成Java对象
     *
     * @param type 目标类型
     * @return 转换后的实例
     */
    public <T> T parseObject(TypeReference<T> type) {
        return JsonUtil.parseObject(json, type);
    }

    /**
     * 将JsonString转换成Map实例
     *
     * @return 转换后的Map实例
     */
    public Map<String, Object> toMap() {
        return JsonUtil.parseMap(json);
    }

    /**
     * JsonString的反序列化器
     */
    public static class JsonStringDeserializer extends JsonDeserializer<JsonField> {

        @Override
        public JsonField deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonToken jsonToken = parser.currentToken();
            String value;
            if (JsonToken.VALUE_STRING == jsonToken) {
                value = parser.getValueAsString();
            } else {
                TreeNode treeNode = parser.readValueAsTree();
                value = treeNode.toString();
            }

            return value != null && !value.isEmpty() ? new JsonField(value) : null;
        }

    }

    public static class JsonFieldDeserializer extends JsonDeserializer<JsonField> {
        @Override
        public JsonField deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
            String text = parser.getValueAsString();
            if (StrUtil.isNotEmpty(text)) {
                return new JsonField(text);
            }
            return null;
        }
    }

    /**
     * JsonString的Jackson序列化器
     */
    public static class JsonFieldSerializer extends JsonSerializer<JsonField> {

        @Override
        public void serialize(JsonField value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.toMap());
        }

    }

}
