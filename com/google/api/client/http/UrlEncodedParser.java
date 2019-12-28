package com.google.api.client.http;

import com.google.api.client.util.Charsets;
import java.io.BufferedReader;
import com.google.api.client.util.Preconditions;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import com.google.api.client.util.FieldInfo;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import com.google.api.client.util.Types;
import com.google.api.client.util.Data;
import com.google.api.client.util.escape.CharEscapers;
import java.io.StringWriter;
import com.google.api.client.util.ArrayValueMap;
import java.util.Map;
import com.google.api.client.util.GenericData;
import java.util.Arrays;
import java.lang.reflect.Type;
import com.google.api.client.util.ClassInfo;
import java.io.IOException;
import com.google.api.client.util.Throwables;
import java.io.Reader;
import java.io.StringReader;
import com.google.api.client.util.ObjectParser;

public class UrlEncodedParser implements ObjectParser
{
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String MEDIA_TYPE;
    
    public static void parse(final String a2, final Object v1) {
        /*SL:92*/if (a2 == null) {
            /*SL:93*/return;
        }
        try {
            parse(/*EL:96*/new StringReader(a2), v1);
        }
        catch (IOException a3) {
            /*SL:99*/throw Throwables.propagate(a3);
        }
    }
    
    public static void parse(final Reader v-16, final Object v-15) throws IOException {
        final Class<?> class1 = /*EL:128*/v-15.getClass();
        final ClassInfo of = /*EL:129*/ClassInfo.of(class1);
        final List<Type> list = /*EL:130*/Arrays.<Type>asList(class1);
        final GenericData genericData = /*EL:131*/GenericData.class.isAssignableFrom(class1) ? ((GenericData)v-15) : null;
        final Map<Object, Object> map = /*EL:133*/(Map<Object, Object>)(Map.class.isAssignableFrom(class1) ? ((Map)v-15) : null);
        final ArrayValueMap arrayValueMap = /*EL:134*/new ArrayValueMap(v-15);
        StringWriter stringWriter = /*EL:135*/new StringWriter();
        StringWriter stringWriter2 = /*EL:136*/new StringWriter();
        boolean b = /*EL:137*/true;
    Block_12:
        while (true) {
            final int read = /*EL:139*/v-16.read();
            /*SL:140*/switch (read) {
                case -1:
                case 38: {
                    final String decodeUri = /*EL:145*/CharEscapers.decodeUri(stringWriter.toString());
                    /*SL:146*/if (decodeUri.length() != 0) {
                        final String decodeUri2 = /*EL:147*/CharEscapers.decodeUri(stringWriter2.toString());
                        final FieldInfo fieldInfo = /*EL:149*/of.getFieldInfo(decodeUri);
                        /*SL:150*/if (fieldInfo != null) {
                            final Type resolveWildcardTypeOrTypeVariable = /*EL:152*/Data.resolveWildcardTypeOrTypeVariable(list, fieldInfo.getGenericType());
                            /*SL:154*/if (Types.isArray(resolveWildcardTypeOrTypeVariable)) {
                                final Class<?> a1 = /*EL:157*/Types.getRawArrayComponentType(list, Types.getArrayComponentType(resolveWildcardTypeOrTypeVariable));
                                /*SL:158*/arrayValueMap.put(fieldInfo.getField(), a1, parseValue(a1, list, decodeUri2));
                            }
                            else/*SL:160*/ if (Types.isAssignableToOrFrom(/*EL:161*/Types.getRawArrayComponentType(list, resolveWildcardTypeOrTypeVariable), Iterable.class)) {
                                Collection<Object> a2 = /*EL:164*/(Collection<Object>)fieldInfo.getValue(v-15);
                                /*SL:165*/if (a2 == null) {
                                    /*SL:166*/a2 = Data.newCollectionInstance(resolveWildcardTypeOrTypeVariable);
                                    /*SL:167*/fieldInfo.setValue(v-15, a2);
                                }
                                final Type v1 = /*EL:169*/(resolveWildcardTypeOrTypeVariable == Object.class) ? null : Types.getIterableParameter(resolveWildcardTypeOrTypeVariable);
                                /*SL:170*/a2.add(parseValue(v1, list, decodeUri2));
                            }
                            else {
                                /*SL:173*/fieldInfo.setValue(v-15, parseValue(resolveWildcardTypeOrTypeVariable, list, decodeUri2));
                            }
                        }
                        else/*SL:175*/ if (map != null) {
                            ArrayList<String> a3 = /*EL:178*/map.get(decodeUri);
                            /*SL:179*/if (a3 == null) {
                                /*SL:180*/a3 = new ArrayList<String>();
                                /*SL:181*/if (genericData != null) {
                                    /*SL:182*/genericData.set(decodeUri, a3);
                                }
                                else {
                                    /*SL:184*/map.put(decodeUri, a3);
                                }
                            }
                            /*SL:187*/a3.add(decodeUri2);
                        }
                    }
                    /*SL:191*/b = true;
                    /*SL:192*/stringWriter = new StringWriter();
                    /*SL:193*/stringWriter2 = new StringWriter();
                    /*SL:194*/if (read == -1) {
                        break Block_12;
                    }
                    continue;
                }
                case 61: {
                    /*SL:199*/if (b) {
                        /*SL:201*/b = false;
                        continue;
                    }
                    /*SL:204*/stringWriter2.write(read);
                    /*SL:206*/continue;
                }
                default: {
                    /*SL:209*/if (b) {
                        /*SL:210*/stringWriter.write(read);
                        continue;
                    }
                    /*SL:212*/stringWriter2.write(read);
                    continue;
                }
            }
        }
        /*SL:216*/arrayValueMap.setValues();
    }
    
    private static Object parseValue(final Type a1, final List<Type> a2, final String a3) {
        final Type v1 = /*EL:220*/Data.resolveWildcardTypeOrTypeVariable(a2, a1);
        /*SL:221*/return Data.parsePrimitiveValue(v1, a3);
    }
    
    @Override
    public <T> T parseAndClose(final InputStream a1, final Charset a2, final Class<T> a3) throws IOException {
        final InputStreamReader v1 = /*EL:226*/new InputStreamReader(a1, a2);
        /*SL:227*/return this.<T>parseAndClose(v1, a3);
    }
    
    @Override
    public Object parseAndClose(final InputStream a1, final Charset a2, final Type a3) throws IOException {
        final InputStreamReader v1 = /*EL:231*/new InputStreamReader(a1, a2);
        /*SL:232*/return this.parseAndClose(v1, a3);
    }
    
    @Override
    public <T> T parseAndClose(final Reader a1, final Class<T> a2) throws IOException {
        /*SL:237*/return (T)this.parseAndClose(a1, (Type)a2);
    }
    
    @Override
    public Object parseAndClose(final Reader a1, final Type a2) throws IOException {
        /*SL:241*/Preconditions.checkArgument(a2 instanceof Class, (Object)"dataType has to be of type Class<?>");
        final Object v1 = /*EL:244*/Types.<Object>newInstance((Class<Object>)a2);
        parse(/*EL:245*/new BufferedReader(a1), v1);
        /*SL:246*/return v1;
    }
    
    static {
        MEDIA_TYPE = new HttpMediaType("application/x-www-form-urlencoded").setCharsetParameter(Charsets.UTF_8).build();
    }
}
