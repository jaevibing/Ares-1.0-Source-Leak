package javassist.bytecode.stackmap;

import javassist.bytecode.ConstPool;
import java.util.ArrayList;
import javassist.bytecode.ByteArray;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.StackMap;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.StackMapTable;
import javassist.bytecode.MethodInfo;
import javassist.ClassPool;

public class MapMaker extends Tracer
{
    public static StackMapTable make(final ClassPool v-4, final MethodInfo v-3) throws BadBytecode {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-3 */
        //     1: invokevirtual   javassist/bytecode/MethodInfo.getCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //     4: astore_2        /* v-2 */
        //     5: aload_2         /* v-2 */
        //     6: ifnonnull       11
        //     9: aconst_null    
        //    10: areturn        
        //    11: aload_1         /* v-3 */
        //    12: aload_2         /* v-2 */
        //    13: iconst_1       
        //    14: invokestatic    javassist/bytecode/stackmap/TypedBlock.makeBlocks:(Ljavassist/bytecode/MethodInfo;Ljavassist/bytecode/CodeAttribute;Z)[Ljavassist/bytecode/stackmap/TypedBlock;
        //    17: astore_3        /* a1 */
        //    18: goto            25
        //    21: astore          a2
        //    23: aconst_null    
        //    24: areturn        
        //    25: aload_3         /* v-1 */
        //    26: ifnonnull       31
        //    29: aconst_null    
        //    30: areturn        
        //    31: new             Ljavassist/bytecode/stackmap/MapMaker;
        //    34: dup            
        //    35: aload_0         /* v-4 */
        //    36: aload_1         /* v-3 */
        //    37: aload_2         /* v-2 */
        //    38: invokespecial   javassist/bytecode/stackmap/MapMaker.<init>:(Ljavassist/ClassPool;Ljavassist/bytecode/MethodInfo;Ljavassist/bytecode/CodeAttribute;)V
        //    41: astore          v0
        //    43: aload           v0
        //    45: aload_3         /* v-1 */
        //    46: aload_2         /* v-2 */
        //    47: invokevirtual   javassist/bytecode/CodeAttribute.getCode:()[B
        //    50: invokevirtual   javassist/bytecode/stackmap/MapMaker.make:([Ljavassist/bytecode/stackmap/TypedBlock;[B)V
        //    53: goto            69
        //    56: astore          v1
        //    58: new             Ljavassist/bytecode/BadBytecode;
        //    61: dup            
        //    62: aload_1         /* v-3 */
        //    63: aload           v1
        //    65: invokespecial   javassist/bytecode/BadBytecode.<init>:(Ljavassist/bytecode/MethodInfo;Ljava/lang/Throwable;)V
        //    68: athrow         
        //    69: aload           v0
        //    71: aload_3         /* v-1 */
        //    72: invokevirtual   javassist/bytecode/stackmap/MapMaker.toStackMap:([Ljavassist/bytecode/stackmap/TypedBlock;)Ljavassist/bytecode/StackMapTable;
        //    75: areturn        
        //    Exceptions:
        //  throws javassist.bytecode.BadBytecode
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------------------------
        //  18     3       3     a1    [Ljavassist/bytecode/stackmap/TypedBlock;
        //  23     2       4     a2    Ljavassist/bytecode/stackmap/BasicBlock$JsrBytecode;
        //  58     11      5     v1    Ljavassist/bytecode/BadBytecode;
        //  0      76      0     v-4   Ljavassist/ClassPool;
        //  0      76      1     v-3   Ljavassist/bytecode/MethodInfo;
        //  5      71      2     v-2   Ljavassist/bytecode/CodeAttribute;
        //  25     51      3     v-1   [Ljavassist/bytecode/stackmap/TypedBlock;
        //  43     33      4     v0    Ljavassist/bytecode/stackmap/MapMaker;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                
        //  -----  -----  -----  -----  ----------------------------------------------------
        //  11     18     21     25     Ljavassist/bytecode/stackmap/BasicBlock$JsrBytecode;
        //  43     53     56     69     Ljavassist/bytecode/BadBytecode;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static StackMap make2(final ClassPool v-4, final MethodInfo v-3) throws BadBytecode {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-3 */
        //     1: invokevirtual   javassist/bytecode/MethodInfo.getCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //     4: astore_2        /* v-2 */
        //     5: aload_2         /* v-2 */
        //     6: ifnonnull       11
        //     9: aconst_null    
        //    10: areturn        
        //    11: aload_1         /* v-3 */
        //    12: aload_2         /* v-2 */
        //    13: iconst_1       
        //    14: invokestatic    javassist/bytecode/stackmap/TypedBlock.makeBlocks:(Ljavassist/bytecode/MethodInfo;Ljavassist/bytecode/CodeAttribute;Z)[Ljavassist/bytecode/stackmap/TypedBlock;
        //    17: astore_3        /* a1 */
        //    18: goto            25
        //    21: astore          a2
        //    23: aconst_null    
        //    24: areturn        
        //    25: aload_3         /* v-1 */
        //    26: ifnonnull       31
        //    29: aconst_null    
        //    30: areturn        
        //    31: new             Ljavassist/bytecode/stackmap/MapMaker;
        //    34: dup            
        //    35: aload_0         /* v-4 */
        //    36: aload_1         /* v-3 */
        //    37: aload_2         /* v-2 */
        //    38: invokespecial   javassist/bytecode/stackmap/MapMaker.<init>:(Ljavassist/ClassPool;Ljavassist/bytecode/MethodInfo;Ljavassist/bytecode/CodeAttribute;)V
        //    41: astore          v0
        //    43: aload           v0
        //    45: aload_3         /* v-1 */
        //    46: aload_2         /* v-2 */
        //    47: invokevirtual   javassist/bytecode/CodeAttribute.getCode:()[B
        //    50: invokevirtual   javassist/bytecode/stackmap/MapMaker.make:([Ljavassist/bytecode/stackmap/TypedBlock;[B)V
        //    53: goto            69
        //    56: astore          v1
        //    58: new             Ljavassist/bytecode/BadBytecode;
        //    61: dup            
        //    62: aload_1         /* v-3 */
        //    63: aload           v1
        //    65: invokespecial   javassist/bytecode/BadBytecode.<init>:(Ljavassist/bytecode/MethodInfo;Ljava/lang/Throwable;)V
        //    68: athrow         
        //    69: aload           v0
        //    71: aload_1         /* v-3 */
        //    72: invokevirtual   javassist/bytecode/MethodInfo.getConstPool:()Ljavassist/bytecode/ConstPool;
        //    75: aload_3         /* v-1 */
        //    76: invokevirtual   javassist/bytecode/stackmap/MapMaker.toStackMap2:(Ljavassist/bytecode/ConstPool;[Ljavassist/bytecode/stackmap/TypedBlock;)Ljavassist/bytecode/StackMap;
        //    79: areturn        
        //    Exceptions:
        //  throws javassist.bytecode.BadBytecode
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------------------------------------
        //  18     3       3     a1    [Ljavassist/bytecode/stackmap/TypedBlock;
        //  23     2       4     a2    Ljavassist/bytecode/stackmap/BasicBlock$JsrBytecode;
        //  58     11      5     v1    Ljavassist/bytecode/BadBytecode;
        //  0      80      0     v-4   Ljavassist/ClassPool;
        //  0      80      1     v-3   Ljavassist/bytecode/MethodInfo;
        //  5      75      2     v-2   Ljavassist/bytecode/CodeAttribute;
        //  25     55      3     v-1   [Ljavassist/bytecode/stackmap/TypedBlock;
        //  43     37      4     v0    Ljavassist/bytecode/stackmap/MapMaker;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                
        //  -----  -----  -----  -----  ----------------------------------------------------
        //  11     18     21     25     Ljavassist/bytecode/stackmap/BasicBlock$JsrBytecode;
        //  43     53     56     69     Ljavassist/bytecode/BadBytecode;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public MapMaker(final ClassPool a1, final MethodInfo a2, final CodeAttribute a3) {
        /*SL:133*/super(a1, a2.getConstPool(), a3.getMaxStack(), a3.getMaxLocals(), /*EL:135*/TypedBlock.getRetType(a2.getDescriptor()));
    }
    
