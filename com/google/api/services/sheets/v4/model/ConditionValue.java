package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ConditionValue extends GenericJson
{
    @Key
    private String relativeDate;
    @Key
    private String userEnteredValue;
    
    public String getRelativeDate() {
        /*SL:64*/return this.relativeDate;
    }
    
    public ConditionValue setRelativeDate(final String relativeDate) {
        /*SL:76*/this.relativeDate = relativeDate;
        /*SL:77*/return this;
    }
    
    public String getUserEnteredValue() {
        /*SL:86*/return this.userEnteredValue;
    }
    
    public ConditionValue setUserEnteredValue(final String userEnteredValue) {
        /*SL:95*/this.userEnteredValue = userEnteredValue;
        /*SL:96*/return this;
    }
    
    public ConditionValue set(final String a1, final Object a2) {
        /*SL:101*/return (ConditionValue)super.set(a1, a2);
    }
    
    public ConditionValue clone() {
        /*SL:106*/return (ConditionValue)super.clone();
    }
}
