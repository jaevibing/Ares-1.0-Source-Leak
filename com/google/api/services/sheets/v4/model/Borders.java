package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Borders extends GenericJson
{
    @Key
    private Border bottom;
    @Key
    private Border left;
    @Key
    private Border right;
    @Key
    private Border top;
    
    public Border getBottom() {
        /*SL:69*/return this.bottom;
    }
    
    public Borders setBottom(final Border bottom) {
        /*SL:77*/this.bottom = bottom;
        /*SL:78*/return this;
    }
    
    public Border getLeft() {
        /*SL:86*/return this.left;
    }
    
    public Borders setLeft(final Border left) {
        /*SL:94*/this.left = left;
        /*SL:95*/return this;
    }
    
    public Border getRight() {
        /*SL:103*/return this.right;
    }
    
    public Borders setRight(final Border right) {
        /*SL:111*/this.right = right;
        /*SL:112*/return this;
    }
    
    public Border getTop() {
        /*SL:120*/return this.top;
    }
    
    public Borders setTop(final Border top) {
        /*SL:128*/this.top = top;
        /*SL:129*/return this;
    }
    
    public Borders set(final String a1, final Object a2) {
        /*SL:134*/return (Borders)super.set(a1, a2);
    }
    
    public Borders clone() {
        /*SL:139*/return (Borders)super.clone();
    }
}
