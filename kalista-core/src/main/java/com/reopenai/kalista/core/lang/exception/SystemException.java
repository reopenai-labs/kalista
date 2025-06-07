package com.reopenai.kalista.core.lang.exception;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.base.constants.EmptyConstants;
import com.reopenai.kalista.core.i18n.I18nUtil;
import lombok.Getter;

import java.util.Locale;

/**
 * 通用的异常，所有的扩展异常可继承此异常
 *
 * @author Allen Huang
 */
@Getter
public class SystemException extends RuntimeException {
    /**
     * 异常代码
     */
    protected final ErrorCode errorCode;
    /**
     * 异常参数，此参数可用于构建国际化的异常信息
     */
    protected final Object[] args;

    public SystemException(ErrorCode errorCode, Object... args) {
        this(Locale.getDefault(), errorCode, args);
    }

    public SystemException(Locale locale, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    public SystemException(Throwable cause, ErrorCode errorCode, Object... args) {
        this(Locale.getDefault(), cause, errorCode, args);
    }

    public SystemException(Locale locale, Throwable cause, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public SystemException(String code, String message) {
        super(message);
        this.errorCode = ErrorCode.temporary(code);
        this.args = EmptyConstants.EMPTY_OBJECT_ARRAY;
    }

    public SystemException(Throwable throwable, String code, String message) {
        super(message, throwable);
        this.errorCode = ErrorCode.temporary(code);
        this.args = EmptyConstants.EMPTY_OBJECT_ARRAY;
    }

    public SystemException(Throwable throwable, String code, String message, Object[] args) {
        super(message, throwable);
        this.errorCode = ErrorCode.temporary(code);
        this.args = args;
    }

}
