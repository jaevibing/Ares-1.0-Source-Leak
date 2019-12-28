package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ExtendedValue extends GenericJson
{
    @Key
    private Boolean boolValue;
    @Key
    private ErrorValue errorValue;
    @Key
    private String formulaValue;
    @Key
    private Double numberValue;
    @Key
    private String stringValue;
    
    public Boolean getBoolValue() {
        /*SL:78*/return this.boolValue;
    }
    
    public ExtendedValue setBoolValue(final Boolean boolValue) {
        /*SL:86*/this.boolValue = boolValue;
        /*SL:87*/return this;
    }
    
    public ErrorValue getErrorValue() {
        /*SL:95*/return this.errorValue;
    }
    
    public ExtendedValue setErrorValue(final ErrorValue errorValue) {
        /*SL:103*/this.errorValue = errorValue;
        /*SL:104*/return this;
    }
    
    public String getFormulaValue() {
        /*SL:112*/return this.formulaValue;
    }
    
    public ExtendedValue setFormulaValue(final String formulaValue) {
        /*SL:120*/this.formulaValue = formulaValue;
        /*SL:121*/return this;
    }
    
    public Double getNumberValue() {
        /*SL:130*/return this.numberValue;
    }
    
    public ExtendedValue setNumberValue(final Double numberValue) {
        /*SL:139*/this.numberValue = numberValue;
        /*SL:140*/return this;
    }
    
    public String getStringValue() {
        /*SL:149*/return this.stringValue;
    }
    
    public ExtendedValue setStringValue(final String stringValue) {
        /*SL:158*/this.stringValue = stringValue;
        /*SL:159*/return this;
    }
    
    public ExtendedValue set(final String a1, final Object a2) {
        /*SL:164*/return (ExtendedValue)super.set(a1, a2);
    }
    
    public ExtendedValue clone() {
        /*SL:169*/return (ExtendedValue)super.clone();
    }
}
