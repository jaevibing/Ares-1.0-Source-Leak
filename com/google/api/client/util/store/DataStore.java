package com.google.api.client.util.store;

import java.util.Collection;
import java.util.Set;
import java.io.IOException;

public interface DataStore<V extends java.lang.Object>
{
    DataStoreFactory getDataStoreFactory();
    
    String getId();
    
    int size() throws IOException;
    
    boolean isEmpty() throws IOException;
    
    boolean containsKey(String p0) throws IOException;
    
    boolean containsValue(V p0) throws IOException;
    
    Set<String> keySet() throws IOException;
    
    Collection<V> values() throws IOException;
    
    V get(String p0) throws IOException;
    
    DataStore<V> set(String p0, V p1) throws IOException;
    
    DataStore<V> clear() throws IOException;
    
    DataStore<V> delete(String p0) throws IOException;
}
