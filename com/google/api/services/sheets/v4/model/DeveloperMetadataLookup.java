package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeveloperMetadataLookup extends GenericJson
{
    @Key
    private String locationMatchingStrategy;
    @Key
    private String locationType;
    @Key
    private Integer metadataId;
    @Key
    private String metadataKey;
    @Key
    private DeveloperMetadataLocation metadataLocation;
    @Key
    private String metadataValue;
    @Key
    private String visibility;
    
    public String getLocationMatchingStrategy() {
        /*SL:118*/return this.locationMatchingStrategy;
    }
    
    public DeveloperMetadataLookup setLocationMatchingStrategy(final String locationMatchingStrategy) {
        /*SL:130*/this.locationMatchingStrategy = locationMatchingStrategy;
        /*SL:131*/return this;
    }
    
    public String getLocationType() {
        /*SL:146*/return this.locationType;
    }
    
    public DeveloperMetadataLookup setLocationType(final String locationType) {
        /*SL:161*/this.locationType = locationType;
        /*SL:162*/return this;
    }
    
    public Integer getMetadataId() {
        /*SL:171*/return this.metadataId;
    }
    
    public DeveloperMetadataLookup setMetadataId(final Integer metadataId) {
        /*SL:180*/this.metadataId = metadataId;
        /*SL:181*/return this;
    }
    
    public String getMetadataKey() {
        /*SL:190*/return this.metadataKey;
    }
    
    public DeveloperMetadataLookup setMetadataKey(final String metadataKey) {
        /*SL:199*/this.metadataKey = metadataKey;
        /*SL:200*/return this;
    }
    
    public DeveloperMetadataLocation getMetadataLocation() {
        /*SL:210*/return this.metadataLocation;
    }
    
    public DeveloperMetadataLookup setMetadataLocation(final DeveloperMetadataLocation metadataLocation) {
        /*SL:220*/this.metadataLocation = metadataLocation;
        /*SL:221*/return this;
    }
    
    public String getMetadataValue() {
        /*SL:230*/return this.metadataValue;
    }
    
    public DeveloperMetadataLookup setMetadataValue(final String metadataValue) {
        /*SL:239*/this.metadataValue = metadataValue;
        /*SL:240*/return this;
    }
    
    public String getVisibility() {
        /*SL:250*/return this.visibility;
    }
    
    public DeveloperMetadataLookup setVisibility(final String visibility) {
        /*SL:260*/this.visibility = visibility;
        /*SL:261*/return this;
    }
    
    public DeveloperMetadataLookup set(final String a1, final Object a2) {
        /*SL:266*/return (DeveloperMetadataLookup)super.set(a1, a2);
    }
    
    public DeveloperMetadataLookup clone() {
        /*SL:271*/return (DeveloperMetadataLookup)super.clone();
    }
}
