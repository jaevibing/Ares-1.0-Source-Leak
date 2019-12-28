package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class DeleteDimensionGroupResponse extends GenericJson
{
    @Key
    private List<DimensionGroup> dimensionGroups;
    
    public List<DimensionGroup> getDimensionGroups() {
        /*SL:48*/return this.dimensionGroups;
    }
    
    public DeleteDimensionGroupResponse setDimensionGroups(final List<DimensionGroup> dimensionGroups) {
        /*SL:56*/this.dimensionGroups = dimensionGroups;
        /*SL:57*/return this;
    }
    
    public DeleteDimensionGroupResponse set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteDimensionGroupResponse)super.set(a1, a2);
    }
    
    public DeleteDimensionGroupResponse clone() {
        /*SL:67*/return (DeleteDimensionGroupResponse)super.clone();
    }
}
