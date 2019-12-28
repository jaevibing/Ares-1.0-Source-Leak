package com.ares.gui.guis.clickgui;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import com.ares.gui.guis.clickgui.buttons.Button;
import java.util.Iterator;
import com.ares.gui.guis.clickgui.buttons.NumberSettingButton;
import com.ares.hack.settings.EnumSettingType;
import com.ares.hack.settings.SettingManager;
import com.ares.gui.guis.clickgui.buttons.SettingButton;
import com.ares.hack.settings.Setting;
import com.ares.gui.guis.clickgui.buttons.HackButton;
import com.ares.hack.hacks.BaseHack;
import com.ares.gui.guis.clickgui.buttons.CategoryButton;
import com.ares.hack.categories.Category;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class GuiUtil
{
    public static final ResourceLocation white;
    public static Map<Category, CategoryButton> categoryButtonMap;
    public static Map<BaseHack, HackButton> hackButtonMap;
    public static Map<Setting, SettingButton> settingButtonMap;
    private static boolean called;
    
    public static void initializeGui() {
        /*SL:49*/if (GuiUtil.called) {
            /*SL:50*/return;
        }
        GuiUtil.called = /*EL:52*/true;
        /*SL:54*/for (final Category a1 : Category.getAll()) {
            GuiUtil.categoryButtonMap.put(/*EL:56*/a1, new CategoryButton(a1));
            /*SL:58*/for (final BaseHack v1 : a1.getHacks()) {
                GuiUtil.hackButtonMap.put(/*EL:60*/v1, new HackButton(v1));
            }
        }
        /*SL:64*/for (final Setting setting : SettingManager.getAllSettings()) {
            /*SL:66*/if (setting.getType() == EnumSettingType.INTEGER || setting.getType() == EnumSettingType.FLOAT || setting.getType() == EnumSettingType.DOUBLE) {
                GuiUtil.settingButtonMap.put(/*EL:68*/setting, new NumberSettingButton(setting));
            }
            else {
                GuiUtil.settingButtonMap.put(/*EL:71*/setting, new SettingButton(setting));
            }
        }
    }
    
    static BaseHack btnToHack(final Button v1) {
        /*SL:77*/for (final HackButton a1 : HackButton.getAll()) {
            /*SL:79*/if (a1.text.equals(v1.text)) {
                /*SL:81*/return a1.getHack();
            }
        }
        /*SL:84*/return null;
    }
    
    static Button findHoveredBtn(final int a2, final int v1) {
        /*SL:89*/for (final Button a3 : getButtonsInverted()) {
            /*SL:91*/if (!a3.isVisible) {
                continue;
            }
            /*SL:93*/if (a2 >= a3.x && a2 <= a3.x + a3.width && v1 >= a3.y && v1 <= a3.y + a3.height) {
                /*SL:95*/return a3;
            }
        }
        /*SL:98*/return null;
    }
    
    public static ArrayList<Button> getButtons() {
        final ArrayList<Button> list = /*EL:103*/new ArrayList<Button>();
        /*SL:105*/for (final CategoryButton categoryButton : CategoryButton.getAll()) {
            /*SL:107*/list.add(categoryButton);
            /*SL:109*/for (final HackButton v1 : categoryButton.getHackButtons()) {
                /*SL:111*/list.add(v1);
                /*SL:112*/list.addAll(v1.getSettingButtons());
            }
        }
        /*SL:116*/return list;
    }
    
    public static ArrayList<Button> getButtonsInverted() {
        final ArrayList<Button> list = /*EL:121*/new ArrayList<Button>();
        /*SL:123*/for (final CategoryButton categoryButton : CategoryButton.getAllInverted()) {
            /*SL:125*/list.add(categoryButton);
            /*SL:126*/list.addAll(categoryButton.getHackButtons());
            /*SL:128*/for (final HackButton v1 : categoryButton.getHackButtons()) {
                /*SL:130*/list.addAll(v1.getSettingButtons());
            }
        }
        /*SL:134*/return list;
    }
    
    static {
        white = new ResourceLocation("ares", "white.png");
        GuiUtil.categoryButtonMap = new HashMap<Category, CategoryButton>();
        GuiUtil.hackButtonMap = new HashMap<BaseHack, HackButton>();
        GuiUtil.settingButtonMap = new HashMap<Setting, SettingButton>();
        GuiUtil.called = false;
    }
}
