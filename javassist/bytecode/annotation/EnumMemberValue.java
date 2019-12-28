package javassist.bytecode.annotation;

import java.io.IOException;
import javassist.bytecode.Descriptor;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class EnumMemberValue extends MemberValue
{
    int typeIndex;
    int valueIndex;
    
    public EnumMemberValue(final int a1, final int a2, final ConstPool a3) {
        super('e', a3);
        this.typeIndex = a1;
        this.valueIndex = a2;
    }
    
    public EnumMemberValue(final ConstPool a1) {
        super('e', a1);
        final boolean b = false;
        this.valueIndex = (b ? 1 : 0);
        this.typeIndex = (b ? 1 : 0);
    }
    
    @Override
    Object getValue(final ClassLoader v1, final ClassPool v2, final Method v3) throws ClassNotFoundException {
        try {
            /*SL:63*/return this.getType(v1).getField(this.getValue()).get(null);
        }
        catch (NoSuchFieldException a1) {
            /*SL:66*/throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
        catch (IllegalAccessException a2) {
            /*SL:69*/throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
    }
    
    @Override
    Class getType(final ClassLoader a1) throws ClassNotFoundException {
        /*SL:74*/return MemberValue.loadClass(a1, this.getType());
    }
    
    public String getType() {
        /*SL:83*/return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
    }
    
    public void setType(final String a1) {
        /*SL:92*/this.typeIndex = this.cp.addUtf8Info(Descriptor.of(a1));
    }
    
    public String getValue() {
        /*SL:99*/return this.cp.getUtf8Info(this.valueIndex);
    }
    
    public void setValue(final String a1) {
        /*SL:106*/this.valueIndex = this.cp.addUtf8Info(a1);
    }
    
    @Override
    public String toString() {
        /*SL:110*/return this.getType() + "." + this.getValue();
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:117*/a1.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:124*/a1.visitEnumMemberValue(this);
    }
}
