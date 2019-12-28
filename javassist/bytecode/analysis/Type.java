package javassist.bytecode.analysis;

import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Iterator;
import javassist.NotFoundException;
import javassist.ClassPool;
import java.util.Map;
import javassist.CtClass;

public class Type
{
    private final CtClass clazz;
    private final boolean special;
    private static final Map prims;
    public static final Type DOUBLE;
    public static final Type BOOLEAN;
    public static final Type LONG;
    public static final Type CHAR;
    public static final Type BYTE;
    public static final Type SHORT;
    public static final Type INTEGER;
    public static final Type FLOAT;
    public static final Type VOID;
    public static final Type UNINIT;
    public static final Type RETURN_ADDRESS;
    public static final Type TOP;
    public static final Type BOGUS;
    public static final Type OBJECT;
    public static final Type SERIALIZABLE;
    public static final Type CLONEABLE;
    public static final Type THROWABLE;
    
    public static Type get(final CtClass a1) {
        final Type v1 = Type.prims.get(/*EL:127*/a1);
        /*SL:128*/return (v1 != null) ? v1 : new Type(a1);
    }
    
    private static Type lookupType(final String v1) {
        try {
            /*SL:133*/return new Type(ClassPool.getDefault().get(v1));
        }
        catch (NotFoundException a1) {
            /*SL:135*/throw new RuntimeException(a1);
        }
    }
    
    Type(final CtClass a1) {
        this(a1, false);
    }
    
    private Type(final CtClass a1, final boolean a2) {
        this.clazz = a1;
        this.special = a2;
    }
    
    boolean popChanged() {
        /*SL:150*/return false;
    }
    
    public int getSize() {
        /*SL:160*/return (this.clazz == CtClass.doubleType || this.clazz == CtClass.longType || this == Type.TOP) ? 2 : 1;
    }
    
    public CtClass getCtClass() {
        /*SL:169*/return this.clazz;
    }
    
    public boolean isReference() {
        /*SL:178*/return !this.special && (this.clazz == null || !this.clazz.isPrimitive());
    }
    
    public boolean isSpecial() {
        /*SL:188*/return this.special;
    }
    
    public boolean isArray() {
        /*SL:197*/return this.clazz != null && this.clazz.isArray();
    }
    
    public int getDimensions() {
        /*SL:207*/if (!this.isArray()) {
            return 0;
        }
        String v1;
        int v2;
        int v3;
        /*SL:212*/for (v1 = this.clazz.getName(), v2 = v1.length() - 1, v3 = 0; v1.charAt(v2) == ']'; /*SL:213*/v2 -= 2, /*SL:214*/++v3) {}
        /*SL:217*/return v3;
    }
    
    public Type getComponent() {
        /*SL:227*/if (this.clazz == null || !this.clazz.isArray()) {
            /*SL:228*/return null;
        }
        CtClass v1;
        try {
            /*SL:232*/v1 = this.clazz.getComponentType();
        }
        catch (NotFoundException v2) {
            /*SL:234*/throw new RuntimeException(v2);
        }
        final Type v3 = Type.prims.get(/*EL:237*/v1);
        /*SL:238*/return (v3 != null) ? v3 : new Type(v1);
    }
    
    public boolean isAssignableFrom(final Type v2) {
        /*SL:250*/if (this == v2) {
            /*SL:251*/return true;
        }
        /*SL:253*/if ((v2 == Type.UNINIT && this.isReference()) || (this == Type.UNINIT && v2.isReference())) {
            /*SL:254*/return true;
        }
        /*SL:256*/if (v2 instanceof MultiType) {
            /*SL:257*/return ((MultiType)v2).isAssignableTo(this);
        }
        /*SL:259*/if (v2 instanceof MultiArrayType) {
            /*SL:260*/return ((MultiArrayType)v2).isAssignableTo(this);
        }
        /*SL:264*/if (this.clazz == null || this.clazz.isPrimitive()) {
            /*SL:265*/return false;
        }
        try {
            /*SL:268*/return v2.clazz.subtypeOf(this.clazz);
        }
        catch (Exception a1) {
            /*SL:270*/throw new RuntimeException(a1);
        }
    }
    
