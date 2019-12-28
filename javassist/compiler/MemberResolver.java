package javassist.compiler;

import javassist.compiler.ast.Keyword;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.ASTList;
import javassist.CtField;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Symbol;
import javassist.bytecode.Descriptor;
import java.util.List;
import javassist.bytecode.ClassFile;
import javassist.NotFoundException;
import javassist.Modifier;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import java.util.Hashtable;
import java.util.WeakHashMap;
import javassist.ClassPool;

public class MemberResolver implements TokenId
{
    private ClassPool classPool;
    private static final int YES = 0;
    private static final int NO = -1;
    private static final String INVALID = "<invalid>";
    private static WeakHashMap invalidNamesMap;
    private Hashtable invalidNames;
    
    public MemberResolver(final ClassPool a1) {
        this.invalidNames = null;
        this.classPool = a1;
    }
    
    public ClassPool getClassPool() {
        /*SL:38*/return this.classPool;
    }
    
    private static void fatal() throws CompileError {
        /*SL:41*/throw new CompileError("fatal");
    }
    
    public Method lookupMethod(final CtClass a4, final CtClass a5, final MethodInfo a6, final String a7, final int[] v1, final int[] v2, final String[] v3) throws CompileError {
        Method v4 = /*EL:70*/null;
        /*SL:72*/if (a6 != null && a4 == a5 && /*EL:73*/a6.getName().equals(a7)) {
            final int a8 = /*EL:74*/this.compareSignature(a6.getDescriptor(), v1, v2, v3);
            /*SL:76*/if (a8 != -1) {
                final Method a9 = /*EL:77*/new Method(a4, a6, a8);
                /*SL:78*/if (a8 == 0) {
                    /*SL:79*/return a9;
                }
                /*SL:81*/v4 = a9;
            }
        }
        final Method v5 = /*EL:85*/this.lookupMethod(a4, a7, v1, v2, v3, v4 != null);
        /*SL:87*/if (v5 != null) {
            /*SL:88*/return v5;
        }
        /*SL:90*/return v4;
    }
    
    private Method lookupMethod(final CtClass v-10, final String v-9, final int[] v-8, final int[] v-7, final String[] v-6, final boolean v-5) throws CompileError {
        Method method = /*EL:98*/null;
        final ClassFile classFile2 = /*EL:99*/v-10.getClassFile2();
        /*SL:102*/if (classFile2 != null) {
            List a5 = /*EL:103*/classFile2.getMethods();
            /*SL:105*/for (int a2 = a5.size(), a3 = 0; a3 < a2; ++a3) {
                final MethodInfo a4 = /*EL:106*/a5.get(a3);
                /*SL:107*/if (a4.getName().equals(v-9) && (a4.getAccessFlags() & /*EL:108*/0x40) == 0x0) {
                    /*SL:109*/a5 = this.compareSignature(a4.getDescriptor(), v-8, v-7, v-6);
                    /*SL:111*/if (a5 != -1) {
                        final Method a6 = /*EL:112*/new Method(v-10, a4, a5);
                        /*SL:113*/if (a5 == 0) {
                            /*SL:114*/return a6;
                        }
                        /*SL:115*/if (method == null || method.notmatch > a5) {
                            /*SL:116*/method = a6;
                        }
                    }
                }
            }
        }
        /*SL:122*/if (v-5) {
            /*SL:123*/method = null;
        }
        else/*SL:125*/ if (method != null) {
            /*SL:126*/return method;
        }
        final int modifiers = /*EL:128*/v-10.getModifiers();
        final boolean interface1 = /*EL:129*/Modifier.isInterface(modifiers);
        try {
            /*SL:132*/if (!interface1) {
                final CtClass v0 = /*EL:133*/v-10.getSuperclass();
                /*SL:134*/if (v0 != null) {
                    final Method v = /*EL:135*/this.lookupMethod(v0, v-9, v-8, v-7, v-6, v-5);
                    /*SL:137*/if (v != null) {
                        /*SL:138*/return v;
                    }
                }
            }
        }
        catch (NotFoundException ex) {}
        try {
            final CtClass[] v2 = /*EL:145*/v-10.getInterfaces();
            /*SL:147*/for (int v3 = v2.length, v4 = 0; v4 < v3; ++v4) {
                final Method v5 = /*EL:148*/this.lookupMethod(v2[v4], v-9, v-8, v-7, v-6, v-5);
                /*SL:151*/if (v5 != null) {
                    /*SL:152*/return v5;
                }
            }
            /*SL:155*/if (interface1) {
                final CtClass v6 = /*EL:157*/v-10.getSuperclass();
                /*SL:158*/if (v6 != null) {
                    final Method v5 = /*EL:159*/this.lookupMethod(v6, v-9, v-8, v-7, v-6, v-5);
                    /*SL:161*/if (v5 != null) {
                        /*SL:162*/return v5;
                    }
                }
            }
        }
        catch (NotFoundException ex2) {}
        /*SL:168*/return method;
    }
    
