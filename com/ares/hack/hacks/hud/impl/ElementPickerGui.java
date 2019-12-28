package com.ares.hack.hacks.hud.impl;

import java.util.Iterator;
import com.ares.utils.render.Render2dUtils;
import java.awt.Color;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.ares.Ares;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import java.util.List;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

public class ElementPickerGui extends MoveableHudElement
{
    public ElementPickerGui() {
        super("Element Picker");
    }
    
    @Override
    public void render(final int v2, final int v3, final float v4) {
        /*SL:22*/if (ElementPickerGui.mc.field_71462_r instanceof GuiEditHud) {
            final List<AbstractHudElement> a2 = Ares.hudManager.elements.stream().filter(/*EL:25*/a1 -> a1 != this).<List<AbstractHudElement>, ?>collect((Collector<? super Object, ?, List<AbstractHudElement>>)Collectors.<? super Object>toList());
            /*SL:30*/Render2dUtils.drawRect(this.getPos().x = a2.size() * (ElementPickerGui.mc.field_71466_p.field_78288_b + 2) - 2, this.getPos().y, this.getSize().x, this.getSize().y, Color.BLACK.getRGB());
            ElementPickerGui.mc.field_71466_p.func_78276_b(/*EL:33*/"Element Picket", this.getPos().x + 2, this.getPos().y + 2, Color.WHITE.getRGB());
            int a3 = ElementPickerGui.mc.field_71466_p.field_78288_b + /*EL:35*/2;
            /*SL:38*/Render2dUtils.drawRect(this.getPos().x + 2, this.getPos().y + 2 + a3, this.getSize().x - 2, this.getSize().y - 2, Color.WHITE.getRGB());
            /*SL:40*/for (final AbstractHudElement a4 : a2) {
                ElementPickerGui.mc.field_71466_p.func_78276_b(/*EL:42*/a4.getName(), this.getPos().x + 2, this.getPos().y + a3, a4.isVisible() ? Color.GRAY.getRGB() : Color.WHITE.getRGB());
                /*SL:43*/a3 += ElementPickerGui.mc.field_71466_p.field_78288_b + 2;
            }
            /*SL:46*/this.getSize().y = a3 + ElementPickerGui.mc.field_71466_p.field_78288_b + 2;
        }
        /*SL:49*/this.forceInView();
    }
    
    @Override
    public void onMouseRelease(final int v-3, final int v-2, final int v-1) {
        /*SL:55*/super.onMouseRelease(v-3, v-2, v-1);
        /*SL:57*/if (v-1 == 0 && this.isMouseOver(v-3, v-2)) {
            final List<AbstractHudElement> a2 = Ares.hudManager.elements.stream().filter(/*EL:59*/a1 -> a1 != this).<List<AbstractHudElement>, ?>collect((Collector<? super Object, ?, List<AbstractHudElement>>)Collectors.<? super Object>toList());
            int v1 = ElementPickerGui.mc.field_71466_p.field_78288_b + /*EL:61*/2;
            /*SL:63*/for (final AbstractHudElement a3 : a2) {
                final boolean a4 = /*EL:65*/v-3 >= this.getPos().x + /*EL:66*/2 && v-2 >= this.getPos().y + /*EL:68*/v1 && v-3 <= this.getPos().x + /*EL:70*/2 + ElementPickerGui.mc.field_71466_p.func_78256_a(a3.getName()) && v-2 <= this.getPos().y + /*EL:72*/v1 + ElementPickerGui.mc.field_71466_p.field_78288_b;
                /*SL:75*/if (a4) {
                    /*SL:77*/a3.setVisible(!a3.isVisible());
                }
                /*SL:80*/v1 += ElementPickerGui.mc.field_71466_p.field_78288_b + 2;
            }
        }
    }
}
