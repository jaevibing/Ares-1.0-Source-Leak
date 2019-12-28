package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteConditionalFormatRuleRequest extends GenericJson
{
    @Key
    private Integer index;
    @Key
    private Integer sheetId;
    
    public Integer getIndex() {
        /*SL:56*/return this.index;
    }
    
    public DeleteConditionalFormatRuleRequest setIndex(final Integer index) {
        /*SL:64*/this.index = index;
        /*SL:65*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:73*/return this.sheetId;
    }
    
    public DeleteConditionalFormatRuleRequest setSheetId(final Integer sheetId) {
        /*SL:81*/this.sheetId = sheetId;
        /*SL:82*/return this;
    }
    
    public DeleteConditionalFormatRuleRequest set(final String a1, final Object a2) {
        /*SL:87*/return (DeleteConditionalFormatRuleRequest)super.set(a1, a2);
    }
    
    public DeleteConditionalFormatRuleRequest clone() {
        /*SL:92*/return (DeleteConditionalFormatRuleRequest)super.clone();
    }
}
