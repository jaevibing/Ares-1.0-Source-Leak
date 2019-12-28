package com.google.api.client.util;

import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class IOUtils
{
    public static void copy(final InputStream a1, final OutputStream a2) throws IOException {
        copy(/*EL:63*/a1, a2, true);
    }
    
    public static void copy(final InputStream a1, final OutputStream a2, final boolean a3) throws IOException {
        try {
            /*SL:94*/ByteStreams.copy(a1, a2);
        }
        finally {
            /*SL:96*/if (a3) {
                /*SL:97*/a1.close();
            }
        }
    }
    
    public static long computeLength(final StreamingContent a1) throws IOException {
        final ByteCountingOutputStream v1 = /*EL:111*/new ByteCountingOutputStream();
        try {
            /*SL:113*/a1.writeTo(v1);
        }
        finally {
            /*SL:115*/v1.close();
        }
        /*SL:117*/return v1.count;
    }
    
    public static byte[] serialize(final Object a1) throws IOException {
        final ByteArrayOutputStream v1 = /*EL:127*/new ByteArrayOutputStream();
        serialize(/*EL:128*/a1, v1);
        /*SL:129*/return v1.toByteArray();
    }
    
    public static void serialize(final Object a1, final OutputStream a2) throws IOException {
        try {
            /*SL:141*/new ObjectOutputStream(a2).writeObject(a1);
        }
        finally {
            /*SL:143*/a2.close();
        }
    }
    
    public static <S extends java.lang.Object> S deserialize(final byte[] a1) throws IOException {
        /*SL:155*/if (a1 == null) {
            /*SL:156*/return null;
        }
        /*SL:158*/return (S)deserialize((InputStream)new ByteArrayInputStream(a1));
    }
    
    public static <S extends java.lang.Object> S deserialize(final InputStream v0) throws IOException {
        try {
            /*SL:171*/return (S)new ObjectInputStream(v0).readObject();
        }
        catch (ClassNotFoundException v) {
            final IOException a1 = /*EL:173*/new IOException("Failed to deserialize object");
            /*SL:174*/a1.initCause(v);
            /*SL:175*/throw a1;
        }
        finally {
            /*SL:177*/v0.close();
        }
    }
    
    public static boolean isSymbolicLink(final File v-1) throws IOException {
        try {
            final Class<?> a1 = /*EL:190*/Class.forName("java.nio.file.Files");
            final Class<?> v1 = /*EL:191*/Class.forName("java.nio.file.Path");
            final Object v2 = /*EL:192*/File.class.getMethod("toPath", (Class<?>[])new Class[0]).invoke(v-1, new Object[0]);
            /*SL:193*/return (boolean)a1.getMethod("isSymbolicLink", v1).invoke(null, v2);
        }
        catch (InvocationTargetException v4) {
            final Throwable v3 = /*EL:196*/v4.getCause();
            /*SL:197*/Throwables.<IOException>propagateIfPossible(v3, IOException.class);
            /*SL:199*/throw new RuntimeException(v3);
        }
        catch (ClassNotFoundException ex) {}
        catch (IllegalArgumentException ex2) {}
        catch (SecurityException ex3) {}
        catch (IllegalAccessException ex4) {}
        catch (NoSuchMethodException ex5) {}
        /*SL:213*/if (File.separatorChar == '\\') {
            /*SL:214*/return false;
        }
        File v5 = /*EL:216*/v-1;
        /*SL:217*/if (v-1.getParent() != null) {
            /*SL:218*/v5 = new File(v-1.getParentFile().getCanonicalFile(), v-1.getName());
        }
        /*SL:220*/return !v5.getCanonicalFile().equals(v5.getAbsoluteFile());
    }
}
