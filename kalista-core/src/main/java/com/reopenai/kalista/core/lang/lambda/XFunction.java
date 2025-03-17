package com.reopenai.kalista.core.lang.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 扩展了{@link Function}的能力，提供了序列化的支持
 *
 * @author Allen Huang
 * @see Function
 */
@FunctionalInterface
public interface XFunction<T, R> extends Function<T, R>, Serializable {

    @FunctionalInterface
    interface R0<RESULT> extends Serializable {
        RESULT call();
    }

    @FunctionalInterface
    interface R1<PARAM, RESULT> extends Serializable {
        RESULT call(PARAM arg1);
    }

    @FunctionalInterface
    interface R2<PARAM1, PARAM2, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2);
    }

    @FunctionalInterface
    interface R3<PARAM1, PARAM2, PARAM3, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3);
    }

    @FunctionalInterface
    interface R4<PARAM1, PARAM2, PARAM3, PARAM4, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4);
    }

    @FunctionalInterface
    interface R5<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5);
    }

    @FunctionalInterface
    interface R6<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6);
    }

    @FunctionalInterface
    interface R7<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7);
    }

    @FunctionalInterface
    interface R8<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8);
    }

    @FunctionalInterface
    interface R9<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9);
    }

    @FunctionalInterface
    interface R10<PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9, PARAM10, RESULT> extends Serializable {
        RESULT call(PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9, PARAM10 arg10);
    }

}