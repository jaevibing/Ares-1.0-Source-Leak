package javassist.expr;

import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.bytecode.Descriptor;
import javassist.NotFoundException;
import javassist.CtBehavior;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class MethodCall extends Expr
{
    protected MethodCall(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4) {
        super(a1, a2, a3, a4);
    }
    
    private int getNameAndType(final ConstPool a1) {
        final int v1 = /*EL:36*/this.currentPos;
        final int v2 = /*EL:37*/this.iterator.byteAt(v1);
        final int v3 = /*EL:38*/this.iterator.u16bitAt(v1 + 1);
        /*SL:40*/if (v2 == 185) {
            /*SL:41*/return a1.getInterfaceMethodrefNameAndType(v3);
        }
        /*SL:43*/return a1.getMethodrefNameAndType(v3);
    }
    
    @Override
    public CtBehavior where() {
        /*SL:50*/return super.where();
    }
    
    @Override
    public int getLineNumber() {
        /*SL:59*/return super.getLineNumber();
    }
    
    @Override
    public String getFileName() {
        /*SL:68*/return super.getFileName();
    }
    
    protected CtClass getCtClass() throws NotFoundException {
        /*SL:76*/return this.thisClass.getClassPool().get(this.getClassName());
    }
    
    public String getClassName() {
        ConstPool v2 = /*EL:86*/this.getConstPool();
        /*SL:87*/v2 = this.currentPos;
        final int v3 = /*EL:88*/this.iterator.byteAt(v2);
        final int v4 = /*EL:89*/this.iterator.u16bitAt(v2 + 1);
        String v5;
        /*SL:91*/if (v3 == 185) {
            /*SL:92*/v5 = v2.getInterfaceMethodrefClassName(v4);
        }
        else {
            /*SL:94*/v5 = v2.getMethodrefClassName(v4);
        }
        /*SL:96*/if (v5.charAt(0) == '[') {
            /*SL:97*/v5 = Descriptor.toClassName(v5);
        }
        /*SL:99*/return v5;
    }
    
    public String getMethodName() {
        final ConstPool v1 = /*EL:106*/this.getConstPool();
        final int v2 = /*EL:107*/this.getNameAndType(v1);
        /*SL:108*/return v1.getUtf8Info(v1.getNameAndTypeName(v2));
    }
    
    public CtMethod getMethod() throws NotFoundException {
        /*SL:115*/return this.getCtClass().getMethod(this.getMethodName(), this.getSignature());
    }
    
    public String getSignature() {
        final ConstPool v1 = /*EL:129*/this.getConstPool();
        final int v2 = /*EL:130*/this.getNameAndType(v1);
        /*SL:131*/return v1.getUtf8Info(v1.getNameAndTypeDescriptor(v2));
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:141*/return super.mayThrow();
    }
    
    public boolean isSuper() {
        /*SL:150*/return this.iterator.byteAt(this.currentPos) == 183 && !this.where().getDeclaringClass().getName().equals(this.getClassName());
    }
    
    @Override
    public void replace(final String v-4) throws CannotCompileException {
        /*SL:180*/this.thisClass.getClassFile();
        final ConstPool constPool = /*EL:181*/this.getConstPool();
        final int currentPos = /*EL:182*/this.currentPos;
        final int u16bit = /*EL:183*/this.iterator.u16bitAt(currentPos + 1);
        int v4 = /*EL:187*/this.iterator.byteAt(currentPos);
        int v2;
        String v3;
        final String v5;
        /*SL:188*/if (v4 == 185) {
            /*SL:189*/v2 = 5;
            final String a1 = /*EL:190*/constPool.getInterfaceMethodrefClassName(u16bit);
            /*SL:191*/v3 = constPool.getInterfaceMethodrefName(u16bit);
            /*SL:192*/v4 = constPool.getInterfaceMethodrefType(u16bit);
        }
        else {
            /*SL:194*/if (v4 != 184 && v4 != 183 && v4 != 182) {
                /*SL:202*/throw new CannotCompileException("not method invocation");
            }
            v2 = 3;
            v5 = constPool.getMethodrefClassName(u16bit);
            v3 = constPool.getMethodrefName(u16bit);
            v4 = constPool.getMethodrefType(u16bit);
        }
        final Javac v6 = /*EL:204*/new Javac(this.thisClass);
        final ClassPool v7 = /*EL:205*/this.thisClass.getClassPool();
        final CodeAttribute v8 = /*EL:206*/this.iterator.get();
        try {
            final CtClass[] v9 = /*EL:208*/Descriptor.getParameterTypes(v4, v7);
            final CtClass v10 = /*EL:209*/Descriptor.getReturnType(v4, v7);
            final int v11 = /*EL:210*/v8.getMaxLocals();
            /*SL:211*/v6.recordParams(v5, v9, true, v11, this.withinStatic());
            final int v12 = /*EL:213*/v6.recordReturnType(v10, true);
            /*SL:214*/if (v4 == 184) {
                /*SL:215*/v6.recordStaticProceed(v5, v3);
            }
            else/*SL:216*/ if (v4 == 183) {
                /*SL:217*/v6.recordSpecialProceed("$0", v5, v3, v4, u16bit);
            }
            else {
                /*SL:220*/v6.recordProceed("$0", v3);
            }
            /*SL:224*/Expr.checkResultValue(v10, v-4);
            final Bytecode v13 = /*EL:226*/v6.getBytecode();
            /*SL:227*/Expr.storeStack(v9, v4 == 184, v11, v13);
            /*SL:228*/v6.recordLocalVariables(v8, currentPos);
            /*SL:230*/if (v10 != CtClass.voidType) {
                /*SL:231*/v13.addConstZero(v10);
                /*SL:232*/v13.addStore(v12, v10);
            }
            /*SL:235*/v6.compileStmnt(v-4);
            /*SL:236*/if (v10 != CtClass.voidType) {
                /*SL:237*/v13.addLoad(v12, v10);
            }
            /*SL:239*/this.replace0(currentPos, v13, v2);
        }
        catch (CompileError v14) {
            /*SL:241*/throw new CannotCompileException(v14);
        }
        catch (NotFoundException v15) {
            /*SL:242*/throw new CannotCompileException(v15);
        }
        catch (BadBytecode v16) {
            /*SL:244*/throw new CannotCompileException("broken method");
        }
    }
}
