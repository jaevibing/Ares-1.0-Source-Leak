package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PieChartSpec extends GenericJson
{
    @Key
    private ChartData domain;
    @Key
    private String legendPosition;
    @Key
    private Double pieHole;
    @Key
    private ChartData series;
    @Key
    private Boolean threeDimensional;
    
    public ChartData getDomain() {
        /*SL:76*/return this.domain;
    }
    
    public PieChartSpec setDomain(final ChartData domain) {
        /*SL:84*/this.domain = domain;
        /*SL:85*/return this;
    }
    
    public String getLegendPosition() {
        /*SL:93*/return this.legendPosition;
    }
    
    public PieChartSpec setLegendPosition(final String legendPosition) {
        /*SL:101*/this.legendPosition = legendPosition;
        /*SL:102*/return this;
    }
    
    public Double getPieHole() {
        /*SL:110*/return this.pieHole;
    }
    
    public PieChartSpec setPieHole(final Double pieHole) {
        /*SL:118*/this.pieHole = pieHole;
        /*SL:119*/return this;
    }
    
    public ChartData getSeries() {
        /*SL:127*/return this.series;
    }
    
    public PieChartSpec setSeries(final ChartData series) {
        /*SL:135*/this.series = series;
        /*SL:136*/return this;
    }
    
    public Boolean getThreeDimensional() {
        /*SL:144*/return this.threeDimensional;
    }
    
    public PieChartSpec setThreeDimensional(final Boolean threeDimensional) {
        /*SL:152*/this.threeDimensional = threeDimensional;
        /*SL:153*/return this;
    }
    
    public PieChartSpec set(final String a1, final Object a2) {
        /*SL:158*/return (PieChartSpec)super.set(a1, a2);
    }
    
    public PieChartSpec clone() {
        /*SL:163*/return (PieChartSpec)super.clone();
    }
}
