package com.reopenai.kalista.core.aop;

import org.springframework.aop.MethodMatcher;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class MonoMethodMatcher implements MethodMatcher {

    public static final MonoMethodMatcher INSTANCE = new MonoMethodMatcher();

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Class<?> returnType = method.getReturnType();
        return Mono.class.isAssignableFrom(returnType);
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