package com.reopenai.kalista.grpc.common;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;

/**
 * Created by Allen Huang
 */
public class GrpcException extends BusinessException {

    public GrpcException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

}
