package javassist.compiler;

import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.IntConst;
import java.util.Arrays;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.Declarator;
import java.util.ArrayList;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.Visitor;

public abstract class CodeGen extends Visitor implements Opcode, TokenId
{
    static final String javaLangObject = "java.lang.Object";
    static final String jvmJavaLangObject = "java/lang/Object";
    static final String javaLangString = "java.lang.String";
    static final String jvmJavaLangString = "java/lang/String";
    protected Bytecode bytecode;
    private int tempVar;
    TypeChecker typeChecker;
    protected boolean hasReturned;
    public boolean inStaticMethod;
    protected ArrayList breakList;
    protected ArrayList continueList;
    protected ReturnHook returnHooks;
    protected int exprType;
    protected int arrayDim;
    protected String className;
    static final int[] binOp;
    private static final int[] ifOp;
    private static final int[] ifOp2;
    private static final int P_DOUBLE = 0;
    private static final int P_FLOAT = 1;
    private static final int P_LONG = 2;
    private static final int P_INT = 3;
    private static final int P_OTHER = -1;
    private static final int[] castOp;
    
    public CodeGen(final Bytecode a1) {
        this.bytecode = a1;
        this.tempVar = -1;
        this.typeChecker = null;
        this.hasReturned = false;
        this.inStaticMethod = false;
        this.breakList = null;
        this.continueList = null;
        this.returnHooks = null;
    }
    
    public void setTypeChecker(final TypeChecker a1) {
        /*SL:95*/this.typeChecker = a1;
    }
    
    protected static void fatal() throws CompileError {
        /*SL:99*/throw new CompileError("fatal");
    }
    
    public static boolean is2word(final int a1, final int a2) {
        /*SL:103*/return a2 == 0 && (a1 == 312 || a1 == 326);
    }
    
    public int getMaxLocals() {
        /*SL:106*/return this.bytecode.getMaxLocals();
    }
    
    public void setMaxLocals(final int a1) {
        /*SL:109*/this.bytecode.setMaxLocals(a1);
    }
    
    protected void incMaxLocals(final int a1) {
        /*SL:113*/this.bytecode.incMaxLocals(a1);
    }
    
    protected int getTempVar() {
        /*SL:121*/if (this.tempVar < 0) {
            /*SL:122*/this.tempVar = this.getMaxLocals();
            /*SL:123*/this.incMaxLocals(2);
        }
        /*SL:126*/return this.tempVar;
    }
    
    protected int getLocalVar(final Declarator a1) {
        int v1 = /*EL:130*/a1.getLocalVar();
        /*SL:131*/if (v1 < 0) {
            /*SL:132*/v1 = this.getMaxLocals();
            /*SL:133*/a1.setLocalVar(v1);
            /*SL:134*/this.incMaxLocals(1);
        }
        /*SL:137*/return v1;
    }
    
    protected abstract String getThisName();
    
    protected abstract String getSuperName() throws CompileError;
    
    protected abstract String resolveClassName(final ASTList p0) throws CompileError;
    
    protected abstract String resolveClassName(final String p0) throws CompileError;
    
    protected static String toJvmArrayName(final String v1, final int v2) {
        /*SL:169*/if (v1 == null) {
            /*SL:170*/return null;
        }
        /*SL:172*/if (v2 == 0) {
            /*SL:173*/return v1;
        }
        final StringBuffer a1 = /*EL:175*/new StringBuffer();
        int a2 = /*EL:176*/v2;
        /*SL:177*/while (a2-- > 0) {
            /*SL:178*/a1.append('[');
        }
        /*SL:180*/a1.append('L');
        /*SL:181*/a1.append(v1);
        /*SL:182*/a1.append(';');
        /*SL:184*/return a1.toString();
    }
    
    protected static String toJvmTypeName(final int a1, int a2) {
        char v1 = /*EL:189*/'I';
        /*SL:190*/switch (a1) {
            case 301: {
                /*SL:192*/v1 = 'Z';
                /*SL:193*/break;
            }
            case 303: {
                /*SL:195*/v1 = 'B';
                /*SL:196*/break;
            }
            case 306: {
                /*SL:198*/v1 = 'C';
                /*SL:199*/break;
            }
            case 334: {
                /*SL:201*/v1 = 'S';
                /*SL:202*/break;
            }
            case 324: {
                /*SL:204*/v1 = 'I';
                /*SL:205*/break;
            }
            case 326: {
                /*SL:207*/v1 = 'J';
                /*SL:208*/break;
            }
            case 317: {
                /*SL:210*/v1 = 'F';
                /*SL:211*/break;
            }
            case 312: {
                /*SL:213*/v1 = 'D';
                /*SL:214*/break;
            }
            case 344: {
                /*SL:216*/v1 = 'V';
                break;
            }
        }
        final StringBuffer v2 = /*EL:220*/new StringBuffer();
        /*SL:221*/while (a2-- > 0) {
            /*SL:222*/v2.append('[');
        }
        /*SL:224*/v2.append(v1);
        /*SL:225*/return v2.toString();
    }
    
    public void compileExpr(final ASTree a1) throws CompileError {
        /*SL:229*/this.doTypeCheck(a1);
        /*SL:230*/a1.accept(this);
    }
    
    public boolean compileBooleanExpr(final boolean a1, final ASTree a2) throws CompileError {
        /*SL:236*/this.doTypeCheck(a2);
        /*SL:237*/return this.booleanExpr(a1, a2);
    }
    
    public void doTypeCheck(final ASTree a1) throws CompileError {
        /*SL:241*/if (this.typeChecker != null) {
            /*SL:242*/a1.accept(this.typeChecker);
        }
    }
    
    @Override
    public void atASTList(final ASTList a1) throws CompileError {
        fatal();
    }
    
    @Override
    public void atPair(final Pair a1) throws CompileError {
        fatal();
    }
    
    @Override
    public void atSymbol(final Symbol a1) throws CompileError {
        fatal();
    }
    
    @Override
    public void atFieldDecl(final FieldDecl a1) throws CompileError {
        /*SL:252*/a1.getInit().accept(this);
    }
    
    @Override
    public void atMethodDecl(final MethodDecl v2) throws CompileError {
        ASTList v3 = /*EL:256*/v2.getModifiers();
        /*SL:257*/this.setMaxLocals(1);
        /*SL:258*/while (v3 != null) {
            final Keyword a1 = /*EL:259*/(Keyword)v3.head();
            /*SL:260*/v3 = v3.tail();
            /*SL:261*/if (a1.get() == 335) {
                /*SL:262*/this.setMaxLocals(0);
                /*SL:263*/this.inStaticMethod = true;
            }
        }
        /*SL:268*/for (ASTList v4 = v2.getParams(); v4 != null; /*SL:270*/v4 = v4.tail()) {
            this.atDeclarator((Declarator)v4.head());
        }
        final Stmnt v5 = /*EL:273*/v2.getBody();
        /*SL:274*/this.atMethodBody(v5, v2.isConstructor(), v2.getReturn().getType() == /*EL:275*/344);
    }
    
    public void atMethodBody(final Stmnt a1, final boolean a2, final boolean a3) throws CompileError {
        /*SL:285*/if (a1 == null) {
            /*SL:286*/return;
        }
        /*SL:288*/if (a2 && this.needsSuperCall(a1)) {
            /*SL:289*/this.insertDefaultSuperCall();
        }
        /*SL:291*/this.hasReturned = false;
        /*SL:292*/a1.accept(this);
        /*SL:293*/if (!this.hasReturned) {
            /*SL:294*/if (!a3) {
                /*SL:299*/throw new CompileError("no return statement");
            }
            this.bytecode.addOpcode(177);
            this.hasReturned = true;
        }
    }
    
    private boolean needsSuperCall(Stmnt v-1) throws CompileError {
        /*SL:303*/if (v-1.getOperator() == 66) {
            /*SL:304*/v-1 = (Stmnt)v-1.head();
        }
        /*SL:306*/if (v-1 != null && v-1.getOperator() == 69) {
            final ASTree v0 = /*EL:307*/v-1.head();
            /*SL:308*/if (v0 != null && v0 instanceof Expr && ((Expr)v0).getOperator() == /*EL:309*/67) {
                final ASTree v = /*EL:310*/((Expr)v0).head();
                /*SL:311*/if (v instanceof Keyword) {
                    final int a1 = /*EL:312*/((Keyword)v).get();
                    /*SL:313*/return a1 != 339 && a1 != 336;
                }
            }
        }
        /*SL:318*/return true;
    }
    
    protected abstract void insertDefaultSuperCall() throws CompileError;
    
