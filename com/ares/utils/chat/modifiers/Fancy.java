package com.ares.utils.chat.modifiers;

import org.apache.commons.lang3.ArrayUtils;

public class Fancy extends ChatModifier
{
    private static final char[] nonUnicodeChars;
    
    @Override
    public String getName() {
        /*SL:12*/return "Fancy";
    }
    
    @Override
    public String mutate(final String v2) {
        final StringBuilder v3 = /*EL:18*/new StringBuilder();
        /*SL:20*/for (final char a1 : v2.toCharArray()) {
            /*SL:22*/if (a1 < '!' || a1 > '\u0080') {
                /*SL:24*/v3.append(a1);
            }
            else/*SL:26*/ if (ArrayUtils.contains(Fancy.nonUnicodeChars, a1)) {
                /*SL:28*/v3.append(a1);
            }
            else {
                /*SL:32*/v3.append(Character.toChars(a1 + '\ufee0'));
            }
        }
        /*SL:36*/return v3.toString();
    }
    
    static {
        nonUnicodeChars = new char[] { '(', ')', '{', '}', '[', ']', '|' };
    }
}
