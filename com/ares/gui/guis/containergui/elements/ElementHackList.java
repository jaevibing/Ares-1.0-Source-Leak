package com.ares.gui.guis.containergui.elements;

import com.ares.hack.categories.EnumCategory;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.elements.ElementTextCentered;
import com.ares.utils.ColourUtils;
import com.ares.gui.elements.ElementContainer;
import java.util.HashMap;
import com.ares.gui.guis.AresGui;
import com.ares.hack.hacks.BaseHack;
import java.util.Map;
import com.ares.hack.categories.Category;
import java.util.ArrayList;
import com.ares.gui.elements.Element;

public class ElementHackList extends Element
{
    private static ArrayList<ElementHackList> elements;
    public static Category currentCategory;
    public Map<BaseHack, ElementHack> leftHacks;
    public Map<BaseHack, ElementHack> rightHacks;
    private Category category;
    
    public ElementHackList(final AresGui v-9, final int v-8, final int v-7, final int v-6, final Category v-5) {
        super(v-9, v-8, v-7);
        this.leftHacks = new HashMap<BaseHack, ElementHack>();
        this.rightHacks = new HashMap<BaseHack, ElementHack>();
        this.category = v-5;
        final ElementContainer elementContainer = this.<ElementContainer>addElement(new ElementContainer(v-9, v-8, v-7, v-6, 30, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, ColourUtils.getAresRed(1.0f)));
        final double a6 = 1.25;
        this.<ElementTextCentered>addElement(new ElementTextCentered(v-9, v-8 + elementContainer.width / 2, v-7 - (int)(ElementHackList.mc.field_71466_p.field_78288_b * a6 / 2.0) + elementContainer.height / 2, v-5.getName(), a6, "FFFFFF"));
        for (int a1 = 0; a1 < v-5.getHacks().size(); ++a1) {
            if (a1 % 2 == 1) {
                this.leftHacks.put(v-5.getHacks().get(a1), null);
            }
            else {
                this.rightHacks.put(v-5.getHacks().get(a1), null);
            }
        }
        for (int a2 = 0; a2 < this.leftHacks.keySet().size(); ++a2) {
            final BaseHack a3 = (BaseHack)this.leftHacks.keySet().toArray()[a2];
            if (a2 == 0) {
                this.leftHacks.put(a3, this.<ElementHack>addElement(new ElementHack(v-9, v-8, v-7 + 30, v-6 / 2, a3)));
            }
            else {
                final BaseHack a4 = (BaseHack)this.leftHacks.keySet().toArray()[a2 - 1];
                final ElementHack a5 = this.leftHacks.get(a4);
                this.leftHacks.put(a3, this.<ElementHack>addElement(new ElementHack(v-9, a5.x, a5.y + a5.height, v-6 / 2, a3)));
            }
        }
        for (int i = 0; i < this.rightHacks.keySet().size(); ++i) {
            final BaseHack v0 = (BaseHack)this.rightHacks.keySet().toArray()[i];
            if (i == 0) {
                this.rightHacks.put(v0, this.<ElementHack>addElement(new ElementHack(v-9, v-8 + v-6 / 2, v-7 + 30, v-6 / 2, v0)));
            }
            else {
                final BaseHack v = (BaseHack)this.rightHacks.keySet().toArray()[i - 1];
                final ElementHack v2 = this.rightHacks.get(v);
                this.rightHacks.put(v0, this.<ElementHack>addElement(new ElementHack(v-9, v2.x, v2.y + v2.height, v-6 / 2, v0)));
            }
        }
        ElementHackList.elements.add(this);
    }
    
    public void onDraw(final GuiScreen v-2) {
        /*SL:91*/for (int i = 0; i < this.leftHacks.keySet().size(); ++i) {
            /*SL:93*/if (i != 0) {
                final BaseHack a1 = /*EL:95*/(BaseHack)this.leftHacks.keySet().toArray()[i];
                final BaseHack v1 = /*EL:96*/(BaseHack)this.leftHacks.keySet().toArray()[i - 1];
                final ElementHack v2 = /*EL:97*/this.leftHacks.get(v1);
                /*SL:99*/this.leftHacks.get(a1).y = v2.y + v2.height;
            }
        }
        /*SL:104*/for (int i = 0; i < this.rightHacks.keySet().size(); ++i) {
            /*SL:106*/if (i != 0) {
                final BaseHack v3 = /*EL:108*/(BaseHack)this.rightHacks.keySet().toArray()[i];
                final BaseHack v1 = /*EL:109*/(BaseHack)this.rightHacks.keySet().toArray()[i - 1];
                final ElementHack v2 = /*EL:110*/this.rightHacks.get(v1);
                /*SL:112*/this.rightHacks.get(v3).y = v2.y + v2.height;
            }
        }
    }
    
    public Category getCategory() {
        /*SL:119*/return this.category;
    }
    
    public static void setList(final Category v1) {
        /*SL:124*/for (final ElementHackList a1 : ElementHackList.elements) {
            /*SL:126*/if (a1.category == v1) {
                /*SL:128*/a1.setVisible(true);
            }
            else {
                /*SL:132*/a1.setVisible(false);
            }
        }
        ElementHackList.currentCategory = /*EL:136*/v1;
    }
    
    public static ArrayList<ElementHackList> getElements() {
        /*SL:141*/return ElementHackList.elements;
    }
    
    static {
        ElementHackList.elements = new ArrayList<ElementHackList>();
        ElementHackList.currentCategory = EnumCategory.MOVEMENT.category;
    }
}
