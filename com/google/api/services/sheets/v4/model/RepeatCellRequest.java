package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class RepeatCellRequest extends GenericJson
{
    @Key
    private CellData cell;
    @Key
    private String fields;
    @Key
    private GridRange range;
    
    public CellData getCell() {
        /*SL:73*/return this.cell;
    }
    
    public RepeatCellRequest setCell(final CellData cell) {
        /*SL:81*/this.cell = cell;
        /*SL:82*/return this;
    }
    
    public String getFields() {
        /*SL:92*/return this.fields;
    }
    
    public RepeatCellRequest setFields(final String fields) {
        /*SL:102*/this.fields = fields;
        /*SL:103*/return this;
    }
    
    public GridRange getRange() {
        /*SL:111*/return this.range;
    }
    
    public RepeatCellRequest setRange(final GridRange range) {
        /*SL:119*/this.range = range;
        /*SL:120*/return this;
    }
    
    public RepeatCellRequest set(final String a1, final Object a2) {
        /*SL:125*/return (RepeatCellRequest)super.set(a1, a2);
    }
    
    public RepeatCellRequest clone() {
        /*SL:130*/return (RepeatCellRequest)super.clone();
    }
}
