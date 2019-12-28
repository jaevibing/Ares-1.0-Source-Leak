package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class StringMemberValue extends MemberValue
{
    int valueIndex;
    
    public StringMemberValue(final int a1, final ConstPool a2) {
        super('s', a2);
        this.valueIndex = a1;
    }
    
    public StringMemberValue(final String a1, final ConstPool a2) {
        super('s', a2);
        this.setValue(a1);
    }
    
    public StringMemberValue(final ConstPool a1) {
        super('s', a1);
        this.setValue("");
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:63*/return this.getValue();
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:67*/return String.class;
    }
    
    public String getValue() {
        /*SL:74*/return this.cp.getUtf8Info(this.valueIndex);
    }
    
    public void setValue(final String a1) {
        /*SL:81*/this.valueIndex = this.cp.addUtf8Info(a1);
    }
    
    @Override
    public String toString() {
        /*SL:88*/return "\"" + this.getValue() + "\"";
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:95*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:102*/a1.visitStringMemberValue(this);
    }
}
