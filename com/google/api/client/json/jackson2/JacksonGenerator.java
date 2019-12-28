package com.google.api.client.json.jackson2;

import com.google.api.client.json.JsonFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import com.google.api.client.json.JsonGenerator;

final class JacksonGenerator extends JsonGenerator
{
    private final com.fasterxml.jackson.core.JsonGenerator generator;
    private final JacksonFactory factory;
    
    @Override
    public JacksonFactory getFactory() {
        /*SL:38*/return this.factory;
    }
    
    JacksonGenerator(final JacksonFactory a1, final com.fasterxml.jackson.core.JsonGenerator a2) {
        this.factory = a1;
        this.generator = a2;
    }
    
    @Override
    public void flush() throws IOException {
        /*SL:48*/this.generator.flush();
    }
    
    @Override
    public void close() throws IOException {
        /*SL:53*/this.generator.close();
    }
    
    @Override
    public void writeBoolean(final boolean a1) throws IOException {
        /*SL:58*/this.generator.writeBoolean(a1);
    }
    
    @Override
    public void writeEndArray() throws IOException {
        /*SL:63*/this.generator.writeEndArray();
    }
    
    @Override
    public void writeEndObject() throws IOException {
        /*SL:68*/this.generator.writeEndObject();
    }
    
    @Override
    public void writeFieldName(final String a1) throws IOException {
        /*SL:73*/this.generator.writeFieldName(a1);
    }
    
    @Override
    public void writeNull() throws IOException {
        /*SL:78*/this.generator.writeNull();
    }
    
    @Override
    public void writeNumber(final int a1) throws IOException {
        /*SL:83*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final long a1) throws IOException {
        /*SL:88*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final BigInteger a1) throws IOException {
        /*SL:93*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final double a1) throws IOException {
        /*SL:98*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final float a1) throws IOException {
        /*SL:103*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final BigDecimal a1) throws IOException {
        /*SL:108*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final String a1) throws IOException {
        /*SL:113*/this.generator.writeNumber(a1);
    }
    
    @Override
    public void writeStartArray() throws IOException {
        /*SL:118*/this.generator.writeStartArray();
    }
    
    @Override
    public void writeStartObject() throws IOException {
        /*SL:123*/this.generator.writeStartObject();
    }
    
    @Override
    public void writeString(final String a1) throws IOException {
        /*SL:128*/this.generator.writeString(a1);
    }
    
    @Override
    public void enablePrettyPrint() throws IOException {
        /*SL:133*/this.generator.useDefaultPrettyPrinter();
    }
}
