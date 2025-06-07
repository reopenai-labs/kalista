package com.reopenai.kalista.grpc.common.exception;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.core.lang.exception.SystemException;

/**
 * Created by Allen Huang
 */
public class GrpcException extends SystemException {

    public GrpcException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

}
