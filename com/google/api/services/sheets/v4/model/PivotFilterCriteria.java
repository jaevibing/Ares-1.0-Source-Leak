package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class PivotFilterCriteria extends GenericJson
{
    @Key
    private List<String> visibleValues;
    
    public List<String> getVisibleValues() {
        /*SL:48*/return this.visibleValues;
    }
    
    public PivotFilterCriteria setVisibleValues(final List<String> visibleValues) {
        /*SL:56*/this.visibleValues = visibleValues;
        /*SL:57*/return this;
    }
    
    public PivotFilterCriteria set(final String a1, final Object a2) {
        /*SL:62*/return (PivotFilterCriteria)super.set(a1, a2);
    }
    
    public PivotFilterCriteria clone() {
        /*SL:67*/return (PivotFilterCriteria)super.clone();
    }
}
