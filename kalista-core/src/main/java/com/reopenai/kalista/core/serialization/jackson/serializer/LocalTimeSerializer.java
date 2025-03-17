package com.reopenai.kalista.core.serialization.jackson.serializer;

import com.reopenai.kalista.base.constants.JavaTimeConstants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

/**
 * 将时间序列化成HH:mm:ss格式
 *
 * @author Allen Huang
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    @Override
    public void serialize(LocalTime localTime, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(localTime.format(JavaTimeConstants.TIME_FORMAT));
    }

}
