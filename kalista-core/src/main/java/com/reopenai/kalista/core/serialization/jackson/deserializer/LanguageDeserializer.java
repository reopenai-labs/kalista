package com.reopenai.kalista.core.serialization.jackson.deserializer;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.base.enums.Language;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Language的反序列化实例
 *
 * @author Allen Huang
 */
@Slf4j
public class LanguageDeserializer extends JsonDeserializer<Language> {

    @Override
    public Language deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String languageStr = parser.getValueAsString();
        return StrUtil.isNotBlank(languageStr) ? Language.fromCode(languageStr) : null;
    }


}
