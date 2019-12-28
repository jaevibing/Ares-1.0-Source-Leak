package javassist.compiler;

import javassist.NotFoundException;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.CallExpr;
import javassist.CtPrimitiveType;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Visitor;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Expr;
import javassist.bytecode.Descriptor;
import javassist.compiler.ast.Member;
import javassist.ClassPool;
import javassist.bytecode.Bytecode;
import javassist.CtClass;

public class JvstCodeGen extends MemberCodeGen
{
    String paramArrayName;
    String paramListName;
    CtClass[] paramTypeList;
    private int paramVarBase;
    private boolean useParam0;
    private String param0Type;
    public static final String sigName = "$sig";
    public static final String dollarTypeName = "$type";
    public static final String clazzName = "$class";
    private CtClass dollarType;
    CtClass returnType;
    String returnCastName;
    private String returnVarName;
    public static final String wrapperCastName = "$w";
    String proceedName;
    public static final String cflowName = "$cflow";
    ProceedHandler procHandler;
    
    public JvstCodeGen(final Bytecode a1, final CtClass a2, final ClassPool a3) {
        super(a1, a2, a3);
        this.paramArrayName = null;
        this.paramListName = null;
        this.paramTypeList = null;
        this.paramVarBase = 0;
        this.useParam0 = false;
        this.param0Type = null;
        this.dollarType = null;
        this.returnType = null;
        this.returnCastName = null;
        this.returnVarName = null;
        this.proceedName = null;
        this.procHandler = null;
        this.setTypeChecker(new JvstTypeChecker(a2, a3, this));
    }
    
    private int indexOfParam1() {
        /*SL:53*/return this.paramVarBase + (this.useParam0 ? 1 : 0);
    }
    
    public void setProceedHandler(final ProceedHandler a1, final String a2) {
        /*SL:62*/this.proceedName = a2;
        /*SL:63*/this.procHandler = a1;
    }
    
    public void addNullIfVoid() {
        /*SL:70*/if (this.exprType == 344) {
            /*SL:71*/this.bytecode.addOpcode(1);
            /*SL:72*/this.exprType = 307;
            /*SL:73*/this.arrayDim = 0;
            /*SL:74*/this.className = "java/lang/Object";
        }
    }
    
    @Override
    public void atMember(final Member a1) throws CompileError {
        final String v1 = /*EL:82*/a1.get();
        /*SL:83*/if (v1.equals(this.paramArrayName)) {
            compileParameterList(/*EL:84*/this.bytecode, this.paramTypeList, this.indexOfParam1());
            /*SL:85*/this.exprType = 307;
            /*SL:86*/this.arrayDim = 1;
            /*SL:87*/this.className = "java/lang/Object";
        }
        else/*SL:89*/ if (v1.equals("$sig")) {
            /*SL:90*/this.bytecode.addLdc(Descriptor.ofMethod(this.returnType, this.paramTypeList));
            /*SL:91*/this.bytecode.addInvokestatic("javassist/runtime/Desc", "getParams", "(Ljava/lang/String;)[Ljava/lang/Class;");
            /*SL:93*/this.exprType = 307;
            /*SL:94*/this.arrayDim = 1;
            /*SL:95*/this.className = "java/lang/Class";
        }
        else/*SL:97*/ if (v1.equals("$type")) {
            /*SL:98*/if (this.dollarType == null) {
                /*SL:99*/throw new CompileError("$type is not available");
            }
            /*SL:101*/this.bytecode.addLdc(Descriptor.of(this.dollarType));
            /*SL:102*/this.callGetType("getType");
        }
        else/*SL:104*/ if (v1.equals("$class")) {
            /*SL:105*/if (this.param0Type == null) {
                /*SL:106*/throw new CompileError("$class is not available");
            }
            /*SL:108*/this.bytecode.addLdc(this.param0Type);
            /*SL:109*/this.callGetType("getClazz");
        }
        else {
            /*SL:112*/super.atMember(a1);
        }
    }
    
