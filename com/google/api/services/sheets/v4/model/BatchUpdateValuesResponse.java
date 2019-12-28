package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchUpdateValuesResponse extends GenericJson
{
    @Key
    private List<UpdateValuesResponse> responses;
    @Key
    private String spreadsheetId;
    @Key
    private Integer totalUpdatedCells;
    @Key
    private Integer totalUpdatedColumns;
    @Key
    private Integer totalUpdatedRows;
    @Key
    private Integer totalUpdatedSheets;
    
    public List<UpdateValuesResponse> getResponses() {
        /*SL:83*/return this.responses;
    }
    
    public BatchUpdateValuesResponse setResponses(final List<UpdateValuesResponse> responses) {
        /*SL:91*/this.responses = responses;
        /*SL:92*/return this;
    }
    
    public String getSpreadsheetId() {
        /*SL:100*/return this.spreadsheetId;
    }
    
    public BatchUpdateValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:108*/this.spreadsheetId = spreadsheetId;
        /*SL:109*/return this;
    }
    
    public Integer getTotalUpdatedCells() {
        /*SL:117*/return this.totalUpdatedCells;
    }
    
    public BatchUpdateValuesResponse setTotalUpdatedCells(final Integer totalUpdatedCells) {
        /*SL:125*/this.totalUpdatedCells = totalUpdatedCells;
        /*SL:126*/return this;
    }
    
    public Integer getTotalUpdatedColumns() {
        /*SL:134*/return this.totalUpdatedColumns;
    }
    
    public BatchUpdateValuesResponse setTotalUpdatedColumns(final Integer totalUpdatedColumns) {
        /*SL:142*/this.totalUpdatedColumns = totalUpdatedColumns;
        /*SL:143*/return this;
    }
    
    public Integer getTotalUpdatedRows() {
        /*SL:151*/return this.totalUpdatedRows;
    }
    
    public BatchUpdateValuesResponse setTotalUpdatedRows(final Integer totalUpdatedRows) {
        /*SL:159*/this.totalUpdatedRows = totalUpdatedRows;
        /*SL:160*/return this;
    }
    
    public Integer getTotalUpdatedSheets() {
        /*SL:168*/return this.totalUpdatedSheets;
    }
    
    public BatchUpdateValuesResponse setTotalUpdatedSheets(final Integer totalUpdatedSheets) {
        /*SL:176*/this.totalUpdatedSheets = totalUpdatedSheets;
        /*SL:177*/return this;
    }
    
    public BatchUpdateValuesResponse set(final String a1, final Object a2) {
        /*SL:182*/return (BatchUpdateValuesResponse)super.set(a1, a2);
    }
    
    public BatchUpdateValuesResponse clone() {
        /*SL:187*/return (BatchUpdateValuesResponse)super.clone();
    }
}
