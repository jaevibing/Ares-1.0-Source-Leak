package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class WaterfallChartSpec extends GenericJson
{
    @Key
    private LineStyle connectorLineStyle;
    @Key
    private WaterfallChartDomain domain;
    @Key
    private Boolean firstValueIsTotal;
    @Key
    private Boolean hideConnectorLines;
    @Key
    private List<WaterfallChartSeries> series;
    @Key
    private String stackedType;
    
    public LineStyle getConnectorLineStyle() {
        /*SL:83*/return this.connectorLineStyle;
    }
    
    public WaterfallChartSpec setConnectorLineStyle(final LineStyle connectorLineStyle) {
        /*SL:91*/this.connectorLineStyle = connectorLineStyle;
        /*SL:92*/return this;
    }
    
    public WaterfallChartDomain getDomain() {
        /*SL:100*/return this.domain;
    }
    
    public WaterfallChartSpec setDomain(final WaterfallChartDomain domain) {
        /*SL:108*/this.domain = domain;
        /*SL:109*/return this;
    }
    
    public Boolean getFirstValueIsTotal() {
        /*SL:117*/return this.firstValueIsTotal;
    }
    
    public WaterfallChartSpec setFirstValueIsTotal(final Boolean firstValueIsTotal) {
        /*SL:125*/this.firstValueIsTotal = firstValueIsTotal;
        /*SL:126*/return this;
    }
    
    public Boolean getHideConnectorLines() {
        /*SL:134*/return this.hideConnectorLines;
    }
    
    public WaterfallChartSpec setHideConnectorLines(final Boolean hideConnectorLines) {
        /*SL:142*/this.hideConnectorLines = hideConnectorLines;
        /*SL:143*/return this;
    }
    
    public List<WaterfallChartSeries> getSeries() {
        /*SL:151*/return this.series;
    }
    
    public WaterfallChartSpec setSeries(final List<WaterfallChartSeries> series) {
        /*SL:159*/this.series = series;
        /*SL:160*/return this;
    }
    
    public String getStackedType() {
        /*SL:168*/return this.stackedType;
    }
    
    public WaterfallChartSpec setStackedType(final String stackedType) {
        /*SL:176*/this.stackedType = stackedType;
        /*SL:177*/return this;
    }
    
    public WaterfallChartSpec set(final String a1, final Object a2) {
        /*SL:182*/return (WaterfallChartSpec)super.set(a1, a2);
    }
    
    public WaterfallChartSpec clone() {
        /*SL:187*/return (WaterfallChartSpec)super.clone();
    }
}
