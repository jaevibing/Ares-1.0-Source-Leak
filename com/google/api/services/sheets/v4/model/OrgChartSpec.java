package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class OrgChartSpec extends GenericJson
{
    @Key
    private ChartData labels;
    @Key
    private Color nodeColor;
    @Key
    private String nodeSize;
    @Key
    private ChartData parentLabels;
    @Key
    private Color selectedNodeColor;
    @Key
    private ChartData tooltips;
    
    public ChartData getLabels() {
        /*SL:92*/return this.labels;
    }
    
    public OrgChartSpec setLabels(final ChartData labels) {
        /*SL:100*/this.labels = labels;
        /*SL:101*/return this;
    }
    
    public Color getNodeColor() {
        /*SL:109*/return this.nodeColor;
    }
    
    public OrgChartSpec setNodeColor(final Color nodeColor) {
        /*SL:117*/this.nodeColor = nodeColor;
        /*SL:118*/return this;
    }
    
    public String getNodeSize() {
        /*SL:126*/return this.nodeSize;
    }
    
    public OrgChartSpec setNodeSize(final String nodeSize) {
        /*SL:134*/this.nodeSize = nodeSize;
        /*SL:135*/return this;
    }
    
    public ChartData getParentLabels() {
        /*SL:144*/return this.parentLabels;
    }
    
    public OrgChartSpec setParentLabels(final ChartData parentLabels) {
        /*SL:153*/this.parentLabels = parentLabels;
        /*SL:154*/return this;
    }
    
    public Color getSelectedNodeColor() {
        /*SL:162*/return this.selectedNodeColor;
    }
    
    public OrgChartSpec setSelectedNodeColor(final Color selectedNodeColor) {
        /*SL:170*/this.selectedNodeColor = selectedNodeColor;
        /*SL:171*/return this;
    }
    
    public ChartData getTooltips() {
        /*SL:180*/return this.tooltips;
    }
    
    public OrgChartSpec setTooltips(final ChartData tooltips) {
        /*SL:189*/this.tooltips = tooltips;
        /*SL:190*/return this;
    }
    
    public OrgChartSpec set(final String a1, final Object a2) {
        /*SL:195*/return (OrgChartSpec)super.set(a1, a2);
    }
    
    public OrgChartSpec clone() {
        /*SL:200*/return (OrgChartSpec)super.clone();
    }
}
