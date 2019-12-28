package javassist.compiler;

import javassist.NotFoundException;
import javassist.Modifier;
import javassist.bytecode.FieldInfo;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.CondExpr;
import javassist.CtField;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.ASTList;
import javassist.ClassPool;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.Visitor;

public class TypeChecker extends Visitor implements Opcode, TokenId
{
    static final String javaLangObject = "java.lang.Object";
    static final String jvmJavaLangObject = "java/lang/Object";
    static final String jvmJavaLangString = "java/lang/String";
    static final String jvmJavaLangClass = "java/lang/Class";
    protected int exprType;
    protected int arrayDim;
    protected String className;
    protected MemberResolver resolver;
    protected CtClass thisClass;
    protected MethodInfo thisMethod;
    
    public TypeChecker(final CtClass a1, final ClassPool a2) {
        this.resolver = new MemberResolver(a2);
        this.thisClass = a1;
        this.thisMethod = null;
    }
    
    protected static String argTypesToString(final int[] a2, final int[] a3, final String[] v1) {
        final StringBuffer v2 = /*EL:56*/new StringBuffer();
        /*SL:57*/v2.append('(');
        final int v3 = /*EL:58*/a2.length;
        /*SL:59*/if (v3 > 0) {
            int a4 = /*EL:60*/0;
            while (true) {
                typeToString(/*EL:62*/v2, a2[a4], a3[a4], v1[a4]);
                /*SL:63*/if (++a4 >= v3) {
                    break;
                }
                /*SL:64*/v2.append(',');
            }
        }
        /*SL:70*/v2.append(')');
        /*SL:71*/return v2.toString();
    }
    
    protected static StringBuffer typeToString(final StringBuffer v1, final int v2, int v3, final String v4) {
        String v5 = null;
        /*SL:81*/if (v2 == 307) {
            final String a1 = /*EL:82*/MemberResolver.jvmToJavaName(v4);
        }
        else/*SL:83*/ if (v2 == 412) {
            final String a2 = /*EL:84*/"Object";
        }
        else {
            try {
                final String a3 = /*EL:87*/MemberResolver.getTypeName(v2);
            }
            catch (CompileError a4) {
                /*SL:90*/v5 = "?";
            }
        }
        /*SL:93*/v1.append(v5);
        /*SL:94*/while (v3-- > 0) {
            /*SL:95*/v1.append("[]");
        }
        /*SL:97*/return v1;
    }
    
    public void setThisMethod(final MethodInfo a1) {
        /*SL:104*/this.thisMethod = a1;
    }
    
    protected static void fatal() throws CompileError {
        /*SL:108*/throw new CompileError("fatal");
    }
    
    protected String getThisName() {
        /*SL:115*/return MemberResolver.javaToJvmName(this.thisClass.getName());
    }
    
    protected String getSuperName() throws CompileError {
        /*SL:122*/return MemberResolver.javaToJvmName(/*EL:123*/MemberResolver.getSuperclass(this.thisClass).getName());
    }
    
    protected String resolveClassName(final ASTList a1) throws CompileError {
        /*SL:132*/return this.resolver.resolveClassName(a1);
    }
    
    protected String resolveClassName(final String a1) throws CompileError {
        /*SL:139*/return this.resolver.resolveJvmClassName(a1);
    }
    
    @Override
    public void atNewExpr(final NewExpr v-1) throws CompileError {
        /*SL:143*/if (v-1.isArray()) {
            /*SL:144*/this.atNewArrayExpr(v-1);
        }
        else {
            final CtClass a1 = /*EL:146*/this.resolver.lookupClassByName(v-1.getClassName());
            final String v1 = /*EL:147*/a1.getName();
            final ASTList v2 = /*EL:148*/v-1.getArguments();
            /*SL:149*/this.atMethodCallCore(a1, "<init>", v2);
            /*SL:150*/this.exprType = 307;
            /*SL:151*/this.arrayDim = 0;
            /*SL:152*/this.className = MemberResolver.javaToJvmName(v1);
        }
    }
    
    public void atNewArrayExpr(final NewExpr v2) throws CompileError {
        final int v3 = /*EL:157*/v2.getArrayType();
        final ASTList v4 = /*EL:158*/v2.getArraySize();
        final ASTList v5 = /*EL:159*/v2.getClassName();
        final ASTree v6 = /*EL:160*/v2.getInitializer();
        /*SL:161*/if (v6 != null) {
            /*SL:162*/v6.accept(this);
        }
        /*SL:164*/if (v4.length() > 1) {
            /*SL:165*/this.atMultiNewArray(v3, v5, v4);
        }
        else {
            final ASTree a1 = /*EL:167*/v4.head();
            /*SL:168*/if (a1 != null) {
                /*SL:169*/a1.accept(this);
            }
            /*SL:171*/this.exprType = v3;
            /*SL:172*/this.arrayDim = 1;
            /*SL:173*/if (v3 == 307) {
                /*SL:174*/this.className = this.resolveClassName(v5);
            }
            else {
                /*SL:176*/this.className = null;
            }
        }
    }
    
