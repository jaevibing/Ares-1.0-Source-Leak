package com.ares.utils.chat.modifiers;

public class Disabled extends ChatModifier
{
    @Override
    public String getName() {
        /*SL:8*/return "Disabled";
    }
    
    @Override
    public String mutate(final String a1) {
        /*SL:14*/return "\u267f" + a1 + "\u267f";
    }
}
