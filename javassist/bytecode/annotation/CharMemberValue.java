package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class CharMemberValue extends MemberValue
{
    int valueIndex;
    
    public CharMemberValue(final int a1, final ConstPool a2) {
        super('C', a2);
        this.valueIndex = a1;
    }
    
    public CharMemberValue(final char a1, final ConstPool a2) {
        super('C', a2);
        this.setValue(a1);
    }
    
    public CharMemberValue(final ConstPool a1) {
        super('C', a1);
        this.setValue('\0');
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) {
        /*SL:63*/return new Character(this.getValue());
    }
    
    @Override
    Class getType(final ClassLoader a1) {
        /*SL:67*/return Character.TYPE;
    }
    
    public char getValue() {
        /*SL:74*/return (char)this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final char a1) {
        /*SL:81*/this.valueIndex = this.cp.addIntegerInfo(a1);
    }
    
    @Override
    public String toString() {
        /*SL:88*/return Character.toString(this.getValue());
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:95*/a1.constValueIndex(this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:102*/a1.visitCharMemberValue(this);
    }
}
