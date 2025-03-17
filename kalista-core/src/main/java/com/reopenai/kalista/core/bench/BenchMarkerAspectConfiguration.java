package com.reopenai.kalista.core.bench;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * Created by Allen Huang
 */
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BenchMarkerAspectConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MonoBenchMarkerAdvisor monoBenchMakerAspect() {
        return new MonoBenchMarkerAdvisor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public FluxBenchMarkerAdvisor fluxBenchMakerAspect() {
        return new FluxBenchMarkerAdvisor();
    }


}
