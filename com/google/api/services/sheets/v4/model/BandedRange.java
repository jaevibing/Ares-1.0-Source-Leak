package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BandedRange extends GenericJson
{
    @Key
    private Integer bandedRangeId;
    @Key
    private BandingProperties columnProperties;
    @Key
    private GridRange range;
    @Key
    private BandingProperties rowProperties;
    
    public Integer getBandedRangeId() {
        /*SL:72*/return this.bandedRangeId;
    }
    
    public BandedRange setBandedRangeId(final Integer bandedRangeId) {
        /*SL:80*/this.bandedRangeId = bandedRangeId;
        /*SL:81*/return this;
    }
    
    public BandingProperties getColumnProperties() {
        /*SL:91*/return this.columnProperties;
    }
    
    public BandedRange setColumnProperties(final BandingProperties columnProperties) {
        /*SL:101*/this.columnProperties = columnProperties;
        /*SL:102*/return this;
    }
    
    public GridRange getRange() {
        /*SL:110*/return this.range;
    }
    
    public BandedRange setRange(final GridRange range) {
        /*SL:118*/this.range = range;
        /*SL:119*/return this;
    }
    
    public BandingProperties getRowProperties() {
        /*SL:128*/return this.rowProperties;
    }
    
    public BandedRange setRowProperties(final BandingProperties rowProperties) {
        /*SL:137*/this.rowProperties = rowProperties;
        /*SL:138*/return this;
    }
    
    public BandedRange set(final String a1, final Object a2) {
        /*SL:143*/return (BandedRange)super.set(a1, a2);
    }
    
    public BandedRange clone() {
        /*SL:148*/return (BandedRange)super.clone();
    }
}
