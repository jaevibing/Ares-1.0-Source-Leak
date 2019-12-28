package javassist.util.proxy;

import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.StackMapTable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import javassist.bytecode.Bytecode;
import javassist.bytecode.MethodInfo;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.lang.reflect.Modifier;
import javassist.bytecode.ConstPool;
import javassist.bytecode.DuplicateMemberException;
import java.util.ArrayList;
import javassist.bytecode.FieldInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import javassist.bytecode.ClassFile;
import javassist.CannotCompileException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Comparator;
import java.util.WeakHashMap;
import java.util.List;

public class ProxyFactory
{
    private Class superClass;
    private Class[] interfaces;
    private MethodFilter methodFilter;
    private MethodHandler handler;
    private List signatureMethods;
    private boolean hasGetHandler;
    private byte[] signature;
    private String classname;
    private String basename;
    private String superName;
    private Class thisClass;
    private boolean factoryUseCache;
    private boolean factoryWriteReplace;
    public String writeDirectory;
    private static final Class OBJECT_TYPE;
    private static final String HOLDER = "_methods_";
    private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
    private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
    private static final String FILTER_SIGNATURE_TYPE = "[B";
    private static final String HANDLER = "handler";
    private static final String NULL_INTERCEPTOR_HOLDER = "javassist.util.proxy.RuntimeSupport";
    private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
    private static final String HANDLER_TYPE;
    private static final String HANDLER_SETTER = "setHandler";
    private static final String HANDLER_SETTER_TYPE;
    private static final String HANDLER_GETTER = "getHandler";
    private static final String HANDLER_GETTER_TYPE;
    private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
    private static final String SERIAL_VERSION_UID_TYPE = "J";
    private static final long SERIAL_VERSION_UID_VALUE = -1L;
    public static volatile boolean useCache;
    public static volatile boolean useWriteReplace;
    private static WeakHashMap proxyCache;
    private static char[] hexDigits;
    public static ClassLoaderProvider classLoaderProvider;
    public static UniqueName nameGenerator;
    private static Comparator sorter;
    private static final String HANDLER_GETTER_KEY = "getHandler:()";
    
    public boolean isUseCache() {
        /*SL:254*/return this.factoryUseCache;
    }
    
    public void setUseCache(final boolean a1) {
        /*SL:266*/if (this.handler != null && a1) {
            /*SL:267*/throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
        }
        /*SL:269*/this.factoryUseCache = a1;
    }
    
    public boolean isUseWriteReplace() {
        /*SL:278*/return this.factoryWriteReplace;
    }
    
    public void setUseWriteReplace(final boolean a1) {
        /*SL:288*/this.factoryWriteReplace = a1;
    }
    
    public static boolean isProxyClass(final Class a1) {
        /*SL:301*/return Proxy.class.isAssignableFrom(a1);
    }
    
    public ProxyFactory() {
        this.superClass = null;
        this.interfaces = null;
        this.methodFilter = null;
        this.handler = null;
        this.signature = null;
        this.signatureMethods = null;
        this.hasGetHandler = false;
        this.thisClass = null;
        this.writeDirectory = null;
        this.factoryUseCache = ProxyFactory.useCache;
        this.factoryWriteReplace = ProxyFactory.useWriteReplace;
    }
    
    public void setSuperclass(final Class a1) {
        /*SL:356*/this.superClass = a1;
        /*SL:358*/this.signature = null;
    }
    
    public Class getSuperclass() {
        /*SL:366*/return this.superClass;
    }
    
    public void setInterfaces(final Class[] a1) {
        /*SL:372*/this.interfaces = a1;
        /*SL:374*/this.signature = null;
    }
    
    public Class[] getInterfaces() {
        /*SL:382*/return this.interfaces;
    }
    
    public void setFilter(final MethodFilter a1) {
        /*SL:388*/this.methodFilter = a1;
        /*SL:390*/this.signature = null;
    }
    
    public Class createClass() {
        /*SL:397*/if (this.signature == null) {
            /*SL:398*/this.computeSignature(this.methodFilter);
        }
        /*SL:400*/return this.createClass1();
    }
    
    public Class createClass(final MethodFilter a1) {
        /*SL:407*/this.computeSignature(a1);
        /*SL:408*/return this.createClass1();
    }
    
    Class createClass(final byte[] a1) {
        /*SL:419*/this.installSignature(a1);
        /*SL:420*/return this.createClass1();
    }
    
    private Class createClass1() {
        Class v0 = /*EL:424*/this.thisClass;
        /*SL:425*/if (v0 == null) {
            final ClassLoader v = /*EL:426*/this.getClassLoader();
            /*SL:427*/synchronized (ProxyFactory.proxyCache) {
                /*SL:428*/if (this.factoryUseCache) {
                    /*SL:429*/this.createClass2(v);
                }
                else {
                    /*SL:431*/this.createClass3(v);
                }
                /*SL:433*/v0 = this.thisClass;
                /*SL:435*/this.thisClass = null;
            }
        }
        /*SL:439*/return v0;
    }
    
    public String getKey(final Class v-4, final Class[] v-3, final byte[] v-2, final boolean v-1) {
        final StringBuffer v0 = /*EL:448*/new StringBuffer();
        /*SL:449*/if (v-4 != null) {
            /*SL:450*/v0.append(v-4.getName());
        }
        /*SL:452*/v0.append(":");
        /*SL:453*/for (int a1 = 0; a1 < v-3.length; ++a1) {
            /*SL:454*/v0.append(v-3[a1].getName());
            /*SL:455*/v0.append(":");
        }
        /*SL:457*/for (int v = 0; v < v-2.length; ++v) {
            final byte a2 = /*EL:458*/v-2[v];
            final int a3 = /*EL:459*/a2 & 0xF;
            final int a4 = /*EL:460*/a2 >> 4 & 0xF;
            /*SL:461*/v0.append(ProxyFactory.hexDigits[a3]);
            /*SL:462*/v0.append(ProxyFactory.hexDigits[a4]);
        }
        /*SL:464*/if (v-1) {
            /*SL:465*/v0.append(":w");
        }
        /*SL:468*/return v0.toString();
    }
    
