package com.fasterxml.jackson.core.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;

class ThreadLocalBufferManager
{
    private final Object RELEASE_LOCK;
    private final Map<SoftReference<BufferRecycler>, Boolean> _trackedRecyclers;
    private final ReferenceQueue<BufferRecycler> _refQueue;
    
    ThreadLocalBufferManager() {
        this.RELEASE_LOCK = new Object();
        this._trackedRecyclers = new ConcurrentHashMap<SoftReference<BufferRecycler>, Boolean>();
        this._refQueue = new ReferenceQueue<BufferRecycler>();
    }
    
    public static ThreadLocalBufferManager instance() {
        /*SL:57*/return ThreadLocalBufferManagerHolder.manager;
    }
    
    public int releaseBuffers() {
        /*SL:67*/synchronized (this.RELEASE_LOCK) {
            int n = /*EL:68*/0;
            /*SL:70*/this.removeSoftRefsClearedByGc();
            /*SL:71*/for (final SoftReference<BufferRecycler> v : this._trackedRecyclers.keySet()) {
                /*SL:72*/v.clear();
                /*SL:73*/++n;
            }
            /*SL:75*/this._trackedRecyclers.clear();
            /*SL:76*/return n;
        }
    }
    
    public SoftReference<BufferRecycler> wrapAndTrack(final BufferRecycler a1) {
        final SoftReference<BufferRecycler> v1 = /*EL:82*/new SoftReference<BufferRecycler>(a1, this._refQueue);
        /*SL:84*/this._trackedRecyclers.put(v1, true);
        /*SL:86*/this.removeSoftRefsClearedByGc();
        /*SL:87*/return v1;
    }
    
    private void removeSoftRefsClearedByGc() {
        SoftReference<?> v1;
        /*SL:103*/while ((v1 = (SoftReference<?>)(SoftReference)this._refQueue.poll()) != null) {
            /*SL:105*/this._trackedRecyclers.remove(v1);
        }
    }
    
    private static final class ThreadLocalBufferManagerHolder
    {
        static final ThreadLocalBufferManager manager;
        
        static {
            manager = new ThreadLocalBufferManager();
        }
    }
}
