package com.ares.hack.settings.settings.number;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;

public class IntegerSetting extends NumberSetting<Integer>
{
    public IntegerSetting(final String a1, final BaseHack a2, final int a3, final int a4, final int a5) {
        super(a1, a2, EnumSettingType.INTEGER, a3, a4, a5);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:33*/return Integer.class;
    }
}
