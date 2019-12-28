package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BasicChartSeries extends GenericJson
{
    @Key
    private Color color;
    @Key
    private LineStyle lineStyle;
    @Key
    private ChartData series;
    @Key
    private String targetAxis;
    @Key
    private String type;
    
    public Color getColor() {
        /*SL:84*/return this.color;
    }
    
    public BasicChartSeries setColor(final Color color) {
        /*SL:93*/this.color = color;
        /*SL:94*/return this;
    }
    
    public LineStyle getLineStyle() {
        /*SL:103*/return this.lineStyle;
    }
    
    public BasicChartSeries setLineStyle(final LineStyle lineStyle) {
        /*SL:112*/this.lineStyle = lineStyle;
        /*SL:113*/return this;
    }
    
    public ChartData getSeries() {
        /*SL:121*/return this.series;
    }
    
    public BasicChartSeries setSeries(final ChartData series) {
        /*SL:129*/this.series = series;
        /*SL:130*/return this;
    }
    
    public String getTargetAxis() {
        /*SL:141*/return this.targetAxis;
    }
    
    public BasicChartSeries setTargetAxis(final String targetAxis) {
        /*SL:152*/this.targetAxis = targetAxis;
        /*SL:153*/return this;
    }
    
    public String getType() {
        /*SL:162*/return this.type;
    }
    
    public BasicChartSeries setType(final String type) {
        /*SL:171*/this.type = type;
        /*SL:172*/return this;
    }
    
    public BasicChartSeries set(final String a1, final Object a2) {
        /*SL:177*/return (BasicChartSeries)super.set(a1, a2);
    }
    
    public BasicChartSeries clone() {
        /*SL:182*/return (BasicChartSeries)super.clone();
    }
}
