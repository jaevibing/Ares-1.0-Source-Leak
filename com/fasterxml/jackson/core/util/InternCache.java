package com.fasterxml.jackson.core.util;

import java.util.concurrent.ConcurrentHashMap;

public final class InternCache extends ConcurrentHashMap<String, String>
{
    private static final long serialVersionUID = 1L;
    private static final int MAX_ENTRIES = 180;
    public static final InternCache instance;
    private final Object lock;
    
    private InternCache() {
        super(180, 0.8f, 4);
        this.lock = new Object();
    }
    
    public String intern(final String a1) {
        String v1 = /*EL:41*/((ConcurrentHashMap<K, String>)this).get(a1);
        /*SL:42*/if (v1 != null) {
            return v1;
        }
        /*SL:49*/if (this.size() >= 180) {
            /*SL:54*/synchronized (this.lock) {
                /*SL:55*/if (this.size() >= 180) {
                    /*SL:56*/this.clear();
                }
            }
        }
        /*SL:60*/v1 = a1.intern();
        /*SL:61*/this.put(v1, v1);
        /*SL:62*/return v1;
    }
    
    static {
        instance = new InternCache();
    }
}
