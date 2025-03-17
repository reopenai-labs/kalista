package com.reopenai.kalista.core.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author Allen Huang
 */
@AutoConfiguration
@RequiredArgsConstructor
public class I18nConfiguration {

    @Bean("applicationMessageSource")
    @ConditionalOnClass(RedisTemplate.class)
    public RedisMessageSource applicationMessageSource(MessageSource messageSource, StringRedisTemplate redisTemplate) {
        RedisMessageSource redisMessageSource = new RedisMessageSource(redisTemplate);
        redisMessageSource.setParentMessageSource(messageSource);
        redisMessageSource.setUseCodeAsDefaultMessage(true);
        redisMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        I18nUtil.messageSource = redisMessageSource;
        return redisMessageSource;
    }

    @Bean("applicationMessageSource")
    @ConditionalOnMissingBean(name = "applicationMessageSource")
    public ProxyMessageSource proxyMessageSource(MessageSource messageSource) {
        return new ProxyMessageSource(messageSource);
    }


}