    private int compareSignature(final String v-10, final int[] v-9, final int[] v-8, final String[] v-7) throws CompileError {
        int n = /*EL:190*/0;
        int i = /*EL:191*/1;
        final int length = /*EL:192*/v-9.length;
        /*SL:193*/if (length != Descriptor.numOfParameters(v-10)) {
            /*SL:194*/return -1;
        }
        final int length2 = /*EL:196*/v-10.length();
        int n2 = /*EL:197*/0;
        while (i < length2) {
            char a5 = /*EL:198*/v-10.charAt(i++);
            /*SL:199*/if (a5 == ')') {
                /*SL:200*/return (n2 == length) ? n : -1;
            }
            /*SL:201*/if (n2 >= length) {
                /*SL:202*/return -1;
            }
            int v0 = /*EL:204*/0;
            /*SL:205*/while (a5 == '[') {
                /*SL:206*/++v0;
                /*SL:207*/a5 = v-10.charAt(i++);
            }
            /*SL:210*/if (v-9[n2] == 412) {
                /*SL:211*/if (v0 == 0 && a5 != 'L') {
                    /*SL:212*/return -1;
                }
                /*SL:214*/if (a5 == 'L') {
                    /*SL:215*/i = v-10.indexOf(59, i) + 1;
                }
            }
            else/*SL:217*/ if (v-8[n2] != v0) {
                /*SL:218*/if (v0 != 0 || a5 != 'L' || !v-10.startsWith("java/lang/Object;", i)) {
                    /*SL:220*/return -1;
                }
                /*SL:223*/i = v-10.indexOf(59, i) + 1;
                /*SL:224*/++n;
                /*SL:225*/if (i <= 0) {
                    /*SL:226*/return -1;
                }
            }
            else/*SL:228*/ if (a5 == 'L') {
                int a3 = /*EL:229*/v-10.indexOf(59, i);
                /*SL:230*/if (a3 < 0 || v-9[n2] != 307) {
                    /*SL:231*/return -1;
                }
                final String a2 = /*EL:233*/v-10.substring(i, a3);
                /*SL:234*/if (!a2.equals(v-7[n2])) {
                    /*SL:235*/a3 = this.lookupClassByJvmName(v-7[n2]);
                    try {
                        /*SL:237*/if (!a3.subtypeOf(this.lookupClassByJvmName(a2))) {
                            /*SL:240*/return -1;
                        }
                        ++n;
                    }
                    catch (NotFoundException a4) {
                        /*SL:243*/++n;
                    }
                }
                /*SL:247*/i = a3 + 1;
            }
            else {
                final int v = descToType(/*EL:250*/a5);
                final int v2 = /*EL:251*/v-9[n2];
                /*SL:252*/if (v != v2) {
                    /*SL:253*/if (v != 324 || (v2 != 334 && v2 != 303 && v2 != 306)) {
                        /*SL:257*/return -1;
                    }
                    ++n;
                }
            }
            ++n2;
        }
        /*SL:261*/return -1;
    }
    
