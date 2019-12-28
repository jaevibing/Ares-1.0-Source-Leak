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

public class Instanceof extends Expr
{
    protected Instanceof(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public CtBehavior where() {
        /*SL:40*/return super.where();
    }
    
    @Override
    public int getLineNumber() {
        /*SL:49*/return super.getLineNumber();
    }
    
    @Override
    public String getFileName() {
        /*SL:59*/return super.getFileName();
    }
    
    public CtClass getType() throws NotFoundException {
        final ConstPool v1 = /*EL:68*/this.getConstPool();
        final int v2 = /*EL:69*/this.currentPos;
        final int v3 = /*EL:70*/this.iterator.u16bitAt(v2 + 1);
        final String v4 = /*EL:71*/v1.getClassInfo(v3);
        /*SL:72*/return this.thisClass.getClassPool().getCtClass(v4);
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:82*/return super.mayThrow();
    }
    
    @Override
    public void replace(final String v-7) throws CannotCompileException {
        /*SL:94*/this.thisClass.getClassFile();
        final ConstPool constPool = /*EL:95*/this.getConstPool();
        final int currentPos = /*EL:96*/this.currentPos;
        final int u16bit = /*EL:97*/this.iterator.u16bitAt(currentPos + 1);
        final Javac javac = /*EL:99*/new Javac(this.thisClass);
        final ClassPool classPool = /*EL:100*/this.thisClass.getClassPool();
        final CodeAttribute value = /*EL:101*/this.iterator.get();
        try {
            final CtClass[] a1 = /*EL:104*/{ classPool.get("java.lang.Object") };
            final CtClass v1 = CtClass.booleanType;
            final int v2 = /*EL:108*/value.getMaxLocals();
            /*SL:109*/javac.recordParams("java.lang.Object", a1, true, v2, this.withinStatic());
            final int v3 = /*EL:111*/javac.recordReturnType(v1, true);
            /*SL:112*/javac.recordProceed(new ProceedForInstanceof(u16bit));
            /*SL:115*/javac.recordType(this.getType());
            /*SL:119*/Expr.checkResultValue(v1, v-7);
            final Bytecode v4 = /*EL:121*/javac.getBytecode();
            /*SL:122*/Expr.storeStack(a1, true, v2, v4);
            /*SL:123*/javac.recordLocalVariables(value, currentPos);
            /*SL:125*/v4.addConstZero(v1);
            /*SL:126*/v4.addStore(v3, v1);
            /*SL:128*/javac.compileStmnt(v-7);
            /*SL:129*/v4.addLoad(v3, v1);
            /*SL:131*/this.replace0(currentPos, v4, 3);
        }
        catch (CompileError v5) {
            /*SL:133*/throw new CannotCompileException(v5);
        }
        catch (NotFoundException v6) {
            /*SL:134*/throw new CannotCompileException(v6);
        }
        catch (BadBytecode v7) {
            /*SL:136*/throw new CannotCompileException("broken method");
        }
    }
    
    static class ProceedForInstanceof implements ProceedHandler
    {
        int index;
        
        ProceedForInstanceof(final int a1) {
            this.index = a1;
        }
        
        @Override
        public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
            /*SL:152*/if (a1.getMethodArgsLength(a3) != 1) {
                /*SL:153*/throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
            }
            /*SL:157*/a1.atMethodArgs(a3, new int[1], new int[1], new String[1]);
            /*SL:158*/a2.addOpcode(193);
            /*SL:159*/a2.addIndex(this.index);
            /*SL:160*/a1.setType(CtClass.booleanType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:166*/a1.atMethodArgs(a2, new int[1], new int[1], new String[1]);
            /*SL:167*/a1.setType(CtClass.booleanType);
        }
    }
}
