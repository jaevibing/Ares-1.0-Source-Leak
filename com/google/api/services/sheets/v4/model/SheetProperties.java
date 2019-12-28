package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SheetProperties extends GenericJson
{
    @Key
    private GridProperties gridProperties;
    @Key
    private Boolean hidden;
    @Key
    private Integer index;
    @Key
    private Boolean rightToLeft;
    @Key
    private Integer sheetId;
    @Key
    private String sheetType;
    @Key
    private Color tabColor;
    @Key
    private String title;
    
    public GridProperties getGridProperties() {
        /*SL:107*/return this.gridProperties;
    }
    
    public SheetProperties setGridProperties(final GridProperties gridProperties) {
        /*SL:117*/this.gridProperties = gridProperties;
        /*SL:118*/return this;
    }
    
    public Boolean getHidden() {
        /*SL:126*/return this.hidden;
    }
    
    public SheetProperties setHidden(final Boolean hidden) {
        /*SL:134*/this.hidden = hidden;
        /*SL:135*/return this;
    }
    
    public Integer getIndex() {
        /*SL:149*/return this.index;
    }
    
    public SheetProperties setIndex(final Integer index) {
        /*SL:163*/this.index = index;
        /*SL:164*/return this;
    }
    
    public Boolean getRightToLeft() {
        /*SL:172*/return this.rightToLeft;
    }
    
    public SheetProperties setRightToLeft(final Boolean rightToLeft) {
        /*SL:180*/this.rightToLeft = rightToLeft;
        /*SL:181*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:189*/return this.sheetId;
    }
    
    public SheetProperties setSheetId(final Integer sheetId) {
        /*SL:197*/this.sheetId = sheetId;
        /*SL:198*/return this;
    }
    
    public String getSheetType() {
        /*SL:206*/return this.sheetType;
    }
    
    public SheetProperties setSheetType(final String sheetType) {
        /*SL:214*/this.sheetType = sheetType;
        /*SL:215*/return this;
    }
    
    public Color getTabColor() {
        /*SL:223*/return this.tabColor;
    }
    
    public SheetProperties setTabColor(final Color tabColor) {
        /*SL:231*/this.tabColor = tabColor;
        /*SL:232*/return this;
    }
    
    public String getTitle() {
        /*SL:240*/return this.title;
    }
    
    public SheetProperties setTitle(final String title) {
        /*SL:248*/this.title = title;
        /*SL:249*/return this;
    }
    
    public SheetProperties set(final String a1, final Object a2) {
        /*SL:254*/return (SheetProperties)super.set(a1, a2);
    }
    
    public SheetProperties clone() {
        /*SL:259*/return (SheetProperties)super.clone();
    }
}
