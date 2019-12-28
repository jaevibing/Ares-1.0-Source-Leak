package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateValuesResponse extends GenericJson
{
    @Key
    private String spreadsheetId;
    @Key
    private Integer updatedCells;
    @Key
    private Integer updatedColumns;
    @Key
    private ValueRange updatedData;
    @Key
    private String updatedRange;
    @Key
    private Integer updatedRows;
    
    public String getSpreadsheetId() {
        /*SL:84*/return this.spreadsheetId;
    }
    
    public UpdateValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:92*/this.spreadsheetId = spreadsheetId;
        /*SL:93*/return this;
    }
    
    public Integer getUpdatedCells() {
        /*SL:101*/return this.updatedCells;
    }
    
    public UpdateValuesResponse setUpdatedCells(final Integer updatedCells) {
        /*SL:109*/this.updatedCells = updatedCells;
        /*SL:110*/return this;
    }
    
    public Integer getUpdatedColumns() {
        /*SL:118*/return this.updatedColumns;
    }
    
    public UpdateValuesResponse setUpdatedColumns(final Integer updatedColumns) {
        /*SL:126*/this.updatedColumns = updatedColumns;
        /*SL:127*/return this;
    }
    
    public ValueRange getUpdatedData() {
        /*SL:136*/return this.updatedData;
    }
    
    public UpdateValuesResponse setUpdatedData(final ValueRange updatedData) {
        /*SL:145*/this.updatedData = updatedData;
        /*SL:146*/return this;
    }
    
    public String getUpdatedRange() {
        /*SL:154*/return this.updatedRange;
    }
    
    public UpdateValuesResponse setUpdatedRange(final String updatedRange) {
        /*SL:162*/this.updatedRange = updatedRange;
        /*SL:163*/return this;
    }
    
    public Integer getUpdatedRows() {
        /*SL:171*/return this.updatedRows;
    }
    
    public UpdateValuesResponse setUpdatedRows(final Integer updatedRows) {
        /*SL:179*/this.updatedRows = updatedRows;
        /*SL:180*/return this;
    }
    
    public UpdateValuesResponse set(final String a1, final Object a2) {
        /*SL:185*/return (UpdateValuesResponse)super.set(a1, a2);
    }
    
    public UpdateValuesResponse clone() {
        /*SL:190*/return (UpdateValuesResponse)super.clone();
    }
}
