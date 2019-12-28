package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class GetSpreadsheetByDataFilterRequest extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    @Key
    private Boolean includeGridData;
    
    public List<DataFilter> getDataFilters() {
        /*SL:56*/return this.dataFilters;
    }
    
    public GetSpreadsheetByDataFilterRequest setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:64*/this.dataFilters = dataFilters;
        /*SL:65*/return this;
    }
    
    public Boolean getIncludeGridData() {
        /*SL:74*/return this.includeGridData;
    }
    
    public GetSpreadsheetByDataFilterRequest setIncludeGridData(final Boolean includeGridData) {
        /*SL:83*/this.includeGridData = includeGridData;
        /*SL:84*/return this;
    }
    
    public GetSpreadsheetByDataFilterRequest set(final String a1, final Object a2) {
        /*SL:89*/return (GetSpreadsheetByDataFilterRequest)super.set(a1, a2);
    }
    
    public GetSpreadsheetByDataFilterRequest clone() {
        /*SL:94*/return (GetSpreadsheetByDataFilterRequest)super.clone();
    }
}
