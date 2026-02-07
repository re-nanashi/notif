package com.notif.api.common.util;

public final class Util {
    public static final long MILLISECONDS_PER_SECOND = 1000;

    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}