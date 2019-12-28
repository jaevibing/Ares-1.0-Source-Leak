package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import java.util.Map;
import com.google.api.client.json.GenericJson;

public final class BasicFilter extends GenericJson
{
    @Key
    private Map<String, FilterCriteria> criteria;
    @Key
    private GridRange range;
    @Key
    private List<SortSpec> sortSpecs;
    
    public Map<String, FilterCriteria> getCriteria() {
        /*SL:77*/return this.criteria;
    }
    
    public BasicFilter setCriteria(final Map<String, FilterCriteria> criteria) {
        /*SL:86*/this.criteria = criteria;
        /*SL:87*/return this;
    }
    
    public GridRange getRange() {
        /*SL:95*/return this.range;
    }
    
    public BasicFilter setRange(final GridRange range) {
        /*SL:103*/this.range = range;
        /*SL:104*/return this;
    }
    
    public List<SortSpec> getSortSpecs() {
        /*SL:113*/return this.sortSpecs;
    }
    
    public BasicFilter setSortSpecs(final List<SortSpec> sortSpecs) {
        /*SL:122*/this.sortSpecs = sortSpecs;
        /*SL:123*/return this;
    }
    
    public BasicFilter set(final String a1, final Object a2) {
        /*SL:128*/return (BasicFilter)super.set(a1, a2);
    }
    
    public BasicFilter clone() {
        /*SL:133*/return (BasicFilter)super.clone();
    }
    
    static {
        Data.<Object>nullOf(FilterCriteria.class);
        Data.<Object>nullOf(SortSpec.class);
    }
}
