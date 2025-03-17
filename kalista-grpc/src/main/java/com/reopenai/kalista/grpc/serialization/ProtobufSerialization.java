package com.reopenai.kalista.grpc.serialization;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by Allen Huang
 */
public class ProtobufSerialization extends BaseRpcSerialization {

    public static final String PROTOBUF = "protobuf";

    @Override
    protected byte[] doSerializer(Object data) {
        if (data == null) {
            return new byte[0];
        }

        // 生成随机数



        if (data instanceof MessageLite) {
            return ((MessageLite) data).toByteArray();
        }

        throw new IllegalArgumentException("Data must be a Protobuf Message type");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object doDeserializer(byte[] data, Type dataType) {
        if (data == null || data.length == 0) {
            return null;
        }

        if (!(dataType instanceof Class)) {
            throw new IllegalArgumentException("DataType must be a Class");
        }

        Class<?> clazz = (Class<?>) dataType;

        try {
            Method parseFromMethod = clazz.getMethod("parseFrom", byte[].class);
            return parseFromMethod.invoke(null, data);
        } catch (Exception e) {
            try {
                Method getParserMethod = clazz.getMethod("parser");
                Parser<Message> parser = (Parser<Message>) getParserMethod.invoke(null);
                return parser.parseFrom(data);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to deserialize Protobuf message", ex);
            }
        }
    }

    @Override
    public String supportType() {
        return PROTOBUF;
    }

}
