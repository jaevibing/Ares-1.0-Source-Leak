package javassist;

import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public final class CtConstructor extends CtBehavior
{
    protected CtConstructor(final MethodInfo a1, final CtClass a2) {
        super(a2, a1);
    }
    
    public CtConstructor(final CtClass[] a1, final CtClass a2) {
        this((MethodInfo)null, a2);
        final ConstPool v1 = a2.getClassFile2().getConstPool();
        final String v2 = Descriptor.ofConstructor(a1);
        this.methodInfo = new MethodInfo(v1, "<init>", v2);
        this.setModifiers(1);
    }
    
    public CtConstructor(final CtConstructor a1, final CtClass a2, final ClassMap a3) throws CannotCompileException {
        this((MethodInfo)null, a2);
        this.copy(a1, true, a3);
    }
    
    public boolean isConstructor() {
        /*SL:109*/return this.methodInfo.isConstructor();
    }
    
    public boolean isClassInitializer() {
        /*SL:116*/return this.methodInfo.isStaticInitializer();
    }
    
    @Override
    public String getLongName() {
        /*SL:127*/return this.getDeclaringClass().getName() + (this.isConstructor() ? Descriptor.toString(this.getSignature()) : ".<clinit>()");
    }
    
    @Override
    public String getName() {
        /*SL:138*/if (this.methodInfo.isStaticInitializer()) {
            /*SL:139*/return "<clinit>";
        }
        /*SL:141*/return this.declaringClass.getSimpleName();
    }
    
    @Override
    public boolean isEmpty() {
        final CodeAttribute codeAttribute = /*EL:152*/this.getMethodInfo2().getCodeAttribute();
        /*SL:153*/if (codeAttribute == null) {
            /*SL:154*/return false;
        }
        final ConstPool constPool = /*EL:157*/codeAttribute.getConstPool();
        final CodeIterator v0 = /*EL:158*/codeAttribute.iterator();
        try {
            final int v = /*EL:161*/v0.byteAt(v0.next());
            final int v2;
            final int v3;
            /*SL:169*/return v == 177 || (v == 42 && v0.byteAt(v2 = v0.next()) == 183 && (v3 = constPool.isConstructor(this.getSuperclassName(), v0.u16bitAt(v2 + 1))) != 0 && "()V".equals(constPool.getUtf8Info(v3)) && v0.byteAt(v0.next()) == 177 && !v0.hasNext());
        }
        catch (BadBytecode badBytecode) {
            /*SL:172*/return false;
        }
    }
    
    private String getSuperclassName() {
        final ClassFile v1 = /*EL:176*/this.declaringClass.getClassFile2();
        /*SL:177*/return v1.getSuperclass();
    }
    
    public boolean callsSuper() throws CannotCompileException {
        final CodeAttribute codeAttribute = /*EL:186*/this.methodInfo.getCodeAttribute();
        /*SL:187*/if (codeAttribute != null) {
            final CodeIterator v0 = /*EL:188*/codeAttribute.iterator();
            try {
                final int v = /*EL:190*/v0.skipSuperConstructor();
                /*SL:191*/return v >= 0;
            }
            catch (BadBytecode v2) {
                /*SL:194*/throw new CannotCompileException(v2);
            }
        }
        /*SL:198*/return false;
    }
    
    @Override
    public void setBody(String a1) throws CannotCompileException {
        /*SL:211*/if (a1 == null) {
            /*SL:212*/if (this.isClassInitializer()) {
                /*SL:213*/a1 = ";";
            }
            else {
                /*SL:215*/a1 = "super();";
            }
        }
        /*SL:217*/super.setBody(a1);
    }
    
    public void setBody(final CtConstructor a1, final ClassMap a2) throws CannotCompileException {
        /*SL:235*/CtBehavior.setBody0(a1.declaringClass, a1.methodInfo, this.declaringClass, this.methodInfo, a2);
    }
    
    public void insertBeforeBody(final String v-5) throws CannotCompileException {
        final CtClass declaringClass = /*EL:248*/this.declaringClass;
        /*SL:249*/declaringClass.checkModify();
        /*SL:250*/if (this.isClassInitializer()) {
            /*SL:251*/throw new CannotCompileException("class initializer");
        }
        final CodeAttribute codeAttribute = /*EL:253*/this.methodInfo.getCodeAttribute();
        final CodeIterator iterator = /*EL:254*/codeAttribute.iterator();
        final Bytecode a2 = /*EL:256*/new Bytecode(this.methodInfo.getConstPool(), codeAttribute.getMaxStack(), codeAttribute.getMaxLocals());
        /*SL:257*/a2.setStackDepth(codeAttribute.getMaxStack());
        final Javac v0 = /*EL:258*/new Javac(a2, declaringClass);
        try {
            /*SL:260*/v0.recordParams(this.getParameterTypes(), false);
            /*SL:261*/v0.compileStmnt(v-5);
            /*SL:262*/codeAttribute.setMaxStack(a2.getMaxStack());
            /*SL:263*/codeAttribute.setMaxLocals(a2.getMaxLocals());
            /*SL:264*/iterator.skipConstructor();
            final int a1 = /*EL:265*/iterator.insertEx(a2.get());
            /*SL:266*/iterator.insert(a2.getExceptionTable(), a1);
            /*SL:267*/this.methodInfo.rebuildStackMapIf6(declaringClass.getClassPool(), declaringClass.getClassFile2());
        }
        catch (NotFoundException v) {
            /*SL:270*/throw new CannotCompileException(v);
        }
        catch (CompileError v2) {
            /*SL:273*/throw new CannotCompileException(v2);
        }
        catch (BadBytecode v3) {
            /*SL:276*/throw new CannotCompileException(v3);
        }
    }
    
    @Override
    int getStartPosOfBody(final CodeAttribute v2) throws CannotCompileException {
        final CodeIterator v3 = /*EL:284*/v2.iterator();
        try {
            /*SL:286*/v3.skipConstructor();
            /*SL:287*/return v3.next();
        }
        catch (BadBytecode a1) {
            /*SL:290*/throw new CannotCompileException(a1);
        }
    }
    
    public CtMethod toMethod(final String a1, final CtClass a2) throws CannotCompileException {
        /*SL:317*/return this.toMethod(a1, a2, null);
    }
    
    public CtMethod toMethod(final String v2, final CtClass v3, final ClassMap v4) throws CannotCompileException {
        final CtMethod v5 = /*EL:351*/new CtMethod(null, v3);
        /*SL:352*/v5.copy(this, false, v4);
        /*SL:353*/if (this.isConstructor()) {
            MethodInfo a2 = /*EL:354*/v5.getMethodInfo2();
            /*SL:355*/a2 = a2.getCodeAttribute();
            /*SL:356*/if (a2 != null) {
                removeConsCall(/*EL:357*/a2);
                try {
                    /*SL:359*/this.methodInfo.rebuildStackMapIf6(v3.getClassPool(), v3.getClassFile2());
                }
                catch (BadBytecode a3) {
                    /*SL:363*/throw new CannotCompileException(a3);
                }
            }
        }
        /*SL:368*/v5.setName(v2);
        /*SL:369*/return v5;
    }
    
    private static void removeConsCall(final CodeAttribute v-3) throws CannotCompileException {
        final CodeIterator iterator = /*EL:375*/v-3.iterator();
        try {
            int n = /*EL:377*/iterator.skipConstructor();
            /*SL:378*/if (n >= 0) {
                final int a1 = /*EL:379*/iterator.u16bitAt(n + 1);
                final String v1 = /*EL:380*/v-3.getConstPool().getMethodrefType(a1);
                final int v2 = /*EL:381*/Descriptor.numOfParameters(v1) + 1;
                /*SL:382*/if (v2 > 3) {
                    /*SL:383*/n = iterator.insertGapAt(n, v2 - 3, false).position;
                }
                /*SL:385*/iterator.writeByte(87, n++);
                /*SL:386*/iterator.writeByte(0, n);
                /*SL:387*/iterator.writeByte(0, n + 1);
                final Descriptor.Iterator v3 = /*EL:388*/new Descriptor.Iterator(v1);
                while (true) {
                    /*SL:390*/v3.next();
                    /*SL:391*/if (!v3.isParameter()) {
                        break;
                    }
                    /*SL:392*/iterator.writeByte(v3.is2byte() ? 88 : 87, n++);
                }
            }
        }
        catch (BadBytecode a2) {
            /*SL:400*/throw new CannotCompileException(a2);
        }
    }
}
