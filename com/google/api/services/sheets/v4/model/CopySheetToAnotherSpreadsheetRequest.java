package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CopySheetToAnotherSpreadsheetRequest extends GenericJson
{
    @Key
    private String destinationSpreadsheetId;
    
    public String getDestinationSpreadsheetId() {
        /*SL:48*/return this.destinationSpreadsheetId;
    }
    
    public CopySheetToAnotherSpreadsheetRequest setDestinationSpreadsheetId(final String destinationSpreadsheetId) {
        /*SL:56*/this.destinationSpreadsheetId = destinationSpreadsheetId;
        /*SL:57*/return this;
    }
    
    public CopySheetToAnotherSpreadsheetRequest set(final String a1, final Object a2) {
        /*SL:62*/return (CopySheetToAnotherSpreadsheetRequest)super.set(a1, a2);
    }
    
    public CopySheetToAnotherSpreadsheetRequest clone() {
        /*SL:67*/return (CopySheetToAnotherSpreadsheetRequest)super.clone();
    }
}
