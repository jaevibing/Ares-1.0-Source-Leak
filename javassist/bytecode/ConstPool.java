package javassist.bytecode;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import javassist.CtClass;
import java.util.HashMap;

public final class ConstPool
{
    LongVector items;
    int numOfItems;
    int thisClassInfo;
    HashMap itemsCache;
    public static final int CONST_Class = 7;
    public static final int CONST_Fieldref = 9;
    public static final int CONST_Methodref = 10;
    public static final int CONST_InterfaceMethodref = 11;
    public static final int CONST_String = 8;
    public static final int CONST_Integer = 3;
    public static final int CONST_Float = 4;
    public static final int CONST_Long = 5;
    public static final int CONST_Double = 6;
    public static final int CONST_NameAndType = 12;
    public static final int CONST_Utf8 = 1;
    public static final int CONST_MethodHandle = 15;
    public static final int CONST_MethodType = 16;
    public static final int CONST_InvokeDynamic = 18;
    public static final CtClass THIS;
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_invokeInterface = 9;
    
    public ConstPool(final String a1) {
        this.items = new LongVector();
        this.itemsCache = null;
        this.numOfItems = 0;
        this.addItem0(null);
        this.thisClassInfo = this.addClassInfo(a1);
    }
    
    public ConstPool(final DataInputStream a1) throws IOException {
        this.itemsCache = null;
        this.thisClassInfo = 0;
        this.read(a1);
    }
    
    void prune() {
        /*SL:189*/this.itemsCache = null;
    }
    
    public int getSize() {
        /*SL:196*/return this.numOfItems;
    }
    
    public String getClassName() {
        /*SL:203*/return this.getClassInfo(this.thisClassInfo);
    }
    
    public int getThisClassInfo() {
        /*SL:211*/return this.thisClassInfo;
    }
    
    void setThisClassInfo(final int a1) {
        /*SL:215*/this.thisClassInfo = a1;
    }
    
    ConstInfo getItem(final int a1) {
        /*SL:219*/return this.items.elementAt(a1);
    }
    
    public int getTag(final int a1) {
        /*SL:230*/return this.getItem(a1).getTag();
    }
    
    public String getClassInfo(final int a1) {
        final ClassInfo v1 = /*EL:245*/(ClassInfo)this.getItem(a1);
        /*SL:246*/if (v1 == null) {
            /*SL:247*/return null;
        }
        /*SL:249*/return Descriptor.toJavaName(this.getUtf8Info(v1.name));
    }
    
    public String getClassInfoByDescriptor(final int v2) {
        final ClassInfo v3 = /*EL:262*/(ClassInfo)this.getItem(v2);
        /*SL:263*/if (v3 == null) {
            /*SL:264*/return null;
        }
        final String a1 = /*EL:266*/this.getUtf8Info(v3.name);
        /*SL:267*/if (a1.charAt(0) == '[') {
            /*SL:268*/return a1;
        }
        /*SL:270*/return Descriptor.of(a1);
    }
    
    public int getNameAndTypeName(final int a1) {
        final NameAndTypeInfo v1 = /*EL:280*/(NameAndTypeInfo)this.getItem(a1);
        /*SL:281*/return v1.memberName;
    }
    
    public int getNameAndTypeDescriptor(final int a1) {
        final NameAndTypeInfo v1 = /*EL:290*/(NameAndTypeInfo)this.getItem(a1);
        /*SL:291*/return v1.typeDescriptor;
    }
    
    public int getMemberClass(final int a1) {
        final MemberrefInfo v1 = /*EL:304*/(MemberrefInfo)this.getItem(a1);
        /*SL:305*/return v1.classIndex;
    }
    
    public int getMemberNameAndType(final int a1) {
        final MemberrefInfo v1 = /*EL:318*/(MemberrefInfo)this.getItem(a1);
        /*SL:319*/return v1.nameAndTypeIndex;
    }
    
    public int getFieldrefClass(final int a1) {
        final FieldrefInfo v1 = /*EL:328*/(FieldrefInfo)this.getItem(a1);
        /*SL:329*/return v1.classIndex;
    }
    
