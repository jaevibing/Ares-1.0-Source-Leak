package com.google.api.client.googleapis.auth.clientlogin;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.lang.reflect.Field;
import java.util.Map;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.FieldInfo;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.api.client.util.Types;
import com.google.api.client.util.ClassInfo;
import java.io.IOException;
import java.io.InputStream;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Beta;
import com.google.api.client.util.ObjectParser;

@Beta
final class AuthKeyValueParser implements ObjectParser
{
    public static final AuthKeyValueParser INSTANCE;
    
    public String getContentType() {
        /*SL:49*/return "text/plain";
    }
    
    public <T> T parse(final HttpResponse a1, final Class<T> a2) throws IOException {
        /*SL:53*/a1.setContentLoggingLimit(0);
        final InputStream v1 = /*EL:54*/a1.getContent();
        try {
            /*SL:58*/return this.<T>parse(v1, a2);
        }
        finally {
            v1.close();
        }
    }
    
    public <T> T parse(final InputStream v-10, final Class<T> v-9) throws IOException {
        final ClassInfo of = /*EL:63*/ClassInfo.of(v-9);
        final T instance = /*EL:64*/Types.<T>newInstance(v-9);
        final BufferedReader bufferedReader = /*EL:65*/new BufferedReader(new InputStreamReader(v-10));
        while (true) {
            final String line = /*EL:67*/bufferedReader.readLine();
            /*SL:68*/if (line == null) {
                break;
            }
            final int index = /*EL:71*/line.indexOf(61);
            final String substring = /*EL:72*/line.substring(0, index);
            final String substring2 = /*EL:73*/line.substring(index + 1);
            final Field field = /*EL:75*/of.getField(substring);
            /*SL:76*/if (field != null) {
                Class<?> a2 = /*EL:77*/field.getType();
                final Object v1;
                /*SL:79*/if (a2 == Boolean.TYPE || a2 == Boolean.class) {
                    /*SL:80*/a2 = Boolean.valueOf(substring2);
                }
                else {
                    /*SL:82*/v1 = substring2;
                }
                /*SL:84*/FieldInfo.setFieldValue(field, instance, v1);
            }
            else/*SL:85*/ if (GenericData.class.isAssignableFrom(v-9)) {
                final GenericData v2 = /*EL:86*/(GenericData)instance;
                /*SL:87*/v2.set(substring, substring2);
            }
            else {
                /*SL:88*/if (!Map.class.isAssignableFrom(v-9)) {
                    continue;
                }
                final Map<Object, Object> a3 = /*EL:90*/(Map<Object, Object>)instance;
                /*SL:91*/a3.put(substring, substring2);
            }
        }
        /*SL:95*/return instance;
    }
    
    public <T> T parseAndClose(final InputStream a1, final Charset a2, final Class<T> a3) throws IOException {
        final Reader v1 = /*EL:103*/new InputStreamReader(a1, a2);
        /*SL:104*/return this.<T>parseAndClose(v1, a3);
    }
    
    public Object parseAndClose(final InputStream a1, final Charset a2, final Type a3) {
        /*SL:108*/throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }
    
    public <T> T parseAndClose(final Reader v-10, final Class<T> v-9) throws IOException {
        try {
            final ClassInfo of = /*EL:114*/ClassInfo.of(v-9);
            final T instance = /*EL:115*/Types.<T>newInstance(v-9);
            final BufferedReader bufferedReader = /*EL:116*/new BufferedReader(v-10);
            while (true) {
                final String line = /*EL:118*/bufferedReader.readLine();
                /*SL:119*/if (line == null) {
                    break;
                }
                final int index = /*EL:122*/line.indexOf(61);
                final String substring = /*EL:123*/line.substring(0, index);
                final String substring2 = /*EL:124*/line.substring(index + 1);
                final Field field = /*EL:126*/of.getField(substring);
                /*SL:127*/if (field != null) {
                    Class<?> a2 = /*EL:128*/field.getType();
                    final Object v1;
                    /*SL:130*/if (a2 == Boolean.TYPE || a2 == Boolean.class) {
                        /*SL:131*/a2 = Boolean.valueOf(substring2);
                    }
                    else {
                        /*SL:133*/v1 = substring2;
                    }
                    /*SL:135*/FieldInfo.setFieldValue(field, instance, v1);
                }
                else/*SL:136*/ if (GenericData.class.isAssignableFrom(v-9)) {
                    final GenericData v2 = /*EL:137*/(GenericData)instance;
                    /*SL:138*/v2.set(substring, substring2);
                }
                else {
                    /*SL:139*/if (!Map.class.isAssignableFrom(v-9)) {
                        continue;
                    }
                    final Map<Object, Object> a3 = /*EL:141*/(Map<Object, Object>)instance;
                    /*SL:142*/a3.put(substring, substring2);
                }
            }
            /*SL:148*/return instance;
        }
        finally {
            v-10.close();
        }
    }
    
    public Object parseAndClose(final Reader a1, final Type a2) {
        /*SL:153*/throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }
    
    static {
        INSTANCE = new AuthKeyValueParser();
    }
}
