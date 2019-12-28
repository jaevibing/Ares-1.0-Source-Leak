package javassist;

import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.CodeIterator;
import javassist.expr.ExprEditor;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;
import javassist.bytecode.LocalVariableTypeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.Bytecode;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ConstPool;
import javassist.bytecode.BadBytecode;
import java.util.Map;
import javassist.bytecode.MethodInfo;

public abstract class CtBehavior extends CtMember
{
    protected MethodInfo methodInfo;
    
    protected CtBehavior(final CtClass a1, final MethodInfo a2) {
        super(a1);
        this.methodInfo = a2;
    }
    
    void copy(final CtBehavior v-8, final boolean v-7, ClassMap v-6) throws CannotCompileException {
        final CtClass declaringClass = /*EL:49*/this.declaringClass;
        final MethodInfo methodInfo = /*EL:50*/v-8.methodInfo;
        final CtClass declaringClass2 = /*EL:51*/v-8.getDeclaringClass();
        final ConstPool constPool = /*EL:52*/declaringClass.getClassFile2().getConstPool();
        /*SL:54*/v-6 = new ClassMap(v-6);
        /*SL:55*/v-6.put(declaringClass2.getName(), declaringClass.getName());
        try {
            boolean a2 = /*EL:57*/false;
            /*SL:58*/a2 = declaringClass2.getSuperclass();
            final CtClass v1 = /*EL:59*/declaringClass.getSuperclass();
            String v2 = /*EL:60*/null;
            /*SL:61*/if (a2 != null && v1 != null) {
                final String a3 = /*EL:62*/a2.getName();
                /*SL:63*/v2 = v1.getName();
                /*SL:64*/if (!a3.equals(v2)) {
                    /*SL:65*/if (a3.equals("java.lang.Object")) {
                        /*SL:66*/a2 = true;
                    }
                    else {
                        /*SL:68*/v-6.putIfNone(a3, v2);
                    }
                }
            }
            /*SL:72*/this.methodInfo = new MethodInfo(constPool, methodInfo.getName(), methodInfo, v-6);
            /*SL:73*/if (v-7 && a2) {
                /*SL:74*/this.methodInfo.setSuperclass(v2);
            }
        }
        catch (NotFoundException a4) {
            /*SL:77*/throw new CannotCompileException(a4);
        }
        catch (BadBytecode a5) {
            /*SL:80*/throw new CannotCompileException(a5);
        }
    }
    
    @Override
    protected void extendToString(final StringBuffer a1) {
        /*SL:85*/a1.append(' ');
        /*SL:86*/a1.append(this.getName());
        /*SL:87*/a1.append(' ');
        /*SL:88*/a1.append(this.methodInfo.getDescriptor());
    }
    
    public abstract String getLongName();
    
    public MethodInfo getMethodInfo() {
        /*SL:111*/this.declaringClass.checkModify();
        /*SL:112*/return this.methodInfo;
    }
    
    public MethodInfo getMethodInfo2() {
        /*SL:134*/return this.methodInfo;
    }
    
    @Override
    public int getModifiers() {
        /*SL:144*/return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
    }
    
    @Override
    public void setModifiers(final int a1) {
        /*SL:157*/this.declaringClass.checkModify();
        /*SL:158*/this.methodInfo.setAccessFlags(AccessFlag.of(a1));
    }
    
    @Override
    public boolean hasAnnotation(final String a1) {
        final MethodInfo v1 = /*EL:170*/this.getMethodInfo2();
        final AnnotationsAttribute v2 = /*EL:171*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:173*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:175*/return CtClassType.hasAnnotationType(a1, this.getDeclaringClass().getClassPool(), /*EL:176*/v2, v3);
    }
    
    @Override
    public Object getAnnotation(final Class a1) throws ClassNotFoundException {
        final MethodInfo v1 = /*EL:192*/this.getMethodInfo2();
        final AnnotationsAttribute v2 = /*EL:193*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:195*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:197*/return CtClassType.getAnnotationType(a1, this.getDeclaringClass().getClassPool(), /*EL:198*/v2, v3);
    }
    
    @Override
    public Object[] getAnnotations() throws ClassNotFoundException {
        /*SL:210*/return this.getAnnotations(false);
    }
    
