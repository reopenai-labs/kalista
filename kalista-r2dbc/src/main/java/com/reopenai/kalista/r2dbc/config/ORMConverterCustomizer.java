package com.reopenai.kalista.r2dbc.config;

import java.util.List;

/**
 * Created by Allen Huang
 */
public interface ORMConverterCustomizer {

    List<Object> customize(List<Object> converters);

}
