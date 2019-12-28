package com.ares.config;

import java.util.ArrayList;
import net.minecraftforge.fml.common.FMLLog;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Iterator;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import com.ares.Ares;
import com.ares.commands.Command;
import com.ares.gui.guis.clickgui.buttons.CategoryButton;
import com.ares.hack.settings.Setting;
import com.ares.hack.settings.SettingManager;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import org.json.JSONObject;
import java.io.File;

@Deprecated
public class ConfigJsonManager
{
    private static final File FILE;
    private static JSONObject rootObj;
    private static JSONObject hacksObj;
    private static JSONObject categoriesObj;
    private static JSONObject commandsObj;
    private static JSONObject hudObj;
    
    public static void read() {
        read(ConfigJsonManager.FILE);
    }
    
    public static void read(final File v-6) {
        try {
            /*SL:42*/if (!v-6.exists()) {
                /*SL:44*/v-6.createNewFile();
            }
            String a2 = /*EL:48*/FileUtils.readFileToString(v-6, Charset.defaultCharset());
            /*SL:49*/if (a2.trim().isEmpty() && /*EL:51*/v-6.renameTo(v-6)) {
                save();
                /*SL:54*/a2 = FileUtils.readFileToString(v-6, Charset.defaultCharset());
            }
            ConfigJsonManager.rootObj = /*EL:57*/new JSONObject(a2);
            ConfigJsonManager.hacksObj = ConfigJsonManager.rootObj.getJSONObject(/*EL:58*/"Hacks");
            ConfigJsonManager.categoriesObj = ConfigJsonManager.rootObj.getJSONObject(/*EL:59*/"Category");
            ConfigJsonManager.commandsObj = ConfigJsonManager.rootObj.getJSONObject(/*EL:60*/"Commands");
            ConfigJsonManager.hudObj = ConfigJsonManager.rootObj.getJSONObject(/*EL:61*/"hud");
            /*SL:63*/for (final BaseHack a3 : HackManager.getAll()) {
                /*SL:65*/if (getHackVal(a3, "Enabled") != null) {
                    try {
                        /*SL:69*/a3.setEnabled((boolean)getHackVal(a3, "Enabled"));
                    }
                    catch (Exception a1) {
                        /*SL:73*/a1.printStackTrace();
                    }
                }
                /*SL:77*/for (final Setting setting : SettingManager.getSettingsByMod(a3)) {
                    try {
                        /*SL:81*/if (getHackVal(a3, setting.getName()) == null) {
                            continue;
                        }
                        final Object v0 = getHackVal(/*EL:83*/a3, setting.getName());
                        /*SL:84*/if (v0 == null || setting.getValueType() == null) {
                            continue;
                        }
                        final Object v = /*EL:86*/setting.getValueType().cast(v0);
                        /*SL:87*/if (v == null) {
                            continue;
                        }
                        /*SL:88*/setting.setValue(v);
                    }
                    catch (Exception v2) {
                        /*SL:94*/v2.printStackTrace();
                    }
                }
            }
            /*SL:99*/for (final CategoryButton categoryButton : CategoryButton.getAll()) {
                /*SL:101*/if (getCategoryVal(categoryButton, "x") != null) {
                    /*SL:103*/categoryButton.x = (int)getCategoryVal(categoryButton, "x");
                }
                /*SL:105*/if (getCategoryVal(categoryButton, "y") != null) {
                    /*SL:107*/categoryButton.y = (int)getCategoryVal(categoryButton, "y");
                }
                /*SL:109*/if (getCategoryVal(categoryButton, "open") != null) {
                    /*SL:111*/categoryButton.rightClickToggled = (boolean)getCategoryVal(categoryButton, "open");
                }
            }
            /*SL:115*/if (getCommandVal("prefix") != null) {
                Command.commandPrefix = (String)getCommandVal(/*EL:117*/"prefix");
            }
            /*SL:120*/for (final AbstractHudElement abstractHudElement : Ares.hudManager.elements) {
                final Object hudVal = getHudVal(/*EL:122*/abstractHudElement.getName(), "Visible");
                /*SL:123*/if (hudVal instanceof Boolean) {
                    /*SL:125*/abstractHudElement.setVisible((boolean)hudVal);
                }
                final Object hudVal2 = getHudVal(/*EL:128*/abstractHudElement.getName(), "x");
                /*SL:129*/if (hudVal2 instanceof Integer) {
                    /*SL:131*/abstractHudElement.getPos().x = (int)hudVal2;
                }
                final Object v0 = getHudVal(/*EL:134*/abstractHudElement.getName(), "y");
                /*SL:135*/if (v0 instanceof Integer) {
                    /*SL:137*/abstractHudElement.getPos().y = (int)v0;
                }
            }
        }
        catch (Exception ex) {
            System.out.println(/*EL:143*/"ERROR READING ARES CONFIG FILE!!!");
            /*SL:144*/ex.printStackTrace();
        }
    }
    
    private static Object getHackVal(final BaseHack a1, final String a2) {
        /*SL:148*/return getHackVal(a1.name, a2);
    }
    
