package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CutPasteRequest extends GenericJson
{
    @Key
    private GridCoordinate destination;
    @Key
    private String pasteType;
    @Key
    private GridRange source;
    
    public GridCoordinate getDestination() {
        /*SL:62*/return this.destination;
    }
    
    public CutPasteRequest setDestination(final GridCoordinate destination) {
        /*SL:70*/this.destination = destination;
        /*SL:71*/return this;
    }
    
    public String getPasteType() {
        /*SL:79*/return this.pasteType;
    }
    
    public CutPasteRequest setPasteType(final String pasteType) {
        /*SL:87*/this.pasteType = pasteType;
        /*SL:88*/return this;
    }
    
    public GridRange getSource() {
        /*SL:96*/return this.source;
    }
    
    public CutPasteRequest setSource(final GridRange source) {
        /*SL:104*/this.source = source;
        /*SL:105*/return this;
    }
    
    public CutPasteRequest set(final String a1, final Object a2) {
        /*SL:110*/return (CutPasteRequest)super.set(a1, a2);
    }
    
    public CutPasteRequest clone() {
        /*SL:115*/return (CutPasteRequest)super.clone();
    }
}