    @Override
    public void atArrayInit(final ArrayInit v2) throws CompileError {
        ASTList v3 = /*EL:181*/v2;
        /*SL:182*/while (v3 != null) {
            final ASTree a1 = /*EL:183*/v3.head();
            /*SL:184*/v3 = v3.tail();
            /*SL:185*/if (a1 != null) {
                /*SL:186*/a1.accept(this);
            }
        }
    }
    
    protected void atMultiNewArray(final int a3, final ASTList v1, ASTList v2) throws CompileError {
        final int v3 = /*EL:194*/v2.length();
        int v4 = /*EL:195*/0;
        while (v2 != null) {
            final ASTree a4 = /*EL:196*/v2.head();
            /*SL:197*/if (a4 == null) {
                /*SL:198*/break;
            }
            /*SL:200*/++v4;
            /*SL:201*/a4.accept(this);
            v2 = v2.tail();
        }
        /*SL:204*/this.exprType = a3;
        /*SL:205*/this.arrayDim = v3;
        /*SL:206*/if (a3 == 307) {
            /*SL:207*/this.className = this.resolveClassName(v1);
        }
        else {
            /*SL:209*/this.className = null;
        }
    }
    
    @Override
    public void atAssignExpr(final AssignExpr v2) throws CompileError {
        final int v3 = /*EL:214*/v2.getOperator();
        final ASTree v4 = /*EL:215*/v2.oprand1();
        final ASTree v5 = /*EL:216*/v2.oprand2();
        /*SL:217*/if (v4 instanceof Variable) {
            /*SL:218*/this.atVariableAssign(v2, v3, (Variable)v4, ((Variable)v4).getDeclarator(), /*EL:219*/v5);
        }
        else {
            /*SL:222*/if (v4 instanceof Expr) {
                final Expr a1 = /*EL:223*/(Expr)v4;
                /*SL:224*/if (a1.getOperator() == 65) {
                    /*SL:225*/this.atArrayAssign(v2, v3, (Expr)v4, v5);
                    /*SL:226*/return;
                }
            }
            /*SL:230*/this.atFieldAssign(v2, v3, v4, v5);
        }
    }
    
    private void atVariableAssign(final Expr a1, final int a2, final Variable a3, final Declarator a4, final ASTree a5) throws CompileError {
        final int v1 = /*EL:242*/a4.getType();
        final int v2 = /*EL:243*/a4.getArrayDim();
        final String v3 = /*EL:244*/a4.getClassName();
        /*SL:246*/if (a2 != 61) {
            /*SL:247*/this.atVariable(a3);
        }
        /*SL:249*/a5.accept(this);
        /*SL:250*/this.exprType = v1;
        /*SL:251*/this.arrayDim = v2;
        /*SL:252*/this.className = v3;
    }
    
    private void atArrayAssign(final Expr a1, final int a2, final Expr a3, final ASTree a4) throws CompileError {
        /*SL:258*/this.atArrayRead(a3.oprand1(), a3.oprand2());
        final int v1 = /*EL:259*/this.exprType;
        final int v2 = /*EL:260*/this.arrayDim;
        final String v3 = /*EL:261*/this.className;
        /*SL:262*/a4.accept(this);
        /*SL:263*/this.exprType = v1;
        /*SL:264*/this.arrayDim = v2;
        /*SL:265*/this.className = v3;
    }
    
    protected void atFieldAssign(final Expr a1, final int a2, final ASTree a3, final ASTree a4) throws CompileError {
        final CtField v1 = /*EL:271*/this.fieldAccess(a3);
        /*SL:272*/this.atFieldRead(v1);
        final int v2 = /*EL:273*/this.exprType;
        final int v3 = /*EL:274*/this.arrayDim;
        final String v4 = /*EL:275*/this.className;
        /*SL:276*/a4.accept(this);
        /*SL:277*/this.exprType = v2;
        /*SL:278*/this.arrayDim = v3;
        /*SL:279*/this.className = v4;
    }
    
    @Override
    public void atCondExpr(final CondExpr a1) throws CompileError {
        /*SL:283*/this.booleanExpr(a1.condExpr());
        /*SL:284*/a1.thenExpr().accept(this);
        final int v1 = /*EL:285*/this.exprType;
        final int v2 = /*EL:286*/this.arrayDim;
        final String v3 = /*EL:287*/this.className;
        /*SL:288*/a1.elseExpr().accept(this);
        /*SL:290*/if (v2 == 0 && v2 == this.arrayDim) {
            /*SL:291*/if (CodeGen.rightIsStrong(v1, this.exprType)) {
                /*SL:292*/a1.setThen(new CastExpr(this.exprType, 0, a1.thenExpr()));
            }
            else/*SL:293*/ if (CodeGen.rightIsStrong(this.exprType, v1)) {
                /*SL:294*/a1.setElse(new CastExpr(v1, 0, a1.elseExpr()));
                /*SL:295*/this.exprType = v1;
            }
        }
    }
    
