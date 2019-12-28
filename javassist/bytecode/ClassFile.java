package javassist.bytecode;

import java.io.DataOutputStream;
import java.util.ListIterator;
import java.util.List;
import javassist.CannotCompileException;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;

public final class ClassFile
{
    int major;
    int minor;
    ConstPool constPool;
    int thisClass;
    int accessFlags;
    int superClass;
    int[] interfaces;
    ArrayList fields;
    ArrayList methods;
    ArrayList attributes;
    String thisclassname;
    String[] cachedInterfaces;
    String cachedSuperclass;
    public static final int JAVA_1 = 45;
    public static final int JAVA_2 = 46;
    public static final int JAVA_3 = 47;
    public static final int JAVA_4 = 48;
    public static final int JAVA_5 = 49;
    public static final int JAVA_6 = 50;
    public static final int JAVA_7 = 51;
    public static final int JAVA_8 = 52;
    public static final int MAJOR_VERSION;
    
    public ClassFile(final DataInputStream a1) throws IOException {
        this.read(a1);
    }
    
    public ClassFile(final boolean a1, final String a2, final String a3) {
        this.major = ClassFile.MAJOR_VERSION;
        this.minor = 0;
        this.constPool = new ConstPool(a2);
        this.thisClass = this.constPool.getThisClassInfo();
        if (a1) {
            this.accessFlags = 1536;
        }
        else {
            this.accessFlags = 32;
        }
        this.initSuperclass(a3);
        this.interfaces = null;
        this.fields = new ArrayList();
        this.methods = new ArrayList();
        this.thisclassname = a2;
        (this.attributes = new ArrayList()).add(new SourceFileAttribute(this.constPool, getSourcefileName(this.thisclassname)));
    }
    
    private void initSuperclass(final String a1) {
        /*SL:189*/if (a1 != null) {
            /*SL:190*/this.superClass = this.constPool.addClassInfo(a1);
            /*SL:191*/this.cachedSuperclass = a1;
        }
        else {
            /*SL:194*/this.superClass = this.constPool.addClassInfo("java.lang.Object");
            /*SL:195*/this.cachedSuperclass = "java.lang.Object";
        }
    }
    
    private static String getSourcefileName(String a1) {
        final int v1 = /*EL:200*/a1.lastIndexOf(46);
        /*SL:201*/if (v1 >= 0) {
            /*SL:202*/a1 = a1.substring(v1 + 1);
        }
        /*SL:204*/return a1 + ".java";
    }
    
    public void compact() {
        final ConstPool compact0 = /*EL:213*/this.compact0();
        ArrayList list = /*EL:214*/this.methods;
        /*SL:216*/for (int n = list.size(), v0 = 0; v0 < n; ++v0) {
            final MethodInfo v = /*EL:217*/list.get(v0);
            /*SL:218*/v.compact(compact0);
        }
        /*SL:221*/list = this.fields;
        /*SL:223*/for (int n = list.size(), v0 = 0; v0 < n; ++v0) {
            final FieldInfo v2 = /*EL:224*/list.get(v0);
            /*SL:225*/v2.compact(compact0);
        }
        /*SL:228*/this.attributes = AttributeInfo.copyAll(this.attributes, compact0);
        /*SL:229*/this.constPool = compact0;
    }
    
    private ConstPool compact0() {
        final ConstPool constPool = /*EL:233*/new ConstPool(this.thisclassname);
        /*SL:234*/this.thisClass = constPool.getThisClassInfo();
        final String superclass = /*EL:235*/this.getSuperclass();
        /*SL:236*/if (superclass != null) {
            /*SL:237*/this.superClass = constPool.addClassInfo(this.getSuperclass());
        }
        /*SL:239*/if (this.interfaces != null) {
            /*SL:241*/for (int v0 = this.interfaces.length, v = 0; v < v0; ++v) {
                /*SL:243*/this.interfaces[v] = constPool.addClassInfo(this.constPool.getClassInfo(this.interfaces[v]));
            }
        }
        /*SL:246*/return constPool;
    }
    
