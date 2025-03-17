package com.reopenai.kalista.lock;

import com.reopenai.kalista.lock.advisor.DistributedLockAdvice;
import com.reopenai.kalista.lock.annotation.DistributedLock;
import com.reopenai.kalista.lock.api.DistributedLockFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;

/**
 * Created by Allen Huang
 */
@Configuration
public class DistributedLockConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultPointcutAdvisor distributedLockPointcutAdvisor(Environment environment, DistributedLockFactory lockFactory) {
        DistributedLockAdvice advice = new DistributedLockAdvice(lockFactory, environment);
        AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(DistributedLock.class);
        return new DefaultPointcutAdvisor(new ComposablePointcut(matcher), advice);
    }

}
