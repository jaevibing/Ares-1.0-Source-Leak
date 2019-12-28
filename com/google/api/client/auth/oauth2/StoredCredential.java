package com.google.api.client.auth.oauth2;

import java.io.IOException;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import java.util.Arrays;
import com.google.api.client.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import com.google.api.client.util.Beta;
import java.io.Serializable;

@Beta
public final class StoredCredential implements Serializable
{
    public static final String DEFAULT_DATA_STORE_ID;
    private static final long serialVersionUID = 1L;
    private final Lock lock;
    private String accessToken;
    private Long expirationTimeMilliseconds;
    private String refreshToken;
    
    public StoredCredential() {
        this.lock = new ReentrantLock();
    }
    
    public StoredCredential(final Credential a1) {
        this.lock = new ReentrantLock();
        this.setAccessToken(a1.getAccessToken());
        this.setRefreshToken(a1.getRefreshToken());
        this.setExpirationTimeMilliseconds(a1.getExpirationTimeMilliseconds());
    }
    
    public String getAccessToken() {
        /*SL:73*/this.lock.lock();
        try {
            /*SL:75*/return this.accessToken;
        }
        finally {
            /*SL:77*/this.lock.unlock();
        }
    }
    
    public StoredCredential setAccessToken(final String a1) {
        /*SL:83*/this.lock.lock();
        try {
            /*SL:85*/this.accessToken = a1;
        }
        finally {
            /*SL:87*/this.lock.unlock();
        }
        /*SL:89*/return this;
    }
    
    public Long getExpirationTimeMilliseconds() {
        /*SL:94*/this.lock.lock();
        try {
            /*SL:96*/return this.expirationTimeMilliseconds;
        }
        finally {
            /*SL:98*/this.lock.unlock();
        }
    }
    
    public StoredCredential setExpirationTimeMilliseconds(final Long a1) {
        /*SL:104*/this.lock.lock();
        try {
            /*SL:106*/this.expirationTimeMilliseconds = a1;
        }
        finally {
            /*SL:108*/this.lock.unlock();
        }
        /*SL:110*/return this;
    }
    
    public String getRefreshToken() {
        /*SL:115*/this.lock.lock();
        try {
            /*SL:117*/return this.refreshToken;
        }
        finally {
            /*SL:119*/this.lock.unlock();
        }
    }
    
    public StoredCredential setRefreshToken(final String a1) {
        /*SL:125*/this.lock.lock();
        try {
            /*SL:127*/this.refreshToken = a1;
        }
        finally {
            /*SL:129*/this.lock.unlock();
        }
        /*SL:131*/return this;
    }
    
    @Override
    public String toString() {
        /*SL:136*/return Objects.toStringHelper(StoredCredential.class).add("accessToken", this.getAccessToken()).add(/*EL:137*/"refreshToken", this.getRefreshToken()).add(/*EL:138*/"expirationTimeMilliseconds", this.getExpirationTimeMilliseconds()).toString();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:145*/if (this == a1) {
            /*SL:146*/return true;
        }
        /*SL:148*/if (!(a1 instanceof StoredCredential)) {
            /*SL:149*/return false;
        }
        final StoredCredential v1 = /*EL:151*/(StoredCredential)a1;
        /*SL:152*/return Objects.equal(this.getAccessToken(), v1.getAccessToken()) && /*EL:153*/Objects.equal(this.getRefreshToken(), v1.getRefreshToken()) && /*EL:154*/Objects.equal(this.getExpirationTimeMilliseconds(), v1.getExpirationTimeMilliseconds());
    }
    
    @Override
    public int hashCode() {
        /*SL:159*/return Arrays.hashCode(new Object[] { this.getAccessToken(), /*EL:160*/this.getRefreshToken(), this.getExpirationTimeMilliseconds() });
    }
    
    public static DataStore<StoredCredential> getDefaultDataStore(final DataStoreFactory a1) throws IOException {
        /*SL:171*/return a1.<StoredCredential>getDataStore(StoredCredential.DEFAULT_DATA_STORE_ID);
    }
    
    static {
        DEFAULT_DATA_STORE_ID = StoredCredential.class.getSimpleName();
    }
}
