package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class RowData extends GenericJson
{
    @Key
    private List<CellData> values;
    
    public List<CellData> getValues() {
        /*SL:48*/return this.values;
    }
    
    public RowData setValues(final List<CellData> values) {
        /*SL:56*/this.values = values;
        /*SL:57*/return this;
    }
    
    public RowData set(final String a1, final Object a2) {
        /*SL:62*/return (RowData)super.set(a1, a2);
    }
    
    public RowData clone() {
        /*SL:67*/return (RowData)super.clone();
    }
}
