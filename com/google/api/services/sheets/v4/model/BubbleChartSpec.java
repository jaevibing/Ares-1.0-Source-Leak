package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BubbleChartSpec extends GenericJson
{
    @Key
    private Color bubbleBorderColor;
    @Key
    private ChartData bubbleLabels;
    @Key
    private Integer bubbleMaxRadiusSize;
    @Key
    private Integer bubbleMinRadiusSize;
    @Key
    private Float bubbleOpacity;
    @Key
    private ChartData bubbleSizes;
    @Key
    private TextFormat bubbleTextStyle;
    @Key
    private ChartData domain;
    @Key
    private ChartData groupIds;
    @Key
    private String legendPosition;
    @Key
    private ChartData series;
    
    public Color getBubbleBorderColor() {
        /*SL:126*/return this.bubbleBorderColor;
    }
    
    public BubbleChartSpec setBubbleBorderColor(final Color bubbleBorderColor) {
        /*SL:134*/this.bubbleBorderColor = bubbleBorderColor;
        /*SL:135*/return this;
    }
    
    public ChartData getBubbleLabels() {
        /*SL:143*/return this.bubbleLabels;
    }
    
    public BubbleChartSpec setBubbleLabels(final ChartData bubbleLabels) {
        /*SL:151*/this.bubbleLabels = bubbleLabels;
        /*SL:152*/return this;
    }
    
    public Integer getBubbleMaxRadiusSize() {
        /*SL:161*/return this.bubbleMaxRadiusSize;
    }
    
    public BubbleChartSpec setBubbleMaxRadiusSize(final Integer bubbleMaxRadiusSize) {
        /*SL:170*/this.bubbleMaxRadiusSize = bubbleMaxRadiusSize;
        /*SL:171*/return this;
    }
    
    public Integer getBubbleMinRadiusSize() {
        /*SL:180*/return this.bubbleMinRadiusSize;
    }
    
    public BubbleChartSpec setBubbleMinRadiusSize(final Integer bubbleMinRadiusSize) {
        /*SL:189*/this.bubbleMinRadiusSize = bubbleMinRadiusSize;
        /*SL:190*/return this;
    }
    
    public Float getBubbleOpacity() {
        /*SL:198*/return this.bubbleOpacity;
    }
    
    public BubbleChartSpec setBubbleOpacity(final Float bubbleOpacity) {
        /*SL:206*/this.bubbleOpacity = bubbleOpacity;
        /*SL:207*/return this;
    }
    
    public ChartData getBubbleSizes() {
        /*SL:217*/return this.bubbleSizes;
    }
    
    public BubbleChartSpec setBubbleSizes(final ChartData bubbleSizes) {
        /*SL:227*/this.bubbleSizes = bubbleSizes;
        /*SL:228*/return this;
    }
    
    public TextFormat getBubbleTextStyle() {
        /*SL:236*/return this.bubbleTextStyle;
    }
    
    public BubbleChartSpec setBubbleTextStyle(final TextFormat bubbleTextStyle) {
        /*SL:244*/this.bubbleTextStyle = bubbleTextStyle;
        /*SL:245*/return this;
    }
    
    public ChartData getDomain() {
        /*SL:254*/return this.domain;
    }
    
    public BubbleChartSpec setDomain(final ChartData domain) {
        /*SL:263*/this.domain = domain;
        /*SL:264*/return this;
    }
    
    public ChartData getGroupIds() {
        /*SL:274*/return this.groupIds;
    }
    
    public BubbleChartSpec setGroupIds(final ChartData groupIds) {
        /*SL:284*/this.groupIds = groupIds;
        /*SL:285*/return this;
    }
    
    public String getLegendPosition() {
        /*SL:293*/return this.legendPosition;
    }
    
    public BubbleChartSpec setLegendPosition(final String legendPosition) {
        /*SL:301*/this.legendPosition = legendPosition;
        /*SL:302*/return this;
    }
    
    public ChartData getSeries() {
        /*SL:311*/return this.series;
    }
    
    public BubbleChartSpec setSeries(final ChartData series) {
        /*SL:320*/this.series = series;
        /*SL:321*/return this;
    }
    
    public BubbleChartSpec set(final String a1, final Object a2) {
        /*SL:326*/return (BubbleChartSpec)super.set(a1, a2);
    }
    
    public BubbleChartSpec clone() {
        /*SL:331*/return (BubbleChartSpec)super.clone();
    }
}
