package javassist;

import java.util.Set;
import javassist.compiler.CompileError;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ConstantAttribute;
import java.util.HashMap;
import java.util.List;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.EnclosingMethodAttribute;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.AnnotationsAttribute;
import java.util.ArrayList;
import javassist.bytecode.InnerClassesAttribute;
import javassist.bytecode.AccessFlag;
import java.util.Map;
import javassist.bytecode.Descriptor;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.SignatureAttribute;
import java.net.URL;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import javassist.compiler.AccessorMaker;
import java.lang.ref.WeakReference;
import javassist.bytecode.ClassFile;

class CtClassType extends CtClass
{
    ClassPool classPool;
    boolean wasChanged;
    private boolean wasFrozen;
    boolean wasPruned;
    boolean gcConstPool;
    ClassFile classfile;
    byte[] rawClassfile;
    private WeakReference memberCache;
    private AccessorMaker accessors;
    private FieldInitLink fieldInitializers;
    private Hashtable hiddenMethods;
    private int uniqueNumberSeed;
    private boolean doPruning;
    private int getCount;
    private static final int GET_THRESHOLD = 2;
    
    CtClassType(final String a1, final ClassPool a2) {
        super(a1);
        this.doPruning = ClassPool.doPruning;
        this.classPool = a2;
        final boolean b = false;
        this.gcConstPool = b;
        this.wasPruned = b;
        this.wasFrozen = b;
        this.wasChanged = b;
        this.classfile = null;
        this.rawClassfile = null;
        this.memberCache = null;
        this.accessors = null;
        this.fieldInitializers = null;
        this.hiddenMethods = null;
        this.uniqueNumberSeed = 0;
        this.getCount = 0;
    }
    
    CtClassType(final InputStream a1, final ClassPool a2) throws IOException {
        this((String)null, a2);
        this.classfile = new ClassFile(new DataInputStream(a1));
        this.qualifiedName = this.classfile.getName();
    }
    
    CtClassType(final ClassFile a1, final ClassPool a2) {
        this((String)null, a2);
        this.classfile = a1;
        this.qualifiedName = this.classfile.getName();
    }
    
    @Override
    protected void extendToString(final StringBuffer v0) {
        /*SL:107*/if (this.wasChanged) {
            /*SL:108*/v0.append("changed ");
        }
        /*SL:110*/if (this.wasFrozen) {
            /*SL:111*/v0.append("frozen ");
        }
        /*SL:113*/if (this.wasPruned) {
            /*SL:114*/v0.append("pruned ");
        }
        /*SL:116*/v0.append(Modifier.toString(this.getModifiers()));
        /*SL:117*/v0.append(" class ");
        /*SL:118*/v0.append(this.getName());
        try {
            final CtClass v = /*EL:121*/this.getSuperclass();
            /*SL:122*/if (v != null) {
                final String a1 = /*EL:123*/v.getName();
                /*SL:124*/if (!a1.equals("java.lang.Object")) {
                    /*SL:125*/v0.append(" extends " + v.getName());
                }
            }
        }
        catch (NotFoundException v5) {
            /*SL:129*/v0.append(" extends ??");
        }
        try {
            final CtClass[] v2 = /*EL:133*/this.getInterfaces();
            /*SL:134*/if (v2.length > 0) {
                /*SL:135*/v0.append(" implements ");
            }
            /*SL:137*/for (int v3 = 0; v3 < v2.length; ++v3) {
                /*SL:138*/v0.append(v2[v3].getName());
                /*SL:139*/v0.append(", ");
            }
        }
        catch (NotFoundException v5) {
            /*SL:143*/v0.append(" extends ??");
        }
        final CtMember.Cache v4 = /*EL:146*/this.getMembers();
        /*SL:147*/this.exToString(v0, " fields=", v4.fieldHead(), /*EL:148*/v4.lastField());
        /*SL:149*/this.exToString(v0, " constructors=", v4.consHead(), /*EL:150*/v4.lastCons());
        /*SL:151*/this.exToString(v0, " methods=", v4.methodHead(), /*EL:152*/v4.lastMethod());
    }
    
    private void exToString(final StringBuffer a1, final String a2, CtMember a3, final CtMember a4) {
        /*SL:157*/a1.append(a2);
        /*SL:158*/while (a3 != a4) {
            /*SL:159*/a3 = a3.next();
            /*SL:160*/a1.append(a3);
            /*SL:161*/a1.append(", ");
        }
    }
    
    @Override
    public AccessorMaker getAccessorMaker() {
        /*SL:166*/if (this.accessors == null) {
            /*SL:167*/this.accessors = new AccessorMaker(this);
        }
        /*SL:169*/return this.accessors;
    }
    
    @Override
    public ClassFile getClassFile2() {
        /*SL:173*/return this.getClassFile3(true);
    }
    
