package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddChartResponse extends GenericJson
{
    @Key
    private EmbeddedChart chart;
    
    public EmbeddedChart getChart() {
        /*SL:48*/return this.chart;
    }
    
    public AddChartResponse setChart(final EmbeddedChart chart) {
        /*SL:56*/this.chart = chart;
        /*SL:57*/return this;
    }
    
    public AddChartResponse set(final String a1, final Object a2) {
        /*SL:62*/return (AddChartResponse)super.set(a1, a2);
    }
    
    public AddChartResponse clone() {
        /*SL:67*/return (AddChartResponse)super.clone();
    }
}
