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
public class LocalCache {

    @SuppressWarnings("rawtypes")
    private static final Map<String, LocalCacheInstance> CACHE_INSTANCES = new HashMap<>();

    private static final Lock INSTANCE_LOCK = new ReentrantLock(false);

    //--------------------
    //   无参数
    //--------------------

    public static <V> XFunction.R0<V> create(String cacheName, Supplier<V> func, CacheConfig<V> config) {
        LocalCacheInstance<Boolean, V> cache = LocalCache.createCache(cacheName, v -> func.get(), config);
        return () -> cache.get(Boolean.TRUE);
    }

    public static <K, V> XFunction.R1<K, V> create(String cacheName, Function<K, V> func, CacheConfig<V> config) {
        LocalCacheInstance<K, V> cache = LocalCache.createCache(cacheName, func, config);
        return cache::get;
    }

    // 2个参数
    public static <K1, K2, V> XFunction.R2<K1, K2, V> create(String cacheName, XFunction.R2<K1, K2, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V2<K1, K2>, V> cache = LocalCache.createCache(cacheName, Tuple.V2.withCall(func), config);
        return (v1, v2) -> cache.get(new Tuple.V2<>(v1, v2));
    }

    // 3个参数
    public static <K1, K2, K3, V> XFunction.R3<K1, K2, K3, V> create(String cacheName, XFunction.R3<K1, K2, K3, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V3<K1, K2, K3>, V> cache = LocalCache.createCache(cacheName, Tuple.V3.withCall(func), config);
        return (v1, v2, v3) -> cache.get(new Tuple.V3<>(v1, v2, v3));
    }

    // 4个参数
    public static <K1, K2, K3, K4, V> XFunction.R4<K1, K2, K3, K4, V> create(String cacheName, XFunction.R4<K1, K2, K3, K4, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V4<K1, K2, K3, K4>, V> cache = LocalCache.createCache(cacheName, Tuple.V4.withCall(func), config);
        return (v1, v2, v3, v4) -> cache.get(new Tuple.V4<>(v1, v2, v3, v4));
    }

    // 5个参数
    public static <K1, K2, K3, K4, K5, V> XFunction.R5<K1, K2, K3, K4, K5, V> create(String cacheName, XFunction.R5<K1, K2, K3, K4, K5, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V5<K1, K2, K3, K4, K5>, V> cache = LocalCache.createCache(cacheName, Tuple.V5.withCall(func), config);
        return (v1, v2, v3, v4, v5) -> cache.get(new Tuple.V5<>(v1, v2, v3, v4, v5));
    }

    // 6个参数
    public static <K1, K2, K3, K4, K5, K6, V> XFunction.R6<K1, K2, K3, K4, K5, K6, V> create(String cacheName, XFunction.R6<K1, K2, K3, K4, K5, K6, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V6<K1, K2, K3, K4, K5, K6>, V> cache = LocalCache.createCache(cacheName, Tuple.V6.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6) -> cache.get(new Tuple.V6<>(v1, v2, v3, v4, v5, v6));
    }

    // 7个参数
    public static <K1, K2, K3, K4, K5, K6, K7, V> XFunction.R7<K1, K2, K3, K4, K5, K6, K7, V> create(String cacheName, XFunction.R7<K1, K2, K3, K4, K5, K6, K7, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V7<K1, K2, K3, K4, K5, K6, K7>, V> cache = LocalCache.createCache(cacheName, Tuple.V7.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7) -> cache.get(new Tuple.V7<>(v1, v2, v3, v4, v5, v6, v7));
    }

    // 8个参数
    public static <K1, K2, K3, K4, K5, K6, K7, K8, V> XFunction.R8<K1, K2, K3, K4, K5, K6, K7, K8, V> create(String cacheName, XFunction.R8<K1, K2, K3, K4, K5, K6, K7, K8, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V8<K1, K2, K3, K4, K5, K6, K7, K8>, V> cache = LocalCache.createCache(cacheName, Tuple.V8.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7, v8) -> cache.get(new Tuple.V8<>(v1, v2, v3, v4, v5, v6, v7, v8));
    }

    // 9个参数
    public static <K1, K2, K3, K4, K5, K6, K7, K8, K9, V> XFunction.R9<K1, K2, K3, K4, K5, K6, K7, K8, K9, V> create(String cacheName, XFunction.R9<K1, K2, K3, K4, K5, K6, K7, K8, K9, V> func, CacheConfig<V> config) {
        LocalCacheInstance<Tuple.V9<K1, K2, K3, K4, K5, K6, K7, K8, K9>, V> cache = LocalCache.createCache(cacheName, Tuple.V9.withCall(func), config);
        return (v1, v2, v3, v4, v5, v6, v7, v8, v9) -> cache.get(new Tuple.V9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9));
    }

    private static <K, V> LocalCacheInstance<K, V> createCache(String cacheName, Function<? super K, V> mapper, CacheConfig<V> config) {
        try {
            INSTANCE_LOCK.lock();
            if (CACHE_INSTANCES.containsKey(cacheName)) {
                throw new IllegalStateException("LocalCache name " + cacheName + " is already registered.");
            }
            LocalCacheInstance<K, V> instance = new LocalCacheInstance<>(cacheName, mapper, config);
            CACHE_INSTANCES.put(cacheName, instance);
            return instance;
        } finally {
            INSTANCE_LOCK.unlock();
        }
    }

    //--------------------
    //   缓存实现类
    //--------------------

    static class LocalCacheInstance<K, V> {

        private static final int concurrency = Runtime.getRuntime().availableProcessors();

        private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(concurrency * 2);

        private final Cache<K, V> cache;

        private final Function<? super K, V> mapper;

        private LocalCacheInstance(String cacheName, Function<? super K, V> mapper, CacheConfig<V> config) {
            Caffeine<Object, Object> builder = Caffeine.newBuilder();
            builder.expireAfterWrite(config.expire())
                    .recordStats()
                    .maximumSize(config.maximumSize())
                    .initialCapacity(config.initialCapacity())
                    .scheduler(Scheduler.systemScheduler());
            if (config.evictionListener() != null) {
                builder.<K, V>evictionListener((k, v, status) -> config.evictionListener().accept(k, v));
            }
            if (config.removalListener() != null) {
                builder.<K, V>removalListener((k, v, status) -> config.removalListener().accept(k, v));
            }
            this.mapper = mapper;
            this.cache = builder.build();
            new CaffeineCacheMetrics<>(this.cache, cacheName, Tags.of("type", "LocalCache"))
                    .bindTo(Metrics.globalRegistry);

            if (config.autoRefresh() != null) {
                SCHEDULER.scheduleWithFixedDelay(() -> {
                    Flux.fromIterable(this.cache.asMap().keySet())
                            .map(this::refreshLoad)
                            .doOnNext(tuple -> cache.put(tuple.v1(), tuple.v2()))
                            .subscribe();
                }, Duration.ofMinutes(1).toMillis(), config.autoRefresh().toMillis(), TimeUnit.MILLISECONDS);
            }
        }

        private Tuple.V2<K, V> refreshLoad(K key) {
            return new Tuple.V2<>(key, mapper.apply(key));
        }

        public V get(K key) {
            return cache.get(key, k -> mapper.apply(key));
        }

        public V put(K key, V value) {
            if (value != null) {
                cache.put(key, value);
            }
            return value;
        }

        public void remove(K key) {
            cache.invalidate(key);
        }

    }

}
