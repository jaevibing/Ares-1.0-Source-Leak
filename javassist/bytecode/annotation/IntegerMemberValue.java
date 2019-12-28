package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class IntegerMemberValue extends MemberValue
{
    int valueIndex;
    
    public IntegerMemberValue(final int a1, final ConstPool a2) {
        super('I', a2);
        this.valueIndex = a1;
    }
    
    public IntegerMemberValue(final ConstPool a1, final int a2) {
        super('I', a1);
        this.setValue(a2);
    }
    
    public IntegerMemberValue(final ConstPool a1) {
        super('I', a1);
        this.setValue(0);
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:69*/return new Integer(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:73*/return Integer.TYPE;
    }
    
    public int getValue() {
        /*SL:80*/return this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final int a1) {
        /*SL:87*/this.valueIndex = this.cp.addIntegerInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:94*/return Integer.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:101*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:108*/a1.visitIntegerMemberValue(this);
    }
}
