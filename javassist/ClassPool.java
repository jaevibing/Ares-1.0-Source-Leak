package javassist;

import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.io.File;
import javassist.bytecode.ClassFile;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;
import javassist.bytecode.Descriptor;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.reflect.Method;

public class ClassPool
{
    private static Method defineClass1;
    private static Method defineClass2;
    private static Method definePackage;
    public boolean childFirstLookup;
    public static boolean doPruning;
    private int compressCount;
    private static final int COMPRESS_THRESHOLD = 100;
    public static boolean releaseUnmodifiedClassFile;
    protected ClassPoolTail source;
    protected ClassPool parent;
    protected Hashtable classes;
    private Hashtable cflow;
    private static final int INIT_HASH_SIZE = 191;
    private ArrayList importedPackages;
    private static ClassPool defaultPool;
    
    public ClassPool() {
        this(null);
    }
    
    public ClassPool(final boolean a1) {
        this(null);
        if (a1) {
            this.appendSystemPath();
        }
    }
    
    public ClassPool(final ClassPool v0) {
        this.childFirstLookup = false;
        this.cflow = null;
        this.classes = new Hashtable(191);
        this.source = new ClassPoolTail();
        this.parent = v0;
        if (v0 == null) {
            final CtClass[] v = CtClass.primitiveTypes;
            for (int a1 = 0; a1 < v.length; ++a1) {
                this.classes.put(v[a1].getName(), v[a1]);
            }
        }
        this.cflow = null;
        this.compressCount = 0;
        this.clearImportedPackages();
    }
    
    public static synchronized ClassPool getDefault() {
        /*SL:229*/if (ClassPool.defaultPool == null) {
            (ClassPool.defaultPool = /*EL:230*/new ClassPool(null)).appendSystemPath();
        }
        /*SL:234*/return ClassPool.defaultPool;
    }
    
    protected CtClass getCached(final String a1) {
        /*SL:247*/return this.classes.get(a1);
    }
    
    protected void cacheCtClass(final String a1, final CtClass a2, final boolean a3) {
        /*SL:258*/this.classes.put(a1, a2);
    }
    
    protected CtClass removeCached(final String a1) {
        /*SL:269*/return this.classes.remove(a1);
    }
    
    @Override
    public String toString() {
        /*SL:276*/return this.source.toString();
    }
    
    void compress() {
        /*SL:284*/if (this.compressCount++ > 100) {
            /*SL:285*/this.compressCount = 0;
            final Enumeration v1 = /*EL:286*/this.classes.elements();
            /*SL:287*/while (v1.hasMoreElements()) {
                /*SL:288*/v1.nextElement().compress();
            }
        }
    }
    
    public void importPackage(final String a1) {
        /*SL:310*/this.importedPackages.add(a1);
    }
    
    public void clearImportedPackages() {
        /*SL:321*/(this.importedPackages = new ArrayList()).add(/*EL:322*/"java.lang");
    }
    
    public Iterator getImportedPackages() {
        /*SL:332*/return this.importedPackages.iterator();
    }
    
    public void recordInvalidClassName(final String a1) {
    }
    
    void recordCflow(final String a1, final String a2, final String a3) {
        /*SL:362*/if (this.cflow == null) {
            /*SL:363*/this.cflow = new Hashtable();
        }
        /*SL:365*/this.cflow.put(a1, new Object[] { a2, a3 });
    }
    
    public Object[] lookupCflow(final String a1) {
        /*SL:374*/if (this.cflow == null) {
            /*SL:375*/this.cflow = new Hashtable();
        }
        /*SL:377*/return this.cflow.get(a1);
    }
    
    public CtClass getAndRename(final String a1, final String a2) throws NotFoundException {
        final CtClass v1 = /*EL:401*/this.get0(a1, false);
        /*SL:402*/if (v1 == null) {
            /*SL:403*/throw new NotFoundException(a1);
        }
        /*SL:405*/if (v1 instanceof CtClassType) {
            /*SL:406*/((CtClassType)v1).setClassPool(this);
        }
        /*SL:408*/v1.setName(a2);
        /*SL:410*/return v1;
    }
    
    synchronized void classNameChanged(final String a1, final CtClass a2) {
        final CtClass v1 = /*EL:419*/this.getCached(a1);
        /*SL:420*/if (v1 == a2) {
            /*SL:421*/this.removeCached(a1);
        }
        final String v2 = /*EL:423*/a2.getName();
        /*SL:424*/this.checkNotFrozen(v2);
        /*SL:425*/this.cacheCtClass(v2, a2, false);
    }
    
