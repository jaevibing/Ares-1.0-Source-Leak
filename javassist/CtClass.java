package javassist;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.security.ProtectionDomain;
import javassist.expr.ExprEditor;
import javassist.bytecode.Descriptor;
import java.util.Map;
import java.util.Collection;
import java.net.URL;
import javassist.compiler.AccessorMaker;
import javassist.bytecode.ClassFile;

public abstract class CtClass
{
    protected String qualifiedName;
    public static String debugDump;
    public static final String version = "3.21.0-GA";
    static final String javaLangObject = "java.lang.Object";
    public static CtClass booleanType;
    public static CtClass charType;
    public static CtClass byteType;
    public static CtClass shortType;
    public static CtClass intType;
    public static CtClass longType;
    public static CtClass floatType;
    public static CtClass doubleType;
    public static CtClass voidType;
    static CtClass[] primitiveTypes;
    
    public static void main(final String[] a1) {
        System.out.println(/*EL:82*/"Javassist version 3.21.0-GA");
        System.out.println(/*EL:83*/"Copyright (C) 1999-2016 Shigeru Chiba. All Rights Reserved.");
    }
    
    protected CtClass(final String a1) {
        this.qualifiedName = a1;
    }
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:202*/new StringBuffer(this.getClass().getName());
        /*SL:203*/v1.append("@");
        /*SL:204*/v1.append(Integer.toHexString(this.hashCode()));
        /*SL:205*/v1.append("[");
        /*SL:206*/this.extendToString(v1);
        /*SL:207*/v1.append("]");
        /*SL:208*/return v1.toString();
    }
    
    protected void extendToString(final StringBuffer a1) {
        /*SL:216*/a1.append(this.getName());
    }
    
    public ClassPool getClassPool() {
        /*SL:222*/return null;
    }
    
    public ClassFile getClassFile() {
        /*SL:231*/this.checkModify();
        /*SL:232*/return this.getClassFile2();
    }
    
    public ClassFile getClassFile2() {
        /*SL:253*/return null;
    }
    
    public AccessorMaker getAccessorMaker() {
        /*SL:259*/return null;
    }
    
    public URL getURL() throws NotFoundException {
        /*SL:266*/throw new NotFoundException(this.getName());
    }
    
    public boolean isModified() {
        /*SL:272*/return false;
    }
    
    public boolean isFrozen() {
        /*SL:281*/return true;
    }
    
    public void freeze() {
    }
    
    void checkModify() throws RuntimeException {
        /*SL:295*/if (this.isFrozen()) {
            /*SL:296*/throw new RuntimeException(this.getName() + " class is frozen");
        }
    }
    
    public void defrost() {
        /*SL:317*/throw new RuntimeException("cannot defrost " + this.getName());
    }
    
    public boolean isPrimitive() {
        /*SL:325*/return false;
    }
    
    public boolean isArray() {
        /*SL:331*/return false;
    }
    
    public CtClass getComponentType() throws NotFoundException {
        /*SL:339*/return null;
    }
    
    public boolean subtypeOf(final CtClass a1) throws NotFoundException {
        /*SL:348*/return this == a1 || this.getName().equals(a1.getName());
    }
    
    public String getName() {
        /*SL:354*/return this.qualifiedName;
    }
    
    public final String getSimpleName() {
        final String v1 = /*EL:360*/this.qualifiedName;
        final int v2 = /*EL:361*/v1.lastIndexOf(46);
        /*SL:362*/if (v2 < 0) {
            /*SL:363*/return v1;
        }
        /*SL:365*/return v1.substring(v2 + 1);
    }
    
    public final String getPackageName() {
        final String v1 = /*EL:372*/this.qualifiedName;
        final int v2 = /*EL:373*/v1.lastIndexOf(46);
        /*SL:374*/if (v2 < 0) {
            /*SL:375*/return null;
        }
        /*SL:377*/return v1.substring(0, v2);
    }
    
    public void setName(final String a1) {
        /*SL:386*/this.checkModify();
        /*SL:387*/if (a1 != null) {
            /*SL:388*/this.qualifiedName = a1;
        }
    }
    
    public String getGenericSignature() {
        /*SL:405*/return null;
    }
    
    public void setGenericSignature(final String a1) {
        /*SL:477*/this.checkModify();
    }
    
    public void replaceClassName(final String a1, final String a2) {
        /*SL:487*/this.checkModify();
    }
    
    public void replaceClassName(final ClassMap a1) {
        /*SL:508*/this.checkModify();
    }
    
    public synchronized Collection getRefClasses() {
        final ClassFile v0 = /*EL:521*/this.getClassFile2();
        /*SL:522*/if (v0 != null) {
            final ClassMap v = /*EL:523*/new ClassMap() {
                @Override
                public void put(final String a1, final String a2) {
                    /*SL:525*/this.put0(a1, a2);
                }
                
                @Override
                public Object get(final Object a1) {
                    final String v1 = /*EL:529*/ClassMap.toJavaName((String)a1);
                    /*SL:530*/this.put0(v1, v1);
                    /*SL:531*/return null;
                }
                
                @Override
                public void fix(final String a1) {
                }
            };
            /*SL:536*/v0.getRefClasses(v);
            /*SL:537*/return v.values();
        }
        /*SL:540*/return null;
    }
    
    public boolean isInterface() {
        /*SL:548*/return false;
    }
    
    public boolean isAnnotation() {
        /*SL:558*/return false;
    }
    
    public boolean isEnum() {
        /*SL:568*/return false;
    }
    
    public int getModifiers() {
        /*SL:581*/return 0;
    }
    
    public boolean hasAnnotation(final Class a1) {
        /*SL:592*/return this.hasAnnotation(a1.getName());
    }
    
    public boolean hasAnnotation(final String a1) {
        /*SL:603*/return false;
    }
    
    public Object getAnnotation(final Class a1) throws ClassNotFoundException {
        /*SL:618*/return null;
    }
    
    public Object[] getAnnotations() throws ClassNotFoundException {
        /*SL:633*/return new Object[0];
    }
    
    public Object[] getAvailableAnnotations() {
        /*SL:648*/return new Object[0];
    }
    
    public CtClass[] getDeclaredClasses() throws NotFoundException {
        /*SL:660*/return this.getNestedClasses();
    }
    
    public CtClass[] getNestedClasses() throws NotFoundException {
        /*SL:671*/return new CtClass[0];
    }
    
    public void setModifiers(final int a1) {
        /*SL:686*/this.checkModify();
    }
    
    public boolean subclassOf(final CtClass a1) {
        /*SL:698*/return false;
    }
    
    public CtClass getSuperclass() throws NotFoundException {
        /*SL:714*/return null;
    }
    
    public void setSuperclass(final CtClass a1) throws CannotCompileException {
        /*SL:731*/this.checkModify();
    }
    
    public CtClass[] getInterfaces() throws NotFoundException {
        /*SL:740*/return new CtClass[0];
    }
    
    public void setInterfaces(final CtClass[] a1) {
        /*SL:753*/this.checkModify();
    }
    
    public void addInterface(final CtClass a1) {
        /*SL:762*/this.checkModify();
    }
    
    public CtClass getDeclaringClass() throws NotFoundException {
        /*SL:772*/return null;
    }
    
    public final CtMethod getEnclosingMethod() throws NotFoundException {
        final CtBehavior v1 = /*EL:786*/this.getEnclosingBehavior();
        /*SL:787*/if (v1 == null) {
            /*SL:788*/return null;
        }
        /*SL:789*/if (v1 instanceof CtMethod) {
            /*SL:790*/return (CtMethod)v1;
        }
        /*SL:792*/throw new NotFoundException(v1.getLongName() + " is enclosing " + this.getName());
    }
    
    public CtBehavior getEnclosingBehavior() throws NotFoundException {
        /*SL:804*/return null;
    }
    
    public CtClass makeNestedClass(final String a1, final boolean a2) {
        /*SL:819*/throw new RuntimeException(this.getName() + " is not a class");
    }
    
    public CtField[] getFields() {
        /*SL:828*/return new CtField[0];
    }
    
    public CtField getField(final String a1) throws NotFoundException {
        /*SL:835*/return this.getField(a1, null);
    }
    
    public CtField getField(final String a1, final String a2) throws NotFoundException {
        /*SL:850*/throw new NotFoundException(a1);
    }
    
    CtField getField2(final String a1, final String a2) {
        /*SL:856*/return null;
    }
    
    public CtField[] getDeclaredFields() {
        /*SL:864*/return new CtField[0];
    }
    
    public CtField getDeclaredField(final String a1) throws NotFoundException {
        /*SL:873*/throw new NotFoundException(a1);
    }
    
    public CtField getDeclaredField(final String a1, final String a2) throws NotFoundException {
        /*SL:889*/throw new NotFoundException(a1);
    }
    
    public CtBehavior[] getDeclaredBehaviors() {
        /*SL:896*/return new CtBehavior[0];
    }
    
    public CtConstructor[] getConstructors() {
        /*SL:904*/return new CtConstructor[0];
    }
    
    public CtConstructor getConstructor(final String a1) throws NotFoundException {
        /*SL:920*/throw new NotFoundException("no such constructor");
    }
    
    public CtConstructor[] getDeclaredConstructors() {
        /*SL:929*/return new CtConstructor[0];
    }
    
    public CtConstructor getDeclaredConstructor(final CtClass[] a1) throws NotFoundException {
        final String v1 = /*EL:940*/Descriptor.ofConstructor(a1);
        /*SL:941*/return this.getConstructor(v1);
    }
    
    public CtConstructor getClassInitializer() {
        /*SL:954*/return null;
    }
    
    public CtMethod[] getMethods() {
        /*SL:964*/return new CtMethod[0];
    }
    
    public CtMethod getMethod(final String a1, final String a2) throws NotFoundException {
        /*SL:982*/throw new NotFoundException(a1);
    }
    
    public CtMethod[] getDeclaredMethods() {
        /*SL:992*/return new CtMethod[0];
    }
    
    public CtMethod getDeclaredMethod(final String a1, final CtClass[] a2) throws NotFoundException {
        /*SL:1008*/throw new NotFoundException(a1);
    }
    
    public CtMethod[] getDeclaredMethods(final String a1) throws NotFoundException {
        /*SL:1022*/throw new NotFoundException(a1);
    }
    
    public CtMethod getDeclaredMethod(final String a1) throws NotFoundException {
        /*SL:1035*/throw new NotFoundException(a1);
    }
    
    public CtConstructor makeClassInitializer() throws CannotCompileException {
        /*SL:1048*/throw new CannotCompileException("not a class");
    }
    
    public void addConstructor(final CtConstructor a1) throws CannotCompileException {
        /*SL:1060*/this.checkModify();
    }
    
    public void removeConstructor(final CtConstructor a1) throws NotFoundException {
        /*SL:1070*/this.checkModify();
    }
    
    public void addMethod(final CtMethod a1) throws CannotCompileException {
        /*SL:1077*/this.checkModify();
    }
    
    public void removeMethod(final CtMethod a1) throws NotFoundException {
        /*SL:1087*/this.checkModify();
    }
    
    public void addField(final CtField a1) throws CannotCompileException {
        /*SL:1100*/this.addField(a1, (CtField.Initializer)null);
    }
    
    public void addField(final CtField a1, final String a2) throws CannotCompileException {
        /*SL:1136*/this.checkModify();
    }
    
    public void addField(final CtField a1, final CtField.Initializer a2) throws CannotCompileException {
        /*SL:1164*/this.checkModify();
    }
    
    public void removeField(final CtField a1) throws NotFoundException {
        /*SL:1174*/this.checkModify();
    }
    
    public byte[] getAttribute(final String a1) {
        /*SL:1195*/return null;
    }
    
    public void setAttribute(final String a1, final byte[] a2) {
        /*SL:1221*/this.checkModify();
    }
    
    public void instrument(final CodeConverter a1) throws CannotCompileException {
        /*SL:1235*/this.checkModify();
    }
    
    public void instrument(final ExprEditor a1) throws CannotCompileException {
        /*SL:1249*/this.checkModify();
    }
    
    public Class toClass() throws CannotCompileException {
        /*SL:1275*/return this.getClassPool().toClass(this);
    }
    
    public Class toClass(ClassLoader a1, final ProtectionDomain a2) throws CannotCompileException {
        final ClassPool v1 = /*EL:1312*/this.getClassPool();
        /*SL:1313*/if (a1 == null) {
            /*SL:1314*/a1 = v1.getClassLoader();
        }
        /*SL:1316*/return v1.toClass(this, a1, a2);
    }
    
    public final Class toClass(final ClassLoader a1) throws CannotCompileException {
        /*SL:1331*/return this.getClassPool().toClass(this, a1);
    }
    
    public void detach() {
        final ClassPool v1 = /*EL:1347*/this.getClassPool();
        final CtClass v2 = /*EL:1348*/v1.removeCached(this.getName());
        /*SL:1349*/if (v2 != this) {
            /*SL:1350*/v1.cacheCtClass(this.getName(), v2, false);
        }
    }
    
    public boolean stopPruning(final boolean a1) {
        /*SL:1377*/return true;
    }
    
    public void prune() {
    }
    
    void incGetCounter() {
    }
    
    public void rebuildClassFile() {
    }
    
    public byte[] toBytecode() throws IOException, CannotCompileException {
        final ByteArrayOutputStream v1 = /*EL:1439*/new ByteArrayOutputStream();
        final DataOutputStream v2 = /*EL:1440*/new DataOutputStream(v1);
        try {
            /*SL:1442*/this.toBytecode(v2);
        }
        finally {
            /*SL:1445*/v2.close();
        }
        /*SL:1448*/return v1.toByteArray();
    }
    
    public void writeFile() throws NotFoundException, IOException, CannotCompileException {
        /*SL:1462*/this.writeFile(".");
    }
    
    public void writeFile(final String a1) throws CannotCompileException, IOException {
        final DataOutputStream v1 = /*EL:1477*/this.makeFileOutput(a1);
        try {
            /*SL:1479*/this.toBytecode(v1);
        }
        finally {
            /*SL:1482*/v1.close();
        }
    }
    
    protected DataOutputStream makeFileOutput(final String v2) {
        final String v3 = /*EL:1487*/this.getName();
        final String v4 = /*EL:1488*/v2 + File.separatorChar + v3.replace('.', File.separatorChar) + /*EL:1489*/".class";
        final int v5 = /*EL:1490*/v4.lastIndexOf(File.separatorChar);
        /*SL:1491*/if (v5 > 0) {
            final String a1 = /*EL:1492*/v4.substring(0, v5);
            /*SL:1493*/if (!a1.equals(".")) {
                /*SL:1494*/new File(a1).mkdirs();
            }
        }
        /*SL:1497*/return new DataOutputStream(new BufferedOutputStream(new DelayedFileOutputStream(v4)));
    }
    
    public void debugWriteFile() {
        /*SL:1509*/this.debugWriteFile(".");
    }
    
    public void debugWriteFile(final String v0) {
        try {
            final boolean a1 = /*EL:1523*/this.stopPruning(true);
            /*SL:1524*/this.writeFile(v0);
            /*SL:1525*/this.defrost();
            /*SL:1526*/this.stopPruning(a1);
        }
        catch (Exception v) {
            /*SL:1529*/throw new RuntimeException(v);
        }
    }
    
    public void toBytecode(final DataOutputStream a1) throws CannotCompileException, IOException {
        /*SL:1586*/throw new CannotCompileException("not a class");
    }
    
    public String makeUniqueName(final String a1) {
        /*SL:1599*/throw new RuntimeException("not available in " + this.getName());
    }
    
    void compress() {
    }
    
    static {
        CtClass.debugDump = null;
        CtClass.primitiveTypes = new CtClass[9];
        CtClass.booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);
        CtClass.primitiveTypes[0] = CtClass.booleanType;
        CtClass.charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
        CtClass.primitiveTypes[1] = CtClass.charType;
        CtClass.byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
        CtClass.primitiveTypes[2] = CtClass.byteType;
        CtClass.shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
        CtClass.primitiveTypes[3] = CtClass.shortType;
        CtClass.intType = new CtPrimitiveType("int", 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
        CtClass.primitiveTypes[4] = CtClass.intType;
        CtClass.longType = new CtPrimitiveType("long", 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
        CtClass.primitiveTypes[5] = CtClass.longType;
        CtClass.floatType = new CtPrimitiveType("float", 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
        CtClass.primitiveTypes[6] = CtClass.floatType;
        CtClass.doubleType = new CtPrimitiveType("double", 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
        CtClass.primitiveTypes[7] = CtClass.doubleType;
        CtClass.voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", null, null, 177, 0, 0);
        CtClass.primitiveTypes[8] = CtClass.voidType;
    }
    
    static class DelayedFileOutputStream extends OutputStream
    {
        private FileOutputStream file;
        private String filename;
        
        DelayedFileOutputStream(final String a1) {
            this.file = null;
            this.filename = a1;
        }
        
        private void init() throws IOException {
            /*SL:1543*/if (this.file == null) {
                /*SL:1544*/this.file = new FileOutputStream(this.filename);
            }
        }
        
        @Override
        public void write(final int a1) throws IOException {
            /*SL:1548*/this.init();
            /*SL:1549*/this.file.write(a1);
        }
        
        @Override
        public void write(final byte[] a1) throws IOException {
            /*SL:1553*/this.init();
            /*SL:1554*/this.file.write(a1);
        }
        
        @Override
        public void write(final byte[] a1, final int a2, final int a3) throws IOException {
            /*SL:1558*/this.init();
            /*SL:1559*/this.file.write(a1, a2, a3);
        }
        
        @Override
        public void flush() throws IOException {
            /*SL:1564*/this.init();
            /*SL:1565*/this.file.flush();
        }
        
        @Override
        public void close() throws IOException {
            /*SL:1569*/this.init();
            /*SL:1570*/this.file.close();
        }
    }
}
