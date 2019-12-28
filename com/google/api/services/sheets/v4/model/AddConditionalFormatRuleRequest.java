package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddConditionalFormatRuleRequest extends GenericJson
{
    @Key
    private Integer index;
    @Key
    private ConditionalFormatRule rule;
    
    public Integer getIndex() {
        /*SL:56*/return this.index;
    }
    
    public AddConditionalFormatRuleRequest setIndex(final Integer index) {
        /*SL:64*/this.index = index;
        /*SL:65*/return this;
    }
    
    public ConditionalFormatRule getRule() {
        /*SL:73*/return this.rule;
    }
    
    public AddConditionalFormatRuleRequest setRule(final ConditionalFormatRule rule) {
        /*SL:81*/this.rule = rule;
        /*SL:82*/return this;
    }
    
    public AddConditionalFormatRuleRequest set(final String a1, final Object a2) {
        /*SL:87*/return (AddConditionalFormatRuleRequest)super.set(a1, a2);
    }
    
    public AddConditionalFormatRuleRequest clone() {
        /*SL:92*/return (AddConditionalFormatRuleRequest)super.clone();
    }
}
