package javassist;

import java.util.Map;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.Bytecode;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public class CtNewMethod
{
    public static CtMethod make(final String a1, final CtClass a2) throws CannotCompileException {
        /*SL:45*/return make(a1, a2, null, null);
    }
    
    public static CtMethod make(final String a3, final CtClass a4, final String v1, final String v2) throws CannotCompileException {
        final Javac v3 = /*EL:69*/new Javac(a4);
        try {
            /*SL:71*/if (v2 != null) {
                /*SL:72*/v3.recordProceed(v1, v2);
            }
            final CtMember a5 = /*EL:74*/v3.compile(a3);
            /*SL:75*/if (a5 instanceof CtMethod) {
                /*SL:76*/return (CtMethod)a5;
            }
        }
        catch (CompileError a6) {
            /*SL:79*/throw new CannotCompileException(a6);
        }
        /*SL:82*/throw new CannotCompileException("not a method");
    }
    
    public static CtMethod make(final CtClass a1, final String a2, final CtClass[] a3, final CtClass[] a4, final String a5, final CtClass a6) throws CannotCompileException {
        /*SL:106*/return make(1, a1, a2, a3, a4, a5, a6);
    }
    
    public static CtMethod make(final int a3, final CtClass a4, final String a5, final CtClass[] a6, final CtClass[] a7, final String v1, final CtClass v2) throws CannotCompileException {
        try {
            final CtMethod a8 = /*EL:134*/new CtMethod(a4, a5, a6, v2);
            /*SL:136*/a8.setModifiers(a3);
            /*SL:137*/a8.setExceptionTypes(a7);
            /*SL:138*/a8.setBody(v1);
            /*SL:139*/return a8;
        }
        catch (NotFoundException a9) {
            /*SL:142*/throw new CannotCompileException(a9);
        }
    }
    
    public static CtMethod copy(final CtMethod a1, final CtClass a2, final ClassMap a3) throws CannotCompileException {
        /*SL:163*/return new CtMethod(a1, a2, a3);
    }
    
    public static CtMethod copy(final CtMethod a1, final String a2, final CtClass a3, final ClassMap a4) throws CannotCompileException {
        final CtMethod v1 = /*EL:185*/new CtMethod(a1, a3, a4);
        /*SL:186*/v1.setName(a2);
        /*SL:187*/return v1;
    }
    
    public static CtMethod abstractMethod(final CtClass a1, final String a2, final CtClass[] a3, final CtClass[] a4, final CtClass a5) throws NotFoundException {
        final CtMethod v1 = /*EL:208*/new CtMethod(a1, a2, a3, a5);
        /*SL:209*/v1.setExceptionTypes(a4);
        /*SL:210*/return v1;
    }
    
    public static CtMethod getter(final String v1, final CtField v2) throws CannotCompileException {
        final FieldInfo v3 = /*EL:225*/v2.getFieldInfo2();
        final String v4 = /*EL:226*/v3.getDescriptor();
        final String v5 = /*EL:227*/"()" + v4;
        final ConstPool v6 = /*EL:228*/v3.getConstPool();
        final MethodInfo v7 = /*EL:229*/new MethodInfo(v6, v1, v5);
        /*SL:230*/v7.setAccessFlags(1);
        final Bytecode v8 = /*EL:232*/new Bytecode(v6, 2, 1);
        try {
            final String a1 = /*EL:234*/v3.getName();
            /*SL:235*/if ((v3.getAccessFlags() & 0x8) == 0x0) {
                /*SL:236*/v8.addAload(0);
                /*SL:237*/v8.addGetfield(Bytecode.THIS, a1, v4);
            }
            else {
                /*SL:240*/v8.addGetstatic(Bytecode.THIS, a1, v4);
            }
            /*SL:242*/v8.addReturn(v2.getType());
        }
        catch (NotFoundException a2) {
            /*SL:245*/throw new CannotCompileException(a2);
        }
        /*SL:248*/v7.setCodeAttribute(v8.toCodeAttribute());
        final CtClass v9 = /*EL:249*/v2.getDeclaringClass();
        /*SL:251*/return new CtMethod(v7, v9);
    }
    
    public static CtMethod setter(final String v1, final CtField v2) throws CannotCompileException {
        final FieldInfo v3 = /*EL:268*/v2.getFieldInfo2();
        final String v4 = /*EL:269*/v3.getDescriptor();
        final String v5 = /*EL:270*/"(" + v4 + ")V";
        final ConstPool v6 = /*EL:271*/v3.getConstPool();
        final MethodInfo v7 = /*EL:272*/new MethodInfo(v6, v1, v5);
        /*SL:273*/v7.setAccessFlags(1);
        final Bytecode v8 = /*EL:275*/new Bytecode(v6, 3, 3);
        try {
            final String a1 = /*EL:277*/v3.getName();
            /*SL:278*/if ((v3.getAccessFlags() & 0x8) == 0x0) {
                /*SL:279*/v8.addAload(0);
                /*SL:280*/v8.addLoad(1, v2.getType());
                /*SL:281*/v8.addPutfield(Bytecode.THIS, a1, v4);
            }
            else {
                /*SL:284*/v8.addLoad(1, v2.getType());
                /*SL:285*/v8.addPutstatic(Bytecode.THIS, a1, v4);
            }
            /*SL:288*/v8.addReturn(null);
        }
        catch (NotFoundException a2) {
            /*SL:291*/throw new CannotCompileException(a2);
        }
        /*SL:294*/v7.setCodeAttribute(v8.toCodeAttribute());
        final CtClass v9 = /*EL:295*/v2.getDeclaringClass();
        /*SL:297*/return new CtMethod(v7, v9);
    }
    
    public static CtMethod delegator(final CtMethod a2, final CtClass v1) throws CannotCompileException {
        try {
            /*SL:326*/return delegator0(a2, v1);
        }
        catch (NotFoundException a3) {
            /*SL:329*/throw new CannotCompileException(a3);
        }
    }
    
    private static CtMethod delegator0(final CtMethod a2, final CtClass v1) throws CannotCompileException, NotFoundException {
        final MethodInfo v2 = /*EL:336*/a2.getMethodInfo2();
        final String v3 = /*EL:337*/v2.getName();
        final String v4 = /*EL:338*/v2.getDescriptor();
        final ConstPool v5 = /*EL:339*/v1.getClassFile2().getConstPool();
        final MethodInfo v6 = /*EL:340*/new MethodInfo(v5, v3, v4);
        /*SL:341*/v6.setAccessFlags(v2.getAccessFlags());
        final ExceptionsAttribute v7 = /*EL:343*/v2.getExceptionsAttribute();
        /*SL:344*/if (v7 != null) {
            /*SL:345*/v6.setExceptionsAttribute((ExceptionsAttribute)v7.copy(v5, null));
        }
        final Bytecode v8 = /*EL:348*/new Bytecode(v5, 0, 0);
        final boolean v9 = /*EL:349*/Modifier.isStatic(a2.getModifiers());
        final CtClass v10 = /*EL:350*/a2.getDeclaringClass();
        final CtClass[] v11 = /*EL:351*/a2.getParameterTypes();
        int v12 = 0;
        /*SL:353*/if (v9) {
            final int a3 = /*EL:354*/v8.addLoadParameters(v11, 0);
            /*SL:355*/v8.addInvokestatic(v10, v3, v4);
        }
        else {
            /*SL:358*/v8.addLoad(0, v10);
            /*SL:359*/v12 = v8.addLoadParameters(v11, 1);
            /*SL:360*/v8.addInvokespecial(v10, v3, v4);
        }
        /*SL:363*/v8.addReturn(a2.getReturnType());
        /*SL:364*/v8.setMaxLocals(++v12);
        /*SL:365*/v8.setMaxStack((v12 < 2) ? 2 : v12);
        /*SL:366*/v6.setCodeAttribute(v8.toCodeAttribute());
        /*SL:368*/return new CtMethod(v6, v1);
    }
    
    public static CtMethod wrapped(final CtClass a1, final String a2, final CtClass[] a3, final CtClass[] a4, final CtMethod a5, final CtMethod.ConstParameter a6, final CtClass a7) throws CannotCompileException {
        /*SL:475*/return CtNewWrappedMethod.wrapped(a1, a2, a3, a4, a5, a6, a7);
    }
}
