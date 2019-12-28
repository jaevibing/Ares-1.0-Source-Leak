package com.ares.gui.guis.clickgui.buttons;

import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.clickgui.GuiUtil;
import com.ares.hack.settings.Setting;
import java.util.ArrayList;

public class SettingButton extends Button
{
    private static ArrayList<SettingButton> settingButtons;
    private HackButton hackButton;
    private Setting setting;
    public boolean held;
    
    public SettingButton(final Setting a1) {
        super(0, 0, 100, 12, a1.getName() + ": " + a1.getValue(), false, new float[] { 1.0f, 0.4f, 0.0f, 1.0f });
        this.held = false;
        this.hackButton = GuiUtil.hackButtonMap.get(a1.getHack());
        this.setting = a1;
        this.hackButton.getSettingButtons().add(this);
        SettingButton.settingButtons.add(this);
    }
    
    @Override
    public void onDrawButton(final GuiScreen a1, final int a2, final int a3) {
    }
    
    public HackButton getHackButton() {
        /*SL:55*/return this.hackButton;
    }
    
    public Setting getSetting() {
        /*SL:60*/return this.setting;
    }
    
    public static ArrayList<SettingButton> getAll() {
        /*SL:65*/return SettingButton.settingButtons;
    }
    
    static {
        SettingButton.settingButtons = new ArrayList<SettingButton>();
    }
}