    public Type merge(final Type v2) {
        /*SL:286*/if (v2 == this) {
            /*SL:287*/return this;
        }
        /*SL:288*/if (v2 == null) {
            /*SL:289*/return this;
        }
        /*SL:290*/if (v2 == Type.UNINIT) {
            /*SL:291*/return this;
        }
        /*SL:292*/if (this == Type.UNINIT) {
            /*SL:293*/return v2;
        }
        /*SL:296*/if (!v2.isReference() || !this.isReference()) {
            /*SL:297*/return Type.BOGUS;
        }
        /*SL:300*/if (v2 instanceof MultiType) {
            /*SL:301*/return v2.merge(this);
        }
        /*SL:303*/if (v2.isArray() && this.isArray()) {
            /*SL:304*/return this.mergeArray(v2);
        }
        try {
            /*SL:307*/return this.mergeClasses(v2);
        }
        catch (NotFoundException a1) {
            /*SL:309*/throw new RuntimeException(a1);
        }
    }
    
    Type getRootComponent(Type a1) {
        /*SL:314*/while (a1.isArray()) {
            /*SL:315*/a1 = a1.getComponent();
        }
        /*SL:317*/return a1;
    }
    
    private Type createArray(final Type v2, final int v3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v2 */
        //     1: instanceof      Ljavassist/bytecode/analysis/MultiType;
        //     4: ifeq            20
        //     7: new             Ljavassist/bytecode/analysis/MultiArrayType;
        //    10: dup            
        //    11: aload_1         /* v2 */
        //    12: checkcast       Ljavassist/bytecode/analysis/MultiType;
        //    15: iload_2         /* v3 */
        //    16: invokespecial   javassist/bytecode/analysis/MultiArrayType.<init>:(Ljavassist/bytecode/analysis/MultiType;I)V
        //    19: areturn        
        //    20: aload_0         /* v1 */
        //    21: aload_1         /* v2 */
        //    22: getfield        javassist/bytecode/analysis/Type.clazz:Ljavassist/CtClass;
        //    25: invokevirtual   javassist/CtClass.getName:()Ljava/lang/String;
        //    28: iload_2         /* v3 */
        //    29: invokevirtual   javassist/bytecode/analysis/Type.arrayName:(Ljava/lang/String;I)Ljava/lang/String;
        //    32: astore_3        /* v4 */
        //    33: aload_0         /* v1 */
        //    34: aload_1         /* v2 */
        //    35: invokespecial   javassist/bytecode/analysis/Type.getClassPool:(Ljavassist/bytecode/analysis/Type;)Ljavassist/ClassPool;
        //    38: aload_3         /* v4 */
        //    39: invokevirtual   javassist/ClassPool.get:(Ljava/lang/String;)Ljavassist/CtClass;
        //    42: invokestatic    javassist/bytecode/analysis/Type.get:(Ljavassist/CtClass;)Ljavassist/bytecode/analysis/Type;
        //    45: astore          a1
        //    47: goto            62
        //    50: astore          a2
        //    52: new             Ljava/lang/RuntimeException;
        //    55: dup            
        //    56: aload           a2
        //    58: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    61: athrow         
        //    62: aload           v5
        //    64: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------
        //  47     3       4     a1    Ljavassist/bytecode/analysis/Type;
        //  52     10      5     a2    Ljavassist/NotFoundException;
        //  0      65      0     v1    Ljavassist/bytecode/analysis/Type;
        //  0      65      1     v2    Ljavassist/bytecode/analysis/Type;
        //  0      65      2     v3    I
        //  33     32      3     v4    Ljava/lang/String;
        //  62     3       4     v5    Ljavassist/bytecode/analysis/Type;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  33     47     50     62     Ljavassist/NotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    String arrayName(String a1, final int a2) {
        int v1 = /*EL:339*/a1.length();
        final int v2 = /*EL:340*/v1 + a2 * 2;
        final char[] v3 = /*EL:341*/new char[v2];
        /*SL:342*/a1.getChars(0, v1, v3, 0);
        /*SL:343*/while (v1 < v2) {
            /*SL:344*/v3[v1++] = '[';
            /*SL:345*/v3[v1++] = ']';
        }
        /*SL:347*/a1 = new String(v3);
        /*SL:348*/return a1;
    }
    
    private ClassPool getClassPool(final Type a1) {
        final ClassPool v1 = /*EL:352*/a1.clazz.getClassPool();
        /*SL:353*/return (v1 != null) ? v1 : ClassPool.getDefault();
    }
    
