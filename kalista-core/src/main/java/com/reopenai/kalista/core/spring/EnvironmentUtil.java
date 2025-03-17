package com.reopenai.kalista.core.spring;

import com.reopenai.kalista.core.cache.local.CacheConfig;
import com.reopenai.kalista.core.cache.local.LocalCache;
import com.reopenai.kalista.core.lang.lambda.XFunction;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.time.Duration;

/**
 * Created by Allen Huang
 */
public final class EnvironmentUtil {

    static Environment environment;

    private static final XFunction.R2<String, Class<?>, ?> readCache = LocalCache.create(
            "system.environment.binding",
            (key, target) -> Binder.get(environment).bind(key, target).get(),
            new CacheConfig<>(16, 64, Duration.ofMinutes(10), Duration.ofMinutes(8)));

    @SuppressWarnings("unchecked")
    public static <T> T readObject(String key, Class<T> target) {
        return (T) readCache.call(key, target);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static <T> T getProperty(String key, Class<T> target) {
        return environment.getProperty(key, target);
    }

}
