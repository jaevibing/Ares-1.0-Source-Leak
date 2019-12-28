package com.ares.utils.chat.modifiers;

public class Soviet extends ChatModifier
{
    @Override
    public String getName() {
        /*SL:8*/return "Soviet";
    }
    
    @Override
    public String mutate(final String a1) {
        /*SL:14*/return "\u262d" + a1.replace("my", "our") + "\u262d";
    }
}
