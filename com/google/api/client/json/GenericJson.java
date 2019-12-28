package com.google.api.client.json;

import java.io.IOException;
import com.google.api.client.util.Throwables;
import com.google.api.client.util.GenericData;

public class GenericJson extends GenericData implements Cloneable
{
    private JsonFactory jsonFactory;
    
    public final JsonFactory getFactory() {
        /*SL:51*/return this.jsonFactory;
    }
    
    public final void setFactory(final JsonFactory a1) {
        /*SL:60*/this.jsonFactory = a1;
    }
    
    @Override
    public String toString() {
        /*SL:65*/if (this.jsonFactory != null) {
            try {
                /*SL:67*/return this.jsonFactory.toString(this);
            }
            catch (IOException v1) {
                /*SL:69*/throw Throwables.propagate(v1);
            }
        }
        /*SL:72*/return super.toString();
    }
    
    public String toPrettyString() throws IOException {
        /*SL:82*/if (this.jsonFactory != null) {
            /*SL:83*/return this.jsonFactory.toPrettyString(this);
        }
        /*SL:85*/return super.toString();
    }
    
    @Override
    public GenericJson clone() {
        /*SL:90*/return (GenericJson)super.clone();
    }
    
    @Override
    public GenericJson set(final String a1, final Object a2) {
        /*SL:95*/return (GenericJson)super.set(a1, a2);
    }
}
