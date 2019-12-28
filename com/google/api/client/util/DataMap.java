package com.google.api.client.util;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractSet;
import java.util.Set;
import java.util.AbstractMap;

final class DataMap extends AbstractMap<String, Object>
{
    final Object object;
    final ClassInfo classInfo;
    
    DataMap(final Object a1, final boolean a2) {
        this.object = a1;
        this.classInfo = ClassInfo.of(a1.getClass(), a2);
        Preconditions.checkArgument(!this.classInfo.isEnum());
    }
    
    @Override
    public EntrySet entrySet() {
        /*SL:51*/return new EntrySet();
    }
    
    @Override
    public boolean containsKey(final Object a1) {
        /*SL:56*/return this.get(a1) != null;
    }
    
    @Override
    public Object get(final Object a1) {
        /*SL:61*/if (!(a1 instanceof String)) {
            /*SL:62*/return null;
        }
        final FieldInfo v1 = /*EL:64*/this.classInfo.getFieldInfo((String)a1);
        /*SL:65*/if (v1 == null) {
            /*SL:66*/return null;
        }
        /*SL:68*/return v1.getValue(this.object);
    }
    
    @Override
    public Object put(final String a1, final Object a2) {
        final FieldInfo v1 = /*EL:73*/this.classInfo.getFieldInfo(a1);
        /*SL:74*/Preconditions.<FieldInfo>checkNotNull(v1, (Object)("no field of key " + a1));
        final Object v2 = /*EL:75*/v1.getValue(this.object);
        /*SL:76*/v1.setValue(this.object, Preconditions.<Object>checkNotNull(a2));
        /*SL:77*/return v2;
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<String, Object>>
    {
        @Override
        public EntryIterator iterator() {
            /*SL:85*/return new EntryIterator();
        }
        
        @Override
        public int size() {
            int n = /*EL:90*/0;
            /*SL:91*/for (final String v1 : DataMap.this.classInfo.names) {
                /*SL:92*/if (DataMap.this.classInfo.getFieldInfo(v1).getValue(DataMap.this.object) != null) {
                    /*SL:93*/++n;
                }
            }
            /*SL:96*/return n;
        }
        
        @Override
        public void clear() {
            /*SL:101*/for (final String v1 : DataMap.this.classInfo.names) {
                /*SL:102*/DataMap.this.classInfo.getFieldInfo(v1).setValue(DataMap.this.object, null);
            }
        }
        
        @Override
        public boolean isEmpty() {
            /*SL:108*/for (final String v1 : DataMap.this.classInfo.names) {
                /*SL:109*/if (DataMap.this.classInfo.getFieldInfo(v1).getValue(DataMap.this.object) != null) {
                    /*SL:110*/return false;
                }
            }
            /*SL:113*/return true;
        }
    }
    
    final class EntryIterator implements Iterator<Map.Entry<String, Object>>
    {
        private int nextKeyIndex;
        private FieldInfo nextFieldInfo;
        private Object nextFieldValue;
        private boolean isRemoved;
        private boolean isComputed;
        private FieldInfo currentFieldInfo;
        
        EntryIterator() {
            this.nextKeyIndex = -1;
        }
        
        @Override
        public boolean hasNext() {
            /*SL:151*/if (!this.isComputed) {
                /*SL:152*/this.isComputed = true;
                /*SL:153*/this.nextFieldValue = null;
                /*SL:154*/while (this.nextFieldValue == null && ++this.nextKeyIndex < DataMap.this.classInfo.names.size()) {
                    /*SL:155*/this.nextFieldInfo = DataMap.this.classInfo.getFieldInfo(DataMap.this.classInfo.names.get(this.nextKeyIndex));
                    /*SL:156*/this.nextFieldValue = this.nextFieldInfo.getValue(DataMap.this.object);
                }
            }
            /*SL:159*/return this.nextFieldValue != null;
        }
        
        @Override
        public Map.Entry<String, Object> next() {
            /*SL:163*/if (!this.hasNext()) {
                /*SL:164*/throw new NoSuchElementException();
            }
            /*SL:166*/this.currentFieldInfo = this.nextFieldInfo;
            final Object v1 = /*EL:167*/this.nextFieldValue;
            /*SL:168*/this.isComputed = false;
            /*SL:169*/this.isRemoved = false;
            /*SL:170*/this.nextFieldInfo = null;
            /*SL:171*/this.nextFieldValue = null;
            /*SL:172*/return new Entry(this.currentFieldInfo, v1);
        }
        
        @Override
        public void remove() {
            /*SL:176*/Preconditions.checkState(this.currentFieldInfo != null && !this.isRemoved);
            /*SL:177*/this.isRemoved = true;
            /*SL:178*/this.currentFieldInfo.setValue(DataMap.this.object, null);
        }
    }
    
    final class Entry implements Map.Entry<String, Object>
    {
        private Object fieldValue;
        private final FieldInfo fieldInfo;
        
        Entry(final FieldInfo a2, final Object a3) {
            this.fieldInfo = a2;
            this.fieldValue = Preconditions.<Object>checkNotNull(a3);
        }
        
        @Override
        public String getKey() {
            String v1 = /*EL:206*/this.fieldInfo.getName();
            /*SL:207*/if (DataMap.this.classInfo.getIgnoreCase()) {
                /*SL:208*/v1 = v1.toLowerCase(Locale.US);
            }
            /*SL:210*/return v1;
        }
        
        @Override
        public Object getValue() {
            /*SL:214*/return this.fieldValue;
        }
        
        @Override
        public Object setValue(final Object a1) {
            final Object v1 = /*EL:218*/this.fieldValue;
            /*SL:219*/this.fieldValue = Preconditions.<Object>checkNotNull(a1);
            /*SL:220*/this.fieldInfo.setValue(DataMap.this.object, a1);
            /*SL:221*/return v1;
        }
        
        @Override
        public int hashCode() {
            /*SL:226*/return this.getKey().hashCode() ^ this.getValue().hashCode();
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:231*/if (this == a1) {
                /*SL:232*/return true;
            }
            /*SL:234*/if (!(a1 instanceof Map.Entry)) {
                /*SL:235*/return false;
            }
            final Map.Entry<?, ?> v1 = /*EL:237*/(Map.Entry<?, ?>)a1;
            /*SL:238*/return this.getKey().equals(v1.getKey()) && this.getValue().equals(v1.getValue());
        }
    }
}