    private void callGetType(final String a1) {
        /*SL:116*/this.bytecode.addInvokestatic("javassist/runtime/Desc", a1, "(Ljava/lang/String;)Ljava/lang/Class;");
        /*SL:118*/this.exprType = 307;
        /*SL:119*/this.arrayDim = 0;
        /*SL:120*/this.className = "java/lang/Class";
    }
    
    @Override
    protected void atFieldAssign(final Expr a1, final int a2, final ASTree a3, final ASTree a4, final boolean a5) throws CompileError {
        /*SL:126*/if (a3 instanceof Member && ((Member)a3).get().equals(/*EL:127*/this.paramArrayName)) {
            /*SL:128*/if (a2 != 61) {
                /*SL:129*/throw new CompileError("bad operator for " + this.paramArrayName);
            }
            /*SL:131*/a4.accept(this);
            /*SL:132*/if (this.arrayDim != 1 || this.exprType != 307) {
                /*SL:133*/throw new CompileError("invalid type for " + this.paramArrayName);
            }
            /*SL:135*/this.atAssignParamList(this.paramTypeList, this.bytecode);
            /*SL:136*/if (!a5) {
                /*SL:137*/this.bytecode.addOpcode(87);
            }
        }
        else {
            /*SL:140*/super.atFieldAssign(a1, a2, a3, a4, a5);
        }
    }
    
    protected void atAssignParamList(final CtClass[] v1, final Bytecode v2) throws CompileError {
        /*SL:146*/if (v1 == null) {
            /*SL:147*/return;
        }
        int v3 = /*EL:149*/this.indexOfParam1();
        /*SL:151*/for (int v4 = v1.length, a1 = 0; a1 < v4; ++a1) {
            /*SL:152*/v2.addOpcode(89);
            /*SL:153*/v2.addIconst(a1);
            /*SL:154*/v2.addOpcode(50);
            /*SL:155*/this.compileUnwrapValue(v1[a1], v2);
            /*SL:156*/v2.addStore(v3, v1[a1]);
            /*SL:157*/v3 += (CodeGen.is2word(this.exprType, this.arrayDim) ? 2 : 1);
        }
    }
    
    @Override
    public void atCastExpr(final CastExpr v-1) throws CompileError {
        final ASTList v0 = /*EL:162*/v-1.getClassName();
        /*SL:163*/if (v0 != null && v-1.getArrayDim() == 0) {
            final ASTree v = /*EL:164*/v0.head();
            /*SL:165*/if (v instanceof Symbol && v0.tail() == null) {
                final String a1 = /*EL:166*/((Symbol)v).get();
                /*SL:167*/if (a1.equals(this.returnCastName)) {
                    /*SL:168*/this.atCastToRtype(v-1);
                    /*SL:169*/return;
                }
                /*SL:171*/if (a1.equals("$w")) {
                    /*SL:172*/this.atCastToWrapper(v-1);
                    /*SL:173*/return;
                }
            }
        }
        /*SL:178*/super.atCastExpr(v-1);
    }
    
    protected void atCastToRtype(final CastExpr v-1) throws CompileError {
        /*SL:186*/v-1.getOprand().accept(this);
        /*SL:187*/if (this.exprType == 344 || CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            /*SL:188*/this.compileUnwrapValue(this.returnType, this.bytecode);
        }
        else {
            /*SL:189*/if (!(this.returnType instanceof CtPrimitiveType)) {
                /*SL:198*/throw new CompileError("invalid cast");
            }
            final CtPrimitiveType a1 = (CtPrimitiveType)this.returnType;
            final int v1 = MemberResolver.descToType(a1.getDescriptor());
            this.atNumCastExpr(this.exprType, v1);
            this.exprType = v1;
            this.arrayDim = 0;
            this.className = null;
        }
    }
    
