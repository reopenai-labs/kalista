package com.reopenai.kalista.core.bench;

import org.springframework.aop.ClassFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by Allen Huang
 */
public class BenchMarkerClassFilter implements ClassFilter {

    public static final BenchMarkerClassFilter INSTANCE = new BenchMarkerClassFilter();

    @Override
    public boolean matches(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(BenchMakerIgnore.class)) {
            return clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Component.class);
        }
        return false;
    }

}