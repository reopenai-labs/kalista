package com.reopenai.kalista.r2dbc.converter.postgres;

import com.reopenai.kalista.r2dbc.builtin.JsonField;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Created by Allen Huang
 */
@ReadingConverter
public class PGJsonFieldReadConverter implements Converter<Json, JsonField> {

    @Override
    public JsonField convert(Json source) {
        return new JsonField(source.asString());
    }

}