    protected void atCastToWrapper(final CastExpr v-2) throws CompileError {
        /*SL:202*/v-2.getOprand().accept(this);
        /*SL:203*/if (CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            /*SL:204*/return;
        }
        final CtClass lookupClass = /*EL:206*/this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
        /*SL:207*/if (lookupClass instanceof CtPrimitiveType) {
            final CtPrimitiveType a1 = /*EL:208*/(CtPrimitiveType)lookupClass;
            final String v1 = /*EL:209*/a1.getWrapperName();
            /*SL:210*/this.bytecode.addNew(v1);
            /*SL:211*/this.bytecode.addOpcode(89);
            /*SL:212*/if (a1.getDataSize() > 1) {
                /*SL:213*/this.bytecode.addOpcode(94);
            }
            else {
                /*SL:215*/this.bytecode.addOpcode(93);
            }
            /*SL:217*/this.bytecode.addOpcode(88);
            /*SL:218*/this.bytecode.addInvokespecial(v1, "<init>", "(" + a1.getDescriptor() + /*EL:219*/")V");
            /*SL:221*/this.exprType = 307;
            /*SL:222*/this.arrayDim = 0;
            /*SL:223*/this.className = "java/lang/Object";
        }
    }
    
    @Override
    public void atCallExpr(final CallExpr v2) throws CompileError {
        final ASTree v3 = /*EL:231*/v2.oprand1();
        /*SL:232*/if (v3 instanceof Member) {
            final String a1 = /*EL:233*/((Member)v3).get();
            /*SL:234*/if (this.procHandler != null && a1.equals(this.proceedName)) {
                /*SL:235*/this.procHandler.doit(this, this.bytecode, (ASTList)v2.oprand2());
                /*SL:236*/return;
            }
            /*SL:238*/if (a1.equals("$cflow")) {
                /*SL:239*/this.atCflow((ASTList)v2.oprand2());
                /*SL:240*/return;
            }
        }
        /*SL:244*/super.atCallExpr(v2);
    }
    
    protected void atCflow(final ASTList a1) throws CompileError {
        final StringBuffer v1 = /*EL:250*/new StringBuffer();
        /*SL:251*/if (a1 == null || a1.tail() != null) {
            /*SL:252*/throw new CompileError("bad $cflow");
        }
        makeCflowName(/*EL:254*/v1, a1.head());
        final String v2 = /*EL:255*/v1.toString();
        final Object[] v3 = /*EL:256*/this.resolver.getClassPool().lookupCflow(v2);
        /*SL:257*/if (v3 == null) {
            /*SL:258*/throw new CompileError("no such $cflow: " + v2);
        }
        /*SL:260*/this.bytecode.addGetstatic((String)v3[0], (String)v3[1], "Ljavassist/runtime/Cflow;");
        /*SL:262*/this.bytecode.addInvokevirtual("javassist.runtime.Cflow", "value", "()I");
        /*SL:264*/this.exprType = 324;
        /*SL:265*/this.arrayDim = 0;
        /*SL:266*/this.className = null;
    }
    
    private static void makeCflowName(final StringBuffer a2, final ASTree v1) throws CompileError {
        /*SL:277*/if (v1 instanceof Symbol) {
            /*SL:278*/a2.append(((Symbol)v1).get());
            /*SL:279*/return;
        }
        /*SL:281*/if (v1 instanceof Expr) {
            final Expr a3 = /*EL:282*/(Expr)v1;
            /*SL:283*/if (a3.getOperator() == 46) {
                makeCflowName(/*EL:284*/a2, a3.oprand1());
                /*SL:285*/a2.append('.');
                makeCflowName(/*EL:286*/a2, a3.oprand2());
                /*SL:287*/return;
            }
        }
        /*SL:291*/throw new CompileError("bad $cflow");
    }
    
    public boolean isParamListName(final ASTList v2) {
        /*SL:298*/if (this.paramTypeList != null && v2 != null && v2.tail() == /*EL:299*/null) {
            final ASTree a1 = /*EL:300*/v2.head();
            /*SL:302*/return a1 instanceof Member && ((Member)a1).get().equals(this.paramListName);
        }
        /*SL:305*/return false;
    }
    
