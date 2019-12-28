package com.google.api.client.json;

import com.google.api.client.util.Sets;
import com.google.api.client.util.Preconditions;
import java.util.Collections;
import java.io.Reader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.google.api.client.util.ObjectParser;

public class JsonObjectParser implements ObjectParser
{
    private final JsonFactory jsonFactory;
    private final Set<String> wrapperKeys;
    
    public JsonObjectParser(final JsonFactory a1) {
        this(new Builder(a1));
    }
    
    protected JsonObjectParser(final Builder a1) {
        this.jsonFactory = a1.jsonFactory;
        this.wrapperKeys = new HashSet<String>(a1.wrapperKeys);
    }
    
    @Override
    public <T> T parseAndClose(final InputStream a1, final Charset a2, final Class<T> a3) throws IOException {
        /*SL:81*/return (T)this.parseAndClose(a1, a2, (Type)a3);
    }
    
    @Override
    public Object parseAndClose(final InputStream a1, final Charset a2, final Type a3) throws IOException {
        final JsonParser v1 = /*EL:85*/this.jsonFactory.createJsonParser(a1, a2);
        /*SL:86*/this.initializeParser(v1);
        /*SL:87*/return v1.parse(a3, true);
    }
    
    @Override
    public <T> T parseAndClose(final Reader a1, final Class<T> a2) throws IOException {
        /*SL:92*/return (T)this.parseAndClose(a1, (Type)a2);
    }
    
    @Override
    public Object parseAndClose(final Reader a1, final Type a2) throws IOException {
        final JsonParser v1 = /*EL:96*/this.jsonFactory.createJsonParser(a1);
        /*SL:97*/this.initializeParser(v1);
        /*SL:98*/return v1.parse(a2, true);
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:103*/return this.jsonFactory;
    }
    
    public Set<String> getWrapperKeys() {
        /*SL:112*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.wrapperKeys);
    }
    
    private void initializeParser(final JsonParser v2) throws IOException {
        /*SL:121*/if (this.wrapperKeys.isEmpty()) {
            /*SL:122*/return;
        }
        boolean v3 = /*EL:124*/true;
        try {
            final String a1 = /*EL:126*/v2.skipToKey(this.wrapperKeys);
            /*SL:127*/Preconditions.checkArgument(a1 != null && v2.getCurrentToken() != JsonToken.END_OBJECT, "wrapper key(s) not found: %s", this.wrapperKeys);
            /*SL:129*/v3 = false;
        }
        finally {
            /*SL:131*/if (v3) {
                /*SL:132*/v2.close();
            }
        }
    }
    
    public static class Builder
    {
        final JsonFactory jsonFactory;
        Collection<String> wrapperKeys;
        
        public Builder(final JsonFactory a1) {
            this.wrapperKeys = (Collection<String>)Sets.<Object>newHashSet();
            this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a1);
        }
        
        public JsonObjectParser build() {
            /*SL:163*/return new JsonObjectParser(this);
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:168*/return this.jsonFactory;
        }
        
        public final Collection<String> getWrapperKeys() {
            /*SL:173*/return this.wrapperKeys;
        }
        
        public Builder setWrapperKeys(final Collection<String> a1) {
            /*SL:185*/this.wrapperKeys = a1;
            /*SL:186*/return this;
        }
    }
}
