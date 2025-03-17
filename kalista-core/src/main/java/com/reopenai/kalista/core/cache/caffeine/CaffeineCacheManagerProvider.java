package com.reopenai.kalista.core.cache.caffeine;

import cn.hutool.core.collection.CollUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.reopenai.kalista.core.cache.CacheManagerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen Huang
 */
@Component
@RequiredArgsConstructor
@ConditionalOnClass({CaffeineCacheManager.class, Caffeine.class})
public class CaffeineCacheManagerProvider implements CacheManagerProvider {

    private final CacheProperties cacheProperties;

    private final CacheManagerCustomizers customizers;

    private final ObjectProvider<Caffeine<Object, Object>> caffeine;

    private final ObjectProvider<CaffeineSpec> caffeineSpec;

    private final ObjectProvider<CacheLoader<Object, Object>> cacheLoader;

    @Override
    public CacheManager getCacheManager() {
        CaffeineCacheManager cacheManager = createCacheManager(cacheProperties, caffeine, caffeineSpec, cacheLoader);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (CollUtil.isNotEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        } else {
            cacheManager.setCacheNames(new ArrayList<>(0));
        }
        return customizers.customize(cacheManager);
    }


    private CaffeineCacheManager createCacheManager(CacheProperties cacheProperties,
                                                    ObjectProvider<Caffeine<Object, Object>> caffeine, ObjectProvider<CaffeineSpec> caffeineSpec,
                                                    ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(), caffeine.getIfAvailable(), cacheManager);
        cacheLoader.ifAvailable(cacheManager::setCacheLoader);
        return cacheManager;
    }

    private void setCacheBuilder(CacheProperties cacheProperties, CaffeineSpec caffeineSpec,
                                 Caffeine<Object, Object> caffeine, CaffeineCacheManager cacheManager) {
        String specification = cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (caffeineSpec != null) {
            cacheManager.setCaffeineSpec(caffeineSpec);
        } else if (caffeine != null) {
            cacheManager.setCaffeine(caffeine);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
