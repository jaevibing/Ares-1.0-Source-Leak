package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CellData extends GenericJson
{
    @Key
    private DataValidationRule dataValidation;
    @Key
    private CellFormat effectiveFormat;
    @Key
    private ExtendedValue effectiveValue;
    @Key
    private String formattedValue;
    @Key
    private String hyperlink;
    @Key
    private String note;
    @Key
    private PivotTable pivotTable;
    @Key
    private List<TextFormatRun> textFormatRuns;
    @Key
    private CellFormat userEnteredFormat;
    @Key
    private ExtendedValue userEnteredValue;
    
    public DataValidationRule getDataValidation() {
        /*SL:140*/return this.dataValidation;
    }
    
    public CellData setDataValidation(final DataValidationRule dataValidation) {
        /*SL:150*/this.dataValidation = dataValidation;
        /*SL:151*/return this;
    }
    
    public CellFormat getEffectiveFormat() {
        /*SL:162*/return this.effectiveFormat;
    }
    
    public CellData setEffectiveFormat(final CellFormat effectiveFormat) {
        /*SL:173*/this.effectiveFormat = effectiveFormat;
        /*SL:174*/return this;
    }
    
    public ExtendedValue getEffectiveValue() {
        /*SL:183*/return this.effectiveValue;
    }
    
    public CellData setEffectiveValue(final ExtendedValue effectiveValue) {
        /*SL:192*/this.effectiveValue = effectiveValue;
        /*SL:193*/return this;
    }
    
    public String getFormattedValue() {
        /*SL:202*/return this.formattedValue;
    }
    
    public CellData setFormattedValue(final String formattedValue) {
        /*SL:211*/this.formattedValue = formattedValue;
        /*SL:212*/return this;
    }
    
    public String getHyperlink() {
        /*SL:221*/return this.hyperlink;
    }
    
    public CellData setHyperlink(final String hyperlink) {
        /*SL:230*/this.hyperlink = hyperlink;
        /*SL:231*/return this;
    }
    
    public String getNote() {
        /*SL:239*/return this.note;
    }
    
    public CellData setNote(final String note) {
        /*SL:247*/this.note = note;
        /*SL:248*/return this;
    }
    
    public PivotTable getPivotTable() {
        /*SL:259*/return this.pivotTable;
    }
    
    public CellData setPivotTable(final PivotTable pivotTable) {
        /*SL:270*/this.pivotTable = pivotTable;
        /*SL:271*/return this;
    }
    
    public List<TextFormatRun> getTextFormatRuns() {
        /*SL:286*/return this.textFormatRuns;
    }
    
    public CellData setTextFormatRuns(final List<TextFormatRun> textFormatRuns) {
        /*SL:301*/this.textFormatRuns = textFormatRuns;
        /*SL:302*/return this;
    }
    
    public CellFormat getUserEnteredFormat() {
        /*SL:312*/return this.userEnteredFormat;
    }
    
    public CellData setUserEnteredFormat(final CellFormat userEnteredFormat) {
        /*SL:322*/this.userEnteredFormat = userEnteredFormat;
        /*SL:323*/return this;
    }
    
    public ExtendedValue getUserEnteredValue() {
        /*SL:332*/return this.userEnteredValue;
    }
    
    public CellData setUserEnteredValue(final ExtendedValue userEnteredValue) {
        /*SL:341*/this.userEnteredValue = userEnteredValue;
        /*SL:342*/return this;
    }
    
    public CellData set(final String a1, final Object a2) {
        /*SL:347*/return (CellData)super.set(a1, a2);
    }
    
    public CellData clone() {
        /*SL:352*/return (CellData)super.clone();
    }
    
    static {
        Data.<Object>nullOf(TextFormatRun.class);
    }
}
