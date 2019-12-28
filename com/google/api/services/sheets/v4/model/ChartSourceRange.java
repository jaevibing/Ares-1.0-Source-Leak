package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class ChartSourceRange extends GenericJson
{
    @Key
    private List<GridRange> sources;
    
    public List<GridRange> getSources() {
        /*SL:76*/return this.sources;
    }
    
    public ChartSourceRange setSources(final List<GridRange> sources) {
        /*SL:95*/this.sources = sources;
        /*SL:96*/return this;
    }
    
    public ChartSourceRange set(final String a1, final Object a2) {
        /*SL:101*/return (ChartSourceRange)super.set(a1, a2);
    }
    
    public ChartSourceRange clone() {
        /*SL:106*/return (ChartSourceRange)super.clone();
    }
    
    static {
        Data.<Object>nullOf(GridRange.class);
    }
}
