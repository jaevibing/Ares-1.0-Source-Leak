package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class HistogramSeries extends GenericJson
{
    @Key
    private Color barColor;
    @Key
    private ChartData data;
    
    public Color getBarColor() {
        /*SL:55*/return this.barColor;
    }
    
    public HistogramSeries setBarColor(final Color barColor) {
        /*SL:63*/this.barColor = barColor;
        /*SL:64*/return this;
    }
    
    public ChartData getData() {
        /*SL:72*/return this.data;
    }
    
    public HistogramSeries setData(final ChartData data) {
        /*SL:80*/this.data = data;
        /*SL:81*/return this;
    }
    
    public HistogramSeries set(final String a1, final Object a2) {
        /*SL:86*/return (HistogramSeries)super.set(a1, a2);
    }
    
    public HistogramSeries clone() {
        /*SL:91*/return (HistogramSeries)super.clone();
    }
}
