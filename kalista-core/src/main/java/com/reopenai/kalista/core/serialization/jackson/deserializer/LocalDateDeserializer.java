package com.reopenai.kalista.core.serialization.jackson.deserializer;

import com.reopenai.kalista.base.constants.JavaTimeConstants;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

/**
 * LocalDateTime的反序列化实例
 *
 * @author Allen Huang
 */
@Slf4j
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String localDate = parser.getValueAsString();
        return StringUtils.hasText(localDate) ? LocalDate.parse(localDate, JavaTimeConstants.DATE_FORMAT) : null;
    }

}
