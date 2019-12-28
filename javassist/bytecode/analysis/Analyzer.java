package javassist.bytecode.analysis;

import java.util.Iterator;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.Opcode;

public class Analyzer implements Opcode
{
    private final SubroutineScanner scanner;
    private CtClass clazz;
    private ExceptionInfo[] exceptions;
    private Frame[] frames;
    private Subroutine[] subroutines;
    
    public Analyzer() {
        this.scanner = new SubroutineScanner();
    }
    
    public Frame[] analyze(final CtClass a1, final MethodInfo a2) throws BadBytecode {
        /*SL:123*/this.clazz = a1;
        final CodeAttribute v1 = /*EL:124*/a2.getCodeAttribute();
        /*SL:126*/if (v1 == null) {
            /*SL:127*/return null;
        }
        final int v2 = /*EL:129*/v1.getMaxLocals();
        final int v3 = /*EL:130*/v1.getMaxStack();
        final int v4 = /*EL:131*/v1.getCodeLength();
        final CodeIterator v5 = /*EL:133*/v1.iterator();
        final IntQueue v6 = /*EL:134*/new IntQueue();
        /*SL:136*/this.exceptions = this.buildExceptionInfo(a2);
        /*SL:137*/this.subroutines = this.scanner.scan(a2);
        final Executor v7 = /*EL:139*/new Executor(a1.getClassPool(), a2.getConstPool());
        /*SL:141*/(this.frames = new Frame[v4])[v5.lookAhead()] = this.firstFrame(a2, v2, v3);
        /*SL:142*/v6.add(v5.next());
        /*SL:143*/while (!v6.isEmpty()) {
            /*SL:144*/this.analyzeNextEntry(a2, v5, v6, v7);
        }
        /*SL:147*/return this.frames;
    }
    
    public Frame[] analyze(final CtMethod a1) throws BadBytecode {
        /*SL:165*/return this.analyze(a1.getDeclaringClass(), a1.getMethodInfo2());
    }
    
    private void analyzeNextEntry(final MethodInfo a4, final CodeIterator v1, final IntQueue v2, final Executor v3) throws BadBytecode {
        final int v4 = /*EL:170*/v2.take();
        /*SL:171*/v1.move(v4);
        /*SL:172*/v1.next();
        final Frame v5 = /*EL:174*/this.frames[v4].copy();
        final Subroutine v6 = /*EL:175*/this.subroutines[v4];
        try {
            /*SL:178*/v3.execute(a4, v4, v1, v5, v6);
        }
        catch (RuntimeException a5) {
            /*SL:180*/throw new BadBytecode(a5.getMessage() + "[pos = " + v4 + "]", a5);
        }
        final int v7 = /*EL:183*/v1.byteAt(v4);
        /*SL:185*/if (v7 == 170) {
            /*SL:186*/this.mergeTableSwitch(v2, v4, v1, v5);
        }
        else/*SL:187*/ if (v7 == 171) {
            /*SL:188*/this.mergeLookupSwitch(v2, v4, v1, v5);
        }
        else/*SL:189*/ if (v7 == 169) {
            /*SL:190*/this.mergeRet(v2, v1, v4, v5, v6);
        }
        else/*SL:191*/ if (Util.isJumpInstruction(v7)) {
            final int a6 = /*EL:192*/Util.getJumpTarget(v4, v1);
            /*SL:194*/if (Util.isJsr(v7)) {
                /*SL:196*/this.mergeJsr(v2, this.frames[v4], this.subroutines[a6], v4, this.lookAhead(v1, v4));
            }
            else/*SL:197*/ if (!Util.isGoto(v7)) {
                /*SL:198*/this.merge(v2, v5, this.lookAhead(v1, v4));
            }
            /*SL:201*/this.merge(v2, v5, a6);
        }
        else/*SL:202*/ if (v7 != 191 && !Util.isReturn(v7)) {
            /*SL:204*/this.merge(v2, v5, this.lookAhead(v1, v4));
        }
        /*SL:210*/this.mergeExceptionHandlers(v2, a4, v4, v5);
    }
    
