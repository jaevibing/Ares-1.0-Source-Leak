package com.google.api.client.http;

import com.google.api.client.util.FieldInfo;
import java.util.HashMap;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Iterator;
import com.google.api.client.util.Types;
import com.google.api.client.util.escape.CharEscapers;
import java.util.Map;
import com.google.api.client.util.Data;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;

public class UrlEncodedContent extends AbstractHttpContent
{
    private Object data;
    
    public UrlEncodedContent(final Object a1) {
        super(UrlEncodedParser.MEDIA_TYPE);
        this.setData(a1);
    }
    
    @Override
    public void writeTo(final OutputStream v-5) throws IOException {
        final Writer writer = /*EL:67*/new BufferedWriter(new OutputStreamWriter(v-5, this.getCharset()));
        boolean b = /*EL:68*/true;
        /*SL:69*/for (final Map.Entry<String, Object> entry : Data.mapOf(this.data).entrySet()) {
            final Object v0 = /*EL:70*/entry.getValue();
            /*SL:71*/if (v0 != null) {
                final String v = /*EL:72*/CharEscapers.escapeUri(entry.getKey());
                final Class<?> v2 = /*EL:73*/v0.getClass();
                /*SL:74*/if (v0 instanceof Iterable || v2.isArray()) {
                    /*SL:75*/for (final Object a1 : Types.<Object>iterableOf(v0)) {
                        /*SL:76*/b = appendParam(b, writer, v, a1);
                    }
                }
                else {
                    /*SL:79*/b = appendParam(b, writer, v, v0);
                }
            }
        }
        /*SL:83*/writer.flush();
    }
    
    @Override
    public UrlEncodedContent setMediaType(final HttpMediaType a1) {
        /*SL:88*/super.setMediaType(a1);
        /*SL:89*/return this;
    }
    
    public final Object getData() {
        /*SL:98*/return this.data;
    }
    
    public UrlEncodedContent setData(final Object a1) {
        /*SL:112*/this.data = Preconditions.<Object>checkNotNull(a1);
        /*SL:113*/return this;
    }
    
    public static UrlEncodedContent getContent(final HttpRequest a1) {
        final HttpContent v1 = /*EL:128*/a1.getContent();
        /*SL:129*/if (v1 != null) {
            /*SL:130*/return (UrlEncodedContent)v1;
        }
        final UrlEncodedContent v2 = /*EL:132*/new UrlEncodedContent(new HashMap());
        /*SL:133*/a1.setContent(v2);
        /*SL:134*/return v2;
    }
    
    private static boolean appendParam(boolean a1, final Writer a2, final String a3, final Object a4) throws IOException {
        /*SL:140*/if (a4 == null || Data.isNull(a4)) {
            /*SL:141*/return a1;
        }
        /*SL:144*/if (a1) {
            /*SL:145*/a1 = false;
        }
        else {
            /*SL:147*/a2.write("&");
        }
        /*SL:149*/a2.write(a3);
        final String v1 = /*EL:150*/CharEscapers.escapeUri((a4 instanceof Enum) ? /*EL:151*/FieldInfo.of((Enum<?>)a4).getName() : a4.toString());
        /*SL:152*/if (v1.length() != 0) {
            /*SL:153*/a2.write("=");
            /*SL:154*/a2.write(v1);
        }
        /*SL:156*/return a1;
    }
}
