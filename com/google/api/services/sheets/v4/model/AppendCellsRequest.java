package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AppendCellsRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private List<RowData> rows;
    @Key
    private Integer sheetId;
    
    public String getFields() {
        /*SL:73*/return this.fields;
    }
    
    public AppendCellsRequest setFields(final String fields) {
        /*SL:83*/this.fields = fields;
        /*SL:84*/return this;
    }
    
    public List<RowData> getRows() {
        /*SL:92*/return this.rows;
    }
    
    public AppendCellsRequest setRows(final List<RowData> rows) {
        /*SL:100*/this.rows = rows;
        /*SL:101*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:109*/return this.sheetId;
    }
    
    public AppendCellsRequest setSheetId(final Integer sheetId) {
        /*SL:117*/this.sheetId = sheetId;
        /*SL:118*/return this;
    }
    
    public AppendCellsRequest set(final String a1, final Object a2) {
        /*SL:123*/return (AppendCellsRequest)super.set(a1, a2);
    }
    
    public AppendCellsRequest clone() {
        /*SL:128*/return (AppendCellsRequest)super.clone();
    }
    
    static {
        Data.<Object>nullOf(RowData.class);
    }
}
