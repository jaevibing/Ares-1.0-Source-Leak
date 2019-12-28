package com.google.api.client.json;

import com.google.api.client.util.Charsets;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.InputStream;

public abstract class JsonFactory
{
    public abstract JsonParser createJsonParser(final InputStream p0) throws IOException;
    
    public abstract JsonParser createJsonParser(final InputStream p0, final Charset p1) throws IOException;
    
    public abstract JsonParser createJsonParser(final String p0) throws IOException;
    
    public abstract JsonParser createJsonParser(final Reader p0) throws IOException;
    
    public abstract JsonGenerator createJsonGenerator(final OutputStream p0, final Charset p1) throws IOException;
    
    public abstract JsonGenerator createJsonGenerator(final Writer p0) throws IOException;
    
    public final JsonObjectParser createJsonObjectParser() {
        /*SL:101*/return new JsonObjectParser(this);
    }
    
    public final String toString(final Object a1) throws IOException {
        /*SL:112*/return this.toString(a1, false);
    }
    
    public final String toPrettyString(final Object a1) throws IOException {
        /*SL:130*/return this.toString(a1, true);
    }
    
    public final byte[] toByteArray(final Object a1) throws IOException {
        /*SL:142*/return this.toByteStream(a1, false).toByteArray();
    }
    
    private String toString(final Object a1, final boolean a2) throws IOException {
        /*SL:154*/return this.toByteStream(a1, a2).toString("UTF-8");
    }
    
    private ByteArrayOutputStream toByteStream(final Object a1, final boolean a2) throws IOException {
        final ByteArrayOutputStream v1 = /*EL:166*/new ByteArrayOutputStream();
        final JsonGenerator v2 = /*EL:167*/this.createJsonGenerator(v1, Charsets.UTF_8);
        /*SL:168*/if (a2) {
            /*SL:169*/v2.enablePrettyPrint();
        }
        /*SL:171*/v2.serialize(a1);
        /*SL:172*/v2.flush();
        /*SL:173*/return v1;
    }
    
    public final <T> T fromString(final String a1, final Class<T> a2) throws IOException {
        /*SL:187*/return this.createJsonParser(a1).<T>parse(a2);
    }
    
    public final <T> T fromInputStream(final InputStream a1, final Class<T> a2) throws IOException {
        /*SL:206*/return this.createJsonParser(a1).<T>parseAndClose(a2);
    }
    
    public final <T> T fromInputStream(final InputStream a1, final Charset a2, final Class<T> a3) throws IOException {
        /*SL:222*/return this.createJsonParser(a1, a2).<T>parseAndClose(a3);
    }
    
    public final <T> T fromReader(final Reader a1, final Class<T> a2) throws IOException {
        /*SL:236*/return this.createJsonParser(a1).<T>parseAndClose(a2);
    }
}
