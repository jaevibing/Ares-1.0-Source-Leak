package com.google.api.client.util;

import com.google.api.client.repackaged.com.google.common.base.Preconditions;

public final class Preconditions
{
    public static void checkArgument(final boolean a1) {
        /*SL:37*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(a1);
    }
    
    public static void checkArgument(final boolean a1, final Object a2) {
        /*SL:49*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(a1, a2);
    }
    
    public static void checkArgument(final boolean a1, final String a2, final Object... a3) {
        /*SL:69*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(a1, a2, a3);
    }
    
    public static void checkState(final boolean a1) {
        /*SL:81*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(a1);
    }
    
    public static void checkState(final boolean a1, final Object a2) {
        /*SL:94*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(a1, a2);
    }
    
    public static void checkState(final boolean a1, final String a2, final Object... a3) {
        /*SL:115*/com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(a1, a2, a3);
    }
    
    public static <T> T checkNotNull(final T a1) {
        /*SL:127*/return com.google.api.client.repackaged.com.google.common.base.Preconditions.<T>checkNotNull(a1);
    }
    
    public static <T> T checkNotNull(final T a1, final Object a2) {
        /*SL:140*/return com.google.api.client.repackaged.com.google.common.base.Preconditions.<T>checkNotNull(a1, a2);
    }
    
    public static <T> T checkNotNull(final T a1, final String a2, final Object... a3) {
        /*SL:159*/return com.google.api.client.repackaged.com.google.common.base.Preconditions.<T>checkNotNull(a1, a2, a3);
    }
}
