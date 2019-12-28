package com.google.api.client.util.store;

import java.util.Arrays;
import com.google.api.client.util.Preconditions;
import java.util.Iterator;
import java.util.List;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Lists;
import java.util.Collection;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import com.google.api.client.util.Maps;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class AbstractMemoryDataStore<V extends java.lang.Object> extends AbstractDataStore<V>
{
    private final Lock lock;
    protected HashMap<String, byte[]> keyValueMap;
    
    protected AbstractMemoryDataStore(final DataStoreFactory a1, final String a2) {
        super(a1, a2);
        this.lock = new ReentrantLock();
        this.keyValueMap = Maps.<String, byte[]>newHashMap();
    }
    
    @Override
    public final Set<String> keySet() throws IOException {
        /*SL:57*/this.lock.lock();
        try {
            /*SL:59*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.keyValueMap.keySet());
        }
        finally {
            /*SL:61*/this.lock.unlock();
        }
    }
    
    @Override
    public final Collection<V> values() throws IOException {
        /*SL:66*/this.lock.lock();
        try {
            final List<V> arrayList = /*EL:68*/(List<V>)Lists.<Object>newArrayList();
            /*SL:69*/for (final byte[] v1 : this.keyValueMap.values()) {
                /*SL:70*/arrayList.add((V)IOUtils.deserialize(v1));
            }
            /*SL:72*/return (Collection<V>)Collections.<Object>unmodifiableList((List<?>)arrayList);
        }
        finally {
            /*SL:74*/this.lock.unlock();
        }
    }
    
    @Override
    public final V get(final String a1) throws IOException {
        /*SL:79*/if (a1 == null) {
            /*SL:80*/return null;
        }
        /*SL:82*/this.lock.lock();
        try {
            /*SL:84*/return (V)IOUtils.deserialize((byte[])this.keyValueMap.get(a1));
        }
        finally {
            /*SL:86*/this.lock.unlock();
        }
    }
    
    @Override
    public final DataStore<V> set(final String a1, final V a2) throws IOException {
        /*SL:91*/Preconditions.<String>checkNotNull(a1);
        /*SL:92*/Preconditions.<V>checkNotNull(a2);
        /*SL:93*/this.lock.lock();
        try {
            /*SL:95*/this.keyValueMap.put(a1, IOUtils.serialize(a2));
            /*SL:96*/this.save();
        }
        finally {
            /*SL:98*/this.lock.unlock();
        }
        /*SL:100*/return this;
    }
    
    @Override
    public DataStore<V> delete(final String a1) throws IOException {
        /*SL:104*/if (a1 == null) {
            /*SL:105*/return this;
        }
        /*SL:107*/this.lock.lock();
        try {
            /*SL:109*/this.keyValueMap.remove(a1);
            /*SL:110*/this.save();
        }
        finally {
            /*SL:112*/this.lock.unlock();
        }
        /*SL:114*/return this;
    }
    
    @Override
    public final DataStore<V> clear() throws IOException {
        /*SL:118*/this.lock.lock();
        try {
            /*SL:120*/this.keyValueMap.clear();
            /*SL:121*/this.save();
        }
        finally {
            /*SL:123*/this.lock.unlock();
        }
        /*SL:125*/return this;
    }
    
    @Override
    public boolean containsKey(final String a1) throws IOException {
        /*SL:130*/if (a1 == null) {
            /*SL:131*/return false;
        }
        /*SL:133*/this.lock.lock();
        try {
            /*SL:135*/return this.keyValueMap.containsKey(a1);
        }
        finally {
            /*SL:137*/this.lock.unlock();
        }
    }
    
    @Override
    public boolean containsValue(final V v0) throws IOException {
        /*SL:143*/if (v0 == null) {
            /*SL:144*/return false;
        }
        /*SL:146*/this.lock.lock();
        try {
            final byte[] v = /*EL:148*/IOUtils.serialize(v0);
            /*SL:149*/for (final byte[] a1 : this.keyValueMap.values()) {
                /*SL:150*/if (Arrays.equals(v, a1)) {
                    /*SL:151*/return true;
                }
            }
            /*SL:154*/return false;
        }
        finally {
            /*SL:156*/this.lock.unlock();
        }
    }
    
    @Override
    public boolean isEmpty() throws IOException {
        /*SL:162*/this.lock.lock();
        try {
            /*SL:164*/return this.keyValueMap.isEmpty();
        }
        finally {
            /*SL:166*/this.lock.unlock();
        }
    }
    
    @Override
    public int size() throws IOException {
        /*SL:172*/this.lock.lock();
        try {
            /*SL:174*/return this.keyValueMap.size();
        }
        finally {
            /*SL:176*/this.lock.unlock();
        }
    }
    
    public void save() throws IOException {
    }
    
    @Override
    public String toString() {
        /*SL:190*/return DataStoreUtils.toString((DataStore<?>)this);
    }
}