    @Override
    public void atStmnt(final Stmnt v-1) throws CompileError {
        /*SL:324*/if (v-1 == null) {
            /*SL:325*/return;
        }
        final int v0 = /*EL:327*/v-1.getOperator();
        /*SL:328*/if (v0 == 69) {
            final ASTree v = /*EL:329*/v-1.getLeft();
            /*SL:330*/this.doTypeCheck(v);
            /*SL:331*/if (v instanceof AssignExpr) {
                /*SL:332*/this.atAssignExpr((AssignExpr)v, false);
            }
            else/*SL:333*/ if (isPlusPlusExpr(v)) {
                final Expr a1 = /*EL:334*/(Expr)v;
                /*SL:335*/this.atPlusPlus(a1.getOperator(), a1.oprand1(), a1, false);
            }
            else {
                /*SL:338*/v.accept(this);
                /*SL:339*/if (is2word(this.exprType, this.arrayDim)) {
                    /*SL:340*/this.bytecode.addOpcode(88);
                }
                else/*SL:341*/ if (this.exprType != 344) {
                    /*SL:342*/this.bytecode.addOpcode(87);
                }
            }
        }
        else/*SL:345*/ if (v0 == 68 || v0 == 66) {
            ASTList v2 = /*EL:346*/v-1;
            /*SL:347*/while (v2 != null) {
                final ASTree v3 = /*EL:348*/v2.head();
                /*SL:349*/v2 = v2.tail();
                /*SL:350*/if (v3 != null) {
                    /*SL:351*/v3.accept(this);
                }
            }
        }
        else/*SL:354*/ if (v0 == 320) {
            /*SL:355*/this.atIfStmnt(v-1);
        }
        else/*SL:356*/ if (v0 == 346 || v0 == 311) {
            /*SL:357*/this.atWhileStmnt(v-1, v0 == 346);
        }
        else/*SL:358*/ if (v0 == 318) {
            /*SL:359*/this.atForStmnt(v-1);
        }
        else/*SL:360*/ if (v0 == 302 || v0 == 309) {
            /*SL:361*/this.atBreakStmnt(v-1, v0 == 302);
        }
        else/*SL:362*/ if (v0 == 333) {
            /*SL:363*/this.atReturnStmnt(v-1);
        }
        else/*SL:364*/ if (v0 == 340) {
            /*SL:365*/this.atThrowStmnt(v-1);
        }
        else/*SL:366*/ if (v0 == 343) {
            /*SL:367*/this.atTryStmnt(v-1);
        }
        else/*SL:368*/ if (v0 == 337) {
            /*SL:369*/this.atSwitchStmnt(v-1);
        }
        else {
            /*SL:370*/if (v0 != 338) {
                /*SL:374*/this.hasReturned = false;
                /*SL:375*/throw new CompileError("sorry, not supported statement: TokenId " + v0);
            }
            this.atSyncStmnt(v-1);
        }
    }
    
    private void atIfStmnt(final Stmnt a1) throws CompileError {
        final ASTree v1 = /*EL:381*/a1.head();
        final Stmnt v2 = /*EL:382*/(Stmnt)a1.tail().head();
        final Stmnt v3 = /*EL:383*/(Stmnt)a1.tail().tail().head();
        /*SL:384*/if (this.compileBooleanExpr(false, v1)) {
            /*SL:385*/this.hasReturned = false;
            /*SL:386*/if (v3 != null) {
                /*SL:387*/v3.accept(this);
            }
            /*SL:389*/return;
        }
        final int v4 = /*EL:392*/this.bytecode.currentPc();
        int v5 = /*EL:393*/0;
        /*SL:394*/this.bytecode.addIndex(0);
        /*SL:396*/this.hasReturned = false;
        /*SL:397*/if (v2 != null) {
            /*SL:398*/v2.accept(this);
        }
        final boolean v6 = /*EL:400*/this.hasReturned;
        /*SL:401*/this.hasReturned = false;
        /*SL:403*/if (v3 != null && !v6) {
            /*SL:404*/this.bytecode.addOpcode(167);
            /*SL:405*/v5 = this.bytecode.currentPc();
            /*SL:406*/this.bytecode.addIndex(0);
        }
        /*SL:409*/this.bytecode.write16bit(v4, this.bytecode.currentPc() - v4 + 1);
        /*SL:410*/if (v3 != null) {
            /*SL:411*/v3.accept(this);
            /*SL:412*/if (!v6) {
                /*SL:413*/this.bytecode.write16bit(v5, this.bytecode.currentPc() - v5 + 1);
            }
            /*SL:415*/this.hasReturned = (v6 && this.hasReturned);
        }
    }
    
    private void atWhileStmnt(final Stmnt a1, final boolean a2) throws CompileError {
        final ArrayList v1 = /*EL:420*/this.breakList;
        final ArrayList v2 = /*EL:421*/this.continueList;
        /*SL:422*/this.breakList = new ArrayList();
        /*SL:423*/this.continueList = new ArrayList();
        final ASTree v3 = /*EL:425*/a1.head();
        final Stmnt v4 = /*EL:426*/(Stmnt)a1.tail();
        int v5 = /*EL:428*/0;
        /*SL:429*/if (a2) {
            /*SL:430*/this.bytecode.addOpcode(167);
            /*SL:431*/v5 = this.bytecode.currentPc();
            /*SL:432*/this.bytecode.addIndex(0);
        }
        final int v6 = /*EL:435*/this.bytecode.currentPc();
        /*SL:436*/if (v4 != null) {
            /*SL:437*/v4.accept(this);
        }
        final int v7 = /*EL:439*/this.bytecode.currentPc();
        /*SL:440*/if (a2) {
            /*SL:441*/this.bytecode.write16bit(v5, v7 - v5 + 1);
        }
        boolean v8 = /*EL:443*/this.compileBooleanExpr(true, v3);
        /*SL:444*/if (v8) {
            /*SL:445*/this.bytecode.addOpcode(167);
            /*SL:446*/v8 = (this.breakList.size() == 0);
        }
        /*SL:449*/this.bytecode.addIndex(v6 - this.bytecode.currentPc() + 1);
        /*SL:450*/this.patchGoto(this.breakList, this.bytecode.currentPc());
        /*SL:451*/this.patchGoto(this.continueList, v7);
        /*SL:452*/this.continueList = v2;
        /*SL:453*/this.breakList = v1;
        /*SL:454*/this.hasReturned = v8;
    }
    
    protected void patchGoto(final ArrayList v2, final int v3) {
        /*SL:459*/for (int v4 = v2.size(), a2 = 0; a2 < v4; ++a2) {
            /*SL:460*/a2 = v2.get(a2);
            /*SL:461*/this.bytecode.write16bit(a2, v3 - a2 + 1);
        }
    }
    
    private void atForStmnt(final Stmnt a1) throws CompileError {
        final ArrayList v1 = /*EL:466*/this.breakList;
        final ArrayList v2 = /*EL:467*/this.continueList;
        /*SL:468*/this.breakList = new ArrayList();
        /*SL:469*/this.continueList = new ArrayList();
        final Stmnt v3 = /*EL:471*/(Stmnt)a1.head();
        ASTList v4 = /*EL:472*/a1.tail();
        final ASTree v5 = /*EL:473*/v4.head();
        /*SL:474*/v4 = v4.tail();
        final Stmnt v6 = /*EL:475*/(Stmnt)v4.head();
        final Stmnt v7 = /*EL:476*/(Stmnt)v4.tail();
        /*SL:478*/if (v3 != null) {
            /*SL:479*/v3.accept(this);
        }
        final int v8 = /*EL:481*/this.bytecode.currentPc();
        int v9 = /*EL:482*/0;
        /*SL:483*/if (v5 != null) {
            /*SL:484*/if (this.compileBooleanExpr(false, v5)) {
                /*SL:486*/this.continueList = v2;
                /*SL:487*/this.breakList = v1;
                /*SL:488*/this.hasReturned = false;
                /*SL:489*/return;
            }
            /*SL:492*/v9 = this.bytecode.currentPc();
            /*SL:493*/this.bytecode.addIndex(0);
        }
        /*SL:496*/if (v7 != null) {
            /*SL:497*/v7.accept(this);
        }
        final int v10 = /*EL:499*/this.bytecode.currentPc();
        /*SL:500*/if (v6 != null) {
            /*SL:501*/v6.accept(this);
        }
        /*SL:503*/this.bytecode.addOpcode(167);
        /*SL:504*/this.bytecode.addIndex(v8 - this.bytecode.currentPc() + 1);
        final int v11 = /*EL:506*/this.bytecode.currentPc();
        /*SL:507*/if (v5 != null) {
            /*SL:508*/this.bytecode.write16bit(v9, v11 - v9 + 1);
        }
        /*SL:510*/this.patchGoto(this.breakList, v11);
        /*SL:511*/this.patchGoto(this.continueList, v10);
        /*SL:512*/this.continueList = v2;
        /*SL:513*/this.breakList = v1;
        /*SL:514*/this.hasReturned = false;
    }
    