    private Type mergeArray(final Type v-4) {
        final Type rootComponent = /*EL:357*/this.getRootComponent(v-4);
        final Type rootComponent2 = /*EL:358*/this.getRootComponent(this);
        final int dimensions = /*EL:359*/v-4.getDimensions();
        final int v0 = /*EL:360*/this.getDimensions();
        /*SL:363*/if (dimensions == v0) {
            final Type a1 = /*EL:364*/rootComponent2.merge(rootComponent);
            /*SL:368*/if (a1 == Type.BOGUS) {
                /*SL:369*/return Type.OBJECT;
            }
            /*SL:371*/return this.createArray(a1, v0);
        }
        else {
            Type v;
            int v2;
            /*SL:377*/if (dimensions < v0) {
                /*SL:378*/v = rootComponent;
                /*SL:379*/v2 = dimensions;
            }
            else {
                /*SL:381*/v = rootComponent2;
                /*SL:382*/v2 = v0;
            }
            /*SL:386*/if (eq(Type.CLONEABLE.clazz, v.clazz) || eq(Type.SERIALIZABLE.clazz, v.clazz)) {
                /*SL:387*/return this.createArray(v, v2);
            }
            /*SL:389*/return this.createArray(Type.OBJECT, v2);
        }
    }
    
    private static CtClass findCommonSuperClass(final CtClass v1, final CtClass v2) throws NotFoundException {
        CtClass v3 = /*EL:393*/v1;
        CtClass v5;
        CtClass v4 = /*EL:395*/v5 = v2;
        CtClass v6 = /*EL:396*/v3;
        /*SL:401*/while (!eq(v3, v4) || v3.getSuperclass() == null) {
            final CtClass a1 = /*EL:404*/v3.getSuperclass();
            final CtClass a2 = /*EL:405*/v4.getSuperclass();
            /*SL:407*/if (a2 == null) {
                /*SL:409*/v4 = v5;
            }
            else {
                /*SL:413*/if (a1 != null) {
                    /*SL:424*/v3 = a1;
                    /*SL:425*/v4 = a2;
                    /*SL:426*/continue;
                }
                v3 = v6;
                v6 = v5;
                v5 = v3;
                v3 = v4;
                v4 = v5;
            }
            while (true) {
                /*SL:430*/v3 = v3.getSuperclass();
                /*SL:431*/if (v3 == null) {
                    break;
                }
                /*SL:434*/v6 = v6.getSuperclass();
            }
            /*SL:441*/for (v3 = v6; !eq(v3, v4); /*SL:442*/v3 = v3.getSuperclass(), /*SL:443*/v4 = v4.getSuperclass()) {}
            /*SL:446*/return v3;
        }
        return v3;
    }
    
    private Type mergeClasses(final Type v2) throws NotFoundException {
        final CtClass v3 = findCommonSuperClass(/*EL:450*/this.clazz, v2.clazz);
        /*SL:453*/if (v3.getSuperclass() == null) {
            final Map a1 = /*EL:454*/this.findCommonInterfaces(v2);
            /*SL:455*/if (a1.size() == 1) {
                /*SL:456*/return new Type(a1.values().iterator().next());
            }
            /*SL:457*/if (a1.size() > 1) {
                /*SL:458*/return new MultiType(a1);
            }
            /*SL:461*/return new Type(v3);
        }
        else {
            final Map v4 = /*EL:465*/this.findExclusiveDeclaredInterfaces(v2, v3);
            /*SL:466*/if (v4.size() > 0) {
                /*SL:467*/return new MultiType(v4, new Type(v3));
            }
            /*SL:470*/return new Type(v3);
        }
    }
    
    private Map findCommonInterfaces(final Type a1) {
        final Map v1 = /*EL:474*/this.getAllInterfaces(a1.clazz, null);
        final Map v2 = /*EL:475*/this.getAllInterfaces(this.clazz, null);
        /*SL:477*/return this.findCommonInterfaces(v1, v2);
    }
    
    private Map findExclusiveDeclaredInterfaces(final Type v1, final CtClass v2) {
        final Map v3 = /*EL:481*/this.getDeclaredInterfaces(v1.clazz, null);
        final Map v4 = /*EL:482*/this.getDeclaredInterfaces(this.clazz, null);
        final Map v5 = /*EL:483*/this.getAllInterfaces(v2, null);
        /*SL:485*/for (final Object a1 : v5.keySet()) {
            /*SL:488*/v3.remove(a1);
            /*SL:489*/v4.remove(a1);
        }
        /*SL:492*/return this.findCommonInterfaces(v3, v4);
    }
    
