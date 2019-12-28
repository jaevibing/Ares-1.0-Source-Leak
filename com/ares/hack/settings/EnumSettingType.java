package com.ares.hack.settings;

import java.util.ArrayList;

public enum EnumSettingType
{
    BIND, 
    ENUM, 
    STRING, 
    BOOLEAN, 
    INTEGER, 
    DOUBLE, 
    FLOAT;
    
    protected ArrayList<Setting> settings;
    
    private EnumSettingType(final int n) {
        this.settings = new ArrayList<Setting>();
    }
    
    public ArrayList<Setting> getAll() {
        /*SL:36*/return this.settings;
    }
}
