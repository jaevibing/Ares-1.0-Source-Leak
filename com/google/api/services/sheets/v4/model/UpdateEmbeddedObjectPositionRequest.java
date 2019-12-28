package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateEmbeddedObjectPositionRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private EmbeddedObjectPosition newPosition;
    @Key
    private Integer objectId;
    
    public String getFields() {
        /*SL:70*/return this.fields;
    }
    
    public UpdateEmbeddedObjectPositionRequest setFields(final String fields) {
        /*SL:81*/this.fields = fields;
        /*SL:82*/return this;
    }
    
    public EmbeddedObjectPosition getNewPosition() {
        /*SL:92*/return this.newPosition;
    }
    
    public UpdateEmbeddedObjectPositionRequest setNewPosition(final EmbeddedObjectPosition newPosition) {
        /*SL:102*/this.newPosition = newPosition;
        /*SL:103*/return this;
    }
    
    public Integer getObjectId() {
        /*SL:111*/return this.objectId;
    }
    
    public UpdateEmbeddedObjectPositionRequest setObjectId(final Integer objectId) {
        /*SL:119*/this.objectId = objectId;
        /*SL:120*/return this;
    }
    
    public UpdateEmbeddedObjectPositionRequest set(final String a1, final Object a2) {
        /*SL:125*/return (UpdateEmbeddedObjectPositionRequest)super.set(a1, a2);
    }
    
    public UpdateEmbeddedObjectPositionRequest clone() {
        /*SL:130*/return (UpdateEmbeddedObjectPositionRequest)super.clone();
    }
}
