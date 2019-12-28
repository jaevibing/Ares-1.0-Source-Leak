package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import java.util.Map;
import com.google.api.client.json.GenericJson;

public final class FilterView extends GenericJson
{
    @Key
    private Map<String, FilterCriteria> criteria;
    @Key
    private Integer filterViewId;
    @Key
    private String namedRangeId;
    @Key
    private GridRange range;
    @Key
    private List<SortSpec> sortSpecs;
    @Key
    private String title;
    
    public Map<String, FilterCriteria> getCriteria() {
        /*SL:102*/return this.criteria;
    }
    
    public FilterView setCriteria(final Map<String, FilterCriteria> criteria) {
        /*SL:111*/this.criteria = criteria;
        /*SL:112*/return this;
    }
    
    public Integer getFilterViewId() {
        /*SL:120*/return this.filterViewId;
    }
    
    public FilterView setFilterViewId(final Integer filterViewId) {
        /*SL:128*/this.filterViewId = filterViewId;
        /*SL:129*/return this;
    }
    
    public String getNamedRangeId() {
        /*SL:139*/return this.namedRangeId;
    }
    
    public FilterView setNamedRangeId(final String namedRangeId) {
        /*SL:149*/this.namedRangeId = namedRangeId;
        /*SL:150*/return this;
    }
    
    public GridRange getRange() {
        /*SL:160*/return this.range;
    }
    
    public FilterView setRange(final GridRange range) {
        /*SL:170*/this.range = range;
        /*SL:171*/return this;
    }
    
    public List<SortSpec> getSortSpecs() {
        /*SL:180*/return this.sortSpecs;
    }
    
    public FilterView setSortSpecs(final List<SortSpec> sortSpecs) {
        /*SL:189*/this.sortSpecs = sortSpecs;
        /*SL:190*/return this;
    }
    
    public String getTitle() {
        /*SL:198*/return this.title;
    }
    
    public FilterView setTitle(final String title) {
        /*SL:206*/this.title = title;
        /*SL:207*/return this;
    }
    
    public FilterView set(final String a1, final Object a2) {
        /*SL:212*/return (FilterView)super.set(a1, a2);
    }
    
    public FilterView clone() {
        /*SL:217*/return (FilterView)super.clone();
    }
    
    static {
        Data.<Object>nullOf(FilterCriteria.class);
        Data.<Object>nullOf(SortSpec.class);
    }
}
