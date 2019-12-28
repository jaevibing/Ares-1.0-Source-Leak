package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateConditionalFormatRuleResponse extends GenericJson
{
    @Key
    private Integer newIndex;
    @Key
    private ConditionalFormatRule newRule;
    @Key
    private Integer oldIndex;
    @Key
    private ConditionalFormatRule oldRule;
    
    public Integer getNewIndex() {
        /*SL:70*/return this.newIndex;
    }
    
    public UpdateConditionalFormatRuleResponse setNewIndex(final Integer newIndex) {
        /*SL:78*/this.newIndex = newIndex;
        /*SL:79*/return this;
    }
    
    public ConditionalFormatRule getNewRule() {
        /*SL:87*/return this.newRule;
    }
    
    public UpdateConditionalFormatRuleResponse setNewRule(final ConditionalFormatRule newRule) {
        /*SL:95*/this.newRule = newRule;
        /*SL:96*/return this;
    }
    
    public Integer getOldIndex() {
        /*SL:105*/return this.oldIndex;
    }
    
    public UpdateConditionalFormatRuleResponse setOldIndex(final Integer oldIndex) {
        /*SL:114*/this.oldIndex = oldIndex;
        /*SL:115*/return this;
    }
    
    public ConditionalFormatRule getOldRule() {
        /*SL:123*/return this.oldRule;
    }
    
    public UpdateConditionalFormatRuleResponse setOldRule(final ConditionalFormatRule oldRule) {
        /*SL:131*/this.oldRule = oldRule;
        /*SL:132*/return this;
    }
    
    public UpdateConditionalFormatRuleResponse set(final String a1, final Object a2) {
        /*SL:137*/return (UpdateConditionalFormatRuleResponse)super.set(a1, a2);
    }
    
    public UpdateConditionalFormatRuleResponse clone() {
        /*SL:142*/return (UpdateConditionalFormatRuleResponse)super.clone();
    }
}
