package javassist.compiler;

import javassist.bytecode.FieldInfo;
import javassist.bytecode.ExceptionsAttribute;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ClassFile;
import javassist.NotFoundException;
import javassist.CannotCompileException;
import javassist.bytecode.Bytecode;
import java.util.Map;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.SyntheticAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import java.util.HashMap;
import javassist.CtClass;

public class AccessorMaker
{
    private CtClass clazz;
    private int uniqueNumber;
    private HashMap accessors;
    static final String lastParamType = "javassist.runtime.Inner";
    
    public AccessorMaker(final CtClass a1) {
        this.clazz = a1;
        this.uniqueNumber = 1;
        this.accessors = new HashMap();
    }
    
    public String getConstructor(final CtClass v-7, final String v-6, final MethodInfo v-5) throws CompileError {
        final String string = /*EL:43*/"<init>:" + v-6;
        String appendParameter = /*EL:44*/this.accessors.get(string);
        /*SL:45*/if (appendParameter != null) {
            /*SL:46*/return appendParameter;
        }
        /*SL:48*/appendParameter = Descriptor.appendParameter("javassist.runtime.Inner", v-6);
        final ClassFile classFile = /*EL:49*/this.clazz.getClassFile();
        try {
            ConstPool a2 = /*EL:51*/classFile.getConstPool();
            /*SL:52*/a2 = this.clazz.getClassPool();
            final MethodInfo v1 = /*EL:53*/new MethodInfo(a2, "<init>", appendParameter);
            /*SL:55*/v1.setAccessFlags(0);
            /*SL:56*/v1.addAttribute(new SyntheticAttribute(a2));
            final ExceptionsAttribute v2 = /*EL:57*/v-5.getExceptionsAttribute();
            /*SL:58*/if (v2 != null) {
                /*SL:59*/v1.addAttribute(v2.copy(a2, null));
            }
            final CtClass[] v3 = /*EL:61*/Descriptor.getParameterTypes(v-6, a2);
            final Bytecode v4 = /*EL:62*/new Bytecode(a2);
            /*SL:63*/v4.addAload(0);
            int v5 = /*EL:64*/1;
            /*SL:65*/for (int a3 = 0; a3 < v3.length; ++a3) {
                /*SL:66*/v5 += v4.addLoad(v5, v3[a3]);
            }
            /*SL:67*/v4.setMaxLocals(v5 + 1);
            /*SL:68*/v4.addInvokespecial(this.clazz, "<init>", v-6);
            /*SL:70*/v4.addReturn(null);
            /*SL:71*/v1.setCodeAttribute(v4.toCodeAttribute());
            /*SL:72*/classFile.addMethod(v1);
        }
        catch (CannotCompileException a4) {
            /*SL:75*/throw new CompileError(a4);
        }
        catch (NotFoundException a5) {
            /*SL:78*/throw new CompileError(a5);
        }
        /*SL:81*/this.accessors.put(string, appendParameter);
        /*SL:82*/return appendParameter;
    }
    
    public String getMethodAccessor(final String v-9, final String v-8, final String v-7, final MethodInfo v-6) throws CompileError {
        final String string = /*EL:102*/v-9 + ":" + v-8;
        String accessorName = /*EL:103*/this.accessors.get(string);
        /*SL:104*/if (accessorName != null) {
            /*SL:105*/return accessorName;
        }
        final ClassFile classFile = /*EL:107*/this.clazz.getClassFile();
        /*SL:108*/accessorName = this.findAccessorName(classFile);
        try {
            ConstPool a2 = /*EL:110*/classFile.getConstPool();
            /*SL:111*/a2 = this.clazz.getClassPool();
            final MethodInfo a3 = /*EL:112*/new MethodInfo(a2, accessorName, v-7);
            /*SL:114*/a3.setAccessFlags(8);
            /*SL:115*/a3.addAttribute(new SyntheticAttribute(a2));
            final ExceptionsAttribute v1 = /*EL:116*/v-6.getExceptionsAttribute();
            /*SL:117*/if (v1 != null) {
                /*SL:118*/a3.addAttribute(v1.copy(a2, null));
            }
            final CtClass[] v2 = /*EL:120*/Descriptor.getParameterTypes(v-7, a2);
            int v3 = /*EL:121*/0;
            final Bytecode v4 = /*EL:122*/new Bytecode(a2);
            /*SL:123*/for (int a4 = 0; a4 < v2.length; ++a4) {
                /*SL:124*/v3 += v4.addLoad(v3, v2[a4]);
            }
            /*SL:126*/v4.setMaxLocals(v3);
            /*SL:127*/if (v-8 == v-7) {
                /*SL:128*/v4.addInvokestatic(this.clazz, v-9, v-8);
            }
            else {
                /*SL:130*/v4.addInvokevirtual(this.clazz, v-9, v-8);
            }
            /*SL:132*/v4.addReturn(Descriptor.getReturnType(v-8, a2));
            /*SL:133*/a3.setCodeAttribute(v4.toCodeAttribute());
            /*SL:134*/classFile.addMethod(a3);
        }
        catch (CannotCompileException a5) {
            /*SL:137*/throw new CompileError(a5);
        }
        catch (NotFoundException a6) {
            /*SL:140*/throw new CompileError(a6);
        }
        /*SL:143*/this.accessors.put(string, accessorName);
        /*SL:144*/return accessorName;
    }
    
