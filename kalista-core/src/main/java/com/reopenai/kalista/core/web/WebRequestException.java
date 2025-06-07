package com.reopenai.kalista.core.web;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Locale;

/**
 * Created by Allen Huang
 */
@Getter
public class WebRequestException extends BusinessException {

    private final HttpStatus httpStatus;

    public WebRequestException(HttpStatus httpStatus, ErrorCode errorCode, Object... args) {
        super(errorCode, args);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, Locale locale, ErrorCode errorCode, Object... args) {
        super(locale, errorCode, args);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, Locale locale, Throwable cause, ErrorCode errorCode, Object... args) {
        super(locale, cause, errorCode, args);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, String code, String message) {
        super(code, message);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, Throwable throwable, String code, String message) {
        super(throwable, code, message);
        this.httpStatus = httpStatus;
    }

    public WebRequestException(HttpStatus httpStatus, Throwable throwable, String code, String message, Object[] args) {
        super(throwable, code, message, args);
        this.httpStatus = httpStatus;
    }

}
