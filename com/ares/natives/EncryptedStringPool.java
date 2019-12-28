package com.ares.natives;

import com.ares.DllManager;
import net.minecraftforge.fml.common.FMLLog;
import java.util.HashMap;

public class EncryptedStringPool
{
    private static final HashMap<Integer, String> pool;
    
    public static String poolGet(final int a1) {
        final String v1 = EncryptedStringPool.pool.get(/*EL:105*/a1);
        /*SL:106*/return v1;
    }
    
    public static void doEncryptionStuff() {
        FMLLog.log.info(/*EL:114*/"Encrypting things");
        final long currentTimeMillis = /*EL:115*/System.currentTimeMillis();
        int n = /*EL:118*/0;
        /*SL:119*/for (final String v1 : new String[] { "Error initialising class ", "Ares tried to load ", " hack, out of which ", " failed", "Failed hack: ", "x", "y", "open", "prefix", "Ares startup finished", "Hack with name ", " not found", "Hack of class ", "Logging into an online account with email: ", "session", "field_71449_j", "Logged in successfully:", "Session ID: ", "Username: ", "textures/cape_ares.png", "textures/cape_ares_dev.png", "http://pastebin.com/raw/ZMZcF3nJ", "Gave capes to: ", "Could not fetch capes", "http://pastebin.com/raw/g4wjzg5U", "Could not fetch dev capes", "dark_red", "red", "gold", "yellow", "dark_green", "green", "aqua", "dark_aqua", "dark_blue", "blue", "light_purple", "dark_purple", "white", "gray", "dark_gray", "black", "Ares/friends.txt" }) {
            System.out.print(/*EL:165*/"\nput(" + n + ",\"" + DllManager.encrypt(v1) + "\");      // " + v1);
            /*SL:166*/++n;
        }
        System.out.print(/*EL:169*/"\nTook " + (System.currentTimeMillis() - currentTimeMillis) / 1000.0 + "s");
        /*SL:171*/System.exit(-1);
    }
    
    static {
        pool = new HashMap<Integer, String>() {
            {
                this.put(0, "Error initialising class ");
                this.put(1, "Ares tried to load ");
                this.put(2, " hack, out of which ");
                this.put(3, " failed");
                this.put(4, "Failed hack: ");
                this.put(5, "x");
                this.put(6, "y");
                this.put(7, "open");
                this.put(8, "prefix");
                this.put(9, "Ares startup finished");
                this.put(10, "Hack with name ");
                this.put(11, " not found");
                this.put(12, "Hack of class ");
                this.put(13, "Logging into an online account with email: ");
                this.put(14, "session");
                this.put(15, "field_71449_j");
                this.put(16, "Logged in successfully:");
                this.put(17, "Session ID: ");
                this.put(18, "Username: ");
                this.put(19, "textures/cape_ares.png");
                this.put(20, "textures/cape_ares_dev.png");
                this.put(21, "http://pastebin.com/raw/ZMZcF3nJ");
                this.put(22, "Gave capes to: ");
                this.put(23, "Could not fetch capes");
                this.put(24, "http://pastebin.com/raw/g4wjzg5U");
                this.put(25, "Could not fetch dev capes");
                this.put(26, "dark_red");
                this.put(27, "red");
                this.put(28, "gold");
                this.put(29, "yellow");
                this.put(30, "dark_green");
                this.put(31, "green");
                this.put(32, "aqua");
                this.put(33, "dark_aqua");
                this.put(34, "dark_blue");
                this.put(35, "blue");
                this.put(36, "light_purple");
                this.put(37, "dark_purple");
                this.put(38, "white");
                this.put(39, "gray");
                this.put(40, "dark_gray");
                this.put(41, "black");
                this.put(42, "Ares/friends.txt");
                this.put(43, "http://pastebin.com/raw/sYrfgjsY");
            }
        };
    }
}
