package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class LongMemberValue extends MemberValue
{
    int valueIndex;
    
    public LongMemberValue(final int a1, final ConstPool a2) {
        super('J', a2);
        this.valueIndex = a1;
    }
    
    public LongMemberValue(final long a1, final ConstPool a2) {
        super('J', a2);
        this.setValue(a1);
    }
    
    public LongMemberValue(final ConstPool a1) {
        super('J', a1);
        this.setValue(0L);
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:63*/return new Long(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:67*/return Long.TYPE;
    }
    
    public long getValue() {
        /*SL:74*/return this.cp.getLongInfo(this.valueIndex);
    }
    
    public void setValue(final long a1) {
        /*SL:81*/this.valueIndex = this.cp.addLongInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:88*/return Long.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:95*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:102*/a1.visitLongMemberValue(this);
    }
}
