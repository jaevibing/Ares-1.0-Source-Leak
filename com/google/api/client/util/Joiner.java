package com.google.api.client.util;

import com.google.api.client.repackaged.com.google.common.base.Joiner;

public final class Joiner
{
    private final com.google.api.client.repackaged.com.google.common.base.Joiner wrapped;
    
    public static Joiner on(final char a1) {
        /*SL:39*/return new Joiner(com.google.api.client.repackaged.com.google.common.base.Joiner.on(a1));
    }
    
    private Joiner(final com.google.api.client.repackaged.com.google.common.base.Joiner a1) {
        this.wrapped = a1;
    }
    
    public final String join(final Iterable<?> a1) {
        /*SL:54*/return this.wrapped.join(a1);
    }
}
