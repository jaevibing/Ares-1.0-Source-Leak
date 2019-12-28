package org.reflections.util;

import com.google.common.base.Joiner;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.IOException;
import org.reflections.Reflections;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.List;
import org.reflections.ReflectionsException;
import org.reflections.ReflectionUtils;
import java.util.ArrayList;
import java.lang.reflect.Member;
import java.io.File;

public abstract class Utils
{
    public static String repeat(final String a2, final int v1) {
        final StringBuilder v2 = /*EL:31*/new StringBuilder();
        /*SL:33*/for (int a3 = 0; a3 < v1; ++a3) {
            /*SL:34*/v2.append(a2);
        }
        /*SL:37*/return v2.toString();
    }
    
    public static boolean isEmpty(final String a1) {
        /*SL:44*/return a1 == null || a1.length() == 0;
    }
    
    public static boolean isEmpty(final Object[] a1) {
        /*SL:48*/return a1 == null || a1.length == 0;
    }
    
    public static File prepareFile(final String a1) {
        final File v1 = /*EL:52*/new File(a1);
        final File v2 = /*EL:53*/v1.getAbsoluteFile().getParentFile();
        /*SL:54*/if (!v2.exists()) {
            /*SL:56*/v2.mkdirs();
        }
        /*SL:58*/return v1;
    }
    
    public static Member getMemberFromDescriptor(final String v-9, final ClassLoader... v-8) throws ReflectionsException {
        final int lastIndex = /*EL:62*/v-9.lastIndexOf(40);
        final String s = /*EL:63*/(lastIndex != -1) ? v-9.substring(0, lastIndex) : v-9;
        final String a4 = /*EL:64*/(lastIndex != -1) ? v-9.substring(lastIndex + 1, v-9.lastIndexOf(41)) : "";
        final int max = /*EL:66*/Math.max(s.lastIndexOf(46), s.lastIndexOf("$"));
        final String substring = /*EL:67*/s.substring(s.lastIndexOf(32) + 1, max);
        final String substring2 = /*EL:68*/s.substring(max + 1);
        Class<?>[] array = /*EL:70*/null;
        /*SL:71*/if (!isEmpty(a4)) {
            String[] a2 = /*EL:72*/a4.split(",");
            final List<Class<?>> v1 = /*EL:73*/new ArrayList<Class<?>>(a2.length);
            final String[] array2 = /*EL:74*/a2;
            for (int length = array2.length, i = 0; i < length; ++i) {
                a2 = array2[i];
                /*SL:75*/v1.add(ReflectionUtils.forName(a2.trim(), v-8));
            }
            /*SL:77*/array = v1.<Class<?>>toArray(new Class[v1.size()]);
        }
        Class<?> a3 = /*EL:80*/ReflectionUtils.forName(substring, v-8);
        /*SL:81*/while (a3 != null) {
            try {
                /*SL:83*/if (!v-9.contains("(")) {
                    /*SL:84*/return a3.isInterface() ? a3.getField(substring2) : a3.getDeclaredField(substring2);
                }
                /*SL:85*/if (isConstructor(v-9)) {
                    /*SL:86*/return a3.isInterface() ? a3.getConstructor(array) : a3.getDeclaredConstructor(array);
                }
                /*SL:88*/return a3.isInterface() ? a3.getMethod(substring2, array) : a3.getDeclaredMethod(substring2, array);
            }
            catch (Exception v2) {
                /*SL:91*/a3 = a3.getSuperclass();
                /*SL:92*/continue;
            }
            break;
        }
        /*SL:94*/throw new ReflectionsException("Can't resolve member named " + substring2 + " for class " + substring);
    }
    
    public static Set<Method> getMethodsFromDescriptors(final Iterable<String> v1, final ClassLoader... v2) {
        final Set<Method> v3 = /*EL:98*/(Set<Method>)Sets.<Object>newHashSet();
        /*SL:99*/for (Method a2 : v1) {
            /*SL:100*/if (!isConstructor(a2)) {
                /*SL:101*/a2 = (Method)getMemberFromDescriptor(a2, v2);
                /*SL:102*/if (a2 == null) {
                    continue;
                }
                v3.add(a2);
            }
        }
        /*SL:105*/return v3;
    }
    
