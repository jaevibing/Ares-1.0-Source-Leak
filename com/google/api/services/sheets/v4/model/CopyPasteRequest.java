package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CopyPasteRequest extends GenericJson
{
    @Key
    private GridRange destination;
    @Key
    private String pasteOrientation;
    @Key
    private String pasteType;
    @Key
    private GridRange source;
    
    public GridRange getDestination() {
        /*SL:75*/return this.destination;
    }
    
    public CopyPasteRequest setDestination(final GridRange destination) {
        /*SL:86*/this.destination = destination;
        /*SL:87*/return this;
    }
    
    public String getPasteOrientation() {
        /*SL:95*/return this.pasteOrientation;
    }
    
    public CopyPasteRequest setPasteOrientation(final String pasteOrientation) {
        /*SL:103*/this.pasteOrientation = pasteOrientation;
        /*SL:104*/return this;
    }
    
    public String getPasteType() {
        /*SL:112*/return this.pasteType;
    }
    
    public CopyPasteRequest setPasteType(final String pasteType) {
        /*SL:120*/this.pasteType = pasteType;
        /*SL:121*/return this;
    }
    
    public GridRange getSource() {
        /*SL:129*/return this.source;
    }
    
    public CopyPasteRequest setSource(final GridRange source) {
        /*SL:137*/this.source = source;
        /*SL:138*/return this;
    }
    
    public CopyPasteRequest set(final String a1, final Object a2) {
        /*SL:143*/return (CopyPasteRequest)super.set(a1, a2);
    }
    
    public CopyPasteRequest clone() {
        /*SL:148*/return (CopyPasteRequest)super.clone();
    }
}
