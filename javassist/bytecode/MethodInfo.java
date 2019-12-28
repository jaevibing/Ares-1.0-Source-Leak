package javassist.bytecode;

import java.io.DataOutputStream;
import javassist.bytecode.stackmap.MapMaker;
import javassist.ClassPool;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;

public class MethodInfo
{
    ConstPool constPool;
    int accessFlags;
    int name;
    String cachedName;
    int descriptor;
    ArrayList attribute;
    public static boolean doPreverify;
    public static final String nameInit = "<init>";
    public static final String nameClinit = "<clinit>";
    
    private MethodInfo(final ConstPool a1) {
        this.constPool = a1;
        this.attribute = null;
    }
    
    public MethodInfo(final ConstPool a1, final String a2, final String a3) {
        this(a1);
        this.accessFlags = 0;
        this.name = a1.addUtf8Info(a2);
        this.cachedName = a2;
        this.descriptor = this.constPool.addUtf8Info(a3);
    }
    
    MethodInfo(final ConstPool a1, final DataInputStream a2) throws IOException {
        this(a1);
        this.read(a2);
    }
    
    public MethodInfo(final ConstPool a1, final String a2, final MethodInfo a3, final Map a4) throws BadBytecode {
        this(a1);
        this.read(a3, a2, a4);
    }
    
    @Override
    public String toString() {
        /*SL:141*/return this.getName() + " " + this.getDescriptor();
    }
    
    void compact(final ConstPool a1) {
        /*SL:153*/this.name = a1.addUtf8Info(this.getName());
        /*SL:154*/this.descriptor = a1.addUtf8Info(this.getDescriptor());
        /*SL:155*/this.attribute = AttributeInfo.copyAll(this.attribute, a1);
        /*SL:156*/this.constPool = a1;
    }
    
    void prune(final ConstPool a1) {
        final ArrayList v1 = /*EL:160*/new ArrayList();
        AttributeInfo v2 = /*EL:162*/this.getAttribute("RuntimeInvisibleAnnotations");
        /*SL:164*/if (v2 != null) {
            /*SL:165*/v2 = v2.copy(a1, null);
            /*SL:166*/v1.add(v2);
        }
        AttributeInfo v3 = /*EL:169*/this.getAttribute("RuntimeVisibleAnnotations");
        /*SL:171*/if (v3 != null) {
            /*SL:172*/v3 = v3.copy(a1, null);
            /*SL:173*/v1.add(v3);
        }
        AttributeInfo v4 = /*EL:176*/this.getAttribute("RuntimeInvisibleParameterAnnotations");
        /*SL:178*/if (v4 != null) {
            /*SL:179*/v4 = v4.copy(a1, null);
            /*SL:180*/v1.add(v4);
        }
        AttributeInfo v5 = /*EL:183*/this.getAttribute("RuntimeVisibleParameterAnnotations");
        /*SL:185*/if (v5 != null) {
            /*SL:186*/v5 = v5.copy(a1, null);
            /*SL:187*/v1.add(v5);
        }
        final AnnotationDefaultAttribute v6 = /*EL:190*/(AnnotationDefaultAttribute)this.getAttribute("AnnotationDefault");
        /*SL:192*/if (v6 != null) {
            /*SL:193*/v1.add(v6);
        }
        final ExceptionsAttribute v7 = /*EL:195*/this.getExceptionsAttribute();
        /*SL:196*/if (v7 != null) {
            /*SL:197*/v1.add(v7);
        }
        AttributeInfo v8 = /*EL:199*/this.getAttribute("Signature");
        /*SL:201*/if (v8 != null) {
            /*SL:202*/v8 = v8.copy(a1, null);
            /*SL:203*/v1.add(v8);
        }
        /*SL:206*/this.attribute = v1;
        /*SL:207*/this.name = a1.addUtf8Info(this.getName());
        /*SL:208*/this.descriptor = a1.addUtf8Info(this.getDescriptor());
        /*SL:209*/this.constPool = a1;
    }
    
    public String getName() {
        /*SL:216*/if (this.cachedName == null) {
            /*SL:217*/this.cachedName = this.constPool.getUtf8Info(this.name);
        }
        /*SL:219*/return this.cachedName;
    }
    
