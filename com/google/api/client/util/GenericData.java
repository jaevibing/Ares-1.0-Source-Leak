package com.google.api.client.util;

import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Locale;
import java.util.EnumSet;
import java.util.Map;
import java.util.AbstractMap;

public class GenericData extends AbstractMap<String, Object> implements Cloneable
{
    Map<String, Object> unknownFields;
    final ClassInfo classInfo;
    
    public GenericData() {
        this(EnumSet.<Flags>noneOf(Flags.class));
    }
    
    public GenericData(final EnumSet<Flags> a1) {
        this.unknownFields = (Map<String, Object>)ArrayMap.<Object, Object>create();
        this.classInfo = ClassInfo.of(this.getClass(), a1.contains(Flags.IGNORE_CASE));
    }
    
    @Override
    public final Object get(final Object a1) {
        /*SL:85*/if (!(a1 instanceof String)) {
            /*SL:86*/return null;
        }
        String v1 = /*EL:88*/(String)a1;
        final FieldInfo v2 = /*EL:89*/this.classInfo.getFieldInfo(v1);
        /*SL:90*/if (v2 != null) {
            /*SL:91*/return v2.getValue(this);
        }
        /*SL:93*/if (this.classInfo.getIgnoreCase()) {
            /*SL:94*/v1 = v1.toLowerCase(Locale.US);
        }
        /*SL:96*/return this.unknownFields.get(v1);
    }
    
    @Override
    public final Object put(String v1, final Object v2) {
        final FieldInfo v3 = /*EL:101*/this.classInfo.getFieldInfo(v1);
        /*SL:102*/if (v3 != null) {
            final Object a1 = /*EL:103*/v3.getValue(this);
            /*SL:104*/v3.setValue(this, v2);
            /*SL:105*/return a1;
        }
        /*SL:107*/if (this.classInfo.getIgnoreCase()) {
            /*SL:108*/v1 = v1.toLowerCase(Locale.US);
        }
        /*SL:110*/return this.unknownFields.put(v1, v2);
    }
    
    public GenericData set(String a1, final Object a2) {
        final FieldInfo v1 = /*EL:124*/this.classInfo.getFieldInfo(a1);
        /*SL:125*/if (v1 != null) {
            /*SL:126*/v1.setValue(this, a2);
        }
        else {
            /*SL:128*/if (this.classInfo.getIgnoreCase()) {
                /*SL:129*/a1 = a1.toLowerCase(Locale.US);
            }
            /*SL:131*/this.unknownFields.put(a1, a2);
        }
        /*SL:133*/return this;
    }
    
    @Override
    public final void putAll(final Map<? extends String, ?> v2) {
        /*SL:138*/for (final Map.Entry<? extends String, ?> a1 : v2.entrySet()) {
            /*SL:139*/this.set((String)a1.getKey(), a1.getValue());
        }
    }
    
    @Override
    public final Object remove(final Object a1) {
        /*SL:145*/if (!(a1 instanceof String)) {
            /*SL:146*/return null;
        }
        String v1 = /*EL:148*/(String)a1;
        final FieldInfo v2 = /*EL:149*/this.classInfo.getFieldInfo(v1);
        /*SL:150*/if (v2 != null) {
            /*SL:151*/throw new UnsupportedOperationException();
        }
        /*SL:153*/if (this.classInfo.getIgnoreCase()) {
            /*SL:154*/v1 = v1.toLowerCase(Locale.US);
        }
        /*SL:156*/return this.unknownFields.remove(v1);
    }
    
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        /*SL:161*/return new EntrySet();
    }
    
    public GenericData clone() {
        try {
            final GenericData v1 = /*EL:172*/(GenericData)super.clone();
            /*SL:173*/Data.deepCopy(this, v1);
            /*SL:174*/v1.unknownFields = Data.<Map<String, Object>>clone(this.unknownFields);
            /*SL:175*/return v1;
        }
        catch (CloneNotSupportedException v2) {
            /*SL:177*/throw new IllegalStateException(v2);
        }
    }
    
    public final Map<String, Object> getUnknownKeys() {
        /*SL:187*/return this.unknownFields;
    }
    
    public final void setUnknownKeys(final Map<String, Object> a1) {
        /*SL:196*/this.unknownFields = a1;
    }
    
    public final ClassInfo getClassInfo() {
        /*SL:205*/return this.classInfo;
    }
    
    public enum Flags
    {
        IGNORE_CASE;
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<String, Object>>
    {
        private final DataMap.EntrySet dataEntrySet;
        
        EntrySet() {
            this.dataEntrySet = new DataMap(GenericData.this, GenericData.this.classInfo.getIgnoreCase()).entrySet();
        }
        
        @Override
        public Iterator<Map.Entry<String, Object>> iterator() {
            /*SL:219*/return new EntryIterator(this.dataEntrySet);
        }
        
        @Override
        public int size() {
            /*SL:224*/return GenericData.this.unknownFields.size() + this.dataEntrySet.size();
        }
        
        @Override
        public void clear() {
            /*SL:229*/GenericData.this.unknownFields.clear();
            /*SL:230*/this.dataEntrySet.clear();
        }
    }
    
    final class EntryIterator implements Iterator<Map.Entry<String, Object>>
    {
        private boolean startedUnknown;
        private final Iterator<Map.Entry<String, Object>> fieldIterator;
        private final Iterator<Map.Entry<String, Object>> unknownIterator;
        
        EntryIterator(final DataMap.EntrySet a2) {
            this.fieldIterator = a2.iterator();
            this.unknownIterator = GenericData.this.unknownFields.entrySet().iterator();
        }
        
        @Override
        public boolean hasNext() {
            /*SL:255*/return this.fieldIterator.hasNext() || this.unknownIterator.hasNext();
        }
        
        @Override
        public Map.Entry<String, Object> next() {
            /*SL:259*/if (!this.startedUnknown) {
                /*SL:260*/if (this.fieldIterator.hasNext()) {
                    /*SL:261*/return this.fieldIterator.next();
                }
                /*SL:263*/this.startedUnknown = true;
            }
            /*SL:265*/return this.unknownIterator.next();
        }
        
        @Override
        public void remove() {
            /*SL:269*/if (this.startedUnknown) {
                /*SL:270*/this.unknownIterator.remove();
            }
            /*SL:272*/this.fieldIterator.remove();
        }
    }
}