    public CtClass get(final String v2) throws NotFoundException {
        final CtClass v3;
        /*SL:446*/if (v2 == null) {
            final CtClass a1 = /*EL:447*/null;
        }
        else {
            /*SL:449*/v3 = this.get0(v2, true);
        }
        /*SL:451*/if (v3 == null) {
            /*SL:452*/throw new NotFoundException(v2);
        }
        /*SL:454*/v3.incGetCounter();
        /*SL:455*/return v3;
    }
    
    public CtClass getOrNull(final String a1) {
        CtClass v1 = /*EL:474*/null;
        /*SL:475*/if (a1 == null) {
            /*SL:476*/v1 = null;
        }
        else {
            try {
                /*SL:483*/v1 = this.get0(a1, true);
            }
            catch (NotFoundException ex) {}
        }
        /*SL:487*/if (v1 != null) {
            /*SL:488*/v1.incGetCounter();
        }
        /*SL:490*/return v1;
    }
    
    public CtClass getCtClass(final String a1) throws NotFoundException {
        /*SL:514*/if (a1.charAt(0) == '[') {
            /*SL:515*/return Descriptor.toCtClass(a1, this);
        }
        /*SL:517*/return this.get(a1);
    }
    
    protected synchronized CtClass get0(final String a1, final boolean a2) throws NotFoundException {
        CtClass v1 = /*EL:527*/null;
        /*SL:528*/if (a2) {
            /*SL:529*/v1 = this.getCached(a1);
            /*SL:530*/if (v1 != null) {
                /*SL:531*/return v1;
            }
        }
        /*SL:534*/if (!this.childFirstLookup && this.parent != null) {
            /*SL:535*/v1 = this.parent.get0(a1, a2);
            /*SL:536*/if (v1 != null) {
                /*SL:537*/return v1;
            }
        }
        /*SL:540*/v1 = this.createCtClass(a1, a2);
        /*SL:541*/if (v1 != null) {
            /*SL:543*/if (a2) {
                /*SL:544*/this.cacheCtClass(v1.getName(), v1, false);
            }
            /*SL:546*/return v1;
        }
        /*SL:549*/if (this.childFirstLookup && this.parent != null) {
            /*SL:550*/v1 = this.parent.get0(a1, a2);
        }
        /*SL:552*/return v1;
    }
    
    protected CtClass createCtClass(String v1, final boolean v2) {
        /*SL:564*/if (v1.charAt(0) == '[') {
            /*SL:565*/v1 = Descriptor.toClassName(v1);
        }
        /*SL:567*/if (v1.endsWith("[]")) {
            final String a1 = /*EL:568*/v1.substring(0, v1.indexOf(91));
            /*SL:569*/if ((!v2 || this.getCached(a1) == null) && this.find(a1) == null) {
                /*SL:570*/return null;
            }
            /*SL:572*/return new CtArray(v1, this);
        }
        else {
            /*SL:575*/if (this.find(v1) == null) {
                /*SL:576*/return null;
            }
            /*SL:578*/return new CtClassType(v1, this);
        }
    }
    
    public URL find(final String a1) {
        /*SL:591*/return this.source.find(a1);
    }
    
    void checkNotFrozen(final String a1) throws RuntimeException {
        CtClass v1 = /*EL:603*/this.getCached(a1);
        /*SL:604*/if (v1 == null) {
            /*SL:605*/if (!this.childFirstLookup && this.parent != null) {
                try {
                    /*SL:607*/v1 = this.parent.get0(a1, true);
                }
                catch (NotFoundException ex) {}
                /*SL:610*/if (v1 != null) {
                    /*SL:611*/throw new RuntimeException(a1 + " is in a parent ClassPool.  Use the parent.");
                }
            }
        }
        else/*SL:616*/ if (v1.isFrozen()) {
            /*SL:617*/throw new RuntimeException(a1 + ": frozen class (cannot edit)");
        }
    }
    
    CtClass checkNotExists(final String a1) {
        CtClass v1 = /*EL:628*/this.getCached(a1);
        /*SL:630*/if (v1 == null && !this.childFirstLookup && this.parent != null) {
            try {
                /*SL:632*/v1 = this.parent.get0(a1, true);
            }
            catch (NotFoundException ex) {}
        }
        /*SL:637*/return v1;
    }
    
    InputStream openClassfile(final String a1) throws NotFoundException {
        /*SL:643*/return this.source.openClassfile(a1);
    }
    
    void writeClassfile(final String a1, final OutputStream a2) throws NotFoundException, IOException, CannotCompileException {
        /*SL:649*/this.source.writeClassfile(a1, a2);
    }
    
