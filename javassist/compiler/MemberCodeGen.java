package javassist.compiler;

import javassist.compiler.ast.MethodDecl;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.Modifier;
import javassist.bytecode.Descriptor;
import javassist.bytecode.AccessFlag;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Visitor;
import java.util.ArrayList;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.Stmnt;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;
import javassist.ClassPool;
import javassist.bytecode.Bytecode;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;

public class MemberCodeGen extends CodeGen
{
    protected MemberResolver resolver;
    protected CtClass thisClass;
    protected MethodInfo thisMethod;
    protected boolean resultStatic;
    
    public MemberCodeGen(final Bytecode a1, final CtClass a2, final ClassPool a3) {
        super(a1);
        this.resolver = new MemberResolver(a3);
        this.thisClass = a2;
        this.thisMethod = null;
    }
    
    public int getMajorVersion() {
        final ClassFile v1 = /*EL:46*/this.thisClass.getClassFile2();
        /*SL:47*/if (v1 == null) {
            /*SL:48*/return ClassFile.MAJOR_VERSION;
        }
        /*SL:50*/return v1.getMajorVersion();
    }
    
    public void setThisMethod(final CtMethod a1) {
        /*SL:57*/this.thisMethod = a1.getMethodInfo2();
        /*SL:58*/if (this.typeChecker != null) {
            /*SL:59*/this.typeChecker.setThisMethod(this.thisMethod);
        }
    }
    
    public CtClass getThisClass() {
        /*SL:62*/return this.thisClass;
    }
    
    @Override
    protected String getThisName() {
        /*SL:68*/return MemberResolver.javaToJvmName(this.thisClass.getName());
    }
    
    @Override
    protected String getSuperName() throws CompileError {
        /*SL:75*/return MemberResolver.javaToJvmName(/*EL:76*/MemberResolver.getSuperclass(this.thisClass).getName());
    }
    
    @Override
    protected void insertDefaultSuperCall() throws CompileError {
        /*SL:80*/this.bytecode.addAload(0);
        /*SL:81*/this.bytecode.addInvokespecial(MemberResolver.getSuperclass(this.thisClass), "<init>", "()V");
    }
    
    @Override
    protected void atTryStmnt(final Stmnt v-11) throws CompileError {
        final Bytecode bytecode = /*EL:190*/this.bytecode;
        final Stmnt stmnt = /*EL:191*/(Stmnt)v-11.getLeft();
        /*SL:192*/if (stmnt == null) {
            /*SL:193*/return;
        }
        ASTList tail = /*EL:195*/(ASTList)v-11.getRight().getLeft();
        final Stmnt v-12 = /*EL:196*/(Stmnt)v-11.getRight().getRight().getLeft();
        final ArrayList v5 = /*EL:197*/new ArrayList();
        JsrHook jsrHook = /*EL:199*/null;
        /*SL:200*/if (v-12 != null) {
            /*SL:201*/jsrHook = new JsrHook(this);
        }
        final int currentPc = /*EL:203*/bytecode.currentPc();
        /*SL:204*/stmnt.accept(this);
        final int currentPc2 = /*EL:205*/bytecode.currentPc();
        /*SL:206*/if (currentPc == currentPc2) {
            /*SL:207*/throw new CompileError("empty try block");
        }
        boolean b = /*EL:209*/!this.hasReturned;
        /*SL:210*/if (b) {
            /*SL:211*/bytecode.addOpcode(167);
            /*SL:212*/v5.add(new Integer(bytecode.currentPc()));
            /*SL:213*/bytecode.addIndex(0);
        }
        final int maxLocals = /*EL:216*/this.getMaxLocals();
        /*SL:217*/this.incMaxLocals(1);
        /*SL:218*/while (tail != null) {
            final Pair a1 = /*EL:220*/(Pair)tail.head();
            /*SL:221*/tail = tail.tail();
            final Declarator v1 = /*EL:222*/(Declarator)a1.getLeft();
            final Stmnt v2 = /*EL:223*/(Stmnt)a1.getRight();
            /*SL:225*/v1.setLocalVar(maxLocals);
            final CtClass v3 = /*EL:227*/this.resolver.lookupClassByJvmName(v1.getClassName());
            /*SL:228*/v1.setClassName(MemberResolver.javaToJvmName(v3.getName()));
            /*SL:229*/bytecode.addExceptionHandler(currentPc, currentPc2, bytecode.currentPc(), v3);
            /*SL:230*/bytecode.growStack(1);
            /*SL:231*/bytecode.addAstore(maxLocals);
            /*SL:232*/this.hasReturned = false;
            /*SL:233*/if (v2 != null) {
                /*SL:234*/v2.accept(this);
            }
            /*SL:236*/if (!this.hasReturned) {
                /*SL:237*/bytecode.addOpcode(167);
                /*SL:238*/v5.add(new Integer(bytecode.currentPc()));
                /*SL:239*/bytecode.addIndex(0);
                /*SL:240*/b = true;
            }
        }
        /*SL:244*/if (v-12 != null) {
            /*SL:245*/jsrHook.remove(this);
            final int v4 = /*EL:247*/bytecode.currentPc();
            /*SL:248*/bytecode.addExceptionHandler(currentPc, v4, v4, 0);
            /*SL:249*/bytecode.growStack(1);
            /*SL:250*/bytecode.addAstore(maxLocals);
            /*SL:251*/this.hasReturned = false;
            /*SL:252*/v-12.accept(this);
            /*SL:253*/if (!this.hasReturned) {
                /*SL:254*/bytecode.addAload(maxLocals);
                /*SL:255*/bytecode.addOpcode(191);
            }
            /*SL:258*/this.addFinally(jsrHook.jsrList, v-12);
        }
        final int v4 = /*EL:261*/bytecode.currentPc();
        /*SL:262*/this.patchGoto(v5, v4);
        /*SL:263*/this.hasReturned = !b;
        /*SL:264*/if (v-12 != null && /*EL:265*/b) {
            /*SL:266*/v-12.accept(this);
        }
    }
    
