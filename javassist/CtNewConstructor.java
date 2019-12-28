package javassist;

import javassist.bytecode.ConstPool;
import javassist.bytecode.Bytecode;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class CtNewConstructor
{
    public static final int PASS_NONE = 0;
    public static final int PASS_ARRAY = 1;
    public static final int PASS_PARAMS = 2;
    
    public static CtConstructor make(final String v1, final CtClass v2) throws CannotCompileException {
        final Javac v3 = /*EL:68*/new Javac(v2);
        try {
            final CtMember a1 = /*EL:70*/v3.compile(v1);
            /*SL:71*/if (a1 instanceof CtConstructor) {
                /*SL:73*/return (CtConstructor)a1;
            }
        }
        catch (CompileError a2) {
            /*SL:77*/throw new CannotCompileException(a2);
        }
        /*SL:80*/throw new CannotCompileException("not a constructor");
    }
    
    public static CtConstructor make(final CtClass[] a3, final CtClass[] a4, final String v1, final CtClass v2) throws CannotCompileException {
        try {
            final CtConstructor a5 = /*EL:101*/new CtConstructor(a3, v2);
            /*SL:102*/a5.setExceptionTypes(a4);
            /*SL:103*/a5.setBody(v1);
            /*SL:104*/return a5;
        }
        catch (NotFoundException a6) {
            /*SL:107*/throw new CannotCompileException(a6);
        }
    }
    
    public static CtConstructor copy(final CtConstructor a1, final CtClass a2, final ClassMap a3) throws CannotCompileException {
        /*SL:127*/return new CtConstructor(a1, a2, a3);
    }
    
    public static CtConstructor defaultConstructor(final CtClass v1) throws CannotCompileException {
        final CtConstructor v2 = /*EL:139*/new CtConstructor((CtClass[])null, v1);
        final ConstPool v3 = /*EL:141*/v1.getClassFile2().getConstPool();
        final Bytecode v4 = /*EL:142*/new Bytecode(v3, 1, 1);
        /*SL:143*/v4.addAload(0);
        try {
            /*SL:145*/v4.addInvokespecial(v1.getSuperclass(), "<init>", "()V");
        }
        catch (NotFoundException a1) {
            /*SL:149*/throw new CannotCompileException(a1);
        }
        /*SL:152*/v4.add(177);
        /*SL:155*/v2.getMethodInfo2().setCodeAttribute(v4.toCodeAttribute());
        /*SL:156*/return v2;
    }
    
    public static CtConstructor skeleton(final CtClass[] a1, final CtClass[] a2, final CtClass a3) throws CannotCompileException {
        /*SL:181*/return make(a1, a2, 0, null, null, a3);
    }
    
    public static CtConstructor make(final CtClass[] a1, final CtClass[] a2, final CtClass a3) throws CannotCompileException {
        /*SL:200*/return make(a1, a2, 2, null, null, a3);
    }
    
    public static CtConstructor make(final CtClass[] a1, final CtClass[] a2, final int a3, final CtMethod a4, final CtMethod.ConstParameter a5, final CtClass a6) throws CannotCompileException {
        /*SL:316*/return CtNewWrappedConstructor.wrapped(a1, a2, a3, a4, a5, a6);
    }
}
