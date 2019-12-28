package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteDeveloperMetadataRequest extends GenericJson
{
    @Key
    private DataFilter dataFilter;
    
    public DataFilter getDataFilter() {
        /*SL:50*/return this.dataFilter;
    }
    
    public DeleteDeveloperMetadataRequest setDataFilter(final DataFilter dataFilter) {
        /*SL:59*/this.dataFilter = dataFilter;
        /*SL:60*/return this;
    }
    
    public DeleteDeveloperMetadataRequest set(final String a1, final Object a2) {
        /*SL:65*/return (DeleteDeveloperMetadataRequest)super.set(a1, a2);
    }
    
    public DeleteDeveloperMetadataRequest clone() {
        /*SL:70*/return (DeleteDeveloperMetadataRequest)super.clone();
    }
}
