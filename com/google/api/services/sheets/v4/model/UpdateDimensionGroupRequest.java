package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateDimensionGroupRequest extends GenericJson
{
    @Key
    private DimensionGroup dimensionGroup;
    @Key
    private String fields;
    
    public DimensionGroup getDimensionGroup() {
        /*SL:59*/return this.dimensionGroup;
    }
    
    public UpdateDimensionGroupRequest setDimensionGroup(final DimensionGroup dimensionGroup) {
        /*SL:68*/this.dimensionGroup = dimensionGroup;
        /*SL:69*/return this;
    }
    
    public String getFields() {
        /*SL:79*/return this.fields;
    }
    
    public UpdateDimensionGroupRequest setFields(final String fields) {
        /*SL:89*/this.fields = fields;
        /*SL:90*/return this;
    }
    
    public UpdateDimensionGroupRequest set(final String a1, final Object a2) {
        /*SL:95*/return (UpdateDimensionGroupRequest)super.set(a1, a2);
    }
    
    public UpdateDimensionGroupRequest clone() {
        /*SL:100*/return (UpdateDimensionGroupRequest)super.clone();
    }
}
