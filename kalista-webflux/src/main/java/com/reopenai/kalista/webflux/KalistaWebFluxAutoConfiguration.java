package com.reopenai.kalista.webflux;

import com.reopenai.kalista.webflux.handler.GlobalWebErrorHandler;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * Created by Allen Huang
 */
@ComponentScan
@AutoConfiguration
public class KalistaWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnExpression("#{environment['management.metrics.tags.application'] == null}")
    public MeterFilter commonTagsMeterFilter(Environment environment) {
        String appName = environment.resolveRequiredPlaceholders("${spring.application.name:unknown}");
        return MeterFilter.commonTags(List.of(Tag.of("application", appName)));
    }

    @Bean
    public GlobalWebErrorHandler globalWebErrorHandler() {
        return new GlobalWebErrorHandler();
    }

}
