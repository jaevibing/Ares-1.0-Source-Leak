package com.ares.gui.elements;

import com.ares.Globals;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.AresGui;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Element
{
    public static final ResourceLocation WHITE;
    public static Minecraft mc;
    private boolean visible;
    private ArrayList<GuiButton> buttons;
    private ArrayList<Element> elements;
    protected AresGui gui;
    public int x;
    public int y;
    public int renderX;
    public int renderY;
    
    protected Element(final AresGui a1, final int a2, final int a3) {
        this.visible = true;
        this.buttons = new ArrayList<GuiButton>();
        this.elements = new ArrayList<Element>();
        this.gui = a1;
        this.x = a2;
        this.y = a3;
        a1.elements.add(this);
    }
    
    protected void onDraw(final GuiScreen a1) {
    }
    
    public void draw(final GuiScreen v2) {
        /*SL:42*/if (this.visible) {
            /*SL:44*/this.onDraw(v2);
            /*SL:46*/for (final Element a1 : this.elements) {
                /*SL:48*/a1.draw(v2);
            }
        }
    }
    
    public void setVisible(final boolean v-1) {
        /*SL:55*/this.visible = v-1;
        /*SL:57*/for (final Element a1 : this.elements) {
            /*SL:59*/a1.setVisible(v-1);
        }
        /*SL:62*/for (final GuiButton v1 : this.buttons) {
            /*SL:64*/v1.field_146125_m = v-1;
        }
    }
    
    public boolean getVisible() {
        /*SL:70*/return this.visible;
    }
    
    protected <T extends GuiButton> T addButton(final T a1) {
        /*SL:75*/this.gui.getButtonList().add(a1);
        /*SL:76*/this.buttons.add(a1);
        /*SL:77*/return a1;
    }
    
    protected <T extends Element> T addElement(final T a1) {
        /*SL:82*/this.elements.add(a1);
        /*SL:83*/return a1;
    }
    
    static {
        WHITE = new ResourceLocation("ares", "white.png");
        Element.mc = Globals.mc;
    }
}
