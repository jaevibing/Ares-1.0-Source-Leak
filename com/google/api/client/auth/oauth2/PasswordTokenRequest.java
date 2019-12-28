package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpExecuteInterceptor;
import java.util.Collection;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Key;

public class PasswordTokenRequest extends TokenRequest
{
    @Key
    private String username;
    @Key
    private String password;
    
    public PasswordTokenRequest(final HttpTransport a1, final JsonFactory a2, final GenericUrl a3, final String a4, final String a5) {
        super(a1, a2, a3, "password");
        this.setUsername(a4);
        this.setPassword(a5);
    }
    
    @Override
    public PasswordTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:108*/return (PasswordTokenRequest)super.setRequestInitializer(a1);
    }
    
    @Override
    public PasswordTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:113*/return (PasswordTokenRequest)super.setTokenServerUrl(a1);
    }
    
    @Override
    public PasswordTokenRequest setScopes(final Collection<String> a1) {
        /*SL:118*/return (PasswordTokenRequest)super.setScopes(a1);
    }
    
    @Override
    public PasswordTokenRequest setGrantType(final String a1) {
        /*SL:123*/return (PasswordTokenRequest)super.setGrantType(a1);
    }
    
    @Override
    public PasswordTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:128*/return (PasswordTokenRequest)super.setClientAuthentication(a1);
    }
    
    public final String getUsername() {
        /*SL:133*/return this.username;
    }
    
    public PasswordTokenRequest setUsername(final String a1) {
        /*SL:145*/this.username = Preconditions.<String>checkNotNull(a1);
        /*SL:146*/return this;
    }
    
    public final String getPassword() {
        /*SL:151*/return this.password;
    }
    
    public PasswordTokenRequest setPassword(final String a1) {
        /*SL:163*/this.password = Preconditions.<String>checkNotNull(a1);
        /*SL:164*/return this;
    }
    
    @Override
    public PasswordTokenRequest set(final String a1, final Object a2) {
        /*SL:169*/return (PasswordTokenRequest)super.set(a1, a2);
    }
}
