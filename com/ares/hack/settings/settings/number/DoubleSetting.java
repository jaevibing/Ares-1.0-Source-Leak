package com.ares.hack.settings.settings.number;

import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.hacks.BaseHack;

public class DoubleSetting extends NumberSetting<Double>
{
    public DoubleSetting(final String a1, final BaseHack a2, final double a3, final double a4, final double a5) {
        super(a1, a2, EnumSettingType.DOUBLE, a3, a4, a5);
    }
    
    @Override
    public Class<?> getValueType() {
        /*SL:33*/return Double.class;
    }
}
