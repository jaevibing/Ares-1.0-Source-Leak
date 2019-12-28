package javassist.bytecode;

import javassist.CtClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class SignatureAttribute extends AttributeInfo
{
    public static final String tag = "Signature";
    
    SignatureAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public SignatureAttribute(final ConstPool a1, final String a2) {
        super(a1, "Signature");
        final int v1 = a1.addUtf8Info(a2);
        final byte[] v2 = { (byte)(v1 >>> 8), (byte)v1 };
        this.set(v2);
    }
    
    public String getSignature() {
        /*SL:63*/return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
    }
    
    public void setSignature(final String a1) {
        final int v1 = /*EL:74*/this.getConstPool().addUtf8Info(a1);
        /*SL:75*/ByteArray.write16bit(v1, this.info, 0);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool a1, final Map a2) {
        /*SL:87*/return new SignatureAttribute(a1, this.getSignature());
    }
    
    @Override
    void renameClass(final String a1, final String a2) {
        final String v1 = renameClass(/*EL:91*/this.getSignature(), a1, a2);
        /*SL:92*/this.setSignature(v1);
    }
    
    @Override
    void renameClass(final Map a1) {
        final String v1 = renameClass(/*EL:96*/this.getSignature(), a1);
        /*SL:97*/this.setSignature(v1);
    }
    
    static String renameClass(final String a1, final String a2, final String a3) {
        final Map v1 = /*EL:101*/new HashMap();
        /*SL:102*/v1.put(a2, a3);
        /*SL:103*/return renameClass(a1, v1);
    }
    
    static String renameClass(final String v-4, final Map v-3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-3 */
        //     1: ifnonnull       6
        //     4: aload_0         /* v-4 */
        //     5: areturn        
        //     6: new             Ljava/lang/StringBuilder;
        //     9: dup            
        //    10: invokespecial   java/lang/StringBuilder.<init>:()V
        //    13: astore_2        /* v-2 */
        //    14: iconst_0       
        //    15: istore_3        /* v-1 */
        //    16: iconst_0       
        //    17: istore          v0
        //    19: aload_0         /* v-4 */
        //    20: bipush          76
        //    22: iload           v0
        //    24: invokevirtual   java/lang/String.indexOf:(II)I
        //    27: istore          v1
        //    29: iload           v1
        //    31: ifge            37
        //    34: goto            199
        //    37: new             Ljava/lang/StringBuilder;
        //    40: dup            
        //    41: invokespecial   java/lang/StringBuilder.<init>:()V
        //    44: astore          v2
        //    46: iload           v1
        //    48: istore          v3
        //    50: aload_0         /* v-4 */
        //    51: iinc            v3, 1
        //    54: iload           v3
        //    56: invokevirtual   java/lang/String.charAt:(I)C
        //    59: dup            
        //    60: istore          a1
        //    62: bipush          59
        //    64: if_icmpeq       121
        //    67: aload           v2
        //    69: iload           a1
        //    71: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    74: pop            
        //    75: iload           a1
        //    77: bipush          60
        //    79: if_icmpne       50
        //    82: aload_0         /* v-4 */
        //    83: iinc            v3, 1
        //    86: iload           v3
        //    88: invokevirtual   java/lang/String.charAt:(I)C
        //    91: dup            
        //    92: istore          a1
        //    94: bipush          62
        //    96: if_icmpeq       110
        //    99: aload           v2
        //   101: iload           a1
        //   103: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   106: pop            
        //   107: goto            82
        //   110: aload           v2
        //   112: iload           a1
        //   114: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   117: pop            
        //   118: goto            50
        //   121: goto            129
        //   124: astore          a2
        //   126: goto            199
        //   129: iload           v3
        //   131: iconst_1       
        //   132: iadd           
        //   133: istore          v0
        //   135: aload           v2
        //   137: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   140: astore          v5
        //   142: aload_1         /* v-3 */
        //   143: aload           v5
        //   145: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   150: checkcast       Ljava/lang/String;
        //   153: astore          v6
        //   155: aload           v6
        //   157: ifnull          196
        //   160: aload_2         /* v-2 */
        //   161: aload_0         /* v-4 */
        //   162: iload_3         /* v-1 */
        //   163: iload           v1
        //   165: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   168: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   171: pop            
        //   172: aload_2         /* v-2 */
        //   173: bipush          76
        //   175: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   178: pop            
        //   179: aload_2         /* v-2 */
        //   180: aload           v6
        //   182: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   185: pop            
        //   186: aload_2         /* v-2 */
        //   187: iload           v4
        //   189: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   192: pop            
        //   193: iload           v0
        //   195: istore_3        /* v-1 */
        //   196: goto            19
        //   199: iload_3         /* v-1 */
        //   200: ifne            205
        //   203: aload_0         /* v-4 */
        //   204: areturn        
        //   205: aload_0         /* v-4 */
        //   206: invokevirtual   java/lang/String.length:()I
        //   209: istore          v1
        //   211: iload_3         /* v-1 */
        //   212: iload           v1
        //   214: if_icmpge       229
        //   217: aload_2         /* v-2 */
        //   218: aload_0         /* v-4 */
        //   219: iload_3         /* v-1 */
        //   220: iload           v1
        //   222: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   225: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   228: pop            
        //   229: aload_2         /* v-2 */
        //   230: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   233: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------------------
        //  62     62      8     a1    C
        //  126    3       9     a2    Ljava/lang/IndexOutOfBoundsException;
        //  29     167     5     v1    I
        //  46     150     6     v2    Ljava/lang/StringBuilder;
        //  50     146     7     v3    I
        //  129    67      8     v4    C
        //  142    54      9     v5    Ljava/lang/String;
        //  155    41      10    v6    Ljava/lang/String;
        //  211    23      5     v1    I
        //  0      234     0     v-4   Ljava/lang/String;
        //  0      234     1     v-3   Ljava/util/Map;
        //  14     220     2     v-2   Ljava/lang/StringBuilder;
        //  16     218     3     v-1   I
        //  19     215     4     v0    I
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                 
        //  -----  -----  -----  -----  -------------------------------------
        //  50     121    124    129    Ljava/lang/IndexOutOfBoundsException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2987)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static boolean isNamePart(final int a1) {
        /*SL:157*/return a1 != 59 && a1 != 60;
    }
    
    public static ClassSignature toClassSignature(final String v1) throws BadBytecode {
        try {
            /*SL:924*/return parseSig(v1);
        }
        catch (IndexOutOfBoundsException a1) {
            throw error(/*EL:927*/v1);
        }
    }
    
    public static MethodSignature toMethodSignature(final String v1) throws BadBytecode {
        try {
            /*SL:944*/return parseMethodSig(v1);
        }
        catch (IndexOutOfBoundsException a1) {
            throw error(/*EL:947*/v1);
        }
    }
    
    public static ObjectType toFieldSignature(final String v1) throws BadBytecode {
        try {
            /*SL:963*/return parseObjectType(v1, new Cursor(), false);
        }
        catch (IndexOutOfBoundsException a1) {
            throw error(/*EL:966*/v1);
        }
    }
    
    public static Type toTypeSignature(final String v1) throws BadBytecode {
        try {
            /*SL:980*/return parseType(v1, new Cursor());
        }
        catch (IndexOutOfBoundsException a1) {
            throw error(/*EL:983*/v1);
        }
    }
    
    private static ClassSignature parseSig(final String a1) throws BadBytecode, IndexOutOfBoundsException {
        final Cursor v1 = /*EL:990*/new Cursor();
        final TypeParameter[] v2 = parseTypeParams(/*EL:991*/a1, v1);
        final ClassType v3 = parseClassType(/*EL:992*/a1, v1);
        final int v4 = /*EL:993*/a1.length();
        final ArrayList v5 = /*EL:994*/new ArrayList();
        /*SL:995*/while (v1.position < v4 && a1.charAt(v1.position) == 'L') {
            /*SL:996*/v5.add(parseClassType(a1, v1));
        }
        final ClassType[] v6 = /*EL:998*/v5.<ClassType>toArray(/*EL:999*/new ClassType[v5.size()]);
        /*SL:1000*/return new ClassSignature(v2, v3, v6);
    }
    
    private static MethodSignature parseMethodSig(final String v-6) throws BadBytecode {
        final Cursor cursor = /*EL:1006*/new Cursor();
        final TypeParameter[] typeParams = parseTypeParams(/*EL:1007*/v-6, cursor);
        /*SL:1008*/if (v-6.charAt(cursor.position++) != '(') {
            throw error(/*EL:1009*/v-6);
        }
        final ArrayList list = /*EL:1011*/new ArrayList();
        /*SL:1012*/while (v-6.charAt(cursor.position) != ')') {
            final Type a1 = parseType(/*EL:1013*/v-6, cursor);
            /*SL:1014*/list.add(a1);
        }
        final Cursor cursor2 = /*EL:1017*/cursor;
        ++cursor2.position;
        final Type type = parseType(/*EL:1018*/v-6, cursor);
        final int length = /*EL:1019*/v-6.length();
        final ArrayList v0 = /*EL:1020*/new ArrayList();
        /*SL:1021*/while (cursor.position < length && v-6.charAt(cursor.position) == '^') {
            final Cursor cursor3 = /*EL:1022*/cursor;
            ++cursor3.position;
            final ObjectType v = parseObjectType(/*EL:1023*/v-6, cursor, false);
            /*SL:1024*/if (v instanceof ArrayType) {
                throw error(/*EL:1025*/v-6);
            }
            /*SL:1027*/v0.add(v);
        }
        final Type[] v2 = /*EL:1030*/list.<Type>toArray(new Type[list.size()]);
        final ObjectType[] v3 = /*EL:1031*/v0.<ObjectType>toArray(new ObjectType[v0.size()]);
        /*SL:1032*/return new MethodSignature(typeParams, v2, type, v3);
    }
    
    private static TypeParameter[] parseTypeParams(final String v-3, final Cursor v-2) throws BadBytecode {
        final ArrayList list = /*EL:1038*/new ArrayList();
        /*SL:1039*/if (v-3.charAt(v-2.position) == '<') {
            /*SL:1040*/++v-2.position;
            /*SL:1041*/while (v-3.charAt(v-2.position) != '>') {
                int a2 = /*EL:1042*/v-2.position;
                final int v1 = /*EL:1043*/v-2.indexOf(v-3, 58);
                final ObjectType v2 = parseObjectType(/*EL:1044*/v-3, v-2, true);
                final ArrayList v3 = /*EL:1045*/new ArrayList();
                /*SL:1046*/while (v-3.charAt(v-2.position) == ':') {
                    /*SL:1047*/++v-2.position;
                    /*SL:1048*/a2 = parseObjectType(v-3, v-2, false);
                    /*SL:1049*/v3.add(a2);
                }
                final TypeParameter v4 = /*EL:1053*/new TypeParameter(v-3, a2, v1, v2, v3.<ObjectType>toArray(new ObjectType[v3.size()]));
                /*SL:1054*/list.add(v4);
            }
            /*SL:1057*/++v-2.position;
        }
        /*SL:1060*/return list.<TypeParameter>toArray(new TypeParameter[list.size()]);
    }
    
    private static ObjectType parseObjectType(final String a2, final Cursor a3, final boolean v1) throws BadBytecode {
        final int v2 = /*EL:1067*/a3.position;
        /*SL:1068*/switch (a2.charAt(v2)) {
            case 'L': {
                /*SL:1070*/return parseClassType2(a2, a3, null);
            }
            case 'T': {
                final int a4 = /*EL:1072*/a3.indexOf(a2, 59);
                /*SL:1073*/return new TypeVariable(a2, v2 + 1, a4);
            }
            case '[': {
                /*SL:1075*/return parseArray(a2, a3);
            }
            default: {
                /*SL:1077*/if (v1) {
                    /*SL:1078*/return null;
                }
                throw error(/*EL:1080*/a2);
            }
        }
    }
    
    private static ClassType parseClassType(final String a1, final Cursor a2) throws BadBytecode {
        /*SL:1087*/if (a1.charAt(a2.position) == 'L') {
            /*SL:1088*/return parseClassType2(a1, a2, null);
        }
        throw error(/*EL:1090*/a1);
    }
    
    private static ClassType parseClassType2(final String a2, final Cursor a3, final ClassType v1) throws BadBytecode {
        final int v2 = /*EL:1096*/++a3.position;
        char v3;
        /*SL:1100*/do {
            v3 = a2.charAt(a3.position++);
        } while (v3 != '$' && v3 != '<' && v3 != ';');
        final int v4 = /*EL:1101*/a3.position - 1;
        final TypeArgument[] v5;
        /*SL:1103*/if (v3 == '<') {
            final TypeArgument[] a4 = parseTypeArgs(/*EL:1104*/a2, a3);
            /*SL:1105*/v3 = a2.charAt(a3.position++);
        }
        else {
            /*SL:1108*/v5 = null;
        }
        final ClassType v6 = /*EL:1110*/ClassType.make(a2, v2, v4, v5, v1);
        /*SL:1111*/if (v3 == '$' || v3 == '.') {
            /*SL:1112*/--a3.position;
            /*SL:1113*/return parseClassType2(a2, a3, v6);
        }
        /*SL:1116*/return v6;
    }
    
    private static TypeArgument[] parseTypeArgs(final String v1, final Cursor v2) throws BadBytecode {
        final ArrayList v3 = /*EL:1120*/new ArrayList();
        char v4;
        /*SL:1122*/while ((v4 = v1.charAt(v2.position++)) != '>') {
            final TypeArgument a2;
            /*SL:1124*/if (v4 == '*') {
                final TypeArgument a1 = /*EL:1125*/new TypeArgument(null, '*');
            }
            else {
                /*SL:1127*/if (v4 != '+' && v4 != '-') {
                    /*SL:1128*/v4 = ' ';
                    /*SL:1129*/--v2.position;
                }
                /*SL:1132*/a2 = new TypeArgument(parseObjectType(v1, v2, false), v4);
            }
            /*SL:1135*/v3.add(a2);
        }
        /*SL:1138*/return v3.<TypeArgument>toArray(new TypeArgument[v3.size()]);
    }
    
    private static ObjectType parseArray(final String a1, final Cursor a2) throws BadBytecode {
        int v1 = /*EL:1142*/1;
        /*SL:1143*/while (a1.charAt(++a2.position) == '[') {
            /*SL:1144*/++v1;
        }
        /*SL:1146*/return new ArrayType(v1, parseType(a1, a2));
    }
    
    private static Type parseType(final String a1, final Cursor a2) throws BadBytecode {
        Type v1 = parseObjectType(/*EL:1150*/a1, a2, true);
        /*SL:1151*/if (v1 == null) {
            /*SL:1152*/v1 = new BaseType(a1.charAt(a2.position++));
        }
        /*SL:1154*/return v1;
    }
    
    private static BadBytecode error(final String a1) {
        /*SL:1158*/return new BadBytecode("bad signature: " + a1);
    }
    
    private static class Cursor
    {
        int position;
        
        private Cursor() {
            this.position = 0;
        }
        
        int indexOf(final String a1, final int a2) throws BadBytecode {
            final int v1 = /*EL:164*/a1.indexOf(a2, this.position);
            /*SL:165*/if (v1 < 0) {
                /*SL:166*/throw error(a1);
            }
            /*SL:168*/this.position = v1 + 1;
            /*SL:169*/return v1;
        }
    }
    
    public static class ClassSignature
    {
        TypeParameter[] params;
        ClassType superClass;
        ClassType[] interfaces;
        
        public ClassSignature(final TypeParameter[] a1, final ClassType a2, final ClassType[] a3) {
            this.params = ((a1 == null) ? new TypeParameter[0] : a1);
            this.superClass = ((a2 == null) ? ClassType.OBJECT : a2);
            this.interfaces = ((a3 == null) ? new ClassType[0] : a3);
        }
        
        public ClassSignature(final TypeParameter[] a1) {
            this(a1, null, null);
        }
        
        public TypeParameter[] getParameters() {
            /*SL:210*/return this.params;
        }
        
        public ClassType getSuperClass() {
            /*SL:216*/return this.superClass;
        }
        
        public ClassType[] getInterfaces() {
            /*SL:223*/return this.interfaces;
        }
        
        @Override
        public String toString() {
            final StringBuffer v1 = /*EL:229*/new StringBuffer();
            /*SL:231*/TypeParameter.toString(v1, this.params);
            /*SL:232*/v1.append(" extends ").append(this.superClass);
            /*SL:233*/if (this.interfaces.length > 0) {
                /*SL:234*/v1.append(" implements ");
                /*SL:235*/Type.toString(v1, this.interfaces);
            }
            /*SL:238*/return v1.toString();
        }
        
        public String encode() {
            final StringBuffer v0 = /*EL:245*/new StringBuffer();
            /*SL:246*/if (this.params.length > 0) {
                /*SL:247*/v0.append('<');
                /*SL:248*/for (int v = 0; v < this.params.length; ++v) {
                    /*SL:249*/this.params[v].encode(v0);
                }
                /*SL:251*/v0.append('>');
            }
            /*SL:254*/this.superClass.encode(v0);
            /*SL:255*/for (int v = 0; v < this.interfaces.length; ++v) {
                /*SL:256*/this.interfaces[v].encode(v0);
            }
            /*SL:258*/return v0.toString();
        }
    }
    
    public static class MethodSignature
    {
        TypeParameter[] typeParams;
        Type[] params;
        Type retType;
        ObjectType[] exceptions;
        
        public MethodSignature(final TypeParameter[] a1, final Type[] a2, final Type a3, final ObjectType[] a4) {
            this.typeParams = ((a1 == null) ? new TypeParameter[0] : a1);
            this.params = ((a2 == null) ? new Type[0] : a2);
            this.retType = ((a3 == null) ? new BaseType("void") : a3);
            this.exceptions = ((a4 == null) ? new ObjectType[0] : a4);
        }
        
        public TypeParameter[] getTypeParameters() {
            /*SL:292*/return this.typeParams;
        }
        
        public Type[] getParameterTypes() {
            /*SL:299*/return this.params;
        }
        
        public Type getReturnType() {
            /*SL:304*/return this.retType;
        }
        
        public ObjectType[] getExceptionTypes() {
            /*SL:312*/return this.exceptions;
        }
        
        @Override
        public String toString() {
            final StringBuffer v1 = /*EL:318*/new StringBuffer();
            /*SL:320*/TypeParameter.toString(v1, this.typeParams);
            /*SL:321*/v1.append(" (");
            /*SL:322*/Type.toString(v1, this.params);
            /*SL:323*/v1.append(") ");
            /*SL:324*/v1.append(this.retType);
            /*SL:325*/if (this.exceptions.length > 0) {
                /*SL:326*/v1.append(" throws ");
                /*SL:327*/Type.toString(v1, this.exceptions);
            }
            /*SL:330*/return v1.toString();
        }
        
        public String encode() {
            final StringBuffer v0 = /*EL:337*/new StringBuffer();
            /*SL:338*/if (this.typeParams.length > 0) {
                /*SL:339*/v0.append('<');
                /*SL:340*/for (int v = 0; v < this.typeParams.length; ++v) {
                    /*SL:341*/this.typeParams[v].encode(v0);
                }
                /*SL:343*/v0.append('>');
            }
            /*SL:346*/v0.append('(');
            /*SL:347*/for (int v = 0; v < this.params.length; ++v) {
                /*SL:348*/this.params[v].encode(v0);
            }
            /*SL:350*/v0.append(')');
            /*SL:351*/this.retType.encode(v0);
            /*SL:352*/if (this.exceptions.length > 0) {
                /*SL:353*/for (int v = 0; v < this.exceptions.length; ++v) {
                    /*SL:354*/v0.append('^');
                    /*SL:355*/this.exceptions[v].encode(v0);
                }
            }
            /*SL:358*/return v0.toString();
        }
    }
    
    public static class TypeParameter
    {
        String name;
        ObjectType superClass;
        ObjectType[] superInterfaces;
        
        TypeParameter(final String a1, final int a2, final int a3, final ObjectType a4, final ObjectType[] a5) {
            this.name = a1.substring(a2, a3);
            this.superClass = a4;
            this.superInterfaces = a5;
        }
        
        public TypeParameter(final String a1, final ObjectType a2, final ObjectType[] a3) {
            this.name = a1;
            this.superClass = a2;
            if (a3 == null) {
                this.superInterfaces = new ObjectType[0];
            }
            else {
                this.superInterfaces = a3;
            }
        }
        
        public TypeParameter(final String a1) {
            this(a1, null, null);
        }
        
        public String getName() {
            /*SL:409*/return this.name;
        }
        
        public ObjectType getClassBound() {
            /*SL:415*/return this.superClass;
        }
        
        public ObjectType[] getInterfaceBound() {
            /*SL:422*/return this.superInterfaces;
        }
        
        @Override
        public String toString() {
            final StringBuffer sb = /*EL:428*/new StringBuffer(this.getName());
            /*SL:429*/if (this.superClass != null) {
                /*SL:430*/sb.append(" extends ").append(this.superClass.toString());
            }
            final int v0 = /*EL:432*/this.superInterfaces.length;
            /*SL:433*/if (v0 > 0) {
                /*SL:434*/for (int v = 0; v < v0; ++v) {
                    /*SL:435*/if (v > 0 || this.superClass != null) {
                        /*SL:436*/sb.append(" & ");
                    }
                    else {
                        /*SL:438*/sb.append(" extends ");
                    }
                    /*SL:440*/sb.append(this.superInterfaces[v].toString());
                }
            }
            /*SL:444*/return sb.toString();
        }
        
        static void toString(final StringBuffer a2, final TypeParameter[] v1) {
            /*SL:448*/a2.append('<');
            /*SL:449*/for (int a3 = 0; a3 < v1.length; ++a3) {
                /*SL:450*/if (a3 > 0) {
                    /*SL:451*/a2.append(", ");
                }
                /*SL:453*/a2.append(v1[a3]);
            }
            /*SL:456*/a2.append('>');
        }
        
        void encode(final StringBuffer v2) {
            /*SL:460*/v2.append(this.name);
            /*SL:461*/if (this.superClass == null) {
                /*SL:462*/v2.append(":Ljava/lang/Object;");
            }
            else {
                /*SL:464*/v2.append(':');
                /*SL:465*/this.superClass.encode(v2);
            }
            /*SL:468*/for (int a1 = 0; a1 < this.superInterfaces.length; ++a1) {
                /*SL:469*/v2.append(':');
                /*SL:470*/this.superInterfaces[a1].encode(v2);
            }
        }
    }
    
    public static class TypeArgument
    {
        ObjectType arg;
        char wildcard;
        
        TypeArgument(final ObjectType a1, final char a2) {
            this.arg = a1;
            this.wildcard = a2;
        }
        
        public TypeArgument(final ObjectType a1) {
            this(a1, ' ');
        }
        
        public TypeArgument() {
            this(null, '*');
        }
        
        public static TypeArgument subclassOf(final ObjectType a1) {
            /*SL:514*/return new TypeArgument(a1, '+');
        }
        
        public static TypeArgument superOf(final ObjectType a1) {
            /*SL:524*/return new TypeArgument(a1, '-');
        }
        
        public char getKind() {
            /*SL:533*/return this.wildcard;
        }
        
        public boolean isWildcard() {
            /*SL:539*/return this.wildcard != ' ';
        }
        
        public ObjectType getType() {
            /*SL:548*/return this.arg;
        }
        
        @Override
        public String toString() {
            /*SL:554*/if (this.wildcard == '*') {
                /*SL:555*/return "?";
            }
            final String v1 = /*EL:557*/this.arg.toString();
            /*SL:558*/if (this.wildcard == ' ') {
                /*SL:559*/return v1;
            }
            /*SL:560*/if (this.wildcard == '+') {
                /*SL:561*/return "? extends " + v1;
            }
            /*SL:563*/return "? super " + v1;
        }
        
        static void encode(final StringBuffer v1, final TypeArgument[] v2) {
            /*SL:567*/v1.append('<');
            /*SL:568*/for (TypeArgument a2 = (TypeArgument)0; a2 < v2.length; ++a2) {
                /*SL:569*/a2 = v2[a2];
                /*SL:570*/if (a2.isWildcard()) {
                    /*SL:571*/v1.append(a2.wildcard);
                }
                /*SL:573*/if (a2.getType() != null) {
                    /*SL:574*/a2.getType().encode(v1);
                }
            }
            /*SL:577*/v1.append('>');
        }
    }
    
    public abstract static class Type
    {
        abstract void encode(final StringBuffer p0);
        
        static void toString(final StringBuffer a2, final Type[] v1) {
            /*SL:587*/for (int a3 = 0; a3 < v1.length; ++a3) {
                /*SL:588*/if (a3 > 0) {
                    /*SL:589*/a2.append(", ");
                }
                /*SL:591*/a2.append(v1[a3]);
            }
        }
        
        public String jvmTypeName() {
            /*SL:600*/return this.toString();
        }
    }
    
    public static class BaseType extends Type
    {
        char descriptor;
        
        BaseType(final char a1) {
            this.descriptor = a1;
        }
        
        public BaseType(final String a1) {
            this(Descriptor.of(a1).charAt(0));
        }
        
        public char getDescriptor() {
            /*SL:624*/return this.descriptor;
        }
        
        public CtClass getCtlass() {
            /*SL:631*/return Descriptor.toPrimitiveClass(this.descriptor);
        }
        
        @Override
        public String toString() {
            /*SL:638*/return Descriptor.toClassName(Character.toString(this.descriptor));
        }
        
        @Override
        void encode(final StringBuffer a1) {
            /*SL:642*/a1.append(this.descriptor);
        }
    }
    
    public abstract static class ObjectType extends Type
    {
        public String encode() {
            final StringBuffer v1 = /*EL:655*/new StringBuffer();
            /*SL:656*/this.encode(v1);
            /*SL:657*/return v1.toString();
        }
    }
    
    public static class ClassType extends ObjectType
    {
        String name;
        TypeArgument[] arguments;
        public static ClassType OBJECT;
        
        static ClassType make(final String a1, final int a2, final int a3, final TypeArgument[] a4, final ClassType a5) {
            /*SL:670*/if (a5 == null) {
                /*SL:671*/return new ClassType(a1, a2, a3, a4);
            }
            /*SL:673*/return new NestedClassType(a1, a2, a3, a4, a5);
        }
        
        ClassType(final String a1, final int a2, final int a3, final TypeArgument[] a4) {
            this.name = a1.substring(a2, a3).replace('/', '.');
            this.arguments = a4;
        }
        
        public ClassType(final String a1, final TypeArgument[] a2) {
            this.name = a1;
            this.arguments = a2;
        }
        
        public ClassType(final String a1) {
            this(a1, null);
        }
        
        public String getName() {
            /*SL:712*/return this.name;
        }
        
        public TypeArgument[] getTypeArguments() {
            /*SL:720*/return this.arguments;
        }
        
        public ClassType getDeclaringClass() {
            /*SL:728*/return null;
        }
        
        @Override
        public String toString() {
            final StringBuffer v1 = /*EL:734*/new StringBuffer();
            final ClassType v2 = /*EL:735*/this.getDeclaringClass();
            /*SL:736*/if (v2 != null) {
                /*SL:737*/v1.append(v2.toString()).append('.');
            }
            /*SL:739*/return this.toString2(v1);
        }
        
        private String toString2(final StringBuffer v0) {
            /*SL:743*/v0.append(this.name);
            /*SL:744*/if (this.arguments != null) {
                /*SL:745*/v0.append('<');
                /*SL:747*/for (int v = this.arguments.length, a1 = 0; a1 < v; ++a1) {
                    /*SL:748*/if (a1 > 0) {
                        /*SL:749*/v0.append(", ");
                    }
                    /*SL:751*/v0.append(this.arguments[a1].toString());
                }
                /*SL:754*/v0.append('>');
            }
            /*SL:757*/return v0.toString();
        }
        
        @Override
        public String jvmTypeName() {
            final StringBuffer v1 = /*EL:766*/new StringBuffer();
            final ClassType v2 = /*EL:767*/this.getDeclaringClass();
            /*SL:768*/if (v2 != null) {
                /*SL:769*/v1.append(v2.jvmTypeName()).append('$');
            }
            /*SL:771*/return this.toString2(v1);
        }
        
        @Override
        void encode(final StringBuffer a1) {
            /*SL:775*/a1.append('L');
            /*SL:776*/this.encode2(a1);
            /*SL:777*/a1.append(';');
        }
        
        void encode2(final StringBuffer a1) {
            final ClassType v1 = /*EL:781*/this.getDeclaringClass();
            /*SL:782*/if (v1 != null) {
                /*SL:783*/v1.encode2(a1);
                /*SL:784*/a1.append('$');
            }
            /*SL:787*/a1.append(this.name.replace('.', '/'));
            /*SL:788*/if (this.arguments != null) {
                /*SL:789*/TypeArgument.encode(a1, this.arguments);
            }
        }
        
        static {
            ClassType.OBJECT = new ClassType("java.lang.Object", null);
        }
    }
    
    public static class NestedClassType extends ClassType
    {
        ClassType parent;
        
        NestedClassType(final String a1, final int a2, final int a3, final TypeArgument[] a4, final ClassType a5) {
            super(a1, a2, a3, a4);
            this.parent = a5;
        }
        
        public NestedClassType(final ClassType a1, final String a2, final TypeArgument[] a3) {
            super(a2, a3);
            this.parent = a1;
        }
        
        @Override
        public ClassType getDeclaringClass() {
            /*SL:821*/return this.parent;
        }
    }
    
    public static class ArrayType extends ObjectType
    {
        int dim;
        Type componentType;
        
        public ArrayType(final int a1, final Type a2) {
            this.dim = a1;
            this.componentType = a2;
        }
        
        public int getDimension() {
            /*SL:845*/return this.dim;
        }
        
        public Type getComponentType() {
            /*SL:851*/return this.componentType;
        }
        
        @Override
        public String toString() {
            final StringBuffer v0 = /*EL:858*/new StringBuffer(this.componentType.toString());
            /*SL:859*/for (int v = 0; v < this.dim; ++v) {
                /*SL:860*/v0.append("[]");
            }
            /*SL:862*/return v0.toString();
        }
        
        @Override
        void encode(final StringBuffer v2) {
            /*SL:866*/for (int a1 = 0; a1 < this.dim; ++a1) {
                /*SL:867*/v2.append('[');
            }
            /*SL:869*/this.componentType.encode(v2);
        }
    }
    
    public static class TypeVariable extends ObjectType
    {
        String name;
        
        TypeVariable(final String a1, final int a2, final int a3) {
            this.name = a1.substring(a2, a3);
        }
        
        public TypeVariable(final String a1) {
            this.name = a1;
        }
        
        public String getName() {
            /*SL:896*/return this.name;
        }
        
        @Override
        public String toString() {
            /*SL:903*/return this.name;
        }
        
        @Override
        void encode(final StringBuffer a1) {
            /*SL:907*/a1.append('T').append(this.name).append(';');
        }
    }
}
