package com.reopenai.kalista.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 运算符
 *
 * @author Allen Huang
 */
@Getter
@RequiredArgsConstructor
public enum OperatorType implements XEnum<String> {
    /**
     * and运算符
     */
    AND("&&"),
    /**
     * or运算符
     */
    OR("||"),
    /**
     * or运算符
     */
    NOT("!=");

    private final String value;

}
