package javassist.scopedpool;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Set;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.AbstractMap;

public class SoftValueHashMap extends AbstractMap implements Map
{
    private Map hash;
    private ReferenceQueue queue;
    
    @Override
    public Set entrySet() {
        /*SL:56*/this.processQueue();
        /*SL:57*/return this.hash.entrySet();
    }
    
    private void processQueue() {
        SoftValueRef v1;
        /*SL:72*/while ((v1 = (SoftValueRef)this.queue.poll()) != null) {
            /*SL:73*/if (v1 == this.hash.get(v1.key)) {
                /*SL:76*/this.hash.remove(v1.key);
            }
        }
    }
    
    public SoftValueHashMap(final int a1, final float a2) {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap(a1, a2);
    }
    
    public SoftValueHashMap(final int a1) {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap(a1);
    }
    
    public SoftValueHashMap() {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap();
    }
    
    public SoftValueHashMap(final Map a1) {
        this(Math.max(2 * a1.size(), 11), 0.75f);
        this.putAll(a1);
    }
    
    @Override
    public int size() {
        /*SL:146*/this.processQueue();
        /*SL:147*/return this.hash.size();
    }
    
    @Override
    public boolean isEmpty() {
        /*SL:154*/this.processQueue();
        /*SL:155*/return this.hash.isEmpty();
    }
    
    @Override
    public boolean containsKey(final Object a1) {
        /*SL:166*/this.processQueue();
        /*SL:167*/return this.hash.containsKey(a1);
    }
    
    @Override
    public Object get(final Object a1) {
        /*SL:181*/this.processQueue();
        final SoftReference v1 = /*EL:182*/this.hash.get(a1);
        /*SL:183*/if (v1 != null) {
            /*SL:184*/return v1.get();
        }
        /*SL:185*/return null;
    }
    
    @Override
    public Object put(final Object a1, final Object a2) {
        /*SL:204*/this.processQueue();
        Object v1 = /*EL:205*/this.hash.put(a1, create(a1, a2, this.queue));
        /*SL:206*/if (v1 != null) {
            /*SL:207*/v1 = ((SoftReference)v1).get();
        }
        /*SL:208*/return v1;
    }
    
    @Override
    public Object remove(final Object a1) {
        /*SL:222*/this.processQueue();
        /*SL:223*/return this.hash.remove(a1);
    }
    
    @Override
    public void clear() {
        /*SL:230*/this.processQueue();
        /*SL:231*/this.hash.clear();
    }
    
    private static class SoftValueRef extends SoftReference
    {
        public Object key;
        
        private SoftValueRef(final Object a1, final Object a2, final ReferenceQueue a3) {
            super(a2, a3);
            this.key = a1;
        }
        
        private static SoftValueRef create(final Object a1, final Object a2, final ReferenceQueue a3) {
            /*SL:44*/if (a2 == null) {
                /*SL:45*/return null;
            }
            /*SL:47*/return new SoftValueRef(a1, a2, a3);
        }
    }
}
