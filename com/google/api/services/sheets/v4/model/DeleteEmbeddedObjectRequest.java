package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteEmbeddedObjectRequest extends GenericJson
{
    @Key
    private Integer objectId;
    
    public Integer getObjectId() {
        /*SL:48*/return this.objectId;
    }
    
    public DeleteEmbeddedObjectRequest setObjectId(final Integer objectId) {
        /*SL:56*/this.objectId = objectId;
        /*SL:57*/return this;
    }
    
    public DeleteEmbeddedObjectRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteEmbeddedObjectRequest)super.set(a1, a2);
    }
    
    public DeleteEmbeddedObjectRequest clone() {
        /*SL:67*/return (DeleteEmbeddedObjectRequest)super.clone();
    }
}
