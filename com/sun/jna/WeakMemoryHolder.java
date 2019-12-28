package com.sun.jna;

import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.IdentityHashMap;
import java.lang.ref.ReferenceQueue;

public class WeakMemoryHolder
{
    ReferenceQueue<Object> referenceQueue;
    IdentityHashMap<Reference<Object>, Memory> backingMap;
    
    public WeakMemoryHolder() {
        this.referenceQueue = new ReferenceQueue<Object>();
        this.backingMap = new IdentityHashMap<Reference<Object>, Memory>();
    }
    
    public synchronized void put(final Object a1, final Memory a2) {
        /*SL:44*/this.clean();
        final Reference<Object> v1 = /*EL:45*/new WeakReference<Object>(a1, this.referenceQueue);
        /*SL:46*/this.backingMap.put(v1, a2);
    }
    
    public synchronized void clean() {
        /*SL:50*/for (Reference v1 = this.referenceQueue.poll(); v1 != null; v1 = this.referenceQueue.poll()) {
            /*SL:51*/this.backingMap.remove(v1);
        }
    }
}
