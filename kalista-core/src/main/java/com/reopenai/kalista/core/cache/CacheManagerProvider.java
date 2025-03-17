package com.reopenai.kalista.core.cache;

import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;

/**
 * @author Allen Huang
 */
public interface CacheManagerProvider extends Ordered {

    CacheManager getCacheManager();

}
