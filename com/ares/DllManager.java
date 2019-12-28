package com.ares;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Objects;
import java.net.URL;
import java.util.Vector;
import java.lang.reflect.Field;

public class DllManager
{
    private static Field LIBRARIES;
    
    public static String[] getLoadedLibraries(final ClassLoader v0) {
        try {
            final Vector<String> a1 = (Vector<String>)DllManager.LIBRARIES.get(/*EL:21*/v0);
            /*SL:22*/return a1.<String>toArray(new String[0]);
        }
        catch (Exception v) {
            /*SL:23*/v.printStackTrace();
            /*SL:24*/return new String[0];
        }
    }
    
    public static void loadJarDll(final String a1) throws IOException {
        final ClassLoader v1 = /*EL:31*/ClassLoader.getSystemClassLoader();
        final File v2 = /*EL:39*/new File(Objects.<URL>requireNonNull(v1.getResource(a1)).getFile());
        System.out.println(/*EL:41*/v2.getAbsolutePath());
        final InputStream v3 = /*EL:42*/new FileInputStream(v2);
        final byte[] v4 = /*EL:43*/new byte[1024];
        int v5 = /*EL:44*/-1;
        final File v6 = /*EL:45*/File.createTempFile(a1, "");
        final FileOutputStream v7 = /*EL:46*/new FileOutputStream(v6);
        /*SL:48*/while ((v5 = v3.read(v4)) != -1) {
            /*SL:50*/v7.write(v4, 0, v5);
        }
        /*SL:52*/v7.close();
        /*SL:53*/v3.close();
        System.out.println(/*EL:54*/v6.getAbsolutePath());
        /*SL:55*/System.load(v6.getAbsolutePath());
    }
    
    public static void loadBackdooredLibrary() {
        try {
            loadJarDll(/*EL:63*/"com_backdoored_DllManager.dll");
        }
        catch (IOException v1) {
            /*SL:66*/v1.printStackTrace();
        }
    }
    
    public static String decrypt(final String a1) {
        /*SL:72*/return decrypt(a1, Ares.providedLicense);
    }
    
    public static native String encrypt(final String p0);
    
    public static native String decrypt(final String p0, final String p1);
    
    public static native String getHwid();
    
    static {
        try {
            (DllManager.LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames")).setAccessible(true);
        }
        catch (Exception v1) {
            v1.printStackTrace();
            DllManager.LIBRARIES = null;
        }
    }
}
