package com.reopenai.kalista.base.structure.lambda;

import java.io.Serializable;

/**
 * @author Allen Huang
 */

public interface MethodReference {

    @FunctionalInterface
    interface R0<RESULT, TYPE> extends Serializable {
        RESULT call(TYPE type) throws Exception;
    }

    @FunctionalInterface
    interface R1<RESULT, TYPE, PARAM> extends Serializable {
        RESULT call(TYPE type, PARAM arg1) throws Exception;
    }

    @FunctionalInterface
    interface R2<RESULT, TYPE, PARAM1, PARAM2> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2) throws Exception;
    }

    @FunctionalInterface
    interface R3<RESULT, TYPE, PARAM1, PARAM2, PARAM3> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3) throws Exception;
    }

    @FunctionalInterface
    interface R4<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4) throws Exception;
    }

    @FunctionalInterface
    interface R5<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5) throws Exception;
    }

    @FunctionalInterface
    interface R6<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6) throws Exception;
    }

    @FunctionalInterface
    interface R7<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7) throws Exception;
    }

    @FunctionalInterface
    interface R8<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8) throws Exception;
    }

    @FunctionalInterface
    interface R9<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9) throws Exception;
    }

    @FunctionalInterface
    interface R10<RESULT, TYPE, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9, PARAM10> extends Serializable {
        RESULT call(TYPE type, PARAM1 arg1, PARAM2 arg2, PARAM3 arg3, PARAM4 arg4, PARAM5 arg5, PARAM6 arg6, PARAM7 arg7, PARAM8 arg8, PARAM9 arg9, PARAM10 arg10) throws Exception;
    }
}