    public CtClass[] get(final String[] v2) throws NotFoundException {
        /*SL:664*/if (v2 == null) {
            /*SL:665*/return new CtClass[0];
        }
        final int v3 = /*EL:667*/v2.length;
        final CtClass[] v4 = /*EL:668*/new CtClass[v3];
        /*SL:669*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:670*/v4[a1] = this.get(v2[a1]);
        }
        /*SL:672*/return v4;
    }
    
    public CtMethod getMethod(final String a1, final String a2) throws NotFoundException {
        final CtClass v1 = /*EL:685*/this.get(a1);
        /*SL:686*/return v1.getDeclaredMethod(a2);
    }
    
    public CtClass makeClass(final InputStream a1) throws IOException, RuntimeException {
        /*SL:707*/return this.makeClass(a1, true);
    }
    
    public CtClass makeClass(InputStream a1, final boolean a2) throws IOException, RuntimeException {
        /*SL:727*/this.compress();
        /*SL:728*/a1 = new BufferedInputStream(a1);
        final CtClass v1 = /*EL:729*/new CtClassType(a1, this);
        /*SL:730*/v1.checkModify();
        final String v2 = /*EL:731*/v1.getName();
        /*SL:732*/if (a2) {
            /*SL:733*/this.checkNotFrozen(v2);
        }
        /*SL:735*/this.cacheCtClass(v2, v1, true);
        /*SL:736*/return v1;
    }
    
    public CtClass makeClass(final ClassFile a1) throws RuntimeException {
        /*SL:756*/return this.makeClass(a1, true);
    }
    
    public CtClass makeClass(final ClassFile a1, final boolean a2) throws RuntimeException {
        /*SL:776*/this.compress();
        final CtClass v1 = /*EL:777*/new CtClassType(a1, this);
        /*SL:778*/v1.checkModify();
        final String v2 = /*EL:779*/v1.getName();
        /*SL:780*/if (a2) {
            /*SL:781*/this.checkNotFrozen(v2);
        }
        /*SL:783*/this.cacheCtClass(v2, v1, true);
        /*SL:784*/return v1;
    }
    
    public CtClass makeClassIfNew(InputStream a1) throws IOException, RuntimeException {
        /*SL:805*/this.compress();
        /*SL:806*/a1 = new BufferedInputStream(a1);
        final CtClass v1 = /*EL:807*/new CtClassType(a1, this);
        /*SL:808*/v1.checkModify();
        final String v2 = /*EL:809*/v1.getName();
        final CtClass v3 = /*EL:810*/this.checkNotExists(v2);
        /*SL:811*/if (v3 != null) {
            /*SL:812*/return v3;
        }
        /*SL:814*/this.cacheCtClass(v2, v1, true);
        /*SL:815*/return v1;
    }
    
    public CtClass makeClass(final String a1) throws RuntimeException {
        /*SL:836*/return this.makeClass(a1, null);
    }
    
    public synchronized CtClass makeClass(final String a1, final CtClass a2) throws RuntimeException {
        /*SL:859*/this.checkNotFrozen(a1);
        final CtClass v1 = /*EL:860*/new CtNewClass(a1, this, false, a2);
        /*SL:861*/this.cacheCtClass(a1, v1, true);
        /*SL:862*/return v1;
    }
    
    synchronized CtClass makeNestedClass(final String a1) {
        /*SL:873*/this.checkNotFrozen(a1);
        final CtClass v1 = /*EL:874*/new CtNewNestedClass(a1, this, false, null);
        /*SL:875*/this.cacheCtClass(a1, v1, true);
        /*SL:876*/return v1;
    }
    
    public CtClass makeInterface(final String a1) throws RuntimeException {
        /*SL:888*/return this.makeInterface(a1, null);
    }
    
    public synchronized CtClass makeInterface(final String a1, final CtClass a2) throws RuntimeException {
        /*SL:903*/this.checkNotFrozen(a1);
        final CtClass v1 = /*EL:904*/new CtNewClass(a1, this, true, a2);
        /*SL:905*/this.cacheCtClass(a1, v1, true);
        /*SL:906*/return v1;
    }
    
    public CtClass makeAnnotation(final String v0) throws RuntimeException {
        try {
            final CtClass a1 = /*EL:921*/this.makeInterface(v0, this.get("java.lang.annotation.Annotation"));
            /*SL:922*/a1.setModifiers(a1.getModifiers() | 0x2000);
            /*SL:923*/return a1;
        }
        catch (NotFoundException v) {
            /*SL:927*/throw new RuntimeException(v.getMessage(), v);
        }
    }
    
    public ClassPath appendSystemPath() {
        /*SL:942*/return this.source.appendSystemPath();
    }
    
    public ClassPath insertClassPath(final ClassPath a1) {
        /*SL:955*/return this.source.insertClassPath(a1);
    }
    
    public ClassPath appendClassPath(final ClassPath a1) {
        /*SL:968*/return this.source.appendClassPath(a1);
    }
    
    public ClassPath insertClassPath(final String a1) throws NotFoundException {
        /*SL:986*/return this.source.insertClassPath(a1);
    }
    
    public ClassPath appendClassPath(final String a1) throws NotFoundException {
        /*SL:1004*/return this.source.appendClassPath(a1);
    }
    
    public void removeClassPath(final ClassPath a1) {
        /*SL:1013*/this.source.removeClassPath(a1);
    }
    
    public void appendPathList(final String v2) throws NotFoundException {
        final char v3 = File.pathSeparatorChar;
        int v4 = /*EL:1030*/0;
        while (true) {
            final int a1 = /*EL:1032*/v2.indexOf(v3, v4);
            /*SL:1033*/if (a1 < 0) {
                break;
            }
            /*SL:1038*/this.appendClassPath(v2.substring(v4, a1));
            /*SL:1039*/v4 = a1 + 1;
        }
        this.appendClassPath(v2.substring(v4));
    }
    
    public Class toClass(final CtClass a1) throws CannotCompileException {
        /*SL:1071*/return this.toClass(a1, this.getClassLoader());
    }
    
    public ClassLoader getClassLoader() {
        /*SL:1085*/return getContextClassLoader();
    }
    
    static ClassLoader getContextClassLoader() {
        /*SL:1093*/return Thread.currentThread().getContextClassLoader();
    }
    
    public Class toClass(final CtClass a1, final ClassLoader a2) throws CannotCompileException {
        /*SL:1113*/return this.toClass(a1, a2, null);
    }
    
    public Class toClass(final CtClass v-3, final ClassLoader v-2, final ProtectionDomain v-1) throws CannotCompileException {
        try {
            byte[] a3 = /*EL:1150*/v-3.toBytecode();
            final Method v1;
            final Object[] v2;
            /*SL:1153*/if (v-1 == null) {
                final Method a2 = ClassPool.defineClass1;
                /*SL:1155*/a3 = new Object[] { v-3.getName(), a3, new Integer(0), new Integer(a3.length) };
            }
            else {
                /*SL:1159*/v1 = ClassPool.defineClass2;
                /*SL:1160*/v2 = new Object[] { v-3.getName(), a3, new Integer(0), new Integer(a3.length), v-1 };
            }
            /*SL:1164*/return (Class)toClass2(v1, v-2, v2);
        }
        catch (RuntimeException v3) {
            /*SL:1167*/throw v3;
        }
        catch (InvocationTargetException v4) {
            /*SL:1170*/throw new CannotCompileException(v4.getTargetException());
        }
        catch (Exception v5) {
            /*SL:1173*/throw new CannotCompileException(v5);
        }
    }
    
    private static synchronized Object toClass2(final Method a1, final ClassLoader a2, final Object[] a3) throws Exception {
        /*SL:1181*/a1.setAccessible(true);
        try {
            /*SL:1186*/return a1.invoke(a2, a3);
        }
        finally {
            a1.setAccessible(false);
        }
    }
    
    public void makePackage(final ClassLoader v-3, final String v-2) throws CannotCompileException {
        final Object[] a3 = /*EL:1210*/{ v-2, null, null, null, null, null, null, null };
        Throwable v0 = null;
        try {
            toClass2(ClassPool.definePackage, /*EL:1214*/v-3, a3);
            /*SL:1215*/return;
        }
        catch (InvocationTargetException a2) {
            /*SL:1218*/a2 = a2.getTargetException();
            /*SL:1219*/if (a2 == null) {
                /*SL:1220*/a2 = a2;
            }
            else/*SL:1221*/ if (a2 instanceof IllegalArgumentException) {
                /*SL:1224*/return;
            }
        }
        catch (Exception v) {
            /*SL:1228*/v0 = v;
        }
        /*SL:1231*/throw new CannotCompileException(v0);
    }
    
    static {
        try {
            AccessController.<Object>doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction() {
                @Override
                public Object run() throws Exception {
                    final Class v1 = /*EL:81*/Class.forName("java.lang.ClassLoader");
                    /*SL:82*/ClassPool.defineClass1 = v1.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                    /*SL:86*/ClassPool.defineClass2 = v1.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                    /*SL:90*/ClassPool.definePackage = v1.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class);
                    /*SL:94*/return null;
                }
            });
        }
        catch (PrivilegedActionException v1) {
            throw new RuntimeException("cannot initialize ClassPool", v1.getException());
        }
        ClassPool.doPruning = false;
        ClassPool.releaseUnmodifiedClassFile = true;
        ClassPool.defaultPool = null;
    }
}
