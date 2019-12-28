package javassist;

import javassist.compiler.SymbolTable;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.bytecode.Bytecode;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AccessFlag;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.compiler.ast.ASTree;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import java.util.ListIterator;
import java.util.Map;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;

public class CtField extends CtMember
{
    static final String javaLangString = "java.lang.String";
    protected FieldInfo fieldInfo;
    
    public CtField(final CtClass a1, final String a2, final CtClass a3) throws CannotCompileException {
        this(Descriptor.of(a1), a2, a3);
    }
    
    public CtField(final CtField v1, final CtClass v2) throws CannotCompileException {
        this(v1.fieldInfo.getDescriptor(), v1.fieldInfo.getName(), v2);
        final ListIterator v3 = v1.fieldInfo.getAttributes().listIterator();
        final FieldInfo v4 = this.fieldInfo;
        v4.setAccessFlags(v1.fieldInfo.getAccessFlags());
        final ConstPool v5 = v4.getConstPool();
        while (v3.hasNext()) {
            final AttributeInfo a1 = v3.next();
            v4.addAttribute(a1.copy(v5, null));
        }
    }
    
    private CtField(final String a1, final String a2, final CtClass a3) throws CannotCompileException {
        super(a3);
        final ClassFile v1 = a3.getClassFile2();
        if (v1 == null) {
            throw new CannotCompileException("bad declaring class: " + a3.getName());
        }
        this.fieldInfo = new FieldInfo(v1.getConstPool(), a2, a1);
    }
    
    CtField(final FieldInfo a1, final CtClass a2) {
        super(a2);
        this.fieldInfo = a1;
    }
    
    @Override
    public String toString() {
        /*SL:119*/return this.getDeclaringClass().getName() + "." + this.getName() + ":" + this.fieldInfo.getDescriptor();
    }
    
    @Override
    protected void extendToString(final StringBuffer a1) {
        /*SL:123*/a1.append(' ');
        /*SL:124*/a1.append(this.getName());
        /*SL:125*/a1.append(' ');
        /*SL:126*/a1.append(this.fieldInfo.getDescriptor());
    }
    
    protected ASTree getInitAST() {
        /*SL:131*/return null;
    }
    
    Initializer getInit() {
        final ASTree v1 = /*EL:136*/this.getInitAST();
        /*SL:137*/if (v1 == null) {
            /*SL:138*/return null;
        }
        /*SL:140*/return Initializer.byExpr(v1);
    }
    
    public static CtField make(final String v1, final CtClass v2) throws CannotCompileException {
        final Javac v3 = /*EL:160*/new Javac(v2);
        try {
            final CtMember a1 = /*EL:162*/v3.compile(v1);
            /*SL:163*/if (a1 instanceof CtField) {
                /*SL:164*/return (CtField)a1;
            }
        }
        catch (CompileError a2) {
            /*SL:167*/throw new CannotCompileException(a2);
        }
        /*SL:170*/throw new CannotCompileException("not a field");
    }
    
    public FieldInfo getFieldInfo() {
        /*SL:177*/this.declaringClass.checkModify();
        /*SL:178*/return this.fieldInfo;
    }
    
    public FieldInfo getFieldInfo2() {
        /*SL:200*/return this.fieldInfo;
    }
    
    @Override
    public CtClass getDeclaringClass() {
        /*SL:207*/return super.getDeclaringClass();
    }
    
    @Override
    public String getName() {
        /*SL:214*/return this.fieldInfo.getName();
    }
    
    public void setName(final String a1) {
        /*SL:221*/this.declaringClass.checkModify();
        /*SL:222*/this.fieldInfo.setName(a1);
    }
    
    @Override
    public int getModifiers() {
        /*SL:231*/return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
    }
    
    @Override
    public void setModifiers(final int a1) {
        /*SL:240*/this.declaringClass.checkModify();
        /*SL:241*/this.fieldInfo.setAccessFlags(AccessFlag.of(a1));
    }
    
    @Override
    public boolean hasAnnotation(final String a1) {
        final FieldInfo v1 = /*EL:252*/this.getFieldInfo2();
        final AnnotationsAttribute v2 = /*EL:253*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:255*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:257*/return CtClassType.hasAnnotationType(a1, this.getDeclaringClass().getClassPool(), v2, v3);
    }
    
