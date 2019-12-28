package com.ares.gui.elements;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.gui.guis.AresGui;

public class ElementText extends Element
{
    public String text;
    public double scale;
    public String color;
    
    public ElementText(final AresGui a1, final int a2, final int a3, final String a4, final String a5) {
        this(a1, a2, a3, a4, 1.0, a5);
    }
    
    public ElementText(final AresGui a1, final int a2, final int a3, final String a4, final double a5, final String a6) {
        super(a1, a2, a3);
        this.text = a4;
        this.scale = a5;
        this.color = a6;
    }
    
    private void drawScaledString(final String a1, final int a2, final int a3, final double a4, final String a5) {
        /*SL:29*/GlStateManager.func_179094_E();
        /*SL:30*/GlStateManager.func_179139_a(a4, a4, 1.0);
        ElementText.mc.field_71466_p.func_78276_b(/*EL:31*/a1, (int)(a2 / a4), (int)(a3 / a4), Integer.parseInt(a5, 16));
        /*SL:32*/GlStateManager.func_179121_F();
    }
    
    public void onDraw(final GuiScreen a1) {
        /*SL:38*/this.drawScaledString(this.text, this.x, this.y, this.scale, this.color);
    }
}
