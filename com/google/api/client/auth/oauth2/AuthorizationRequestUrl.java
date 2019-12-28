package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import com.google.api.client.util.Key;
import com.google.api.client.http.GenericUrl;

public class AuthorizationRequestUrl extends GenericUrl
{
    @Key("response_type")
    private String responseTypes;
    @Key("redirect_uri")
    private String redirectUri;
    @Key("scope")
    private String scopes;
    @Key("client_id")
    private String clientId;
    @Key
    private String state;
    
    public AuthorizationRequestUrl(final String a1, final String a2, final Collection<String> a3) {
        super(a1);
        Preconditions.checkArgument(this.getFragment() == null);
        this.setClientId(a2);
        this.setResponseTypes(a3);
    }
    
    public final String getResponseTypes() {
        /*SL:111*/return this.responseTypes;
    }
    
    public AuthorizationRequestUrl setResponseTypes(final Collection<String> a1) {
        /*SL:127*/this.responseTypes = Joiner.on(' ').join(a1);
        /*SL:128*/return this;
    }
    
    public final String getRedirectUri() {
        /*SL:138*/return this.redirectUri;
    }
    
    public AuthorizationRequestUrl setRedirectUri(final String a1) {
        /*SL:153*/this.redirectUri = a1;
        /*SL:154*/return this;
    }
    
    public final String getScopes() {
        /*SL:163*/return this.scopes;
    }
    
    public AuthorizationRequestUrl setScopes(final Collection<String> a1) {
        /*SL:182*/this.scopes = ((a1 == null || !a1.iterator().hasNext()) ? null : Joiner.on(' ').join(a1));
        /*SL:183*/return this;
    }
    
    public final String getClientId() {
        /*SL:188*/return this.clientId;
    }
    
    public AuthorizationRequestUrl setClientId(final String a1) {
        /*SL:200*/this.clientId = Preconditions.<String>checkNotNull(a1);
        /*SL:201*/return this;
    }
    
    public final String getState() {
        /*SL:211*/return this.state;
    }
    
    public AuthorizationRequestUrl setState(final String a1) {
        /*SL:226*/this.state = a1;
        /*SL:227*/return this;
    }
    
    @Override
    public AuthorizationRequestUrl set(final String a1, final Object a2) {
        /*SL:232*/return (AuthorizationRequestUrl)super.set(a1, a2);
    }
    
    @Override
    public AuthorizationRequestUrl clone() {
        /*SL:237*/return (AuthorizationRequestUrl)super.clone();
    }
}
