package com.reopenai.kalista.core.lang.exception;

import com.reopenai.kalista.base.ErrorCode;

import java.util.Locale;

/**
 * 通用的异常，所有的扩展异常可继承此异常
 *
 * @author Allen Huang
 */
public class BusinessException extends SystemException {

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public BusinessException(Locale locale, ErrorCode errorCode, Object... args) {
        super(locale, errorCode, args);
    }

    public BusinessException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public BusinessException(Locale locale, Throwable cause, ErrorCode errorCode, Object... args) {
        super(locale, cause, errorCode, args);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(Throwable throwable, String code, String message) {
        super(throwable, code, message);
    }

    public BusinessException(Throwable throwable, String code, String message, Object[] args) {
        super(throwable, code, message, args);
    }

}
