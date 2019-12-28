package com.ares.utils.chat.modifiers;

public class Reverse extends ChatModifier
{
    @Override
    public String getName() {
        /*SL:8*/return "Reverse";
    }
    
    @Override
    public String mutate(final String a1) {
        /*SL:14*/return new StringBuilder(a1).reverse().toString();
    }
}
