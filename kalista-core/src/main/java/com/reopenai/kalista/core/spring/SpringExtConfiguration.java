package com.reopenai.kalista.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * Created by Allen Huang
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringExtConfiguration implements EnvironmentAware, ApplicationContextAware, ApplicationEventPublisherAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtil.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        ApplicationUtil.publisher = applicationEventPublisher;
    }

    @Override
    public void setEnvironment(Environment environment) {
        EnvironmentUtil.environment = environment;
    }

}
