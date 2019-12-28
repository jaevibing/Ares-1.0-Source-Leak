package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CreateDeveloperMetadataRequest extends GenericJson
{
    @Key
    private DeveloperMetadata developerMetadata;
    
    public DeveloperMetadata getDeveloperMetadata() {
        /*SL:48*/return this.developerMetadata;
    }
    
    public CreateDeveloperMetadataRequest setDeveloperMetadata(final DeveloperMetadata developerMetadata) {
        /*SL:56*/this.developerMetadata = developerMetadata;
        /*SL:57*/return this;
    }
    
    public CreateDeveloperMetadataRequest set(final String a1, final Object a2) {
        /*SL:62*/return (CreateDeveloperMetadataRequest)super.set(a1, a2);
    }
    
    public CreateDeveloperMetadataRequest clone() {
        /*SL:67*/return (CreateDeveloperMetadataRequest)super.clone();
    }
}