    Map findCommonInterfaces(final Map v-4, final Map v-3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface java/util/Map.keySet:()Ljava/util/Set;
        //     6: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    11: astore_3        /* v-2 */
        //    12: aload_3         /* v-2 */
        //    13: invokeinterface java/util/Iterator.hasNext:()Z
        //    18: ifeq            45
        //    21: aload_1         /* v-4 */
        //    22: aload_3         /* v-2 */
        //    23: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    28: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    33: ifne            12
        //    36: aload_3         /* v-2 */
        //    37: invokeinterface java/util/Iterator.remove:()V
        //    42: goto            12
        //    45: new             Ljava/util/ArrayList;
        //    48: dup            
        //    49: aload_2         /* v-3 */
        //    50: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //    55: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //    58: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //    61: astore_3        /* v-2 */
        //    62: aload_3         /* v-2 */
        //    63: invokeinterface java/util/Iterator.hasNext:()Z
        //    68: ifeq            139
        //    71: aload_3         /* v-2 */
        //    72: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    77: checkcast       Ljavassist/CtClass;
        //    80: astore          v-1
        //    82: aload           v-1
        //    84: invokevirtual   javassist/CtClass.getInterfaces:()[Ljavassist/CtClass;
        //    87: astore          a1
        //    89: goto            104
        //    92: astore          a2
        //    94: new             Ljava/lang/RuntimeException;
        //    97: dup            
        //    98: aload           a2
        //   100: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   103: athrow         
        //   104: iconst_0       
        //   105: istore          v1
        //   107: iload           v1
        //   109: aload           v0
        //   111: arraylength    
        //   112: if_icmpge       136
        //   115: aload_2         /* v-3 */
        //   116: aload           v0
        //   118: iload           v1
        //   120: aaload         
        //   121: invokevirtual   javassist/CtClass.getName:()Ljava/lang/String;
        //   124: invokeinterface java/util/Map.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //   129: pop            
        //   130: iinc            v1, 1
        //   133: goto            107
        //   136: goto            62
        //   139: aload_2         /* v-3 */
        //   140: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------
        //  89     3       5     a1    [Ljavassist/CtClass;
        //  94     10      6     a2    Ljavassist/NotFoundException;
        //  107    29      6     v1    I
        //  82     54      4     v-1   Ljavassist/CtClass;
        //  104    32      5     v0    [Ljavassist/CtClass;
        //  0      141     0     v-5   Ljavassist/bytecode/analysis/Type;
        //  0      141     1     v-4   Ljava/util/Map;
        //  0      141     2     v-3   Ljava/util/Map;
        //  12     129     3     v-2   Ljava/util/Iterator;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  82     89     92     104    Ljavassist/NotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    Map getAllInterfaces(CtClass v-1, Map v0) {
        /*SL:524*/if (v0 == null) {
            /*SL:525*/v0 = new HashMap<String, CtClass>();
        }
        /*SL:527*/if (v-1.isInterface()) {
            /*SL:528*/v0.put(v-1.getName(), v-1);
        }
        do {
            try {
                final CtClass[] v = /*EL:531*/v-1.getInterfaces();
                /*SL:532*/for (CtClass a2 = (CtClass)0; a2 < v.length; ++a2) {
                    /*SL:533*/a2 = v[a2];
                    /*SL:534*/v0.put(a2.getName(), a2);
                    /*SL:535*/this.getAllInterfaces(a2, v0);
                }
                /*SL:538*/v-1 = v-1.getSuperclass();
            }
            catch (NotFoundException v2) {
                /*SL:540*/throw new RuntimeException(v2);
            }
        } while (/*EL:542*/v-1 != null);
        /*SL:544*/return v0;
    }
    
