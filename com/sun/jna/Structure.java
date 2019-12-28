package com.sun.jna;

import java.util.LinkedHashMap;
import java.util.AbstractCollection;
import java.util.WeakHashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.Buffer;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Map;

public abstract class Structure
{
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    protected static final int CALCULATE_SIZE = -1;
    static final Map<Class<?>, LayoutInfo> layoutInfo;
    static final Map<Class<?>, List<String>> fieldOrder;
    private Pointer memory;
    private int size;
    private int alignType;
    private String encoding;
    private int actualAlignType;
    private int structAlignment;
    private Map<String, StructField> structFields;
    private final Map<String, Object> nativeStrings;
    private TypeMapper typeMapper;
    private long typeInfo;
    private boolean autoRead;
    private boolean autoWrite;
    private Structure[] array;
    private boolean readCalled;
    private static final ThreadLocal<Map<Pointer, Structure>> reads;
    private static final ThreadLocal<Set<Structure>> busy;
    private static final Pointer PLACEHOLDER_MEMORY;
    
    protected Structure() {
        this(0);
    }
    
    protected Structure(final TypeMapper a1) {
        this(null, 0, a1);
    }
    
    protected Structure(final int a1) {
        this(null, a1);
    }
    
    protected Structure(final int a1, final TypeMapper a2) {
        this(null, a1, a2);
    }
    
    protected Structure(final Pointer a1) {
        this(a1, 0);
    }
    
    protected Structure(final Pointer a1, final int a2) {
        this(a1, a2, null);
    }
    
    protected Structure(final Pointer a1, final int a2, final TypeMapper a3) {
        this.size = -1;
        this.nativeStrings = new HashMap<String, Object>();
        this.autoRead = true;
        this.autoWrite = true;
        this.setAlignType(a2);
        this.setStringEncoding(Native.getStringEncoding(this.getClass()));
        this.initializeTypeMapper(a3);
        this.validateFields();
        if (a1 != null) {
            this.useMemory(a1, 0, true);
        }
        else {
            this.allocateMemory(-1);
        }
        this.initializeFields();
    }
    
    Map<String, StructField> fields() {
        /*SL:207*/return this.structFields;
    }
    
    TypeMapper getTypeMapper() {
        /*SL:214*/return this.typeMapper;
    }
    
    private void initializeTypeMapper(TypeMapper a1) {
        /*SL:224*/if (a1 == null) {
            /*SL:225*/a1 = Native.getTypeMapper(this.getClass());
        }
        /*SL:227*/this.typeMapper = a1;
        /*SL:228*/this.layoutChanged();
    }
    
    private void layoutChanged() {
        /*SL:235*/if (this.size != -1) {
            /*SL:236*/this.size = -1;
            /*SL:237*/if (this.memory instanceof AutoAllocated) {
                /*SL:238*/this.memory = null;
            }
            /*SL:241*/this.ensureAllocated();
        }
    }
    
    protected void setStringEncoding(final String a1) {
        /*SL:250*/this.encoding = a1;
    }
    
    protected String getStringEncoding() {
        /*SL:258*/return this.encoding;
    }
    
    protected void setAlignType(int a1) {
        /*SL:267*/this.alignType = a1;
        /*SL:268*/if (a1 == 0) {
            /*SL:269*/a1 = Native.getStructureAlignment(this.getClass());
            /*SL:270*/if (a1 == 0) {
                /*SL:271*/if (Platform.isWindows()) {
                    /*SL:272*/a1 = 3;
                }
                else {
                    /*SL:274*/a1 = 2;
                }
            }
        }
        /*SL:277*/this.actualAlignType = a1;
        /*SL:278*/this.layoutChanged();
    }
    
    protected Memory autoAllocate(final int a1) {
        /*SL:287*/return new AutoAllocated(a1);
    }
    
    protected void useMemory(final Pointer a1) {
        /*SL:297*/this.useMemory(a1, 0);
    }
    
    protected void useMemory(final Pointer a1, final int a2) {
        /*SL:309*/this.useMemory(a1, a2, false);
    }
    
    void useMemory(final Pointer v1, final int v2, final boolean v3) {
        try {
            /*SL:325*/this.nativeStrings.clear();
            /*SL:327*/if (this instanceof ByValue && !v3) {
                final byte[] a1 = /*EL:330*/new byte[this.size()];
                /*SL:331*/v1.read(0L, a1, 0, a1.length);
                /*SL:332*/this.memory.write(0L, a1, 0, a1.length);
            }
            else {
                /*SL:337*/this.memory = v1.share(v2);
                /*SL:338*/if (this.size == -1) {
                    /*SL:339*/this.size = this.calculateSize(false);
                }
                /*SL:341*/if (this.size != -1) {
                    /*SL:342*/this.memory = v1.share(v2, this.size);
                }
            }
            /*SL:345*/this.array = null;
            /*SL:346*/this.readCalled = false;
        }
        catch (IndexOutOfBoundsException a2) {
            /*SL:349*/throw new IllegalArgumentException("Structure exceeds provided memory bounds", a2);
        }
    }
    
    protected void ensureAllocated() {
        /*SL:356*/this.ensureAllocated(false);
    }
    
    private void ensureAllocated(final boolean v2) {
        /*SL:365*/if (this.memory == null) {
            /*SL:366*/this.allocateMemory(v2);
        }
        else/*SL:368*/ if (this.size == -1) {
            /*SL:369*/this.size = this.calculateSize(true, v2);
            /*SL:370*/if (!(this.memory instanceof AutoAllocated)) {
                try {
                    /*SL:373*/this.memory = this.memory.share(0L, this.size);
                }
                catch (IndexOutOfBoundsException a1) {
                    /*SL:376*/throw new IllegalArgumentException("Structure exceeds provided memory bounds", a1);
                }
            }
        }
    }
    
    protected void allocateMemory() {
        /*SL:386*/this.allocateMemory(false);
    }
    
    private void allocateMemory(final boolean a1) {
        /*SL:390*/this.allocateMemory(this.calculateSize(true, a1));
    }
    
    protected void allocateMemory(int a1) {
        /*SL:401*/if (a1 == -1) {
            /*SL:403*/a1 = this.calculateSize(false);
        }
        else/*SL:405*/ if (a1 <= 0) {
            /*SL:406*/throw new IllegalArgumentException("Structure size must be greater than zero: " + a1);
        }
        /*SL:410*/if (a1 != -1) {
            /*SL:411*/if (this.memory == null || this.memory instanceof AutoAllocated) {
                /*SL:413*/this.memory = this.autoAllocate(a1);
            }
            /*SL:415*/this.size = a1;
        }
    }
    
    public int size() {
        /*SL:423*/this.ensureAllocated();
        /*SL:424*/return this.size;
    }
    
    public void clear() {
        /*SL:429*/this.ensureAllocated();
        /*SL:430*/this.memory.clear(this.size());
    }
    
    public Pointer getPointer() {
        /*SL:444*/this.ensureAllocated();
        /*SL:445*/return this.memory;
    }
    
    static Set<Structure> busy() {
        /*SL:541*/return Structure.busy.get();
    }
    
    static Map<Pointer, Structure> reading() {
        /*SL:544*/return Structure.reads.get();
    }
    
    void conditionalAutoRead() {
        /*SL:549*/if (!this.readCalled) {
            /*SL:550*/this.autoRead();
        }
    }
    
    public void read() {
        /*SL:559*/if (this.memory == Structure.PLACEHOLDER_MEMORY) {
            /*SL:560*/return;
        }
        /*SL:562*/this.readCalled = true;
        /*SL:568*/this.ensureAllocated();
        /*SL:571*/if (busy().contains(this)) {
            /*SL:572*/return;
        }
        busy().add(/*EL:574*/this);
        /*SL:575*/if (this instanceof ByReference) {
            reading().put(/*EL:576*/this.getPointer(), this);
        }
        try {
            /*SL:579*/for (final StructField v1 : this.fields().values()) {
                /*SL:580*/this.readField(v1);
            }
        }
        finally {
            busy().remove(/*EL:584*/this);
            /*SL:585*/if (reading().get(this.getPointer()) == this) {
                reading().remove(/*EL:586*/this.getPointer());
            }
        }
    }
    
