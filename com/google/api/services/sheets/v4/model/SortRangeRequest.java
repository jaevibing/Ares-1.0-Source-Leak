package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SortRangeRequest extends GenericJson
{
    @Key
    private GridRange range;
    @Key
    private List<SortSpec> sortSpecs;
    
    public GridRange getRange() {
        /*SL:62*/return this.range;
    }
    
    public SortRangeRequest setRange(final GridRange range) {
        /*SL:70*/this.range = range;
        /*SL:71*/return this;
    }
    
    public List<SortSpec> getSortSpecs() {
        /*SL:80*/return this.sortSpecs;
    }
    
    public SortRangeRequest setSortSpecs(final List<SortSpec> sortSpecs) {
        /*SL:89*/this.sortSpecs = sortSpecs;
        /*SL:90*/return this;
    }
    
    public SortRangeRequest set(final String a1, final Object a2) {
        /*SL:95*/return (SortRangeRequest)super.set(a1, a2);
    }
    
    public SortRangeRequest clone() {
        /*SL:100*/return (SortRangeRequest)super.clone();
    }
    
    static {
        Data.<Object>nullOf(SortSpec.class);
    }
}
