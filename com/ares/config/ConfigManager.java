package com.ares.config;

import java.io.File;

public class ConfigManager
{
    static final File saveDir;
    
    public static void save() {
        try {
            /*SL:29*/ConfigSaveManager.save(ConfigManager.saveDir);
        }
        catch (Exception v1) {
            System.out.println(/*EL:33*/"Error saving config");
            /*SL:34*/v1.printStackTrace();
        }
    }
    
    public static void read() {
        try {
            /*SL:42*/ConfigReadManager.read(ConfigManager.saveDir);
        }
        catch (Exception v1) {
            System.out.println(/*EL:46*/"Error reading config");
            /*SL:47*/v1.printStackTrace();
        }
    }
    
    static {
        saveDir = new File("Ares/config.json");
        try {
            if (!ConfigManager.saveDir.exists()) {
                ConfigManager.saveDir.createNewFile();
            }
        }
        catch (Exception v1) {
            v1.printStackTrace();
        }
    }
}
