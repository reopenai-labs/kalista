package com.reopenai.kalista.r2dbc.converter;

import com.reopenai.kalista.base.enums.XEnum;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Set;

@WritingConverter
@SuppressWarnings("rawtypes")
public class NumberXEnumWriteConverter<E extends Number, T extends XEnum<E>> implements GenericConverter {

    private final Class<T> targetType;

    public NumberXEnumWriteConverter(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ((XEnum) source).getValue();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(targetType, Number.class));
    }

}

