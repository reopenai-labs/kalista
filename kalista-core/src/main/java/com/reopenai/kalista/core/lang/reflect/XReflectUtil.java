package com.reopenai.kalista.core.lang.reflect;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

import java.util.Locale;

/**
 * 反射工具类。
 * 如果有反射工具类的需求，请优先使用{@link ReflectUtil}
 *
 * @author Allen Huang
 */
@Slf4j
public final class XReflectUtil {

    /**
     * 获取某个对象的Class。如果这个对象是代理对象，则会获取这个对象真正的Class
     *
     * @param bean 需要获取Class的实例
     * @return Class实例
     */
    public static Class<?> getClass(Object bean) {
        return AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
    }

    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
