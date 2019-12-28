package org.reflections;

import java.util.Collections;
import java.util.ArrayList;
import org.reflections.util.ClasspathHelper;
import com.google.common.collect.Lists;
import java.util.regex.Pattern;
import java.lang.reflect.Member;
import javax.annotation.Nullable;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicates;
import org.reflections.util.Utils;
import java.lang.reflect.AnnotatedElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Predicate;
import java.util.List;

public abstract class ReflectionUtils
{
    public static boolean includeObject;
    private static List<String> primitiveNames;
    private static List<Class> primitiveTypes;
    private static List<String> primitiveDescriptors;
    
    public static Set<Class<?>> getAllSuperTypes(final Class<?> a2, final Predicate<? super Class<?>>... v1) {
        final Set<Class<?>> v2 = /*EL:65*/(Set<Class<?>>)Sets.<Object>newLinkedHashSet();
        /*SL:66*/if (a2 != null && (ReflectionUtils.includeObject || !a2.equals(Object.class))) {
            /*SL:67*/v2.add(a2);
            /*SL:68*/for (final Class<?> a3 : getSuperTypes(a2)) {
                /*SL:69*/v2.addAll(getAllSuperTypes(a3, (Predicate<? super Class<?>>[])new Predicate[0]));
            }
        }
        /*SL:72*/return ReflectionUtils.<Class<?>>filter(v2, v1);
    }
    
    public static Set<Class<?>> getSuperTypes(final Class<?> a1) {
        final Set<Class<?>> v1 = /*EL:77*/new LinkedHashSet<Class<?>>();
        final Class<?> v2 = /*EL:78*/a1.getSuperclass();
        final Class<?>[] v3 = /*EL:79*/a1.getInterfaces();
        /*SL:80*/if (v2 != null && (ReflectionUtils.includeObject || !v2.equals(Object.class))) {
            v1.add(v2);
        }
        /*SL:81*/if (v3 != null && v3.length > 0) {
            v1.addAll(Arrays.<Class<?>>asList(v3));
        }
        /*SL:82*/return v1;
    }
    
    public static Set<Method> getAllMethods(final Class<?> a2, final Predicate<? super Method>... v1) {
        final Set<Method> v2 = /*EL:87*/(Set<Method>)Sets.<Object>newHashSet();
        /*SL:88*/for (final Class<?> a3 : getAllSuperTypes(a2, (Predicate<? super Class<?>>[])new Predicate[0])) {
            /*SL:89*/v2.addAll(getMethods(a3, v1));
        }
        /*SL:91*/return v2;
    }
    
    public static Set<Method> getMethods(final Class<?> a1, final Predicate<? super Method>... a2) {
        /*SL:96*/return ReflectionUtils.<Method>filter(a1.isInterface() ? a1.getMethods() : a1.getDeclaredMethods(), a2);
    }
    
    public static Set<Constructor> getAllConstructors(final Class<?> a2, final Predicate<? super Constructor>... v1) {
        final Set<Constructor> v2 = /*EL:101*/(Set<Constructor>)Sets.<Object>newHashSet();
        /*SL:102*/for (final Class<?> a3 : getAllSuperTypes(a2, (Predicate<? super Class<?>>[])new Predicate[0])) {
            /*SL:103*/v2.addAll(getConstructors(a3, v1));
        }
        /*SL:105*/return v2;
    }
    
    public static Set<Constructor> getConstructors(final Class<?> a1, final Predicate<? super Constructor>... a2) {
        /*SL:110*/return (Set<Constructor>)ReflectionUtils.<Constructor<?>>filter(a1.getDeclaredConstructors(), (Predicate<? super Constructor<?>>[])a2);
    }
    
    public static Set<Field> getAllFields(final Class<?> a2, final Predicate<? super Field>... v1) {
        final Set<Field> v2 = /*EL:115*/(Set<Field>)Sets.<Object>newHashSet();
        /*SL:116*/for (final Class<?> a3 : getAllSuperTypes(a2, (Predicate<? super Class<?>>[])new Predicate[0])) {
            v2.addAll(getFields(a3, v1));
        }
        /*SL:117*/return v2;
    }
    
    public static Set<Field> getFields(final Class<?> a1, final Predicate<? super Field>... a2) {
        /*SL:122*/return ReflectionUtils.<Field>filter(a1.getDeclaredFields(), a2);
    }
    
