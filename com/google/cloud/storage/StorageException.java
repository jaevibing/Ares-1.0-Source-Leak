package com.google.cloud.storage;

import com.google.common.collect.ImmutableSet;
import com.google.cloud.RetryHelper;
import com.google.api.client.googleapis.json.GoogleJsonError;
import java.io.IOException;
import com.google.cloud.BaseServiceException;
import java.util.Set;
import com.google.api.core.InternalApi;
import com.google.cloud.http.BaseHttpServiceException;

@InternalApi
public final class StorageException extends BaseHttpServiceException
{
    private static final Set<BaseServiceException.Error> RETRYABLE_ERRORS;
    private static final long serialVersionUID = -4168430271327813063L;
    
    public StorageException(final int a1, final String a2) {
        this(a1, a2, null);
    }
    
    public StorageException(final int a1, final String a2, final Throwable a3) {
        super(a1, a2, (String)null, true, (Set)StorageException.RETRYABLE_ERRORS, a3);
    }
    
    public StorageException(final IOException a1) {
        super(a1, true, (Set)StorageException.RETRYABLE_ERRORS);
    }
    
    public StorageException(final GoogleJsonError a1) {
        super(a1, true, (Set)StorageException.RETRYABLE_ERRORS);
    }
    
    public static StorageException translateAndThrow(final RetryHelper.RetryHelperException a1) {
        /*SL:73*/BaseServiceException.translate(a1);
        /*SL:74*/throw new StorageException(0, a1.getMessage(), a1.getCause());
    }
    
    static {
        RETRYABLE_ERRORS = ImmutableSet.<BaseServiceException.Error>of(new BaseServiceException.Error(504, (String)null), new BaseServiceException.Error(503, (String)null), new BaseServiceException.Error(502, (String)null), new BaseServiceException.Error(500, (String)null), new BaseServiceException.Error(429, (String)null), new BaseServiceException.Error(408, (String)null), new BaseServiceException.Error((Integer)null, "internalError"));
    }
}
