package javassist.expr;

import javassist.compiler.JvstTypeChecker;
import javassist.compiler.ast.ASTList;
import javassist.compiler.JvstCodeGen;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.compiler.CompileError;
import javassist.CannotCompileException;
import javassist.compiler.ProceedHandler;
import javassist.compiler.Javac;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class Cast extends Expr
{
    protected Cast(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public CtBehavior where() {
        /*SL:39*/return super.where();
    }
    
    @Override
    public int getLineNumber() {
        /*SL:48*/return super.getLineNumber();
    }
    
    @Override
    public String getFileName() {
        /*SL:57*/return super.getFileName();
    }
    
    public CtClass getType() throws NotFoundException {
        final ConstPool v1 = /*EL:65*/this.getConstPool();
        final int v2 = /*EL:66*/this.currentPos;
        final int v3 = /*EL:67*/this.iterator.u16bitAt(v2 + 1);
        final String v4 = /*EL:68*/v1.getClassInfo(v3);
        /*SL:69*/return this.thisClass.getClassPool().getCtClass(v4);
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:79*/return super.mayThrow();
    }
    
    @Override
    public void replace(final String v-7) throws CannotCompileException {
        /*SL:91*/this.thisClass.getClassFile();
        final ConstPool constPool = /*EL:92*/this.getConstPool();
        final int currentPos = /*EL:93*/this.currentPos;
        final int u16bit = /*EL:94*/this.iterator.u16bitAt(currentPos + 1);
        final Javac javac = /*EL:96*/new Javac(this.thisClass);
        final ClassPool classPool = /*EL:97*/this.thisClass.getClassPool();
        final CodeAttribute value = /*EL:98*/this.iterator.get();
        try {
            final CtClass[] a1 = /*EL:101*/{ classPool.get("java.lang.Object") };
            final CtClass v1 = /*EL:103*/this.getType();
            final int v2 = /*EL:105*/value.getMaxLocals();
            /*SL:106*/javac.recordParams("java.lang.Object", a1, true, v2, this.withinStatic());
            final int v3 = /*EL:108*/javac.recordReturnType(v1, true);
            /*SL:109*/javac.recordProceed(new ProceedForCast(u16bit, v1));
            /*SL:113*/Expr.checkResultValue(v1, v-7);
            final Bytecode v4 = /*EL:115*/javac.getBytecode();
            /*SL:116*/Expr.storeStack(a1, true, v2, v4);
            /*SL:117*/javac.recordLocalVariables(value, currentPos);
            /*SL:119*/v4.addConstZero(v1);
            /*SL:120*/v4.addStore(v3, v1);
            /*SL:122*/javac.compileStmnt(v-7);
            /*SL:123*/v4.addLoad(v3, v1);
            /*SL:125*/this.replace0(currentPos, v4, 3);
        }
        catch (CompileError v5) {
            /*SL:127*/throw new CannotCompileException(v5);
        }
        catch (NotFoundException v6) {
            /*SL:128*/throw new CannotCompileException(v6);
        }
        catch (BadBytecode v7) {
            /*SL:130*/throw new CannotCompileException("broken method");
        }
    }
    
    static class ProceedForCast implements ProceedHandler
    {
        int index;
        CtClass retType;
        
        ProceedForCast(final int a1, final CtClass a2) {
            this.index = a1;
            this.retType = a2;
        }
        
        @Override
        public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
            /*SL:148*/if (a1.getMethodArgsLength(a3) != 1) {
                /*SL:149*/throw new CompileError("$proceed() cannot take more than one parameter for cast");
            }
            /*SL:153*/a1.atMethodArgs(a3, new int[1], new int[1], new String[1]);
            /*SL:154*/a2.addOpcode(192);
            /*SL:155*/a2.addIndex(this.index);
            /*SL:156*/a1.setType(this.retType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:162*/a1.atMethodArgs(a2, new int[1], new int[1], new String[1]);
            /*SL:163*/a1.setType(this.retType);
        }
    }
}
