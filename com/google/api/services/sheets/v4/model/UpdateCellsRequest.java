package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateCellsRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private GridRange range;
    @Key
    private List<RowData> rows;
    @Key
    private GridCoordinate start;
    
    public String getFields() {
        /*SL:83*/return this.fields;
    }
    
    public UpdateCellsRequest setFields(final String fields) {
        /*SL:93*/this.fields = fields;
        /*SL:94*/return this;
    }
    
    public GridRange getRange() {
        /*SL:105*/return this.range;
    }
    
    public UpdateCellsRequest setRange(final GridRange range) {
        /*SL:116*/this.range = range;
        /*SL:117*/return this;
    }
    
    public List<RowData> getRows() {
        /*SL:125*/return this.rows;
    }
    
    public UpdateCellsRequest setRows(final List<RowData> rows) {
        /*SL:133*/this.rows = rows;
        /*SL:134*/return this;
    }
    
    public GridCoordinate getStart() {
        /*SL:143*/return this.start;
    }
    
    public UpdateCellsRequest setStart(final GridCoordinate start) {
        /*SL:152*/this.start = start;
        /*SL:153*/return this;
    }
    
    public UpdateCellsRequest set(final String a1, final Object a2) {
        /*SL:158*/return (UpdateCellsRequest)super.set(a1, a2);
    }
    
    public UpdateCellsRequest clone() {
        /*SL:163*/return (UpdateCellsRequest)super.clone();
    }
    
    static {
        Data.<Object>nullOf(RowData.class);
    }
}
