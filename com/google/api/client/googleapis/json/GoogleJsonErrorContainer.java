package com.google.api.client.googleapis.json;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public class GoogleJsonErrorContainer extends GenericJson
{
    @Key
    private GoogleJsonError error;
    
    public final GoogleJsonError getError() {
        /*SL:33*/return this.error;
    }
    
    public final void setError(final GoogleJsonError a1) {
        /*SL:38*/this.error = a1;
    }
    
    public GoogleJsonErrorContainer set(final String a1, final Object a2) {
        /*SL:43*/return (GoogleJsonErrorContainer)super.set(a1, a2);
    }
    
    public GoogleJsonErrorContainer clone() {
        /*SL:48*/return (GoogleJsonErrorContainer)super.clone();
    }
}
