package com.reopenai.kalista.core.aop;

import org.springframework.aop.MethodMatcher;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class FluxMethodMatcher implements MethodMatcher {

    public static final FluxMethodMatcher INSTANCE = new FluxMethodMatcher();

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Class<?> returnType = method.getReturnType();
        return Flux.class.isAssignableFrom(returnType);
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        throw new UnsupportedOperationException();
    }

}