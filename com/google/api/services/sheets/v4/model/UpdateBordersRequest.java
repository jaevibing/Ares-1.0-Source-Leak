package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateBordersRequest extends GenericJson
{
    @Key
    private Border bottom;
    @Key
    private Border innerHorizontal;
    @Key
    private Border innerVertical;
    @Key
    private Border left;
    @Key
    private GridRange range;
    @Key
    private Border right;
    @Key
    private Border top;
    
    public Border getBottom() {
        /*SL:96*/return this.bottom;
    }
    
    public UpdateBordersRequest setBottom(final Border bottom) {
        /*SL:104*/this.bottom = bottom;
        /*SL:105*/return this;
    }
    
    public Border getInnerHorizontal() {
        /*SL:113*/return this.innerHorizontal;
    }
    
    public UpdateBordersRequest setInnerHorizontal(final Border innerHorizontal) {
        /*SL:121*/this.innerHorizontal = innerHorizontal;
        /*SL:122*/return this;
    }
    
    public Border getInnerVertical() {
        /*SL:130*/return this.innerVertical;
    }
    
    public UpdateBordersRequest setInnerVertical(final Border innerVertical) {
        /*SL:138*/this.innerVertical = innerVertical;
        /*SL:139*/return this;
    }
    
    public Border getLeft() {
        /*SL:147*/return this.left;
    }
    
    public UpdateBordersRequest setLeft(final Border left) {
        /*SL:155*/this.left = left;
        /*SL:156*/return this;
    }
    
    public GridRange getRange() {
        /*SL:164*/return this.range;
    }
    
    public UpdateBordersRequest setRange(final GridRange range) {
        /*SL:172*/this.range = range;
        /*SL:173*/return this;
    }
    
    public Border getRight() {
        /*SL:181*/return this.right;
    }
    
    public UpdateBordersRequest setRight(final Border right) {
        /*SL:189*/this.right = right;
        /*SL:190*/return this;
    }
    
    public Border getTop() {
        /*SL:198*/return this.top;
    }
    
    public UpdateBordersRequest setTop(final Border top) {
        /*SL:206*/this.top = top;
        /*SL:207*/return this;
    }
    
    public UpdateBordersRequest set(final String a1, final Object a2) {
        /*SL:212*/return (UpdateBordersRequest)super.set(a1, a2);
    }
    
    public UpdateBordersRequest clone() {
        /*SL:217*/return (UpdateBordersRequest)super.clone();
    }
}
