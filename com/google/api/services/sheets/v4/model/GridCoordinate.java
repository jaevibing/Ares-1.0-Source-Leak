package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class GridCoordinate extends GenericJson
{
    @Key
    private Integer columnIndex;
    @Key
    private Integer rowIndex;
    @Key
    private Integer sheetId;
    
    public Integer getColumnIndex() {
        /*SL:62*/return this.columnIndex;
    }
    
    public GridCoordinate setColumnIndex(final Integer columnIndex) {
        /*SL:70*/this.columnIndex = columnIndex;
        /*SL:71*/return this;
    }
    
    public Integer getRowIndex() {
        /*SL:79*/return this.rowIndex;
    }
    
    public GridCoordinate setRowIndex(final Integer rowIndex) {
        /*SL:87*/this.rowIndex = rowIndex;
        /*SL:88*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:96*/return this.sheetId;
    }
    
    public GridCoordinate setSheetId(final Integer sheetId) {
        /*SL:104*/this.sheetId = sheetId;
        /*SL:105*/return this;
    }
    
    public GridCoordinate set(final String a1, final Object a2) {
        /*SL:110*/return (GridCoordinate)super.set(a1, a2);
    }
    
    public GridCoordinate clone() {
        /*SL:115*/return (GridCoordinate)super.clone();
    }
}