    public void setName(final String a1) {
        /*SL:226*/this.name = this.constPool.addUtf8Info(a1);
        /*SL:227*/this.cachedName = a1;
    }
    
    public boolean isMethod() {
        final String v1 = /*EL:235*/this.getName();
        /*SL:236*/return !v1.equals("<init>") && !v1.equals("<clinit>");
    }
    
    public ConstPool getConstPool() {
        /*SL:243*/return this.constPool;
    }
    
    public boolean isConstructor() {
        /*SL:250*/return this.getName().equals("<init>");
    }
    
    public boolean isStaticInitializer() {
        /*SL:257*/return this.getName().equals("<clinit>");
    }
    
    public int getAccessFlags() {
        /*SL:266*/return this.accessFlags;
    }
    
    public void setAccessFlags(final int a1) {
        /*SL:275*/this.accessFlags = a1;
    }
    
    public String getDescriptor() {
        /*SL:284*/return this.constPool.getUtf8Info(this.descriptor);
    }
    
    public void setDescriptor(final String a1) {
        /*SL:293*/if (!a1.equals(this.getDescriptor())) {
            /*SL:294*/this.descriptor = this.constPool.addUtf8Info(a1);
        }
    }
    
    public List getAttributes() {
        /*SL:308*/if (this.attribute == null) {
            /*SL:309*/this.attribute = new ArrayList();
        }
        /*SL:311*/return this.attribute;
    }
    
    public AttributeInfo getAttribute(final String a1) {
        /*SL:328*/return AttributeInfo.lookup(this.attribute, a1);
    }
    
    public AttributeInfo removeAttribute(final String a1) {
        /*SL:339*/return AttributeInfo.remove(this.attribute, a1);
    }
    
    public void addAttribute(final AttributeInfo a1) {
        /*SL:349*/if (this.attribute == null) {
            /*SL:350*/this.attribute = new ArrayList();
        }
        /*SL:352*/AttributeInfo.remove(this.attribute, a1.getName());
        /*SL:353*/this.attribute.add(a1);
    }
    
    public ExceptionsAttribute getExceptionsAttribute() {
        final AttributeInfo v1 = /*EL:362*/AttributeInfo.lookup(this.attribute, "Exceptions");
        /*SL:364*/return (ExceptionsAttribute)v1;
    }
    
    public CodeAttribute getCodeAttribute() {
        final AttributeInfo v1 = /*EL:373*/AttributeInfo.lookup(this.attribute, "Code");
        /*SL:374*/return (CodeAttribute)v1;
    }
    
    public void removeExceptionsAttribute() {
        /*SL:381*/AttributeInfo.remove(this.attribute, "Exceptions");
    }
    
    public void setExceptionsAttribute(final ExceptionsAttribute a1) {
        /*SL:392*/this.removeExceptionsAttribute();
        /*SL:393*/if (this.attribute == null) {
            /*SL:394*/this.attribute = new ArrayList();
        }
        /*SL:396*/this.attribute.add(a1);
    }
    
    public void removeCodeAttribute() {
        /*SL:403*/AttributeInfo.remove(this.attribute, "Code");
    }
    
    public void setCodeAttribute(final CodeAttribute a1) {
        /*SL:414*/this.removeCodeAttribute();
        /*SL:415*/if (this.attribute == null) {
            /*SL:416*/this.attribute = new ArrayList();
        }
        /*SL:418*/this.attribute.add(a1);
    }
    
    public void rebuildStackMapIf6(final ClassPool a1, final ClassFile a2) throws BadBytecode {
        /*SL:437*/if (a2.getMajorVersion() >= 50) {
            /*SL:438*/this.rebuildStackMap(a1);
        }
        /*SL:440*/if (MethodInfo.doPreverify) {
            /*SL:441*/this.rebuildStackMapForME(a1);
        }
    }
    
    public void rebuildStackMap(final ClassPool v2) throws BadBytecode {
        final CodeAttribute v3 = /*EL:454*/this.getCodeAttribute();
        /*SL:455*/if (v3 != null) {
            final StackMapTable a1 = /*EL:456*/MapMaker.make(v2, this);
            /*SL:457*/v3.setAttribute(a1);
        }
    }
    
