package com.reopenai.kalista.core.lang.lambda;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 具有序列化能力的XSupplier
 *
 * @author Allen Huang
 */
@FunctionalInterface
public interface XConsumer<T> extends Consumer<T>, Serializable {

    @FunctionalInterface
    interface R2<PARAM1, PARAM2> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2);
    }

    @FunctionalInterface
    interface R3<PARAM1, PARAM2, PARAM3> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3);
    }

    @FunctionalInterface
    interface R4<PARAM1, PARAM2, PARAM3, PARAM4> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4);
    }

    @FunctionalInterface
    interface R5<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5);
    }

    @FunctionalInterface
    interface R6<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6);
    }

    @FunctionalInterface
    interface R7<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7);
    }

    @FunctionalInterface
    interface R8<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8);
    }

    @FunctionalInterface
    interface R9<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9);
    }

    @FunctionalInterface
    interface R10<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9, PARAM10> extends Serializable {
        void call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9, PARAM10 arg10);
    }
}
