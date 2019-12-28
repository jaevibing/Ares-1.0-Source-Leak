package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PivotGroupValueMetadata extends GenericJson
{
    @Key
    private Boolean collapsed;
    @Key
    private ExtendedValue value;
    
    public Boolean getCollapsed() {
        /*SL:56*/return this.collapsed;
    }
    
    public PivotGroupValueMetadata setCollapsed(final Boolean collapsed) {
        /*SL:64*/this.collapsed = collapsed;
        /*SL:65*/return this;
    }
    
    public ExtendedValue getValue() {
        /*SL:74*/return this.value;
    }
    
    public PivotGroupValueMetadata setValue(final ExtendedValue value) {
        /*SL:83*/this.value = value;
        /*SL:84*/return this;
    }
    
    public PivotGroupValueMetadata set(final String a1, final Object a2) {
        /*SL:89*/return (PivotGroupValueMetadata)super.set(a1, a2);
    }
    
    public PivotGroupValueMetadata clone() {
        /*SL:94*/return (PivotGroupValueMetadata)super.clone();
    }
}
