package com.ares.utils.chat.modifiers;

public class JustLearntEngrish extends ChatModifier
{
    @Override
    public String getName() {
        /*SL:8*/return "JustLearntEngrish";
    }
    
    @Override
    public String mutate(final String v2) {
        final StringBuilder v3 = /*EL:14*/new StringBuilder();
        final String[] split;
        final String[] v4 = /*EL:17*/split = v2.split(" ");
        for (final String a1 : split) {
            /*SL:19*/v3.append(a1.substring(0, 1).toUpperCase()).append(a1.substring(1)).append(" ");
        }
        /*SL:22*/return v3.toString();
    }
}
