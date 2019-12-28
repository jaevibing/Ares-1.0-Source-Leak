package com.sun.jna;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.lang.ref.Reference;
import java.util.Map;

public class NativeLibrary
{
    private long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map<String, Function> functions;
    final int callFlags;
    private String encoding;
    final Map<String, ?> options;
    private static final Map<String, Reference<NativeLibrary>> libraries;
    private static final Map<String, List<String>> searchPaths;
    private static final List<String> librarySearchPath;
    private static final int DEFAULT_OPEN_OPTIONS = -1;
    
    private static String functionKey(final String a1, final int a2, final String a3) {
        /*SL:101*/return a1 + "|" + a2 + "|" + a3;
    }
    
    private NativeLibrary(final String a3, final String a4, final long v1, final Map<String, ?> v3) {
        this.functions = new HashMap<String, Function>();
        this.libraryName = this.getLibraryName(a3);
        this.libraryPath = a4;
        this.handle = v1;
        final Object v4 = v3.get("calling-convention");
        final int v5 = (v4 instanceof Number) ? ((Number)v4).intValue() : 0;
        this.callFlags = v5;
        this.options = v3;
        this.encoding = (String)v3.get("string-encoding");
        if (this.encoding == null) {
            this.encoding = Native.getDefaultStringEncoding();
        }
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            synchronized (this.functions) {
                final Function a5 = new Function(this, "GetLastError", 63, this.encoding) {
                    @Override
                    Object invoke(final Object[] a1, final Class<?> a2, final boolean a3, final int a4) {
                        /*SL:124*/return Native.getLastError();
                    }
                    
                    @Override
                    Object invoke(final Method a1, final Class<?>[] a2, final Class<?> a3, final Object[] a4, final Map<String, ?> a5) {
                        /*SL:129*/return Native.getLastError();
                    }
                };
                this.functions.put(functionKey("GetLastError", this.callFlags, this.encoding), a5);
            }
        }
    }
    
    private static int openFlags(final Map<String, ?> a1) {
        final Object v1 = /*EL:139*/a1.get("open-flags");
        /*SL:140*/if (v1 instanceof Number) {
            /*SL:141*/return ((Number)v1).intValue();
        }
        /*SL:143*/return -1;
    }
    
    private static NativeLibrary loadLibrary(final String v-10, final Map<String, ?> v-9) {
        /*SL:147*/if (Native.DEBUG_LOAD) {
            System.out.println(/*EL:148*/"Looking for library '" + v-10 + "'");
        }
        final boolean absolute = /*EL:151*/new File(v-10).isAbsolute();
        final List<String> list = /*EL:152*/new ArrayList<String>();
        final int openFlags = openFlags(/*EL:153*/v-9);
        final String webStartLibraryPath = /*EL:157*/Native.getWebStartLibraryPath(v-10);
        /*SL:158*/if (webStartLibraryPath != null) {
            /*SL:159*/if (Native.DEBUG_LOAD) {
                System.out.println(/*EL:160*/"Adding web start path " + webStartLibraryPath);
            }
            /*SL:162*/list.add(webStartLibraryPath);
        }
        final List<String> list2 = NativeLibrary.searchPaths.get(/*EL:168*/v-10);
        /*SL:169*/if (list2 != null) {
            /*SL:170*/synchronized (list2) {
                /*SL:171*/list.addAll(0, list2);
            }
        }
        /*SL:175*/if (Native.DEBUG_LOAD) {
            System.out.println(/*EL:176*/"Adding paths from jna.library.path: " + System.getProperty("jna.library.path"));
        }
        /*SL:179*/list.addAll(initPaths("jna.library.path"));
        String a3 = findLibraryPath(/*EL:180*/v-10, list);
        long v4 = /*EL:181*/0L;
        try {
            /*SL:188*/if (Native.DEBUG_LOAD) {
                System.out.println(/*EL:189*/"Trying " + a3);
            }
            /*SL:191*/v4 = Native.open(a3, openFlags);
        }
        catch (UnsatisfiedLinkError a2) {
            /*SL:194*/if (Native.DEBUG_LOAD) {
                System.out.println(/*EL:195*/"Adding system paths: " + NativeLibrary.librarySearchPath);
            }
            /*SL:197*/list.addAll(NativeLibrary.librarySearchPath);
        }
        try {
            /*SL:201*/if (v4 == 0L) {
                /*SL:202*/a3 = findLibraryPath(v-10, list);
                /*SL:203*/if (Native.DEBUG_LOAD) {
                    System.out.println(/*EL:204*/"Trying " + a3);
                }
                /*SL:206*/v4 = Native.open(a3, openFlags);
                /*SL:207*/if (v4 == 0L) {
                    /*SL:208*/throw new UnsatisfiedLinkError("Failed to load library '" + v-10 + "'");
                }
            }
        }
        catch (UnsatisfiedLinkError v0) {
            /*SL:215*/if (Platform.isAndroid()) {
                try {
                    /*SL:217*/if (Native.DEBUG_LOAD) {
                        System.out.println(/*EL:218*/"Preload (via System.loadLibrary) " + v-10);
                    }
                    /*SL:220*/System.loadLibrary(v-10);
                    /*SL:221*/v4 = Native.open(a3, openFlags);
                }
                catch (UnsatisfiedLinkError a2) {
                    /*SL:224*/v0 = a2;
                }
            }
            else/*SL:227*/ if (Platform.isLinux() || Platform.isFreeBSD()) {
                /*SL:231*/if (Native.DEBUG_LOAD) {
                    System.out.println(/*EL:232*/"Looking for version variants");
                }
                /*SL:234*/a3 = matchLibrary(v-10, list);
                /*SL:235*/if (a3 != null) {
                    /*SL:236*/if (Native.DEBUG_LOAD) {
                        System.out.println(/*EL:237*/"Trying " + a3);
                    }
                    try {
                        /*SL:240*/v4 = Native.open(a3, openFlags);
                    }
                    catch (UnsatisfiedLinkError v) {
                        /*SL:243*/v0 = v;
                    }
                }
            }
            else/*SL:248*/ if (Platform.isMac() && !v-10.endsWith(".dylib")) {
                /*SL:249*/if (Native.DEBUG_LOAD) {
                    System.out.println(/*EL:250*/"Looking for matching frameworks");
                }
                /*SL:252*/a3 = matchFramework(v-10);
                /*SL:253*/if (a3 != null) {
                    try {
                        /*SL:255*/if (Native.DEBUG_LOAD) {
                            System.out.println(/*EL:256*/"Trying " + a3);
                        }
                        /*SL:258*/v4 = Native.open(a3, openFlags);
                    }
                    catch (UnsatisfiedLinkError v) {
                        /*SL:261*/v0 = v;
                    }
                }
            }
            else/*SL:266*/ if (Platform.isWindows() && !absolute) {
                /*SL:267*/if (Native.DEBUG_LOAD) {
                    System.out.println(/*EL:268*/"Looking for lib- prefix");
                }
                /*SL:270*/a3 = findLibraryPath("lib" + v-10, list);
                /*SL:271*/if (a3 != null) {
                    /*SL:272*/if (Native.DEBUG_LOAD) {
                        System.out.println(/*EL:273*/"Trying " + a3);
                    }
                    try {
                        /*SL:276*/v4 = Native.open(a3, openFlags);
                    }
                    catch (UnsatisfiedLinkError v) {
                        /*SL:278*/v0 = v;
                    }
                }
            }
            /*SL:284*/if (v4 == 0L) {
                try {
                    final File v2 = /*EL:286*/Native.extractFromResourcePath(v-10, (ClassLoader)v-9.get("classloader"));
                    try {
                        /*SL:288*/v4 = Native.open(v2.getAbsolutePath(), openFlags);
                        /*SL:289*/a3 = v2.getAbsolutePath();
                    }
                    finally {
                        /*SL:292*/if (Native.isUnpacked(v2)) {
                            /*SL:293*/Native.deleteLibrary(v2);
                        }
                    }
                }
                catch (IOException v3) {
                    /*SL:298*/v0 = new UnsatisfiedLinkError(v3.getMessage());
                }
            }
            /*SL:302*/if (v4 == 0L) {
                /*SL:303*/throw new UnsatisfiedLinkError("Unable to load library '" + v-10 + "': " + v0.getMessage());
            }
        }
        /*SL:307*/if (Native.DEBUG_LOAD) {
            System.out.println(/*EL:308*/"Found library '" + v-10 + "' at " + a3);
        }
        /*SL:310*/return new NativeLibrary(v-10, a3, v4, v-9);
    }
    
    static String matchFramework(final String v-3) {
        File file = /*EL:315*/new File(v-3);
        /*SL:316*/if (file.isAbsolute()) {
            /*SL:317*/if (v-3.indexOf(".framework") != -1 && file.exists()) {
                /*SL:319*/return file.getAbsolutePath();
            }
            /*SL:321*/file = new File(new File(file.getParentFile(), file.getName() + ".framework"), file.getName());
            /*SL:322*/if (file.exists()) {
                /*SL:323*/return file.getAbsolutePath();
            }
        }
        else {
            final String[] array = /*EL:327*/{ System.getProperty("user.home"), "", "/System" };
            final String v0 = /*EL:328*/(v-3.indexOf(".framework") == -1) ? (v-3 + ".framework/" + v-3) : v-3;
            /*SL:330*/for (int v = 0; v < array.length; ++v) {
                final String a1 = /*EL:331*/array[v] + "/Library/Frameworks/" + v0;
                /*SL:332*/if (new File(a1).exists()) {
                    /*SL:333*/return a1;
                }
            }
        }
        /*SL:337*/return null;
    }
    
    private String getLibraryName(final String a1) {
        String v1 = /*EL:341*/a1;
        final String v2 = /*EL:342*/"---";
        final String v3 = mapSharedLibraryName(/*EL:343*/"---");
        final int v4 = /*EL:344*/v3.indexOf("---");
        /*SL:345*/if (v4 > 0 && v1.startsWith(v3.substring(0, v4))) {
            /*SL:346*/v1 = v1.substring(v4);
        }
        final String v5 = /*EL:348*/v3.substring(v4 + "---".length());
        final int v6 = /*EL:349*/v1.indexOf(v5);
        /*SL:350*/if (v6 != -1) {
            /*SL:351*/v1 = v1.substring(0, v6);
        }
        /*SL:353*/return v1;
    }
    
    public static final NativeLibrary getInstance(final String a1) {
        /*SL:369*/return getInstance(a1, Collections.<String, ?>emptyMap());
    }
    
    public static final NativeLibrary getInstance(final String a1, final ClassLoader a2) {
        /*SL:389*/return getInstance(a1, Collections.<String, ClassLoader>singletonMap("classloader", a2));
    }
    
    public static final NativeLibrary getInstance(String v-4, final Map<String, ?> v-3) {
        final Map<String, Object> v-5 = /*EL:407*/new HashMap<String, Object>(v-3);
        /*SL:408*/if (v-5.get("calling-convention") == null) {
            /*SL:409*/v-5.put("calling-convention", 0);
        }
        /*SL:414*/if ((Platform.isLinux() || Platform.isFreeBSD() || Platform.isAIX()) && Platform.C_LIBRARY_NAME.equals(v-4)) {
            /*SL:416*/v-4 = null;
        }
        /*SL:418*/synchronized (NativeLibrary.libraries) {
            Reference<NativeLibrary> a2 = NativeLibrary.libraries.get(/*EL:419*/v-4 + v-5);
            NativeLibrary v1 = /*EL:420*/(a2 != null) ? a2.get() : null;
            /*SL:422*/if (v1 == null) {
                /*SL:423*/if (v-4 == null) {
                    /*SL:424*/v1 = new NativeLibrary("<process>", null, Native.open(null, openFlags(v-5)), v-5);
                }
                else {
                    /*SL:427*/v1 = loadLibrary(v-4, v-5);
                }
                /*SL:429*/a2 = new WeakReference<NativeLibrary>(v1);
                NativeLibrary.libraries.put(/*EL:430*/v1.getName() + v-5, a2);
                /*SL:431*/a2 = v1.getFile();
                /*SL:432*/if (a2 != null) {
                    NativeLibrary.libraries.put(/*EL:433*/a2.getAbsolutePath() + v-5, a2);
                    NativeLibrary.libraries.put(/*EL:434*/a2.getName() + v-5, a2);
                }
            }
            /*SL:437*/return v1;
        }
    }
    
    public static final synchronized NativeLibrary getProcess() {
        /*SL:448*/return getInstance(null);
    }
    
    public static final synchronized NativeLibrary getProcess(final Map<String, ?> a1) {
        /*SL:458*/return getInstance(null, a1);
    }
    
    public static final void addSearchPath(final String a2, final String v1) {
        /*SL:470*/synchronized (NativeLibrary.searchPaths) {
            List<String> a3 = NativeLibrary.searchPaths.get(/*EL:471*/a2);
            /*SL:472*/if (a3 == null) {
                /*SL:473*/a3 = Collections.<String>synchronizedList(new ArrayList<String>());
                NativeLibrary.searchPaths.put(/*EL:474*/a2, a3);
            }
            /*SL:477*/a3.add(v1);
        }
    }
    
    public Function getFunction(final String a1) {
        /*SL:493*/return this.getFunction(a1, this.callFlags);
    }
    
    Function getFunction(String v1, final Method v2) {
        final FunctionMapper v3 = /*EL:512*/(FunctionMapper)this.options.get("function-mapper");
        /*SL:513*/if (v3 != null) {
            /*SL:514*/v1 = v3.getFunctionName(this, v2);
        }
        final String v4 = /*EL:517*/System.getProperty("jna.profiler.prefix", "$$YJP$$");
        /*SL:518*/if (v1.startsWith(v4)) {
            /*SL:519*/v1 = v1.substring(v4.length());
        }
        int v5 = /*EL:521*/this.callFlags;
        final Class<?>[] v6 = /*EL:522*/v2.getExceptionTypes();
        /*SL:523*/for (int a1 = 0; a1 < v6.length; ++a1) {
            /*SL:524*/if (LastErrorException.class.isAssignableFrom(v6[a1])) {
                /*SL:525*/v5 |= 0x40;
            }
        }
        /*SL:528*/return this.getFunction(v1, v5);
    }
    
    public Function getFunction(final String a1, final int a2) {
        /*SL:542*/return this.getFunction(a1, a2, this.encoding);
    }
    
    public Function getFunction(final String v1, final int v2, final String v3) {
        /*SL:559*/if (v1 == null) {
            /*SL:560*/throw new NullPointerException("Function name may not be null");
        }
        /*SL:562*/synchronized (this.functions) {
            final String a1 = functionKey(/*EL:563*/v1, v2, v3);
            Function a2 = /*EL:564*/this.functions.get(a1);
            /*SL:565*/if (a2 == null) {
                /*SL:566*/a2 = new Function(this, v1, v2, v3);
                /*SL:567*/this.functions.put(a1, a2);
            }
            /*SL:569*/return a2;
        }
    }
    
    public Map<String, ?> getOptions() {
        /*SL:575*/return this.options;
    }
    
    public Pointer getGlobalVariableAddress(final String v2) {
        try {
            /*SL:585*/return new Pointer(this.getSymbolAddress(v2));
        }
        catch (UnsatisfiedLinkError a1) {
            /*SL:587*/throw new UnsatisfiedLinkError("Error looking up '" + v2 + "': " + a1.getMessage());
        }
    }
    
    long getSymbolAddress(final String a1) {
        /*SL:596*/if (this.handle == 0L) {
            /*SL:597*/throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        /*SL:599*/return Native.findSymbol(this.handle, a1);
    }
    
    @Override
    public String toString() {
        /*SL:604*/return "Native Library <" + this.libraryPath + "@" + this.handle + ">";
    }
    
    public String getName() {
        /*SL:608*/return this.libraryName;
    }
    
    public File getFile() {
        /*SL:615*/if (this.libraryPath == null) {
            /*SL:616*/return null;
        }
        /*SL:617*/return new File(this.libraryPath);
    }
    
    @Override
    protected void finalize() {
        /*SL:622*/this.dispose();
    }
    
    static void disposeAll() {
        final Set<Reference<NativeLibrary>> v1;
        /*SL:628*/synchronized (NativeLibrary.libraries) {
            /*SL:629*/v1 = new LinkedHashSet<Reference<NativeLibrary>>(NativeLibrary.libraries.values());
        }
        /*SL:631*/for (final Reference<NativeLibrary> v2 : v1) {
            final NativeLibrary v3 = /*EL:632*/v2.get();
            /*SL:633*/if (v3 != null) {
                /*SL:634*/v3.dispose();
            }
        }
    }
    
    public void dispose() {
        final Set<String> set = /*EL:641*/new HashSet<String>();
        /*SL:642*/synchronized (NativeLibrary.libraries) {
            /*SL:643*/for (final Map.Entry<String, Reference<NativeLibrary>> v0 : NativeLibrary.libraries.entrySet()) {
                final Reference<NativeLibrary> v = /*EL:644*/v0.getValue();
                /*SL:645*/if (v.get() == this) {
                    /*SL:646*/set.add(v0.getKey());
                }
            }
            /*SL:650*/for (final String v2 : set) {
                NativeLibrary.libraries.remove(/*EL:651*/v2);
            }
        }
        /*SL:655*/synchronized (this) {
            /*SL:656*/if (this.handle != 0L) {
                /*SL:657*/Native.close(this.handle);
                /*SL:658*/this.handle = 0L;
            }
        }
    }
    
    private static List<String> initPaths(final String v1) {
        final String v2 = /*EL:664*/System.getProperty(v1, "");
        /*SL:665*/if ("".equals(v2)) {
            /*SL:666*/return Collections.<String>emptyList();
        }
        final StringTokenizer v3 = /*EL:668*/new StringTokenizer(v2, File.pathSeparator);
        final List<String> v4 = /*EL:669*/new ArrayList<String>();
        /*SL:670*/while (v3.hasMoreTokens()) {
            final String a1 = /*EL:671*/v3.nextToken();
            /*SL:672*/if (!"".equals(a1)) {
                /*SL:673*/v4.add(a1);
            }
        }
        /*SL:676*/return v4;
    }
    
    private static String findLibraryPath(final String v1, final List<String> v2) {
        /*SL:685*/if (new File(v1).isAbsolute()) {
            /*SL:686*/return v1;
        }
        final String v3 = mapSharedLibraryName(/*EL:692*/v1);
        /*SL:695*/for (File a2 : v2) {
            /*SL:696*/a2 = new File(a2, v3);
            /*SL:697*/if (a2.exists()) {
                /*SL:698*/return a2.getAbsolutePath();
            }
            /*SL:700*/if (!Platform.isMac() || /*EL:703*/!v3.endsWith(".dylib")) {
                continue;
            }
            /*SL:704*/a2 = new File(a2, v3.substring(0, v3.lastIndexOf(".dylib")) + ".jnilib");
            /*SL:705*/if (a2.exists()) {
                /*SL:706*/return a2.getAbsolutePath();
            }
        }
        /*SL:716*/return v3;
    }
    
    static String mapSharedLibraryName(final String v1) {
        /*SL:724*/if (!Platform.isMac()) {
            /*SL:739*/if (Platform.isLinux() || Platform.isFreeBSD()) {
                /*SL:740*/if (isVersionedName(v1) || v1.endsWith(".so")) {
                    /*SL:742*/return v1;
                }
            }
            else/*SL:745*/ if (Platform.isAIX()) {
                /*SL:746*/if (v1.startsWith("lib")) {
                    /*SL:747*/return v1;
                }
            }
            else/*SL:750*/ if (Platform.isWindows() && /*EL:751*/(v1.endsWith(".drv") || v1.endsWith(".dll"))) {
                /*SL:752*/return v1;
            }
            /*SL:756*/return System.mapLibraryName(v1);
        }
        if (v1.startsWith("lib") && (v1.endsWith(".dylib") || v1.endsWith(".jnilib"))) {
            return v1;
        }
        final String a1 = System.mapLibraryName(v1);
        if (a1.endsWith(".jnilib")) {
            return a1.substring(0, a1.lastIndexOf(".jnilib")) + ".dylib";
        }
        return a1;
    }
    
    private static boolean isVersionedName(final String v-1) {
        /*SL:760*/if (v-1.startsWith("lib")) {
            final int v0 = /*EL:761*/v-1.lastIndexOf(".so.");
            /*SL:762*/if (v0 != -1 && v0 + 4 < v-1.length()) {
                /*SL:763*/for (int v = v0 + 4; v < v-1.length(); ++v) {
                    final char a1 = /*EL:764*/v-1.charAt(v);
                    /*SL:765*/if (!Character.isDigit(a1) && a1 != '.') {
                        /*SL:766*/return false;
                    }
                }
                /*SL:769*/return true;
            }
        }
        /*SL:772*/return false;
    }
    
    static String matchLibrary(final String v-9, List<String> v-8) {
        final File file = /*EL:781*/new File(v-9);
        /*SL:782*/if (file.isAbsolute()) {
            /*SL:783*/v-8 = Arrays.<String>asList(file.getParent());
        }
        final FilenameFilter filenameFilter = /*EL:785*/new FilenameFilter() {
            @Override
            public boolean accept(final File a1, final String a2) {
                /*SL:788*/return (a2.startsWith("lib" + v-9 + ".so") || /*EL:789*/(a2.startsWith(v-9 + ".so") && v-9.startsWith("lib"))) && isVersionedName(/*EL:790*/a2);
            }
        };
        final Collection<File> collection = /*EL:795*/new LinkedList<File>();
        /*SL:796*/for (File[] a2 : v-8) {
            /*SL:797*/a2 = new File(a2).listFiles(filenameFilter);
            /*SL:798*/if (a2 != null && a2.length > 0) {
                /*SL:799*/collection.addAll(Arrays.<File>asList(a2));
            }
        }
        double n = /*EL:806*/-1.0;
        String s = /*EL:807*/null;
        /*SL:808*/for (final File v0 : collection) {
            final String v = /*EL:809*/v0.getAbsolutePath();
            final String v2 = /*EL:810*/v.substring(v.lastIndexOf(".so.") + 4);
            final double v3 = parseVersion(/*EL:811*/v2);
            /*SL:812*/if (v3 > n) {
                /*SL:813*/n = v3;
                /*SL:814*/s = v;
            }
        }
        /*SL:817*/return s;
    }
    
    static double parseVersion(String v-6) {
        double n = /*EL:821*/0.0;
        double n2 = /*EL:822*/1.0;
        int n3 = /*EL:823*/v-6.indexOf(".");
        /*SL:824*/while (v-6 != null) {
            final String v0;
            /*SL:826*/if (n3 != -1) {
                final String a1 = /*EL:827*/v-6.substring(0, n3);
                /*SL:828*/v-6 = v-6.substring(n3 + 1);
                /*SL:829*/n3 = v-6.indexOf(".");
            }
            else {
                /*SL:832*/v0 = v-6;
                /*SL:833*/v-6 = null;
            }
            try {
                /*SL:836*/n += Integer.parseInt(v0) / n2;
            }
            catch (NumberFormatException v) {
                /*SL:839*/return 0.0;
            }
            /*SL:841*/n2 *= 100.0;
        }
        /*SL:844*/return n;
    }
    
    private static String getMultiArchPath() {
        String v1 = Platform.ARCH;
        final String v2 = /*EL:935*/Platform.iskFreeBSD() ? "-kfreebsd" : /*EL:937*/(Platform.isGNU() ? "" : "-linux");
        String v3 = /*EL:938*/"-gnu";
        /*SL:940*/if (Platform.isIntel()) {
            /*SL:941*/v1 = (Platform.is64Bit() ? "x86_64" : "i386");
        }
        else/*SL:943*/ if (Platform.isPPC()) {
            /*SL:944*/v1 = (Platform.is64Bit() ? "powerpc64" : "powerpc");
        }
        else/*SL:946*/ if (Platform.isARM()) {
            /*SL:947*/v1 = "arm";
            /*SL:948*/v3 = "-gnueabi";
        }
        /*SL:951*/return v1 + v2 + v3;
    }
    
    private static ArrayList<String> getLinuxLdPaths() {
        final ArrayList<String> list = /*EL:958*/new ArrayList<String>();
        try {
            final Process exec = /*EL:960*/Runtime.getRuntime().exec("/sbin/ldconfig -p");
            final BufferedReader bufferedReader = /*EL:961*/new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = /*EL:962*/"";
            /*SL:963*/while ((line = bufferedReader.readLine()) != null) {
                final int index = /*EL:964*/line.indexOf(" => ");
                final int v0 = /*EL:965*/line.lastIndexOf(47);
                /*SL:966*/if (index != -1 && v0 != -1 && index < v0) {
                    final String v = /*EL:967*/line.substring(index + 4, v0);
                    /*SL:968*/if (list.contains(v)) {
                        continue;
                    }
                    /*SL:969*/list.add(v);
                }
            }
            /*SL:973*/bufferedReader.close();
        }
        catch (Exception ex) {}
        /*SL:976*/return list;
    }
    
    static {
        libraries = new HashMap<String, Reference<NativeLibrary>>();
        searchPaths = Collections.<String, List<String>>synchronizedMap(new HashMap<String, List<String>>());
        librarySearchPath = new ArrayList<String>();
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        final String webStartLibraryPath = Native.getWebStartLibraryPath("jnidispatch");
        if (webStartLibraryPath != null) {
            NativeLibrary.librarySearchPath.add(webStartLibraryPath);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String string = "";
            String pathSeparator = "";
            String string2 = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD() || Platform.iskFreeBSD()) {
                string2 = (Platform.isSolaris() ? "/" : "") + Pointer.SIZE * 8;
            }
            String[] v0 = { "/usr/lib" + string2, "/lib" + string2, "/usr/lib", "/lib" };
            if (Platform.isLinux() || Platform.iskFreeBSD() || Platform.isGNU()) {
                final String v = getMultiArchPath();
                v0 = new String[] { "/usr/lib/" + v, "/lib/" + v, "/usr/lib" + string2, "/lib" + string2, "/usr/lib", "/lib" };
            }
            if (Platform.isLinux()) {
                final ArrayList<String> v2 = getLinuxLdPaths();
                for (int v3 = v0.length - 1; 0 <= v3; --v3) {
                    final int v4 = v2.indexOf(v0[v3]);
                    if (v4 != -1) {
                        v2.remove(v4);
                    }
                    v2.add(0, v0[v3]);
                }
                v0 = v2.<String>toArray(new String[v2.size()]);
            }
            for (int v5 = 0; v5 < v0.length; ++v5) {
                final File v6 = new File(v0[v5]);
                if (v6.exists() && v6.isDirectory()) {
                    string = string + pathSeparator + v0[v5];
                    pathSeparator = File.pathSeparator;
                }
            }
            if (!"".equals(string)) {
                System.setProperty("jna.platform.library.path", string);
            }
        }
        NativeLibrary.librarySearchPath.addAll(initPaths("jna.platform.library.path"));
    }
}
