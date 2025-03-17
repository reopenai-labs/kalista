package com.reopenai.kalista.core.i18n;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.base.constants.EmptyConstants;
import com.reopenai.kalista.base.constants.JavaTimeConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 国际化配置
 *
 * @author Allen Huang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nUtil {

    private static final NumberFormat NUMBER_FORMAT;

    static {
        NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.CHINA);
        NUMBER_FORMAT.setMaximumIntegerDigits(32);
        NUMBER_FORMAT.setMaximumFractionDigits(32);
        NUMBER_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    static MessageSource messageSource;

    /**
     * 根据异常代码和格式化参数，解析出该代码对应的本地语言国际化信息.
     * 异常代码是全局唯一的，每一个异常代码都对应一个国际化代码。
     *
     * @param errorCode 异常代码
     * @return 解析后的国际化信息
     * @see ErrorCode
     */
    public static String parseLocaleMessage(ErrorCode errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, errorCode.getValue());
    }


    /**
     * 根据异常代码和格式化参数，解析出该代码对应的本地语言国际化信息.
     * 异常代码是全局唯一的，每一个异常代码都对应一个国际化代码。
     *
     * @param errorCode 异常代码
     * @return 解析后的国际化信息
     * @see ErrorCode
     */
    public static String parseLocaleMessage(Locale locale, ErrorCode errorCode) {
        return parseLocaleMessage(locale, errorCode.getValue());
    }

    /**
     * 根据国际化代码解析出该代码对应的本地语言国际化信息.
     *
     * @param code 国际化代码
     * @return 解析后的国际化信息
     */
    public static String parseLocaleMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, code, EmptyConstants.EMPTY_OBJECT_ARRAY);
    }

    /**
     * 根据国际化代码解析出该代码对应的本地语言国际化信息.
     *
     * @param code 国际化代码
     * @return 解析后的国际化信息
     */
    public static String parseLocaleMessage(Locale locale, String code) {
        return parseLocaleMessage(locale, code, EmptyConstants.EMPTY_OBJECT_ARRAY);
    }

    /**
     * 根据异常代码和格式化参数，解析出该国际化代码对应的本地语言国际化信息.
     * 异常代码是全局唯一的，每一个异常代码都对应一个国际化代码。
     *
     * @param errorCode 异常代码
     * @param args      国际化信息的格式化参数
     * @return 解析后的国际化信息
     * @see ErrorCode
     */
    public static String parseLocaleMessage(ErrorCode errorCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, errorCode.getValue(), args);
    }

    /**
     * 根据异常代码和格式化参数，解析出该国际化代码对应的本地语言国际化信息.
     * 异常代码是全局唯一的，每一个异常代码都对应一个国际化代码。
     *
     * @param errorCode 异常代码
     * @param args      国际化信息的格式化参数
     * @return 解析后的国际化信息
     * @see ErrorCode
     */
    public static String parseLocaleMessage(Locale locale, ErrorCode errorCode, Object... args) {
        return parseLocaleMessage(locale, errorCode.getValue(), args);
    }

    /**
     * 根据国际化代码和格式化参数，解析出该国际化代码对应的本地语言国际化信息.
     *
     * @param code 国际化代码
     * @param args 国际化信息的格式化参数
     * @return 解析后的国际化信息
     */
    public static String parseLocaleMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, code, args);
    }

    /**
     * 根据国际化代码和格式化参数，解析出该国际化代码对应的本地语言国际化信息.
     *
     * @param code 国际化代码
     * @param args 国际化信息的格式化参数
     * @return 解析后的国际化信息
     */
    public static String parseLocaleMessage(Locale locale, String code, Object... args) {
        try {
            Object[] vars = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                // 将BigDecimal转换成toPlainString()
                if (arg instanceof BigDecimal numberValue) {
                    arg = numberValue.stripTrailingZeros().toPlainString();
                } else if (arg instanceof LocalDateTime time) {
                    arg = time.format(JavaTimeConstants.DATE_TIME_FORMATTER);
                } else if (arg instanceof Number number) {
                    arg = NUMBER_FORMAT.format(number);
                }
                vars[i] = arg;
            }
            return messageSource.getMessage(code, vars, locale);
        } catch (Exception e) {
            return code;
        }
    }

}
