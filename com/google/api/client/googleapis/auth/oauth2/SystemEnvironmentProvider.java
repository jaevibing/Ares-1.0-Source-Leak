package com.google.api.client.googleapis.auth.oauth2;

class SystemEnvironmentProvider
{
    static final SystemEnvironmentProvider INSTANCE;
    
    String getEnv(final String a1) {
        /*SL:26*/return System.getenv(a1);
    }
    
    static {
        INSTANCE = new SystemEnvironmentProvider();
    }
}