    @Override
    public Object getAnnotation(final Class a1) throws ClassNotFoundException {
        final FieldInfo v1 = /*EL:273*/this.getFieldInfo2();
        final AnnotationsAttribute v2 = /*EL:274*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:276*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:278*/return CtClassType.getAnnotationType(a1, this.getDeclaringClass().getClassPool(), v2, v3);
    }
    
    @Override
    public Object[] getAnnotations() throws ClassNotFoundException {
        /*SL:290*/return this.getAnnotations(false);
    }
    
    @Override
    public Object[] getAvailableAnnotations() {
        try {
            /*SL:304*/return this.getAnnotations(true);
        }
        catch (ClassNotFoundException v1) {
            /*SL:307*/throw new RuntimeException("Unexpected exception", v1);
        }
    }
    
    private Object[] getAnnotations(final boolean a1) throws ClassNotFoundException {
        final FieldInfo v1 = /*EL:312*/this.getFieldInfo2();
        final AnnotationsAttribute v2 = /*EL:313*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:315*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:317*/return CtClassType.toAnnotationType(a1, this.getDeclaringClass().getClassPool(), v2, v3);
    }
    
    @Override
    public String getSignature() {
        /*SL:336*/return this.fieldInfo.getDescriptor();
    }
    
    @Override
    public String getGenericSignature() {
        final SignatureAttribute v1 = /*EL:347*/(SignatureAttribute)this.fieldInfo.getAttribute("Signature");
        /*SL:349*/return (v1 == null) ? null : v1.getSignature();
    }
    
    @Override
    public void setGenericSignature(final String a1) {
        /*SL:363*/this.declaringClass.checkModify();
        /*SL:364*/this.fieldInfo.addAttribute(new SignatureAttribute(this.fieldInfo.getConstPool(), a1));
    }
    
