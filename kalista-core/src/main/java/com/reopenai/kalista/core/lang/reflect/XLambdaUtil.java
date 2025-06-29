package com.reopenai.kalista.core.lang.reflect;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.base.structure.lambda.MethodReference;
import com.reopenai.kalista.base.structure.lambda.XConsumer;
import com.reopenai.kalista.base.structure.lambda.XFunction;
import com.reopenai.kalista.base.structure.lambda.XSupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda工具类，能够通过Lambda表达式完成一些任务。
 *
 * @author Allen Huang
 */
@Slf4j
public final class XLambdaUtil {

    private static final Map<Class<?>, SerializedLambda> SERIALIZED_LAMBDA_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据Lambda表达式获取属性名称，如果属性的名称是驼峰命名法，则会将此名称转换成下划线形式。
     * 该方法为属性的getter或setter方法，且遵守Java Bean规范。示例如下:
     * <pre>{@code
     *   XLambdaUtil.propertyUnderlineCase(Demo::getUsername)
     * }</pre>
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 属性的名称
     */
    public static <T> String propertyUnderlineCase(XFunction<T, ?> serializable) {
        return StrUtil.toUnderlineCase(property(serializable));
    }

    /**
     * 根据Lambda表达式获取属性名称，该方法为属性的getter或setter方法，且遵守Java Bean规范。示例如下:
     * <pre>{@code
     *   XLambdaUtil.property(Demo::getUsername)
     * }</pre>
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 属性的名称
     */
    public static <T> String property(XFunction<T, ?> serializable) {
        return parseMethodName(serializable);
    }

    /**
     * 根据Lambda表达式获取属性名称，该方法为属性的getter或setter方法，且遵守Java Bean规范。示例如下:
     * <pre>{@code
     *   XLambdaUtil.property(Demo::getUsername)
     * }</pre>
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 属性的名称
     */
    public static <T> String property(XSupplier<T> serializable) {
        return parseMethodName(serializable);
    }


    /**
     * 获取此Lambda表达式的方法名称
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 方法名称
     */
    public static <T> String getMethodName(XFunction<T, ?> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return lambda.getImplMethodName();
    }

    /**
     * 获取此Lambda表达式的方法名称
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 方法名称
     */
    public static <T> String getMethodName(XConsumer<T> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return lambda.getImplMethodName();
    }

    public static <T> Method parseMethod(XFunction<T, ?> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <T> Method parseMethod(XSupplier<T> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <T> Method parseMethod(XConsumer<T> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE> Method parseMethod(MethodReference.R0<RESULT, TYPE> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE, PARAM> Method parseMethod(MethodReference.R1<RESULT, TYPE, PARAM> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2> Method parseMethod(MethodReference.R2<RESULT, TYPE, PARAM1, PARAM2> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3> Method parseMethod(MethodReference.R3<RESULT, TYPE, PARAM1, PARAM2, PARAM3> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4> Method parseMethod(MethodReference.R4<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    public static <RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> Method parseMethod(MethodReference.R5<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return parseMethod(lambda);
    }

    private static Method parseMethod(SerializedLambda lambda) {
        try {
            String className = lambda.getImplClass().replace('/', '.');
            String methodName = lambda.getImplMethodName();
            ClassLoader classLoader = ClassLoaderUtil.getClassLoader();
            Class<?> implClass = ClassUtils.forName(className, classLoader);
            String methodSignature = lambda.getImplMethodSignature();
            MethodType methodType = MethodType.fromMethodDescriptorString(methodSignature, implClass.getClassLoader());
            Class<?>[] parameterTypes = methodType.parameterArray();
            return implClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取此Lambda表达式的方法名称
     *
     * @param serializable 支持序列化的Lambda表达式
     * @return 方法名称
     */
    public static <T> String getMethodName(XSupplier<T> serializable) {
        SerializedLambda lambda = resolve(serializable);
        return lambda.getImplMethodName();
    }

    private static String parseMethodName(Serializable serializable) {
        SerializedLambda lambda = resolve(serializable);
        return XBeanUtil.methodToProperty(lambda.getImplMethodName());
    }


    private static <T> SerializedLambda resolve(Serializable serializable) {
        return SERIALIZED_LAMBDA_CACHE.computeIfAbsent(serializable.getClass(), clazz -> {
            try {
                Method method = clazz.getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                return (SerializedLambda) method.invoke(serializable);
            } catch (Exception e) {
                log.error("Failed to resolve lambda.", e);
                throw new IllegalArgumentException("Failed to resolve lambda", e);
            }
        });
    }

}
