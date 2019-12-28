package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PivotValue extends GenericJson
{
    @Key
    private String calculatedDisplayType;
    @Key
    private String formula;
    @Key
    private String name;
    @Key
    private Integer sourceColumnOffset;
    @Key
    private String summarizeFunction;
    
    public String getCalculatedDisplayType() {
        /*SL:88*/return this.calculatedDisplayType;
    }
    
    public PivotValue setCalculatedDisplayType(final String calculatedDisplayType) {
        /*SL:100*/this.calculatedDisplayType = calculatedDisplayType;
        /*SL:101*/return this;
    }
    
    public String getFormula() {
        /*SL:109*/return this.formula;
    }
    
    public PivotValue setFormula(final String formula) {
        /*SL:117*/this.formula = formula;
        /*SL:118*/return this;
    }
    
    public String getName() {
        /*SL:126*/return this.name;
    }
    
    public PivotValue setName(final String name) {
        /*SL:134*/this.name = name;
        /*SL:135*/return this;
    }
    
    public Integer getSourceColumnOffset() {
        /*SL:146*/return this.sourceColumnOffset;
    }
    
    public PivotValue setSourceColumnOffset(final Integer sourceColumnOffset) {
        /*SL:157*/this.sourceColumnOffset = sourceColumnOffset;
        /*SL:158*/return this;
    }
    
    public String getSummarizeFunction() {
        /*SL:167*/return this.summarizeFunction;
    }
    
    public PivotValue setSummarizeFunction(final String summarizeFunction) {
        /*SL:176*/this.summarizeFunction = summarizeFunction;
        /*SL:177*/return this;
    }
    
    public PivotValue set(final String a1, final Object a2) {
        /*SL:182*/return (PivotValue)super.set(a1, a2);
    }
    
    public PivotValue clone() {
        /*SL:187*/return (PivotValue)super.clone();
    }
}
