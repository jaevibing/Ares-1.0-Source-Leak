package com.google.api.client.util;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Map;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.util.Iterator;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Types
{
    public static ParameterizedType getSuperParameterizedType(Type v-6, final Class<?> v-5) {
        /*SL:57*/if (v-6 instanceof Class || v-6 instanceof ParameterizedType) {
            /*SL:58*/Label_0014:
            while (v-6 != null && v-6 != Object.class) {
                Class<?> a1;
                /*SL:60*/if (v-6 instanceof Class) {
                    /*SL:62*/a1 = (Class<?>)v-6;
                }
                else {
                    final ParameterizedType a3 = /*EL:65*/(ParameterizedType)v-6;
                    /*SL:66*/a1 = getRawClass(a3);
                    /*SL:68*/if (a1 == v-5) {
                        /*SL:70*/return a3;
                    }
                    /*SL:72*/if (v-5.isInterface()) {
                        /*SL:73*/for (final Type v1 : a1.getGenericInterfaces()) {
                            final Class<?> a2 = /*EL:75*/(Class<?>)((v1 instanceof Class) ? ((Class)v1) : getRawClass((ParameterizedType)v1));
                            /*SL:78*/if (v-5.isAssignableFrom(a2)) {
                                /*SL:79*/v-6 = v1;
                                /*SL:80*/continue Label_0014;
                            }
                        }
                    }
                }
                /*SL:86*/v-6 = a1.getGenericSuperclass();
            }
        }
        /*SL:89*/return null;
    }
    
    public static boolean isAssignableToOrFrom(final Class<?> a1, final Class<?> a2) {
        /*SL:99*/return a1.isAssignableFrom(a2) || a2.isAssignableFrom(a1);
    }
    
    public static <T> T newInstance(final Class<T> v0) {
        try {
            /*SL:116*/return v0.newInstance();
        }
        catch (IllegalAccessException a1) {
            throw handleExceptionForNewInstance(/*EL:118*/a1, v0);
        }
        catch (InstantiationException v) {
            throw handleExceptionForNewInstance(/*EL:120*/v, v0);
        }
    }
    
    private static IllegalArgumentException handleExceptionForNewInstance(final Exception v1, final Class<?> v2) {
        final StringBuilder v3 = /*EL:126*/new StringBuilder("unable to create new instance of class ").append(v2.getName());
        final ArrayList<String> v4 = /*EL:128*/new ArrayList<String>();
        /*SL:129*/if (v2.isArray()) {
            /*SL:130*/v4.add("because it is an array");
        }
        else/*SL:131*/ if (v2.isPrimitive()) {
            /*SL:132*/v4.add("because it is primitive");
        }
        else/*SL:133*/ if (v2 == Void.class) {
            /*SL:134*/v4.add("because it is void");
        }
        else {
            /*SL:136*/if (Modifier.isInterface(v2.getModifiers())) {
                /*SL:137*/v4.add("because it is an interface");
            }
            else/*SL:138*/ if (Modifier.isAbstract(v2.getModifiers())) {
                /*SL:139*/v4.add("because it is abstract");
            }
            /*SL:141*/if (v2.getEnclosingClass() != null && !Modifier.isStatic(v2.getModifiers())) {
                /*SL:142*/v4.add("because it is not static");
            }
            /*SL:145*/if (!Modifier.isPublic(v2.getModifiers())) {
                /*SL:146*/v4.add("possibly because it is not public");
            }
            else {
                try {
                    /*SL:149*/v2.getConstructor((Class<?>[])new Class[0]);
                }
                catch (NoSuchMethodException a2) {
                    /*SL:151*/v4.add("because it has no accessible default constructor");
                }
            }
        }
        boolean v5 = /*EL:156*/false;
        /*SL:157*/for (final String a2 : v4) {
            /*SL:158*/if (v5) {
                /*SL:159*/v3.append(" and");
            }
            else {
                /*SL:161*/v5 = true;
            }
            /*SL:163*/v3.append(" ").append(a2);
        }
        /*SL:165*/return new IllegalArgumentException(v3.toString(), v1);
    }
    
    public static boolean isArray(final Type a1) {
        /*SL:170*/return a1 instanceof GenericArrayType || (a1 instanceof Class && ((Class)a1).isArray());
    }
    
    public static Type getArrayComponentType(final Type a1) {
        /*SL:185*/return (a1 instanceof GenericArrayType) ? ((GenericArrayType)a1).getGenericComponentType() : ((Class)a1).getComponentType();
    }
    
    public static Class<?> getRawClass(final ParameterizedType a1) {
        /*SL:197*/return (Class<?>)a1.getRawType();
    }
    
    public static Type getBound(final WildcardType a1) {
        final Type[] v1 = /*EL:207*/a1.getLowerBounds();
        /*SL:208*/if (v1.length != 0) {
            /*SL:209*/return v1[0];
        }
        /*SL:211*/return a1.getUpperBounds()[0];
    }
    
    public static Type resolveTypeVariable(final List<Type> v-5, final TypeVariable<?> v-4) {
        final GenericDeclaration genericDeclaration = /*EL:231*/(GenericDeclaration)v-4.getGenericDeclaration();
        /*SL:232*/if (genericDeclaration instanceof Class) {
            Class<?> v-6;
            int size;
            ParameterizedType v0;
            /*SL:237*/for (v-6 = (Class<?>)genericDeclaration, size = v-5.size(), v0 = null; v0 == null && --size >= 0; /*SL:239*/v0 = getSuperParameterizedType(v-5.get(size), v-6)) {}
            /*SL:241*/if (v0 != null) {
                TypeVariable<?>[] v;
                int v2;
                TypeVariable<?> a1;
                /*SL:245*/for (v = genericDeclaration.getTypeParameters(), v2 = 0; v2 < v.length; ++v2) {
                    /*SL:246*/a1 = v[v2];
                    /*SL:247*/if (a1.equals(v-4)) {
                        /*SL:248*/break;
                    }
                }
                final Type v3 = /*EL:252*/v0.getActualTypeArguments()[v2];
                /*SL:253*/if (v3 instanceof TypeVariable) {
                    final Type a2 = resolveTypeVariable(/*EL:255*/v-5, (TypeVariable<?>)v3);
                    /*SL:256*/if (a2 != null) {
                        /*SL:257*/return a2;
                    }
                }
                /*SL:261*/return v3;
            }
        }
        /*SL:264*/return null;
    }
    
    public static Class<?> getRawArrayComponentType(final List<Type> a2, Type v1) {
        /*SL:277*/if (v1 instanceof TypeVariable) {
            /*SL:278*/v1 = resolveTypeVariable(a2, (TypeVariable<?>)v1);
        }
        /*SL:280*/if (v1 instanceof GenericArrayType) {
            final Class<?> a3 = getRawArrayComponentType(/*EL:281*/a2, getArrayComponentType(v1));
            /*SL:282*/return Array.newInstance(a3, 0).getClass();
        }
        /*SL:284*/if (v1 instanceof Class) {
            /*SL:285*/return (Class<?>)v1;
        }
        /*SL:287*/if (v1 instanceof ParameterizedType) {
            /*SL:288*/return getRawClass((ParameterizedType)v1);
        }
        /*SL:290*/Preconditions.checkArgument(v1 == null, "wildcard type is not supported: %s", v1);
        /*SL:292*/return Object.class;
    }
    
    public static Type getIterableParameter(final Type a1) {
        /*SL:307*/return getActualParameterAtPosition(a1, Iterable.class, 0);
    }
    
    public static Type getMapValueParameter(final Type a1) {
        /*SL:322*/return getActualParameterAtPosition(a1, Map.class, 1);
    }
    
    private static Type getActualParameterAtPosition(final Type a2, final Class<?> a3, final int v1) {
        final ParameterizedType v2 = getSuperParameterizedType(/*EL:326*/a2, a3);
        /*SL:327*/if (v2 == null) {
            /*SL:328*/return null;
        }
        final Type v3 = /*EL:330*/v2.getActualTypeArguments()[v1];
        /*SL:333*/if (v3 instanceof TypeVariable) {
            final Type a4 = resolveTypeVariable(/*EL:334*/Arrays.<Type>asList(a2), (TypeVariable<?>)v3);
            /*SL:335*/if (a4 != null) {
                /*SL:336*/return a4;
            }
        }
        /*SL:339*/return v3;
    }
    
    public static <T> Iterable<T> iterableOf(final Object a1) {
        /*SL:355*/if (a1 instanceof Iterable) {
            /*SL:356*/return (Iterable<T>)a1;
        }
        final Class<?> v1 = /*EL:358*/a1.getClass();
        /*SL:359*/Preconditions.checkArgument(v1.isArray(), "not an array or Iterable: %s", v1);
        final Class<?> v2 = /*EL:360*/v1.getComponentType();
        /*SL:361*/if (!v2.isPrimitive()) {
            /*SL:362*/return (Iterable<T>)Arrays.<Object>asList((Object[])a1);
        }
        /*SL:364*/return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                /*SL:367*/return new Iterator<T>() {
                    final int length = Array.getLength(a1);
                    int index = 0;
                    
                    @Override
                    public boolean hasNext() {
                        /*SL:373*/return this.index < this.length;
                    }
                    
                    @Override
                    public T next() {
                        /*SL:377*/if (!this.hasNext()) {
                            /*SL:378*/throw new NoSuchElementException();
                        }
                        /*SL:380*/return (T)Array.get(a1, this.index++);
                    }
                    
                    @Override
                    public void remove() {
                        /*SL:384*/throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    
    public static Object toArray(final Collection<?> v-2, final Class<?> v-1) {
        /*SL:400*/if (v-1.isPrimitive()) {
            Object a2 = /*EL:401*/Array.newInstance(v-1, v-2.size());
            int v1 = /*EL:402*/0;
            final Iterator<?> iterator = /*EL:403*/v-2.iterator();
            while (iterator.hasNext()) {
                a2 = iterator.next();
                /*SL:404*/Array.set(a2, v1++, a2);
            }
            /*SL:406*/return a2;
        }
        /*SL:408*/return v-2.<Object>toArray((Object[])Array.newInstance(v-1, v-2.size()));
    }
}