    private void addFinally(final ArrayList v-6, final Stmnt v-5) throws CompileError {
        final Bytecode bytecode = /*EL:276*/this.bytecode;
        /*SL:278*/for (int size = v-6.size(), i = 0; i < size; ++i) {
            final int[] a1 = /*EL:279*/v-6.get(i);
            final int a2 = /*EL:280*/a1[0];
            /*SL:281*/bytecode.write16bit(a2, bytecode.currentPc() - a2 + 1);
            final ReturnHook v1 = /*EL:282*/new JsrHook2(this, a1);
            /*SL:283*/v-5.accept(this);
            /*SL:284*/v1.remove(this);
            /*SL:285*/if (!this.hasReturned) {
                /*SL:286*/bytecode.addOpcode(167);
                /*SL:287*/bytecode.addIndex(a2 + 3 - bytecode.currentPc());
            }
        }
    }
    
    @Override
    public void atNewExpr(final NewExpr v-1) throws CompileError {
        /*SL:293*/if (v-1.isArray()) {
            /*SL:294*/this.atNewArrayExpr(v-1);
        }
        else {
            final CtClass a1 = /*EL:296*/this.resolver.lookupClassByName(v-1.getClassName());
            final String v1 = /*EL:297*/a1.getName();
            final ASTList v2 = /*EL:298*/v-1.getArguments();
            /*SL:299*/this.bytecode.addNew(v1);
            /*SL:300*/this.bytecode.addOpcode(89);
            /*SL:302*/this.atMethodCallCore(a1, "<init>", v2, false, true, -1, null);
            /*SL:305*/this.exprType = 307;
            /*SL:306*/this.arrayDim = 0;
            /*SL:307*/this.className = MemberResolver.javaToJvmName(v1);
        }
    }
    
    public void atNewArrayExpr(final NewExpr a1) throws CompileError {
        final int v1 = /*EL:312*/a1.getArrayType();
        final ASTList v2 = /*EL:313*/a1.getArraySize();
        final ASTList v3 = /*EL:314*/a1.getClassName();
        final ArrayInit v4 = /*EL:315*/a1.getInitializer();
        /*SL:316*/if (v2.length() <= 1) {
            final ASTree v5 = /*EL:326*/v2.head();
            /*SL:327*/this.atNewArrayExpr2(v1, v5, Declarator.astToClassName(v3, '/'), v4);
            /*SL:328*/return;
        }
        if (v4 != null) {
            throw new CompileError("sorry, multi-dimensional array initializer for new is not supported");
        }
        this.atMultiNewArray(v1, v3, v2);
    }
    
    private void atNewArrayExpr2(final int v-4, final ASTree v-3, final String v-2, final ArrayInit v-1) throws CompileError {
        /*SL:332*/if (v-1 == null) {
            /*SL:333*/if (v-3 == null) {
                /*SL:334*/throw new CompileError("no array size");
            }
            /*SL:336*/v-3.accept(this);
        }
        else {
            /*SL:338*/if (v-3 != null) {
                /*SL:343*/throw new CompileError("unnecessary array size specified for new");
            }
            final int a1 = v-1.length();
            this.bytecode.addIconst(a1);
        }
        final String v0;
        /*SL:346*/if (v-4 == 307) {
            final String a2 = /*EL:347*/this.resolveClassName(v-2);
            /*SL:348*/this.bytecode.addAnewarray(MemberResolver.jvmToJavaName(a2));
        }
        else {
            /*SL:351*/v0 = null;
            int a3 = /*EL:352*/0;
            /*SL:353*/switch (v-4) {
                case 301: {
                    /*SL:355*/a3 = 4;
                    /*SL:356*/break;
                }
                case 306: {
                    /*SL:358*/a3 = 5;
                    /*SL:359*/break;
                }
                case 317: {
                    /*SL:361*/a3 = 6;
                    /*SL:362*/break;
                }
                case 312: {
                    /*SL:364*/a3 = 7;
                    /*SL:365*/break;
                }
                case 303: {
                    /*SL:367*/a3 = 8;
                    /*SL:368*/break;
                }
                case 334: {
                    /*SL:370*/a3 = 9;
                    /*SL:371*/break;
                }
                case 324: {
                    /*SL:373*/a3 = 10;
                    /*SL:374*/break;
                }
                case 326: {
                    /*SL:376*/a3 = 11;
                    /*SL:377*/break;
                }
                default: {
                    badNewExpr();
                    break;
                }
            }
            /*SL:383*/this.bytecode.addOpcode(188);
            /*SL:384*/this.bytecode.add(a3);
        }
        /*SL:387*/if (v-1 != null) {
            final int v = /*EL:388*/v-1.length();
            ASTList v2 = /*EL:389*/v-1;
            /*SL:390*/for (int a4 = 0; a4 < v; ++a4) {
                /*SL:391*/this.bytecode.addOpcode(89);
                /*SL:392*/this.bytecode.addIconst(a4);
                /*SL:393*/v2.head().accept(this);
                /*SL:394*/if (!CodeGen.isRefType(v-4)) {
                    /*SL:395*/this.atNumCastExpr(this.exprType, v-4);
                }
                /*SL:397*/this.bytecode.addOpcode(CodeGen.getArrayWriteOp(v-4, 0));
                /*SL:398*/v2 = v2.tail();
            }
        }
        /*SL:402*/this.exprType = v-4;
        /*SL:403*/this.arrayDim = 1;
        /*SL:404*/this.className = v0;
    }
    
