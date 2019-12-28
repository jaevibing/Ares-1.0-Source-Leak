package com.sun.jna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

public final class Platform
{
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final int AIX = 7;
    public static final int ANDROID = 8;
    public static final int GNU = 9;
    public static final int KFREEBSD = 10;
    public static final int NETBSD = 11;
    public static final boolean RO_FIELDS;
    public static final boolean HAS_BUFFERS;
    public static final boolean HAS_AWT;
    public static final boolean HAS_JAWT;
    public static final String MATH_LIBRARY_NAME;
    public static final String C_LIBRARY_NAME;
    public static final boolean HAS_DLL_CALLBACKS;
    public static final String RESOURCE_PREFIX;
    private static final int osType;
    public static final String ARCH;
    
    public static final int getOSType() {
        /*SL:138*/return Platform.osType;
    }
    
    public static final boolean isMac() {
        /*SL:141*/return Platform.osType == 0;
    }
    
    public static final boolean isAndroid() {
        /*SL:144*/return Platform.osType == 8;
    }
    
    public static final boolean isLinux() {
        /*SL:147*/return Platform.osType == 1;
    }
    
    public static final boolean isAIX() {
        /*SL:150*/return Platform.osType == 7;
    }
    
    public static final boolean isAix() {
        /*SL:154*/return isAIX();
    }
    
    public static final boolean isWindowsCE() {
        /*SL:157*/return Platform.osType == 6;
    }
    
    public static final boolean isWindows() {
        /*SL:161*/return Platform.osType == 2 || Platform.osType == 6;
    }
    
    public static final boolean isSolaris() {
        /*SL:164*/return Platform.osType == 3;
    }
    
    public static final boolean isFreeBSD() {
        /*SL:167*/return Platform.osType == 4;
    }
    
    public static final boolean isOpenBSD() {
        /*SL:170*/return Platform.osType == 5;
    }
    
    public static final boolean isNetBSD() {
        /*SL:173*/return Platform.osType == 11;
    }
    
    public static final boolean isGNU() {
        /*SL:176*/return Platform.osType == 9;
    }
    
    public static final boolean iskFreeBSD() {
        /*SL:179*/return Platform.osType == 10;
    }
    
    public static final boolean isX11() {
        /*SL:183*/return !isWindows() && !isMac();
    }
    
    public static final boolean hasRuntimeExec() {
        /*SL:186*/return !isWindowsCE() || !"J9".equals(System.getProperty("java.vm.name"));
    }
    
    public static final boolean is64Bit() {
        final String v1 = /*EL:191*/System.getProperty("sun.arch.data.model", /*EL:192*/System.getProperty("com.ibm.vm.bitmode"));
        /*SL:193*/if (v1 != null) {
            /*SL:194*/return "64".equals(v1);
        }
        /*SL:199*/return "x86-64".equals(Platform.ARCH) || "ia64".equals(Platform.ARCH) || "ppc64".equals(Platform.ARCH) || "ppc64le".equals(Platform.ARCH) || "sparcv9".equals(Platform.ARCH) || "amd64".equals(Platform.ARCH) || Native.POINTER_SIZE == /*EL:203*/8;
    }
    
    public static final boolean isIntel() {
        /*SL:207*/return Platform.ARCH.startsWith("x86");
    }
    
    public static final boolean isPPC() {
        /*SL:214*/return Platform.ARCH.startsWith("ppc");
    }
    
    public static final boolean isARM() {
        /*SL:221*/return Platform.ARCH.startsWith("arm");
    }
    
    public static final boolean isSPARC() {
        /*SL:225*/return Platform.ARCH.startsWith("sparc");
    }
    
    static String getCanonicalArchitecture(String a1, final boolean a2) {
        /*SL:229*/a1 = a1.toLowerCase().trim();
        /*SL:230*/if ("powerpc".equals(a1)) {
            /*SL:231*/a1 = "ppc";
        }
        else/*SL:233*/ if ("powerpc64".equals(a1)) {
            /*SL:234*/a1 = "ppc64";
        }
        else/*SL:236*/ if ("i386".equals(a1) || "i686".equals(a1)) {
            /*SL:237*/a1 = "x86";
        }
        else/*SL:239*/ if ("x86_64".equals(a1) || "amd64".equals(a1)) {
            /*SL:240*/a1 = "x86-64";
        }
        /*SL:244*/if ("ppc64".equals(a1) && "little".equals(System.getProperty("sun.cpu.endian"))) {
            /*SL:245*/a1 = "ppc64le";
        }
        /*SL:248*/if ("arm".equals(a1) && a2) {
            /*SL:249*/a1 = "armel";
        }
        /*SL:253*/return a1;
    }
    
