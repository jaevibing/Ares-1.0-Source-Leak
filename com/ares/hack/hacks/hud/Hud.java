package com.ares.hack.hacks.hud;

import com.ares.hack.hacks.hud.impl.GuiHud;
import org.lwjgl.input.Mouse;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Hud", description = "Hud Overlay", category = EnumCategory.UI, defaultOn = true, defaultIsVisible = false)
public class Hud extends BaseHack
{
    public static Hud INSTANCE;
    
    public Hud() {
        Hud.INSTANCE = this;
    }
    
    public void onRender2d() {
        /*SL:23*/if (this.getEnabled() && !Hud.mc.field_71474_y.field_74330_P && !(Hud.mc.field_71462_r instanceof GuiEditHud)) {
            /*SL:25*/GuiHud.drawScreen(Mouse.getX(), Mouse.getY(), Hud.mc.func_184121_ak());
        }
    }
}
