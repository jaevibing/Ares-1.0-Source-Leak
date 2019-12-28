package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateEmbeddedObjectPositionResponse extends GenericJson
{
    @Key
    private EmbeddedObjectPosition position;
    
    public EmbeddedObjectPosition getPosition() {
        /*SL:48*/return this.position;
    }
    
    public UpdateEmbeddedObjectPositionResponse setPosition(final EmbeddedObjectPosition position) {
        /*SL:56*/this.position = position;
        /*SL:57*/return this;
    }
    
    public UpdateEmbeddedObjectPositionResponse set(final String a1, final Object a2) {
        /*SL:62*/return (UpdateEmbeddedObjectPositionResponse)super.set(a1, a2);
    }
    
    public UpdateEmbeddedObjectPositionResponse clone() {
        /*SL:67*/return (UpdateEmbeddedObjectPositionResponse)super.clone();
    }
}