    private void atSwitchStmnt(final Stmnt v-10) throws CompileError {
        /*SL:518*/this.compileExpr(v-10.head());
        final ArrayList breakList = /*EL:520*/this.breakList;
        /*SL:521*/this.breakList = new ArrayList();
        final int currentPc = /*EL:522*/this.bytecode.currentPc();
        /*SL:523*/this.bytecode.addOpcode(171);
        int n = /*EL:524*/3 - (currentPc & 0x3);
        /*SL:525*/while (n-- > 0) {
            /*SL:526*/this.bytecode.add(0);
        }
        final Stmnt stmnt = /*EL:528*/(Stmnt)v-10.tail();
        int a2 = /*EL:529*/0;
        /*SL:530*/for (ASTList a1 = stmnt; a1 != null; a1 = a1.tail()) {
            /*SL:531*/if (((Stmnt)a1.head()).getOperator() == 304) {
                /*SL:532*/++a2;
            }
        }
        final int currentPc2 = /*EL:535*/this.bytecode.currentPc();
        /*SL:536*/this.bytecode.addGap(4);
        /*SL:537*/this.bytecode.add32bit(a2);
        /*SL:538*/this.bytecode.addGap(a2 * 8);
        final long[] array = /*EL:540*/new long[a2];
        int n2 = /*EL:541*/0;
        int currentPc3 = /*EL:542*/-1;
        /*SL:543*/for (ASTList v0 = stmnt; v0 != null; v0 = v0.tail()) {
            final Stmnt v = /*EL:544*/(Stmnt)v0.head();
            final int v2 = /*EL:545*/v.getOperator();
            /*SL:546*/if (v2 == 310) {
                /*SL:547*/currentPc3 = this.bytecode.currentPc();
            }
            else/*SL:548*/ if (v2 != 304) {
                fatal();
            }
            else {
                /*SL:553*/array[n2++] = (this.computeLabel(v.head()) << 32) + (this.bytecode.currentPc() - currentPc & -1L);
            }
            /*SL:556*/this.hasReturned = false;
            /*SL:557*/((Stmnt)v.tail()).accept(this);
        }
        /*SL:560*/Arrays.sort(array);
        int v3 = /*EL:561*/currentPc2 + 8;
        /*SL:562*/for (int v4 = 0; v4 < a2; ++v4) {
            /*SL:563*/this.bytecode.write32bit(v3, (int)(array[v4] >>> 32));
            /*SL:564*/this.bytecode.write32bit(v3 + 4, (int)array[v4]);
            /*SL:565*/v3 += 8;
        }
        /*SL:568*/if (currentPc3 < 0 || this.breakList.size() > 0) {
            /*SL:569*/this.hasReturned = false;
        }
        int v4 = /*EL:571*/this.bytecode.currentPc();
        /*SL:572*/if (currentPc3 < 0) {
            /*SL:573*/currentPc3 = v4;
        }
        /*SL:575*/this.bytecode.write32bit(currentPc2, currentPc3 - currentPc);
        /*SL:577*/this.patchGoto(this.breakList, v4);
        /*SL:578*/this.breakList = breakList;
    }
    
    private int computeLabel(ASTree a1) throws CompileError {
        /*SL:582*/this.doTypeCheck(a1);
        /*SL:583*/a1 = TypeChecker.stripPlusExpr(a1);
        /*SL:584*/if (a1 instanceof IntConst) {
            /*SL:585*/return (int)((IntConst)a1).get();
        }
        /*SL:587*/throw new CompileError("bad case label");
    }
    
    private void atBreakStmnt(final Stmnt a1, final boolean a2) throws CompileError {
        /*SL:593*/if (a1.head() != null) {
            /*SL:594*/throw new CompileError("sorry, not support labeled break or continue");
        }
        /*SL:597*/this.bytecode.addOpcode(167);
        final Integer v1 = /*EL:598*/new Integer(this.bytecode.currentPc());
        /*SL:599*/this.bytecode.addIndex(0);
        /*SL:600*/if (a2) {
            /*SL:601*/this.breakList.add(v1);
        }
        else {
            /*SL:603*/this.continueList.add(v1);
        }
    }
    
    protected void atReturnStmnt(final Stmnt a1) throws CompileError {
        /*SL:607*/this.atReturnStmnt2(a1.getLeft());
    }
    
    protected final void atReturnStmnt2(final ASTree v0) throws CompileError {
        int v = 0;
        /*SL:612*/if (v0 == null) {
            final int a1 = /*EL:613*/177;
        }
        else {
            /*SL:615*/this.compileExpr(v0);
            /*SL:616*/if (this.arrayDim > 0) {
                /*SL:617*/v = 176;
            }
            else {
                final int v2 = /*EL:619*/this.exprType;
                /*SL:620*/if (v2 == 312) {
                    /*SL:621*/v = 175;
                }
                else/*SL:622*/ if (v2 == 317) {
                    /*SL:623*/v = 174;
                }
                else/*SL:624*/ if (v2 == 326) {
                    /*SL:625*/v = 173;
                }
                else/*SL:626*/ if (isRefType(v2)) {
                    /*SL:627*/v = 176;
                }
                else {
                    /*SL:629*/v = 172;
                }
            }
        }
        /*SL:633*/for (ReturnHook v3 = this.returnHooks; v3 != null; v3 = v3.next) {
            /*SL:634*/if (v3.doit(this.bytecode, v)) {
                /*SL:635*/this.hasReturned = true;
                /*SL:636*/return;
            }
        }
        /*SL:639*/this.bytecode.addOpcode(v);
        /*SL:640*/this.hasReturned = true;
    }
    
    private void atThrowStmnt(final Stmnt a1) throws CompileError {
        final ASTree v1 = /*EL:644*/a1.getLeft();
        /*SL:645*/this.compileExpr(v1);
        /*SL:646*/if (this.exprType != 307 || this.arrayDim > 0) {
            /*SL:647*/throw new CompileError("bad throw statement");
        }
        /*SL:649*/this.bytecode.addOpcode(191);
        /*SL:650*/this.hasReturned = true;
    }
    
    protected void atTryStmnt(final Stmnt a1) throws CompileError {
        /*SL:656*/this.hasReturned = false;
    }
    
    private void atSyncStmnt(final Stmnt v2) throws CompileError {
        final int v3 = getListSize(/*EL:660*/this.breakList);
        final int v4 = getListSize(/*EL:661*/this.continueList);
        /*SL:663*/this.compileExpr(v2.head());
        /*SL:664*/if (this.exprType != 307 && this.arrayDim == 0) {
            /*SL:665*/throw new CompileError("bad type expr for synchronized block");
        }
        final Bytecode v5 = /*EL:667*/this.bytecode;
        final int v6 = /*EL:668*/v5.getMaxLocals();
        /*SL:669*/v5.incMaxLocals(1);
        /*SL:670*/v5.addOpcode(89);
        /*SL:671*/v5.addAstore(v6);
        /*SL:672*/v5.addOpcode(194);
        final ReturnHook v7 = /*EL:674*/new ReturnHook(this) {
            @Override
            protected boolean doit(final Bytecode a1, final int a2) {
                /*SL:676*/a1.addAload(/*EL:678*/v6);
                a1.addOpcode(195);
                return false;
            }
        };
        final int v8 = /*EL:682*/v5.currentPc();
        final Stmnt v9 = /*EL:683*/(Stmnt)v2.tail();
        /*SL:684*/if (v9 != null) {
            /*SL:685*/v9.accept(this);
        }
        final int v10 = /*EL:687*/v5.currentPc();
        int v11 = /*EL:688*/0;
        /*SL:689*/if (!this.hasReturned) {
            /*SL:690*/v7.doit(v5, 0);
            /*SL:691*/v5.addOpcode(167);
            /*SL:692*/v11 = v5.currentPc();
            /*SL:693*/v5.addIndex(0);
        }
        /*SL:696*/if (v8 < v10) {
            final int a1 = /*EL:697*/v5.currentPc();
            /*SL:698*/v7.doit(v5, 0);
            /*SL:699*/v5.addOpcode(191);
            /*SL:700*/v5.addExceptionHandler(v8, v10, a1, 0);
        }
        /*SL:703*/if (!this.hasReturned) {
            /*SL:704*/v5.write16bit(v11, v5.currentPc() - v11 + 1);
        }
        /*SL:706*/v7.remove(this);
        /*SL:708*/if (getListSize(this.breakList) != v3 || getListSize(this.continueList) != /*EL:709*/v4) {
            /*SL:710*/throw new CompileError("sorry, cannot break/continue in synchronized block");
        }
    }
    
    private static int getListSize(final ArrayList a1) {
        /*SL:715*/return (a1 == null) ? 0 : a1.size();
    }
    
    private static boolean isPlusPlusExpr(final ASTree v1) {
        /*SL:719*/if (v1 instanceof Expr) {
            final int a1 = /*EL:720*/((Expr)v1).getOperator();
            /*SL:721*/return a1 == 362 || a1 == 363;
        }
        /*SL:724*/return false;
    }
    
    @Override
    public void atDeclarator(final Declarator v2) throws CompileError {
        /*SL:728*/v2.setLocalVar(this.getMaxLocals());
        /*SL:729*/v2.setClassName(this.resolveClassName(v2.getClassName()));
        final int v3;
        /*SL:732*/if (is2word(v2.getType(), v2.getArrayDim())) {
            final int a1 = /*EL:733*/2;
        }
        else {
            /*SL:735*/v3 = 1;
        }
        /*SL:737*/this.incMaxLocals(v3);
        final ASTree v4 = /*EL:741*/v2.getInitializer();
        /*SL:742*/if (v4 != null) {
            /*SL:743*/this.doTypeCheck(v4);
            /*SL:744*/this.atVariableAssign(null, 61, null, v2, v4, false);
        }
    }
    
    @Override
    public abstract void atNewExpr(final NewExpr p0) throws CompileError;
    
    @Override
    public abstract void atArrayInit(final ArrayInit p0) throws CompileError;
    
    @Override
    public void atAssignExpr(final AssignExpr a1) throws CompileError {
        /*SL:753*/this.atAssignExpr(a1, true);
    }
    
    protected void atAssignExpr(final AssignExpr v1, final boolean v2) throws CompileError {
        final int v3 = /*EL:760*/v1.getOperator();
        final ASTree v4 = /*EL:761*/v1.oprand1();
        final ASTree v5 = /*EL:762*/v1.oprand2();
        /*SL:763*/if (v4 instanceof Variable) {
            /*SL:764*/this.atVariableAssign(v1, v3, (Variable)v4, ((Variable)v4).getDeclarator(), /*EL:765*/v5, v2);
        }
        else {
            /*SL:768*/if (v4 instanceof Expr) {
                final Expr a1 = /*EL:769*/(Expr)v4;
                /*SL:770*/if (a1.getOperator() == 65) {
                    /*SL:771*/this.atArrayAssign(v1, v3, (Expr)v4, v5, v2);
                    /*SL:772*/return;
                }
            }
            /*SL:776*/this.atFieldAssign(v1, v3, v4, v5, v2);
        }
    }
    
