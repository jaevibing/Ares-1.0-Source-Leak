package com.ares.utils.data;

import com.ares.natives.EncryptedStringPool;
import com.ares.Globals;
import net.minecraft.entity.player.EntityPlayer;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FriendUtils
{
    private static final String FILENAME;
    private static ArrayList<String> friends;
    
    public static boolean save() {
        /*SL:35*/if (FriendUtils.friends == null) {
            FriendUtils.friends = /*EL:37*/new ArrayList<String>() {};
        }
        try {
            final PrintWriter printWriter = /*EL:43*/new PrintWriter(FriendUtils.FILENAME);
            /*SL:44*/printWriter.print("");
            /*SL:45*/printWriter.close();
            final BufferedWriter bufferedWriter = /*EL:48*/new BufferedWriter(new FileWriter(FriendUtils.FILENAME));
            /*SL:49*/for (final String v1 : FriendUtils.friends) {
                /*SL:51*/bufferedWriter.write(v1);
                /*SL:52*/bufferedWriter.newLine();
            }
            /*SL:54*/bufferedWriter.flush();
            /*SL:55*/bufferedWriter.close();
            /*SL:56*/return true;
        }
        catch (Exception ex) {
            System.out.println(/*EL:60*/"Could not write friends.txt: " + ex.toString());
            /*SL:61*/ex.printStackTrace();
            System.out.println(FriendUtils.friends);
            /*SL:64*/return false;
        }
    }
    
    public static boolean read() {
        try {
            try {
                FriendUtils.friends = /*EL:73*/new ArrayList<String>();
                final BufferedReader v1 = /*EL:75*/new BufferedReader(new FileReader(FriendUtils.FILENAME));
                String v2;
                /*SL:78*/while ((v2 = v1.readLine()) != null) {
                    FriendUtils.friends.add(/*EL:79*/v2);
                }
                /*SL:81*/v1.close();
                System.out.println(/*EL:82*/"Successfully read friends: " + FriendUtils.friends.toString());
                /*SL:83*/return true;
            }
            catch (FileNotFoundException v5) {
                final File v3 = /*EL:87*/new File(FriendUtils.FILENAME);
                /*SL:88*/v3.createNewFile();
                FriendUtils.friends = /*EL:89*/new ArrayList<String>() {};
                /*SL:90*/return true;
            }
        }
        catch (Exception v4) {
            System.out.println(/*EL:95*/"Could not read friends: " + v4.toString());
            /*SL:96*/v4.printStackTrace();
            /*SL:98*/return false;
        }
    }
    
    public static ArrayList<String> getFriends() {
        /*SL:101*/return FriendUtils.friends;
    }
    
    public static void addFriend(final String a1) {
        /*SL:111*/if (FriendUtils.friends == null) {
            return;
        }
        FriendUtils.friends.add(/*EL:112*/a1);
    }
    
    public static void removeFriend(final String a1) {
        /*SL:123*/if (FriendUtils.friends == null) {
            return;
        }
        FriendUtils.friends.remove(/*EL:124*/a1);
    }
    
    public static boolean isFriend(final String a1) {
        /*SL:129*/return FriendUtils.friends != null && FriendUtils.friends.contains(/*EL:130*/a1);
    }
    
    public static boolean isFriend(final EntityPlayer a1) {
        /*SL:135*/return FriendUtils.friends != null && (Globals.mc.field_71439_g.func_110124_au().equals(/*EL:136*/a1.func_110124_au()) || isFriend(/*EL:137*/a1.func_70005_c_()));
    }
    
    static {
        FILENAME = EncryptedStringPool.poolGet(42);
    }
}
