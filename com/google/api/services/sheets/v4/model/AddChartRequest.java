package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddChartRequest extends GenericJson
{
    @Key
    private EmbeddedChart chart;
    
    public EmbeddedChart getChart() {
        /*SL:52*/return this.chart;
    }
    
    public AddChartRequest setChart(final EmbeddedChart chart) {
        /*SL:62*/this.chart = chart;
        /*SL:63*/return this;
    }
    
    public AddChartRequest set(final String a1, final Object a2) {
        /*SL:68*/return (AddChartRequest)super.set(a1, a2);
    }
    
    public AddChartRequest clone() {
        /*SL:73*/return (AddChartRequest)super.clone();
    }
}