    public CtField lookupFieldByJvmName2(String v1, final Symbol v2, final ASTree v3) throws NoFieldException {
        final String v4 = /*EL:273*/v2.get();
        CtClass v5 = /*EL:274*/null;
        try {
            /*SL:276*/v5 = this.lookupClass(jvmToJavaName(v1), true);
        }
        catch (CompileError a1) {
            /*SL:280*/throw new NoFieldException(v1 + "/" + v4, v3);
        }
        try {
            /*SL:284*/return v5.getField(v4);
        }
        catch (NotFoundException a2) {
            /*SL:288*/v1 = javaToJvmName(v5.getName());
            /*SL:289*/throw new NoFieldException(v1 + "$" + v4, v3);
        }
    }
    
    public CtField lookupFieldByJvmName(final String a1, final Symbol a2) throws CompileError {
        /*SL:299*/return this.lookupField(jvmToJavaName(a1), a2);
    }
    
    public CtField lookupField(final String a1, final Symbol a2) throws CompileError {
        final CtClass v1 = /*EL:308*/this.lookupClass(a1, false);
        try {
            /*SL:310*/return v1.getField(a2.get());
        }
        catch (NotFoundException ex) {
            /*SL:313*/throw new CompileError("no such field: " + a2.get());
        }
    }
    
    public CtClass lookupClassByName(final ASTList a1) throws CompileError {
        /*SL:317*/return this.lookupClass(Declarator.astToClassName(a1, '.'), false);
    }
    
    public CtClass lookupClassByJvmName(final String a1) throws CompileError {
        /*SL:321*/return this.lookupClass(jvmToJavaName(a1), false);
    }
    
    public CtClass lookupClass(final Declarator a1) throws CompileError {
        /*SL:325*/return this.lookupClass(a1.getType(), a1.getArrayDim(), a1.getClassName());
    }
    
    public CtClass lookupClass(final int a3, int v1, final String v2) throws CompileError {
        String v3 = /*EL:335*/"";
        /*SL:337*/if (a3 == 307) {
            final CtClass a4 = /*EL:338*/this.lookupClassByJvmName(v2);
            /*SL:339*/if (v1 <= 0) {
                /*SL:342*/return a4;
            }
            v3 = a4.getName();
        }
        else {
            /*SL:345*/v3 = getTypeName(a3);
        }
        /*SL:347*/while (v1-- > 0) {
            /*SL:348*/v3 += "[]";
        }
        /*SL:350*/return this.lookupClass(v3, false);
    }
    
    static String getTypeName(final int a1) throws CompileError {
        String v1 = /*EL:357*/"";
        /*SL:358*/switch (a1) {
            case 301: {
                /*SL:360*/v1 = "boolean";
                /*SL:361*/break;
            }
            case 306: {
                /*SL:363*/v1 = "char";
                /*SL:364*/break;
            }
            case 303: {
                /*SL:366*/v1 = "byte";
                /*SL:367*/break;
            }
            case 334: {
                /*SL:369*/v1 = "short";
                /*SL:370*/break;
            }
            case 324: {
                /*SL:372*/v1 = "int";
                /*SL:373*/break;
            }
            case 326: {
                /*SL:375*/v1 = "long";
                /*SL:376*/break;
            }
            case 317: {
                /*SL:378*/v1 = "float";
                /*SL:379*/break;
            }
            case 312: {
                /*SL:381*/v1 = "double";
                /*SL:382*/break;
            }
            case 344: {
                /*SL:384*/v1 = "void";
                /*SL:385*/break;
            }
            default: {
                fatal();
                break;
            }
        }
        /*SL:390*/return v1;
    }
    