    private static boolean isSoftFloat() {
        try {
            final File v1 = /*EL:258*/new File("/proc/self/exe");
            final ELFAnalyser v2 = /*EL:259*/ELFAnalyser.analyse(v1.getCanonicalPath());
            /*SL:260*/return v2.isArmSoftFloat();
        }
        catch (IOException v3) {
            /*SL:263*/Logger.getLogger(Platform.class.getName()).log(Level.FINE, null, v3);
            /*SL:265*/return false;
        }
    }
    
    static String getNativeLibraryResourcePrefix() {
        final String v1 = /*EL:272*/System.getProperty("jna.prefix");
        /*SL:273*/if (v1 != null) {
            /*SL:274*/return v1;
        }
        /*SL:276*/return getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
    }
    
    static String getNativeLibraryResourcePrefix(final int a1, final String a2, final String a3) {
        /*SL:287*/return getNativeLibraryResourcePrefix(a1, a2, a3, isSoftFloat());
    }
    
    static String getNativeLibraryResourcePrefix(final int v-3, String v-2, final String v-1, final boolean v0) {
        /*SL:292*/v-2 = getCanonicalArchitecture(v-2, v0);
        String v = null;
        /*SL:293*/switch (v-3) {
            case 8: {
                /*SL:295*/if (v-2.startsWith("arm")) {
                    /*SL:296*/v-2 = "arm";
                }
                final String a1 = /*EL:298*/"android-" + v-2;
                /*SL:299*/break;
            }
            case 2: {
                final String a2 = /*EL:301*/"win32-" + v-2;
                /*SL:302*/break;
            }
            case 6: {
                final String a3 = /*EL:304*/"w32ce-" + v-2;
                /*SL:305*/break;
            }
            case 0: {
                final String a4 = /*EL:307*/"darwin";
                /*SL:308*/break;
            }
            case 1: {
                /*SL:310*/v = "linux-" + v-2;
                /*SL:311*/break;
            }
            case 3: {
                /*SL:313*/v = "sunos-" + v-2;
                /*SL:314*/break;
            }
            case 4: {
                /*SL:316*/v = "freebsd-" + v-2;
                /*SL:317*/break;
            }
            case 5: {
                /*SL:319*/v = "openbsd-" + v-2;
                /*SL:320*/break;
            }
            case 11: {
                /*SL:322*/v = "netbsd-" + v-2;
                /*SL:323*/break;
            }
            case 10: {
                /*SL:325*/v = "kfreebsd-" + v-2;
                /*SL:326*/break;
            }
            default: {
                /*SL:328*/v = v-1.toLowerCase();
                final int v2 = /*EL:329*/v.indexOf(" ");
                /*SL:330*/if (v2 != -1) {
                    /*SL:331*/v = v.substring(0, v2);
                }
                /*SL:333*/v = v + "-" + v-2;
                break;
            }
        }
        /*SL:336*/return v;
    }
    
    static {
        final String v1 = System.getProperty("os.name");
        if (v1.startsWith("Linux")) {
            if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
                osType = 8;
                System.setProperty("jna.nounpack", "true");
            }
            else {
                osType = 1;
            }
        }
        else if (v1.startsWith("AIX")) {
            osType = 7;
        }
        else if (v1.startsWith("Mac") || v1.startsWith("Darwin")) {
            osType = 0;
        }
        else if (v1.startsWith("Windows CE")) {
            osType = 6;
        }
        else if (v1.startsWith("Windows")) {
            osType = 2;
        }
        else if (v1.startsWith("Solaris") || v1.startsWith("SunOS")) {
            osType = 3;
        }
        else if (v1.startsWith("FreeBSD")) {
            osType = 4;
        }
        else if (v1.startsWith("OpenBSD")) {
            osType = 5;
        }
        else if (v1.equalsIgnoreCase("gnu")) {
            osType = 9;
        }
        else if (v1.equalsIgnoreCase("gnu/kfreebsd")) {
            osType = 10;
        }
        else if (v1.equalsIgnoreCase("netbsd")) {
            osType = 11;
        }
        else {
            osType = -1;
        }
        boolean v2 = false;
        try {
            Class.forName("java.nio.Buffer");
            v2 = true;
        }
        catch (ClassNotFoundException ex) {}
        HAS_AWT = (Platform.osType != 6 && Platform.osType != 8 && Platform.osType != 7);
        HAS_JAWT = (Platform.HAS_AWT && Platform.osType != 0);
        HAS_BUFFERS = v2;
        RO_FIELDS = (Platform.osType != 6);
        C_LIBRARY_NAME = ((Platform.osType == 2) ? "msvcrt" : ((Platform.osType == 6) ? "coredll" : "c"));
        MATH_LIBRARY_NAME = ((Platform.osType == 2) ? "msvcrt" : ((Platform.osType == 6) ? "coredll" : "m"));
        HAS_DLL_CALLBACKS = (Platform.osType == 2);
        ARCH = getCanonicalArchitecture(System.getProperty("os.arch"), isSoftFloat());
        RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
    }
}