    private static void badNewExpr() throws CompileError {
        /*SL:408*/throw new CompileError("bad new expression");
    }
    
    @Override
    protected void atArrayVariableAssign(final ArrayInit a1, final int a2, final int a3, final String a4) throws CompileError {
        /*SL:413*/this.atNewArrayExpr2(a2, null, a4, a1);
    }
    
    @Override
    public void atArrayInit(final ArrayInit a1) throws CompileError {
        /*SL:417*/throw new CompileError("array initializer is not supported");
    }
    
    protected void atMultiNewArray(final int v1, final ASTList v2, ASTList v3) throws CompileError {
        final int v4 = /*EL:424*/v3.length();
        int v5 = /*EL:425*/0;
        while (v3 != null) {
            final ASTree a1 = /*EL:426*/v3.head();
            /*SL:427*/if (a1 == null) {
                /*SL:428*/break;
            }
            /*SL:430*/++v5;
            /*SL:431*/a1.accept(this);
            /*SL:432*/if (this.exprType != 324) {
                /*SL:433*/throw new CompileError("bad type for array size");
            }
            v3 = v3.tail();
        }
        /*SL:437*/this.exprType = v1;
        /*SL:438*/this.arrayDim = v4;
        final String v6;
        /*SL:439*/if (v1 == 307) {
            /*SL:440*/this.className = this.resolveClassName(v2);
            final String a2 = /*EL:441*/CodeGen.toJvmArrayName(this.className, v4);
        }
        else {
            /*SL:444*/v6 = CodeGen.toJvmTypeName(v1, v4);
        }
        /*SL:446*/this.bytecode.addMultiNewarray(v6, v5);
    }
    
    @Override
    public void atCallExpr(final CallExpr v-10) throws CompileError {
        String a2 = /*EL:450*/null;
        CtClass ctClass = /*EL:451*/null;
        final ASTree oprand1 = /*EL:452*/v-10.oprand1();
        final ASTList a3 = /*EL:453*/(ASTList)v-10.oprand2();
        boolean a4 = /*EL:454*/false;
        boolean v3 = /*EL:455*/false;
        int v4 = /*EL:456*/-1;
        final MemberResolver.Method method = /*EL:458*/v-10.getMethod();
        /*SL:459*/if (oprand1 instanceof Member) {
            /*SL:460*/a2 = ((Member)oprand1).get();
            /*SL:461*/ctClass = this.thisClass;
            /*SL:462*/if (this.inStaticMethod || (method != null && method.isStatic())) {
                /*SL:463*/a4 = true;
            }
            else {
                /*SL:465*/v4 = this.bytecode.currentPc();
                /*SL:466*/this.bytecode.addAload(0);
            }
        }
        else/*SL:469*/ if (oprand1 instanceof Keyword) {
            /*SL:470*/v3 = true;
            /*SL:471*/a2 = "<init>";
            /*SL:472*/ctClass = this.thisClass;
            /*SL:473*/if (this.inStaticMethod) {
                /*SL:474*/throw new CompileError("a constructor cannot be static");
            }
            /*SL:476*/this.bytecode.addAload(0);
            /*SL:478*/if (((Keyword)oprand1).get() == 336) {
                /*SL:479*/ctClass = MemberResolver.getSuperclass(ctClass);
            }
        }
        else/*SL:481*/ if (oprand1 instanceof Expr) {
            final Expr expr = /*EL:482*/(Expr)oprand1;
            /*SL:483*/a2 = ((Symbol)expr.oprand2()).get();
            final int v0 = /*EL:484*/expr.getOperator();
            /*SL:485*/if (v0 == 35) {
                /*SL:487*/ctClass = this.resolver.lookupClass(((Symbol)expr.oprand1()).get(), false);
                /*SL:488*/a4 = true;
            }
            else/*SL:490*/ if (v0 == 46) {
                final ASTree v = /*EL:491*/expr.oprand1();
                final String v2 = /*EL:492*/TypeChecker.isDotSuper(v);
                /*SL:493*/if (v2 != null) {
                    /*SL:494*/v3 = true;
                    /*SL:495*/ctClass = MemberResolver.getSuperInterface(this.thisClass, v2);
                    /*SL:497*/if (this.inStaticMethod || (method != null && method.isStatic())) {
                        /*SL:498*/a4 = true;
                    }
                    else {
                        /*SL:500*/v4 = this.bytecode.currentPc();
                        /*SL:501*/this.bytecode.addAload(0);
                    }
                }
                else {
                    /*SL:505*/if (v instanceof Keyword && /*EL:506*/((Keyword)v).get() == 336) {
                        /*SL:507*/v3 = true;
                    }
                    try {
                        /*SL:510*/v.accept(this);
                    }
                    catch (NoFieldException a1) {
                        /*SL:513*/if (a1.getExpr() != v) {
                            /*SL:514*/throw a1;
                        }
                        /*SL:517*/this.exprType = 307;
                        /*SL:518*/this.arrayDim = 0;
                        /*SL:519*/this.className = a1.getField();
                        /*SL:520*/a4 = true;
                    }
                    /*SL:523*/if (this.arrayDim > 0) {
                        /*SL:524*/ctClass = this.resolver.lookupClass("java.lang.Object", true);
                    }
                    else/*SL:525*/ if (this.exprType == 307) {
                        /*SL:526*/ctClass = this.resolver.lookupClassByJvmName(this.className);
                    }
                    else {
                        badMethod();
                    }
                }
            }
            else {
                badMethod();
            }
        }
        else {
            fatal();
        }
        /*SL:537*/this.atMethodCallCore(ctClass, a2, a3, a4, v3, v4, method);
    }
    
