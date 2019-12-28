package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DimensionGroup extends GenericJson
{
    @Key
    private Boolean collapsed;
    @Key
    private Integer depth;
    @Key
    private DimensionRange range;
    
    public Boolean getCollapsed() {
        /*SL:76*/return this.collapsed;
    }
    
    public DimensionGroup setCollapsed(final Boolean collapsed) {
        /*SL:90*/this.collapsed = collapsed;
        /*SL:91*/return this;
    }
    
    public Integer getDepth() {
        /*SL:100*/return this.depth;
    }
    
    public DimensionGroup setDepth(final Integer depth) {
        /*SL:109*/this.depth = depth;
        /*SL:110*/return this;
    }
    
    public DimensionRange getRange() {
        /*SL:118*/return this.range;
    }
    
    public DimensionGroup setRange(final DimensionRange range) {
        /*SL:126*/this.range = range;
        /*SL:127*/return this;
    }
    
    public DimensionGroup set(final String a1, final Object a2) {
        /*SL:132*/return (DimensionGroup)super.set(a1, a2);
    }
    
    public DimensionGroup clone() {
        /*SL:137*/return (DimensionGroup)super.clone();
    }
}
