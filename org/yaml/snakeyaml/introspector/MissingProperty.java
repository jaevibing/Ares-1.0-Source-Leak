package org.yaml.snakeyaml.introspector;

import java.util.Collections;
import java.lang.annotation.Annotation;
import java.util.List;

public class MissingProperty extends Property
{
    public MissingProperty(final String a1) {
        super(a1, Object.class);
    }
    
    @Override
    public Class<?>[] getActualTypeArguments() {
        /*SL:34*/return (Class<?>[])new Class[0];
    }
    
    @Override
    public void set(final Object a1, final Object a2) throws Exception {
    }
    
    @Override
    public Object get(final Object a1) {
        /*SL:46*/return a1;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        /*SL:51*/return Collections.<Annotation>emptyList();
    }
    
    @Override
    public <A extends java.lang.Object> A getAnnotation(final Class<A> a1) {
        /*SL:56*/return null;
    }
}
