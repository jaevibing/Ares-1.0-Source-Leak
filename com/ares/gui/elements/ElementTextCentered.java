package com.ares.gui.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.AresGui;

public class ElementTextCentered extends Element
{
    public String text;
    public double scale;
    public String color;
    
    public ElementTextCentered(final AresGui a1, final int a2, final int a3, final String a4, final double a5, final String a6) {
        super(a1, a2, a3);
        this.text = a4;
        this.scale = a5;
        this.color = a6;
    }
    
    private void drawScaledString(final String a1, final int a2, final int a3, final double a4, final String a5, final GuiScreen a6) {
        /*SL:24*/GlStateManager.func_179094_E();
        /*SL:25*/GlStateManager.func_179139_a(a4, a4, 1.0);
        /*SL:26*/a6.func_73732_a(ElementTextCentered.mc.field_71466_p, a1, (int)(a2 / a4), (int)(a3 / a4), Integer.parseInt(a5, 16));
        /*SL:27*/GlStateManager.func_179121_F();
    }
    
    public void onDraw(final GuiScreen a1) {
        /*SL:33*/this.drawScaledString(this.text, this.x, this.y, this.scale, this.color, a1);
    }
}
