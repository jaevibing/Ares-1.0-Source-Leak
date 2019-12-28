package javassist;

import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public final class CtMethod extends CtBehavior
{
    protected String cachedStringRep;
    
    CtMethod(final MethodInfo a1, final CtClass a2) {
        super(a2, a1);
        this.cachedStringRep = null;
    }
    
    public CtMethod(final CtClass a1, final String a2, final CtClass[] a3, final CtClass a4) {
        this(null, a4);
        final ConstPool v1 = a4.getClassFile2().getConstPool();
        final String v2 = Descriptor.ofMethod(a1, a3);
        this.methodInfo = new MethodInfo(v1, a2, v2);
        this.setModifiers(1025);
    }
    
    public CtMethod(final CtMethod a1, final CtClass a2, final ClassMap a3) throws CannotCompileException {
        this(null, a2);
        this.copy(a1, false, a3);
    }
    
    public static CtMethod make(final String a1, final CtClass a2) throws CannotCompileException {
        /*SL:132*/return CtNewMethod.make(a1, a2);
    }
    
    public static CtMethod make(final MethodInfo a1, final CtClass a2) throws CannotCompileException {
        /*SL:147*/if (a2.getClassFile2().getConstPool() != a1.getConstPool()) {
            /*SL:148*/throw new CannotCompileException("bad declaring class");
        }
        /*SL:150*/return new CtMethod(a1, a2);
    }
    
    @Override
    public int hashCode() {
        /*SL:159*/return this.getStringRep().hashCode();
    }
    
    @Override
    void nameReplaced() {
        /*SL:167*/this.cachedStringRep = null;
    }
    
    final String getStringRep() {
        /*SL:173*/if (this.cachedStringRep == null) {
            /*SL:175*/this.cachedStringRep = this.methodInfo.getName() + Descriptor.getParamDescriptor(this.methodInfo.getDescriptor());
        }
        /*SL:177*/return this.cachedStringRep;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:186*/return a1 != null && a1 instanceof CtMethod && ((CtMethod)a1).getStringRep().equals(this.getStringRep());
    }
    
    @Override
    public String getLongName() {
        /*SL:197*/return this.getDeclaringClass().getName() + "." + this.getName() + Descriptor.toString(this.getSignature());
    }
    
    @Override
    public String getName() {
        /*SL:204*/return this.methodInfo.getName();
    }
    
    public void setName(final String a1) {
        /*SL:211*/this.declaringClass.checkModify();
        /*SL:212*/this.methodInfo.setName(a1);
    }
    
    public CtClass getReturnType() throws NotFoundException {
        /*SL:219*/return this.getReturnType0();
    }
    
    @Override
    public boolean isEmpty() {
        final CodeAttribute v1 = /*EL:227*/this.getMethodInfo2().getCodeAttribute();
        /*SL:228*/if (v1 == null) {
            /*SL:229*/return (this.getModifiers() & 0x400) != 0x0;
        }
        final CodeIterator v2 = /*EL:231*/v1.iterator();
        try {
            /*SL:234*/return v2.hasNext() && v2.byteAt(v2.next()) == 177 && !v2.hasNext();
        }
        catch (BadBytecode badBytecode) {
            /*SL:237*/return false;
        }
    }
    
    public void setBody(final CtMethod a1, final ClassMap a2) throws CannotCompileException {
        /*SL:257*/CtBehavior.setBody0(a1.declaringClass, a1.methodInfo, this.declaringClass, this.methodInfo, a2);
    }
    
    public void setWrappedBody(final CtMethod v-4, final ConstParameter v-3) throws CannotCompileException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v-5 */
        //     1: getfield        javassist/CtMethod.declaringClass:Ljavassist/CtClass;
        //     4: invokevirtual   javassist/CtClass.checkModify:()V
        //     7: aload_0         /* v-5 */
        //     8: invokevirtual   javassist/CtMethod.getDeclaringClass:()Ljavassist/CtClass;
        //    11: astore_3        /* v-2 */
        //    12: aload_0         /* v-5 */
        //    13: invokevirtual   javassist/CtMethod.getParameterTypes:()[Ljavassist/CtClass;
        //    16: astore          a1
        //    18: aload_0         /* v-5 */
        //    19: invokevirtual   javassist/CtMethod.getReturnType:()Ljavassist/CtClass;
        //    22: astore          a2
        //    24: goto            39
        //    27: astore          v1
        //    29: new             Ljavassist/CannotCompileException;
        //    32: dup            
        //    33: aload           v1
        //    35: invokespecial   javassist/CannotCompileException.<init>:(Ljavassist/NotFoundException;)V
        //    38: athrow         
        //    39: aload_3         /* v-2 */
        //    40: aload_3         /* v-2 */
        //    41: invokevirtual   javassist/CtClass.getClassFile2:()Ljavassist/bytecode/ClassFile;
        //    44: aload_1         /* v-4 */
        //    45: aload           v-1
        //    47: aload           v0
        //    49: aload_2         /* v-3 */
        //    50: invokestatic    javassist/CtNewWrappedMethod.makeBody:(Ljavassist/CtClass;Ljavassist/bytecode/ClassFile;Ljavassist/CtMethod;[Ljavassist/CtClass;Ljavassist/CtClass;Ljavassist/CtMethod$ConstParameter;)Ljavassist/bytecode/Bytecode;
        //    53: astore          v1
        //    55: aload           v1
        //    57: invokevirtual   javassist/bytecode/Bytecode.toCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //    60: astore          v2
        //    62: aload_0         /* v-5 */
        //    63: getfield        javassist/CtMethod.methodInfo:Ljavassist/bytecode/MethodInfo;
        //    66: aload           v2
        //    68: invokevirtual   javassist/bytecode/MethodInfo.setCodeAttribute:(Ljavassist/bytecode/CodeAttribute;)V
        //    71: aload_0         /* v-5 */
        //    72: getfield        javassist/CtMethod.methodInfo:Ljavassist/bytecode/MethodInfo;
        //    75: aload_0         /* v-5 */
        //    76: getfield        javassist/CtMethod.methodInfo:Ljavassist/bytecode/MethodInfo;
        //    79: invokevirtual   javassist/bytecode/MethodInfo.getAccessFlags:()I
        //    82: sipush          -1025
        //    85: iand           
        //    86: invokevirtual   javassist/bytecode/MethodInfo.setAccessFlags:(I)V
        //    89: return         
        //    Exceptions:
        //  throws javassist.CannotCompileException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -----------------------------------
        //  18     9       4     a1    [Ljavassist/CtClass;
        //  24     3       5     a2    Ljavassist/CtClass;
        //  29     10      6     v1    Ljavassist/NotFoundException;
        //  0      90      0     v-5   Ljavassist/CtMethod;
        //  0      90      1     v-4   Ljavassist/CtMethod;
        //  0      90      2     v-3   Ljavassist/CtMethod$ConstParameter;
        //  12     78      3     v-2   Ljavassist/CtClass;
        //  39     51      4     v-1   [Ljavassist/CtClass;
        //  39     51      5     v0    Ljavassist/CtClass;
        //  55     35      6     v1    Ljavassist/bytecode/Bytecode;
        //  62     28      7     v2    Ljavassist/bytecode/CodeAttribute;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  12     24     27     39     Ljavassist/NotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2987)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static class ConstParameter
    {
        public static ConstParameter integer(final int a1) {
            /*SL:318*/return new IntConstParameter(a1);
        }
        
        public static ConstParameter integer(final long a1) {
            /*SL:327*/return new LongConstParameter(a1);
        }
        
        public static ConstParameter string(final String a1) {
            /*SL:336*/return new StringConstParameter(a1);
        }
        
        int compile(final Bytecode a1) throws CannotCompileException {
            /*SL:345*/return 0;
        }
        
        String descriptor() {
            /*SL:349*/return defaultDescriptor();
        }
        
        static String defaultDescriptor() {
            /*SL:356*/return "([Ljava/lang/Object;)Ljava/lang/Object;";
        }
        
        String constDescriptor() {
            /*SL:365*/return defaultConstDescriptor();
        }
        
        static String defaultConstDescriptor() {
            /*SL:372*/return "([Ljava/lang/Object;)V";
        }
    }
    
    static class IntConstParameter extends ConstParameter
    {
        int param;
        
        IntConstParameter(final int a1) {
            this.param = a1;
        }
        
        @Override
        int compile(final Bytecode a1) throws CannotCompileException {
            /*SL:384*/a1.addIconst(this.param);
            /*SL:385*/return 1;
        }
        
        @Override
        String descriptor() {
            /*SL:389*/return "([Ljava/lang/Object;I)Ljava/lang/Object;";
        }
        
        @Override
        String constDescriptor() {
            /*SL:393*/return "([Ljava/lang/Object;I)V";
        }
    }
    
    static class LongConstParameter extends ConstParameter
    {
        long param;
        
        LongConstParameter(final long a1) {
            this.param = a1;
        }
        
        @Override
        int compile(final Bytecode a1) throws CannotCompileException {
            /*SL:405*/a1.addLconst(this.param);
            /*SL:406*/return 2;
        }
        
        @Override
        String descriptor() {
            /*SL:410*/return "([Ljava/lang/Object;J)Ljava/lang/Object;";
        }
        
        @Override
        String constDescriptor() {
            /*SL:414*/return "([Ljava/lang/Object;J)V";
        }
    }
    
    static class StringConstParameter extends ConstParameter
    {
        String param;
        
        StringConstParameter(final String a1) {
            this.param = a1;
        }
        
        @Override
        int compile(final Bytecode a1) throws CannotCompileException {
            /*SL:426*/a1.addLdc(this.param);
            /*SL:427*/return 1;
        }
        
        @Override
        String descriptor() {
            /*SL:431*/return "([Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;";
        }
        
        @Override
        String constDescriptor() {
            /*SL:435*/return "([Ljava/lang/Object;Ljava/lang/String;)V";
        }
    }
}
