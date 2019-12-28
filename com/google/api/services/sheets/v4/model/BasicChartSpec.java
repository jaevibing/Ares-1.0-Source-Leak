package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BasicChartSpec extends GenericJson
{
    @Key
    private List<BasicChartAxis> axis;
    @Key
    private String chartType;
    @Key
    private String compareMode;
    @Key
    private List<BasicChartDomain> domains;
    @Key
    private Integer headerCount;
    @Key
    private Boolean interpolateNulls;
    @Key
    private String legendPosition;
    @Key
    private Boolean lineSmoothing;
    @Key
    private List<BasicChartSeries> series;
    @Key
    private String stackedType;
    @Key
    private Boolean threeDimensional;
    
    public List<BasicChartAxis> getAxis() {
        /*SL:131*/return this.axis;
    }
    
    public BasicChartSpec setAxis(final List<BasicChartAxis> axis) {
        /*SL:139*/this.axis = axis;
        /*SL:140*/return this;
    }
    
    public String getChartType() {
        /*SL:148*/return this.chartType;
    }
    
    public BasicChartSpec setChartType(final String chartType) {
        /*SL:156*/this.chartType = chartType;
        /*SL:157*/return this;
    }
    
    public String getCompareMode() {
        /*SL:165*/return this.compareMode;
    }
    
    public BasicChartSpec setCompareMode(final String compareMode) {
        /*SL:173*/this.compareMode = compareMode;
        /*SL:174*/return this;
    }
    
    public List<BasicChartDomain> getDomains() {
        /*SL:182*/return this.domains;
    }
    
    public BasicChartSpec setDomains(final List<BasicChartDomain> domains) {
        /*SL:190*/this.domains = domains;
        /*SL:191*/return this;
    }
    
    public Integer getHeaderCount() {
        /*SL:202*/return this.headerCount;
    }
    
    public BasicChartSpec setHeaderCount(final Integer headerCount) {
        /*SL:213*/this.headerCount = headerCount;
        /*SL:214*/return this;
    }
    
    public Boolean getInterpolateNulls() {
        /*SL:224*/return this.interpolateNulls;
    }
    
    public BasicChartSpec setInterpolateNulls(final Boolean interpolateNulls) {
        /*SL:234*/this.interpolateNulls = interpolateNulls;
        /*SL:235*/return this;
    }
    
    public String getLegendPosition() {
        /*SL:243*/return this.legendPosition;
    }
    
    public BasicChartSpec setLegendPosition(final String legendPosition) {
        /*SL:251*/this.legendPosition = legendPosition;
        /*SL:252*/return this;
    }
    
    public Boolean getLineSmoothing() {
        /*SL:261*/return this.lineSmoothing;
    }
    
    public BasicChartSpec setLineSmoothing(final Boolean lineSmoothing) {
        /*SL:270*/this.lineSmoothing = lineSmoothing;
        /*SL:271*/return this;
    }
    
    public List<BasicChartSeries> getSeries() {
        /*SL:279*/return this.series;
    }
    
    public BasicChartSpec setSeries(final List<BasicChartSeries> series) {
        /*SL:287*/this.series = series;
        /*SL:288*/return this;
    }
    
    public String getStackedType() {
        /*SL:297*/return this.stackedType;
    }
    
    public BasicChartSpec setStackedType(final String stackedType) {
        /*SL:306*/this.stackedType = stackedType;
        /*SL:307*/return this;
    }
    
    public Boolean getThreeDimensional() {
        /*SL:315*/return this.threeDimensional;
    }
    
    public BasicChartSpec setThreeDimensional(final Boolean threeDimensional) {
        /*SL:323*/this.threeDimensional = threeDimensional;
        /*SL:324*/return this;
    }
    
    public BasicChartSpec set(final String a1, final Object a2) {
        /*SL:329*/return (BasicChartSpec)super.set(a1, a2);
    }
    
    public BasicChartSpec clone() {
        /*SL:334*/return (BasicChartSpec)super.clone();
    }
    
    static {
        Data.<Object>nullOf(BasicChartDomain.class);
    }
}
