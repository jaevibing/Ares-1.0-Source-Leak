package com.ares.hack.settings.settings;

import java.util.Objects;
import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.settings.Setting;

public class EnumSetting<T extends Enum> extends Setting<T>
{
    private Enum[] modes;
    
    public EnumSetting(final String a1, final BaseHack a2, final T a3) {
        super(a1, a2, EnumSettingType.ENUM, a3);
        this.modes = (Enum[])a3.getClass().getEnumConstants();
    }
    
    public void setValueFromString(final String v2) {
        try {
            /*SL:41*/for (final Enum a1 : this.modes) {
                /*SL:43*/if (a1.toString().equalsIgnoreCase(v2)) {
                    /*SL:45*/this.setValue(Objects.<T>requireNonNull(a1));
                }
            }
        }
        catch (Exception ex) {}
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:54*/return String.class;
    }
    
    @Override
    public Enum[] getModes() {
        /*SL:60*/return this.modes;
    }
}