    public CtClass lookupClass(final String v1, final boolean v2) throws CompileError {
        final Hashtable v3 = /*EL:399*/this.getInvalidNames();
        final Object v4 = /*EL:400*/v3.get(v1);
        /*SL:401*/if (v4 == "<invalid>") {
            /*SL:402*/throw new CompileError("no such class: " + v1);
        }
        /*SL:403*/if (v4 != null) {
            try {
                /*SL:405*/return this.classPool.get((String)v4);
            }
            catch (NotFoundException ex) {}
        }
        CtClass v5 = /*EL:409*/null;
        try {
            /*SL:411*/v5 = this.lookupClass0(v1, v2);
        }
        catch (NotFoundException a1) {
            /*SL:414*/v5 = this.searchImports(v1);
        }
        /*SL:417*/v3.put(v1, v5.getName());
        /*SL:418*/return v5;
    }
    
    public static int getInvalidMapSize() {
        /*SL:426*/return MemberResolver.invalidNamesMap.size();
    }
    
    private Hashtable getInvalidNames() {
        Hashtable invalidNames = /*EL:429*/this.invalidNames;
        /*SL:430*/if (invalidNames == null) {
            /*SL:431*/synchronized (MemberResolver.class) {
                final WeakReference v1 = MemberResolver.invalidNamesMap.get(/*EL:432*/this.classPool);
                /*SL:433*/if (v1 != null) {
                    /*SL:434*/invalidNames = (Hashtable)v1.get();
                }
                /*SL:436*/if (invalidNames == null) {
                    /*SL:437*/invalidNames = new Hashtable();
                    MemberResolver.invalidNamesMap.put(/*EL:438*/this.classPool, new WeakReference<Hashtable>(invalidNames));
                }
            }
            /*SL:442*/this.invalidNames = invalidNames;
        }
        /*SL:445*/return invalidNames;
    }
    
    private CtClass searchImports(final String v-1) throws CompileError {
        /*SL:451*/if (v-1.indexOf(46) < 0) {
            final Iterator v0 = /*EL:452*/this.classPool.getImportedPackages();
            /*SL:453*/while (v0.hasNext()) {
                final String v = /*EL:454*/v0.next();
                final String v2 = /*EL:455*/v + '.' + v-1;
                try {
                    /*SL:457*/return this.classPool.get(v2);
                }
                catch (NotFoundException a1) {
                    try {
                        /*SL:461*/if (v.endsWith("." + v-1)) {
                            /*SL:462*/return this.classPool.get(v);
                        }
                        /*SL:464*/continue;
                    }
                    catch (NotFoundException ex) {}
                    /*SL:466*/continue;
                }
                break;
            }
        }
        /*SL:469*/this.getInvalidNames().put(v-1, "<invalid>");
        /*SL:470*/throw new CompileError("no such class: " + v-1);
    }
    
    private CtClass lookupClass0(String v-2, final boolean v-1) throws NotFoundException {
        CtClass v0 = /*EL:476*/null;
        do {
            try {
                /*SL:479*/v0 = this.classPool.get(v-2);
            }
            catch (NotFoundException v) {
                int a2 = /*EL:482*/v-2.lastIndexOf(46);
                /*SL:483*/if (v-1 || a2 < 0) {
                    /*SL:484*/throw v;
                }
                /*SL:486*/a2 = new StringBuffer(v-2);
                /*SL:487*/a2.setCharAt(a2, '$');
                /*SL:488*/v-2 = a2.toString();
            }
        } while (/*EL:491*/v0 == null);
        /*SL:492*/return v0;
    }
    
    public String resolveClassName(final ASTList a1) throws CompileError {
        /*SL:501*/if (a1 == null) {
            /*SL:502*/return null;
        }
        /*SL:504*/return javaToJvmName(this.lookupClassByName(a1).getName());
    }
    
    public String resolveJvmClassName(final String a1) throws CompileError {
        /*SL:511*/if (a1 == null) {
            /*SL:512*/return null;
        }
        /*SL:514*/return javaToJvmName(this.lookupClassByJvmName(a1).getName());
    }
    
