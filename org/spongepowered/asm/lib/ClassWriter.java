package org.spongepowered.asm.lib;

public class ClassWriter extends ClassVisitor
{
    public static final int COMPUTE_MAXS = 1;
    public static final int COMPUTE_FRAMES = 2;
    static final int ACC_SYNTHETIC_ATTRIBUTE = 262144;
    static final int TO_ACC_SYNTHETIC = 64;
    static final int NOARG_INSN = 0;
    static final int SBYTE_INSN = 1;
    static final int SHORT_INSN = 2;
    static final int VAR_INSN = 3;
    static final int IMPLVAR_INSN = 4;
    static final int TYPE_INSN = 5;
    static final int FIELDORMETH_INSN = 6;
    static final int ITFMETH_INSN = 7;
    static final int INDYMETH_INSN = 8;
    static final int LABEL_INSN = 9;
    static final int LABELW_INSN = 10;
    static final int LDC_INSN = 11;
    static final int LDCW_INSN = 12;
    static final int IINC_INSN = 13;
    static final int TABL_INSN = 14;
    static final int LOOK_INSN = 15;
    static final int MANA_INSN = 16;
    static final int WIDE_INSN = 17;
    static final int ASM_LABEL_INSN = 18;
    static final int F_INSERT = 256;
    static final byte[] TYPE;
    static final int CLASS = 7;
    static final int FIELD = 9;
    static final int METH = 10;
    static final int IMETH = 11;
    static final int STR = 8;
    static final int INT = 3;
    static final int FLOAT = 4;
    static final int LONG = 5;
    static final int DOUBLE = 6;
    static final int NAME_TYPE = 12;
    static final int UTF8 = 1;
    static final int MTYPE = 16;
    static final int HANDLE = 15;
    static final int INDY = 18;
    static final int HANDLE_BASE = 20;
    static final int TYPE_NORMAL = 30;
    static final int TYPE_UNINIT = 31;
    static final int TYPE_MERGED = 32;
    static final int BSM = 33;
    ClassReader cr;
    int version;
    int index;
    final ByteVector pool;
    Item[] items;
    int threshold;
    final Item key;
    final Item key2;
    final Item key3;
    final Item key4;
    Item[] typeTable;
    private short typeCount;
    private int access;
    private int name;
    String thisName;
    private int signature;
    private int superName;
    private int interfaceCount;
    private int[] interfaces;
    private int sourceFile;
    private ByteVector sourceDebug;
    private int enclosingMethodOwner;
    private int enclosingMethod;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private Attribute attrs;
    private int innerClassesCount;
    private ByteVector innerClasses;
    int bootstrapMethodsCount;
    ByteVector bootstrapMethods;
    FieldWriter firstField;
    FieldWriter lastField;
    MethodWriter firstMethod;
    MethodWriter lastMethod;
    private int compute;
    boolean hasAsmInsns;
    
    public ClassWriter(final int a1) {
        super(327680);
        this.index = 1;
        this.pool = new ByteVector();
        this.items = new Item[256];
        this.threshold = (int)(0.75 * this.items.length);
        this.key = new Item();
        this.key2 = new Item();
        this.key3 = new Item();
        this.key4 = new Item();
        this.compute = (((a1 & 0x2) != 0x0) ? 0 : (((a1 & 0x1) != 0x0) ? 2 : 3));
    }
    
    public ClassWriter(final ClassReader a1, final int a2) {
        this(a2);
        a1.copyPool(this);
        this.cr = a1;
    }
    
    public final void visit(final int a3, final int a4, final String a5, final String a6, final String v1, final String[] v2) {
        /*SL:678*/this.version = a3;
        /*SL:679*/this.access = a4;
        /*SL:680*/this.name = this.newClass(a5);
        /*SL:681*/this.thisName = a5;
        /*SL:682*/if (a6 != null) {
            /*SL:683*/this.signature = this.newUTF8(a6);
        }
        /*SL:685*/this.superName = ((v1 == null) ? 0 : this.newClass(v1));
        /*SL:686*/if (v2 != null && v2.length > 0) {
            /*SL:687*/this.interfaceCount = v2.length;
            /*SL:688*/this.interfaces = new int[this.interfaceCount];
            /*SL:689*/for (int a7 = 0; a7 < this.interfaceCount; ++a7) {
                /*SL:690*/this.interfaces[a7] = this.newClass(v2[a7]);
            }
        }
    }
    
