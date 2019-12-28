package com.ares.gui.elements;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.AresGui;
import net.minecraft.util.ResourceLocation;

public class ElementImage extends Element
{
    public int width;
    public int height;
    private ResourceLocation location;
    public float alpha;
    
    public ElementImage(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final ResourceLocation a6) {
        super(a1, a2, a3);
        this.alpha = 1.0f;
        this.width = a4;
        this.height = a5;
        this.location = a6;
    }
    
    public void onDraw(final GuiScreen a1) {
        ElementImage.mc.func_110434_K().func_110577_a(/*EL:28*/this.location);
        /*SL:29*/GL11.glColor4f(1.0f, 1.0f, 1.0f, this.alpha);
        /*SL:30*/Gui.func_146110_a(this.x, this.y, 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
    }
}
