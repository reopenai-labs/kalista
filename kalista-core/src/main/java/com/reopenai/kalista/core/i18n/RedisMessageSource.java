package com.reopenai.kalista.core.i18n;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.base.enums.Language;
import com.reopenai.kalista.core.cache.local.CacheConfig;
import com.reopenai.kalista.core.cache.local.LocalCache;
import com.reopenai.kalista.base.structure.lambda.XFunction;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Locale;

/**
 * @author Allen Huang
 */
public class RedisMessageSource extends ResourceBundleMessageSource {

    private static final String CACHE_NAME = "system.i18n";

    private static final MessageFormat NULL_VALUE = new MessageFormat("");

    private final XFunction.R2<Language, String, MessageFormat> localCache;

    public RedisMessageSource(StringRedisTemplate redisTemplate) {
        this.localCache = LocalCache.create(CACHE_NAME, (Language language, String code) -> {
            String msgCode = String.format("SYSTEM:I18N:%s", language.getDisplayCode());
            String value = redisTemplate.<String, String>opsForHash().get(msgCode, code);
            if (StrUtil.isNotBlank(value)) {
                return new MessageFormat(value);
            }
            return NULL_VALUE;
        }, new CacheConfig<>(64, 512, Duration.ofMinutes(15), Duration.ofMinutes(10)));
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        Language language = Language.fromLocale(locale);
        MessageFormat messageFormat = localCache.call(language, code);
        if (messageFormat == NULL_VALUE) {
            Language subLanguage = Language.fromLocale(new Locale(locale.getLanguage()));
            if (subLanguage != language) {
                messageFormat = localCache.call(subLanguage, code);
                if (messageFormat != NULL_VALUE) {
                    return messageFormat;
                }
            }
            return null;
        }
        return messageFormat;
    }

    @Nullable
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        MessageFormat messageFormat = resolveCode(code, locale);
        if (messageFormat != null) {
            synchronized (messageFormat) {
                return messageFormat.format(new Object[0]);
            }
        }
        return null;
    }

}
