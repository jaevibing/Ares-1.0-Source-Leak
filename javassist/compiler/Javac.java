package javassist.compiler;

import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.ASTree;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.CodeAttribute;
import javassist.CtPrimitiveType;
import javassist.compiler.ast.Stmnt;
import javassist.NotFoundException;
import javassist.CtMethod;
import javassist.compiler.ast.Visitor;
import javassist.CtConstructor;
import javassist.Modifier;
import javassist.compiler.ast.Declarator;
import javassist.CtField;
import javassist.CtBehavior;
import javassist.compiler.ast.ASTList;
import javassist.CannotCompileException;
import javassist.bytecode.BadBytecode;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.FieldDecl;
import javassist.CtMember;
import javassist.CtClass;
import javassist.bytecode.Bytecode;

public class Javac
{
    JvstCodeGen gen;
    SymbolTable stable;
    private Bytecode bytecode;
    public static final String param0Name = "$0";
    public static final String resultVarName = "$_";
    public static final String proceedName = "$proceed";
    
    public Javac(final CtClass a1) {
        this(new Bytecode(a1.getClassFile2().getConstPool(), 0, 0), a1);
    }
    
    public Javac(final Bytecode a1, final CtClass a2) {
        this.gen = new JvstCodeGen(a1, a2, a2.getClassPool());
        this.stable = new SymbolTable();
        this.bytecode = a1;
    }
    
    public Bytecode getBytecode() {
        /*SL:74*/return this.bytecode;
    }
    
    public CtMember compile(final String v-3) throws CompileError {
        final Parser v-4 = /*EL:89*/new Parser(new Lex(v-3));
        final ASTList member1 = /*EL:90*/v-4.parseMember1(this.stable);
        try {
            /*SL:92*/if (member1 instanceof FieldDecl) {
                /*SL:93*/return this.compileField((FieldDecl)member1);
            }
            final CtBehavior a1 = /*EL:95*/this.compileMethod(v-4, (MethodDecl)member1);
            final CtClass v1 = /*EL:96*/a1.getDeclaringClass();
            /*SL:97*/a1.getMethodInfo2().rebuildStackMapIf6(v1.getClassPool(), /*EL:98*/v1.getClassFile2());
            /*SL:100*/return a1;
        }
        catch (BadBytecode v2) {
            /*SL:104*/throw new CompileError(v2.getMessage());
        }
        catch (CannotCompileException v3) {
            /*SL:107*/throw new CompileError(v3.getMessage());
        }
    }
    
    private CtField compileField(final FieldDecl a1) throws CompileError, CannotCompileException {
        Declarator v2 = /*EL:132*/a1.getDeclarator();
        /*SL:134*/v2 = new CtFieldWithInit(this.gen.resolver.lookupClass(v2), v2.getVariable().get(), this.gen.getThisClass());
        /*SL:135*/v2.setModifiers(MemberResolver.getModifiers(a1.getModifiers()));
        /*SL:136*/if (a1.getInit() != null) {
            /*SL:137*/v2.setInit(a1.getInit());
        }
        /*SL:139*/return v2;
    }
    
