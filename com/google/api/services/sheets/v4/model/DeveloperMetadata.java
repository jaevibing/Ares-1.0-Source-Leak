package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeveloperMetadata extends GenericJson
{
    @Key
    private DeveloperMetadataLocation location;
    @Key
    private Integer metadataId;
    @Key
    private String metadataKey;
    @Key
    private String metadataValue;
    @Key
    private String visibility;
    
    public DeveloperMetadataLocation getLocation() {
        /*SL:83*/return this.location;
    }
    
    public DeveloperMetadata setLocation(final DeveloperMetadataLocation location) {
        /*SL:91*/this.location = location;
        /*SL:92*/return this;
    }
    
    public Integer getMetadataId() {
        /*SL:101*/return this.metadataId;
    }
    
    public DeveloperMetadata setMetadataId(final Integer metadataId) {
        /*SL:110*/this.metadataId = metadataId;
        /*SL:111*/return this;
    }
    
    public String getMetadataKey() {
        /*SL:120*/return this.metadataKey;
    }
    
    public DeveloperMetadata setMetadataKey(final String metadataKey) {
        /*SL:129*/this.metadataKey = metadataKey;
        /*SL:130*/return this;
    }
    
    public String getMetadataValue() {
        /*SL:138*/return this.metadataValue;
    }
    
    public DeveloperMetadata setMetadataValue(final String metadataValue) {
        /*SL:146*/this.metadataValue = metadataValue;
        /*SL:147*/return this;
    }
    
    public String getVisibility() {
        /*SL:155*/return this.visibility;
    }
    
    public DeveloperMetadata setVisibility(final String visibility) {
        /*SL:163*/this.visibility = visibility;
        /*SL:164*/return this;
    }
    
    public DeveloperMetadata set(final String a1, final Object a2) {
        /*SL:169*/return (DeveloperMetadata)super.set(a1, a2);
    }
    
    public DeveloperMetadata clone() {
        /*SL:174*/return (DeveloperMetadata)super.clone();
    }
}
