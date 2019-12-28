package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ChartSpec extends GenericJson
{
    @Key
    private String altText;
    @Key
    private Color backgroundColor;
    @Key
    private BasicChartSpec basicChart;
    @Key
    private BubbleChartSpec bubbleChart;
    @Key
    private CandlestickChartSpec candlestickChart;
    @Key
    private String fontName;
    @Key
    private String hiddenDimensionStrategy;
    @Key
    private HistogramChartSpec histogramChart;
    @Key
    private Boolean maximized;
    @Key
    private OrgChartSpec orgChart;
    @Key
    private PieChartSpec pieChart;
    @Key
    private String subtitle;
    @Key
    private TextFormat subtitleTextFormat;
    @Key
    private TextPosition subtitleTextPosition;
    @Key
    private String title;
    @Key
    private TextFormat titleTextFormat;
    @Key
    private TextPosition titleTextPosition;
    @Key
    private TreemapChartSpec treemapChart;
    @Key
    private WaterfallChartSpec waterfallChart;
    
    public String getAltText() {
        /*SL:177*/return this.altText;
    }
    
    public ChartSpec setAltText(final String altText) {
        /*SL:185*/this.altText = altText;
        /*SL:186*/return this;
    }
    
    public Color getBackgroundColor() {
        /*SL:194*/return this.backgroundColor;
    }
    
    public ChartSpec setBackgroundColor(final Color backgroundColor) {
        /*SL:202*/this.backgroundColor = backgroundColor;
        /*SL:203*/return this;
    }
    
    public BasicChartSpec getBasicChart() {
        /*SL:212*/return this.basicChart;
    }
    
    public ChartSpec setBasicChart(final BasicChartSpec basicChart) {
        /*SL:221*/this.basicChart = basicChart;
        /*SL:222*/return this;
    }
    
    public BubbleChartSpec getBubbleChart() {
        /*SL:230*/return this.bubbleChart;
    }
    
    public ChartSpec setBubbleChart(final BubbleChartSpec bubbleChart) {
        /*SL:238*/this.bubbleChart = bubbleChart;
        /*SL:239*/return this;
    }
    
    public CandlestickChartSpec getCandlestickChart() {
        /*SL:247*/return this.candlestickChart;
    }
    
    public ChartSpec setCandlestickChart(final CandlestickChartSpec candlestickChart) {
        /*SL:255*/this.candlestickChart = candlestickChart;
        /*SL:256*/return this;
    }
    
    public String getFontName() {
        /*SL:265*/return this.fontName;
    }
    
    public ChartSpec setFontName(final String fontName) {
        /*SL:274*/this.fontName = fontName;
        /*SL:275*/return this;
    }
    
    public String getHiddenDimensionStrategy() {
        /*SL:283*/return this.hiddenDimensionStrategy;
    }
    
    public ChartSpec setHiddenDimensionStrategy(final String hiddenDimensionStrategy) {
        /*SL:291*/this.hiddenDimensionStrategy = hiddenDimensionStrategy;
        /*SL:292*/return this;
    }
    
    public HistogramChartSpec getHistogramChart() {
        /*SL:300*/return this.histogramChart;
    }
    
    public ChartSpec setHistogramChart(final HistogramChartSpec histogramChart) {
        /*SL:308*/this.histogramChart = histogramChart;
        /*SL:309*/return this;
    }
    
    public Boolean getMaximized() {
        /*SL:318*/return this.maximized;
    }
    
    public ChartSpec setMaximized(final Boolean maximized) {
        /*SL:327*/this.maximized = maximized;
        /*SL:328*/return this;
    }
    
    public OrgChartSpec getOrgChart() {
        /*SL:336*/return this.orgChart;
    }
    
    public ChartSpec setOrgChart(final OrgChartSpec orgChart) {
        /*SL:344*/this.orgChart = orgChart;
        /*SL:345*/return this;
    }
    
    public PieChartSpec getPieChart() {
        /*SL:353*/return this.pieChart;
    }
    
    public ChartSpec setPieChart(final PieChartSpec pieChart) {
        /*SL:361*/this.pieChart = pieChart;
        /*SL:362*/return this;
    }
    
    public String getSubtitle() {
        /*SL:370*/return this.subtitle;
    }
    
    public ChartSpec setSubtitle(final String subtitle) {
        /*SL:378*/this.subtitle = subtitle;
        /*SL:379*/return this;
    }
    
    public TextFormat getSubtitleTextFormat() {
        /*SL:387*/return this.subtitleTextFormat;
    }
    
    public ChartSpec setSubtitleTextFormat(final TextFormat subtitleTextFormat) {
        /*SL:395*/this.subtitleTextFormat = subtitleTextFormat;
        /*SL:396*/return this;
    }
    
    public TextPosition getSubtitleTextPosition() {
        /*SL:404*/return this.subtitleTextPosition;
    }
    
    public ChartSpec setSubtitleTextPosition(final TextPosition subtitleTextPosition) {
        /*SL:412*/this.subtitleTextPosition = subtitleTextPosition;
        /*SL:413*/return this;
    }
    
    public String getTitle() {
        /*SL:421*/return this.title;
    }
    
    public ChartSpec setTitle(final String title) {
        /*SL:429*/this.title = title;
        /*SL:430*/return this;
    }
    
    public TextFormat getTitleTextFormat() {
        /*SL:438*/return this.titleTextFormat;
    }
    
    public ChartSpec setTitleTextFormat(final TextFormat titleTextFormat) {
        /*SL:446*/this.titleTextFormat = titleTextFormat;
        /*SL:447*/return this;
    }
    
    public TextPosition getTitleTextPosition() {
        /*SL:455*/return this.titleTextPosition;
    }
    
    public ChartSpec setTitleTextPosition(final TextPosition titleTextPosition) {
        /*SL:463*/this.titleTextPosition = titleTextPosition;
        /*SL:464*/return this;
    }
    
    public TreemapChartSpec getTreemapChart() {
        /*SL:472*/return this.treemapChart;
    }
    
    public ChartSpec setTreemapChart(final TreemapChartSpec treemapChart) {
        /*SL:480*/this.treemapChart = treemapChart;
        /*SL:481*/return this;
    }
    
    public WaterfallChartSpec getWaterfallChart() {
        /*SL:489*/return this.waterfallChart;
    }
    
    public ChartSpec setWaterfallChart(final WaterfallChartSpec waterfallChart) {
        /*SL:497*/this.waterfallChart = waterfallChart;
        /*SL:498*/return this;
    }
    
    public ChartSpec set(final String a1, final Object a2) {
        /*SL:503*/return (ChartSpec)super.set(a1, a2);
    }
    
    public ChartSpec clone() {
        /*SL:508*/return (ChartSpec)super.clone();
    }
}