    private CtBehavior compileMethod(final Parser v-5, MethodDecl v-4) throws CompileError {
        final int modifiers = /*EL:145*/MemberResolver.getModifiers(v-4.getModifiers());
        final CtClass[] paramList = /*EL:146*/this.gen.makeParamList(v-4);
        final CtClass[] throwsList = /*EL:147*/this.gen.makeThrowsList(v-4);
        /*SL:148*/this.recordParams(paramList, Modifier.isStatic(modifiers));
        /*SL:149*/v-4 = v-5.parseMethod2(this.stable, v-4);
        try {
            /*SL:151*/if (v-4.isConstructor()) {
                final CtConstructor a1 = /*EL:152*/new CtConstructor(paramList, this.gen.getThisClass());
                /*SL:154*/a1.setModifiers(modifiers);
                /*SL:155*/v-4.accept(this.gen);
                /*SL:156*/a1.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
                /*SL:158*/a1.setExceptionTypes(throwsList);
                /*SL:159*/return a1;
            }
            final Declarator a2 = /*EL:162*/v-4.getReturn();
            final CtClass v1 = /*EL:163*/this.gen.resolver.lookupClass(a2);
            /*SL:164*/this.recordReturnType(v1, false);
            final CtMethod v2 = /*EL:165*/new CtMethod(v1, a2.getVariable().get(), paramList, this.gen.getThisClass());
            /*SL:167*/v2.setModifiers(modifiers);
            /*SL:168*/this.gen.setThisMethod(v2);
            /*SL:169*/v-4.accept(this.gen);
            /*SL:170*/if (v-4.getBody() != null) {
                /*SL:171*/v2.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
            }
            else {
                /*SL:174*/v2.setModifiers(modifiers | 0x400);
            }
            /*SL:176*/v2.setExceptionTypes(throwsList);
            /*SL:177*/return v2;
        }
        catch (NotFoundException v3) {
            /*SL:181*/throw new CompileError(v3.toString());
        }
    }
    
    public Bytecode compileBody(final CtBehavior v-5, final String v-4) throws CompileError {
        try {
            final int modifiers = /*EL:195*/v-5.getModifiers();
            /*SL:196*/this.recordParams(v-5.getParameterTypes(), Modifier.isStatic(modifiers));
            final CtClass voidType;
            /*SL:199*/if (v-5 instanceof CtMethod) {
                /*SL:200*/this.gen.setThisMethod((CtMethod)v-5);
                final CtClass a1 = /*EL:201*/((CtMethod)v-5).getReturnType();
            }
            else {
                /*SL:204*/voidType = CtClass.voidType;
            }
            /*SL:206*/this.recordReturnType(voidType, false);
            final boolean a3 = /*EL:207*/voidType == CtClass.voidType;
            /*SL:209*/if (v-4 == null) {
                makeDefaultBody(/*EL:210*/this.bytecode, voidType);
            }
            else {
                final Parser a2 = /*EL:212*/new Parser(new Lex(v-4));
                final SymbolTable v1 = /*EL:213*/new SymbolTable(this.stable);
                final Stmnt v2 = /*EL:214*/a2.parseStatement(v1);
                /*SL:215*/if (a2.hasMore()) {
                    /*SL:216*/throw new CompileError("the method/constructor body must be surrounded by {}");
                }
                boolean v3 = /*EL:219*/false;
                /*SL:220*/if (v-5 instanceof CtConstructor) {
                    /*SL:221*/v3 = !((CtConstructor)v-5).isClassInitializer();
                }
                /*SL:223*/this.gen.atMethodBody(v2, v3, a3);
            }
            /*SL:226*/return this.bytecode;
        }
        catch (NotFoundException ex) {
            /*SL:229*/throw new CompileError(ex.toString());
        }
    }
    
    private static void makeDefaultBody(final Bytecode v-2, final CtClass v-1) {
        CtPrimitiveType v2;
        int v2;
        int v3 = 0;
        /*SL:236*/if (v-1 instanceof CtPrimitiveType) {
            /*SL:237*/v2 = (CtPrimitiveType)v-1;
            /*SL:238*/v2 = v2.getReturnOp();
            /*SL:239*/if (v2 == 175) {
                final int a1 = /*EL:240*/14;
            }
            else/*SL:241*/ if (v2 == 174) {
                final int a2 = /*EL:242*/11;
            }
            else/*SL:243*/ if (v2 == 173) {
                /*SL:244*/v3 = 9;
            }
            else/*SL:245*/ if (v2 == 177) {
                /*SL:246*/v3 = 0;
            }
            else {
                /*SL:248*/v3 = 3;
            }
        }
        else {
            /*SL:251*/v2 = 176;
            /*SL:252*/v3 = 1;
        }
        /*SL:255*/if (v3 != 0) {
            /*SL:256*/v-2.addOpcode(v3);
        }
        /*SL:258*/v-2.addOpcode(v2);
    }
    
