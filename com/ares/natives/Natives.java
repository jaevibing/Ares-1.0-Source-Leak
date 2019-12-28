package com.ares.natives;

public class Natives
{
    private static Natives INSTANCE;
    
    public static String decryptStr(final String a1) {
        /*SL:14*/if (Natives.INSTANCE == null) {
            Natives.INSTANCE = /*EL:16*/new Natives();
        }
        /*SL:19*/return "";
    }
    
    public static String encryptStr(final String a1) {
        /*SL:24*/if (Natives.INSTANCE == null) {
            Natives.INSTANCE = /*EL:26*/new Natives();
        }
        /*SL:29*/return "";
    }
    
    native String decryptStrNative(final String p0);
    
    native String encryptStrNative(final String p0);
}
