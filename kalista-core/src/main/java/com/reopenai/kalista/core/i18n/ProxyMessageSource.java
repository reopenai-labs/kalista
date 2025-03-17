package com.reopenai.kalista.core.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * @author Allen Huang
 */
@RequiredArgsConstructor
public class ProxyMessageSource implements MessageSource {

    private final MessageSource parent;

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return parent.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return parent.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return parent.getMessage(resolvable, locale);
    }

}
