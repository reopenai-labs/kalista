package com.reopenai.kalista.core.cache.redis;

import com.reopenai.kalista.core.cache.CacheManagerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

/**
 * @author Allen Huang
 */
@Component
@RequiredArgsConstructor
@ConditionalOnClass({RedisCacheManager.class, RedisTemplate.class})
public class RedisCacheManagerProvider implements CacheManagerProvider {

    private final CacheProperties cacheProperties;

    private final CacheManagerCustomizers cacheManagerCustomizers;

    private final ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration;

    private final ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers;

    private final RedisConnectionFactory redisConnectionFactory;

    private final ResourceLoader resourceLoader;

    @Override
    public RedisCacheManager getCacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
                .allowCreateOnMissingCache(false)
                .cacheDefaults(determineConfiguration(cacheProperties, redisCacheConfiguration, resourceLoader.getClassLoader()));
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }
        if (cacheProperties.getRedis().isEnableStatistics()) {
            builder.enableStatistics();
        }

        redisCacheManagerBuilderCustomizers.orderedStream()
                .forEach((customizer) -> customizer.customize(builder));
        return cacheManagerCustomizers.customize(builder.build());
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties properties, ObjectProvider<RedisCacheConfiguration> configurations, ClassLoader classLoader) {
        return configurations.getIfAvailable(() -> createConfiguration(properties, classLoader));
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, ClassLoader classLoader) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .disableKeyPrefix()
                .serializeKeysWith(RedisSerializationContext.string().getStringSerializationPair())
                .serializeValuesWith(fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