    @Override
    public void atBinExpr(final BinExpr v-2) throws CompileError {
        final int operator = /*EL:306*/v-2.getOperator();
        final int v0 = /*EL:307*/CodeGen.lookupBinOp(operator);
        /*SL:308*/if (v0 >= 0) {
            /*SL:311*/if (operator == 43) {
                Expr a1 = /*EL:312*/this.atPlusExpr(v-2);
                /*SL:313*/if (a1 != null) {
                    /*SL:317*/a1 = CallExpr.makeCall(Expr.make(46, a1, new Member("toString")), null);
                    /*SL:319*/v-2.setOprand1(a1);
                    /*SL:320*/v-2.setOprand2(null);
                    /*SL:321*/this.className = "java/lang/String";
                }
            }
            else {
                final ASTree v = /*EL:325*/v-2.oprand1();
                final ASTree v2 = /*EL:326*/v-2.oprand2();
                /*SL:327*/v.accept(this);
                final int v3 = /*EL:328*/this.exprType;
                /*SL:329*/v2.accept(this);
                /*SL:330*/if (!this.isConstant(v-2, operator, v, v2)) {
                    /*SL:331*/this.computeBinExprType(v-2, operator, v3);
                }
            }
        }
        else {
            /*SL:337*/this.booleanExpr(v-2);
        }
    }
    
    private Expr atPlusExpr(final BinExpr v-5) throws CompileError {
        final ASTree oprand1 = /*EL:346*/v-5.oprand1();
        final ASTree oprand2 = /*EL:347*/v-5.oprand2();
        /*SL:348*/if (oprand2 == null) {
            /*SL:351*/oprand1.accept(this);
            /*SL:352*/return null;
        }
        /*SL:355*/if (isPlusExpr(oprand1)) {
            final Expr a1 = /*EL:356*/this.atPlusExpr((BinExpr)oprand1);
            /*SL:357*/if (a1 != null) {
                /*SL:358*/oprand2.accept(this);
                /*SL:359*/this.exprType = 307;
                /*SL:360*/this.arrayDim = 0;
                /*SL:361*/this.className = "java/lang/StringBuffer";
                /*SL:362*/return makeAppendCall(a1, oprand2);
            }
        }
        else {
            /*SL:366*/oprand1.accept(this);
        }
        final int exprType = /*EL:368*/this.exprType;
        final int arrayDim = /*EL:369*/this.arrayDim;
        final String v0 = /*EL:370*/this.className;
        /*SL:371*/oprand2.accept(this);
        /*SL:373*/if (this.isConstant(v-5, 43, oprand1, oprand2)) {
            /*SL:374*/return null;
        }
        /*SL:376*/if ((exprType == 307 && arrayDim == 0 && "java/lang/String".equals(v0)) || (this.exprType == 307 && this.arrayDim == 0 && "java/lang/String".equals(this.className))) {
            final ASTList v = /*EL:379*/ASTList.make(new Symbol("java"), new Symbol("lang"), new Symbol("StringBuffer"));
            final ASTree v2 = /*EL:381*/new NewExpr(v, null);
            /*SL:382*/this.exprType = 307;
            /*SL:383*/this.arrayDim = 0;
            /*SL:384*/this.className = "java/lang/StringBuffer";
            /*SL:385*/return makeAppendCall(makeAppendCall(v2, oprand1), oprand2);
        }
        /*SL:388*/this.computeBinExprType(v-5, 43, exprType);
        /*SL:389*/return null;
    }
    
    private boolean isConstant(final BinExpr a1, final int a2, ASTree a3, ASTree a4) throws CompileError {
        /*SL:396*/a3 = stripPlusExpr(a3);
        /*SL:397*/a4 = stripPlusExpr(a4);
        ASTree v1 = /*EL:398*/null;
        /*SL:399*/if (a3 instanceof StringL && a4 instanceof StringL && a2 == 43) {
            /*SL:401*/v1 = new StringL(((StringL)a3).get() + ((StringL)a4).get());
        }
        else/*SL:402*/ if (a3 instanceof IntConst) {
            /*SL:403*/v1 = ((IntConst)a3).compute(a2, a4);
        }
        else/*SL:404*/ if (a3 instanceof DoubleConst) {
            /*SL:405*/v1 = ((DoubleConst)a3).compute(a2, a4);
        }
        /*SL:407*/if (v1 == null) {
            /*SL:408*/return false;
        }
        /*SL:410*/a1.setOperator(43);
        /*SL:411*/a1.setOprand1(v1);
        /*SL:412*/a1.setOprand2(null);
        /*SL:413*/v1.accept(this);
        /*SL:414*/return true;
    }
    
