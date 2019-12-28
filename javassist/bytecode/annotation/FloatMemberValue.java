package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class FloatMemberValue extends MemberValue
{
    int valueIndex;
    
    public FloatMemberValue(final int a1, final ConstPool a2) {
        super('F', a2);
        this.valueIndex = a1;
    }
    
    public FloatMemberValue(final float a1, final ConstPool a2) {
        super('F', a2);
        this.setValue(a1);
    }
    
    public FloatMemberValue(final ConstPool a1) {
        super('F', a1);
        this.setValue(0.0f);
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:64*/return new Float(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:68*/return Float.TYPE;
    }
    
    public float getValue() {
        /*SL:75*/return this.cp.getFloatInfo(this.valueIndex);
    }
    
    public void setValue(final float a1) {
        /*SL:82*/this.valueIndex = this.cp.addFloatInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:89*/return Float.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:96*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:103*/a1.visitFloatMemberValue(this);
    }
}
