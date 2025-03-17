package com.reopenai.kalista.core.lang.reflect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanCopy工具类
 *
 * @author Allen Huang
 */
public final class XBeanUtil {

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
    private static final Map<Class<?>, Map<Class<?>, Map<Boolean, Map<String, CustomBeanCopier>>>> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 将源对象中的属性复制给目标对象。能够被成功复制的属性的类型和名称必须一致。
     *
     * @param source       源对象
     * @param target       目标对象
     * @param ignoreFields 需要忽略的字段名
     */
    public static void copyProperties(Object source, Object target, String... ignoreFields) {
        copyProperties(source, target, null, ignoreFields);
    }

    /**
     * 使用转换器将源对象中的属性复制给目标对象。能够被成功复制的属性的类型和名称必须一致。
     *
     * @param source    源对象
     * @param target    目标对象
     * @param converter 转换器实例
     */
    public static void copyProperties(Object source, Object target, Converter converter, String... ignoreFields) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        //获取所有需要被忽略的字段
        Set<String> ignores = ignoreFields.length == 0 ? Collections.emptySet() : new TreeSet<>(Set.of(ignoreFields));
        getBeanCopier(sourceClass, targetClass, converter, ignores)
                .copy(source, target, converter);
    }

    public static CustomBeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass, Converter converter, Set<String> ignores) {
        //计算出需要忽略的字段
        StringJoiner joiner = new StringJoiner("&");
        for (String ignore : ignores) {
            joiner.add(ignore);
        }
        String key = joiner.toString();
        boolean hasConverter = converter != null;

        // 根据sourceClass获取第一层
        Map<Class<?>, Map<Boolean, Map<String, CustomBeanCopier>>> sourceMap = BEAN_COPIER_MAP
                .computeIfAbsent(sourceClass, k -> new ConcurrentHashMap<>(16));

        // 根据targetClass获取第二层
        Map<Boolean, Map<String, CustomBeanCopier>> targetMap = sourceMap
                .computeIfAbsent(targetClass, k -> buildTargetMap());

        // 根据hasConverter获取第三层
        Map<String, CustomBeanCopier> instantMap = targetMap.get(Boolean.valueOf(hasConverter)); // 优化拆箱带来的性能损失

        return instantMap.computeIfAbsent(key, k -> CustomBeanCopier.create(sourceClass, targetClass, hasConverter, ignores));
    }

    private static Map<Boolean, Map<String, CustomBeanCopier>> buildTargetMap() {
        Map<Boolean, Map<String, CustomBeanCopier>> map = new HashMap<>();
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static abstract class CustomBeanCopier {
        private static final BeanCopierKey KEY_FACTORY =
                (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
        private static final Type CONVERTER =
                TypeUtils.parseType("org.springframework.cglib.core.Converter");
        private static final Type BEAN_COPIER =
                TypeUtils.parseType(CustomBeanCopier.class.getName());
        private static final Signature COPY =
                new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
        private static final Signature CONVERT =
                TypeUtils.parseSignature("Object convert(Object, Class, Object)");

        interface BeanCopierKey {
            Object newInstance(String source, String target, boolean useConverter);
        }

        public static CustomBeanCopier create(Class source, Class target, boolean useConverter, Set<String> ignoreFields) {
            Generator gen = new Generator();
            gen.setSource(source);
            gen.setTarget(target);
            gen.setUseConverter(useConverter);
            gen.setIgnoreFields(ignoreFields);
            return gen.create();
        }

        abstract public void copy(Object from, Object to, Converter converter);

        public static class Generator extends AbstractClassGenerator {
            private static final Source SOURCE = new Source(CustomBeanCopier.class.getName());
            private Class source;
            private Class target;
            private boolean useConverter;

            private Set<String> ignoreFields;

            public Generator() {
                super(SOURCE);
            }

            public void setSource(Class source) {
                if (!Modifier.isPublic(source.getModifiers())) {
                    setNamePrefix(source.getName());
                }
                this.source = source;
            }

            public void setTarget(Class target) {
                if (!Modifier.isPublic(target.getModifiers())) {
                    setNamePrefix(target.getName());
                }
                this.target = target;
                // SPRING PATCH BEGIN
                setContextClass(target);
                // SPRING PATCH END
            }

            public void setIgnoreFields(Set<String> ignoreFields) {
                this.ignoreFields = ignoreFields;
            }

            public void setUseConverter(boolean useConverter) {
                this.useConverter = useConverter;
            }

            protected ClassLoader getDefaultClassLoader() {
                return source.getClassLoader();
            }

            protected ProtectionDomain getProtectionDomain() {
                return ReflectUtils.getProtectionDomain(source);
            }

            public CustomBeanCopier create() {
                Object key = KEY_FACTORY.newInstance(source.getName(), target.getName(), useConverter);
                return (CustomBeanCopier) super.create(key);
            }

            public void generateClass(ClassVisitor v) {
                Type sourceType = Type.getType(source);
                Type targetType = Type.getType(target);
                ClassEmitter ce = new ClassEmitter(v);
                ce.begin_class(Constants.V1_8,
                        Constants.ACC_PUBLIC,
                        getClassName(),
                        BEAN_COPIER,
                        null,
                        Constants.SOURCE_FILE);

                EmitUtils.null_constructor(ce);
                CodeEmitter e = ce.begin_method(Constants.ACC_PUBLIC, COPY, null);
                PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(source);
                PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(target);

                Map names = new HashMap();
                for (PropertyDescriptor propertyDescriptor : getters) {
                    names.put(propertyDescriptor.getName(), propertyDescriptor);
                }
                Local targetLocal = e.make_local();
                Local sourceLocal = e.make_local();
                if (useConverter) {
                    e.load_arg(1);
                    e.checkcast(targetType);
                    e.store_local(targetLocal);
                    e.load_arg(0);
                    e.checkcast(sourceType);
                    e.store_local(sourceLocal);
                } else {
                    e.load_arg(1);
                    e.checkcast(targetType);
                    e.load_arg(0);
                    e.checkcast(sourceType);
                }
                for (PropertyDescriptor setter : setters) {
                    // 过滤掉忽略的字段
                    if (ignoreFields.contains(setter.getName())) {
                        continue;
                    }
                    PropertyDescriptor getter = (PropertyDescriptor) names.get(setter.getName());
                    if (getter != null) {
                        MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                        MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                        if (parseSetterType(setter).equals(parseGetterType(getter))) {
                            if (useConverter) {
                                Type setterType = write.getSignature().getArgumentTypes()[0];
                                e.load_local(targetLocal);
                                e.load_arg(2);
                                e.load_local(sourceLocal);
                                e.invoke(read);
                                e.box(read.getSignature().getReturnType());
                                EmitUtils.load_class(e, setterType);
                                e.push(write.getSignature().getName());
                                e.invoke_interface(CONVERTER, CONVERT);
                                e.unbox_or_zero(setterType);
                                e.invoke(write);
                            } else if (compatible(getter, setter)) {
                                e.dup2();
                                e.invoke(read);
                                e.invoke(write);
                            }
                        }
                    }
                }
                e.return_value();
                e.end_method();
                ce.end_class();
            }

            private java.lang.reflect.Type parseSetterType(PropertyDescriptor setter) {
                return setter.getWriteMethod().getParameters()[0].getParameterizedType();
            }

            private java.lang.reflect.Type parseGetterType(PropertyDescriptor getter) {
                return getter.getReadMethod().getGenericReturnType();
            }

            private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
                // TODO: allow automatic widening conversions?
                return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
            }

            protected Object firstInstance(Class type) {
                return ReflectUtils.newInstance(type);
            }

            protected Object nextInstance(Object instance) {
                return instance;
            }
        }
    }


}