    @Override
    public int getMethodArgsLength(ASTList v2) {
        final String v3 = /*EL:318*/this.paramListName;
        int v4 = /*EL:319*/0;
        /*SL:320*/while (v2 != null) {
            final ASTree a1 = /*EL:321*/v2.head();
            /*SL:322*/if (a1 instanceof Member && ((Member)a1).get().equals(v3)) {
                /*SL:323*/if (this.paramTypeList != null) {
                    /*SL:324*/v4 += this.paramTypeList.length;
                }
            }
            else {
                /*SL:327*/++v4;
            }
            /*SL:329*/v2 = v2.tail();
        }
        /*SL:332*/return v4;
    }
    
    @Override
    public void atMethodArgs(ASTList v-6, final int[] v-5, final int[] v-4, final String[] v-3) throws CompileError {
        final CtClass[] paramTypeList = /*EL:337*/this.paramTypeList;
        final String paramListName = /*EL:338*/this.paramListName;
        int v0 = /*EL:339*/0;
        /*SL:340*/while (v-6 != null) {
            final ASTree v = /*EL:341*/v-6.head();
            /*SL:342*/if (v instanceof Member && ((Member)v).get().equals(paramListName)) {
                /*SL:343*/if (paramTypeList != null) {
                    int a3 = /*EL:344*/paramTypeList.length;
                    int a2 = /*EL:345*/this.indexOfParam1();
                    /*SL:346*/for (a3 = 0; a3 < a3; ++a3) {
                        final CtClass a4 = /*EL:347*/paramTypeList[a3];
                        /*SL:348*/a2 += this.bytecode.addLoad(a2, a4);
                        /*SL:349*/this.setType(a4);
                        /*SL:350*/v-5[v0] = this.exprType;
                        /*SL:351*/v-4[v0] = this.arrayDim;
                        /*SL:352*/v-3[v0] = this.className;
                        /*SL:353*/++v0;
                    }
                }
            }
            else {
                /*SL:358*/v.accept(this);
                /*SL:359*/v-5[v0] = this.exprType;
                /*SL:360*/v-4[v0] = this.arrayDim;
                /*SL:361*/v-3[v0] = this.className;
                /*SL:362*/++v0;
            }
            /*SL:365*/v-6 = v-6.tail();
        }
    }
    
    void compileInvokeSpecial(final ASTree a1, final int a2, final String a3, final ASTList a4) throws CompileError {
        /*SL:400*/a1.accept(this);
        final int v1 = /*EL:401*/this.getMethodArgsLength(a4);
        /*SL:402*/this.atMethodArgs(a4, new int[v1], new int[v1], new String[v1]);
        /*SL:404*/this.bytecode.addInvokespecial(a2, a3);
        /*SL:405*/this.setReturnType(a3, false, false);
        /*SL:406*/this.addNullIfVoid();
    }
    
    @Override
    protected void atReturnStmnt(final Stmnt a1) throws CompileError {
        ASTree v1 = /*EL:413*/a1.getLeft();
        /*SL:414*/if (v1 != null && this.returnType == CtClass.voidType) {
            /*SL:415*/this.compileExpr(v1);
            /*SL:416*/if (CodeGen.is2word(this.exprType, this.arrayDim)) {
                /*SL:417*/this.bytecode.addOpcode(88);
            }
            else/*SL:418*/ if (this.exprType != 344) {
                /*SL:419*/this.bytecode.addOpcode(87);
            }
            /*SL:421*/v1 = null;
        }
        /*SL:424*/this.atReturnStmnt2(v1);
    }
    
    public int recordReturnType(final CtClass a4, final String v1, final String v2, final SymbolTable v3) throws CompileError {
        /*SL:440*/this.returnType = a4;
        /*SL:441*/this.returnCastName = v1;
        /*SL:442*/this.returnVarName = v2;
        /*SL:443*/if (v2 == null) {
            /*SL:444*/return -1;
        }
        final int a5 = /*EL:446*/this.getMaxLocals();
        final int a6 = /*EL:447*/a5 + this.recordVar(a4, v2, a5, v3);
        /*SL:448*/this.setMaxLocals(a6);
        /*SL:449*/return a5;
    }
    
