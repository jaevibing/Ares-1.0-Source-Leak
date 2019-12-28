package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class UpdateDeveloperMetadataRequest extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    @Key
    private DeveloperMetadata developerMetadata;
    @Key
    private String fields;
    
    public List<DataFilter> getDataFilters() {
        /*SL:67*/return this.dataFilters;
    }
    
    public UpdateDeveloperMetadataRequest setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:75*/this.dataFilters = dataFilters;
        /*SL:76*/return this;
    }
    
    public DeveloperMetadata getDeveloperMetadata() {
        /*SL:84*/return this.developerMetadata;
    }
    
    public UpdateDeveloperMetadataRequest setDeveloperMetadata(final DeveloperMetadata developerMetadata) {
        /*SL:92*/this.developerMetadata = developerMetadata;
        /*SL:93*/return this;
    }
    
    public String getFields() {
        /*SL:103*/return this.fields;
    }
    
    public UpdateDeveloperMetadataRequest setFields(final String fields) {
        /*SL:113*/this.fields = fields;
        /*SL:114*/return this;
    }
    
    public UpdateDeveloperMetadataRequest set(final String a1, final Object a2) {
        /*SL:119*/return (UpdateDeveloperMetadataRequest)super.set(a1, a2);
    }
    
    public UpdateDeveloperMetadataRequest clone() {
        /*SL:124*/return (UpdateDeveloperMetadataRequest)super.clone();
    }
}