    public void prune() {
        final ConstPool compact0 = /*EL:256*/this.compact0();
        final ArrayList attributes = /*EL:257*/new ArrayList();
        AttributeInfo attributeInfo = /*EL:258*/this.getAttribute("RuntimeInvisibleAnnotations");
        /*SL:260*/if (attributeInfo != null) {
            /*SL:261*/attributeInfo = attributeInfo.copy(compact0, null);
            /*SL:262*/attributes.add(attributeInfo);
        }
        AttributeInfo attributeInfo2 = /*EL:265*/this.getAttribute("RuntimeVisibleAnnotations");
        /*SL:267*/if (attributeInfo2 != null) {
            /*SL:268*/attributeInfo2 = attributeInfo2.copy(compact0, null);
            /*SL:269*/attributes.add(attributeInfo2);
        }
        AttributeInfo attributeInfo3 = /*EL:272*/this.getAttribute("Signature");
        /*SL:274*/if (attributeInfo3 != null) {
            /*SL:275*/attributeInfo3 = attributeInfo3.copy(compact0, null);
            /*SL:276*/attributes.add(attributeInfo3);
        }
        ArrayList list = /*EL:279*/this.methods;
        /*SL:281*/for (int n = list.size(), v0 = 0; v0 < n; ++v0) {
            final MethodInfo v = /*EL:282*/list.get(v0);
            /*SL:283*/v.prune(compact0);
        }
        /*SL:286*/list = this.fields;
        /*SL:288*/for (int n = list.size(), v0 = 0; v0 < n; ++v0) {
            final FieldInfo v2 = /*EL:289*/list.get(v0);
            /*SL:290*/v2.prune(compact0);
        }
        /*SL:293*/this.attributes = attributes;
        /*SL:294*/this.constPool = compact0;
    }
    
    public ConstPool getConstPool() {
        /*SL:301*/return this.constPool;
    }
    
    public boolean isInterface() {
        /*SL:308*/return (this.accessFlags & 0x200) != 0x0;
    }
    
    public boolean isFinal() {
        /*SL:315*/return (this.accessFlags & 0x10) != 0x0;
    }
    
    public boolean isAbstract() {
        /*SL:322*/return (this.accessFlags & 0x400) != 0x0;
    }
    
    public int getAccessFlags() {
        /*SL:331*/return this.accessFlags;
    }
    
    public void setAccessFlags(int a1) {
        /*SL:340*/if ((a1 & 0x200) == 0x0) {
            /*SL:341*/a1 |= 0x20;
        }
        /*SL:343*/this.accessFlags = a1;
    }
    
    public int getInnerAccessFlags() {
        final InnerClassesAttribute innerClassesAttribute = /*EL:355*/(InnerClassesAttribute)this.getAttribute("InnerClasses");
        /*SL:357*/if (innerClassesAttribute == null) {
            /*SL:358*/return -1;
        }
        final String name = /*EL:360*/this.getName();
        /*SL:362*/for (int v0 = innerClassesAttribute.tableLength(), v = 0; v < v0; ++v) {
            /*SL:363*/if (name.equals(innerClassesAttribute.innerClass(v))) {
                /*SL:364*/return innerClassesAttribute.accessFlags(v);
            }
        }
        /*SL:366*/return -1;
    }
    
    public String getName() {
        /*SL:373*/return this.thisclassname;
    }
    
    public void setName(final String a1) {
        /*SL:381*/this.renameClass(this.thisclassname, a1);
    }
    
    public String getSuperclass() {
        /*SL:388*/if (this.cachedSuperclass == null) {
            /*SL:389*/this.cachedSuperclass = this.constPool.getClassInfo(this.superClass);
        }
        /*SL:391*/return this.cachedSuperclass;
    }
    
    public int getSuperclassId() {
        /*SL:399*/return this.superClass;
    }
    
