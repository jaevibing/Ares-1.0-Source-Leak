package javassist.bytecode;

import javassist.ClassPool;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import java.util.Map;
import javassist.CtClass;

public class Descriptor
{
    public static String toJvmName(final String a1) {
        /*SL:39*/return a1.replace('.', '/');
    }
    
    public static String toJavaName(final String a1) {
        /*SL:52*/return a1.replace('/', '.');
    }
    
    public static String toJvmName(final CtClass a1) {
        /*SL:60*/if (a1.isArray()) {
            /*SL:61*/return of(a1);
        }
        /*SL:63*/return toJvmName(a1.getName());
    }
    
    public static String toClassName(final String v-3) {
        int n = /*EL:72*/0;
        int n2;
        char v0;
        /*SL:75*/for (n2 = 0, v0 = v-3.charAt(0); v0 == '['; /*SL:77*/v0 = v-3.charAt(++n2)) {
            ++n;
        }
        String v;
        /*SL:81*/if (v0 == 'L') {
            final int a1 = /*EL:82*/v-3.indexOf(59, n2++);
            /*SL:83*/v = v-3.substring(n2, a1).replace('/', '.');
            /*SL:84*/n2 = a1;
        }
        else/*SL:86*/ if (v0 == 'V') {
            /*SL:87*/v = "void";
        }
        else/*SL:88*/ if (v0 == 'I') {
            /*SL:89*/v = "int";
        }
        else/*SL:90*/ if (v0 == 'B') {
            /*SL:91*/v = "byte";
        }
        else/*SL:92*/ if (v0 == 'J') {
            /*SL:93*/v = "long";
        }
        else/*SL:94*/ if (v0 == 'D') {
            /*SL:95*/v = "double";
        }
        else/*SL:96*/ if (v0 == 'F') {
            /*SL:97*/v = "float";
        }
        else/*SL:98*/ if (v0 == 'C') {
            /*SL:99*/v = "char";
        }
        else/*SL:100*/ if (v0 == 'S') {
            /*SL:101*/v = "short";
        }
        else {
            /*SL:102*/if (v0 != 'Z') {
                /*SL:105*/throw new RuntimeException("bad descriptor: " + v-3);
            }
            v = "boolean";
        }
        /*SL:107*/if (n2 + 1 != v-3.length()) {
            /*SL:108*/throw new RuntimeException("multiple descriptors?: " + v-3);
        }
        /*SL:110*/if (n == 0) {
            /*SL:111*/return v;
        }
        final StringBuffer v2 = /*EL:113*/new StringBuffer(v);
        /*SL:116*/do {
            v2.append("[]");
        } while (--n > 0);
        /*SL:118*/return v2.toString();
    }
    
    public static String of(final String a1) {
        /*SL:126*/if (a1.equals("void")) {
            /*SL:127*/return "V";
        }
        /*SL:128*/if (a1.equals("int")) {
            /*SL:129*/return "I";
        }
        /*SL:130*/if (a1.equals("byte")) {
            /*SL:131*/return "B";
        }
        /*SL:132*/if (a1.equals("long")) {
            /*SL:133*/return "J";
        }
        /*SL:134*/if (a1.equals("double")) {
            /*SL:135*/return "D";
        }
        /*SL:136*/if (a1.equals("float")) {
            /*SL:137*/return "F";
        }
        /*SL:138*/if (a1.equals("char")) {
            /*SL:139*/return "C";
        }
        /*SL:140*/if (a1.equals("short")) {
            /*SL:141*/return "S";
        }
        /*SL:142*/if (a1.equals("boolean")) {
            /*SL:143*/return "Z";
        }
        /*SL:145*/return "L" + toJvmName(a1) + ";";
    }
    
    public static String rename(final String a3, final String v1, final String v2) {
        /*SL:159*/if (a3.indexOf(v1) < 0) {
            /*SL:160*/return a3;
        }
        final StringBuffer v3 = /*EL:162*/new StringBuffer();
        int v4 = /*EL:163*/0;
        int v5 = /*EL:164*/0;
        while (true) {
            final int a4 = /*EL:166*/a3.indexOf(76, v5);
            /*SL:167*/if (a4 < 0) {
                /*SL:168*/break;
            }
            /*SL:169*/if (a3.startsWith(v1, a4 + 1) && a3.charAt(a4 + v1.length() + /*EL:170*/1) == ';') {
                /*SL:171*/v3.append(a3.substring(v4, a4));
                /*SL:172*/v3.append('L');
                /*SL:173*/v3.append(v2);
                /*SL:174*/v3.append(';');
                /*SL:175*/v5 = (v4 = a4 + v1.length() + 2);
            }
            else {
                /*SL:178*/v5 = a3.indexOf(59, a4) + 1;
                /*SL:179*/if (v5 < 1) {
                    /*SL:180*/break;
                }
                continue;
            }
        }
        /*SL:184*/if (v4 == 0) {
            /*SL:185*/return a3;
        }
        final int a5 = /*EL:187*/a3.length();
        /*SL:188*/if (v4 < a5) {
            /*SL:189*/v3.append(a3.substring(v4, a5));
        }
        /*SL:191*/return v3.toString();
    }
    
