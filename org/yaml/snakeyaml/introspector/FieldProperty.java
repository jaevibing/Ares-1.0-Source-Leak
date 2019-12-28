package org.yaml.snakeyaml.introspector;

import org.yaml.snakeyaml.util.ArrayUtils;
import java.lang.annotation.Annotation;
import java.util.List;
import org.yaml.snakeyaml.error.YAMLException;
import java.lang.reflect.Field;

public class FieldProperty extends GenericProperty
{
    private final Field field;
    
    public FieldProperty(final Field a1) {
        super(a1.getName(), a1.getType(), a1.getGenericType());
        (this.field = a1).setAccessible(true);
    }
    
    @Override
    public void set(final Object a1, final Object a2) throws Exception {
        /*SL:44*/this.field.set(a1, a2);
    }
    
    @Override
    public Object get(final Object v2) {
        try {
            /*SL:50*/return this.field.get(v2);
        }
        catch (Exception a1) {
            /*SL:52*/throw new YAMLException("Unable to access field " + this.field.getName() + " on object " + v2 + " : " + a1);
        }
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        /*SL:59*/return ArrayUtils.<Annotation>toUnmodifiableList(this.field.getAnnotations());
    }
    
    @Override
    public <A extends java.lang.Object> A getAnnotation(final Class<A> a1) {
        /*SL:64*/return this.field.<A>getAnnotation(a1);
    }
}