    public static Set<Constructor> getConstructorsFromDescriptors(final Iterable<String> v1, final ClassLoader... v2) {
        final Set<Constructor> v3 = /*EL:109*/(Set<Constructor>)Sets.<Object>newHashSet();
        /*SL:110*/for (Constructor a2 : v1) {
            /*SL:111*/if (isConstructor(a2)) {
                /*SL:112*/a2 = (Constructor)getMemberFromDescriptor(a2, v2);
                /*SL:113*/if (a2 == null) {
                    continue;
                }
                v3.add(a2);
            }
        }
        /*SL:116*/return v3;
    }
    
    public static Set<Member> getMembersFromDescriptors(final Iterable<String> v1, final ClassLoader... v2) {
        final Set<Member> v3 = /*EL:120*/(Set<Member>)Sets.<Object>newHashSet();
        /*SL:121*/for (final ReflectionsException a2 : v1) {
            try {
                /*SL:123*/v3.add(getMemberFromDescriptor(a2, v2));
            }
            catch (ReflectionsException a2) {
                /*SL:125*/throw new ReflectionsException("Can't resolve member named " + a2, a2);
            }
        }
        /*SL:128*/return v3;
    }
    
    public static Field getFieldFromString(final String a2, final ClassLoader... v1) {
        final String v2 = /*EL:132*/a2.substring(0, a2.lastIndexOf(46));
        final String v3 = /*EL:133*/a2.substring(a2.lastIndexOf(46) + 1);
        try {
            /*SL:136*/return ReflectionUtils.forName(v2, v1).getDeclaredField(v3);
        }
        catch (NoSuchFieldException a3) {
            /*SL:138*/throw new ReflectionsException("Can't resolve field named " + v3, a3);
        }
    }
    
    public static void close(final InputStream v1) {
        try {
            /*SL:143*/if (v1 != null) {
                v1.close();
            }
        }
        catch (IOException a1) {
            /*SL:145*/if (Reflections.log != null) {
                Reflections.log.warn(/*EL:146*/"Could not close InputStream", (Throwable)a1);
            }
        }
    }
    
    @Nullable
    public static Logger findLogger(final Class<?> v1) {
        try {
            /*SL:154*/Class.forName("org.slf4j.impl.StaticLoggerBinder");
            /*SL:155*/return LoggerFactory.getLogger((Class)v1);
        }
        catch (Throwable a1) {
            /*SL:157*/return null;
        }
    }
    
    public static boolean isConstructor(final String a1) {
        /*SL:162*/return a1.contains("init>");
    }
    
    public static String name(Class v1) {
        /*SL:166*/if (!v1.isArray()) {
            /*SL:167*/return v1.getName();
        }
        int a1 = /*EL:169*/0;
        /*SL:170*/while (v1.isArray()) {
            /*SL:171*/++a1;
            /*SL:172*/v1 = v1.getComponentType();
        }
        /*SL:174*/return v1.getName() + repeat("[]", a1);
    }
    
    public static List<String> names(final Iterable<Class<?>> v1) {
        final List<String> v2 = /*EL:180*/new ArrayList<String>();
        /*SL:181*/for (final Class<?> a1 : v1) {
            v2.add(name(a1));
        }
        /*SL:182*/return v2;
    }
    
    public static List<String> names(final Class<?>... a1) {
        /*SL:186*/return names(Arrays.<Class<?>>asList(a1));
    }
    
    public static String name(final Constructor a1) {
        /*SL:190*/return a1.getName() + ".<init>(" + Joiner.on(", ").join(names((Class<?>[])a1.getParameterTypes())) + ")";
    }
    
    public static String name(final Method a1) {
        /*SL:194*/return a1.getDeclaringClass().getName() + "." + a1.getName() + "(" + Joiner.on(", ").join(names(a1.getParameterTypes())) + ")";
    }
    
    public static String name(final Field a1) {
        /*SL:198*/return a1.getDeclaringClass().getName() + "." + a1.getName();
    }
}
