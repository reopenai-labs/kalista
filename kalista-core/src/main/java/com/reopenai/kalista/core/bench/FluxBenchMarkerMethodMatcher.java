package com.reopenai.kalista.core.bench;

import com.reopenai.kalista.core.aop.FluxMethodMatcher;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class FluxBenchMarkerMethodMatcher extends FluxMethodMatcher {

    public static final FluxBenchMarkerMethodMatcher INSTANCE = new FluxBenchMarkerMethodMatcher();

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return super.matches(method, targetClass)
                && !method.isAnnotationPresent(BenchMakerIgnore.class);
    }

}