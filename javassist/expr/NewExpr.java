package javassist.expr;

import javassist.compiler.JvstTypeChecker;
import javassist.compiler.MemberResolver;
import javassist.compiler.ast.ASTList;
import javassist.compiler.JvstCodeGen;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.compiler.CompileError;
import javassist.compiler.ProceedHandler;
import javassist.bytecode.Descriptor;
import javassist.compiler.Javac;
import javassist.CannotCompileException;
import javassist.CtConstructor;
import javassist.bytecode.ConstPool;
import javassist.NotFoundException;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class NewExpr extends Expr
{
    String newTypeName;
    int newPos;
    
    protected NewExpr(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4, final String a5, final int a6) {
        super(a1, a2, a3, a4);
        this.newTypeName = a5;
        this.newPos = a6;
    }
    
    @Override
    public CtBehavior where() {
        /*SL:59*/return super.where();
    }
    
    @Override
    public int getLineNumber() {
        /*SL:68*/return super.getLineNumber();
    }
    
    @Override
    public String getFileName() {
        /*SL:77*/return super.getFileName();
    }
    
    private CtClass getCtClass() throws NotFoundException {
        /*SL:84*/return this.thisClass.getClassPool().get(this.newTypeName);
    }
    
    public String getClassName() {
        /*SL:91*/return this.newTypeName;
    }
    
    public String getSignature() {
        final ConstPool v1 = /*EL:105*/this.getConstPool();
        final int v2 = /*EL:106*/this.iterator.u16bitAt(this.currentPos + 1);
        /*SL:107*/return v1.getMethodrefType(v2);
    }
    
    public CtConstructor getConstructor() throws NotFoundException {
        final ConstPool v1 = /*EL:114*/this.getConstPool();
        final int v2 = /*EL:115*/this.iterator.u16bitAt(this.currentPos + 1);
        final String v3 = /*EL:116*/v1.getMethodrefType(v2);
        /*SL:117*/return this.getCtClass().getConstructor(v3);
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:127*/return super.mayThrow();
    }
    
    private int canReplace() throws CannotCompileException {
        final int v1 = /*EL:142*/this.iterator.byteAt(this.newPos + 3);
        /*SL:143*/if (v1 == 89) {
            /*SL:145*/return (this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator.byteAt(this.newPos + 5) == 88) ? 6 : 4;
        }
        /*SL:146*/if (v1 == 90 && this.iterator.byteAt(this.newPos + 4) == /*EL:147*/95) {
            /*SL:148*/return 5;
        }
        /*SL:150*/return 3;
    }
    
    @Override
    public void replace(final String v-11) throws CannotCompileException {
        /*SL:164*/this.thisClass.getClassFile();
        final int n = /*EL:166*/3;
        int n2 = /*EL:167*/this.newPos;
        final int u16bit = /*EL:169*/this.iterator.u16bitAt(n2 + 1);
        final int canReplace = /*EL:173*/this.canReplace();
        /*SL:175*/for (int n3 = n2 + canReplace, a1 = n2; a1 < n3; ++a1) {
            /*SL:176*/this.iterator.writeByte(0, a1);
        }
        final ConstPool constPool = /*EL:178*/this.getConstPool();
        /*SL:179*/n2 = this.currentPos;
        final int u16bit2 = /*EL:180*/this.iterator.u16bitAt(n2 + 1);
        final String methodrefType = /*EL:182*/constPool.getMethodrefType(u16bit2);
        final Javac javac = /*EL:184*/new Javac(this.thisClass);
        final ClassPool classPool = /*EL:185*/this.thisClass.getClassPool();
        final CodeAttribute v0 = /*EL:186*/this.iterator.get();
        try {
            final CtClass[] v = /*EL:188*/Descriptor.getParameterTypes(methodrefType, classPool);
            final CtClass v2 = /*EL:189*/classPool.get(this.newTypeName);
            final int v3 = /*EL:190*/v0.getMaxLocals();
            /*SL:191*/javac.recordParams(this.newTypeName, v, true, v3, this.withinStatic());
            final int v4 = /*EL:193*/javac.recordReturnType(v2, true);
            /*SL:194*/javac.recordProceed(new ProceedForNew(v2, u16bit, u16bit2));
            /*SL:199*/Expr.checkResultValue(v2, v-11);
            final Bytecode v5 = /*EL:201*/javac.getBytecode();
            /*SL:202*/Expr.storeStack(v, true, v3, v5);
            /*SL:203*/javac.recordLocalVariables(v0, n2);
            /*SL:205*/v5.addConstZero(v2);
            /*SL:206*/v5.addStore(v4, v2);
            /*SL:208*/javac.compileStmnt(v-11);
            /*SL:209*/if (canReplace > 3) {
                /*SL:210*/v5.addAload(v4);
            }
            /*SL:212*/this.replace0(n2, v5, 3);
        }
        catch (CompileError v6) {
            /*SL:214*/throw new CannotCompileException(v6);
        }
        catch (NotFoundException v7) {
            /*SL:215*/throw new CannotCompileException(v7);
        }
        catch (BadBytecode v8) {
            /*SL:217*/throw new CannotCompileException("broken method");
        }
    }
    
    static class ProceedForNew implements ProceedHandler
    {
        CtClass newType;
        int newIndex;
        int methodIndex;
        
        ProceedForNew(final CtClass a1, final int a2, final int a3) {
            this.newType = a1;
            this.newIndex = a2;
            this.methodIndex = a3;
        }
        
        @Override
        public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
            /*SL:234*/a2.addOpcode(187);
            /*SL:235*/a2.addIndex(this.newIndex);
            /*SL:236*/a2.addOpcode(89);
            /*SL:237*/a1.atMethodCallCore(this.newType, "<init>", a3, false, true, -1, null);
            /*SL:239*/a1.setType(this.newType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:245*/a1.atMethodCallCore(this.newType, "<init>", a2);
            /*SL:246*/a1.setType(this.newType);
        }
    }
}
