package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JavaVersion
{
    private static double current;
    
    public static double current() {
        /*SL:43*/if (JavaVersion.current == 0.0) {
            JavaVersion.current = resolveCurrentVersion();
        }
        /*SL:46*/return JavaVersion.current;
    }
    
    private static double resolveCurrentVersion() {
        final String v1 = /*EL:50*/System.getProperty("java.version");
        final Matcher v2 = /*EL:51*/Pattern.compile("[0-9]+\\.[0-9]+").matcher(v1);
        /*SL:52*/if (v2.find()) {
            /*SL:53*/return Double.parseDouble(v2.group());
        }
        /*SL:55*/return 1.6;
    }
    
    static {
        JavaVersion.current = 0.0;
    }
}