    private void createClass2(final ClassLoader v2) {
        final String v3 = /*EL:472*/this.getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
        HashMap v4 = ProxyFactory.proxyCache.get(/*EL:479*/v2);
        /*SL:481*/if (v4 == null) {
            /*SL:482*/v4 = new HashMap();
            ProxyFactory.proxyCache.put(/*EL:483*/v2, v4);
        }
        ProxyDetails v5 = /*EL:485*/v4.get(v3);
        /*SL:486*/if (v5 != null) {
            final WeakReference a1 = /*EL:487*/v5.proxyClass;
            /*SL:488*/this.thisClass = (Class)a1.get();
            /*SL:489*/if (this.thisClass != null) {
                /*SL:490*/return;
            }
        }
        /*SL:493*/this.createClass3(v2);
        /*SL:494*/v5 = new ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace);
        /*SL:495*/v4.put(v3, v5);
    }
    
    private void createClass3(final ClassLoader v0) {
        /*SL:501*/this.allocateClassName();
        try {
            final ClassFile a1 = /*EL:504*/this.make();
            /*SL:505*/if (this.writeDirectory != null) {
                /*SL:506*/FactoryHelper.writeFile(a1, this.writeDirectory);
            }
            /*SL:508*/this.thisClass = FactoryHelper.toClass(a1, v0, this.getDomain());
            /*SL:509*/this.setField("_filter_signature", this.signature);
            /*SL:511*/if (!this.factoryUseCache) {
                /*SL:512*/this.setField("default_interceptor", this.handler);
            }
        }
        catch (CannotCompileException v) {
            /*SL:516*/throw new RuntimeException(v.getMessage(), v);
        }
    }
    
    private void setField(final String v2, final Object v3) {
        /*SL:522*/if (this.thisClass != null && v3 != null) {
            try {
                final Field a1 = /*EL:524*/this.thisClass.getField(v2);
                /*SL:525*/SecurityActions.setAccessible(a1, true);
                /*SL:526*/a1.set(null, v3);
                /*SL:527*/SecurityActions.setAccessible(a1, false);
            }
            catch (Exception a2) {
                /*SL:530*/throw new RuntimeException(a2);
            }
        }
    }
    
    static byte[] getFilterSignature(final Class a1) {
        /*SL:535*/return (byte[])getField(a1, "_filter_signature");
    }
    
    private static Object getField(final Class v-1, final String v0) {
        try {
            final Field a1 = /*EL:540*/v-1.getField(v0);
            /*SL:541*/a1.setAccessible(true);
            final Object a2 = /*EL:542*/a1.get(null);
            /*SL:543*/a1.setAccessible(false);
            /*SL:544*/return a2;
        }
        catch (Exception v) {
            /*SL:547*/throw new RuntimeException(v);
        }
    }
    
    public static MethodHandler getHandler(final Proxy v-1) {
        try {
            final Field a1 = /*EL:560*/v-1.getClass().getDeclaredField("handler");
            /*SL:561*/a1.setAccessible(true);
            final Object v1 = /*EL:562*/a1.get(v-1);
            /*SL:563*/a1.setAccessible(false);
            /*SL:564*/return (MethodHandler)v1;
        }
        catch (Exception v2) {
            /*SL:567*/throw new RuntimeException(v2);
        }
    }
    
    protected ClassLoader getClassLoader() {
        /*SL:614*/return ProxyFactory.classLoaderProvider.get(this);
    }
    
    protected ClassLoader getClassLoader0() {
        ClassLoader v1 = /*EL:618*/null;
        /*SL:619*/if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
            /*SL:620*/v1 = this.superClass.getClassLoader();
        }
        else/*SL:621*/ if (this.interfaces != null && this.interfaces.length > 0) {
            /*SL:622*/v1 = this.interfaces[0].getClassLoader();
        }
        /*SL:624*/if (v1 == null) {
            /*SL:625*/v1 = this.getClass().getClassLoader();
            /*SL:627*/if (v1 == null) {
                /*SL:628*/v1 = Thread.currentThread().getContextClassLoader();
                /*SL:629*/if (v1 == null) {
                    /*SL:630*/v1 = ClassLoader.getSystemClassLoader();
                }
            }
        }
        /*SL:634*/return v1;
    }
    
    protected ProtectionDomain getDomain() {
        Class v1;
        /*SL:639*/if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
            /*SL:640*/v1 = this.superClass;
        }
        else/*SL:641*/ if (this.interfaces != null && this.interfaces.length > 0) {
            /*SL:642*/v1 = this.interfaces[0];
        }
        else {
            /*SL:644*/v1 = this.getClass();
        }
        /*SL:646*/return v1.getProtectionDomain();
    }
    
    public Object create(final Class[] a1, final Object[] a2, final MethodHandler a3) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Object v1 = /*EL:661*/this.create(a1, a2);
        /*SL:662*/((Proxy)v1).setHandler(a3);
        /*SL:663*/return v1;
    }
    
    public Object create(final Class[] a1, final Object[] a2) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Class v1 = /*EL:676*/this.createClass();
        final Constructor v2 = /*EL:677*/v1.getConstructor((Class[])a1);
        /*SL:678*/return v2.newInstance(a2);
    }
    
    public void setHandler(final MethodHandler a1) {
        /*SL:693*/if (this.factoryUseCache && a1 != null) {
            /*SL:694*/this.factoryUseCache = false;
            /*SL:696*/this.thisClass = null;
        }
        /*SL:701*/this.setField("default_interceptor", this.handler = a1);
    }
    
    private static String makeProxyName(final String a1) {
        /*SL:733*/synchronized (ProxyFactory.nameGenerator) {
            /*SL:734*/return ProxyFactory.nameGenerator.get(a1);
        }
    }
    
    private ClassFile make() throws CannotCompileException {
        final ClassFile classFile = /*EL:739*/new ClassFile(false, this.classname, this.superName);
        /*SL:740*/classFile.setAccessFlags(1);
        setInterfaces(/*EL:741*/classFile, this.interfaces, (Class)(this.hasGetHandler ? Proxy.class : ProxyObject.class));
        final ConstPool v0 = /*EL:742*/classFile.getConstPool();
        /*SL:745*/if (!this.factoryUseCache) {
            final FieldInfo v = /*EL:746*/new FieldInfo(v0, "default_interceptor", ProxyFactory.HANDLER_TYPE);
            /*SL:747*/v.setAccessFlags(9);
            /*SL:748*/classFile.addField(v);
        }
        final FieldInfo v = /*EL:752*/new FieldInfo(v0, "handler", ProxyFactory.HANDLER_TYPE);
        /*SL:753*/v.setAccessFlags(2);
        /*SL:754*/classFile.addField(v);
        final FieldInfo v2 = /*EL:757*/new FieldInfo(v0, "_filter_signature", "[B");
        /*SL:758*/v2.setAccessFlags(9);
        /*SL:759*/classFile.addField(v2);
        final FieldInfo v3 = /*EL:762*/new FieldInfo(v0, "serialVersionUID", "J");
        /*SL:763*/v3.setAccessFlags(25);
        /*SL:764*/classFile.addField(v3);
        /*SL:768*/this.makeConstructors(this.classname, classFile, v0, this.classname);
        final ArrayList v4 = /*EL:770*/new ArrayList();
        final int v5 = /*EL:771*/this.overrideMethods(classFile, v0, this.classname, v4);
        addClassInitializer(/*EL:772*/classFile, v0, this.classname, v5, v4);
        addSetter(/*EL:773*/this.classname, classFile, v0);
        /*SL:774*/if (!this.hasGetHandler) {
            addGetter(/*EL:775*/this.classname, classFile, v0);
        }
        /*SL:777*/if (this.factoryWriteReplace) {
            try {
                /*SL:779*/classFile.addMethod(makeWriteReplace(v0));
            }
            catch (DuplicateMemberException ex) {}
        }
        /*SL:786*/this.thisClass = null;
        /*SL:787*/return classFile;
    }
    
    private void checkClassAndSuperName() {
        /*SL:791*/if (this.interfaces == null) {
            /*SL:792*/this.interfaces = new Class[0];
        }
        /*SL:794*/if (this.superClass == null) {
            /*SL:795*/this.superClass = ProxyFactory.OBJECT_TYPE;
            /*SL:796*/this.superName = this.superClass.getName();
            /*SL:798*/this.basename = ((this.interfaces.length == 0) ? this.superName : this.interfaces[0].getName());
        }
        else {
            /*SL:800*/this.superName = this.superClass.getName();
            /*SL:801*/this.basename = this.superName;
        }
        /*SL:804*/if (Modifier.isFinal(this.superClass.getModifiers())) {
            /*SL:805*/throw new RuntimeException(this.superName + " is final");
        }
        /*SL:807*/if (this.basename.startsWith("java.")) {
            /*SL:808*/this.basename = "org.javassist.tmp." + this.basename;
        }
    }
    
    private void allocateClassName() {
        /*SL:812*/this.classname = makeProxyName(this.basename);
    }
    
    private void makeSortedMethodList() {
        /*SL:827*/this.checkClassAndSuperName();
        /*SL:829*/this.hasGetHandler = false;
        final HashMap v1 = /*EL:830*/this.getMethods(this.superClass, this.interfaces);
        /*SL:832*/Collections.<Object>sort((List<Object>)(this.signatureMethods = new ArrayList(v1.entrySet())), ProxyFactory.sorter);
    }
    
    private void computeSignature(final MethodFilter v-4) {
        /*SL:837*/this.makeSortedMethodList();
        final int size = /*EL:839*/this.signatureMethods.size();
        final int n = /*EL:840*/size + 7 >> 3;
        /*SL:841*/this.signature = new byte[n];
        /*SL:842*/for (int i = 0; i < size; ++i) {
            final Map.Entry a1 = /*EL:844*/this.signatureMethods.get(i);
            final Method v1 = /*EL:845*/a1.getValue();
            final int v2 = /*EL:846*/v1.getModifiers();
            /*SL:848*/if (!Modifier.isFinal(v2) && !Modifier.isStatic(v2) && isVisible(v2, this.basename, v1) && (v-4 == null || v-4.isHandled(v1))) {
                /*SL:849*/this.setBit(this.signature, i);
            }
        }
    }
    
    private void installSignature(final byte[] a1) {
        /*SL:856*/this.makeSortedMethodList();
        final int v1 = /*EL:858*/this.signatureMethods.size();
        final int v2 = /*EL:859*/v1 + 7 >> 3;
        /*SL:860*/if (a1.length != v2) {
            /*SL:861*/throw new RuntimeException("invalid filter signature length for deserialized proxy class");
        }
        /*SL:864*/this.signature = a1;
    }
    
    private boolean testBit(final byte[] v-4, final int v-3) {
        final int n = /*EL:868*/v-3 >> 3;
        /*SL:869*/if (n > v-4.length) {
            /*SL:870*/return false;
        }
        final int a1 = /*EL:872*/v-3 & 0x7;
        final int a2 = /*EL:873*/1 << a1;
        final int v1 = /*EL:874*/v-4[n];
        /*SL:875*/return (v1 & a2) != 0x0;
    }
    
    private void setBit(final byte[] v-4, final int v-3) {
        final int n = /*EL:880*/v-3 >> 3;
        /*SL:881*/if (n < v-4.length) {
            final int a1 = /*EL:882*/v-3 & 0x7;
            final int a2 = /*EL:883*/1 << a1;
            final int v1 = /*EL:884*/v-4[n];
            /*SL:885*/v-4[n] = (byte)(v1 | a2);
        }
    }
    
    private static void setInterfaces(final ClassFile a3, final Class[] v1, final Class v2) {
        final String v3 = /*EL:890*/v2.getName();
        final String[] v4;
        /*SL:892*/if (v1 == null || v1.length == 0) {
            final String[] a4 = /*EL:893*/{ v3 };
        }
        else {
            /*SL:895*/v4 = new String[v1.length + 1];
            /*SL:896*/for (int a5 = 0; a5 < v1.length; ++a5) {
                /*SL:897*/v4[a5] = v1[a5].getName();
            }
            /*SL:899*/v4[v1.length] = v3;
        }
        /*SL:902*/a3.setInterfaces(v4);
    }
    
    private static void addClassInitializer(final ClassFile a2, final ConstPool a3, final String a4, final int a5, final ArrayList v1) throws CannotCompileException {
        final FieldInfo v2 = /*EL:909*/new FieldInfo(a3, "_methods_", "[Ljava/lang/reflect/Method;");
        /*SL:910*/v2.setAccessFlags(10);
        /*SL:911*/a2.addField(v2);
        final MethodInfo v3 = /*EL:912*/new MethodInfo(a3, "<clinit>", "()V");
        /*SL:913*/v3.setAccessFlags(8);
        setThrows(/*EL:914*/v3, a3, new Class[] { ClassNotFoundException.class });
        final Bytecode v4 = /*EL:916*/new Bytecode(a3, 0, 2);
        /*SL:917*/v4.addIconst(a5 * 2);
        /*SL:918*/v4.addAnewarray("java.lang.reflect.Method");
        final int v5 = /*EL:919*/0;
        /*SL:920*/v4.addAstore(0);
        /*SL:924*/v4.addLdc(a4);
        /*SL:925*/v4.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        final int v6 = /*EL:927*/1;
        /*SL:928*/v4.addAstore(1);
        /*SL:930*/for (final Find2MethodsArgs a6 : v1) {
            callFind2Methods(/*EL:933*/v4, a6.methodName, a6.delegatorName, a6.origIndex, a6.descriptor, 1, 0);
        }
        /*SL:937*/v4.addAload(0);
        /*SL:938*/v4.addPutstatic(a4, "_methods_", "[Ljava/lang/reflect/Method;");
        /*SL:940*/v4.addLconst(-1L);
        /*SL:941*/v4.addPutstatic(a4, "serialVersionUID", "J");
        /*SL:942*/v4.addOpcode(177);
        /*SL:943*/v3.setCodeAttribute(v4.toCodeAttribute());
        /*SL:944*/a2.addMethod(v3);
    }
    
    private static void callFind2Methods(final Bytecode a1, final String a2, final String a3, final int a4, final String a5, final int a6, final int a7) {
        final String v1 = /*EL:952*/RuntimeSupport.class.getName();
        final String v2 = /*EL:953*/"(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V";
        /*SL:956*/a1.addAload(a6);
        /*SL:957*/a1.addLdc(a2);
        /*SL:958*/if (a3 == null) {
            /*SL:959*/a1.addOpcode(1);
        }
        else {
            /*SL:961*/a1.addLdc(a3);
        }
        /*SL:963*/a1.addIconst(a4);
        /*SL:964*/a1.addLdc(a5);
        /*SL:965*/a1.addAload(a7);
        /*SL:966*/a1.addInvokestatic(v1, "find2Methods", v2);
    }
    
    private static void addSetter(final String a1, final ClassFile a2, final ConstPool a3) throws CannotCompileException {
        final MethodInfo v1 = /*EL:972*/new MethodInfo(a3, "setHandler", ProxyFactory.HANDLER_SETTER_TYPE);
        /*SL:974*/v1.setAccessFlags(1);
        final Bytecode v2 = /*EL:975*/new Bytecode(a3, 2, 2);
        /*SL:976*/v2.addAload(0);
        /*SL:977*/v2.addAload(1);
        /*SL:978*/v2.addPutfield(a1, "handler", ProxyFactory.HANDLER_TYPE);
        /*SL:979*/v2.addOpcode(177);
        /*SL:980*/v1.setCodeAttribute(v2.toCodeAttribute());
        /*SL:981*/a2.addMethod(v1);
    }
    
    private static void addGetter(final String a1, final ClassFile a2, final ConstPool a3) throws CannotCompileException {
        final MethodInfo v1 = /*EL:987*/new MethodInfo(a3, "getHandler", ProxyFactory.HANDLER_GETTER_TYPE);
        /*SL:989*/v1.setAccessFlags(1);
        final Bytecode v2 = /*EL:990*/new Bytecode(a3, 1, 1);
        /*SL:991*/v2.addAload(0);
        /*SL:992*/v2.addGetfield(a1, "handler", ProxyFactory.HANDLER_TYPE);
        /*SL:993*/v2.addOpcode(176);
        /*SL:994*/v1.setCodeAttribute(v2.toCodeAttribute());
        /*SL:995*/a2.addMethod(v1);
    }
    
    private int overrideMethods(final ClassFile v1, final ConstPool v2, final String v3, final ArrayList v4) throws CannotCompileException {
        final String v5 = makeUniqueName(/*EL:1001*/"_d", this.signatureMethods);
        final Iterator v6 = /*EL:1002*/this.signatureMethods.iterator();
        int v7 = /*EL:1003*/0;
        /*SL:1004*/while (v6.hasNext()) {
            final Map.Entry a1 = /*EL:1005*/v6.next();
            final String a2 = /*EL:1006*/a1.getKey();
            final Method a3 = /*EL:1007*/a1.getValue();
            /*SL:1008*/if ((ClassFile.MAJOR_VERSION < 49 || !isBridge(a3)) && /*EL:1009*/this.testBit(this.signature, v7)) {
                /*SL:1010*/this.override(v3, a3, v5, v7, keyToDesc(a2, a3), /*EL:1011*/v1, v2, v4);
            }
            /*SL:1014*/++v7;
        }
        /*SL:1017*/return v7;
    }
    
    private static boolean isBridge(final Method a1) {
        /*SL:1021*/return a1.isBridge();
    }
    
    private void override(final String a3, final Method a4, final String a5, final int a6, final String a7, final ClassFile a8, final ConstPool v1, final ArrayList v2) throws CannotCompileException {
        final Class v3 = /*EL:1028*/a4.getDeclaringClass();
        String v4 = /*EL:1029*/a5 + a6 + a4.getName();
        /*SL:1030*/if (Modifier.isAbstract(a4.getModifiers())) {
            /*SL:1031*/v4 = null;
        }
        else {
            final MethodInfo a9 = /*EL:1033*/this.makeDelegator(a4, a7, v1, v3, v4);
            /*SL:1036*/a9.setAccessFlags(a9.getAccessFlags() & 0xFFFFFFBF);
            /*SL:1037*/a8.addMethod(a9);
        }
        final MethodInfo v5 = makeForwarder(/*EL:1040*/a3, a4, a7, v1, v3, v4, a6, v2);
        /*SL:1043*/a8.addMethod(v5);
    }
    
    private void makeConstructors(final String v2, final ClassFile v3, final ConstPool v4, final String v5) throws CannotCompileException {
        final Constructor[] v6 = /*EL:1049*/SecurityActions.getDeclaredConstructors(this.superClass);
        final boolean v7 = /*EL:1051*/!this.factoryUseCache;
        /*SL:1052*/for (MethodInfo a4 = (MethodInfo)0; a4 < v6.length; ++a4) {
            final Constructor a2 = /*EL:1053*/v6[a4];
            final int a3 = /*EL:1054*/a2.getModifiers();
            /*SL:1055*/if (!Modifier.isFinal(a3) && !Modifier.isPrivate(a3) && isVisible(a3, this.basename, a2)) {
                /*SL:1057*/a4 = makeConstructor(v2, a2, v4, this.superClass, v7);
                /*SL:1058*/v3.addMethod(a4);
            }
        }
    }
    
    private static String makeUniqueName(final String v1, final List v2) {
        /*SL:1064*/if (makeUniqueName0(v1, v2.iterator())) {
            /*SL:1065*/return v1;
        }
        /*SL:1067*/for (String a2 = (String)100; a2 < 999; ++a2) {
            /*SL:1068*/a2 = v1 + a2;
            /*SL:1069*/if (makeUniqueName0(a2, v2.iterator())) {
                /*SL:1070*/return a2;
            }
        }
        /*SL:1073*/throw new RuntimeException("cannot make a unique method name");
    }
    
    private static boolean makeUniqueName0(final String v1, final Iterator v2) {
        /*SL:1077*/while (v2.hasNext()) {
            final Map.Entry a1 = /*EL:1078*/v2.next();
            final String a2 = /*EL:1079*/a1.getKey();
            /*SL:1080*/if (a2.startsWith(v1)) {
                /*SL:1081*/return false;
            }
        }
        /*SL:1084*/return true;
    }
    
    private static boolean isVisible(final int a3, final String v1, final Member v2) {
        /*SL:1093*/if ((a3 & 0x2) != 0x0) {
            /*SL:1094*/return false;
        }
        /*SL:1095*/if ((a3 & 0x5) != 0x0) {
            /*SL:1096*/return true;
        }
        final String a4 = getPackageName(/*EL:1098*/v1);
        final String a5 = getPackageName(/*EL:1099*/v2.getDeclaringClass().getName());
        /*SL:1100*/if (a4 == null) {
            /*SL:1101*/return a5 == null;
        }
        /*SL:1103*/return a4.equals(a5);
    }
    
    private static String getPackageName(final String a1) {
        final int v1 = /*EL:1108*/a1.lastIndexOf(46);
        /*SL:1109*/if (v1 < 0) {
            /*SL:1110*/return null;
        }
        /*SL:1112*/return a1.substring(0, v1);
    }
    
    private HashMap getMethods(final Class v1, final Class[] v2) {
        final HashMap v3 = /*EL:1118*/new HashMap();
        final HashSet v4 = /*EL:1119*/new HashSet();
        /*SL:1120*/for (int a1 = 0; a1 < v2.length; ++a1) {
            /*SL:1121*/this.getMethods(v3, v2[a1], v4);
        }
        /*SL:1123*/this.getMethods(v3, v1, v4);
        /*SL:1124*/return v3;
    }
    
    private void getMethods(final HashMap v-8, final Class v-7, final Set v-6) {
        /*SL:1130*/if (!v-6.add(v-7)) {
            /*SL:1131*/return;
        }
        final Class[] interfaces = /*EL:1133*/v-7.getInterfaces();
        /*SL:1134*/for (int a1 = 0; a1 < interfaces.length; ++a1) {
            /*SL:1135*/this.getMethods(v-8, interfaces[a1], v-6);
        }
        final Class superclass = /*EL:1137*/v-7.getSuperclass();
        /*SL:1138*/if (superclass != null) {
            /*SL:1139*/this.getMethods(v-8, superclass, v-6);
        }
        final Method[] declaredMethods = /*EL:1146*/SecurityActions.getDeclaredMethods(v-7);
        /*SL:1147*/for (int i = 0; i < declaredMethods.length; ++i) {
            /*SL:1148*/if (!Modifier.isPrivate(declaredMethods[i].getModifiers())) {
                final Method a2 = /*EL:1149*/declaredMethods[i];
                final String a3 = /*EL:1150*/a2.getName() + ':' + RuntimeSupport.makeDescriptor(a2);
                /*SL:1151*/if (a3.startsWith("getHandler:()")) {
                    /*SL:1152*/this.hasGetHandler = true;
                }
                final Method v1 = /*EL:1156*/v-8.put(a3, a2);
                /*SL:1162*/if (null != v1 && isBridge(a2) && !Modifier.isPublic(v1.getDeclaringClass().getModifiers()) && !Modifier.isAbstract(v1.getModifiers()) && !isOverloaded(i, declaredMethods)) {
                    /*SL:1163*/v-8.put(a3, v1);
                }
                /*SL:1166*/if (null != v1 && Modifier.isPublic(v1.getModifiers()) && /*EL:1167*/!Modifier.isPublic(a2.getModifiers())) {
                    /*SL:1170*/v-8.put(a3, v1);
                }
            }
        }
    }
    
    private static boolean isOverloaded(final int a2, final Method[] v1) {
        final String v2 = /*EL:1176*/v1[a2].getName();
        /*SL:1177*/for (int a3 = 0; a3 < v1.length; ++a3) {
            /*SL:1178*/if (a3 != a2 && /*EL:1179*/v2.equals(v1[a3].getName())) {
                /*SL:1180*/return true;
            }
        }
        /*SL:1182*/return false;
    }
    
    private static String keyToDesc(final String a1, final Method a2) {
        /*SL:1189*/return a1.substring(a1.indexOf(58) + 1);
    }
    
    private static MethodInfo makeConstructor(final String a1, final Constructor a2, final ConstPool a3, final Class a4, final boolean a5) {
        final String v1 = /*EL:1194*/RuntimeSupport.makeDescriptor(a2.getParameterTypes(), Void.TYPE);
        final MethodInfo v2 = /*EL:1196*/new MethodInfo(a3, "<init>", v1);
        /*SL:1197*/v2.setAccessFlags(1);
        setThrows(/*EL:1198*/v2, a3, a2.getExceptionTypes());
        final Bytecode v3 = /*EL:1199*/new Bytecode(a3, 0, 0);
        /*SL:1204*/if (a5) {
            /*SL:1205*/v3.addAload(0);
            /*SL:1206*/v3.addGetstatic(a1, "default_interceptor", ProxyFactory.HANDLER_TYPE);
            /*SL:1207*/v3.addPutfield(a1, "handler", ProxyFactory.HANDLER_TYPE);
            /*SL:1208*/v3.addGetstatic(a1, "default_interceptor", ProxyFactory.HANDLER_TYPE);
            /*SL:1209*/v3.addOpcode(199);
            /*SL:1210*/v3.addIndex(10);
        }
        /*SL:1214*/v3.addAload(0);
        /*SL:1215*/v3.addGetstatic("javassist.util.proxy.RuntimeSupport", "default_interceptor", ProxyFactory.HANDLER_TYPE);
        /*SL:1216*/v3.addPutfield(a1, "handler", ProxyFactory.HANDLER_TYPE);
        final int v4 = /*EL:1217*/v3.currentPc();
        /*SL:1219*/v3.addAload(0);
        final int v5 = addLoadParameters(/*EL:1220*/v3, a2.getParameterTypes(), 1);
        /*SL:1221*/v3.addInvokespecial(a4.getName(), "<init>", v1);
        /*SL:1222*/v3.addOpcode(177);
        /*SL:1223*/v3.setMaxLocals(v5 + 1);
        final CodeAttribute v6 = /*EL:1224*/v3.toCodeAttribute();
        /*SL:1225*/v2.setCodeAttribute(v6);
        final StackMapTable.Writer v7 = /*EL:1227*/new StackMapTable.Writer(32);
        /*SL:1228*/v7.sameFrame(v4);
        /*SL:1229*/v6.setAttribute(v7.toStackMapTable(a3));
        /*SL:1230*/return v2;
    }
    
    private MethodInfo makeDelegator(final Method a1, final String a2, final ConstPool a3, final Class a4, final String a5) {
        final MethodInfo v1 = /*EL:1235*/new MethodInfo(a3, a5, a2);
        /*SL:1236*/v1.setAccessFlags(0x11 | (a1.getModifiers() & /*EL:1237*/0xFFFFFAD9));
        setThrows(/*EL:1242*/v1, a3, a1);
        final Bytecode v2 = /*EL:1243*/new Bytecode(a3, 0, 0);
        /*SL:1244*/v2.addAload(0);
        int v3 = addLoadParameters(/*EL:1245*/v2, a1.getParameterTypes(), 1);
        final Class v4 = /*EL:1246*/this.invokespecialTarget(a4);
        /*SL:1247*/v2.addInvokespecial(v4.isInterface(), a3.addClassInfo(v4.getName()), a1.getName(), /*EL:1248*/a2);
        addReturn(/*EL:1249*/v2, a1.getReturnType());
        /*SL:1250*/v2.setMaxLocals(++v3);
        /*SL:1251*/v1.setCodeAttribute(v2.toCodeAttribute());
        /*SL:1252*/return v1;
    }
    
    private Class invokespecialTarget(final Class v2) {
        /*SL:1261*/if (v2.isInterface()) {
            /*SL:1262*/for (final Class a1 : this.interfaces) {
                /*SL:1263*/if (v2.isAssignableFrom(a1)) {
                    /*SL:1264*/return a1;
                }
            }
        }
        /*SL:1266*/return this.superClass;
    }
    
    private static MethodInfo makeForwarder(final String a1, final Method a2, final String a3, final ConstPool a4, final Class a5, final String a6, final int a7, final ArrayList a8) {
        final MethodInfo v1 = /*EL:1276*/new MethodInfo(a4, a2.getName(), a3);
        /*SL:1277*/v1.setAccessFlags(0x10 | (a2.getModifiers() & /*EL:1278*/0xFFFFFADF));
        setThrows(/*EL:1281*/v1, a4, a2);
        final int v2 = /*EL:1282*/Descriptor.paramSize(a3);
        final Bytecode v3 = /*EL:1283*/new Bytecode(a4, 0, v2 + 2);
        final int v4 = /*EL:1296*/a7 * 2;
        final int v5 = /*EL:1297*/a7 * 2 + 1;
        final int v6 = /*EL:1298*/v2 + 1;
        /*SL:1299*/v3.addGetstatic(a1, "_methods_", "[Ljava/lang/reflect/Method;");
        /*SL:1300*/v3.addAstore(v6);
        /*SL:1302*/a8.add(new Find2MethodsArgs(a2.getName(), a6, a3, v4));
        /*SL:1304*/v3.addAload(0);
        /*SL:1305*/v3.addGetfield(a1, "handler", ProxyFactory.HANDLER_TYPE);
        /*SL:1306*/v3.addAload(0);
        /*SL:1308*/v3.addAload(v6);
        /*SL:1309*/v3.addIconst(v4);
        /*SL:1310*/v3.addOpcode(50);
        /*SL:1312*/v3.addAload(v6);
        /*SL:1313*/v3.addIconst(v5);
        /*SL:1314*/v3.addOpcode(50);
        makeParameterList(/*EL:1316*/v3, a2.getParameterTypes());
        /*SL:1317*/v3.addInvokeinterface(MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
        final Class v7 = /*EL:1320*/a2.getReturnType();
        addUnwrapper(/*EL:1321*/v3, v7);
        addReturn(/*EL:1322*/v3, v7);
        final CodeAttribute v8 = /*EL:1324*/v3.toCodeAttribute();
        /*SL:1325*/v1.setCodeAttribute(v8);
        /*SL:1326*/return v1;
    }
    
    private static void setThrows(final MethodInfo a1, final ConstPool a2, final Method a3) {
        final Class[] v1 = /*EL:1342*/a3.getExceptionTypes();
        setThrows(/*EL:1343*/a1, a2, v1);
    }
    
    private static void setThrows(final MethodInfo a2, final ConstPool a3, final Class[] v1) {
        /*SL:1348*/if (v1.length == 0) {
            /*SL:1349*/return;
        }
        final String[] v2 = /*EL:1351*/new String[v1.length];
        /*SL:1352*/for (int a4 = 0; a4 < v1.length; ++a4) {
            /*SL:1353*/v2[a4] = v1[a4].getName();
        }
        final ExceptionsAttribute v3 = /*EL:1355*/new ExceptionsAttribute(a3);
        /*SL:1356*/v3.setExceptions(v2);
        /*SL:1357*/a2.setExceptionsAttribute(v3);
    }
    
    private static int addLoadParameters(final Bytecode a2, final Class[] a3, final int v1) {
        int v2 = /*EL:1362*/0;
        /*SL:1364*/for (int v3 = a3.length, a4 = 0; a4 < v3; ++a4) {
            /*SL:1365*/v2 += addLoad(a2, v2 + v1, a3[a4]);
        }
        /*SL:1367*/return v2;
    }
    
    private static int addLoad(final Bytecode a1, final int a2, final Class a3) {
        /*SL:1371*/if (a3.isPrimitive()) {
            /*SL:1372*/if (a3 == Long.TYPE) {
                /*SL:1373*/a1.addLload(a2);
                /*SL:1374*/return 2;
            }
            /*SL:1376*/if (a3 == Float.TYPE) {
                /*SL:1377*/a1.addFload(a2);
            }
            else {
                /*SL:1378*/if (a3 == Double.TYPE) {
                    /*SL:1379*/a1.addDload(a2);
                    /*SL:1380*/return 2;
                }
                /*SL:1383*/a1.addIload(a2);
            }
        }
        else {
            /*SL:1386*/a1.addAload(a2);
        }
        /*SL:1388*/return 1;
    }
    
    private static int addReturn(final Bytecode a1, final Class a2) {
        /*SL:1392*/if (a2.isPrimitive()) {
            /*SL:1393*/if (a2 == Long.TYPE) {
                /*SL:1394*/a1.addOpcode(173);
                /*SL:1395*/return 2;
            }
            /*SL:1397*/if (a2 == Float.TYPE) {
                /*SL:1398*/a1.addOpcode(174);
            }
            else {
                /*SL:1399*/if (a2 == Double.TYPE) {
                    /*SL:1400*/a1.addOpcode(175);
                    /*SL:1401*/return 2;
                }
                /*SL:1403*/if (a2 == Void.TYPE) {
                    /*SL:1404*/a1.addOpcode(177);
                    /*SL:1405*/return 0;
                }
                /*SL:1408*/a1.addOpcode(172);
            }
        }
        else {
            /*SL:1411*/a1.addOpcode(176);
        }
        /*SL:1413*/return 1;
    }
    
    private static void makeParameterList(final Bytecode v1, final Class[] v2) {
        int v3 = /*EL:1417*/1;
        final int v4 = /*EL:1418*/v2.length;
        /*SL:1419*/v1.addIconst(v4);
        /*SL:1420*/v1.addAnewarray("java/lang/Object");
        /*SL:1421*/for (Class a2 = (Class)0; a2 < v4; ++a2) {
            /*SL:1422*/v1.addOpcode(89);
            /*SL:1423*/v1.addIconst(a2);
            /*SL:1424*/a2 = v2[a2];
            /*SL:1425*/if (a2.isPrimitive()) {
                /*SL:1426*/v3 = makeWrapper(v1, a2, v3);
            }
            else {
                /*SL:1428*/v1.addAload(v3);
                /*SL:1429*/++v3;
            }
            /*SL:1432*/v1.addOpcode(83);
        }
    }
    
    private static int makeWrapper(final Bytecode a1, final Class a2, final int a3) {
        final int v1 = /*EL:1437*/FactoryHelper.typeIndex(a2);
        final String v2 = /*EL:1438*/FactoryHelper.wrapperTypes[v1];
        /*SL:1439*/a1.addNew(v2);
        /*SL:1440*/a1.addOpcode(89);
        addLoad(/*EL:1441*/a1, a3, a2);
        /*SL:1442*/a1.addInvokespecial(v2, "<init>", FactoryHelper.wrapperDesc[v1]);
        /*SL:1444*/return a3 + FactoryHelper.dataSize[v1];
    }
    
    private static void addUnwrapper(final Bytecode v1, final Class v2) {
        /*SL:1448*/if (v2.isPrimitive()) {
            /*SL:1449*/if (v2 == Void.TYPE) {
                /*SL:1450*/v1.addOpcode(87);
            }
            else {
                final int a1 = /*EL:1452*/FactoryHelper.typeIndex(v2);
                final String a2 = /*EL:1453*/FactoryHelper.wrapperTypes[a1];
                /*SL:1454*/v1.addCheckcast(a2);
                /*SL:1455*/v1.addInvokevirtual(a2, FactoryHelper.unwarpMethods[a1], FactoryHelper.unwrapDesc[a1]);
            }
        }
        else {
            /*SL:1461*/v1.addCheckcast(v2.getName());
        }
    }
    
    private static MethodInfo makeWriteReplace(final ConstPool a1) {
        final MethodInfo v1 = /*EL:1465*/new MethodInfo(a1, "writeReplace", "()Ljava/lang/Object;");
        final String[] v2 = /*EL:1466*/{ /*EL:1467*/"java.io.ObjectStreamException" };
        final ExceptionsAttribute v3 = /*EL:1468*/new ExceptionsAttribute(a1);
        /*SL:1469*/v3.setExceptions(v2);
        /*SL:1470*/v1.setExceptionsAttribute(v3);
        final Bytecode v4 = /*EL:1471*/new Bytecode(a1, 0, 1);
        /*SL:1472*/v4.addAload(0);
        /*SL:1473*/v4.addInvokestatic("javassist.util.proxy.RuntimeSupport", "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
        /*SL:1476*/v4.addOpcode(176);
        /*SL:1477*/v1.setCodeAttribute(v4.toCodeAttribute());
        /*SL:1478*/return v1;
    }
    
    static {
        OBJECT_TYPE = Object.class;
        HANDLER_TYPE = 'L' + MethodHandler.class.getName().replace('.', '/') + ';';
        HANDLER_SETTER_TYPE = "(" + ProxyFactory.HANDLER_TYPE + ")V";
        HANDLER_GETTER_TYPE = "()" + ProxyFactory.HANDLER_TYPE;
        ProxyFactory.useCache = true;
        ProxyFactory.useWriteReplace = true;
        ProxyFactory.proxyCache = new WeakHashMap();
        ProxyFactory.hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        ProxyFactory.classLoaderProvider = new ClassLoaderProvider() {
            @Override
            public ClassLoader get(final ProxyFactory a1) {
                /*SL:609*/return a1.getClassLoader0();
            }
        };
        ProxyFactory.nameGenerator = new UniqueName() {
            private final String sep = "_$$_jvst" + Integer.toHexString(this.hashCode() & 0xFFF) + "_";
            private int counter = 0;
            
            @Override
            public String get(final String a1) {
                /*SL:728*/return a1 + this.sep + Integer.toHexString(this.counter++);
            }
        };
        ProxyFactory.sorter = new Comparator() {
            @Override
            public int compare(final Object a1, final Object a2) {
                final Map.Entry v1 = /*EL:818*/(Map.Entry)a1;
                final Map.Entry v2 = /*EL:819*/(Map.Entry)a2;
                final String v3 = /*EL:820*/v1.getKey();
                final String v4 = /*EL:821*/v2.getKey();
                /*SL:822*/return v3.compareTo(v4);
            }
        };
    }
    
    static class ProxyDetails
    {
        byte[] signature;
        WeakReference proxyClass;
        boolean isUseWriteReplace;
        
        ProxyDetails(final byte[] a1, final Class a2, final boolean a3) {
            this.signature = a1;
            this.proxyClass = new WeakReference((T)a2);
            this.isUseWriteReplace = a3;
        }
    }
    
    static class Find2MethodsArgs
    {
        String methodName;
        String delegatorName;
        String descriptor;
        int origIndex;
        
        Find2MethodsArgs(final String a1, final String a2, final String a3, final int a4) {
            this.methodName = a1;
            this.delegatorName = a2;
            this.descriptor = a3;
            this.origIndex = a4;
        }
    }
    
    public interface UniqueName
    {
        String get(String p0);
    }
    
    public interface ClassLoaderProvider
    {
        ClassLoader get(ProxyFactory p0);
    }
}
