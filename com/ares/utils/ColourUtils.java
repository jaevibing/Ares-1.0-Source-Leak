package com.ares.utils;

import com.ares.natives.EncryptedStringPool;
import java.util.HashMap;
import java.awt.Color;
import java.util.Map;
import com.ares.Globals;

public class ColourUtils implements Globals
{
    public static final Map<String, String> colours;
    
    public static Color rainbow() {
        final float v1 = /*EL:32*/(System.nanoTime() + 10L) / 1.0E10f % 1.0f;
        final long v2 = /*EL:33*/Long.parseLong(Integer.toHexString(Color.HSBtoRGB(v1, 1.0f, 1.0f)), 16);
        final Color v3 = /*EL:34*/new Color((int)v2);
        /*SL:35*/return new Color(v3.getRed() / 255.0f, v3.getGreen() / 255.0f, v3.getBlue() / 255.0f, v3.getAlpha() / 255.0f);
    }
    
    public static void drawRainbowString(final String a1, final int a2, final int a3) {
        drawRainbowString(/*EL:40*/a1, a2, a3, false);
    }
    
    public static void drawRainbowString(final String a4, final int v1, final int v2, final boolean v3) {
        final float v4 = /*EL:52*/System.currentTimeMillis() % 11520L / 11520.0f;
        int v5 = /*EL:53*/v1;
        /*SL:54*/for (final char a5 : a4.toCharArray()) {
            final float a6 = /*EL:56*/v4 + v2 / ColourUtils.mc.field_71440_d + v1 / ColourUtils.mc.field_71443_c;
            final int a7 = /*EL:57*/Color.HSBtoRGB(a6, 1.0f, 1.0f);
            ColourUtils.mc.field_71466_p.func_175065_a(/*EL:59*/String.valueOf(a5), (float)v5, (float)v2, a7, v3);
            /*SL:60*/v5 += ColourUtils.mc.field_71466_p.func_78263_a(a5);
        }
    }
    
    public static String strToColour(final String a1) {
        /*SL:117*/return ColourUtils.colours.getOrDefault(a1.replace(" ", "_").trim().toLowerCase(), "§d");
    }
    
    public static float[] getAresRed(final float a1) {
        /*SL:122*/return new float[] { 0.54f, 0.03f, 0.03f, a1 };
    }
    
    public static String getAresRedString() {
        /*SL:127*/return "890707";
    }
    
    static {
        colours = new HashMap<String, String>() {
            {
                this.put(EncryptedStringPool.poolGet(26), "§4");
                this.put(EncryptedStringPool.poolGet(27), "§c");
                this.put(EncryptedStringPool.poolGet(28), "§6");
                this.put(EncryptedStringPool.poolGet(29), "§e");
                this.put(EncryptedStringPool.poolGet(30), "§2");
                this.put(EncryptedStringPool.poolGet(31), "§a");
                this.put(EncryptedStringPool.poolGet(32), "§b");
                this.put(EncryptedStringPool.poolGet(33), "§3");
                this.put(EncryptedStringPool.poolGet(34), "§1");
                this.put(EncryptedStringPool.poolGet(35), "§9");
                this.put(EncryptedStringPool.poolGet(36), "§d");
                this.put(EncryptedStringPool.poolGet(37), "§5");
                this.put(EncryptedStringPool.poolGet(38), "§f");
                this.put(EncryptedStringPool.poolGet(39), "§7");
                this.put(EncryptedStringPool.poolGet(40), "§8");
                this.put(EncryptedStringPool.poolGet(41), "§0");
            }
        };
    }
    
    public enum Colours
    {
        DARKRED("Dark Red"), 
        RED("Red"), 
        GOLD("Gold"), 
        YELLOW("Yellow"), 
        DARKGREEN("Dark Green"), 
        GREEN("Green"), 
        AQUA("Aqua"), 
        DARKAQUA("Dark Aqua"), 
        DARKBLUE("Dark Blue"), 
        BLUE("Blue"), 
        LIGHTPURPLE("Light Purple"), 
        DARKPURPLE("Dark Purple"), 
        WHITE("White"), 
        GRAY("Gray"), 
        DARKGRAY("Dark Gray"), 
        BLACK("Black");
        
        private String name;
        
        private Colours(final String a1) {
            this.name = a1;
        }
        
        @Override
        public String toString() {
            /*SL:92*/return this.name;
        }
    }
}
