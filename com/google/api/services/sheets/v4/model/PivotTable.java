package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.Map;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class PivotTable extends GenericJson
{
    @Key
    private List<PivotGroup> columns;
    @Key
    private Map<String, PivotFilterCriteria> criteria;
    @Key
    private List<PivotGroup> rows;
    @Key
    private GridRange source;
    @Key
    private String valueLayout;
    @Key
    private List<PivotValue> values;
    
    public List<PivotGroup> getColumns() {
        /*SL:114*/return this.columns;
    }
    
    public PivotTable setColumns(final List<PivotGroup> columns) {
        /*SL:122*/this.columns = columns;
        /*SL:123*/return this;
    }
    
    public Map<String, PivotFilterCriteria> getCriteria() {
        /*SL:138*/return this.criteria;
    }
    
    public PivotTable setCriteria(final Map<String, PivotFilterCriteria> criteria) {
        /*SL:153*/this.criteria = criteria;
        /*SL:154*/return this;
    }
    
    public List<PivotGroup> getRows() {
        /*SL:162*/return this.rows;
    }
    
    public PivotTable setRows(final List<PivotGroup> rows) {
        /*SL:170*/this.rows = rows;
        /*SL:171*/return this;
    }
    
    public GridRange getSource() {
        /*SL:179*/return this.source;
    }
    
    public PivotTable setSource(final GridRange source) {
        /*SL:187*/this.source = source;
        /*SL:188*/return this;
    }
    
    public String getValueLayout() {
        /*SL:196*/return this.valueLayout;
    }
    
    public PivotTable setValueLayout(final String valueLayout) {
        /*SL:204*/this.valueLayout = valueLayout;
        /*SL:205*/return this;
    }
    
    public List<PivotValue> getValues() {
        /*SL:213*/return this.values;
    }
    
    public PivotTable setValues(final List<PivotValue> values) {
        /*SL:221*/this.values = values;
        /*SL:222*/return this;
    }
    
    public PivotTable set(final String a1, final Object a2) {
        /*SL:227*/return (PivotTable)super.set(a1, a2);
    }
    
    public PivotTable clone() {
        /*SL:232*/return (PivotTable)super.clone();
    }
    
    static {
        Data.<Object>nullOf(PivotGroup.class);
        Data.<Object>nullOf(PivotFilterCriteria.class);
        Data.<Object>nullOf(PivotGroup.class);
        Data.<Object>nullOf(PivotValue.class);
    }
}
