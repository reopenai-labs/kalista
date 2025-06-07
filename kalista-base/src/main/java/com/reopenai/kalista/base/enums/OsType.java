package com.reopenai.kalista.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 终端设备的类型
 *
 * @author Allen Huang
 */
@Getter
@RequiredArgsConstructor
public enum OsType implements XEnum<Integer> {
    /**
     * ios
     */
    IOS(1),
    /**
     * macOS
     */
    OSX(2),
    /**
     * iPadOS
     */
    IPADOS(3),
    /**
     * Windows
     */
    WINDOWS(4),
    /**
     * Windows Phone
     */
    WINDOWS_PHONE(5),
    /**
     * Android
     */
    ANDROID(6),
    /**
     * Linux
     */
    Linux(7),
    /**
     * 鸿蒙OS
     */
    HARMONY_OS(8);

    private final Integer value;

}
