package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CreateDeveloperMetadataResponse extends GenericJson
{
    @Key
    private DeveloperMetadata developerMetadata;
    
    public DeveloperMetadata getDeveloperMetadata() {
        /*SL:48*/return this.developerMetadata;
    }
    
    public CreateDeveloperMetadataResponse setDeveloperMetadata(final DeveloperMetadata developerMetadata) {
        /*SL:56*/this.developerMetadata = developerMetadata;
        /*SL:57*/return this;
    }
    
    public CreateDeveloperMetadataResponse set(final String a1, final Object a2) {
        /*SL:62*/return (CreateDeveloperMetadataResponse)super.set(a1, a2);
    }
    
    public CreateDeveloperMetadataResponse clone() {
        /*SL:67*/return (CreateDeveloperMetadataResponse)super.clone();
    }
}