    public void setSuperclass(String v-2) throws CannotCompileException {
        /*SL:411*/if (v-2 == null) {
            /*SL:412*/v-2 = "java.lang.Object";
        }
        try {
            /*SL:415*/this.superClass = this.constPool.addClassInfo(v-2);
            final ArrayList methods = /*EL:416*/this.methods;
            /*SL:418*/for (int v0 = methods.size(), v = 0; v < v0; ++v) {
                final MethodInfo a1 = /*EL:419*/methods.get(v);
                /*SL:420*/a1.setSuperclass(v-2);
            }
        }
        catch (BadBytecode a2) {
            /*SL:424*/throw new CannotCompileException(a2);
        }
        /*SL:426*/this.cachedSuperclass = v-2;
    }
    
    public final void renameClass(String v-3, String v-2) {
        /*SL:447*/if (v-3.equals(v-2)) {
            /*SL:448*/return;
        }
        /*SL:450*/if (v-3.equals(this.thisclassname)) {
            /*SL:451*/this.thisclassname = v-2;
        }
        /*SL:453*/v-3 = Descriptor.toJvmName(v-3);
        /*SL:454*/v-2 = Descriptor.toJvmName(v-2);
        /*SL:455*/this.constPool.renameClass(v-3, v-2);
        /*SL:457*/AttributeInfo.renameClass(this.attributes, v-3, v-2);
        ArrayList list = /*EL:458*/this.methods;
        /*SL:460*/for (int v0 = list.size(), v = 0; v < v0; ++v) {
            final MethodInfo a1 = /*EL:461*/list.get(v);
            final String a2 = /*EL:462*/a1.getDescriptor();
            /*SL:463*/a1.setDescriptor(Descriptor.rename(a2, v-3, v-2));
            /*SL:464*/AttributeInfo.renameClass(a1.getAttributes(), v-3, v-2);
        }
        /*SL:467*/list = this.fields;
        /*SL:469*/for (int v0 = list.size(), v = 0; v < v0; ++v) {
            final FieldInfo v2 = /*EL:470*/list.get(v);
            final String v3 = /*EL:471*/v2.getDescriptor();
            /*SL:472*/v2.setDescriptor(Descriptor.rename(v3, v-3, v-2));
            /*SL:473*/AttributeInfo.renameClass(v2.getAttributes(), v-3, v-2);
        }
    }
    
    public final void renameClass(final Map v-5) {
        final String a2 = /*EL:487*/v-5.get(/*EL:488*/Descriptor.toJvmName(this.thisclassname));
        /*SL:489*/if (a2 != null) {
            /*SL:490*/this.thisclassname = Descriptor.toJavaName(a2);
        }
        /*SL:492*/this.constPool.renameClass(v-5);
        /*SL:494*/AttributeInfo.renameClass(this.attributes, v-5);
        ArrayList list = /*EL:495*/this.methods;
        /*SL:497*/for (int n = list.size(), i = 0; i < n; ++i) {
            final MethodInfo a1 = /*EL:498*/list.get(i);
            final String v1 = /*EL:499*/a1.getDescriptor();
            /*SL:500*/a1.setDescriptor(Descriptor.rename(v1, v-5));
            /*SL:501*/AttributeInfo.renameClass(a1.getAttributes(), v-5);
        }
        /*SL:504*/list = this.fields;
        /*SL:506*/for (int n = list.size(), i = 0; i < n; ++i) {
            final FieldInfo v2 = /*EL:507*/list.get(i);
            final String v1 = /*EL:508*/v2.getDescriptor();
            /*SL:509*/v2.setDescriptor(Descriptor.rename(v1, v-5));
            /*SL:510*/AttributeInfo.renameClass(v2.getAttributes(), v-5);
        }
    }
    
