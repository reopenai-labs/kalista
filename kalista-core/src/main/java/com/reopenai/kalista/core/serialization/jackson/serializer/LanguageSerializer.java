package com.reopenai.kalista.core.serialization.jackson.serializer;

import com.reopenai.kalista.base.enums.Language;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Language序列化器
 *
 * @author Allen Huang
 */
public class LanguageSerializer extends JsonSerializer<Language> {

    @Override
    public void serialize(Language value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(value.getDisplayCode());
    }

}