    public void rebuildStackMapForME(final ClassPool v2) throws BadBytecode {
        final CodeAttribute v3 = /*EL:471*/this.getCodeAttribute();
        /*SL:472*/if (v3 != null) {
            final StackMap a1 = /*EL:473*/MapMaker.make2(v2, this);
            /*SL:474*/v3.setAttribute(a1);
        }
    }
    
    public int getLineNumber(final int a1) {
        final CodeAttribute v1 = /*EL:488*/this.getCodeAttribute();
        /*SL:489*/if (v1 == null) {
            /*SL:490*/return -1;
        }
        final LineNumberAttribute v2 = /*EL:492*/(LineNumberAttribute)v1.getAttribute("LineNumberTable");
        /*SL:494*/if (v2 == null) {
            /*SL:495*/return -1;
        }
        /*SL:497*/return v2.toLineNumber(a1);
    }
    
    public void setSuperclass(final String v-5) throws BadBytecode {
        /*SL:522*/if (!this.isConstructor()) {
            /*SL:523*/return;
        }
        final CodeAttribute codeAttribute = /*EL:525*/this.getCodeAttribute();
        final byte[] code = /*EL:526*/codeAttribute.getCode();
        final CodeIterator iterator = /*EL:527*/codeAttribute.iterator();
        final int skipSuperConstructor = /*EL:528*/iterator.skipSuperConstructor();
        /*SL:529*/if (skipSuperConstructor >= 0) {
            final ConstPool a1 = /*EL:530*/this.constPool;
            final int v1 = /*EL:531*/ByteArray.readU16bit(code, skipSuperConstructor + 1);
            final int v2 = /*EL:532*/a1.getMethodrefNameAndType(v1);
            final int v3 = /*EL:533*/a1.addClassInfo(v-5);
            final int v4 = /*EL:534*/a1.addMethodrefInfo(v3, v2);
            /*SL:535*/ByteArray.write16bit(v4, code, skipSuperConstructor + 1);
        }
    }
    
    private void read(final MethodInfo a1, final String a2, final Map a3) throws BadBytecode {
        final ConstPool v1 = /*EL:541*/this.constPool;
        /*SL:542*/this.accessFlags = a1.accessFlags;
        /*SL:543*/this.name = v1.addUtf8Info(a2);
        /*SL:544*/this.cachedName = a2;
        final ConstPool v2 = /*EL:545*/a1.constPool;
        final String v3 = /*EL:546*/v2.getUtf8Info(a1.descriptor);
        final String v4 = /*EL:547*/Descriptor.rename(v3, a3);
        /*SL:548*/this.descriptor = v1.addUtf8Info(v4);
        /*SL:550*/this.attribute = new ArrayList();
        final ExceptionsAttribute v5 = /*EL:551*/a1.getExceptionsAttribute();
        /*SL:552*/if (v5 != null) {
            /*SL:553*/this.attribute.add(v5.copy(v1, a3));
        }
        final CodeAttribute v6 = /*EL:555*/a1.getCodeAttribute();
        /*SL:556*/if (v6 != null) {
            /*SL:557*/this.attribute.add(v6.copy(v1, a3));
        }
    }
    
    private void read(final DataInputStream v2) throws IOException {
        /*SL:561*/this.accessFlags = v2.readUnsignedShort();
        /*SL:562*/this.name = v2.readUnsignedShort();
        /*SL:563*/this.descriptor = v2.readUnsignedShort();
        final int v3 = /*EL:564*/v2.readUnsignedShort();
        /*SL:565*/this.attribute = new ArrayList();
        /*SL:566*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:567*/this.attribute.add(AttributeInfo.read(this.constPool, v2));
        }
    }
    
    void write(final DataOutputStream a1) throws IOException {
        /*SL:571*/a1.writeShort(this.accessFlags);
        /*SL:572*/a1.writeShort(this.name);
        /*SL:573*/a1.writeShort(this.descriptor);
        /*SL:575*/if (this.attribute == null) {
            /*SL:576*/a1.writeShort(0);
        }
        else {
            /*SL:578*/a1.writeShort(this.attribute.size());
            /*SL:579*/AttributeInfo.writeAll(this.attribute, a1);
        }
    }
    
    static {
        MethodInfo.doPreverify = false;
    }
}
