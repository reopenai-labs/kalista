package com.reopenai.kalista.core.aop;

import org.reactivestreams.Publisher;
import org.springframework.aop.MethodMatcher;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class ReactiveMethodMatcher implements MethodMatcher {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Class<?> returnType = method.getReturnType();
        return Publisher.class.isAssignableFrom(returnType);
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