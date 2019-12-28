package javassist.expr;

import javassist.bytecode.BadBytecode;
import javassist.CtPrimitiveType;
import javassist.bytecode.Bytecode;
import javassist.CannotCompileException;
import javassist.bytecode.ClassFile;
import java.util.Iterator;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.CodeAttribute;
import javassist.ClassPool;
import javassist.NotFoundException;
import java.util.LinkedList;
import javassist.CtConstructor;
import javassist.CtBehavior;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;

public abstract class Expr implements Opcode
{
    int currentPos;
    CodeIterator iterator;
    CtClass thisClass;
    MethodInfo thisMethod;
    boolean edited;
    int maxLocals;
    int maxStack;
    static final String javaLangObject = "java.lang.Object";
    
    protected Expr(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4) {
        this.currentPos = a1;
        this.iterator = a2;
        this.thisClass = a3;
        this.thisMethod = a4;
    }
    
    public CtClass getEnclosingClass() {
        /*SL:71*/return this.thisClass;
    }
    
    protected final ConstPool getConstPool() {
        /*SL:74*/return this.thisMethod.getConstPool();
    }
    
    protected final boolean edited() {
        /*SL:78*/return this.edited;
    }
    
    protected final int locals() {
        /*SL:82*/return this.maxLocals;
    }
    
    protected final int stack() {
        /*SL:86*/return this.maxStack;
    }
    
    protected final boolean withinStatic() {
        /*SL:93*/return (this.thisMethod.getAccessFlags() & 0x8) != 0x0;
    }
    
    public CtBehavior where() {
        final MethodInfo thisMethod = /*EL:100*/this.thisMethod;
        final CtBehavior[] v0 = /*EL:101*/this.thisClass.getDeclaredBehaviors();
        /*SL:102*/for (int v = v0.length - 1; v >= 0; --v) {
            /*SL:103*/if (v0[v].getMethodInfo2() == thisMethod) {
                /*SL:104*/return v0[v];
            }
        }
        final CtConstructor v2 = /*EL:106*/this.thisClass.getClassInitializer();
        /*SL:107*/if (v2 != null && v2.getMethodInfo2() == thisMethod) {
            /*SL:108*/return v2;
        }
        /*SL:115*/for (int v3 = v0.length - 1; v3 >= 0; --v3) {
            /*SL:116*/if (this.thisMethod.getName().equals(v0[v3].getMethodInfo2().getName()) && this.thisMethod.getDescriptor().equals(/*EL:117*/v0[v3].getMethodInfo2().getDescriptor())) {
                /*SL:119*/return v0[v3];
            }
        }
        /*SL:123*/throw new RuntimeException("fatal: not found");
    }
    
    public CtClass[] mayThrow() {
        final ClassPool classPool = /*EL:133*/this.thisClass.getClassPool();
        final ConstPool constPool = /*EL:134*/this.thisMethod.getConstPool();
        final LinkedList list = /*EL:135*/new LinkedList();
        try {
            final CodeAttribute codeAttribute = /*EL:137*/this.thisMethod.getCodeAttribute();
            final ExceptionTable exceptionTable = /*EL:138*/codeAttribute.getExceptionTable();
            final int n = /*EL:139*/this.currentPos;
            /*SL:141*/for (int i = exceptionTable.size(), v0 = 0; v0 < i; ++v0) {
                /*SL:142*/if (exceptionTable.startPc(v0) <= n && n < exceptionTable.endPc(v0)) {
                    final int v = /*EL:143*/exceptionTable.catchType(v0);
                    /*SL:144*/if (v > 0) {
                        try {
                            addClass(/*EL:146*/list, classPool.get(constPool.getClassInfo(v)));
                        }
                        catch (NotFoundException ex) {}
                    }
                }
            }
        }
        catch (NullPointerException ex2) {}
        final ExceptionsAttribute exceptionsAttribute = /*EL:155*/this.thisMethod.getExceptionsAttribute();
        /*SL:156*/if (exceptionsAttribute != null) {
            final String[] exceptions = /*EL:157*/exceptionsAttribute.getExceptions();
            /*SL:158*/if (exceptions != null) {
                /*SL:160*/for (int n = exceptions.length, i = 0; i < n; ++i) {
                    try {
                        addClass(/*EL:162*/list, classPool.get(exceptions[i]));
                    }
                    catch (NotFoundException ex3) {}
                }
            }
        }
        /*SL:169*/return list.<CtClass>toArray(new CtClass[list.size()]);
    }
    
