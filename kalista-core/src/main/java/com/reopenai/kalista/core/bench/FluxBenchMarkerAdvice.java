package com.reopenai.kalista.core.bench;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;

/**
 * Created by Allen Huang
 */
public class FluxBenchMarkerAdvice implements MethodInterceptor, Ordered {

    public static final FluxBenchMarkerAdvice INSTANCE = new FluxBenchMarkerAdvice();

    @Override
    public Flux<?> invoke(MethodInvocation invocation) throws Throwable {
        Flux<?> proceed = (Flux<?>) invocation.proceed();
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