    public MethodInfo getFieldGetter(final FieldInfo v-7, final boolean v-6) throws CompileError {
        final String name = /*EL:153*/v-7.getName();
        final String string = /*EL:154*/name + ":getter";
        final Object value = /*EL:155*/this.accessors.get(string);
        /*SL:156*/if (value != null) {
            /*SL:157*/return (MethodInfo)value;
        }
        final ClassFile classFile = /*EL:159*/this.clazz.getClassFile();
        final String accessorName = /*EL:160*/this.findAccessorName(classFile);
        try {
            ConstPool a2 = /*EL:162*/classFile.getConstPool();
            final ClassPool v1 = /*EL:163*/this.clazz.getClassPool();
            final String v2 = /*EL:164*/v-7.getDescriptor();
            final String v3;
            /*SL:166*/if (v-6) {
                /*SL:167*/a2 = "()" + v2;
            }
            else {
                /*SL:169*/v3 = "(" + Descriptor.of(this.clazz) + ")" + v2;
            }
            final MethodInfo v4 = /*EL:171*/new MethodInfo(a2, accessorName, v3);
            /*SL:172*/v4.setAccessFlags(8);
            /*SL:173*/v4.addAttribute(new SyntheticAttribute(a2));
            final Bytecode v5 = /*EL:174*/new Bytecode(a2);
            /*SL:175*/if (v-6) {
                /*SL:176*/v5.addGetstatic(Bytecode.THIS, name, v2);
            }
            else {
                /*SL:179*/v5.addAload(0);
                /*SL:180*/v5.addGetfield(Bytecode.THIS, name, v2);
                /*SL:181*/v5.setMaxLocals(1);
            }
            /*SL:184*/v5.addReturn(Descriptor.toCtClass(v2, v1));
            /*SL:185*/v4.setCodeAttribute(v5.toCodeAttribute());
            /*SL:186*/classFile.addMethod(v4);
            /*SL:187*/this.accessors.put(string, v4);
            /*SL:188*/return v4;
        }
        catch (CannotCompileException v6) {
            /*SL:191*/throw new CompileError(v6);
        }
        catch (NotFoundException v7) {
            /*SL:194*/throw new CompileError(v7);
        }
    }
    
    public MethodInfo getFieldSetter(final FieldInfo v-6, final boolean v-5) throws CompileError {
        final String name = /*EL:204*/v-6.getName();
        final String string = /*EL:205*/name + ":setter";
        final Object value = /*EL:206*/this.accessors.get(string);
        /*SL:207*/if (value != null) {
            /*SL:208*/return (MethodInfo)value;
        }
        final ClassFile classFile = /*EL:210*/this.clazz.getClassFile();
        final String v0 = /*EL:211*/this.findAccessorName(classFile);
        try {
            final ConstPool v = /*EL:213*/classFile.getConstPool();
            final ClassPool v2 = /*EL:214*/this.clazz.getClassPool();
            final String v3 = /*EL:215*/v-6.getDescriptor();
            final String v4;
            /*SL:217*/if (v-5) {
                final String a1 = /*EL:218*/"(" + v3 + ")V";
            }
            else {
                /*SL:220*/v4 = "(" + Descriptor.of(this.clazz) + v3 + ")V";
            }
            final MethodInfo v5 = /*EL:222*/new MethodInfo(v, v0, v4);
            /*SL:223*/v5.setAccessFlags(8);
            /*SL:224*/v5.addAttribute(new SyntheticAttribute(v));
            final Bytecode v6 = /*EL:225*/new Bytecode(v);
            final int v7;
            /*SL:227*/if (v-5) {
                final int a2 = /*EL:228*/v6.addLoad(0, Descriptor.toCtClass(v3, v2));
                /*SL:229*/v6.addPutstatic(Bytecode.THIS, name, v3);
            }
            else {
                /*SL:232*/v6.addAload(0);
                /*SL:233*/v7 = v6.addLoad(1, Descriptor.toCtClass(v3, v2)) + 1;
                /*SL:235*/v6.addPutfield(Bytecode.THIS, name, v3);
            }
            /*SL:238*/v6.addReturn(null);
            /*SL:239*/v6.setMaxLocals(v7);
            /*SL:240*/v5.setCodeAttribute(v6.toCodeAttribute());
            /*SL:241*/classFile.addMethod(v5);
            /*SL:242*/this.accessors.put(string, v5);
            /*SL:243*/return v5;
        }
        catch (CannotCompileException v8) {
            /*SL:246*/throw new CompileError(v8);
        }
        catch (NotFoundException v9) {
            /*SL:249*/throw new CompileError(v9);
        }
    }
    
    private String findAccessorName(final ClassFile a1) {
        String v1;
        /*SL:257*/do {
            v1 = "access$" + this.uniqueNumber++;
        } while (a1.getMethod(v1) != null);
        /*SL:258*/return v1;
    }
}
