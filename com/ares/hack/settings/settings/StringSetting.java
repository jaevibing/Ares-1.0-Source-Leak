package com.ares.hack.settings.settings;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.settings.Setting;

public class StringSetting extends Setting<String>
{
    public StringSetting(final String a1, final BaseHack a2, final String a3) {
        super(a1, a2, EnumSettingType.STRING, a3);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:34*/return String.class;
    }
}
