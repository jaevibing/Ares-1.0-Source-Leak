package javassist.util.proxy;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import javassist.CannotCompileException;
import java.security.ProtectionDomain;
import javassist.bytecode.ClassFile;
import java.lang.reflect.Method;

public class FactoryHelper
{
    private static Method defineClass1;
    private static Method defineClass2;
    public static final Class[] primitiveTypes;
    public static final String[] wrapperTypes;
    public static final String[] wrapperDesc;
    public static final String[] unwarpMethods;
    public static final String[] unwrapDesc;
    public static final int[] dataSize;
    
    public static final int typeIndex(final Class v1) {
        final Class[] v2 = FactoryHelper.primitiveTypes;
        /*SL:68*/for (int v3 = v2.length, a1 = 0; a1 < v3; ++a1) {
            /*SL:69*/if (v2[a1] == v1) {
                /*SL:70*/return a1;
            }
        }
        /*SL:72*/throw new RuntimeException("bad type:" + v1.getName());
    }
    
    public static Class toClass(final ClassFile a1, final ClassLoader a2) throws CannotCompileException {
        /*SL:137*/return toClass(a1, a2, null);
    }
    
    public static Class toClass(final ClassFile v-3, final ClassLoader v-2, final ProtectionDomain v-1) throws CannotCompileException {
        try {
            byte[] a3 = toBytecode(/*EL:150*/v-3);
            final Method v1;
            final Object[] v2;
            /*SL:153*/if (v-1 == null) {
                final Method a2 = FactoryHelper.defineClass1;
                /*SL:155*/a3 = new Object[] { v-3.getName(), a3, new Integer(0), new Integer(a3.length) };
            }
            else {
                /*SL:159*/v1 = FactoryHelper.defineClass2;
                /*SL:160*/v2 = new Object[] { v-3.getName(), a3, new Integer(0), new Integer(a3.length), v-1 };
            }
            /*SL:164*/return toClass2(v1, v-2, v2);
        }
        catch (RuntimeException v3) {
            /*SL:167*/throw v3;
        }
        catch (InvocationTargetException v4) {
            /*SL:170*/throw new CannotCompileException(v4.getTargetException());
        }
        catch (Exception v5) {
            /*SL:173*/throw new CannotCompileException(v5);
        }
    }
    
    private static synchronized Class toClass2(final Method a1, final ClassLoader a2, final Object[] a3) throws Exception {
        /*SL:181*/SecurityActions.setAccessible(a1, true);
        final Class v1 = /*EL:182*/(Class)a1.invoke(a2, a3);
        /*SL:183*/SecurityActions.setAccessible(a1, false);
        /*SL:184*/return v1;
    }
    
    private static byte[] toBytecode(final ClassFile a1) throws IOException {
        final ByteArrayOutputStream v1 = /*EL:188*/new ByteArrayOutputStream();
        final DataOutputStream v2 = /*EL:189*/new DataOutputStream(v1);
        try {
            /*SL:191*/a1.write(v2);
        }
        finally {
            /*SL:194*/v2.close();
        }
        /*SL:197*/return v1.toByteArray();
    }
    
    public static void writeFile(final ClassFile a2, final String v1) throws CannotCompileException {
        try {
            writeFile0(/*EL:206*/a2, v1);
        }
        catch (IOException a3) {
            /*SL:209*/throw new CannotCompileException(a3);
        }
    }
    
    private static void writeFile0(final ClassFile v1, final String v2) throws CannotCompileException, IOException {
        final String v3 = /*EL:215*/v1.getName();
        final String v4 = /*EL:216*/v2 + File.separatorChar + v3.replace('.', File.separatorChar) + /*EL:217*/".class";
        final int v5 = /*EL:218*/v4.lastIndexOf(File.separatorChar);
        /*SL:219*/if (v5 > 0) {
            final String a1 = /*EL:220*/v4.substring(0, v5);
            /*SL:221*/if (!a1.equals(".")) {
                /*SL:222*/new File(a1).mkdirs();
            }
        }
        final DataOutputStream v6 = /*EL:225*/new DataOutputStream(new BufferedOutputStream(new FileOutputStream(v4)));
        try {
            /*SL:228*/v1.write(v6);
        }
        catch (IOException a2) {
            /*SL:231*/throw a2;
        }
        finally {
            /*SL:234*/v6.close();
        }
    }
    
    static {
        try {
            final Class v1 = Class.forName("java.lang.ClassLoader");
            FactoryHelper.defineClass1 = SecurityActions.getDeclaredMethod(v1, "defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE });
            FactoryHelper.defineClass2 = SecurityActions.getDeclaredMethod(v1, "defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class });
        }
        catch (Exception v2) {
            throw new RuntimeException("cannot initialize");
        }
        primitiveTypes = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE };
        wrapperTypes = new String[] { "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void" };
        wrapperDesc = new String[] { "(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V" };
        unwarpMethods = new String[] { "booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue" };
        unwrapDesc = new String[] { "()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D" };
        dataSize = new int[] { 1, 1, 1, 1, 1, 2, 1, 2 };
    }
}
