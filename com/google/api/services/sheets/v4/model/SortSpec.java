package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SortSpec extends GenericJson
{
    @Key
    private Integer dimensionIndex;
    @Key
    private String sortOrder;
    
    public Integer getDimensionIndex() {
        /*SL:55*/return this.dimensionIndex;
    }
    
    public SortSpec setDimensionIndex(final Integer dimensionIndex) {
        /*SL:63*/this.dimensionIndex = dimensionIndex;
        /*SL:64*/return this;
    }
    
    public String getSortOrder() {
        /*SL:72*/return this.sortOrder;
    }
    
    public SortSpec setSortOrder(final String sortOrder) {
        /*SL:80*/this.sortOrder = sortOrder;
        /*SL:81*/return this;
    }
    
    public SortSpec set(final String a1, final Object a2) {
        /*SL:86*/return (SortSpec)super.set(a1, a2);
    }
    
    public SortSpec clone() {
        /*SL:91*/return (SortSpec)super.clone();
    }
}
