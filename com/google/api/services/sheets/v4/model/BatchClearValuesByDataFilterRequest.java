package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchClearValuesByDataFilterRequest extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    
    public List<DataFilter> getDataFilters() {
        /*SL:48*/return this.dataFilters;
    }
    
    public BatchClearValuesByDataFilterRequest setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:56*/this.dataFilters = dataFilters;
        /*SL:57*/return this;
    }
    
    public BatchClearValuesByDataFilterRequest set(final String a1, final Object a2) {
        /*SL:62*/return (BatchClearValuesByDataFilterRequest)super.set(a1, a2);
    }
    
    public BatchClearValuesByDataFilterRequest clone() {
        /*SL:67*/return (BatchClearValuesByDataFilterRequest)super.clone();
    }
}
