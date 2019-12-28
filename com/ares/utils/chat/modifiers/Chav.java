package com.ares.utils.chat.modifiers;

import java.util.HashMap;
import java.util.Map;

public class Chav extends ChatModifier
{
    private Map<String, String> replaceMap;
    
    public Chav() {
        this.replaceMap = new HashMap<String, String>() {
            {
                this.put("the", "te");
                this.put("check", "czech");
                this.put("your", "ur");
                this.put("who ", "hoo");
                this.put("me", " i");
                this.put("i", " me");
                this.put("ok", "k");
                this.put("inter", "intre");
                this.put("family", "fam");
                this.put("crystal", "cyrtal");
                this.put("i'm", "me is");
                this.put("im", "me is");
                this.put("racist", "rasist");
                this.put("to", "2");
                this.put("have", "av");
                this.put("32k", "32kay");
                this.put("32ks", "32kays");
                this.put("hause", "horse");
                this.put("house", "horse");
                this.put("hausemaster", "horsemaster");
                this.put("housemaster", "horsemaster");
                this.put("jesus", "jebus");
                this.put("spawn", "spwan");
                this.put("cookiedragon", "cookiewagon");
                this.put("cookiedragon234", "cookiewagon234");
                this.put("tigermouthbear", "tigressmouthbear");
                this.put("carbolemons", "carbonlenons");
                this.put("give", "gib");
            }
        };
    }
    
    @Override
    public String getName() {
        /*SL:11*/return "Chav";
    }
    
    @Override
    public String mutate(final String v2) {
        final StringBuilder v3 = /*EL:48*/new StringBuilder();
        final String[] split;
        final String[] v4 = /*EL:51*/split = v2.split(" ");
        for (final String a1 : split) {
            /*SL:53*/v3.append(this.replaceMap.getOrDefault(a1.toLowerCase(), a1)).append(" ");
        }
        /*SL:56*/return v3.toString();
    }
}