    @Override
    public Object[] getAvailableAnnotations() {
        try {
            /*SL:224*/return this.getAnnotations(true);
        }
        catch (ClassNotFoundException v1) {
            /*SL:227*/throw new RuntimeException("Unexpected exception", v1);
        }
    }
    
    private Object[] getAnnotations(final boolean a1) throws ClassNotFoundException {
        final MethodInfo v1 = /*EL:234*/this.getMethodInfo2();
        final AnnotationsAttribute v2 = /*EL:235*/(AnnotationsAttribute)v1.getAttribute("RuntimeInvisibleAnnotations");
        final AnnotationsAttribute v3 = /*EL:237*/(AnnotationsAttribute)v1.getAttribute("RuntimeVisibleAnnotations");
        /*SL:239*/return CtClassType.toAnnotationType(a1, this.getDeclaringClass().getClassPool(), /*EL:240*/v2, v3);
    }
    
    public Object[][] getParameterAnnotations() throws ClassNotFoundException {
        /*SL:256*/return this.getParameterAnnotations(false);
    }
    
    public Object[][] getAvailableParameterAnnotations() {
        try {
            /*SL:274*/return this.getParameterAnnotations(true);
        }
        catch (ClassNotFoundException v1) {
            /*SL:277*/throw new RuntimeException("Unexpected exception", v1);
        }
    }
    
    Object[][] getParameterAnnotations(final boolean a1) throws ClassNotFoundException {
        final MethodInfo v1 = /*EL:284*/this.getMethodInfo2();
        final ParameterAnnotationsAttribute v2 = /*EL:285*/(ParameterAnnotationsAttribute)v1.getAttribute("RuntimeInvisibleParameterAnnotations");
        final ParameterAnnotationsAttribute v3 = /*EL:287*/(ParameterAnnotationsAttribute)v1.getAttribute("RuntimeVisibleParameterAnnotations");
        /*SL:289*/return CtClassType.toAnnotationType(a1, this.getDeclaringClass().getClassPool(), /*EL:290*/v2, v3, v1);
    }
    
    public CtClass[] getParameterTypes() throws NotFoundException {
        /*SL:298*/return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }
    
    CtClass getReturnType0() throws NotFoundException {
        /*SL:306*/return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }
    
    @Override
    public String getSignature() {
        /*SL:328*/return this.methodInfo.getDescriptor();
    }
    
    @Override
    public String getGenericSignature() {
        final SignatureAttribute v1 = /*EL:339*/(SignatureAttribute)this.methodInfo.getAttribute("Signature");
        /*SL:341*/return (v1 == null) ? null : v1.getSignature();
    }
    
    @Override
    public void setGenericSignature(final String a1) {
        /*SL:355*/this.declaringClass.checkModify();
        /*SL:356*/this.methodInfo.addAttribute(new SignatureAttribute(this.methodInfo.getConstPool(), a1));
    }
    
    public CtClass[] getExceptionTypes() throws NotFoundException {
        ExceptionsAttribute v2 = /*EL:366*/this.methodInfo.getExceptionsAttribute();
        /*SL:367*/if (v2 == null) {
            /*SL:368*/v2 = null;
        }
        else {
            /*SL:370*/v2 = v2.getExceptions();
        }
        /*SL:372*/return this.declaringClass.getClassPool().get(v2);
    }
    
    public void setExceptionTypes(final CtClass[] v2) throws NotFoundException {
        /*SL:379*/this.declaringClass.checkModify();
        /*SL:380*/if (v2 == null || v2.length == 0) {
            /*SL:381*/this.methodInfo.removeExceptionsAttribute();
            /*SL:382*/return;
        }
        final String[] v3 = /*EL:385*/new String[v2.length];
        /*SL:386*/for (int a1 = 0; a1 < v2.length; ++a1) {
            /*SL:387*/v3[a1] = v2[a1].getName();
        }
        ExceptionsAttribute v4 = /*EL:389*/this.methodInfo.getExceptionsAttribute();
        /*SL:390*/if (v4 == null) {
            /*SL:391*/v4 = new ExceptionsAttribute(this.methodInfo.getConstPool());
            /*SL:392*/this.methodInfo.setExceptionsAttribute(v4);
        }
        /*SL:395*/v4.setExceptions(v3);
    }
    
    public abstract boolean isEmpty();
    
