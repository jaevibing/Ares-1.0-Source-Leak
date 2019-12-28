package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class DoubleMemberValue extends MemberValue
{
    int valueIndex;
    
    public DoubleMemberValue(final int a1, final ConstPool a2) {
        super('D', a2);
        this.valueIndex = a1;
    }
    
    public DoubleMemberValue(final double a1, final ConstPool a2) {
        super('D', a2);
        this.setValue(a1);
    }
    
    public DoubleMemberValue(final ConstPool a1) {
        super('D', a1);
        this.setValue(0.0);
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:64*/return new Double(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:68*/return Double.TYPE;
    }
    
    public double getValue() {
        /*SL:75*/return this.cp.getDoubleInfo(this.valueIndex);
    }
    
    public void setValue(final double a1) {
        /*SL:82*/this.valueIndex = this.cp.addDoubleInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:89*/return Double.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:96*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:103*/a1.visitDoubleMemberValue(this);
    }
}