    private ExceptionInfo[] buildExceptionInfo(final MethodInfo v-7) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-7 */
        //     1: invokevirtual   javassist/bytecode/MethodInfo.getConstPool:()Ljavassist/bytecode/ConstPool;
        //     4: astore_2        /* v-6 */
        //     5: aload_0         /* v-8 */
        //     6: getfield        javassist/bytecode/analysis/Analyzer.clazz:Ljavassist/CtClass;
        //     9: invokevirtual   javassist/CtClass.getClassPool:()Ljavassist/ClassPool;
        //    12: astore_3        /* v-5 */
        //    13: aload_1         /* v-7 */
        //    14: invokevirtual   javassist/bytecode/MethodInfo.getCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //    17: invokevirtual   javassist/bytecode/CodeAttribute.getExceptionTable:()Ljavassist/bytecode/ExceptionTable;
        //    20: astore          v-4
        //    22: aload           v-4
        //    24: invokevirtual   javassist/bytecode/ExceptionTable.size:()I
        //    27: anewarray       Ljavassist/bytecode/analysis/Analyzer$ExceptionInfo;
        //    30: astore          v-3
        //    32: iconst_0       
        //    33: istore          v-2
        //    35: iload           v-2
        //    37: aload           v-4
        //    39: invokevirtual   javassist/bytecode/ExceptionTable.size:()I
        //    42: if_icmpge       140
        //    45: aload           v-4
        //    47: iload           v-2
        //    49: invokevirtual   javassist/bytecode/ExceptionTable.catchType:(I)I
        //    52: istore          v-1
        //    54: iload           v-1
        //    56: ifne            65
        //    59: getstatic       javassist/bytecode/analysis/Type.THROWABLE:Ljavassist/bytecode/analysis/Type;
        //    62: goto            78
        //    65: aload_3         /* v-5 */
        //    66: aload_2         /* v-6 */
        //    67: iload           v-1
        //    69: invokevirtual   javassist/bytecode/ConstPool.getClassInfo:(I)Ljava/lang/String;
        //    72: invokevirtual   javassist/ClassPool.get:(Ljava/lang/String;)Ljavassist/CtClass;
        //    75: invokestatic    javassist/bytecode/analysis/Type.get:(Ljavassist/CtClass;)Ljavassist/bytecode/analysis/Type;
        //    78: astore          a1
        //    80: goto            98
        //    83: astore          v1
        //    85: new             Ljava/lang/IllegalStateException;
        //    88: dup            
        //    89: aload           v1
        //    91: invokevirtual   javassist/NotFoundException.getMessage:()Ljava/lang/String;
        //    94: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    97: athrow         
        //    98: aload           v-3
        //   100: iload           v-2
        //   102: new             Ljavassist/bytecode/analysis/Analyzer$ExceptionInfo;
        //   105: dup            
        //   106: aload           v-4
        //   108: iload           v-2
        //   110: invokevirtual   javassist/bytecode/ExceptionTable.startPc:(I)I
        //   113: aload           v-4
        //   115: iload           v-2
        //   117: invokevirtual   javassist/bytecode/ExceptionTable.endPc:(I)I
        //   120: aload           v-4
        //   122: iload           v-2
        //   124: invokevirtual   javassist/bytecode/ExceptionTable.handlerPc:(I)I
        //   127: aload           v0
        //   129: aconst_null    
        //   130: invokespecial   javassist/bytecode/analysis/Analyzer$ExceptionInfo.<init>:(IIILjavassist/bytecode/analysis/Type;Ljavassist/bytecode/analysis/Analyzer$1;)V
        //   133: aastore        
        //   134: iinc            v-2, 1
        //   137: goto            35
        //   140: aload           v-3
        //   142: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -----------------------------------------------------
        //  80     3       8     a1    Ljavassist/bytecode/analysis/Type;
        //  85     13      9     v1    Ljavassist/NotFoundException;
        //  54     80      7     v-1   I
        //  98     36      8     v0    Ljavassist/bytecode/analysis/Type;
        //  35     105     6     v-2   I
        //  0      143     0     v-8   Ljavassist/bytecode/analysis/Analyzer;
        //  0      143     1     v-7   Ljavassist/bytecode/MethodInfo;
        //  5      138     2     v-6   Ljavassist/bytecode/ConstPool;
        //  13     130     3     v-5   Ljavassist/ClassPool;
        //  22     121     4     v-4   Ljavassist/bytecode/ExceptionTable;
        //  32     111     5     v-3   [Ljavassist/bytecode/analysis/Analyzer$ExceptionInfo;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  54     80     83     98     Ljavassist/NotFoundException;
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
    