    public static <T extends java.lang.Object> Set<Annotation> getAllAnnotations(final T a2, final Predicate<Annotation>... v1) {
        final Set<Annotation> v2 = /*EL:127*/(Set<Annotation>)Sets.<Object>newHashSet();
        /*SL:128*/if (a2 instanceof Class) {
            /*SL:129*/for (final Class<?> a3 : getAllSuperTypes((Class<?>)a2, (Predicate<? super Class<?>>[])new Predicate[0])) {
                /*SL:130*/v2.addAll(getAnnotations((AnnotatedElement)a3, (Predicate[])v1));
            }
        }
        else {
            /*SL:133*/v2.addAll(getAnnotations((AnnotatedElement)a2, (Predicate[])v1));
        }
        /*SL:135*/return v2;
    }
    
    public static <T extends java.lang.Object> Set<Annotation> getAnnotations(final T a1, final Predicate<Annotation>... a2) {
        /*SL:140*/return ReflectionUtils.<Annotation>filter(((AnnotatedElement)a1).getDeclaredAnnotations(), (Predicate<? super Annotation>[])a2);
    }
    
    public static <T extends java.lang.Object> Set<T> getAll(final Set<T> a1, final Predicate<? super T>... a2) {
        /*SL:145*/return (Set<T>)(Utils.isEmpty(a2) ? a1 : Sets.<Object>newHashSet((Iterable<?>)Iterables.<? extends E>filter((Iterable<? extends E>)a1, Predicates.<? super E>and((Predicate<? super E>[])a2))));
    }
    
    public static <T extends java.lang.Object> Predicate<T> withName(final String a1) {
        /*SL:151*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:153*/return a1 != null && ((Member)a1).getName().equals(a1);
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withPrefix(final String a1) {
        /*SL:160*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:162*/return a1 != null && ((Member)a1).getName().startsWith(a1);
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withPattern(final String a1) {
        /*SL:174*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:176*/return Pattern.matches(a1, a1.toString());
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withAnnotation(final Class<? extends Annotation> a1) {
        /*SL:183*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:185*/return a1 != null && ((AnnotatedElement)a1).isAnnotationPresent(a1);
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withAnnotations(final Class<? extends Annotation>... a1) {
        /*SL:192*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:194*/return a1 != null && Arrays.equals(a1, annotationTypes(((AnnotatedElement)a1).getAnnotations()));
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withAnnotation(final Annotation a1) {
        /*SL:201*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:203*/return a1 != null && ((AnnotatedElement)a1).isAnnotationPresent(a1.annotationType()) && areAnnotationMembersMatching(((AnnotatedElement)a1).<Annotation>getAnnotation((Class<Annotation>)a1.annotationType()), a1);
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withAnnotations(final Annotation... a1) {
        /*SL:211*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T v0) {
                /*SL:213*/if (v0 != null) {
                    final Annotation[] v = /*EL:214*/((AnnotatedElement)v0).getAnnotations();
                    /*SL:215*/if (v.length == a1.length) {
                        /*SL:216*/for (int a1 = 0; a1 < v.length; ++a1) {
                            /*SL:217*/if (!areAnnotationMembersMatching(v[a1], a1[a1])) {
                                return false;
                            }
                        }
                    }
                }
                /*SL:221*/return true;
            }
        };
    }
    
    public static Predicate<Member> withParameters(final Class<?>... a1) {
        /*SL:228*/return new Predicate<Member>() {
            @Override
            public boolean apply(@Nullable final Member a1) {
                /*SL:230*/return Arrays.equals(parameterTypes(a1), a1);
            }
        };
    }
    
    public static Predicate<Member> withParametersAssignableTo(final Class... a1) {
        /*SL:237*/return new Predicate<Member>() {
            @Override
            public boolean apply(@Nullable final Member v0) {
                /*SL:239*/if (v0 != null) {
                    final Class<?>[] v = (Class<?>[])parameterTypes(/*EL:240*/v0);
                    /*SL:241*/if (v.length == a1.length) {
                        /*SL:242*/for (int a1 = 0; a1 < v.length; ++a1) {
                            /*SL:243*/if (!v[a1].isAssignableFrom(a1[a1]) || (v[a1] == Object.class && a1[a1] != Object.class)) {
                                /*SL:245*/return false;
                            }
                        }
                        /*SL:248*/return true;
                    }
                }
                /*SL:251*/return false;
            }
        };
    }
    
    public static Predicate<Member> withParametersCount(final int a1) {
        /*SL:258*/return new Predicate<Member>() {
            @Override
            public boolean apply(@Nullable final Member a1) {
                /*SL:260*/return a1 != null && parameterTypes(a1).length == a1;
            }
        };
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Class<? extends Annotation> a1) {
        /*SL:267*/return new Predicate<Member>() {
            @Override
            public boolean apply(@Nullable final Member a1) {
                /*SL:269*/return a1 != null && Iterables.<Object>any((Iterable<Object>)annotationTypes(parameterAnnotations(a1)), (Predicate<? super Object>)new Predicate<Class<? extends Annotation>>() {
                    @Override
                    public boolean apply(@Nullable final Class<? extends Annotation> a1) {
                        /*SL:271*/return a1.equals(a1);
                    }
                });
            }
        };
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Annotation a1) {
        /*SL:280*/return new Predicate<Member>() {
            @Override
            public boolean apply(@Nullable final Member a1) {
                /*SL:282*/return a1 != null && Iterables.<Object>any((Iterable<Object>)parameterAnnotations(a1), (Predicate<? super Object>)new Predicate<Annotation>() {
                    @Override
                    public boolean apply(@Nullable final Annotation a1) {
                        /*SL:284*/return areAnnotationMembersMatching(a1, a1);
                    }
                });
            }
        };
    }
    
    public static <T> Predicate<Field> withType(final Class<T> a1) {
        /*SL:293*/return new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable final Field a1) {
                /*SL:295*/return a1 != null && a1.getType().equals(a1);
            }
        };
    }
    
    public static <T> Predicate<Field> withTypeAssignableTo(final Class<T> a1) {
        /*SL:302*/return new Predicate<Field>() {
            @Override
            public boolean apply(@Nullable final Field a1) {
                /*SL:304*/return a1 != null && a1.isAssignableFrom(a1.getType());
            }
        };
    }
    
    public static <T> Predicate<Method> withReturnType(final Class<T> a1) {
        /*SL:311*/return new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable final Method a1) {
                /*SL:313*/return a1 != null && a1.getReturnType().equals(a1);
            }
        };
    }
    
