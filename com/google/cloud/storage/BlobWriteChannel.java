package com.google.cloud.storage;

import com.google.cloud.RestorableState;
import com.google.cloud.Restorable;
import com.google.cloud.WriteChannel;
import com.google.cloud.BaseWriteChannel.BaseState;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import java.util.concurrent.Callable;
import com.google.cloud.RetryHelper;
import java.util.concurrent.Executors;
import java.io.Serializable;
import com.google.cloud.ServiceOptions;
import java.net.URL;
import com.google.cloud.storage.spi.v1.StorageRpc;
import java.util.Map;
import com.google.cloud.BaseWriteChannel;

class BlobWriteChannel extends BaseWriteChannel<StorageOptions, BlobInfo>
{
    BlobWriteChannel(final StorageOptions a1, final BlobInfo a2, final Map<StorageRpc.Option, ?> a3) {
        this(a1, a2, open(a1, a2, a3));
    }
    
    BlobWriteChannel(final StorageOptions a1, final URL a2) {
        this(a1, open(a2, a1));
    }
    
    BlobWriteChannel(final StorageOptions a1, final BlobInfo a2, final String a3) {
        super((ServiceOptions)a1, (Serializable)a2, a3);
    }
    
    BlobWriteChannel(final StorageOptions a1, final String a2) {
        super((ServiceOptions)a1, (Serializable)null, a2);
    }
    
    protected void flushBuffer(final int v1, final boolean v2) {
        try {
            /*SL:53*/RetryHelper.runWithRetries(/*EL:54*/(Callable)Executors.callable(new Runnable() {
                @Override
                public void run() {
                    /*SL:58*/((StorageOptions)BlobWriteChannel.access$300(BlobWriteChannel.this)).getStorageRpcV1().write(/*EL:60*/BlobWriteChannel.access$000(BlobWriteChannel.this), BlobWriteChannel.access$100(BlobWriteChannel.this), 0, BlobWriteChannel.access$200(BlobWriteChannel.this), v1, v2);
                }
            }), ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:63*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:67*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    protected StateImpl.Builder stateBuilder() {
        /*SL:72*/return StateImpl.builder((StorageOptions)this.getOptions(), (BlobInfo)this.getEntity(), this.getUploadId());
    }
    
    private static String open(final StorageOptions a2, final BlobInfo a3, final Map<StorageRpc.Option, ?> v1) {
        try {
            /*SL:80*/return (String)RetryHelper.runWithRetries((Callable)new Callable<String>() {
                @Override
                public String call() {
                    /*SL:84*/return a2.getStorageRpcV1().open(a3.toPb(), v1);
                }
            }, a2.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:87*/a2.getClock());
        }
        catch (RetryHelper.RetryHelperException a4) {
            /*SL:91*/throw StorageException.translateAndThrow(a4);
        }
    }
    
    private static String open(final URL a2, final StorageOptions v1) {
        try {
            /*SL:97*/return (String)RetryHelper.runWithRetries((Callable)new Callable<String>() {
                @Override
                public String call() {
                    /*SL:101*/if (!isValidSignedURL(a2.getQuery())) {
                        /*SL:102*/throw new StorageException(2, "invalid signedURL");
                    }
                    /*SL:104*/return v1.getStorageRpcV1().open(a2.toString());
                }
            }, v1.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:107*/v1.getClock());
        }
        catch (RetryHelper.RetryHelperException a3) {
            /*SL:111*/throw StorageException.translateAndThrow(a3);
        }
    }
    
    private static boolean isValidSignedURL(final String a1) {
        boolean v1 = /*EL:116*/true;
        /*SL:117*/if (a1.startsWith("X-Goog-Algorithm=")) {
            /*SL:121*/if (!a1.contains("&X-Goog-Credential=") || !a1.contains("&X-Goog-Date=") || !a1.contains("&X-Goog-Expires=") || !a1.contains("&X-Goog-SignedHeaders=") || !a1.contains("&X-Goog-Signature=")) {
                /*SL:123*/v1 = false;
            }
        }
        else/*SL:125*/ if (a1.startsWith("GoogleAccessId=")) {
            /*SL:126*/if (!a1.contains("&Expires=") || !a1.contains("&Signature=")) {
                /*SL:127*/v1 = false;
            }
        }
        else {
            /*SL:130*/v1 = false;
        }
        /*SL:132*/return v1;
    }
    
    static /* synthetic */ String access$000(final BlobWriteChannel a1) {
        /*SL:32*/return a1.getUploadId();
    }
    
    static /* synthetic */ byte[] access$100(final BlobWriteChannel a1) {
        /*SL:32*/return a1.getBuffer();
    }
    
    static /* synthetic */ long access$200(final BlobWriteChannel a1) {
        /*SL:32*/return a1.getPosition();
    }
    
    static /* synthetic */ ServiceOptions access$300(final BlobWriteChannel a1) {
        /*SL:32*/return a1.getOptions();
    }
    
    static /* synthetic */ void access$600(final BlobWriteChannel a1, final BaseState a2) {
        /*SL:32*/a1.restore(a2);
    }
    
    static class StateImpl extends BaseState<StorageOptions, BlobInfo>
    {
        private static final long serialVersionUID = -9028324143780151286L;
        
        StateImpl(final Builder a1) {
            super((BaseState.Builder)a1);
        }
        
        static Builder builder(final StorageOptions a1, final BlobInfo a2, final String a3) {
            /*SL:156*/return new Builder(a1, a2, a3);
        }
        
        public WriteChannel restore() {
            final BlobWriteChannel v1 = /*EL:161*/new BlobWriteChannel((StorageOptions)this.serviceOptions, (BlobInfo)this.entity, this.uploadId);
            /*SL:162*/BlobWriteChannel.access$600(v1, this);
            /*SL:163*/return (WriteChannel)v1;
        }
        
        static class Builder extends BaseState.Builder<StorageOptions, BlobInfo>
        {
            private Builder(final StorageOptions a1, final BlobInfo a2, final String a3) {
                super((ServiceOptions)a1, (Serializable)a2, a3);
            }
            
            public RestorableState<WriteChannel> build() {
                /*SL:151*/return (RestorableState<WriteChannel>)new StateImpl(this);
            }
        }
    }
}
