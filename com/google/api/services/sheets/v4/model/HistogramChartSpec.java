package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class HistogramChartSpec extends GenericJson
{
    @Key
    private Double bucketSize;
    @Key
    private String legendPosition;
    @Key
    private Double outlierPercentile;
    @Key
    private List<HistogramSeries> series;
    @Key
    private Boolean showItemDividers;
    
    public Double getBucketSize() {
        /*SL:89*/return this.bucketSize;
    }
    
    public HistogramChartSpec setBucketSize(final Double bucketSize) {
        /*SL:99*/this.bucketSize = bucketSize;
        /*SL:100*/return this;
    }
    
    public String getLegendPosition() {
        /*SL:108*/return this.legendPosition;
    }
    
    public HistogramChartSpec setLegendPosition(final String legendPosition) {
        /*SL:116*/this.legendPosition = legendPosition;
        /*SL:117*/return this;
    }
    
    public Double getOutlierPercentile() {
        /*SL:129*/return this.outlierPercentile;
    }
    
    public HistogramChartSpec setOutlierPercentile(final Double outlierPercentile) {
        /*SL:141*/this.outlierPercentile = outlierPercentile;
        /*SL:142*/return this;
    }
    
    public List<HistogramSeries> getSeries() {
        /*SL:152*/return this.series;
    }
    
    public HistogramChartSpec setSeries(final List<HistogramSeries> series) {
        /*SL:162*/this.series = series;
        /*SL:163*/return this;
    }
    
    public Boolean getShowItemDividers() {
        /*SL:171*/return this.showItemDividers;
    }
    
    public HistogramChartSpec setShowItemDividers(final Boolean showItemDividers) {
        /*SL:179*/this.showItemDividers = showItemDividers;
        /*SL:180*/return this;
    }
    
    public HistogramChartSpec set(final String a1, final Object a2) {
        /*SL:185*/return (HistogramChartSpec)super.set(a1, a2);
    }
    
    public HistogramChartSpec clone() {
        /*SL:190*/return (HistogramChartSpec)super.clone();
    }
}
