package com.reopenai.kalista.core.bench;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * Created by Allen Huang
 */
public class MonoBenchMarkerAdvisor extends DefaultPointcutAdvisor {

    public MonoBenchMarkerAdvisor() {
        this(MonoBenchMarkerMethodMatcher.INSTANCE);
    }

    public MonoBenchMarkerAdvisor(ClassFilter classFilter) {
        this(classFilter, MonoBenchMarkerMethodMatcher.INSTANCE);
    }

    public MonoBenchMarkerAdvisor(MethodMatcher methodMatcher) {
        this(BenchMarkerClassFilter.INSTANCE, methodMatcher);
    }

    public MonoBenchMarkerAdvisor(ClassFilter classFilter, MethodMatcher methodMatcher) {
        super(new ComposablePointcut(classFilter, methodMatcher), MonoBenchMarkerAdvice.INSTANCE);
    }

}
