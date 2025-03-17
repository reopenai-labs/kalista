package com.reopenai.kalista.core.cache.local;

import java.time.Duration;
import java.util.function.BiConsumer;

/**
 * Created by Allen Huang
 */
public record CacheConfig<V>(
        int initialCapacity,
        long maximumSize,
        Duration expire,
        Duration autoRefresh,
        BiConsumer<Object, V> evictionListener,
        BiConsumer<Object, V> removalListener
) {

    public CacheConfig() {
        this(64, 256);
    }

    public CacheConfig(int initialCapacity, long maximumSize) {
        this(initialCapacity, maximumSize, Duration.ofMinutes(10));
    }

    public CacheConfig(int initialCapacity, long maximumSize, Duration expire) {
        this(initialCapacity, maximumSize, expire, null);
    }

    public CacheConfig(int initialCapacity, long maximumSize, Duration expire, Duration autoRefresh) {
        this(initialCapacity, maximumSize, expire, autoRefresh, null, null);
    }

}