    private static void badMethod() throws CompileError {
        /*SL:542*/throw new CompileError("bad method");
    }
    
    public void atMethodCallCore(final CtClass a4, final String a5, final ASTList a6, boolean a7, final boolean v1, final int v2, MemberResolver.Method v3) throws CompileError {
        final int v4 = /*EL:556*/this.getMethodArgsLength(a6);
        final int[] v5 = /*EL:557*/new int[v4];
        final int[] v6 = /*EL:558*/new int[v4];
        final String[] v7 = /*EL:559*/new String[v4];
        /*SL:561*/if (!a7 && v3 != null && v3.isStatic()) {
            /*SL:562*/this.bytecode.addOpcode(87);
            /*SL:563*/a7 = true;
        }
        final int v8 = /*EL:566*/this.bytecode.getStackDepth();
        /*SL:569*/this.atMethodArgs(a6, v5, v6, v7);
        /*SL:571*/if (v3 == null) {
            /*SL:572*/v3 = this.resolver.lookupMethod(a4, this.thisClass, this.thisMethod, a5, v5, v6, v7);
        }
        /*SL:575*/if (v3 == null) {
            final String a9;
            /*SL:577*/if (a5.equals("<init>")) {
                final String a8 = /*EL:578*/"constructor not found";
            }
            else {
                /*SL:581*/a9 = "Method " + a5 + " not found in " + a4.getName();
            }
            /*SL:583*/throw new CompileError(a9);
        }
        /*SL:586*/this.atMethodCallCore2(a4, a5, a7, v1, v2, v3);
    }
    
    private void atMethodCallCore2(final CtClass a4, String a5, boolean a6, boolean v1, final int v2, final MemberResolver.Method v3) throws CompileError {
        CtClass v4 = /*EL:596*/v3.declaring;
        final MethodInfo v5 = /*EL:597*/v3.info;
        String v6 = /*EL:598*/v5.getDescriptor();
        int v7 = /*EL:599*/v5.getAccessFlags();
        /*SL:601*/if (a5.equals("<init>")) {
            /*SL:602*/v1 = true;
            /*SL:603*/if (v4 != a4) {
                /*SL:604*/throw new CompileError("no such constructor: " + a4.getName());
            }
            /*SL:606*/if (v4 != this.thisClass && AccessFlag.isPrivate(v7)) {
                /*SL:607*/v6 = this.getAccessibleConstructor(v6, v4, v5);
                /*SL:608*/this.bytecode.addOpcode(1);
            }
        }
        else/*SL:611*/ if (AccessFlag.isPrivate(v7)) {
            /*SL:612*/if (v4 == this.thisClass) {
                /*SL:613*/v1 = true;
            }
            else {
                /*SL:615*/v1 = false;
                /*SL:616*/a6 = true;
                final String a7 = /*EL:617*/v6;
                /*SL:618*/if ((v7 & 0x8) == 0x0) {
                    /*SL:619*/v6 = Descriptor.insertParameter(v4.getName(), a7);
                }
                /*SL:622*/v7 = (AccessFlag.setPackage(v7) | 0x8);
                /*SL:623*/a5 = this.getAccessiblePrivate(a5, a7, v6, v5, v4);
            }
        }
        boolean v8 = /*EL:627*/false;
        /*SL:628*/if ((v7 & 0x8) != 0x0) {
            /*SL:629*/if (!a6) {
                /*SL:635*/a6 = true;
                /*SL:636*/if (v2 >= 0) {
                    /*SL:637*/this.bytecode.write(v2, 0);
                }
                else {
                    /*SL:639*/v8 = true;
                }
            }
            /*SL:642*/this.bytecode.addInvokestatic(v4, a5, v6);
        }
        else/*SL:644*/ if (v1) {
            /*SL:645*/this.bytecode.addInvokespecial(a4, a5, v6);
        }
        else {
            /*SL:647*/if (!Modifier.isPublic(v4.getModifiers()) || v4.isInterface() != /*EL:648*/a4.isInterface()) {
                /*SL:649*/v4 = a4;
            }
            /*SL:651*/if (v4.isInterface()) {
                final int a8 = /*EL:652*/Descriptor.paramSize(v6) + 1;
                /*SL:653*/this.bytecode.addInvokeinterface(v4, a5, v6, a8);
            }
            else {
                /*SL:656*/if (a6) {
                    /*SL:657*/throw new CompileError(a5 + " is not static");
                }
                /*SL:659*/this.bytecode.addInvokevirtual(v4, a5, v6);
            }
        }
        /*SL:662*/this.setReturnType(v6, a6, v8);
    }
    
