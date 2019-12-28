package com.google.api.client.util.store;

import java.io.IOException;
import com.google.api.client.util.Preconditions;

public abstract class AbstractDataStore<V extends java.lang.Object> implements DataStore<V>
{
    private final DataStoreFactory dataStoreFactory;
    private final String id;
    
    protected AbstractDataStore(final DataStoreFactory a1, final String a2) {
        this.dataStoreFactory = Preconditions.<DataStoreFactory>checkNotNull(a1);
        this.id = Preconditions.<String>checkNotNull(a2);
    }
    
    @Override
    public DataStoreFactory getDataStoreFactory() {
        /*SL:58*/return this.dataStoreFactory;
    }
    
    @Override
    public final String getId() {
        /*SL:62*/return this.id;
    }
    
    @Override
    public boolean containsKey(final String a1) throws IOException {
        /*SL:73*/return this.get(a1) != null;
    }
    
    @Override
    public boolean containsValue(final V a1) throws IOException {
        /*SL:84*/return this.values().contains(a1);
    }
    
    @Override
    public boolean isEmpty() throws IOException {
        /*SL:95*/return this.size() == 0;
    }
    
    @Override
    public int size() throws IOException {
        /*SL:106*/return this.keySet().size();
    }
}
