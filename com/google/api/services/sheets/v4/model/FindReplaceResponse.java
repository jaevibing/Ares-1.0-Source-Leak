package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class FindReplaceResponse extends GenericJson
{
    @Key
    private Integer formulasChanged;
    @Key
    private Integer occurrencesChanged;
    @Key
    private Integer rowsChanged;
    @Key
    private Integer sheetsChanged;
    @Key
    private Integer valuesChanged;
    
    public Integer getFormulasChanged() {
        /*SL:78*/return this.formulasChanged;
    }
    
    public FindReplaceResponse setFormulasChanged(final Integer formulasChanged) {
        /*SL:86*/this.formulasChanged = formulasChanged;
        /*SL:87*/return this;
    }
    
    public Integer getOccurrencesChanged() {
        /*SL:97*/return this.occurrencesChanged;
    }
    
    public FindReplaceResponse setOccurrencesChanged(final Integer occurrencesChanged) {
        /*SL:107*/this.occurrencesChanged = occurrencesChanged;
        /*SL:108*/return this;
    }
    
    public Integer getRowsChanged() {
        /*SL:116*/return this.rowsChanged;
    }
    
    public FindReplaceResponse setRowsChanged(final Integer rowsChanged) {
        /*SL:124*/this.rowsChanged = rowsChanged;
        /*SL:125*/return this;
    }
    
    public Integer getSheetsChanged() {
        /*SL:133*/return this.sheetsChanged;
    }
    
    public FindReplaceResponse setSheetsChanged(final Integer sheetsChanged) {
        /*SL:141*/this.sheetsChanged = sheetsChanged;
        /*SL:142*/return this;
    }
    
    public Integer getValuesChanged() {
        /*SL:150*/return this.valuesChanged;
    }
    
    public FindReplaceResponse setValuesChanged(final Integer valuesChanged) {
        /*SL:158*/this.valuesChanged = valuesChanged;
        /*SL:159*/return this;
    }
    
    public FindReplaceResponse set(final String a1, final Object a2) {
        /*SL:164*/return (FindReplaceResponse)super.set(a1, a2);
    }
    
    public FindReplaceResponse clone() {
        /*SL:169*/return (FindReplaceResponse)super.clone();
    }
}
