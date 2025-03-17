package com.reopenai.kalista.core.cache.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.reopenai.kalista.core.lang.lambda.MethodReference;
import com.reopenai.kalista.core.lang.lambda.XLambdaUtil;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

/**
 * @author Allen Huang
 */
public final class RedisSerializerUtil {

    public static <T> RedisSerializationContext.SerializationPair<T> JSON(ObjectMapper objectMapper, Class<T> type) {
        return fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, type));
    }

    public static <T> RedisSerializationContext.SerializationPair<T> JSON(ObjectMapper objectMapper, Method method) {
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <T> RedisSerializationContext.SerializationPair<T> JSON(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        return JSON(objectMapper, typeReference.getType());
    }

    public static <T> RedisSerializationContext.SerializationPair<T> JSON(ObjectMapper objectMapper, Type type) {
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        return fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, javaType));
    }

    public static <RESULT, TYPE> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R0<RESULT, TYPE> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <RESULT, TYPE, PARAM> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R1<RESULT, TYPE, PARAM> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R2<RESULT, TYPE, PARAM1, PARAM2> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R3<RESULT, TYPE, PARAM1, PARAM2, PARAM3> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R4<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> RedisSerializationContext.SerializationPair<RESULT> JSON(ObjectMapper objectMapper, MethodReference.R5<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> reference) {
        Method method = XLambdaUtil.parseMethod(reference);
        Type returnType = method.getGenericReturnType();
        return JSON(objectMapper, returnType);
    }

}
