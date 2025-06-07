package com.reopenai.kalista.grpc.server.handler;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.core.lang.exception.SystemException;
import com.reopenai.kalista.grpc.common.metadata.GrpcMetaDataKey;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import org.springframework.core.Ordered;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

import java.util.StringJoiner;

/**
 * Created by Allen Huang
 */
public class DefaultGrpcExceptionHandler implements GrpcExceptionHandler, Ordered {

    @Override
    public StatusException handleException(Throwable exception) {
        Status status = Status.INTERNAL;
        Metadata trailers = new Metadata();
        if (exception instanceof SystemException err) {
            if (exception instanceof BusinessException) {
                status = Status.FAILED_PRECONDITION;
            }
            trailers.put(GrpcMetaDataKey.ERROR_CODE, err.getErrorCode().getValue());
            trailers.put(GrpcMetaDataKey.ERROR_MSG, err.getMessage());
            Object[] args = err.getArgs();
            if (args != null && args.length > 0) {
                StringJoiner joiner = new StringJoiner(",");
                for (Object arg : args) {
                    joiner.add(arg.toString());
                }
                trailers.put(GrpcMetaDataKey.ERROR_PARAMS, joiner.toString());
            }
        } else {
            trailers.put(GrpcMetaDataKey.ERROR_MSG, exception.getMessage());
            trailers.put(GrpcMetaDataKey.ERROR_CODE, ErrorCode.Builtin.RPC_SERVER_ERROR.getValue());
        }
        return new StatusException(status, trailers);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