    public static String rename(final String v-6, final Map v-5) {
        /*SL:204*/if (v-5 == null) {
            /*SL:205*/return v-6;
        }
        final StringBuffer sb = /*EL:207*/new StringBuffer();
        int n = /*EL:208*/0;
        int n2 = /*EL:209*/0;
        while (true) {
            final int a1 = /*EL:211*/v-6.indexOf(76, n2);
            /*SL:212*/if (a1 < 0) {
                /*SL:213*/break;
            }
            final int a2 = /*EL:215*/v-6.indexOf(59, a1);
            /*SL:216*/if (a2 < 0) {
                /*SL:217*/break;
            }
            /*SL:219*/n2 = a2 + 1;
            final String v1 = /*EL:220*/v-6.substring(a1 + 1, a2);
            final String v2 = /*EL:221*/v-5.get(v1);
            /*SL:222*/if (v2 == null) {
                continue;
            }
            /*SL:223*/sb.append(v-6.substring(n, a1));
            /*SL:224*/sb.append('L');
            /*SL:225*/sb.append(v2);
            /*SL:226*/sb.append(';');
            /*SL:227*/n = n2;
        }
        /*SL:231*/if (n == 0) {
            /*SL:232*/return v-6;
        }
        final int length = /*EL:234*/v-6.length();
        /*SL:235*/if (n < length) {
            /*SL:236*/sb.append(v-6.substring(n, length));
        }
        /*SL:238*/return sb.toString();
    }
    
    public static String of(final CtClass a1) {
        final StringBuffer v1 = /*EL:246*/new StringBuffer();
        toDescriptor(/*EL:247*/v1, a1);
        /*SL:248*/return v1.toString();
    }
    
    private static void toDescriptor(final StringBuffer v-1, final CtClass v0) {
        /*SL:252*/if (v0.isArray()) {
            /*SL:253*/v-1.append('[');
            try {
                toDescriptor(/*EL:255*/v-1, v0.getComponentType());
            }
            catch (NotFoundException a2) {
                /*SL:258*/v-1.append('L');
                final String a1 = /*EL:259*/v0.getName();
                /*SL:260*/v-1.append(toJvmName(a1.substring(0, a1.length() - 2)));
                /*SL:261*/v-1.append(';');
            }
        }
        else/*SL:264*/ if (v0.isPrimitive()) {
            final CtPrimitiveType v = /*EL:265*/(CtPrimitiveType)v0;
            /*SL:266*/v-1.append(v.getDescriptor());
        }
        else {
            /*SL:269*/v-1.append('L');
            /*SL:270*/v-1.append(v0.getName().replace('.', '/'));
            /*SL:271*/v-1.append(';');
        }
    }
    
    public static String ofConstructor(final CtClass[] a1) {
        /*SL:282*/return ofMethod(CtClass.voidType, a1);
    }
    
    public static String ofMethod(final CtClass v1, final CtClass[] v2) {
        final StringBuffer v3 = /*EL:293*/new StringBuffer();
        /*SL:294*/v3.append('(');
        /*SL:295*/if (v2 != null) {
            int a2;
            int a2;
            /*SL:297*/for (a2 = v2.length, a2 = 0; a2 < a2; ++a2) {
                toDescriptor(/*EL:298*/v3, v2[a2]);
            }
        }
        /*SL:301*/v3.append(')');
        /*SL:302*/if (v1 != null) {
            toDescriptor(/*EL:303*/v3, v1);
        }
        /*SL:305*/return v3.toString();
    }
    
    public static String ofParameters(final CtClass[] a1) {
        /*SL:316*/return ofMethod(null, a1);
    }
    