    public void setBody(final String a1) throws CannotCompileException {
        /*SL:412*/this.setBody(a1, null, null);
    }
    
    public void setBody(final String v-3, final String v-2, final String v-1) throws CannotCompileException {
        final CtClass v0 = /*EL:431*/this.declaringClass;
        /*SL:432*/v0.checkModify();
        try {
            final Javac a1 = /*EL:434*/new Javac(v0);
            /*SL:435*/if (v-1 != null) {
                /*SL:436*/a1.recordProceed(v-2, v-1);
            }
            final Bytecode a2 = /*EL:438*/a1.compileBody(this, v-3);
            /*SL:439*/this.methodInfo.setCodeAttribute(a2.toCodeAttribute());
            /*SL:440*/this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
            /*SL:442*/this.methodInfo.rebuildStackMapIf6(v0.getClassPool(), v0.getClassFile2());
            /*SL:443*/this.declaringClass.rebuildClassFile();
        }
        catch (CompileError a3) {
            /*SL:446*/throw new CannotCompileException(a3);
        }
        catch (BadBytecode v) {
            /*SL:448*/throw new CannotCompileException(v);
        }
    }
    
    static void setBody0(final CtClass a5, final MethodInfo v1, final CtClass v2, final MethodInfo v3, ClassMap v4) throws CannotCompileException {
        /*SL:457*/v2.checkModify();
        /*SL:459*/v4 = new ClassMap(v4);
        /*SL:460*/v4.put(a5.getName(), v2.getName());
        try {
            final CodeAttribute a6 = /*EL:462*/v1.getCodeAttribute();
            /*SL:463*/if (a6 != null) {
                final ConstPool a7 = /*EL:464*/v3.getConstPool();
                final CodeAttribute a8 = /*EL:465*/(CodeAttribute)a6.copy(a7, v4);
                /*SL:466*/v3.setCodeAttribute(a8);
            }
        }
        catch (CodeAttribute.RuntimeCopyException a9) {
            /*SL:473*/throw new CannotCompileException(a9);
        }
        /*SL:476*/v3.setAccessFlags(v3.getAccessFlags() & 0xFFFFFBFF);
        /*SL:478*/v2.rebuildClassFile();
    }
    
    @Override
    public byte[] getAttribute(final String a1) {
        final AttributeInfo v1 = /*EL:493*/this.methodInfo.getAttribute(a1);
        /*SL:494*/if (v1 == null) {
            /*SL:495*/return null;
        }
        /*SL:497*/return v1.get();
    }
    
