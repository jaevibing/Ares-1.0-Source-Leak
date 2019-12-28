package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class GridData extends GenericJson
{
    @Key
    private List<DimensionProperties> columnMetadata;
    @Key
    private List<RowData> rowData;
    @Key
    private List<DimensionProperties> rowMetadata;
    @Key
    private Integer startColumn;
    @Key
    private Integer startRow;
    
    public List<DimensionProperties> getColumnMetadata() {
        /*SL:83*/return this.columnMetadata;
    }
    
    public GridData setColumnMetadata(final List<DimensionProperties> columnMetadata) {
        /*SL:91*/this.columnMetadata = columnMetadata;
        /*SL:92*/return this;
    }
    
    public List<RowData> getRowData() {
        /*SL:101*/return this.rowData;
    }
    
    public GridData setRowData(final List<RowData> rowData) {
        /*SL:110*/this.rowData = rowData;
        /*SL:111*/return this;
    }
    
    public List<DimensionProperties> getRowMetadata() {
        /*SL:119*/return this.rowMetadata;
    }
    
    public GridData setRowMetadata(final List<DimensionProperties> rowMetadata) {
        /*SL:127*/this.rowMetadata = rowMetadata;
        /*SL:128*/return this;
    }
    
    public Integer getStartColumn() {
        /*SL:136*/return this.startColumn;
    }
    
    public GridData setStartColumn(final Integer startColumn) {
        /*SL:144*/this.startColumn = startColumn;
        /*SL:145*/return this;
    }
    
    public Integer getStartRow() {
        /*SL:153*/return this.startRow;
    }
    
    public GridData setStartRow(final Integer startRow) {
        /*SL:161*/this.startRow = startRow;
        /*SL:162*/return this;
    }
    
    public GridData set(final String a1, final Object a2) {
        /*SL:167*/return (GridData)super.set(a1, a2);
    }
    
    public GridData clone() {
        /*SL:172*/return (GridData)super.clone();
    }
    
    static {
        Data.<Object>nullOf(RowData.class);
    }
}