    public static String appendParameter(final String a2, final String v1) {
        final int v2 = /*EL:329*/v1.indexOf(41);
        /*SL:330*/if (v2 < 0) {
            /*SL:331*/return v1;
        }
        final StringBuffer a3 = /*EL:333*/new StringBuffer();
        /*SL:334*/a3.append(v1.substring(0, v2));
        /*SL:335*/a3.append('L');
        /*SL:336*/a3.append(a2.replace('.', '/'));
        /*SL:337*/a3.append(';');
        /*SL:338*/a3.append(v1.substring(v2));
        /*SL:339*/return a3.toString();
    }
    
    public static String insertParameter(final String a1, final String a2) {
        /*SL:354*/if (a2.charAt(0) != '(') {
            /*SL:355*/return a2;
        }
        /*SL:358*/return "(L" + a1.replace('.', '/') + ';' + a2.substring(1);
    }
    
    public static String appendParameter(final CtClass a2, final String v1) {
        final int v2 = /*EL:370*/v1.indexOf(41);
        /*SL:371*/if (v2 < 0) {
            /*SL:372*/return v1;
        }
        final StringBuffer a3 = /*EL:374*/new StringBuffer();
        /*SL:375*/a3.append(v1.substring(0, v2));
        toDescriptor(/*EL:376*/a3, a2);
        /*SL:377*/a3.append(v1.substring(v2));
        /*SL:378*/return a3.toString();
    }
    
    public static String insertParameter(final CtClass a1, final String a2) {
        /*SL:392*/if (a2.charAt(0) != '(') {
            /*SL:393*/return a2;
        }
        /*SL:395*/return "(" + of(a1) + a2.substring(1);
    }
    
    public static String changeReturnType(final String a2, final String v1) {
        final int v2 = /*EL:407*/v1.indexOf(41);
        /*SL:408*/if (v2 < 0) {
            /*SL:409*/return v1;
        }
        final StringBuffer a3 = /*EL:411*/new StringBuffer();
        /*SL:412*/a3.append(v1.substring(0, v2 + 1));
        /*SL:413*/a3.append('L');
        /*SL:414*/a3.append(a2.replace('.', '/'));
        /*SL:415*/a3.append(';');
        /*SL:416*/return a3.toString();
    }
    
    public static CtClass[] getParameterTypes(final String v-3, final ClassPool v-2) throws NotFoundException {
        /*SL:431*/if (v-3.charAt(0) != '(') {
            /*SL:432*/return null;
        }
        final int a1 = numOfParameters(/*EL:434*/v-3);
        final CtClass[] a2 = /*EL:435*/new CtClass[a1];
        int v1 = /*EL:436*/0;
        int v2 = /*EL:437*/1;
        /*SL:440*/do {
            v2 = toCtClass(v-2, v-3, v2, a2, v1++);
        } while (v2 > 0);
        /*SL:441*/return a2;
    }
    
    public static boolean eqParamTypes(final String v1, final String v2) {
        /*SL:451*/if (v1.charAt(0) != '(') {
            /*SL:452*/return false;
        }
        int a2 = /*EL:454*/0;
        while (true) {
            /*SL:455*/a2 = v1.charAt(a2);
            /*SL:456*/if (a2 != v2.charAt(a2)) {
                /*SL:457*/return false;
            }
            /*SL:459*/if (a2 == ')') {
                /*SL:460*/return true;
            }
            ++a2;
        }
    }
    
    public static String getParamDescriptor(final String a1) {
        /*SL:470*/return a1.substring(0, a1.indexOf(41) + 1);
    }
    
    public static CtClass getReturnType(final String a2, final ClassPool v1) throws NotFoundException {
        final int v2 = /*EL:484*/a2.indexOf(41);
        /*SL:485*/if (v2 < 0) {
            /*SL:486*/return null;
        }
        final CtClass[] a3 = /*EL:488*/{ null };
        toCtClass(/*EL:489*/v1, a2, v2 + 1, a3, 0);
        /*SL:490*/return a3[0];
    }
    
