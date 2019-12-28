package com.ares.hack.settings.settings.number;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.settings.Setting;

public abstract class NumberSetting<T extends Number> extends Setting<T>
{
    private T min;
    private T max;
    
    NumberSetting(final String a1, final BaseHack a2, final EnumSettingType a3, final T a4, final T a5, final T a6) {
        super(a1, a2, a3, a4);
        this.min = a5;
        this.max = a6;
    }
    
    @Override
    public abstract Class<?> getValueType();
    
    @Override
    public T getMin() {
        /*SL:43*/return this.min;
    }
    
    @Override
    public T getMax() {
        /*SL:49*/return this.max;
    }
}
