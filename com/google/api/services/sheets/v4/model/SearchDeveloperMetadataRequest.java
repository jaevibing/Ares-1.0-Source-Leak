package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class SearchDeveloperMetadataRequest extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    
    public List<DataFilter> getDataFilters() {
        /*SL:52*/return this.dataFilters;
    }
    
    public SearchDeveloperMetadataRequest setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:62*/this.dataFilters = dataFilters;
        /*SL:63*/return this;
    }
    
    public SearchDeveloperMetadataRequest set(final String a1, final Object a2) {
        /*SL:68*/return (SearchDeveloperMetadataRequest)super.set(a1, a2);
    }
    
    public SearchDeveloperMetadataRequest clone() {
        /*SL:73*/return (SearchDeveloperMetadataRequest)super.clone();
    }
}
