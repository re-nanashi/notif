package com.notif.api.shared.constants;

public final class AppConstants {
    private AppConstants() {
        throw new AssertionError("This class cannot be instantiated.");
    }

    // Time conversions
    public static final long MILLISECONDS_PER_SECOND = 1000;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int HOURS_PER_DAY = 24;
}