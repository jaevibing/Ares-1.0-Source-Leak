package javassist.expr;

import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;

public class Handler extends Expr
{
    private static String EXCEPTION_NAME;
    private ExceptionTable etable;
    private int index;
    
    protected Handler(final ExceptionTable a1, final int a2, final CodeIterator a3, final CtClass a4, final MethodInfo a5) {
        super(a1.handlerPc(a2), a3, a4, a5);
        this.etable = a1;
        this.index = a2;
    }
    
    @Override
    public CtBehavior where() {
        /*SL:44*/return super.where();
    }
    
    @Override
    public int getLineNumber() {
        /*SL:52*/return super.getLineNumber();
    }
    
    @Override
    public String getFileName() {
        /*SL:61*/return super.getFileName();
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:68*/return super.mayThrow();
    }
    
    public CtClass getType() throws NotFoundException {
        final int v0 = /*EL:76*/this.etable.catchType(this.index);
        /*SL:77*/if (v0 == 0) {
            /*SL:78*/return null;
        }
        final ConstPool v = /*EL:80*/this.getConstPool();
        final String v2 = /*EL:81*/v.getClassInfo(v0);
        /*SL:82*/return this.thisClass.getClassPool().getCtClass(v2);
    }
    
    public boolean isFinally() {
        /*SL:90*/return this.etable.catchType(this.index) == 0;
    }
    
    @Override
    public void replace(final String a1) throws CannotCompileException {
        /*SL:99*/throw new RuntimeException("not implemented yet");
    }
    
    public void insertBefore(final String v-5) throws CannotCompileException {
        /*SL:110*/this.edited = true;
        final ConstPool constPool = /*EL:112*/this.getConstPool();
        final CodeAttribute value = /*EL:113*/this.iterator.get();
        final Javac javac = /*EL:114*/new Javac(this.thisClass);
        final Bytecode bytecode = /*EL:115*/javac.getBytecode();
        /*SL:116*/bytecode.setStackDepth(1);
        /*SL:117*/bytecode.setMaxLocals(value.getMaxLocals());
        try {
            final CtClass a1 = /*EL:120*/this.getType();
            final int v1 = /*EL:121*/javac.recordVariable(a1, Handler.EXCEPTION_NAME);
            /*SL:122*/javac.recordReturnType(a1, false);
            /*SL:123*/bytecode.addAstore(v1);
            /*SL:124*/javac.compileStmnt(v-5);
            /*SL:125*/bytecode.addAload(v1);
            final int v2 = /*EL:127*/this.etable.handlerPc(this.index);
            /*SL:128*/bytecode.addOpcode(167);
            /*SL:129*/bytecode.addIndex(v2 - this.iterator.getCodeLength() - bytecode.currentPc() + /*EL:130*/1);
            /*SL:132*/this.maxStack = bytecode.getMaxStack();
            /*SL:133*/this.maxLocals = bytecode.getMaxLocals();
            final int v3 = /*EL:135*/this.iterator.append(bytecode.get());
            /*SL:136*/this.iterator.append(bytecode.getExceptionTable(), v3);
            /*SL:137*/this.etable.setHandlerPc(this.index, v3);
        }
        catch (NotFoundException v4) {
            /*SL:140*/throw new CannotCompileException(v4);
        }
        catch (CompileError v5) {
            /*SL:143*/throw new CannotCompileException(v5);
        }
    }
    
    static {
        Handler.EXCEPTION_NAME = "$1";
    }
}
