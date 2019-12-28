package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateConditionalFormatRuleRequest extends GenericJson
{
    @Key
    private Integer index;
    @Key
    private Integer newIndex;
    @Key
    private ConditionalFormatRule rule;
    @Key
    private Integer sheetId;
    
    public Integer getIndex() {
        /*SL:70*/return this.index;
    }
    
    public UpdateConditionalFormatRuleRequest setIndex(final Integer index) {
        /*SL:78*/this.index = index;
        /*SL:79*/return this;
    }
    
    public Integer getNewIndex() {
        /*SL:87*/return this.newIndex;
    }
    
    public UpdateConditionalFormatRuleRequest setNewIndex(final Integer newIndex) {
        /*SL:95*/this.newIndex = newIndex;
        /*SL:96*/return this;
    }
    
    public ConditionalFormatRule getRule() {
        /*SL:104*/return this.rule;
    }
    
    public UpdateConditionalFormatRuleRequest setRule(final ConditionalFormatRule rule) {
        /*SL:112*/this.rule = rule;
        /*SL:113*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:121*/return this.sheetId;
    }
    
    public UpdateConditionalFormatRuleRequest setSheetId(final Integer sheetId) {
        /*SL:129*/this.sheetId = sheetId;
        /*SL:130*/return this;
    }
    
    public UpdateConditionalFormatRuleRequest set(final String a1, final Object a2) {
        /*SL:135*/return (UpdateConditionalFormatRuleRequest)super.set(a1, a2);
    }
    
    public UpdateConditionalFormatRuleRequest clone() {
        /*SL:140*/return (UpdateConditionalFormatRuleRequest)super.clone();
    }
}