    public static <T> Predicate<Method> withReturnTypeAssignableTo(final Class<T> a1) {
        /*SL:320*/return new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable final Method a1) {
                /*SL:322*/return a1 != null && a1.isAssignableFrom(a1.getReturnType());
            }
        };
    }
    
    public static <T extends java.lang.Object> Predicate<T> withModifier(final int a1) {
        /*SL:334*/return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T a1) {
                /*SL:336*/return a1 != null && (((Member)a1).getModifiers() & a1) != 0x0;
            }
        };
    }
    
    public static Predicate<Class<?>> withClassModifier(final int a1) {
        /*SL:348*/return new Predicate<Class<?>>() {
            @Override
            public boolean apply(@Nullable final Class<?> a1) {
                /*SL:350*/return a1 != null && (a1.getModifiers() & a1) != 0x0;
            }
        };
    }
    
    public static Class<?> forName(final String v-1, final ClassLoader... v0) {
        /*SL:360*/if (getPrimitiveNames().contains(v-1)) {
            /*SL:361*/return getPrimitiveTypes().get(getPrimitiveNames().indexOf(v-1));
        }
        String v;
        /*SL:364*/if (v-1.contains("[")) {
            final int a1 = /*EL:365*/v-1.indexOf("[");
            /*SL:366*/v = v-1.substring(0, a1);
            final String a2 = /*EL:367*/v-1.substring(a1).replace("]", "");
            /*SL:369*/if (getPrimitiveNames().contains(v)) {
                /*SL:370*/v = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(v));
            }
            else {
                /*SL:372*/v = "L" + v + ";";
            }
            /*SL:375*/v = a2 + v;
        }
        else {
            /*SL:377*/v = v-1;
        }
        final List<ReflectionsException> a3 = /*EL:380*/(List<ReflectionsException>)Lists.<Object>newArrayList();
        final ClassLoader[] classLoaders = /*EL:381*/ClasspathHelper.classLoaders(v0);
        final int length = classLoaders.length;
        int i = 0;
        while (i < length) {
            final ClassLoader v2 = classLoaders[i];
            /*SL:382*/if (v.contains("[")) {
                try {
                    /*SL:383*/return Class.forName(v, false, v2);
                }
                catch (Throwable v3) {
                    /*SL:385*/a3.add(new ReflectionsException("could not get type for name " + v-1, v3));
                }
            }
            try {
                /*SL:388*/return v2.loadClass(v);
            }
            catch (Throwable v3) {
                /*SL:390*/a3.add(new ReflectionsException("could not get type for name " + v-1, v3));
                ++i;
                continue;
            }
            break;
        }
        /*SL:394*/if (Reflections.log != null) {
            /*SL:395*/for (final ReflectionsException v4 : a3) {
                Reflections.log.warn(/*EL:396*/"could not get type for name " + v-1 + " from any class loader", (Throwable)v4);
            }
        }
        /*SL:401*/return null;
    }
    
    public static <T> List<Class<? extends T>> forNames(final Iterable<String> v1, final ClassLoader... v2) {
        final List<Class<? extends T>> v3 = /*EL:407*/new ArrayList<Class<? extends T>>();
        /*SL:408*/for (Class<?> a2 : v1) {
            /*SL:409*/a2 = forName(a2, v2);
            /*SL:410*/if (a2 != null) {
                /*SL:411*/v3.add((Class<? extends T>)a2);
            }
        }
        /*SL:414*/return v3;
    }
    
    private static Class[] parameterTypes(final Member a1) {
        /*SL:418*/return (Class[])((a1 != null) ? ((a1.getClass() == /*EL:419*/Method.class) ? ((Method)a1).getParameterTypes() : ((a1.getClass() == /*EL:420*/Constructor.class) ? ((Constructor)a1).getParameterTypes() : null)) : null);
    }
    
    private static Set<Annotation> parameterAnnotations(final Member v1) {
        final Set<Annotation> v2 = /*EL:424*/(Set<Annotation>)Sets.<Object>newHashSet();
        final Annotation[][] array;
        final Annotation[][] v3 = /*EL:428*/array = ((v1 instanceof Method) ? ((Method)v1).getParameterAnnotations() : ((v1 instanceof Constructor) ? ((Constructor)v1).getParameterAnnotations() : null));
        for (final Annotation[] a1 : array) {
            Collections.<Annotation>addAll(v2, a1);
        }
        /*SL:429*/return v2;
    }
    
    private static Set<Class<? extends Annotation>> annotationTypes(final Iterable<Annotation> v1) {
        final Set<Class<? extends Annotation>> v2 = /*EL:433*/(Set<Class<? extends Annotation>>)Sets.<Object>newHashSet();
        /*SL:434*/for (final Annotation a1 : v1) {
            v2.add(a1.annotationType());
        }
        /*SL:435*/return v2;
    }
    
    private static Class<? extends Annotation>[] annotationTypes(final Annotation[] v1) {
        final Class<? extends Annotation>[] v2 = /*EL:439*/(Class<? extends Annotation>[])new Class[v1.length];
        /*SL:440*/for (int a1 = 0; a1 < v1.length; ++a1) {
            v2[a1] = v1[a1].annotationType();
        }
        /*SL:441*/return v2;
    }
    
    private static void initPrimitives() {
        /*SL:450*/if (ReflectionUtils.primitiveNames == null) {
            ReflectionUtils.primitiveNames = /*EL:451*/Lists.<String>newArrayList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
            ReflectionUtils.primitiveTypes = /*EL:452*/(List<Class>)Lists.<Class>newArrayList(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE);
            ReflectionUtils.primitiveDescriptors = /*EL:453*/Lists.<String>newArrayList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
        }
    }
    
    private static List<String> getPrimitiveNames() {
        initPrimitives();
        /*SL:457*/return ReflectionUtils.primitiveNames;
    }
    
    private static List<Class> getPrimitiveTypes() {
        initPrimitives();
        /*SL:458*/return ReflectionUtils.primitiveTypes;
    }
    
    private static List<String> getPrimitiveDescriptors() {
        initPrimitives();
        /*SL:459*/return ReflectionUtils.primitiveDescriptors;
    }
    
    static <T> Set<T> filter(final T[] a1, final Predicate<? super T>... a2) {
        /*SL:463*/return Utils.isEmpty(a2) ? Sets.<T>newHashSet(a1) : /*EL:464*/Sets.<T>newHashSet((Iterable<? extends T>)Iterables.<? extends E>filter((Iterable<? extends E>)Arrays.<T>asList(a1), Predicates.<? super E>and((Predicate<? super E>[])a2)));
    }
    
    static <T> Set<T> filter(final Iterable<T> a1, final Predicate<? super T>... a2) {
        /*SL:468*/return (Set<T>)(Utils.isEmpty(a2) ? Sets.<Object>newHashSet((Iterable<?>)a1) : /*EL:469*/Sets.<Object>newHashSet((Iterable<?>)Iterables.<? extends E>filter((Iterable<? extends E>)a1, Predicates.<? super E>and((Predicate<? super E>[])a2))));
    }
    
    private static boolean areAnnotationMembersMatching(final Annotation v1, final Annotation v2) {
        /*SL:473*/if (v2 != null && v1.annotationType() == v2.annotationType()) {
            /*SL:474*/for (final Exception a2 : v1.annotationType().getDeclaredMethods()) {
                try {
                    /*SL:476*/if (!a2.invoke(v1, new Object[0]).equals(a2.invoke(v2, new Object[0]))) {
                        return false;
                    }
                }
                catch (Exception a2) {
                    /*SL:478*/throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", a2.getName(), v1.annotationType()), a2);
                }
            }
            /*SL:481*/return true;
        }
        /*SL:483*/return false;
    }
    
    static {
        ReflectionUtils.includeObject = false;
    }
}
