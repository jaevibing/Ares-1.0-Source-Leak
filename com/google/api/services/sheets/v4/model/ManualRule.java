package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class ManualRule extends GenericJson
{
    @Key
    private List<ManualRuleGroup> groups;
    
    public List<ManualRuleGroup> getGroups() {
        /*SL:63*/return this.groups;
    }
    
    public ManualRule setGroups(final List<ManualRuleGroup> groups) {
        /*SL:72*/this.groups = groups;
        /*SL:73*/return this;
    }
    
    public ManualRule set(final String a1, final Object a2) {
        /*SL:78*/return (ManualRule)super.set(a1, a2);
    }
    
    public ManualRule clone() {
        /*SL:83*/return (ManualRule)super.clone();
    }
}