    public final void getRefClasses(final Map v-4) {
        /*SL:519*/this.constPool.renameClass(v-4);
        /*SL:521*/AttributeInfo.getRefClasses(this.attributes, v-4);
        ArrayList list = /*EL:522*/this.methods;
        /*SL:524*/for (int n = list.size(), i = 0; i < n; ++i) {
            final MethodInfo a1 = /*EL:525*/list.get(i);
            final String v1 = /*EL:526*/a1.getDescriptor();
            /*SL:527*/Descriptor.rename(v1, v-4);
            /*SL:528*/AttributeInfo.getRefClasses(a1.getAttributes(), v-4);
        }
        /*SL:531*/list = this.fields;
        /*SL:533*/for (int n = list.size(), i = 0; i < n; ++i) {
            final FieldInfo v2 = /*EL:534*/list.get(i);
            final String v1 = /*EL:535*/v2.getDescriptor();
            /*SL:536*/Descriptor.rename(v1, v-4);
            /*SL:537*/AttributeInfo.getRefClasses(v2.getAttributes(), v-4);
        }
    }
    
    public String[] getInterfaces() {
        /*SL:546*/if (this.cachedInterfaces != null) {
            /*SL:547*/return this.cachedInterfaces;
        }
        String[] cachedInterfaces = /*EL:549*/null;
        /*SL:550*/if (this.interfaces == null) {
            /*SL:551*/cachedInterfaces = new String[0];
        }
        else {
            final int length = /*EL:553*/this.interfaces.length;
            final String[] v0 = /*EL:554*/new String[length];
            /*SL:555*/for (int v = 0; v < length; ++v) {
                /*SL:556*/v0[v] = this.constPool.getClassInfo(this.interfaces[v]);
            }
            /*SL:558*/cachedInterfaces = v0;
        }
        /*SL:562*/return this.cachedInterfaces = cachedInterfaces;
    }
    
    public void setInterfaces(final String[] v0) {
        /*SL:572*/this.cachedInterfaces = null;
        /*SL:573*/if (v0 != null) {
            final int v = /*EL:574*/v0.length;
            /*SL:575*/this.interfaces = new int[v];
            /*SL:576*/for (int a1 = 0; a1 < v; ++a1) {
                /*SL:577*/this.interfaces[a1] = this.constPool.addClassInfo(v0[a1]);
            }
        }
    }
    
    public void addInterface(final String v-2) {
        /*SL:585*/this.cachedInterfaces = null;
        final int addClassInfo = /*EL:586*/this.constPool.addClassInfo(v-2);
        /*SL:587*/if (this.interfaces == null) {
            /*SL:589*/(this.interfaces = new int[1])[0] = addClassInfo;
        }
        else {
            final int a1 = /*EL:592*/this.interfaces.length;
            final int[] v1 = /*EL:593*/new int[a1 + 1];
            /*SL:594*/System.arraycopy(this.interfaces, 0, v1, 0, a1);
            /*SL:595*/v1[a1] = addClassInfo;
            /*SL:596*/this.interfaces = v1;
        }
    }
    
    public List getFields() {
        /*SL:607*/return this.fields;
    }
    
    public void addField(final FieldInfo a1) throws DuplicateMemberException {
        /*SL:616*/this.testExistingField(a1.getName(), a1.getDescriptor());
        /*SL:617*/this.fields.add(a1);
    }
    
    public final void addField2(final FieldInfo a1) {
        /*SL:629*/this.fields.add(a1);
    }
    
    private void testExistingField(final String v1, final String v2) throws DuplicateMemberException {
        final ListIterator v3 = /*EL:634*/this.fields.listIterator(0);
        /*SL:635*/while (v3.hasNext()) {
            final FieldInfo a1 = /*EL:636*/v3.next();
            /*SL:637*/if (a1.getName().equals(v1)) {
                /*SL:638*/throw new DuplicateMemberException("duplicate field: " + v1);
            }
        }
    }
    
    public List getMethods() {
        /*SL:649*/return this.methods;
    }
    
    public MethodInfo getMethod(final String v-2) {
        final ArrayList methods = /*EL:659*/this.methods;
        /*SL:661*/for (int v0 = methods.size(), v = 0; v < v0; ++v) {
            final MethodInfo a1 = /*EL:662*/methods.get(v);
            /*SL:663*/if (a1.getName().equals(v-2)) {
                /*SL:664*/return a1;
            }
        }
        /*SL:667*/return null;
    }
    