    public CtClass getType() throws NotFoundException {
        /*SL:371*/return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass.getClassPool());
    }
    
    public void setType(final CtClass a1) {
        /*SL:390*/this.declaringClass.checkModify();
        /*SL:391*/this.fieldInfo.setDescriptor(Descriptor.of(a1));
    }
    
    public Object getConstantValue() {
        final int constantValue = /*EL:412*/this.fieldInfo.getConstantValue();
        /*SL:413*/if (constantValue == 0) {
            /*SL:414*/return null;
        }
        final ConstPool v0 = /*EL:416*/this.fieldInfo.getConstPool();
        /*SL:417*/switch (v0.getTag(constantValue)) {
            case 5: {
                /*SL:419*/return new Long(v0.getLongInfo(constantValue));
            }
            case 4: {
                /*SL:421*/return new Float(v0.getFloatInfo(constantValue));
            }
            case 6: {
                /*SL:423*/return new Double(v0.getDoubleInfo(constantValue));
            }
            case 3: {
                final int v = /*EL:425*/v0.getIntegerInfo(constantValue);
                /*SL:427*/if ("Z".equals(this.fieldInfo.getDescriptor())) {
                    /*SL:428*/return new Boolean(v != 0);
                }
                /*SL:430*/return new Integer(v);
            }
            case 8: {
                /*SL:432*/return v0.getStringInfo(constantValue);
            }
            default: {
                /*SL:434*/throw new RuntimeException("bad tag: " + v0.getTag(constantValue) + " at " + constantValue);
            }
        }
    }
    
    @Override
    public byte[] getAttribute(final String a1) {
        final AttributeInfo v1 = /*EL:451*/this.fieldInfo.getAttribute(a1);
        /*SL:452*/if (v1 == null) {
            /*SL:453*/return null;
        }
        /*SL:455*/return v1.get();
    }
    
    @Override
    public void setAttribute(final String a1, final byte[] a2) {
        /*SL:469*/this.declaringClass.checkModify();
        /*SL:470*/this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), a1, a2));
    }
    
    public abstract static class Initializer
    {
        public static Initializer constant(final int a1) {
            /*SL:495*/return new IntInitializer(a1);
        }
        
        public static Initializer constant(final boolean a1) {
            /*SL:503*/return new IntInitializer(a1 ? 1 : 0);
        }
        
        public static Initializer constant(final long a1) {
            /*SL:511*/return new LongInitializer(a1);
        }
        
        public static Initializer constant(final float a1) {
            /*SL:519*/return new FloatInitializer(a1);
        }
        
        public static Initializer constant(final double a1) {
            /*SL:527*/return new DoubleInitializer(a1);
        }
        
        public static Initializer constant(final String a1) {
            /*SL:535*/return new StringInitializer(a1);
        }
        
        public static Initializer byParameter(final int a1) {
            final ParamInitializer v1 = /*EL:553*/new ParamInitializer();
            /*SL:554*/v1.nthParam = a1;
            /*SL:555*/return v1;
        }
        
        public static Initializer byNew(final CtClass a1) {
            final NewInitializer v1 = /*EL:573*/new NewInitializer();
            /*SL:574*/v1.objectType = a1;
            /*SL:575*/v1.stringParams = null;
            /*SL:576*/v1.withConstructorParams = false;
            /*SL:577*/return v1;
        }
        
        public static Initializer byNew(final CtClass a1, final String[] a2) {
            final NewInitializer v1 = /*EL:600*/new NewInitializer();
            /*SL:601*/v1.objectType = a1;
            /*SL:602*/v1.stringParams = a2;
            /*SL:603*/v1.withConstructorParams = false;
            /*SL:604*/return v1;
        }
        
        public static Initializer byNewWithParams(final CtClass a1) {
            final NewInitializer v1 = /*EL:628*/new NewInitializer();
            /*SL:629*/v1.objectType = a1;
            /*SL:630*/v1.stringParams = null;
            /*SL:631*/v1.withConstructorParams = true;
            /*SL:632*/return v1;
        }
        
        public static Initializer byNewWithParams(final CtClass a1, final String[] a2) {
            final NewInitializer v1 = /*EL:658*/new NewInitializer();
            /*SL:659*/v1.objectType = a1;
            /*SL:660*/v1.stringParams = a2;
            /*SL:661*/v1.withConstructorParams = true;
            /*SL:662*/return v1;
        }
        
        public static Initializer byCall(final CtClass a1, final String a2) {
            final MethodInitializer v1 = /*EL:686*/new MethodInitializer();
            /*SL:687*/v1.objectType = a1;
            /*SL:688*/v1.methodName = a2;
            /*SL:689*/v1.stringParams = null;
            /*SL:690*/v1.withConstructorParams = false;
            /*SL:691*/return v1;
        }
        
        public static Initializer byCall(final CtClass a1, final String a2, final String[] a3) {
            final MethodInitializer v1 = /*EL:720*/new MethodInitializer();
            /*SL:721*/v1.objectType = a1;
            /*SL:722*/v1.methodName = a2;
            /*SL:723*/v1.stringParams = a3;
            /*SL:724*/v1.withConstructorParams = false;
            /*SL:725*/return v1;
        }
        
        public static Initializer byCallWithParams(final CtClass a1, final String a2) {
            final MethodInitializer v1 = /*EL:752*/new MethodInitializer();
            /*SL:753*/v1.objectType = a1;
            /*SL:754*/v1.methodName = a2;
            /*SL:755*/v1.stringParams = null;
            /*SL:756*/v1.withConstructorParams = true;
            /*SL:757*/return v1;
        }
        
        public static Initializer byCallWithParams(final CtClass a1, final String a2, final String[] a3) {
            final MethodInitializer v1 = /*EL:788*/new MethodInitializer();
            /*SL:789*/v1.objectType = a1;
            /*SL:790*/v1.methodName = a2;
            /*SL:791*/v1.stringParams = a3;
            /*SL:792*/v1.withConstructorParams = true;
            /*SL:793*/return v1;
        }
        
        public static Initializer byNewArray(final CtClass a1, final int a2) throws NotFoundException {
            /*SL:807*/return new ArrayInitializer(a1.getComponentType(), a2);
        }
        
        public static Initializer byNewArray(final CtClass a1, final int[] a2) {
            /*SL:820*/return new MultiArrayInitializer(a1, a2);
        }
        
        public static Initializer byExpr(final String a1) {
            /*SL:829*/return new CodeInitializer(a1);
        }
        
        static Initializer byExpr(final ASTree a1) {
            /*SL:833*/return new PtreeInitializer(a1);
        }
        
        void check(final String a1) throws CannotCompileException {
        }
        
        abstract int compile(final CtClass p0, final String p1, final Bytecode p2, final CtClass[] p3, final Javac p4) throws CannotCompileException;
        
        abstract int compileIfStatic(final CtClass p0, final String p1, final Bytecode p2, final Javac p3) throws CannotCompileException;
        
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:851*/return 0;
        }
    }
    
    abstract static class CodeInitializer0 extends Initializer
    {
        abstract void compileExpr(final Javac p0) throws CompileError;
        
        @Override
        int compile(final CtClass a3, final String a4, final Bytecode a5, final CtClass[] v1, final Javac v2) throws CannotCompileException {
            try {
                /*SL:862*/a5.addAload(0);
                /*SL:863*/this.compileExpr(v2);
                /*SL:864*/a5.addPutfield(Bytecode.THIS, a4, Descriptor.of(a3));
                /*SL:865*/return a5.getMaxStack();
            }
            catch (CompileError a6) {
                /*SL:868*/throw new CannotCompileException(a6);
            }
        }
        
        @Override
        int compileIfStatic(final CtClass a3, final String a4, final Bytecode v1, final Javac v2) throws CannotCompileException {
            try {
                /*SL:876*/this.compileExpr(v2);
                /*SL:877*/v1.addPutstatic(Bytecode.THIS, a4, Descriptor.of(a3));
                /*SL:878*/return v1.getMaxStack();
            }
            catch (CompileError a5) {
                /*SL:881*/throw new CannotCompileException(a5);
            }
        }
        
        int getConstantValue2(final ConstPool v1, final CtClass v2, final ASTree v3) {
            /*SL:886*/if (v2.isPrimitive()) {
                /*SL:887*/if (v3 instanceof IntConst) {
                    final long a1 = /*EL:888*/((IntConst)v3).get();
                    /*SL:889*/if (v2 == CtClass.doubleType) {
                        /*SL:890*/return v1.addDoubleInfo(a1);
                    }
                    /*SL:891*/if (v2 == CtClass.floatType) {
                        /*SL:892*/return v1.addFloatInfo(a1);
                    }
                    /*SL:893*/if (v2 == CtClass.longType) {
                        /*SL:894*/return v1.addLongInfo(a1);
                    }
                    /*SL:895*/if (v2 != CtClass.voidType) {
                        /*SL:896*/return v1.addIntegerInfo((int)a1);
                    }
                }
                else/*SL:898*/ if (v3 instanceof DoubleConst) {
                    final double a2 = /*EL:899*/((DoubleConst)v3).get();
                    /*SL:900*/if (v2 == CtClass.floatType) {
                        /*SL:901*/return v1.addFloatInfo((float)a2);
                    }
                    /*SL:902*/if (v2 == CtClass.doubleType) {
                        /*SL:903*/return v1.addDoubleInfo(a2);
                    }
                }
            }
            else/*SL:906*/ if (v3 instanceof StringL && v2.getName().equals(/*EL:907*/"java.lang.String")) {
                /*SL:908*/return v1.addStringInfo(((StringL)v3).get());
            }
            /*SL:910*/return 0;
        }
    }
    
    static class CodeInitializer extends CodeInitializer0
    {
        private String expression;
        
        CodeInitializer(final String a1) {
            this.expression = a1;
        }
        
        @Override
        void compileExpr(final Javac a1) throws CompileError {
            /*SL:920*/a1.compileExpr(this.expression);
        }
        
        @Override
        int getConstantValue(final ConstPool v2, final CtClass v3) {
            try {
                final ASTree a1 = /*EL:925*/Javac.parseExpr(this.expression, new SymbolTable());
                /*SL:926*/return this.getConstantValue2(v2, v3, a1);
            }
            catch (CompileError a2) {
                /*SL:929*/return 0;
            }
        }
    }
    
    static class PtreeInitializer extends CodeInitializer0
    {
        private ASTree expression;
        
        PtreeInitializer(final ASTree a1) {
            this.expression = a1;
        }
        
        @Override
        void compileExpr(final Javac a1) throws CompileError {
            /*SL:940*/a1.compileExpr(this.expression);
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:944*/return this.getConstantValue2(a1, a2, this.expression);
        }
    }
    
    static class ParamInitializer extends Initializer
    {
        int nthParam;
        
        @Override
        int compile(final CtClass a4, final String a5, final Bytecode v1, final CtClass[] v2, final Javac v3) throws CannotCompileException {
            /*SL:961*/if (v2 != null && this.nthParam < v2.length) {
                /*SL:962*/v1.addAload(0);
                final int a6 = nthParamToLocal(/*EL:963*/this.nthParam, v2, false);
                final int a7 = /*EL:964*/v1.addLoad(a6, a4) + 1;
                /*SL:965*/v1.addPutfield(Bytecode.THIS, a5, Descriptor.of(a4));
                /*SL:966*/return a7;
            }
            /*SL:969*/return 0;
        }
        
        static int nthParamToLocal(final int v1, final CtClass[] v2, final boolean v3) {
            final CtClass v4 = CtClass.longType;
            final CtClass v5 = CtClass.doubleType;
            int v6 = 0;
            /*SL:985*/if (v3) {
                final int a1 = /*EL:986*/0;
            }
            else {
                /*SL:988*/v6 = 1;
            }
            /*SL:990*/for (final CtClass a3 : /*EL:991*/v2) {
                /*SL:992*/if (a3 == v4 || a3 == v5) {
                    /*SL:993*/v6 += 2;
                }
                else {
                    /*SL:995*/++v6;
                }
            }
            /*SL:998*/return v6;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1004*/return 0;
        }
    }
    
    static class NewInitializer extends Initializer
    {
        CtClass objectType;
        String[] stringParams;
        boolean withConstructorParams;
        
        @Override
        int compile(final CtClass a3, final String a4, final Bytecode a5, final CtClass[] v1, final Javac v2) throws CannotCompileException {
            /*SL:1028*/a5.addAload(0);
            /*SL:1029*/a5.addNew(this.objectType);
            /*SL:1030*/a5.add(89);
            /*SL:1031*/a5.addAload(0);
            int v3 = 0;
            /*SL:1033*/if (this.stringParams == null) {
                final int a6 = /*EL:1034*/4;
            }
            else {
                /*SL:1036*/v3 = this.compileStringParameter(a5) + 4;
            }
            /*SL:1038*/if (this.withConstructorParams) {
                /*SL:1039*/v3 += CtNewWrappedMethod.compileParameterList(a5, v1, 1);
            }
            /*SL:1042*/a5.addInvokespecial(this.objectType, "<init>", this.getDescriptor());
            /*SL:1043*/a5.addPutfield(Bytecode.THIS, a4, Descriptor.of(a3));
            /*SL:1044*/return v3;
        }
        
        private String getDescriptor() {
            final String v1 = /*EL:1048*/"(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
            /*SL:1051*/if (this.stringParams == null) {
                /*SL:1052*/if (this.withConstructorParams) {
                    /*SL:1053*/return "(Ljava/lang/Object;[Ljava/lang/Object;)V";
                }
                /*SL:1055*/return "(Ljava/lang/Object;)V";
            }
            else {
                /*SL:1057*/if (this.withConstructorParams) {
                    /*SL:1058*/return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
                }
                /*SL:1060*/return "(Ljava/lang/Object;[Ljava/lang/String;)V";
            }
        }
        
        @Override
        int compileIfStatic(final CtClass a3, final String a4, final Bytecode v1, final Javac v2) throws CannotCompileException {
            /*SL:1071*/v1.addNew(this.objectType);
            /*SL:1072*/v1.add(89);
            int v3 = /*EL:1074*/2;
            final String v4;
            /*SL:1075*/if (this.stringParams == null) {
                final String a5 = /*EL:1076*/"()V";
            }
            else {
                /*SL:1078*/v4 = "([Ljava/lang/String;)V";
                /*SL:1079*/v3 += this.compileStringParameter(v1);
            }
            /*SL:1082*/v1.addInvokespecial(this.objectType, "<init>", v4);
            /*SL:1083*/v1.addPutstatic(Bytecode.THIS, a4, Descriptor.of(a3));
            /*SL:1084*/return v3;
        }
        
        protected final int compileStringParameter(final Bytecode v2) throws CannotCompileException {
            final int v3 = /*EL:1090*/this.stringParams.length;
            /*SL:1091*/v2.addIconst(v3);
            /*SL:1092*/v2.addAnewarray("java.lang.String");
            /*SL:1093*/for (int a1 = 0; a1 < v3; ++a1) {
                /*SL:1094*/v2.add(89);
                /*SL:1095*/v2.addIconst(a1);
                /*SL:1096*/v2.addLdc(this.stringParams[a1]);
                /*SL:1097*/v2.add(83);
            }
            /*SL:1100*/return 4;
        }
    }
    
    static class MethodInitializer extends NewInitializer
    {
        String methodName;
        
        @Override
        int compile(final CtClass a3, final String a4, final Bytecode a5, final CtClass[] v1, final Javac v2) throws CannotCompileException {
            /*SL:1124*/a5.addAload(0);
            /*SL:1125*/a5.addAload(0);
            int v3 = 0;
            /*SL:1127*/if (this.stringParams == null) {
                final int a6 = /*EL:1128*/2;
            }
            else {
                /*SL:1130*/v3 = this.compileStringParameter(a5) + 2;
            }
            /*SL:1132*/if (this.withConstructorParams) {
                /*SL:1133*/v3 += CtNewWrappedMethod.compileParameterList(a5, v1, 1);
            }
            final String v4 = /*EL:1136*/Descriptor.of(a3);
            final String v5 = /*EL:1137*/this.getDescriptor() + v4;
            /*SL:1138*/a5.addInvokestatic(this.objectType, this.methodName, v5);
            /*SL:1139*/a5.addPutfield(Bytecode.THIS, a4, v4);
            /*SL:1140*/return v3;
        }
        
        private String getDescriptor() {
            final String v1 = /*EL:1144*/"(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
            /*SL:1147*/if (this.stringParams == null) {
                /*SL:1148*/if (this.withConstructorParams) {
                    /*SL:1149*/return "(Ljava/lang/Object;[Ljava/lang/Object;)";
                }
                /*SL:1151*/return "(Ljava/lang/Object;)";
            }
            else {
                /*SL:1153*/if (this.withConstructorParams) {
                    /*SL:1154*/return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
                }
                /*SL:1156*/return "(Ljava/lang/Object;[Ljava/lang/String;)";
            }
        }
        
        @Override
        int compileIfStatic(final CtClass a3, final String a4, final Bytecode v1, final Javac v2) throws CannotCompileException {
            int v3 = /*EL:1167*/1;
            final String v4;
            /*SL:1168*/if (this.stringParams == null) {
                final String a5 = /*EL:1169*/"()";
            }
            else {
                /*SL:1171*/v4 = "([Ljava/lang/String;)";
                /*SL:1172*/v3 += this.compileStringParameter(v1);
            }
            final String v5 = /*EL:1175*/Descriptor.of(a3);
            /*SL:1176*/v1.addInvokestatic(this.objectType, this.methodName, v4 + v5);
            /*SL:1177*/v1.addPutstatic(Bytecode.THIS, a4, v5);
            /*SL:1178*/return v3;
        }
    }
    
    static class IntInitializer extends Initializer
    {
        int value;
        
        IntInitializer(final int a1) {
            this.value = a1;
        }
        
        @Override
        void check(final String a1) throws CannotCompileException {
            final char v1 = /*EL:1188*/a1.charAt(0);
            /*SL:1189*/if (v1 != 'I' && v1 != 'S' && v1 != 'B' && v1 != 'C' && v1 != 'Z') {
                /*SL:1190*/throw new CannotCompileException("type mismatch");
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1197*/a3.addAload(0);
            /*SL:1198*/a3.addIconst(this.value);
            /*SL:1199*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1200*/return 2;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1206*/a3.addIconst(this.value);
            /*SL:1207*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1208*/return 1;
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:1212*/return a1.addIntegerInfo(this.value);
        }
    }
    
    static class LongInitializer extends Initializer
    {
        long value;
        
        LongInitializer(final long a1) {
            this.value = a1;
        }
        
        @Override
        void check(final String a1) throws CannotCompileException {
            /*SL:1222*/if (!a1.equals("J")) {
                /*SL:1223*/throw new CannotCompileException("type mismatch");
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1230*/a3.addAload(0);
            /*SL:1231*/a3.addLdc2w(this.value);
            /*SL:1232*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1233*/return 3;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1239*/a3.addLdc2w(this.value);
            /*SL:1240*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1241*/return 2;
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:1245*/if (a2 == CtClass.longType) {
                /*SL:1246*/return a1.addLongInfo(this.value);
            }
            /*SL:1248*/return 0;
        }
    }
    
    static class FloatInitializer extends Initializer
    {
        float value;
        
        FloatInitializer(final float a1) {
            this.value = a1;
        }
        
        @Override
        void check(final String a1) throws CannotCompileException {
            /*SL:1258*/if (!a1.equals("F")) {
                /*SL:1259*/throw new CannotCompileException("type mismatch");
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1266*/a3.addAload(0);
            /*SL:1267*/a3.addFconst(this.value);
            /*SL:1268*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1269*/return 3;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1275*/a3.addFconst(this.value);
            /*SL:1276*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1277*/return 2;
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:1281*/if (a2 == CtClass.floatType) {
                /*SL:1282*/return a1.addFloatInfo(this.value);
            }
            /*SL:1284*/return 0;
        }
    }
    
    static class DoubleInitializer extends Initializer
    {
        double value;
        
        DoubleInitializer(final double a1) {
            this.value = a1;
        }
        
        @Override
        void check(final String a1) throws CannotCompileException {
            /*SL:1294*/if (!a1.equals("D")) {
                /*SL:1295*/throw new CannotCompileException("type mismatch");
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1302*/a3.addAload(0);
            /*SL:1303*/a3.addLdc2w(this.value);
            /*SL:1304*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1305*/return 3;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1311*/a3.addLdc2w(this.value);
            /*SL:1312*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1313*/return 2;
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:1317*/if (a2 == CtClass.doubleType) {
                /*SL:1318*/return a1.addDoubleInfo(this.value);
            }
            /*SL:1320*/return 0;
        }
    }
    
    static class StringInitializer extends Initializer
    {
        String value;
        
        StringInitializer(final String a1) {
            this.value = a1;
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1333*/a3.addAload(0);
            /*SL:1334*/a3.addLdc(this.value);
            /*SL:1335*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1336*/return 2;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1342*/a3.addLdc(this.value);
            /*SL:1343*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1344*/return 1;
        }
        
        @Override
        int getConstantValue(final ConstPool a1, final CtClass a2) {
            /*SL:1348*/if (a2.getName().equals("java.lang.String")) {
                /*SL:1349*/return a1.addStringInfo(this.value);
            }
            /*SL:1351*/return 0;
        }
    }
    
    static class ArrayInitializer extends Initializer
    {
        CtClass type;
        int size;
        
        ArrayInitializer(final CtClass a1, final int a2) {
            this.type = a1;
            this.size = a2;
        }
        
        private void addNewarray(final Bytecode a1) {
            /*SL:1362*/if (this.type.isPrimitive()) {
                /*SL:1363*/a1.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
            }
            else {
                /*SL:1366*/a1.addAnewarray(this.type, this.size);
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1373*/a3.addAload(0);
            /*SL:1374*/this.addNewarray(a3);
            /*SL:1375*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1376*/return 2;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            /*SL:1382*/this.addNewarray(a3);
            /*SL:1383*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1384*/return 1;
        }
    }
    
    static class MultiArrayInitializer extends Initializer
    {
        CtClass type;
        int[] dim;
        
        MultiArrayInitializer(final CtClass a1, final int[] a2) {
            this.type = a1;
            this.dim = a2;
        }
        
        @Override
        void check(final String a1) throws CannotCompileException {
            /*SL:1395*/if (a1.charAt(0) != '[') {
                /*SL:1396*/throw new CannotCompileException("type mismatch");
            }
        }
        
        @Override
        int compile(final CtClass a1, final String a2, final Bytecode a3, final CtClass[] a4, final Javac a5) throws CannotCompileException {
            /*SL:1403*/a3.addAload(0);
            final int v1 = /*EL:1404*/a3.addMultiNewarray(a1, this.dim);
            /*SL:1405*/a3.addPutfield(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1406*/return v1 + 1;
        }
        
        @Override
        int compileIfStatic(final CtClass a1, final String a2, final Bytecode a3, final Javac a4) throws CannotCompileException {
            final int v1 = /*EL:1412*/a3.addMultiNewarray(a1, this.dim);
            /*SL:1413*/a3.addPutstatic(Bytecode.THIS, a2, Descriptor.of(a1));
            /*SL:1414*/return v1;
        }
    }
}
