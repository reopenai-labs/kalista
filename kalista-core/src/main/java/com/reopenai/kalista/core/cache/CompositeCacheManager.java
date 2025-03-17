package com.reopenai.kalista.core.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.lang.Nullable;

import java.util.*;


/**
 * @author Allen Huang
 */
public class CompositeCacheManager implements CacheManager, InitializingBean {

    private final List<CacheManager> cacheManagers = new ArrayList<>();

    public void setCacheManagers(Collection<CacheManager> cacheManagers) {
        this.cacheManagers.addAll(cacheManagers);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (CacheManager cacheManager : cacheManagers) {
            if (cacheManager instanceof InitializingBean initializingBean) {
                initializingBean.afterPropertiesSet();
            }
        }
        this.cacheManagers.add(new NoOpCacheManager());
    }


    @Override
    @Nullable
    public Cache getCache(String name) {
        for (CacheManager cacheManager : this.cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                return cache;
            }
        }
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> names = new LinkedHashSet<>();
        for (CacheManager manager : this.cacheManagers) {
            names.addAll(manager.getCacheNames());
        }
        return Collections.unmodifiableSet(names);
    }

}
