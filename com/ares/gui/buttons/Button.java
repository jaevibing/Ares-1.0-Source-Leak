package com.ares.gui.buttons;

import com.ares.Globals;
import com.ares.gui.guis.AresGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Button extends GuiButton
{
    protected static Minecraft mc;
    protected AresGui gui;
    
    public Button(final AresGui a1, final int a2, final int a3, final String a4) {
        super(a1.getNewButtonId(), a2, a3, a4);
        this.gui = a1;
    }
    
    public Button(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final String a6) {
        super(a1.getNewButtonId(), a2, a3, a4, a5, a6);
        this.gui = a1;
    }
    
    protected void draw() {
    }
    
    public void func_191745_a(final Minecraft a1, final int a2, final int a3, final float a4) {
        /*SL:31*/if (this.field_146125_m) {
            this.draw();
        }
    }
    
    public boolean func_146115_a() {
        /*SL:37*/return this.gui.mouseX >= this.field_146128_h && this.gui.mouseX <= this.field_146128_h + this.field_146120_f && this.gui.mouseY >= this.field_146129_i && this.gui.mouseY <= this.field_146129_i + this.field_146121_g;
    }
    
    static {
        Button.mc = Globals.mc;
    }
}
