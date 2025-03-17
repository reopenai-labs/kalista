package com.reopenai.kalista.core.serialization.jackson.deserializer;

import com.reopenai.kalista.base.constants.JavaTimeConstants;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalTime;

/**
 * 反序列化LocalTime对象.从HH:mm:ss格式反序列化成LocalTime
 *
 * @author Allen Huang
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JacksonException {
        String localDate = jsonParser.getValueAsString();
        return StringUtils.hasText(localDate) ? LocalTime.parse(localDate, JavaTimeConstants.TIME_FORMAT) : null;
    }

}
