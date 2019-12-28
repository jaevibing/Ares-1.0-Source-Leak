package com.ares.hack.hacks.hud.impl.elements;

import com.ares.utils.ColourUtils;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Watermark", description = "Ares client on top", category = EnumCategory.HUD, defaultIsVisible = false)
public class Watermark extends MoveableHudElement
{
    public Watermark() {
        super("Watermark");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:20*/super.render(a1, a2, a3);
        /*SL:21*/if (!this.shouldRender()) {
            return;
        }
        final String v1 = /*EL:23*/"Ares 1.0";
        /*SL:24*/ColourUtils.drawRainbowString(v1, this.getPos().x, this.getPos().y);
        /*SL:26*/this.getSize().x = Watermark.mc.field_71466_p.func_78256_a(v1);
        /*SL:27*/this.getSize().y = Watermark.mc.field_71466_p.field_78288_b;
    }
}
