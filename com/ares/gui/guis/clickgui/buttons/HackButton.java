package com.ares.gui.guis.clickgui.buttons;

import com.ares.gui.guis.clickgui.GuiUtil;
import com.ares.hack.hacks.BaseHack;
import java.util.ArrayList;

public class HackButton extends Button
{
    private static ArrayList<HackButton> hackButtons;
    private ArrayList<SettingButton> settingButtons;
    private CategoryButton categoryButton;
    private BaseHack hack;
    
    public HackButton(final BaseHack a1) {
        super(GuiUtil.categoryButtonMap.get(a1.category).x, GuiUtil.categoryButtonMap.get(a1.category).y + 20 * GuiUtil.categoryButtonMap.get(a1.category).listPlaces, 100, 20, a1.name, true, new float[] { 0.2f, 0.0f, 0.9f, 1.0f });
        this.settingButtons = new ArrayList<SettingButton>();
        final CategoryButton categoryButton = GuiUtil.categoryButtonMap.get(a1.category);
        ++categoryButton.listPlaces;
        this.categoryButton = GuiUtil.categoryButtonMap.get(a1.category);
        this.hack = a1;
        HackButton.hackButtons.add(this);
        this.categoryButton.getHackButtons().add(this);
    }
    
    public BaseHack getHack() {
        /*SL:57*/return this.hack;
    }
    
    public CategoryButton getCategoryButton() {
        /*SL:62*/return this.categoryButton;
    }
    
    public ArrayList<SettingButton> getSettingButtons() {
        /*SL:67*/return this.settingButtons;
    }
    
    public static ArrayList<HackButton> getAll() {
        /*SL:72*/return HackButton.hackButtons;
    }
    
    @Override
    public String toString() {
        /*SL:78*/return this.text;
    }
    
    static {
        HackButton.hackButtons = new ArrayList<HackButton>();
    }
}
