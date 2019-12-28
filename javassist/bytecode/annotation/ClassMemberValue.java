package javassist.bytecode.annotation;

import java.io.IOException;
import javassist.bytecode.Descriptor;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ClassMemberValue extends MemberValue
{
    int valueIndex;
    
    public ClassMemberValue(final int a1, final ConstPool a2) {
        super('c', a2);
        this.valueIndex = a1;
    }
    
    public ClassMemberValue(final String a1, final ConstPool a2) {
        super('c', a2);
        this.setValue(a1);
    }
    
    public ClassMemberValue(final ConstPool a1) {
        super('c', a1);
        this.setValue("java.lang.Class");
    }
    
    @Override
    Object getValue(final ClassLoader a1, final ClassPool a2, final Method a3) throws ClassNotFoundException {
        final String v1 = /*EL:69*/this.getValue();
        /*SL:70*/if (v1.equals("void")) {
            /*SL:71*/return Void.TYPE;
        }
        /*SL:72*/if (v1.equals("int")) {
            /*SL:73*/return Integer.TYPE;
        }
        /*SL:74*/if (v1.equals("byte")) {
            /*SL:75*/return Byte.TYPE;
        }
        /*SL:76*/if (v1.equals("long")) {
            /*SL:77*/return Long.TYPE;
        }
        /*SL:78*/if (v1.equals("double")) {
            /*SL:79*/return Double.TYPE;
        }
        /*SL:80*/if (v1.equals("float")) {
            /*SL:81*/return Float.TYPE;
        }
        /*SL:82*/if (v1.equals("char")) {
            /*SL:83*/return Character.TYPE;
        }
        /*SL:84*/if (v1.equals("short")) {
            /*SL:85*/return Short.TYPE;
        }
        /*SL:86*/if (v1.equals("boolean")) {
            /*SL:87*/return Boolean.TYPE;
        }
        /*SL:89*/return MemberValue.loadClass(a1, v1);
    }
    
    @Override
    Class getType(final ClassLoader a1) throws ClassNotFoundException {
        /*SL:93*/return MemberValue.loadClass(a1, "java.lang.Class");
    }
    
    public String getValue() {
        final String v0 = /*EL:102*/this.cp.getUtf8Info(this.valueIndex);
        try {
            /*SL:104*/return SignatureAttribute.toTypeSignature(v0).jvmTypeName();
        }
        catch (BadBytecode v) {
            /*SL:106*/throw new RuntimeException(v);
        }
    }
    
    public void setValue(final String a1) {
        final String v1 = /*EL:116*/Descriptor.of(a1);
        /*SL:117*/this.valueIndex = this.cp.addUtf8Info(v1);
    }
    
    @Override
    public String toString() {
        /*SL:124*/return this.getValue().replace('$', '.') + ".class";
    }
    
    @Override
    public void write(final AnnotationsWriter a1) throws IOException {
        /*SL:131*/a1.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:138*/a1.visitClassMemberValue(this);
    }
}