    @Override
    public void setAttribute(final String a1, final byte[] a2) {
        /*SL:511*/this.declaringClass.checkModify();
        /*SL:512*/this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), a1, a2));
    }
    
    public void useCflow(final String v-4) throws CannotCompileException {
        final CtClass declaringClass = /*EL:534*/this.declaringClass;
        /*SL:535*/declaringClass.checkModify();
        final ClassPool classPool = /*EL:536*/declaringClass.getClassPool();
        int v0 = /*EL:538*/0;
        while (true) {
            final String string = /*EL:540*/"_cflow$" + v0++;
            try {
                /*SL:542*/declaringClass.getDeclaredField(string);
            }
            catch (NotFoundException a1) {
                /*SL:549*/classPool.recordCflow(v-4, this.declaringClass.getName(), string);
                try {
                    final CtClass v = /*EL:551*/classPool.get("javassist.runtime.Cflow");
                    final CtField v2 = /*EL:552*/new CtField(v, string, declaringClass);
                    /*SL:553*/v2.setModifiers(9);
                    /*SL:554*/declaringClass.addField(v2, CtField.Initializer.byNew(v));
                    /*SL:555*/this.insertBefore(string + ".enter();", false);
                    final String v3 = /*EL:556*/string + ".exit();";
                    /*SL:557*/this.insertAfter(v3, true);
                }
                catch (NotFoundException v4) {
                    /*SL:560*/throw new CannotCompileException(v4);
                }
            }
        }
    }
    
    public void addLocalVariable(final String a1, final CtClass a2) throws CannotCompileException {
        /*SL:582*/this.declaringClass.checkModify();
        final ConstPool v1 = /*EL:583*/this.methodInfo.getConstPool();
        final CodeAttribute v2 = /*EL:584*/this.methodInfo.getCodeAttribute();
        /*SL:585*/if (v2 == null) {
            /*SL:586*/throw new CannotCompileException("no method body");
        }
        LocalVariableAttribute v3 = /*EL:588*/(LocalVariableAttribute)v2.getAttribute("LocalVariableTable");
        /*SL:590*/if (v3 == null) {
            /*SL:591*/v3 = new LocalVariableAttribute(v1);
            /*SL:592*/v2.getAttributes().add(v3);
        }
        final int v4 = /*EL:595*/v2.getMaxLocals();
        final String v5 = /*EL:596*/Descriptor.of(a2);
        /*SL:597*/v3.addEntry(0, v2.getCodeLength(), v1.addUtf8Info(a1), /*EL:598*/v1.addUtf8Info(v5), v4);
        /*SL:599*/v2.setMaxLocals(v4 + Descriptor.dataSize(v5));
    }
    
    public void insertParameter(final CtClass v2) throws CannotCompileException {
        /*SL:608*/this.declaringClass.checkModify();
        final String v3 = /*EL:609*/this.methodInfo.getDescriptor();
        final String v4 = /*EL:610*/Descriptor.insertParameter(v2, v3);
        try {
            /*SL:612*/this.addParameter2(Modifier.isStatic(this.getModifiers()) ? 0 : 1, v2, v3);
        }
        catch (BadBytecode a1) {
            /*SL:615*/throw new CannotCompileException(a1);
        }
        /*SL:618*/this.methodInfo.setDescriptor(v4);
    }
    
    public void addParameter(final CtClass v2) throws CannotCompileException {
        /*SL:627*/this.declaringClass.checkModify();
        final String v3 = /*EL:628*/this.methodInfo.getDescriptor();
        final String v4 = /*EL:629*/Descriptor.appendParameter(v2, v3);
        final int v5 = /*EL:630*/Modifier.isStatic(this.getModifiers()) ? 0 : 1;
        try {
            /*SL:632*/this.addParameter2(v5 + Descriptor.paramSize(v3), v2, v3);
        }
        catch (BadBytecode a1) {
            /*SL:635*/throw new CannotCompileException(a1);
        }
        /*SL:638*/this.methodInfo.setDescriptor(v4);
    }
    
    private void addParameter2(final int v-5, final CtClass v-4, final String v-3) throws BadBytecode {
        final CodeAttribute codeAttribute = /*EL:644*/this.methodInfo.getCodeAttribute();
        /*SL:645*/if (codeAttribute != null) {
            int a2 = /*EL:646*/1;
            /*SL:647*/a2 = 'L';
            int v1 = /*EL:648*/0;
            /*SL:649*/if (v-4.isPrimitive()) {
                final CtPrimitiveType a3 = /*EL:650*/(CtPrimitiveType)v-4;
                /*SL:651*/a2 = a3.getDataSize();
                /*SL:652*/a2 = a3.getDescriptor();
            }
            else {
                /*SL:655*/v1 = this.methodInfo.getConstPool().addClassInfo(v-4);
            }
            /*SL:657*/codeAttribute.insertLocalVar(v-5, a2);
            final LocalVariableAttribute v2 = /*EL:658*/(LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
            /*SL:660*/if (v2 != null) {
                /*SL:661*/v2.shiftIndex(v-5, a2);
            }
            final LocalVariableTypeAttribute v3 = /*EL:663*/(LocalVariableTypeAttribute)codeAttribute.getAttribute("LocalVariableTypeTable");
            /*SL:665*/if (v3 != null) {
                /*SL:666*/v3.shiftIndex(v-5, a2);
            }
            final StackMapTable v4 = /*EL:668*/(StackMapTable)codeAttribute.getAttribute("StackMapTable");
            /*SL:669*/if (v4 != null) {
                /*SL:670*/v4.insertLocal(v-5, StackMapTable.typeTagOf(a2), v1);
            }
            final StackMap v5 = /*EL:672*/(StackMap)codeAttribute.getAttribute("StackMap");
            /*SL:673*/if (v5 != null) {
                /*SL:674*/v5.insertLocal(v-5, StackMapTable.typeTagOf(a2), v1);
            }
        }
    }
    
    public void instrument(final CodeConverter a1) throws CannotCompileException {
        /*SL:686*/this.declaringClass.checkModify();
        final ConstPool v1 = /*EL:687*/this.methodInfo.getConstPool();
        /*SL:688*/a1.doit(this.getDeclaringClass(), this.methodInfo, v1);
    }
    
    public void instrument(final ExprEditor a1) throws CannotCompileException {
        /*SL:709*/if (this.declaringClass.isFrozen()) {
            /*SL:710*/this.declaringClass.checkModify();
        }
        /*SL:712*/if (a1.doit(this.declaringClass, this.methodInfo)) {
            /*SL:713*/this.declaringClass.checkModify();
        }
    }
    
    public void insertBefore(final String a1) throws CannotCompileException {
        /*SL:734*/this.insertBefore(a1, true);
    }
    
    private void insertBefore(final String v-7, final boolean v-6) throws CannotCompileException {
        final CtClass declaringClass = /*EL:740*/this.declaringClass;
        /*SL:741*/declaringClass.checkModify();
        final CodeAttribute codeAttribute = /*EL:742*/this.methodInfo.getCodeAttribute();
        /*SL:743*/if (codeAttribute == null) {
            /*SL:744*/throw new CannotCompileException("no method body");
        }
        final CodeIterator iterator = /*EL:746*/codeAttribute.iterator();
        final Javac javac = /*EL:747*/new Javac(declaringClass);
        try {
            final int a1 = /*EL:749*/javac.recordParams(this.getParameterTypes(), /*EL:750*/Modifier.isStatic(this.getModifiers()));
            /*SL:751*/javac.recordParamNames(codeAttribute, a1);
            /*SL:752*/javac.recordLocalVariables(codeAttribute, 0);
            /*SL:753*/javac.recordType(this.getReturnType0());
            /*SL:754*/javac.compileStmnt(v-7);
            final Bytecode a2 = /*EL:755*/javac.getBytecode();
            final int v1 = /*EL:756*/a2.getMaxStack();
            final int v2 = /*EL:757*/a2.getMaxLocals();
            /*SL:759*/if (v1 > codeAttribute.getMaxStack()) {
                /*SL:760*/codeAttribute.setMaxStack(v1);
            }
            /*SL:762*/if (v2 > codeAttribute.getMaxLocals()) {
                /*SL:763*/codeAttribute.setMaxLocals(v2);
            }
            final int v3 = /*EL:765*/iterator.insertEx(a2.get());
            /*SL:766*/iterator.insert(a2.getExceptionTable(), v3);
            /*SL:767*/if (v-6) {
                /*SL:768*/this.methodInfo.rebuildStackMapIf6(declaringClass.getClassPool(), declaringClass.getClassFile2());
            }
        }
        catch (NotFoundException a3) {
            /*SL:771*/throw new CannotCompileException(a3);
        }
        catch (CompileError a4) {
            /*SL:774*/throw new CannotCompileException(a4);
        }
        catch (BadBytecode a5) {
            /*SL:777*/throw new CannotCompileException(a5);
        }
    }
    
    public void insertAfter(final String a1) throws CannotCompileException {
        /*SL:792*/this.insertAfter(a1, false);
    }
    
    public void insertAfter(final String v-8, final boolean v-7) throws CannotCompileException {
        final CtClass declaringClass = /*EL:810*/this.declaringClass;
        /*SL:811*/declaringClass.checkModify();
        final ConstPool constPool = /*EL:812*/this.methodInfo.getConstPool();
        final CodeAttribute codeAttribute = /*EL:813*/this.methodInfo.getCodeAttribute();
        /*SL:814*/if (codeAttribute == null) {
            /*SL:815*/throw new CannotCompileException("no method body");
        }
        final CodeIterator iterator = /*EL:817*/codeAttribute.iterator();
        final int maxLocals = /*EL:818*/codeAttribute.getMaxLocals();
        final Bytecode a3 = /*EL:819*/new Bytecode(constPool, 0, maxLocals + 1);
        /*SL:820*/a3.setStackDepth(codeAttribute.getMaxStack() + 1);
        final Javac v0 = /*EL:821*/new Javac(a3, declaringClass);
        try {
            final int v = /*EL:823*/v0.recordParams(this.getParameterTypes(), /*EL:824*/Modifier.isStatic(this.getModifiers()));
            /*SL:825*/v0.recordParamNames(codeAttribute, v);
            final CtClass v2 = /*EL:826*/this.getReturnType0();
            final int v3 = /*EL:827*/v0.recordReturnType(v2, true);
            /*SL:828*/v0.recordLocalVariables(codeAttribute, 0);
            int v4 = /*EL:831*/this.insertAfterHandler(v-7, a3, v2, v3, v0, v-8);
            int v5 = /*EL:833*/iterator.getCodeLength();
            /*SL:834*/if (v-7) {
                /*SL:835*/codeAttribute.getExceptionTable().add(this.getStartPosOfBody(codeAttribute), v5, v5, 0);
            }
            int v6 = /*EL:837*/0;
            int v7 = /*EL:838*/0;
            boolean v8 = /*EL:839*/true;
            /*SL:840*/while (iterator.hasNext()) {
                final int a1 = /*EL:841*/iterator.next();
                /*SL:842*/if (a1 >= v5) {
                    /*SL:843*/break;
                }
                final int a2 = /*EL:845*/iterator.byteAt(a1);
                /*SL:846*/if (a2 != 176 && a2 != 172 && a2 != 174 && a2 != 173 && a2 != 175 && a2 != 177) {
                    continue;
                }
                /*SL:849*/if (v8) {
                    /*SL:851*/v6 = this.insertAfterAdvice(a3, v0, v-8, constPool, v2, v3);
                    /*SL:852*/v5 = iterator.append(a3.get());
                    /*SL:853*/iterator.append(a3.getExceptionTable(), v5);
                    /*SL:854*/v7 = iterator.getCodeLength() - v6;
                    /*SL:855*/v4 = v7 - v5;
                    /*SL:856*/v8 = false;
                }
                /*SL:858*/this.insertGoto(iterator, v7, a1);
                /*SL:859*/v7 = iterator.getCodeLength() - v6;
                /*SL:860*/v5 = v7 - v4;
            }
            /*SL:864*/if (v8) {
                /*SL:865*/v5 = iterator.append(a3.get());
                /*SL:866*/iterator.append(a3.getExceptionTable(), v5);
            }
            /*SL:869*/codeAttribute.setMaxStack(a3.getMaxStack());
            /*SL:870*/codeAttribute.setMaxLocals(a3.getMaxLocals());
            /*SL:871*/this.methodInfo.rebuildStackMapIf6(declaringClass.getClassPool(), declaringClass.getClassFile2());
        }
        catch (NotFoundException v9) {
            /*SL:874*/throw new CannotCompileException(v9);
        }
        catch (CompileError v10) {
            /*SL:877*/throw new CannotCompileException(v10);
        }
        catch (BadBytecode v11) {
            /*SL:880*/throw new CannotCompileException(v11);
        }
    }
    
    private int insertAfterAdvice(final Bytecode a1, final Javac a2, final String a3, final ConstPool a4, final CtClass a5, final int a6) throws CompileError {
        final int v1 = /*EL:888*/a1.currentPc();
        /*SL:889*/if (a5 == CtClass.voidType) {
            /*SL:890*/a1.addOpcode(1);
            /*SL:891*/a1.addAstore(a6);
            /*SL:892*/a2.compileStmnt(a3);
            /*SL:893*/a1.addOpcode(177);
            /*SL:894*/if (a1.getMaxLocals() < 1) {
                /*SL:895*/a1.setMaxLocals(1);
            }
        }
        else {
            /*SL:898*/a1.addStore(a6, a5);
            /*SL:899*/a2.compileStmnt(a3);
            /*SL:900*/a1.addLoad(a6, a5);
            /*SL:901*/if (a5.isPrimitive()) {
                /*SL:902*/a1.addOpcode(((CtPrimitiveType)a5).getReturnOp());
            }
            else {
                /*SL:904*/a1.addOpcode(176);
            }
        }
        /*SL:907*/return a1.currentPc() - v1;
    }
    
    private void insertGoto(final CodeIterator a3, final int v1, int v2) throws BadBytecode {
        /*SL:916*/a3.setMark(v1);
        /*SL:918*/a3.writeByte(0, v2);
        final boolean v3 = /*EL:919*/v1 + 2 - v2 > 32767;
        final int v4 = /*EL:920*/v3 ? 4 : 2;
        final CodeIterator.Gap v5 = /*EL:921*/a3.insertGapAt(v2, v4, false);
        /*SL:922*/v2 = v5.position + v5.length - v4;
        final int v6 = /*EL:923*/a3.getMark() - v2;
        /*SL:924*/if (v3) {
            /*SL:925*/a3.writeByte(200, v2);
            /*SL:926*/a3.write32bit(v6, v2 + 1);
        }
        else/*SL:928*/ if (v6 <= 32767) {
            /*SL:929*/a3.writeByte(167, v2);
            /*SL:930*/a3.write16bit(v6, v2 + 1);
        }
        else {
            /*SL:933*/if (v5.length < 4) {
                final CodeIterator.Gap a4 = /*EL:934*/a3.insertGapAt(v5.position, 2, false);
                /*SL:935*/v2 = a4.position + a4.length + v5.length - 4;
            }
            /*SL:938*/a3.writeByte(200, v2);
            /*SL:939*/a3.write32bit(a3.getMark() - v2, v2 + 1);
        }
    }
    
    private int insertAfterHandler(final boolean a3, final Bytecode a4, final CtClass a5, final int a6, final Javac v1, final String v2) throws CompileError {
        /*SL:950*/if (!a3) {
            /*SL:951*/return 0;
        }
        final int v3 = /*EL:953*/a4.getMaxLocals();
        /*SL:954*/a4.incMaxLocals(1);
        final int v4 = /*EL:955*/a4.currentPc();
        /*SL:956*/a4.addAstore(v3);
        /*SL:957*/if (a5.isPrimitive()) {
            final char a7 = /*EL:958*/((CtPrimitiveType)a5).getDescriptor();
            /*SL:959*/if (a7 == 'D') {
                /*SL:960*/a4.addDconst(0.0);
                /*SL:961*/a4.addDstore(a6);
            }
            else/*SL:963*/ if (a7 == 'F') {
                /*SL:964*/a4.addFconst(0.0f);
                /*SL:965*/a4.addFstore(a6);
            }
            else/*SL:967*/ if (a7 == 'J') {
                /*SL:968*/a4.addLconst(0L);
                /*SL:969*/a4.addLstore(a6);
            }
            else/*SL:971*/ if (a7 == 'V') {
                /*SL:972*/a4.addOpcode(1);
                /*SL:973*/a4.addAstore(a6);
            }
            else {
                /*SL:976*/a4.addIconst(0);
                /*SL:977*/a4.addIstore(a6);
            }
        }
        else {
            /*SL:981*/a4.addOpcode(1);
            /*SL:982*/a4.addAstore(a6);
        }
        /*SL:985*/v1.compileStmnt(v2);
        /*SL:986*/a4.addAload(v3);
        /*SL:987*/a4.addOpcode(191);
        /*SL:988*/return a4.currentPc() - v4;
    }
    
    public void addCatch(final String a1, final CtClass a2) throws CannotCompileException {
        /*SL:1054*/this.addCatch(a1, a2, "$e");
    }
    
    public void addCatch(final String v-11, final CtClass v-10, final String v-9) throws CannotCompileException {
        final CtClass declaringClass = /*EL:1073*/this.declaringClass;
        /*SL:1074*/declaringClass.checkModify();
        final ConstPool constPool = /*EL:1075*/this.methodInfo.getConstPool();
        final CodeAttribute codeAttribute = /*EL:1076*/this.methodInfo.getCodeAttribute();
        final CodeIterator iterator = /*EL:1077*/codeAttribute.iterator();
        final Bytecode a4 = /*EL:1078*/new Bytecode(constPool, codeAttribute.getMaxStack(), codeAttribute.getMaxLocals());
        /*SL:1079*/a4.setStackDepth(1);
        final Javac javac = /*EL:1080*/new Javac(a4, declaringClass);
        try {
            /*SL:1082*/javac.recordParams(this.getParameterTypes(), /*EL:1083*/Modifier.isStatic(this.getModifiers()));
            final int a1 = /*EL:1084*/javac.recordVariable(v-10, v-9);
            /*SL:1085*/a4.addAstore(a1);
            /*SL:1086*/javac.compileStmnt(v-11);
            final int a2 = /*EL:1088*/a4.getMaxStack();
            final int a3 = /*EL:1089*/a4.getMaxLocals();
            /*SL:1091*/if (a2 > codeAttribute.getMaxStack()) {
                /*SL:1092*/codeAttribute.setMaxStack(a2);
            }
            /*SL:1094*/if (a3 > codeAttribute.getMaxLocals()) {
                /*SL:1095*/codeAttribute.setMaxLocals(a3);
            }
            final int v1 = /*EL:1097*/iterator.getCodeLength();
            final int v2 = /*EL:1098*/iterator.append(a4.get());
            /*SL:1099*/codeAttribute.getExceptionTable().add(this.getStartPosOfBody(codeAttribute), v1, v1, constPool.addClassInfo(v-10));
            /*SL:1101*/iterator.append(a4.getExceptionTable(), v2);
            /*SL:1102*/this.methodInfo.rebuildStackMapIf6(declaringClass.getClassPool(), declaringClass.getClassFile2());
        }
        catch (NotFoundException a5) {
            /*SL:1105*/throw new CannotCompileException(a5);
        }
        catch (CompileError a6) {
            /*SL:1108*/throw new CannotCompileException(a6);
        }
        catch (BadBytecode a7) {
            /*SL:1110*/throw new CannotCompileException(a7);
        }
    }
    
    int getStartPosOfBody(final CodeAttribute a1) throws CannotCompileException {
        /*SL:1117*/return 0;
    }
    
    public int insertAt(final int a1, final String a2) throws CannotCompileException {
        /*SL:1140*/return this.insertAt(a1, true, a2);
    }
    
    public int insertAt(int v-9, final boolean v-8, final String v-7) throws CannotCompileException {
        final CodeAttribute codeAttribute = /*EL:1168*/this.methodInfo.getCodeAttribute();
        /*SL:1169*/if (codeAttribute == null) {
            /*SL:1170*/throw new CannotCompileException("no method body");
        }
        final LineNumberAttribute lineNumberAttribute = /*EL:1172*/(LineNumberAttribute)codeAttribute.getAttribute("LineNumberTable");
        /*SL:1174*/if (lineNumberAttribute == null) {
            /*SL:1175*/throw new CannotCompileException("no line number info");
        }
        final LineNumberAttribute.Pc nearPc = /*EL:1177*/lineNumberAttribute.toNearPc(v-9);
        /*SL:1178*/v-9 = nearPc.line;
        int a4 = /*EL:1179*/nearPc.index;
        /*SL:1180*/if (!v-8) {
            /*SL:1181*/return v-9;
        }
        final CtClass declaringClass = /*EL:1183*/this.declaringClass;
        /*SL:1184*/declaringClass.checkModify();
        final CodeIterator iterator = /*EL:1185*/codeAttribute.iterator();
        final Javac v0 = /*EL:1186*/new Javac(declaringClass);
        try {
            /*SL:1188*/v0.recordLocalVariables(codeAttribute, a4);
            /*SL:1189*/v0.recordParams(this.getParameterTypes(), /*EL:1190*/Modifier.isStatic(this.getModifiers()));
            /*SL:1191*/v0.setMaxLocals(codeAttribute.getMaxLocals());
            /*SL:1192*/v0.compileStmnt(v-7);
            final Bytecode a1 = /*EL:1193*/v0.getBytecode();
            final int a2 = /*EL:1194*/a1.getMaxLocals();
            final int a3 = /*EL:1195*/a1.getMaxStack();
            /*SL:1196*/codeAttribute.setMaxLocals(a2);
            /*SL:1201*/if (a3 > codeAttribute.getMaxStack()) {
                /*SL:1202*/codeAttribute.setMaxStack(a3);
            }
            /*SL:1204*/a4 = iterator.insertAt(a4, a1.get());
            /*SL:1205*/iterator.insert(a1.getExceptionTable(), a4);
            /*SL:1206*/this.methodInfo.rebuildStackMapIf6(declaringClass.getClassPool(), declaringClass.getClassFile2());
            /*SL:1207*/return v-9;
        }
        catch (NotFoundException v) {
            /*SL:1210*/throw new CannotCompileException(v);
        }
        catch (CompileError v2) {
            /*SL:1213*/throw new CannotCompileException(v2);
        }
        catch (BadBytecode v3) {
            /*SL:1216*/throw new CannotCompileException(v3);
        }
    }
}