    protected String getAccessiblePrivate(final String a3, final String a4, final String a5, final MethodInfo v1, final CtClass v2) throws CompileError {
        /*SL:677*/if (this.isEnclosing(v2, this.thisClass)) {
            final AccessorMaker a6 = /*EL:678*/v2.getAccessorMaker();
            /*SL:679*/if (a6 != null) {
                /*SL:680*/return a6.getMethodAccessor(a3, a4, a5, v1);
            }
        }
        /*SL:684*/throw new CompileError("Method " + a3 + " is private");
    }
    
    protected String getAccessibleConstructor(final String a3, final CtClass v1, final MethodInfo v2) throws CompileError {
        /*SL:701*/if (this.isEnclosing(v1, this.thisClass)) {
            final AccessorMaker a4 = /*EL:702*/v1.getAccessorMaker();
            /*SL:703*/if (a4 != null) {
                /*SL:704*/return a4.getConstructor(v1, a3, v2);
            }
        }
        /*SL:707*/throw new CompileError("the called constructor is private in " + v1.getName());
    }
    
    private boolean isEnclosing(final CtClass a1, CtClass a2) {
        try {
            /*SL:713*/while (a2 != null) {
                /*SL:714*/a2 = a2.getDeclaringClass();
                /*SL:715*/if (a2 == a1) {
                    /*SL:716*/return true;
                }
            }
        }
        catch (NotFoundException ex) {}
        /*SL:720*/return false;
    }
    
    public int getMethodArgsLength(final ASTList a1) {
        /*SL:724*/return ASTList.length(a1);
    }
    
    public void atMethodArgs(ASTList a3, final int[] a4, final int[] v1, final String[] v2) throws CompileError {
        int v3 = /*EL:729*/0;
        /*SL:730*/while (a3 != null) {
            final ASTree a5 = /*EL:731*/a3.head();
            /*SL:732*/a5.accept(this);
            /*SL:733*/a4[v3] = this.exprType;
            /*SL:734*/v1[v3] = this.arrayDim;
            /*SL:735*/v2[v3] = this.className;
            /*SL:736*/++v3;
            /*SL:737*/a3 = a3.tail();
        }
    }
    
    void setReturnType(final String a3, final boolean v1, final boolean v2) throws CompileError {
        int v3 = /*EL:744*/a3.indexOf(41);
        /*SL:745*/if (v3 < 0) {
            badMethod();
        }
        char v4 = /*EL:748*/a3.charAt(++v3);
        int v5 = /*EL:749*/0;
        /*SL:750*/while (v4 == '[') {
            /*SL:751*/++v5;
            /*SL:752*/v4 = a3.charAt(++v3);
        }
        /*SL:755*/this.arrayDim = v5;
        /*SL:756*/if (v4 == 'L') {
            final int a4 = /*EL:757*/a3.indexOf(59, v3 + 1);
            /*SL:758*/if (a4 < 0) {
                badMethod();
            }
            /*SL:761*/this.exprType = 307;
            /*SL:762*/this.className = a3.substring(v3 + 1, a4);
        }
        else {
            /*SL:765*/this.exprType = MemberResolver.descToType(v4);
            /*SL:766*/this.className = null;
        }
        final int v6 = /*EL:769*/this.exprType;
        /*SL:770*/if (v1 && /*EL:771*/v2) {
            /*SL:772*/if (CodeGen.is2word(v6, v5)) {
                /*SL:773*/this.bytecode.addOpcode(93);
                /*SL:774*/this.bytecode.addOpcode(88);
                /*SL:775*/this.bytecode.addOpcode(87);
            }
            else/*SL:777*/ if (v6 == 344) {
                /*SL:778*/this.bytecode.addOpcode(87);
            }
            else {
                /*SL:780*/this.bytecode.addOpcode(95);
                /*SL:781*/this.bytecode.addOpcode(87);
            }
        }
    }
    
    @Override
    protected void atFieldAssign(final Expr v-11, final int v-10, final ASTree v-9, final ASTree v-8, final boolean v-7) throws CompileError {
        final CtField fieldAccess = /*EL:790*/this.fieldAccess(v-9, false);
        final boolean resultStatic = /*EL:791*/this.resultStatic;
        /*SL:792*/if (v-10 != 61 && !resultStatic) {
            /*SL:793*/this.bytecode.addOpcode(89);
        }
        final int atFieldRead;
        /*SL:796*/if (v-10 == 61) {
            FieldInfo a2 = /*EL:797*/fieldAccess.getFieldInfo2();
            /*SL:798*/this.setFieldType(a2);
            /*SL:799*/a2 = this.isAccessibleField(fieldAccess, a2);
            /*SL:800*/if (a2 == null) {
                final int a3 = /*EL:801*/this.addFieldrefInfo(fieldAccess, a2);
            }
            else {
                final int a4 = /*EL:803*/0;
            }
        }
        else {
            /*SL:806*/atFieldRead = this.atFieldRead(fieldAccess, resultStatic);
        }
        final int exprType = /*EL:808*/this.exprType;
        final int arrayDim = /*EL:809*/this.arrayDim;
        final String className = /*EL:810*/this.className;
        /*SL:812*/this.atAssignCore(v-11, v-10, v-8, exprType, arrayDim, className);
        final boolean v0 = /*EL:814*/CodeGen.is2word(exprType, arrayDim);
        /*SL:815*/if (v-7) {
            final int v;
            /*SL:817*/if (resultStatic) {
                final int a5 = /*EL:818*/v0 ? 92 : 89;
            }
            else {
                /*SL:820*/v = (v0 ? 93 : 90);
            }
            /*SL:822*/this.bytecode.addOpcode(v);
        }
        /*SL:825*/this.atFieldAssignCore(fieldAccess, resultStatic, atFieldRead, v0);
        /*SL:827*/this.exprType = exprType;
        /*SL:828*/this.arrayDim = arrayDim;
        /*SL:829*/this.className = className;
    }
    
