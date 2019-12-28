package com.ares.hack.hacks.ui;

import java.util.Iterator;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.extensions.Wrapper;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Radar", description = "See nearby players", category = EnumCategory.UI)
public class Radar extends BaseHack
{
    private Setting<Integer> xS;
    private Setting<Integer> yS;
    private Setting<Integer> textHeight;
    
    public Radar() {
        this.xS = new IntegerSetting("x", this, 0, 0, Radar.mc.field_71443_c + 50);
        this.yS = new IntegerSetting("y", this, 0, 0, Radar.mc.field_71443_c + 50);
        this.textHeight = new IntegerSetting("Text Height", this, 20, 1, 50);
    }
    
    public void onRender2d() {
        /*SL:22*/if (!this.getEnabled()) {
            return;
        }
        int n = /*EL:24*/0;
        final int field_78288_b = Wrapper.fontRenderer.field_78288_b;
        Wrapper.fontRenderer.field_78288_b = /*EL:27*/this.textHeight.getValue();
        /*SL:29*/for (final EntityPlayer v1 : Radar.mc.field_71441_e.field_73010_i) {
            /*SL:31*/if (!v1.equals((Object)Radar.mc.field_71439_g)) {
                Wrapper.fontRenderer.func_78276_b(/*EL:33*/v1.getDisplayNameString(), /*EL:34*/(int)this.xS.getValue(), /*EL:35*/this.yS.getValue() + /*EL:36*/n, Color.WHITE.getRGB());
                /*SL:40*/n += Wrapper.fontRenderer.field_78288_b + 2;
            }
        }
        Wrapper.fontRenderer.field_78288_b = /*EL:44*/field_78288_b;
    }
}
