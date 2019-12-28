package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class OverlayPosition extends GenericJson
{
    @Key
    private GridCoordinate anchorCell;
    @Key
    private Integer heightPixels;
    @Key
    private Integer offsetXPixels;
    @Key
    private Integer offsetYPixels;
    @Key
    private Integer widthPixels;
    
    public GridCoordinate getAnchorCell() {
        /*SL:76*/return this.anchorCell;
    }
    
    public OverlayPosition setAnchorCell(final GridCoordinate anchorCell) {
        /*SL:84*/this.anchorCell = anchorCell;
        /*SL:85*/return this;
    }
    
    public Integer getHeightPixels() {
        /*SL:93*/return this.heightPixels;
    }
    
    public OverlayPosition setHeightPixels(final Integer heightPixels) {
        /*SL:101*/this.heightPixels = heightPixels;
        /*SL:102*/return this;
    }
    
    public Integer getOffsetXPixels() {
        /*SL:110*/return this.offsetXPixels;
    }
    
    public OverlayPosition setOffsetXPixels(final Integer offsetXPixels) {
        /*SL:118*/this.offsetXPixels = offsetXPixels;
        /*SL:119*/return this;
    }
    
    public Integer getOffsetYPixels() {
        /*SL:127*/return this.offsetYPixels;
    }
    
    public OverlayPosition setOffsetYPixels(final Integer offsetYPixels) {
        /*SL:135*/this.offsetYPixels = offsetYPixels;
        /*SL:136*/return this;
    }
    
    public Integer getWidthPixels() {
        /*SL:144*/return this.widthPixels;
    }
    
    public OverlayPosition setWidthPixels(final Integer widthPixels) {
        /*SL:152*/this.widthPixels = widthPixels;
        /*SL:153*/return this;
    }
    
    public OverlayPosition set(final String a1, final Object a2) {
        /*SL:158*/return (OverlayPosition)super.set(a1, a2);
    }
    
    public OverlayPosition clone() {
        /*SL:163*/return (OverlayPosition)super.clone();
    }
}