    private void atFieldAssignCore(final CtField v2, final boolean v3, final int v4, final boolean v5) throws CompileError {
        /*SL:836*/if (v4 != 0) {
            /*SL:837*/if (v3) {
                /*SL:838*/this.bytecode.add(179);
                /*SL:839*/this.bytecode.growStack(v5 ? -2 : -1);
            }
            else {
                /*SL:842*/this.bytecode.add(181);
                /*SL:843*/this.bytecode.growStack(v5 ? -3 : -2);
            }
            /*SL:846*/this.bytecode.addIndex(v4);
        }
        else {
            final CtClass a1 = /*EL:849*/v2.getDeclaringClass();
            final AccessorMaker a2 = /*EL:850*/a1.getAccessorMaker();
            final FieldInfo a3 = /*EL:852*/v2.getFieldInfo2();
            final MethodInfo a4 = /*EL:853*/a2.getFieldSetter(a3, v3);
            /*SL:854*/this.bytecode.addInvokestatic(a1, a4.getName(), a4.getDescriptor());
        }
    }
    
    @Override
    public void atMember(final Member a1) throws CompileError {
        /*SL:862*/this.atFieldRead(a1);
    }
    
    @Override
    protected void atFieldRead(final ASTree a1) throws CompileError {
        final CtField v1 = /*EL:867*/this.fieldAccess(a1, true);
        /*SL:868*/if (v1 == null) {
            /*SL:869*/this.atArrayLength(a1);
            /*SL:870*/return;
        }
        final boolean v2 = /*EL:873*/this.resultStatic;
        final ASTree v3 = /*EL:874*/TypeChecker.getConstantFieldValue(v1);
        /*SL:875*/if (v3 == null) {
            /*SL:876*/this.atFieldRead(v1, v2);
        }
        else {
            /*SL:878*/v3.accept(this);
            /*SL:879*/this.setFieldType(v1.getFieldInfo2());
        }
    }
    
    private void atArrayLength(final ASTree a1) throws CompileError {
        /*SL:884*/if (this.arrayDim == 0) {
            /*SL:885*/throw new CompileError(".length applied to a non array");
        }
        /*SL:887*/this.bytecode.addOpcode(190);
        /*SL:888*/this.exprType = 324;
        /*SL:889*/this.arrayDim = 0;
    }
    
    private int atFieldRead(final CtField v2, final boolean v3) throws CompileError {
        final FieldInfo v4 = /*EL:898*/v2.getFieldInfo2();
        final boolean v5 = /*EL:899*/this.setFieldType(v4);
        final AccessorMaker v6 = /*EL:900*/this.isAccessibleField(v2, v4);
        /*SL:901*/if (v6 != null) {
            final MethodInfo a1 = /*EL:902*/v6.getFieldGetter(v4, v3);
            /*SL:903*/this.bytecode.addInvokestatic(v2.getDeclaringClass(), a1.getName(), a1.getDescriptor());
            /*SL:905*/return 0;
        }
        final int a2 = /*EL:908*/this.addFieldrefInfo(v2, v4);
        /*SL:909*/if (v3) {
            /*SL:910*/this.bytecode.add(178);
            /*SL:911*/this.bytecode.growStack(v5 ? 2 : 1);
        }
        else {
            /*SL:914*/this.bytecode.add(180);
            /*SL:915*/this.bytecode.growStack(v5 ? 1 : 0);
        }
        /*SL:918*/this.bytecode.addIndex(a2);
        /*SL:919*/return a2;
    }
    
    private AccessorMaker isAccessibleField(final CtField v2, final FieldInfo v3) throws CompileError {
        /*SL:931*/if (!AccessFlag.isPrivate(v3.getAccessFlags()) || v2.getDeclaringClass() == /*EL:932*/this.thisClass) {
            /*SL:946*/return null;
        }
        CtClass a2 = v2.getDeclaringClass();
        if (!this.isEnclosing(a2, this.thisClass)) {
            throw new CompileError("Field " + v2.getName() + " in " + a2.getName() + " is private.");
        }
        a2 = a2.getAccessorMaker();
        if (a2 != null) {
            return a2;
        }
        throw new CompileError("fatal error.  bug?");
    }
    
    private boolean setFieldType(final FieldInfo a1) throws CompileError {
        final String v1 = /*EL:955*/a1.getDescriptor();
        int v2 = /*EL:957*/0;
        int v3 = /*EL:958*/0;
        char v4;
        /*SL:960*/for (v4 = v1.charAt(v2); v4 == '['; /*SL:962*/v4 = v1.charAt(++v2)) {
            ++v3;
        }
        /*SL:965*/this.arrayDim = v3;
        /*SL:966*/this.exprType = MemberResolver.descToType(v4);
        /*SL:968*/if (v4 == 'L') {
            /*SL:969*/this.className = v1.substring(v2 + 1, v1.indexOf(59, v2 + 1));
        }
        else {
            /*SL:971*/this.className = null;
        }
        final boolean v5 = /*EL:973*/v3 == 0 && (v4 == 'J' || v4 == 'D');
        /*SL:974*/return v5;
    }
    
