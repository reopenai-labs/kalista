package com.reopenai.kalista.core.lang.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen Huang
 */
public abstract class BaseJdkProxy implements InvocationHandler {

    private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
            | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

    protected static final Constructor<MethodHandles.Lookup> lookupConstructor;
    protected static final Method privateLookupInMethod;

    static {
        Method privateLookupIn;
        try {
            privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        } catch (NoSuchMethodException e) {
            privateLookupIn = null;
        }
        privateLookupInMethod = privateLookupIn;

        Constructor<MethodHandles.Lookup> lookup = null;
        if (privateLookupInMethod == null) {
            // JDK 1.8
            try {
                lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                lookup.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("无法绑定MethodHandles", e);
            } catch (Exception e) {
                lookup = null;
            }
        }
        lookupConstructor = lookup;
    }

    private final Map<Method, MethodInvoker> methodCache = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(getInstance(), args);
        }
        if (method.isDefault()) {
            MethodInvoker invoker = getDefaultInvoker(method);
            return invoker.invoke(proxy, method, args);
        }
        return doInvoke(proxy, method, args);
    }

    protected abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;

    protected abstract Object getInstance();

    protected MethodInvoker getDefaultInvoker(Method method) throws Throwable {
        MethodInvoker invoker = methodCache.get(method);
        if (invoker == null) {
            synchronized (methodCache) {
                invoker = methodCache.get(method);
                if (invoker == null) {
                    if (privateLookupInMethod == null) {
                        invoker = new MethodInvoker(getMethodHandleJava8(method));
                    } else {
                        invoker = new MethodInvoker(getMethodHandleJava9(method));
                    }
                }
                this.methodCache.put(method, invoker);
            }
        }
        return invoker;
    }

    private MethodHandle getMethodHandleJava9(Method method)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Class<?> declaringClass = method.getDeclaringClass();
        return ((MethodHandles.Lookup) privateLookupInMethod.invoke(null, declaringClass, MethodHandles.lookup())).findSpecial(
                declaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                declaringClass);
    }

    private MethodHandle getMethodHandleJava8(Method method)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final Class<?> declaringClass = method.getDeclaringClass();
        return lookupConstructor.newInstance(declaringClass, ALLOWED_MODES).unreflectSpecial(method, declaringClass);
    }


    protected static class MethodInvoker implements InvocationHandler {

        private final MethodHandle methodHandle;

        public MethodInvoker(MethodHandle methodHandle) {
            this.methodHandle = methodHandle;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return methodHandle.bindTo(proxy).invokeWithArguments(args);
        }

    }
}
