package com.google.api.client.util;

import com.google.api.client.repackaged.com.google.common.base.Throwables;

public final class Throwables
{
    public static RuntimeException propagate(final Throwable a1) {
        /*SL:56*/return com.google.api.client.repackaged.com.google.common.base.Throwables.propagate(a1);
    }
    
    public static void propagateIfPossible(final Throwable a1) {
        /*SL:77*/if (a1 != null) {
            /*SL:78*/com.google.api.client.repackaged.com.google.common.base.Throwables.throwIfUnchecked(a1);
        }
    }
    
    public static <X extends Throwable> void propagateIfPossible(final Throwable a1, final Class<X> a2) throws X, Throwable {
        /*SL:102*/com.google.api.client.repackaged.com.google.common.base.Throwables.<X>propagateIfPossible(a1, a2);
    }
}