    protected static void badAssign(final Expr v1) throws CompileError {
        final String v2;
        /*SL:782*/if (v1 == null) {
            final String a1 = /*EL:783*/"incompatible type for assignment";
        }
        else {
            /*SL:785*/v2 = "incompatible type for " + v1.getName();
        }
        /*SL:787*/throw new CompileError(v2);
    }
    
    private void atVariableAssign(final Expr a1, final int a2, final Variable a3, final Declarator a4, final ASTree a5, final boolean a6) throws CompileError {
        final int v1 = /*EL:798*/a4.getType();
        final int v2 = /*EL:799*/a4.getArrayDim();
        final String v3 = /*EL:800*/a4.getClassName();
        final int v4 = /*EL:801*/this.getLocalVar(a4);
        /*SL:803*/if (a2 != 61) {
            /*SL:804*/this.atVariable(a3);
        }
        /*SL:807*/if (a1 == null && a5 instanceof ArrayInit) {
            /*SL:808*/this.atArrayVariableAssign((ArrayInit)a5, v1, v2, v3);
        }
        else {
            /*SL:810*/this.atAssignCore(a1, a2, a5, v1, v2, v3);
        }
        /*SL:812*/if (a6) {
            /*SL:813*/if (is2word(v1, v2)) {
                /*SL:814*/this.bytecode.addOpcode(92);
            }
            else {
                /*SL:816*/this.bytecode.addOpcode(89);
            }
        }
        /*SL:818*/if (v2 > 0) {
            /*SL:819*/this.bytecode.addAstore(v4);
        }
        else/*SL:820*/ if (v1 == 312) {
            /*SL:821*/this.bytecode.addDstore(v4);
        }
        else/*SL:822*/ if (v1 == 317) {
            /*SL:823*/this.bytecode.addFstore(v4);
        }
        else/*SL:824*/ if (v1 == 326) {
            /*SL:825*/this.bytecode.addLstore(v4);
        }
        else/*SL:826*/ if (isRefType(v1)) {
            /*SL:827*/this.bytecode.addAstore(v4);
        }
        else {
            /*SL:829*/this.bytecode.addIstore(v4);
        }
        /*SL:831*/this.exprType = v1;
        /*SL:832*/this.arrayDim = v2;
        /*SL:833*/this.className = v3;
    }
    
    protected abstract void atArrayVariableAssign(final ArrayInit p0, final int p1, final int p2, final String p3) throws CompileError;
    
    private void atArrayAssign(final Expr a1, final int a2, final Expr a3, final ASTree a4, final boolean a5) throws CompileError {
        /*SL:842*/this.arrayAccess(a3.oprand1(), a3.oprand2());
        /*SL:844*/if (a2 != 61) {
            /*SL:845*/this.bytecode.addOpcode(92);
            /*SL:846*/this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
        }
        final int v1 = /*EL:849*/this.exprType;
        final int v2 = /*EL:850*/this.arrayDim;
        final String v3 = /*EL:851*/this.className;
        /*SL:853*/this.atAssignCore(a1, a2, a4, v1, v2, v3);
        /*SL:855*/if (a5) {
            /*SL:856*/if (is2word(v1, v2)) {
                /*SL:857*/this.bytecode.addOpcode(94);
            }
            else {
                /*SL:859*/this.bytecode.addOpcode(91);
            }
        }
        /*SL:861*/this.bytecode.addOpcode(getArrayWriteOp(v1, v2));
        /*SL:862*/this.exprType = v1;
        /*SL:863*/this.arrayDim = v2;
        /*SL:864*/this.className = v3;
    }
    
    protected abstract void atFieldAssign(final Expr p0, final int p1, final ASTree p2, final ASTree p3, final boolean p4) throws CompileError;
    
    protected void atAssignCore(final Expr a4, final int a5, final ASTree a6, final int v1, final int v2, final String v3) throws CompileError {
        /*SL:874*/if (a5 == 354 && v2 == 0 && v1 == 307) {
            /*SL:875*/this.atStringPlusEq(a4, v1, v2, v3, a6);
        }
        else {
            /*SL:877*/a6.accept(this);
            /*SL:878*/if (this.invalidDim(this.exprType, this.arrayDim, this.className, v1, v2, v3, false) || (a5 != 61 && v2 > 0)) {
                badAssign(/*EL:880*/a4);
            }
            /*SL:882*/if (a5 != 61) {
                final int a7 = /*EL:883*/CodeGen.assignOps[a5 - 351];
                final int a8 = lookupBinOp(/*EL:884*/a7);
                /*SL:885*/if (a8 < 0) {
                    fatal();
                }
                /*SL:888*/this.atArithBinExpr(a4, a7, a8, v1);
            }
        }
        /*SL:892*/if (a5 != 61 || (v2 == 0 && !isRefType(v1))) {
            /*SL:893*/this.atNumCastExpr(this.exprType, v1);
        }
    }
    
    private void atStringPlusEq(final Expr a1, final int a2, final int a3, final String a4, final ASTree a5) throws CompileError {
        /*SL:902*/if (!"java/lang/String".equals(a4)) {
            badAssign(/*EL:903*/a1);
        }
        /*SL:905*/this.convToString(a2, a3);
        /*SL:906*/a5.accept(this);
        /*SL:907*/this.convToString(this.exprType, this.arrayDim);
        /*SL:908*/this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        /*SL:910*/this.exprType = 307;
        /*SL:911*/this.arrayDim = 0;
        /*SL:912*/this.className = "java/lang/String";
    }
    
    private boolean invalidDim(final int a1, final int a2, final String a3, final int a4, final int a5, final String a6, final boolean a7) {
        /*SL:922*/return a2 != a5 && a1 != 412 && (a5 != 0 || a4 != 307 || !"java/lang/Object".equals(a6)) && /*EL:925*/(!a7 || a2 != 0 || a1 != 307 || !"java/lang/Object".equals(a3));
    }
    
    @Override
    public void atCondExpr(final CondExpr v-1) throws CompileError {
        /*SL:935*/if (this.booleanExpr(false, v-1.condExpr())) {
            /*SL:936*/v-1.elseExpr().accept(this);
        }
        else {
            final int a1 = /*EL:938*/this.bytecode.currentPc();
            /*SL:939*/this.bytecode.addIndex(0);
            /*SL:940*/v-1.thenExpr().accept(this);
            final int v1 = /*EL:941*/this.arrayDim;
            /*SL:942*/this.bytecode.addOpcode(167);
            final int v2 = /*EL:943*/this.bytecode.currentPc();
            /*SL:944*/this.bytecode.addIndex(0);
            /*SL:945*/this.bytecode.write16bit(a1, this.bytecode.currentPc() - a1 + 1);
            /*SL:946*/v-1.elseExpr().accept(this);
            /*SL:947*/if (v1 != this.arrayDim) {
                /*SL:948*/throw new CompileError("type mismatch in ?:");
            }
            /*SL:950*/this.bytecode.write16bit(v2, this.bytecode.currentPc() - v2 + 1);
        }
    }
    
    static int lookupBinOp(final int v1) {
        final int[] v2 = CodeGen.binOp;
        /*SL:970*/for (int v3 = v2.length, a1 = 0; a1 < v3; a1 += 5) {
            /*SL:971*/if (v2[a1] == v1) {
                /*SL:972*/return a1;
            }
        }
        /*SL:974*/return -1;
    }
    
    @Override
    public void atBinExpr(final BinExpr v-3) throws CompileError {
        final int operator = /*EL:978*/v-3.getOperator();
        final int lookupBinOp = lookupBinOp(/*EL:982*/operator);
        /*SL:983*/if (lookupBinOp >= 0) {
            /*SL:984*/v-3.oprand1().accept(this);
            final ASTree a1 = /*EL:985*/v-3.oprand2();
            /*SL:986*/if (a1 == null) {
                /*SL:987*/return;
            }
            final int v1 = /*EL:989*/this.exprType;
            final int v2 = /*EL:990*/this.arrayDim;
            final String v3 = /*EL:991*/this.className;
            /*SL:992*/a1.accept(this);
            /*SL:993*/if (v2 != this.arrayDim) {
                /*SL:994*/throw new CompileError("incompatible array types");
            }
            /*SL:996*/if (operator == 43 && v2 == 0 && (v1 == 307 || this.exprType == 307)) {
                /*SL:998*/this.atStringConcatExpr(v-3, v1, v2, v3);
            }
            else {
                /*SL:1000*/this.atArithBinExpr(v-3, operator, lookupBinOp, v1);
            }
        }
        else {
            /*SL:1005*/if (!this.booleanExpr(true, v-3)) {
                /*SL:1006*/this.bytecode.addIndex(7);
                /*SL:1007*/this.bytecode.addIconst(0);
                /*SL:1008*/this.bytecode.addOpcode(167);
                /*SL:1009*/this.bytecode.addIndex(4);
            }
            /*SL:1012*/this.bytecode.addIconst(1);
        }
    }
    
