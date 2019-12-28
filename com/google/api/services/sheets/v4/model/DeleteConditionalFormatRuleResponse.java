package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteConditionalFormatRuleResponse extends GenericJson
{
    @Key
    private ConditionalFormatRule rule;
    
    public ConditionalFormatRule getRule() {
        /*SL:48*/return this.rule;
    }
    
    public DeleteConditionalFormatRuleResponse setRule(final ConditionalFormatRule rule) {
        /*SL:56*/this.rule = rule;
        /*SL:57*/return this;
    }
    
    public DeleteConditionalFormatRuleResponse set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteConditionalFormatRuleResponse)super.set(a1, a2);
    }
    
    public DeleteConditionalFormatRuleResponse clone() {
        /*SL:67*/return (DeleteConditionalFormatRuleResponse)super.clone();
    }
}
