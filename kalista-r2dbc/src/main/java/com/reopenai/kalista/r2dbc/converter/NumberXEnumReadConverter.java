package com.reopenai.kalista.r2dbc.converter;

import com.reopenai.kalista.base.enums.XEnum;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Arrays;
import java.util.Set;

@ReadingConverter
public class NumberXEnumReadConverter<E extends Number, T extends XEnum<E>> implements GenericConverter {

    private final Class<T> targetType;

    private final T[] values;

    public NumberXEnumReadConverter(Class<T> targetType) {
        this.targetType = targetType;
        this.values = targetType.getEnumConstants();
        Arrays.sort(values,
                (v1, v2) -> {
                    int i = v1.getValue().intValue();
                    int j = v2.getValue().intValue();
                    return Integer.compare(i, j);
                });
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Number number = (Number) source;
        int value = number.intValue();
        for (T targetValue : values) {
            if (value == targetValue.getValue().intValue()) {
                return targetValue;
            }
        }
        return null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(Number.class, targetType));
    }

}

