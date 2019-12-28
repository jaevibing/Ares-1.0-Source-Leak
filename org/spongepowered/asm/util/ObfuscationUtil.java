package org.spongepowered.asm.util;

public abstract class ObfuscationUtil
{
    public static String mapDescriptor(final String a1, final IClassRemapper a2) {
        /*SL:65*/return remapDescriptor(a1, a2, false);
    }
    
    public static String unmapDescriptor(final String a1, final IClassRemapper a2) {
        /*SL:76*/return remapDescriptor(a1, a2, true);
    }
    
    private static String remapDescriptor(final String a3, final IClassRemapper v1, final boolean v2) {
        final StringBuilder v3 = /*EL:80*/new StringBuilder();
        StringBuilder v4 = /*EL:81*/null;
        /*SL:83*/for (int a4 = 0; a4 < a3.length(); ++a4) {
            final char a5 = /*EL:84*/a3.charAt(a4);
            /*SL:85*/if (v4 != null) {
                /*SL:86*/if (a5 == ';') {
                    /*SL:87*/v3.append('L').append(remap(v4.toString(), v1, v2)).append(';');
                    /*SL:88*/v4 = null;
                }
                else {
                    /*SL:90*/v4.append(a5);
                }
            }
            else/*SL:94*/ if (a5 == 'L') {
                /*SL:95*/v4 = new StringBuilder();
            }
            else {
                /*SL:97*/v3.append(a5);
            }
        }
        /*SL:101*/if (v4 != null) {
            /*SL:102*/throw new IllegalArgumentException("Invalid descriptor '" + a3 + "', missing ';'");
        }
        /*SL:105*/return v3.toString();
    }
    
    private static Object remap(final String a1, final IClassRemapper a2, final boolean a3) {
        final String v1 = /*EL:109*/a3 ? a2.unmap(a1) : a2.map(a1);
        /*SL:110*/return (v1 != null) ? v1 : a1;
    }
    
    public interface IClassRemapper
    {
        String map(String p0);
        
        String unmap(String p0);
    }
}
