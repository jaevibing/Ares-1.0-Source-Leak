package com.google.api.client.testing.json;

import java.io.Writer;
import com.google.api.client.json.JsonGenerator;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.io.IOException;
import com.google.api.client.json.JsonParser;
import java.io.InputStream;
import com.google.api.client.util.Beta;
import com.google.api.client.json.JsonFactory;

@Beta
public class MockJsonFactory extends JsonFactory
{
    @Override
    public JsonParser createJsonParser(final InputStream a1) throws IOException {
        /*SL:45*/return new MockJsonParser(this);
    }
    
    @Override
    public JsonParser createJsonParser(final InputStream a1, final Charset a2) throws IOException {
        /*SL:50*/return new MockJsonParser(this);
    }
    
    @Override
    public JsonParser createJsonParser(final String a1) throws IOException {
        /*SL:55*/return new MockJsonParser(this);
    }
    
    @Override
    public JsonParser createJsonParser(final Reader a1) throws IOException {
        /*SL:60*/return new MockJsonParser(this);
    }
    
    @Override
    public JsonGenerator createJsonGenerator(final OutputStream a1, final Charset a2) throws IOException {
        /*SL:65*/return new MockJsonGenerator(this);
    }
    
    @Override
    public JsonGenerator createJsonGenerator(final Writer a1) throws IOException {
        /*SL:70*/return new MockJsonGenerator(this);
    }
}
