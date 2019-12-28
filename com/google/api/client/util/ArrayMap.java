package com.google.api.client.util;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Cloneable
{
    int size;
    private Object[] data;
    
    public static <K, V> ArrayMap<K, V> create() {
        /*SL:57*/return new ArrayMap<K, V>();
    }
    
    public static <K, V> ArrayMap<K, V> create(final int a1) {
        final ArrayMap<K, V> v1 = /*EL:65*/ArrayMap.<K, V>create();
        /*SL:66*/v1.ensureCapacity(a1);
        /*SL:67*/return v1;
    }
    
    public static <K, V> ArrayMap<K, V> of(final Object... a1) {
        final ArrayMap<K, V> v1 = /*EL:81*/ArrayMap.<K, V>create(1);
        final int v2 = /*EL:82*/a1.length;
        /*SL:83*/if (1 == v2 % 2) {
            /*SL:84*/throw new IllegalArgumentException("missing value for last key: " + a1[v2 - 1]);
        }
        /*SL:87*/v1.size = a1.length / 2;
        final ArrayMap<K, V> arrayMap = /*EL:88*/v1;
        final Object[] data = new Object[v2];
        arrayMap.data = data;
        final Object[] v3 = data;
        /*SL:89*/System.arraycopy(a1, 0, v3, 0, v2);
        /*SL:90*/return v1;
    }
    
    @Override
    public final int size() {
        /*SL:96*/return this.size;
    }
    
    public final K getKey(final int a1) {
        /*SL:101*/if (a1 < 0 || a1 >= this.size) {
            /*SL:102*/return null;
        }
        final K v1 = /*EL:105*/(K)this.data[a1 << 1];
        /*SL:106*/return v1;
    }
    
    public final V getValue(final int a1) {
        /*SL:111*/if (a1 < 0 || a1 >= this.size) {
            /*SL:112*/return null;
        }
        /*SL:114*/return this.valueAtDataIndex(1 + (a1 << 1));
    }
    
    public final V set(final int a1, final K a2, final V a3) {
        /*SL:128*/if (a1 < 0) {
            /*SL:129*/throw new IndexOutOfBoundsException();
        }
        final int v1 = /*EL:131*/a1 + 1;
        /*SL:132*/this.ensureCapacity(v1);
        final int v2 = /*EL:133*/a1 << 1;
        final V v3 = /*EL:134*/this.valueAtDataIndex(v2 + 1);
        /*SL:135*/this.setData(v2, a2, a3);
        /*SL:136*/if (v1 > this.size) {
            /*SL:137*/this.size = v1;
        }
        /*SL:139*/return v3;
    }
    
    public final V set(final int a1, final V a2) {
        final int v1 = /*EL:149*/this.size;
        /*SL:150*/if (a1 < 0 || a1 >= v1) {
            /*SL:151*/throw new IndexOutOfBoundsException();
        }
        final int v2 = /*EL:153*/1 + (a1 << 1);
        final V v3 = /*EL:154*/this.valueAtDataIndex(v2);
        /*SL:155*/this.data[v2] = a2;
        /*SL:156*/return v3;
    }
    
    public final void add(final K a1, final V a2) {
        /*SL:166*/this.set(this.size, a1, a2);
    }
    
    public final V remove(final int a1) {
        /*SL:175*/return this.removeFromDataIndexOfKey(a1 << 1);
    }
    
    @Override
    public final boolean containsKey(final Object a1) {
        /*SL:181*/return -2 != this.getDataIndexOfKey(a1);
    }
    
    public final int getIndexOfKey(final K a1) {
        /*SL:186*/return this.getDataIndexOfKey(a1) >> 1;
    }
    
    @Override
    public final V get(final Object a1) {
        /*SL:195*/return this.valueAtDataIndex(this.getDataIndexOfKey(a1) + 1);
    }
    
    @Override
    public final V put(final K a1, final V a2) {
        int v1 = /*EL:205*/this.getIndexOfKey(a1);
        /*SL:206*/if (v1 == -1) {
            /*SL:207*/v1 = this.size;
        }
        /*SL:209*/return this.set(v1, a1, a2);
    }
    
    @Override
    public final V remove(final Object a1) {
        /*SL:219*/return this.removeFromDataIndexOfKey(this.getDataIndexOfKey(a1));
    }
    
    public final void trim() {
        /*SL:224*/this.setDataCapacity(this.size << 1);
    }
    
    public final void ensureCapacity(final int v2) {
        /*SL:231*/if (v2 < 0) {
            /*SL:232*/throw new IndexOutOfBoundsException();
        }
        final Object[] v3 = /*EL:234*/this.data;
        final int v4 = /*EL:235*/v2 << 1;
        final int v5 = /*EL:236*/(v3 == null) ? 0 : v3.length;
        /*SL:237*/if (v4 > v5) {
            int a1 = /*EL:238*/v5 / 2 * 3 + 1;
            /*SL:239*/if (a1 % 2 != 0) {
                /*SL:240*/++a1;
            }
            /*SL:242*/if (a1 < v4) {
                /*SL:243*/a1 = v4;
            }
            /*SL:245*/this.setDataCapacity(a1);
        }
    }
    
    private void setDataCapacity(final int v2) {
        /*SL:250*/if (v2 == 0) {
            /*SL:251*/this.data = null;
            /*SL:252*/return;
        }
        final int v3 = /*EL:254*/this.size;
        final Object[] v4 = /*EL:255*/this.data;
        /*SL:256*/if (v3 == 0 || v2 != v4.length) {
            final Object[] data = /*EL:257*/new Object[v2];
            this.data = data;
            final Object[] a1 = data;
            /*SL:258*/if (v3 != 0) {
                /*SL:259*/System.arraycopy(v4, 0, a1, 0, v3 << 1);
            }
        }
    }
    
    private void setData(final int a1, final K a2, final V a3) {
        final Object[] v1 = /*EL:265*/this.data;
        /*SL:266*/v1[a1] = a2;
        /*SL:267*/v1[a1 + 1] = a3;
    }
    
    private V valueAtDataIndex(final int a1) {
        /*SL:271*/if (a1 < 0) {
            /*SL:272*/return null;
        }
        final V v1 = /*EL:275*/(V)this.data[a1];
        /*SL:276*/return v1;
    }
    
    private int getDataIndexOfKey(final Object v-2) {
        final int n = /*EL:283*/this.size << 1;
        final Object[] v0 = /*EL:284*/this.data;
        /*SL:285*/for (int v = 0; v < n; v += 2) {
            final Object a1 = /*EL:286*/v0[v];
            /*SL:287*/if (v-2 == null) {
                if (a1 == null) {
                    return /*EL:288*/v;
                }
            }
            else if (v-2.equals(a1)) {
                return v;
            }
        }
        /*SL:291*/return -2;
    }
    
    private V removeFromDataIndexOfKey(final int a1) {
        final int v1 = /*EL:299*/this.size << 1;
        /*SL:300*/if (a1 < 0 || a1 >= v1) {
            /*SL:301*/return null;
        }
        final V v2 = /*EL:303*/this.valueAtDataIndex(a1 + 1);
        final Object[] v3 = /*EL:304*/this.data;
        final int v4 = /*EL:305*/v1 - a1 - 2;
        /*SL:306*/if (v4 != 0) {
            /*SL:307*/System.arraycopy(v3, a1 + 2, v3, a1, v4);
        }
        /*SL:309*/--this.size;
        /*SL:310*/this.setData(v1 - 2, null, null);
        /*SL:311*/return v2;
    }
    
    @Override
    public void clear() {
        /*SL:316*/this.size = 0;
        /*SL:317*/this.data = null;
    }
    
    @Override
    public final boolean containsValue(final Object v-2) {
        final int n = /*EL:322*/this.size << 1;
        final Object[] v0 = /*EL:323*/this.data;
        /*SL:324*/for (int v = 1; v < n; v += 2) {
            final Object a1 = /*EL:325*/v0[v];
            /*SL:326*/if (v-2 == null) {
                if (a1 == null) {
                    return /*EL:327*/true;
                }
            }
            else if (v-2.equals(a1)) {
                return true;
            }
        }
        /*SL:330*/return false;
    }
    
    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        /*SL:335*/return new EntrySet();
    }
    
    public ArrayMap<K, V> clone() {
        try {
            final ArrayMap<K, V> arrayMap = /*EL:342*/(ArrayMap<K, V>)super.clone();
            final Object[] v0 = /*EL:343*/this.data;
            /*SL:344*/if (v0 != null) {
                final int v = /*EL:345*/v0.length;
                final ArrayMap<K, V> arrayMap2 = /*EL:346*/arrayMap;
                final Object[] data = new Object[v];
                arrayMap2.data = data;
                final Object[] v2 = data;
                /*SL:347*/System.arraycopy(v0, 0, v2, 0, v);
            }
            /*SL:349*/return arrayMap;
        }
        catch (CloneNotSupportedException ex) {
            /*SL:352*/return null;
        }
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<K, V>>
    {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            /*SL:360*/return new EntryIterator();
        }
        
        @Override
        public int size() {
            /*SL:365*/return ArrayMap.this.size;
        }
    }
    
    final class EntryIterator implements Iterator<Map.Entry<K, V>>
    {
        private boolean removed;
        private int nextIndex;
        
        @Override
        public boolean hasNext() {
            /*SL:375*/return this.nextIndex < ArrayMap.this.size;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            final int v1 = /*EL:379*/this.nextIndex;
            /*SL:380*/if (v1 == ArrayMap.this.size) {
                /*SL:381*/throw new NoSuchElementException();
            }
            /*SL:383*/++this.nextIndex;
            /*SL:384*/return new Entry(v1);
        }
        
        @Override
        public void remove() {
            final int v1 = /*EL:388*/this.nextIndex - 1;
            /*SL:389*/if (this.removed || v1 < 0) {
                /*SL:390*/throw new IllegalArgumentException();
            }
            /*SL:392*/ArrayMap.this.remove(v1);
            /*SL:393*/this.removed = true;
        }
    }
    
    final class Entry implements Map.Entry<K, V>
    {
        private int index;
        
        Entry(final int a2) {
            this.index = a2;
        }
        
        @Override
        public K getKey() {
            /*SL:406*/return ArrayMap.this.getKey(this.index);
        }
        
        @Override
        public V getValue() {
            /*SL:410*/return ArrayMap.this.getValue(this.index);
        }
        
        @Override
        public V setValue(final V a1) {
            /*SL:414*/return ArrayMap.this.set(this.index, a1);
        }
        
        @Override
        public int hashCode() {
            final K v1 = /*EL:419*/this.getKey();
            final V v2 = /*EL:420*/this.getValue();
            /*SL:421*/return ((v1 != null) ? v1.hashCode() : 0) ^ ((v2 != null) ? v2.hashCode() : 0);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:426*/if (this == a1) {
                /*SL:427*/return true;
            }
            /*SL:429*/if (!(a1 instanceof Map.Entry)) {
                /*SL:430*/return false;
            }
            final Map.Entry<?, ?> v1 = /*EL:432*/(Map.Entry<?, ?>)a1;
            /*SL:433*/return Objects.equal(this.getKey(), v1.getKey()) && Objects.equal(this.getValue(), v1.getValue());
        }
    }
}
