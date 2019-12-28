package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DataFilter extends GenericJson
{
    @Key
    private String a1Range;
    @Key
    private DeveloperMetadataLookup developerMetadataLookup;
    @Key
    private GridRange gridRange;
    
    public String getA1Range() {
        /*SL:63*/return this.a1Range;
    }
    
    public DataFilter setA1Range(final String a1Range) {
        /*SL:71*/this.a1Range = a1Range;
        /*SL:72*/return this;
    }
    
    public DeveloperMetadataLookup getDeveloperMetadataLookup() {
        /*SL:81*/return this.developerMetadataLookup;
    }
    
    public DataFilter setDeveloperMetadataLookup(final DeveloperMetadataLookup developerMetadataLookup) {
        /*SL:90*/this.developerMetadataLookup = developerMetadataLookup;
        /*SL:91*/return this;
    }
    
    public GridRange getGridRange() {
        /*SL:99*/return this.gridRange;
    }
    
    public DataFilter setGridRange(final GridRange gridRange) {
        /*SL:107*/this.gridRange = gridRange;
        /*SL:108*/return this;
    }
    
    public DataFilter set(final String a1, final Object a2) {
        /*SL:113*/return (DataFilter)super.set(a1, a2);
    }
    
    public DataFilter clone() {
        /*SL:118*/return (DataFilter)super.clone();
    }
}
