package com.reopenai.kalista.core.cache.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.reopenai.kalista.core.lang.bean.Tuple;
import com.reopenai.kalista.base.structure.lambda.XFunction;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Allen Huang
 */
@Slf4j
public class ReactiveLocalCache {

    @SuppressWarnings("rawtypes")
    private static final Map<String, ReactiveLocalCacheInstance> CACHE_INSTANCES = new HashMap<>();

    private static final Lock INSTANCE_LOCK = new ReentrantLock(false);

    //--------------------
    //   无参数
    //--------------------

    public static <V> XFunction.R0<Mono<V>> create(String cacheName, Supplier<Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Boolean, V> cache = ReactiveLocalCache.getOrCreate(cacheName, v -> func.get(), config);
        return () -> cache.get(Boolean.TRUE);
    }

    public static <K, V> XFunction.R1<K, Mono<V>> create(String cacheName, Function<K, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<K, V> cache = ReactiveLocalCache.getOrCreate(cacheName, func, config);
        return cache::get;
    }

    // 2个参数
    public static <K1, K2, V> XFunction.R2<K1, K2, Mono<V>> create(String cacheName, XFunction.R2<K1, K2, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V2<K1, K2>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V2.withCall(func), config);
        return (v1, v2) -> cache.get(new Tuple.V2<>(v1, v2));
    }

    // 3个参数
    public static <K1, K2, K3, V> XFunction.R3<K1, K2, K3, Mono<V>> create(String cacheName, XFunction.R3<K1, K2, K3, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V3<K1, K2, K3>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V3.withCall(func), config);
        return (v1, v2, v3) -> cache.get(new Tuple.V3<>(v1, v2, v3));
    }

    // 4个参数
    public static <K1, K2, K3, K4, V> XFunction.R4<K1, K2, K3, K4, Mono<V>> create(String cacheName, XFunction.R4<K1, K2, K3, K4, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V4<K1, K2, K3, K4>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V4.withCall(func), config);
        return (v1, v2, v3, v4) -> cache.get(new Tuple.V4<>(v1, v2, v3, v4));
    }

    // 5个参数
    public static <K1, K2, K3, K4, K5, V> XFunction.R5<K1, K2, K3, K4, K5, Mono<V>> create(String cacheName, XFunction.R5<K1, K2, K3, K4, K5, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V5<K1, K2, K3, K4, K5>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V5.withCall(func), config);
        return (v1, v2, v3, v4, v5) -> cache.get(new Tuple.V5<>(v1, v2, v3, v4, v5));
    }

    // 6个参数
    public static <K1, K2, K3, K4, K5, K6, V> XFunction.R6<K1, K2, K3, K4, K5, K6, Mono<V>> create(String cacheName, XFunction.R6<K1, K2, K3, K4, K5, K6, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V6<K1, K2, K3, K4, K5, K6>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V6.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6) -> cache.get(new Tuple.V6<>(v1, v2, v3, v4, v5, v6));
    }

    // 7个参数
    public static <K1, K2, K3, K4, K5, K6, K7, V> XFunction.R7<K1, K2, K3, K4, K5, K6, K7, Mono<V>> create(String cacheName, XFunction.R7<K1, K2, K3, K4, K5, K6, K7, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V7<K1, K2, K3, K4, K5, K6, K7>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V7.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7) -> cache.get(new Tuple.V7<>(v1, v2, v3, v4, v5, v6, v7));
    }

    // 8个参数
    public static <K1, K2, K3, K4, K5, K6, K7, K8, V> XFunction.R8<K1, K2, K3, K4, K5, K6, K7, K8, Mono<V>> create(String cacheName, XFunction.R8<K1, K2, K3, K4, K5, K6, K7, K8, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V8<K1, K2, K3, K4, K5, K6, K7, K8>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V8.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7, v8) -> cache.get(new Tuple.V8<>(v1, v2, v3, v4, v5, v6, v7, v8));
    }

    // 9个参数
    public static <K1, K2, K3, K4, K5, K6, K7, K8, K9, V> XFunction.R9<K1, K2, K3, K4, K5, K6, K7, K8, K9, Mono<V>> create(String cacheName, XFunction.R9<K1, K2, K3, K4, K5, K6, K7, K8, K9, Mono<V>> func, CacheConfig<V> config) {
        ReactiveLocalCacheInstance<Tuple.V9<K1, K2, K3, K4, K5, K6, K7, K8, K9>, V> cache = ReactiveLocalCache.getOrCreate(cacheName, Tuple.V9.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7, v8, v9) -> cache.get(new Tuple.V9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9));
    }

    private static <K, V> ReactiveLocalCacheInstance<K, V> getOrCreate(String cacheName, Function<? super K, Mono<V>> mapper, CacheConfig<V> config) {
        try {
            INSTANCE_LOCK.lock();
            if (CACHE_INSTANCES.containsKey(cacheName)) {
                throw new IllegalStateException("LocalCache name " + cacheName + " is already registered.");
            }
            ReactiveLocalCacheInstance<K, V> instance = new ReactiveLocalCacheInstance<>(cacheName, mapper, config);
            CACHE_INSTANCES.put(cacheName, instance);
            return instance;
        } finally {
            INSTANCE_LOCK.unlock();
        }
    }

    //--------------------
    //   缓存实现类
    //--------------------

    static class ReactiveLocalCacheInstance<K, V> {

        private static final int concurrency = Runtime.getRuntime().availableProcessors();

        private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(concurrency);

        private final Cache<K, V> cache;

        private final Function<? super K, Mono<V>> mapper;

        private ReactiveLocalCacheInstance(String cacheName, Function<? super K, Mono<V>> mapper, CacheConfig<V> config) {
            Caffeine<Object, Object> builder = Caffeine.newBuilder();
            builder.recordStats()
                    .expireAfterWrite(config.expire())
                    .maximumSize(config.maximumSize())
                    .initialCapacity(config.initialCapacity())
                    .scheduler(Scheduler.systemScheduler());
            if (config.removalListener() != null) {
                builder.<K, V>removalListener((k, v, status) -> config.removalListener().accept(k, v));
            }
            if (config.evictionListener() != null) {
                builder.<K, V>evictionListener((k, v, status) -> config.evictionListener().accept(k, v));
            }
            this.mapper = mapper;
            this.cache = builder.build();
            new CaffeineCacheMetrics<>(this.cache, cacheName, Tags.of("type", "ReactiveLocalCache"))
                    .bindTo(Metrics.globalRegistry);

            if ((config.autoRefresh()) != null) {
                SCHEDULER.scheduleWithFixedDelay(() -> {
                    Flux.fromIterable(cache.asMap().keySet())
                            .flatMap(this::refreshLoad, concurrency)
                            .doOnNext(tuple -> {
                                V value = tuple.v2();
                                if (value != null) {
                                    cache.put(tuple.v1(), value);
                                }
                            })
                            .subscribe();
                }, Duration.ofMinutes(1).toMillis(), config.autoRefresh().toMillis(), TimeUnit.MILLISECONDS);
            }
        }

        private Mono<Tuple.V2<K, V>> refreshLoad(K key) {
            return mapper.apply(key)
                    .map(v -> new Tuple.V2<>(key, v))
                    .onErrorResume(e -> {
                        log.error("[ReactiveLocalCache]自动刷新缓存时加载数据出错:", e);
                        return Mono.just(new Tuple.V2<>(key, null));
                    });
        }

        public Mono<V> get(K key) {
            V value = cache.getIfPresent(key);
            if (value != null) {
                return Mono.just(value);
            }
            return this.mapper.apply(key)
                    .doOnNext(v -> cache.put(key, v));
        }

        public V put(K key, V value) {
            if (value != null) {
                cache.put(key, value);
            }
            return value;
        }

    }

}
