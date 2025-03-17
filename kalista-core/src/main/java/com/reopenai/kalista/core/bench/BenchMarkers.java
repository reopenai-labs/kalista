package com.reopenai.kalista.core.bench;

import com.alibaba.ttl.TransmittableThreadLocal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.Supplier;

/**
 * Created by Allen Huang
 */
public final class BenchMarkers {

    private static final ThreadLocal<BenchMarker> CURRENT_MARKER = TransmittableThreadLocal.withInitial(BenchMarker::new);

    public static void mark(String flag) {
        CURRENT_MARKER.get().mark(flag);
    }

    public static BenchMarker current() {
        return CURRENT_MARKER.get();
    }

    public static void remove() {
        CURRENT_MARKER.remove();
    }

    public static <T> Flux<T> markWithContext(Flux<T> publisher, ContextView ctx, Supplier<String> flagSupplier) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            String flag = flagSupplier.get();
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Mono<T> markWithContext(Mono<T> publisher, ContextView ctx, Supplier<String> flagSupplier) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            String flag = flagSupplier.get();
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Flux<T> markWithContext(Flux<T> publisher, ContextView ctx, String flag) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    public static <T> Mono<T> markWithContext(Mono<T> publisher, ContextView ctx, String flag) {
        BenchMarker benchMarker = ctx.getOrDefault(BenchMarker.class, null);
        if (benchMarker != null) {
            return doWithContext(publisher, benchMarker, flag);
        }
        return publisher;
    }

    private static <T> Flux<T> doWithContext(Flux<T> publisher, BenchMarker benchMarker, String flag) {
        return publisher
                .doOnRequest(event -> benchMarker.mark(flag))
                .doOnTerminate(() -> benchMarker.mark(flag));
    }

    private static <T> Mono<T> doWithContext(Mono<T> publisher, BenchMarker benchMarker, String flag) {
        return publisher
                .doOnRequest(event -> benchMarker.mark(flag))
                .doOnTerminate(() -> benchMarker.mark(flag));
    }

}
