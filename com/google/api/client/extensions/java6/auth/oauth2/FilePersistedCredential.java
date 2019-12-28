package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Key;
import com.google.api.client.util.Beta;
import com.google.api.client.json.GenericJson;

@Deprecated
@Beta
public class FilePersistedCredential extends GenericJson
{
    @Key("access_token")
    private String accessToken;
    @Key("refresh_token")
    private String refreshToken;
    @Key("expiration_time_millis")
    private Long expirationTimeMillis;
    
    void store(final Credential a1) {
        /*SL:56*/this.accessToken = a1.getAccessToken();
        /*SL:57*/this.refreshToken = a1.getRefreshToken();
        /*SL:58*/this.expirationTimeMillis = a1.getExpirationTimeMilliseconds();
    }
    
    void load(final Credential a1) {
        /*SL:68*/a1.setAccessToken(this.accessToken);
        /*SL:69*/a1.setRefreshToken(this.refreshToken);
        /*SL:70*/a1.setExpirationTimeMilliseconds(this.expirationTimeMillis);
    }
    
    public FilePersistedCredential set(final String a1, final Object a2) {
        /*SL:75*/return (FilePersistedCredential)super.set(a1, a2);
    }
    
    public FilePersistedCredential clone() {
        /*SL:80*/return (FilePersistedCredential)super.clone();
    }
    
    StoredCredential toStoredCredential() {
        /*SL:84*/return new StoredCredential().setAccessToken(this.accessToken).setRefreshToken(this.refreshToken).setExpirationTimeMilliseconds(this.expirationTimeMillis);
    }
}
