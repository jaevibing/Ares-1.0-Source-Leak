package javassist.expr;

import javassist.compiler.JvstTypeChecker;
import javassist.compiler.ast.ASTList;
import javassist.compiler.JvstCodeGen;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.compiler.ProceedHandler;
import javassist.compiler.Javac;
import javassist.CtPrimitiveType;
import javassist.bytecode.BadBytecode;
import javassist.compiler.CompileError;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.CtBehavior;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class NewArray extends Expr
{
    int opcode;
    
    protected NewArray(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4, final int a5) {
        super(a1, a2, a3, a4);
        this.opcode = a5;
    }
    
    @Override
    public CtBehavior where() {
        /*SL:43*/return super.where();
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
        /*SL:71*/return super.mayThrow();
    }
    
    public CtClass getComponentType() throws NotFoundException {
        /*SL:81*/if (this.opcode == 188) {
            final int v1 = /*EL:82*/this.iterator.byteAt(this.currentPos + 1);
            /*SL:83*/return this.getPrimitiveType(v1);
        }
        /*SL:85*/if (this.opcode == 189 || this.opcode == 197) {
            final int v1 = /*EL:87*/this.iterator.u16bitAt(this.currentPos + 1);
            String v2 = /*EL:88*/this.getConstPool().getClassInfo(v1);
            final int v3 = /*EL:89*/Descriptor.arrayDimension(v2);
            /*SL:90*/v2 = Descriptor.toArrayComponent(v2, v3);
            /*SL:91*/return Descriptor.toCtClass(v2, this.thisClass.getClassPool());
        }
        /*SL:94*/throw new RuntimeException("bad opcode: " + this.opcode);
    }
    
    CtClass getPrimitiveType(final int a1) {
        /*SL:98*/switch (a1) {
            case 4: {
                /*SL:100*/return CtClass.booleanType;
            }
            case 5: {
                /*SL:102*/return CtClass.charType;
            }
            case 6: {
                /*SL:104*/return CtClass.floatType;
            }
            case 7: {
                /*SL:106*/return CtClass.doubleType;
            }
            case 8: {
                /*SL:108*/return CtClass.byteType;
            }
            case 9: {
                /*SL:110*/return CtClass.shortType;
            }
            case 10: {
                /*SL:112*/return CtClass.intType;
            }
            case 11: {
                /*SL:114*/return CtClass.longType;
            }
            default: {
                /*SL:116*/throw new RuntimeException("bad atype: " + a1);
            }
        }
    }
    
    public int getDimension() {
        /*SL:124*/if (this.opcode == 188) {
            /*SL:125*/return 1;
        }
        /*SL:126*/if (this.opcode == 189 || this.opcode == 197) {
            final int v1 = /*EL:128*/this.iterator.u16bitAt(this.currentPos + 1);
            final String v2 = /*EL:129*/this.getConstPool().getClassInfo(v1);
            /*SL:130*/return Descriptor.arrayDimension(v2) + ((this.opcode == 189) ? 1 : 0);
        }
        /*SL:134*/throw new RuntimeException("bad opcode: " + this.opcode);
    }
    
    public int getCreatedDimensions() {
        /*SL:143*/if (this.opcode == 197) {
            /*SL:144*/return this.iterator.byteAt(this.currentPos + 3);
        }
        /*SL:146*/return 1;
    }
    
    @Override
    public void replace(final String v0) throws CannotCompileException {
        try {
            /*SL:161*/this.replace2(v0);
        }
        catch (CompileError a1) {
            /*SL:163*/throw new CannotCompileException(a1);
        }
        catch (NotFoundException v) {
            /*SL:164*/throw new CannotCompileException(v);
        }
        catch (BadBytecode v2) {
            /*SL:166*/throw new CannotCompileException("broken method");
        }
    }
    
    private void replace2(final String v-3) throws CompileError, NotFoundException, BadBytecode, CannotCompileException {
        /*SL:174*/this.thisClass.getClassFile();
        final ConstPool constPool = /*EL:175*/this.getConstPool();
        final int currentPos = /*EL:176*/this.currentPos;
        int v2 = /*EL:179*/0;
        /*SL:180*/v2 = 1;
        String v3;
        int v4;
        /*SL:182*/if (this.opcode == 188) {
            /*SL:183*/v2 = this.iterator.byteAt(this.currentPos + 1);
            final CtPrimitiveType a1 = /*EL:184*/(CtPrimitiveType)this.getPrimitiveType(v2);
            /*SL:185*/v3 = "[" + a1.getDescriptor();
            /*SL:186*/v4 = 2;
        }
        else/*SL:188*/ if (this.opcode == 189) {
            /*SL:189*/v2 = this.iterator.u16bitAt(currentPos + 1);
            /*SL:190*/v3 = constPool.getClassInfo(v2);
            /*SL:191*/if (v3.startsWith("[")) {
                /*SL:192*/v3 = "[" + v3;
            }
            else {
                /*SL:194*/v3 = "[L" + v3 + ";";
            }
            /*SL:196*/v4 = 3;
        }
        else {
            /*SL:198*/if (this.opcode != 197) {
                /*SL:205*/throw new RuntimeException("bad opcode: " + this.opcode);
            }
            v2 = this.iterator.u16bitAt(this.currentPos + 1);
            v3 = constPool.getClassInfo(v2);
            v2 = this.iterator.byteAt(this.currentPos + 3);
            v4 = 4;
        }
        final CtClass v5 = /*EL:207*/Descriptor.toCtClass(v3, this.thisClass.getClassPool());
        final Javac v6 = /*EL:209*/new Javac(this.thisClass);
        final CodeAttribute v7 = /*EL:210*/this.iterator.get();
        final CtClass[] v8 = /*EL:212*/new CtClass[v2];
        /*SL:213*/for (int v9 = 0; v9 < v2; ++v9) {
            /*SL:214*/v8[v9] = CtClass.intType;
        }
        int v9 = /*EL:216*/v7.getMaxLocals();
        /*SL:217*/v6.recordParams("java.lang.Object", v8, true, v9, this.withinStatic());
        /*SL:222*/Expr.checkResultValue(v5, v-3);
        final int v10 = /*EL:223*/v6.recordReturnType(v5, true);
        /*SL:224*/v6.recordProceed(new ProceedForArray(v5, this.opcode, v2, v2));
        final Bytecode v11 = /*EL:226*/v6.getBytecode();
        /*SL:227*/Expr.storeStack(v8, true, v9, v11);
        /*SL:228*/v6.recordLocalVariables(v7, currentPos);
        /*SL:230*/v11.addOpcode(1);
        /*SL:231*/v11.addAstore(v10);
        /*SL:233*/v6.compileStmnt(v-3);
        /*SL:234*/v11.addAload(v10);
        /*SL:236*/this.replace0(currentPos, v11, v4);
    }
    
    static class ProceedForArray implements ProceedHandler
    {
        CtClass arrayType;
        int opcode;
        int index;
        int dimension;
        
        ProceedForArray(final CtClass a1, final int a2, final int a3, final int a4) {
            this.arrayType = a1;
            this.opcode = a2;
            this.index = a3;
            this.dimension = a4;
        }
        
        @Override
        public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
            final int v1 = /*EL:256*/a1.getMethodArgsLength(a3);
            /*SL:257*/if (v1 != this.dimension) {
                /*SL:258*/throw new CompileError("$proceed() with a wrong number of parameters");
            }
            /*SL:261*/a1.atMethodArgs(a3, new int[v1], new int[v1], new String[v1]);
            /*SL:263*/a2.addOpcode(this.opcode);
            /*SL:264*/if (this.opcode == 189) {
                /*SL:265*/a2.addIndex(this.index);
            }
            else/*SL:266*/ if (this.opcode == 188) {
                /*SL:267*/a2.add(this.index);
            }
            else {
                /*SL:269*/a2.addIndex(this.index);
                /*SL:270*/a2.add(this.dimension);
                /*SL:271*/a2.growStack(1 - this.dimension);
            }
            /*SL:274*/a1.setType(this.arrayType);
        }
        
        @Override
        public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
            /*SL:280*/a1.setType(this.arrayType);
        }
    }
}