    private static void addClass(final LinkedList a1, final CtClass a2) {
        final Iterator v1 = /*EL:173*/a1.iterator();
        /*SL:174*/while (v1.hasNext()) {
            /*SL:175*/if (v1.next() == a2) {
                /*SL:176*/return;
            }
        }
        /*SL:178*/a1.add(a2);
    }
    
    public int indexOfBytecode() {
        /*SL:187*/return this.currentPos;
    }
    
    public int getLineNumber() {
        /*SL:196*/return this.thisMethod.getLineNumber(this.currentPos);
    }
    
    public String getFileName() {
        final ClassFile v1 = /*EL:205*/this.thisClass.getClassFile2();
        /*SL:206*/if (v1 == null) {
            /*SL:207*/return null;
        }
        /*SL:209*/return v1.getSourceFile();
    }
    
    static final boolean checkResultValue(final CtClass a1, final String a2) throws CannotCompileException {
        final boolean v1 = /*EL:217*/a2.indexOf("$_") >= 0;
        /*SL:218*/if (!v1 && a1 != CtClass.voidType) {
            /*SL:219*/throw new CannotCompileException("the resulting value is not stored in $_");
        }
        /*SL:223*/return v1;
    }
    
    static final void storeStack(final CtClass[] a1, final boolean a2, final int a3, final Bytecode a4) {
        storeStack0(/*EL:235*/0, a1.length, a1, a3 + 1, a4);
        /*SL:236*/if (a2) {
            /*SL:237*/a4.addOpcode(1);
        }
        /*SL:239*/a4.addAstore(a3);
    }
    
    private static void storeStack0(final int a4, final int a5, final CtClass[] v1, final int v2, final Bytecode v3) {
        /*SL:244*/if (a4 >= a5) {
            /*SL:245*/return;
        }
        final CtClass a6 = /*EL:247*/v1[a4];
        final int a8;
        /*SL:249*/if (a6 instanceof CtPrimitiveType) {
            final int a7 = /*EL:250*/((CtPrimitiveType)a6).getDataSize();
        }
        else {
            /*SL:252*/a8 = 1;
        }
        storeStack0(/*EL:254*/a4 + 1, a5, v1, v2 + a8, v3);
        /*SL:255*/v3.addStore(v2, a6);
    }
    
    public abstract void replace(final String p0) throws CannotCompileException;
    
    public void replace(final String a1, final ExprEditor a2) throws CannotCompileException {
        /*SL:285*/this.replace(a1);
        /*SL:286*/if (a2 != null) {
            /*SL:287*/this.runEditor(a2, this.iterator);
        }
    }
    
    protected void replace0(int a3, final Bytecode v1, final int v2) throws BadBytecode {
        final byte[] v3 = /*EL:292*/v1.get();
        /*SL:293*/this.edited = true;
        final int v4 = /*EL:294*/v3.length - v2;
        /*SL:295*/for (int a4 = 0; a4 < v2; ++a4) {
            /*SL:296*/this.iterator.writeByte(0, a3 + a4);
        }
        /*SL:298*/if (v4 > 0) {
            /*SL:299*/a3 = this.iterator.insertGapAt(a3, v4, false).position;
        }
        /*SL:301*/this.iterator.write(v3, a3);
        /*SL:302*/this.iterator.insert(v1.getExceptionTable(), a3);
        /*SL:303*/this.maxLocals = v1.getMaxLocals();
        /*SL:304*/this.maxStack = v1.getMaxStack();
    }
    
    protected void runEditor(final ExprEditor a1, final CodeIterator a2) throws CannotCompileException {
        final CodeAttribute v1 = /*EL:310*/a2.get();
        final int v2 = /*EL:311*/v1.getMaxLocals();
        final int v3 = /*EL:312*/v1.getMaxStack();
        final int v4 = /*EL:313*/this.locals();
        /*SL:314*/v1.setMaxStack(this.stack());
        /*SL:315*/v1.setMaxLocals(v4);
        final ExprEditor.LoopContext v5 = /*EL:316*/new ExprEditor.LoopContext(v4);
        final int v6 = /*EL:318*/a2.getCodeLength();
        final int v7 = /*EL:319*/a2.lookAhead();
        /*SL:320*/a2.move(this.currentPos);
        /*SL:321*/if (a1.doit(this.thisClass, this.thisMethod, v5, a2, v7)) {
            /*SL:322*/this.edited = true;
        }
        /*SL:324*/a2.move(v7 + a2.getCodeLength() - v6);
        /*SL:325*/v1.setMaxLocals(v2);
        /*SL:326*/v1.setMaxStack(v3);
        /*SL:327*/this.maxLocals = v5.maxLocals;
        /*SL:328*/this.maxStack += v5.maxStack;
    }
}
