package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DataValidationRule extends GenericJson
{
    @Key
    private BooleanCondition condition;
    @Key
    private String inputMessage;
    @Key
    private Boolean showCustomUi;
    @Key
    private Boolean strict;
    
    public BooleanCondition getCondition() {
        /*SL:70*/return this.condition;
    }
    
    public DataValidationRule setCondition(final BooleanCondition condition) {
        /*SL:78*/this.condition = condition;
        /*SL:79*/return this;
    }
    
    public String getInputMessage() {
        /*SL:87*/return this.inputMessage;
    }
    
    public DataValidationRule setInputMessage(final String inputMessage) {
        /*SL:95*/this.inputMessage = inputMessage;
        /*SL:96*/return this;
    }
    
    public Boolean getShowCustomUi() {
        /*SL:105*/return this.showCustomUi;
    }
    
    public DataValidationRule setShowCustomUi(final Boolean showCustomUi) {
        /*SL:114*/this.showCustomUi = showCustomUi;
        /*SL:115*/return this;
    }
    
    public Boolean getStrict() {
        /*SL:123*/return this.strict;
    }
    
    public DataValidationRule setStrict(final Boolean strict) {
        /*SL:131*/this.strict = strict;
        /*SL:132*/return this;
    }
    
    public DataValidationRule set(final String a1, final Object a2) {
        /*SL:137*/return (DataValidationRule)super.set(a1, a2);
    }
    
    public DataValidationRule clone() {
        /*SL:142*/return (DataValidationRule)super.clone();
    }
}
