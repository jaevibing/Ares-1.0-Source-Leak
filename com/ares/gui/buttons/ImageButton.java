package com.ares.gui.buttons;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import com.ares.gui.guis.AresGui;
import net.minecraft.util.ResourceLocation;

public class ImageButton extends Button
{
    private ResourceLocation location;
    public float alpha;
    
    public ImageButton(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final ResourceLocation a6) {
        super(a1, a2, a3, a4, a5, "");
        this.alpha = 1.0f;
        this.location = a6;
    }
    
    public void draw() {
        ImageButton.mc.func_110434_K().func_110577_a(/*EL:22*/this.location);
        /*SL:23*/GL11.glColor4f(1.0f, 1.0f, 1.0f, this.alpha);
        /*SL:24*/Gui.func_146110_a(this.field_146128_h, this.field_146129_i, 0.0f, 0.0f, this.field_146120_f, this.field_146121_g, (float)this.field_146120_f, (float)this.field_146121_g);
    }
}