    protected MapMaker(final MapMaker a1) {
        super(a1);
    }
    
    void make(final TypedBlock[] v1, final byte[] v2) throws BadBytecode {
        /*SL:164*/this.make(v2, v1[0]);
        /*SL:165*/this.findDeadCatchers(v2, v1);
        try {
            /*SL:167*/this.fixTypes(v2, v1);
        }
        catch (NotFoundException a1) {
            /*SL:169*/throw new BadBytecode("failed to resolve types", a1);
        }
    }
    
    private void make(final byte[] v-3, final TypedBlock v-2) throws BadBytecode {
        copyTypeData(/*EL:178*/v-2.stackTop, v-2.stackTypes, this.stackTypes);
        /*SL:179*/this.stackTop = v-2.stackTop;
        copyTypeData(/*EL:180*/v-2.localsTypes.length, v-2.localsTypes, this.localsTypes);
        /*SL:182*/this.traceException(v-3, v-2.toCatch);
        int i = /*EL:184*/v-2.position;
        final int v0 = /*EL:185*/i + v-2.length;
        /*SL:186*/while (i < v0) {
            /*SL:187*/i += this.doOpcode(i, v-3);
            /*SL:188*/this.traceException(v-3, v-2.toCatch);
        }
        /*SL:191*/if (v-2.exit != null) {
            /*SL:192*/for (int v = 0; v < v-2.exit.length; ++v) {
                TypedBlock a2 = /*EL:193*/(TypedBlock)v-2.exit[v];
                /*SL:194*/if (a2.alreadySet()) {
                    /*SL:195*/this.mergeMap(a2, true);
                }
                else {
                    /*SL:197*/this.recordStackMap(a2);
                    /*SL:198*/a2 = new MapMaker(this);
                    /*SL:199*/a2.make(v-3, a2);
                }
            }
        }
    }
    