    static ASTree stripPlusExpr(final ASTree v-2) {
        /*SL:421*/if (v-2 instanceof BinExpr) {
            final BinExpr a1 = /*EL:422*/(BinExpr)v-2;
            /*SL:423*/if (a1.getOperator() == 43 && a1.oprand2() == null) {
                /*SL:424*/return a1.getLeft();
            }
        }
        else/*SL:426*/ if (v-2 instanceof Expr) {
            final Expr expr = /*EL:427*/(Expr)v-2;
            final int v0 = /*EL:428*/expr.getOperator();
            /*SL:429*/if (v0 == 35) {
                final ASTree v = getConstantFieldValue(/*EL:430*/(Member)expr.oprand2());
                /*SL:431*/if (v != null) {
                    /*SL:432*/return v;
                }
            }
            else/*SL:434*/ if (v0 == 43 && expr.getRight() == null) {
                /*SL:435*/return expr.getLeft();
            }
        }
        else/*SL:437*/ if (v-2 instanceof Member) {
            final ASTree constantFieldValue = getConstantFieldValue(/*EL:438*/(Member)v-2);
            /*SL:439*/if (constantFieldValue != null) {
                /*SL:440*/return constantFieldValue;
            }
        }
        /*SL:443*/return v-2;
    }
    
    private static ASTree getConstantFieldValue(final Member a1) {
        /*SL:451*/return getConstantFieldValue(a1.getField());
    }
    
    public static ASTree getConstantFieldValue(final CtField v-1) {
        /*SL:455*/if (v-1 == null) {
            /*SL:456*/return null;
        }
        final Object v0 = /*EL:458*/v-1.getConstantValue();
        /*SL:459*/if (v0 == null) {
            /*SL:460*/return null;
        }
        /*SL:462*/if (v0 instanceof String) {
            /*SL:463*/return new StringL((String)v0);
        }
        /*SL:464*/if (v0 instanceof Double || v0 instanceof Float) {
            final int a1 = /*EL:465*/(v0 instanceof Double) ? 405 : 404;
            /*SL:467*/return new DoubleConst(((Number)v0).doubleValue(), a1);
        }
        /*SL:469*/if (v0 instanceof Number) {
            final int v = /*EL:470*/(v0 instanceof Long) ? 403 : 402;
            /*SL:471*/return new IntConst(((Number)v0).longValue(), v);
        }
        /*SL:473*/if (v0 instanceof Boolean) {
            /*SL:474*/return new Keyword(v0 ? 410 : 411);
        }
        /*SL:477*/return null;
    }
    
    private static boolean isPlusExpr(final ASTree v-1) {
        /*SL:481*/if (v-1 instanceof BinExpr) {
            final BinExpr a1 = /*EL:482*/(BinExpr)v-1;
            final int v1 = /*EL:483*/a1.getOperator();
            /*SL:484*/return v1 == 43;
        }
        /*SL:487*/return false;
    }
    
    private static Expr makeAppendCall(final ASTree a1, final ASTree a2) {
        /*SL:491*/return CallExpr.makeCall(Expr.make(46, a1, new Member("append")), new ASTList(a2));
    }
    
    private void computeBinExprType(final BinExpr a1, final int a2, final int a3) throws CompileError {
        final int v1 = /*EL:499*/this.exprType;
        /*SL:500*/if (a2 == 364 || a2 == 366 || a2 == 370) {
            /*SL:501*/this.exprType = a3;
        }
        else {
            /*SL:503*/this.insertCast(a1, a3, v1);
        }
        /*SL:505*/if (CodeGen.isP_INT(this.exprType) && this.exprType != 301) {
            /*SL:506*/this.exprType = 324;
        }
    }
    
    private void booleanExpr(final ASTree v-2) throws CompileError {
        final int compOperator = /*EL:512*/CodeGen.getCompOperator(v-2);
        /*SL:513*/if (compOperator == 358) {
            final BinExpr a1 = /*EL:514*/(BinExpr)v-2;
            /*SL:515*/a1.oprand1().accept(this);
            final int v1 = /*EL:516*/this.exprType;
            final int v2 = /*EL:517*/this.arrayDim;
            /*SL:518*/a1.oprand2().accept(this);
            /*SL:519*/if (v2 == 0 && this.arrayDim == 0) {
                /*SL:520*/this.insertCast(a1, v1, this.exprType);
            }
        }
        else/*SL:522*/ if (compOperator == 33) {
            /*SL:523*/((Expr)v-2).oprand1().accept(this);
        }
        else/*SL:524*/ if (compOperator == 369 || compOperator == 368) {
            final BinExpr v3 = /*EL:525*/(BinExpr)v-2;
            /*SL:526*/v3.oprand1().accept(this);
            /*SL:527*/v3.oprand2().accept(this);
        }
        else {
            /*SL:530*/v-2.accept(this);
        }
        /*SL:532*/this.exprType = 301;
        /*SL:533*/this.arrayDim = 0;
    }
    