    private static Object getHackVal(final String a2, final String v1) {
        try {
            /*SL:154*/return ConfigJsonManager.hacksObj.getJSONObject(a2).get(v1);
        }
        catch (Exception a3) {
            /*SL:156*/return null;
        }
    }
    
    private static Object getCategoryVal(final CategoryButton a1, final String a2) {
        /*SL:162*/return getCategoryVal(a1.text, a2);
    }
    
    private static Object getCategoryVal(final String a2, final String v1) {
        try {
            /*SL:168*/return ConfigJsonManager.categoriesObj.getJSONObject(a2).get(v1);
        }
        catch (Exception a3) {
            /*SL:170*/return null;
        }
    }
    
    private static Object getCommandVal(final String v1) {
        try {
            /*SL:177*/return ConfigJsonManager.commandsObj.get(v1);
        }
        catch (Exception a1) {
            /*SL:179*/return null;
        }
    }
    
    private static Object getHudVal(final String a2, final String v1) {
        try {
            /*SL:186*/return ConfigJsonManager.hudObj.getJSONObject(a2).get(v1);
        }
        catch (Exception a3) {
            /*SL:188*/return null;
        }
    }
    
    public static void save() {
        save(ConfigJsonManager.FILE);
    }
    
    public static void save(final File v0) {
        ConfigJsonManager.rootObj.put(/*EL:200*/"Hacks", processHacks());
        ConfigJsonManager.rootObj.put(/*EL:201*/"Category", processCategories());
        ConfigJsonManager.rootObj.put(/*EL:202*/"Commands", processCommands());
        ConfigJsonManager.rootObj.put(/*EL:203*/"hud", processHud());
        try {
            /*SL:207*/if (!v0.exists()) {
                /*SL:209*/v0.createNewFile();
            }
            /*SL:213*/if (v0.renameTo(v0)) {
                final PrintWriter a1 = /*EL:215*/new PrintWriter(new FileWriter(v0));
                /*SL:216*/a1.print(ConfigJsonManager.rootObj.toString(4));
                /*SL:217*/a1.close();
                FMLLog.log.info(ConfigJsonManager.rootObj.toString());
            }
            else {
                System.out.println(/*EL:224*/"Failed to save, file already in use");
            }
        }
        catch (Exception v) {
            System.out.println(/*EL:229*/"ERROR SAVING ARES CONFIG FILE!!!");
            /*SL:230*/v.printStackTrace();
        }
    }
    
    private static JSONObject processHacks() {
        /*SL:236*/for (final BaseHack v2 : HackManager.getAll()) {
            final JSONObject a2 = /*EL:238*/new JSONObject();
            /*SL:240*/a2.put("Enabled", v2.getEnabled());
            final ArrayList<Setting> settingsByMod = /*EL:242*/SettingManager.getSettingsByMod(v2);
            /*SL:243*/if (settingsByMod != null) {
                /*SL:245*/for (final Setting v1 : settingsByMod) {
                    /*SL:247*/a2.put(v1.getName(), v1.getValue());
                }
            }
            ConfigJsonManager.hacksObj.put(/*EL:251*/v2.name, a2);
        }
        /*SL:254*/return ConfigJsonManager.hacksObj;
    }
    
    private static JSONObject processCategories() {
        /*SL:259*/for (final CategoryButton v0 : CategoryButton.getAll()) {
            final JSONObject v = /*EL:261*/new JSONObject();
            /*SL:263*/v.put("x", v0.x);
            /*SL:264*/v.put("y", v0.y);
            /*SL:266*/v.put("open", v0.rightClickToggled);
            ConfigJsonManager.categoriesObj.put(/*EL:268*/v0.text, v);
        }
        /*SL:271*/return ConfigJsonManager.categoriesObj;
    }
    
    private static JSONObject processCommands() {
        ConfigJsonManager.commandsObj.put(/*EL:276*/"prefix", Command.commandPrefix);
        /*SL:278*/return ConfigJsonManager.commandsObj;
    }
    
    private static JSONObject processHud() {
        /*SL:283*/for (final AbstractHudElement v0 : Ares.hudManager.elements) {
            final JSONObject v = /*EL:285*/new JSONObject();
            /*SL:287*/v.put("Visible", (Object)v0.isVisible());
            /*SL:288*/v.put("x", (Object)v0.getPos().x);
            /*SL:289*/v.put("y", (Object)v0.getPos().y);
            ConfigJsonManager.hudObj.put(/*EL:291*/v0.getName(), v);
        }
        /*SL:294*/return ConfigJsonManager.hudObj;
    }
    
    static {
        FILE = new File("Ares/config.json");
        ConfigJsonManager.rootObj = new JSONObject();
        ConfigJsonManager.hacksObj = new JSONObject();
        ConfigJsonManager.categoriesObj = new JSONObject();
        ConfigJsonManager.commandsObj = new JSONObject();
        ConfigJsonManager.hudObj = new JSONObject();
    }
}
