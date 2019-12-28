package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class HistogramRule extends GenericJson
{
    @Key
    private Double end;
    @Key
    private Double interval;
    @Key
    private Double start;
    
    public Double getEnd() {
        /*SL:83*/return this.end;
    }
    
    public HistogramRule setEnd(final Double end) {
        /*SL:92*/this.end = end;
        /*SL:93*/return this;
    }
    
    public Double getInterval() {
        /*SL:101*/return this.interval;
    }
    
    public HistogramRule setInterval(final Double interval) {
        /*SL:109*/this.interval = interval;
        /*SL:110*/return this;
    }
    
    public Double getStart() {
        /*SL:119*/return this.start;
    }
    
    public HistogramRule setStart(final Double start) {
        /*SL:128*/this.start = start;
        /*SL:129*/return this;
    }
    
    public HistogramRule set(final String a1, final Object a2) {
        /*SL:134*/return (HistogramRule)super.set(a1, a2);
    }
    
    public HistogramRule clone() {
        /*SL:139*/return (HistogramRule)super.clone();
    }
}
