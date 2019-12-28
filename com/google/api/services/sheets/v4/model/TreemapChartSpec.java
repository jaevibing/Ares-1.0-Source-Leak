package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TreemapChartSpec extends GenericJson
{
    @Key
    private ChartData colorData;
    @Key
    private TreemapChartColorScale colorScale;
    @Key
    private Color headerColor;
    @Key
    private Boolean hideTooltips;
    @Key
    private Integer hintedLevels;
    @Key
    private ChartData labels;
    @Key
    private Integer levels;
    @Key
    private Double maxValue;
    @Key
    private Double minValue;
    @Key
    private ChartData parentLabels;
    @Key
    private ChartData sizeData;
    @Key
    private TextFormat textFormat;
    
    public ChartData getColorData() {
        /*SL:148*/return this.colorData;
    }
    
    public TreemapChartSpec setColorData(final ChartData colorData) {
        /*SL:159*/this.colorData = colorData;
        /*SL:160*/return this;
    }
    
    public TreemapChartColorScale getColorScale() {
        /*SL:175*/return this.colorScale;
    }
    
    public TreemapChartSpec setColorScale(final TreemapChartColorScale colorScale) {
        /*SL:190*/this.colorScale = colorScale;
        /*SL:191*/return this;
    }
    
    public Color getHeaderColor() {
        /*SL:199*/return this.headerColor;
    }
    
    public TreemapChartSpec setHeaderColor(final Color headerColor) {
        /*SL:207*/this.headerColor = headerColor;
        /*SL:208*/return this;
    }
    
    public Boolean getHideTooltips() {
        /*SL:216*/return this.hideTooltips;
    }
    
    public TreemapChartSpec setHideTooltips(final Boolean hideTooltips) {
        /*SL:224*/this.hideTooltips = hideTooltips;
        /*SL:225*/return this;
    }
    
    public Integer getHintedLevels() {
        /*SL:235*/return this.hintedLevels;
    }
    
    public TreemapChartSpec setHintedLevels(final Integer hintedLevels) {
        /*SL:245*/this.hintedLevels = hintedLevels;
        /*SL:246*/return this;
    }
    
    public ChartData getLabels() {
        /*SL:254*/return this.labels;
    }
    
    public TreemapChartSpec setLabels(final ChartData labels) {
        /*SL:262*/this.labels = labels;
        /*SL:263*/return this;
    }
    
    public Integer getLevels() {
        /*SL:272*/return this.levels;
    }
    
    public TreemapChartSpec setLevels(final Integer levels) {
        /*SL:281*/this.levels = levels;
        /*SL:282*/return this;
    }
    
    public Double getMaxValue() {
        /*SL:292*/return this.maxValue;
    }
    
    public TreemapChartSpec setMaxValue(final Double maxValue) {
        /*SL:302*/this.maxValue = maxValue;
        /*SL:303*/return this;
    }
    
    public Double getMinValue() {
        /*SL:313*/return this.minValue;
    }
    
    public TreemapChartSpec setMinValue(final Double minValue) {
        /*SL:323*/this.minValue = minValue;
        /*SL:324*/return this;
    }
    
    public ChartData getParentLabels() {
        /*SL:332*/return this.parentLabels;
    }
    
    public TreemapChartSpec setParentLabels(final ChartData parentLabels) {
        /*SL:340*/this.parentLabels = parentLabels;
        /*SL:341*/return this;
    }
    
    public ChartData getSizeData() {
        /*SL:352*/return this.sizeData;
    }
    
    public TreemapChartSpec setSizeData(final ChartData sizeData) {
        /*SL:363*/this.sizeData = sizeData;
        /*SL:364*/return this;
    }
    
    public TextFormat getTextFormat() {
        /*SL:372*/return this.textFormat;
    }
    
    public TreemapChartSpec setTextFormat(final TextFormat textFormat) {
        /*SL:380*/this.textFormat = textFormat;
        /*SL:381*/return this;
    }
    
    public TreemapChartSpec set(final String a1, final Object a2) {
        /*SL:386*/return (TreemapChartSpec)super.set(a1, a2);
    }
    
    public TreemapChartSpec clone() {
        /*SL:391*/return (TreemapChartSpec)super.clone();
    }
}