    public boolean recordLocalVariables(final CodeAttribute v-3, final int v-2) throws CompileError {
        final LocalVariableAttribute localVariableAttribute = /*EL:273*/(LocalVariableAttribute)v-3.getAttribute("LocalVariableTable");
        /*SL:276*/if (localVariableAttribute == null) {
            /*SL:277*/return false;
        }
        /*SL:280*/for (int v0 = localVariableAttribute.tableLength(), v = 0; v < v0; ++v) {
            final int a1 = /*EL:281*/localVariableAttribute.startPc(v);
            final int a2 = /*EL:282*/localVariableAttribute.codeLength(v);
            /*SL:283*/if (a1 <= v-2 && v-2 < a1 + a2) {
                /*SL:284*/this.gen.recordVariable(localVariableAttribute.descriptor(v), localVariableAttribute.variableName(v), localVariableAttribute.index(v), /*EL:285*/this.stable);
            }
        }
        /*SL:288*/return true;
    }
    
    public boolean recordParamNames(final CodeAttribute v2, final int v3) throws CompileError {
        final LocalVariableAttribute v4 = /*EL:303*/(LocalVariableAttribute)v2.getAttribute("LocalVariableTable");
        /*SL:306*/if (v4 == null) {
            /*SL:307*/return false;
        }
        /*SL:310*/for (int v5 = v4.tableLength(), a2 = 0; a2 < v5; ++a2) {
            /*SL:311*/a2 = v4.index(a2);
            /*SL:312*/if (a2 < v3) {
                /*SL:313*/this.gen.recordVariable(v4.descriptor(a2), v4.variableName(a2), a2, this.stable);
            }
        }
        /*SL:317*/return true;
    }
    
    public int recordParams(final CtClass[] a1, final boolean a2) throws CompileError {
        /*SL:334*/return this.gen.recordParams(a1, a2, "$", "$args", "$$", this.stable);
    }
    
    public int recordParams(final String a1, final CtClass[] a2, final boolean a3, final int a4, final boolean a5) throws CompileError {
        /*SL:362*/return this.gen.recordParams(a2, a5, "$", "$args", "$$", a3, a4, a1, this.stable);
    }
    
    public void setMaxLocals(final int a1) {
        /*SL:376*/this.gen.setMaxLocals(a1);
    }
    
    public int recordReturnType(final CtClass a1, final boolean a2) throws CompileError {
        /*SL:396*/this.gen.recordType(a1);
        /*SL:397*/return this.gen.recordReturnType(a1, "$r", a2 ? "$_" : null, this.stable);
    }
    
    public void recordType(final CtClass a1) {
        /*SL:408*/this.gen.recordType(a1);
    }
    
    public int recordVariable(final CtClass a1, final String a2) throws CompileError {
        /*SL:420*/return this.gen.recordVariable(a1, a2, this.stable);
    }
    