    private void traceException(final byte[] v2, BasicBlock.Catch v3) throws BadBytecode {
        /*SL:208*/while (v3 != null) {
            TypedBlock a2 = /*EL:209*/(TypedBlock)v3.body;
            /*SL:210*/if (a2.alreadySet()) {
                /*SL:211*/this.mergeMap(a2, false);
                /*SL:212*/if (a2.stackTop < 1) {
                    /*SL:213*/throw new BadBytecode("bad catch clause: " + v3.typeIndex);
                }
                /*SL:215*/a2.stackTypes[0] = this.merge(this.toExceptionType(v3.typeIndex), a2.stackTypes[0]);
            }
            else {
                /*SL:219*/this.recordStackMap(a2, v3.typeIndex);
                /*SL:220*/a2 = new MapMaker(this);
                /*SL:221*/a2.make(v2, a2);
            }
            /*SL:224*/v3 = v3.next;
        }
    }
    
    private void mergeMap(final TypedBlock v2, final boolean v3) throws BadBytecode {
        /*SL:230*/for (int v4 = this.localsTypes.length, a1 = 0; a1 < v4; ++a1) {
            /*SL:231*/v2.localsTypes[a1] = this.merge(validateTypeData(this.localsTypes, v4, a1), v2.localsTypes[a1]);
        }
        /*SL:234*/if (v3) {
            /*SL:236*/for (int v4 = this.stackTop, a2 = 0; a2 < v4; ++a2) {
                /*SL:237*/v2.stackTypes[a2] = this.merge(this.stackTypes[a2], v2.stackTypes[a2]);
            }
        }
    }
    
    private TypeData merge(final TypeData a1, final TypeData a2) throws BadBytecode {
        /*SL:242*/if (a1 == a2) {
            /*SL:243*/return a2;
        }
        /*SL:244*/if (a2 instanceof TypeData.ClassName || a2 instanceof TypeData.BasicType) {
            /*SL:246*/return a2;
        }
        /*SL:247*/if (a2 instanceof TypeData.AbsTypeVar) {
            /*SL:248*/((TypeData.AbsTypeVar)a2).merge(a1);
            /*SL:249*/return a2;
        }
        /*SL:252*/throw new RuntimeException("fatal: this should never happen");
    }
    
    private void recordStackMap(final TypedBlock a1) throws BadBytecode {
        final TypeData[] v1 = /*EL:258*/TypeData.make(this.stackTypes.length);
        final int v2 = /*EL:259*/this.stackTop;
        recordTypeData(/*EL:260*/v2, this.stackTypes, v1);
        /*SL:261*/this.recordStackMap0(a1, v2, v1);
    }
    
    private void recordStackMap(final TypedBlock a1, final int a2) throws BadBytecode {
        final TypeData[] v1 = /*EL:267*/TypeData.make(this.stackTypes.length);
        /*SL:268*/v1[0] = this.toExceptionType(a2).join();
        /*SL:269*/this.recordStackMap0(a1, 1, v1);
    }
    
    private TypeData.ClassName toExceptionType(final int v2) {
        final String v3;
        /*SL:274*/if (v2 == 0) {
            final String a1 = /*EL:275*/"java.lang.Throwable";
        }
        else {
            /*SL:277*/v3 = this.cpool.getClassInfo(v2);
        }
        /*SL:279*/return new TypeData.ClassName(v3);
    }
    
