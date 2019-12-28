package com.ares.hack.hacks.hud.impl.elements;

import com.ares.utils.ColourUtils;
import net.minecraft.client.Minecraft;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "FPS", description = "Lists the fps", category = EnumCategory.HUD, defaultIsVisible = false)
public class FpsElement extends MoveableHudElement
{
    public FpsElement() {
        super("Fps");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:21*/super.render(a1, a2, a3);
        /*SL:22*/if (!this.shouldRender()) {
            return;
        }
        final String v1 = /*EL:24*/Minecraft.func_175610_ah() + " fps";
        /*SL:26*/ColourUtils.drawRainbowString(v1, this.getPos().x, this.getPos().y);
        /*SL:28*/this.getSize().x = FpsElement.mc.field_71466_p.func_78256_a(v1);
        /*SL:29*/this.getSize().y = FpsElement.mc.field_71466_p.field_78288_b;
    }
}
