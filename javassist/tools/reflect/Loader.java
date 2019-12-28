package javassist.tools.reflect;

import javassist.NotFoundException;
import javassist.CannotCompileException;
import javassist.Translator;
import javassist.ClassPool;
import javassist.Loader;

public class Loader extends javassist.Loader
{
    protected Reflection reflection;
    
    public static void main(final String[] a1) throws Throwable {
        final Loader v1 = /*EL:123*/new Loader();
        /*SL:124*/v1.run(a1);
    }
    
    public Loader() throws CannotCompileException, NotFoundException {
        this.delegateLoadingOf("javassist.tools.reflect.Loader");
        /*SL:125*/this.reflection = new Reflection();
        final ClassPool v1 = ClassPool.getDefault();
        this.addTranslator(v1, this.reflection);
    }
    
    public boolean makeReflective(final String a1, final String a2, final String a3) throws CannotCompileException, NotFoundException {
        /*SL:160*/return this.reflection.makeReflective(a1, a2, a3);
    }
}
