package com.google.api.client.util;

import java.lang.reflect.TypeVariable;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashSet;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Data
{
    public static final Boolean NULL_BOOLEAN;
    public static final String NULL_STRING;
    public static final Character NULL_CHARACTER;
    public static final Byte NULL_BYTE;
    public static final Short NULL_SHORT;
    public static final Integer NULL_INTEGER;
    public static final Float NULL_FLOAT;
    public static final Long NULL_LONG;
    public static final Double NULL_DOUBLE;
    public static final BigInteger NULL_BIG_INTEGER;
    public static final BigDecimal NULL_BIG_DECIMAL;
    public static final DateTime NULL_DATE_TIME;
    private static final ConcurrentHashMap<Class<?>, Object> NULL_CACHE;
    
    public static <T> T nullOf(final Class<?> v-3) {
        Object o = Data.NULL_CACHE.get(/*EL:111*/v-3);
        /*SL:112*/if (o == null) {
            /*SL:113*/synchronized (Data.NULL_CACHE) {
                /*SL:114*/o = Data.NULL_CACHE.get(v-3);
                /*SL:115*/if (o == null) {
                    /*SL:116*/if (v-3.isArray()) {
                        int a1 = /*EL:118*/0;
                        Class<?> v1 = /*EL:119*/v-3;
                        /*SL:123*/do {
                            v1 = v1.getComponentType();
                            ++a1;
                        } while (v1.isArray());
                        /*SL:124*/o = Array.newInstance(v1, new int[a1]);
                    }
                    else/*SL:125*/ if (v-3.isEnum()) {
                        final FieldInfo v2 = /*EL:127*/ClassInfo.of(v-3).getFieldInfo(null);
                        /*SL:128*/Preconditions.<FieldInfo>checkNotNull(v2, "enum missing constant with @NullValue annotation: %s", v-3);
                        final Enum v3 = /*EL:132*/(Enum)(o = v2.<Object>enumValue());
                    }
                    else {
                        /*SL:135*/o = Types.<Object>newInstance(v-3);
                    }
                    Data.NULL_CACHE.put(/*EL:137*/v-3, o);
                }
            }
        }
        final T t = /*EL:142*/(T)o;
        /*SL:143*/return t;
    }
    
    public static boolean isNull(final Object a1) {
        /*SL:155*/return a1 != null && a1 == Data.NULL_CACHE.get(a1.getClass());
    }
    
    public static Map<String, Object> mapOf(final Object v1) {
        /*SL:178*/if (v1 == null || isNull(v1)) {
            /*SL:179*/return Collections.<String, Object>emptyMap();
        }
        /*SL:181*/if (v1 instanceof Map) {
            final Map<String, Object> a1 = /*EL:183*/(Map<String, Object>)v1;
            /*SL:184*/return a1;
        }
        final Map<String, Object> a1 = /*EL:186*/new DataMap(v1, false);
        /*SL:187*/return a1;
    }
    
    public static <T> T clone(final T v-1) {
        /*SL:208*/if (v-1 == null || isPrimitive(v-1.getClass())) {
            /*SL:209*/return v-1;
        }
        /*SL:211*/if (v-1 instanceof GenericData) {
            /*SL:212*/return (T)((GenericData)v-1).clone();
        }
        final Class<?> v0 = /*EL:215*/v-1.getClass();
        T a1;
        /*SL:216*/if (v0.isArray()) {
            /*SL:217*/a1 = (T)Array.newInstance(v0.getComponentType(), Array.getLength(v-1));
        }
        else/*SL:218*/ if (v-1 instanceof ArrayMap) {
            /*SL:219*/a1 = (T)((ArrayMap)v-1).clone();
        }
        else {
            /*SL:220*/if ("java.util.Arrays$ArrayList".equals(v0.getName())) {
                final Object[] v = /*EL:224*/((List)v-1).toArray();
                deepCopy(/*EL:225*/v, v);
                /*SL:226*/a1 = (T)Arrays.<Object>asList(v);
                /*SL:227*/return a1;
            }
            /*SL:229*/a1 = Types.<T>newInstance(v0);
        }
        deepCopy(/*EL:231*/v-1, a1);
        /*SL:232*/return a1;
    }
    
    public static void deepCopy(final Object v-3, final Object v-2) {
        final Class<?> class1 = /*EL:265*/v-3.getClass();
        /*SL:266*/Preconditions.checkArgument(class1 == v-2.getClass());
        /*SL:267*/if (class1.isArray()) {
            /*SL:269*/Preconditions.checkArgument(Array.getLength(v-3) == Array.getLength(v-2));
            int a2 = /*EL:270*/0;
            final Iterator<Object> iterator = /*EL:271*/Types.<Object>iterableOf(v-3).iterator();
            while (iterator.hasNext()) {
                a2 = iterator.next();
                /*SL:272*/Array.set(v-2, a2++, Data.<Object>clone(a2));
            }
        }
        else/*SL:274*/ if (Collection.class.isAssignableFrom(class1)) {
            final Collection<Object> a3 = /*EL:277*/(Collection<Object>)v-3;
            /*SL:278*/if (ArrayList.class.isAssignableFrom(class1)) {
                final ArrayList<Object> v1 = /*EL:280*/(ArrayList<Object>)v-2;
                /*SL:281*/v1.ensureCapacity(a3.size());
            }
            final Collection<Object> v2 = /*EL:284*/(Collection<Object>)v-2;
            /*SL:285*/for (final Object v3 : a3) {
                /*SL:286*/v2.add(Data.<Object>clone(v3));
            }
        }
        else {
            final boolean v4 = /*EL:290*/GenericData.class.isAssignableFrom(class1);
            /*SL:291*/if (v4 || !Map.class.isAssignableFrom(class1)) {
                final ClassInfo v5 = /*EL:292*/v4 ? ((GenericData)v-3).classInfo : /*EL:293*/ClassInfo.of(class1);
                /*SL:294*/for (final String v6 : v5.names) {
                    final FieldInfo v7 = /*EL:295*/v5.getFieldInfo(v6);
                    /*SL:297*/if (!v7.isFinal() && /*EL:299*/(!v4 || !v7.isPrimitive())) {
                        final Object v8 = /*EL:300*/v7.getValue(v-3);
                        /*SL:301*/if (v8 == null) {
                            continue;
                        }
                        /*SL:302*/v7.setValue(v-2, Data.<Object>clone(v8));
                    }
                }
            }
            else/*SL:307*/ if (ArrayMap.class.isAssignableFrom(class1)) {
                final ArrayMap<Object, Object> v9 = /*EL:310*/(ArrayMap<Object, Object>)v-2;
                final ArrayMap<Object, Object> a4 = /*EL:312*/(ArrayMap<Object, Object>)v-3;
                /*SL:314*/for (int v10 = a4.size(), v11 = 0; v11 < v10; ++v11) {
                    final Object v8 = /*EL:315*/a4.getValue(v11);
                    /*SL:316*/v9.set(v11, Data.<Object>clone(v8));
                }
            }
            else {
                final Map<String, Object> v12 = /*EL:321*/(Map<String, Object>)v-2;
                final Map<String, Object> a5 = /*EL:323*/(Map<String, Object>)v-3;
                /*SL:324*/for (final Map.Entry<String, Object> v13 : a5.entrySet()) {
                    /*SL:325*/v12.put(v13.getKey(), Data.<Object>clone(v13.getValue()));
                }
            }
        }
    }
    
    public static boolean isPrimitive(Type a1) {
        /*SL:347*/if (a1 instanceof WildcardType) {
            /*SL:348*/a1 = Types.getBound((WildcardType)a1);
        }
        /*SL:350*/if (!(a1 instanceof Class)) {
            /*SL:351*/return false;
        }
        final Class<?> v1 = /*EL:353*/(Class<?>)a1;
        /*SL:354*/return v1.isPrimitive() || v1 == Character.class || v1 == String.class || v1 == Integer.class || v1 == Long.class || v1 == Short.class || v1 == Byte.class || v1 == Float.class || v1 == Double.class || v1 == BigInteger.class || v1 == BigDecimal.class || v1 == DateTime.class || v1 == Boolean.class;
    }
    
    public static boolean isValueOfPrimitiveType(final Object a1) {
        /*SL:366*/return a1 == null || isPrimitive(a1.getClass());
    }
    
    public static Object parsePrimitiveValue(final Type a2, final String v1) {
        final Class<?> v2 = /*EL:401*/(Class<?>)((a2 instanceof Class) ? ((Class)a2) : null);
        /*SL:402*/if (a2 == null || v2 != null) {
            /*SL:403*/if (v2 == Void.class) {
                /*SL:404*/return null;
            }
            /*SL:406*/if (v1 == null || v2 == null || v2.isAssignableFrom(String.class)) {
                /*SL:408*/return v1;
            }
            /*SL:410*/if (v2 == Character.class || v2 == Character.TYPE) {
                /*SL:411*/if (v1.length() != 1) {
                    /*SL:412*/throw new IllegalArgumentException("expected type Character/char but got " + v2);
                }
                /*SL:415*/return v1.charAt(0);
            }
            else {
                /*SL:417*/if (v2 == Boolean.class || v2 == Boolean.TYPE) {
                    /*SL:418*/return Boolean.valueOf(v1);
                }
                /*SL:420*/if (v2 == Byte.class || v2 == Byte.TYPE) {
                    /*SL:421*/return Byte.valueOf(v1);
                }
                /*SL:423*/if (v2 == Short.class || v2 == Short.TYPE) {
                    /*SL:424*/return Short.valueOf(v1);
                }
                /*SL:426*/if (v2 == Integer.class || v2 == Integer.TYPE) {
                    /*SL:427*/return Integer.valueOf(v1);
                }
                /*SL:429*/if (v2 == Long.class || v2 == Long.TYPE) {
                    /*SL:430*/return Long.valueOf(v1);
                }
                /*SL:432*/if (v2 == Float.class || v2 == Float.TYPE) {
                    /*SL:433*/return Float.valueOf(v1);
                }
                /*SL:435*/if (v2 == Double.class || v2 == Double.TYPE) {
                    /*SL:436*/return Double.valueOf(v1);
                }
                /*SL:438*/if (v2 == DateTime.class) {
                    /*SL:439*/return DateTime.parseRfc3339(v1);
                }
                /*SL:441*/if (v2 == BigInteger.class) {
                    /*SL:442*/return new BigInteger(v1);
                }
                /*SL:444*/if (v2 == BigDecimal.class) {
                    /*SL:445*/return new BigDecimal(v1);
                }
                /*SL:447*/if (v2.isEnum()) {
                    final Enum a3 = /*EL:449*/ClassInfo.of(v2).getFieldInfo(v1).<Enum>enumValue();
                    /*SL:450*/return a3;
                }
            }
        }
        /*SL:453*/throw new IllegalArgumentException("expected primitive class, but got: " + a2);
    }
    
    public static Collection<Object> newCollectionInstance(Type a1) {
        /*SL:474*/if (a1 instanceof WildcardType) {
            /*SL:475*/a1 = Types.getBound((WildcardType)a1);
        }
        /*SL:477*/if (a1 instanceof ParameterizedType) {
            /*SL:478*/a1 = ((ParameterizedType)a1).getRawType();
        }
        final Class<?> v1 = /*EL:480*/(Class<?>)((a1 instanceof Class) ? ((Class)a1) : null);
        /*SL:481*/if (a1 == null || a1 instanceof GenericArrayType || (v1 != null && (v1.isArray() || /*EL:482*/v1.isAssignableFrom(ArrayList.class)))) {
            /*SL:483*/return new ArrayList<Object>();
        }
        /*SL:485*/if (v1 == null) {
            /*SL:486*/throw new IllegalArgumentException("unable to create new instance of type: " + a1);
        }
        /*SL:488*/if (v1.isAssignableFrom(HashSet.class)) {
            /*SL:489*/return new HashSet<Object>();
        }
        /*SL:491*/if (v1.isAssignableFrom(TreeSet.class)) {
            /*SL:492*/return new TreeSet<Object>();
        }
        final Collection<Object> v2 = /*EL:495*/Types.<Collection<Object>>newInstance(v1);
        /*SL:496*/return v2;
    }
    
    public static Map<String, Object> newMapInstance(final Class<?> a1) {
        /*SL:515*/if (a1 == null || a1.isAssignableFrom(ArrayMap.class)) {
            /*SL:516*/return (Map<String, Object>)ArrayMap.<Object, Object>create();
        }
        /*SL:518*/if (a1.isAssignableFrom(TreeMap.class)) {
            /*SL:519*/return new TreeMap<String, Object>();
        }
        final Map<String, Object> v1 = /*EL:522*/Types.<Map<String, Object>>newInstance(a1);
        /*SL:523*/return v1;
    }
    
    public static Type resolveWildcardTypeOrTypeVariable(final List<Type> a2, Type v1) {
        /*SL:539*/if (v1 instanceof WildcardType) {
            /*SL:540*/v1 = Types.getBound((WildcardType)v1);
        }
        /*SL:543*/while (v1 instanceof TypeVariable) {
            final Type a3 = /*EL:545*/Types.resolveTypeVariable(a2, (TypeVariable<?>)v1);
            /*SL:546*/if (a3 != null) {
                /*SL:547*/v1 = a3;
            }
            /*SL:550*/if (v1 instanceof TypeVariable) {
                /*SL:551*/v1 = ((TypeVariable)v1).getBounds()[0];
            }
        }
        /*SL:555*/return v1;
    }
    
    static {
        NULL_BOOLEAN = new Boolean(true);
        NULL_STRING = new String();
        NULL_CHARACTER = new Character('\0');
        NULL_BYTE = new Byte((byte)0);
        NULL_SHORT = new Short((short)0);
        NULL_INTEGER = new Integer(0);
        NULL_FLOAT = new Float(0.0f);
        NULL_LONG = new Long(0L);
        NULL_DOUBLE = new Double(0.0);
        NULL_BIG_INTEGER = new BigInteger("0");
        NULL_BIG_DECIMAL = new BigDecimal("0");
        NULL_DATE_TIME = new DateTime(0L);
        (NULL_CACHE = new ConcurrentHashMap<Class<?>, Object>()).put(Boolean.class, Data.NULL_BOOLEAN);
        Data.NULL_CACHE.put(String.class, Data.NULL_STRING);
        Data.NULL_CACHE.put(Character.class, Data.NULL_CHARACTER);
        Data.NULL_CACHE.put(Byte.class, Data.NULL_BYTE);
        Data.NULL_CACHE.put(Short.class, Data.NULL_SHORT);
        Data.NULL_CACHE.put(Integer.class, Data.NULL_INTEGER);
        Data.NULL_CACHE.put(Float.class, Data.NULL_FLOAT);
        Data.NULL_CACHE.put(Long.class, Data.NULL_LONG);
        Data.NULL_CACHE.put(Double.class, Data.NULL_DOUBLE);
        Data.NULL_CACHE.put(BigInteger.class, Data.NULL_BIG_INTEGER);
        Data.NULL_CACHE.put(BigDecimal.class, Data.NULL_BIG_DECIMAL);
        Data.NULL_CACHE.put(DateTime.class, Data.NULL_DATE_TIME);
    }
}
