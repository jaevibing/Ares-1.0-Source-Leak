package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BooleanCondition extends GenericJson
{
    @Key
    private String type;
    @Key
    private List<ConditionValue> values;
    
    public String getType() {
        /*SL:58*/return this.type;
    }
    
    public BooleanCondition setType(final String type) {
        /*SL:66*/this.type = type;
        /*SL:67*/return this;
    }
    
    public List<ConditionValue> getValues() {
        /*SL:77*/return this.values;
    }
    
    public BooleanCondition setValues(final List<ConditionValue> values) {
        /*SL:87*/this.values = values;
        /*SL:88*/return this;
    }
    
    public BooleanCondition set(final String a1, final Object a2) {
        /*SL:93*/return (BooleanCondition)super.set(a1, a2);
    }
    
    public BooleanCondition clone() {
        /*SL:98*/return (BooleanCondition)super.clone();
    }
}
