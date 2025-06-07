package com.reopenai.kalista.grpc.common.exception;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;

/**
 * Created by Allen Huang
 */
public class GrpcClientException extends BusinessException {

    public GrpcClientException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

}
