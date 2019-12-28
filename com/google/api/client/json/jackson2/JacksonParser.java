package com.google.api.client.json.jackson2;

import com.google.api.client.json.JsonFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.google.api.client.json.JsonToken;
import java.io.IOException;
import com.google.api.client.json.JsonParser;

final class JacksonParser extends JsonParser
{
    private final com.fasterxml.jackson.core.JsonParser parser;
    private final JacksonFactory factory;
    
    @Override
    public JacksonFactory getFactory() {
        /*SL:40*/return this.factory;
    }
    
    JacksonParser(final JacksonFactory a1, final com.fasterxml.jackson.core.JsonParser a2) {
        this.factory = a1;
        this.parser = a2;
    }
    
    @Override
    public void close() throws IOException {
        /*SL:50*/this.parser.close();
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:55*/return JacksonFactory.convert(this.parser.nextToken());
    }
    
    @Override
    public String getCurrentName() throws IOException {
        /*SL:60*/return this.parser.getCurrentName();
    }
    
    @Override
    public JsonToken getCurrentToken() {
        /*SL:65*/return JacksonFactory.convert(this.parser.getCurrentToken());
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:70*/this.parser.skipChildren();
        /*SL:71*/return this;
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:76*/return this.parser.getText();
    }
    
    @Override
    public byte getByteValue() throws IOException {
        /*SL:81*/return this.parser.getByteValue();
    }
    
    @Override
    public float getFloatValue() throws IOException {
        /*SL:86*/return this.parser.getFloatValue();
    }
    
    @Override
    public int getIntValue() throws IOException {
        /*SL:91*/return this.parser.getIntValue();
    }
    
    @Override
    public short getShortValue() throws IOException {
        /*SL:96*/return this.parser.getShortValue();
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        /*SL:101*/return this.parser.getBigIntegerValue();
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException {
        /*SL:106*/return this.parser.getDecimalValue();
    }
    
    @Override
    public double getDoubleValue() throws IOException {
        /*SL:111*/return this.parser.getDoubleValue();
    }
    
    @Override
    public long getLongValue() throws IOException {
        /*SL:116*/return this.parser.getLongValue();
    }
}
