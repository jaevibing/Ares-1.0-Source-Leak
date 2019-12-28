package org.yaml.snakeyaml.introspector;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class Property implements Comparable<Property>
{
    private final String name;
    private final Class<?> type;
    
    public Property(final String a1, final Class<?> a2) {
        this.name = a1;
        this.type = a2;
    }
    
    public Class<?> getType() {
        /*SL:45*/return this.type;
    }
    
    public abstract Class<?>[] getActualTypeArguments();
    
    public String getName() {
        /*SL:51*/return this.name;
    }
    
    @Override
    public String toString() {
        /*SL:56*/return this.getName() + " of " + this.getType();
    }
    
    @Override
    public int compareTo(final Property a1) {
        /*SL:60*/return this.getName().compareTo(a1.getName());
    }
    
    public boolean isWritable() {
        /*SL:64*/return true;
    }
    
    public boolean isReadable() {
        /*SL:68*/return true;
    }
    
    public abstract void set(final Object p0, final Object p1) throws Exception;
    
    public abstract Object get(final Object p0);
    
    public abstract List<Annotation> getAnnotations();
    
    public abstract <A extends java.lang.Object> A getAnnotation(final Class<A> p0);
    
    @Override
    public int hashCode() {
        /*SL:94*/return this.getName().hashCode() + this.getType().hashCode();
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:99*/if (v2 instanceof Property) {
            final Property a1 = /*EL:100*/(Property)v2;
            /*SL:101*/return this.getName().equals(a1.getName()) && this.getType().equals(a1.getType());
        }
        /*SL:103*/return false;
    }
}