    public ClassFile getClassFile3(final boolean v-1) {
        final ClassFile v0 = /*EL:177*/this.classfile;
        /*SL:178*/if (v0 != null) {
            /*SL:179*/return v0;
        }
        /*SL:181*/if (v-1) {
            /*SL:182*/this.classPool.compress();
        }
        /*SL:184*/if (this.rawClassfile != null) {
            try {
                final ClassFile a1 = /*EL:186*/new ClassFile(new DataInputStream(new ByteArrayInputStream(this.rawClassfile)));
                /*SL:188*/this.rawClassfile = null;
                /*SL:189*/this.getCount = 2;
                /*SL:190*/return this.setClassFile(a1);
            }
            catch (IOException v) {
                /*SL:193*/throw new RuntimeException(v.toString(), v);
            }
        }
        InputStream v2 = /*EL:197*/null;
        try {
            /*SL:199*/v2 = this.classPool.openClassfile(this.getName());
            /*SL:200*/if (v2 == null) {
                /*SL:201*/throw new NotFoundException(this.getName());
            }
            /*SL:203*/v2 = new BufferedInputStream(v2);
            final ClassFile v3 = /*EL:204*/new ClassFile(new DataInputStream(v2));
            /*SL:205*/if (!v3.getName().equals(this.qualifiedName)) {
                /*SL:206*/throw new RuntimeException("cannot find " + this.qualifiedName + ": " + v3.getName() + /*EL:207*/" found in " + this.qualifiedName.replace('.', '/') + /*EL:208*/".class");
            }
            /*SL:223*/return this.setClassFile(v3);
        }
        catch (NotFoundException v4) {
            throw new RuntimeException(v4.toString(), v4);
        }
        catch (IOException v5) {
            throw new RuntimeException(v5.toString(), v5);
        }
        finally {
            if (v2 != null) {
                try {
                    v2.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    @Override
    final void incGetCounter() {
        /*SL:232*/++this.getCount;
    }
    
    @Override
    void compress() {
        /*SL:240*/if (this.getCount < 2) {
            /*SL:241*/if (!this.isModified() && ClassPool.releaseUnmodifiedClassFile) {
                /*SL:242*/this.removeClassFile();
            }
            else/*SL:243*/ if (this.isFrozen() && !this.wasPruned) {
                /*SL:244*/this.saveClassFile();
            }
        }
        /*SL:246*/this.getCount = 0;
    }
    
    private synchronized void saveClassFile() {
        /*SL:256*/if (this.classfile == null || this.hasMemberCache() != null) {
            /*SL:257*/return;
        }
        final ByteArrayOutputStream v1 = /*EL:259*/new ByteArrayOutputStream();
        final DataOutputStream v2 = /*EL:260*/new DataOutputStream(v1);
        try {
            /*SL:262*/this.classfile.write(v2);
            /*SL:263*/v1.close();
            /*SL:264*/this.rawClassfile = v1.toByteArray();
            /*SL:265*/this.classfile = null;
        }
        catch (IOException ex) {}
    }
    
    private synchronized void removeClassFile() {
        /*SL:271*/if (this.classfile != null && !this.isModified() && this.hasMemberCache() == null) {
            /*SL:272*/this.classfile = null;
        }
    }
    
    private synchronized ClassFile setClassFile(final ClassFile a1) {
        /*SL:279*/if (this.classfile == null) {
            /*SL:280*/this.classfile = a1;
        }
        /*SL:282*/return this.classfile;
    }
    
    @Override
    public ClassPool getClassPool() {
        /*SL:285*/return this.classPool;
    }
    
    void setClassPool(final ClassPool a1) {
        /*SL:287*/this.classPool = a1;
    }
    
    @Override
    public URL getURL() throws NotFoundException {
        final URL v1 = /*EL:290*/this.classPool.find(this.getName());
        /*SL:291*/if (v1 == null) {
            /*SL:292*/throw new NotFoundException(this.getName());
        }
        /*SL:294*/return v1;
    }
    
    @Override
    public boolean isModified() {
        /*SL:297*/return this.wasChanged;
    }
    
    @Override
    public boolean isFrozen() {
        /*SL:299*/return this.wasFrozen;
    }
    
    @Override
    public void freeze() {
        /*SL:301*/this.wasFrozen = true;
    }
    
    @Override
    void checkModify() throws RuntimeException {
        /*SL:304*/if (this.isFrozen()) {
            String v1 = /*EL:305*/this.getName() + " class is frozen";
            /*SL:306*/if (this.wasPruned) {
                /*SL:307*/v1 += " and pruned";
            }
            /*SL:309*/throw new RuntimeException(v1);
        }
        /*SL:312*/this.wasChanged = true;
    }
    
    @Override
    public void defrost() {
        /*SL:316*/this.checkPruned("defrost");
        /*SL:317*/this.wasFrozen = false;
    }
    
    @Override
    public boolean subtypeOf(final CtClass a1) throws NotFoundException {
        String v2 = /*EL:322*/a1.getName();
        /*SL:323*/if (this == a1 || this.getName().equals(v2)) {
            /*SL:324*/return true;
        }
        /*SL:326*/v2 = this.getClassFile2();
        final String v3 = /*EL:327*/v2.getSuperclass();
        /*SL:328*/if (v3 != null && v3.equals(v2)) {
            /*SL:329*/return true;
        }
        final String[] v4 = /*EL:331*/v2.getInterfaces();
        final int v5 = /*EL:332*/v4.length;
        /*SL:333*/for (int v6 = 0; v6 < v5; ++v6) {
            /*SL:334*/if (v4[v6].equals(v2)) {
                /*SL:335*/return true;
            }
        }
        /*SL:337*/if (v3 != null && this.classPool.get(v3).subtypeOf(a1)) {
            /*SL:338*/return true;
        }
        /*SL:340*/for (int v6 = 0; v6 < v5; ++v6) {
            /*SL:341*/if (this.classPool.get(v4[v6]).subtypeOf(a1)) {
                /*SL:342*/return true;
            }
        }
        /*SL:344*/return false;
    }
    
    @Override
    public void setName(final String a1) throws RuntimeException {
        final String v1 = /*EL:348*/this.getName();
        /*SL:349*/if (a1.equals(v1)) {
            /*SL:350*/return;
        }
        /*SL:353*/this.classPool.checkNotFrozen(a1);
        final ClassFile v2 = /*EL:354*/this.getClassFile2();
        /*SL:355*/super.setName(a1);
        /*SL:356*/v2.setName(a1);
        /*SL:357*/this.nameReplaced();
        /*SL:358*/this.classPool.classNameChanged(v1, this);
    }
    
    @Override
    public String getGenericSignature() {
        final SignatureAttribute v1 = /*EL:362*/(SignatureAttribute)this.getClassFile2().getAttribute(/*EL:363*/"Signature");
        /*SL:364*/return (v1 == null) ? null : v1.getSignature();
    }
    
    @Override
    public void setGenericSignature(final String a1) {
        final ClassFile v1 = /*EL:368*/this.getClassFile();
        final SignatureAttribute v2 = /*EL:369*/new SignatureAttribute(v1.getConstPool(), a1);
        /*SL:370*/v1.addAttribute(v2);
    }
    
    @Override
    public void replaceClassName(final ClassMap a1) throws RuntimeException {
        final String v1 = /*EL:376*/this.getName();
        String v2 = /*EL:377*/(String)a1.get(/*EL:378*/Descriptor.toJvmName(v1));
        /*SL:379*/if (v2 != null) {
            /*SL:380*/v2 = Descriptor.toJavaName(v2);
            /*SL:382*/this.classPool.checkNotFrozen(v2);
        }
        /*SL:385*/super.replaceClassName(a1);
        final ClassFile v3 = /*EL:386*/this.getClassFile2();
        /*SL:387*/v3.renameClass(a1);
        /*SL:388*/this.nameReplaced();
        /*SL:390*/if (v2 != null) {
            /*SL:391*/super.setName(v2);
            /*SL:392*/this.classPool.classNameChanged(v1, this);
        }
    }
    
    @Override
    public void replaceClassName(final String a1, final String a2) throws RuntimeException {
        final String v1 = /*EL:399*/this.getName();
        /*SL:400*/if (v1.equals(a1)) {
            /*SL:401*/this.setName(a2);
        }
        else {
            /*SL:403*/super.replaceClassName(a1, a2);
            /*SL:404*/this.getClassFile2().renameClass(a1, a2);
            /*SL:405*/this.nameReplaced();
        }
    }
    
    @Override
    public boolean isInterface() {
        /*SL:410*/return Modifier.isInterface(this.getModifiers());
    }
    
    @Override
    public boolean isAnnotation() {
        /*SL:414*/return Modifier.isAnnotation(this.getModifiers());
    }
    
    @Override
    public boolean isEnum() {
        /*SL:418*/return Modifier.isEnum(this.getModifiers());
    }
    
    @Override
    public int getModifiers() {
        final ClassFile v1 = /*EL:422*/this.getClassFile2();
        int v2 = /*EL:423*/v1.getAccessFlags();
        /*SL:424*/v2 = AccessFlag.clear(v2, 32);
        final int v3 = /*EL:425*/v1.getInnerAccessFlags();
        /*SL:426*/if (v3 != -1 && (v3 & 0x8) != 0x0) {
            /*SL:427*/v2 |= 0x8;
        }
        /*SL:429*/return AccessFlag.toModifier(v2);
    }
    
    @Override
    public CtClass[] getNestedClasses() throws NotFoundException {
        final ClassFile classFile2 = /*EL:433*/this.getClassFile2();
        final InnerClassesAttribute innerClassesAttribute = /*EL:434*/(InnerClassesAttribute)classFile2.getAttribute("InnerClasses");
        /*SL:436*/if (innerClassesAttribute == null) {
            /*SL:437*/return new CtClass[0];
        }
        final String string = /*EL:439*/classFile2.getName() + "$";
        final int tableLength = /*EL:440*/innerClassesAttribute.tableLength();
        final ArrayList list = /*EL:441*/new ArrayList(tableLength);
        /*SL:442*/for (int v0 = 0; v0 < tableLength; ++v0) {
            final String v = /*EL:443*/innerClassesAttribute.innerClass(v0);
            /*SL:445*/if (v != null && v.startsWith(string) && /*EL:447*/v.lastIndexOf(36) < string.length()) {
                /*SL:448*/list.add(this.classPool.get(v));
            }
        }
        /*SL:452*/return list.<CtClass>toArray(new CtClass[list.size()]);
    }
    
    @Override
    public void setModifiers(int v2) {
        final ClassFile v3 = /*EL:456*/this.getClassFile2();
        /*SL:457*/if (Modifier.isStatic(v2)) {
            final int a1 = /*EL:458*/v3.getInnerAccessFlags();
            /*SL:459*/if (a1 == -1 || (a1 & 0x8) == 0x0) {
                /*SL:462*/throw new RuntimeException("cannot change " + this.getName() + " into a static class");
            }
            v2 &= 0xFFFFFFF7;
        }
        /*SL:465*/this.checkModify();
        /*SL:466*/v3.setAccessFlags(AccessFlag.of(v2));
    }
    
    @Override
    public boolean hasAnnotation(final String a1) {
        final ClassFile v1 = /*EL:471*/this.getClassFile2();
        final AnnotationsAttribute v2 = /*EL:472*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:474*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:476*/return hasAnnotationType(a1, this.getClassPool(), v2, v3);
    }
    
    static boolean hasAnnotationType(final Class a1, final ClassPool a2, final AnnotationsAttribute a3, final AnnotationsAttribute a4) {
        /*SL:486*/return hasAnnotationType(a1.getName(), a2, a3, a4);
    }
    
    static boolean hasAnnotationType(final String v1, final ClassPool v2, final AnnotationsAttribute v3, final AnnotationsAttribute v4) {
        final Annotation[] v5;
        /*SL:495*/if (v3 == null) {
            final Annotation[] a1 = /*EL:496*/null;
        }
        else {
            /*SL:498*/v5 = v3.getAnnotations();
        }
        final Annotation[] v6;
        /*SL:500*/if (v4 == null) {
            final Annotation[] a2 = /*EL:501*/null;
        }
        else {
            /*SL:503*/v6 = v4.getAnnotations();
        }
        /*SL:505*/if (v5 != null) {
            /*SL:506*/for (int a3 = 0; a3 < v5.length; ++a3) {
                /*SL:507*/if (v5[a3].getTypeName().equals(v1)) {
                    /*SL:508*/return true;
                }
            }
        }
        /*SL:510*/if (v6 != null) {
            /*SL:511*/for (int a4 = 0; a4 < v6.length; ++a4) {
                /*SL:512*/if (v6[a4].getTypeName().equals(v1)) {
                    /*SL:513*/return true;
                }
            }
        }
        /*SL:515*/return false;
    }
    
    @Override
    public Object getAnnotation(final Class a1) throws ClassNotFoundException {
        final ClassFile v1 = /*EL:519*/this.getClassFile2();
        final AnnotationsAttribute v2 = /*EL:520*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:522*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:524*/return getAnnotationType(a1, this.getClassPool(), v2, v3);
    }
    
    static Object getAnnotationType(final Class v1, final ClassPool v2, final AnnotationsAttribute v3, final AnnotationsAttribute v4) throws ClassNotFoundException {
        final Annotation[] v5;
        /*SL:533*/if (v3 == null) {
            final Annotation[] a1 = /*EL:534*/null;
        }
        else {
            /*SL:536*/v5 = v3.getAnnotations();
        }
        final Annotation[] v6;
        /*SL:538*/if (v4 == null) {
            final Annotation[] a2 = /*EL:539*/null;
        }
        else {
            /*SL:541*/v6 = v4.getAnnotations();
        }
        final String v7 = /*EL:543*/v1.getName();
        /*SL:544*/if (v5 != null) {
            /*SL:545*/for (int a3 = 0; a3 < v5.length; ++a3) {
                /*SL:546*/if (v5[a3].getTypeName().equals(v7)) {
                    /*SL:547*/return toAnnoType(v5[a3], v2);
                }
            }
        }
        /*SL:549*/if (v6 != null) {
            /*SL:550*/for (int a4 = 0; a4 < v6.length; ++a4) {
                /*SL:551*/if (v6[a4].getTypeName().equals(v7)) {
                    /*SL:552*/return toAnnoType(v6[a4], v2);
                }
            }
        }
        /*SL:554*/return null;
    }
    
    @Override
    public Object[] getAnnotations() throws ClassNotFoundException {
        /*SL:558*/return this.getAnnotations(false);
    }
    
    @Override
    public Object[] getAvailableAnnotations() {
        try {
            /*SL:563*/return this.getAnnotations(true);
        }
        catch (ClassNotFoundException v1) {
            /*SL:566*/throw new RuntimeException("Unexpected exception ", v1);
        }
    }
    
    private Object[] getAnnotations(final boolean a1) throws ClassNotFoundException {
        final ClassFile v1 = /*EL:573*/this.getClassFile2();
        final AnnotationsAttribute v2 = /*EL:574*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:576*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:578*/return toAnnotationType(a1, this.getClassPool(), v2, v3);
    }
    
    static Object[] toAnnotationType(final boolean v-8, final ClassPool v-7, final AnnotationsAttribute v-6, final AnnotationsAttribute v-5) throws ClassNotFoundException {
        final Annotation[] annotations;
        final int length;
        /*SL:588*/if (v-6 == null) {
            final Annotation[] a1 = /*EL:589*/null;
            final int a2 = /*EL:590*/0;
        }
        else {
            /*SL:593*/annotations = v-6.getAnnotations();
            /*SL:594*/length = annotations.length;
        }
        final Annotation[] annotations2;
        final int length2;
        /*SL:597*/if (v-5 == null) {
            final Annotation[] a3 = /*EL:598*/null;
            final int a4 = /*EL:599*/0;
        }
        else {
            /*SL:602*/annotations2 = v-5.getAnnotations();
            /*SL:603*/length2 = annotations2.length;
        }
        /*SL:606*/if (!v-8) {
            final Object[] v0 = /*EL:607*/new Object[length + length2];
            /*SL:608*/for (int v = 0; v < length; ++v) {
                /*SL:609*/v0[v] = toAnnoType(annotations[v], v-7);
            }
            /*SL:611*/for (int v = 0; v < length2; ++v) {
                /*SL:612*/v0[v + length] = toAnnoType(annotations2[v], v-7);
            }
            /*SL:614*/return v0;
        }
        final ArrayList v2 = /*EL:617*/new ArrayList();
        /*SL:618*/for (int v = 0; v < length; ++v) {
            try {
                /*SL:620*/v2.add(toAnnoType(annotations[v], v-7));
            }
            catch (ClassNotFoundException ex) {}
        }
        /*SL:624*/for (int v = 0; v < length2; ++v) {
            try {
                /*SL:626*/v2.add(toAnnoType(annotations2[v], v-7));
            }
            catch (ClassNotFoundException ex2) {}
        }
        /*SL:631*/return v2.toArray();
    }
    
    static Object[][] toAnnotationType(final boolean v-11, final ClassPool v-10, final ParameterAnnotationsAttribute v-9, final ParameterAnnotationsAttribute v-8, final MethodInfo v-7) throws ClassNotFoundException {
        int n = /*EL:641*/0;
        /*SL:642*/if (v-9 != null) {
            /*SL:643*/n = v-9.numParameters();
        }
        else/*SL:644*/ if (v-8 != null) {
            /*SL:645*/n = v-8.numParameters();
        }
        else {
            /*SL:647*/n = Descriptor.numOfParameters(v-7.getDescriptor());
        }
        final Object[][] array = /*EL:649*/new Object[n][];
        /*SL:650*/for (int i = 0; i < n; ++i) {
            final Annotation[] array2;
            final int length;
            /*SL:654*/if (v-9 == null) {
                final Annotation[] a1 = /*EL:655*/null;
                final int a2 = /*EL:656*/0;
            }
            else {
                /*SL:659*/array2 = v-9.getAnnotations()[i];
                /*SL:660*/length = array2.length;
            }
            final Annotation[] array3;
            final int v0;
            /*SL:663*/if (v-8 == null) {
                final Annotation[] a3 = /*EL:664*/null;
                final int a4 = /*EL:665*/0;
            }
            else {
                /*SL:668*/array3 = v-8.getAnnotations()[i];
                /*SL:669*/v0 = array3.length;
            }
            /*SL:672*/if (!v-11) {
                /*SL:673*/array[i] = new Object[length + v0];
                /*SL:674*/for (int a5 = 0; a5 < length; ++a5) {
                    /*SL:675*/array[i][a5] = toAnnoType(array2[a5], v-10);
                }
                /*SL:677*/for (int v = 0; v < v0; ++v) {
                    /*SL:678*/array[i][v + length] = toAnnoType(array3[v], v-10);
                }
            }
            else {
                final ArrayList v2 = /*EL:681*/new ArrayList();
                /*SL:682*/for (int v3 = 0; v3 < length; ++v3) {
                    try {
                        /*SL:684*/v2.add(toAnnoType(array2[v3], v-10));
                    }
                    catch (ClassNotFoundException ex) {}
                }
                /*SL:688*/for (int v3 = 0; v3 < v0; ++v3) {
                    try {
                        /*SL:690*/v2.add(toAnnoType(array3[v3], v-10));
                    }
                    catch (ClassNotFoundException ex2) {}
                }
                /*SL:695*/array[i] = v2.toArray();
            }
        }
        /*SL:699*/return array;
    }
    
    private static Object toAnnoType(final Annotation v-4, final ClassPool v-3) throws ClassNotFoundException {
        try {
            final ClassLoader a1 = /*EL:706*/v-3.getClassLoader();
            /*SL:707*/return v-4.toAnnotationType(a1, v-3);
        }
        catch (ClassNotFoundException ex) {
            final ClassLoader classLoader = /*EL:710*/v-3.getClass().getClassLoader();
            try {
                /*SL:712*/return v-4.toAnnotationType(classLoader, v-3);
            }
            catch (ClassNotFoundException v0) {
                try {
                    final Class a2 = /*EL:716*/v-3.get(v-4.getTypeName()).toClass();
                    /*SL:717*/return AnnotationImpl.make(a2.getClassLoader(), /*EL:718*/a2, v-3, v-4);
                }
                catch (Throwable v) {
                    /*SL:722*/throw new ClassNotFoundException(v-4.getTypeName());
                }
            }
        }
    }
    
    @Override
    public boolean subclassOf(final CtClass a1) {
        /*SL:729*/if (a1 == null) {
            /*SL:730*/return false;
        }
        final String v1 = /*EL:732*/a1.getName();
        CtClass v2 = /*EL:733*/this;
        try {
            /*SL:735*/while (v2 != null) {
                /*SL:736*/if (v2.getName().equals(v1)) {
                    /*SL:737*/return true;
                }
                /*SL:739*/v2 = v2.getSuperclass();
            }
        }
        catch (Exception ex) {}
        /*SL:743*/return false;
    }
    
    @Override
    public CtClass getSuperclass() throws NotFoundException {
        final String v1 = /*EL:747*/this.getClassFile2().getSuperclass();
        /*SL:748*/if (v1 == null) {
            /*SL:749*/return null;
        }
        /*SL:751*/return this.classPool.get(v1);
    }
    
    @Override
    public void setSuperclass(final CtClass a1) throws CannotCompileException {
        /*SL:755*/this.checkModify();
        /*SL:756*/if (this.isInterface()) {
            /*SL:757*/this.addInterface(a1);
        }
        else {
            /*SL:759*/this.getClassFile2().setSuperclass(a1.getName());
        }
    }
    
    @Override
    public CtClass[] getInterfaces() throws NotFoundException {
        final String[] interfaces = /*EL:763*/this.getClassFile2().getInterfaces();
        final int length = /*EL:764*/interfaces.length;
        final CtClass[] v0 = /*EL:765*/new CtClass[length];
        /*SL:766*/for (int v = 0; v < length; ++v) {
            /*SL:767*/v0[v] = this.classPool.get(interfaces[v]);
        }
        /*SL:769*/return v0;
    }
    
    @Override
    public void setInterfaces(final CtClass[] v-2) {
        /*SL:773*/this.checkModify();
        final String[] interfaces;
        /*SL:775*/if (v-2 == null) {
            final String[] a1 = /*EL:776*/new String[0];
        }
        else {
            final int v0 = /*EL:778*/v-2.length;
            /*SL:779*/interfaces = new String[v0];
            /*SL:780*/for (int v = 0; v < v0; ++v) {
                /*SL:781*/interfaces[v] = v-2[v].getName();
            }
        }
        /*SL:784*/this.getClassFile2().setInterfaces(interfaces);
    }
    
    @Override
    public void addInterface(final CtClass a1) {
        /*SL:788*/this.checkModify();
        /*SL:789*/if (a1 != null) {
            /*SL:790*/this.getClassFile2().addInterface(a1.getName());
        }
    }
    
    @Override
    public CtClass getDeclaringClass() throws NotFoundException {
        final ClassFile classFile2 = /*EL:794*/this.getClassFile2();
        final InnerClassesAttribute innerClassesAttribute = /*EL:795*/(InnerClassesAttribute)classFile2.getAttribute("InnerClasses");
        /*SL:797*/if (innerClassesAttribute == null) {
            /*SL:798*/return null;
        }
        final String name = /*EL:800*/this.getName();
        /*SL:802*/for (int tableLength = innerClassesAttribute.tableLength(), i = 0; i < tableLength; ++i) {
            /*SL:803*/if (name.equals(innerClassesAttribute.innerClass(i))) {
                final String v0 = /*EL:804*/innerClassesAttribute.outerClass(i);
                /*SL:805*/if (v0 != null) {
                    /*SL:806*/return this.classPool.get(v0);
                }
                final EnclosingMethodAttribute v = /*EL:809*/(EnclosingMethodAttribute)classFile2.getAttribute("EnclosingMethod");
                /*SL:812*/if (v != null) {
                    /*SL:813*/return this.classPool.get(v.className());
                }
            }
        }
        /*SL:817*/return null;
    }
    
    @Override
    public CtBehavior getEnclosingBehavior() throws NotFoundException {
        final ClassFile classFile2 = /*EL:821*/this.getClassFile2();
        final EnclosingMethodAttribute v0 = /*EL:822*/(EnclosingMethodAttribute)classFile2.getAttribute("EnclosingMethod");
        /*SL:825*/if (v0 == null) {
            /*SL:826*/return null;
        }
        final CtClass v = /*EL:828*/this.classPool.get(v0.className());
        final String v2 = /*EL:829*/v0.methodName();
        /*SL:830*/if ("<init>".equals(v2)) {
            /*SL:831*/return v.getConstructor(v0.methodDescriptor());
        }
        /*SL:832*/if ("<clinit>".equals(v2)) {
            /*SL:833*/return v.getClassInitializer();
        }
        /*SL:835*/return v.getMethod(v2, v0.methodDescriptor());
    }
    
    @Override
    public CtClass makeNestedClass(final String a1, final boolean a2) {
        /*SL:840*/if (!a2) {
            /*SL:841*/throw new RuntimeException("sorry, only nested static class is supported");
        }
        /*SL:844*/this.checkModify();
        final CtClass v1 = /*EL:845*/this.classPool.makeNestedClass(this.getName() + "$" + a1);
        final ClassFile v2 = /*EL:846*/this.getClassFile2();
        final ClassFile v3 = /*EL:847*/v1.getClassFile2();
        InnerClassesAttribute v4 = /*EL:848*/(InnerClassesAttribute)v2.getAttribute("InnerClasses");
        /*SL:850*/if (v4 == null) {
            /*SL:851*/v4 = new InnerClassesAttribute(v2.getConstPool());
            /*SL:852*/v2.addAttribute(v4);
        }
        /*SL:855*/v4.append(v1.getName(), this.getName(), a1, (v3.getAccessFlags() & /*EL:856*/0xFFFFFFDF) | 0x8);
        /*SL:857*/v3.addAttribute(v4.copy(v3.getConstPool(), null));
        /*SL:858*/return v1;
    }
    
    private void nameReplaced() {
        final CtMember.Cache v0 = /*EL:864*/this.hasMemberCache();
        /*SL:865*/if (v0 != null) {
            CtMember v = /*EL:866*/v0.methodHead();
            final CtMember v2 = /*EL:867*/v0.lastMethod();
            /*SL:868*/while (v != v2) {
                /*SL:869*/v = v.next();
                /*SL:870*/v.nameReplaced();
            }
        }
    }
    
    protected CtMember.Cache hasMemberCache() {
        final WeakReference v1 = /*EL:879*/this.memberCache;
        /*SL:880*/if (v1 != null) {
            /*SL:881*/return (CtMember.Cache)v1.get();
        }
        /*SL:883*/return null;
    }
    
    protected synchronized CtMember.Cache getMembers() {
        CtMember.Cache v1 = /*EL:887*/null;
        /*SL:888*/if (this.memberCache == null || /*EL:889*/(v1 = (CtMember.Cache)this.memberCache.get()) == null) {
            /*SL:890*/v1 = new CtMember.Cache(this);
            /*SL:891*/this.makeFieldCache(v1);
            /*SL:892*/this.makeBehaviorCache(v1);
            /*SL:893*/this.memberCache = new WeakReference((T)v1);
        }
        /*SL:896*/return v1;
    }
    
    private void makeFieldCache(final CtMember.Cache v-4) {
        final List fields = /*EL:900*/this.getClassFile3(false).getFields();
        /*SL:902*/for (int size = fields.size(), i = 0; i < size; ++i) {
            final FieldInfo a1 = /*EL:903*/fields.get(i);
            final CtField v1 = /*EL:904*/new CtField(a1, this);
            /*SL:905*/v-4.addField(v1);
        }
    }
    
    private void makeBehaviorCache(final CtMember.Cache v-4) {
        final List methods = /*EL:910*/this.getClassFile3(false).getMethods();
        /*SL:912*/for (int size = methods.size(), i = 0; i < size; ++i) {
            final MethodInfo v0 = /*EL:913*/methods.get(i);
            /*SL:914*/if (v0.isMethod()) {
                final CtMethod a1 = /*EL:915*/new CtMethod(v0, this);
                /*SL:916*/v-4.addMethod(a1);
            }
            else {
                final CtConstructor v = /*EL:919*/new CtConstructor(v0, this);
                /*SL:920*/v-4.addConstructor(v);
            }
        }
    }
    
    @Override
    public CtField[] getFields() {
        final ArrayList v1 = /*EL:926*/new ArrayList();
        getFields(/*EL:927*/v1, this);
        /*SL:928*/return v1.<CtField>toArray(new CtField[v1.size()]);
    }
    
    private static void getFields(final ArrayList v-2, final CtClass v-1) {
        /*SL:933*/if (v-1 == null) {
            /*SL:934*/return;
        }
        try {
            getFields(/*EL:937*/v-2, v-1.getSuperclass());
        }
        catch (NotFoundException ex) {}
        try {
            final CtClass[] a1 = /*EL:942*/v-1.getInterfaces();
            /*SL:944*/for (int v1 = a1.length, a2 = 0; a2 < v1; ++a2) {
                getFields(/*EL:945*/v-2, a1[a2]);
            }
        }
        catch (NotFoundException ex2) {}
        final CtMember.Cache v2 = /*EL:949*/((CtClassType)v-1).getMembers();
        CtMember v3 = /*EL:950*/v2.fieldHead();
        final CtMember v4 = /*EL:951*/v2.lastField();
        /*SL:952*/while (v3 != v4) {
            /*SL:953*/v3 = v3.next();
            /*SL:954*/if (!Modifier.isPrivate(v3.getModifiers())) {
                /*SL:955*/v-2.add(v3);
            }
        }
    }
    
    @Override
    public CtField getField(final String a1, final String a2) throws NotFoundException {
        final CtField v1 = /*EL:960*/this.getField2(a1, a2);
        /*SL:961*/return this.checkGetField(v1, a1, a2);
    }
    
    private CtField checkGetField(final CtField a3, final String v1, final String v2) throws NotFoundException {
        /*SL:967*/if (a3 == null) {
            String a4 = /*EL:968*/"field: " + v1;
            /*SL:969*/if (v2 != null) {
                /*SL:970*/a4 = a4 + " type " + v2;
            }
            /*SL:972*/throw new NotFoundException(a4 + " in " + this.getName());
        }
        /*SL:975*/return a3;
    }
    
    @Override
    CtField getField2(final String v-2, final String v-1) {
        final CtField v0 = /*EL:979*/this.getDeclaredField2(v-2, v-1);
        /*SL:980*/if (v0 != null) {
            /*SL:981*/return v0;
        }
        try {
            final CtClass[] v = /*EL:984*/this.getInterfaces();
            final int v2 = /*EL:985*/v.length;
            /*SL:986*/for (CtField a2 = (CtField)0; a2 < v2; ++a2) {
                /*SL:987*/a2 = v[a2].getField2(v-2, v-1);
                /*SL:988*/if (a2 != null) {
                    /*SL:989*/return a2;
                }
            }
            final CtClass v3 = /*EL:992*/this.getSuperclass();
            /*SL:993*/if (v3 != null) {
                /*SL:994*/return v3.getField2(v-2, v-1);
            }
        }
        catch (NotFoundException ex) {}
        /*SL:997*/return null;
    }
    
    @Override
    public CtField[] getDeclaredFields() {
        final CtMember.Cache v1 = /*EL:1001*/this.getMembers();
        CtMember v2 = /*EL:1002*/v1.fieldHead();
        final CtMember v3 = /*EL:1003*/v1.lastField();
        final int v4 = /*EL:1004*/CtMember.Cache.count(v2, v3);
        final CtField[] v5 = /*EL:1005*/new CtField[v4];
        /*SL:1007*/for (int v6 = 0; v2 != v3; /*SL:1008*/v2 = v2.next(), /*SL:1009*/v5[v6++] = (CtField)v2) {}
        /*SL:1012*/return v5;
    }
    
    @Override
    public CtField getDeclaredField(final String a1) throws NotFoundException {
        /*SL:1016*/return this.getDeclaredField(a1, null);
    }
    
    @Override
    public CtField getDeclaredField(final String a1, final String a2) throws NotFoundException {
        final CtField v1 = /*EL:1020*/this.getDeclaredField2(a1, a2);
        /*SL:1021*/return this.checkGetField(v1, a1, a2);
    }
    
    private CtField getDeclaredField2(final String a1, final String a2) {
        final CtMember.Cache v1 = /*EL:1025*/this.getMembers();
        CtMember v2 = /*EL:1026*/v1.fieldHead();
        final CtMember v3 = /*EL:1027*/v1.lastField();
        /*SL:1028*/while (v2 != v3) {
            /*SL:1029*/v2 = v2.next();
            /*SL:1030*/if (v2.getName().equals(a1) && (a2 == null || a2.equals(v2.getSignature()))) {
                /*SL:1032*/return (CtField)v2;
            }
        }
        /*SL:1035*/return null;
    }
    
    @Override
    public CtBehavior[] getDeclaredBehaviors() {
        final CtMember.Cache v1 = /*EL:1039*/this.getMembers();
        CtMember v2 = /*EL:1040*/v1.consHead();
        final CtMember v3 = /*EL:1041*/v1.lastCons();
        final int v4 = /*EL:1042*/CtMember.Cache.count(v2, v3);
        CtMember v5 = /*EL:1043*/v1.methodHead();
        final CtMember v6 = /*EL:1044*/v1.lastMethod();
        final int v7 = /*EL:1045*/CtMember.Cache.count(v5, v6);
        CtBehavior[] v8;
        int v9;
        /*SL:1049*/for (v8 = new CtBehavior[v4 + v7], v9 = 0; v2 != v3; /*SL:1050*/v2 = v2.next(), /*SL:1051*/v8[v9++] = (CtBehavior)v2) {}
        /*SL:1054*/while (v5 != v6) {
            /*SL:1055*/v5 = v5.next();
            /*SL:1056*/v8[v9++] = (CtBehavior)v5;
        }
        /*SL:1059*/return v8;
    }
    
    @Override
    public CtConstructor[] getConstructors() {
        final CtMember.Cache members = /*EL:1063*/this.getMembers();
        final CtMember consHead = /*EL:1064*/members.consHead();
        final CtMember lastCons = /*EL:1065*/members.lastCons();
        int n = /*EL:1067*/0;
        CtMember ctMember = /*EL:1068*/consHead;
        /*SL:1069*/while (ctMember != lastCons) {
            /*SL:1070*/ctMember = ctMember.next();
            /*SL:1071*/if (isPubCons((CtConstructor)ctMember)) {
                /*SL:1072*/++n;
            }
        }
        final CtConstructor[] array = /*EL:1075*/new CtConstructor[n];
        int v0 = /*EL:1076*/0;
        /*SL:1077*/ctMember = consHead;
        /*SL:1078*/while (ctMember != lastCons) {
            /*SL:1079*/ctMember = ctMember.next();
            final CtConstructor v = /*EL:1080*/(CtConstructor)ctMember;
            /*SL:1081*/if (isPubCons(v)) {
                /*SL:1082*/array[v0++] = v;
            }
        }
        /*SL:1085*/return array;
    }
    
    private static boolean isPubCons(final CtConstructor a1) {
        /*SL:1090*/return !Modifier.isPrivate(a1.getModifiers()) && a1.isConstructor();
    }
    
    @Override
    public CtConstructor getConstructor(final String v2) throws NotFoundException {
        final CtMember.Cache v3 = /*EL:1096*/this.getMembers();
        CtMember v4 = /*EL:1097*/v3.consHead();
        final CtMember v5 = /*EL:1098*/v3.lastCons();
        /*SL:1100*/while (v4 != v5) {
            /*SL:1101*/v4 = v4.next();
            final CtConstructor a1 = /*EL:1102*/(CtConstructor)v4;
            /*SL:1103*/if (a1.getMethodInfo2().getDescriptor().equals(v2) && a1.isConstructor()) {
                /*SL:1105*/return a1;
            }
        }
        /*SL:1108*/return super.getConstructor(v2);
    }
    
    @Override
    public CtConstructor[] getDeclaredConstructors() {
        final CtMember.Cache members = /*EL:1112*/this.getMembers();
        final CtMember consHead = /*EL:1113*/members.consHead();
        final CtMember lastCons = /*EL:1114*/members.lastCons();
        int n = /*EL:1116*/0;
        CtMember v0 = /*EL:1117*/consHead;
        /*SL:1118*/while (v0 != lastCons) {
            /*SL:1119*/v0 = v0.next();
            final CtConstructor v = /*EL:1120*/(CtConstructor)v0;
            /*SL:1121*/if (v.isConstructor()) {
                /*SL:1122*/++n;
            }
        }
        final CtConstructor[] v2 = /*EL:1125*/new CtConstructor[n];
        int v3 = /*EL:1126*/0;
        /*SL:1127*/v0 = consHead;
        /*SL:1128*/while (v0 != lastCons) {
            /*SL:1129*/v0 = v0.next();
            final CtConstructor v4 = /*EL:1130*/(CtConstructor)v0;
            /*SL:1131*/if (v4.isConstructor()) {
                /*SL:1132*/v2[v3++] = v4;
            }
        }
        /*SL:1135*/return v2;
    }
    
    @Override
    public CtConstructor getClassInitializer() {
        final CtMember.Cache members = /*EL:1139*/this.getMembers();
        CtMember ctMember = /*EL:1140*/members.consHead();
        final CtMember v0 = /*EL:1141*/members.lastCons();
        /*SL:1143*/while (ctMember != v0) {
            /*SL:1144*/ctMember = ctMember.next();
            final CtConstructor v = /*EL:1145*/(CtConstructor)ctMember;
            /*SL:1146*/if (v.isClassInitializer()) {
                /*SL:1147*/return v;
            }
        }
        /*SL:1150*/return null;
    }
    
    @Override
    public CtMethod[] getMethods() {
        final HashMap v1 = /*EL:1154*/new HashMap();
        getMethods0(/*EL:1155*/v1, this);
        /*SL:1156*/return (CtMethod[])v1.values().toArray(new CtMethod[v1.size()]);
    }
    
    private static void getMethods0(final HashMap v-2, final CtClass v-1) {
        try {
            CtClass[] a2 = /*EL:1161*/v-1.getInterfaces();
            int v1;
            /*SL:1163*/for (v1 = a2.length, a2 = 0; a2 < v1; ++a2) {
                getMethods0(/*EL:1164*/v-2, a2[a2]);
            }
        }
        catch (NotFoundException ex) {}
        try {
            final CtClass v2 = /*EL:1169*/v-1.getSuperclass();
            /*SL:1170*/if (v2 != null) {
                getMethods0(/*EL:1171*/v-2, v2);
            }
        }
        catch (NotFoundException ex2) {}
        /*SL:1175*/if (v-1 instanceof CtClassType) {
            final CtMember.Cache v3 = /*EL:1176*/((CtClassType)v-1).getMembers();
            CtMember v4 = /*EL:1177*/v3.methodHead();
            final CtMember v5 = /*EL:1178*/v3.lastMethod();
            /*SL:1180*/while (v4 != v5) {
                /*SL:1181*/v4 = v4.next();
                /*SL:1182*/if (!Modifier.isPrivate(v4.getModifiers())) {
                    /*SL:1183*/v-2.put(((CtMethod)v4).getStringRep(), v4);
                }
            }
        }
    }
    
    @Override
    public CtMethod getMethod(final String a1, final String a2) throws NotFoundException {
        final CtMethod v1 = getMethod0(/*EL:1191*/this, a1, a2);
        /*SL:1192*/if (v1 != null) {
            /*SL:1193*/return v1;
        }
        /*SL:1195*/throw new NotFoundException(a1 + "(..) is not found in " + this.getName());
    }
    
    private static CtMethod getMethod0(final CtClass v-3, final String v-2, final String v-1) {
        /*SL:1201*/if (v-3 instanceof CtClassType) {
            final CtMember.Cache a1 = /*EL:1202*/((CtClassType)v-3).getMembers();
            CtMember a2 = /*EL:1203*/a1.methodHead();
            final CtMember a3 = /*EL:1204*/a1.lastMethod();
            /*SL:1206*/while (a2 != a3) {
                /*SL:1207*/a2 = a2.next();
                /*SL:1208*/if (a2.getName().equals(v-2) && ((CtMethod)a2).getMethodInfo2().getDescriptor().equals(/*EL:1209*/v-1)) {
                    /*SL:1210*/return (CtMethod)a2;
                }
            }
        }
        try {
            final CtClass v0 = /*EL:1215*/v-3.getSuperclass();
            /*SL:1216*/if (v0 != null) {
                final CtMethod v = getMethod0(/*EL:1217*/v0, v-2, v-1);
                /*SL:1218*/if (v != null) {
                    /*SL:1219*/return v;
                }
            }
        }
        catch (NotFoundException ex) {}
        try {
            final CtClass[] v2 = /*EL:1225*/v-3.getInterfaces();
            /*SL:1227*/for (int v3 = v2.length, v4 = 0; v4 < v3; ++v4) {
                final CtMethod v5 = getMethod0(/*EL:1228*/v2[v4], v-2, v-1);
                /*SL:1229*/if (v5 != null) {
                    /*SL:1230*/return v5;
                }
            }
        }
        catch (NotFoundException ex2) {}
        /*SL:1234*/return null;
    }
    
    @Override
    public CtMethod[] getDeclaredMethods() {
        final CtMember.Cache v1 = /*EL:1238*/this.getMembers();
        CtMember v2 = /*EL:1239*/v1.methodHead();
        final CtMember v3 = /*EL:1240*/v1.lastMethod();
        final int v4 = /*EL:1241*/CtMember.Cache.count(v2, v3);
        final CtMethod[] v5 = /*EL:1242*/new CtMethod[v4];
        /*SL:1244*/for (int v6 = 0; v2 != v3; /*SL:1245*/v2 = v2.next(), /*SL:1246*/v5[v6++] = (CtMethod)v2) {}
        /*SL:1249*/return v5;
    }
    
    @Override
    public CtMethod[] getDeclaredMethods(final String a1) throws NotFoundException {
        final CtMember.Cache v1 = /*EL:1253*/this.getMembers();
        CtMember v2 = /*EL:1254*/v1.methodHead();
        final CtMember v3 = /*EL:1255*/v1.lastMethod();
        final ArrayList v4 = /*EL:1256*/new ArrayList();
        /*SL:1257*/while (v2 != v3) {
            /*SL:1258*/v2 = v2.next();
            /*SL:1259*/if (v2.getName().equals(a1)) {
                /*SL:1260*/v4.add(v2);
            }
        }
        /*SL:1263*/return v4.<CtMethod>toArray(new CtMethod[v4.size()]);
    }
    
    @Override
    public CtMethod getDeclaredMethod(final String a1) throws NotFoundException {
        final CtMember.Cache v1 = /*EL:1267*/this.getMembers();
        CtMember v2 = /*EL:1268*/v1.methodHead();
        final CtMember v3 = /*EL:1269*/v1.lastMethod();
        /*SL:1270*/while (v2 != v3) {
            /*SL:1271*/v2 = v2.next();
            /*SL:1272*/if (v2.getName().equals(a1)) {
                /*SL:1273*/return (CtMethod)v2;
            }
        }
        /*SL:1276*/throw new NotFoundException(a1 + "(..) is not found in " + this.getName());
    }
    
    @Override
    public CtMethod getDeclaredMethod(final String a1, final CtClass[] a2) throws NotFoundException {
        final String v1 = /*EL:1283*/Descriptor.ofParameters(a2);
        final CtMember.Cache v2 = /*EL:1284*/this.getMembers();
        CtMember v3 = /*EL:1285*/v2.methodHead();
        final CtMember v4 = /*EL:1286*/v2.lastMethod();
        /*SL:1288*/while (v3 != v4) {
            /*SL:1289*/v3 = v3.next();
            /*SL:1290*/if (v3.getName().equals(a1) && ((CtMethod)v3).getMethodInfo2().getDescriptor().startsWith(/*EL:1291*/v1)) {
                /*SL:1292*/return (CtMethod)v3;
            }
        }
        /*SL:1295*/throw new NotFoundException(a1 + "(..) is not found in " + this.getName());
    }
    
    @Override
    public void addField(final CtField a1, final String a2) throws CannotCompileException {
        /*SL:1302*/this.addField(a1, CtField.Initializer.byExpr(a2));
    }
    
    @Override
    public void addField(final CtField v-1, CtField.Initializer v0) throws CannotCompileException {
        /*SL:1308*/this.checkModify();
        /*SL:1309*/if (v-1.getDeclaringClass() != this) {
            /*SL:1310*/throw new CannotCompileException("cannot add");
        }
        /*SL:1312*/if (v0 == null) {
            /*SL:1313*/v0 = v-1.getInit();
        }
        /*SL:1315*/if (v0 != null) {
            /*SL:1316*/v0.check(v-1.getSignature());
            final int v = /*EL:1317*/v-1.getModifiers();
            /*SL:1318*/if (Modifier.isStatic(v) && Modifier.isFinal(v)) {
                try {
                    final ConstPool a1 = /*EL:1320*/this.getClassFile2().getConstPool();
                    final int a2 = /*EL:1321*/v0.getConstantValue(a1, v-1.getType());
                    /*SL:1322*/if (a2 != 0) {
                        /*SL:1323*/v-1.getFieldInfo2().addAttribute(new ConstantAttribute(a1, a2));
                        /*SL:1324*/v0 = null;
                    }
                }
                catch (NotFoundException ex) {}
            }
        }
        /*SL:1330*/this.getMembers().addField(v-1);
        /*SL:1331*/this.getClassFile2().addField(v-1.getFieldInfo2());
        /*SL:1333*/if (v0 != null) {
            final FieldInitLink v2 = /*EL:1334*/new FieldInitLink(v-1, v0);
            FieldInitLink v3 = /*EL:1335*/this.fieldInitializers;
            /*SL:1336*/if (v3 == null) {
                /*SL:1337*/this.fieldInitializers = v2;
            }
            else {
                /*SL:1339*/while (v3.next != null) {
                    /*SL:1340*/v3 = v3.next;
                }
                /*SL:1342*/v3.next = v2;
            }
        }
    }
    
    @Override
    public void removeField(final CtField a1) throws NotFoundException {
        /*SL:1348*/this.checkModify();
        final FieldInfo v1 = /*EL:1349*/a1.getFieldInfo2();
        final ClassFile v2 = /*EL:1350*/this.getClassFile2();
        /*SL:1351*/if (v2.getFields().remove(v1)) {
            /*SL:1352*/this.getMembers().remove(a1);
            /*SL:1353*/this.gcConstPool = true;
            /*SL:1357*/return;
        }
        throw new NotFoundException(a1.toString());
    }
    
    @Override
    public CtConstructor makeClassInitializer() throws CannotCompileException {
        final CtConstructor v1 = /*EL:1362*/this.getClassInitializer();
        /*SL:1363*/if (v1 != null) {
            /*SL:1364*/return v1;
        }
        /*SL:1366*/this.checkModify();
        final ClassFile v2 = /*EL:1367*/this.getClassFile2();
        final Bytecode v3 = /*EL:1368*/new Bytecode(v2.getConstPool(), 0, 0);
        /*SL:1369*/this.modifyClassConstructor(v2, v3, 0, 0);
        /*SL:1370*/return this.getClassInitializer();
    }
    
    @Override
    public void addConstructor(final CtConstructor a1) throws CannotCompileException {
        /*SL:1376*/this.checkModify();
        /*SL:1377*/if (a1.getDeclaringClass() != this) {
            /*SL:1378*/throw new CannotCompileException("cannot add");
        }
        /*SL:1380*/this.getMembers().addConstructor(a1);
        /*SL:1381*/this.getClassFile2().addMethod(a1.getMethodInfo2());
    }
    
    @Override
    public void removeConstructor(final CtConstructor a1) throws NotFoundException {
        /*SL:1385*/this.checkModify();
        final MethodInfo v1 = /*EL:1386*/a1.getMethodInfo2();
        final ClassFile v2 = /*EL:1387*/this.getClassFile2();
        /*SL:1388*/if (v2.getMethods().remove(v1)) {
            /*SL:1389*/this.getMembers().remove(a1);
            /*SL:1390*/this.gcConstPool = true;
            /*SL:1394*/return;
        }
        throw new NotFoundException(a1.toString());
    }
    
    @Override
    public void addMethod(final CtMethod a1) throws CannotCompileException {
        /*SL:1397*/this.checkModify();
        /*SL:1398*/if (a1.getDeclaringClass() != this) {
            /*SL:1399*/throw new CannotCompileException("bad declaring class");
        }
        final int v1 = /*EL:1401*/a1.getModifiers();
        /*SL:1402*/if ((this.getModifiers() & 0x200) != 0x0) {
            /*SL:1403*/if (Modifier.isProtected(v1) || Modifier.isPrivate(v1)) {
                /*SL:1404*/throw new CannotCompileException("an interface method must be public: " + a1.toString());
            }
            /*SL:1407*/a1.setModifiers(v1 | 0x1);
        }
        /*SL:1410*/this.getMembers().addMethod(a1);
        /*SL:1411*/this.getClassFile2().addMethod(a1.getMethodInfo2());
        /*SL:1412*/if ((v1 & 0x400) != 0x0) {
            /*SL:1413*/this.setModifiers(this.getModifiers() | 0x400);
        }
    }
    
    @Override
    public void removeMethod(final CtMethod a1) throws NotFoundException {
        /*SL:1417*/this.checkModify();
        final MethodInfo v1 = /*EL:1418*/a1.getMethodInfo2();
        final ClassFile v2 = /*EL:1419*/this.getClassFile2();
        /*SL:1420*/if (v2.getMethods().remove(v1)) {
            /*SL:1421*/this.getMembers().remove(a1);
            /*SL:1422*/this.gcConstPool = true;
            /*SL:1426*/return;
        }
        throw new NotFoundException(a1.toString());
    }
    
    @Override
    public byte[] getAttribute(final String a1) {
        final AttributeInfo v1 = /*EL:1429*/this.getClassFile2().getAttribute(a1);
        /*SL:1430*/if (v1 == null) {
            /*SL:1431*/return null;
        }
        /*SL:1433*/return v1.get();
    }
    
    @Override
    public void setAttribute(final String a1, final byte[] a2) {
        /*SL:1437*/this.checkModify();
        final ClassFile v1 = /*EL:1438*/this.getClassFile2();
        /*SL:1439*/v1.addAttribute(new AttributeInfo(v1.getConstPool(), a1, a2));
    }
    
    @Override
    public void instrument(final CodeConverter v-4) throws CannotCompileException {
        /*SL:1445*/this.checkModify();
        final ClassFile classFile2 = /*EL:1446*/this.getClassFile2();
        final ConstPool constPool = /*EL:1447*/classFile2.getConstPool();
        final List methods = /*EL:1448*/classFile2.getMethods();
        /*SL:1450*/for (int v0 = methods.size(), v = 0; v < v0; ++v) {
            final MethodInfo a1 = /*EL:1451*/methods.get(v);
            /*SL:1452*/v-4.doit(this, a1, constPool);
        }
    }
    
    @Override
    public void instrument(final ExprEditor v-3) throws CannotCompileException {
        /*SL:1459*/this.checkModify();
        final ClassFile classFile2 = /*EL:1460*/this.getClassFile2();
        final List methods = /*EL:1461*/classFile2.getMethods();
        /*SL:1463*/for (int v0 = methods.size(), v = 0; v < v0; ++v) {
            final MethodInfo a1 = /*EL:1464*/methods.get(v);
            /*SL:1465*/v-3.doit(this, a1);
        }
    }
    
    @Override
    public void prune() {
        /*SL:1474*/if (this.wasPruned) {
            /*SL:1475*/return;
        }
        final boolean b = /*EL:1477*/true;
        this.wasFrozen = b;
        this.wasPruned = b;
        /*SL:1478*/this.getClassFile2().prune();
    }
    
    @Override
    public void rebuildClassFile() {
        /*SL:1481*/this.gcConstPool = true;
    }
    
    @Override
    public void toBytecode(final DataOutputStream v0) throws CannotCompileException, IOException {
        try {
            /*SL:1487*/if (this.isModified()) {
                /*SL:1488*/this.checkPruned("toBytecode");
                final ClassFile a1 = /*EL:1489*/this.getClassFile2();
                /*SL:1490*/if (this.gcConstPool) {
                    /*SL:1491*/a1.compact();
                    /*SL:1492*/this.gcConstPool = false;
                }
                /*SL:1495*/this.modifyClassConstructor(a1);
                /*SL:1496*/this.modifyConstructors(a1);
                /*SL:1497*/if (CtClassType.debugDump != null) {
                    /*SL:1498*/this.dumpClassFile(a1);
                }
                /*SL:1500*/a1.write(v0);
                /*SL:1501*/v0.flush();
                /*SL:1502*/this.fieldInitializers = null;
                /*SL:1503*/if (this.doPruning) {
                    /*SL:1505*/a1.prune();
                    /*SL:1506*/this.wasPruned = true;
                }
            }
            else {
                /*SL:1510*/this.classPool.writeClassfile(this.getName(), v0);
            }
            /*SL:1515*/this.getCount = 0;
            /*SL:1516*/this.wasFrozen = true;
        }
        catch (NotFoundException v) {
            /*SL:1519*/throw new CannotCompileException(v);
        }
        catch (IOException v2) {
            /*SL:1522*/throw new CannotCompileException(v2);
        }
    }
    
    private void dumpClassFile(final ClassFile a1) throws IOException {
        final DataOutputStream v1 = /*EL:1527*/this.makeFileOutput(CtClassType.debugDump);
        try {
            /*SL:1529*/a1.write(v1);
        }
        finally {
            /*SL:1532*/v1.close();
        }
    }
    
    private void checkPruned(final String a1) {
        /*SL:1539*/if (this.wasPruned) {
            /*SL:1540*/throw new RuntimeException(a1 + "(): " + this.getName() + " was pruned.");
        }
    }
    
    @Override
    public boolean stopPruning(final boolean a1) {
        final boolean v1 = /*EL:1545*/!this.doPruning;
        /*SL:1546*/this.doPruning = !a1;
        /*SL:1547*/return v1;
    }
    
    private void modifyClassConstructor(final ClassFile v-5) throws CannotCompileException, NotFoundException {
        /*SL:1553*/if (this.fieldInitializers == null) {
            /*SL:1554*/return;
        }
        final Bytecode bytecode = /*EL:1556*/new Bytecode(v-5.getConstPool(), 0, 0);
        final Javac javac = /*EL:1557*/new Javac(bytecode, this);
        int v-6 = /*EL:1558*/0;
        boolean b = /*EL:1559*/false;
        /*SL:1560*/for (FieldInitLink v0 = this.fieldInitializers; v0 != null; v0 = v0.next) {
            final CtField v = /*EL:1561*/v0.field;
            /*SL:1562*/if (Modifier.isStatic(v.getModifiers())) {
                /*SL:1563*/b = true;
                final int a1 = /*EL:1564*/v0.init.compileIfStatic(v.getType(), v.getName(), bytecode, javac);
                /*SL:1566*/if (v-6 < a1) {
                    /*SL:1567*/v-6 = a1;
                }
            }
        }
        /*SL:1571*/if (b) {
            /*SL:1572*/this.modifyClassConstructor(v-5, bytecode, v-6, 0);
        }
    }
    
    private void modifyClassConstructor(final ClassFile v-8, final Bytecode v-7, final int v-6, final int v-5) throws CannotCompileException {
        MethodInfo staticInitializer = /*EL:1579*/v-8.getStaticInitializer();
        /*SL:1580*/if (staticInitializer == null) {
            /*SL:1581*/v-7.add(177);
            /*SL:1582*/v-7.setMaxStack(v-6);
            /*SL:1583*/v-7.setMaxLocals(v-5);
            /*SL:1584*/staticInitializer = new MethodInfo(v-8.getConstPool(), "<clinit>", "()V");
            /*SL:1585*/staticInitializer.setAccessFlags(8);
            /*SL:1586*/staticInitializer.setCodeAttribute(v-7.toCodeAttribute());
            /*SL:1587*/v-8.addMethod(staticInitializer);
            final CtMember.Cache a1 = /*EL:1588*/this.hasMemberCache();
            /*SL:1589*/if (a1 != null) {
                /*SL:1590*/a1.addConstructor(new CtConstructor(staticInitializer, this));
            }
        }
        else {
            final CodeAttribute codeAttribute = /*EL:1593*/staticInitializer.getCodeAttribute();
            /*SL:1594*/if (codeAttribute == null) {
                /*SL:1595*/throw new CannotCompileException("empty <clinit>");
            }
            try {
                final CodeIterator a2 = /*EL:1598*/codeAttribute.iterator();
                final int a3 = /*EL:1599*/a2.insertEx(v-7.get());
                /*SL:1600*/a2.insert(v-7.getExceptionTable(), a3);
                final int a4 = /*EL:1601*/codeAttribute.getMaxStack();
                /*SL:1602*/if (a4 < v-6) {
                    /*SL:1603*/codeAttribute.setMaxStack(v-6);
                }
                final int v1 = /*EL:1605*/codeAttribute.getMaxLocals();
                /*SL:1606*/if (v1 < v-5) {
                    /*SL:1607*/codeAttribute.setMaxLocals(v-5);
                }
            }
            catch (BadBytecode a5) {
                /*SL:1610*/throw new CannotCompileException(a5);
            }
        }
        try {
            /*SL:1615*/staticInitializer.rebuildStackMapIf6(this.classPool, v-8);
        }
        catch (BadBytecode a6) {
            /*SL:1618*/throw new CannotCompileException(a6);
        }
    }
    
    private void modifyConstructors(final ClassFile v-7) throws CannotCompileException, NotFoundException {
        /*SL:1625*/if (this.fieldInitializers == null) {
            /*SL:1626*/return;
        }
        final ConstPool constPool = /*EL:1628*/v-7.getConstPool();
        final List methods = /*EL:1629*/v-7.getMethods();
        /*SL:1631*/for (int size = methods.size(), i = 0; i < size; ++i) {
            final MethodInfo methodInfo = /*EL:1632*/methods.get(i);
            /*SL:1633*/if (methodInfo.isConstructor()) {
                final CodeAttribute codeAttribute = /*EL:1634*/methodInfo.getCodeAttribute();
                /*SL:1635*/if (codeAttribute != null) {
                    try {
                        final Bytecode a1 = /*EL:1637*/new Bytecode(constPool, 0, codeAttribute.getMaxLocals());
                        final CtClass[] v1 = /*EL:1640*/Descriptor.getParameterTypes(methodInfo.getDescriptor(), /*EL:1641*/this.classPool);
                        final int v2 = /*EL:1643*/this.makeFieldInitializer(a1, v1);
                        insertAuxInitializer(/*EL:1644*/codeAttribute, a1, v2);
                        /*SL:1645*/methodInfo.rebuildStackMapIf6(this.classPool, v-7);
                    }
                    catch (BadBytecode v3) {
                        /*SL:1648*/throw new CannotCompileException(v3);
                    }
                }
            }
        }
    }
    
    private static void insertAuxInitializer(final CodeAttribute a1, final Bytecode a2, final int a3) throws BadBytecode {
        final CodeIterator v1 = /*EL:1659*/a1.iterator();
        int v2 = /*EL:1660*/v1.skipSuperConstructor();
        /*SL:1661*/if (v2 < 0) {
            /*SL:1662*/v2 = v1.skipThisConstructor();
            /*SL:1663*/if (v2 >= 0) {
                /*SL:1664*/return;
            }
        }
        final int v3 = /*EL:1669*/v1.insertEx(a2.get());
        /*SL:1670*/v1.insert(a2.getExceptionTable(), v3);
        final int v4 = /*EL:1671*/a1.getMaxStack();
        /*SL:1672*/if (v4 < a3) {
            /*SL:1673*/a1.setMaxStack(a3);
        }
    }
    
    private int makeFieldInitializer(final Bytecode v-4, final CtClass[] v-3) throws CannotCompileException, NotFoundException {
        int n = /*EL:1679*/0;
        final Javac javac = /*EL:1680*/new Javac(v-4, this);
        try {
            /*SL:1682*/javac.recordParams(v-3, false);
        }
        catch (CompileError a1) {
            /*SL:1685*/throw new CannotCompileException(a1);
        }
        /*SL:1688*/for (FieldInitLink v0 = this.fieldInitializers; v0 != null; v0 = v0.next) {
            final CtField v = /*EL:1689*/v0.field;
            /*SL:1690*/if (!Modifier.isStatic(v.getModifiers())) {
                final int a2 = /*EL:1691*/v0.init.compile(v.getType(), v.getName(), v-4, v-3, javac);
                /*SL:1693*/if (n < a2) {
                    /*SL:1694*/n = a2;
                }
            }
        }
        /*SL:1698*/return n;
    }
    
    Hashtable getHiddenMethods() {
        /*SL:1704*/if (this.hiddenMethods == null) {
            /*SL:1705*/this.hiddenMethods = new Hashtable();
        }
        /*SL:1707*/return this.hiddenMethods;
    }
    
    int getUniqueNumber() {
        /*SL:1710*/return this.uniqueNumberSeed++;
    }
    
    @Override
    public String makeUniqueName(final String a1) {
        final HashMap v1 = /*EL:1713*/new HashMap();
        /*SL:1714*/this.makeMemberList(v1);
        final Set v2 = /*EL:1715*/v1.keySet();
        final String[] v3 = /*EL:1716*/new String[v2.size()];
        /*SL:1717*/v2.<String>toArray(v3);
        /*SL:1719*/if (notFindInArray(a1, v3)) {
            /*SL:1720*/return a1;
        }
        int v4 = /*EL:1722*/100;
        /*SL:1725*/while (v4 <= 999) {
            final String v5 = /*EL:1728*/a1 + v4++;
            /*SL:1729*/if (notFindInArray(v5, v3)) {
                /*SL:1730*/return v5;
            }
        }
        throw new RuntimeException("too many unique name");
    }
    
    private static boolean notFindInArray(final String a2, final String[] v1) {
        /*SL:1735*/for (int v2 = v1.length, a3 = 0; a3 < v2; ++a3) {
            /*SL:1736*/if (v1[a3].startsWith(a2)) {
                /*SL:1737*/return false;
            }
        }
        /*SL:1739*/return true;
    }
    
    private void makeMemberList(final HashMap v-3) {
        final int modifiers = /*EL:1743*/this.getModifiers();
        Label_0076: {
            /*SL:1744*/if (!Modifier.isAbstract(modifiers)) {
                if (!Modifier.isInterface(modifiers)) {
                    break Label_0076;
                }
            }
            try {
                /*SL:1748*/for (final CtClass a1 : this.getInterfaces()) {
                    /*SL:1750*/if (a1 != null && a1 instanceof CtClassType) {
                        /*SL:1751*/((CtClassType)a1).makeMemberList(v-3);
                    }
                }
            }
            catch (NotFoundException ex) {}
            try {
                final CtClass superclass = /*EL:1757*/this.getSuperclass();
                /*SL:1758*/if (superclass != null && superclass instanceof CtClassType) {
                    /*SL:1759*/((CtClassType)superclass).makeMemberList(v-3);
                }
            }
            catch (NotFoundException ex2) {}
        }
        List list = /*EL:1763*/this.getClassFile2().getMethods();
        /*SL:1765*/for (int v0 = list.size(), v = 0; v < v0; ++v) {
            final MethodInfo v2 = /*EL:1766*/list.get(v);
            /*SL:1767*/v-3.put(v2.getName(), this);
        }
        /*SL:1770*/list = this.getClassFile2().getFields();
        /*SL:1772*/for (int v0 = list.size(), v = 0; v < v0; ++v) {
            final FieldInfo v3 = /*EL:1773*/list.get(v);
            /*SL:1774*/v-3.put(v3.getName(), this);
        }
    }
}
