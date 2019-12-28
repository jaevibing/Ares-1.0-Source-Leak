package com.ares.gui.buttons;

import com.ares.gui.elements.Element;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.utils.ColourUtils;
import com.ares.Globals;
import com.ares.gui.guis.AresGui;

public class TextButton extends Button
{
    public String textColor;
    public String text;
    private double scale;
    private float[] color;
    
    public TextButton(final AresGui a1, final int a2, final int a3, final String a4) {
        this(a1, a2, a3, Globals.mc.field_71466_p.func_78256_a(a4), Globals.mc.field_71466_p.field_78288_b, a4, 1.0);
    }
    
    public TextButton(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final String a6, final double a7) {
        super(a1, a2, a3, a4, a5, a6);
        this.textColor = "FFFFFF";
        this.color = ColourUtils.getAresRed(1.0f);
        this.scale = a7;
        this.text = a6;
    }
    
    public void draw() {
        /*SL:32*/GlStateManager.func_179094_E();
        /*SL:33*/GlStateManager.func_179139_a(this.scale, this.scale, 1.0);
        /*SL:34*/this.func_73731_b(TextButton.mc.field_71466_p, this.text, (int)(this.field_146128_h / this.scale), (int)(this.field_146129_i / this.scale), Integer.parseInt(this.textColor, 16));
        /*SL:35*/GlStateManager.func_179121_F();
        Globals.mc.func_110434_K().func_110577_a(Element.WHITE);
        /*SL:39*/if (this.func_146115_a()) {
            /*SL:41*/GlStateManager.func_179131_c(this.color[0], this.color[1] + 0.2f, this.color[2] + 0.2f, this.color[3]);
        }
        else {
            /*SL:45*/GlStateManager.func_179131_c(this.color[0], this.color[1], this.color[2], this.color[3]);
        }
        /*SL:47*/this.func_73729_b(this.field_146128_h, this.field_146129_i + TextButton.mc.field_71466_p.field_78288_b, 0, 0, (int)(TextButton.mc.field_71466_p.func_78256_a(this.text) * this.scale), 1);
        /*SL:48*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