    protected int fieldOffset(final String a1) {
        /*SL:596*/this.ensureAllocated();
        final StructField v1 = /*EL:597*/this.fields().get(a1);
        /*SL:598*/if (v1 == null) {
            /*SL:599*/throw new IllegalArgumentException("No such field: " + a1);
        }
        /*SL:600*/return v1.offset;
    }
    
    public Object readField(final String a1) {
        /*SL:610*/this.ensureAllocated();
        final StructField v1 = /*EL:611*/this.fields().get(a1);
        /*SL:612*/if (v1 == null) {
            /*SL:613*/throw new IllegalArgumentException("No such field: " + a1);
        }
        /*SL:614*/return this.readField(v1);
    }
    
    Object getFieldValue(final Field v2) {
        try {
            /*SL:624*/return v2.get(this);
        }
        catch (Exception a1) {
            /*SL:627*/throw new Error("Exception reading field '" + v2.getName() + "' in " + this.getClass(), a1);
        }
    }
    
    void setFieldValue(final Field a1, final Object a2) {
        /*SL:636*/this.setFieldValue(a1, a2, false);
    }
    
    private void setFieldValue(final Field v1, final Object v2, final boolean v3) {
        try {
            /*SL:642*/v1.set(this, v2);
        }
        catch (IllegalAccessException a2) {
            final int a1 = /*EL:645*/v1.getModifiers();
            /*SL:646*/if (!Modifier.isFinal(a1)) {
                /*SL:654*/throw new Error("Unexpectedly unable to write to field '" + v1.getName() + "' within " + this.getClass(), a2);
            }
            if (v3) {
                throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + v1.getName() + "' within " + this.getClass() + ")", a2);
            }
            throw new UnsupportedOperationException("Attempt to write to read-only field '" + v1.getName() + "' within " + this.getClass(), a2);
        }
    }
    
    static Structure updateStructureByReference(final Class<?> a2, Structure a3, final Pointer v1) {
        /*SL:666*/if (v1 == null) {
            /*SL:667*/a3 = null;
        }
        else/*SL:670*/ if (a3 == null || !v1.equals(a3.getPointer())) {
            final Structure a4 = reading().get(/*EL:671*/v1);
            /*SL:672*/if (a4 != null && a2.equals(a4.getClass())) {
                /*SL:673*/a3 = a4;
                /*SL:674*/a3.autoRead();
            }
            else {
                /*SL:677*/a3 = newInstance(a2, v1);
                /*SL:678*/a3.conditionalAutoRead();
            }
        }
        else {
            /*SL:682*/a3.autoRead();
        }
        /*SL:685*/return a3;
    }
    
    protected Object readField(final StructField v-4) {
        final int offset = /*EL:697*/v-4.offset;
        Class<?> v-5 = /*EL:700*/v-4.type;
        final FromNativeConverter readConverter = /*EL:701*/v-4.readConverter;
        /*SL:702*/if (readConverter != null) {
            /*SL:703*/v-5 = readConverter.nativeType();
        }
        final Object v0 = /*EL:706*/(Structure.class.isAssignableFrom(v-5) || Callback.class.isAssignableFrom(v-5) || (Platform.HAS_BUFFERS && /*EL:707*/Buffer.class.isAssignableFrom(v-5)) || /*EL:708*/Pointer.class.isAssignableFrom(v-5) || /*EL:709*/NativeMapped.class.isAssignableFrom(v-5) || /*EL:710*/v-5.isArray()) ? /*EL:711*/this.getFieldValue(v-4.field) : /*EL:712*/null;
        Object v;
        /*SL:715*/if (v-5 == String.class) {
            final Pointer a1 = /*EL:716*/this.memory.getPointer(offset);
            /*SL:717*/v = ((a1 == null) ? null : a1.getString(0L, this.encoding));
        }
        else {
            /*SL:720*/v = this.memory.getValue(offset, v-5, v0);
        }
        /*SL:722*/if (readConverter != null) {
            /*SL:723*/v = readConverter.fromNative(v, v-4.context);
            /*SL:724*/if (v0 != null && v0.equals(v)) {
                /*SL:725*/v = v0;
            }
        }
        /*SL:729*/if (v-5.equals(String.class) || v-5.equals(WString.class)) {
            /*SL:731*/this.nativeStrings.put(v-4.name + ".ptr", this.memory.getPointer(offset));
            /*SL:732*/this.nativeStrings.put(v-4.name + ".val", v);
        }
        /*SL:736*/this.setFieldValue(v-4.field, v, true);
        /*SL:737*/return v;
    }
    
    public void write() {
        /*SL:745*/if (this.memory == Structure.PLACEHOLDER_MEMORY) {
            /*SL:746*/return;
        }
        /*SL:752*/this.ensureAllocated();
        /*SL:755*/if (this instanceof ByValue) {
            /*SL:756*/this.getTypeInfo();
        }
        /*SL:760*/if (busy().contains(this)) {
            /*SL:761*/return;
        }
        busy().add(/*EL:763*/this);
        try {
            /*SL:766*/for (final StructField v1 : this.fields().values()) {
                /*SL:767*/if (!v1.isVolatile) {
                    /*SL:768*/this.writeField(v1);
                }
            }
        }
        finally {
            busy().remove(/*EL:773*/this);
        }
    }
    
    public void writeField(final String a1) {
        /*SL:783*/this.ensureAllocated();
        final StructField v1 = /*EL:784*/this.fields().get(a1);
        /*SL:785*/if (v1 == null) {
            /*SL:786*/throw new IllegalArgumentException("No such field: " + a1);
        }
        /*SL:787*/this.writeField(v1);
    }
    
    public void writeField(final String a1, final Object a2) {
        /*SL:798*/this.ensureAllocated();
        final StructField v1 = /*EL:799*/this.fields().get(a1);
        /*SL:800*/if (v1 == null) {
            /*SL:801*/throw new IllegalArgumentException("No such field: " + a1);
        }
        /*SL:802*/this.setFieldValue(v1.field, a2);
        /*SL:803*/this.writeField(v1);
    }
    
    protected void writeField(final StructField v-4) {
        /*SL:811*/if (v-4.isReadOnly) {
            /*SL:812*/return;
        }
        final int offset = /*EL:815*/v-4.offset;
        Object v-5 = /*EL:818*/this.getFieldValue(v-4.field);
        Class<?> v-6 = /*EL:821*/v-4.type;
        final ToNativeConverter v0 = /*EL:822*/v-4.writeConverter;
        /*SL:823*/if (v0 != null) {
            /*SL:824*/v-5 = v0.toNative(v-5, new StructureWriteContext(this, v-4.field));
            /*SL:825*/v-6 = v0.nativeType();
        }
        /*SL:829*/if (String.class == v-6 || WString.class == v-6) {
            final boolean v = /*EL:832*/v-6 == WString.class;
            /*SL:833*/if (v-5 != null) {
                /*SL:836*/if (this.nativeStrings.containsKey(v-4.name + ".ptr") && v-5.equals(this.nativeStrings.get(v-4.name + ".val"))) {
                    /*SL:838*/return;
                }
                final NativeString a1 = /*EL:840*/v ? new NativeString(v-5.toString(), /*EL:841*/true) : new NativeString(v-5.toString(), /*EL:842*/this.encoding);
                /*SL:845*/this.nativeStrings.put(v-4.name, a1);
                /*SL:846*/v-5 = a1.getPointer();
            }
            else {
                /*SL:849*/this.nativeStrings.remove(v-4.name);
            }
            /*SL:851*/this.nativeStrings.remove(v-4.name + ".ptr");
            /*SL:852*/this.nativeStrings.remove(v-4.name + ".val");
        }
        try {
            /*SL:856*/this.memory.setValue(offset, v-5, v-6);
        }
        catch (IllegalArgumentException v3) {
            final String v2 = /*EL:859*/"Structure field \"" + v-4.name + "\" was declared as " + v-4.type + ((v-4.type == v-6) ? "" : (" (native type " + v-6 + ")")) + ", which is not supported within a Structure";
            /*SL:864*/throw new IllegalArgumentException(v2, v3);
        }
    }
    
    protected abstract List<String> getFieldOrder();
    
    @Deprecated
    protected final void setFieldOrder(final String[] a1) {
        /*SL:901*/throw new Error("This method is obsolete, use getFieldOrder() instead");
    }
    
    protected void sortFields(final List<Field> v-2, final List<String> v-1) {
        /*SL:909*/for (int v0 = 0; v0 < v-1.size(); ++v0) {
            final String v = /*EL:910*/v-1.get(v0);
            /*SL:911*/for (Field a2 = (Field)0; a2 < v-2.size(); ++a2) {
                /*SL:912*/a2 = v-2.get(a2);
                /*SL:913*/if (v.equals(a2.getName())) {
                    /*SL:914*/Collections.swap(v-2, v0, a2);
                    /*SL:915*/break;
                }
            }
        }
    }
    
    protected List<Field> getFieldList() {
        final List<Field> list = /*EL:926*/new ArrayList<Field>();
        /*SL:928*/for (Class<?> clazz = this.getClass(); !clazz.equals(Structure.class); /*SL:929*/clazz = clazz.getSuperclass()) {
            final List<Field> list2 = /*EL:930*/new ArrayList<Field>();
            final Field[] declaredFields = /*EL:931*/clazz.getDeclaredFields();
            /*SL:932*/for (int v0 = 0; v0 < declaredFields.length; ++v0) {
                final int v = /*EL:933*/declaredFields[v0].getModifiers();
                /*SL:934*/if (!Modifier.isStatic(v)) {
                    if (Modifier.isPublic(v)) {
                        /*SL:937*/list2.add(declaredFields[v0]);
                    }
                }
            }
            /*SL:939*/list.addAll(0, list2);
        }
        /*SL:941*/return list;
    }
    
    private List<String> fieldOrder() {
        final Class<?> class1 = /*EL:948*/this.getClass();
        /*SL:949*/synchronized (Structure.fieldOrder) {
            List<String> v1 = Structure.fieldOrder.get(/*EL:950*/class1);
            /*SL:951*/if (v1 == null) {
                /*SL:952*/v1 = this.getFieldOrder();
                Structure.fieldOrder.put(/*EL:953*/class1, v1);
            }
            /*SL:955*/return v1;
        }
    }
    
    public static List<String> createFieldsOrder(final List<String> a1, final String... a2) {
        /*SL:960*/return createFieldsOrder(a1, Arrays.<String>asList(a2));
    }
    
    public static List<String> createFieldsOrder(final List<String> a1, final List<String> a2) {
        final List<String> v1 = /*EL:964*/new ArrayList<String>(a1.size() + a2.size());
        /*SL:965*/v1.addAll(a1);
        /*SL:966*/v1.addAll(a2);
        /*SL:967*/return Collections.<String>unmodifiableList((List<? extends String>)v1);
    }
    
    public static List<String> createFieldsOrder(final String a1) {
        /*SL:975*/return Collections.<String>unmodifiableList((List<? extends String>)Collections.<? extends T>singletonList((T)a1));
    }
    
    public static List<String> createFieldsOrder(final String... a1) {
        /*SL:983*/return Collections.<String>unmodifiableList((List<? extends String>)Arrays.<? extends T>asList((T[])a1));
    }
    
    private static <T extends java.lang.Object> List<T> sort(final Collection<? extends T> a1) {
        final List<T> v1 = /*EL:987*/new ArrayList<T>(a1);
        /*SL:988*/Collections.<T>sort(v1);
        /*SL:989*/return v1;
    }
    
    protected List<Field> getFields(final boolean v2) {
        final List<Field> v3 = /*EL:1000*/this.getFieldList();
        final Set<String> v4 = /*EL:1001*/new HashSet<String>();
        /*SL:1002*/for (final Field a1 : v3) {
            /*SL:1003*/v4.add(a1.getName());
        }
        final List<String> v5 = /*EL:1006*/this.fieldOrder();
        /*SL:1007*/if (v5.size() != v3.size() && v3.size() > 1) {
            /*SL:1008*/if (v2) {
                /*SL:1009*/throw new Error("Structure.getFieldOrder() on " + this.getClass() + " does not provide enough names [" + v5.size() + /*EL:1010*/"] (" + /*EL:1012*/Structure.<Object>sort((Collection<?>)v5) + ") to match declared fields [" + v3.size() + /*EL:1013*/"] (" + /*EL:1015*/Structure.<Object>sort((Collection<?>)v4) + ")");
            }
            /*SL:1018*/return null;
        }
        else {
            final Set<String> a2 = /*EL:1021*/new HashSet<String>(v5);
            /*SL:1022*/if (!a2.equals(v4)) {
                /*SL:1023*/throw new Error("Structure.getFieldOrder() on " + this.getClass() + " returns names (" + /*EL:1025*/Structure.<Object>sort((Collection<?>)v5) + ") which do not match declared field names (" + /*EL:1027*/Structure.<Object>sort((Collection<?>)v4) + ")");
            }
            /*SL:1030*/this.sortFields(v3, v5);
            /*SL:1031*/return v3;
        }
    }
    
    protected int calculateSize(final boolean a1) {
        /*SL:1049*/return this.calculateSize(a1, false);
    }
    
    static int size(final Class<?> a1) {
        /*SL:1057*/return size(a1, null);
    }
    
    static int size(final Class<?> a2, final Structure v1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //     3: dup            
        //     4: astore_3       
        //     5: monitorenter   
        //     6: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //     9: aload_0         /* a2 */
        //    10: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    15: checkcast       Lcom/sun/jna/Structure$LayoutInfo;
        //    18: astore_2        /* a1 */
        //    19: aload_3        
        //    20: monitorexit    
        //    21: goto            31
        //    24: astore          4
        //    26: aload_3        
        //    27: monitorexit    
        //    28: aload           4
        //    30: athrow         
        //    31: aload_2         /* v2 */
        //    32: ifnull          49
        //    35: aload_2         /* v2 */
        //    36: invokestatic    com/sun/jna/Structure$LayoutInfo.access$000:(Lcom/sun/jna/Structure$LayoutInfo;)Z
        //    39: ifne            49
        //    42: aload_2         /* v2 */
        //    43: invokestatic    com/sun/jna/Structure$LayoutInfo.access$100:(Lcom/sun/jna/Structure$LayoutInfo;)I
        //    46: goto            50
        //    49: iconst_m1      
        //    50: istore_3        /* v3 */
        //    51: iload_3         /* v3 */
        //    52: iconst_m1      
        //    53: if_icmpne       73
        //    56: aload_1         /* v1 */
        //    57: ifnonnull       68
        //    60: aload_0         /* a2 */
        //    61: getstatic       com/sun/jna/Structure.PLACEHOLDER_MEMORY:Lcom/sun/jna/Pointer;
        //    64: invokestatic    com/sun/jna/Structure.newInstance:(Ljava/lang/Class;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Structure;
        //    67: astore_1        /* v1 */
        //    68: aload_1         /* v1 */
        //    69: invokevirtual   com/sun/jna/Structure.size:()I
        //    72: istore_3        /* v3 */
        //    73: iload_3         /* v3 */
        //    74: ireturn        
        //    Signature:
        //  (Ljava/lang/Class<*>;Lcom/sun/jna/Structure;)I
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------
        //  19     5       2     a1    Lcom/sun/jna/Structure$LayoutInfo;
        //  0      75      0     a2    Ljava/lang/Class;
        //  0      75      1     v1    Lcom/sun/jna/Structure;
        //  31     44      2     v2    Lcom/sun/jna/Structure$LayoutInfo;
        //  51     24      3     v3    I
        //    LocalVariableTypeTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  --------------------
        //  0      75      0     a2    Ljava/lang/Class<*>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      21     24     31     Any
        //  24     28     24     31     Any
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
    
    int calculateSize(final boolean v1, final boolean v2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_3        /* v3 */
        //     2: aload_0         /* a2 */
        //     3: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     6: astore          v4
        //     8: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //    11: dup            
        //    12: astore          6
        //    14: monitorenter   
        //    15: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //    18: aload           v4
        //    20: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    25: checkcast       Lcom/sun/jna/Structure$LayoutInfo;
        //    28: astore          a1
        //    30: aload           6
        //    32: monitorexit    
        //    33: goto            44
        //    36: astore          7
        //    38: aload           6
        //    40: monitorexit    
        //    41: aload           7
        //    43: athrow         
        //    44: aload           v5
        //    46: ifnull          73
        //    49: aload_0         /* a2 */
        //    50: getfield        com/sun/jna/Structure.alignType:I
        //    53: aload           v5
        //    55: invokestatic    com/sun/jna/Structure$LayoutInfo.access$200:(Lcom/sun/jna/Structure$LayoutInfo;)I
        //    58: if_icmpne       73
        //    61: aload_0         /* a2 */
        //    62: getfield        com/sun/jna/Structure.typeMapper:Lcom/sun/jna/TypeMapper;
        //    65: aload           v5
        //    67: invokestatic    com/sun/jna/Structure$LayoutInfo.access$300:(Lcom/sun/jna/Structure$LayoutInfo;)Lcom/sun/jna/TypeMapper;
        //    70: if_acmpeq       81
        //    73: aload_0         /* a2 */
        //    74: iload_1         /* v1 */
        //    75: iload_2         /* v2 */
        //    76: invokespecial   com/sun/jna/Structure.deriveLayout:(ZZ)Lcom/sun/jna/Structure$LayoutInfo;
        //    79: astore          v5
        //    81: aload           v5
        //    83: ifnull          179
        //    86: aload_0         /* a2 */
        //    87: aload           v5
        //    89: invokestatic    com/sun/jna/Structure$LayoutInfo.access$400:(Lcom/sun/jna/Structure$LayoutInfo;)I
        //    92: putfield        com/sun/jna/Structure.structAlignment:I
        //    95: aload_0         /* a2 */
        //    96: aload           v5
        //    98: invokestatic    com/sun/jna/Structure$LayoutInfo.access$500:(Lcom/sun/jna/Structure$LayoutInfo;)Ljava/util/Map;
        //   101: putfield        com/sun/jna/Structure.structFields:Ljava/util/Map;
        //   104: aload           v5
        //   106: invokestatic    com/sun/jna/Structure$LayoutInfo.access$000:(Lcom/sun/jna/Structure$LayoutInfo;)Z
        //   109: ifne            173
        //   112: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //   115: dup            
        //   116: astore          6
        //   118: monitorenter   
        //   119: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //   122: aload           v4
        //   124: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   129: ifeq            146
        //   132: aload_0         /* a2 */
        //   133: getfield        com/sun/jna/Structure.alignType:I
        //   136: ifne            146
        //   139: aload_0         /* a2 */
        //   140: getfield        com/sun/jna/Structure.typeMapper:Lcom/sun/jna/TypeMapper;
        //   143: ifnull          159
        //   146: getstatic       com/sun/jna/Structure.layoutInfo:Ljava/util/Map;
        //   149: aload           v4
        //   151: aload           v5
        //   153: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   158: pop            
        //   159: aload           6
        //   161: monitorexit    
        //   162: goto            173
        //   165: astore          8
        //   167: aload           6
        //   169: monitorexit    
        //   170: aload           8
        //   172: athrow         
        //   173: aload           v5
        //   175: invokestatic    com/sun/jna/Structure$LayoutInfo.access$100:(Lcom/sun/jna/Structure$LayoutInfo;)I
        //   178: istore_3        /* v3 */
        //   179: iload_3         /* v3 */
        //   180: ireturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------
        //  30     6       5     a1    Lcom/sun/jna/Structure$LayoutInfo;
        //  0      181     0     a2    Lcom/sun/jna/Structure;
        //  0      181     1     v1    Z
        //  0      181     2     v2    Z
        //  2      179     3     v3    I
        //  8      173     4     v4    Ljava/lang/Class;
        //  44     137     5     v5    Lcom/sun/jna/Structure$LayoutInfo;
        //    LocalVariableTypeTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  --------------------
        //  8      173     4     v4    Ljava/lang/Class<*>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  15     33     36     44     Any
        //  36     41     36     44     Any
        //  119    162    165    173    Any
        //  165    170    165    173    Any
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
    
    private void validateField(final String v-1, final Class<?> v0) {
        /*SL:1137*/if (this.typeMapper != null) {
            final ToNativeConverter a1 = /*EL:1138*/this.typeMapper.getToNativeConverter(v0);
            /*SL:1139*/if (a1 != null) {
                /*SL:1140*/this.validateField(v-1, a1.nativeType());
                /*SL:1141*/return;
            }
        }
        /*SL:1144*/if (v0.isArray()) {
            /*SL:1145*/this.validateField(v-1, v0.getComponentType());
        }
        else {
            try {
                /*SL:1149*/this.getNativeSize(v0);
            }
            catch (IllegalArgumentException v) {
                final String a2 = /*EL:1152*/"Invalid Structure field in " + this.getClass() + ", field name '" + v-1 + "' (" + v0 + "): " + v.getMessage();
                /*SL:1153*/throw new IllegalArgumentException(a2, v);
            }
        }
    }
    
    private void validateFields() {
        final List<Field> fieldList = /*EL:1160*/this.getFieldList();
        /*SL:1161*/for (final Field v1 : fieldList) {
            /*SL:1162*/this.validateField(v1.getName(), v1.getType());
        }
    }
    
    private LayoutInfo deriveLayout(final boolean v-15, final boolean v-14) {
        int max = /*EL:1171*/0;
        final List<Field> fields = /*EL:1172*/this.getFields(v-15);
        /*SL:1173*/if (fields == null) {
            /*SL:1174*/return null;
        }
        final LayoutInfo layoutInfo = /*EL:1177*/new LayoutInfo();
        /*SL:1178*/layoutInfo.alignType = this.alignType;
        /*SL:1179*/layoutInfo.typeMapper = this.typeMapper;
        boolean v3 = /*EL:1181*/true;
        /*SL:1182*/for (final Field a3 : fields) {
            final int modifiers = /*EL:1184*/a3.getModifiers();
            final Class<?> type = /*EL:1186*/a3.getType();
            /*SL:1187*/if (type.isArray()) {
                /*SL:1188*/layoutInfo.variable = true;
            }
            final StructField a4 = /*EL:1190*/new StructField();
            /*SL:1191*/a4.isVolatile = Modifier.isVolatile(modifiers);
            /*SL:1192*/a4.isReadOnly = Modifier.isFinal(modifiers);
            /*SL:1193*/if (a4.isReadOnly) {
                /*SL:1194*/if (!Platform.RO_FIELDS) {
                    /*SL:1195*/throw new IllegalArgumentException("This VM does not support read-only fields (field '" + a3.getName() + /*EL:1196*/"' within " + this.getClass() + ")");
                }
                /*SL:1200*/a3.setAccessible(true);
            }
            /*SL:1202*/a4.field = a3;
            /*SL:1203*/a4.name = a3.getName();
            /*SL:1204*/a4.type = type;
            /*SL:1207*/if (Callback.class.isAssignableFrom(type) && !type.isInterface()) {
                /*SL:1208*/throw new IllegalArgumentException("Structure Callback field '" + a3.getName() + /*EL:1209*/"' must be an interface");
            }
            /*SL:1212*/if (type.isArray() && Structure.class.equals(type.getComponentType())) {
                final String a1 = /*EL:1214*/"Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
                /*SL:1217*/throw new IllegalArgumentException(a1);
            }
            int nativeAlignment = /*EL:1220*/1;
            /*SL:1221*/if (Modifier.isPublic(a3.getModifiers())) {
                Object o = /*EL:1225*/this.getFieldValue(a4.field);
                /*SL:1226*/if (o == null && type.isArray()) {
                    /*SL:1227*/if (v-15) {
                        /*SL:1228*/throw new IllegalStateException("Array fields must be initialized");
                    }
                    /*SL:1231*/return null;
                }
                else {
                    Class<?> nativeType = /*EL:1233*/type;
                    /*SL:1234*/if (NativeMapped.class.isAssignableFrom(type)) {
                        final NativeMappedConverter a2 = /*EL:1235*/NativeMappedConverter.getInstance(type);
                        /*SL:1236*/nativeType = a2.nativeType();
                        /*SL:1237*/a4.writeConverter = a2;
                        /*SL:1238*/a4.readConverter = a2;
                        /*SL:1239*/a4.context = new StructureReadContext(this, a3);
                    }
                    else/*SL:1241*/ if (this.typeMapper != null) {
                        final ToNativeConverter toNativeConverter = /*EL:1242*/this.typeMapper.getToNativeConverter(type);
                        final FromNativeConverter v0 = /*EL:1243*/this.typeMapper.getFromNativeConverter(type);
                        /*SL:1244*/if (toNativeConverter != null && v0 != null) {
                            /*SL:1245*/o = toNativeConverter.toNative(o, new StructureWriteContext(this, a4.field));
                            /*SL:1247*/nativeType = ((o != null) ? o.getClass() : Pointer.class);
                            /*SL:1248*/a4.writeConverter = toNativeConverter;
                            /*SL:1249*/a4.readConverter = v0;
                            /*SL:1250*/a4.context = new StructureReadContext(this, a3);
                        }
                        else/*SL:1252*/ if (toNativeConverter != null || v0 != null) {
                            final String v = /*EL:1253*/"Structures require bidirectional type conversion for " + type;
                            /*SL:1254*/throw new IllegalArgumentException(v);
                        }
                    }
                    /*SL:1258*/if (o == null) {
                        /*SL:1259*/o = this.initializeField(a4.field, type);
                    }
                    try {
                        /*SL:1263*/a4.size = this.getNativeSize(nativeType, o);
                        /*SL:1264*/nativeAlignment = this.getNativeAlignment(nativeType, o, v3);
                    }
                    catch (IllegalArgumentException ex) {
                        /*SL:1268*/if (!v-15 && this.typeMapper == null) {
                            /*SL:1269*/return null;
                        }
                        final String v2 = /*EL:1271*/"Invalid Structure field in " + this.getClass() + ", field name '" + a4.name + "' (" + a4.type + "): " + ex.getMessage();
                        /*SL:1272*/throw new IllegalArgumentException(v2, ex);
                    }
                    /*SL:1276*/if (nativeAlignment == 0) {
                        /*SL:1277*/throw new Error("Field alignment is zero for field '" + a4.name + "' within " + this.getClass());
                    }
                    /*SL:1279*/layoutInfo.alignment = Math.max(layoutInfo.alignment, nativeAlignment);
                    /*SL:1280*/if (max % nativeAlignment != 0) {
                        /*SL:1281*/max += nativeAlignment - max % nativeAlignment;
                    }
                    /*SL:1283*/if (this instanceof Union) {
                        /*SL:1284*/a4.offset = 0;
                        /*SL:1285*/max = Math.max(max, a4.size);
                    }
                    else {
                        /*SL:1288*/a4.offset = max;
                        /*SL:1289*/max += a4.size;
                    }
                    /*SL:1293*/layoutInfo.fields.put(a4.name, a4);
                    /*SL:1296*/if (layoutInfo.typeInfoField == null || layoutInfo.typeInfoField.size < a4.size || /*EL:1297*/(layoutInfo.typeInfoField.size == a4.size && Structure.class.isAssignableFrom(a4.type))) {
                        /*SL:1299*/layoutInfo.typeInfoField = a4;
                    }
                }
            }
            v3 = false;
        }
        /*SL:1303*/if (max > 0) {
            final int addPadding = /*EL:1304*/this.addPadding(max, layoutInfo.alignment);
            /*SL:1306*/if (this instanceof ByValue && !v-14) {
                /*SL:1307*/this.getTypeInfo();
            }
            /*SL:1309*/layoutInfo.size = addPadding;
            /*SL:1310*/return layoutInfo;
        }
        /*SL:1313*/throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown or zero size (ensure all fields are public)");
    }
    
    private void initializeFields() {
        final List<Field> fieldList = /*EL:1324*/this.getFieldList();
        /*SL:1325*/for (final Field v0 : fieldList) {
            try {
                final Object v = /*EL:1327*/v0.get(this);
                /*SL:1328*/if (v != null) {
                    continue;
                }
                /*SL:1329*/this.initializeField(v0, v0.getType());
            }
            catch (Exception v2) {
                /*SL:1333*/throw new Error("Exception reading field '" + v0.getName() + "' in " + this.getClass(), v2);
            }
        }
    }
    
    private Object initializeField(final Field v-2, final Class<?> v-1) {
        Object v0 = /*EL:1339*/null;
        /*SL:1340*/if (Structure.class.isAssignableFrom(v-1) && !ByReference.class.isAssignableFrom(v-1)) {
            try {
                /*SL:1343*/v0 = newInstance(v-1, Structure.PLACEHOLDER_MEMORY);
                /*SL:1344*/this.setFieldValue(v-2, v0);
                /*SL:1349*/return /*EL:1356*/v0;
            }
            catch (IllegalArgumentException a2) {
                final String a1 = "Can't determine size of nested structure";
                throw new IllegalArgumentException(a1, a2);
            }
        }
        if (NativeMapped.class.isAssignableFrom(v-1)) {
            final NativeMappedConverter v = NativeMappedConverter.getInstance(v-1);
            v0 = v.defaultValue();
            this.setFieldValue(v-2, v0);
        }
        return v0;
    }
    
    private int addPadding(final int a1) {
        /*SL:1360*/return this.addPadding(a1, this.structAlignment);
    }
    
    private int addPadding(int a1, final int a2) {
        /*SL:1366*/if (this.actualAlignType != 1 && /*EL:1367*/a1 % a2 != 0) {
            /*SL:1368*/a1 += a2 - a1 % a2;
        }
        /*SL:1371*/return a1;
    }
    
    protected int getStructAlignment() {
        /*SL:1378*/if (this.size == -1) {
            /*SL:1380*/this.calculateSize(true);
        }
        /*SL:1382*/return this.structAlignment;
    }
    
    protected int getNativeAlignment(Class<?> a3, Object v1, final boolean v2) {
        int v3 = /*EL:1396*/1;
        /*SL:1397*/if (NativeMapped.class.isAssignableFrom(a3)) {
            final NativeMappedConverter a4 = /*EL:1398*/NativeMappedConverter.getInstance(a3);
            /*SL:1399*/a3 = a4.nativeType();
            /*SL:1400*/v1 = a4.toNative(v1, new ToNativeContext());
        }
        final int v4 = /*EL:1402*/Native.getNativeSize(a3, v1);
        /*SL:1403*/if (a3.isPrimitive() || Long.class == a3 || Integer.class == a3 || Short.class == a3 || Character.class == a3 || Byte.class == a3 || Boolean.class == a3 || Float.class == a3 || Double.class == a3) {
            /*SL:1407*/v3 = v4;
        }
        else/*SL:1411*/ if ((Pointer.class.isAssignableFrom(a3) && !Function.class.isAssignableFrom(a3)) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(a3)) || Callback.class.isAssignableFrom(a3) || WString.class == a3 || String.class == a3) {
            /*SL:1414*/v3 = Pointer.SIZE;
        }
        else/*SL:1416*/ if (Structure.class.isAssignableFrom(a3)) {
            /*SL:1417*/if (ByReference.class.isAssignableFrom(a3)) {
                /*SL:1418*/v3 = Pointer.SIZE;
            }
            else {
                /*SL:1421*/if (v1 == null) {
                    /*SL:1422*/v1 = newInstance(a3, Structure.PLACEHOLDER_MEMORY);
                }
                /*SL:1423*/v3 = ((Structure)v1).getStructAlignment();
            }
        }
        else {
            /*SL:1426*/if (!a3.isArray()) {
                /*SL:1430*/throw new IllegalArgumentException("Type " + a3 + " has unknown native alignment");
            }
            v3 = this.getNativeAlignment(a3.getComponentType(), null, v2);
        }
        /*SL:1433*/if (this.actualAlignType == 1) {
            /*SL:1434*/v3 = 1;
        }
        else/*SL:1436*/ if (this.actualAlignType == 3) {
            /*SL:1437*/v3 = Math.min(8, v3);
        }
        else/*SL:1439*/ if (this.actualAlignType == 2) {
            /*SL:1442*/if (!v2 || !Platform.isMac() || !Platform.isPPC()) {
                /*SL:1443*/v3 = Math.min(Native.MAX_ALIGNMENT, v3);
            }
            /*SL:1445*/if (!v2 && Platform.isAIX() && (a3 == Double.TYPE || a3 == Double.class)) {
                /*SL:1446*/v3 = 4;
            }
        }
        /*SL:1449*/return v3;
    }
    
    @Override
    public String toString() {
        /*SL:1459*/return this.toString(Boolean.getBoolean("jna.dump_memory"));
    }
    
    public String toString(final boolean a1) {
        /*SL:1468*/return this.toString(0, true, a1);
    }
    
    private String format(final Class<?> a1) {
        final String v1 = /*EL:1472*/a1.getName();
        final int v2 = /*EL:1473*/v1.lastIndexOf(".");
        /*SL:1474*/return v1.substring(v2 + 1);
    }
    
    private String toString(final int v-9, final boolean v-8, final boolean v-7) {
        /*SL:1478*/this.ensureAllocated();
        final String property = /*EL:1479*/System.getProperty("line.separator");
        String s = /*EL:1480*/this.format(this.getClass()) + "(" + this.getPointer() + ")";
        /*SL:1481*/if (!(this.getPointer() instanceof Memory)) {
            /*SL:1482*/s = s + " (" + this.size() + " bytes)";
        }
        String string = /*EL:1484*/"";
        /*SL:1485*/for (int a1 = 0; a1 < v-9; ++a1) {
            /*SL:1486*/string += "  ";
        }
        String s2 = /*EL:1488*/property;
        /*SL:1489*/if (!v-8) {
            /*SL:1490*/s2 = "...}";
        }
        else {
            final Iterator<StructField> iterator = /*EL:1492*/this.fields().values().iterator();
            while (iterator.hasNext()) {
                final StructField a2 = /*EL:1493*/iterator.next();
                Object a3 = /*EL:1494*/this.getFieldValue(a2.field);
                String v1 = /*EL:1495*/this.format(a2.type);
                String v2 = /*EL:1496*/"";
                /*SL:1497*/s2 += string;
                /*SL:1498*/if (a2.type.isArray() && a3 != null) {
                    /*SL:1499*/v1 = this.format(a2.type.getComponentType());
                    /*SL:1500*/v2 = "[" + Array.getLength(a3) + "]";
                }
                /*SL:1503*/s2 = s2 + "  " + v1 + " " + a2.name + v2 + "@" + Integer.toHexString(a2.offset);
                /*SL:1504*/if (a3 instanceof Structure) {
                    /*SL:1505*/a3 = ((Structure)a3).toString(v-9 + 1, !(a3 instanceof ByReference), v-7);
                }
                /*SL:1507*/s2 += "=";
                /*SL:1508*/if (a3 instanceof Long) {
                    /*SL:1509*/s2 += Long.toHexString((long)a3);
                }
                else/*SL:1511*/ if (a3 instanceof Integer) {
                    /*SL:1512*/s2 += Integer.toHexString((int)a3);
                }
                else/*SL:1514*/ if (a3 instanceof Short) {
                    /*SL:1515*/s2 += Integer.toHexString((short)a3);
                }
                else/*SL:1517*/ if (a3 instanceof Byte) {
                    /*SL:1518*/s2 += Integer.toHexString((byte)a3);
                }
                else {
                    /*SL:1521*/s2 += String.valueOf(a3).trim();
                }
                /*SL:1523*/s2 += property;
                /*SL:1524*/if (!iterator.hasNext()) {
                    /*SL:1525*/s2 = s2 + string + "}";
                }
            }
        }
        /*SL:1527*/if (v-9 == 0 && v-7) {
            final int n = /*EL:1528*/4;
            /*SL:1529*/s2 = s2 + property + "memory dump" + property;
            final byte[] byteArray = /*EL:1530*/this.getPointer().getByteArray(0L, this.size());
            /*SL:1531*/for (int v3 = 0; v3 < byteArray.length; ++v3) {
                /*SL:1532*/if (v3 % 4 == 0) {
                    s2 += "[";
                }
                /*SL:1533*/if (byteArray[v3] >= 0 && byteArray[v3] < 16) {
                    /*SL:1534*/s2 += "0";
                }
                /*SL:1535*/s2 += Integer.toHexString(byteArray[v3] & 0xFF);
                /*SL:1536*/if (v3 % 4 == 3 && v3 < byteArray.length - 1) {
                    /*SL:1537*/s2 = s2 + "]" + property;
                }
            }
            /*SL:1539*/s2 += "]";
        }
        /*SL:1541*/return s + " {" + s2;
    }
    
    public Structure[] toArray(final Structure[] v-1) {
        /*SL:1553*/this.ensureAllocated();
        /*SL:1554*/if (this.memory instanceof AutoAllocated) {
            final Memory a1 = /*EL:1556*/(Memory)this.memory;
            final int v1 = /*EL:1557*/v-1.length * this.size();
            /*SL:1558*/if (a1.size() < v1) {
                /*SL:1559*/this.useMemory(this.autoAllocate(v1));
            }
        }
        /*SL:1563*/v-1[0] = this;
        final int v2 = /*EL:1564*/this.size();
        /*SL:1565*/for (int v1 = 1; v1 < v-1.length; ++v1) {
            /*SL:1566*/(v-1[v1] = newInstance(this.getClass(), this.memory.share(v1 * v2, v2))).conditionalAutoRead();
        }
        /*SL:1570*/if (!(this instanceof ByValue)) {
            /*SL:1572*/this.array = v-1;
        }
        /*SL:1575*/return v-1;
    }
    
    public Structure[] toArray(final int a1) {
        /*SL:1588*/return this.toArray((Structure[])Array.newInstance(this.getClass(), a1));
    }
    
    private Class<?> baseClass() {
        /*SL:1592*/if ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())) {
            /*SL:1595*/return this.getClass().getSuperclass();
        }
        /*SL:1597*/return this.getClass();
    }
    
    public boolean dataEquals(final Structure a1) {
        /*SL:1606*/return this.dataEquals(a1, false);
    }
    
    public boolean dataEquals(final Structure v1, final boolean v2) {
        /*SL:1616*/if (v2) {
            /*SL:1617*/v1.getPointer().clear(v1.size());
            /*SL:1618*/v1.write();
            /*SL:1619*/this.getPointer().clear(this.size());
            /*SL:1620*/this.write();
        }
        final byte[] v3 = /*EL:1622*/v1.getPointer().getByteArray(0L, v1.size());
        final byte[] v4 = /*EL:1623*/this.getPointer().getByteArray(0L, this.size());
        /*SL:1624*/if (v3.length == v4.length) {
            /*SL:1625*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:1626*/if (v3[a1] != v4[a1]) {
                    /*SL:1627*/return false;
                }
            }
            /*SL:1630*/return true;
        }
        /*SL:1632*/return false;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1640*/return a1 instanceof Structure && a1.getClass() == /*EL:1641*/this.getClass() && ((Structure)a1).getPointer().equals(/*EL:1642*/this.getPointer());
    }
    
    @Override
    public int hashCode() {
        final Pointer v1 = /*EL:1650*/this.getPointer();
        /*SL:1651*/if (v1 != null) {
            /*SL:1652*/return this.getPointer().hashCode();
        }
        /*SL:1654*/return this.getClass().hashCode();
    }
    
    protected void cacheTypeInfo(final Pointer a1) {
        /*SL:1661*/this.typeInfo = a1.peer;
    }
    
    Pointer getFieldTypeInfo(final StructField v2) {
        Class<?> v3 = /*EL:1669*/v2.type;
        Object v4 = /*EL:1670*/this.getFieldValue(v2.field);
        /*SL:1671*/if (this.typeMapper != null) {
            final ToNativeConverter a1 = /*EL:1672*/this.typeMapper.getToNativeConverter(v3);
            /*SL:1673*/if (a1 != null) {
                /*SL:1674*/v3 = a1.nativeType();
                /*SL:1675*/v4 = a1.toNative(v4, new ToNativeContext());
            }
        }
        /*SL:1678*/return get(v4, v3);
    }
    
    Pointer getTypeInfo() {
        final Pointer v1 = getTypeInfo(/*EL:1685*/this);
        /*SL:1686*/this.cacheTypeInfo(v1);
        /*SL:1687*/return v1;
    }
    
    public void setAutoSynch(final boolean a1) {
        /*SL:1711*/this.setAutoRead(a1);
        /*SL:1712*/this.setAutoWrite(a1);
    }
    
    public void setAutoRead(final boolean a1) {
        /*SL:1720*/this.autoRead = a1;
    }
    
    public boolean getAutoRead() {
        /*SL:1728*/return this.autoRead;
    }
    
    public void setAutoWrite(final boolean a1) {
        /*SL:1736*/this.autoWrite = a1;
    }
    
    public boolean getAutoWrite() {
        /*SL:1744*/return this.autoWrite;
    }
    
    static Pointer getTypeInfo(final Object a1) {
        /*SL:1752*/return FFIType.get(a1);
    }
    
    private static Structure newInstance(final Class<?> v1, final long v2) {
        try {
            final Structure a1 = newInstance(/*EL:1761*/v1, (v2 == 0L) ? Structure.PLACEHOLDER_MEMORY : new Pointer(v2));
            /*SL:1762*/if (v2 != 0L) {
                /*SL:1763*/a1.conditionalAutoRead();
            }
            /*SL:1765*/return a1;
        }
        catch (Throwable a2) {
            System.err.println(/*EL:1768*/"JNA: Error creating structure: " + a2);
            /*SL:1769*/return null;
        }
    }
    
    public static Structure newInstance(final Class<?> v-1, final Pointer v0) throws IllegalArgumentException {
        try {
            final Constructor<?> a1 = /*EL:1782*/v-1.getConstructor(Pointer.class);
            /*SL:1783*/return (Structure)a1.newInstance(v0);
        }
        catch (NoSuchMethodException ex) {}
        catch (SecurityException ex2) {}
        catch (InstantiationException v) {
            final String a2 = /*EL:1792*/"Can't instantiate " + v-1;
            /*SL:1793*/throw new IllegalArgumentException(a2, v);
        }
        catch (IllegalAccessException v3) {
            final String v2 = /*EL:1796*/"Instantiation of " + v-1 + " (Pointer) not allowed, is it public?";
            /*SL:1797*/throw new IllegalArgumentException(v2, v3);
        }
        catch (InvocationTargetException v4) {
            final String v2 = /*EL:1800*/"Exception thrown while instantiating an instance of " + v-1;
            /*SL:1801*/v4.printStackTrace();
            /*SL:1802*/throw new IllegalArgumentException(v2, v4);
        }
        final Structure v5 = newInstance(/*EL:1804*/v-1);
        /*SL:1805*/if (v0 != Structure.PLACEHOLDER_MEMORY) {
            /*SL:1806*/v5.useMemory(v0);
        }
        /*SL:1808*/return v5;
    }
    
    public static Structure newInstance(final Class<?> v-1) throws IllegalArgumentException {
        try {
            final Structure a1 = /*EL:1818*/(Structure)v-1.newInstance();
            /*SL:1819*/if (a1 instanceof ByValue) {
                /*SL:1820*/a1.allocateMemory();
            }
            /*SL:1822*/return a1;
        }
        catch (InstantiationException v2) {
            final String v1 = /*EL:1825*/"Can't instantiate " + v-1;
            /*SL:1826*/throw new IllegalArgumentException(v1, v2);
        }
        catch (IllegalAccessException v3) {
            final String v1 = /*EL:1829*/"Instantiation of " + v-1 + " not allowed, is it public?";
            /*SL:1831*/throw new IllegalArgumentException(v1, v3);
        }
    }
    
    StructField typeInfoField() {
        final LayoutInfo v1;
        /*SL:1841*/synchronized (Structure.layoutInfo) {
            /*SL:1842*/v1 = Structure.layoutInfo.get(this.getClass());
        }
        /*SL:1844*/if (v1 != null) {
            /*SL:1845*/return v1.typeInfoField;
        }
        /*SL:1847*/return null;
    }
    
    private static void structureArrayCheck(final Structure[] v-2) {
        /*SL:2045*/if (ByReference[].class.isAssignableFrom(v-2.getClass())) {
            /*SL:2046*/return;
        }
        final Pointer pointer = /*EL:2048*/v-2[0].getPointer();
        final int v0 = /*EL:2049*/v-2[0].size();
        /*SL:2050*/for (int v = 1; v < v-2.length; ++v) {
            /*SL:2051*/if (v-2[v].getPointer().peer != pointer.peer + v0 * v) {
                final String a1 = /*EL:2052*/"Structure array elements must use contiguous memory (bad backing address at Structure array index " + v + ")";
                /*SL:2054*/throw new IllegalArgumentException(a1);
            }
        }
    }
    
    public static void autoRead(final Structure[] v1) {
        structureArrayCheck(/*EL:2060*/v1);
        /*SL:2061*/if (v1[0].array == v1) {
            /*SL:2062*/v1[0].autoRead();
        }
        else {
            /*SL:2065*/for (int a1 = 0; a1 < v1.length; ++a1) {
                /*SL:2066*/if (v1[a1] != null) {
                    /*SL:2067*/v1[a1].autoRead();
                }
            }
        }
    }
    
    public void autoRead() {
        /*SL:2074*/if (this.getAutoRead()) {
            /*SL:2075*/this.read();
            /*SL:2076*/if (this.array != null) {
                /*SL:2077*/for (int v1 = 1; v1 < this.array.length; ++v1) {
                    /*SL:2078*/this.array[v1].autoRead();
                }
            }
        }
    }
    
    public static void autoWrite(final Structure[] v1) {
        structureArrayCheck(/*EL:2085*/v1);
        /*SL:2086*/if (v1[0].array == v1) {
            /*SL:2087*/v1[0].autoWrite();
        }
        else {
            /*SL:2090*/for (int a1 = 0; a1 < v1.length; ++a1) {
                /*SL:2091*/if (v1[a1] != null) {
                    /*SL:2092*/v1[a1].autoWrite();
                }
            }
        }
    }
    
    public void autoWrite() {
        /*SL:2099*/if (this.getAutoWrite()) {
            /*SL:2100*/this.write();
            /*SL:2101*/if (this.array != null) {
                /*SL:2102*/for (int v1 = 1; v1 < this.array.length; ++v1) {
                    /*SL:2103*/this.array[v1].autoWrite();
                }
            }
        }
    }
    
    protected int getNativeSize(final Class<?> a1) {
        /*SL:2115*/return this.getNativeSize(a1, null);
    }
    
    protected int getNativeSize(final Class<?> a1, final Object a2) {
        /*SL:2125*/return Native.getNativeSize(a1, a2);
    }
    
    static void validate(final Class<?> a1) {
        newInstance(/*EL:2140*/a1, Structure.PLACEHOLDER_MEMORY);
    }
    
    static {
        layoutInfo = new WeakHashMap<Class<?>, LayoutInfo>();
        fieldOrder = new WeakHashMap<Class<?>, List<String>>();
        reads = new ThreadLocal<Map<Pointer, Structure>>() {
            @Override
            protected synchronized Map<Pointer, Structure> initialValue() {
                /*SL:457*/return new HashMap<Pointer, Structure>();
            }
        };
        busy = new ThreadLocal<Set<Structure>>() {
            @Override
            protected synchronized Set<Structure> initialValue() {
                /*SL:466*/return new StructureSet();
            }
        };
        PLACEHOLDER_MEMORY = new Pointer(0L) {
            @Override
            public Pointer share(final long a1, final long a2) {
                /*SL:2133*/return this;
            }
        };
    }
    
    static class StructureSet extends AbstractCollection<Structure> implements Set<Structure>
    {
        Structure[] elements;
        private int count;
        
        private void ensureCapacity(final int v2) {
            /*SL:477*/if (this.elements == null) {
                /*SL:478*/this.elements = new Structure[v2 * 3 / 2];
            }
            else/*SL:480*/ if (this.elements.length < v2) {
                final Structure[] a1 = /*EL:481*/new Structure[v2 * 3 / 2];
                /*SL:482*/System.arraycopy(this.elements, 0, a1, 0, this.elements.length);
                /*SL:483*/this.elements = a1;
            }
        }
        
        public Structure[] getElements() {
            /*SL:487*/return this.elements;
        }
        
        @Override
        public int size() {
            /*SL:490*/return this.count;
        }
        
        @Override
        public boolean contains(final Object a1) {
            /*SL:493*/return this.indexOf((Structure)a1) != -1;
        }
        
        @Override
        public boolean add(final Structure a1) {
            /*SL:497*/if (!this.contains(a1)) {
                /*SL:498*/this.ensureCapacity(this.count + 1);
                /*SL:499*/this.elements[this.count++] = a1;
            }
            /*SL:501*/return true;
        }
        
        private int indexOf(final Structure v0) {
            /*SL:504*/for (int v = 0; v < this.count; ++v) {
                final Structure a1 = /*EL:505*/this.elements[v];
                /*SL:506*/if (v0 == a1 || (v0.getClass() == /*EL:507*/a1.getClass() && v0.size() == /*EL:508*/a1.size() && v0.getPointer().equals(/*EL:509*/a1.getPointer()))) {
                    /*SL:510*/return v;
                }
            }
            /*SL:513*/return -1;
        }
        
        @Override
        public boolean remove(final Object a1) {
            final int v1 = /*EL:517*/this.indexOf((Structure)a1);
            /*SL:518*/if (v1 != -1) {
                /*SL:519*/if (--this.count >= 0) {
                    /*SL:520*/this.elements[v1] = this.elements[this.count];
                    /*SL:521*/this.elements[this.count] = null;
                }
                /*SL:523*/return true;
            }
            /*SL:525*/return false;
        }
        
        @Override
        public Iterator<Structure> iterator() {
            final Structure[] v1 = /*EL:532*/new Structure[this.count];
            /*SL:533*/if (this.count > 0) {
                /*SL:534*/System.arraycopy(this.elements, 0, v1, 0, this.count);
            }
            /*SL:536*/return Arrays.<Structure>asList(v1).iterator();
        }
    }
    
    private static class LayoutInfo
    {
        private int size;
        private int alignment;
        private final Map<String, StructField> fields;
        private int alignType;
        private TypeMapper typeMapper;
        private boolean variable;
        private StructField typeInfoField;
        
        private LayoutInfo() {
            this.size = -1;
            this.alignment = 1;
            this.fields = Collections.<String, StructField>synchronizedMap(new LinkedHashMap<String, StructField>());
            this.alignType = 0;
        }
    }
    
    protected static class StructField
    {
        public String name;
        public Class<?> type;
        public Field field;
        public int size;
        public int offset;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;
        
        protected StructField() {
            this.size = -1;
            this.offset = -1;
        }
        
        @Override
        public String toString() {
            /*SL:1863*/return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
        }
    }
    
    static class FFIType extends Structure
    {
        private static final Map<Object, Object> typeInfoMap;
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type;
        public Pointer elements;
        
        private FFIType(final Structure v0) {
            this.type = 13;
            v0.ensureAllocated(true);
            Pointer[] v;
            if (v0 instanceof Union) {
                final StructField a1 = ((Union)v0).typeInfoField();
                v = new Pointer[] { get(v0.getFieldValue(a1.field), a1.type), null };
            }
            else {
                v = new Pointer[v0.fields().size() + 1];
                int v2 = 0;
                for (final StructField v3 : v0.fields().values()) {
                    v[v2++] = v0.getFieldTypeInfo(v3);
                }
            }
            this.init(v);
        }
        
        private FFIType(final Object v1, final Class<?> v2) {
            this.type = 13;
            final int v3 = Array.getLength(v1);
            final Pointer[] v4 = new Pointer[v3 + 1];
            final Pointer v5 = get(null, v2.getComponentType());
            for (int a1 = 0; a1 < v3; ++a1) {
                v4[a1] = v5;
            }
            this.init(v4);
        }
        
        @Override
        protected List<String> getFieldOrder() {
            /*SL:1969*/return Arrays.<String>asList("size", "alignment", "type", "elements");
        }
        
        private void init(final Pointer[] a1) {
            /*SL:1972*/(this.elements = new Memory(Pointer.SIZE * a1.length)).write(/*EL:1973*/0L, a1, 0, a1.length);
            /*SL:1974*/this.write();
        }
        
        static Pointer get(final Object a1) {
            /*SL:1979*/if (a1 == null) {
                /*SL:1980*/return FFITypes.ffi_type_pointer;
            }
            /*SL:1981*/if (a1 instanceof Class) {
                /*SL:1982*/return get(null, (Class<?>)a1);
            }
            /*SL:1983*/return get(a1, a1.getClass());
        }
        
        private static Pointer get(Object v-4, Class<?> v-3) {
            final TypeMapper typeMapper = /*EL:1987*/Native.getTypeMapper(v-3);
            /*SL:1988*/if (typeMapper != null) {
                final ToNativeConverter a1 = /*EL:1989*/typeMapper.getToNativeConverter(v-3);
                /*SL:1990*/if (a1 != null) {
                    /*SL:1991*/v-3 = a1.nativeType();
                }
            }
            /*SL:1994*/synchronized (FFIType.typeInfoMap) {
                final Object v0 = FFIType.typeInfoMap.get(/*EL:1995*/v-3);
                /*SL:1996*/if (v0 instanceof Pointer) {
                    /*SL:1997*/return (Pointer)v0;
                }
                /*SL:1999*/if (v0 instanceof FFIType) {
                    /*SL:2000*/return ((FFIType)v0).getPointer();
                }
                /*SL:2002*/if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(v-3)) || Callback.class.isAssignableFrom(v-3)) {
                    FFIType.typeInfoMap.put(/*EL:2004*/v-3, FFITypes.ffi_type_pointer);
                    /*SL:2005*/return FFITypes.ffi_type_pointer;
                }
                /*SL:2007*/if (Structure.class.isAssignableFrom(v-3)) {
                    /*SL:2008*/if (v-4 == null) {
                        v-4 = Structure.newInstance(v-3, Structure.PLACEHOLDER_MEMORY);
                    }
                    /*SL:2009*/if (ByReference.class.isAssignableFrom(v-3)) {
                        FFIType.typeInfoMap.put(/*EL:2010*/v-3, FFITypes.ffi_type_pointer);
                        /*SL:2011*/return FFITypes.ffi_type_pointer;
                    }
                    final FFIType a2 = /*EL:2013*/new FFIType((Structure)v-4);
                    FFIType.typeInfoMap.put(/*EL:2014*/v-3, a2);
                    /*SL:2015*/return a2.getPointer();
                }
                else {
                    /*SL:2017*/if (NativeMapped.class.isAssignableFrom(v-3)) {
                        final NativeMappedConverter v = /*EL:2018*/NativeMappedConverter.getInstance(v-3);
                        /*SL:2019*/return get(v.toNative(v-4, new ToNativeContext()), v.nativeType());
                    }
                    /*SL:2021*/if (v-3.isArray()) {
                        final FFIType v2 = /*EL:2022*/new FFIType(v-4, v-3);
                        FFIType.typeInfoMap.put(/*EL:2024*/v-4, v2);
                        /*SL:2025*/return v2.getPointer();
                    }
                    /*SL:2027*/throw new IllegalArgumentException("Unsupported type " + v-3);
                }
            }
        }
        
        static {
            typeInfoMap = new WeakHashMap<Object, Object>();
            if (Native.POINTER_SIZE == 0) {
                throw new Error("Native library not initialized");
            }
            if (FFITypes.ffi_type_void == null) {
                throw new Error("FFI types not initialized");
            }
            FFIType.typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
            FFIType.typeInfoMap.put(Void.class, FFITypes.ffi_type_void);
            FFIType.typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
            FFIType.typeInfoMap.put(Float.class, FFITypes.ffi_type_float);
            FFIType.typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
            FFIType.typeInfoMap.put(Double.class, FFITypes.ffi_type_double);
            FFIType.typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
            FFIType.typeInfoMap.put(Long.class, FFITypes.ffi_type_sint64);
            FFIType.typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
            FFIType.typeInfoMap.put(Integer.class, FFITypes.ffi_type_sint32);
            FFIType.typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
            FFIType.typeInfoMap.put(Short.class, FFITypes.ffi_type_sint16);
            final Pointer v1 = (Native.WCHAR_SIZE == 2) ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
            FFIType.typeInfoMap.put(Character.TYPE, v1);
            FFIType.typeInfoMap.put(Character.class, v1);
            FFIType.typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
            FFIType.typeInfoMap.put(Byte.class, FFITypes.ffi_type_sint8);
            FFIType.typeInfoMap.put(Pointer.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(String.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(WString.class, FFITypes.ffi_type_pointer);
            FFIType.typeInfoMap.put(Boolean.TYPE, FFITypes.ffi_type_uint32);
            FFIType.typeInfoMap.put(Boolean.class, FFITypes.ffi_type_uint32);
        }
        
        public static class size_t extends IntegerType
        {
            private static final long serialVersionUID = 1L;
            
            public size_t() {
                this(0L);
            }
            
            public size_t(final long a1) {
                super(Native.SIZE_T_SIZE, a1);
            }
        }
        
        private static class FFITypes
        {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;
        }
    }
    
    private static class AutoAllocated extends Memory
    {
        public AutoAllocated(final int a1) {
            super(a1);
            super.clear();
        }
        
        @Override
        public String toString() {
            /*SL:2040*/return "auto-" + super.toString();
        }
    }
    
    public interface ByReference
    {
    }
    
    public interface ByValue
    {
    }
}
