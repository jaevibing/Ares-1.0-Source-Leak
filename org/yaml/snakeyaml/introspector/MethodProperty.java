package org.yaml.snakeyaml.introspector;

import org.yaml.snakeyaml.util.ArrayUtils;
import java.lang.annotation.Annotation;
import java.util.List;
import org.yaml.snakeyaml.error.YAMLException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.beans.PropertyDescriptor;

public class MethodProperty extends GenericProperty
{
    private final PropertyDescriptor property;
    private final boolean readable;
    private final boolean writable;
    
    private static Type discoverGenericType(final PropertyDescriptor v1) {
        final Method v2 = /*EL:43*/v1.getReadMethod();
        /*SL:44*/if (v2 != null) {
            /*SL:45*/return v2.getGenericReturnType();
        }
        final Method v3 = /*EL:48*/v1.getWriteMethod();
        /*SL:49*/if (v3 != null) {
            final Type[] a1 = /*EL:50*/v3.getGenericParameterTypes();
            /*SL:51*/if (a1.length > 0) {
                /*SL:52*/return a1[0];
            }
        }
        /*SL:60*/return null;
    }
    
    public MethodProperty(final PropertyDescriptor a1) {
        super(a1.getName(), a1.getPropertyType(), discoverGenericType(a1));
        this.property = a1;
        this.readable = (a1.getReadMethod() != null);
        this.writable = (a1.getWriteMethod() != null);
    }
    
    @Override
    public void set(final Object a1, final Object a2) throws Exception {
        /*SL:73*/if (!this.writable) {
            /*SL:74*/throw new YAMLException("No writable property '" + this.getName() + "' on class: " + a1.getClass().getName());
        }
        /*SL:77*/this.property.getWriteMethod().invoke(a1, a2);
    }
    
    @Override
    public Object get(final Object v2) {
        try {
            /*SL:83*/this.property.getReadMethod().setAccessible(true);
            /*SL:84*/return this.property.getReadMethod().invoke(v2, new Object[0]);
        }
        catch (Exception a1) {
            /*SL:86*/throw new YAMLException("Unable to find getter for property '" + this.property.getName() + "' on object " + v2 + ":" + a1);
        }
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        List<Annotation> v1;
        /*SL:100*/if (this.isReadable() && this.isWritable()) {
            /*SL:101*/v1 = ArrayUtils.<Annotation>toUnmodifiableCompositeList(this.property.getReadMethod().getAnnotations(), this.property.getWriteMethod().getAnnotations());
        }
        else/*SL:102*/ if (this.isReadable()) {
            /*SL:103*/v1 = ArrayUtils.<Annotation>toUnmodifiableList(this.property.getReadMethod().getAnnotations());
        }
        else {
            /*SL:105*/v1 = ArrayUtils.<Annotation>toUnmodifiableList(this.property.getWriteMethod().getAnnotations());
        }
        /*SL:107*/return v1;
    }
    
    @Override
    public <A extends java.lang.Object> A getAnnotation(final Class<A> a1) {
        A v1 = /*EL:119*/null;
        /*SL:120*/if (this.isReadable()) {
            /*SL:121*/v1 = this.property.getReadMethod().<A>getAnnotation(a1);
        }
        /*SL:123*/if (v1 == null && this.isWritable()) {
            /*SL:124*/v1 = this.property.getWriteMethod().<A>getAnnotation(a1);
        }
        /*SL:126*/return v1;
    }
    
    @Override
    public boolean isWritable() {
        /*SL:131*/return this.writable;
    }
    
    @Override
    public boolean isReadable() {
        /*SL:136*/return this.readable;
    }
}
