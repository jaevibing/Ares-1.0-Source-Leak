package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class WaterfallChartColumnStyle extends GenericJson
{
    @Key
    private Color color;
    @Key
    private String label;
    
    public Color getColor() {
        /*SL:55*/return this.color;
    }
    
    public WaterfallChartColumnStyle setColor(final Color color) {
        /*SL:63*/this.color = color;
        /*SL:64*/return this;
    }
    
    public String getLabel() {
        /*SL:72*/return this.label;
    }
    
    public WaterfallChartColumnStyle setLabel(final String label) {
        /*SL:80*/this.label = label;
        /*SL:81*/return this;
    }
    
    public WaterfallChartColumnStyle set(final String a1, final Object a2) {
        /*SL:86*/return (WaterfallChartColumnStyle)super.set(a1, a2);
    }
    
    public WaterfallChartColumnStyle clone() {
        /*SL:91*/return (WaterfallChartColumnStyle)super.clone();
    }
}
