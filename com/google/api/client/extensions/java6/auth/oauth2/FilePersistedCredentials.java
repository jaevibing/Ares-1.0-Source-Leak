package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.util.GenericData;
import java.io.IOException;
import java.util.Iterator;
import java.io.Serializable;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.Preconditions;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Maps;
import com.google.api.client.util.Key;
import java.util.Map;
import com.google.api.client.util.Beta;
import com.google.api.client.json.GenericJson;

@Deprecated
@Beta
public class FilePersistedCredentials extends GenericJson
{
    @Key
    private Map<String, FilePersistedCredential> credentials;
    
    public FilePersistedCredentials() {
        this.credentials = (Map<String, FilePersistedCredential>)Maps.<Object, Object>newHashMap();
    }
    
    void store(final String a1, final Credential a2) {
        /*SL:55*/Preconditions.<String>checkNotNull(a1);
        FilePersistedCredential v1 = /*EL:56*/this.credentials.get(a1);
        /*SL:57*/if (v1 == null) {
            /*SL:58*/v1 = new FilePersistedCredential();
            /*SL:59*/this.credentials.put(a1, v1);
        }
        /*SL:61*/v1.store(a2);
    }
    
    boolean load(final String a1, final Credential a2) {
        /*SL:72*/Preconditions.<String>checkNotNull(a1);
        final FilePersistedCredential v1 = /*EL:73*/this.credentials.get(a1);
        /*SL:74*/if (v1 == null) {
            /*SL:75*/return false;
        }
        /*SL:77*/v1.load(a2);
        /*SL:78*/return true;
    }
    
    void delete(final String a1) {
        /*SL:82*/Preconditions.<String>checkNotNull(a1);
        /*SL:83*/this.credentials.remove(a1);
    }
    
    public FilePersistedCredentials set(final String a1, final Object a2) {
        /*SL:88*/return (FilePersistedCredentials)super.set(a1, a2);
    }
    
    public FilePersistedCredentials clone() {
        /*SL:93*/return (FilePersistedCredentials)super.clone();
    }
    
    void migrateTo(final DataStore<StoredCredential> v0) throws IOException {
        /*SL:97*/for (final Map.Entry<String, FilePersistedCredential> a1 : this.credentials.entrySet()) {
            /*SL:98*/v0.set((String)a1.getKey(), (Serializable)a1.getValue().toStoredCredential());
        }
    }
}
