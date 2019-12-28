package com.ares.config;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Iterator;
import com.ares.gui.guis.clickgui.buttons.CategoryButton;
import com.ares.commands.Command;
import com.ares.hack.hacks.hud.api.MoveableHudElement;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import com.ares.Ares;
import com.ares.hack.optimizations.BaseOptimization;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.settings.SettingManager;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import org.json.JSONObject;
import java.io.InputStream;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.io.File;

public class ConfigReadManager
{
    public static void read(final File v-8) throws FileNotFoundException {
        final JSONObject jsonObject = /*EL:29*/new JSONObject(new JSONTokener(new FileInputStream(v-8)));
        try {
            final JSONObject jsonObject2 = /*EL:34*/jsonObject.getJSONObject("Hacks");
            /*SL:36*/for (final BaseHack v2 : HackManager.getAll()) {
                try {
                    final JSONObject jsonObject3 = /*EL:40*/jsonObject2.getJSONObject(v2.name);
                    /*SL:42*/v2.setEnabled(jsonObject3.getBoolean("Enabled"));
                    final List<Setting> settingsByMod = /*EL:44*/SettingManager.getSettingsByMod(v2);
                    /*SL:46*/if (settingsByMod == null) {
                        continue;
                    }
                    /*SL:48*/for (final Setting v0 : settingsByMod) {
                        try {
                            /*SL:52*/if (v0 instanceof EnumSetting) {
                                /*SL:54*/for (final Enum a1 : (Enum[])v0.getValue().getClass().getEnumConstants()) {
                                    /*SL:56*/if (a1.name().equalsIgnoreCase(jsonObject3.getString(v0.getName()))) {
                                        /*SL:58*/v0.setValue(a1);
                                    }
                                }
                            }
                            else {
                                /*SL:64*/v0.setValue(v0.getValueType().cast(/*EL:65*/jsonObject3.get(v0.getName())));
                            }
                        }
                        catch (Exception v) {
                            /*SL:71*/v.printStackTrace();
                        }
                    }
                }
                catch (Exception ex) {
                    /*SL:75*/ex.printStackTrace();
                }
            }
        }
        catch (Exception ex3) {}
        try {
            final JSONObject jsonObject2 = /*EL:82*/jsonObject.getJSONObject("Optimizations");
            /*SL:84*/for (final BaseOptimization baseOptimization : BaseOptimization.getAll()) {
                try {
                    final JSONObject jsonObject3 = /*EL:88*/jsonObject2.getJSONObject(baseOptimization.name);
                    /*SL:89*/baseOptimization.setEnabled(jsonObject3.getBoolean("Enabled"));
                }
                catch (Exception ex) {
                    /*SL:91*/ex.printStackTrace();
                }
            }
        }
        catch (Exception ex4) {}
        try {
            final JSONObject jsonObject2 = /*EL:98*/jsonObject.getJSONObject("hud");
            /*SL:100*/for (final AbstractHudElement abstractHudElement : Ares.hudManager.elements) {
                try {
                    final JSONObject jsonObject3 = /*EL:104*/jsonObject2.getJSONObject(abstractHudElement.getName());
                    /*SL:106*/abstractHudElement.setVisible(jsonObject3.getBoolean("Visible"));
                    /*SL:108*/if (!(abstractHudElement instanceof MoveableHudElement)) {
                        continue;
                    }
                    /*SL:110*/abstractHudElement.getPos().x = jsonObject3.getInt("x");
                    /*SL:111*/abstractHudElement.getPos().y = jsonObject3.getInt("y");
                }
                catch (Exception ex) {
                    /*SL:115*/ex.printStackTrace();
                }
            }
        }
        catch (Exception ex2) {
            /*SL:119*/ex2.printStackTrace();
        }
        try {
            final JSONObject jsonObject2 = /*EL:124*/jsonObject.getJSONObject("Commands");
            Command.commandPrefix = /*EL:126*/jsonObject2.getString("Prefix");
        }
        catch (Exception ex2) {
            /*SL:128*/ex2.printStackTrace();
        }
        try {
            final JSONObject jsonObject2 = /*EL:133*/jsonObject.getJSONObject("Categories");
            /*SL:135*/for (final CategoryButton categoryButton : CategoryButton.getAll()) {
                try {
                    final JSONObject jsonObject3 = /*EL:139*/jsonObject2.getJSONObject(categoryButton.text);
                    /*SL:141*/categoryButton.rightClickToggled = jsonObject3.getBoolean("Open");
                    /*SL:143*/categoryButton.x = jsonObject3.getInt("x");
                    /*SL:144*/categoryButton.y = jsonObject3.getInt("y");
                }
                catch (Exception ex) {
                    /*SL:147*/ex.printStackTrace();
                }
            }
        }
        catch (Exception ex2) {
            /*SL:151*/ex2.printStackTrace();
        }
    }
}
