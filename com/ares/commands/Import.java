package com.ares.commands;

import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import java.io.File;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.data.FriendUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;

public class Import extends CommandBase
{
    public Import() {
        super(new String[] { "import", "importfriends" });
    }
    
    @Override
    public boolean execute(final String[] v0) {
        /*SL:18*/if (v0.length == 1) {
            try {
                /*SL:22*/if (v0[0].equalsIgnoreCase("impact")) {
                    final String v = /*EL:24*/"Impact/friends.cfg";
                    final BufferedReader v2 = /*EL:25*/new BufferedReader(new FileReader(v));
                    String v3;
                    /*SL:28*/while ((v3 = v2.readLine()) != null) {
                        final String a1 = /*EL:30*/v3.split(":")[0];
                        /*SL:31*/if (!FriendUtils.isFriend(a1)) {
                            /*SL:33*/FriendUtils.addFriend(a1);
                            /*SL:34*/ChatUtils.printMessage("Added '" + a1 + "' to your friends!", "green");
                        }
                        else {
                            /*SL:37*/ChatUtils.printMessage("'" + a1 + "' was already a friend", "red");
                        }
                    }
                    /*SL:40*/v2.close();
                    System.out.println(/*EL:41*/"Successfully imported friends");
                }
                else/*SL:42*/ if (v0[0].equalsIgnoreCase("wwe")) {
                    final String v = /*EL:44*/"WWE/friends.txt";
                    final BufferedReader v2 = /*EL:45*/new BufferedReader(new FileReader(v));
                    String v3;
                    /*SL:48*/while ((v3 = v2.readLine()) != null) {
                        final String v4 = /*EL:50*/v3.split(" ")[0];
                        /*SL:51*/if (!FriendUtils.isFriend(v4)) {
                            /*SL:53*/FriendUtils.addFriend(v4);
                            /*SL:54*/ChatUtils.printMessage("Added '" + v4 + "' to your friends!", "green");
                        }
                        else {
                            /*SL:57*/ChatUtils.printMessage("'" + v4 + "' was already a friend", "red");
                        }
                    }
                    /*SL:60*/v2.close();
                    System.out.println(/*EL:61*/"Successfully imported friends");
                }
                else/*SL:62*/ if (v0[0].equalsIgnoreCase("future")) {
                    final String v = /*EL:64*/System.getProperty("user.home") + "/Future/friends.json";
                    final String v5 = /*EL:65*/FileUtils.readFileToString(new File(v), Charset.defaultCharset());
                    final JSONObject v6 = /*EL:67*/new JSONObject(v5);
                    final JSONObject v7 = /*EL:68*/v6.getJSONObject("friend-label");
                    final Object[] array;
                    final Object[] v8 = /*EL:72*/array = v7.keySet().toArray();
                    for (final Object v9 : array) {
                        final String v10 = /*EL:74*/v9.toString();
                        /*SL:75*/if (!FriendUtils.isFriend(v10)) {
                            /*SL:77*/FriendUtils.addFriend(v10);
                            /*SL:78*/ChatUtils.printMessage("Added '" + v10 + "' to your friends!", "green");
                        }
                        else {
                            /*SL:81*/ChatUtils.printMessage("'" + v10 + "' was already a friend", "red");
                        }
                    }
                    System.out.println(/*EL:84*/"Successfully imported friends");
                }
            }
            catch (Exception v11) {
                System.out.println(/*EL:89*/"Could not import to friends.txt: " + v11.toString());
                /*SL:90*/v11.printStackTrace();
                System.out.println(/*EL:91*/FriendUtils.getFriends());
            }
            /*SL:99*/return true;
        }
        return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:105*/return "-import <Impact/WWE only ones supported now>";
    }
}