    public void recordProceed(final String a1, final String a2) throws CompileError {
        final Parser v1 = /*EL:435*/new Parser(new Lex(a1));
        final ASTree v2 = /*EL:436*/v1.parseExpression(this.stable);
        final ProceedHandler v3 = /*EL:439*/new ProceedHandler() {
            @Override
            public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
                ASTree v1 = /*EL:443*/new Member(/*EL:447*/a2);
                if (v2 != null) {
                    v1 = Expr.make(46, v2, v1);
                }
                v1 = CallExpr.makeCall(v1, a3);
                /*SL:448*/a1.compileExpr(v1);
                /*SL:449*/a1.addNullIfVoid();
            }
            
            @Override
            public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
                ASTree v1 = /*EL:455*/new Member(/*EL:459*/a2);
                if (/*EL:460*/v2 != null) {
                    v1 = Expr.make(46, v2, v1);
                }
                v1 = CallExpr.makeCall(v1, a2);
                v1.accept(a1);
                /*SL:461*/a1.addNullIfVoid();
            }
        };
        /*SL:465*/this.gen.setProceedHandler(v3, "$proceed");
    }
    
    public void recordStaticProceed(final String a1, final String a2) throws CompileError {
        final ProceedHandler v3 = /*EL:483*/new ProceedHandler() {
            @Override
            public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
                Expr v1 = /*EL:487*/Expr.make(35, new Symbol(a1), new Member(a2));
                /*SL:489*/v1 = CallExpr.makeCall(v1, a3);
                /*SL:490*/a1.compileExpr(v1);
                /*SL:491*/a1.addNullIfVoid();
            }
            
            @Override
            public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
                Expr v1 = /*EL:497*/Expr.make(35, new Symbol(a1), new Member(a2));
                /*SL:499*/v1 = CallExpr.makeCall(v1, a2);
                /*SL:500*/v1.accept(a1);
                /*SL:501*/a1.addNullIfVoid();
            }
        };
        /*SL:505*/this.gen.setProceedHandler(v3, "$proceed");
    }
    
    public void recordSpecialProceed(final String a1, final String a2, final String a3, final String a4, final int a5) throws CompileError {
        final Parser v1 = /*EL:524*/new Parser(new Lex(a1));
        final ASTree v2 = /*EL:525*/v1.parseExpression(this.stable);
        final ProceedHandler v3 = /*EL:527*/new ProceedHandler() {
            @Override
            public void doit(final JvstCodeGen a1, final Bytecode a2, final ASTList a3) throws CompileError {
                /*SL:531*/a1.compileInvokeSpecial(/*EL:532*/v2, a5, a4, a3);
            }
            
            @Override
            public void setReturnType(final JvstTypeChecker a1, final ASTList a2) throws CompileError {
                /*SL:537*/a1.compileInvokeSpecial(/*EL:538*/v2, a2, a3, a4, a2);
            }
        };
        /*SL:542*/this.gen.setProceedHandler(v3, "$proceed");
    }
    
    public void recordProceed(final ProceedHandler a1) {
        /*SL:549*/this.gen.setProceedHandler(a1, "$proceed");
    }
    
    public void compileStmnt(final String v2) throws CompileError {
        final Parser v3 = /*EL:562*/new Parser(new Lex(v2));
        final SymbolTable v4 = /*EL:563*/new SymbolTable(this.stable);
        /*SL:564*/while (v3.hasMore()) {
            final Stmnt a1 = /*EL:565*/v3.parseStatement(v4);
            /*SL:566*/if (a1 != null) {
                /*SL:567*/a1.accept(this.gen);
            }
        }
    }
    
    public void compileExpr(final String a1) throws CompileError {
        final ASTree v1 = parseExpr(/*EL:581*/a1, this.stable);
        /*SL:582*/this.compileExpr(v1);
    }
    
    public static ASTree parseExpr(final String a1, final SymbolTable a2) throws CompileError {
        final Parser v1 = /*EL:591*/new Parser(new Lex(a1));
        /*SL:592*/return v1.parseExpression(a2);
    }
    
    public void compileExpr(final ASTree a1) throws CompileError {
        /*SL:605*/if (a1 != null) {
            /*SL:606*/this.gen.compileExpr(a1);
        }
    }
    
    public static class CtFieldWithInit extends CtField
    {
        private ASTree init;
        
        CtFieldWithInit(final CtClass a1, final String a2, final CtClass a3) throws CannotCompileException {
            super(a1, a2, a3);
            this.init = null;
        }
        
        protected void setInit(final ASTree a1) {
            /*SL:121*/this.init = a1;
        }
        
        @Override
        protected ASTree getInitAST() {
            /*SL:124*/return this.init;
        }
    }
}
