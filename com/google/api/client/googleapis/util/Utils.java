package com.google.api.client.googleapis.util;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;

@Beta
public final class Utils
{
    public static JsonFactory getDefaultJsonFactory() {
        /*SL:36*/return JsonFactoryInstanceHolder.INSTANCE;
    }
    
    public static HttpTransport getDefaultTransport() {
        /*SL:51*/return TransportInstanceHolder.INSTANCE;
    }
    
    private static class JsonFactoryInstanceHolder
    {
        static final JsonFactory INSTANCE;
        
        static {
            INSTANCE = new JacksonFactory();
        }
    }
    
    private static class TransportInstanceHolder
    {
        static final HttpTransport INSTANCE;
        
        static {
            INSTANCE = new NetHttpTransport();
        }
    }
}
