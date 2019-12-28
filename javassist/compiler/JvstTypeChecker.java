package javassist.compiler;

import javassist.NotFoundException;
import javassist.compiler.ast.CallExpr;
import javassist.CtPrimitiveType;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Visitor;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Member;
import javassist.ClassPool;
import javassist.CtClass;

public class JvstTypeChecker extends TypeChecker
{
    private JvstCodeGen codeGen;
    
    public JvstTypeChecker(final CtClass a1, final ClassPool a2, final JvstCodeGen a3) {
        super(a1, a2);
        this.codeGen = a3;
    }
    
    public void addNullIfVoid() {
        /*SL:37*/if (this.exprType == 344) {
            /*SL:38*/this.exprType = 307;
            /*SL:39*/this.arrayDim = 0;
            /*SL:40*/this.className = "java/lang/Object";
        }
    }
    
    @Override
    public void atMember(final Member a1) throws CompileError {
        final String v1 = /*EL:48*/a1.get();
        /*SL:49*/if (v1.equals(this.codeGen.paramArrayName)) {
            /*SL:50*/this.exprType = 307;
            /*SL:51*/this.arrayDim = 1;
            /*SL:52*/this.className = "java/lang/Object";
        }
        else/*SL:54*/ if (v1.equals("$sig")) {
            /*SL:55*/this.exprType = 307;
            /*SL:56*/this.arrayDim = 1;
            /*SL:57*/this.className = "java/lang/Class";
        }
        else/*SL:59*/ if (v1.equals("$type") || v1.equals("$class")) {
            /*SL:61*/this.exprType = 307;
            /*SL:62*/this.arrayDim = 0;
            /*SL:63*/this.className = "java/lang/Class";
        }
        else {
            /*SL:66*/super.atMember(a1);
        }
    }
    
    @Override
    protected void atFieldAssign(final Expr v1, final int v2, final ASTree v3, final ASTree v4) throws CompileError {
        /*SL:72*/if (v3 instanceof Member && ((Member)v3).get().equals(/*EL:73*/this.codeGen.paramArrayName)) {
            /*SL:74*/v4.accept(this);
            CtClass[] a2 = /*EL:75*/this.codeGen.paramTypeList;
            /*SL:76*/if (a2 == null) {
                /*SL:77*/return;
            }
            /*SL:79*/a2 = a2.length;
            /*SL:80*/for (int a3 = 0; a3 < a2; ++a3) {
                /*SL:81*/this.compileUnwrapValue(a2[a3]);
            }
        }
        else {
            /*SL:84*/super.atFieldAssign(v1, v2, v3, v4);
        }
    }
    
    @Override
    public void atCastExpr(final CastExpr v-1) throws CompileError {
        final ASTList v0 = /*EL:88*/v-1.getClassName();
        /*SL:89*/if (v0 != null && v-1.getArrayDim() == 0) {
            final ASTree v = /*EL:90*/v0.head();
            /*SL:91*/if (v instanceof Symbol && v0.tail() == null) {
                final String a1 = /*EL:92*/((Symbol)v).get();
                /*SL:93*/if (a1.equals(this.codeGen.returnCastName)) {
                    /*SL:94*/this.atCastToRtype(v-1);
                    /*SL:95*/return;
                }
                /*SL:97*/if (a1.equals("$w")) {
                    /*SL:98*/this.atCastToWrapper(v-1);
                    /*SL:99*/return;
                }
            }
        }
        /*SL:104*/super.atCastExpr(v-1);
    }
    
    protected void atCastToRtype(final CastExpr v-2) throws CompileError {
        final CtClass returnType = /*EL:112*/this.codeGen.returnType;
        /*SL:113*/v-2.getOprand().accept(this);
        /*SL:114*/if (this.exprType == 344 || CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            /*SL:115*/this.compileUnwrapValue(returnType);
        }
        else/*SL:116*/ if (returnType instanceof CtPrimitiveType) {
            final CtPrimitiveType a1 = /*EL:117*/(CtPrimitiveType)returnType;
            final int v1 = /*EL:118*/MemberResolver.descToType(a1.getDescriptor());
            /*SL:119*/this.exprType = v1;
            /*SL:120*/this.arrayDim = 0;
            /*SL:121*/this.className = null;
        }
    }
    
    protected void atCastToWrapper(final CastExpr a1) throws CompileError {
        /*SL:126*/a1.getOprand().accept(this);
        /*SL:127*/if (CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            /*SL:128*/return;
        }
        final CtClass v1 = /*EL:130*/this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
        /*SL:131*/if (v1 instanceof CtPrimitiveType) {
            /*SL:132*/this.exprType = 307;
            /*SL:133*/this.arrayDim = 0;
            /*SL:134*/this.className = "java/lang/Object";
        }
    }
    
