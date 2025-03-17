package com.reopenai.kalista.base.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Allen Huang
 */
@Getter
@JsonSerialize(keyUsing = Language.LanguageKeySerializer.class)
public enum Language implements XEnum<Integer> {
    /**
     * 简体中文
     */
    SIMPLE_CHINESE(1, "zh-CN", "zh-CN", "zh"),
    /**
     * 繁体中文（台湾、香港）
     */
    TRADITIONAL_CHINESE(2, "zh-TW", "zh-TW", "zh-HK"),
    /**
     * 英语（包含美式、英式及其他变体）
     */
    ENGLISH(3, "en", "en", "en-US", "en-GB", "en-AU", "en-CA", "en-NZ", "en-IN", "en-ZA", "en-IE", "en-SG"),
    /**
     * 西班牙语（包含多种地区变体）
     */
    SPANISH(4, "es", "es", "es-ES", "es-MX", "es-AR", "es-CO", "es-CL", "es-PE", "es-VE"),
    /**
     * 法语
     */
    FRENCH(5, "fr", "fr", "fr-FR", "fr-CA", "fr-BE", "fr-CH"),
    /**
     * 德语
     */
    GERMAN(6, "de", "de", "de-DE", "de-AT", "de-CH"),
    /**
     * 日语
     */
    JAPANESE(7, "ja", "ja", "ja-JP"),
    /**
     * 朝鲜语（韩语）
     */
    KOREAN(8, "ko", "ko", "ko-KR"),
    /**
     * 葡萄牙语（包括巴西、葡萄牙变体）
     */
    PORTUGUESE(9, "pt", "pt", "pt-PT", "pt-BR"),
    /**
     * 俄语
     */
    RUSSIAN(10, "ru", "ru", "ru-RU"),
    /**
     * 阿拉伯语
     */
    ARABIC(11, "ar", "ar", "ar-SA", "ar-EG", "ar-AE", "ar-IQ", "ar-MA"),
    /**
     * 印地语
     */
    HINDI(12, "hi", "hi", "hi-IN"),
    /**
     * 孟加拉语
     */
    BENGALI(13, "bn", "bn", "bn-BD", "bn-IN"),
    /**
     * 旁遮普语
     */
    PUNJABI(14, "pa", "pa", "pa-IN", "pa-PK"),
    /**
     * 土耳其语
     */
    TURKISH(15, "tr", "tr", "tr-TR"),
    /**
     * 意大利语
     */
    ITALIAN(16, "it", "it", "it-IT", "it-CH"),
    /**
     * 荷兰语
     */
    DUTCH(17, "nl", "nl", "nl-NL", "nl-BE"),
    /**
     * 越南语
     */
    VIETNAMESE(18, "vi", "vi", "vi-VN"),
    /**
     * 波兰语
     */
    POLISH(19, "pl", "pl", "pl-PL"),
    /**
     * 乌克兰语
     */
    UKRAINIAN(20, "uk", "uk", "uk-UA");

    private static final Map<Integer, Language> VALUE_MAP = new HashMap<>();
    private static final Map<String, Language> CODE_MAP = new HashMap<>();

    private static final Language DEFAULT_LANGUAGE = SIMPLE_CHINESE;

    static {
        for (Language lang : values()) {
            VALUE_MAP.put(lang.value, lang);
            for (String codeVariant : lang.codes) {
                CODE_MAP.put(codeVariant.toLowerCase(), lang);
            }
        }
    }

    private final Integer value;
    private final String displayCode;
    private final Set<String> codes;

    /**
     * @param value       语言编号
     * @param displayCode 主要展示语言代码
     * @param codes       语言代码集合
     */
    Language(int value, String displayCode, String... codes) {
        this.value = value;
        this.displayCode = displayCode;
        this.codes = Set.of(codes);
    }

    /**
     * 通过编号查找语言
     *
     * @param value 语言编号
     * @return 对应的 LanguageCode 或 null
     */
    public static Language fromValue(int value) {
        return VALUE_MAP.get(value);
    }

    /**
     * 通过代码查找语言（支持变体解析）
     *
     * @param code 语言代码
     * @return 对应的 LanguageCode 或 null
     */
    public static Language fromCode(String code) {
        if (code == null) return null;
        String normalized = code.toLowerCase();
        if (CODE_MAP.containsKey(normalized)) {
            return CODE_MAP.get(normalized);
        }
        String baseLang = normalized.split("-")[0];
        return CODE_MAP.getOrDefault(baseLang, null);
    }

    /**
     * 通过 Locale 转换成 LanguageCode 对象
     *
     * @param locale Java 的 Locale 对象
     * @return 对应的 LanguageCode 或 null
     */
    public static Language fromLocale(Locale locale) {
        if (locale == null) return null;
        // 先尝试完整的语言标签，如 zh-CN
        Language language = fromCode(locale.toLanguageTag());
        if (language != null) {
            return language;
        }
        // 若找不到，则尝试仅使用语言部分，如 zh
        language = fromCode(locale.getLanguage());
        if (language != null) {
            return language;
        }
        return DEFAULT_LANGUAGE;
    }

    static class LanguageKeySerializer extends JsonSerializer<Language> {
        @Override
        public void serialize(Language value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeFieldName(value.getDisplayCode());
        }
    }

}
