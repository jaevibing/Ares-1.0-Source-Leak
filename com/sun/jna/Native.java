package com.sun.jna;

import java.awt.GraphicsEnvironment;
import java.util.WeakHashMap;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.io.FilenameFilter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Method;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.net.URLClassLoader;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.lang.ref.Reference;
import java.util.Map;

public final class Native implements Version
{
    public static final String DEFAULT_ENCODING;
    public static boolean DEBUG_LOAD;
    public static boolean DEBUG_JNA_LOAD;
    static String jnidispatchPath;
    private static final Map<Class<?>, Map<String, Object>> typeOptions;
    private static final Map<Class<?>, Reference<?>> libraries;
    private static final String _OPTION_ENCLOSING_LIBRARY = "enclosing-library";
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    public static final int BOOL_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    private static final int TYPE_BOOL = 4;
    static final int MAX_ALIGNMENT;
    static final int MAX_PADDING;
    private static final Object finalizer;
    static final String JNA_TMPLIB_PREFIX = "jna";
    private static Map<Class<?>, long[]> registeredClasses;
    private static Map<Class<?>, NativeLibrary> registeredLibraries;
    static final int CB_HAS_INITIALIZER = 1;
    private static final int CVT_UNSUPPORTED = -1;
    private static final int CVT_DEFAULT = 0;
    private static final int CVT_POINTER = 1;
    private static final int CVT_STRING = 2;
    private static final int CVT_STRUCTURE = 3;
    private static final int CVT_STRUCTURE_BYVAL = 4;
    private static final int CVT_BUFFER = 5;
    private static final int CVT_ARRAY_BYTE = 6;
    private static final int CVT_ARRAY_SHORT = 7;
    private static final int CVT_ARRAY_CHAR = 8;
    private static final int CVT_ARRAY_INT = 9;
    private static final int CVT_ARRAY_LONG = 10;
    private static final int CVT_ARRAY_FLOAT = 11;
    private static final int CVT_ARRAY_DOUBLE = 12;
    private static final int CVT_ARRAY_BOOLEAN = 13;
    private static final int CVT_BOOLEAN = 14;
    private static final int CVT_CALLBACK = 15;
    private static final int CVT_FLOAT = 16;
    private static final int CVT_NATIVE_MAPPED = 17;
    private static final int CVT_NATIVE_MAPPED_STRING = 18;
    private static final int CVT_NATIVE_MAPPED_WSTRING = 19;
    private static final int CVT_WSTRING = 20;
    private static final int CVT_INTEGER_TYPE = 21;
    private static final int CVT_POINTER_TYPE = 22;
    private static final int CVT_TYPE_MAPPER = 23;
    private static final int CVT_TYPE_MAPPER_STRING = 24;
    private static final int CVT_TYPE_MAPPER_WSTRING = 25;
    static final int CB_OPTION_DIRECT = 1;
    static final int CB_OPTION_IN_DLL = 2;
    private static final ThreadLocal<Memory> nativeThreadTerminationFlag;
    private static final Map<Thread, Pointer> nativeThreads;
    
    @Deprecated
    public static float parseVersion(final String a1) {
        /*SL:153*/return Float.parseFloat(a1.substring(0, a1.lastIndexOf(".")));
    }
    
    static boolean isCompatibleVersion(final String a1, final String a2) {
        final String[] v1 = /*EL:167*/a1.split("\\.");
        final String[] v2 = /*EL:168*/a2.split("\\.");
        /*SL:169*/if (v1.length < 3 || v2.length < 3) {
            /*SL:170*/return false;
        }
        final int v3 = /*EL:173*/Integer.parseInt(v1[0]);
        final int v4 = /*EL:174*/Integer.parseInt(v2[0]);
        final int v5 = /*EL:175*/Integer.parseInt(v1[1]);
        final int v6 = /*EL:176*/Integer.parseInt(v2[1]);
        /*SL:178*/return v3 == v4 && /*EL:182*/v5 <= v6;
    }
    
    private static void dispose() {
        /*SL:242*/CallbackReference.disposeAll();
        /*SL:243*/Memory.disposeAll();
        /*SL:244*/NativeLibrary.disposeAll();
        unregisterAll();
        Native.jnidispatchPath = /*EL:246*/null;
        /*SL:247*/System.setProperty("jna.loaded", "false");
    }
    
    static boolean deleteLibrary(final File a1) {
        /*SL:262*/if (a1.delete()) {
            /*SL:263*/return true;
        }
        markTemporaryFile(/*EL:267*/a1);
        /*SL:269*/return false;
    }
    
    private static native void initIDs();
    
    public static synchronized native void setProtected(final boolean p0);
    
    public static synchronized native boolean isProtected();
    
    @Deprecated
    public static void setPreserveLastError(final boolean a1) {
    }
    
    @Deprecated
    public static boolean getPreserveLastError() {
        /*SL:315*/return true;
    }
    
    public static long getWindowID(final Window a1) throws HeadlessException {
        /*SL:324*/return AWT.getWindowID(a1);
    }
    
    public static long getComponentID(final Component a1) throws HeadlessException {
        /*SL:334*/return AWT.getComponentID(a1);
    }
    
    public static Pointer getWindowPointer(final Window a1) throws HeadlessException {
        /*SL:344*/return new Pointer(AWT.getWindowID(a1));
    }
    
    public static Pointer getComponentPointer(final Component a1) throws HeadlessException {
        /*SL:354*/return new Pointer(AWT.getComponentID(a1));
    }
    
    static native long getWindowHandle0(final Component p0);
    
    public static Pointer getDirectBufferPointer(final Buffer a1) {
        final long v1 = _getDirectBufferPointer(/*EL:363*/a1);
        /*SL:364*/return (v1 == 0L) ? null : new Pointer(v1);
    }
    
    private static native long _getDirectBufferPointer(final Buffer p0);
    
    public static String toString(final byte[] a1) {
        /*SL:378*/return toString(a1, getDefaultStringEncoding());
    }
    
