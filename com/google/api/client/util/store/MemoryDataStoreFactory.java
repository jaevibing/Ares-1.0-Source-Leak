package com.google.api.client.util.store;

import java.io.IOException;

public class MemoryDataStoreFactory extends AbstractDataStoreFactory
{
    @Override
    protected <V extends java.lang.Object> DataStore<V> createDataStore(final String a1) throws IOException {
        /*SL:36*/return new MemoryDataStore<V>(this, a1);
    }
    
    public static MemoryDataStoreFactory getDefaultInstance() {
        /*SL:41*/return InstanceHolder.INSTANCE;
    }
    
    static class InstanceHolder
    {
        static final MemoryDataStoreFactory INSTANCE;
        
        static {
            INSTANCE = new MemoryDataStoreFactory();
        }
    }
    
    static class MemoryDataStore<V extends java.lang.Object> extends AbstractMemoryDataStore<V>
    {
        MemoryDataStore(final MemoryDataStoreFactory a1, final String a2) {
            super(a1, a2);
        }
        
        @Override
        public MemoryDataStoreFactory getDataStoreFactory() {
            /*SL:57*/return (MemoryDataStoreFactory)super.getDataStoreFactory();
        }
    }
}
