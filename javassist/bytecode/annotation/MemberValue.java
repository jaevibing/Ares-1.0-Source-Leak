package javassist.bytecode.annotation;

import java.io.IOException;
import javassist.bytecode.Descriptor;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public abstract class MemberValue
{
    ConstPool cp;
    char tag;
    
    MemberValue(final char a1, final ConstPool a2) {
        this.cp = a2;
        this.tag = a1;
    }
    
    abstract Object getValue(final ClassLoader p0, final ClassPool p1, final Method p2) throws ClassNotFoundException;
    
    abstract Class getType(final ClassLoader p0) throws ClassNotFoundException;
    
    static Class loadClass(final ClassLoader a2, final String v1) throws ClassNotFoundException, NoSuchClassError {
        try {
            /*SL:56*/return Class.forName(convertFromArray(v1), true, a2);
        }
        catch (LinkageError a3) {
            /*SL:59*/throw new NoSuchClassError(v1, a3);
        }
    }
    
    private static String convertFromArray(final String v-2) {
        int i = /*EL:65*/v-2.indexOf("[]");
        /*SL:66*/if (i != -1) {
            final String a1 = /*EL:67*/v-2.substring(0, i);
            final StringBuffer v1 = /*EL:68*/new StringBuffer(Descriptor.of(a1));
            /*SL:69*/while (i != -1) {
                /*SL:70*/v1.insert(0, "[");
                /*SL:71*/i = v-2.indexOf("[]", i + 1);
            }
            /*SL:73*/return v1.toString().replace('/', '.');
        }
        /*SL:75*/return v-2;
    }
    
    public abstract void accept(final MemberValueVisitor p0);
    
    public abstract void write(final AnnotationsWriter p0) throws IOException;
}
