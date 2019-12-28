package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class EmbeddedObjectPosition extends GenericJson
{
    @Key
    private Boolean newSheet;
    @Key
    private OverlayPosition overlayPosition;
    @Key
    private Integer sheetId;
    
    public Boolean getNewSheet() {
        /*SL:65*/return this.newSheet;
    }
    
    public EmbeddedObjectPosition setNewSheet(final Boolean newSheet) {
        /*SL:74*/this.newSheet = newSheet;
        /*SL:75*/return this;
    }
    
    public OverlayPosition getOverlayPosition() {
        /*SL:83*/return this.overlayPosition;
    }
    
    public EmbeddedObjectPosition setOverlayPosition(final OverlayPosition overlayPosition) {
        /*SL:91*/this.overlayPosition = overlayPosition;
        /*SL:92*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:101*/return this.sheetId;
    }
    
    public EmbeddedObjectPosition setSheetId(final Integer sheetId) {
        /*SL:110*/this.sheetId = sheetId;
        /*SL:111*/return this;
    }
    
    public EmbeddedObjectPosition set(final String a1, final Object a2) {
        /*SL:116*/return (EmbeddedObjectPosition)super.set(a1, a2);
    }
    
    public EmbeddedObjectPosition clone() {
        /*SL:121*/return (EmbeddedObjectPosition)super.clone();
    }
}
