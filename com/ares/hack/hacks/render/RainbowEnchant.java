package com.ares.hack.hacks.render;

import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "RainbowEnchant", description = "Rainbow enchant", category = EnumCategory.RENDER)
public class RainbowEnchant extends BaseHack
{
    public static RainbowEnchant INSTANCE;
    
    public RainbowEnchant() {
        RainbowEnchant.INSTANCE = this;
    }
}