    private void recordStackMap0(final TypedBlock a1, final int a2, final TypeData[] a3) throws BadBytecode {
        final int v1 = /*EL:285*/this.localsTypes.length;
        final TypeData[] v2 = /*EL:286*/TypeData.make(v1);
        final int v3 = recordTypeData(/*EL:287*/v1, this.localsTypes, v2);
        /*SL:288*/a1.setStackMap(a2, a3, v3, v2);
    }
    
    protected static int recordTypeData(final int a3, final TypeData[] v1, final TypeData[] v2) {
        int v3 = /*EL:292*/-1;
        /*SL:293*/for (int a4 = 0; a4 < a3; ++a4) {
            final TypeData a5 = validateTypeData(/*EL:294*/v1, a3, a4);
            /*SL:295*/v2[a4] = a5.join();
            /*SL:296*/if (a5 != MapMaker.TOP) {
                /*SL:297*/v3 = a4 + 1;
            }
        }
        /*SL:300*/return v3 + 1;
    }
    
    protected static void copyTypeData(final int a2, final TypeData[] a3, final TypeData[] v1) {
        /*SL:304*/for (int a4 = 0; a4 < a2; ++a4) {
            /*SL:305*/v1[a4] = a3[a4];
        }
    }
    
    private static TypeData validateTypeData(final TypeData[] a1, final int a2, final int a3) {
        final TypeData v1 = /*EL:309*/a1[a3];
        /*SL:310*/if (v1.is2WordType() && a3 + 1 < a2 && /*EL:311*/a1[a3 + 1] != MapMaker.TOP) {
            /*SL:312*/return MapMaker.TOP;
        }
        /*SL:314*/return v1;
    }
    
    private void findDeadCatchers(final byte[] v-3, final TypedBlock[] v-2) throws BadBytecode {
        /*SL:327*/for (final TypedBlock v : v-2) {
            /*SL:329*/if (!v.alreadySet()) {
                /*SL:330*/this.fixDeadcode(v-3, v);
                BasicBlock.Catch a2 = /*EL:331*/v.toCatch;
                /*SL:332*/if (a2 != null) {
                    /*SL:333*/a2 = (TypedBlock)a2.body;
                    /*SL:334*/if (!a2.alreadySet()) {
                        /*SL:337*/this.recordStackMap(a2, a2.typeIndex);
                        /*SL:338*/this.fixDeadcode(v-3, a2);
                        /*SL:339*/a2.incoming = 1;
                    }
                }
            }
        }
    }
    
    private void fixDeadcode(final byte[] v1, final TypedBlock v2) throws BadBytecode {
        final int v3 = /*EL:348*/v2.position;
        final int v4 = /*EL:349*/v2.length - 3;
        /*SL:350*/if (v4 < 0) {
            /*SL:352*/if (v4 == -1) {
                /*SL:353*/v1[v3] = 0;
            }
            /*SL:355*/v1[v3 + v2.length - 1] = -65;
            /*SL:356*/v2.incoming = 1;
            /*SL:357*/this.recordStackMap(v2, 0);
            /*SL:358*/return;
        }
        /*SL:363*/v2.incoming = 0;
        /*SL:365*/for (int a1 = 0; a1 < v4; ++a1) {
            /*SL:366*/v1[v3 + a1] = 0;
        }
        /*SL:368*/v1[v3 + v4] = -89;
        /*SL:369*/ByteArray.write16bit(-v4, v1, v3 + v4 + 1);
    }
    
    private void fixTypes(final byte[] v-6, final TypedBlock[] v-5) throws NotFoundException, BadBytecode {
        final ArrayList list = /*EL:382*/new ArrayList();
        final int length = /*EL:383*/v-5.length;
        int n = /*EL:384*/0;
        /*SL:385*/for (final TypedBlock v0 : /*EL:386*/v-5) {
            /*SL:387*/if (v0.alreadySet()) {
                /*SL:389*/for (int v = v0.localsTypes.length, a1 = 0; a1 < v; ++a1) {
                    /*SL:390*/n = v0.localsTypes[a1].dfs(list, n, this.classPool);
                }
                /*SL:393*/for (int v = v0.stackTop, a2 = 0; a2 < v; ++a2) {
                    /*SL:394*/n = v0.stackTypes[a2].dfs(list, n, this.classPool);
                }
            }
        }
    }
    
