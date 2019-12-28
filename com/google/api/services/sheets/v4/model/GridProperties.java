package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class GridProperties extends GenericJson
{
    @Key
    private Integer columnCount;
    @Key
    private Boolean columnGroupControlAfter;
    @Key
    private Integer frozenColumnCount;
    @Key
    private Integer frozenRowCount;
    @Key
    private Boolean hideGridlines;
    @Key
    private Integer rowCount;
    @Key
    private Boolean rowGroupControlAfter;
    
    public Integer getColumnCount() {
        /*SL:90*/return this.columnCount;
    }
    
    public GridProperties setColumnCount(final Integer columnCount) {
        /*SL:98*/this.columnCount = columnCount;
        /*SL:99*/return this;
    }
    
    public Boolean getColumnGroupControlAfter() {
        /*SL:107*/return this.columnGroupControlAfter;
    }
    
    public GridProperties setColumnGroupControlAfter(final Boolean columnGroupControlAfter) {
        /*SL:115*/this.columnGroupControlAfter = columnGroupControlAfter;
        /*SL:116*/return this;
    }
    
    public Integer getFrozenColumnCount() {
        /*SL:124*/return this.frozenColumnCount;
    }
    
    public GridProperties setFrozenColumnCount(final Integer frozenColumnCount) {
        /*SL:132*/this.frozenColumnCount = frozenColumnCount;
        /*SL:133*/return this;
    }
    
    public Integer getFrozenRowCount() {
        /*SL:141*/return this.frozenRowCount;
    }
    
    public GridProperties setFrozenRowCount(final Integer frozenRowCount) {
        /*SL:149*/this.frozenRowCount = frozenRowCount;
        /*SL:150*/return this;
    }
    
    public Boolean getHideGridlines() {
        /*SL:158*/return this.hideGridlines;
    }
    
    public GridProperties setHideGridlines(final Boolean hideGridlines) {
        /*SL:166*/this.hideGridlines = hideGridlines;
        /*SL:167*/return this;
    }
    
    public Integer getRowCount() {
        /*SL:175*/return this.rowCount;
    }
    
    public GridProperties setRowCount(final Integer rowCount) {
        /*SL:183*/this.rowCount = rowCount;
        /*SL:184*/return this;
    }
    
    public Boolean getRowGroupControlAfter() {
        /*SL:192*/return this.rowGroupControlAfter;
    }
    
    public GridProperties setRowGroupControlAfter(final Boolean rowGroupControlAfter) {
        /*SL:200*/this.rowGroupControlAfter = rowGroupControlAfter;
        /*SL:201*/return this;
    }
    
    public GridProperties set(final String a1, final Object a2) {
        /*SL:206*/return (GridProperties)super.set(a1, a2);
    }
    
    public GridProperties clone() {
        /*SL:211*/return (GridProperties)super.clone();
    }
}
