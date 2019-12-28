package com.ares.hack.settings.settings.number;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;

public class FloatSetting extends NumberSetting<Float>
{
    public FloatSetting(final String a1, final BaseHack a2, final float a3, final float a4, final float a5) {
        super(a1, a2, EnumSettingType.FLOAT, a3, a4, a5);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:33*/return Float.class;
    }
}