    public void recordType(final CtClass a1) {
        /*SL:457*/this.dollarType = a1;
    }
    
    public int recordParams(final CtClass[] a1, final boolean a2, final String a3, final String a4, final String a5, final SymbolTable a6) throws CompileError {
        /*SL:470*/return this.recordParams(a1, a2, a3, a4, a5, !a2, 0, this.getThisName(), /*EL:471*/a6);
    }
    
    public int recordParams(final CtClass[] a5, final boolean a6, final String a7, final String a8, final String a9, final boolean v1, final int v2, final String v3, final SymbolTable v4) throws CompileError {
        /*SL:501*/this.paramTypeList = a5;
        /*SL:502*/this.paramArrayName = a8;
        /*SL:503*/this.paramListName = a9;
        /*SL:504*/this.paramVarBase = v2;
        /*SL:505*/this.useParam0 = v1;
        /*SL:507*/if (v3 != null) {
            /*SL:508*/this.param0Type = MemberResolver.jvmToJavaName(v3);
        }
        /*SL:510*/this.inStaticMethod = a6;
        int v5 = /*EL:511*/v2;
        /*SL:512*/if (v1) {
            final String a10 = /*EL:513*/a7 + "0";
            final Declarator a11 = /*EL:514*/new Declarator(307, /*EL:515*/MemberResolver.javaToJvmName(v3), 0, v5++, new Symbol(a10));
            /*SL:517*/v4.append(a10, a11);
        }
        /*SL:520*/for (int a12 = 0; a12 < a5.length; ++a12) {
            /*SL:521*/v5 += this.recordVar(a5[a12], a7 + (a12 + 1), v5, v4);
        }
        /*SL:523*/if (this.getMaxLocals() < v5) {
            /*SL:524*/this.setMaxLocals(v5);
        }
        /*SL:526*/return v5;
    }
    
    public int recordVariable(final CtClass v1, final String v2, final SymbolTable v3) throws CompileError {
        /*SL:538*/if (v2 == null) {
            /*SL:539*/return -1;
        }
        final int a1 = /*EL:541*/this.getMaxLocals();
        final int a2 = /*EL:542*/a1 + this.recordVar(v1, v2, a1, v3);
        /*SL:543*/this.setMaxLocals(a2);
        /*SL:544*/return a1;
    }
    
    private int recordVar(final CtClass a1, final String a2, final int a3, final SymbolTable a4) throws CompileError {
        /*SL:551*/if (a1 == CtClass.voidType) {
            /*SL:552*/this.exprType = 307;
            /*SL:553*/this.arrayDim = 0;
            /*SL:554*/this.className = "java/lang/Object";
        }
        else {
            /*SL:557*/this.setType(a1);
        }
        final Declarator v1 = /*EL:559*/new Declarator(this.exprType, this.className, this.arrayDim, a3, new Symbol(a2));
        /*SL:562*/a4.append(a2, v1);
        /*SL:563*/return CodeGen.is2word(this.exprType, this.arrayDim) ? 2 : 1;
    }
    
    public void recordVariable(final String a1, final String a2, final int a3, final SymbolTable a4) throws CompileError {
        int v2;
        char v2;
        /*SL:578*/for (v2 = 0; (v2 = a1.charAt(v2)) == '['; /*SL:579*/++v2) {}
        final int v3 = /*EL:581*/MemberResolver.descToType(v2);
        String v4 = /*EL:582*/null;
        /*SL:583*/if (v3 == 307) {
            /*SL:584*/if (v2 == 0) {
                /*SL:585*/v4 = a1.substring(1, a1.length() - 1);
            }
            else {
                /*SL:587*/v4 = a1.substring(v2 + 1, a1.length() - 1);
            }
        }
        final Declarator v5 = /*EL:590*/new Declarator(v3, v4, v2, a3, new Symbol(a2));
        /*SL:592*/a4.append(a2, v5);
    }
    
