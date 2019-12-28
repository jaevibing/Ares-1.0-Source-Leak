package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Padding extends GenericJson
{
    @Key
    private Integer bottom;
    @Key
    private Integer left;
    @Key
    private Integer right;
    @Key
    private Integer top;
    
    public Integer getBottom() {
        /*SL:70*/return this.bottom;
    }
    
    public Padding setBottom(final Integer bottom) {
        /*SL:78*/this.bottom = bottom;
        /*SL:79*/return this;
    }
    
    public Integer getLeft() {
        /*SL:87*/return this.left;
    }
    
    public Padding setLeft(final Integer left) {
        /*SL:95*/this.left = left;
        /*SL:96*/return this;
    }
    
    public Integer getRight() {
        /*SL:104*/return this.right;
    }
    
    public Padding setRight(final Integer right) {
        /*SL:112*/this.right = right;
        /*SL:113*/return this;
    }
    
    public Integer getTop() {
        /*SL:121*/return this.top;
    }
    
    public Padding setTop(final Integer top) {
        /*SL:129*/this.top = top;
        /*SL:130*/return this;
    }
    
    public Padding set(final String a1, final Object a2) {
        /*SL:135*/return (Padding)super.set(a1, a2);
    }
    
    public Padding clone() {
        /*SL:140*/return (Padding)super.clone();
    }
}
