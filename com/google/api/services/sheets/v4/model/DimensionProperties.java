package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class DimensionProperties extends GenericJson
{
    @Key
    private List<DeveloperMetadata> developerMetadata;
    @Key
    private Boolean hiddenByFilter;
    @Key
    private Boolean hiddenByUser;
    @Key
    private Integer pixelSize;
    
    public List<DeveloperMetadata> getDeveloperMetadata() {
        /*SL:75*/return this.developerMetadata;
    }
    
    public DimensionProperties setDeveloperMetadata(final List<DeveloperMetadata> developerMetadata) {
        /*SL:83*/this.developerMetadata = developerMetadata;
        /*SL:84*/return this;
    }
    
    public Boolean getHiddenByFilter() {
        /*SL:92*/return this.hiddenByFilter;
    }
    
    public DimensionProperties setHiddenByFilter(final Boolean hiddenByFilter) {
        /*SL:100*/this.hiddenByFilter = hiddenByFilter;
        /*SL:101*/return this;
    }
    
    public Boolean getHiddenByUser() {
        /*SL:109*/return this.hiddenByUser;
    }
    
    public DimensionProperties setHiddenByUser(final Boolean hiddenByUser) {
        /*SL:117*/this.hiddenByUser = hiddenByUser;
        /*SL:118*/return this;
    }
    
    public Integer getPixelSize() {
        /*SL:126*/return this.pixelSize;
    }
    
    public DimensionProperties setPixelSize(final Integer pixelSize) {
        /*SL:134*/this.pixelSize = pixelSize;
        /*SL:135*/return this;
    }
    
    public DimensionProperties set(final String a1, final Object a2) {
        /*SL:140*/return (DimensionProperties)super.set(a1, a2);
    }
    
    public DimensionProperties clone() {
        /*SL:145*/return (DimensionProperties)super.clone();
    }
    
    static {
        Data.<Object>nullOf(DeveloperMetadata.class);
    }
}
