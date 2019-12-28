package com.google.api.client.json;

import java.lang.reflect.Field;
import java.util.Iterator;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.GenericData;
import java.util.Map;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Data;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;

public abstract class JsonGenerator
{
    public abstract JsonFactory getFactory();
    
    public abstract void flush() throws IOException;
    
    public abstract void close() throws IOException;
    
    public abstract void writeStartArray() throws IOException;
    
    public abstract void writeEndArray() throws IOException;
    
    public abstract void writeStartObject() throws IOException;
    
    public abstract void writeEndObject() throws IOException;
    
    public abstract void writeFieldName(final String p0) throws IOException;
    
    public abstract void writeNull() throws IOException;
    
    public abstract void writeString(final String p0) throws IOException;
    
    public abstract void writeBoolean(final boolean p0) throws IOException;
    
    public abstract void writeNumber(final int p0) throws IOException;
    
    public abstract void writeNumber(final long p0) throws IOException;
    
    public abstract void writeNumber(final BigInteger p0) throws IOException;
    
    public abstract void writeNumber(final float p0) throws IOException;
    
    public abstract void writeNumber(final double p0) throws IOException;
    
    public abstract void writeNumber(final BigDecimal p0) throws IOException;
    
    public abstract void writeNumber(final String p0) throws IOException;
    
    public final void serialize(final Object a1) throws IOException {
        /*SL:106*/this.serialize(false, a1);
    }
    
    private void serialize(final boolean v-5, final Object v-4) throws IOException {
        /*SL:110*/if (v-4 == null) {
            /*SL:111*/return;
        }
        final Class<?> class1 = /*EL:113*/v-4.getClass();
        /*SL:114*/if (Data.isNull(v-4)) {
            /*SL:115*/this.writeNull();
        }
        else/*SL:116*/ if (v-4 instanceof String) {
            /*SL:117*/this.writeString((String)v-4);
        }
        else/*SL:118*/ if (v-4 instanceof Number) {
            /*SL:119*/if (v-5) {
                /*SL:120*/this.writeString(v-4.toString());
            }
            else/*SL:121*/ if (v-4 instanceof BigDecimal) {
                /*SL:122*/this.writeNumber((BigDecimal)v-4);
            }
            else/*SL:123*/ if (v-4 instanceof BigInteger) {
                /*SL:124*/this.writeNumber((BigInteger)v-4);
            }
            else/*SL:125*/ if (v-4 instanceof Long) {
                /*SL:126*/this.writeNumber((long)v-4);
            }
            else/*SL:127*/ if (v-4 instanceof Float) {
                final float a1 = /*EL:128*/((Number)v-4).floatValue();
                /*SL:129*/Preconditions.checkArgument(!Float.isInfinite(a1) && !Float.isNaN(a1));
                /*SL:130*/this.writeNumber(a1);
            }
            else/*SL:131*/ if (v-4 instanceof Integer || v-4 instanceof Short || v-4 instanceof Byte) {
                /*SL:132*/this.writeNumber(((Number)v-4).intValue());
            }
            else {
                final double a2 = /*EL:134*/((Number)v-4).doubleValue();
                /*SL:135*/Preconditions.checkArgument(!Double.isInfinite(a2) && !Double.isNaN(a2));
                /*SL:136*/this.writeNumber(a2);
            }
        }
        else/*SL:138*/ if (v-4 instanceof Boolean) {
            /*SL:139*/this.writeBoolean((boolean)v-4);
        }
        else/*SL:140*/ if (v-4 instanceof DateTime) {
            /*SL:141*/this.writeString(((DateTime)v-4).toStringRfc3339());
        }
        else/*SL:142*/ if (v-4 instanceof Iterable || class1.isArray()) {
            /*SL:143*/this.writeStartArray();
            /*SL:144*/for (final Object v1 : Types.<Object>iterableOf(v-4)) {
                /*SL:145*/this.serialize(v-5, v1);
            }
            /*SL:147*/this.writeEndArray();
        }
        else/*SL:148*/ if (class1.isEnum()) {
            final String name = /*EL:149*/FieldInfo.of((Enum<?>)v-4).getName();
            /*SL:150*/if (name == null) {
                /*SL:151*/this.writeNull();
            }
            else {
                /*SL:153*/this.writeString(name);
            }
        }
        else {
            /*SL:156*/this.writeStartObject();
            final boolean b = /*EL:158*/v-4 instanceof Map && !(v-4 instanceof GenericData);
            final ClassInfo v2 = /*EL:159*/b ? null : ClassInfo.of(class1);
            /*SL:160*/for (final Map.Entry<String, Object> v3 : Data.mapOf(v-4).entrySet()) {
                final Object v4 = /*EL:161*/v3.getValue();
                /*SL:162*/if (v4 != null) {
                    final String v5 = /*EL:163*/v3.getKey();
                    boolean v6;
                    /*SL:165*/if (b) {
                        /*SL:166*/v6 = v-5;
                    }
                    else {
                        final Field v7 = /*EL:168*/v2.getField(v5);
                        /*SL:169*/v6 = (v7 != null && v7.<JsonString>getAnnotation(JsonString.class) != null);
                    }
                    /*SL:171*/this.writeFieldName(v5);
                    /*SL:172*/this.serialize(v6, v4);
                }
            }
            /*SL:175*/this.writeEndObject();
        }
    }
    
    public void enablePrettyPrint() throws IOException {
    }
}
