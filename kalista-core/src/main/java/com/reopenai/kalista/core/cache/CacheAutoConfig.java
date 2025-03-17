package com.reopenai.kalista.core.cache;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Allen Huang
 */
@EnableCaching
@Configuration
@ComponentScan
@ConditionalOnClass(CacheManager.class)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty(value = "spring.cache.type", matchIfMissing = true)
public class CacheAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().toList());
    }

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager compositeCacheManager(List<CacheManagerProvider> cacheManagers) {
        cacheManagers.sort(Comparator.comparingInt(CacheManagerProvider::getOrder));
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        if (CollUtil.isNotEmpty(cacheManagers)) {
            List<CacheManager> instances = new ArrayList<>(cacheManagers.size());
            for (CacheManagerProvider provider : cacheManagers) {
                CacheManager cacheManager = provider.getCacheManager();
                instances.add(cacheManager);
            }
            compositeCacheManager.setCacheManagers(instances);
        }
        return compositeCacheManager;
    }


}
