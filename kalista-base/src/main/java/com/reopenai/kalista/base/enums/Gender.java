package com.reopenai.kalista.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别枚举
 *
 * @author Allen Huang
 */
@Getter
@RequiredArgsConstructor
public enum Gender implements XEnum<Integer> {

    /**
     * 男性
     */
    MALE(1),

    /**
     * 女性
     */
    FEMALE(2),

    /**
     * 未知性别
     */
    UNKNOWN(0);

    private final Integer value;

}
