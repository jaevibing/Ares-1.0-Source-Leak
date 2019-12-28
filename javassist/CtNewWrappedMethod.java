package javassist;

import javassist.compiler.JvstCodeGen;
import javassist.bytecode.BadBytecode;
import java.util.Hashtable;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.SyntheticAttribute;
import javassist.bytecode.AccessFlag;
import java.util.Map;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Bytecode;

class CtNewWrappedMethod
{
    private static final String addedWrappedMethod = "_added_m$";
    
    public static CtMethod wrapped(final CtClass a2, final String a3, final CtClass[] a4, final CtClass[] a5, final CtMethod a6, final CtMethod.ConstParameter a7, final CtClass v1) throws CannotCompileException {
        final CtMethod v2 = /*EL:35*/new CtMethod(a2, a3, a4, v1);
        /*SL:37*/v2.setModifiers(a6.getModifiers());
        try {
            /*SL:39*/v2.setExceptionTypes(a5);
        }
        catch (NotFoundException a8) {
            /*SL:42*/throw new CannotCompileException(a8);
        }
        final Bytecode v3 = makeBody(/*EL:45*/v1, v1.getClassFile2(), a6, a4, a2, a7);
        final MethodInfo v4 = /*EL:47*/v2.getMethodInfo2();
        /*SL:48*/v4.setCodeAttribute(v3.toCodeAttribute());
        /*SL:50*/return v2;
    }
    
    static Bytecode makeBody(final CtClass a1, final ClassFile a2, final CtMethod a3, final CtClass[] a4, final CtClass a5, final CtMethod.ConstParameter a6) throws CannotCompileException {
        final boolean v1 = /*EL:60*/Modifier.isStatic(a3.getModifiers());
        final Bytecode v2 = /*EL:61*/new Bytecode(a2.getConstPool(), 0, 0);
        final int v3 = makeBody0(/*EL:62*/a1, a2, a3, v1, a4, a5, a6, v2);
        /*SL:64*/v2.setMaxStack(v3);
        /*SL:65*/v2.setMaxLocals(v1, a4, 0);
        /*SL:66*/return v2;
    }
    
    protected static int makeBody0(final CtClass a5, final ClassFile a6, final CtMethod a7, final boolean a8, final CtClass[] v1, final CtClass v2, final CtMethod.ConstParameter v3, final Bytecode v4) throws CannotCompileException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* a5 */
        //     1: instanceof      Ljavassist/CtClassType;
        //     4: ifne            37
        //     7: new             Ljavassist/CannotCompileException;
        //    10: dup            
        //    11: new             Ljava/lang/StringBuilder;
        //    14: dup            
        //    15: invokespecial   java/lang/StringBuilder.<init>:()V
        //    18: ldc             "bad declaring class"
        //    20: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    23: aload_0         /* a5 */
        //    24: invokevirtual   javassist/CtClass.getName:()Ljava/lang/String;
        //    27: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    30: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    33: invokespecial   javassist/CannotCompileException.<init>:(Ljava/lang/String;)V
        //    36: athrow         
        //    37: iload_3         /* a8 */
        //    38: ifne            47
        //    41: aload           v4
        //    43: iconst_0       
        //    44: invokevirtual   javassist/bytecode/Bytecode.addAload:(I)V
        //    47: aload           v4
        //    49: aload           v1
        //    51: iload_3         /* a8 */
        //    52: ifeq            59
        //    55: iconst_0       
        //    56: goto            60
        //    59: iconst_1       
        //    60: invokestatic    javassist/CtNewWrappedMethod.compileParameterList:(Ljavassist/bytecode/Bytecode;[Ljavassist/CtClass;I)I
        //    63: istore          v5
        //    65: aload           v3
        //    67: ifnonnull       81
        //    70: iconst_0       
        //    71: istore          a1
        //    73: invokestatic    javassist/CtMethod$ConstParameter.defaultDescriptor:()Ljava/lang/String;
        //    76: astore          a2
        //    78: goto            97
        //    81: aload           v3
        //    83: aload           v4
        //    85: invokevirtual   javassist/CtMethod$ConstParameter.compile:(Ljavassist/bytecode/Bytecode;)I
        //    88: istore          v6
        //    90: aload           v3
        //    92: invokevirtual   javassist/CtMethod$ConstParameter.descriptor:()Ljava/lang/String;
        //    95: astore          v7
        //    97: aload_2         /* a7 */
        //    98: aload           v7
        //   100: invokestatic    javassist/CtNewWrappedMethod.checkSignature:(Ljavassist/CtMethod;Ljava/lang/String;)V
        //   103: aload_0         /* a5 */
        //   104: checkcast       Ljavassist/CtClassType;
        //   107: aload_1         /* a6 */
        //   108: aload_2         /* a7 */
        //   109: invokestatic    javassist/CtNewWrappedMethod.addBodyMethod:(Ljavassist/CtClassType;Ljavassist/bytecode/ClassFile;Ljavassist/CtMethod;)Ljava/lang/String;
        //   112: astore          a3
        //   114: goto            129
        //   117: astore          a4
        //   119: new             Ljavassist/CannotCompileException;
        //   122: dup            
        //   123: aload           a4
        //   125: invokespecial   javassist/CannotCompileException.<init>:(Ljava/lang/Throwable;)V
        //   128: athrow         
        //   129: iload_3         /* a8 */
        //   130: ifeq            148
        //   133: aload           v4
        //   135: getstatic       javassist/bytecode/Bytecode.THIS:Ljavassist/CtClass;
        //   138: aload           v8
        //   140: aload           v7
        //   142: invokevirtual   javassist/bytecode/Bytecode.addInvokestatic:(Ljavassist/CtClass;Ljava/lang/String;Ljava/lang/String;)V
        //   145: goto            160
        //   148: aload           v4
        //   150: getstatic       javassist/bytecode/Bytecode.THIS:Ljavassist/CtClass;
        //   153: aload           v8
        //   155: aload           v7
        //   157: invokevirtual   javassist/bytecode/Bytecode.addInvokespecial:(Ljavassist/CtClass;Ljava/lang/String;Ljava/lang/String;)V
        //   160: aload           v4
        //   162: aload           v2
        //   164: invokestatic    javassist/CtNewWrappedMethod.compileReturn:(Ljavassist/bytecode/Bytecode;Ljavassist/CtClass;)V
        //   167: iload           v5
        //   169: iload           v6
        //   171: iconst_2       
        //   172: iadd           
        //   173: if_icmpge       182
        //   176: iload           v6
        //   178: iconst_2       
        //   179: iadd           
        //   180: istore          v5
        //   182: iload           v5
        //   184: ireturn        
        //    Exceptions:
        //  throws javassist.CannotCompileException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -----------------------------------
        //  73     8       9     a1    I
        //  78     3       10    a2    Ljava/lang/String;
        //  114    3       11    a3    Ljava/lang/String;
        //  119    10      12    a4    Ljavassist/bytecode/BadBytecode;
        //  0      185     0     a5    Ljavassist/CtClass;
        //  0      185     1     a6    Ljavassist/bytecode/ClassFile;
        //  0      185     2     a7    Ljavassist/CtMethod;
        //  0      185     3     a8    Z
        //  0      185     4     v1    [Ljavassist/CtClass;
        //  0      185     5     v2    Ljavassist/CtClass;
        //  0      185     6     v3    Ljavassist/CtMethod$ConstParameter;
        //  0      185     7     v4    Ljavassist/bytecode/Bytecode;
        //  65     120     8     v5    I
        //  90     95      9     v6    I
        //  97     88      10    v7    Ljava/lang/String;
        //  129    56      11    v8    Ljava/lang/String;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  103    114    117    129    Ljavassist/bytecode/BadBytecode;
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
    
