package javassist;

import javassist.bytecode.Descriptor;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Bytecode;

class CtNewWrappedConstructor extends CtNewWrappedMethod
{
    private static final int PASS_NONE = 0;
    private static final int PASS_PARAMS = 2;
    
    public static CtConstructor wrapped(final CtClass[] a4, final CtClass[] a5, final int a6, final CtMethod v1, final CtMethod.ConstParameter v2, final CtClass v3) throws CannotCompileException {
        try {
            final CtConstructor a7 = /*EL:36*/new CtConstructor(a4, v3);
            /*SL:37*/a7.setExceptionTypes(a5);
            final Bytecode a8 = makeBody(/*EL:38*/v3, v3.getClassFile2(), a6, v1, a4, v2);
            /*SL:41*/a7.getMethodInfo2().setCodeAttribute(a8.toCodeAttribute());
            /*SL:43*/return a7;
        }
        catch (NotFoundException a9) {
            /*SL:46*/throw new CannotCompileException(a9);
        }
    }
    
    protected static Bytecode makeBody(final CtClass v-6, final ClassFile v-5, final int v-4, final CtMethod v-3, final CtClass[] v-2, final CtMethod.ConstParameter v-1) throws CannotCompileException {
        int v2 = /*EL:59*/v-5.getSuperclassId();
        /*SL:60*/v2 = new Bytecode(v-5.getConstPool(), 0, 0);
        /*SL:61*/v2.setMaxLocals(false, v-2, 0);
        /*SL:62*/v2.addAload(0);
        int v3 = 0;
        /*SL:63*/if (v-4 == 0) {
            final int a1 = /*EL:64*/1;
            /*SL:65*/v2.addInvokespecial(v2, "<init>", "()V");
        }
        else/*SL:67*/ if (v-4 == 2) {
            final int a2 = /*EL:68*/v2.addLoadParameters(v-2, 1) + 1;
            /*SL:69*/v2.addInvokespecial(v2, "<init>", /*EL:70*/Descriptor.ofConstructor(v-2));
        }
        else {
            /*SL:73*/v3 = CtNewWrappedMethod.compileParameterList(v2, v-2, 1);
            final int a5;
            final String a6;
            /*SL:75*/if (v-1 == null) {
                final int a3 = /*EL:76*/2;
                final String a4 = /*EL:77*/CtMethod.ConstParameter.defaultConstDescriptor();
            }
            else {
                /*SL:80*/a5 = v-1.compile(v2) + 2;
                /*SL:81*/a6 = v-1.constDescriptor();
            }
            /*SL:84*/if (v3 < a5) {
                /*SL:85*/v3 = a5;
            }
            /*SL:87*/v2.addInvokespecial(v2, "<init>", a6);
        }
        /*SL:90*/if (v-3 == null) {
            /*SL:91*/v2.add(177);
        }
        else {
            final int v4 = /*EL:93*/CtNewWrappedMethod.makeBody0(v-6, v-5, v-3, false, v-2, CtClass.voidType, v-1, v2);
            /*SL:96*/if (v3 < v4) {
                /*SL:97*/v3 = v4;
            }
        }
        /*SL:100*/v2.setMaxStack(v3);
        /*SL:101*/return v2;
    }
}
