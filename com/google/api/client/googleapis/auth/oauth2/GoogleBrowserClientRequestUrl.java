package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import com.google.api.client.util.Key;
import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;

public class GoogleBrowserClientRequestUrl extends BrowserClientRequestUrl
{
    @Key("approval_prompt")
    private String approvalPrompt;
    
    public GoogleBrowserClientRequestUrl(final String a1, final String a2, final Collection<String> a3) {
        super("https://accounts.google.com/o/oauth2/auth", a1);
        this.setRedirectUri(a2);
        this.setScopes(a3);
    }
    
    public GoogleBrowserClientRequestUrl(final GoogleClientSecrets a1, final String a2, final Collection<String> a3) {
        this(a1.getDetails().getClientId(), a2, a3);
    }
    
    public final String getApprovalPrompt() {
        /*SL:100*/return this.approvalPrompt;
    }
    
    public GoogleBrowserClientRequestUrl setApprovalPrompt(final String a1) {
        /*SL:113*/this.approvalPrompt = a1;
        /*SL:114*/return this;
    }
    
    public GoogleBrowserClientRequestUrl setResponseTypes(final Collection<String> a1) {
        /*SL:119*/return (GoogleBrowserClientRequestUrl)super.setResponseTypes(a1);
    }
    
    public GoogleBrowserClientRequestUrl setRedirectUri(final String a1) {
        /*SL:124*/return (GoogleBrowserClientRequestUrl)super.setRedirectUri(a1);
    }
    
    public GoogleBrowserClientRequestUrl setScopes(final Collection<String> a1) {
        /*SL:129*/Preconditions.checkArgument(a1.iterator().hasNext());
        /*SL:130*/return (GoogleBrowserClientRequestUrl)super.setScopes(a1);
    }
    
    public GoogleBrowserClientRequestUrl setClientId(final String a1) {
        /*SL:135*/return (GoogleBrowserClientRequestUrl)super.setClientId(a1);
    }
    
    public GoogleBrowserClientRequestUrl setState(final String a1) {
        /*SL:140*/return (GoogleBrowserClientRequestUrl)super.setState(a1);
    }
    
    public GoogleBrowserClientRequestUrl set(final String a1, final Object a2) {
        /*SL:145*/return (GoogleBrowserClientRequestUrl)super.set(a1, a2);
    }
    
    public GoogleBrowserClientRequestUrl clone() {
        /*SL:150*/return (GoogleBrowserClientRequestUrl)super.clone();
    }
}