    public MethodInfo getStaticInitializer() {
        /*SL:675*/return this.getMethod("<clinit>");
    }
    
    public void addMethod(final MethodInfo a1) throws DuplicateMemberException {
        /*SL:686*/this.testExistingMethod(a1);
        /*SL:687*/this.methods.add(a1);
    }
    
    public final void addMethod2(final MethodInfo a1) {
        /*SL:699*/this.methods.add(a1);
    }
    
    private void testExistingMethod(final MethodInfo a1) throws DuplicateMemberException {
        final String v1 = /*EL:705*/a1.getName();
        final String v2 = /*EL:706*/a1.getDescriptor();
        final ListIterator v3 = /*EL:707*/this.methods.listIterator(0);
        /*SL:708*/while (v3.hasNext()) {
            /*SL:709*/if (isDuplicated(a1, v1, v2, v3.next(), v3)) {
                /*SL:710*/throw new DuplicateMemberException("duplicate method: " + v1 + " in " + this.getName());
            }
        }
    }
    
    private static boolean isDuplicated(final MethodInfo a1, final String a2, final String a3, final MethodInfo a4, final ListIterator a5) {
        /*SL:718*/if (!a4.getName().equals(a2)) {
            /*SL:719*/return false;
        }
        final String v1 = /*EL:721*/a4.getDescriptor();
        /*SL:722*/if (!Descriptor.eqParamTypes(v1, a3)) {
            /*SL:723*/return false;
        }
        /*SL:725*/if (!v1.equals(a3)) {
            /*SL:736*/return false;
        }
        if (notBridgeMethod(a4)) {
            return true;
        }
        a5.remove();
        return false;
    }
    
    private static boolean notBridgeMethod(final MethodInfo a1) {
        /*SL:743*/return (a1.getAccessFlags() & 0x40) == 0x0;
    }
    
    public List getAttributes() {
        /*SL:757*/return this.attributes;
    }
    
    public AttributeInfo getAttribute(final String v-2) {
        final ArrayList attributes = /*EL:774*/this.attributes;
        /*SL:776*/for (int v0 = attributes.size(), v = 0; v < v0; ++v) {
            final AttributeInfo a1 = /*EL:777*/attributes.get(v);
            /*SL:778*/if (a1.getName().equals(v-2)) {
                /*SL:779*/return a1;
            }
        }
        /*SL:782*/return null;
    }
    
    public AttributeInfo removeAttribute(final String a1) {
        /*SL:793*/return AttributeInfo.remove(this.attributes, a1);
    }
    
    public void addAttribute(final AttributeInfo a1) {
        /*SL:803*/AttributeInfo.remove(this.attributes, a1.getName());
        /*SL:804*/this.attributes.add(a1);
    }
    
    public String getSourceFile() {
        final SourceFileAttribute v1 = /*EL:813*/(SourceFileAttribute)this.getAttribute("SourceFile");
        /*SL:815*/if (v1 == null) {
            /*SL:816*/return null;
        }
        /*SL:818*/return v1.getFileName();
    }
    
