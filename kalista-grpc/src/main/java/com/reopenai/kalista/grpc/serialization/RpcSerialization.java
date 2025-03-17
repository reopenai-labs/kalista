package com.reopenai.kalista.grpc.serialization;

import org.springframework.core.Ordered;

import java.lang.reflect.Type;

/**
 * RPC序列化处理器
 * <p>
 * Created by Allen Huang
 */
public interface RpcSerialization extends Ordered {

    /**
     * 序列化参数
     *
     * @param data 序列化参数
     * @return 序列化之后的结果
     */
    byte[] serializer(Object data);

    /**
     * 反序列化参数
     *
     * @param data     待反序列化的结果
     * @param dataType 字段类型
     * @return 反序列化之后的结果
     */
    Object deserializer(byte[] data, Type dataType);

    /**
     * 支持的序列化协议类型
     *
     * @return 序列化协议
     */
    String supportType();

    @Override
    default int getOrder() {
        return 0;
    }

}
