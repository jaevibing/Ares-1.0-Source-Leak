package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class MergeCellsRequest extends GenericJson
{
    @Key
    private String mergeType;
    @Key
    private GridRange range;
    
    public String getMergeType() {
        /*SL:55*/return this.mergeType;
    }
    
    public MergeCellsRequest setMergeType(final String mergeType) {
        /*SL:63*/this.mergeType = mergeType;
        /*SL:64*/return this;
    }
    
    public GridRange getRange() {
        /*SL:72*/return this.range;
    }
    
    public MergeCellsRequest setRange(final GridRange range) {
        /*SL:80*/this.range = range;
        /*SL:81*/return this;
    }
    
    public MergeCellsRequest set(final String a1, final Object a2) {
        /*SL:86*/return (MergeCellsRequest)super.set(a1, a2);
    }
    
    public MergeCellsRequest clone() {
        /*SL:91*/return (MergeCellsRequest)super.clone();
    }
}