    public static String toString(final byte[] v1, final String v2) {
        int v3 = /*EL:395*/v1.length;
        /*SL:397*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:398*/if (v1[a1] == 0) {
                /*SL:399*/v3 = a1;
                /*SL:400*/break;
            }
        }
        /*SL:404*/if (v3 == 0) {
            /*SL:405*/return "";
        }
        /*SL:408*/if (v2 != null) {
            try {
                /*SL:410*/return new String(v1, 0, v3, v2);
            }
            catch (UnsupportedEncodingException a2) {
                System.err.println(/*EL:413*/"JNA Warning: Encoding '" + v2 + "' is unsupported");
            }
        }
        System.err.println(/*EL:418*/"JNA Warning: Decoding with fallback " + System.getProperty("file.encoding"));
        /*SL:419*/return new String(v1, 0, v3);
    }
    
    public static String toString(final char[] v1) {
        int v2 = /*EL:429*/v1.length;
        /*SL:430*/for (int a1 = 0; a1 < v2; ++a1) {
            /*SL:431*/if (v1[a1] == '\0') {
                /*SL:432*/v2 = a1;
                /*SL:433*/break;
            }
        }
        /*SL:437*/if (v2 == 0) {
            /*SL:438*/return "";
        }
        /*SL:440*/return new String(v1, 0, v2);
    }
    
    public static List<String> toStringList(final char[] a1) {
        /*SL:454*/return toStringList(a1, 0, a1.length);
    }
    
    public static List<String> toStringList(final char[] v1, final int v2, final int v3) {
        final List<String> v4 = /*EL:468*/new ArrayList<String>();
        int v5 = /*EL:469*/v2;
        final int v6 = /*EL:470*/v2 + v3;
        /*SL:471*/for (String a2 = (String)v2; a2 < v6; ++a2) {
            /*SL:472*/if (v1[a2] == '\0') {
                /*SL:477*/if (v5 == a2) {
                    /*SL:478*/return v4;
                }
                /*SL:481*/a2 = new String(v1, v5, a2 - v5);
                /*SL:482*/v4.add(a2);
                /*SL:483*/v5 = a2 + 1;
            }
        }
        /*SL:487*/if (v5 < v6) {
            final String a3 = /*EL:488*/new String(v1, v5, v6 - v5);
            /*SL:489*/v4.add(a3);
        }
        /*SL:492*/return v4;
    }
    
    public static <T> T loadLibrary(final Class<T> a1) {
        /*SL:507*/return Native.<T>loadLibrary(null, a1);
    }
    
    public static <T> T loadLibrary(final Class<T> a1, final Map<String, ?> a2) {
        /*SL:526*/return Native.<T>loadLibrary(null, a1, a2);
    }
    
    public static <T> T loadLibrary(final String a1, final Class<T> a2) {
        /*SL:544*/return Native.<T>loadLibrary(a1, a2, Collections.<String, ?>emptyMap());
    }
    
    public static <T> T loadLibrary(final String a1, final Class<T> a2, final Map<String, ?> a3) {
        /*SL:564*/if (!Library.class.isAssignableFrom(a2)) {
            /*SL:565*/throw new IllegalArgumentException("Interface (" + a2.getSimpleName() + ") of library=" + a1 + " does not extend " + Library.class.getSimpleName());
        }
        final Library.Handler v1 = /*EL:569*/new Library.Handler(a1, a2, a3);
        final ClassLoader v2 = /*EL:570*/a2.getClassLoader();
        final Object v3 = /*EL:571*/Proxy.newProxyInstance(v2, new Class[] { a2 }, v1);
        cacheOptions(/*EL:572*/a2, a3, v3);
        /*SL:573*/return a2.cast(v3);
    }
    
    private static void loadLibraryInstance(final Class<?> v-2) {
        /*SL:582*/synchronized (Native.libraries) {
            /*SL:583*/if (v-2 != null && !Native.libraries.containsKey(v-2)) {
                try {
                    final Field[] v0 = /*EL:585*/v-2.getFields();
                    /*SL:586*/for (int v = 0; v < v0.length; ++v) {
                        final Field a1 = /*EL:587*/v0[v];
                        /*SL:588*/if (a1.getType() == v-2 && /*EL:589*/Modifier.isStatic(a1.getModifiers())) {
                            Native.libraries.put(/*EL:591*/v-2, new WeakReference<Object>(a1.get(null)));
                            /*SL:592*/break;
                        }
                    }
                }
                catch (Exception v2) {
                    /*SL:597*/throw new IllegalArgumentException("Could not access instance of " + v-2 + " (" + v2 + ")");
                }
            }
        }
    }
    
    static Class<?> findEnclosingLibraryClass(Class<?> v-2) {
        /*SL:612*/if (v-2 == null) {
            /*SL:613*/return null;
        }
        /*SL:617*/synchronized (Native.libraries) {
            /*SL:618*/if (Native.typeOptions.containsKey(v-2)) {
                final Map<String, ?> a1 = Native.typeOptions.get(/*EL:619*/v-2);
                final Class<?> v1 = /*EL:620*/(Class<?>)a1.get("enclosing-library");
                /*SL:621*/if (v1 != null) {
                    /*SL:622*/return v1;
                }
                /*SL:624*/return v-2;
            }
        }
        /*SL:627*/if (Library.class.isAssignableFrom(v-2)) {
            /*SL:628*/return v-2;
        }
        /*SL:630*/if (Callback.class.isAssignableFrom(v-2)) {
            /*SL:631*/v-2 = CallbackReference.findCallbackClass(v-2);
        }
        final Class<?> declaringClass = /*EL:633*/v-2.getDeclaringClass();
        final Class<?> a2 = findEnclosingLibraryClass(/*EL:634*/declaringClass);
        /*SL:635*/if (a2 != null) {
            /*SL:636*/return a2;
        }
        /*SL:638*/return findEnclosingLibraryClass(v-2.getSuperclass());
    }
    
    public static Map<String, Object> getLibraryOptions(final Class<?> v-3) {
        /*SL:657*/synchronized (Native.libraries) {
            final Map<String, Object> a1 = Native.typeOptions.get(/*EL:658*/v-3);
            /*SL:659*/if (a1 != null) {
                /*SL:660*/return a1;
            }
        }
        Class<?> enclosingLibraryClass = findEnclosingLibraryClass(/*EL:664*/v-3);
        /*SL:665*/if (enclosingLibraryClass != null) {
            loadLibraryInstance(/*EL:666*/enclosingLibraryClass);
        }
        else {
            /*SL:668*/enclosingLibraryClass = v-3;
        }
        /*SL:671*/synchronized (Native.libraries) {
            Map<String, Object> a1 = Native.typeOptions.get(/*EL:672*/enclosingLibraryClass);
            /*SL:673*/if (a1 != null) {
                Native.typeOptions.put(/*EL:674*/v-3, a1);
                /*SL:675*/return a1;
            }
            try {
                final Field v1 = /*EL:679*/enclosingLibraryClass.getField("OPTIONS");
                /*SL:680*/v1.setAccessible(true);
                /*SL:681*/a1 = (Map<String, Object>)v1.get(null);
                /*SL:682*/if (a1 == null) {
                    /*SL:683*/throw new IllegalStateException("Null options field");
                }
            }
            catch (NoSuchFieldException v3) {
                /*SL:686*/a1 = Collections.<String, Object>emptyMap();
            }
            catch (Exception v2) {
                /*SL:688*/throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + v2 + "): " + enclosingLibraryClass);
            }
            /*SL:691*/a1 = new HashMap<String, Object>(a1);
            /*SL:692*/if (!a1.containsKey("type-mapper")) {
                /*SL:693*/a1.put("type-mapper", lookupField(enclosingLibraryClass, "TYPE_MAPPER", TypeMapper.class));
            }
            /*SL:695*/if (!a1.containsKey("structure-alignment")) {
                /*SL:696*/a1.put("structure-alignment", lookupField(enclosingLibraryClass, "STRUCTURE_ALIGNMENT", Integer.class));
            }
            /*SL:698*/if (!a1.containsKey("string-encoding")) {
                /*SL:699*/a1.put("string-encoding", lookupField(enclosingLibraryClass, "STRING_ENCODING", String.class));
            }
            /*SL:701*/a1 = cacheOptions(enclosingLibraryClass, a1, null);
            /*SL:703*/if (v-3 != enclosingLibraryClass) {
                Native.typeOptions.put(/*EL:704*/v-3, a1);
            }
            /*SL:706*/return a1;
        }
    }
    
    private static Object lookupField(final Class<?> v1, final String v2, final Class<?> v3) {
        try {
            final Field a1 = /*EL:712*/v1.getField(v2);
            /*SL:713*/a1.setAccessible(true);
            /*SL:714*/return a1.get(null);
        }
        catch (NoSuchFieldException a3) {
            /*SL:717*/return null;
        }
        catch (Exception a2) {
            /*SL:720*/throw new IllegalArgumentException(v2 + " must be a public field of type " + v3.getName() + /*EL:721*/" (" + a2 + "): " + v1);
        }
    }
    
    public static TypeMapper getTypeMapper(final Class<?> a1) {
        final Map<String, ?> v1 = getLibraryOptions(/*EL:730*/a1);
        /*SL:731*/return (TypeMapper)v1.get("type-mapper");
    }
    
    public static String getStringEncoding(final Class<?> a1) {
        final Map<String, ?> v1 = getLibraryOptions(/*EL:741*/a1);
        final String v2 = /*EL:742*/(String)v1.get("string-encoding");
        /*SL:743*/return (v2 != null) ? v2 : getDefaultStringEncoding();
    }
    
    public static String getDefaultStringEncoding() {
        /*SL:751*/return System.getProperty("jna.encoding", Native.DEFAULT_ENCODING);
    }
    
    public static int getStructureAlignment(final Class<?> a1) {
        final Integer v1 = getLibraryOptions(/*EL:760*/a1).get("structure-alignment");
        /*SL:761*/return (v1 == null) ? 0 : v1;
    }
    
    static byte[] getBytes(final String a1) {
        /*SL:770*/return getBytes(a1, getDefaultStringEncoding());
    }
    
    static byte[] getBytes(final String a2, final String v1) {
        /*SL:782*/if (v1 != null) {
            try {
                /*SL:784*/return a2.getBytes(v1);
            }
            catch (UnsupportedEncodingException a3) {
                System.err.println(/*EL:787*/"JNA Warning: Encoding '" + v1 + "' is unsupported");
            }
        }
        System.err.println(/*EL:791*/"JNA Warning: Encoding with fallback " + /*EL:792*/System.getProperty("file.encoding"));
        /*SL:793*/return a2.getBytes();
    }
    
    public static byte[] toByteArray(final String a1) {
        /*SL:803*/return toByteArray(a1, getDefaultStringEncoding());
    }
    
    public static byte[] toByteArray(final String a1, final String a2) {
        final byte[] v1 = getBytes(/*EL:813*/a1, a2);
        final byte[] v2 = /*EL:814*/new byte[v1.length + 1];
        /*SL:815*/System.arraycopy(v1, 0, v2, 0, v1.length);
        /*SL:816*/return v2;
    }
    
    public static char[] toCharArray(final String a1) {
        final char[] v1 = /*EL:824*/a1.toCharArray();
        final char[] v2 = /*EL:825*/new char[v1.length + 1];
        /*SL:826*/System.arraycopy(v1, 0, v2, 0, v1.length);
        /*SL:827*/return v2;
    }
    
    private static void loadNativeDispatchLibrary() {
        /*SL:836*/if (!Boolean.getBoolean("jna.nounpack")) {
            try {
                removeTemporaryFiles();
            }
            catch (IOException v1) {
                System.err.println(/*EL:841*/"JNA Warning: IOException removing temporary files: " + v1.getMessage());
            }
        }
        final String v2 = /*EL:845*/System.getProperty("jna.boot.library.name", "jnidispatch");
        final String v3 = /*EL:846*/System.getProperty("jna.boot.library.path");
        /*SL:847*/if (v3 != null) {
            final StringTokenizer v4 = /*EL:849*/new StringTokenizer(v3, File.pathSeparator);
            /*SL:850*/while (v4.hasMoreTokens()) {
                final String v5 = /*EL:851*/v4.nextToken();
                final File v6 = /*EL:852*/new File(new File(v5), System.mapLibraryName(v2).replace(".dylib", ".jnilib"));
                String v7 = /*EL:853*/v6.getAbsolutePath();
                /*SL:854*/if (Native.DEBUG_JNA_LOAD) {
                    System.out.println(/*EL:855*/"Looking in " + v7);
                }
                /*SL:857*/if (v6.exists()) {
                    try {
                        /*SL:859*/if (Native.DEBUG_JNA_LOAD) {
                            System.out.println(/*EL:860*/"Trying " + v7);
                        }
                        /*SL:862*/System.setProperty("jnidispatch.path", v7);
                        /*SL:863*/System.load(v7);
                        Native.jnidispatchPath = /*EL:864*/v7;
                        /*SL:865*/if (Native.DEBUG_JNA_LOAD) {
                            System.out.println(/*EL:866*/"Found jnidispatch at " + v7);
                        }
                        /*SL:868*/return;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                }
                /*SL:875*/if (Platform.isMac()) {
                    String v8;
                    String v9;
                    /*SL:877*/if (v7.endsWith("dylib")) {
                        /*SL:878*/v8 = "dylib";
                        /*SL:879*/v9 = "jnilib";
                    }
                    else {
                        /*SL:881*/v8 = "jnilib";
                        /*SL:882*/v9 = "dylib";
                    }
                    /*SL:884*/v7 = v7.substring(0, v7.lastIndexOf(v8)) + v9;
                    /*SL:885*/if (Native.DEBUG_JNA_LOAD) {
                        System.out.println(/*EL:886*/"Looking in " + v7);
                    }
                    /*SL:888*/if (!new File(v7).exists()) {
                        continue;
                    }
                    try {
                        /*SL:890*/if (Native.DEBUG_JNA_LOAD) {
                            System.out.println(/*EL:891*/"Trying " + v7);
                        }
                        /*SL:893*/System.setProperty("jnidispatch.path", v7);
                        /*SL:894*/System.load(v7);
                        Native.jnidispatchPath = /*EL:895*/v7;
                        /*SL:896*/if (Native.DEBUG_JNA_LOAD) {
                            System.out.println(/*EL:897*/"Found jnidispatch at " + v7);
                        }
                        /*SL:899*/return;
                    }
                    catch (UnsatisfiedLinkError v10) {
                        System.err.println(/*EL:901*/"File found at " + v7 + " but not loadable: " + v10.getMessage());
                    }
                }
            }
        }
        /*SL:907*/if (!Boolean.getBoolean("jna.nosys")) {
            try {
                /*SL:909*/if (Native.DEBUG_JNA_LOAD) {
                    System.out.println(/*EL:910*/"Trying (via loadLibrary) " + v2);
                }
                /*SL:912*/System.loadLibrary(v2);
                /*SL:913*/if (Native.DEBUG_JNA_LOAD) {
                    System.out.println(/*EL:914*/"Found jnidispatch on system path");
                }
                /*SL:916*/return;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError2) {}
        }
        /*SL:921*/if (!Boolean.getBoolean("jna.noclasspath")) {
            loadNativeDispatchLibraryFromClasspath();
            /*SL:927*/return;
        }
        throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
    }
    
    private static void loadNativeDispatchLibraryFromClasspath() {
        try {
            final String v1 = /*EL:936*/"/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
            final File v2 = extractFromResourcePath(/*EL:937*/v1, Native.class.getClassLoader());
            /*SL:938*/if (v2 == null && /*EL:939*/v2 == null) {
                /*SL:940*/throw new UnsatisfiedLinkError("Could not find JNA native support");
            }
            /*SL:943*/if (Native.DEBUG_JNA_LOAD) {
                System.out.println(/*EL:944*/"Trying " + v2.getAbsolutePath());
            }
            /*SL:946*/System.setProperty("jnidispatch.path", v2.getAbsolutePath());
            /*SL:947*/System.load(v2.getAbsolutePath());
            Native.jnidispatchPath = /*EL:948*/v2.getAbsolutePath();
            /*SL:949*/if (Native.DEBUG_JNA_LOAD) {
                System.out.println(/*EL:950*/"Found jnidispatch at " + Native.jnidispatchPath);
            }
            /*SL:956*/if (isUnpacked(v2) && /*EL:957*/!Boolean.getBoolean("jnidispatch.preserve")) {
                deleteLibrary(/*EL:958*/v2);
            }
        }
        catch (IOException v3) {
            /*SL:962*/throw new UnsatisfiedLinkError(v3.getMessage());
        }
    }
    
    static boolean isUnpacked(final File a1) {
        /*SL:968*/return a1.getName().startsWith("jna");
    }
    
    public static File extractFromResourcePath(final String a1) throws IOException {
        /*SL:983*/return extractFromResourcePath(a1, null);
    }
    
    public static File extractFromResourcePath(final String v-8, ClassLoader v-7) throws IOException {
        final boolean b = Native.DEBUG_LOAD || (Native.DEBUG_JNA_LOAD && /*EL:999*/v-8.indexOf("jnidispatch") != /*EL:1000*/-1);
        /*SL:1001*/if (v-7 == null) {
            /*SL:1002*/v-7 = Thread.currentThread().getContextClassLoader();
            /*SL:1004*/if (v-7 == null) {
                /*SL:1005*/v-7 = Native.class.getClassLoader();
            }
        }
        /*SL:1008*/if (b) {
            System.out.println(/*EL:1009*/"Looking in classpath from " + v-7 + " for " + v-8);
        }
        final String s = /*EL:1011*/v-8.startsWith("/") ? v-8 : NativeLibrary.mapSharedLibraryName(v-8);
        String substring = /*EL:1012*/v-8.startsWith("/") ? v-8 : (Platform.RESOURCE_PREFIX + "/" + s);
        /*SL:1013*/if (substring.startsWith("/")) {
            /*SL:1014*/substring = substring.substring(1);
        }
        URL url = /*EL:1016*/v-7.getResource(substring);
        /*SL:1017*/if (url == null && substring.startsWith(Platform.RESOURCE_PREFIX)) {
            /*SL:1019*/url = v-7.getResource(s);
        }
        /*SL:1021*/if (url == null) {
            String a1 = /*EL:1022*/System.getProperty("java.class.path");
            /*SL:1023*/if (v-7 instanceof URLClassLoader) {
                /*SL:1024*/a1 = Arrays.<URL>asList(((URLClassLoader)v-7).getURLs()).toString();
            }
            /*SL:1026*/throw new IOException("Native library (" + substring + ") not found in resource path (" + a1 + ")");
        }
        /*SL:1028*/if (b) {
            System.out.println(/*EL:1029*/"Found library resource at " + url);
        }
        File tempFile = /*EL:1032*/null;
        /*SL:1033*/if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                /*SL:1035*/tempFile = new File(new URI(url.toString()));
            }
            catch (URISyntaxException a2) {
                /*SL:1038*/tempFile = new File(url.getPath());
            }
            /*SL:1040*/if (b) {
                System.out.println(/*EL:1041*/"Looking in " + tempFile.getAbsolutePath());
            }
            /*SL:1043*/if (!tempFile.exists()) {
                /*SL:1044*/throw new IOException("File URL " + url + " could not be properly decoded");
            }
        }
        else/*SL:1047*/ if (!Boolean.getBoolean("jna.nounpack")) {
            final InputStream resourceAsStream = /*EL:1048*/v-7.getResourceAsStream(substring);
            /*SL:1049*/if (resourceAsStream == null) {
                /*SL:1050*/throw new IOException("Can't obtain InputStream for " + substring);
            }
            FileOutputStream v0 = /*EL:1053*/null;
            try {
                final File v = getTempDir();
                /*SL:1059*/tempFile = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, v);
                /*SL:1060*/if (!Boolean.getBoolean("jnidispatch.preserve")) {
                    /*SL:1061*/tempFile.deleteOnExit();
                }
                /*SL:1063*/v0 = new FileOutputStream(tempFile);
                final byte[] v2 = /*EL:1065*/new byte[1024];
                int v3;
                /*SL:1066*/while ((v3 = resourceAsStream.read(v2, 0, v2.length)) > 0) {
                    /*SL:1067*/v0.write(v2, 0, v3);
                }
            }
            catch (IOException v4) {
                /*SL:1071*/throw new IOException("Failed to create temporary file for " + v-8 + " library: " + v4.getMessage());
            }
            finally {
                try {
                    /*SL:1074*/resourceAsStream.close();
                }
                catch (IOException ex) {}
                /*SL:1075*/if (v0 != null) {
                    try {
                        /*SL:1076*/v0.close();
                    }
                    catch (IOException ex2) {}
                }
            }
        }
        /*SL:1080*/return tempFile;
    }
    
    private static native int sizeof(final int p0);
    
    private static native String getNativeVersion();
    
    private static native String getAPIChecksum();
    
    public static native int getLastError();
    
    public static native void setLastError(final int p0);
    
    public static Library synchronizedLibrary(final Library a1) {
        final Class<?> v1 = /*EL:1123*/a1.getClass();
        /*SL:1124*/if (!Proxy.isProxyClass(v1)) {
            /*SL:1125*/throw new IllegalArgumentException("Library must be a proxy class");
        }
        final InvocationHandler v2 = /*EL:1127*/Proxy.getInvocationHandler(a1);
        /*SL:1128*/if (!(v2 instanceof Library.Handler)) {
            /*SL:1129*/throw new IllegalArgumentException("Unrecognized proxy handler: " + v2);
        }
        final Library.Handler v3 = /*EL:1131*/(Library.Handler)v2;
        final InvocationHandler v4 = /*EL:1132*/new InvocationHandler() {
            @Override
            public Object invoke(final Object a1, final Method a2, final Object[] a3) throws Throwable {
                /*SL:1135*/synchronized (/*EL:1137*/v3.getNativeLibrary()) {
                    return v3.invoke(a1, a2, a3);
                }
            }
        };
        /*SL:1140*/return (Library)Proxy.newProxyInstance(v1.getClassLoader(), v1.getInterfaces(), /*EL:1141*/v4);
    }
    
    public static String getWebStartLibraryPath(final String v-1) {
        /*SL:1161*/if (System.getProperty("javawebstart.version") == null) {
            /*SL:1162*/return null;
        }
        try {
            final ClassLoader a1 = /*EL:1165*/Native.class.getClassLoader();
            final Method v1 = /*EL:1166*/AccessController.<Method>doPrivileged((PrivilegedAction<Method>)new PrivilegedAction<Method>() {
                @Override
                public Method run() {
                    try {
                        final Method v1 = /*EL:1170*/ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
                        /*SL:1171*/v1.setAccessible(true);
                        /*SL:1172*/return v1;
                    }
                    catch (Exception v2) {
                        /*SL:1175*/return null;
                    }
                }
            });
            final String v2 = /*EL:1179*/(String)v1.invoke(a1, v-1);
            /*SL:1180*/if (v2 != null) {
                /*SL:1181*/return new File(v2).getParent();
            }
            /*SL:1183*/return null;
        }
        catch (Exception v3) {
            /*SL:1186*/return null;
        }
    }
    
    static void markTemporaryFile(final File v0) {
        try {
            final File a1 = /*EL:1196*/new File(v0.getParentFile(), v0.getName() + ".x");
            /*SL:1197*/a1.createNewFile();
        }
        catch (IOException v) {
            /*SL:1199*/v.printStackTrace();
        }
    }
    
    static File getTempDir() throws IOException {
        String v2 = /*EL:1207*/System.getProperty("jna.tmpdir");
        /*SL:1208*/if (v2 != null) {
            /*SL:1209*/v2 = new File(v2);
            /*SL:1210*/v2.mkdirs();
        }
        else {
            final File v3 = /*EL:1213*/new File(System.getProperty("java.io.tmpdir"));
            /*SL:1217*/v2 = new File(v3, "jna-" + System.getProperty("user.name").hashCode());
            /*SL:1218*/v2.mkdirs();
            /*SL:1219*/if (!v2.exists() || !v2.canWrite()) {
                /*SL:1220*/v2 = v3;
            }
        }
        /*SL:1223*/if (!v2.exists()) {
            /*SL:1224*/throw new IOException("JNA temporary directory '" + v2 + "' does not exist");
        }
        /*SL:1226*/if (!v2.canWrite()) {
            /*SL:1227*/throw new IOException("JNA temporary directory '" + v2 + "' is not writable");
        }
        /*SL:1229*/return v2;
    }
    
    static void removeTemporaryFiles() throws IOException {
        final File tempDir = getTempDir();
        final FilenameFilter filenameFilter = /*EL:1235*/new FilenameFilter() {
            @Override
            public boolean accept(final File a1, final String a2) {
                /*SL:1238*/return a2.endsWith(".x") && a2.startsWith("jna");
            }
        };
        final File[] listFiles = /*EL:1241*/tempDir.listFiles(filenameFilter);
        /*SL:1242*/for (int v0 = 0; listFiles != null && v0 < listFiles.length; ++v0) {
            final File v = /*EL:1243*/listFiles[v0];
            String v2 = /*EL:1244*/v.getName();
            /*SL:1245*/v2 = v2.substring(0, v2.length() - 2);
            final File v3 = /*EL:1246*/new File(v.getParentFile(), v2);
            /*SL:1247*/if (!v3.exists() || v3.delete()) {
                /*SL:1248*/v.delete();
            }
        }
    }
    
    public static int getNativeSize(final Class<?> v-1, final Object v0) {
        /*SL:1260*/if (v-1.isArray()) {
            int a2 = /*EL:1261*/Array.getLength(v0);
            /*SL:1262*/if (a2 > 0) {
                /*SL:1263*/a2 = Array.get(v0, 0);
                /*SL:1264*/return a2 * getNativeSize(v-1.getComponentType(), a2);
            }
            /*SL:1267*/throw new IllegalArgumentException("Arrays of length zero not allowed: " + v-1);
        }
        else {
            /*SL:1269*/if (Structure.class.isAssignableFrom(v-1) && !Structure.ByReference.class.isAssignableFrom(v-1)) {
                /*SL:1271*/return Structure.size(v-1, (Structure)v0);
            }
            try {
                /*SL:1274*/return getNativeSize(v-1);
            }
            catch (IllegalArgumentException v) {
                /*SL:1277*/throw new IllegalArgumentException("The type \"" + v-1.getName() + "\" is not supported: " + v.getMessage());
            }
        }
    }
    
    public static int getNativeSize(Class<?> a1) {
        /*SL:1292*/if (NativeMapped.class.isAssignableFrom(a1)) {
            /*SL:1293*/a1 = NativeMappedConverter.getInstance(a1).nativeType();
        }
        /*SL:1296*/if (a1 == Boolean.TYPE || a1 == Boolean.class) {
            return 4;
        }
        /*SL:1297*/if (a1 == Byte.TYPE || a1 == Byte.class) {
            return 1;
        }
        /*SL:1298*/if (a1 == Short.TYPE || a1 == Short.class) {
            return 2;
        }
        /*SL:1299*/if (a1 == Character.TYPE || a1 == Character.class) {
            return Native.WCHAR_SIZE;
        }
        /*SL:1300*/if (a1 == Integer.TYPE || a1 == Integer.class) {
            return 4;
        }
        /*SL:1301*/if (a1 == Long.TYPE || a1 == Long.class) {
            return 8;
        }
        /*SL:1302*/if (a1 == Float.TYPE || a1 == Float.class) {
            return 4;
        }
        /*SL:1303*/if (a1 == Double.TYPE || a1 == Double.class) {
            return 8;
        }
        /*SL:1304*/if (Structure.class.isAssignableFrom(a1)) {
            /*SL:1305*/if (Structure.ByValue.class.isAssignableFrom(a1)) {
                /*SL:1306*/return Structure.size(a1);
            }
            /*SL:1308*/return Native.POINTER_SIZE;
        }
        else {
            /*SL:1312*/if (Pointer.class.isAssignableFrom(a1) || (Platform.HAS_BUFFERS && Buffers.isBuffer(a1)) || Callback.class.isAssignableFrom(a1) || String.class == a1 || WString.class == a1) {
                /*SL:1315*/return Native.POINTER_SIZE;
            }
            /*SL:1317*/throw new IllegalArgumentException("Native size for type \"" + a1.getName() + "\" is unknown");
        }
    }
    
    public static boolean isSupportedNativeType(final Class<?> v1) {
        /*SL:1326*/if (Structure.class.isAssignableFrom(v1)) {
            /*SL:1327*/return true;
        }
        try {
            /*SL:1330*/return getNativeSize(v1) != 0;
        }
        catch (IllegalArgumentException a1) {
            /*SL:1333*/return false;
        }
    }
    
    public static void setCallbackExceptionHandler(final Callback.UncaughtExceptionHandler a1) {
        Native.callbackExceptionHandler = /*EL:1345*/((a1 == null) ? Native.DEFAULT_HANDLER : a1);
    }
    
    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        /*SL:1350*/return Native.callbackExceptionHandler;
    }
    
    public static void register(final String a1) {
        register(findDirectMappedClass(getCallingClass()), /*EL:1360*/a1);
    }
    
    public static void register(final NativeLibrary a1) {
        register(findDirectMappedClass(getCallingClass()), /*EL:1370*/a1);
    }
    
    static Class<?> findDirectMappedClass(final Class<?> v-2) {
        final Method[] declaredMethods;
        final Method[] array = /*EL:1376*/declaredMethods = v-2.getDeclaredMethods();
        for (final Method a1 : declaredMethods) {
            /*SL:1377*/if ((a1.getModifiers() & 0x100) != 0x0) {
                /*SL:1378*/return v-2;
            }
        }
        final int v0 = /*EL:1381*/v-2.getName().lastIndexOf("$");
        /*SL:1382*/if (v0 != -1) {
            final String v = /*EL:1383*/v-2.getName().substring(0, v0);
            try {
                /*SL:1385*/return findDirectMappedClass(Class.forName(v, true, v-2.getClassLoader()));
            }
            catch (ClassNotFoundException ex) {}
        }
        /*SL:1390*/throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + v-2 + ")");
    }
    
    static Class<?> getCallingClass() {
        final Class<?>[] v1 = /*EL:1397*/new SecurityManager() {
            public Class<?>[] getClassContext() {
                /*SL:1400*/return (Class<?>[])super.getClassContext();
            }
        }.getClassContext();
        /*SL:1403*/if (v1 == null) {
            /*SL:1404*/throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
        }
        /*SL:1406*/if (v1.length < 4) {
            /*SL:1407*/throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        /*SL:1409*/return v1[3];
    }
    
    public static void setCallbackThreadInitializer(final Callback a1, final CallbackThreadInitializer a2) {
        /*SL:1419*/CallbackReference.setCallbackThreadInitializer(a1, a2);
    }
    
    private static void unregisterAll() {
        /*SL:1426*/synchronized (Native.registeredClasses) {
            /*SL:1427*/for (final Map.Entry<Class<?>, long[]> v1 : Native.registeredClasses.entrySet()) {
                unregister(/*EL:1428*/v1.getKey(), v1.getValue());
            }
            Native.registeredClasses.clear();
        }
    }
    
    public static void unregister() {
        unregister(findDirectMappedClass(getCallingClass()));
    }
    
    public static void unregister(final Class<?> v1) {
        /*SL:1448*/synchronized (Native.registeredClasses) {
            final long[] a1 = Native.registeredClasses.get(/*EL:1449*/v1);
            /*SL:1450*/if (a1 != null) {
                unregister(/*EL:1451*/v1, a1);
                Native.registeredClasses.remove(/*EL:1452*/v1);
                Native.registeredLibraries.remove(/*EL:1453*/v1);
            }
        }
    }
    
    public static boolean registered(final Class<?> a1) {
        /*SL:1463*/synchronized (Native.registeredClasses) {
            /*SL:1464*/return Native.registeredClasses.containsKey(a1);
        }
    }
    
    private static native void unregister(final Class<?> p0, final long[] p1);
    
    static String getSignature(final Class<?> a1) {
        /*SL:1472*/if (a1.isArray()) {
            /*SL:1473*/return "[" + getSignature(a1.getComponentType());
        }
        /*SL:1475*/if (a1.isPrimitive()) {
            /*SL:1476*/if (a1 == Void.TYPE) {
                return "V";
            }
            /*SL:1477*/if (a1 == Boolean.TYPE) {
                return "Z";
            }
            /*SL:1478*/if (a1 == Byte.TYPE) {
                return "B";
            }
            /*SL:1479*/if (a1 == Short.TYPE) {
                return "S";
            }
            /*SL:1480*/if (a1 == Character.TYPE) {
                return "C";
            }
            /*SL:1481*/if (a1 == Integer.TYPE) {
                return "I";
            }
            /*SL:1482*/if (a1 == Long.TYPE) {
                return "J";
            }
            /*SL:1483*/if (a1 == Float.TYPE) {
                return "F";
            }
            /*SL:1484*/if (a1 == Double.TYPE) {
                return "D";
            }
        }
        /*SL:1486*/return "L" + replace(".", "/", a1.getName()) + ";";
    }
    
    static String replace(final String a2, final String a3, String v1) {
        final StringBuilder v2 = /*EL:1491*/new StringBuilder();
        while (true) {
            final int a4 = /*EL:1493*/v1.indexOf(a2);
            /*SL:1494*/if (a4 == -1) {
                break;
            }
            /*SL:1499*/v2.append(v1.substring(0, a4));
            /*SL:1500*/v2.append(a3);
            /*SL:1501*/v1 = v1.substring(a4 + a2.length());
        }
        v2.append(v1);
        /*SL:1504*/return v2.toString();
    }
    
    private static int getConversion(Class<?> v-1, final TypeMapper v0) {
        /*SL:1539*/if (v-1 == Boolean.class) {
            v-1 = Boolean.TYPE;
        }
        else/*SL:1540*/ if (v-1 == Byte.class) {
            v-1 = Byte.TYPE;
        }
        else/*SL:1541*/ if (v-1 == Short.class) {
            v-1 = Short.TYPE;
        }
        else/*SL:1542*/ if (v-1 == Character.class) {
            v-1 = Character.TYPE;
        }
        else/*SL:1543*/ if (v-1 == Integer.class) {
            v-1 = Integer.TYPE;
        }
        else/*SL:1544*/ if (v-1 == Long.class) {
            v-1 = Long.TYPE;
        }
        else/*SL:1545*/ if (v-1 == Float.class) {
            v-1 = Float.TYPE;
        }
        else/*SL:1546*/ if (v-1 == Double.class) {
            v-1 = Double.TYPE;
        }
        else/*SL:1547*/ if (v-1 == Void.class) {
            v-1 = Void.TYPE;
        }
        /*SL:1549*/if (v0 != null) {
            final FromNativeConverter v = /*EL:1550*/v0.getFromNativeConverter(v-1);
            final ToNativeConverter v2 = /*EL:1551*/v0.getToNativeConverter(v-1);
            /*SL:1552*/if (v != null) {
                final Class<?> a1 = /*EL:1553*/v.nativeType();
                /*SL:1554*/if (a1 == String.class) {
                    /*SL:1555*/return 24;
                }
                /*SL:1557*/if (a1 == WString.class) {
                    /*SL:1558*/return 25;
                }
                /*SL:1560*/return 23;
            }
            else/*SL:1562*/ if (v2 != null) {
                final Class<?> a1 = /*EL:1563*/v2.nativeType();
                /*SL:1564*/if (a1 == String.class) {
                    /*SL:1565*/return 24;
                }
                /*SL:1567*/if (a1 == WString.class) {
                    /*SL:1568*/return 25;
                }
                /*SL:1570*/return 23;
            }
        }
        /*SL:1574*/if (Pointer.class.isAssignableFrom(v-1)) {
            /*SL:1575*/return 1;
        }
        /*SL:1577*/if (String.class == v-1) {
            /*SL:1578*/return 2;
        }
        /*SL:1580*/if (WString.class.isAssignableFrom(v-1)) {
            /*SL:1581*/return 20;
        }
        /*SL:1583*/if (Platform.HAS_BUFFERS && Buffers.isBuffer(v-1)) {
            /*SL:1584*/return 5;
        }
        /*SL:1586*/if (Structure.class.isAssignableFrom(v-1)) {
            /*SL:1587*/if (Structure.ByValue.class.isAssignableFrom(v-1)) {
                /*SL:1588*/return 4;
            }
            /*SL:1590*/return 3;
        }
        else {
            /*SL:1592*/if (v-1.isArray()) {
                /*SL:1593*/switch (v-1.getName().charAt(1)) {
                    case 'Z': {
                        /*SL:1594*/return 13;
                    }
                    case 'B': {
                        /*SL:1595*/return 6;
                    }
                    case 'S': {
                        /*SL:1596*/return 7;
                    }
                    case 'C': {
                        /*SL:1597*/return 8;
                    }
                    case 'I': {
                        /*SL:1598*/return 9;
                    }
                    case 'J': {
                        /*SL:1599*/return 10;
                    }
                    case 'F': {
                        /*SL:1600*/return 11;
                    }
                    case 'D': {
                        /*SL:1601*/return 12;
                    }
                }
            }
            /*SL:1605*/if (v-1.isPrimitive()) {
                /*SL:1606*/return (v-1 == Boolean.TYPE) ? 14 : 0;
            }
            /*SL:1608*/if (Callback.class.isAssignableFrom(v-1)) {
                /*SL:1609*/return 15;
            }
            /*SL:1611*/if (IntegerType.class.isAssignableFrom(v-1)) {
                /*SL:1612*/return 21;
            }
            /*SL:1614*/if (PointerType.class.isAssignableFrom(v-1)) {
                /*SL:1615*/return 22;
            }
            /*SL:1617*/if (!NativeMapped.class.isAssignableFrom(v-1)) {
                /*SL:1627*/return -1;
            }
            final Class<?> v3 = NativeMappedConverter.getInstance(v-1).nativeType();
            if (v3 == String.class) {
                return 18;
            }
            if (v3 == WString.class) {
                return 19;
            }
            return 17;
        }
    }
    
    public static void register(final Class<?> a1, final String a2) {
        final NativeLibrary v1 = /*EL:1642*/NativeLibrary.getInstance(a2, Collections.<String, ClassLoader>singletonMap("classloader", a1.getClassLoader()));
        register(/*EL:1643*/a1, v1);
    }
    
    public static void register(final Class<?> v-12, final NativeLibrary v-11) {
        final Method[] declaredMethods = /*EL:1656*/v-12.getDeclaredMethods();
        final List<Method> list = /*EL:1657*/new ArrayList<Method>();
        Map<String, ?> v17 = /*EL:1658*/v-11.getOptions();
        final TypeMapper typeMapper = /*EL:1659*/(TypeMapper)v17.get("type-mapper");
        /*SL:1660*/v17 = cacheOptions(v-12, v17, null);
        /*SL:1662*/for (final Method a1 : declaredMethods) {
            /*SL:1663*/if ((a1.getModifiers() & 0x100) != 0x0) {
                /*SL:1664*/list.add(a1);
            }
        }
        final long[] array2 = /*EL:1668*/new long[list.size()];
        /*SL:1669*/for (int j = 0; j < array2.length; ++j) {
            final Method v18 = /*EL:1670*/list.get(j);
            String s = /*EL:1671*/"(";
            final Class<?> returnType = /*EL:1672*/v18.getReturnType();
            Class<?>[] v3 = /*EL:1674*/v18.getParameterTypes();
            final long[] v2 = /*EL:1675*/new long[v3.length];
            /*SL:1676*/v3 = new long[v3.length];
            final int[] v4 = /*EL:1677*/new int[v3.length];
            final ToNativeConverter[] v5 = /*EL:1678*/new ToNativeConverter[v3.length];
            FromNativeConverter v6 = /*EL:1679*/null;
            final int v7 = getConversion(/*EL:1680*/returnType, typeMapper);
            boolean v8 = /*EL:1681*/false;
            long v9 = 0L;
            long n = 0L;
            /*SL:1682*/switch (v7) {
                case -1: {
                    /*SL:1684*/throw new IllegalArgumentException(returnType + " is not a supported return type (in method " + v18.getName() + " in " + v-12 + ")");
                }
                case 23:
                case 24:
                case 25: {
                    /*SL:1688*/v6 = typeMapper.getFromNativeConverter(returnType);
                    /*SL:1692*/v9 = Structure.FFIType.get(returnType.isPrimitive() ? returnType : Pointer.class).peer;
                    final long a2 = /*EL:1693*/Structure.FFIType.get(v6.nativeType()).peer;
                    /*SL:1694*/break;
                }
                case 17:
                case 18:
                case 19:
                case 21:
                case 22: {
                    /*SL:1700*/v9 = Structure.FFIType.get(Pointer.class).peer;
                    /*SL:1701*/n = Structure.FFIType.get(NativeMappedConverter.getInstance(returnType).nativeType()).peer;
                    /*SL:1702*/break;
                }
                case 3: {
                    /*SL:1704*/n = (v9 = Structure.FFIType.get(Pointer.class).peer);
                    /*SL:1705*/break;
                }
                case 4: {
                    /*SL:1707*/v9 = Structure.FFIType.get(Pointer.class).peer;
                    /*SL:1708*/n = Structure.FFIType.get(returnType).peer;
                    /*SL:1709*/break;
                }
                default: {
                    /*SL:1711*/n = (v9 = Structure.FFIType.get(returnType).peer);
                    break;
                }
            }
            /*SL:1714*/for (int v10 = 0; v10 < v3.length; ++v10) {
                Class<?> v11 = /*EL:1715*/v3[v10];
                /*SL:1716*/s += getSignature(v11);
                final int v12 = getConversion(/*EL:1717*/v11, typeMapper);
                /*SL:1719*/if ((v4[v10] = v12) == -1) {
                    /*SL:1720*/throw new IllegalArgumentException(v11 + " is not a supported argument type (in method " + v18.getName() + " in " + v-12 + ")");
                }
                /*SL:1722*/if (v12 == 17 || v12 == 18 || v12 == 19 || v12 == 21) {
                    /*SL:1726*/v11 = NativeMappedConverter.getInstance(v11).nativeType();
                }
                else/*SL:1727*/ if (v12 == 23 || v12 == 24 || v12 == 25) {
                    /*SL:1730*/v5[v10] = typeMapper.getToNativeConverter(v11);
                }
                /*SL:1736*/switch (v12) {
                    case 4:
                    case 17:
                    case 18:
                    case 19:
                    case 21:
                    case 22: {
                        /*SL:1743*/v2[v10] = Structure.FFIType.get(v11).peer;
                        /*SL:1744*/v3[v10] = Structure.FFIType.get(Pointer.class).peer;
                        /*SL:1745*/break;
                    }
                    case 23:
                    case 24:
                    case 25: {
                        /*SL:1749*/v3[v10] = Structure.FFIType.get(v11.isPrimitive() ? v11 : Pointer.class).peer;
                        /*SL:1750*/v2[v10] = Structure.FFIType.get(v5[v10].nativeType()).peer;
                        /*SL:1751*/break;
                    }
                    case 0: {
                        /*SL:1753*/v3[v10] = (v2[v10] = Structure.FFIType.get(v11).peer);
                        /*SL:1754*/break;
                    }
                    default: {
                        /*SL:1756*/v3[v10] = (v2[v10] = Structure.FFIType.get(Pointer.class).peer);
                        break;
                    }
                }
            }
            /*SL:1759*/s += ")";
            /*SL:1760*/s += getSignature(returnType);
            final Class<?>[] v13 = /*EL:1762*/v18.getExceptionTypes();
            /*SL:1763*/for (int v14 = 0; v14 < v13.length; ++v14) {
                /*SL:1764*/if (LastErrorException.class.isAssignableFrom(v13[v14])) {
                    /*SL:1765*/v8 = true;
                    /*SL:1766*/break;
                }
            }
            final Function v15 = /*EL:1770*/v-11.getFunction(v18.getName(), v18);
            try {
                /*SL:1772*/array2[j] = registerMethod(v-12, v18.getName(), s, v4, v3, v2, v7, v9, n, v18, v15.peer, v15.getCallingConvention(), /*EL:1777*/v8, v5, v6, v15.encoding);
            }
            catch (NoSuchMethodError v16) {
                /*SL:1782*/throw new UnsatisfiedLinkError("No method " + v18.getName() + " with signature " + s + " in " + v-12);
            }
        }
        /*SL:1785*/synchronized (Native.registeredClasses) {
            Native.registeredClasses.put(/*EL:1786*/v-12, array2);
            Native.registeredLibraries.put(/*EL:1787*/v-12, v-11);
        }
    }
    
    private static Map<String, Object> cacheOptions(final Class<?> a3, final Map<String, ?> v1, final Object v2) {
        final Map<String, Object> v3 = /*EL:1795*/new HashMap<String, Object>(v1);
        /*SL:1796*/v3.put("enclosing-library", a3);
        /*SL:1797*/synchronized (Native.libraries) {
            Native.typeOptions.put(/*EL:1798*/a3, v3);
            /*SL:1799*/if (v2 != null) {
                Native.libraries.put(/*EL:1800*/a3, new WeakReference<Object>(v2));
            }
            /*SL:1806*/if (!a3.isInterface() && Library.class.isAssignableFrom(a3)) {
                final Class<?>[] interfaces;
                final Class<?>[] a4 = /*EL:1809*/interfaces = a3.getInterfaces();
                for (final Class<?> a5 : interfaces) {
                    /*SL:1810*/if (Library.class.isAssignableFrom(a5)) {
                        cacheOptions(/*EL:1811*/a5, v3, v2);
                        /*SL:1812*/break;
                    }
                }
            }
        }
        /*SL:1817*/return v3;
    }
    
    private static native long registerMethod(final Class<?> p0, final String p1, final String p2, final int[] p3, final long[] p4, final long[] p5, final int p6, final long p7, final long p8, final Method p9, final long p10, final int p11, final boolean p12, final ToNativeConverter[] p13, final FromNativeConverter p14, final String p15);
    
    private static NativeMapped fromNative(final Class<?> a1, final Object a2) {
        /*SL:1841*/return (NativeMapped)NativeMappedConverter.getInstance(a1).fromNative(a2, new FromNativeContext(a1));
    }
    
    private static NativeMapped fromNative(final Method a1, final Object a2) {
        final Class<?> v1 = /*EL:1845*/a1.getReturnType();
        /*SL:1846*/return (NativeMapped)NativeMappedConverter.getInstance(v1).fromNative(a2, new MethodResultContext(v1, null, null, a1));
    }
    
    private static Class<?> nativeType(final Class<?> a1) {
        /*SL:1850*/return NativeMappedConverter.getInstance(a1).nativeType();
    }
    
    private static Object toNative(final ToNativeConverter a1, final Object a2) {
        /*SL:1856*/return a1.toNative(a2, new ToNativeContext());
    }
    
    private static Object fromNative(final FromNativeConverter a1, final Object a2, final Method a3) {
        /*SL:1860*/return a1.fromNative(a2, new MethodResultContext(a3.getReturnType(), null, null, a3));
    }
    
    public static native long ffi_prep_cif(final int p0, final int p1, final long p2, final long p3);
    
    public static native void ffi_call(final long p0, final long p1, final long p2, final long p3);
    
    public static native long ffi_prep_closure(final long p0, final ffi_callback p1);
    
    public static native void ffi_free_closure(final long p0);
    
    static native int initialize_ffi_type(final long p0);
    
    public static void main(final String[] a1) {
        final String v1 = /*EL:1879*/"Java Native Access (JNA)";
        final String v2 = /*EL:1880*/"4.4.0";
        final String v3 = /*EL:1881*/"4.4.0 (package information missing)";
        final Package v4 = /*EL:1882*/Native.class.getPackage();
        String v5 = /*EL:1883*/(v4 != null) ? v4.getSpecificationTitle() : /*EL:1884*/"Java Native Access (JNA)";
        /*SL:1885*/if (v5 == null) {
            v5 = "Java Native Access (JNA)";
        }
        String v6 = /*EL:1886*/(v4 != null) ? v4.getSpecificationVersion() : /*EL:1887*/"4.4.0";
        /*SL:1888*/if (v6 == null) {
            v6 = "4.4.0";
        }
        /*SL:1889*/v5 = v5 + " API Version " + v6;
        System.out.println(/*EL:1890*/v5);
        /*SL:1892*/v6 = ((v4 != null) ? v4.getImplementationVersion() : "4.4.0 (package information missing)");
        /*SL:1893*/if (v6 == null) {
            v6 = "4.4.0 (package information missing)";
        }
        System.out.println(/*EL:1894*/"Version: " + v6);
        System.out.println(/*EL:1895*/" Native: " + getNativeVersion() + " (" + getAPIChecksum() + /*EL:1896*/")");
        System.out.println(/*EL:1897*/" Prefix: " + Platform.RESOURCE_PREFIX);
    }
    
    static synchronized native void freeNativeCallback(final long p0);
    
    static synchronized native long createNativeCallback(final Callback p0, final Method p1, final Class<?>[] p2, final Class<?> p3, final int p4, final int p5, final String p6);
    
    static native int invokeInt(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static native long invokeLong(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static native void invokeVoid(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static native float invokeFloat(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static native double invokeDouble(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static native long invokePointer(final Function p0, final long p1, final int p2, final Object[] p3);
    
    private static native void invokeStructure(final Function p0, final long p1, final int p2, final Object[] p3, final long p4, final long p5);
    
    static Structure invokeStructure(final Function a1, final long a2, final int a3, final Object[] a4, final Structure a5) {
        invokeStructure(/*EL:2022*/a1, a2, a3, a4, a5.getPointer().peer, a5.getTypeInfo().peer);
        /*SL:2024*/return a5;
    }
    
    static native Object invokeObject(final Function p0, final long p1, final int p2, final Object[] p3);
    
    static long open(final String a1) {
        /*SL:2042*/return open(a1, -1);
    }
    
    static native long open(final String p0, final int p1);
    
    static native void close(final long p0);
    
    static native long findSymbol(final long p0, final String p1);
    
    static native long indexOf(final Pointer p0, final long p1, final long p2, final byte p3);
    
    static native void read(final Pointer p0, final long p1, final long p2, final byte[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final short[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final char[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final int[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final long[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final float[] p3, final int p4, final int p5);
    
    static native void read(final Pointer p0, final long p1, final long p2, final double[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final byte[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final short[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final char[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final int[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final long[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final float[] p3, final int p4, final int p5);
    
    static native void write(final Pointer p0, final long p1, final long p2, final double[] p3, final int p4, final int p5);
    
    static native byte getByte(final Pointer p0, final long p1, final long p2);
    
    static native char getChar(final Pointer p0, final long p1, final long p2);
    
    static native short getShort(final Pointer p0, final long p1, final long p2);
    
    static native int getInt(final Pointer p0, final long p1, final long p2);
    
    static native long getLong(final Pointer p0, final long p1, final long p2);
    
    static native float getFloat(final Pointer p0, final long p1, final long p2);
    
    static native double getDouble(final Pointer p0, final long p1, final long p2);
    
    static Pointer getPointer(final long a1) {
        final long v1 = _getPointer(/*EL:2131*/a1);
        /*SL:2132*/return (v1 == 0L) ? null : new Pointer(v1);
    }
    
    private static native long _getPointer(final long p0);
    
    static native String getWideString(final Pointer p0, final long p1, final long p2);
    
    static String getString(final Pointer a1, final long a2) {
        /*SL:2140*/return getString(a1, a2, getDefaultStringEncoding());
    }
    
    static String getString(final Pointer a1, final long a2, final String a3) {
        final byte[] v1 = getStringBytes(/*EL:2144*/a1, a1.peer, a2);
        /*SL:2145*/if (a3 != null) {
            try {
                /*SL:2147*/return new String(v1, a3);
            }
            catch (UnsupportedEncodingException ex) {}
        }
        /*SL:2152*/return new String(v1);
    }
    
    static native byte[] getStringBytes(final Pointer p0, final long p1, final long p2);
    
    static native void setMemory(final Pointer p0, final long p1, final long p2, final long p3, final byte p4);
    
    static native void setByte(final Pointer p0, final long p1, final long p2, final byte p3);
    
    static native void setShort(final Pointer p0, final long p1, final long p2, final short p3);
    
    static native void setChar(final Pointer p0, final long p1, final long p2, final char p3);
    
    static native void setInt(final Pointer p0, final long p1, final long p2, final int p3);
    
    static native void setLong(final Pointer p0, final long p1, final long p2, final long p3);
    
    static native void setFloat(final Pointer p0, final long p1, final long p2, final float p3);
    
    static native void setDouble(final Pointer p0, final long p1, final long p2, final double p3);
    
    static native void setPointer(final Pointer p0, final long p1, final long p2, final long p3);
    
    static native void setWideString(final Pointer p0, final long p1, final long p2, final String p3);
    
    static native ByteBuffer getDirectByteBuffer(final Pointer p0, final long p1, final long p2, final long p3);
    
    public static native long malloc(final long p0);
    
    public static native void free(final long p0);
    
    @Deprecated
    public static native ByteBuffer getDirectByteBuffer(final long p0, final long p1);
    
    public static void detach(final boolean v-1) {
        final Thread v0 = /*EL:2229*/Thread.currentThread();
        /*SL:2230*/if (v-1) {
            Native.nativeThreads.remove(/*EL:2237*/v0);
            final Pointer a1 = Native.nativeThreadTerminationFlag.get();
            setDetachState(/*EL:2239*/true, 0L);
        }
        else/*SL:2242*/ if (!Native.nativeThreads.containsKey(v0)) {
            final Pointer v = Native.nativeThreadTerminationFlag.get();
            Native.nativeThreads.put(/*EL:2244*/v0, v);
            setDetachState(/*EL:2245*/false, v.peer);
        }
    }
    
    static Pointer getTerminationFlag(final Thread a1) {
        /*SL:2251*/return Native.nativeThreads.get(a1);
    }
    
    private static native void setDetachState(final boolean p0, final long p1);
    
    static {
        DEFAULT_ENCODING = Charset.defaultCharset().name();
        Native.DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
        Native.DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
        Native.jnidispatchPath = null;
        typeOptions = new WeakHashMap<Class<?>, Map<String, Object>>();
        libraries = new WeakHashMap<Class<?>, Reference<?>>();
        DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Callback a1, final Throwable a2) {
                System.err.println(/*EL:123*/"JNA: Callback " + a1 + " threw the following exception:");
                /*SL:124*/a2.printStackTrace();
            }
        };
        Native.callbackExceptionHandler = Native.DEFAULT_HANDLER;
        loadNativeDispatchLibrary();
        if (!isCompatibleVersion("5.1.0", getNativeVersion())) {
            final String v1 = System.getProperty("line.separator");
            throw new Error(v1 + v1 + "There is an incompatible JNA native library installed on this system" + v1 + "Expected: " + "5.1.0" + v1 + "Found:    " + getNativeVersion() + v1 + ((Native.jnidispatchPath != null) ? ("(at " + Native.jnidispatchPath + ")") : System.getProperty("java.library.path")) + "." + v1 + "To resolve this issue you may do one of the following:" + v1 + " - remove or uninstall the offending library" + v1 + " - set the system property jna.nosys=true" + v1 + " - set jna.boot.library.path to include the path to the version of the " + v1 + "   jnidispatch library included with the JNA jar file you are using" + v1);
        }
        POINTER_SIZE = sizeof(0);
        LONG_SIZE = sizeof(1);
        WCHAR_SIZE = sizeof(2);
        SIZE_T_SIZE = sizeof(3);
        BOOL_SIZE = sizeof(4);
        initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            setProtected(true);
        }
        MAX_ALIGNMENT = ((Platform.isSPARC() || Platform.isWindows() || (Platform.isLinux() && (Platform.isARM() || Platform.isPPC())) || Platform.isAIX() || Platform.isAndroid()) ? 8 : Native.LONG_SIZE);
        MAX_PADDING = ((Platform.isMac() && Platform.isPPC()) ? 8 : Native.MAX_ALIGNMENT);
        System.setProperty("jna.loaded", "true");
        finalizer = new Object() {
            @Override
            protected void finalize() {
                /*SL:233*/dispose();
            }
        };
        Native.registeredClasses = new WeakHashMap<Class<?>, long[]>();
        Native.registeredLibraries = new WeakHashMap<Class<?>, NativeLibrary>();
        nativeThreadTerminationFlag = new ThreadLocal<Memory>() {
            @Override
            protected Memory initialValue() {
                final Memory v1 = /*EL:2210*/new Memory(4L);
                /*SL:2211*/v1.clear();
                /*SL:2212*/return v1;
            }
        };
        nativeThreads = Collections.<Thread, Pointer>synchronizedMap(new WeakHashMap<Thread, Pointer>());
    }
    
    private static class Buffers
    {
        static boolean isBuffer(final Class<?> a1) {
            /*SL:2258*/return Buffer.class.isAssignableFrom(a1);
        }
    }
    
    private static class AWT
    {
        static long getWindowID(final Window a1) throws HeadlessException {
            /*SL:2267*/return getComponentID(a1);
        }
        
        static long getComponentID(final Object a1) throws HeadlessException {
            /*SL:2272*/if (GraphicsEnvironment.isHeadless()) {
                /*SL:2273*/throw new HeadlessException("No native windows when headless");
            }
            final Component v1 = /*EL:2275*/(Component)a1;
            /*SL:2276*/if (v1.isLightweight()) {
                /*SL:2277*/throw new IllegalArgumentException("Component must be heavyweight");
            }
            /*SL:2279*/if (!v1.isDisplayable()) {
                /*SL:2280*/throw new IllegalStateException("Component must be displayable");
            }
            /*SL:2283*/if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && /*EL:2284*/!v1.isVisible()) {
                /*SL:2285*/throw new IllegalStateException("Component must be visible");
            }
            /*SL:2291*/return Native.getWindowHandle0(v1);
        }
    }
    
    public interface ffi_callback
    {
        void invoke(long p0, long p1, long p2);
    }
}
