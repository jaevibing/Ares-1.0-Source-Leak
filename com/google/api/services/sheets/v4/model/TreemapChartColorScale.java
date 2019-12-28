package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TreemapChartColorScale extends GenericJson
{
    @Key
    private Color maxValueColor;
    @Key
    private Color midValueColor;
    @Key
    private Color minValueColor;
    @Key
    private Color noDataColor;
    
    public Color getMaxValueColor() {
        /*SL:74*/return this.maxValueColor;
    }
    
    public TreemapChartColorScale setMaxValueColor(final Color maxValueColor) {
        /*SL:83*/this.maxValueColor = maxValueColor;
        /*SL:84*/return this;
    }
    
    public Color getMidValueColor() {
        /*SL:93*/return this.midValueColor;
    }
    
    public TreemapChartColorScale setMidValueColor(final Color midValueColor) {
        /*SL:102*/this.midValueColor = midValueColor;
        /*SL:103*/return this;
    }
    
    public Color getMinValueColor() {
        /*SL:112*/return this.minValueColor;
    }
    
    public TreemapChartColorScale setMinValueColor(final Color minValueColor) {
        /*SL:121*/this.minValueColor = minValueColor;
        /*SL:122*/return this;
    }
    
    public Color getNoDataColor() {
        /*SL:131*/return this.noDataColor;
    }
    
    public TreemapChartColorScale setNoDataColor(final Color noDataColor) {
        /*SL:140*/this.noDataColor = noDataColor;
        /*SL:141*/return this;
    }
    
    public TreemapChartColorScale set(final String a1, final Object a2) {
        /*SL:146*/return (TreemapChartColorScale)super.set(a1, a2);
    }
    
    public TreemapChartColorScale clone() {
        /*SL:151*/return (TreemapChartColorScale)super.clone();
    }
}
