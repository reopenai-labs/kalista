package com.reopenai.kalista.core.cache.local;

import com.reopenai.kalista.core.lang.bean.Tuple;
import com.reopenai.kalista.core.lang.lambda.XConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Allen Huang
 */
class CacheAdapter {

    //---------------------
    //    监听器的适配器
    //---------------------

    public static <V> BiConsumer<Boolean, V> listenerAdapter(Consumer<V> consumer) {
        return consumer == null ? null : (k, v) -> consumer.accept(v);
    }

    public static <K1, K2, V> BiConsumer<Tuple.V2<K1, K2>, V> listenerAdapter(XConsumer.R3<K1, K2, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), v);
    }

    public static <K1, K2, K3, V> BiConsumer<Tuple.V3<K1, K2, K3>, V> listenerAdapter(XConsumer.R4<K1, K2, K3, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), v);
    }

    public static <K1, K2, K3, K4, V> BiConsumer<Tuple.V4<K1, K2, K3, K4>, V> listenerAdapter(XConsumer.R5<K1, K2, K3, K4, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), v);
    }

    public static <K1, K2, K3, K4, K5, V> BiConsumer<Tuple.V5<K1, K2, K3, K4, K5>, V> listenerAdapter(XConsumer.R6<K1, K2, K3, K4, K5, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), v);
    }

    public static <K1, K2, K3, K4, K5, K6, V> BiConsumer<Tuple.V6<K1, K2, K3, K4, K5, K6>, V> listenerAdapter(XConsumer.R7<K1, K2, K3, K4, K5, K6, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), v);
    }

    public static <K1, K2, K3, K4, K5, K6, K7, V> BiConsumer<Tuple.V7<K1, K2, K3, K4, K5, K6, K7>, V> listenerAdapter(XConsumer.R8<K1, K2, K3, K4, K5, K6, K7, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), v);
    }

    public static <K1, K2, K3, K4, K5, K6, K7, K8, V> BiConsumer<Tuple.V8<K1, K2, K3, K4, K5, K6, K7, K8>, V> listenerAdapter(XConsumer.R9<K1, K2, K3, K4, K5, K6, K7, K8, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), tuple.v8(), v);
    }

    public static <K1, K2, K3, K4, K5, K6, K7, K8, K9, V> BiConsumer<Tuple.V9<K1, K2, K3, K4, K5, K6, K7, K8, K9>, V> listenerAdapter(XConsumer.R10<K1, K2, K3, K4, K5, K6, K7, K8, K9, V> func) {
        if (func == null) {
            return null;
        }
        return (tuple, v) -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), tuple.v8(), tuple.v9(), v);
    }

}
