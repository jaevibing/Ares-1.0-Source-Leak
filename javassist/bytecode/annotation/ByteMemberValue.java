package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ByteMemberValue extends MemberValue
{
    int valueIndex;
    
    public ByteMemberValue(final int a1, final ConstPool a2) {
        super('B', a2);
        this.valueIndex = a1;
    }
    
    public ByteMemberValue(final byte a1, final ConstPool a2) {
        super('B', a2);
        this.setValue(a1);
    }
    
    public ByteMemberValue(final ConstPool a1) {
        super('B', a1);
        this.setValue((byte)0);
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:62*/return new Byte(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:66*/return Byte.TYPE;
    }
    
    public byte getValue() {
        /*SL:73*/return (byte)this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final byte a1) {
        /*SL:80*/this.valueIndex = this.cp.addIntegerInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:87*/return Byte.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:94*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:101*/a1.visitByteMemberValue(this);
    }
}
