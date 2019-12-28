package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class AnnotationMemberValue extends MemberValue
{
    Annotation value;
    
    public AnnotationMemberValue(final ConstPool a1) {
        this(null, a1);
    }
    
    public AnnotationMemberValue(final Annotation a1, final ConstPool a2) {
        super('@', a2);
        this.value = a1;
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) throws ClassNotFoundException {
        /*SL:51*/return AnnotationImpl.make(a1, this.getType(a1), a2, this.value);
    }
    
    @Override
    Class getType(final ClassLoader a1) throws ClassNotFoundException {
        /*SL:55*/if (this.value == null) {
            /*SL:56*/throw new ClassNotFoundException("no type specified");
        }
        /*SL:58*/return MemberValue.loadClass(a1, this.value.getTypeName());
    }
    
    public Annotation getValue() {
        /*SL:65*/return this.value;
    }
    
    public void setValue(final Annotation a1) {
        /*SL:72*/this.value = a1;
    }
    
    @Override
    public String toString() {
        /*SL:79*/return this.value.toString();
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:86*/a1.annotationValue();
        /*SL:87*/this.value.write(a1);
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:94*/a1.visitAnnotationMemberValue(this);
    }
}