    private void atArithBinExpr(final Expr a3, final int a4, final int v1, final int v2) throws CompileError {
        /*SL:1023*/if (this.arrayDim != 0) {
            badTypes(/*EL:1024*/a3);
        }
        final int v3 = /*EL:1026*/this.exprType;
        /*SL:1027*/if (a4 == 364 || a4 == 366 || a4 == 370) {
            /*SL:1028*/if (v3 == 324 || v3 == 334 || v3 == 306 || v3 == 303) {
                /*SL:1030*/this.exprType = v2;
            }
            else {
                badTypes(/*EL:1032*/a3);
            }
        }
        else {
            /*SL:1034*/this.convertOprandTypes(v2, v3, a3);
        }
        final int v4 = typePrecedence(/*EL:1036*/this.exprType);
        /*SL:1037*/if (v4 >= 0) {
            final int a5 = /*EL:1038*/CodeGen.binOp[v1 + v4 + 1];
            /*SL:1039*/if (a5 != 0) {
                /*SL:1040*/if (v4 == 3 && this.exprType != 301) {
                    /*SL:1041*/this.exprType = 324;
                }
                /*SL:1043*/this.bytecode.addOpcode(a5);
                /*SL:1044*/return;
            }
        }
        badTypes(/*EL:1048*/a3);
    }
    
    private void atStringConcatExpr(final Expr a1, final int a2, final int a3, final String a4) throws CompileError {
        final int v1 = /*EL:1054*/this.exprType;
        final int v2 = /*EL:1055*/this.arrayDim;
        final boolean v3 = is2word(/*EL:1056*/v1, v2);
        final boolean v4 = /*EL:1057*/v1 == 307 && "java/lang/String".equals(this.className);
        /*SL:1060*/if (v3) {
            /*SL:1061*/this.convToString(v1, v2);
        }
        /*SL:1063*/if (is2word(a2, a3)) {
            /*SL:1064*/this.bytecode.addOpcode(91);
            /*SL:1065*/this.bytecode.addOpcode(87);
        }
        else {
            /*SL:1068*/this.bytecode.addOpcode(95);
        }
        /*SL:1071*/this.convToString(a2, a3);
        /*SL:1072*/this.bytecode.addOpcode(95);
        /*SL:1074*/if (!v3 && !v4) {
            /*SL:1075*/this.convToString(v1, v2);
        }
        /*SL:1077*/this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        /*SL:1079*/this.exprType = 307;
        /*SL:1080*/this.arrayDim = 0;
        /*SL:1081*/this.className = "java/lang/String";
    }
    
