package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class MatchedValueRange extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    @Key
    private ValueRange valueRange;
    
    public List<DataFilter> getDataFilters() {
        /*SL:55*/return this.dataFilters;
    }
    
    public MatchedValueRange setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:63*/this.dataFilters = dataFilters;
        /*SL:64*/return this;
    }
    
    public ValueRange getValueRange() {
        /*SL:72*/return this.valueRange;
    }
    
    public MatchedValueRange setValueRange(final ValueRange valueRange) {
        /*SL:80*/this.valueRange = valueRange;
        /*SL:81*/return this;
    }
    
    public MatchedValueRange set(final String a1, final Object a2) {
        /*SL:86*/return (MatchedValueRange)super.set(a1, a2);
    }
    
    public MatchedValueRange clone() {
        /*SL:91*/return (MatchedValueRange)super.clone();
    }
}
