package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class Sheet extends GenericJson
{
    @Key
    private List<BandedRange> bandedRanges;
    @Key
    private BasicFilter basicFilter;
    @Key
    private List<EmbeddedChart> charts;
    @Key
    private List<DimensionGroup> columnGroups;
    @Key
    private List<ConditionalFormatRule> conditionalFormats;
    @Key
    private List<GridData> data;
    @Key
    private List<DeveloperMetadata> developerMetadata;
    @Key
    private List<FilterView> filterViews;
    @Key
    private List<GridRange> merges;
    @Key
    private SheetProperties properties;
    @Key
    private List<ProtectedRange> protectedRanges;
    @Key
    private List<DimensionGroup> rowGroups;
    
    public List<BandedRange> getBandedRanges() {
        /*SL:141*/return this.bandedRanges;
    }
    
    public Sheet setBandedRanges(final List<BandedRange> bandedRanges) {
        /*SL:149*/this.bandedRanges = bandedRanges;
        /*SL:150*/return this;
    }
    
    public BasicFilter getBasicFilter() {
        /*SL:158*/return this.basicFilter;
    }
    
    public Sheet setBasicFilter(final BasicFilter basicFilter) {
        /*SL:166*/this.basicFilter = basicFilter;
        /*SL:167*/return this;
    }
    
    public List<EmbeddedChart> getCharts() {
        /*SL:175*/return this.charts;
    }
    
    public Sheet setCharts(final List<EmbeddedChart> charts) {
        /*SL:183*/this.charts = charts;
        /*SL:184*/return this;
    }
    
    public List<DimensionGroup> getColumnGroups() {
        /*SL:192*/return this.columnGroups;
    }
    
    public Sheet setColumnGroups(final List<DimensionGroup> columnGroups) {
        /*SL:200*/this.columnGroups = columnGroups;
        /*SL:201*/return this;
    }
    
    public List<ConditionalFormatRule> getConditionalFormats() {
        /*SL:209*/return this.conditionalFormats;
    }
    
    public Sheet setConditionalFormats(final List<ConditionalFormatRule> conditionalFormats) {
        /*SL:217*/this.conditionalFormats = conditionalFormats;
        /*SL:218*/return this;
    }
    
    public List<GridData> getData() {
        /*SL:230*/return this.data;
    }
    
    public Sheet setData(final List<GridData> data) {
        /*SL:242*/this.data = data;
        /*SL:243*/return this;
    }
    
    public List<DeveloperMetadata> getDeveloperMetadata() {
        /*SL:251*/return this.developerMetadata;
    }
    
    public Sheet setDeveloperMetadata(final List<DeveloperMetadata> developerMetadata) {
        /*SL:259*/this.developerMetadata = developerMetadata;
        /*SL:260*/return this;
    }
    
    public List<FilterView> getFilterViews() {
        /*SL:268*/return this.filterViews;
    }
    
    public Sheet setFilterViews(final List<FilterView> filterViews) {
        /*SL:276*/this.filterViews = filterViews;
        /*SL:277*/return this;
    }
    
    public List<GridRange> getMerges() {
        /*SL:285*/return this.merges;
    }
    
    public Sheet setMerges(final List<GridRange> merges) {
        /*SL:293*/this.merges = merges;
        /*SL:294*/return this;
    }
    
    public SheetProperties getProperties() {
        /*SL:302*/return this.properties;
    }
    
    public Sheet setProperties(final SheetProperties properties) {
        /*SL:310*/this.properties = properties;
        /*SL:311*/return this;
    }
    
    public List<ProtectedRange> getProtectedRanges() {
        /*SL:319*/return this.protectedRanges;
    }
    
    public Sheet setProtectedRanges(final List<ProtectedRange> protectedRanges) {
        /*SL:327*/this.protectedRanges = protectedRanges;
        /*SL:328*/return this;
    }
    
    public List<DimensionGroup> getRowGroups() {
        /*SL:336*/return this.rowGroups;
    }
    
    public Sheet setRowGroups(final List<DimensionGroup> rowGroups) {
        /*SL:344*/this.rowGroups = rowGroups;
        /*SL:345*/return this;
    }
    
    public Sheet set(final String a1, final Object a2) {
        /*SL:350*/return (Sheet)super.set(a1, a2);
    }
    
    public Sheet clone() {
        /*SL:355*/return (Sheet)super.clone();
    }
    
    static {
        Data.<Object>nullOf(EmbeddedChart.class);
        Data.<Object>nullOf(GridData.class);
    }
}
