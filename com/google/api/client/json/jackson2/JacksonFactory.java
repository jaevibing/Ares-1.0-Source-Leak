package com.google.api.client.json.jackson2;

import com.fasterxml.jackson.core.JsonToken;
import java.io.InputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.json.JsonParser;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonEncoding;
import java.nio.charset.Charset;
import java.io.OutputStream;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.api.client.json.JsonFactory;

public final class JacksonFactory extends JsonFactory
{
    private final com.fasterxml.jackson.core.JsonFactory factory;
    
    public JacksonFactory() {
        (this.factory = new com.fasterxml.jackson.core.JsonFactory()).configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
    }
    
    public static JacksonFactory getDefaultInstance() {
        /*SL:59*/return InstanceHolder.INSTANCE;
    }
    
    @Override
    public com.google.api.client.json.JsonGenerator createJsonGenerator(final OutputStream a1, final Charset a2) throws IOException {
        /*SL:69*/return /*EL:70*/new JacksonGenerator(this, this.factory.createJsonGenerator(a1, JsonEncoding.UTF8));
    }
    
    @Override
    public com.google.api.client.json.JsonGenerator createJsonGenerator(final Writer a1) throws IOException {
        /*SL:75*/return new JacksonGenerator(this, this.factory.createJsonGenerator(a1));
    }
    
    @Override
    public JsonParser createJsonParser(final Reader a1) throws IOException {
        /*SL:80*/Preconditions.<Reader>checkNotNull(a1);
        /*SL:81*/return new JacksonParser(this, this.factory.createJsonParser(a1));
    }
    
    @Override
    public JsonParser createJsonParser(final InputStream a1) throws IOException {
        /*SL:86*/Preconditions.<InputStream>checkNotNull(a1);
        /*SL:87*/return new JacksonParser(this, this.factory.createJsonParser(a1));
    }
    
    @Override
    public JsonParser createJsonParser(final InputStream a1, final Charset a2) throws IOException {
        /*SL:92*/Preconditions.<InputStream>checkNotNull(a1);
        /*SL:93*/return new JacksonParser(this, this.factory.createJsonParser(a1));
    }
    
    @Override
    public JsonParser createJsonParser(final String a1) throws IOException {
        /*SL:98*/Preconditions.<String>checkNotNull(a1);
        /*SL:99*/return new JacksonParser(this, this.factory.createJsonParser(a1));
    }
    
    static com.google.api.client.json.JsonToken convert(final JsonToken a1) {
        /*SL:103*/if (a1 == null) {
            /*SL:104*/return null;
        }
        /*SL:106*/switch (a1) {
            case END_ARRAY: {
                /*SL:108*/return com.google.api.client.json.JsonToken.END_ARRAY;
            }
            case START_ARRAY: {
                /*SL:110*/return com.google.api.client.json.JsonToken.START_ARRAY;
            }
            case END_OBJECT: {
                /*SL:112*/return com.google.api.client.json.JsonToken.END_OBJECT;
            }
            case START_OBJECT: {
                /*SL:114*/return com.google.api.client.json.JsonToken.START_OBJECT;
            }
            case VALUE_FALSE: {
                /*SL:116*/return com.google.api.client.json.JsonToken.VALUE_FALSE;
            }
            case VALUE_TRUE: {
                /*SL:118*/return com.google.api.client.json.JsonToken.VALUE_TRUE;
            }
            case VALUE_NULL: {
                /*SL:120*/return com.google.api.client.json.JsonToken.VALUE_NULL;
            }
            case VALUE_STRING: {
                /*SL:122*/return com.google.api.client.json.JsonToken.VALUE_STRING;
            }
            case VALUE_NUMBER_FLOAT: {
                /*SL:124*/return com.google.api.client.json.JsonToken.VALUE_NUMBER_FLOAT;
            }
            case VALUE_NUMBER_INT: {
                /*SL:126*/return com.google.api.client.json.JsonToken.VALUE_NUMBER_INT;
            }
            case FIELD_NAME: {
                /*SL:128*/return com.google.api.client.json.JsonToken.FIELD_NAME;
            }
            default: {
                /*SL:130*/return com.google.api.client.json.JsonToken.NOT_AVAILABLE;
            }
        }
    }
    
    static class InstanceHolder
    {
        static final JacksonFactory INSTANCE;
        
        static {
            INSTANCE = new JacksonFactory();
        }
    }
}
