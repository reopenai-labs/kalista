package com.reopenai.kalista.core.lang.reflect;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 方法描述信息
 *
 * @author Allen Huang
 */
@Data
public class MethodDescription {
    /**
     * 方法所属的类
     */
    private Class<?> targetClass;
    /**
     * 方法的参数列表
     */
    private Object[] args;
    /**
     * 方法的名称
     */
    private String methodName;
    /**
     * 方法实例
     */
    private Method method;
    /**
     * 目标对象的引用
     */
    private Object target;
}
