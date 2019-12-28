package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeveloperMetadataLocation extends GenericJson
{
    @Key
    private DimensionRange dimensionRange;
    @Key
    private String locationType;
    @Key
    private Integer sheetId;
    @Key
    private Boolean spreadsheet;
    
    public DimensionRange getDimensionRange() {
        /*SL:73*/return this.dimensionRange;
    }
    
    public DeveloperMetadataLocation setDimensionRange(final DimensionRange dimensionRange) {
        /*SL:83*/this.dimensionRange = dimensionRange;
        /*SL:84*/return this;
    }
    
    public String getLocationType() {
        /*SL:92*/return this.locationType;
    }
    
    public DeveloperMetadataLocation setLocationType(final String locationType) {
        /*SL:100*/this.locationType = locationType;
        /*SL:101*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:109*/return this.sheetId;
    }
    
    public DeveloperMetadataLocation setSheetId(final Integer sheetId) {
        /*SL:117*/this.sheetId = sheetId;
        /*SL:118*/return this;
    }
    
    public Boolean getSpreadsheet() {
        /*SL:126*/return this.spreadsheet;
    }
    
    public DeveloperMetadataLocation setSpreadsheet(final Boolean spreadsheet) {
        /*SL:134*/this.spreadsheet = spreadsheet;
        /*SL:135*/return this;
    }
    
    public DeveloperMetadataLocation set(final String a1, final Object a2) {
        /*SL:140*/return (DeveloperMetadataLocation)super.set(a1, a2);
    }
    
    public DeveloperMetadataLocation clone() {
        /*SL:145*/return (DeveloperMetadataLocation)super.clone();
    }
}
