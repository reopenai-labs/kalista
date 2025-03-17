package com.reopenai.kalista.webflux.advisor;

import com.reopenai.kalista.core.web.ApiResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Allen Huang
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class EmptyBodyAdvisor extends DefaultPointcutAdvisor {

    public EmptyBodyAdvisor() {
        super(new EmptyBodyPointcut(), new EmptyBodyBodyAdvice());
    }

    public static final class EmptyBodyBodyAdvice implements MethodInterceptor {

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Mono<?> invoke(MethodInvocation invocation) throws Throwable {
            Object proceed = invocation.proceed();
            if (proceed != null) {
                return ((Mono) proceed)
                        .defaultIfEmpty(ApiResponse.success());
            }
            return null;
        }

    }


    public static class EmptyBodyPointcut extends AnnotationMatchingPointcut {

        public EmptyBodyPointcut() {
            super(RestController.class);
        }

        @Override
        public @NonNull MethodMatcher getMethodMatcher() {
            MethodMatcher methodMatcher = super.getMethodMatcher();
            return new EmptyBodyMethodMatcher(methodMatcher);
        }

    }

    private record EmptyBodyMethodMatcher(MethodMatcher delegate) implements MethodMatcher {

        @Override
        public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
            boolean matches = delegate.matches(method, targetClass);
            if (matches) {
                Type returnType = method.getGenericReturnType();
                if (returnType instanceof ParameterizedType genericParameterTypes) {
                    Class<?> rawType = (Class<?>) genericParameterTypes.getRawType();
                    Type bodyType = genericParameterTypes.getActualTypeArguments()[0];
                    if (bodyType instanceof ParameterizedType bodyParameterizedType) {
                        return Mono.class.isAssignableFrom(rawType) && ApiResponse.class == bodyParameterizedType.getRawType();
                    }
                    return false;
                }
            }
            return false;
        }

        @Override
        public boolean isRuntime() {
            return delegate.isRuntime();
        }

        @Override
        public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass, @NonNull Object... args) {
            return delegate.matches(method, targetClass, args);
        }

    }

}
