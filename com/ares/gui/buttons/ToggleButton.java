package com.ares.gui.buttons;

import com.ares.gui.guis.AresGui;
import net.minecraft.util.ResourceLocation;

public class ToggleButton extends Button
{
    public static final ResourceLocation ON_SWITCH;
    public static final ResourceLocation OFF_SWITCH;
    protected boolean toggled;
    
    public ToggleButton(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final boolean a6) {
        super(a1, a2, a3, a4, a5, "");
        this.toggled = a6;
    }
    
    public void draw() {
        /*SL:23*/if (this.toggled) {
            ToggleButton.mc.func_110434_K().func_110577_a(ToggleButton.ON_SWITCH);
        }
        else {
            ToggleButton.mc.func_110434_K().func_110577_a(ToggleButton.OFF_SWITCH);
        }
        func_146110_a(/*EL:32*/this.field_146128_h, this.field_146129_i, 0.0f, 0.0f, this.field_146120_f, this.field_146121_g, (float)this.field_146120_f, (float)this.field_146121_g);
    }
    
    static {
        ON_SWITCH = new ResourceLocation("ares", "onswitch.png");
        OFF_SWITCH = new ResourceLocation("ares", "offswitch.png");
    }
}
