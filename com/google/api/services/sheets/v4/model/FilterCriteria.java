package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class FilterCriteria extends GenericJson
{
    @Key
    private BooleanCondition condition;
    @Key
    private List<String> hiddenValues;
    
    public BooleanCondition getCondition() {
        /*SL:57*/return this.condition;
    }
    
    public FilterCriteria setCondition(final BooleanCondition condition) {
        /*SL:66*/this.condition = condition;
        /*SL:67*/return this;
    }
    
    public List<String> getHiddenValues() {
        /*SL:75*/return this.hiddenValues;
    }
    
    public FilterCriteria setHiddenValues(final List<String> hiddenValues) {
        /*SL:83*/this.hiddenValues = hiddenValues;
        /*SL:84*/return this;
    }
    
    public FilterCriteria set(final String a1, final Object a2) {
        /*SL:89*/return (FilterCriteria)super.set(a1, a2);
    }
    
    public FilterCriteria clone() {
        /*SL:94*/return (FilterCriteria)super.clone();
    }
}
