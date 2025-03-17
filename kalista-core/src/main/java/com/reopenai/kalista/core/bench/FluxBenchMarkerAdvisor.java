package com.reopenai.kalista.core.bench;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * Created by Allen Huang
 */
public class FluxBenchMarkerAdvisor extends DefaultPointcutAdvisor {

    public FluxBenchMarkerAdvisor() {
        this(FluxBenchMarkerMethodMatcher.INSTANCE);
    }

    public FluxBenchMarkerAdvisor(ClassFilter classFilter) {
        this(classFilter, FluxBenchMarkerMethodMatcher.INSTANCE);
    }

    public FluxBenchMarkerAdvisor(MethodMatcher methodMatcher) {
        this(BenchMarkerClassFilter.INSTANCE, methodMatcher);
    }

    public FluxBenchMarkerAdvisor(ClassFilter classFilter, MethodMatcher methodMatcher) {
        super(new ComposablePointcut(classFilter, methodMatcher), FluxBenchMarkerAdvice.INSTANCE);
    }

}