    public static CtClass getSuperclass(final CtClass v1) throws CompileError {
        try {
            final CtClass a1 = /*EL:519*/v1.getSuperclass();
            /*SL:520*/if (a1 != null) {
                /*SL:521*/return a1;
            }
        }
        catch (NotFoundException ex) {}
        /*SL:524*/throw new CompileError("cannot find the super class of " + v1.getName());
    }
    
    public static CtClass getSuperInterface(final CtClass v1, final String v2) throws CompileError {
        try {
            CtClass[] a2;
            int a2;
            /*SL:533*/for (a2 = v1.getInterfaces(), a2 = 0; a2 < a2.length; ++a2) {
                /*SL:534*/if (a2[a2].getName().equals(v2)) {
                    /*SL:535*/return a2[a2];
                }
            }
        }
        catch (NotFoundException ex) {}
        /*SL:537*/throw new CompileError("cannot find the super inetrface " + v2 + " of " + v1.getName());
    }
    
    public static String javaToJvmName(final String a1) {
        /*SL:542*/return a1.replace('.', '/');
    }
    
    public static String jvmToJavaName(final String a1) {
        /*SL:546*/return a1.replace('/', '.');
    }
    
    public static int descToType(final char a1) throws CompileError {
        /*SL:550*/switch (a1) {
            case 'Z': {
                /*SL:552*/return 301;
            }
            case 'C': {
                /*SL:554*/return 306;
            }
            case 'B': {
                /*SL:556*/return 303;
            }
            case 'S': {
                /*SL:558*/return 334;
            }
            case 'I': {
                /*SL:560*/return 324;
            }
            case 'J': {
                /*SL:562*/return 326;
            }
            case 'F': {
                /*SL:564*/return 317;
            }
            case 'D': {
                /*SL:566*/return 312;
            }
            case 'V': {
                /*SL:568*/return 344;
            }
            case 'L':
            case '[': {
                /*SL:571*/return 307;
            }
            default: {
                fatal();
                /*SL:574*/return 344;
            }
        }
    }
    
    public static int getModifiers(ASTList v1) {
        int v2 = /*EL:579*/0;
        /*SL:580*/while (v1 != null) {
            final Keyword a1 = /*EL:581*/(Keyword)v1.head();
            /*SL:582*/v1 = v1.tail();
            /*SL:583*/switch (a1.get()) {
                case 335: {
                    /*SL:585*/v2 |= 0x8;
                    /*SL:586*/continue;
                }
                case 315: {
                    /*SL:588*/v2 |= 0x10;
                    /*SL:589*/continue;
                }
                case 338: {
                    /*SL:591*/v2 |= 0x20;
                    /*SL:592*/continue;
                }
                case 300: {
                    /*SL:594*/v2 |= 0x400;
                    /*SL:595*/continue;
                }
                case 332: {
                    /*SL:597*/v2 |= 0x1;
                    /*SL:598*/continue;
                }
                case 331: {
                    /*SL:600*/v2 |= 0x4;
                    /*SL:601*/continue;
                }
                case 330: {
                    /*SL:603*/v2 |= 0x2;
                    /*SL:604*/continue;
                }
                case 345: {
                    /*SL:606*/v2 |= 0x40;
                    /*SL:607*/continue;
                }
                case 342: {
                    /*SL:609*/v2 |= 0x80;
                    /*SL:610*/continue;
                }
                case 347: {
                    /*SL:612*/v2 |= 0x800;
                    continue;
                }
            }
        }
        /*SL:617*/return v2;
    }
    
    static {
        MemberResolver.invalidNamesMap = new WeakHashMap();
    }
    
    public static class Method
    {
        public CtClass declaring;
        public MethodInfo info;
        public int notmatch;
        
        public Method(final CtClass a1, final MethodInfo a2, final int a3) {
            this.declaring = a1;
            this.info = a2;
            this.notmatch = a3;
        }
        
        public boolean isStatic() {
            final int v1 = /*EL:59*/this.info.getAccessFlags();
            /*SL:60*/return (v1 & 0x8) != 0x0;
        }
    }
}
