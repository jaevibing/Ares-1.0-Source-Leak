package com.google.api.client.util;

import java.util.WeakHashMap;
import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.Map;

public class FieldInfo
{
    private static final Map<Field, FieldInfo> CACHE;
    private final boolean isPrimitive;
    private final Field field;
    private final String name;
    
    public static FieldInfo of(final Enum<?> v0) {
        try {
            final FieldInfo a1 = of(/*EL:48*/v0.getClass().getField(v0.name()));
            /*SL:49*/Preconditions.checkArgument(a1 != null, "enum constant missing @Value or @NullValue annotation: %s", v0);
            /*SL:51*/return a1;
        }
        catch (NoSuchFieldException v) {
            /*SL:54*/throw new RuntimeException(v);
        }
    }
    
    public static FieldInfo of(final Field v-4) {
        /*SL:66*/if (v-4 == null) {
            /*SL:67*/return null;
        }
        /*SL:69*/synchronized (FieldInfo.CACHE) {
            FieldInfo fieldInfo = FieldInfo.CACHE.get(/*EL:70*/v-4);
            final boolean enumConstant = /*EL:71*/v-4.isEnumConstant();
            /*SL:72*/if (fieldInfo == null && (enumConstant || !Modifier.isStatic(v-4.getModifiers()))) {
                String v2 = null;
                /*SL:74*/if (enumConstant) {
                    final Value v0 = /*EL:76*/v-4.<Value>getAnnotation(Value.class);
                    /*SL:77*/if (v0 != null) {
                        final String a1 = /*EL:78*/v0.value();
                    }
                    else {
                        final NullValue v = /*EL:81*/v-4.<NullValue>getAnnotation(NullValue.class);
                        /*SL:82*/if (v == null) {
                            /*SL:86*/return null;
                        }
                        v2 = null;
                    }
                }
                else {
                    final Key v3 = /*EL:91*/v-4.<Key>getAnnotation(Key.class);
                    /*SL:92*/if (v3 == null) {
                        /*SL:94*/return null;
                    }
                    /*SL:96*/v2 = v3.value();
                    /*SL:97*/v-4.setAccessible(true);
                }
                /*SL:99*/if ("##default".equals(v2)) {
                    /*SL:100*/v2 = v-4.getName();
                }
                /*SL:102*/fieldInfo = new FieldInfo(v-4, v2);
                FieldInfo.CACHE.put(/*EL:103*/v-4, fieldInfo);
            }
            /*SL:105*/return fieldInfo;
        }
    }
    
    FieldInfo(final Field a1, final String a2) {
        this.field = a1;
        this.name = ((a2 == null) ? null : a2.intern());
        this.isPrimitive = Data.isPrimitive(this.getType());
    }
    
    public Field getField() {
        /*SL:138*/return this.field;
    }
    
    public String getName() {
        /*SL:153*/return this.name;
    }
    
    public Class<?> getType() {
        /*SL:162*/return this.field.getType();
    }
    
    public Type getGenericType() {
        /*SL:172*/return this.field.getGenericType();
    }
    
    public boolean isFinal() {
        /*SL:181*/return Modifier.isFinal(this.field.getModifiers());
    }
    
    public boolean isPrimitive() {
        /*SL:190*/return this.isPrimitive;
    }
    
    public Object getValue(final Object a1) {
        /*SL:197*/return getFieldValue(this.field, a1);
    }
    
    public void setValue(final Object a1, final Object a2) {
        setFieldValue(/*EL:206*/this.field, a1, a2);
    }
    
    public ClassInfo getClassInfo() {
        /*SL:211*/return ClassInfo.of(this.field.getDeclaringClass());
    }
    
    public <T extends Enum<T>> T enumValue() {
        /*SL:216*/return Enum.<T>valueOf(this.field.getDeclaringClass(), this.field.getName());
    }
    
    public static Object getFieldValue(final Field a2, final Object v1) {
        try {
            /*SL:224*/return a2.get(v1);
        }
        catch (IllegalAccessException a3) {
            /*SL:226*/throw new IllegalArgumentException(a3);
        }
    }
    
    public static void setFieldValue(final Field v1, final Object v2, final Object v3) {
        /*SL:236*/if (Modifier.isFinal(v1.getModifiers())) {
            final Object a1 = getFieldValue(/*EL:237*/v1, v2);
            /*SL:238*/if (v3 == null) {
                if (a1 == null) {
                    return;
                }
            }
            else if (v3.equals(a1)) {
                return;
            }
            /*SL:239*/throw new IllegalArgumentException("expected final value <" + a1 + "> but was <" + v3 + "> on " + v1.getName() + /*EL:241*/" field in " + v2.getClass().getName());
        }
        try {
            /*SL:245*/v1.set(v2, v3);
        }
        catch (SecurityException a2) {
            /*SL:247*/throw new IllegalArgumentException(a2);
        }
        catch (IllegalAccessException a3) {
            /*SL:249*/throw new IllegalArgumentException(a3);
        }
    }
    
    static {
        CACHE = new WeakHashMap<Field, FieldInfo>();
    }
}
