package com.reopenai.kalista.core.bench;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class MonoBenchMarkerAdvice implements MethodInterceptor, Ordered {

    public static final MonoBenchMarkerAdvice INSTANCE = new MonoBenchMarkerAdvice();

    @Override
    public Mono<?> invoke(MethodInvocation invocation) throws Throwable {
        Mono<?> proceed = (Mono<?>) invocation.proceed();
        Method method = invocation.getMethod();
        String flag = BenchMarker.parseMethodFlag(method);
        return proceed.transformDeferredContextual((origin, ctx)
                -> BenchMarker.markWithContext(origin, ctx, flag));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
