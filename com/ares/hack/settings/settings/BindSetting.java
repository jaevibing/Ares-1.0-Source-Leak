package com.ares.hack.settings.settings;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.settings.Setting;

public class BindSetting extends Setting<String>
{
    public BindSetting(final String a1, final BaseHack a2, final String a3) {
        super(a1, a2, EnumSettingType.BIND, a3);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:34*/return String.class;
    }
}
