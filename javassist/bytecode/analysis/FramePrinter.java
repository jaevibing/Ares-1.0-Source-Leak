package javassist.bytecode.analysis;

import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.Modifier;
import javassist.CtMethod;
import javassist.CtClass;
import java.io.PrintStream;

public final class FramePrinter
{
    private final PrintStream stream;
    
    public FramePrinter(final PrintStream a1) {
        this.stream = a1;
    }
    
    public static void print(final CtClass a1, final PrintStream a2) {
        /*SL:52*/new FramePrinter(a2).print(a1);
    }
    
    public void print(final CtClass v2) {
        final CtMethod[] v3 = /*EL:59*/v2.getDeclaredMethods();
        /*SL:60*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:61*/this.print(v3[a1]);
        }
    }
    
    private String getMethodString(final CtMethod v2) {
        try {
            /*SL:69*/return Modifier.toString(v2.getModifiers()) + " " + v2.getReturnType().getName() + " " + v2.getName() + Descriptor.toString(v2.getSignature()) + ";";
        }
        catch (NotFoundException a1) {
            /*SL:71*/throw new RuntimeException(a1);
        }
    }
    
    public void print(final CtMethod v-4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v-5 */
        //     1: getfield        javassist/bytecode/analysis/FramePrinter.stream:Ljava/io/PrintStream;
        //     4: new             Ljava/lang/StringBuilder;
        //     7: dup            
        //     8: invokespecial   java/lang/StringBuilder.<init>:()V
        //    11: ldc             "\n"
        //    13: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    16: aload_0         /* v-5 */
        //    17: aload_1         /* v-4 */
        //    18: invokespecial   javassist/bytecode/analysis/FramePrinter.getMethodString:(Ljavassist/CtMethod;)Ljava/lang/String;
        //    21: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    24: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    27: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //    30: aload_1         /* v-4 */
        //    31: invokevirtual   javassist/CtMethod.getMethodInfo2:()Ljavassist/bytecode/MethodInfo;
        //    34: astore_2        /* v-3 */
        //    35: aload_2         /* v-3 */
        //    36: invokevirtual   javassist/bytecode/MethodInfo.getConstPool:()Ljavassist/bytecode/ConstPool;
        //    39: astore_3        /* v-2 */
        //    40: aload_2         /* v-3 */
        //    41: invokevirtual   javassist/bytecode/MethodInfo.getCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //    44: astore          v-1
        //    46: aload           v-1
        //    48: ifnonnull       52
        //    51: return         
        //    52: new             Ljavassist/bytecode/analysis/Analyzer;
        //    55: dup            
        //    56: invokespecial   javassist/bytecode/analysis/Analyzer.<init>:()V
        //    59: aload_1         /* v-4 */
        //    60: invokevirtual   javassist/CtMethod.getDeclaringClass:()Ljavassist/CtClass;
        //    63: aload_2         /* v-3 */
        //    64: invokevirtual   javassist/bytecode/analysis/Analyzer.analyze:(Ljavassist/CtClass;Ljavassist/bytecode/MethodInfo;)[Ljavassist/bytecode/analysis/Frame;
        //    67: astore          a1
        //    69: goto            84
        //    72: astore          v1
        //    74: new             Ljava/lang/RuntimeException;
        //    77: dup            
        //    78: aload           v1
        //    80: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    83: athrow         
        //    84: aload           v-1
        //    86: invokevirtual   javassist/bytecode/CodeAttribute.getCodeLength:()I
        //    89: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //    92: invokevirtual   java/lang/String.length:()I
        //    95: istore          v1
        //    97: aload           v-1
        //    99: invokevirtual   javassist/bytecode/CodeAttribute.iterator:()Ljavassist/bytecode/CodeIterator;
        //   102: astore          v2
        //   104: aload           v2
        //   106: invokevirtual   javassist/bytecode/CodeIterator.hasNext:()Z
        //   109: ifeq            227
        //   112: aload           v2
        //   114: invokevirtual   javassist/bytecode/CodeIterator.next:()I
        //   117: istore          v3
        //   119: goto            134
        //   122: astore          v4
        //   124: new             Ljava/lang/RuntimeException;
        //   127: dup            
        //   128: aload           v4
        //   130: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   133: athrow         
        //   134: aload_0         /* v-5 */
        //   135: getfield        javassist/bytecode/analysis/FramePrinter.stream:Ljava/io/PrintStream;
        //   138: new             Ljava/lang/StringBuilder;
        //   141: dup            
        //   142: invokespecial   java/lang/StringBuilder.<init>:()V
        //   145: iload           v3
        //   147: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   150: ldc             ": "
        //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   155: aload           v2
        //   157: iload           v3
        //   159: aload_3         /* v-2 */
        //   160: invokestatic    javassist/bytecode/InstructionPrinter.instructionString:(Ljavassist/bytecode/CodeIterator;ILjavassist/bytecode/ConstPool;)Ljava/lang/String;
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   169: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   172: aload_0         /* v-5 */
        //   173: iload           v1
        //   175: iconst_3       
        //   176: iadd           
        //   177: invokespecial   javassist/bytecode/analysis/FramePrinter.addSpacing:(I)V
        //   180: aload           v0
        //   182: iload           v3
        //   184: aaload         
        //   185: astore          v4
        //   187: aload           v4
        //   189: ifnonnull       204
        //   192: aload_0         /* v-5 */
        //   193: getfield        javassist/bytecode/analysis/FramePrinter.stream:Ljava/io/PrintStream;
        //   196: ldc             "--DEAD CODE--"
        //   198: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   201: goto            104
        //   204: aload_0         /* v-5 */
        //   205: aload           v4
        //   207: invokespecial   javassist/bytecode/analysis/FramePrinter.printStack:(Ljavassist/bytecode/analysis/Frame;)V
        //   210: aload_0         /* v-5 */
        //   211: iload           v1
        //   213: iconst_3       
        //   214: iadd           
        //   215: invokespecial   javassist/bytecode/analysis/FramePrinter.addSpacing:(I)V
        //   218: aload_0         /* v-5 */
        //   219: aload           v4
        //   221: invokespecial   javassist/bytecode/analysis/FramePrinter.printLocals:(Ljavassist/bytecode/analysis/Frame;)V
        //   224: goto            104
        //   227: return         
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ------------------------------------------
        //  69     3       5     a1    [Ljavassist/bytecode/analysis/Frame;
        //  74     10      6     v1    Ljavassist/bytecode/BadBytecode;
        //  119    3       8     v3    I
        //  124    10      9     v4    Ljavassist/bytecode/BadBytecode;
        //  134    90      8     v3    I
        //  187    37      9     v4    Ljavassist/bytecode/analysis/Frame;
        //  0      228     0     v-5   Ljavassist/bytecode/analysis/FramePrinter;
        //  0      228     1     v-4   Ljavassist/CtMethod;
        //  35     193     2     v-3   Ljavassist/bytecode/MethodInfo;
        //  40     188     3     v-2   Ljavassist/bytecode/ConstPool;
        //  46     182     4     v-1   Ljavassist/bytecode/CodeAttribute;
        //  84     144     5     v0    [Ljavassist/bytecode/analysis/Frame;
        //  97     131     6     v1    I
        //  104    124     7     v2    Ljavassist/bytecode/CodeIterator;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  52     69     72     84     Ljavassist/bytecode/BadBytecode;
        //  112    119    122    134    Ljavassist/bytecode/BadBytecode;
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
    
    private void printStack(final Frame v-1) {
        /*SL:121*/this.stream.print("stack [");
        /*SL:123*/for (int v0 = v-1.getTopIndex(), v = 0; v <= v0; ++v) {
            /*SL:124*/if (v > 0) {
                /*SL:125*/this.stream.print(", ");
            }
            final Type a1 = /*EL:126*/v-1.getStack(v);
            /*SL:127*/this.stream.print(a1);
        }
        /*SL:129*/this.stream.println("]");
    }
    
    private void printLocals(final Frame v-1) {
        /*SL:133*/this.stream.print("locals [");
        /*SL:135*/for (int v0 = v-1.localsLength(), v = 0; v < v0; ++v) {
            /*SL:136*/if (v > 0) {
                /*SL:137*/this.stream.print(", ");
            }
            final Type a1 = /*EL:138*/v-1.getLocal(v);
            /*SL:139*/this.stream.print((a1 == null) ? "empty" : a1.toString());
        }
        /*SL:141*/this.stream.println("]");
    }
    
    private void addSpacing(int a1) {
        /*SL:145*/while (a1-- > 0) {
            /*SL:146*/this.stream.print(' ');
        }
    }
}
