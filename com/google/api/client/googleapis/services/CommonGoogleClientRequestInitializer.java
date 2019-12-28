package com.google.api.client.googleapis.services;

import java.io.IOException;

public class CommonGoogleClientRequestInitializer implements GoogleClientRequestInitializer
{
    private final String key;
    private final String userIp;
    
    public CommonGoogleClientRequestInitializer() {
        this(null);
    }
    
    public CommonGoogleClientRequestInitializer(final String a1) {
        this(a1, null);
    }
    
    public CommonGoogleClientRequestInitializer(final String a1, final String a2) {
        this.key = a1;
        this.userIp = a2;
    }
    
    public void initialize(final AbstractGoogleClientRequest<?> a1) throws IOException {
        /*SL:116*/if (this.key != null) {
            /*SL:117*/a1.put("key", this.key);
        }
        /*SL:119*/if (this.userIp != null) {
            /*SL:120*/a1.put("userIp", this.userIp);
        }
    }
    
    public final String getKey() {
        /*SL:126*/return this.key;
    }
    
    public final String getUserIp() {
        /*SL:131*/return this.userIp;
    }
}
