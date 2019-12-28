package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class EmbeddedChart extends GenericJson
{
    @Key
    private Integer chartId;
    @Key
    private EmbeddedObjectPosition position;
    @Key
    private ChartSpec spec;
    
    public Integer getChartId() {
        /*SL:62*/return this.chartId;
    }
    
    public EmbeddedChart setChartId(final Integer chartId) {
        /*SL:70*/this.chartId = chartId;
        /*SL:71*/return this;
    }
    
    public EmbeddedObjectPosition getPosition() {
        /*SL:79*/return this.position;
    }
    
    public EmbeddedChart setPosition(final EmbeddedObjectPosition position) {
        /*SL:87*/this.position = position;
        /*SL:88*/return this;
    }
    
    public ChartSpec getSpec() {
        /*SL:96*/return this.spec;
    }
    
    public EmbeddedChart setSpec(final ChartSpec spec) {
        /*SL:104*/this.spec = spec;
        /*SL:105*/return this;
    }
    
    public EmbeddedChart set(final String a1, final Object a2) {
        /*SL:110*/return (EmbeddedChart)super.set(a1, a2);
    }
    
    public EmbeddedChart clone() {
        /*SL:115*/return (EmbeddedChart)super.clone();
    }
}
