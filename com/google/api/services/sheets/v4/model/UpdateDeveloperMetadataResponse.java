package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class UpdateDeveloperMetadataResponse extends GenericJson
{
    @Key
    private List<DeveloperMetadata> developerMetadata;
    
    public List<DeveloperMetadata> getDeveloperMetadata() {
        /*SL:48*/return this.developerMetadata;
    }
    
    public UpdateDeveloperMetadataResponse setDeveloperMetadata(final List<DeveloperMetadata> developerMetadata) {
        /*SL:56*/this.developerMetadata = developerMetadata;
        /*SL:57*/return this;
    }
    
    public UpdateDeveloperMetadataResponse set(final String a1, final Object a2) {
        /*SL:62*/return (UpdateDeveloperMetadataResponse)super.set(a1, a2);
    }
    
    public UpdateDeveloperMetadataResponse clone() {
        /*SL:67*/return (UpdateDeveloperMetadataResponse)super.clone();
    }
}
