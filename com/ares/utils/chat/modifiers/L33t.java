package com.ares.utils.chat.modifiers;

import java.util.HashMap;
import java.util.Map;

public class L33t extends ChatModifier
{
    private Map<String, String> replaceMap;
    
    public L33t() {
        this.replaceMap = new HashMap<String, String>() {
            {
                this.put("a", "4");
                this.put("b", "8");
                this.put("e", "3");
                this.put("i", "1");
                this.put("l", "1");
                this.put("o", "0");
                this.put("s", "5");
                this.put("t", "7");
                this.put("z", "5");
            }
        };
    }
    
    @Override
    public String getName() {
        /*SL:12*/return "L33t";
    }
    
    @Override
    public String mutate(final String v2) {
        final StringBuilder v3 = /*EL:30*/new StringBuilder();
        /*SL:31*/for (final String a1 : v2.split("")) {
            /*SL:33*/v3.append(this.replaceMap.getOrDefault(a1.toLowerCase(), a1));
        }
        /*SL:36*/return v3.toString();
    }
}
