package com.ares.gui.elements;

import org.lwjgl.opengl.GL11;
import com.ares.Globals;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.AresGui;

public class ElementRectangle extends Element
{
    public int width;
    public int height;
    public float[] color;
    
    public ElementRectangle(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final float[] a6) {
        super(a1, a2, a3);
        this.width = a4;
        this.height = a5;
        this.color = a6;
    }
    
    public void onDraw(final GuiScreen a1) {
        Globals.mc.func_110434_K().func_110577_a(ElementRectangle.WHITE);
        /*SL:26*/GL11.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
        /*SL:27*/a1.func_73729_b(this.x, this.y, 0, 0, this.width, this.height);
    }
}