    Map getDeclaredInterfaces(final CtClass v-3, final Map v-2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnonnull       12
        //     4: new             Ljava/util/HashMap;
        //     7: dup            
        //     8: invokespecial   java/util/HashMap.<init>:()V
        //    11: astore_2        /* v-2 */
        //    12: aload_1         /* v-3 */
        //    13: invokevirtual   javassist/CtClass.isInterface:()Z
        //    16: ifeq            31
        //    19: aload_2         /* v-2 */
        //    20: aload_1         /* v-3 */
        //    21: invokevirtual   javassist/CtClass.getName:()Ljava/lang/String;
        //    24: aload_1         /* v-3 */
        //    25: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    30: pop            
        //    31: aload_1         /* v-3 */
        //    32: invokevirtual   javassist/CtClass.getInterfaces:()[Ljavassist/CtClass;
        //    35: astore_3        /* a1 */
        //    36: goto            51
        //    39: astore          a2
        //    41: new             Ljava/lang/RuntimeException;
        //    44: dup            
        //    45: aload           a2
        //    47: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    50: athrow         
        //    51: iconst_0       
        //    52: istore          v0
        //    54: iload           v0
        //    56: aload_3         /* v-1 */
        //    57: arraylength    
        //    58: if_icmpge       95
        //    61: aload_3         /* v-1 */
        //    62: iload           v0
        //    64: aaload         
        //    65: astore          v1
        //    67: aload_2         /* v-2 */
        //    68: aload           v1
        //    70: invokevirtual   javassist/CtClass.getName:()Ljava/lang/String;
        //    73: aload           v1
        //    75: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    80: pop            
        //    81: aload_0         /* v-4 */
        //    82: aload           v1
        //    84: aload_2         /* v-2 */
        //    85: invokevirtual   javassist/bytecode/analysis/Type.getDeclaredInterfaces:(Ljavassist/CtClass;Ljava/util/Map;)Ljava/util/Map;
        //    88: pop            
        //    89: iinc            v0, 1
        //    92: goto            54
        //    95: aload_2         /* v-2 */
        //    96: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------
        //  36     3       3     a1    [Ljavassist/CtClass;
        //  41     10      4     a2    Ljavassist/NotFoundException;
        //  67     22      5     v1    Ljavassist/CtClass;
        //  54     41      4     v0    I
        //  0      97      0     v-4   Ljavassist/bytecode/analysis/Type;
        //  0      97      1     v-3   Ljavassist/CtClass;
        //  0      97      2     v-2   Ljava/util/Map;
        //  51     46      3     v-1   [Ljavassist/CtClass;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  31     36     39     51     Ljavassist/NotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:574*/return a1 instanceof Type && a1.getClass() == this.getClass() && eq(this.clazz, ((Type)a1).clazz);
    }
    
    static boolean eq(final CtClass a1, final CtClass a2) {
        /*SL:578*/return a1 == a2 || (a1 != null && a2 != null && a1.getName().equals(a2.getName()));
    }
    
    @Override
    public String toString() {
        /*SL:582*/if (this == Type.BOGUS) {
            /*SL:583*/return "BOGUS";
        }
        /*SL:584*/if (this == Type.UNINIT) {
            /*SL:585*/return "UNINIT";
        }
        /*SL:586*/if (this == Type.RETURN_ADDRESS) {
            /*SL:587*/return "RETURN ADDRESS";
        }
        /*SL:588*/if (this == Type.TOP) {
            /*SL:589*/return "TOP";
        }
        /*SL:591*/return (this.clazz == null) ? "null" : this.clazz.getName();
    }
    
    static {
        prims = new IdentityHashMap();
        DOUBLE = new Type(CtClass.doubleType);
        BOOLEAN = new Type(CtClass.booleanType);
        LONG = new Type(CtClass.longType);
        CHAR = new Type(CtClass.charType);
        BYTE = new Type(CtClass.byteType);
        SHORT = new Type(CtClass.shortType);
        INTEGER = new Type(CtClass.intType);
        FLOAT = new Type(CtClass.floatType);
        VOID = new Type(CtClass.voidType);
        UNINIT = new Type(null);
        RETURN_ADDRESS = new Type(null, true);
        TOP = new Type(null, true);
        BOGUS = new Type(null, true);
        OBJECT = lookupType("java.lang.Object");
        SERIALIZABLE = lookupType("java.io.Serializable");
        CLONEABLE = lookupType("java.lang.Cloneable");
        THROWABLE = lookupType("java.lang.Throwable");
        Type.prims.put(CtClass.doubleType, Type.DOUBLE);
        Type.prims.put(CtClass.longType, Type.LONG);
        Type.prims.put(CtClass.charType, Type.CHAR);
        Type.prims.put(CtClass.shortType, Type.SHORT);
        Type.prims.put(CtClass.intType, Type.INTEGER);
        Type.prims.put(CtClass.floatType, Type.FLOAT);
        Type.prims.put(CtClass.byteType, Type.BYTE);
        Type.prims.put(CtClass.booleanType, Type.BOOLEAN);
        Type.prims.put(CtClass.voidType, Type.VOID);
    }
}
