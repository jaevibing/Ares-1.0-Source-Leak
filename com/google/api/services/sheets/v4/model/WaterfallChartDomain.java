package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class WaterfallChartDomain extends GenericJson
{
    @Key
    private ChartData data;
    @Key
    private Boolean reversed;
    
    public ChartData getData() {
        /*SL:55*/return this.data;
    }
    
    public WaterfallChartDomain setData(final ChartData data) {
        /*SL:63*/this.data = data;
        /*SL:64*/return this;
    }
    
    public Boolean getReversed() {
        /*SL:72*/return this.reversed;
    }
    
    public WaterfallChartDomain setReversed(final Boolean reversed) {
        /*SL:80*/this.reversed = reversed;
        /*SL:81*/return this;
    }
    
    public WaterfallChartDomain set(final String a1, final Object a2) {
        /*SL:86*/return (WaterfallChartDomain)super.set(a1, a2);
    }
    
    public WaterfallChartDomain clone() {
        /*SL:91*/return (WaterfallChartDomain)super.clone();
    }
}
