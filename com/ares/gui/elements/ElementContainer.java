package com.ares.gui.elements;

import org.lwjgl.opengl.GL11;
import com.ares.Globals;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.AresGui;

public class ElementContainer extends Element
{
    public int width;
    public int height;
    private float[] mainColor;
    private float[] border;
    
    public ElementContainer(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final float[] a6, final float[] a7) {
        super(a1, a2, a3);
        this.width = a4;
        this.height = a5;
        this.mainColor = a6;
        this.border = a7;
    }
    
    public void onDraw(final GuiScreen a1) {
        Globals.mc.func_110434_K().func_110577_a(ElementContainer.WHITE);
        /*SL:28*/GL11.glPushAttrib(1048575);
        /*SL:29*/GL11.glPushMatrix();
        /*SL:31*/GL11.glColor4f(this.border[0], this.border[1], this.border[2], this.border[3]);
        /*SL:33*/a1.func_73729_b(this.x, this.y, 0, 0, this.width, 1);
        /*SL:34*/a1.func_73729_b(this.x, this.y + 1, 0, 0, 1, this.height - 1);
        /*SL:35*/a1.func_73729_b(this.x + 1, this.y + this.height - 1, 0, 0, this.width - 1, 1);
        /*SL:36*/a1.func_73729_b(this.x + this.width - 1, this.y + 1, 0, 0, 1, this.height - 2);
        /*SL:38*/GL11.glColor4f(this.mainColor[0], this.mainColor[1], this.mainColor[2], this.mainColor[3]);
        /*SL:41*/a1.func_73729_b(this.x + 1, this.y + 1, 0, 0, this.width - 2, this.height - 2);
        /*SL:43*/GL11.glPopMatrix();
        /*SL:44*/GL11.glPopAttrib();
    }
}
