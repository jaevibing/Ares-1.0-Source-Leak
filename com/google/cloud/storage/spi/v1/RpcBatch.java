package com.google.cloud.storage.spi.v1;

import com.google.api.client.googleapis.json.GoogleJsonError;
import java.util.Map;
import com.google.api.services.storage.model.StorageObject;

public interface RpcBatch
{
    void addDelete(StorageObject p0, Callback<Void> p1, Map<StorageRpc.Option, ?> p2);
    
    void addPatch(StorageObject p0, Callback<StorageObject> p1, Map<StorageRpc.Option, ?> p2);
    
    void addGet(StorageObject p0, Callback<StorageObject> p1, Map<StorageRpc.Option, ?> p2);
    
    void submit();
    
    public interface Callback<T>
    {
        void onSuccess(T p0);
        
        void onFailure(GoogleJsonError p0);
    }
}