    public StackMapTable toStackMap(final TypedBlock[] v-5) {
        final StackMapTable.Writer v2 = /*EL:402*/new StackMapTable.Writer(32);
        final int length = /*EL:403*/v-5.length;
        TypedBlock v3 = /*EL:404*/v-5[0];
        int length2 = /*EL:405*/v3.length;
        /*SL:406*/if (v3.incoming > 0) {
            /*SL:407*/v2.sameFrame(0);
            /*SL:408*/--length2;
        }
        /*SL:411*/for (int v0 = 1; v0 < length; ++v0) {
            final TypedBlock v = /*EL:412*/v-5[v0];
            /*SL:413*/if (this.isTarget(v, v-5[v0 - 1])) {
                /*SL:414*/v.resetNumLocals();
                final int a1 = stackMapDiff(/*EL:415*/v3.numLocals, v3.localsTypes, v.numLocals, v.localsTypes);
                /*SL:417*/this.toStackMapBody(v2, v, a1, length2, v3);
                /*SL:418*/length2 = v.length - 1;
                /*SL:419*/v3 = v;
            }
            else/*SL:421*/ if (v.incoming == 0) {
                /*SL:423*/v2.sameFrame(length2);
                /*SL:424*/length2 = v.length - 1;
                /*SL:425*/v3 = v;
            }
            else {
                /*SL:428*/length2 += v.length;
            }
        }
        /*SL:431*/return v2.toStackMapTable(this.cpool);
    }
    
    private boolean isTarget(final TypedBlock a1, final TypedBlock a2) {
        final int v1 = /*EL:438*/a1.incoming;
        /*SL:439*/return v1 > 1 || /*EL:441*/(v1 >= 1 && /*EL:444*/a2.stop);
    }
    
    private void toStackMapBody(final StackMapTable.Writer v1, final TypedBlock v2, final int v3, final int v4, final TypedBlock v5) {
        final int v6 = /*EL:452*/v2.stackTop;
        /*SL:453*/if (v6 == 0) {
            /*SL:454*/if (v3 == 0) {
                /*SL:455*/v1.sameFrame(v4);
                /*SL:456*/return;
            }
            /*SL:458*/if (0 > v3 && v3 >= -3) {
                /*SL:459*/v1.chopFrame(v4, -v3);
                /*SL:460*/return;
            }
            /*SL:462*/if (0 < v3 && v3 <= 3) {
                final int[] a1 = /*EL:463*/new int[v3];
                final int[] a2 = /*EL:464*/this.fillStackMap(v2.numLocals - v5.numLocals, v5.numLocals, a1, v2.localsTypes);
                /*SL:467*/v1.appendFrame(v4, a2, a1);
                /*SL:468*/return;
            }
        }
        else {
            /*SL:471*/if (v6 == 1 && v3 == 0) {
                final TypeData a3 = /*EL:472*/v2.stackTypes[0];
                /*SL:473*/v1.sameLocals(v4, a3.getTypeTag(), a3.getTypeData(this.cpool));
                /*SL:474*/return;
            }
            /*SL:476*/if (v6 == 2 && v3 == 0) {
                final TypeData a4 = /*EL:477*/v2.stackTypes[0];
                /*SL:478*/if (a4.is2WordType()) {
                    /*SL:480*/v1.sameLocals(v4, a4.getTypeTag(), a4.getTypeData(this.cpool));
                    /*SL:481*/return;
                }
            }
        }
        final int[] v7 = /*EL:485*/new int[v6];
        final int[] v8 = /*EL:486*/this.fillStackMap(v6, 0, v7, v2.stackTypes);
        final int[] v9 = /*EL:487*/new int[v2.numLocals];
        final int[] v10 = /*EL:488*/this.fillStackMap(v2.numLocals, 0, v9, v2.localsTypes);
        /*SL:489*/v1.fullFrame(v4, v10, v9, v8, v7);
    }
    
    private int[] fillStackMap(final int a4, final int v1, final int[] v2, final TypeData[] v3) {
        final int v4 = diffSize(/*EL:493*/v3, v1, v1 + a4);
        final ConstPool v5 = /*EL:494*/this.cpool;
        final int[] v6 = /*EL:495*/new int[v4];
        int v7 = /*EL:496*/0;
        /*SL:497*/for (int a5 = 0; a5 < a4; ++a5) {
            final TypeData a6 = /*EL:498*/v3[v1 + a5];
            /*SL:499*/v6[v7] = a6.getTypeTag();
            /*SL:500*/v2[v7] = a6.getTypeData(v5);
            /*SL:501*/if (a6.is2WordType()) {
                /*SL:502*/++a5;
            }
            /*SL:504*/++v7;
        }
        /*SL:507*/return v6;
    }
    
