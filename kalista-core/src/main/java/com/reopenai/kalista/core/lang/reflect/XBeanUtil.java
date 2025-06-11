package com.reopenai.kalista.core.lang.reflect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanCopy工具类
 *
 * @author Allen Huang
 */
public final class XBeanUtil {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";

    static {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target");
    }

    /**
     * 多层次结构缓存CustomBeanCopier。具体结构如下:
     * .
     * └── sourceClass
     * └── targetClass
     * └── hasConverter
     * └── key
     * └── CustomBeanCopier
     */
    private static final Map<Class<?>, Map<Class<?>, BeanCopier>> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 将源对象中的属性复制给目标对象。能够被成功复制的属性的类型和名称必须一致。
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null);
    }

    /**
     * 使用转换器将源对象中的属性复制给目标对象。能够被成功复制的属性的类型和名称必须一致。
     *
     * @param source    源对象
     * @param target    目标对象
     * @param converter 转换器实例
     */
    public static void copyProperties(Object source, Object target, Converter converter) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        getBeanCopier(sourceClass, targetClass, converter)
                .copy(source, target, converter);
    }

    public static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass, Converter converter) {
        boolean hasConverter = converter != null;

        // 根据sourceClass获取第一层
        Map<Class<?>, BeanCopier> sourceMap = BEAN_COPIER_MAP
                .computeIfAbsent(sourceClass, k -> new ConcurrentHashMap<>(16));
        return sourceMap.computeIfAbsent(targetClass, k -> BeanCopier.create(sourceClass, targetClass, hasConverter));
    }

    private static Map<Boolean, Map<String, BeanCopier>> buildTargetMap() {
        Map<Boolean, Map<String, BeanCopier>> map = new HashMap<>();
        map.put(Boolean.FALSE, new ConcurrentHashMap<>(4));
        map.put(Boolean.TRUE, new ConcurrentHashMap<>(0));
        return map;
    }

    /**
     * 将Map对象转换成Object对象
     *
     * @param source 源Map对象
     * @param target 要转换的目标类型
     * @return 转换后的结果
     */
    public static <T> T mapToBean(Map<String, Object> source, Class<T> target) {
        if (CollUtil.isNotEmpty(source)) {
            Map<String, Object> param = new HashMap<>(source.size());
            source.forEach((k, v) -> {
                if (v instanceof String) {
                    String sv = ((String) v);
                    // 如果是JSON字符串，则序列化成list,如果是JSON对象，则序列化成Map
                    if (sv.startsWith("[") && sv.endsWith("]")) {
                        param.put(k, JsonUtil.parseObject(sv, List.class));
                    } else if (sv.startsWith("{") && sv.endsWith("}")) {
                        param.put(k, JsonUtil.parseObject(sv, Map.class));
                    } else {
                        param.put(k, v);
                    }
                } else {
                    param.put(k, v);
                }
            });
            return JsonUtil.parseObject(param, target);
        }
        return ReflectUtil.newInstance(target);
    }


    /**
     * 将Map对象扁平化
     *
     * @param source map源
     * @return 扁平化后的结果
     */
    public static Map<String, Object> flattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, source, null);
        return result;
    }

    private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
        source.forEach((key, value) -> {
            if (StringUtils.hasText(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) value;
                if (collection.isEmpty()) {
                    result.put(key, "");
                } else {
                    int count = 0;
                    for (Object object : collection) {
                        buildFlattenedMap(result, Collections.singletonMap(
                                "[" + (count++) + "]", object), key);
                    }
                }
            } else {
                if (value != null) {
                    Map<String, Object> v = BeanUtil.beanToMap(value);
                    if (CollUtil.isNotEmpty(v)) {
                        buildFlattenedMap(result, v, key);
                    } else {
                        result.put(key, value.toString());
                    }
                } else {
                    result.put(key, null);
                }
            }
        });
    }

    /**
     * 将getter、setter名称转换成属性名称
     *
     * @param name getter/setter名称
     * @return 属性名称
     */
    public static String methodToProperty(String name) {
        if (name.startsWith(IS)) {
            name = name.substring(2);
        } else if (name.startsWith(GET) || name.startsWith(SET)) {
            name = name.substring(3);
        } else {
            return null;
        }
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

}