    private int addFieldrefInfo(final CtField a1, final FieldInfo a2) {
        final ConstPool v1 = /*EL:978*/this.bytecode.getConstPool();
        final String v2 = /*EL:979*/a1.getDeclaringClass().getName();
        final int v3 = /*EL:980*/v1.addClassInfo(v2);
        final String v4 = /*EL:981*/a2.getName();
        final String v5 = /*EL:982*/a2.getDescriptor();
        /*SL:983*/return v1.addFieldrefInfo(v3, v4, v5);
    }
    
    @Override
    protected void atClassObject2(final String a1) throws CompileError {
        /*SL:987*/if (this.getMajorVersion() < 49) {
            /*SL:988*/super.atClassObject2(a1);
        }
        else {
            /*SL:990*/this.bytecode.addLdc(this.bytecode.getConstPool().addClassInfo(a1));
        }
    }
    
    @Override
    protected void atFieldPlusPlus(final int a3, final boolean a4, final ASTree a5, final Expr v1, final boolean v2) throws CompileError {
        final CtField v3 = /*EL:997*/this.fieldAccess(a5, false);
        final boolean v4 = /*EL:998*/this.resultStatic;
        /*SL:999*/if (!v4) {
            /*SL:1000*/this.bytecode.addOpcode(89);
        }
        final int v5 = /*EL:1002*/this.atFieldRead(v3, v4);
        final int v6 = /*EL:1003*/this.exprType;
        final boolean v7 = /*EL:1004*/CodeGen.is2word(v6, this.arrayDim);
        final int v8;
        /*SL:1007*/if (v4) {
            final int a6 = /*EL:1008*/v7 ? 92 : 89;
        }
        else {
            /*SL:1010*/v8 = (v7 ? 93 : 90);
        }
        /*SL:1012*/this.atPlusPlusCore(v8, v2, a3, a4, v1);
        /*SL:1013*/this.atFieldAssignCore(v3, v4, v5, v7);
    }
    
    protected CtField fieldAccess(final ASTree v-2, final boolean v-1) throws CompileError {
        /*SL:1023*/if (v-2 instanceof Member) {
            final String a2 = /*EL:1024*/((Member)v-2).get();
            CtField v1 = /*EL:1025*/null;
            try {
                /*SL:1027*/v1 = this.thisClass.getField(a2);
            }
            catch (NotFoundException a2) {
                /*SL:1031*/throw new NoFieldException(a2, v-2);
            }
            final boolean v2 = /*EL:1034*/Modifier.isStatic(v1.getModifiers());
            /*SL:1035*/if (!v2) {
                /*SL:1036*/if (this.inStaticMethod) {
                    /*SL:1037*/throw new CompileError("not available in a static method: " + a2);
                }
                /*SL:1040*/this.bytecode.addAload(0);
            }
            /*SL:1042*/this.resultStatic = v2;
            /*SL:1043*/return v1;
        }
        /*SL:1045*/if (v-2 instanceof Expr) {
            final Expr v3 = /*EL:1046*/(Expr)v-2;
            final int v4 = /*EL:1047*/v3.getOperator();
            /*SL:1048*/if (v4 == 35) {
                final CtField v5 = /*EL:1053*/this.resolver.lookupField(((Symbol)v3.oprand1()).get(), (Symbol)v3.oprand2());
                /*SL:1055*/this.resultStatic = true;
                /*SL:1056*/return v5;
            }
            /*SL:1058*/if (v4 == 46) {
                CtField v5 = /*EL:1059*/null;
                try {
                    /*SL:1061*/v3.oprand1().accept(this);
                    /*SL:1066*/if (this.exprType == 307 && this.arrayDim == 0) {
                        /*SL:1067*/v5 = this.resolver.lookupFieldByJvmName(this.className, (Symbol)v3.oprand2());
                    }
                    else {
                        /*SL:1069*/if (v-1 && this.arrayDim > 0 && ((Symbol)v3.oprand2()).get().equals(/*EL:1070*/"length")) {
                            /*SL:1071*/return null;
                        }
                        badLvalue();
                    }
                    final boolean v6 = /*EL:1075*/Modifier.isStatic(v5.getModifiers());
                    /*SL:1076*/if (v6) {
                        /*SL:1077*/this.bytecode.addOpcode(87);
                    }
                    /*SL:1079*/this.resultStatic = v6;
                    /*SL:1080*/return v5;
                }
                catch (NoFieldException v7) {
                    /*SL:1083*/if (v7.getExpr() != v3.oprand1()) {
                        /*SL:1084*/throw v7;
                    }
                    final Symbol v8 = /*EL:1090*/(Symbol)v3.oprand2();
                    final String v9 = /*EL:1091*/v7.getField();
                    /*SL:1092*/v5 = this.resolver.lookupFieldByJvmName2(v9, v8, v-2);
                    /*SL:1093*/this.resultStatic = true;
                    /*SL:1094*/return v5;
                }
            }
            badLvalue();
        }
        else {
            badLvalue();
        }
        /*SL:1103*/this.resultStatic = false;
        /*SL:1104*/return null;
    }
    
