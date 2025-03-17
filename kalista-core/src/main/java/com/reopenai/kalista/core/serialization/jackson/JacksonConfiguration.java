package com.reopenai.kalista.core.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reopenai.kalista.base.enums.Language;
import com.reopenai.kalista.core.serialization.jackson.deserializer.LanguageDeserializer;
import com.reopenai.kalista.core.serialization.jackson.deserializer.LocalDateDeserializer;
import com.reopenai.kalista.core.serialization.jackson.deserializer.LocalDateTimeDeserializer;
import com.reopenai.kalista.core.serialization.jackson.deserializer.LocalTimeDeserializer;
import com.reopenai.kalista.core.serialization.jackson.serializer.*;
import com.reopenai.kalista.core.spring.ApplicationUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Jackson自动配置类
 *
 * @author Allen Huang
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonConfiguration implements CommandLineRunner {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // 序列化配置
                .serializerByType(Number.class, new NumberSerializer())
                .serializerByType(Language.class, new LanguageSerializer())
                .serializerByType(LocalTime.class, new LocalTimeSerializer())
                .serializerByType(LocalDate.class, new LocalDateSerializer())
                .serializerByType(BigDecimal.class, new BigDecimalSerializer())
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                // 反序列化配置
                .deserializerByType(Language.class, new LanguageDeserializer())
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer())
                .deserializerByType(LocalDate.class, new LocalDateDeserializer())
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
    }

    @Override
    public void run(String... args) throws Exception {
        JsonUtil.objectMapper = ApplicationUtil.getBean(ObjectMapper.class);
    }

}