    public static int compileParameterList(final Bytecode v-4, final CtClass[] v-3, int v-2) {
        /*SL:606*/if (v-3 == null) {
            /*SL:607*/v-4.addIconst(0);
            /*SL:608*/v-4.addAnewarray("java.lang.Object");
            /*SL:609*/return 1;
        }
        final CtClass[] v2 = /*EL:612*/{ null };
        final int v0 = /*EL:613*/v-3.length;
        /*SL:614*/v-4.addIconst(v0);
        /*SL:615*/v-4.addAnewarray("java.lang.Object");
        /*SL:616*/for (int v = 0; v < v0; ++v) {
            /*SL:617*/v-4.addOpcode(89);
            /*SL:618*/v-4.addIconst(v);
            /*SL:619*/if (v-3[v].isPrimitive()) {
                final CtPrimitiveType a1 = /*EL:620*/(CtPrimitiveType)v-3[v];
                final String a2 = /*EL:621*/a1.getWrapperName();
                /*SL:622*/v-4.addNew(a2);
                /*SL:623*/v-4.addOpcode(89);
                final int a3 = /*EL:624*/v-4.addLoad(v-2, a1);
                /*SL:625*/v-2 += a3;
                /*SL:626*/v2[0] = a1;
                /*SL:627*/v-4.addInvokespecial(a2, "<init>", /*EL:628*/Descriptor.ofMethod(CtClass.voidType, v2));
            }
            else {
                /*SL:632*/v-4.addAload(v-2);
                /*SL:633*/++v-2;
            }
            /*SL:636*/v-4.addOpcode(83);
        }
        /*SL:639*/return 8;
    }
    
    protected void compileUnwrapValue(final CtClass v2, final Bytecode v3) throws CompileError {
        /*SL:646*/if (v2 == CtClass.voidType) {
            /*SL:647*/this.addNullIfVoid();
            /*SL:648*/return;
        }
        /*SL:651*/if (this.exprType == 344) {
            /*SL:652*/throw new CompileError("invalid type for " + this.returnCastName);
        }
        /*SL:654*/if (v2 instanceof CtPrimitiveType) {
            final CtPrimitiveType a1 = /*EL:655*/(CtPrimitiveType)v2;
            final String a2 = /*EL:657*/a1.getWrapperName();
            /*SL:658*/v3.addCheckcast(a2);
            /*SL:659*/v3.addInvokevirtual(a2, a1.getGetMethodName(), a1.getGetMethodDescriptor());
            /*SL:661*/this.setType(v2);
        }
        else {
            /*SL:664*/v3.addCheckcast(v2);
            /*SL:665*/this.setType(v2);
        }
    }
    
    public void setType(final CtClass a1) throws CompileError {
        /*SL:673*/this.setType(a1, 0);
    }
    
    private void setType(final CtClass v2, final int v3) throws CompileError {
        /*SL:677*/if (v2.isPrimitive()) {
            final CtPrimitiveType a1 = /*EL:678*/(CtPrimitiveType)v2;
            /*SL:679*/this.exprType = MemberResolver.descToType(a1.getDescriptor());
            /*SL:680*/this.arrayDim = v3;
            /*SL:681*/this.className = null;
        }
        else {
            /*SL:683*/if (v2.isArray()) {
                try {
                    /*SL:685*/this.setType(v2.getComponentType(), v3 + 1);
                    /*SL:689*/return;
                }
                catch (NotFoundException a2) {
                    throw new CompileError("undefined type: " + v2.getName());
                }
            }
            /*SL:691*/this.exprType = 307;
            /*SL:692*/this.arrayDim = v3;
            /*SL:693*/this.className = MemberResolver.javaToJvmName(v2.getName());
        }
    }
    
    public void doNumCast(final CtClass v2) throws CompileError {
        /*SL:700*/if (this.arrayDim == 0 && !CodeGen.isRefType(this.exprType)) {
            /*SL:701*/if (!(v2 instanceof CtPrimitiveType)) {
                /*SL:707*/throw new CompileError("type mismatch");
            }
            final CtPrimitiveType a1 = (CtPrimitiveType)v2;
            this.atNumCastExpr(this.exprType, MemberResolver.descToType(a1.getDescriptor()));
        }
    }
}
