package com.ares.api;

import com.ares.Globals;
import com.ares.subguis.MainMenuGui;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.common.collect.HashBiMap;

public class MojangWebApi
{
    public static HashBiMap<String, String> userbase;
    private static final Boolean[] statuses;
    
    public static String grabRealName(final String a1) {
        /*SL:55*/if (MojangWebApi.userbase.containsKey(a1)) {
            System.out.println(/*EL:56*/"Grabbing username from hash map");
            /*SL:58*/return MojangWebApi.userbase.get(a1);
        }
        System.out.println(/*EL:61*/"Grabbing username from Mojang Web Api");
        /*SL:62*/return QueryWebApi(a1, true);
    }
    
    public static String grabRealUUID(final String a1) {
        /*SL:67*/if (MojangWebApi.userbase.containsValue(a1)) {
            System.out.println(/*EL:68*/"Grabbing UUID from hash map");
            /*SL:70*/return MojangWebApi.userbase.inverse().get(a1);
        }
        System.out.println(/*EL:72*/"Grabbing UUID from Mojang Web Api");
        /*SL:73*/return QueryWebApi(a1, false);
    }
    
    private static String QueryWebApi(final String v-9, final Boolean v-8) {
        /*SL:79*/if (v-8) {
            try {
                final String string = /*EL:83*/"https://api.mojang.com/user/profiles/" + v-9.replace("-", "") + "/names";
                final URL url = /*EL:85*/new URL(string);
                final BufferedReader bufferedReader = /*EL:87*/new BufferedReader(new InputStreamReader(url.openStream()));
                String s = /*EL:89*/"Popbob";
                final String line = /*EL:90*/bufferedReader.readLine();
                System.out.println(/*EL:91*/line);
                /*SL:92*/bufferedReader.close();
                /*SL:93*/if (line != null) {
                    final JSONParser a1 = /*EL:94*/new JSONParser();
                    final JSONArray a2 = /*EL:96*/(JSONArray)a1.parse(line);
                    final JSONObject v1 = /*EL:98*/a2.get(a2.size() - 1);
                    /*SL:99*/s = v1.get("name").toString();
                }
                /*SL:103*/bufferedReader.close();
                MojangWebApi.userbase.put(/*EL:104*/v-9, s);
                /*SL:105*/return s;
            }
            catch (MalformedURLException ex) {
                System.out.println(/*EL:107*/"MALIGNED URL, CARBOLEMONS IS DUMB IF YOU ARE READING THIS, BECAUSE, WHAT, IMPOSSIBLE... LITCHERALLLY...");
                /*SL:108*/return "";
            }
            catch (IOException ex2) {
                System.out.println(/*EL:110*/"uh, something went horribly wrong if you are seeing this in your log.");
                /*SL:111*/return "";
            }
            catch (ParseException ex3) {
                System.out.println(/*EL:113*/"JSON userdata was parsed wrong, shit.");
                /*SL:114*/return "";
            }
        }
        try {
            final String string2 = /*EL:118*/"https://api.mojang.com/users/profiles/minecraft/" + v-9;
            final URL url2 = /*EL:119*/new URL(string2);
            final BufferedReader bufferedReader2 = /*EL:121*/new BufferedReader(new InputStreamReader(url2.openStream()));
            String string3 = /*EL:123*/"00000000000000000000000000000000";
            String s = /*EL:124*/"00000000-0000-0000-0000-000000000000";
            final String line = /*EL:125*/"Popbob";
            final String line2 = /*EL:126*/bufferedReader2.readLine();
            /*SL:127*/bufferedReader2.close();
            /*SL:128*/if (line2 != null) {
                final JSONParser v2 = /*EL:129*/new JSONParser();
                final JSONObject v1 = /*EL:130*/(JSONObject)v2.parse(line2);
                /*SL:131*/string3 = v1.get("id").toString();
                final String v3 = /*EL:132*/new String(string3);
                final StringBuilder v4 = /*EL:133*/new StringBuilder(v3);
                /*SL:134*/v4.insert(8, '-');
                /*SL:135*/v4.insert(13, '-');
                /*SL:136*/v4.insert(18, '-');
                /*SL:137*/v4.insert(23, '-');
                /*SL:138*/s = v4.toString();
            }
            MojangWebApi.userbase.put(/*EL:142*/s, v-9);
            /*SL:143*/return s;
        }
        catch (MalformedURLException ex4) {
            System.out.println(/*EL:145*/"MALIGNED URL, CARBOLEMONS IS DUMB IF YOU ARE READING THIS, BECAUSE, WHAT, IMPOSSIBLE... LITCHERALLLY...");
            /*SL:146*/return "";
        }
        catch (IOException ex5) {
            System.out.println(/*EL:148*/"uh, something went horribly wrong if you are seeing this in your log.");
            /*SL:149*/return "";
        }
        catch (ParseException ex6) {
            System.out.println(/*EL:151*/"JSON userdata was parsed wrong, shit.");
            /*SL:152*/return "";
        }
    }
    
    private static boolean isAuthUpImpl() {
        /*SL:195*/return isWebsiteUp("https://authserver.mojang.com/authenticate");
    }
    
    private static boolean isSeshUpImpl() {
        /*SL:200*/return isWebsiteUp("https://sessionserver.mojang.com/");
    }
    
    public static boolean isAuthUp() {
        /*SL:205*/synchronized (MojangWebApi.statuses) {
            /*SL:207*/return MojangWebApi.statuses[0];
        }
    }
    
    public static boolean isSeshUp() {
        /*SL:213*/synchronized (MojangWebApi.statuses) {
            /*SL:215*/return MojangWebApi.statuses[1];
        }
    }
    
    private static boolean isWebsiteUp(final String v-1) {
        try {
            final URL a1 = /*EL:223*/new URL(v-1);
            final HttpsURLConnection v1 = /*EL:224*/(HttpsURLConnection)a1.openConnection();
            /*SL:225*/v1.connect();
            /*SL:226*/v1.disconnect();
            /*SL:227*/return true;
        }
        catch (IOException v2) {
            /*SL:231*/v2.printStackTrace();
            /*SL:232*/return false;
        }
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    com/google/common/collect/HashBiMap.create:()Lcom/google/common/collect/HashBiMap;
        //     3: putstatic       com/ares/api/MojangWebApi.userbase:Lcom/google/common/collect/HashBiMap;
        //     6: iconst_2       
        //     7: anewarray       Ljava/lang/Boolean;
        //    10: dup            
        //    11: iconst_0       
        //    12: iconst_1       
        //    13: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    16: aastore        
        //    17: dup            
        //    18: iconst_1       
        //    19: iconst_1       
        //    20: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    23: aastore        
        //    24: putstatic       com/ares/api/MojangWebApi.statuses:[Ljava/lang/Boolean;
        //    27: new             Ljava/lang/Thread;
        //    30: dup            
        //    31: invokedynamic   run:()Ljava/lang/Runnable;
        //    36: ldc_w           "Status checker"
        //    39: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;Ljava/lang/String;)V
        //    42: invokevirtual   java/lang/Thread.start:()V
        //    45: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Unsupported node type: com.strobel.decompiler.ast.Lambda
        //     at com.strobel.decompiler.ast.Error.unsupportedNode(Error.java:32)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:612)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:586)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:598)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:586)
        //     at com.strobel.decompiler.ast.GotoRemoval.transformLeaveStatements(GotoRemoval.java:625)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotosCore(GotoRemoval.java:57)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotos(GotoRemoval.java:53)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:276)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
