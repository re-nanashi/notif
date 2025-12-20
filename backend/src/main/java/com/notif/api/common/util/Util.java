package com.notif.api.common.util;

public final class Util {
    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
    public static boolean hasValue(String s) {
        return s != null && !s.isBlank();
    }
}