    private static int stackMapDiff(final int a2, final TypeData[] a3, final int a4, final TypeData[] v1) {
        final int v2 = /*EL:513*/a4 - a2;
        /*SL:515*/if (v2 > 0) {}
        /*SL:520*/if (!stackMapEq(a3, v1, a4)) {
            /*SL:526*/return -100;
        }
        if (v2 > 0) {
            return diffSize(v1, a4, a4);
        }
        return -diffSize(a3, a4, a2);
    }
    
    private static boolean stackMapEq(final TypeData[] a2, final TypeData[] a3, final int v1) {
        /*SL:530*/for (int a4 = 0; a4 < v1; ++a4) {
            /*SL:531*/if (!a2[a4].eq(a3[a4])) {
                /*SL:532*/return false;
            }
        }
        /*SL:535*/return true;
    }
    
    private static int diffSize(final TypeData[] a2, int a3, final int v1) {
        int v2 = /*EL:539*/0;
        /*SL:540*/while (a3 < v1) {
            final TypeData a4 = /*EL:541*/a2[a3++];
            /*SL:542*/++v2;
            /*SL:543*/if (a4.is2WordType()) {
                /*SL:544*/++a3;
            }
        }
        /*SL:547*/return v2;
    }
    
    public StackMap toStackMap2(final ConstPool v-6, final TypedBlock[] v-5) {
        final StackMap.Writer a3 = /*EL:553*/new StackMap.Writer();
        final int length = /*EL:554*/v-5.length;
        final boolean[] array = /*EL:555*/new boolean[length];
        TypedBlock typedBlock = /*EL:556*/v-5[0];
        /*SL:559*/array[0] = (typedBlock.incoming > 0);
        int v0 = /*EL:561*/array[0] ? 1 : 0;
        /*SL:562*/for (TypedBlock a2 = (TypedBlock)1; a2 < length; ++a2) {
            /*SL:563*/a2 = v-5[a2];
            final boolean[] array2 = /*EL:564*/array;
            final int n = a2;
            final boolean target = this.isTarget(a2, v-5[a2 - 1]);
            array2[n] = target;
            if (target) {
                /*SL:565*/a2.resetNumLocals();
                /*SL:566*/typedBlock = a2;
                /*SL:567*/++v0;
            }
        }
        /*SL:571*/if (v0 == 0) {
            /*SL:572*/return null;
        }
        /*SL:574*/a3.write16bit(v0);
        /*SL:575*/for (int v = 0; v < length; ++v) {
            /*SL:576*/if (array[v]) {
                /*SL:577*/this.writeStackFrame(a3, v-6, v-5[v].position, v-5[v]);
            }
        }
        /*SL:579*/return a3.toStackMap(v-6);
    }
    
    private void writeStackFrame(final StackMap.Writer a1, final ConstPool a2, final int a3, final TypedBlock a4) {
        /*SL:583*/a1.write16bit(a3);
        /*SL:584*/this.writeVerifyTypeInfo(a1, a2, a4.localsTypes, a4.numLocals);
        /*SL:585*/this.writeVerifyTypeInfo(a1, a2, a4.stackTypes, a4.stackTop);
    }
    
    private void writeVerifyTypeInfo(final StackMap.Writer v2, final ConstPool v3, final TypeData[] v4, final int v5) {
        int v6 = /*EL:589*/0;
        /*SL:590*/for (TypeData a2 = (TypeData)0; a2 < v5; ++a2) {
            /*SL:591*/a2 = v4[a2];
            /*SL:592*/if (a2 != null && a2.is2WordType()) {
                /*SL:593*/++v6;
                /*SL:594*/++a2;
            }
        }
        /*SL:598*/v2.write16bit(v5 - v6);
        /*SL:599*/for (int a3 = 0; a3 < v5; ++a3) {
            final TypeData a4 = /*EL:600*/v4[a3];
            /*SL:601*/v2.writeVerifyTypeInfo(a4.getTypeTag(), a4.getTypeData(v3));
            /*SL:602*/if (a4.is2WordType()) {
                /*SL:603*/++a3;
            }
        }
    }
}
