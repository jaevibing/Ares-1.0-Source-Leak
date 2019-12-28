package com.google.api.client.googleapis.services.json;

import java.io.IOException;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;

public class CommonGoogleJsonClientRequestInitializer extends CommonGoogleClientRequestInitializer
{
    public CommonGoogleJsonClientRequestInitializer() {
    }
    
    public CommonGoogleJsonClientRequestInitializer(final String a1) {
        super(a1);
    }
    
    public CommonGoogleJsonClientRequestInitializer(final String a1, final String a2) {
        super(a1, a2);
    }
    
    public final void initialize(final AbstractGoogleClientRequest<?> a1) throws IOException {
        /*SL:107*/super.initialize(a1);
        /*SL:108*/this.initializeJsonRequest((AbstractGoogleJsonClientRequest)a1);
    }
    
    protected void initializeJsonRequest(final AbstractGoogleJsonClientRequest<?> a1) throws IOException {
    }
}
