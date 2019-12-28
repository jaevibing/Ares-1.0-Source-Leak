package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateDimensionPropertiesRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private DimensionProperties properties;
    @Key
    private DimensionRange range;
    
    public String getFields() {
        /*SL:66*/return this.fields;
    }
    
    public UpdateDimensionPropertiesRequest setFields(final String fields) {
        /*SL:76*/this.fields = fields;
        /*SL:77*/return this;
    }
    
    public DimensionProperties getProperties() {
        /*SL:85*/return this.properties;
    }
    
    public UpdateDimensionPropertiesRequest setProperties(final DimensionProperties properties) {
        /*SL:93*/this.properties = properties;
        /*SL:94*/return this;
    }
    
    public DimensionRange getRange() {
        /*SL:102*/return this.range;
    }
    
    public UpdateDimensionPropertiesRequest setRange(final DimensionRange range) {
        /*SL:110*/this.range = range;
        /*SL:111*/return this;
    }
    
    public UpdateDimensionPropertiesRequest set(final String a1, final Object a2) {
        /*SL:116*/return (UpdateDimensionPropertiesRequest)super.set(a1, a2);
    }
    
    public UpdateDimensionPropertiesRequest clone() {
        /*SL:121*/return (UpdateDimensionPropertiesRequest)super.clone();
    }
}
