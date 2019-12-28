package com.google.api.client.util.store;

import java.io.IOException;

public interface DataStoreFactory
{
     <V extends java.lang.Object> DataStore<V> getDataStore(String p0) throws IOException;
}
