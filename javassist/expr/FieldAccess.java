package javassist.expr;

import javassist.compiler.JvstTypeChecker;
import javassist.CtPrimitiveType;
import javassist.compiler.ast.ASTList;
import javassist.compiler.JvstCodeGen;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.compiler.CompileError;
import javassist.CannotCompileException;
import javassist.compiler.ProceedHandler;
import javassist.bytecode.Descriptor;
import javassist.compiler.Javac;
import javassist.bytecode.ConstPool;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class FieldAccess extends Expr
{
    int opcode;
    
    protected FieldAccess(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4, final int a5) {
        super(a1, a2, a3, a4);
        this.opcode = a5;
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
        /*SL:58*/return super.getFileName();
    }
    
    public boolean isStatic() {
        /*SL:65*/return isStatic(this.opcode);
    }
    
    static boolean isStatic(final int a1) {
        /*SL:69*/return a1 == 178 || a1 == 179;
    }
    
    public boolean isReader() {
        /*SL:76*/return this.opcode == 180 || this.opcode == 178;
    }
    
    public boolean isWriter() {
        /*SL:83*/return this.opcode == 181 || this.opcode == 179;
    }
    
    private CtClass getCtClass() throws NotFoundException {
        /*SL:90*/return this.thisClass.getClassPool().get(this.getClassName());
    }
    
    public String getClassName() {
        final int v1 = /*EL:97*/this.iterator.u16bitAt(this.currentPos + 1);
        /*SL:98*/return this.getConstPool().getFieldrefClassName(v1);
    }
    
    public String getFieldName() {
        final int v1 = /*EL:105*/this.iterator.u16bitAt(this.currentPos + 1);
        /*SL:106*/return this.getConstPool().getFieldrefName(v1);
    }
    
    public CtField getField() throws NotFoundException {
        final CtClass v1 = /*EL:113*/this.getCtClass();
        final int v2 = /*EL:114*/this.iterator.u16bitAt(this.currentPos + 1);
        final ConstPool v3 = /*EL:115*/this.getConstPool();
        /*SL:116*/return v1.getField(v3.getFieldrefName(v2), v3.getFieldrefType(v2));
    }
    
    @Override
    public CtClass[] mayThrow() {
        /*SL:126*/return super.mayThrow();
    }
    
    public String getSignature() {
        final int v1 = /*EL:138*/this.iterator.u16bitAt(this.currentPos + 1);
        /*SL:139*/return this.getConstPool().getFieldrefType(v1);
    }
    
    @Override
    public void replace(final String v-6) throws CannotCompileException {
        /*SL:153*/this.thisClass.getClassFile();
        final ConstPool constPool = /*EL:154*/this.getConstPool();
        final int currentPos = /*EL:155*/this.currentPos;
        final int u16bit = /*EL:156*/this.iterator.u16bitAt(currentPos + 1);
        final Javac javac = /*EL:158*/new Javac(this.thisClass);
        final CodeAttribute value = /*EL:159*/this.iterator.get();
        try {
            CtClass v2 = /*EL:164*/Descriptor.toCtClass(constPool.getFieldrefType(u16bit), this.thisClass.getClassPool());
            /*SL:166*/v2 = this.isReader();
            CtClass v3;
            final CtClass[] v4;
            /*SL:167*/if (v2) {
                final CtClass[] a1 = /*EL:168*/new CtClass[0];
                /*SL:169*/v3 = v2;
            }
            else {
                /*SL:172*/v4 = new CtClass[] { /*EL:173*/v2 };
                /*SL:174*/v3 = CtClass.voidType;
            }
            final int v5 = /*EL:177*/value.getMaxLocals();
            /*SL:178*/javac.recordParams(constPool.getFieldrefClassName(u16bit), v4, true, v5, this.withinStatic());
            boolean v6 = /*EL:183*/Expr.checkResultValue(v3, v-6);
            /*SL:184*/if (v2) {
                /*SL:185*/v6 = true;
            }
            final int v7 = /*EL:187*/javac.recordReturnType(v3, v6);
            /*SL:188*/if (v2) {
                /*SL:189*/javac.recordProceed(new ProceedForRead(v3, this.opcode, u16bit, v5));
            }
            else {
                /*SL:193*/javac.recordType(v2);
                /*SL:194*/javac.recordProceed(new ProceedForWrite(v4[0], this.opcode, u16bit, v5));
            }
            final Bytecode v8 = /*EL:198*/javac.getBytecode();
            /*SL:199*/Expr.storeStack(v4, this.isStatic(), v5, v8);
            /*SL:200*/javac.recordLocalVariables(value, currentPos);
            /*SL:202*/if (v6) {
                /*SL:203*/if (v3 == CtClass.voidType) {
                    /*SL:204*/v8.addOpcode(1);
                    /*SL:205*/v8.addAstore(v7);
                }
                else {
                    /*SL:208*/v8.addConstZero(v3);
                    /*SL:209*/v8.addStore(v7, v3);
                }
            }
            /*SL:212*/javac.compileStmnt(v-6);
            /*SL:213*/if (v2) {
                /*SL:214*/v8.addLoad(v7, v3);
            }
            /*SL:216*/this.replace0(currentPos, v8, 3);
        }
        catch (CompileError v9) {
            /*SL:218*/throw new CannotCompileException(v9);
        }
        catch (NotFoundException v10) {
            /*SL:219*/throw new CannotCompileException(v10);
        }
        catch (BadBytecode v11) {
            /*SL:221*/throw new CannotCompileException("broken method");
        }
    }
    
    static class ProceedForRead implements ProceedHandler
    {
        CtClass fieldType;
        int opcode;
        int targetVar;
        int index;
        
        ProceedForRead(final CtClass a1, final int a2, final int a3, final int a4) {
            this.fieldType = a1;
            this.targetVar = a4;
            this.opcode = a2;
            this.index = a3;
        }
        
        @Override
        public void doit(final JvstCodeGen a3, final Bytecode v1, final ASTList v2) throws CompileError {
            /*SL:242*/if (v2 != null && !a3.isParamListName(v2)) {
                /*SL:243*/throw new CompileError("$proceed() cannot take a parameter for field reading");
            }
            int v3 = 0;
            /*SL:247*/if (FieldAccess.isStatic(this.opcode)) {
                final int a4 = /*EL:248*/0;
            }
            else {
                /*SL:250*/v3 = -1;
                /*SL:251*/v1.addAload(this.targetVar);
            }
            /*SL:254*/if (this.fieldType instanceof CtPrimitiveType) {
                /*SL:255*/v3 += ((CtPrimitiveType)this.fieldType).getDataSize();
            }
            else {
                /*SL:257*/++v3;
            }
            /*SL:259*/v1.add(this.opcode);
            /*SL:260*/v1.addIndex(this.index);
            /*SL:261*/v1.growStack(v3);
            /*SL:262*/a3.setType(this.fieldType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:268*/a1.setType(this.fieldType);
        }
    }
    
    static class ProceedForWrite implements ProceedHandler
    {
        CtClass fieldType;
        int opcode;
        int targetVar;
        int index;
        
        ProceedForWrite(final CtClass a1, final int a2, final int a3, final int a4) {
            this.fieldType = a1;
            /*SL:269*/this.targetVar = a4;
            this.opcode = a2;
            this.index = a3;
        }
        
        @Override
        public void doit(final JvstCodeGen a3, final Bytecode v1, final ASTList v2) throws CompileError {
            /*SL:290*/if (a3.getMethodArgsLength(v2) != 1) {
                /*SL:291*/throw new CompileError("$proceed() cannot take more than one parameter for field writing");
            }
            int v3 = 0;
            /*SL:296*/if (FieldAccess.isStatic(this.opcode)) {
                final int a4 = /*EL:297*/0;
            }
            else {
                /*SL:299*/v3 = -1;
                /*SL:300*/v1.addAload(this.targetVar);
            }
            /*SL:303*/a3.atMethodArgs(v2, new int[1], new int[1], new String[1]);
            /*SL:304*/a3.doNumCast(this.fieldType);
            /*SL:305*/if (this.fieldType instanceof CtPrimitiveType) {
                /*SL:306*/v3 -= ((CtPrimitiveType)this.fieldType).getDataSize();
            }
            else {
                /*SL:308*/--v3;
            }
            /*SL:310*/v1.add(this.opcode);
            /*SL:311*/v1.addIndex(this.index);
            /*SL:312*/v1.growStack(v3);
            /*SL:313*/a3.setType(CtClass.voidType);
            /*SL:314*/a3.addNullIfVoid();
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:320*/a1.atMethodArgs(a2, new int[1], new int[1], new String[1]);
            /*SL:321*/a1.setType(CtClass.voidType);
            /*SL:322*/a1.addNullIfVoid();
        }
    }
}
