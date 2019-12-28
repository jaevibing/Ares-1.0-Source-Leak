package com.google.api.client.http.json;

import com.google.api.client.http.HttpMediaType;
import java.io.IOException;
import com.google.api.client.json.JsonGenerator;
import java.io.OutputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.AbstractHttpContent;

public class JsonHttpContent extends AbstractHttpContent
{
    private final Object data;
    private final JsonFactory jsonFactory;
    private String wrapperKey;
    
    public JsonHttpContent(final JsonFactory a1, final Object a2) {
        super("application/json; charset=UTF-8");
        this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a1);
        this.data = Preconditions.<Object>checkNotNull(a2);
    }
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        final JsonGenerator v1 = /*EL:73*/this.jsonFactory.createJsonGenerator(a1, this.getCharset());
        /*SL:74*/if (this.wrapperKey != null) {
            /*SL:75*/v1.writeStartObject();
            /*SL:76*/v1.writeFieldName(this.wrapperKey);
        }
        /*SL:78*/v1.serialize(this.data);
        /*SL:79*/if (this.wrapperKey != null) {
            /*SL:80*/v1.writeEndObject();
        }
        /*SL:82*/v1.flush();
    }
    
    @Override
    public JsonHttpContent setMediaType(final HttpMediaType a1) {
        /*SL:87*/super.setMediaType(a1);
        /*SL:88*/return this;
    }
    
    public final Object getData() {
        /*SL:97*/return this.data;
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:106*/return this.jsonFactory;
    }
    
    public final String getWrapperKey() {
        /*SL:115*/return this.wrapperKey;
    }
    
    public JsonHttpContent setWrapperKey(final String a1) {
        /*SL:129*/this.wrapperKey = a1;
        /*SL:130*/return this;
    }
}
