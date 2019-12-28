package com.google.cloud.storage;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.storage.model.StorageObject;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.cloud.storage.spi.v1.RpcBatch;

public class StorageBatch
{
    private final RpcBatch batch;
    private final StorageRpc storageRpc;
    private final StorageOptions options;
    
    StorageBatch(final StorageOptions a1) {
        this.options = a1;
        this.storageRpc = a1.getStorageRpcV1();
        this.batch = this.storageRpc.createBatch();
    }
    
    @VisibleForTesting
    Object getBatch() {
        /*SL:69*/return this.batch;
    }
    
    @VisibleForTesting
    StorageRpc getStorageRpc() {
        /*SL:74*/return this.storageRpc;
    }
    
    @VisibleForTesting
    StorageOptions getOptions() {
        /*SL:79*/return this.options;
    }
    
    public StorageBatchResult<Boolean> delete(final String a1, final String a2, final Storage.BlobSourceOption... a3) {
        /*SL:90*/return this.delete(BlobId.of(a1, a2), a3);
    }
    
    public StorageBatchResult<Boolean> delete(final BlobId a1, final Storage.BlobSourceOption... a2) {
        final StorageBatchResult<Boolean> v1 = /*EL:100*/new StorageBatchResult<Boolean>();
        final RpcBatch.Callback<Void> v2 = /*EL:101*/this.createDeleteCallback(v1);
        final Map<StorageRpc.Option, ?> v3 = /*EL:102*/StorageImpl.optionMap(a1, (Option[])a2);
        /*SL:103*/this.batch.addDelete(a1.toPb(), v2, v3);
        /*SL:104*/return v1;
    }
    
    public StorageBatchResult<Blob> update(final BlobInfo a1, final Storage.BlobTargetOption... a2) {
        final StorageBatchResult<Blob> v1 = /*EL:114*/new StorageBatchResult<Blob>();
        final RpcBatch.Callback<StorageObject> v2 = /*EL:115*/this.createUpdateCallback(this.options, v1);
        final Map<StorageRpc.Option, ?> v3 = /*EL:116*/StorageImpl.optionMap(a1, (Option[])a2);
        /*SL:117*/this.batch.addPatch(a1.toPb(), v2, v3);
        /*SL:118*/return v1;
    }
    
    public StorageBatchResult<Blob> get(final String a1, final String a2, final Storage.BlobGetOption... a3) {
        /*SL:129*/return this.get(BlobId.of(a1, a2), a3);
    }
    
    public StorageBatchResult<Blob> get(final BlobId a1, final Storage.BlobGetOption... a2) {
        final StorageBatchResult<Blob> v1 = /*EL:140*/new StorageBatchResult<Blob>();
        final RpcBatch.Callback<StorageObject> v2 = /*EL:141*/this.createGetCallback(this.options, v1);
        final Map<StorageRpc.Option, ?> v3 = /*EL:142*/StorageImpl.optionMap(a1, (Option[])a2);
        /*SL:143*/this.batch.addGet(a1.toPb(), v2, v3);
        /*SL:144*/return v1;
    }
    
    public void submit() {
        /*SL:149*/this.batch.submit();
    }
    
    private RpcBatch.Callback<Void> createDeleteCallback(final StorageBatchResult<Boolean> a1) {
        /*SL:153*/return new RpcBatch.Callback<Void>() {
            @Override
            public void onSuccess(final Void a1) {
                /*SL:156*/a1.success(true);
            }
            
            @Override
            public void onFailure(final GoogleJsonError a1) {
                final StorageException v1 = /*EL:161*/new StorageException(a1);
                /*SL:162*/if (v1.getCode() == 404) {
                    /*SL:163*/a1.success(false);
                }
                else {
                    /*SL:165*/a1.error(v1);
                }
            }
        };
    }
    
    private RpcBatch.Callback<StorageObject> createGetCallback(final StorageOptions a1, final StorageBatchResult<Blob> a2) {
        /*SL:173*/return new RpcBatch.Callback<StorageObject>() {
            @Override
            public void onSuccess(final StorageObject a1) {
                /*SL:176*/a2.success((a1 == null) ? null : /*EL:177*/Blob.fromPb((Storage)a1.getService(), a1));
            }
            
            @Override
            public void onFailure(final GoogleJsonError a1) {
                final StorageException v1 = /*EL:182*/new StorageException(a1);
                /*SL:183*/if (v1.getCode() == 404) {
                    /*SL:184*/a2.success(null);
                }
                else {
                    /*SL:186*/a2.error(v1);
                }
            }
        };
    }
    
    private RpcBatch.Callback<StorageObject> createUpdateCallback(final StorageOptions a1, final StorageBatchResult<Blob> a2) {
        /*SL:194*/return new RpcBatch.Callback<StorageObject>() {
            @Override
            public void onSuccess(final StorageObject a1) {
                /*SL:197*/a2.success((a1 == null) ? null : /*EL:198*/Blob.fromPb((Storage)a1.getService(), a1));
            }
            
            @Override
            public void onFailure(final GoogleJsonError a1) {
                /*SL:203*/a2.error(new StorageException(a1));
            }
        };
    }
}
