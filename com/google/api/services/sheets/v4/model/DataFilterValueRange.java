package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DataFilterValueRange extends GenericJson
{
    @Key
    private DataFilter dataFilter;
    @Key
    private String majorDimension;
    @Key
    private List<List<Object>> values;
    
    public DataFilter getDataFilter() {
        /*SL:65*/return this.dataFilter;
    }
    
    public DataFilterValueRange setDataFilter(final DataFilter dataFilter) {
        /*SL:73*/this.dataFilter = dataFilter;
        /*SL:74*/return this;
    }
    
    public String getMajorDimension() {
        /*SL:82*/return this.majorDimension;
    }
    
    public DataFilterValueRange setMajorDimension(final String majorDimension) {
        /*SL:90*/this.majorDimension = majorDimension;
        /*SL:91*/return this;
    }
    
    public List<List<Object>> getValues() {
        /*SL:102*/return this.values;
    }
    
    public DataFilterValueRange setValues(final List<List<Object>> values) {
        /*SL:113*/this.values = values;
        /*SL:114*/return this;
    }
    
    public DataFilterValueRange set(final String a1, final Object a2) {
        /*SL:119*/return (DataFilterValueRange)super.set(a1, a2);
    }
    
    public DataFilterValueRange clone() {
        /*SL:124*/return (DataFilterValueRange)super.clone();
    }
}
