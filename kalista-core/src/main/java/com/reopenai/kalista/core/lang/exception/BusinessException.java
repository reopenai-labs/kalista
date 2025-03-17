package com.reopenai.kalista.core.lang.exception;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.i18n.I18nUtil;
import lombok.Getter;

import java.util.Locale;

/**
 * 通用的异常，所有的扩展异常可继承此异常
 *
 * @author Allen Huang
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 异常代码
     */
    private final ErrorCode errorCode;
    /**
     * 异常参数，此参数可用于构建国际化的异常信息
     */
    private final Object[] args;
    /**
     * 响应时的错误结果
     */
    private Object data;

    public BusinessException(ErrorCode errorCode, Object... args) {
        this(Locale.SIMPLIFIED_CHINESE, errorCode, args);
    }

    public BusinessException(Locale locale, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    public BusinessException(Throwable cause, ErrorCode errorCode, Object... args) {
        this(Locale.SIMPLIFIED_CHINESE, cause, errorCode, args);
    }

    public BusinessException(Locale locale, Throwable cause, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.errorCode = ErrorCode.temporary(code);
        this.args = new Object[0];
    }

    public BusinessException(Throwable throwable, String code, String message) {
        super(message, throwable);
        this.errorCode = ErrorCode.temporary(code);
        this.args = new Object[0];
    }

    public BusinessException(Throwable throwable, String code, String message, Object[] args) {
        super(message, throwable);
        this.errorCode = ErrorCode.temporary(code);
        this.args = args;
    }

    /**
     * 错误时要返回的结果信息
     *
     * @param data 结果内容
     * @return 异常引用
     */
    public BusinessException data(Object data) {
        this.data = data;
        return this;
    }


}