    private static void checkSignature(final CtMethod a1, final String a2) throws CannotCompileException {
        /*SL:130*/if (!a2.equals(a1.getMethodInfo2().getDescriptor())) {
            /*SL:131*/throw new CannotCompileException("wrapped method with a bad signature: " + a1.getDeclaringClass().getName() + /*EL:133*/'.' + a1.getName());
        }
    }
    
    private static String addBodyMethod(final CtClassType v-7, final ClassFile v-6, final CtMethod v-5) throws BadBytecode, CannotCompileException {
        final Hashtable hiddenMethods = /*EL:142*/v-7.getHiddenMethods();
        String string = /*EL:143*/hiddenMethods.get(v-5);
        /*SL:144*/if (string == null) {
            /*SL:147*/do {
                string = "_added_m$" + v-7.getUniqueNumber();
            } while (v-6.getMethod(string) != null);
            final ClassMap a1 = /*EL:148*/new ClassMap();
            /*SL:149*/a1.put(v-5.getDeclaringClass().getName(), v-7.getName());
            final MethodInfo a2 = /*EL:151*/new MethodInfo(v-6.getConstPool(), string, v-5.getMethodInfo2(), a1);
            final int a3 = /*EL:153*/a2.getAccessFlags();
            /*SL:154*/a2.setAccessFlags(AccessFlag.setPrivate(a3));
            /*SL:155*/a2.addAttribute(new SyntheticAttribute(v-6.getConstPool()));
            /*SL:157*/v-6.addMethod(a2);
            /*SL:158*/hiddenMethods.put(v-5, string);
            final CtMember.Cache v1 = /*EL:159*/v-7.hasMemberCache();
            /*SL:160*/if (v1 != null) {
                /*SL:161*/v1.addMethod(new CtMethod(a2, v-7));
            }
        }
        /*SL:164*/return string;
    }
    
    static int compileParameterList(final Bytecode a1, final CtClass[] a2, final int a3) {
        /*SL:176*/return JvstCodeGen.compileParameterList(a1, a2, a3);
    }
    
    private static void compileReturn(final Bytecode v1, final CtClass v2) {
        /*SL:183*/if (v2.isPrimitive()) {
            CtPrimitiveType a2 = /*EL:184*/(CtPrimitiveType)v2;
            /*SL:185*/if (a2 != CtClass.voidType) {
                /*SL:186*/a2 = a2.getWrapperName();
                /*SL:187*/v1.addCheckcast(a2);
                /*SL:188*/v1.addInvokevirtual(a2, a2.getGetMethodName(), a2.getGetMethodDescriptor());
            }
            /*SL:192*/v1.addOpcode(a2.getReturnOp());
        }
        else {
            /*SL:195*/v1.addCheckcast(v2);
            /*SL:196*/v1.addOpcode(176);
        }
    }
}
