package com.ares.hack.settings.settings;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.settings.Setting;

public class BooleanSetting extends Setting<Boolean>
{
    public BooleanSetting(final String a1, final BaseHack a2, final boolean a3) {
        super(a1, a2, EnumSettingType.BOOLEAN, a3);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:34*/return Boolean.class;
    }
}
