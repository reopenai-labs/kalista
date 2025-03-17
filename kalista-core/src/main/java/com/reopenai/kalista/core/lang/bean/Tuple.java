package com.reopenai.kalista.core.lang.bean;

import com.reopenai.kalista.core.lang.lambda.XFunction;

import java.util.function.Function;

/**
 * Created by Allen Huang
 */
public interface Tuple {

    record V2<V1, V2>(V1 v1, V2 v2) implements Tuple {
        public <R> R call(XFunction.R2<V1, V2, R> func) {
            return func.call(this.v1(), this.v2());
        }

        public static <K1, K2, V> Function<Tuple.V2<K1, K2>, V> withCall(XFunction.R2<K1, K2, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2());
        }
    }

    record V3<V1, V2, V3>(V1 v1, V2 v2, V3 v3) implements Tuple {
        public <R> R call(XFunction.R3<V1, V2, V3, R> func) {
            return func.call(this.v1(), this.v2(), this.v3());
        }

        public static <K1, K2, K3, V> Function<Tuple.V3<K1, K2, K3>, V> withCall(XFunction.R3<K1, K2, K3, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3());
        }
    }

    record V4<V1, V2, V3, V4>(V1 v1, V2 v2, V3 v3, V4 v4) implements Tuple {
        public <R> R call(XFunction.R4<V1, V2, V3, V4, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4());
        }

        public static <K1, K2, K3, K4, V> Function<Tuple.V4<K1, K2, K3, K4>, V> withCall(XFunction.R4<K1, K2, K3, K4, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4());
        }
    }

    record V5<V1, V2, V3, V4, V5>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) implements Tuple {
        public <R> R call(XFunction.R5<V1, V2, V3, V4, V5, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5());
        }

        public static <K1, K2, K3, K4, K5, V> Function<Tuple.V5<K1, K2, K3, K4, K5>, V> withCall(XFunction.R5<K1, K2, K3, K4, K5, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5());
        }
    }

    record V6<V1, V2, V3, V4, V5, V6>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) implements Tuple {
        public <R> R call(XFunction.R6<V1, V2, V3, V4, V5, V6, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5(), this.v6());
        }

        public static <K1, K2, K3, K4, K5, K6, V> Function<Tuple.V6<K1, K2, K3, K4, K5, K6>, V> withCall(XFunction.R6<K1, K2, K3, K4, K5, K6, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6());
        }
    }

    record V7<V1, V2, V3, V4, V5, V6, V7>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) implements Tuple {
        public <R> R call(XFunction.R7<V1, V2, V3, V4, V5, V6, V7, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5(), this.v6(), this.v7());
        }

        public static <K1, K2, K3, K4, K5, K6, K7, V> Function<Tuple.V7<K1, K2, K3, K4, K5, K6, K7>, V> withCall(XFunction.R7<K1, K2, K3, K4, K5, K6, K7, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7());
        }
    }

    record V8<V1, V2, V3, V4, V5, V6, V7, V8>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) implements Tuple {
        public <R> R call(XFunction.R8<V1, V2, V3, V4, V5, V6, V7, V8, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5(), this.v6(), this.v7(), this.v8());
        }

        public static <K1, K2, K3, K4, K5, K6, K7, K8, V> Function<Tuple.V8<K1, K2, K3, K4, K5, K6, K7, K8>, V> withCall(XFunction.R8<K1, K2, K3, K4, K5, K6, K7, K8, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), tuple.v8());
        }
    }

    record V9<V1, V2, V3, V4, V5, V6, V7, V8, V9>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) implements Tuple {
        public <R> R call(XFunction.R9<V1, V2, V3, V4, V5, V6, V7, V8, V9, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5(), this.v6(), this.v7(), this.v8(), this.v9());
        }

        public static <K1, K2, K3, K4, K5, K6, K7, K8, K9, V> Function<Tuple.V9<K1, K2, K3, K4, K5, K6, K7, K8, K9>, V> withCall(XFunction.R9<K1, K2, K3, K4, K5, K6, K7, K8, K9, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), tuple.v8(), tuple.v9());
        }
    }

    record V10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10>(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10) implements Tuple {
        public <R> R call(XFunction.R10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, R> func) {
            return func.call(this.v1(), this.v2(), this.v3(), this.v4(), this.v5(), this.v6(), this.v7(), this.v8(), this.v9(), this.v10());
        }

        public static <K1, K2, K3, K4, K5, K6, K7, K8, K9, K10, V> Function<Tuple.V10<K1, K2, K3, K4, K5, K6, K7, K8, K9, K10>, V> withCall(XFunction.R10<K1, K2, K3, K4, K5, K6, K7, K8, K9, K10, V> func) {
            return tuple -> func.call(tuple.v1(), tuple.v2(), tuple.v3(), tuple.v4(), tuple.v5(), tuple.v6(), tuple.v7(), tuple.v8(), tuple.v9(), tuple.v10());
        }
    }

}
