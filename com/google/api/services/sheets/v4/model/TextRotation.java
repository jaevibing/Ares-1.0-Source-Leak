package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TextRotation extends GenericJson
{
    @Key
    private Integer angle;
    @Key
    private Boolean vertical;
    
    public Integer getAngle() {
        /*SL:68*/return this.angle;
    }
    
    public TextRotation setAngle(final Integer angle) {
        /*SL:81*/this.angle = angle;
        /*SL:82*/return this;
    }
    
    public Boolean getVertical() {
        /*SL:93*/return this.vertical;
    }
    
    public TextRotation setVertical(final Boolean vertical) {
        /*SL:104*/this.vertical = vertical;
        /*SL:105*/return this;
    }
    
    public TextRotation set(final String a1, final Object a2) {
        /*SL:110*/return (TextRotation)super.set(a1, a2);
    }
    
    public TextRotation clone() {
        /*SL:115*/return (TextRotation)super.clone();
    }
}
