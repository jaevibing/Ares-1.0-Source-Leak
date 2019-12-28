package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Objects;
import com.google.api.client.util.store.DataStore;
import java.io.IOException;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.Preconditions;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import com.google.api.client.util.Beta;
import java.io.Serializable;

@Beta
public final class StoredChannel implements Serializable
{
    public static final String DEFAULT_DATA_STORE_ID;
    private static final long serialVersionUID = 1L;
    private final Lock lock;
    private final UnparsedNotificationCallback notificationCallback;
    private String clientToken;
    private Long expiration;
    private final String id;
    private String topicId;
    
    public StoredChannel(final UnparsedNotificationCallback a1) {
        this(a1, NotificationUtils.randomUuidString());
    }
    
    public StoredChannel(final UnparsedNotificationCallback a1, final String a2) {
        this.lock = new ReentrantLock();
        this.notificationCallback = Preconditions.<UnparsedNotificationCallback>checkNotNull(a1);
        this.id = Preconditions.<String>checkNotNull(a2);
    }
    
    public StoredChannel store(final DataStoreFactory a1) throws IOException {
        /*SL:109*/return this.store(getDefaultDataStore(a1));
    }
    
    public StoredChannel store(final DataStore<StoredChannel> a1) throws IOException {
        /*SL:123*/this.lock.lock();
        try {
            /*SL:125*/a1.set(this.getId(), (Serializable)this);
            /*SL:128*/return this;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public UnparsedNotificationCallback getNotificationCallback() {
        /*SL:136*/this.lock.lock();
        try {
            /*SL:140*/return this.notificationCallback;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public String getClientToken() {
        /*SL:149*/this.lock.lock();
        try {
            /*SL:153*/return this.clientToken;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public StoredChannel setClientToken(final String a1) {
        /*SL:162*/this.lock.lock();
        try {
            /*SL:164*/this.clientToken = a1;
        }
        finally {
            /*SL:166*/this.lock.unlock();
        }
        /*SL:168*/return this;
    }
    
    public Long getExpiration() {
        /*SL:176*/this.lock.lock();
        try {
            /*SL:180*/return this.expiration;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public StoredChannel setExpiration(final Long a1) {
        /*SL:189*/this.lock.lock();
        try {
            /*SL:191*/this.expiration = a1;
        }
        finally {
            /*SL:193*/this.lock.unlock();
        }
        /*SL:195*/return this;
    }
    
    public String getId() {
        /*SL:200*/this.lock.lock();
        try {
            /*SL:204*/return this.id;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public String getTopicId() {
        /*SL:213*/this.lock.lock();
        try {
            /*SL:217*/return this.topicId;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public StoredChannel setTopicId(final String a1) {
        /*SL:226*/this.lock.lock();
        try {
            /*SL:228*/this.topicId = a1;
        }
        finally {
            /*SL:230*/this.lock.unlock();
        }
        /*SL:232*/return this;
    }
    
    public String toString() {
        /*SL:237*/return Objects.toStringHelper(StoredChannel.class).add("notificationCallback", this.getNotificationCallback()).add("clientToken", this.getClientToken()).add("expiration", this.getExpiration()).add("id", this.getId()).add("topicId", this.getTopicId()).toString();
    }
    
    public boolean equals(final Object a1) {
        /*SL:245*/if (this == a1) {
            /*SL:246*/return true;
        }
        /*SL:248*/if (!(a1 instanceof StoredChannel)) {
            /*SL:249*/return false;
        }
        final StoredChannel v1 = /*EL:251*/(StoredChannel)a1;
        /*SL:252*/return this.getId().equals(v1.getId());
    }
    
    public int hashCode() {
        /*SL:257*/return this.getId().hashCode();
    }
    
    public static DataStore<StoredChannel> getDefaultDataStore(final DataStoreFactory a1) throws IOException {
        /*SL:268*/return a1.<StoredChannel>getDataStore(StoredChannel.DEFAULT_DATA_STORE_ID);
    }
    
    static {
        DEFAULT_DATA_STORE_ID = StoredChannel.class.getSimpleName();
    }
}
