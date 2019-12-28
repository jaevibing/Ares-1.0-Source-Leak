package com.google.cloud.storage;

import com.google.cloud.BaseServiceException;
import com.google.cloud.BatchResult;

public class StorageBatchResult<T> extends BatchResult<T, StorageException>
{
    protected void error(final StorageException a1) {
        /*SL:28*/super.error((BaseServiceException)a1);
    }
    
    protected void success(final T a1) {
        /*SL:33*/super.success((Object)a1);
    }
}
