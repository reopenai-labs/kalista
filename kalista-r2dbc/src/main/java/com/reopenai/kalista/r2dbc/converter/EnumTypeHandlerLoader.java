package com.reopenai.kalista.r2dbc.converter;

import com.reopenai.kalista.base.enums.XEnum;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Allen Huang
 */
public class EnumTypeHandlerLoader {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void loadHandlers(String packageName, List<Object> converters) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = "classpath*:" + packageName.replace('.', '/') + "/**/*.class";
        Resource[] resources = resolver.getResources(packageSearchPath);

        Set<Class<?>> classes = new HashSet<>();
        for (Resource resource : resources) {
            MetadataReader metadataReader = new SimpleMetadataReaderFactory().getMetadataReader(resource);
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            String className = classMetadata.getClassName();
            Class<?> targetType = Class.forName(className);
            if (XEnum.class.isAssignableFrom(targetType)) {
                if (!classes.contains(targetType) && targetType.isEnum()) {
                    classes.add(targetType);
                    Type[] genericInterfaces = targetType.getGenericInterfaces();
                    for (Type genericInterface : genericInterfaces) {
                        ParameterizedType type = (ParameterizedType) genericInterface;
                        if (XEnum.class.isAssignableFrom((Class<?>) type.getRawType())) {
                            Type actualTypeArgument = type.getActualTypeArguments()[0];
                            if (Number.class.isAssignableFrom((Class<?>) actualTypeArgument)) {
                                converters.add(new NumberXEnumReadConverter(targetType));
                                converters.add(new NumberXEnumWriteConverter(targetType));
                            }
                        }
                    }
                }
            }
        }
    }

}
