package com.reopenai.kalista.base.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * 定义的空常量
 *
 * @author Allen Huang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyConstants {

    /**
     * 空Object数组
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * 空字符串列表
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 空的字节数组
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";

}
