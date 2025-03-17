package com.reopenai.kalista.r2dbc.aop;

import com.reopenai.kalista.core.bench.BenchMarkerClassFilter;
import com.reopenai.kalista.r2dbc.repository.RepositoryExtension;
import org.springframework.stereotype.Repository;

/**
 * Created by Allen Huang
 */
public class OrmBenchMarkerClassFilter extends BenchMarkerClassFilter {

    @Override
    public boolean matches(Class<?> clazz) {
        return matchRepository(clazz) && super.matches(clazz);
    }

    public boolean matchRepository(Class<?> clazz) {
        return clazz.isAnnotationPresent(Repository.class) ||
                clazz == RepositoryExtension.class ||
                org.springframework.data.repository.Repository.class.isAssignableFrom(clazz);
    }
}