    private void insertCast(final BinExpr a1, final int a2, final int a3) throws CompileError {
        /*SL:539*/if (CodeGen.rightIsStrong(a2, a3)) {
            /*SL:540*/a1.setLeft(new CastExpr(a3, 0, a1.oprand1()));
        }
        else {
            /*SL:542*/this.exprType = a2;
        }
    }
    
    @Override
    public void atCastExpr(final CastExpr a1) throws CompileError {
        final String v1 = /*EL:546*/this.resolveClassName(a1.getClassName());
        /*SL:547*/a1.getOprand().accept(this);
        /*SL:548*/this.exprType = a1.getType();
        /*SL:549*/this.arrayDim = a1.getArrayDim();
        /*SL:550*/this.className = v1;
    }
    
    @Override
    public void atInstanceOfExpr(final InstanceOfExpr a1) throws CompileError {
        /*SL:554*/a1.getOprand().accept(this);
        /*SL:555*/this.exprType = 301;
        /*SL:556*/this.arrayDim = 0;
    }
    
    @Override
    public void atExpr(final Expr v-2) throws CompileError {
        final int operator = /*EL:563*/v-2.getOperator();
        final ASTree v0 = /*EL:564*/v-2.oprand1();
        /*SL:565*/if (operator == 46) {
            final String v = /*EL:566*/((Symbol)v-2.oprand2()).get();
            /*SL:567*/if (v.equals("length")) {
                try {
                    /*SL:569*/this.atArrayLength(v-2);
                }
                catch (NoFieldException a1) {
                    /*SL:573*/this.atFieldRead(v-2);
                }
            }
            else/*SL:575*/ if (v.equals("class")) {
                /*SL:576*/this.atClassObject(v-2);
            }
            else {
                /*SL:578*/this.atFieldRead(v-2);
            }
        }
        else/*SL:580*/ if (operator == 35) {
            final String v = /*EL:581*/((Symbol)v-2.oprand2()).get();
            /*SL:582*/if (v.equals("class")) {
                /*SL:583*/this.atClassObject(v-2);
            }
            else {
                /*SL:585*/this.atFieldRead(v-2);
            }
        }
        else/*SL:587*/ if (operator == 65) {
            /*SL:588*/this.atArrayRead(v0, v-2.oprand2());
        }
        else/*SL:589*/ if (operator == 362 || operator == 363) {
            /*SL:590*/this.atPlusPlus(operator, v0, v-2);
        }
        else/*SL:591*/ if (operator == 33) {
            /*SL:592*/this.booleanExpr(v-2);
        }
        else/*SL:593*/ if (operator == 67) {
            fatal();
        }
        else {
            /*SL:596*/v0.accept(this);
            /*SL:598*/if (!this.isConstant(v-2, operator, v0) && (operator == 45 || operator == 126) && /*EL:599*/CodeGen.isP_INT(this.exprType)) {
                /*SL:600*/this.exprType = 324;
            }
        }
    }
    
    private boolean isConstant(final Expr v2, final int v3, ASTree v4) {
        /*SL:605*/v4 = stripPlusExpr(v4);
        /*SL:606*/if (v4 instanceof IntConst) {
            final IntConst a1 = /*EL:607*/(IntConst)v4;
            long a2 = /*EL:608*/a1.get();
            /*SL:609*/if (v3 == 45) {
                /*SL:610*/a2 = -a2;
            }
            else {
                /*SL:611*/if (v3 != 126) {
                    /*SL:614*/return false;
                }
                a2 ^= -1L;
            }
            /*SL:616*/a1.set(a2);
        }
        else {
            /*SL:618*/if (!(v4 instanceof DoubleConst)) {
                /*SL:626*/return false;
            }
            final DoubleConst a3 = (DoubleConst)v4;
            if (v3 != 45) {
                return false;
            }
            a3.set(-a3.get());
        }
        /*SL:628*/v2.setOperator(43);
        /*SL:629*/return true;
    }
    
