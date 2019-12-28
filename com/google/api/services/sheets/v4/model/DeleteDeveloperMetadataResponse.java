package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class DeleteDeveloperMetadataResponse extends GenericJson
{
    @Key
    private List<DeveloperMetadata> deletedDeveloperMetadata;
    
    public List<DeveloperMetadata> getDeletedDeveloperMetadata() {
        /*SL:48*/return this.deletedDeveloperMetadata;
    }
    
    public DeleteDeveloperMetadataResponse setDeletedDeveloperMetadata(final List<DeveloperMetadata> deletedDeveloperMetadata) {
        /*SL:56*/this.deletedDeveloperMetadata = deletedDeveloperMetadata;
        /*SL:57*/return this;
    }
    
    public DeleteDeveloperMetadataResponse set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteDeveloperMetadataResponse)super.set(a1, a2);
    }
    
    public DeleteDeveloperMetadataResponse clone() {
        /*SL:67*/return (DeleteDeveloperMetadataResponse)super.clone();
    }
}