    private static void badLvalue() throws CompileError {
        /*SL:1108*/throw new CompileError("bad l-value");
    }
    
    public CtClass[] makeParamList(final MethodDecl v-2) throws CompileError {
        ASTList v0 = /*EL:1113*/v-2.getParams();
        final CtClass[] array;
        /*SL:1114*/if (v0 == null) {
            final CtClass[] a1 = /*EL:1115*/new CtClass[0];
        }
        else {
            int v = /*EL:1117*/0;
            /*SL:1118*/array = new CtClass[v0.length()];
            /*SL:1119*/while (v0 != null) {
                /*SL:1120*/array[v++] = this.resolver.lookupClass((Declarator)v0.head());
                /*SL:1121*/v0 = v0.tail();
            }
        }
        /*SL:1125*/return array;
    }
    
    public CtClass[] makeThrowsList(final MethodDecl v2) throws CompileError {
        ASTList v3 = /*EL:1130*/v2.getThrows();
        /*SL:1131*/if (v3 == null) {
            /*SL:1132*/return null;
        }
        int a1 = /*EL:1134*/0;
        final CtClass[] v4 = /*EL:1135*/new CtClass[v3.length()];
        /*SL:1136*/while (v3 != null) {
            /*SL:1137*/v4[a1++] = this.resolver.lookupClassByName((ASTList)v3.head());
            /*SL:1138*/v3 = v3.tail();
        }
        /*SL:1141*/return v4;
    }
    
    @Override
    protected String resolveClassName(final ASTList a1) throws CompileError {
        /*SL:1151*/return this.resolver.resolveClassName(a1);
    }
    
    @Override
    protected String resolveClassName(final String a1) throws CompileError {
        /*SL:1158*/return this.resolver.resolveJvmClassName(a1);
    }
    
    static class JsrHook extends ReturnHook
    {
        ArrayList jsrList;
        CodeGen cgen;
        int var;
        
        JsrHook(final CodeGen a1) {
            super(a1);
            this.jsrList = new ArrayList();
            this.cgen = a1;
            this.var = -1;
        }
        
        private int getVar(final int a1) {
            /*SL:98*/if (this.var < 0) {
                /*SL:99*/this.var = this.cgen.getMaxLocals();
                /*SL:100*/this.cgen.incMaxLocals(a1);
            }
            /*SL:103*/return this.var;
        }
        
        private void jsrJmp(final Bytecode a1) {
            /*SL:107*/a1.addOpcode(167);
            /*SL:108*/this.jsrList.add(new int[] { a1.currentPc(), this.var });
            /*SL:109*/a1.addIndex(0);
        }
        
        @Override
        protected boolean doit(final Bytecode a1, final int a2) {
            /*SL:113*/switch (a2) {
                case 177: {
                    /*SL:115*/this.jsrJmp(a1);
                    /*SL:116*/break;
                }
                case 176: {
                    /*SL:118*/a1.addAstore(this.getVar(1));
                    /*SL:119*/this.jsrJmp(a1);
                    /*SL:120*/a1.addAload(this.var);
                    /*SL:121*/break;
                }
                case 172: {
                    /*SL:123*/a1.addIstore(this.getVar(1));
                    /*SL:124*/this.jsrJmp(a1);
                    /*SL:125*/a1.addIload(this.var);
                    /*SL:126*/break;
                }
                case 173: {
                    /*SL:128*/a1.addLstore(this.getVar(2));
                    /*SL:129*/this.jsrJmp(a1);
                    /*SL:130*/a1.addLload(this.var);
                    /*SL:131*/break;
                }
                case 175: {
                    /*SL:133*/a1.addDstore(this.getVar(2));
                    /*SL:134*/this.jsrJmp(a1);
                    /*SL:135*/a1.addDload(this.var);
                    /*SL:136*/break;
                }
                case 174: {
                    /*SL:138*/a1.addFstore(this.getVar(1));
                    /*SL:139*/this.jsrJmp(a1);
                    /*SL:140*/a1.addFload(this.var);
                    /*SL:141*/break;
                }
                default: {
                    /*SL:143*/throw new RuntimeException("fatal");
                }
            }
            /*SL:146*/return false;
        }
    }
    
    static class JsrHook2 extends ReturnHook
    {
        int var;
        int target;
        
        JsrHook2(final CodeGen a1, final int[] a2) {
            super(a1);
            this.target = a2[0];
            this.var = a2[1];
        }
        
        @Override
        protected boolean doit(final Bytecode a1, final int a2) {
            /*SL:161*/switch (a2) {
                case 177: {
                    /*SL:163*/break;
                }
                case 176: {
                    /*SL:165*/a1.addAstore(this.var);
                    /*SL:166*/break;
                }
                case 172: {
                    /*SL:168*/a1.addIstore(this.var);
                    /*SL:169*/break;
                }
                case 173: {
                    /*SL:171*/a1.addLstore(this.var);
                    /*SL:172*/break;
                }
                case 175: {
                    /*SL:174*/a1.addDstore(this.var);
                    /*SL:175*/break;
                }
                case 174: {
                    /*SL:177*/a1.addFstore(this.var);
                    /*SL:178*/break;
                }
                default: {
                    /*SL:180*/throw new RuntimeException("fatal");
                }
            }
            /*SL:183*/a1.addOpcode(167);
            /*SL:184*/a1.addIndex(this.target - a1.currentPc() + 3);
            /*SL:185*/return true;
        }
    }
}
