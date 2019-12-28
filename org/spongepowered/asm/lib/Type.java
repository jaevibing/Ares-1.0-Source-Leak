package org.spongepowered.asm.lib;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public class Type
{
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final int METHOD = 11;
    public static final Type VOID_TYPE;
    public static final Type BOOLEAN_TYPE;
    public static final Type CHAR_TYPE;
    public static final Type BYTE_TYPE;
    public static final Type SHORT_TYPE;
    public static final Type INT_TYPE;
    public static final Type FLOAT_TYPE;
    public static final Type LONG_TYPE;
    public static final Type DOUBLE_TYPE;
    private final int sort;
    private final char[] buf;
    private final int off;
    private final int len;
    
    private Type(final int a1, final char[] a2, final int a3, final int a4) {
        this.sort = a1;
        this.buf = a2;
        this.off = a3;
        this.len = a4;
    }
    
    public static Type getType(final String a1) {
        /*SL:217*/return getType(a1.toCharArray(), 0);
    }
    
    public static Type getObjectType(final String a1) {
        final char[] v1 = /*EL:228*/a1.toCharArray();
        /*SL:229*/return new Type((v1[0] == '[') ? 9 : 10, v1, 0, v1.length);
    }
    
    public static Type getMethodType(final String a1) {
        /*SL:241*/return getType(a1.toCharArray(), 0);
    }
    
    public static Type getMethodType(final Type a1, final Type... a2) {
        /*SL:257*/return getType(getMethodDescriptor(a1, a2));
    }
    
    public static Type getType(final Class<?> a1) {
        /*SL:268*/if (!a1.isPrimitive()) {
            /*SL:289*/return getType(getDescriptor(a1));
        }
        if (a1 == Integer.TYPE) {
            return Type.INT_TYPE;
        }
        if (a1 == Void.TYPE) {
            return Type.VOID_TYPE;
        }
        if (a1 == Boolean.TYPE) {
            return Type.BOOLEAN_TYPE;
        }
        if (a1 == Byte.TYPE) {
            return Type.BYTE_TYPE;
        }
        if (a1 == Character.TYPE) {
            return Type.CHAR_TYPE;
        }
        if (a1 == Short.TYPE) {
            return Type.SHORT_TYPE;
        }
        if (a1 == Double.TYPE) {
            return Type.DOUBLE_TYPE;
        }
        if (a1 == Float.TYPE) {
            return Type.FLOAT_TYPE;
        }
        return Type.LONG_TYPE;
    }
    
    public static Type getType(final Constructor<?> a1) {
        /*SL:301*/return getType(getConstructorDescriptor(a1));
    }
    
    public static Type getType(final Method a1) {
        /*SL:312*/return getType(getMethodDescriptor(a1));
    }
    
    public static Type[] getArgumentTypes(final String v1) {
        final char[] v2 = /*EL:325*/v1.toCharArray();
        int v3 = /*EL:326*/1;
        int v4 = /*EL:327*/0;
        while (true) {
            final char a1 = /*EL:329*/v2[v3++];
            /*SL:330*/if (a1 == ')') {
                break;
            }
            /*SL:332*/if (a1 == 'L') {
                /*SL:333*/while (v2[v3++] != ';') {}
                /*SL:335*/++v4;
            }
            else {
                /*SL:336*/if (a1 == '[') {
                    continue;
                }
                /*SL:337*/++v4;
            }
        }
        Type[] v5;
        /*SL:343*/for (v5 = new Type[v4], v3 = 1, v4 = 0; v2[v3] != ')'; /*SL:345*/v3 += v5[v4].len + ((v5[v4].sort == 10) ? 2 : 0), /*SL:346*/++v4) {
            v5[v4] = getType(v2, v3);
        }
        /*SL:348*/return v5;
    }
    
    public static Type[] getArgumentTypes(final Method v1) {
        final Class<?>[] v2 = /*EL:361*/v1.getParameterTypes();
        final Type[] v3 = /*EL:362*/new Type[v2.length];
        /*SL:363*/for (int a1 = v2.length - 1; a1 >= 0; --a1) {
            /*SL:364*/v3[a1] = getType(v2[a1]);
        }
        /*SL:366*/return v3;
    }
    
    public static Type getReturnType(final String v1) {
        final char[] v2 = /*EL:379*/v1.toCharArray();
        int v3 = /*EL:380*/1;
        while (true) {
            final char a1 = /*EL:382*/v2[v3++];
            /*SL:383*/if (a1 == ')') {
                break;
            }
            /*SL:385*/if (a1 != 'L') {
                continue;
            }
            /*SL:386*/while (v2[v3++] != ';') {}
        }
        return getType(v2, v3);
    }
    
    public static Type getReturnType(final Method a1) {
        /*SL:402*/return getType(a1.getReturnType());
    }
    
    public static int getArgumentsAndReturnSizes(final String v1) {
        int v2 = /*EL:417*/1;
        int v3 = /*EL:418*/1;
        while (true) {
            char a1 = /*EL:420*/v1.charAt(v3++);
            /*SL:421*/if (a1 == ')') {
                break;
            }
            /*SL:425*/if (a1 == 'L') {
                /*SL:426*/while (v1.charAt(v3++) != ';') {}
                /*SL:428*/++v2;
            }
            else/*SL:429*/ if (a1 == '[') {
                /*SL:430*/while ((a1 = v1.charAt(v3)) == '[') {
                    /*SL:431*/++v3;
                }
                /*SL:433*/if (a1 != 'D' && a1 != 'J') {
                    continue;
                }
                /*SL:434*/--v2;
            }
            else/*SL:436*/ if (a1 == 'D' || a1 == 'J') {
                /*SL:437*/v2 += 2;
            }
            else {
                /*SL:439*/++v2;
            }
        }
        char a1 = v1.charAt(v3);
        return v2 << 2 | ((a1 == 'V') ? 0 : ((a1 == 'D' || a1 == 'J') ? 2 : 1));
    }
    
    private static Type getType(final char[] v1, final int v2) {
        /*SL:457*/switch (v1[v2]) {
            case 'V': {
                /*SL:459*/return Type.VOID_TYPE;
            }
            case 'Z': {
                /*SL:461*/return Type.BOOLEAN_TYPE;
            }
            case 'C': {
                /*SL:463*/return Type.CHAR_TYPE;
            }
            case 'B': {
                /*SL:465*/return Type.BYTE_TYPE;
            }
            case 'S': {
                /*SL:467*/return Type.SHORT_TYPE;
            }
            case 'I': {
                /*SL:469*/return Type.INT_TYPE;
            }
            case 'F': {
                /*SL:471*/return Type.FLOAT_TYPE;
            }
            case 'J': {
                /*SL:473*/return Type.LONG_TYPE;
            }
            case 'D': {
                /*SL:475*/return Type.DOUBLE_TYPE;
            }
            case '[': {
                int a1;
                /*SL:478*/for (a1 = 1; v1[v2 + a1] == '['; /*SL:479*/++a1) {}
                /*SL:481*/if (v1[v2 + a1] == 'L') {
                    /*SL:482*/++a1;
                    /*SL:483*/while (v1[v2 + a1] != ';') {
                        /*SL:484*/++a1;
                    }
                }
                /*SL:487*/return new Type(9, v1, v2, a1 + 1);
            }
            case 'L': {
                int a2;
                /*SL:490*/for (a2 = 1; v1[v2 + a2] != ';'; /*SL:491*/++a2) {}
                /*SL:493*/return new Type(10, v1, v2 + 1, a2 - 1);
            }
            default: {
                /*SL:496*/return new Type(11, v1, v2, v1.length - v2);
            }
        }
    }
    
    public int getSort() {
        /*SL:514*/return this.sort;
    }
    
    public int getDimensions() {
        int v1;
        /*SL:525*/for (v1 = 1; this.buf[this.off + v1] == '['; /*SL:526*/++v1) {}
        /*SL:528*/return v1;
    }
    
    public Type getElementType() {
        /*SL:538*/return getType(this.buf, this.off + this.getDimensions());
    }
    
    public String getClassName() {
        /*SL:548*/switch (this.sort) {
            case 0: {
                /*SL:550*/return "void";
            }
            case 1: {
                /*SL:552*/return "boolean";
            }
            case 2: {
                /*SL:554*/return "char";
            }
            case 3: {
                /*SL:556*/return "byte";
            }
            case 4: {
                /*SL:558*/return "short";
            }
            case 5: {
                /*SL:560*/return "int";
            }
            case 6: {
                /*SL:562*/return "float";
            }
            case 7: {
                /*SL:564*/return "long";
            }
            case 8: {
                /*SL:566*/return "double";
            }
            case 9: {
                final StringBuilder v0 = /*EL:568*/new StringBuilder(this.getElementType().getClassName());
                /*SL:569*/for (int v = this.getDimensions(); v > 0; --v) {
                    /*SL:570*/v0.append("[]");
                }
                /*SL:572*/return v0.toString();
            }
            case 10: {
                /*SL:574*/return new String(this.buf, this.off, this.len).replace('/', '.');
            }
            default: {
                /*SL:576*/return null;
            }
        }
    }
    
    public String getInternalName() {
        /*SL:589*/return new String(this.buf, this.off, this.len);
    }
    
    public Type[] getArgumentTypes() {
        /*SL:599*/return getArgumentTypes(this.getDescriptor());
    }
    
    public Type getReturnType() {
        /*SL:609*/return getReturnType(this.getDescriptor());
    }
    
    public int getArgumentsAndReturnSizes() {
        /*SL:624*/return getArgumentsAndReturnSizes(this.getDescriptor());
    }
    
    public String getDescriptor() {
        final StringBuilder v1 = /*EL:637*/new StringBuilder();
        /*SL:638*/this.getDescriptor(v1);
        /*SL:639*/return v1.toString();
    }
    
    public static String getMethodDescriptor(final Type a2, final Type... v1) {
        final StringBuilder v2 = /*EL:655*/new StringBuilder();
        /*SL:656*/v2.append('(');
        /*SL:657*/for (int a3 = 0; a3 < v1.length; ++a3) {
            /*SL:658*/v1[a3].getDescriptor(v2);
        }
        /*SL:660*/v2.append(')');
        /*SL:661*/a2.getDescriptor(v2);
        /*SL:662*/return v2.toString();
    }
    
    private void getDescriptor(final StringBuilder a1) {
        /*SL:673*/if (this.buf == null) {
            /*SL:676*/a1.append((char)((this.off & 0xFF000000) >>> 24));
        }
        else/*SL:677*/ if (this.sort == 10) {
            /*SL:678*/a1.append('L');
            /*SL:679*/a1.append(this.buf, this.off, this.len);
            /*SL:680*/a1.append(';');
        }
        else {
            /*SL:682*/a1.append(this.buf, this.off, this.len);
        }
    }
    
    public static String getInternalName(final Class<?> a1) {
        /*SL:701*/return a1.getName().replace('.', '/');
    }
    
    public static String getDescriptor(final Class<?> a1) {
        final StringBuilder v1 = /*EL:712*/new StringBuilder();
        getDescriptor(/*EL:713*/v1, a1);
        /*SL:714*/return v1.toString();
    }
    
    public static String getConstructorDescriptor(final Constructor<?> v1) {
        final Class<?>[] v2 = /*EL:725*/v1.getParameterTypes();
        final StringBuilder v3 = /*EL:726*/new StringBuilder();
        /*SL:727*/v3.append('(');
        /*SL:728*/for (int a1 = 0; a1 < v2.length; ++a1) {
            getDescriptor(/*EL:729*/v3, v2[a1]);
        }
        /*SL:731*/return v3.append(")V").toString();
    }
    
    public static String getMethodDescriptor(final Method v1) {
        final Class<?>[] v2 = /*EL:742*/v1.getParameterTypes();
        final StringBuilder v3 = /*EL:743*/new StringBuilder();
        /*SL:744*/v3.append('(');
        /*SL:745*/for (int a1 = 0; a1 < v2.length; ++a1) {
            getDescriptor(/*EL:746*/v3, v2[a1]);
        }
        /*SL:748*/v3.append(')');
        getDescriptor(/*EL:749*/v3, v1.getReturnType());
        /*SL:750*/return v3.toString();
    }
    
    private static void getDescriptor(final StringBuilder v-2, final Class<?> v-1) {
        Class<?> v0;
        /*SL:764*/for (v0 = v-1; !v0.isPrimitive(); /*SL:789*/v0 = v0.getComponentType()) {
            if (!v0.isArray()) {
                /*SL:791*/v-2.append('L');
                final String v = /*EL:792*/v0.getName();
                /*SL:794*/for (int v2 = v.length(), v3 = 0; v3 < v2; ++v3) {
                    final char v4 = /*EL:795*/v.charAt(v3);
                    /*SL:796*/v-2.append((v4 == '.') ? '/' : v4);
                }
                /*SL:798*/v-2.append(';');
                /*SL:799*/return;
            }
            v-2.append('[');
        }
        char v5 = '\0';
        if (v0 == Integer.TYPE) {
            final char a1 = 'I';
        }
        else if (v0 == Void.TYPE) {
            final char a2 = 'V';
        }
        else if (v0 == Boolean.TYPE) {
            v5 = 'Z';
        }
        else if (v0 == Byte.TYPE) {
            v5 = 'B';
        }
        else if (v0 == Character.TYPE) {
            v5 = 'C';
        }
        else if (v0 == Short.TYPE) {
            v5 = 'S';
        }
        else if (v0 == Double.TYPE) {
            v5 = 'D';
        }
        else if (v0 == Float.TYPE) {
            v5 = 'F';
        }
        else {
            v5 = 'J';
        }
        v-2.append(v5);
    }
    
    public int getSize() {
        /*SL:817*/return (this.buf == null) ? (this.off & 0xFF) : 1;
    }
    
    public int getOpcode(final int a1) {
        /*SL:833*/if (a1 == 46 || a1 == 79) {
            /*SL:836*/return a1 + ((this.buf == null) ? ((this.off & 0xFF00) >> 8) : 4);
        }
        /*SL:840*/return a1 + ((this.buf == null) ? ((this.off & 0xFF0000) >> 16) : 4);
    }
    
    public boolean equals(final Object v-2) {
        /*SL:857*/if (this == v-2) {
            /*SL:858*/return true;
        }
        /*SL:860*/if (!(v-2 instanceof Type)) {
            /*SL:861*/return false;
        }
        final Type type = /*EL:863*/(Type)v-2;
        /*SL:864*/if (this.sort != type.sort) {
            /*SL:865*/return false;
        }
        /*SL:867*/if (this.sort >= 9) {
            /*SL:868*/if (this.len != type.len) {
                /*SL:869*/return false;
            }
            /*SL:871*/for (int a1 = this.off, v1 = type.off, v2 = a1 + this.len; a1 < v2; ++a1, ++v1) {
                /*SL:872*/if (this.buf[a1] != type.buf[v1]) {
                    /*SL:873*/return false;
                }
            }
        }
        /*SL:877*/return true;
    }
    
    public int hashCode() {
        int v0 = /*EL:887*/13 * this.sort;
        /*SL:888*/if (this.sort >= 9) {
            /*SL:889*/for (int v = this.off, v2 = v + this.len; v < v2; ++v) {
                /*SL:890*/v0 = 17 * (v0 + this.buf[v]);
            }
        }
        /*SL:893*/return v0;
    }
    
    public String toString() {
        /*SL:903*/return this.getDescriptor();
    }
    
    static {
        VOID_TYPE = new Type(0, null, 1443168256, 1);
        BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
        CHAR_TYPE = new Type(2, null, 1124075009, 1);
        BYTE_TYPE = new Type(3, null, 1107297537, 1);
        SHORT_TYPE = new Type(4, null, 1392510721, 1);
        INT_TYPE = new Type(5, null, 1224736769, 1);
        FLOAT_TYPE = new Type(6, null, 1174536705, 1);
        LONG_TYPE = new Type(7, null, 1241579778, 1);
        DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    }
}
