package com.google.api.client.testing.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.google.api.client.json.JsonToken;
import java.io.IOException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.json.JsonParser;

@Beta
public class MockJsonParser extends JsonParser
{
    private boolean isClosed;
    private final JsonFactory factory;
    
    MockJsonParser(final JsonFactory a1) {
        this.factory = a1;
    }
    
    @Override
    public JsonFactory getFactory() {
        /*SL:50*/return this.factory;
    }
    
    @Override
    public void close() throws IOException {
        /*SL:55*/this.isClosed = true;
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:60*/return null;
    }
    
    @Override
    public JsonToken getCurrentToken() {
        /*SL:65*/return null;
    }
    
    @Override
    public String getCurrentName() throws IOException {
        /*SL:70*/return null;
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:75*/return null;
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:80*/return null;
    }
    
    @Override
    public byte getByteValue() throws IOException {
        /*SL:85*/return 0;
    }
    
    @Override
    public short getShortValue() throws IOException {
        /*SL:90*/return 0;
    }
    
    @Override
    public int getIntValue() throws IOException {
        /*SL:95*/return 0;
    }
    
    @Override
    public float getFloatValue() throws IOException {
        /*SL:100*/return 0.0f;
    }
    
    @Override
    public long getLongValue() throws IOException {
        /*SL:105*/return 0L;
    }
    
    @Override
    public double getDoubleValue() throws IOException {
        /*SL:110*/return 0.0;
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        /*SL:115*/return null;
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException {
        /*SL:120*/return null;
    }
    
    public boolean isClosed() {
        /*SL:129*/return this.isClosed;
    }
}
