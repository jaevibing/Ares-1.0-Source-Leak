package com.google.api.client.util.store;

import java.io.IOException;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Maps;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public abstract class AbstractDataStoreFactory implements DataStoreFactory
{
    private final Lock lock;
    private final Map<String, DataStore<? extends Serializable>> dataStoreMap;
    private static final Pattern ID_PATTERN;
    
    public AbstractDataStoreFactory() {
        this.lock = new ReentrantLock();
        this.dataStoreMap = (Map<String, DataStore<? extends Serializable>>)Maps.<Object, Object>newHashMap();
    }
    
    @Override
    public final <V extends java.lang.Object> DataStore<V> getDataStore(final String v2) throws IOException {
        /*SL:48*/Preconditions.checkArgument(AbstractDataStoreFactory.ID_PATTERN.matcher(v2).matches(), /*EL:49*/"%s does not match pattern %s", v2, AbstractDataStoreFactory.ID_PATTERN);
        /*SL:50*/this.lock.lock();
        try {
            DataStore<V> a1 = /*EL:53*/(DataStore<V>)this.dataStoreMap.get(v2);
            /*SL:54*/if (a1 == null) {
                /*SL:55*/a1 = (DataStore<V>)this.<Object>createDataStore(v2);
                /*SL:56*/this.dataStoreMap.put(v2, (DataStore<? extends Serializable>)a1);
            }
            /*SL:58*/return a1;
        }
        finally {
            /*SL:60*/this.lock.unlock();
        }
    }
    
    protected abstract <V extends java.lang.Object> DataStore<V> createDataStore(final String p0) throws IOException;
    
    static {
        ID_PATTERN = Pattern.compile("\\w{1,30}");
    }
}
