package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class MatchedDeveloperMetadata extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    @Key
    private DeveloperMetadata developerMetadata;
    
    public List<DataFilter> getDataFilters() {
        /*SL:56*/return this.dataFilters;
    }
    
    public MatchedDeveloperMetadata setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:64*/this.dataFilters = dataFilters;
        /*SL:65*/return this;
    }
    
    public DeveloperMetadata getDeveloperMetadata() {
        /*SL:73*/return this.developerMetadata;
    }
    
    public MatchedDeveloperMetadata setDeveloperMetadata(final DeveloperMetadata developerMetadata) {
        /*SL:81*/this.developerMetadata = developerMetadata;
        /*SL:82*/return this;
    }
    
    public MatchedDeveloperMetadata set(final String a1, final Object a2) {
        /*SL:87*/return (MatchedDeveloperMetadata)super.set(a1, a2);
    }
    
    public MatchedDeveloperMetadata clone() {
        /*SL:92*/return (MatchedDeveloperMetadata)super.clone();
    }
}
