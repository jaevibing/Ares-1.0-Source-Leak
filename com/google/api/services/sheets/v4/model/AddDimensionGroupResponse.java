package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class AddDimensionGroupResponse extends GenericJson
{
    @Key
    private List<DimensionGroup> dimensionGroups;
    
    public List<DimensionGroup> getDimensionGroups() {
        /*SL:54*/return this.dimensionGroups;
    }
    
    public AddDimensionGroupResponse setDimensionGroups(final List<DimensionGroup> dimensionGroups) {
        /*SL:62*/this.dimensionGroups = dimensionGroups;
        /*SL:63*/return this;
    }
    
    public AddDimensionGroupResponse set(final String a1, final Object a2) {
        /*SL:68*/return (AddDimensionGroupResponse)super.set(a1, a2);
    }
    
    public AddDimensionGroupResponse clone() {
        /*SL:73*/return (AddDimensionGroupResponse)super.clone();
    }
    
    static {
        Data.<Object>nullOf(DimensionGroup.class);
    }
}