    public static int numOfParameters(final String v1) {
        int v2 = /*EL:501*/0;
        int v3 = /*EL:502*/1;
        while (true) {
            char a1 = /*EL:504*/v1.charAt(v3);
            /*SL:505*/if (a1 == ')') {
                /*SL:522*/return v2;
            }
            while (a1 == '[') {
                a1 = v1.charAt(++v3);
            }
            if (a1 == 'L') {
                v3 = v1.indexOf(59, v3) + 1;
                if (v3 <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            }
            else {
                ++v3;
            }
            ++v2;
        }
    }
    
    public static CtClass toCtClass(final String a1, final ClassPool a2) throws NotFoundException {
        final CtClass[] v1 = /*EL:541*/{ null };
        final int v2 = toCtClass(/*EL:542*/a2, a1, 0, v1, 0);
        /*SL:543*/if (v2 >= 0) {
            /*SL:544*/return v1[0];
        }
        /*SL:548*/return a2.get(a1.replace('/', '.'));
    }
    
    private static int toCtClass(final ClassPool a5, final String v1, int v2, final CtClass[] v3, final int v4) throws NotFoundException {
        int v5 = /*EL:559*/0;
        char v6;
        /*SL:561*/for (v6 = v1.charAt(v2); v6 == '['; /*SL:563*/v6 = v1.charAt(++v2)) {
            ++v5;
        }
        final int v7;
        String v8 = null;
        /*SL:566*/if (v6 == 'L') {
            int a6 = /*EL:567*/v1.indexOf(59, ++v2);
            final String a7 = /*EL:568*/v1.substring(v2, a6++).replace('/', '.');
        }
        else {
            final CtClass a8 = toPrimitiveClass(/*EL:571*/v6);
            /*SL:572*/if (a8 == null) {
                /*SL:573*/return -1;
            }
            /*SL:575*/v7 = v2 + 1;
            /*SL:576*/if (v5 == 0) {
                /*SL:577*/v3[v4] = a8;
                /*SL:578*/return v7;
            }
            /*SL:581*/v8 = a8.getName();
        }
        /*SL:584*/if (v5 > 0) {
            final StringBuffer a9 = /*EL:585*/new StringBuffer(v8);
            /*SL:586*/while (v5-- > 0) {
                /*SL:587*/a9.append("[]");
            }
            /*SL:589*/v8 = a9.toString();
        }
        /*SL:592*/v3[v4] = a5.get(v8);
        /*SL:593*/return v7;
    }
    
    static CtClass toPrimitiveClass(final char a1) {
        CtClass v1 = /*EL:597*/null;
        /*SL:598*/switch (a1) {
            case 'Z': {
                /*SL:600*/v1 = CtClass.booleanType;
                /*SL:601*/break;
            }
            case 'C': {
                /*SL:603*/v1 = CtClass.charType;
                /*SL:604*/break;
            }
            case 'B': {
                /*SL:606*/v1 = CtClass.byteType;
                /*SL:607*/break;
            }
            case 'S': {
                /*SL:609*/v1 = CtClass.shortType;
                /*SL:610*/break;
            }
            case 'I': {
                /*SL:612*/v1 = CtClass.intType;
                /*SL:613*/break;
            }
            case 'J': {
                /*SL:615*/v1 = CtClass.longType;
                /*SL:616*/break;
            }
            case 'F': {
                /*SL:618*/v1 = CtClass.floatType;
                /*SL:619*/break;
            }
            case 'D': {
                /*SL:621*/v1 = CtClass.doubleType;
                /*SL:622*/break;
            }
            case 'V': {
                /*SL:624*/v1 = CtClass.voidType;
                break;
            }
        }
        /*SL:628*/return v1;
    }
    
    public static int arrayDimension(final String a1) {
        int v1;
        /*SL:641*/for (v1 = 0; a1.charAt(v1) == '['; /*SL:642*/++v1) {}
        /*SL:644*/return v1;
    }
    
    public static String toArrayComponent(final String a1, final int a2) {
        /*SL:657*/return a1.substring(a2);
    }
    
    public static int dataSize(final String a1) {
        /*SL:672*/return dataSize(a1, true);
    }
    
    public static int paramSize(final String a1) {
        /*SL:685*/return -dataSize(a1, false);
    }
    
    private static int dataSize(final String v1, final boolean v2) {
        int v3 = /*EL:689*/0;
        char v4 = /*EL:690*/v1.charAt(0);
        /*SL:691*/if (v4 == '(') {
            int a2 = /*EL:692*/1;
            while (true) {
                /*SL:694*/v4 = v1.charAt(a2);
                /*SL:695*/if (v4 == ')') {
                    /*SL:696*/v4 = v1.charAt(a2 + 1);
                    /*SL:697*/break;
                }
                /*SL:700*/a2 = false;
                /*SL:701*/while (v4 == '[') {
                    /*SL:702*/a2 = true;
                    /*SL:703*/v4 = v1.charAt(++a2);
                }
                /*SL:706*/if (v4 == 'L') {
                    /*SL:707*/a2 = v1.indexOf(59, a2) + 1;
                    /*SL:708*/if (a2 <= 0) {
                        /*SL:709*/throw new IndexOutOfBoundsException("bad descriptor");
                    }
                }
                else {
                    /*SL:712*/++a2;
                }
                /*SL:714*/if (!a2 && (v4 == 'J' || v4 == 'D')) {
                    /*SL:715*/v3 -= 2;
                }
                else {
                    /*SL:717*/--v3;
                }
            }
        }
        /*SL:721*/if (v2) {
            /*SL:722*/if (v4 == 'J' || v4 == 'D') {
                /*SL:723*/v3 += 2;
            }
            else/*SL:724*/ if (v4 != 'V') {
                /*SL:725*/++v3;
            }
        }
        /*SL:727*/return v3;
    }
    
    public static String toString(final String a1) {
        /*SL:738*/return PrettyPrinter.toString(a1);
    }
    
    static class PrettyPrinter
    {
        static String toString(final String v1) {
            final StringBuffer v2 = /*EL:743*/new StringBuffer();
            /*SL:744*/if (v1.charAt(0) == '(') {
                int a1 = /*EL:745*/1;
                /*SL:746*/v2.append('(');
                /*SL:747*/while (v1.charAt(a1) != ')') {
                    /*SL:748*/if (a1 > 1) {
                        /*SL:749*/v2.append(',');
                    }
                    /*SL:751*/a1 = readType(v2, a1, v1);
                }
                /*SL:754*/v2.append(')');
            }
            else {
                readType(/*EL:757*/v2, 0, v1);
            }
            /*SL:759*/return v2.toString();
        }
        
        static int readType(final StringBuffer a2, int a3, final String v1) {
            char v2 = /*EL:763*/v1.charAt(a3);
            int v3 = /*EL:764*/0;
            /*SL:765*/while (v2 == '[') {
                /*SL:766*/++v3;
                /*SL:767*/v2 = v1.charAt(++a3);
            }
            /*SL:770*/if (v2 == 'L') {
                while (true) {
                    /*SL:772*/v2 = v1.charAt(++a3);
                    /*SL:773*/if (v2 == ';') {
                        break;
                    }
                    /*SL:776*/if (v2 == '/') {
                        /*SL:777*/v2 = '.';
                    }
                    /*SL:779*/a2.append(v2);
                }
            }
            else {
                final CtClass a4 = /*EL:782*/Descriptor.toPrimitiveClass(v2);
                /*SL:783*/a2.append(a4.getName());
            }
            /*SL:786*/while (v3-- > 0) {
                /*SL:787*/a2.append("[]");
            }
            /*SL:789*/return a3 + 1;
        }
    }
    
    public static class Iterator
    {
        private String desc;
        private int index;
        private int curPos;
        private boolean param;
        
        public Iterator(final String a1) {
            this.desc = a1;
            final boolean b = false;
            this.curPos = (b ? 1 : 0);
            this.index = (b ? 1 : 0);
            this.param = false;
        }
        
        public boolean hasNext() {
            /*SL:816*/return this.index < this.desc.length();
        }
        
        public boolean isParameter() {
            /*SL:822*/return this.param;
        }
        
        public char currentChar() {
            /*SL:827*/return this.desc.charAt(this.curPos);
        }
        
        public boolean is2byte() {
            final char v1 = /*EL:833*/this.currentChar();
            /*SL:834*/return v1 == 'D' || v1 == 'J';
        }
        
        public int next() {
            int v1 = /*EL:842*/this.index;
            char v2 = /*EL:843*/this.desc.charAt(v1);
            /*SL:844*/if (v2 == '(') {
                /*SL:845*/++this.index;
                /*SL:846*/v2 = this.desc.charAt(++v1);
                /*SL:847*/this.param = true;
            }
            /*SL:850*/if (v2 == ')') {
                /*SL:851*/++this.index;
                /*SL:852*/v2 = this.desc.charAt(++v1);
                /*SL:853*/this.param = false;
            }
            /*SL:856*/while (v2 == '[') {
                /*SL:857*/v2 = this.desc.charAt(++v1);
            }
            /*SL:859*/if (v2 == 'L') {
                /*SL:860*/v1 = this.desc.indexOf(59, v1) + 1;
                /*SL:861*/if (v1 <= 0) {
                    /*SL:862*/throw new IndexOutOfBoundsException("bad descriptor");
                }
            }
            else {
                /*SL:865*/++v1;
            }
            /*SL:867*/this.curPos = this.index;
            /*SL:868*/this.index = v1;
            /*SL:869*/return this.curPos;
        }
    }
}