    @Override
    public void atCallExpr(final CallExpr v-6) throws CompileError {
        String v-7 = /*EL:633*/null;
        CtClass v-8 = /*EL:634*/null;
        final ASTree oprand1 = /*EL:635*/v-6.oprand1();
        final ASTList v-9 = /*EL:636*/(ASTList)v-6.oprand2();
        /*SL:638*/if (oprand1 instanceof Member) {
            /*SL:639*/v-7 = ((Member)oprand1).get();
            /*SL:640*/v-8 = this.thisClass;
        }
        else/*SL:642*/ if (oprand1 instanceof Keyword) {
            /*SL:643*/v-7 = "<init>";
            /*SL:644*/if (((Keyword)oprand1).get() == 336) {
                /*SL:645*/v-8 = MemberResolver.getSuperclass(this.thisClass);
            }
            else {
                /*SL:647*/v-8 = this.thisClass;
            }
        }
        else/*SL:649*/ if (oprand1 instanceof Expr) {
            final Expr expr = /*EL:650*/(Expr)oprand1;
            /*SL:651*/v-7 = ((Symbol)expr.oprand2()).get();
            final int v0 = /*EL:652*/expr.getOperator();
            /*SL:653*/if (v0 == 35) {
                /*SL:655*/v-8 = this.resolver.lookupClass(((Symbol)expr.oprand1()).get(), false);
            }
            else/*SL:657*/ if (v0 == 46) {
                final ASTree v = /*EL:658*/expr.oprand1();
                final String v2 = isDotSuper(/*EL:659*/v);
                /*SL:660*/if (v2 != null) {
                    /*SL:661*/v-8 = MemberResolver.getSuperInterface(this.thisClass, v2);
                }
                else {
                    try {
                        /*SL:665*/v.accept(this);
                    }
                    catch (NoFieldException a1) {
                        /*SL:668*/if (a1.getExpr() != v) {
                            /*SL:669*/throw a1;
                        }
                        /*SL:672*/this.exprType = 307;
                        /*SL:673*/this.arrayDim = 0;
                        /*SL:674*/this.className = a1.getField();
                        /*SL:675*/expr.setOperator(35);
                        /*SL:676*/expr.setOprand1(new Symbol(MemberResolver.jvmToJavaName(this.className)));
                    }
                    /*SL:680*/if (this.arrayDim > 0) {
                        /*SL:681*/v-8 = this.resolver.lookupClass("java.lang.Object", true);
                    }
                    else/*SL:682*/ if (this.exprType == 307) {
                        /*SL:683*/v-8 = this.resolver.lookupClassByJvmName(this.className);
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
        final MemberResolver.Method atMethodCallCore = /*EL:694*/this.atMethodCallCore(v-8, v-7, v-9);
        /*SL:696*/v-6.setMethod(atMethodCallCore);
    }
    
    private static void badMethod() throws CompileError {
        /*SL:700*/throw new CompileError("bad method");
    }
    
    static String isDotSuper(final ASTree v0) {
        /*SL:711*/if (v0 instanceof Expr) {
            final Expr v = /*EL:712*/(Expr)v0;
            /*SL:713*/if (v.getOperator() == 46) {
                final ASTree a1 = /*EL:714*/v.oprand2();
                /*SL:715*/if (a1 instanceof Keyword && ((Keyword)a1).get() == 336) {
                    /*SL:716*/return ((Symbol)v.oprand1()).get();
                }
            }
        }
        /*SL:720*/return null;
    }
    
    public MemberResolver.Method atMethodCallCore(final CtClass v-9, final String v-8, final ASTList v-7) throws CompileError {
        final int methodArgsLength = /*EL:731*/this.getMethodArgsLength(v-7);
        final int[] a4 = /*EL:732*/new int[methodArgsLength];
        final int[] a5 = /*EL:733*/new int[methodArgsLength];
        final String[] v2 = /*EL:734*/new String[methodArgsLength];
        /*SL:735*/this.atMethodArgs(v-7, a4, a5, v2);
        final MemberResolver.Method lookupMethod = /*EL:737*/this.resolver.lookupMethod(v-9, this.thisClass, this.thisMethod, v-8, a4, a5, v2);
        /*SL:740*/if (lookupMethod == null) {
            String a2 = /*EL:741*/v-9.getName();
            /*SL:742*/a2 = argTypesToString(a4, a5, v2);
            final String v1;
            /*SL:744*/if (v-8.equals("<init>")) {
                final String a3 = /*EL:745*/"cannot find constructor " + a2 + a2;
            }
            else {
                /*SL:747*/v1 = v-8 + a2 + " not found in " + a2;
            }
            /*SL:749*/throw new CompileError(v1);
        }
        final String descriptor = /*EL:752*/lookupMethod.info.getDescriptor();
        /*SL:753*/this.setReturnType(descriptor);
        /*SL:754*/return lookupMethod;
    }
    
    public int getMethodArgsLength(final ASTList a1) {
        /*SL:758*/return ASTList.length(a1);
    }
    
    public void atMethodArgs(ASTList a3, final int[] a4, final int[] v1, final String[] v2) throws CompileError {
        int v3 = /*EL:763*/0;
        /*SL:764*/while (a3 != null) {
            final ASTree a5 = /*EL:765*/a3.head();
            /*SL:766*/a5.accept(this);
            /*SL:767*/a4[v3] = this.exprType;
            /*SL:768*/v1[v3] = this.arrayDim;
            /*SL:769*/v2[v3] = this.className;
            /*SL:770*/++v3;
            /*SL:771*/a3 = a3.tail();
        }
    }
    
    void setReturnType(final String v2) throws CompileError {
        int v3 = /*EL:776*/v2.indexOf(41);
        /*SL:777*/if (v3 < 0) {
            badMethod();
        }
        char v4 = /*EL:780*/v2.charAt(++v3);
        int v5 = /*EL:781*/0;
        /*SL:782*/while (v4 == '[') {
            /*SL:783*/++v5;
            /*SL:784*/v4 = v2.charAt(++v3);
        }
        /*SL:787*/this.arrayDim = v5;
        /*SL:788*/if (v4 == 'L') {
            final int a1 = /*EL:789*/v2.indexOf(59, v3 + 1);
            /*SL:790*/if (a1 < 0) {
                badMethod();
            }
            /*SL:793*/this.exprType = 307;
            /*SL:794*/this.className = v2.substring(v3 + 1, a1);
        }
        else {
            /*SL:797*/this.exprType = MemberResolver.descToType(v4);
            /*SL:798*/this.className = null;
        }
    }
    
    private void atFieldRead(final ASTree a1) throws CompileError {
        /*SL:803*/this.atFieldRead(this.fieldAccess(a1));
    }
    
    private void atFieldRead(final CtField a1) throws CompileError {
        final FieldInfo v1 = /*EL:807*/a1.getFieldInfo2();
        final String v2 = /*EL:808*/v1.getDescriptor();
        int v3 = /*EL:810*/0;
        int v4 = /*EL:811*/0;
        char v5;
        /*SL:813*/for (v5 = v2.charAt(v3); v5 == '['; /*SL:815*/v5 = v2.charAt(++v3)) {
            ++v4;
        }
        /*SL:818*/this.arrayDim = v4;
        /*SL:819*/this.exprType = MemberResolver.descToType(v5);
        /*SL:821*/if (v5 == 'L') {
            /*SL:822*/this.className = v2.substring(v3 + 1, v2.indexOf(59, v3 + 1));
        }
        else {
            /*SL:824*/this.className = null;
        }
    }
    
    protected CtField fieldAccess(final ASTree v-2) throws CompileError {
        /*SL:833*/if (v-2 instanceof Member) {
            final Member member = /*EL:834*/(Member)v-2;
            final String v0 = /*EL:835*/member.get();
            try {
                final CtField a1 = /*EL:837*/this.thisClass.getField(v0);
                /*SL:838*/if (Modifier.isStatic(a1.getModifiers())) {
                    /*SL:839*/member.setField(a1);
                }
                /*SL:841*/return a1;
            }
            catch (NotFoundException v8) {
                /*SL:845*/throw new NoFieldException(v0, v-2);
            }
        }
        /*SL:848*/if (v-2 instanceof Expr) {
            final Expr expr = /*EL:849*/(Expr)v-2;
            final int v = /*EL:850*/expr.getOperator();
            /*SL:851*/if (v == 35) {
                final Member v2 = /*EL:852*/(Member)expr.oprand2();
                final CtField v3 = /*EL:853*/this.resolver.lookupField(((Symbol)expr.oprand1()).get(), /*EL:854*/v2);
                /*SL:855*/v2.setField(v3);
                /*SL:856*/return v3;
            }
            /*SL:858*/if (v == 46) {
                try {
                    /*SL:860*/expr.oprand1().accept(this);
                }
                catch (NoFieldException v4) {
                    /*SL:863*/if (v4.getExpr() != expr.oprand1()) {
                        /*SL:864*/throw v4;
                    }
                    /*SL:870*/return this.fieldAccess2(expr, v4.getField());
                }
                CompileError v5 = /*EL:873*/null;
                try {
                    /*SL:875*/if (this.exprType == 307 && this.arrayDim == 0) {
                        /*SL:876*/return this.resolver.lookupFieldByJvmName(this.className, (Symbol)expr.oprand2());
                    }
                }
                catch (CompileError v6) {
                    /*SL:880*/v5 = v6;
                }
                final ASTree v7 = /*EL:899*/expr.oprand1();
                /*SL:900*/if (v7 instanceof Symbol) {
                    /*SL:901*/return this.fieldAccess2(expr, ((Symbol)v7).get());
                }
                /*SL:903*/if (v5 != null) {
                    /*SL:904*/throw v5;
                }
            }
        }
        /*SL:908*/throw new CompileError("bad filed access");
    }
    
    private CtField fieldAccess2(final Expr a1, final String a2) throws CompileError {
        final Member v1 = /*EL:912*/(Member)a1.oprand2();
        final CtField v2 = /*EL:913*/this.resolver.lookupFieldByJvmName2(a2, v1, a1);
        /*SL:914*/a1.setOperator(35);
        /*SL:915*/a1.setOprand1(new Symbol(MemberResolver.jvmToJavaName(a2)));
        /*SL:916*/v1.setField(v2);
        /*SL:917*/return v2;
    }
    
    public void atClassObject(final Expr a1) throws CompileError {
        /*SL:921*/this.exprType = 307;
        /*SL:922*/this.arrayDim = 0;
        /*SL:923*/this.className = "java/lang/Class";
    }
    
    public void atArrayLength(final Expr a1) throws CompileError {
        /*SL:927*/a1.oprand1().accept(this);
        /*SL:928*/if (this.arrayDim == 0) {
            /*SL:929*/throw new NoFieldException("length", a1);
        }
        /*SL:931*/this.exprType = 324;
        /*SL:932*/this.arrayDim = 0;
    }
    
    public void atArrayRead(final ASTree a1, final ASTree a2) throws CompileError {
        /*SL:938*/a1.accept(this);
        final int v1 = /*EL:939*/this.exprType;
        final int v2 = /*EL:940*/this.arrayDim;
        final String v3 = /*EL:941*/this.className;
        /*SL:942*/a2.accept(this);
        /*SL:943*/this.exprType = v1;
        /*SL:944*/this.arrayDim = v2 - 1;
        /*SL:945*/this.className = v3;
    }
    
    private void atPlusPlus(final int v2, ASTree v3, final Expr v4) throws CompileError {
        final boolean v5 = /*EL:951*/v3 == null;
        /*SL:952*/if (v5) {
            /*SL:953*/v3 = v4.oprand2();
        }
        /*SL:955*/if (v3 instanceof Variable) {
            final Declarator a1 = /*EL:956*/((Variable)v3).getDeclarator();
            /*SL:957*/this.exprType = a1.getType();
            /*SL:958*/this.arrayDim = a1.getArrayDim();
        }
        else {
            /*SL:961*/if (v3 instanceof Expr) {
                final Expr a2 = /*EL:962*/(Expr)v3;
                /*SL:963*/if (a2.getOperator() == 65) {
                    /*SL:964*/this.atArrayRead(a2.oprand1(), a2.oprand2());
                    final int a3 = /*EL:966*/this.exprType;
                    /*SL:967*/if (a3 == 324 || a3 == 303 || a3 == 306 || a3 == 334) {
                        /*SL:968*/this.exprType = 324;
                    }
                    /*SL:970*/return;
                }
            }
            /*SL:974*/this.atFieldPlusPlus(v3);
        }
    }
    
    protected void atFieldPlusPlus(final ASTree a1) throws CompileError {
        final CtField v1 = /*EL:980*/this.fieldAccess(a1);
        /*SL:981*/this.atFieldRead(v1);
        final int v2 = /*EL:982*/this.exprType;
        /*SL:983*/if (v2 == 324 || v2 == 303 || v2 == 306 || v2 == 334) {
            /*SL:984*/this.exprType = 324;
        }
    }
    
    @Override
    public void atMember(final Member a1) throws CompileError {
        /*SL:988*/this.atFieldRead(a1);
    }
    
    @Override
    public void atVariable(final Variable a1) throws CompileError {
        final Declarator v1 = /*EL:992*/a1.getDeclarator();
        /*SL:993*/this.exprType = v1.getType();
        /*SL:994*/this.arrayDim = v1.getArrayDim();
        /*SL:995*/this.className = v1.getClassName();
    }
    
    @Override
    public void atKeyword(final Keyword a1) throws CompileError {
        /*SL:999*/this.arrayDim = 0;
        final int v1 = /*EL:1000*/a1.get();
        /*SL:1001*/switch (v1) {
            case 410:
            case 411: {
                /*SL:1004*/this.exprType = 301;
                /*SL:1005*/break;
            }
            case 412: {
                /*SL:1007*/this.exprType = 412;
                /*SL:1008*/break;
            }
            case 336:
            case 339: {
                /*SL:1011*/this.exprType = 307;
                /*SL:1012*/if (v1 == 339) {
                    /*SL:1013*/this.className = this.getThisName();
                    break;
                }
                /*SL:1015*/this.className = this.getSuperName();
                /*SL:1016*/break;
            }
            default: {
                fatal();
                break;
            }
        }
    }
    
    @Override
    public void atStringL(final StringL a1) throws CompileError {
        /*SL:1023*/this.exprType = 307;
        /*SL:1024*/this.arrayDim = 0;
        /*SL:1025*/this.className = "java/lang/String";
    }
    
    @Override
    public void atIntConst(final IntConst a1) throws CompileError {
        /*SL:1029*/this.arrayDim = 0;
        final int v1 = /*EL:1030*/a1.getType();
        /*SL:1031*/if (v1 == 402 || v1 == 401) {
            /*SL:1032*/this.exprType = ((v1 == 402) ? 324 : 306);
        }
        else {
            /*SL:1034*/this.exprType = 326;
        }
    }
    
    @Override
    public void atDoubleConst(final DoubleConst a1) throws CompileError {
        /*SL:1038*/this.arrayDim = 0;
        /*SL:1039*/if (a1.getType() == 405) {
            /*SL:1040*/this.exprType = 312;
        }
        else {
            /*SL:1042*/this.exprType = 317;
        }
    }
}
