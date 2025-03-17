package com.reopenai.kalista.core.reactive;

import org.reactivestreams.Publisher;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.BiFunction;

/**
 * Created by Allen Huang
 */
public final class ReactiveUtil {

    @SuppressWarnings("unchecked")
    public static <V, E, P extends CorePublisher<E>, R extends Publisher<V>> R transformDeferredContextual(P publisher, BiFunction<P, ContextView, R> transformer) {
        if (publisher instanceof Mono<?> mono) {
            return (R) mono.transformDeferredContextual((origin, ctx) -> transformer.apply((P) origin, ctx));
        } else if (publisher instanceof Flux<?> flux) {
            return (R) flux.transformDeferredContextual((origin, ctx) -> transformer.apply((P) origin, ctx));
        }
        return (R) publisher;
    }

}
