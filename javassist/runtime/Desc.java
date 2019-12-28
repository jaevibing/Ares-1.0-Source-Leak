package javassist.runtime;

public class Desc
{
    public static boolean useContextClassLoader;
    
    private static Class getClassObject(final String a1) throws ClassNotFoundException {
        /*SL:40*/if (Desc.useContextClassLoader) {
            /*SL:41*/return Class.forName(a1, true, Thread.currentThread().getContextClassLoader());
        }
        /*SL:43*/return Class.forName(a1);
    }
    
    public static Class getClazz(final String v1) {
        try {
            /*SL:52*/return getClassObject(v1);
        }
        catch (ClassNotFoundException a1) {
            /*SL:55*/throw new RuntimeException("$class: internal error, could not find class '" + v1 + "' (Desc.useContextClassLoader: " + /*EL:58*/Boolean.toString(Desc.useContextClassLoader) + ")", a1);
        }
    }
    
    public static Class[] getParams(final String a1) {
        /*SL:67*/if (a1.charAt(0) != '(') {
            /*SL:68*/throw new RuntimeException("$sig: internal error");
        }
        /*SL:70*/return getType(a1, a1.length(), 1, 0);
    }
    
    public static Class getType(final String a1) {
        final Class[] v1 = getType(/*EL:78*/a1, a1.length(), 0, 0);
        /*SL:79*/if (v1 == null || v1.length != 1) {
            /*SL:80*/throw new RuntimeException("$type: internal error");
        }
        /*SL:82*/return v1[0];
    }
    
    private static Class[] getType(final String v-3, final int v-2, final int v-1, final int v0) {
        /*SL:88*/if (v-1 >= v-2) {
            /*SL:89*/return new Class[v0];
        }
        final char v = /*EL:91*/v-3.charAt(v-1);
        Class v2 = null;
        /*SL:92*/switch (v) {
            case 'Z': {
                final Class a1 = Boolean.TYPE;
                /*SL:95*/break;
            }
            case 'C': {
                final Class a2 = Character.TYPE;
                /*SL:98*/break;
            }
            case 'B': {
                final Class a3 = Byte.TYPE;
                /*SL:101*/break;
            }
            case 'S': {
                final Class a4 = Short.TYPE;
                /*SL:104*/break;
            }
            case 'I': {
                /*SL:106*/v2 = Integer.TYPE;
                /*SL:107*/break;
            }
            case 'J': {
                /*SL:109*/v2 = Long.TYPE;
                /*SL:110*/break;
            }
            case 'F': {
                /*SL:112*/v2 = Float.TYPE;
                /*SL:113*/break;
            }
            case 'D': {
                /*SL:115*/v2 = Double.TYPE;
                /*SL:116*/break;
            }
            case 'V': {
                /*SL:118*/v2 = Void.TYPE;
                /*SL:119*/break;
            }
            case 'L':
            case '[': {
                /*SL:122*/return getClassType(v-3, v-2, v-1, v0);
            }
            default: {
                /*SL:124*/return new Class[v0];
            }
        }
        final Class[] v3 = getType(/*EL:127*/v-3, v-2, v-1 + 1, v0 + 1);
        /*SL:128*/v3[v0] = v2;
        /*SL:129*/return v3;
    }
    
    private static Class[] getClassType(final String a3, final int a4, final int v1, final int v2) {
        int v3;
        /*SL:135*/for (v3 = v1; a3.charAt(v3) == '['; /*SL:136*/++v3) {}
        /*SL:138*/if (a3.charAt(v3) == 'L') {
            /*SL:139*/v3 = a3.indexOf(59, v3);
            /*SL:140*/if (v3 < 0) {
                /*SL:141*/throw new IndexOutOfBoundsException("bad descriptor");
            }
        }
        final String v4;
        /*SL:145*/if (a3.charAt(v1) == 'L') {
            final String a5 = /*EL:146*/a3.substring(v1 + 1, v3);
        }
        else {
            /*SL:148*/v4 = a3.substring(v1, v3 + 1);
        }
        final Class[] v5 = getType(/*EL:150*/a3, a4, v3 + 1, v2 + 1);
        try {
            /*SL:152*/v5[v2] = getClassObject(v4.replace('/', '.'));
        }
        catch (ClassNotFoundException a6) {
            /*SL:156*/throw new RuntimeException(a6.getMessage());
        }
        /*SL:159*/return v5;
    }
    
    static {
        Desc.useContextClassLoader = false;
    }
}
