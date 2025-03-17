package com.reopenai.kalista.core;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.integration.Slf4jThreadLocalAccessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.ReactorProperties;
import org.springframework.context.i18n.LocaleContextThreadLocalAccessor;

/**
 * Created by Allen Huang
 */
@AutoConfiguration
public class KalistaCoreAutoConfiguration {

    public KalistaCoreAutoConfiguration(ReactorProperties properties) {
        if (properties.getContextPropagation() == ReactorProperties.ContextPropagationMode.AUTO) {
            ContextRegistry.getInstance()
                    .registerThreadLocalAccessor(new LocaleContextThreadLocalAccessor())
                    .registerThreadLocalAccessor(new Slf4jThreadLocalAccessor());
        }
    }

}