    private Frame firstFrame(final MethodInfo v-5, final int v-4, final int v-3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_0       
        //     1: istore          v-2
        //     3: new             Ljavassist/bytecode/analysis/Frame;
        //     6: dup            
        //     7: iload_2         /* v-4 */
        //     8: iload_3         /* v-3 */
        //     9: invokespecial   javassist/bytecode/analysis/Frame.<init>:(II)V
        //    12: astore          v-1
        //    14: aload_1         /* v-5 */
        //    15: invokevirtual   javassist/bytecode/MethodInfo.getAccessFlags:()I
        //    18: bipush          8
        //    20: iand           
        //    21: ifne            41
        //    24: aload           v-1
        //    26: iload           v-2
        //    28: iinc            v-2, 1
        //    31: aload_0         /* v-6 */
        //    32: getfield        javassist/bytecode/analysis/Analyzer.clazz:Ljavassist/CtClass;
        //    35: invokestatic    javassist/bytecode/analysis/Type.get:(Ljavassist/CtClass;)Ljavassist/bytecode/analysis/Type;
        //    38: invokevirtual   javassist/bytecode/analysis/Frame.setLocal:(ILjavassist/bytecode/analysis/Type;)V
        //    41: aload_1         /* v-5 */
        //    42: invokevirtual   javassist/bytecode/MethodInfo.getDescriptor:()Ljava/lang/String;
        //    45: aload_0         /* v-6 */
        //    46: getfield        javassist/bytecode/analysis/Analyzer.clazz:Ljavassist/CtClass;
        //    49: invokevirtual   javassist/CtClass.getClassPool:()Ljavassist/ClassPool;
        //    52: invokestatic    javassist/bytecode/Descriptor.getParameterTypes:(Ljava/lang/String;Ljavassist/ClassPool;)[Ljavassist/CtClass;
        //    55: astore          a1
        //    57: goto            72
        //    60: astore          a2
        //    62: new             Ljava/lang/RuntimeException;
        //    65: dup            
        //    66: aload           a2
        //    68: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    71: athrow         
        //    72: iconst_0       
        //    73: istore          v1
        //    75: iload           v1
        //    77: aload           v0
        //    79: arraylength    
        //    80: if_icmpge       137
        //    83: aload_0         /* v-6 */
        //    84: aload           v0
        //    86: iload           v1
        //    88: aaload         
        //    89: invokestatic    javassist/bytecode/analysis/Type.get:(Ljavassist/CtClass;)Ljavassist/bytecode/analysis/Type;
        //    92: invokespecial   javassist/bytecode/analysis/Analyzer.zeroExtend:(Ljavassist/bytecode/analysis/Type;)Ljavassist/bytecode/analysis/Type;
        //    95: astore          a3
        //    97: aload           v-1
        //    99: iload           v-2
        //   101: iinc            v-2, 1
        //   104: aload           a3
        //   106: invokevirtual   javassist/bytecode/analysis/Frame.setLocal:(ILjavassist/bytecode/analysis/Type;)V
        //   109: aload           a3
        //   111: invokevirtual   javassist/bytecode/analysis/Type.getSize:()I
        //   114: iconst_2       
        //   115: if_icmpne       131
        //   118: aload           v-1
        //   120: iload           v-2
        //   122: iinc            v-2, 1
        //   125: getstatic       javassist/bytecode/analysis/Type.TOP:Ljavassist/bytecode/analysis/Type;
        //   128: invokevirtual   javassist/bytecode/analysis/Frame.setLocal:(ILjavassist/bytecode/analysis/Type;)V
        //   131: iinc            v1, 1
        //   134: goto            75
        //   137: aload           v-1
        //   139: areturn        
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  --------------------------------------
        //  57     3       6     a1    [Ljavassist/CtClass;
        //  62     10      7     a2    Ljavassist/NotFoundException;
        //  97     34      8     a3    Ljavassist/bytecode/analysis/Type;
        //  75     62      7     v1    I
        //  0      140     0     v-6   Ljavassist/bytecode/analysis/Analyzer;
        //  0      140     1     v-5   Ljavassist/bytecode/MethodInfo;
        //  0      140     2     v-4   I
        //  0      140     3     v-3   I
        //  3      137     4     v-2   I
        //  14     126     5     v-1   Ljavassist/bytecode/analysis/Frame;
        //  72     68      6     v0    [Ljavassist/CtClass;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  41     57     60     72     Ljavassist/NotFoundException;
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
    
    private int getNext(final CodeIterator a1, final int a2, final int a3) throws BadBytecode {
        /*SL:260*/a1.move(a2);
        /*SL:261*/a1.next();
        final int v1 = /*EL:262*/a1.lookAhead();
        /*SL:263*/a1.move(a3);
        /*SL:264*/a1.next();
        /*SL:266*/return v1;
    }
    
    private int lookAhead(final CodeIterator a1, final int a2) throws BadBytecode {
        /*SL:270*/if (!a1.hasNext()) {
            /*SL:271*/throw new BadBytecode("Execution falls off end! [pos = " + a2 + "]");
        }
        /*SL:273*/return a1.lookAhead();
    }
    
    private void merge(final IntQueue a3, final Frame v1, final int v2) {
        final Frame v3 = /*EL:278*/this.frames[v2];
        final boolean v4;
        /*SL:281*/if (v3 == null) {
            /*SL:282*/this.frames[v2] = v1.copy();
            final boolean a4 = /*EL:283*/true;
        }
        else {
            /*SL:285*/v4 = v3.merge(v1);
        }
        /*SL:288*/if (v4) {
            /*SL:289*/a3.add(v2);
        }
    }
    
    private void mergeExceptionHandlers(final IntQueue v1, final MethodInfo v2, final int v3, final Frame v4) {
        /*SL:294*/for (Frame a3 = (Frame)0; a3 < this.exceptions.length; ++a3) {
            final ExceptionInfo a2 = /*EL:295*/this.exceptions[a3];
            /*SL:298*/if (v3 >= a2.start && v3 < a2.end) {
                /*SL:299*/a3 = v4.copy();
                /*SL:300*/a3.clearStack();
                /*SL:301*/a3.push(a2.type);
                /*SL:302*/this.merge(v1, a3, a2.handler);
            }
        }
    }
    
    private void mergeJsr(final IntQueue a5, final Frame v1, final Subroutine v2, final int v3, final int v4) throws BadBytecode {
        /*SL:308*/if (v2 == null) {
            /*SL:309*/throw new BadBytecode("No subroutine at jsr target! [pos = " + v3 + "]");
        }
        Frame v5 = /*EL:311*/this.frames[v4];
        boolean v6 = /*EL:312*/false;
        /*SL:314*/if (v5 == null) {
            final Frame[] frames = /*EL:315*/this.frames;
            final Frame copy = v1.copy();
            frames[v4] = copy;
            v5 = copy;
            /*SL:316*/v6 = true;
        }
        else {
            /*SL:318*/for (int a6 = 0; a6 < v1.localsLength(); ++a6) {
                /*SL:320*/if (!v2.isAccessed(a6)) {
                    final Type a7 = /*EL:321*/v5.getLocal(a6);
                    Type a8 = /*EL:322*/v1.getLocal(a6);
                    /*SL:323*/if (a7 == null) {
                        /*SL:324*/v5.setLocal(a6, a8);
                        /*SL:325*/v6 = true;
                    }
                    else {
                        /*SL:329*/a8 = a7.merge(a8);
                        /*SL:331*/v5.setLocal(a6, a8);
                        /*SL:332*/if (!a8.equals(a7) || a8.popChanged()) {
                            /*SL:333*/v6 = true;
                        }
                    }
                }
            }
        }
        /*SL:338*/if (!v5.isJsrMerged()) {
            /*SL:339*/v5.setJsrMerged(true);
            /*SL:340*/v6 = true;
        }
        /*SL:343*/if (v6 && v5.isRetMerged()) {
            /*SL:344*/a5.add(v4);
        }
    }
    
    private void mergeLookupSwitch(final IntQueue a3, final int a4, final CodeIterator v1, final Frame v2) throws BadBytecode {
        int v3 = /*EL:349*/(a4 & 0xFFFFFFFC) + 4;
        /*SL:351*/this.merge(a3, v2, a4 + v1.s32bitAt(v3));
        /*SL:352*/v3 += 4;
        final int v4 = v1.s32bitAt(v3);
        final int n = /*EL:353*/v4 * 8;
        /*SL:356*/for (v3 += 4, final int v5 = n + v3, v3 += 4; v3 < v5; v3 += 8) {
            final int a5 = /*EL:357*/v1.s32bitAt(v3) + a4;
            /*SL:358*/this.merge(a3, v2, a5);
        }
    }
    
    private void mergeRet(final IntQueue v-6, final CodeIterator v-5, final int v-4, final Frame v-3, final Subroutine v-2) throws BadBytecode {
        /*SL:363*/if (v-2 == null) {
            /*SL:364*/throw new BadBytecode("Ret on no subroutine! [pos = " + v-4 + "]");
        }
        /*SL:366*/for (Type a5 : v-2.callers()) {
            final int v1 = /*EL:369*/this.getNext(v-5, a5, v-4);
            boolean v2 = /*EL:370*/false;
            Frame v3 = /*EL:372*/this.frames[v1];
            /*SL:373*/if (v3 == null) {
                final Frame[] frames = /*EL:374*/this.frames;
                final int n = v1;
                final Frame copyStack = v-3.copyStack();
                frames[n] = copyStack;
                v3 = copyStack;
                /*SL:375*/v2 = true;
            }
            else {
                /*SL:377*/v2 = v3.mergeStack(v-3);
            }
            /*SL:380*/for (final int a3 : v-2.accessed()) {
                final Type a4 = /*EL:382*/v3.getLocal(a3);
                /*SL:383*/a5 = v-3.getLocal(a3);
                /*SL:384*/if (a4 != a5) {
                    /*SL:385*/v3.setLocal(a3, a5);
                    /*SL:386*/v2 = true;
                }
            }
            /*SL:390*/if (!v3.isRetMerged()) {
                /*SL:391*/v3.setRetMerged(true);
                /*SL:392*/v2 = true;
            }
            /*SL:395*/if (v2 && v3.isJsrMerged()) {
                /*SL:396*/v-6.add(v1);
            }
        }
    }
    
    private void mergeTableSwitch(final IntQueue a3, final int a4, final CodeIterator v1, final Frame v2) throws BadBytecode {
        int v3 = /*EL:403*/(a4 & 0xFFFFFFFC) + 4;
        /*SL:405*/this.merge(a3, v2, a4 + v1.s32bitAt(v3));
        /*SL:406*/v3 += 4;
        final int v4 = v1.s32bitAt(v3);
        /*SL:407*/v3 += 4;
        final int v5 = v1.s32bitAt(v3);
        final int n = /*EL:408*/(v5 - v4 + 1) * 4;
        v3 += 4;
        /*SL:411*/for (int v6 = n + v3; v3 < v6; v3 += 4) {
            final int a5 = /*EL:412*/v1.s32bitAt(v3) + a4;
            /*SL:413*/this.merge(a3, v2, a5);
        }
    }
    
    private Type zeroExtend(final Type a1) {
        /*SL:418*/if (a1 == Type.SHORT || a1 == Type.BYTE || a1 == Type.CHAR || a1 == Type.BOOLEAN) {
            /*SL:419*/return Type.INTEGER;
        }
        /*SL:421*/return a1;
    }
    
    private static class ExceptionInfo
    {
        private int end;
        private int handler;
        private int start;
        private Type type;
        
        private ExceptionInfo(final int a1, final int a2, final int a3, final Type a4) {
            this.start = a1;
            this.end = a2;
            this.handler = a3;
            this.type = a4;
        }
    }
}
