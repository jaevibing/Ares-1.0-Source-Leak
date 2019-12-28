package com.ares.hack.settings;

import java.util.Iterator;
import com.ares.hack.hacks.BaseHack;
import java.util.ArrayList;

public class SettingManager
{
    static ArrayList<Setting> allSettings;
    
    public static ArrayList<Setting> getAllSettings() {
        /*SL:30*/return SettingManager.allSettings;
    }
    
    public static ArrayList<Setting> getSettingsByMod(final BaseHack v1) {
        final ArrayList<Setting> v2 = /*EL:35*/new ArrayList<Setting>();
        /*SL:37*/for (final Setting a1 : SettingManager.allSettings) {
            /*SL:39*/if (a1.getHack() == v1) {
                v2.add(a1);
            }
        }
        /*SL:42*/return v2;
    }
    
    static {
        SettingManager.allSettings = new ArrayList<Setting>();
    }
}
