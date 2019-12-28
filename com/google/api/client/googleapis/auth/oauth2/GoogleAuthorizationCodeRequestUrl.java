package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import com.google.api.client.util.Key;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

public class GoogleAuthorizationCodeRequestUrl extends AuthorizationCodeRequestUrl
{
    @Key("approval_prompt")
    private String approvalPrompt;
    @Key("access_type")
    private String accessType;
    
    public GoogleAuthorizationCodeRequestUrl(final String a1, final String a2, final Collection<String> a3) {
        this("https://accounts.google.com/o/oauth2/auth", a1, a2, a3);
    }
    
    public GoogleAuthorizationCodeRequestUrl(final String a1, final String a2, final String a3, final Collection<String> a4) {
        super(a1, a2);
        this.setRedirectUri(a3);
        this.setScopes(a4);
    }
    
    public GoogleAuthorizationCodeRequestUrl(final GoogleClientSecrets a1, final String a2, final Collection<String> a3) {
        this(a1.getDetails().getClientId(), a2, a3);
    }
    
    public final String getApprovalPrompt() {
        /*SL:126*/return this.approvalPrompt;
    }
    
    public GoogleAuthorizationCodeRequestUrl setApprovalPrompt(final String a1) {
        /*SL:139*/this.approvalPrompt = a1;
        /*SL:140*/return this;
    }
    
    public final String getAccessType() {
        /*SL:148*/return this.accessType;
    }
    
    public GoogleAuthorizationCodeRequestUrl setAccessType(final String a1) {
        /*SL:161*/this.accessType = a1;
        /*SL:162*/return this;
    }
    
    public GoogleAuthorizationCodeRequestUrl setResponseTypes(final Collection<String> a1) {
        /*SL:167*/return (GoogleAuthorizationCodeRequestUrl)super.setResponseTypes(a1);
    }
    
    public GoogleAuthorizationCodeRequestUrl setRedirectUri(final String a1) {
        /*SL:172*/Preconditions.<String>checkNotNull(a1);
        /*SL:173*/return (GoogleAuthorizationCodeRequestUrl)super.setRedirectUri(a1);
    }
    
    public GoogleAuthorizationCodeRequestUrl setScopes(final Collection<String> a1) {
        /*SL:178*/Preconditions.checkArgument(a1.iterator().hasNext());
        /*SL:179*/return (GoogleAuthorizationCodeRequestUrl)super.setScopes(a1);
    }
    
    public GoogleAuthorizationCodeRequestUrl setClientId(final String a1) {
        /*SL:184*/return (GoogleAuthorizationCodeRequestUrl)super.setClientId(a1);
    }
    
    public GoogleAuthorizationCodeRequestUrl setState(final String a1) {
        /*SL:189*/return (GoogleAuthorizationCodeRequestUrl)super.setState(a1);
    }
    
    public GoogleAuthorizationCodeRequestUrl set(final String a1, final Object a2) {
        /*SL:194*/return (GoogleAuthorizationCodeRequestUrl)super.set(a1, a2);
    }
    
    public GoogleAuthorizationCodeRequestUrl clone() {
        /*SL:199*/return (GoogleAuthorizationCodeRequestUrl)super.clone();
    }
}