    private void read(final DataInputStream v2) throws IOException {
        final int v3 = /*EL:823*/v2.readInt();
        /*SL:824*/if (v3 != -889275714) {
            /*SL:825*/throw new IOException("bad magic number: " + Integer.toHexString(v3));
        }
        /*SL:827*/this.minor = v2.readUnsignedShort();
        /*SL:828*/this.major = v2.readUnsignedShort();
        /*SL:829*/this.constPool = new ConstPool(v2);
        /*SL:830*/this.accessFlags = v2.readUnsignedShort();
        /*SL:831*/this.thisClass = v2.readUnsignedShort();
        /*SL:832*/this.constPool.setThisClassInfo(this.thisClass);
        /*SL:833*/this.superClass = v2.readUnsignedShort();
        int v4 = /*EL:834*/v2.readUnsignedShort();
        /*SL:835*/if (v4 == 0) {
            /*SL:836*/this.interfaces = null;
        }
        else {
            /*SL:838*/this.interfaces = new int[v4];
            /*SL:839*/for (int a1 = 0; a1 < v4; ++a1) {
                /*SL:840*/this.interfaces[a1] = v2.readUnsignedShort();
            }
        }
        final ConstPool v5 = /*EL:843*/this.constPool;
        /*SL:844*/v4 = v2.readUnsignedShort();
        /*SL:845*/this.fields = new ArrayList();
        /*SL:846*/for (int v6 = 0; v6 < v4; ++v6) {
            /*SL:847*/this.addField2(new FieldInfo(v5, v2));
        }
        /*SL:849*/v4 = v2.readUnsignedShort();
        /*SL:850*/this.methods = new ArrayList();
        /*SL:851*/for (int v6 = 0; v6 < v4; ++v6) {
            /*SL:852*/this.addMethod2(new MethodInfo(v5, v2));
        }
        /*SL:854*/this.attributes = new ArrayList();
        /*SL:855*/v4 = v2.readUnsignedShort();
        /*SL:856*/for (int v6 = 0; v6 < v4; ++v6) {
            /*SL:857*/this.addAttribute(AttributeInfo.read(v5, v2));
        }
        /*SL:859*/this.thisclassname = this.constPool.getClassInfo(this.thisClass);
    }
    
    public void write(final DataOutputStream v-3) throws IOException {
        /*SL:868*/v-3.writeInt(-889275714);
        /*SL:869*/v-3.writeShort(this.minor);
        /*SL:870*/v-3.writeShort(this.major);
        /*SL:871*/this.constPool.write(v-3);
        /*SL:872*/v-3.writeShort(this.accessFlags);
        /*SL:873*/v-3.writeShort(this.thisClass);
        /*SL:874*/v-3.writeShort(this.superClass);
        int n = 0;
        /*SL:876*/if (this.interfaces == null) {
            final int a1 = /*EL:877*/0;
        }
        else {
            /*SL:879*/n = this.interfaces.length;
        }
        /*SL:881*/v-3.writeShort(n);
        /*SL:882*/for (int i = 0; i < n; ++i) {
            /*SL:883*/v-3.writeShort(this.interfaces[i]);
        }
        ArrayList v0 = /*EL:885*/this.fields;
        /*SL:886*/n = v0.size();
        /*SL:887*/v-3.writeShort(n);
        /*SL:888*/for (int i = 0; i < n; ++i) {
            final FieldInfo v = /*EL:889*/v0.get(i);
            /*SL:890*/v.write(v-3);
        }
        /*SL:893*/v0 = this.methods;
        /*SL:894*/n = v0.size();
        /*SL:895*/v-3.writeShort(n);
        /*SL:896*/for (int i = 0; i < n; ++i) {
            final MethodInfo v2 = /*EL:897*/v0.get(i);
            /*SL:898*/v2.write(v-3);
        }
        /*SL:901*/v-3.writeShort(this.attributes.size());
        /*SL:902*/AttributeInfo.writeAll(this.attributes, v-3);
    }
    
    public int getMajorVersion() {
        /*SL:911*/return this.major;
    }
    
    public void setMajorVersion(final int a1) {
        /*SL:921*/this.major = a1;
    }
    
    public int getMinorVersion() {
        /*SL:930*/return this.minor;
    }
    
    public void setMinorVersion(final int a1) {
        /*SL:940*/this.minor = a1;
    }
    
    public void setVersionToJava5() {
        /*SL:951*/this.major = 49;
        /*SL:952*/this.minor = 0;
    }
    
    static {
        int v1 = 47;
        try {
            Class.forName("java.lang.StringBuilder");
            v1 = 49;
            Class.forName("java.util.zip.DeflaterInputStream");
            v1 = 50;
            Class.forName("java.lang.invoke.CallSite");
            v1 = 51;
            Class.forName("java.util.function.Function");
            v1 = 52;
        }
        catch (Throwable t) {}
        MAJOR_VERSION = v1;
    }
}
