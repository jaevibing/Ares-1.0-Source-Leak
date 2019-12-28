package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class WaterfallChartSeries extends GenericJson
{
    @Key
    private List<WaterfallChartCustomSubtotal> customSubtotals;
    @Key
    private ChartData data;
    @Key
    private Boolean hideTrailingSubtotal;
    @Key
    private WaterfallChartColumnStyle negativeColumnsStyle;
    @Key
    private WaterfallChartColumnStyle positiveColumnsStyle;
    @Key
    private WaterfallChartColumnStyle subtotalColumnsStyle;
    
    public List<WaterfallChartCustomSubtotal> getCustomSubtotals() {
        /*SL:87*/return this.customSubtotals;
    }
    
    public WaterfallChartSeries setCustomSubtotals(final List<WaterfallChartCustomSubtotal> customSubtotals) {
        /*SL:96*/this.customSubtotals = customSubtotals;
        /*SL:97*/return this;
    }
    
    public ChartData getData() {
        /*SL:105*/return this.data;
    }
    
    public WaterfallChartSeries setData(final ChartData data) {
        /*SL:113*/this.data = data;
        /*SL:114*/return this;
    }
    
    public Boolean getHideTrailingSubtotal() {
        /*SL:124*/return this.hideTrailingSubtotal;
    }
    
    public WaterfallChartSeries setHideTrailingSubtotal(final Boolean hideTrailingSubtotal) {
        /*SL:134*/this.hideTrailingSubtotal = hideTrailingSubtotal;
        /*SL:135*/return this;
    }
    
    public WaterfallChartColumnStyle getNegativeColumnsStyle() {
        /*SL:143*/return this.negativeColumnsStyle;
    }
    
    public WaterfallChartSeries setNegativeColumnsStyle(final WaterfallChartColumnStyle negativeColumnsStyle) {
        /*SL:151*/this.negativeColumnsStyle = negativeColumnsStyle;
        /*SL:152*/return this;
    }
    
    public WaterfallChartColumnStyle getPositiveColumnsStyle() {
        /*SL:160*/return this.positiveColumnsStyle;
    }
    
    public WaterfallChartSeries setPositiveColumnsStyle(final WaterfallChartColumnStyle positiveColumnsStyle) {
        /*SL:168*/this.positiveColumnsStyle = positiveColumnsStyle;
        /*SL:169*/return this;
    }
    
    public WaterfallChartColumnStyle getSubtotalColumnsStyle() {
        /*SL:177*/return this.subtotalColumnsStyle;
    }
    
    public WaterfallChartSeries setSubtotalColumnsStyle(final WaterfallChartColumnStyle subtotalColumnsStyle) {
        /*SL:185*/this.subtotalColumnsStyle = subtotalColumnsStyle;
        /*SL:186*/return this;
    }
    
    public WaterfallChartSeries set(final String a1, final Object a2) {
        /*SL:191*/return (WaterfallChartSeries)super.set(a1, a2);
    }
    
    public WaterfallChartSeries clone() {
        /*SL:196*/return (WaterfallChartSeries)super.clone();
    }
}
