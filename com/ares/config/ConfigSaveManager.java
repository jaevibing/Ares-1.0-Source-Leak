package com.ares.config;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import com.ares.gui.guis.clickgui.buttons.CategoryButton;
import com.ares.commands.Command;
import com.ares.hack.hacks.hud.api.MoveableHudElement;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import com.ares.Ares;
import com.ares.hack.optimizations.BaseOptimization;
import com.ares.hack.settings.Setting;
import com.ares.hack.settings.SettingManager;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import org.json.JSONObject;
import java.io.File;

public class ConfigSaveManager
{
    public static void save(final File v-4) throws IOException {
        final JSONObject jsonObject = /*EL:28*/new JSONObject();
        JSONObject a2 = /*EL:33*/new JSONObject();
        /*SL:35*/for (final BaseHack v0 : HackManager.getAll()) {
            final JSONObject v = /*EL:37*/new JSONObject();
            /*SL:39*/v.put("Enabled", v0.getSetEnabledQueue());
            final List<Setting> v2 = /*EL:41*/SettingManager.getSettingsByMod(v0);
            /*SL:43*/if (v2 != null) {
                /*SL:45*/for (final Setting a1 : v2) {
                    /*SL:47*/if (a1.getValue() instanceof Enum) {
                        /*SL:49*/v.put(a1.getName(), a1.getValue().name());
                    }
                    else {
                        /*SL:53*/v.put(a1.getName(), a1.getValue());
                    }
                }
            }
            /*SL:58*/a2.put(v0.name, v);
        }
        /*SL:61*/jsonObject.put("Hacks", a2);
        /*SL:66*/a2 = new JSONObject();
        /*SL:68*/for (final BaseOptimization v3 : BaseOptimization.getAll()) {
            final JSONObject v = /*EL:70*/new JSONObject();
            /*SL:72*/v.put("Enabled", v3.getEnabled());
            /*SL:74*/a2.put(v3.name, v);
        }
        /*SL:77*/jsonObject.put("Optimizations", a2);
        /*SL:82*/a2 = new JSONObject();
        /*SL:84*/for (final AbstractHudElement v4 : Ares.hudManager.elements) {
            final JSONObject v = /*EL:86*/new JSONObject();
            /*SL:88*/v.put("Visible", v4.isVisible());
            /*SL:90*/if (v4 instanceof MoveableHudElement) {
                /*SL:92*/v.put("x", v4.getPos().x);
                /*SL:93*/v.put("y", v4.getPos().y);
            }
            /*SL:96*/a2.put(v4.getName(), v);
        }
        /*SL:99*/jsonObject.put("hud", a2);
        /*SL:104*/a2 = new JSONObject();
        /*SL:106*/a2.put("Prefix", Command.commandPrefix);
        /*SL:108*/jsonObject.put("Commands", a2);
        /*SL:113*/a2 = new JSONObject();
        /*SL:115*/for (final CategoryButton v5 : CategoryButton.getAll()) {
            final JSONObject v = /*EL:117*/new JSONObject();
            /*SL:119*/v.put("Open", v5.rightClickToggled);
            /*SL:121*/v.put("x", v5.x);
            /*SL:122*/v.put("y", v5.y);
            /*SL:124*/a2.put(v5.text, v);
        }
        /*SL:127*/jsonObject.put("Categories", a2);
        final PrintWriter printWriter = /*EL:131*/new PrintWriter(new FileWriter(v-4));
        /*SL:132*/printWriter.print(jsonObject.toString(4));
        /*SL:133*/printWriter.close();
    }
}
