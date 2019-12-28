package com.google.api.client.util;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.Map;

public final class ArrayValueMap
{
    private final Map<String, ArrayValue> keyMap;
    private final Map<Field, ArrayValue> fieldMap;
    private final Object destination;
    
    public ArrayValueMap(final Object a1) {
        this.keyMap = (Map<String, ArrayValue>)ArrayMap.<Object, Object>create();
        this.fieldMap = (Map<Field, ArrayValue>)ArrayMap.<Object, Object>create();
        this.destination = a1;
    }
    
    public void setValues() {
        /*SL:101*/for (final Map.Entry<String, ArrayValue> v0 : this.keyMap.entrySet()) {
            final Map<String, Object> v = /*EL:103*/(Map<String, Object>)this.destination;
            /*SL:104*/v.put(v0.getKey(), v0.getValue().toArray());
        }
        /*SL:106*/for (final Map.Entry<Field, ArrayValue> v2 : this.fieldMap.entrySet()) {
            /*SL:107*/FieldInfo.setFieldValue(v2.getKey(), this.destination, v2.getValue().toArray());
        }
    }
    
    public void put(final Field a1, final Class<?> a2, final Object a3) {
        ArrayValue v1 = /*EL:120*/this.fieldMap.get(a1);
        /*SL:121*/if (v1 == null) {
            /*SL:122*/v1 = new ArrayValue(a2);
            /*SL:123*/this.fieldMap.put(a1, v1);
        }
        /*SL:125*/v1.addValue(a2, a3);
    }
    
    public void put(final String a1, final Class<?> a2, final Object a3) {
        ArrayValue v1 = /*EL:137*/this.keyMap.get(a1);
        /*SL:138*/if (v1 == null) {
            /*SL:139*/v1 = new ArrayValue(a2);
            /*SL:140*/this.keyMap.put(a1, v1);
        }
        /*SL:142*/v1.addValue(a2, a3);
    }
    
    static class ArrayValue
    {
        final Class<?> componentType;
        final ArrayList<Object> values;
        
        ArrayValue(final Class<?> a1) {
            this.values = new ArrayList<Object>();
            this.componentType = a1;
        }
        
        Object toArray() {
            /*SL:66*/return Types.toArray(this.values, this.componentType);
        }
        
        void addValue(final Class<?> a1, final Object a2) {
            /*SL:74*/Preconditions.checkArgument(a1 == this.componentType);
            /*SL:75*/this.values.add(a2);
        }
    }
}
