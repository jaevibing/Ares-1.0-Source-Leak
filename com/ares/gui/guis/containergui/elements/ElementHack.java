package com.ares.gui.guis.containergui.elements;

import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiScreen;
import java.util.Iterator;
import com.ares.hack.settings.Setting;
import com.ares.hack.settings.SettingManager;
import com.ares.utils.ColourUtils;
import com.ares.gui.guis.AresGui;
import com.ares.gui.guis.containergui.buttons.DropDownButton;
import com.ares.gui.guis.containergui.buttons.HackToggleButton;
import com.ares.gui.elements.ElementContainer;
import com.ares.gui.elements.ElementTextCentered;
import com.ares.hack.hacks.BaseHack;
import com.ares.gui.guis.containergui.buttons.SettingButton;
import java.util.ArrayList;
import com.ares.gui.elements.Element;

public class ElementHack extends Element
{
    public int numOfSettings;
    public int height;
    public int width;
    private ArrayList<SettingButton> settings;
    private BaseHack hack;
    private boolean leftHeld;
    ElementTextCentered text;
    ElementContainer container;
    HackToggleButton toggleButton;
    public DropDownButton dropButton;
    
    public ElementHack(final AresGui a4, final int a5, final int v1, final int v2, final BaseHack v3) {
        super(a4, a5, v1);
        this.numOfSettings = 0;
        this.height = 20;
        this.settings = new ArrayList<SettingButton>();
        this.leftHeld = false;
        this.width = v2;
        this.hack = v3;
        this.text = this.<ElementTextCentered>addElement(new ElementTextCentered(a4, a5 + v2 / 2 - 1, v1 + 10 - ElementHack.mc.field_71466_p.field_78288_b / 2, v3.name, 1.0, "FFFFFF"));
        this.container = this.<ElementContainer>addElement(new ElementContainer(a4, a5, v1, v2, this.height, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, ColourUtils.getAresRed(1.0f)));
        this.toggleButton = this.<HackToggleButton>addButton(new HackToggleButton(a4, a5 + v2 - 21, v1, 20, 20, v3));
        this.dropButton = this.<DropDownButton>addButton(new DropDownButton(a4, a5, v1, 20, 20, this));
        for (final Setting a6 : SettingManager.getSettingsByMod(v3)) {
            final SettingButton a7 = this.<SettingButton>addButton(new SettingButton(a4, a5 + 3, this.container.y + this.container.height + this.numOfSettings * (ElementHack.mc.field_71466_p.field_78288_b + 4) + 1, a6));
            a7.field_146125_m = false;
            this.settings.add(a7);
            ++this.numOfSettings;
        }
    }
    
    public void onDraw(final GuiScreen v-1) {
        /*SL:67*/if (this.dropButton.getToggled()) {
            /*SL:69*/for (final SettingButton a1 : this.settings) {
                /*SL:71*/a1.field_146125_m = true;
            }
        }
        else {
            /*SL:76*/for (final SettingButton v1 : this.settings) {
                /*SL:78*/v1.field_146125_m = false;
            }
        }
        /*SL:82*/this.text.x = this.x + this.width / 2 - 1;
        /*SL:83*/this.text.y = this.y + 10 - ElementHack.mc.field_71466_p.field_78288_b / 2;
        /*SL:86*/this.container.y = this.y;
        /*SL:87*/this.container.height = this.height;
        /*SL:90*/this.toggleButton.field_146129_i = this.y;
        /*SL:93*/this.dropButton.field_146129_i = this.y;
        /*SL:95*/for (final SettingButton v1 : this.settings) {
            /*SL:97*/v1.field_146129_i = this.container.y + 20 + this.settings.indexOf(v1) * (ElementHack.mc.field_71466_p.field_78288_b + 4) + 1;
        }
        /*SL:101*/if (this.gui.mouseX >= this.x && this.gui.mouseX <= this.x + this.width && this.gui.mouseY >= this.y && this.gui.mouseY <= this.y + this.height && Mouse.isButtonDown(1) && !this.leftHeld) {
            /*SL:103*/this.dropButton.toggle();
            /*SL:104*/this.leftHeld = true;
        }
        /*SL:107*/if (!Mouse.isButtonDown(1) && this.leftHeld) {
            /*SL:109*/this.leftHeld = false;
        }
    }
    
    public BaseHack getHack() {
        /*SL:130*/return this.hack;
    }
}
