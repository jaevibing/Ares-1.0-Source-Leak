package com.ares.hack.settings;

import com.ares.hack.hacks.BaseHack;

public abstract class Setting<T>
{
    private String name;
    private BaseHack hack;
    private EnumSettingType type;
    private T value;
    private final T defaultValue;
    
    public Setting(final String a1, final BaseHack a2, final EnumSettingType a3, final T a4) {
        this.name = a1;
        this.hack = a2;
        this.type = a3;
        this.value = a4;
        this.defaultValue = a4;
        a3.settings.add(this);
        SettingManager.allSettings.add(this);
    }
    
    public String getName() {
        /*SL:45*/return this.name;
    }
    
    public BaseHack getHack() {
        /*SL:50*/return this.hack;
    }
    
    public EnumSettingType getType() {
        /*SL:55*/return this.type;
    }
    
    public T getValue() {
        /*SL:60*/return this.value;
    }
    
    public void setValue(final T a1) {
        /*SL:65*/this.value = a1;
    }
    
    public T getDefaultValue() {
        /*SL:70*/return this.defaultValue;
    }
    
    public abstract Class<?> getValueType();
    
    public Number getMin() {
        /*SL:77*/return null;
    }
    
    public Number getMax() {
        /*SL:82*/return null;
    }
    
    public Enum[] getModes() {
        /*SL:87*/return null;
    }
}