    public final void visitSource(final String a1, final String a2) {
        /*SL:697*/if (a1 != null) {
            /*SL:698*/this.sourceFile = this.newUTF8(a1);
        }
        /*SL:700*/if (a2 != null) {
            /*SL:701*/this.sourceDebug = new ByteVector().encodeUTF8(a2, 0, Integer.MAX_VALUE);
        }
    }
    
    public final void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:709*/this.enclosingMethodOwner = this.newClass(a1);
        /*SL:710*/if (a2 != null && a3 != null) {
            /*SL:711*/this.enclosingMethod = this.newNameType(a2, a3);
        }
    }
    
    public final AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final ByteVector v1 = /*EL:721*/new ByteVector();
        /*SL:723*/v1.putShort(this.newUTF8(a1)).putShort(0);
        final AnnotationWriter v2 = /*EL:724*/new AnnotationWriter(this, true, v1, v1, 2);
        /*SL:725*/if (a2) {
            /*SL:726*/v2.next = this.anns;
            /*SL:727*/this.anns = v2;
        }
        else {
            /*SL:729*/v2.next = this.ianns;
            /*SL:730*/this.ianns = v2;
        }
        /*SL:732*/return v2;
    }
    
    public final AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final ByteVector v1 = /*EL:741*/new ByteVector();
        /*SL:743*/AnnotationWriter.putTarget(a1, a2, v1);
        /*SL:745*/v1.putShort(this.newUTF8(a3)).putShort(0);
        final AnnotationWriter v2 = /*EL:746*/new AnnotationWriter(this, true, v1, v1, v1.length - 2);
        /*SL:748*/if (a4) {
            /*SL:749*/v2.next = this.tanns;
            /*SL:750*/this.tanns = v2;
        }
        else {
            /*SL:752*/v2.next = this.itanns;
            /*SL:753*/this.itanns = v2;
        }
        /*SL:755*/return v2;
    }
    
    public final void visitAttribute(final Attribute a1) {
        /*SL:760*/a1.next = this.attrs;
        /*SL:761*/this.attrs = a1;
    }
    
    public final void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        /*SL:767*/if (this.innerClasses == null) {
            /*SL:768*/this.innerClasses = new ByteVector();
        }
        final Item v1 = /*EL:780*/this.newClassItem(a1);
        /*SL:781*/if (v1.intVal == 0) {
            /*SL:782*/++this.innerClassesCount;
            /*SL:783*/this.innerClasses.putShort(v1.index);
            /*SL:784*/this.innerClasses.putShort((a2 == null) ? 0 : this.newClass(a2));
            /*SL:785*/this.innerClasses.putShort((a3 == null) ? 0 : this.newUTF8(a3));
            /*SL:786*/this.innerClasses.putShort(a4);
            /*SL:787*/v1.intVal = this.innerClassesCount;
        }
    }
    
    public final FieldVisitor visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        /*SL:798*/return new FieldWriter(this, a1, a2, a3, a4, a5);
    }
    
    public final MethodVisitor visitMethod(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
        /*SL:804*/return new MethodWriter(this, a1, a2, a3, a4, a5, this.compute);
    }
    
    public final void visitEnd() {
    }
    
    public byte[] toByteArray() {
        /*SL:822*/if (this.index > 65535) {
            /*SL:823*/throw new RuntimeException("Class file too large!");
        }
        int a1 = /*EL:826*/24 + 2 * this.interfaceCount;
        int a2 = /*EL:827*/0;
        /*SL:829*/for (FieldWriter fieldWriter = this.firstField; fieldWriter != null; /*SL:832*/fieldWriter = (FieldWriter)fieldWriter.fv) {
            ++a2;
            a1 += fieldWriter.getSize();
        }
        int a3 = /*EL:834*/0;
        /*SL:836*/for (MethodWriter methodWriter = this.firstMethod; methodWriter != null; /*SL:839*/methodWriter = (MethodWriter)methodWriter.mv) {
            ++a3;
            a1 += methodWriter.getSize();
        }
        int a4 = /*EL:841*/0;
        /*SL:842*/if (this.bootstrapMethods != null) {
            /*SL:845*/++a4;
            /*SL:846*/a1 += 8 + this.bootstrapMethods.length;
            /*SL:847*/this.newUTF8("BootstrapMethods");
        }
        /*SL:849*/if (this.signature != 0) {
            /*SL:850*/++a4;
            /*SL:851*/a1 += 8;
            /*SL:852*/this.newUTF8("Signature");
        }
        /*SL:854*/if (this.sourceFile != 0) {
            /*SL:855*/++a4;
            /*SL:856*/a1 += 8;
            /*SL:857*/this.newUTF8("SourceFile");
        }
        /*SL:859*/if (this.sourceDebug != null) {
            /*SL:860*/++a4;
            /*SL:861*/a1 += this.sourceDebug.length + 6;
            /*SL:862*/this.newUTF8("SourceDebugExtension");
        }
        /*SL:864*/if (this.enclosingMethodOwner != 0) {
            /*SL:865*/++a4;
            /*SL:866*/a1 += 10;
            /*SL:867*/this.newUTF8("EnclosingMethod");
        }
        /*SL:869*/if ((this.access & 0x20000) != 0x0) {
            /*SL:870*/++a4;
            /*SL:871*/a1 += 6;
            /*SL:872*/this.newUTF8("Deprecated");
        }
        /*SL:874*/if ((this.access & 0x1000) != 0x0 && /*EL:875*/((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:877*/++a4;
            /*SL:878*/a1 += 6;
            /*SL:879*/this.newUTF8("Synthetic");
        }
        /*SL:882*/if (this.innerClasses != null) {
            /*SL:883*/++a4;
            /*SL:884*/a1 += 8 + this.innerClasses.length;
            /*SL:885*/this.newUTF8("InnerClasses");
        }
        /*SL:887*/if (this.anns != null) {
            /*SL:888*/++a4;
            /*SL:889*/a1 += 8 + this.anns.getSize();
            /*SL:890*/this.newUTF8("RuntimeVisibleAnnotations");
        }
        /*SL:892*/if (this.ianns != null) {
            /*SL:893*/++a4;
            /*SL:894*/a1 += 8 + this.ianns.getSize();
            /*SL:895*/this.newUTF8("RuntimeInvisibleAnnotations");
        }
        /*SL:897*/if (this.tanns != null) {
            /*SL:898*/++a4;
            /*SL:899*/a1 += 8 + this.tanns.getSize();
            /*SL:900*/this.newUTF8("RuntimeVisibleTypeAnnotations");
        }
        /*SL:902*/if (this.itanns != null) {
            /*SL:903*/++a4;
            /*SL:904*/a1 += 8 + this.itanns.getSize();
            /*SL:905*/this.newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        /*SL:907*/if (this.attrs != null) {
            /*SL:908*/a4 += this.attrs.getCount();
            /*SL:909*/a1 += this.attrs.getSize(this, null, 0, -1, -1);
        }
        /*SL:911*/a1 += this.pool.length;
        final ByteVector v2 = /*EL:914*/new ByteVector(a1);
        /*SL:915*/v2.putInt(-889275714).putInt(this.version);
        /*SL:916*/v2.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
        final int v0 = /*EL:917*/0x60000 | (this.access & 0x40000) / 64;
        /*SL:919*/v2.putShort(this.access & ~v0).putShort(this.name).putShort(this.superName);
        /*SL:920*/v2.putShort(this.interfaceCount);
        /*SL:921*/for (int v = 0; v < this.interfaceCount; ++v) {
            /*SL:922*/v2.putShort(this.interfaces[v]);
        }
        /*SL:924*/v2.putShort(a2);
        /*SL:926*/for (FieldWriter fieldWriter = this.firstField; fieldWriter != null; /*SL:928*/fieldWriter = (FieldWriter)fieldWriter.fv) {
            fieldWriter.put(v2);
        }
        /*SL:930*/v2.putShort(a3);
        /*SL:932*/for (MethodWriter methodWriter = this.firstMethod; methodWriter != null; /*SL:934*/methodWriter = (MethodWriter)methodWriter.mv) {
            methodWriter.put(v2);
        }
        /*SL:936*/v2.putShort(a4);
        /*SL:937*/if (this.bootstrapMethods != null) {
            /*SL:938*/v2.putShort(this.newUTF8("BootstrapMethods"));
            /*SL:939*/v2.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
            /*SL:941*/v2.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
        }
        /*SL:943*/if (this.signature != 0) {
            /*SL:944*/v2.putShort(this.newUTF8("Signature")).putInt(2).putShort(this.signature);
        }
        /*SL:946*/if (this.sourceFile != 0) {
            /*SL:947*/v2.putShort(this.newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
        }
        /*SL:949*/if (this.sourceDebug != null) {
            final int v = /*EL:950*/this.sourceDebug.length;
            /*SL:951*/v2.putShort(this.newUTF8("SourceDebugExtension")).putInt(v);
            /*SL:952*/v2.putByteArray(this.sourceDebug.data, 0, v);
        }
        /*SL:954*/if (this.enclosingMethodOwner != 0) {
            /*SL:955*/v2.putShort(this.newUTF8("EnclosingMethod")).putInt(4);
            /*SL:956*/v2.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
        }
        /*SL:958*/if ((this.access & 0x20000) != 0x0) {
            /*SL:959*/v2.putShort(this.newUTF8("Deprecated")).putInt(0);
        }
        /*SL:961*/if ((this.access & 0x1000) != 0x0 && /*EL:962*/((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:964*/v2.putShort(this.newUTF8("Synthetic")).putInt(0);
        }
        /*SL:967*/if (this.innerClasses != null) {
            /*SL:968*/v2.putShort(this.newUTF8("InnerClasses"));
            /*SL:969*/v2.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
            /*SL:970*/v2.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        /*SL:972*/if (this.anns != null) {
            /*SL:973*/v2.putShort(this.newUTF8("RuntimeVisibleAnnotations"));
            /*SL:974*/this.anns.put(v2);
        }
        /*SL:976*/if (this.ianns != null) {
            /*SL:977*/v2.putShort(this.newUTF8("RuntimeInvisibleAnnotations"));
            /*SL:978*/this.ianns.put(v2);
        }
        /*SL:980*/if (this.tanns != null) {
            /*SL:981*/v2.putShort(this.newUTF8("RuntimeVisibleTypeAnnotations"));
            /*SL:982*/this.tanns.put(v2);
        }
        /*SL:984*/if (this.itanns != null) {
            /*SL:985*/v2.putShort(this.newUTF8("RuntimeInvisibleTypeAnnotations"));
            /*SL:986*/this.itanns.put(v2);
        }
        /*SL:988*/if (this.attrs != null) {
            /*SL:989*/this.attrs.put(this, null, 0, -1, -1, v2);
        }
        /*SL:991*/if (this.hasAsmInsns) {
            /*SL:992*/this.anns = null;
            /*SL:993*/this.ianns = null;
            /*SL:994*/this.attrs = null;
            /*SL:995*/this.innerClassesCount = 0;
            /*SL:996*/this.innerClasses = null;
            /*SL:997*/this.firstField = null;
            /*SL:998*/this.lastField = null;
            /*SL:999*/this.firstMethod = null;
            /*SL:1000*/this.lastMethod = null;
            /*SL:1001*/this.compute = 1;
            /*SL:1002*/this.hasAsmInsns = false;
            /*SL:1003*/new ClassReader(v2.data).accept(this, 264);
            /*SL:1005*/return this.toByteArray();
        }
        /*SL:1007*/return v2.data;
    }
    
    Item newConstItem(final Object v0) {
        /*SL:1026*/if (v0 instanceof Integer) {
            final int a1 = /*EL:1027*/(int)v0;
            /*SL:1028*/return this.newInteger(a1);
        }
        /*SL:1029*/if (v0 instanceof Byte) {
            final int v = /*EL:1030*/(int)v0;
            /*SL:1031*/return this.newInteger(v);
        }
        /*SL:1032*/if (v0 instanceof Character) {
            final int v = /*EL:1033*/(char)v0;
            /*SL:1034*/return this.newInteger(v);
        }
        /*SL:1035*/if (v0 instanceof Short) {
            final int v = /*EL:1036*/(int)v0;
            /*SL:1037*/return this.newInteger(v);
        }
        /*SL:1038*/if (v0 instanceof Boolean) {
            final int v = /*EL:1039*/((boolean)v0) ? 1 : 0;
            /*SL:1040*/return this.newInteger(v);
        }
        /*SL:1041*/if (v0 instanceof Float) {
            final float v2 = /*EL:1042*/(float)v0;
            /*SL:1043*/return this.newFloat(v2);
        }
        /*SL:1044*/if (v0 instanceof Long) {
            final long v3 = /*EL:1045*/(long)v0;
            /*SL:1046*/return this.newLong(v3);
        }
        /*SL:1047*/if (v0 instanceof Double) {
            final double v4 = /*EL:1048*/(double)v0;
            /*SL:1049*/return this.newDouble(v4);
        }
        /*SL:1050*/if (v0 instanceof String) {
            /*SL:1051*/return this.newString((String)v0);
        }
        /*SL:1052*/if (v0 instanceof Type) {
            final Type v5 = /*EL:1053*/(Type)v0;
            final int v6 = /*EL:1054*/v5.getSort();
            /*SL:1055*/if (v6 == 10) {
                /*SL:1056*/return this.newClassItem(v5.getInternalName());
            }
            /*SL:1057*/if (v6 == 11) {
                /*SL:1058*/return this.newMethodTypeItem(v5.getDescriptor());
            }
            /*SL:1060*/return this.newClassItem(v5.getDescriptor());
        }
        else {
            /*SL:1062*/if (v0 instanceof Handle) {
                final Handle v7 = /*EL:1063*/(Handle)v0;
                /*SL:1064*/return this.newHandleItem(v7.tag, v7.owner, v7.name, v7.desc, v7.itf);
            }
            /*SL:1066*/throw new IllegalArgumentException("value " + v0);
        }
    }
    
    public int newConst(final Object a1) {
        /*SL:1084*/return this.newConstItem(a1).index;
    }
    
    public int newUTF8(final String a1) {
        /*SL:1098*/this.key.set(1, a1, null, null);
        Item v1 = /*EL:1099*/this.get(this.key);
        /*SL:1100*/if (v1 == null) {
            /*SL:1101*/this.pool.putByte(1).putUTF8(a1);
            /*SL:1102*/v1 = new Item(this.index++, this.key);
            /*SL:1103*/this.put(v1);
        }
        /*SL:1105*/return v1.index;
    }
    
    Item newClassItem(final String a1) {
        /*SL:1119*/this.key2.set(7, a1, null, null);
        Item v1 = /*EL:1120*/this.get(this.key2);
        /*SL:1121*/if (v1 == null) {
            /*SL:1122*/this.pool.put12(7, this.newUTF8(a1));
            /*SL:1123*/v1 = new Item(this.index++, this.key2);
            /*SL:1124*/this.put(v1);
        }
        /*SL:1126*/return v1;
    }
    
    public int newClass(final String a1) {
        /*SL:1140*/return this.newClassItem(a1).index;
    }
    
    Item newMethodTypeItem(final String a1) {
        /*SL:1154*/this.key2.set(16, a1, null, null);
        Item v1 = /*EL:1155*/this.get(this.key2);
        /*SL:1156*/if (v1 == null) {
            /*SL:1157*/this.pool.put12(16, this.newUTF8(a1));
            /*SL:1158*/v1 = new Item(this.index++, this.key2);
            /*SL:1159*/this.put(v1);
        }
        /*SL:1161*/return v1;
    }
    
    public int newMethodType(final String a1) {
        /*SL:1176*/return this.newMethodTypeItem(a1).index;
    }
    
    Item newHandleItem(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:1205*/this.key4.set(20 + a1, a2, a3, a4);
        Item v1 = /*EL:1206*/this.get(this.key4);
        /*SL:1207*/if (v1 == null) {
            /*SL:1208*/if (a1 <= 4) {
                /*SL:1209*/this.put112(15, a1, this.newField(a2, a3, a4));
            }
            else {
                /*SL:1211*/this.put112(15, a1, this.newMethod(a2, a3, a4, a5));
            }
            /*SL:1215*/v1 = new Item(this.index++, this.key4);
            /*SL:1216*/this.put(v1);
        }
        /*SL:1218*/return v1;
    }
    
    @Deprecated
    public int newHandle(final int a1, final String a2, final String a3, final String a4) {
        /*SL:1250*/return this.newHandle(a1, a2, a3, a4, a1 == 9);
    }
    
    public int newHandle(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:1280*/return this.newHandleItem(a1, a2, a3, a4, a5).index;
    }
    
    Item newInvokeDynamicItem(final String v-10, final String v-9, final Handle v-8, final Object... v-7) {
        ByteVector bootstrapMethods = /*EL:1303*/this.bootstrapMethods;
        /*SL:1304*/if (bootstrapMethods == null) {
            final ByteVector bootstrapMethods2 = /*EL:1305*/new ByteVector();
            this.bootstrapMethods = bootstrapMethods2;
            bootstrapMethods = bootstrapMethods2;
        }
        final int length = /*EL:1308*/bootstrapMethods.length;
        int hashCode = /*EL:1310*/v-8.hashCode();
        /*SL:1311*/bootstrapMethods.putShort(this.newHandle(v-8.tag, v-8.owner, v-8.name, v-8.desc, v-8.isInterface()));
        final int length2 = /*EL:1314*/v-7.length;
        /*SL:1315*/bootstrapMethods.putShort(length2);
        /*SL:1317*/for (Object a2 = 0; a2 < length2; ++a2) {
            /*SL:1318*/a2 = v-7[a2];
            /*SL:1319*/hashCode ^= a2.hashCode();
            /*SL:1320*/bootstrapMethods.putShort(this.newConst(a2));
        }
        final byte[] data = /*EL:1323*/bootstrapMethods.data;
        final int n = /*EL:1324*/2 + length2 << 1;
        /*SL:1325*/hashCode &= Integer.MAX_VALUE;
        Item v0 = /*EL:1326*/this.items[hashCode % this.items.length];
        /*SL:1327*/Label_0163:
        while (v0 != null) {
            /*SL:1328*/if (v0.type == 33 && v0.hashCode == hashCode) {
                final int a3 = /*EL:1335*/v0.intVal;
                /*SL:1336*/for (int a4 = 0; a4 < n; ++a4) {
                    /*SL:1337*/if (data[length + a4] != data[a3 + a4]) {
                        /*SL:1338*/v0 = v0.next;
                        /*SL:1339*/continue Label_0163;
                    }
                }
                break;
            }
            v0 = v0.next;
        }
        int v;
        /*SL:1346*/if (v0 != null) {
            /*SL:1347*/v = v0.index;
            /*SL:1348*/bootstrapMethods.length = length;
        }
        else {
            /*SL:1350*/v = this.bootstrapMethodsCount++;
            /*SL:1351*/v0 = new Item(v);
            /*SL:1352*/v0.set(length, hashCode);
            /*SL:1353*/this.put(v0);
        }
        /*SL:1357*/this.key3.set(v-10, v-9, v);
        /*SL:1358*/v0 = this.get(this.key3);
        /*SL:1359*/if (v0 == null) {
            /*SL:1360*/this.put122(18, v, this.newNameType(v-10, v-9));
            /*SL:1361*/v0 = new Item(this.index++, this.key3);
            /*SL:1362*/this.put(v0);
        }
        /*SL:1364*/return v0;
    }
    
    public int newInvokeDynamic(final String a1, final String a2, final Handle a3, final Object... a4) {
        /*SL:1387*/return this.newInvokeDynamicItem(a1, a2, a3, a4).index;
    }
    
    Item newFieldItem(final String a1, final String a2, final String a3) {
        /*SL:1403*/this.key3.set(9, a1, a2, a3);
        Item v1 = /*EL:1404*/this.get(this.key3);
        /*SL:1405*/if (v1 == null) {
            /*SL:1406*/this.put122(9, this.newClass(a1), this.newNameType(a2, a3));
            /*SL:1407*/v1 = new Item(this.index++, this.key3);
            /*SL:1408*/this.put(v1);
        }
        /*SL:1410*/return v1;
    }
    
    public int newField(final String a1, final String a2, final String a3) {
        /*SL:1428*/return this.newFieldItem(a1, a2, a3).index;
    }
    
    Item newMethodItem(final String a1, final String a2, final String a3, final boolean a4) {
        final int v1 = /*EL:1447*/a4 ? 11 : 10;
        /*SL:1448*/this.key3.set(v1, a1, a2, a3);
        Item v2 = /*EL:1449*/this.get(this.key3);
        /*SL:1450*/if (v2 == null) {
            /*SL:1451*/this.put122(v1, this.newClass(a1), this.newNameType(a2, a3));
            /*SL:1452*/v2 = new Item(this.index++, this.key3);
            /*SL:1453*/this.put(v2);
        }
        /*SL:1455*/return v2;
    }
    
    public int newMethod(final String a1, final String a2, final String a3, final boolean a4) {
        /*SL:1476*/return this.newMethodItem(a1, a2, a3, a4).index;
    }
    
    Item newInteger(final int a1) {
        /*SL:1488*/this.key.set(a1);
        Item v1 = /*EL:1489*/this.get(this.key);
        /*SL:1490*/if (v1 == null) {
            /*SL:1491*/this.pool.putByte(3).putInt(a1);
            /*SL:1492*/v1 = new Item(this.index++, this.key);
            /*SL:1493*/this.put(v1);
        }
        /*SL:1495*/return v1;
    }
    
    Item newFloat(final float a1) {
        /*SL:1507*/this.key.set(a1);
        Item v1 = /*EL:1508*/this.get(this.key);
        /*SL:1509*/if (v1 == null) {
            /*SL:1510*/this.pool.putByte(4).putInt(this.key.intVal);
            /*SL:1511*/v1 = new Item(this.index++, this.key);
            /*SL:1512*/this.put(v1);
        }
        /*SL:1514*/return v1;
    }
    
    Item newLong(final long a1) {
        /*SL:1526*/this.key.set(a1);
        Item v1 = /*EL:1527*/this.get(this.key);
        /*SL:1528*/if (v1 == null) {
            /*SL:1529*/this.pool.putByte(5).putLong(a1);
            /*SL:1530*/v1 = new Item(this.index, this.key);
            /*SL:1531*/this.index += 2;
            /*SL:1532*/this.put(v1);
        }
        /*SL:1534*/return v1;
    }
    
    Item newDouble(final double a1) {
        /*SL:1546*/this.key.set(a1);
        Item v1 = /*EL:1547*/this.get(this.key);
        /*SL:1548*/if (v1 == null) {
            /*SL:1549*/this.pool.putByte(6).putLong(this.key.longVal);
            /*SL:1550*/v1 = new Item(this.index, this.key);
            /*SL:1551*/this.index += 2;
            /*SL:1552*/this.put(v1);
        }
        /*SL:1554*/return v1;
    }
    
    private Item newString(final String a1) {
        /*SL:1566*/this.key2.set(8, a1, null, null);
        Item v1 = /*EL:1567*/this.get(this.key2);
        /*SL:1568*/if (v1 == null) {
            /*SL:1569*/this.pool.put12(8, this.newUTF8(a1));
            /*SL:1570*/v1 = new Item(this.index++, this.key2);
            /*SL:1571*/this.put(v1);
        }
        /*SL:1573*/return v1;
    }
    
    public int newNameType(final String a1, final String a2) {
        /*SL:1589*/return this.newNameTypeItem(a1, a2).index;
    }
    
    Item newNameTypeItem(final String a1, final String a2) {
        /*SL:1603*/this.key2.set(12, a1, a2, null);
        Item v1 = /*EL:1604*/this.get(this.key2);
        /*SL:1605*/if (v1 == null) {
            /*SL:1606*/this.put122(12, this.newUTF8(a1), this.newUTF8(a2));
            /*SL:1607*/v1 = new Item(this.index++, this.key2);
            /*SL:1608*/this.put(v1);
        }
        /*SL:1610*/return v1;
    }
    
    int addType(final String a1) {
        /*SL:1622*/this.key.set(30, a1, null, null);
        Item v1 = /*EL:1623*/this.get(this.key);
        /*SL:1624*/if (v1 == null) {
            /*SL:1625*/v1 = this.addType(this.key);
        }
        /*SL:1627*/return v1.index;
    }
    
    int addUninitializedType(final String a1, final int a2) {
        /*SL:1643*/this.key.type = 31;
        /*SL:1644*/this.key.intVal = a2;
        /*SL:1645*/this.key.strVal1 = a1;
        /*SL:1646*/this.key.hashCode = (Integer.MAX_VALUE & 31 + a1.hashCode() + a2);
        Item v1 = /*EL:1647*/this.get(this.key);
        /*SL:1648*/if (v1 == null) {
            /*SL:1649*/v1 = this.addType(this.key);
        }
        /*SL:1651*/return v1.index;
    }
    
    private Item addType(final Item v2) {
        /*SL:1663*/++this.typeCount;
        final Item v3 = /*EL:1664*/new Item(this.typeCount, this.key);
        /*SL:1665*/this.put(v3);
        /*SL:1666*/if (this.typeTable == null) {
            /*SL:1667*/this.typeTable = new Item[16];
        }
        /*SL:1669*/if (this.typeCount == this.typeTable.length) {
            final Item[] a1 = /*EL:1670*/new Item[2 * this.typeTable.length];
            /*SL:1671*/System.arraycopy(this.typeTable, 0, a1, 0, this.typeTable.length);
            /*SL:1672*/this.typeTable = a1;
        }
        /*SL:1675*/return this.typeTable[this.typeCount] = v3;
    }
    
    int getMergedType(final int v2, final int v3) {
        /*SL:1691*/this.key2.type = 32;
        /*SL:1692*/this.key2.longVal = (v2 | v3 << 32);
        /*SL:1693*/this.key2.hashCode = (Integer.MAX_VALUE & 32 + v2 + v3);
        Item v4 = /*EL:1694*/this.get(this.key2);
        /*SL:1695*/if (v4 == null) {
            final String a1 = /*EL:1696*/this.typeTable[v2].strVal1;
            final String a2 = /*EL:1697*/this.typeTable[v3].strVal1;
            /*SL:1698*/this.key2.intVal = this.addType(this.getCommonSuperClass(a1, a2));
            /*SL:1699*/v4 = new Item(0, this.key2);
            /*SL:1700*/this.put(v4);
        }
        /*SL:1702*/return v4.intVal;
    }
    
    protected String getCommonSuperClass(final String v-4, final String v-3) {
        final ClassLoader classLoader = /*EL:1723*/this.getClass().getClassLoader();
        Class<?> a1;
        Class<?> a2;
        try {
            /*SL:1725*/a1 = Class.forName(v-4.replace('/', '.'), false, classLoader);
            /*SL:1726*/a2 = Class.forName(v-3.replace('/', '.'), false, classLoader);
        }
        catch (Exception v1) {
            /*SL:1728*/throw new RuntimeException(v1.toString());
        }
        /*SL:1730*/if (a1.isAssignableFrom(a2)) {
            /*SL:1731*/return v-4;
        }
        /*SL:1733*/if (a2.isAssignableFrom(a1)) {
            /*SL:1734*/return v-3;
        }
        /*SL:1736*/if (a1.isInterface() || a2.isInterface()) {
            /*SL:1737*/return "java/lang/Object";
        }
        /*SL:1741*/do {
            a1 = a1.getSuperclass();
        } while (!a1.isAssignableFrom(a2));
        /*SL:1742*/return a1.getName().replace('.', '/');
    }
    
    private Item get(final Item a1) {
        Item v1;
        /*SL:1757*/for (v1 = this.items[a1.hashCode % this.items.length]; v1 != null && (v1.type != a1.type || !a1.isEqualTo(v1)); /*SL:1758*/v1 = v1.next) {}
        /*SL:1760*/return v1;
    }
    
    private void put(final Item v-6) {
        /*SL:1771*/if (this.index + this.typeCount > this.threshold) {
            final int length = /*EL:1772*/this.items.length;
            final int n = /*EL:1773*/length * 2 + 1;
            final Item[] items = /*EL:1774*/new Item[n];
            /*SL:1775*/for (int i = length - 1; i >= 0; --i) {
                Item v1;
                /*SL:1777*/for (Item item = this.items[i]; item != null; /*SL:1782*/item = v1) {
                    final int a1 = item.hashCode % items.length;
                    v1 = item.next;
                    item.next = items[a1];
                    items[a1] = item;
                }
            }
            /*SL:1785*/this.items = items;
            /*SL:1786*/this.threshold = (int)(n * 0.75);
        }
        final int length = /*EL:1788*/v-6.hashCode % this.items.length;
        /*SL:1789*/v-6.next = this.items[length];
        /*SL:1790*/this.items[length] = v-6;
    }
    
    private void put122(final int a1, final int a2, final int a3) {
        /*SL:1804*/this.pool.put12(a1, a2).putShort(a3);
    }
    
    private void put112(final int a1, final int a2, final int a3) {
        /*SL:1818*/this.pool.put11(a1, a2).putShort(a3);
    }
    
    static {
        final byte[] type = new byte[220];
        final String v0 = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKSSSSSSSSSSSSSSSSSS";
        for (int v = 0; v < type.length; ++v) {
            type[v] = (byte)(v0.charAt(v) - 'A');
        }
        TYPE = type;
    }
}
