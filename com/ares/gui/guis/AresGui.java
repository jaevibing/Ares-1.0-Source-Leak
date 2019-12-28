package com.ares.gui.guis;

import net.minecraft.client.gui.GuiButton;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.gui.elements.Element;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class AresGui extends GuiScreen
{
    public ArrayList<Element> elements;
    public int mouseX;
    public int mouseY;
    protected boolean drawDefaultBackground;
    private int buttonIdCount;
    
    public AresGui() {
        this.elements = new ArrayList<Element>();
        this.drawDefaultBackground = false;
        this.buttonIdCount = 0;
        this.field_146291_p = true;
    }
    
    public void func_73863_a(final int a3, final int v1, final float v2) {
        /*SL:28*/this.mouseX = a3;
        /*SL:29*/this.mouseY = v1;
        /*SL:31*/if (this.drawDefaultBackground) {
            this.func_146276_q_();
        }
        /*SL:33*/GlStateManager.func_179094_E();
        /*SL:34*/GlStateManager.func_179123_a();
        /*SL:36*/for (final Element a4 : this.elements) {
            /*SL:38*/a4.draw(this);
        }
        /*SL:41*/GlStateManager.func_179099_b();
        /*SL:42*/GlStateManager.func_179121_F();
        /*SL:44*/super.func_73863_a(a3, v1, v2);
    }
    
    public boolean func_73868_f() {
        /*SL:50*/return false;
    }
    
    public List<GuiButton> getButtonList() {
        /*SL:55*/return (List<GuiButton>)this.field_146292_n;
    }
    
    public int getNewButtonId() {
        /*SL:62*/return ++this.buttonIdCount;
    }
    
    protected <T extends Element> T addElement(final T a1) {
        /*SL:67*/this.elements.add(a1);
        /*SL:68*/return a1;
    }
}
