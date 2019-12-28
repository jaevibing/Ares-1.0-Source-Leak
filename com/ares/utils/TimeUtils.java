package com.ares.utils;

import java.time.temporal.Temporal;
import java.time.Duration;
import java.time.Instant;

public class TimeUtils
{
    public static Instant now() {
        /*SL:10*/return Instant.now();
    }
    
    public static long getSecondsPassed(final Instant a1, final Instant a2) {
        /*SL:15*/return Duration.between(a1, a2).getSeconds();
    }
    
    public static boolean haveSecondsPassed(final Instant a1, final Instant a2, final long a3) {
        /*SL:20*/return getSecondsPassed(a1, a2) >= a3;
    }
    
    public static long getMilliSecondsPassed(final Instant a1, final Instant a2) {
        /*SL:25*/return Duration.between(a1, a2).toMillis();
    }
    
    public static boolean haveMilliSecondsPassed(final Instant a1, final Instant a2, final long a3) {
        /*SL:30*/return getMilliSecondsPassed(a1, a2) >= a3;
    }
}
