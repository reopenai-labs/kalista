package com.reopenai.kalista.grpc.common.marshaller;

import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于数组格式的序列化处理器
 *
 * @author Allen Huang
 */
public class ByteArrayMarshaller implements MethodDescriptor.Marshaller<byte[]> {

    public static final ByteArrayMarshaller INSTANCE = new ByteArrayMarshaller();

    @Override
    public InputStream stream(byte[] value) {
        return new ByteArrayInputStream(value);
    }

    @Override
    public byte[] parse(InputStream stream) {
        try {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