    private void convToString(final int a1, final int a2) throws CompileError {
        final String v1 = /*EL:1085*/"valueOf";
        /*SL:1087*/if (isRefType(a1) || a2 > 0) {
            /*SL:1088*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
        }
        else/*SL:1090*/ if (a1 == 312) {
            /*SL:1091*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(D)Ljava/lang/String;");
        }
        else/*SL:1093*/ if (a1 == 317) {
            /*SL:1094*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(F)Ljava/lang/String;");
        }
        else/*SL:1096*/ if (a1 == 326) {
            /*SL:1097*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(J)Ljava/lang/String;");
        }
        else/*SL:1099*/ if (a1 == 301) {
            /*SL:1100*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Z)Ljava/lang/String;");
        }
        else/*SL:1102*/ if (a1 == 306) {
            /*SL:1103*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(C)Ljava/lang/String;");
        }
        else {
            /*SL:1105*/if (a1 == 344) {
                /*SL:1106*/throw new CompileError("void type expression");
            }
            /*SL:1108*/this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(I)Ljava/lang/String;");
        }
    }
    
    private boolean booleanExpr(final boolean v-4, final ASTree v-3) throws CompileError {
        final int compOperator = getCompOperator(/*EL:1122*/v-3);
        /*SL:1123*/if (compOperator == 358) {
            final BinExpr a1 = /*EL:1124*/(BinExpr)v-3;
            final int a2 = /*EL:1125*/this.compileOprands(a1);
            /*SL:1128*/this.compareExpr(v-4, a1.getOperator(), a2, a1);
        }
        else {
            /*SL:1130*/if (compOperator == 33) {
                /*SL:1131*/return this.booleanExpr(!v-4, ((Expr)v-3).oprand1());
            }
            final boolean v-5;
            /*SL:1132*/if ((v-5 = (compOperator == 369)) || compOperator == 368) {
                final BinExpr v0 = /*EL:1133*/(BinExpr)v-3;
                /*SL:1134*/if (this.booleanExpr(!v-5, v0.oprand1())) {
                    /*SL:1135*/this.exprType = 301;
                    /*SL:1136*/this.arrayDim = 0;
                    /*SL:1137*/return true;
                }
                final int v = /*EL:1140*/this.bytecode.currentPc();
                /*SL:1141*/this.bytecode.addIndex(0);
                /*SL:1142*/if (this.booleanExpr(v-5, v0.oprand2())) {
                    /*SL:1143*/this.bytecode.addOpcode(167);
                }
                /*SL:1145*/this.bytecode.write16bit(v, this.bytecode.currentPc() - v + 3);
                /*SL:1146*/if (v-4 != v-5) {
                    /*SL:1147*/this.bytecode.addIndex(6);
                    /*SL:1148*/this.bytecode.addOpcode(167);
                }
            }
            else {
                /*SL:1152*/if (isAlwaysBranch(v-3, v-4)) {
                    /*SL:1154*/this.exprType = 301;
                    /*SL:1155*/this.arrayDim = 0;
                    /*SL:1156*/return true;
                }
                /*SL:1159*/v-3.accept(this);
                /*SL:1160*/if (this.exprType != 301 || this.arrayDim != 0) {
                    /*SL:1161*/throw new CompileError("boolean expr is required");
                }
                /*SL:1163*/this.bytecode.addOpcode(v-4 ? 154 : 153);
            }
        }
        /*SL:1166*/this.exprType = 301;
        /*SL:1167*/this.arrayDim = 0;
        /*SL:1168*/return false;
    }
    
    private static boolean isAlwaysBranch(final ASTree a2, final boolean v1) {
        /*SL:1172*/if (a2 instanceof Keyword) {
            final int a3 = /*EL:1173*/((Keyword)a2).get();
            /*SL:1174*/return v1 ? (a3 == 410) : (a3 == 411);
        }
        /*SL:1177*/return false;
    }
    
    static int getCompOperator(final ASTree v-1) throws CompileError {
        /*SL:1181*/if (!(v-1 instanceof Expr)) {
            /*SL:1194*/return 32;
        }
        final Expr a1 = (Expr)v-1;
        final int v1 = a1.getOperator();
        if (v1 == 33) {
            return 33;
        }
        if (a1 instanceof BinExpr && v1 != 368 && v1 != 369 && v1 != 38 && v1 != 124) {
            return 358;
        }
        return v1;
    }
    
    private int compileOprands(final BinExpr a1) throws CompileError {
        /*SL:1198*/a1.oprand1().accept(this);
        final int v1 = /*EL:1199*/this.exprType;
        final int v2 = /*EL:1200*/this.arrayDim;
        /*SL:1201*/a1.oprand2().accept(this);
        /*SL:1202*/if (v2 != this.arrayDim) {
            /*SL:1203*/if (v1 != 412 && this.exprType != 412) {
                /*SL:1204*/throw new CompileError("incompatible array types");
            }
            /*SL:1205*/if (this.exprType == 412) {
                /*SL:1206*/this.arrayDim = v2;
            }
        }
        /*SL:1208*/if (v1 == 412) {
            /*SL:1209*/return this.exprType;
        }
        /*SL:1211*/return v1;
    }
    
    private void compareExpr(final boolean v2, final int v3, final int v4, final BinExpr v5) throws CompileError {
        /*SL:1237*/if (this.arrayDim == 0) {
            /*SL:1238*/this.convertOprandTypes(v4, this.exprType, v5);
        }
        final int v6 = typePrecedence(/*EL:1240*/this.exprType);
        /*SL:1241*/if (v6 == -1 || this.arrayDim > 0) {
            /*SL:1242*/if (v3 == 358) {
                /*SL:1243*/this.bytecode.addOpcode(v2 ? 165 : 166);
            }
            else/*SL:1244*/ if (v3 == 350) {
                /*SL:1245*/this.bytecode.addOpcode(v2 ? 166 : 165);
            }
            else {
                badTypes(/*EL:1247*/v5);
            }
        }
        else/*SL:1249*/ if (v6 == 3) {
            int[] a2;
            int a2;
            /*SL:1251*/for (a2 = CodeGen.ifOp, a2 = 0; a2 < a2.length; a2 += 3) {
                /*SL:1252*/if (a2[a2] == v3) {
                    /*SL:1253*/this.bytecode.addOpcode(a2[a2 + (v2 ? 1 : 2)]);
                    /*SL:1254*/return;
                }
            }
            badTypes(/*EL:1257*/v5);
        }
        else {
            /*SL:1260*/if (v6 == 0) {
                /*SL:1261*/if (v3 == 60 || v3 == 357) {
                    /*SL:1262*/this.bytecode.addOpcode(152);
                }
                else {
                    /*SL:1264*/this.bytecode.addOpcode(151);
                }
            }
            else/*SL:1265*/ if (v6 == 1) {
                /*SL:1266*/if (v3 == 60 || v3 == 357) {
                    /*SL:1267*/this.bytecode.addOpcode(150);
                }
                else {
                    /*SL:1269*/this.bytecode.addOpcode(149);
                }
            }
            else/*SL:1270*/ if (v6 == 2) {
                /*SL:1271*/this.bytecode.addOpcode(148);
            }
            else {
                fatal();
            }
            final int[] a3 = CodeGen.ifOp2;
            /*SL:1276*/for (int a4 = 0; a4 < a3.length; a4 += 3) {
                /*SL:1277*/if (a3[a4] == v3) {
                    /*SL:1278*/this.bytecode.addOpcode(a3[a4 + (v2 ? 1 : 2)]);
                    /*SL:1279*/return;
                }
            }
            badTypes(/*EL:1282*/v5);
        }
    }
    
    protected static void badTypes(final Expr a1) throws CompileError {
        /*SL:1287*/throw new CompileError("invalid types for " + a1.getName());
    }
    
    protected static boolean isRefType(final int a1) {
        /*SL:1297*/return a1 == 307 || a1 == 412;
    }
    
    private static int typePrecedence(final int a1) {
        /*SL:1301*/if (a1 == 312) {
            /*SL:1302*/return 0;
        }
        /*SL:1303*/if (a1 == 317) {
            /*SL:1304*/return 1;
        }
        /*SL:1305*/if (a1 == 326) {
            /*SL:1306*/return 2;
        }
        /*SL:1307*/if (isRefType(a1)) {
            /*SL:1308*/return -1;
        }
        /*SL:1309*/if (a1 == 344) {
            /*SL:1310*/return -1;
        }
        /*SL:1312*/return 3;
    }
    
    static boolean isP_INT(final int a1) {
        /*SL:1317*/return typePrecedence(a1) == 3;
    }
    
    static boolean rightIsStrong(final int a1, final int a2) {
        final int v1 = typePrecedence(/*EL:1322*/a1);
        final int v2 = typePrecedence(/*EL:1323*/a2);
        /*SL:1324*/return v1 >= 0 && v2 >= 0 && v1 > v2;
    }
    
    private void convertOprandTypes(final int v2, final int v3, final Expr v4) throws CompileError {
        final int v5 = typePrecedence(/*EL:1341*/v2);
        final int v6 = typePrecedence(/*EL:1342*/v3);
        /*SL:1344*/if (v6 < 0 && v5 < 0) {
            /*SL:1345*/return;
        }
        /*SL:1347*/if (v6 < 0 || v5 < 0) {
            badTypes(/*EL:1348*/v4);
        }
        final boolean v7;
        final int v8;
        final int v9;
        /*SL:1351*/if (v5 <= v6) {
            final boolean a1 = /*EL:1352*/false;
            /*SL:1353*/this.exprType = v2;
            final int a2 = /*EL:1354*/CodeGen.castOp[v6 * 4 + v5];
            final int a3 = /*EL:1355*/v5;
        }
        else {
            /*SL:1358*/v7 = true;
            /*SL:1359*/v8 = CodeGen.castOp[v5 * 4 + v6];
            /*SL:1360*/v9 = v6;
        }
        /*SL:1363*/if (v7) {
            /*SL:1364*/if (v9 == 0 || v9 == 2) {
                /*SL:1365*/if (v5 == 0 || v5 == 2) {
                    /*SL:1366*/this.bytecode.addOpcode(94);
                }
                else {
                    /*SL:1368*/this.bytecode.addOpcode(93);
                }
                /*SL:1370*/this.bytecode.addOpcode(88);
                /*SL:1371*/this.bytecode.addOpcode(v8);
                /*SL:1372*/this.bytecode.addOpcode(94);
                /*SL:1373*/this.bytecode.addOpcode(88);
            }
            else/*SL:1375*/ if (v9 == 1) {
                /*SL:1376*/if (v5 == 2) {
                    /*SL:1377*/this.bytecode.addOpcode(91);
                    /*SL:1378*/this.bytecode.addOpcode(87);
                }
                else {
                    /*SL:1381*/this.bytecode.addOpcode(95);
                }
                /*SL:1383*/this.bytecode.addOpcode(v8);
                /*SL:1384*/this.bytecode.addOpcode(95);
            }
            else {
                fatal();
            }
        }
        else/*SL:1389*/ if (v8 != 0) {
            /*SL:1390*/this.bytecode.addOpcode(v8);
        }
    }
    
    @Override
    public void atCastExpr(final CastExpr a1) throws CompileError {
        final String v1 = /*EL:1394*/this.resolveClassName(a1.getClassName());
        final String v2 = /*EL:1395*/this.checkCastExpr(a1, v1);
        final int v3 = /*EL:1396*/this.exprType;
        /*SL:1397*/this.exprType = a1.getType();
        /*SL:1398*/this.arrayDim = a1.getArrayDim();
        /*SL:1399*/this.className = v1;
        /*SL:1400*/if (v2 == null) {
            /*SL:1401*/this.atNumCastExpr(v3, this.exprType);
        }
        else {
            /*SL:1403*/this.bytecode.addCheckcast(v2);
        }
    }
    
    @Override
    public void atInstanceOfExpr(final InstanceOfExpr a1) throws CompileError {
        final String v1 = /*EL:1407*/this.resolveClassName(a1.getClassName());
        final String v2 = /*EL:1408*/this.checkCastExpr(a1, v1);
        /*SL:1409*/this.bytecode.addInstanceof(v2);
        /*SL:1410*/this.exprType = 301;
        /*SL:1411*/this.arrayDim = 0;
    }
    
    private String checkCastExpr(final CastExpr a1, final String a2) throws CompileError {
        final String v1 = /*EL:1417*/"invalid cast";
        final ASTree v2 = /*EL:1418*/a1.getOprand();
        final int v3 = /*EL:1419*/a1.getArrayDim();
        final int v4 = /*EL:1420*/a1.getType();
        /*SL:1421*/v2.accept(this);
        final int v5 = /*EL:1422*/this.exprType;
        final int v6 = /*EL:1423*/this.arrayDim;
        /*SL:1424*/if (this.invalidDim(v5, this.arrayDim, this.className, v4, v3, a2, true) || v5 == 344 || v4 == 344) {
            /*SL:1426*/throw new CompileError("invalid cast");
        }
        /*SL:1428*/if (v4 == 307) {
            /*SL:1429*/if (!isRefType(v5) && v6 == 0) {
                /*SL:1430*/throw new CompileError("invalid cast");
            }
            /*SL:1432*/return toJvmArrayName(a2, v3);
        }
        else {
            /*SL:1435*/if (v3 > 0) {
                /*SL:1436*/return toJvmTypeName(v4, v3);
            }
            /*SL:1438*/return null;
        }
    }
    
    void atNumCastExpr(final int v-2, final int v-1) throws CompileError {
        /*SL:1444*/if (v-2 == v-1) {
            /*SL:1445*/return;
        }
        int v2 = typePrecedence(/*EL:1448*/v-2);
        /*SL:1449*/v2 = typePrecedence(v-1);
        final int v3;
        /*SL:1450*/if (0 <= v2 && v2 < 3) {
            final int a1 = /*EL:1451*/CodeGen.castOp[v2 * 4 + v2];
        }
        else {
            /*SL:1453*/v3 = 0;
        }
        int v4 = 0;
        /*SL:1455*/if (v-1 == 312) {
            final int a2 = /*EL:1456*/135;
        }
        else/*SL:1457*/ if (v-1 == 317) {
            /*SL:1458*/v4 = 134;
        }
        else/*SL:1459*/ if (v-1 == 326) {
            /*SL:1460*/v4 = 133;
        }
        else/*SL:1461*/ if (v-1 == 334) {
            /*SL:1462*/v4 = 147;
        }
        else/*SL:1463*/ if (v-1 == 306) {
            /*SL:1464*/v4 = 146;
        }
        else/*SL:1465*/ if (v-1 == 303) {
            /*SL:1466*/v4 = 145;
        }
        else {
            /*SL:1468*/v4 = 0;
        }
        /*SL:1470*/if (v3 != 0) {
            /*SL:1471*/this.bytecode.addOpcode(v3);
        }
        /*SL:1473*/if ((v3 == 0 || v3 == 136 || v3 == 139 || v3 == 142) && /*EL:1474*/v4 != 0) {
            /*SL:1475*/this.bytecode.addOpcode(v4);
        }
    }
    
    @Override
    public void atExpr(final Expr v-2) throws CompileError {
        final int operator = /*EL:1482*/v-2.getOperator();
        final ASTree v0 = /*EL:1483*/v-2.oprand1();
        /*SL:1484*/if (operator == 46) {
            final String a1 = /*EL:1485*/((Symbol)v-2.oprand2()).get();
            /*SL:1486*/if (a1.equals("class")) {
                /*SL:1487*/this.atClassObject(v-2);
            }
            else {
                /*SL:1489*/this.atFieldRead(v-2);
            }
        }
        else/*SL:1491*/ if (operator == 35) {
            /*SL:1496*/this.atFieldRead(v-2);
        }
        else/*SL:1498*/ if (operator == 65) {
            /*SL:1499*/this.atArrayRead(v0, v-2.oprand2());
        }
        else/*SL:1500*/ if (operator == 362 || operator == 363) {
            /*SL:1501*/this.atPlusPlus(operator, v0, v-2, true);
        }
        else/*SL:1502*/ if (operator == 33) {
            /*SL:1503*/if (!this.booleanExpr(false, v-2)) {
                /*SL:1504*/this.bytecode.addIndex(7);
                /*SL:1505*/this.bytecode.addIconst(1);
                /*SL:1506*/this.bytecode.addOpcode(167);
                /*SL:1507*/this.bytecode.addIndex(4);
            }
            /*SL:1510*/this.bytecode.addIconst(0);
        }
        else/*SL:1512*/ if (operator == 67) {
            fatal();
        }
        else {
            /*SL:1515*/v-2.oprand1().accept(this);
            final int v = typePrecedence(/*EL:1516*/this.exprType);
            /*SL:1517*/if (this.arrayDim > 0) {
                badType(/*EL:1518*/v-2);
            }
            /*SL:1520*/if (operator == 45) {
                /*SL:1521*/if (v == 0) {
                    /*SL:1522*/this.bytecode.addOpcode(119);
                }
                else/*SL:1523*/ if (v == 1) {
                    /*SL:1524*/this.bytecode.addOpcode(118);
                }
                else/*SL:1525*/ if (v == 2) {
                    /*SL:1526*/this.bytecode.addOpcode(117);
                }
                else/*SL:1527*/ if (v == 3) {
                    /*SL:1528*/this.bytecode.addOpcode(116);
                    /*SL:1529*/this.exprType = 324;
                }
                else {
                    badType(/*EL:1532*/v-2);
                }
            }
            else/*SL:1534*/ if (operator == 126) {
                /*SL:1535*/if (v == 3) {
                    /*SL:1536*/this.bytecode.addIconst(-1);
                    /*SL:1537*/this.bytecode.addOpcode(130);
                    /*SL:1538*/this.exprType = 324;
                }
                else/*SL:1540*/ if (v == 2) {
                    /*SL:1541*/this.bytecode.addLconst(-1L);
                    /*SL:1542*/this.bytecode.addOpcode(131);
                }
                else {
                    badType(/*EL:1545*/v-2);
                }
            }
            else/*SL:1548*/ if (operator == 43) {
                /*SL:1549*/if (v == -1) {
                    badType(/*EL:1550*/v-2);
                }
            }
            else {
                fatal();
            }
        }
    }
    
    protected static void badType(final Expr a1) throws CompileError {
        /*SL:1560*/throw new CompileError("invalid type for " + a1.getName());
    }
    
    @Override
    public abstract void atCallExpr(final CallExpr p0) throws CompileError;
    
    protected abstract void atFieldRead(final ASTree p0) throws CompileError;
    
    public void atClassObject(final Expr v-3) throws CompileError {
        final ASTree oprand1 = /*EL:1568*/v-3.oprand1();
        /*SL:1569*/if (!(oprand1 instanceof Symbol)) {
            /*SL:1570*/throw new CompileError("fatal error: badly parsed .class expr");
        }
        String a2 = /*EL:1572*/((Symbol)oprand1).get();
        /*SL:1573*/if (a2.startsWith("[")) {
            int v0 = /*EL:1574*/a2.indexOf("[L");
            /*SL:1575*/if (v0 >= 0) {
                final String v = /*EL:1576*/a2.substring(v0 + 2, a2.length() - 1);
                String v2 = /*EL:1577*/this.resolveClassName(v);
                /*SL:1578*/if (!v.equals(v2)) {
                    /*SL:1583*/v2 = MemberResolver.jvmToJavaName(v2);
                    final StringBuffer a1 = /*EL:1584*/new StringBuffer();
                    /*SL:1585*/while (v0-- >= 0) {
                        /*SL:1586*/a1.append('[');
                    }
                    /*SL:1588*/a1.append('L').append(v2).append(';');
                    /*SL:1589*/a2 = a1.toString();
                }
            }
        }
        else {
            /*SL:1594*/a2 = this.resolveClassName(MemberResolver.javaToJvmName(a2));
            /*SL:1595*/a2 = MemberResolver.jvmToJavaName(a2);
        }
        /*SL:1598*/this.atClassObject2(a2);
        /*SL:1599*/this.exprType = 307;
        /*SL:1600*/this.arrayDim = 0;
        /*SL:1601*/this.className = "java/lang/Class";
    }
    
    protected void atClassObject2(final String a1) throws CompileError {
        final int v1 = /*EL:1607*/this.bytecode.currentPc();
        /*SL:1608*/this.bytecode.addLdc(a1);
        /*SL:1609*/this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        final int v2 = /*EL:1611*/this.bytecode.currentPc();
        /*SL:1612*/this.bytecode.addOpcode(167);
        final int v3 = /*EL:1613*/this.bytecode.currentPc();
        /*SL:1614*/this.bytecode.addIndex(0);
        /*SL:1616*/this.bytecode.addExceptionHandler(v1, v2, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
        /*SL:1635*/this.bytecode.growStack(1);
        /*SL:1636*/this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
        /*SL:1639*/this.bytecode.addOpcode(191);
        /*SL:1640*/this.bytecode.write16bit(v3, this.bytecode.currentPc() - v3 + 1);
    }
    
    public void atArrayRead(final ASTree a1, final ASTree a2) throws CompileError {
        /*SL:1646*/this.arrayAccess(a1, a2);
        /*SL:1647*/this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
    }
    
    protected void arrayAccess(final ASTree a1, final ASTree a2) throws CompileError {
        /*SL:1653*/a1.accept(this);
        final int v1 = /*EL:1654*/this.exprType;
        final int v2 = /*EL:1655*/this.arrayDim;
        /*SL:1656*/if (v2 == 0) {
            /*SL:1657*/throw new CompileError("bad array access");
        }
        final String v3 = /*EL:1659*/this.className;
        /*SL:1661*/a2.accept(this);
        /*SL:1662*/if (typePrecedence(this.exprType) != 3 || this.arrayDim > 0) {
            /*SL:1663*/throw new CompileError("bad array index");
        }
        /*SL:1665*/this.exprType = v1;
        /*SL:1666*/this.arrayDim = v2 - 1;
        /*SL:1667*/this.className = v3;
    }
    
    protected static int getArrayReadOp(final int a1, final int a2) {
        /*SL:1671*/if (a2 > 0) {
            /*SL:1672*/return 50;
        }
        /*SL:1674*/switch (a1) {
            case 312: {
                /*SL:1676*/return 49;
            }
            case 317: {
                /*SL:1678*/return 48;
            }
            case 326: {
                /*SL:1680*/return 47;
            }
            case 324: {
                /*SL:1682*/return 46;
            }
            case 334: {
                /*SL:1684*/return 53;
            }
            case 306: {
                /*SL:1686*/return 52;
            }
            case 301:
            case 303: {
                /*SL:1689*/return 51;
            }
            default: {
                /*SL:1691*/return 50;
            }
        }
    }
    
    protected static int getArrayWriteOp(final int a1, final int a2) {
        /*SL:1696*/if (a2 > 0) {
            /*SL:1697*/return 83;
        }
        /*SL:1699*/switch (a1) {
            case 312: {
                /*SL:1701*/return 82;
            }
            case 317: {
                /*SL:1703*/return 81;
            }
            case 326: {
                /*SL:1705*/return 80;
            }
            case 324: {
                /*SL:1707*/return 79;
            }
            case 334: {
                /*SL:1709*/return 86;
            }
            case 306: {
                /*SL:1711*/return 85;
            }
            case 301:
            case 303: {
                /*SL:1714*/return 84;
            }
            default: {
                /*SL:1716*/return 83;
            }
        }
    }
    
    private void atPlusPlus(final int v-4, ASTree v-3, final Expr v-2, final boolean v-1) throws CompileError {
        final boolean v0 = /*EL:1723*/v-3 == null;
        /*SL:1724*/if (v0) {
            /*SL:1725*/v-3 = v-2.oprand2();
        }
        /*SL:1727*/if (v-3 instanceof Variable) {
            Declarator a2 = /*EL:1728*/((Variable)v-3).getDeclarator();
            final int type = /*EL:1729*/a2.getType();
            this.exprType = type;
            a2 = type;
            /*SL:1730*/this.arrayDim = a2.getArrayDim();
            final int a3 = /*EL:1731*/this.getLocalVar(a2);
            /*SL:1732*/if (this.arrayDim > 0) {
                badType(/*EL:1733*/v-2);
            }
            /*SL:1735*/if (a2 == 312) {
                /*SL:1736*/this.bytecode.addDload(a3);
                /*SL:1737*/if (v-1 && v0) {
                    /*SL:1738*/this.bytecode.addOpcode(92);
                }
                /*SL:1740*/this.bytecode.addDconst(1.0);
                /*SL:1741*/this.bytecode.addOpcode((v-4 == 362) ? 99 : 103);
                /*SL:1742*/if (v-1 && !v0) {
                    /*SL:1743*/this.bytecode.addOpcode(92);
                }
                /*SL:1745*/this.bytecode.addDstore(a3);
            }
            else/*SL:1747*/ if (a2 == 326) {
                /*SL:1748*/this.bytecode.addLload(a3);
                /*SL:1749*/if (v-1 && v0) {
                    /*SL:1750*/this.bytecode.addOpcode(92);
                }
                /*SL:1752*/this.bytecode.addLconst(1L);
                /*SL:1753*/this.bytecode.addOpcode((v-4 == 362) ? 97 : 101);
                /*SL:1754*/if (v-1 && !v0) {
                    /*SL:1755*/this.bytecode.addOpcode(92);
                }
                /*SL:1757*/this.bytecode.addLstore(a3);
            }
            else/*SL:1759*/ if (a2 == 317) {
                /*SL:1760*/this.bytecode.addFload(a3);
                /*SL:1761*/if (v-1 && v0) {
                    /*SL:1762*/this.bytecode.addOpcode(89);
                }
                /*SL:1764*/this.bytecode.addFconst(1.0f);
                /*SL:1765*/this.bytecode.addOpcode((v-4 == 362) ? 98 : 102);
                /*SL:1766*/if (v-1 && !v0) {
                    /*SL:1767*/this.bytecode.addOpcode(89);
                }
                /*SL:1769*/this.bytecode.addFstore(a3);
            }
            else/*SL:1771*/ if (a2 == 303 || a2 == 306 || a2 == 334 || a2 == 324) {
                /*SL:1772*/if (v-1 && v0) {
                    /*SL:1773*/this.bytecode.addIload(a3);
                }
                final int a4 = /*EL:1775*/(v-4 == 362) ? 1 : -1;
                /*SL:1776*/if (a3 > 255) {
                    /*SL:1777*/this.bytecode.addOpcode(196);
                    /*SL:1778*/this.bytecode.addOpcode(132);
                    /*SL:1779*/this.bytecode.addIndex(a3);
                    /*SL:1780*/this.bytecode.addIndex(a4);
                }
                else {
                    /*SL:1783*/this.bytecode.addOpcode(132);
                    /*SL:1784*/this.bytecode.add(a3);
                    /*SL:1785*/this.bytecode.add(a4);
                }
                /*SL:1788*/if (v-1 && !v0) {
                    /*SL:1789*/this.bytecode.addIload(a3);
                }
            }
            else {
                badType(/*EL:1792*/v-2);
            }
        }
        else {
            /*SL:1795*/if (v-3 instanceof Expr) {
                final Expr v = /*EL:1796*/(Expr)v-3;
                /*SL:1797*/if (v.getOperator() == 65) {
                    /*SL:1798*/this.atArrayPlusPlus(v-4, v0, v, v-1);
                    /*SL:1799*/return;
                }
            }
            /*SL:1803*/this.atFieldPlusPlus(v-4, v0, v-3, v-2, v-1);
        }
    }
    
    public void atArrayPlusPlus(final int a1, final boolean a2, final Expr a3, final boolean a4) throws CompileError {
        /*SL:1810*/this.arrayAccess(a3.oprand1(), a3.oprand2());
        final int v1 = /*EL:1811*/this.exprType;
        final int v2 = /*EL:1812*/this.arrayDim;
        /*SL:1813*/if (v2 > 0) {
            badType(/*EL:1814*/a3);
        }
        /*SL:1816*/this.bytecode.addOpcode(92);
        /*SL:1817*/this.bytecode.addOpcode(getArrayReadOp(v1, this.arrayDim));
        final int v3 = is2word(/*EL:1818*/v1, v2) ? 94 : 91;
        /*SL:1819*/this.atPlusPlusCore(v3, a4, a1, a2, a3);
        /*SL:1820*/this.bytecode.addOpcode(getArrayWriteOp(v1, v2));
    }
    
    protected void atPlusPlusCore(final int a1, final boolean a2, final int a3, final boolean a4, final Expr a5) throws CompileError {
        final int v1 = /*EL:1827*/this.exprType;
        /*SL:1829*/if (a2 && a4) {
            /*SL:1830*/this.bytecode.addOpcode(a1);
        }
        /*SL:1832*/if (v1 == 324 || v1 == 303 || v1 == 306 || v1 == 334) {
            /*SL:1833*/this.bytecode.addIconst(1);
            /*SL:1834*/this.bytecode.addOpcode((a3 == 362) ? 96 : 100);
            /*SL:1835*/this.exprType = 324;
        }
        else/*SL:1837*/ if (v1 == 326) {
            /*SL:1838*/this.bytecode.addLconst(1L);
            /*SL:1839*/this.bytecode.addOpcode((a3 == 362) ? 97 : 101);
        }
        else/*SL:1841*/ if (v1 == 317) {
            /*SL:1842*/this.bytecode.addFconst(1.0f);
            /*SL:1843*/this.bytecode.addOpcode((a3 == 362) ? 98 : 102);
        }
        else/*SL:1845*/ if (v1 == 312) {
            /*SL:1846*/this.bytecode.addDconst(1.0);
            /*SL:1847*/this.bytecode.addOpcode((a3 == 362) ? 99 : 103);
        }
        else {
            badType(/*EL:1850*/a5);
        }
        /*SL:1852*/if (a2 && !a4) {
            /*SL:1853*/this.bytecode.addOpcode(a1);
        }
    }
    
    protected abstract void atFieldPlusPlus(final int p0, final boolean p1, final ASTree p2, final Expr p3, final boolean p4) throws CompileError;
    
    @Override
    public abstract void atMember(final Member p0) throws CompileError;
    
    @Override
    public void atVariable(final Variable a1) throws CompileError {
        final Declarator v1 = /*EL:1862*/a1.getDeclarator();
        /*SL:1863*/this.exprType = v1.getType();
        /*SL:1864*/this.arrayDim = v1.getArrayDim();
        /*SL:1865*/this.className = v1.getClassName();
        final int v2 = /*EL:1866*/this.getLocalVar(v1);
        /*SL:1868*/if (this.arrayDim > 0) {
            /*SL:1869*/this.bytecode.addAload(v2);
        }
        else {
            /*SL:1871*/switch (this.exprType) {
                case 307: {
                    /*SL:1873*/this.bytecode.addAload(v2);
                    /*SL:1874*/break;
                }
                case 326: {
                    /*SL:1876*/this.bytecode.addLload(v2);
                    /*SL:1877*/break;
                }
                case 317: {
                    /*SL:1879*/this.bytecode.addFload(v2);
                    /*SL:1880*/break;
                }
                case 312: {
                    /*SL:1882*/this.bytecode.addDload(v2);
                    /*SL:1883*/break;
                }
                default: {
                    /*SL:1885*/this.bytecode.addIload(v2);
                    break;
                }
            }
        }
    }
    
    @Override
    public void atKeyword(final Keyword a1) throws CompileError {
        /*SL:1891*/this.arrayDim = 0;
        final int v1 = /*EL:1892*/a1.get();
        /*SL:1893*/switch (v1) {
            case 410: {
                /*SL:1895*/this.bytecode.addIconst(1);
                /*SL:1896*/this.exprType = 301;
                /*SL:1897*/break;
            }
            case 411: {
                /*SL:1899*/this.bytecode.addIconst(0);
                /*SL:1900*/this.exprType = 301;
                /*SL:1901*/break;
            }
            case 412: {
                /*SL:1903*/this.bytecode.addOpcode(1);
                /*SL:1904*/this.exprType = 412;
                /*SL:1905*/break;
            }
            case 336:
            case 339: {
                /*SL:1908*/if (this.inStaticMethod) {
                    /*SL:1909*/throw new CompileError("not-available: " + ((v1 == 339) ? "this" : "super"));
                }
                /*SL:1912*/this.bytecode.addAload(0);
                /*SL:1913*/this.exprType = 307;
                /*SL:1914*/if (v1 == 339) {
                    /*SL:1915*/this.className = this.getThisName();
                    break;
                }
                /*SL:1917*/this.className = this.getSuperName();
                /*SL:1918*/break;
            }
            default: {
                fatal();
                break;
            }
        }
    }
    
    @Override
    public void atStringL(final StringL a1) throws CompileError {
        /*SL:1925*/this.exprType = 307;
        /*SL:1926*/this.arrayDim = 0;
        /*SL:1927*/this.className = "java/lang/String";
        /*SL:1928*/this.bytecode.addLdc(a1.get());
    }
    
    @Override
    public void atIntConst(final IntConst a1) throws CompileError {
        /*SL:1932*/this.arrayDim = 0;
        final long v1 = /*EL:1933*/a1.get();
        final int v2 = /*EL:1934*/a1.getType();
        /*SL:1935*/if (v2 == 402 || v2 == 401) {
            /*SL:1936*/this.exprType = ((v2 == 402) ? 324 : 306);
            /*SL:1937*/this.bytecode.addIconst((int)v1);
        }
        else {
            /*SL:1940*/this.exprType = 326;
            /*SL:1941*/this.bytecode.addLconst(v1);
        }
    }
    
    @Override
    public void atDoubleConst(final DoubleConst a1) throws CompileError {
        /*SL:1946*/this.arrayDim = 0;
        /*SL:1947*/if (a1.getType() == 405) {
            /*SL:1948*/this.exprType = 312;
            /*SL:1949*/this.bytecode.addDconst(a1.get());
        }
        else {
            /*SL:1952*/this.exprType = 317;
            /*SL:1953*/this.bytecode.addFconst((float)a1.get());
        }
    }
    
    static {
        binOp = new int[] { 43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124 };
        ifOp = new int[] { 358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164 };
        ifOp2 = new int[] { 358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158 };
        castOp = new int[] { 0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0 };
    }
    
    protected abstract static class ReturnHook
    {
        ReturnHook next;
        
        protected abstract boolean doit(final Bytecode p0, final int p1);
        
        protected ReturnHook(final CodeGen a1) {
            this.next = a1.returnHooks;
            a1.returnHooks = this;
        }
        
        protected void remove(final CodeGen a1) {
            /*SL:70*/a1.returnHooks = this.next;
        }
    }
}