    @Override
    public void atCallExpr(final CallExpr v2) throws CompileError {
        final ASTree v3 = /*EL:142*/v2.oprand1();
        /*SL:143*/if (v3 instanceof Member) {
            final String a1 = /*EL:144*/((Member)v3).get();
            /*SL:145*/if (this.codeGen.procHandler != null && a1.equals(this.codeGen.proceedName)) {
                /*SL:147*/this.codeGen.procHandler.setReturnType(this, (ASTList)v2.oprand2());
                /*SL:149*/return;
            }
            /*SL:151*/if (a1.equals("$cflow")) {
                /*SL:152*/this.atCflow((ASTList)v2.oprand2());
                /*SL:153*/return;
            }
        }
        /*SL:157*/super.atCallExpr(v2);
    }
    
    protected void atCflow(final ASTList a1) throws CompileError {
        /*SL:163*/this.exprType = 324;
        /*SL:164*/this.arrayDim = 0;
        /*SL:165*/this.className = null;
    }
    
    public boolean isParamListName(final ASTList v2) {
        /*SL:172*/if (this.codeGen.paramTypeList != null && v2 != null && v2.tail() == /*EL:173*/null) {
            final ASTree a1 = /*EL:174*/v2.head();
            /*SL:176*/return a1 instanceof Member && ((Member)a1).get().equals(this.codeGen.paramListName);
        }
        /*SL:179*/return false;
    }
    
    @Override
    public int getMethodArgsLength(ASTList v2) {
        final String v3 = /*EL:183*/this.codeGen.paramListName;
        int v4 = /*EL:184*/0;
        /*SL:185*/while (v2 != null) {
            final ASTree a1 = /*EL:186*/v2.head();
            /*SL:187*/if (a1 instanceof Member && ((Member)a1).get().equals(v3)) {
                /*SL:188*/if (this.codeGen.paramTypeList != null) {
                    /*SL:189*/v4 += this.codeGen.paramTypeList.length;
                }
            }
            else {
                /*SL:192*/++v4;
            }
            /*SL:194*/v2 = v2.tail();
        }
        /*SL:197*/return v4;
    }
    
    @Override
    public void atMethodArgs(ASTList v2, final int[] v3, final int[] v4, final String[] v5) throws CompileError {
        final CtClass[] v6 = /*EL:202*/this.codeGen.paramTypeList;
        final String v7 = /*EL:203*/this.codeGen.paramListName;
        int v8 = /*EL:204*/0;
        /*SL:205*/while (v2 != null) {
            ASTree a4 = /*EL:206*/v2.head();
            /*SL:207*/if (a4 instanceof Member && ((Member)a4).get().equals(v7)) {
                /*SL:208*/if (v6 != null) {
                    /*SL:210*/for (int a2 = v6.length, a3 = 0; a3 < a2; ++a3) {
                        /*SL:211*/a4 = v6[a3];
                        /*SL:212*/this.setType(a4);
                        /*SL:213*/v3[v8] = this.exprType;
                        /*SL:214*/v4[v8] = this.arrayDim;
                        /*SL:215*/v5[v8] = this.className;
                        /*SL:216*/++v8;
                    }
                }
            }
            else {
                /*SL:221*/a4.accept(this);
                /*SL:222*/v3[v8] = this.exprType;
                /*SL:223*/v4[v8] = this.arrayDim;
                /*SL:224*/v5[v8] = this.className;
                /*SL:225*/++v8;
            }
            /*SL:228*/v2 = v2.tail();
        }
    }
    
    void compileInvokeSpecial(final ASTree a1, final String a2, final String a3, final String a4, final ASTList a5) throws CompileError {
        /*SL:239*/a1.accept(this);
        final int v1 = /*EL:240*/this.getMethodArgsLength(a5);
        /*SL:241*/this.atMethodArgs(a5, new int[v1], new int[v1], new String[v1]);
        /*SL:243*/this.setReturnType(a4);
        /*SL:244*/this.addNullIfVoid();
    }
    
    protected void compileUnwrapValue(final CtClass a1) throws CompileError {
        /*SL:249*/if (a1 == CtClass.voidType) {
            /*SL:250*/this.addNullIfVoid();
        }
        else {
            /*SL:252*/this.setType(a1);
        }
    }
    
    public void setType(final CtClass a1) throws CompileError {
        /*SL:259*/this.setType(a1, 0);
    }
    
    private void setType(final CtClass v2, final int v3) throws CompileError {
        /*SL:263*/if (v2.isPrimitive()) {
            final CtPrimitiveType a1 = /*EL:264*/(CtPrimitiveType)v2;
            /*SL:265*/this.exprType = MemberResolver.descToType(a1.getDescriptor());
            /*SL:266*/this.arrayDim = v3;
            /*SL:267*/this.className = null;
        }
        else {
            /*SL:269*/if (v2.isArray()) {
                try {
                    /*SL:271*/this.setType(v2.getComponentType(), v3 + 1);
                    /*SL:275*/return;
                }
                catch (NotFoundException a2) {
                    throw new CompileError("undefined type: " + v2.getName());
                }
            }
            /*SL:277*/this.exprType = 307;
            /*SL:278*/this.arrayDim = v3;
            /*SL:279*/this.className = MemberResolver.javaToJvmName(v2.getName());
        }
    }
}
