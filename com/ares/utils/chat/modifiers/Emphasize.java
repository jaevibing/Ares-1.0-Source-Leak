package com.ares.utils.chat.modifiers;

public class Emphasize extends ChatModifier
{
    @Override
    public String getName() {
        /*SL:8*/return "Emphasize";
    }
    
    @Override
    public String mutate(String v2) {
        /*SL:15*/v2 = v2.replaceAll(" ", "");
        final StringBuilder v3 = /*EL:17*/new StringBuilder();
        /*SL:18*/for (final char a1 : v2.toCharArray()) {
            /*SL:21*/v3.append(Character.toUpperCase(a1)).append(" ");
        }
        /*SL:23*/return v3.toString();
    }
}