    public String getFieldrefClassName(final int a1) {
        final FieldrefInfo v1 = /*EL:340*/(FieldrefInfo)this.getItem(a1);
        /*SL:341*/if (v1 == null) {
            /*SL:342*/return null;
        }
        /*SL:344*/return this.getClassInfo(v1.classIndex);
    }
    
    public int getFieldrefNameAndType(final int a1) {
        final FieldrefInfo v1 = /*EL:353*/(FieldrefInfo)this.getItem(a1);
        /*SL:354*/return v1.nameAndTypeIndex;
    }
    
    public String getFieldrefName(final int v2) {
        final FieldrefInfo v3 = /*EL:366*/(FieldrefInfo)this.getItem(v2);
        /*SL:367*/if (v3 == null) {
            /*SL:368*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:370*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:371*/if (a1 == null) {
            /*SL:372*/return null;
        }
        /*SL:374*/return this.getUtf8Info(a1.memberName);
    }
    
    public String getFieldrefType(final int v2) {
        final FieldrefInfo v3 = /*EL:387*/(FieldrefInfo)this.getItem(v2);
        /*SL:388*/if (v3 == null) {
            /*SL:389*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:391*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:392*/if (a1 == null) {
            /*SL:393*/return null;
        }
        /*SL:395*/return this.getUtf8Info(a1.typeDescriptor);
    }
    
    public int getMethodrefClass(final int a1) {
        final MemberrefInfo v1 = /*EL:405*/(MemberrefInfo)this.getItem(a1);
        /*SL:406*/return v1.classIndex;
    }
    
    public String getMethodrefClassName(final int a1) {
        final MemberrefInfo v1 = /*EL:417*/(MemberrefInfo)this.getItem(a1);
        /*SL:418*/if (v1 == null) {
            /*SL:419*/return null;
        }
        /*SL:421*/return this.getClassInfo(v1.classIndex);
    }
    
    public int getMethodrefNameAndType(final int a1) {
        final MemberrefInfo v1 = /*EL:430*/(MemberrefInfo)this.getItem(a1);
        /*SL:431*/return v1.nameAndTypeIndex;
    }
    
    public String getMethodrefName(final int v2) {
        final MemberrefInfo v3 = /*EL:443*/(MemberrefInfo)this.getItem(v2);
        /*SL:444*/if (v3 == null) {
            /*SL:445*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:447*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:449*/if (a1 == null) {
            /*SL:450*/return null;
        }
        /*SL:452*/return this.getUtf8Info(a1.memberName);
    }
    
    public String getMethodrefType(final int v2) {
        final MemberrefInfo v3 = /*EL:465*/(MemberrefInfo)this.getItem(v2);
        /*SL:466*/if (v3 == null) {
            /*SL:467*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:469*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:471*/if (a1 == null) {
            /*SL:472*/return null;
        }
        /*SL:474*/return this.getUtf8Info(a1.typeDescriptor);
    }
    
    public int getInterfaceMethodrefClass(final int a1) {
        final MemberrefInfo v1 = /*EL:484*/(MemberrefInfo)this.getItem(a1);
        /*SL:485*/return v1.classIndex;
    }
    
    public String getInterfaceMethodrefClassName(final int a1) {
        final MemberrefInfo v1 = /*EL:496*/(MemberrefInfo)this.getItem(a1);
        /*SL:497*/return this.getClassInfo(v1.classIndex);
    }
    
    public int getInterfaceMethodrefNameAndType(final int a1) {
        final MemberrefInfo v1 = /*EL:506*/(MemberrefInfo)this.getItem(a1);
        /*SL:507*/return v1.nameAndTypeIndex;
    }
    
    public String getInterfaceMethodrefName(final int v2) {
        final MemberrefInfo v3 = /*EL:520*/(MemberrefInfo)this.getItem(v2);
        /*SL:521*/if (v3 == null) {
            /*SL:522*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:524*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:526*/if (a1 == null) {
            /*SL:527*/return null;
        }
        /*SL:529*/return this.getUtf8Info(a1.memberName);
    }
    
    public String getInterfaceMethodrefType(final int v2) {
        final MemberrefInfo v3 = /*EL:543*/(MemberrefInfo)this.getItem(v2);
        /*SL:544*/if (v3 == null) {
            /*SL:545*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:547*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
        /*SL:549*/if (a1 == null) {
            /*SL:550*/return null;
        }
        /*SL:552*/return this.getUtf8Info(a1.typeDescriptor);
    }
    
    public Object getLdcValue(final int a1) {
        final ConstInfo v1 = /*EL:565*/this.getItem(a1);
        Object v2 = /*EL:566*/null;
        /*SL:567*/if (v1 instanceof StringInfo) {
            /*SL:568*/v2 = this.getStringInfo(a1);
        }
        else/*SL:569*/ if (v1 instanceof FloatInfo) {
            /*SL:570*/v2 = new Float(this.getFloatInfo(a1));
        }
        else/*SL:571*/ if (v1 instanceof IntegerInfo) {
            /*SL:572*/v2 = new Integer(this.getIntegerInfo(a1));
        }
        else/*SL:573*/ if (v1 instanceof LongInfo) {
            /*SL:574*/v2 = new Long(this.getLongInfo(a1));
        }
        else/*SL:575*/ if (v1 instanceof DoubleInfo) {
            /*SL:576*/v2 = new Double(this.getDoubleInfo(a1));
        }
        else {
            /*SL:578*/v2 = null;
        }
        /*SL:580*/return v2;
    }
    
    public int getIntegerInfo(final int a1) {
        final IntegerInfo v1 = /*EL:590*/(IntegerInfo)this.getItem(a1);
        /*SL:591*/return v1.value;
    }
    
    public float getFloatInfo(final int a1) {
        final FloatInfo v1 = /*EL:601*/(FloatInfo)this.getItem(a1);
        /*SL:602*/return v1.value;
    }
    
    public long getLongInfo(final int a1) {
        final LongInfo v1 = /*EL:612*/(LongInfo)this.getItem(a1);
        /*SL:613*/return v1.value;
    }
    
    public double getDoubleInfo(final int a1) {
        final DoubleInfo v1 = /*EL:623*/(DoubleInfo)this.getItem(a1);
        /*SL:624*/return v1.value;
    }
    
    public String getStringInfo(final int a1) {
        final StringInfo v1 = /*EL:634*/(StringInfo)this.getItem(a1);
        /*SL:635*/return this.getUtf8Info(v1.string);
    }
    
    public String getUtf8Info(final int a1) {
        final Utf8Info v1 = /*EL:645*/(Utf8Info)this.getItem(a1);
        /*SL:646*/return v1.string;
    }
    
    public int getMethodHandleKind(final int a1) {
        final MethodHandleInfo v1 = /*EL:666*/(MethodHandleInfo)this.getItem(a1);
        /*SL:667*/return v1.refKind;
    }
    
    public int getMethodHandleIndex(final int a1) {
        final MethodHandleInfo v1 = /*EL:678*/(MethodHandleInfo)this.getItem(a1);
        /*SL:679*/return v1.refIndex;
    }
    
    public int getMethodTypeInfo(final int a1) {
        final MethodTypeInfo v1 = /*EL:690*/(MethodTypeInfo)this.getItem(a1);
        /*SL:691*/return v1.descriptor;
    }
    
    public int getInvokeDynamicBootstrap(final int a1) {
        final InvokeDynamicInfo v1 = /*EL:702*/(InvokeDynamicInfo)this.getItem(a1);
        /*SL:703*/return v1.bootstrap;
    }
    
    public int getInvokeDynamicNameAndType(final int a1) {
        final InvokeDynamicInfo v1 = /*EL:714*/(InvokeDynamicInfo)this.getItem(a1);
        /*SL:715*/return v1.nameAndType;
    }
    
    public String getInvokeDynamicType(final int v2) {
        final InvokeDynamicInfo v3 = /*EL:728*/(InvokeDynamicInfo)this.getItem(v2);
        /*SL:729*/if (v3 == null) {
            /*SL:730*/return null;
        }
        final NameAndTypeInfo a1 = /*EL:732*/(NameAndTypeInfo)this.getItem(v3.nameAndType);
        /*SL:733*/if (a1 == null) {
            /*SL:734*/return null;
        }
        /*SL:736*/return this.getUtf8Info(a1.typeDescriptor);
    }
    
    public int isConstructor(final String a1, final int a2) {
        /*SL:751*/return this.isMember(a1, "<init>", a2);
    }
    
    public int isMember(final String a3, final String v1, final int v2) {
        final MemberrefInfo v3 = /*EL:771*/(MemberrefInfo)this.getItem(v2);
        /*SL:772*/if (this.getClassInfo(v3.classIndex).equals(a3)) {
            final NameAndTypeInfo a4 = /*EL:773*/(NameAndTypeInfo)this.getItem(v3.nameAndTypeIndex);
            /*SL:775*/if (this.getUtf8Info(a4.memberName).equals(v1)) {
                /*SL:776*/return a4.typeDescriptor;
            }
        }
        /*SL:779*/return 0;
    }
    
    public String eqMember(final String a1, final String a2, final int a3) {
        final MemberrefInfo v1 = /*EL:800*/(MemberrefInfo)this.getItem(a3);
        final NameAndTypeInfo v2 = /*EL:801*/(NameAndTypeInfo)this.getItem(v1.nameAndTypeIndex);
        /*SL:803*/if (this.getUtf8Info(v2.memberName).equals(a1) && this.getUtf8Info(v2.typeDescriptor).equals(/*EL:804*/a2)) {
            /*SL:805*/return this.getClassInfo(v1.classIndex);
        }
        /*SL:807*/return null;
    }
    
    private int addItem0(final ConstInfo a1) {
        /*SL:811*/this.items.addElement(a1);
        /*SL:812*/return this.numOfItems++;
    }
    
    private int addItem(final ConstInfo a1) {
        /*SL:816*/if (this.itemsCache == null) {
            /*SL:817*/this.itemsCache = makeItemsCache(this.items);
        }
        final ConstInfo v1 = /*EL:819*/this.itemsCache.get(a1);
        /*SL:820*/if (v1 != null) {
            /*SL:821*/return v1.index;
        }
        /*SL:823*/this.items.addElement(a1);
        /*SL:824*/this.itemsCache.put(a1, a1);
        /*SL:825*/return this.numOfItems++;
    }
    
    public int copy(final int a1, final ConstPool a2, final Map a3) {
        /*SL:841*/if (a1 == 0) {
            /*SL:842*/return 0;
        }
        final ConstInfo v1 = /*EL:844*/this.getItem(a1);
        /*SL:845*/return v1.copy(this, a2, a3);
    }
    
    int addConstInfoPadding() {
        /*SL:849*/return this.addItem0(new ConstInfoPadding(this.numOfItems));
    }
    
    public int addClassInfo(final CtClass a1) {
        /*SL:861*/if (a1 == ConstPool.THIS) {
            /*SL:862*/return this.thisClassInfo;
        }
        /*SL:863*/if (!a1.isArray()) {
            /*SL:864*/return this.addClassInfo(a1.getName());
        }
        /*SL:871*/return this.addClassInfo(Descriptor.toJvmName(a1));
    }
    
    public int addClassInfo(final String a1) {
        final int v1 = /*EL:886*/this.addUtf8Info(Descriptor.toJvmName(a1));
        /*SL:887*/return this.addItem(new ClassInfo(v1, this.numOfItems));
    }
    
    public int addNameAndTypeInfo(final String a1, final String a2) {
        /*SL:900*/return this.addNameAndTypeInfo(this.addUtf8Info(a1), this.addUtf8Info(a2));
    }
    
    public int addNameAndTypeInfo(final int a1, final int a2) {
        /*SL:911*/return this.addItem(new NameAndTypeInfo(a1, a2, this.numOfItems));
    }
    
    public int addFieldrefInfo(final int a1, final String a2, final String a3) {
        final int v1 = /*EL:928*/this.addNameAndTypeInfo(a2, a3);
        /*SL:929*/return this.addFieldrefInfo(a1, v1);
    }
    
    public int addFieldrefInfo(final int a1, final int a2) {
        /*SL:940*/return this.addItem(new FieldrefInfo(a1, a2, this.numOfItems));
    }
    
    public int addMethodrefInfo(final int a1, final String a2, final String a3) {
        final int v1 = /*EL:957*/this.addNameAndTypeInfo(a2, a3);
        /*SL:958*/return this.addMethodrefInfo(a1, v1);
    }
    
    public int addMethodrefInfo(final int a1, final int a2) {
        /*SL:969*/return this.addItem(new MethodrefInfo(a1, a2, this.numOfItems));
    }
    
    public int addInterfaceMethodrefInfo(final int a1, final String a2, final String a3) {
        final int v1 = /*EL:988*/this.addNameAndTypeInfo(a2, a3);
        /*SL:989*/return this.addInterfaceMethodrefInfo(a1, v1);
    }
    
    public int addInterfaceMethodrefInfo(final int a1, final int a2) {
        /*SL:1002*/return this.addItem(new InterfaceMethodrefInfo(a1, a2, this.numOfItems));
    }
    
    public int addStringInfo(final String a1) {
        final int v1 = /*EL:1016*/this.addUtf8Info(a1);
        /*SL:1017*/return this.addItem(new StringInfo(v1, this.numOfItems));
    }
    
    public int addIntegerInfo(final int a1) {
        /*SL:1027*/return this.addItem(new IntegerInfo(a1, this.numOfItems));
    }
    
    public int addFloatInfo(final float a1) {
        /*SL:1037*/return this.addItem(new FloatInfo(a1, this.numOfItems));
    }
    
    public int addLongInfo(final long a1) {
        final int v1 = /*EL:1047*/this.addItem(new LongInfo(a1, this.numOfItems));
        /*SL:1048*/if (v1 == this.numOfItems - 1) {
            /*SL:1049*/this.addConstInfoPadding();
        }
        /*SL:1051*/return v1;
    }
    
    public int addDoubleInfo(final double a1) {
        final int v1 = /*EL:1061*/this.addItem(new DoubleInfo(a1, this.numOfItems));
        /*SL:1062*/if (v1 == this.numOfItems - 1) {
            /*SL:1063*/this.addConstInfoPadding();
        }
        /*SL:1065*/return v1;
    }
    
    public int addUtf8Info(final String a1) {
        /*SL:1075*/return this.addItem(new Utf8Info(a1, this.numOfItems));
    }
    
    public int addMethodHandleInfo(final int a1, final int a2) {
        /*SL:1090*/return this.addItem(new MethodHandleInfo(a1, a2, this.numOfItems));
    }
    
    public int addMethodTypeInfo(final int a1) {
        /*SL:1103*/return this.addItem(new MethodTypeInfo(a1, this.numOfItems));
    }
    
    public int addInvokeDynamicInfo(final int a1, final int a2) {
        /*SL:1117*/return this.addItem(new InvokeDynamicInfo(a1, a2, this.numOfItems));
    }
    
    public Set getClassNames() {
        final HashSet set = /*EL:1126*/new HashSet();
        final LongVector items = /*EL:1127*/this.items;
        /*SL:1129*/for (int numOfItems = this.numOfItems, v0 = 1; v0 < numOfItems; ++v0) {
            final String v = /*EL:1130*/items.elementAt(v0).getClassName(this);
            /*SL:1131*/if (v != null) {
                /*SL:1132*/set.add(v);
            }
        }
        /*SL:1134*/return set;
    }
    
    public void renameClass(final String v2, final String v3) {
        final LongVector v4 = /*EL:1144*/this.items;
        final int v5 = /*EL:1145*/this.numOfItems;
        /*SL:1146*/for (ConstInfo a2 = (ConstInfo)1; a2 < v5; ++a2) {
            /*SL:1147*/a2 = v4.elementAt(a2);
            /*SL:1148*/a2.renameClass(this, v2, v3, this.itemsCache);
        }
    }
    
    public void renameClass(final Map v-2) {
        final LongVector items = /*EL:1159*/this.items;
        /*SL:1161*/for (int v0 = this.numOfItems, v = 1; v < v0; ++v) {
            final ConstInfo a1 = /*EL:1162*/items.elementAt(v);
            /*SL:1163*/a1.renameClass(this, v-2, this.itemsCache);
        }
    }
    
    private void read(final DataInputStream v2) throws IOException {
        int v3 = /*EL:1168*/v2.readUnsignedShort();
        /*SL:1170*/this.items = new LongVector(v3);
        /*SL:1171*/this.numOfItems = 0;
        /*SL:1172*/this.addItem0(null);
        /*SL:1174*/while (--v3 > 0) {
            final int a1 = /*EL:1175*/this.readOne(v2);
            /*SL:1176*/if (a1 == 5 || a1 == 6) {
                /*SL:1177*/this.addConstInfoPadding();
                /*SL:1178*/--v3;
            }
        }
    }
    
    private static HashMap makeItemsCache(final LongVector v1) {
        final HashMap v2 = /*EL:1184*/new HashMap();
        int v3 = /*EL:1185*/1;
        while (true) {
            final ConstInfo a1 = /*EL:1187*/v1.elementAt(v3++);
            /*SL:1188*/if (a1 == null) {
                break;
            }
            /*SL:1191*/v2.put(a1, a1);
        }
        /*SL:1194*/return v2;
    }
    
    private int readOne(final DataInputStream v0) throws IOException {
        final int v = /*EL:1199*/v0.readUnsignedByte();
        ConstInfo v2 = null;
        /*SL:1200*/switch (v) {
            case 1: {
                final ConstInfo a1 = /*EL:1202*/new Utf8Info(v0, this.numOfItems);
                /*SL:1203*/break;
            }
            case 3: {
                /*SL:1205*/v2 = new IntegerInfo(v0, this.numOfItems);
                /*SL:1206*/break;
            }
            case 4: {
                /*SL:1208*/v2 = new FloatInfo(v0, this.numOfItems);
                /*SL:1209*/break;
            }
            case 5: {
                /*SL:1211*/v2 = new LongInfo(v0, this.numOfItems);
                /*SL:1212*/break;
            }
            case 6: {
                /*SL:1214*/v2 = new DoubleInfo(v0, this.numOfItems);
                /*SL:1215*/break;
            }
            case 7: {
                /*SL:1217*/v2 = new ClassInfo(v0, this.numOfItems);
                /*SL:1218*/break;
            }
            case 8: {
                /*SL:1220*/v2 = new StringInfo(v0, this.numOfItems);
                /*SL:1221*/break;
            }
            case 9: {
                /*SL:1223*/v2 = new FieldrefInfo(v0, this.numOfItems);
                /*SL:1224*/break;
            }
            case 10: {
                /*SL:1226*/v2 = new MethodrefInfo(v0, this.numOfItems);
                /*SL:1227*/break;
            }
            case 11: {
                /*SL:1229*/v2 = new InterfaceMethodrefInfo(v0, this.numOfItems);
                /*SL:1230*/break;
            }
            case 12: {
                /*SL:1232*/v2 = new NameAndTypeInfo(v0, this.numOfItems);
                /*SL:1233*/break;
            }
            case 15: {
                /*SL:1235*/v2 = new MethodHandleInfo(v0, this.numOfItems);
                /*SL:1236*/break;
            }
            case 16: {
                /*SL:1238*/v2 = new MethodTypeInfo(v0, this.numOfItems);
                /*SL:1239*/break;
            }
            case 18: {
                /*SL:1241*/v2 = new InvokeDynamicInfo(v0, this.numOfItems);
                /*SL:1242*/break;
            }
            default: {
                /*SL:1244*/throw new IOException("invalid constant type: " + v + " at " + this.numOfItems);
            }
        }
        /*SL:1247*/this.addItem0(v2);
        /*SL:1248*/return v;
    }
    
    public void write(final DataOutputStream v2) throws IOException {
        /*SL:1255*/v2.writeShort(this.numOfItems);
        final LongVector v3 = /*EL:1256*/this.items;
        /*SL:1258*/for (int v4 = this.numOfItems, a1 = 1; a1 < v4; ++a1) {
            /*SL:1259*/v3.elementAt(a1).write(v2);
        }
    }
    
    public void print() {
        /*SL:1266*/this.print(new PrintWriter(System.out, true));
    }
    
    public void print(final PrintWriter v2) {
        /*SL:1274*/for (int v3 = this.numOfItems, a1 = 1; a1 < v3; ++a1) {
            /*SL:1275*/v2.print(a1);
            /*SL:1276*/v2.print(" ");
            /*SL:1277*/this.items.elementAt(a1).print(v2);
        }
    }
    
    static {
        THIS = null;
    }
}
