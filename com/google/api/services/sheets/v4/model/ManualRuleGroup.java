package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ManualRuleGroup extends GenericJson
{
    @Key
    private ExtendedValue groupName;
    @Key
    private List<ExtendedValue> items;
    
    public ExtendedValue getGroupName() {
        /*SL:66*/return this.groupName;
    }
    
    public ManualRuleGroup setGroupName(final ExtendedValue groupName) {
        /*SL:75*/this.groupName = groupName;
        /*SL:76*/return this;
    }
    
    public List<ExtendedValue> getItems() {
        /*SL:86*/return this.items;
    }
    
    public ManualRuleGroup setItems(final List<ExtendedValue> items) {
        /*SL:96*/this.items = items;
        /*SL:97*/return this;
    }
    
    public ManualRuleGroup set(final String a1, final Object a2) {
        /*SL:102*/return (ManualRuleGroup)super.set(a1, a2);
    }
    
    public ManualRuleGroup clone() {
        /*SL:107*/return (ManualRuleGroup)super.clone();
    }
    
    static {
        Data.<Object>nullOf(ExtendedValue.class);
    }
}
