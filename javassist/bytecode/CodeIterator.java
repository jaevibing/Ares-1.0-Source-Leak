package javassist.bytecode;

import java.util.ArrayList;

public class CodeIterator implements Opcode
{
    protected CodeAttribute codeAttr;
    protected byte[] bytecode;
    protected int endPos;
    protected int currentPos;
    protected int mark;
    private static final int[] opcodeLength;
    
    protected CodeIterator(final CodeAttribute a1) {
        this.codeAttr = a1;
        this.bytecode = a1.getCode();
        this.begin();
    }
    
    public void begin() {
        final boolean b = /*EL:66*/false;
        this.mark = (b ? 1 : 0);
        this.currentPos = (b ? 1 : 0);
        /*SL:67*/this.endPos = this.getCodeLength();
    }
    
    public void move(final int a1) {
        /*SL:83*/this.currentPos = a1;
    }
    
    public void setMark(final int a1) {
        /*SL:97*/this.mark = a1;
    }
    
    public int getMark() {
        /*SL:108*/return this.mark;
    }
    
    public CodeAttribute get() {
        /*SL:114*/return this.codeAttr;
    }
    
    public int getCodeLength() {
        /*SL:121*/return this.bytecode.length;
    }
    
    public int byteAt(final int a1) {
        /*SL:127*/return this.bytecode[a1] & 0xFF;
    }
    
    public int signedByteAt(final int a1) {
        /*SL:132*/return this.bytecode[a1];
    }
    
    public void writeByte(final int a1, final int a2) {
        /*SL:138*/this.bytecode[a2] = (byte)a1;
    }
    
    public int u16bitAt(final int a1) {
        /*SL:145*/return ByteArray.readU16bit(this.bytecode, a1);
    }
    
    public int s16bitAt(final int a1) {
        /*SL:152*/return ByteArray.readS16bit(this.bytecode, a1);
    }
    
    public void write16bit(final int a1, final int a2) {
        /*SL:159*/ByteArray.write16bit(a1, this.bytecode, a2);
    }
    
    public int s32bitAt(final int a1) {
        /*SL:166*/return ByteArray.read32bit(this.bytecode, a1);
    }
    
    public void write32bit(final int a1, final int a2) {
        /*SL:173*/ByteArray.write32bit(a1, this.bytecode, a2);
    }
    
    public void write(final byte[] v1, int v2) {
        /*SL:183*/for (int v3 = v1.length, a1 = 0; a1 < v3; ++a1) {
            /*SL:184*/this.bytecode[v2++] = v1[a1];
        }
    }
    
    public boolean hasNext() {
        /*SL:190*/return this.currentPos < this.endPos;
    }
    
    public int next() throws BadBytecode {
        final int v1 = /*EL:203*/this.currentPos;
        /*SL:204*/this.currentPos = nextOpcode(this.bytecode, v1);
        /*SL:205*/return v1;
    }
    
    public int lookAhead() {
        /*SL:217*/return this.currentPos;
    }
    
    public int skipConstructor() throws BadBytecode {
        /*SL:239*/return this.skipSuperConstructor0(-1);
    }
    
    public int skipSuperConstructor() throws BadBytecode {
        /*SL:261*/return this.skipSuperConstructor0(0);
    }
    
    public int skipThisConstructor() throws BadBytecode {
        /*SL:283*/return this.skipSuperConstructor0(1);
    }
    
    private int skipSuperConstructor0(final int v-5) throws BadBytecode {
        /*SL:289*/this.begin();
        final ConstPool constPool = /*EL:290*/this.codeAttr.getConstPool();
        final String declaringClass = /*EL:291*/this.codeAttr.getDeclaringClass();
        int n = /*EL:292*/0;
        /*SL:293*/while (this.hasNext()) {
            final int next = /*EL:294*/this.next();
            final int v0 = /*EL:295*/this.byteAt(next);
            /*SL:296*/if (v0 == 187) {
                /*SL:297*/++n;
            }
            else {
                /*SL:298*/if (v0 != 183) {
                    continue;
                }
                final int v = /*EL:299*/ByteArray.readU16bit(this.bytecode, next + 1);
                /*SL:300*/if (!constPool.getMethodrefName(v).equals("<init>") || /*EL:301*/--n >= 0) {
                    continue;
                }
                /*SL:302*/if (v-5 < 0) {
                    /*SL:303*/return next;
                }
                final String a1 = /*EL:305*/constPool.getMethodrefClassName(v);
                /*SL:306*/if (a1.equals(declaringClass) == v-5 > 0) {
                    /*SL:307*/return next;
                }
                break;
            }
        }
        /*SL:314*/this.begin();
        /*SL:315*/return -1;
    }
    
    public int insert(final byte[] a1) throws BadBytecode {
        /*SL:339*/return this.insert0(this.currentPos, a1, false);
    }
    
    public void insert(final int a1, final byte[] a2) throws BadBytecode {
        /*SL:364*/this.insert0(a1, a2, false);
    }
    
    public int insertAt(final int a1, final byte[] a2) throws BadBytecode {
        /*SL:388*/return this.insert0(a1, a2, false);
    }
    
    public int insertEx(final byte[] a1) throws BadBytecode {
        /*SL:412*/return this.insert0(this.currentPos, a1, true);
    }
    
    public void insertEx(final int a1, final byte[] a2) throws BadBytecode {
        /*SL:437*/this.insert0(a1, a2, true);
    }
    
    public int insertExAt(final int a1, final byte[] a2) throws BadBytecode {
        /*SL:461*/return this.insert0(a1, a2, true);
    }
    
    private int insert0(int a3, final byte[] v1, final boolean v2) throws BadBytecode {
        final int v3 = /*EL:471*/v1.length;
        /*SL:472*/if (v3 <= 0) {
            /*SL:473*/return a3;
        }
        int v4;
        /*SL:476*/a3 = /*EL:478*/(v4 = this.insertGapAt(a3, v3, v2).position);
        /*SL:479*/for (int a4 = 0; a4 < v3; ++a4) {
            /*SL:480*/this.bytecode[v4++] = v1[a4];
        }
        /*SL:482*/return a3;
    }
    
    public int insertGap(final int a1) throws BadBytecode {
        /*SL:501*/return this.insertGapAt(this.currentPos, a1, false).position;
    }
    
    public int insertGap(final int a1, final int a2) throws BadBytecode {
        /*SL:521*/return this.insertGapAt(a1, a2, false).length;
    }
    
    public int insertExGap(final int a1) throws BadBytecode {
        /*SL:540*/return this.insertGapAt(this.currentPos, a1, true).position;
    }
    
    public int insertExGap(final int a1, final int a2) throws BadBytecode {
        /*SL:560*/return this.insertGapAt(a1, a2, true).length;
    }
    
    public Gap insertGapAt(int v2, final int v3, final boolean v4) throws BadBytecode {
        final Gap v5 = /*EL:619*/new Gap();
        /*SL:620*/if (v3 <= 0) {
            /*SL:621*/v5.position = v2;
            /*SL:622*/v5.length = 0;
            /*SL:623*/return v5;
        }
        final byte[] v6;
        final int v7;
        /*SL:628*/if (this.bytecode.length + v3 > 32767) {
            final byte[] a1 = /*EL:630*/this.insertGapCore0w(this.bytecode, v2, v3, v4, this.get().getExceptionTable(), /*EL:631*/this.codeAttr, v5);
            /*SL:632*/v2 = v5.position;
        }
        else {
            final int a2 = /*EL:636*/this.currentPos;
            /*SL:637*/v6 = insertGapCore0(this.bytecode, v2, v3, v4, this.get().getExceptionTable(), /*EL:638*/this.codeAttr);
            /*SL:640*/v7 = v6.length - this.bytecode.length;
            /*SL:641*/v5.position = v2;
            /*SL:642*/v5.length = v7;
            /*SL:643*/if (a2 >= v2) {
                /*SL:644*/this.currentPos = a2 + v7;
            }
            /*SL:646*/if (this.mark > v2 || (this.mark == v2 && v4)) {
                /*SL:647*/this.mark += v7;
            }
        }
        /*SL:650*/this.codeAttr.setCode(v6);
        /*SL:651*/this.bytecode = v6;
        /*SL:652*/this.endPos = this.getCodeLength();
        /*SL:653*/this.updateCursors(v2, v7);
        /*SL:654*/return v5;
    }
    
    protected void updateCursors(final int a1, final int a2) {
    }
    
    public void insert(final ExceptionTable a1, final int a2) {
        /*SL:677*/this.codeAttr.getExceptionTable().add(0, a1, a2);
    }
    
    public int append(final byte[] v2) {
        final int v3 = /*EL:687*/this.getCodeLength();
        final int v4 = /*EL:688*/v2.length;
        /*SL:689*/if (v4 <= 0) {
            /*SL:690*/return v3;
        }
        /*SL:692*/this.appendGap(v4);
        final byte[] v5 = /*EL:693*/this.bytecode;
        /*SL:694*/for (int a1 = 0; a1 < v4; ++a1) {
            /*SL:695*/v5[a1 + v3] = v2[a1];
        }
        /*SL:697*/return v3;
    }
    
    public void appendGap(final int a1) {
        final byte[] v1 = /*EL:706*/this.bytecode;
        final int v2 = /*EL:707*/v1.length;
        final byte[] v3 = /*EL:708*/new byte[v2 + a1];
        /*SL:711*/for (int v4 = 0; v4 < v2; ++v4) {
            /*SL:712*/v3[v4] = v1[v4];
        }
        /*SL:714*/for (int v4 = v2; v4 < v2 + a1; ++v4) {
            /*SL:715*/v3[v4] = 0;
        }
        /*SL:717*/this.codeAttr.setCode(v3);
        /*SL:718*/this.bytecode = v3;
        /*SL:719*/this.endPos = this.getCodeLength();
    }
    
    public void append(final ExceptionTable a1, final int a2) {
        final ExceptionTable v1 = /*EL:731*/this.codeAttr.getExceptionTable();
        /*SL:732*/v1.add(v1.size(), a1, a2);
    }
    
    static int nextOpcode(final byte[] v-4, final int v-3) throws BadBytecode {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v-4 */
        //     1: iload_1         /* v-3 */
        //     2: baload         
        //     3: sipush          255
        //     6: iand           
        //     7: istore_2        /* a1 */
        //     8: goto            23
        //    11: astore_3        /* a2 */
        //    12: new             Ljavassist/bytecode/BadBytecode;
        //    15: dup            
        //    16: ldc_w           "invalid opcode address"
        //    19: invokespecial   javassist/bytecode/BadBytecode.<init>:(Ljava/lang/String;)V
        //    22: athrow         
        //    23: getstatic       javassist/bytecode/CodeIterator.opcodeLength:[I
        //    26: iload_2         /* v-2 */
        //    27: iaload         
        //    28: istore_3        /* v-1 */
        //    29: iload_3         /* v-1 */
        //    30: ifle            37
        //    33: iload_1         /* v-3 */
        //    34: iload_3         /* v-1 */
        //    35: iadd           
        //    36: ireturn        
        //    37: iload_2         /* v-2 */
        //    38: sipush          196
        //    41: if_icmpne       63
        //    44: aload_0         /* v-4 */
        //    45: iload_1         /* v-3 */
        //    46: iconst_1       
        //    47: iadd           
        //    48: baload         
        //    49: bipush          -124
        //    51: if_icmpne       59
        //    54: iload_1         /* v-3 */
        //    55: bipush          6
        //    57: iadd           
        //    58: ireturn        
        //    59: iload_1         /* v-3 */
        //    60: iconst_4       
        //    61: iadd           
        //    62: ireturn        
        //    63: iload_1         /* v-3 */
        //    64: bipush          -4
        //    66: iand           
        //    67: bipush          8
        //    69: iadd           
        //    70: istore          v0
        //    72: iload_2         /* v-2 */
        //    73: sipush          171
        //    76: if_icmpne       98
        //    79: aload_0         /* v-4 */
        //    80: iload           v0
        //    82: invokestatic    javassist/bytecode/ByteArray.read32bit:([BI)I
        //    85: istore          v1
        //    87: iload           v0
        //    89: iload           v1
        //    91: bipush          8
        //    93: imul           
        //    94: iadd           
        //    95: iconst_4       
        //    96: iadd           
        //    97: ireturn        
        //    98: iload_2         /* v-2 */
        //    99: sipush          170
        //   102: if_icmpne       139
        //   105: aload_0         /* v-4 */
        //   106: iload           v0
        //   108: invokestatic    javassist/bytecode/ByteArray.read32bit:([BI)I
        //   111: istore          v1
        //   113: aload_0         /* v-4 */
        //   114: iload           v0
        //   116: iconst_4       
        //   117: iadd           
        //   118: invokestatic    javassist/bytecode/ByteArray.read32bit:([BI)I
        //   121: istore          v2
        //   123: iload           v0
        //   125: iload           v2
        //   127: iload           v1
        //   129: isub           
        //   130: iconst_1       
        //   131: iadd           
        //   132: iconst_4       
        //   133: imul           
        //   134: iadd           
        //   135: bipush          8
        //   137: iadd           
        //   138: ireturn        
        //   139: goto            143
        //   142: astore_3       
        //   143: new             Ljavassist/bytecode/BadBytecode;
        //   146: dup            
        //   147: iload_2         /* v-2 */
        //   148: invokespecial   javassist/bytecode/BadBytecode.<init>:(I)V
        //   151: athrow         
        //    Exceptions:
        //  throws javassist.bytecode.BadBytecode
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------------------
        //  8      3       2     a1    I
        //  12     11      3     a2    Ljava/lang/IndexOutOfBoundsException;
        //  87     11      5     v1    I
        //  113    26      5     v1    I
        //  123    16      6     v2    I
        //  72     67      4     v0    I
        //  29     110     3     v-1   I
        //  0      152     0     v-4   [B
        //  0      152     1     v-3   I
        //  23     129     2     v-2   I
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                 
        //  -----  -----  -----  -----  -------------------------------------
        //  0      8      11     23     Ljava/lang/IndexOutOfBoundsException;
        //  23     36     142    143    Ljava/lang/IndexOutOfBoundsException;
        //  37     58     142    143    Ljava/lang/IndexOutOfBoundsException;
        //  59     62     142    143    Ljava/lang/IndexOutOfBoundsException;
        //  63     97     142    143    Ljava/lang/IndexOutOfBoundsException;
        //  98     138    142    143    Ljava/lang/IndexOutOfBoundsException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static byte[] insertGapCore0(final byte[] a3, final int a4, final int a5, final boolean a6, final ExceptionTable v1, final CodeAttribute v2) throws BadBytecode {
        /*SL:820*/if (a5 <= 0) {
            /*SL:821*/return a3;
        }
        try {
            /*SL:824*/return insertGapCore1(a3, a4, a5, a6, v1, v2);
        }
        catch (AlignmentException a7) {
            try {
                /*SL:828*/return insertGapCore1(a3, a4, a5 + 3 & 0xFFFFFFFC, a6, v1, v2);
            }
            catch (AlignmentException a8) {
                /*SL:832*/throw new RuntimeException("fatal error?");
            }
        }
    }
    
    private static byte[] insertGapCore1(final byte[] a1, final int a2, final int a3, final boolean a4, final ExceptionTable a5, final CodeAttribute a6) throws BadBytecode, AlignmentException {
        final int v1 = /*EL:842*/a1.length;
        final byte[] v2 = /*EL:843*/new byte[v1 + a3];
        insertGap2(/*EL:844*/a1, a2, a3, v1, v2, a4);
        /*SL:845*/a5.shiftPc(a2, a3, a4);
        final LineNumberAttribute v3 = /*EL:846*/(LineNumberAttribute)a6.getAttribute("LineNumberTable");
        /*SL:848*/if (v3 != null) {
            /*SL:849*/v3.shiftPc(a2, a3, a4);
        }
        final LocalVariableAttribute v4 = /*EL:851*/(LocalVariableAttribute)a6.getAttribute("LocalVariableTable");
        /*SL:853*/if (v4 != null) {
            /*SL:854*/v4.shiftPc(a2, a3, a4);
        }
        final LocalVariableAttribute v5 = /*EL:856*/(LocalVariableAttribute)a6.getAttribute("LocalVariableTypeTable");
        /*SL:859*/if (v5 != null) {
            /*SL:860*/v5.shiftPc(a2, a3, a4);
        }
        final StackMapTable v6 = /*EL:862*/(StackMapTable)a6.getAttribute("StackMapTable");
        /*SL:863*/if (v6 != null) {
            /*SL:864*/v6.shiftPc(a2, a3, a4);
        }
        final StackMap v7 = /*EL:866*/(StackMap)a6.getAttribute("StackMap");
        /*SL:867*/if (v7 != null) {
            /*SL:868*/v7.shiftPc(a2, a3, a4);
        }
        /*SL:870*/return v2;
    }
    
    private static void insertGap2(final byte[] v-11, final int v-10, final int v-9, final int v-8, final byte[] v-7, final boolean v-6) throws BadBytecode, AlignmentException {
        int i = /*EL:878*/0;
        int j = /*EL:879*/0;
        /*SL:880*/while (i < v-8) {
            /*SL:881*/if (i == v-10) {
                /*SL:883*/for (int a1 = j + v-9; j < a1; /*SL:884*/v-7[j++] = 0) {}
            }
            final int nextOpcode = nextOpcode(/*EL:887*/v-11, i);
            final int n = /*EL:888*/v-11[i] & 0xFF;
            /*SL:890*/if ((153 <= n && n <= 168) || n == 198 || n == 199) {
                int a2 = /*EL:893*/v-11[i + 1] << 8 | (v-11[i + 2] & 0xFF);
                /*SL:894*/a2 = newOffset(i, a2, v-10, v-9, v-6);
                /*SL:895*/v-7[j] = v-11[i];
                /*SL:896*/ByteArray.write16bit(a2, v-7, j + 1);
                /*SL:897*/j += 3;
            }
            else/*SL:899*/ if (n == 200 || n == 201) {
                int a3 = /*EL:901*/ByteArray.read32bit(v-11, i + 1);
                /*SL:902*/a3 = newOffset(i, a3, v-10, v-9, v-6);
                /*SL:903*/v-7[j++] = v-11[i];
                /*SL:904*/ByteArray.write32bit(a3, v-7, j);
                /*SL:905*/j += 4;
            }
            else/*SL:907*/ if (n == 170) {
                /*SL:908*/if (i != j && (v-9 & 0x3) != 0x0) {
                    /*SL:909*/throw new AlignmentException();
                }
                int a4 = /*EL:911*/(i & 0xFFFFFFFC) + 4;
                /*SL:918*/j = copyGapBytes(v-7, j, v-11, i, a4);
                final int a5 = newOffset(/*EL:920*/i, ByteArray.read32bit(v-11, a4), v-10, v-9, v-6);
                /*SL:922*/ByteArray.write32bit(a5, v-7, j);
                final int v1 = /*EL:923*/ByteArray.read32bit(v-11, a4 + 4);
                /*SL:924*/ByteArray.write32bit(v1, v-7, j + 4);
                final int v2 = /*EL:925*/ByteArray.read32bit(v-11, a4 + 8);
                /*SL:926*/ByteArray.write32bit(v2, v-7, j + 8);
                /*SL:927*/j += 12;
                int v3;
                /*SL:930*/for (v3 = a4 + 12, a4 = v3 + (v2 - v1 + 1) * 4; v3 < a4; /*SL:935*/v3 += 4) {
                    final int a6 = newOffset(i, ByteArray.read32bit(v-11, v3), v-10, v-9, v-6);
                    ByteArray.write32bit(a6, v-7, j);
                    j += 4;
                }
            }
            else/*SL:938*/ if (n == 171) {
                /*SL:939*/if (i != j && (v-9 & 0x3) != 0x0) {
                    /*SL:940*/throw new AlignmentException();
                }
                int n2 = /*EL:942*/(i & 0xFFFFFFFC) + 4;
                /*SL:950*/j = copyGapBytes(v-7, j, v-11, i, n2);
                final int v4 = newOffset(/*EL:952*/i, ByteArray.read32bit(v-11, n2), v-10, v-9, v-6);
                /*SL:954*/ByteArray.write32bit(v4, v-7, j);
                final int v1 = /*EL:955*/ByteArray.read32bit(v-11, n2 + 4);
                /*SL:956*/ByteArray.write32bit(v1, v-7, j + 4);
                /*SL:957*/j += 8;
                int v2;
                /*SL:960*/for (v2 = n2 + 8, n2 = v2 + v1 * 8; v2 < n2; /*SL:967*/v2 += 8) {
                    ByteArray.copy32bit(v-11, v2, v-7, j);
                    final int v3 = newOffset(i, ByteArray.read32bit(v-11, v2 + 4), v-10, v-9, v-6);
                    ByteArray.write32bit(v3, v-7, j + 4);
                    j += 8;
                }
            }
            else {
                /*SL:971*/while (i < nextOpcode) {
                    /*SL:972*/v-7[j++] = v-11[i++];
                }
            }
            i = nextOpcode;
        }
    }
    
    private static int copyGapBytes(final byte[] a1, int a2, final byte[] a3, int a4, final int a5) {
        /*SL:978*/switch (a5 - a4) {
            case 4: {
                /*SL:980*/a1[a2++] = a3[a4++];
            }
            case 3: {
                /*SL:982*/a1[a2++] = a3[a4++];
            }
            case 2: {
                /*SL:984*/a1[a2++] = a3[a4++];
            }
            case 1: {
                /*SL:986*/a1[a2++] = a3[a4++];
                break;
            }
        }
        /*SL:990*/return a2;
    }
    
    private static int newOffset(final int a1, int a2, final int a3, final int a4, final boolean a5) {
        final int v1 = /*EL:995*/a1 + a2;
        /*SL:996*/if (a1 < a3) {
            /*SL:997*/if (a3 < v1 || (a5 && a3 == v1)) {
                /*SL:998*/a2 += a4;
            }
        }
        else/*SL:1000*/ if (a1 == a3) {
            /*SL:1003*/if (v1 < a3) {
                /*SL:1004*/a2 -= a4;
            }
        }
        else/*SL:1007*/ if (v1 < a3 || (!a5 && a3 == v1)) {
            /*SL:1008*/a2 -= a4;
        }
        /*SL:1010*/return a2;
    }
    
    static byte[] changeLdcToLdcW(final byte[] a1, final ExceptionTable a2, final CodeAttribute a3, CodeAttribute.LdcEntry a4) throws BadBytecode {
        final Pointers v1 = /*EL:1077*/new Pointers(0, 0, 0, a2, a3);
        final ArrayList v2 = makeJumpList(/*EL:1078*/a1, a1.length, v1);
        /*SL:1079*/while (a4 != null) {
            addLdcW(/*EL:1080*/a4, v2);
            /*SL:1081*/a4 = a4.next;
        }
        final byte[] v3 = insertGap2w(/*EL:1084*/a1, 0, 0, false, v2, v1);
        /*SL:1085*/return v3;
    }
    
    private static void addLdcW(final CodeAttribute.LdcEntry a2, final ArrayList v1) {
        final int v2 = /*EL:1089*/a2.where;
        final LdcW v3 = /*EL:1090*/new LdcW(v2, a2.index);
        /*SL:1092*/for (int v4 = v1.size(), a3 = 0; a3 < v4; ++a3) {
            /*SL:1093*/if (v2 < v1.get(a3).orgPos) {
                /*SL:1094*/v1.add(a3, v3);
                /*SL:1095*/return;
            }
        }
        /*SL:1098*/v1.add(v3);
    }
    
    private byte[] insertGapCore0w(final byte[] a1, final int a2, final int a3, final boolean a4, final ExceptionTable a5, final CodeAttribute a6, final Gap a7) throws BadBytecode {
        /*SL:1118*/if (a3 <= 0) {
            /*SL:1119*/return a1;
        }
        final Pointers v1 = /*EL:1121*/new Pointers(this.currentPos, this.mark, a2, a5, a6);
        final ArrayList v2 = makeJumpList(/*EL:1122*/a1, a1.length, v1);
        final byte[] v3 = insertGap2w(/*EL:1123*/a1, a2, a3, a4, v2, v1);
        /*SL:1124*/this.currentPos = v1.cursor;
        /*SL:1125*/this.mark = v1.mark;
        int v4 = /*EL:1126*/v1.mark0;
        /*SL:1127*/if (v4 == this.currentPos && !a4) {
            /*SL:1128*/this.currentPos += a3;
        }
        /*SL:1130*/if (a4) {
            /*SL:1131*/v4 -= a3;
        }
        /*SL:1133*/a7.position = v4;
        /*SL:1134*/a7.length = a3;
        /*SL:1135*/return v3;
    }
    
    private static byte[] insertGap2w(final byte[] v-11, final int v-10, final int v-9, final boolean v-8, final ArrayList v-7, final Pointers v-6) throws BadBytecode {
        final int size = /*EL:1142*/v-7.size();
        /*SL:1143*/if (v-9 > 0) {
            /*SL:1144*/v-6.shiftPc(v-10, v-9, v-8);
            /*SL:1145*/for (int a1 = 0; a1 < size; ++a1) {
                /*SL:1146*/v-7.get(a1).shift(v-10, v-9, v-8);
            }
        }
        boolean b = /*EL:1149*/true;
        while (true) {
            /*SL:1151*/if (b) {
                /*SL:1152*/b = false;
                /*SL:1153*/for (int a2 = 0; a2 < size; ++a2) {
                    final Branch a3 = /*EL:1154*/v-7.get(a2);
                    /*SL:1155*/if (a3.expanded()) {
                        /*SL:1156*/b = true;
                        final int a4 = /*EL:1157*/a3.pos;
                        final int a5 = /*EL:1158*/a3.deltaSize();
                        /*SL:1159*/v-6.shiftPc(a4, a5, false);
                        /*SL:1160*/for (int a6 = 0; a6 < size; ++a6) {
                            /*SL:1161*/v-7.get(a6).shift(a4, a5, false);
                        }
                    }
                }
            }
            else {
                /*SL:1166*/for (int i = 0; i < size; ++i) {
                    final Branch branch = /*EL:1167*/v-7.get(i);
                    final int gapChanged = /*EL:1168*/branch.gapChanged();
                    /*SL:1169*/if (gapChanged > 0) {
                        /*SL:1170*/b = true;
                        final int v0 = /*EL:1171*/branch.pos;
                        /*SL:1172*/v-6.shiftPc(v0, gapChanged, false);
                        /*SL:1173*/for (int v = 0; v < size; ++v) {
                            /*SL:1174*/v-7.get(v).shift(v0, gapChanged, false);
                        }
                    }
                }
                /*SL:1177*/if (!b) {
                    break;
                }
                continue;
            }
        }
        /*SL:1179*/return makeExapndedCode(v-11, v-7, v-10, v-9);
    }
    
    private static ArrayList makeJumpList(final byte[] v-6, final int v-5, final Pointers v-4) throws BadBytecode {
        final ArrayList list = /*EL:1185*/new ArrayList();
        int nextOpcode;
        /*SL:1187*/for (int i = 0; i < v-5; i = nextOpcode) {
            /*SL:1188*/nextOpcode = nextOpcode(v-6, i);
            final int v0 = /*EL:1189*/v-6[i] & 0xFF;
            /*SL:1191*/if ((153 <= v0 && v0 <= 168) || v0 == 198 || v0 == 199) {
                int a2 = /*EL:1194*/v-6[i + 1] << 8 | (v-6[i + 2] & 0xFF);
                final Branch a3;
                /*SL:1196*/if (v0 == 167 || v0 == 168) {
                    /*SL:1197*/a2 = new Jump16(i, a2);
                }
                else {
                    /*SL:1199*/a3 = new If16(i, a2);
                }
                /*SL:1201*/list.add(a3);
            }
            else/*SL:1203*/ if (v0 == 200 || v0 == 201) {
                final int v = /*EL:1205*/ByteArray.read32bit(v-6, i + 1);
                /*SL:1206*/list.add(new Jump32(i, v));
            }
            else/*SL:1208*/ if (v0 == 170) {
                final int v = /*EL:1209*/(i & 0xFFFFFFFC) + 4;
                final int v2 = /*EL:1210*/ByteArray.read32bit(v-6, v);
                final int v3 = /*EL:1211*/ByteArray.read32bit(v-6, v + 4);
                final int v4 = /*EL:1212*/ByteArray.read32bit(v-6, v + 8);
                int v5 = /*EL:1213*/v + 12;
                final int v6 = /*EL:1214*/v4 - v3 + 1;
                final int[] v7 = /*EL:1215*/new int[v6];
                /*SL:1216*/for (int v8 = 0; v8 < v6; ++v8) {
                    /*SL:1217*/v7[v8] = ByteArray.read32bit(v-6, v5);
                    /*SL:1218*/v5 += 4;
                }
                /*SL:1221*/list.add(new Table(i, v2, v3, v4, v7, v-4));
            }
            else/*SL:1223*/ if (v0 == 171) {
                final int v = /*EL:1224*/(i & 0xFFFFFFFC) + 4;
                final int v2 = /*EL:1225*/ByteArray.read32bit(v-6, v);
                final int v3 = /*EL:1226*/ByteArray.read32bit(v-6, v + 4);
                int v4 = /*EL:1227*/v + 8;
                final int[] v9 = /*EL:1228*/new int[v3];
                final int[] v10 = /*EL:1229*/new int[v3];
                /*SL:1230*/for (int v11 = 0; v11 < v3; ++v11) {
                    /*SL:1231*/v9[v11] = ByteArray.read32bit(v-6, v4);
                    /*SL:1232*/v10[v11] = ByteArray.read32bit(v-6, v4 + 4);
                    /*SL:1233*/v4 += 8;
                }
                /*SL:1236*/list.add(new Lookup(i, v2, v9, v10, v-4));
            }
        }
        /*SL:1240*/return list;
    }
    
    private static byte[] makeExapndedCode(final byte[] v-12, final ArrayList v-11, final int v-10, final int v-9) throws BadBytecode {
        final int size = /*EL:1247*/v-11.size();
        int n = /*EL:1248*/v-12.length + v-9;
        /*SL:1249*/for (Branch a2 = (Branch)0; a2 < size; ++a2) {
            /*SL:1250*/a2 = v-11.get(a2);
            /*SL:1251*/n += a2.deltaSize();
        }
        final byte[] array = /*EL:1254*/new byte[n];
        int i = /*EL:1255*/0;
        int j = 0;
        int n2 = 0;
        final int length = /*EL:1256*/v-12.length;
        Branch branch = null;
        int v0 = 0;
        /*SL:1259*/if (0 < size) {
            final Branch a3 = /*EL:1260*/v-11.get(0);
            final int a4 = /*EL:1261*/a3.orgPos;
        }
        else {
            /*SL:1264*/branch = null;
            /*SL:1265*/v0 = length;
        }
        /*SL:1268*/while (i < length) {
            /*SL:1269*/if (i == v-10) {
                /*SL:1271*/for (int v = j + v-9; j < v; /*SL:1272*/array[j++] = 0) {}
            }
            /*SL:1275*/if (i != v0) {
                /*SL:1276*/array[j++] = v-12[i++];
            }
            else {
                final int v = /*EL:1278*/branch.write(i, v-12, j, array);
                /*SL:1279*/i += v;
                /*SL:1280*/j += v + branch.deltaSize();
                /*SL:1281*/if (++n2 < size) {
                    /*SL:1282*/branch = v-11.get(n2);
                    /*SL:1283*/v0 = branch.orgPos;
                }
                else {
                    /*SL:1286*/branch = null;
                    /*SL:1287*/v0 = length;
                }
            }
        }
        /*SL:1292*/return array;
    }
    
    static {
        opcodeLength = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5 };
    }
    
    public static class Gap
    {
        public int position;
        public int length;
    }
    
    static class AlignmentException extends Exception
    {
    }
    
    static class Pointers
    {
        int cursor;
        int mark0;
        int mark;
        ExceptionTable etable;
        LineNumberAttribute line;
        LocalVariableAttribute vars;
        LocalVariableAttribute types;
        StackMapTable stack;
        StackMap stack2;
        
        Pointers(final int a1, final int a2, final int a3, final ExceptionTable a4, final CodeAttribute a5) {
            this.cursor = a1;
            this.mark = a2;
            this.mark0 = a3;
            this.etable = a4;
            this.line = (LineNumberAttribute)a5.getAttribute("LineNumberTable");
            this.vars = (LocalVariableAttribute)a5.getAttribute("LocalVariableTable");
            this.types = (LocalVariableAttribute)a5.getAttribute("LocalVariableTypeTable");
            this.stack = (StackMapTable)a5.getAttribute("StackMapTable");
            this.stack2 = (StackMap)a5.getAttribute("StackMap");
        }
        
        void shiftPc(final int a1, final int a2, final boolean a3) throws BadBytecode {
            /*SL:1035*/if (a1 < this.cursor || (a1 == this.cursor && a3)) {
                /*SL:1036*/this.cursor += a2;
            }
            /*SL:1038*/if (a1 < this.mark || (a1 == this.mark && a3)) {
                /*SL:1039*/this.mark += a2;
            }
            /*SL:1041*/if (a1 < this.mark0 || (a1 == this.mark0 && a3)) {
                /*SL:1042*/this.mark0 += a2;
            }
            /*SL:1044*/this.etable.shiftPc(a1, a2, a3);
            /*SL:1045*/if (this.line != null) {
                /*SL:1046*/this.line.shiftPc(a1, a2, a3);
            }
            /*SL:1048*/if (this.vars != null) {
                /*SL:1049*/this.vars.shiftPc(a1, a2, a3);
            }
            /*SL:1051*/if (this.types != null) {
                /*SL:1052*/this.types.shiftPc(a1, a2, a3);
            }
            /*SL:1054*/if (this.stack != null) {
                /*SL:1055*/this.stack.shiftPc(a1, a2, a3);
            }
            /*SL:1057*/if (this.stack2 != null) {
                /*SL:1058*/this.stack2.shiftPc(a1, a2, a3);
            }
        }
        
        void shiftForSwitch(final int a1, final int a2) throws BadBytecode {
            /*SL:1062*/if (this.stack != null) {
                /*SL:1063*/this.stack.shiftForSwitch(a1, a2);
            }
            /*SL:1065*/if (this.stack2 != null) {
                /*SL:1066*/this.stack2.shiftForSwitch(a1, a2);
            }
        }
    }
    
    abstract static class Branch
    {
        int pos;
        int orgPos;
        
        Branch(final int a1) {
            this.orgPos = a1;
            this.pos = a1;
        }
        
        void shift(final int a1, final int a2, final boolean a3) {
            /*SL:1299*/if (a1 < this.pos || (a1 == this.pos && a3)) {
                /*SL:1300*/this.pos += a2;
            }
        }
        
        static int shiftOffset(final int a1, int a2, final int a3, final int a4, final boolean a5) {
            final int v1 = /*EL:1305*/a1 + a2;
            /*SL:1306*/if (a1 < a3) {
                /*SL:1307*/if (a3 < v1 || (a5 && a3 == v1)) {
                    /*SL:1308*/a2 += a4;
                }
            }
            else/*SL:1310*/ if (a1 == a3) {
                /*SL:1313*/if (v1 < a3 && a5) {
                    /*SL:1314*/a2 -= a4;
                }
                else/*SL:1315*/ if (a3 < v1 && !a5) {
                    /*SL:1316*/a2 += a4;
                }
            }
            else/*SL:1319*/ if (v1 < a3 || (!a5 && a3 == v1)) {
                /*SL:1320*/a2 -= a4;
            }
            /*SL:1322*/return a2;
        }
        
        boolean expanded() {
            /*SL:1325*/return false;
        }
        
        int gapChanged() {
            /*SL:1326*/return 0;
        }
        
        int deltaSize() {
            /*SL:1327*/return 0;
        }
        
        abstract int write(final int p0, final byte[] p1, final int p2, final byte[] p3) throws BadBytecode;
    }
    
    static class LdcW extends Branch
    {
        int index;
        boolean state;
        
        LdcW(final int a1, final int a2) {
            super(a1);
            this.index = a2;
            this.state = true;
        }
        
        @Override
        boolean expanded() {
            /*SL:1345*/if (this.state) {
                /*SL:1346*/this.state = false;
                /*SL:1347*/return true;
            }
            /*SL:1350*/return false;
        }
        
        @Override
        int deltaSize() {
            /*SL:1353*/return 1;
        }
        
        @Override
        int write(final int a1, final byte[] a2, final int a3, final byte[] a4) {
            /*SL:1356*/a4[a3] = 19;
            /*SL:1357*/ByteArray.write16bit(this.index, a4, a3 + 1);
            /*SL:1358*/return 2;
        }
    }
    
    abstract static class Branch16 extends Branch
    {
        int offset;
        int state;
        static final int BIT16 = 0;
        static final int EXPAND = 1;
        static final int BIT32 = 2;
        
        Branch16(final int a1, final int a2) {
            super(a1);
            this.offset = a2;
            this.state = 0;
        }
        
        @Override
        void shift(final int a1, final int a2, final boolean a3) {
            /*SL:1376*/this.offset = Branch.shiftOffset(this.pos, this.offset, a1, a2, a3);
            /*SL:1377*/super.shift(a1, a2, a3);
            /*SL:1378*/if (this.state == 0 && /*EL:1379*/(this.offset < -32768 || 32767 < this.offset)) {
                /*SL:1380*/this.state = 1;
            }
        }
        
        @Override
        boolean expanded() {
            /*SL:1384*/if (this.state == 1) {
                /*SL:1385*/this.state = 2;
                /*SL:1386*/return true;
            }
            /*SL:1389*/return false;
        }
        
        @Override
        abstract int deltaSize();
        
        abstract void write32(final int p0, final byte[] p1, final int p2, final byte[] p3);
        
        @Override
        int write(final int a1, final byte[] a2, final int a3, final byte[] a4) {
            /*SL:1396*/if (this.state == 2) {
                /*SL:1397*/this.write32(a1, a2, a3, a4);
            }
            else {
                /*SL:1399*/a4[a3] = a2[a1];
                /*SL:1400*/ByteArray.write16bit(this.offset, a4, a3 + 1);
            }
            /*SL:1403*/return 3;
        }
    }
    
    static class Jump16 extends Branch16
    {
        Jump16(final int a1, final int a2) {
            super(a1, a2);
        }
        
        @Override
        int deltaSize() {
            /*SL:1414*/return (this.state == 2) ? 2 : 0;
        }
        
        @Override
        void write32(final int a1, final byte[] a2, final int a3, final byte[] a4) {
            /*SL:1418*/a4[a3] = (byte)(((a2[a1] & 0xFF) == 0xA7) ? 200 : 201);
            /*SL:1419*/ByteArray.write32bit(this.offset, a4, a3 + 1);
        }
    }
    
    static class If16 extends Branch16
    {
        If16(final int a1, final int a2) {
            super(a1, a2);
        }
        
        @Override
        int deltaSize() {
            /*SL:1430*/return (this.state == 2) ? 5 : 0;
        }
        
        @Override
        void write32(final int a1, final byte[] a2, final int a3, final byte[] a4) {
            /*SL:1434*/a4[a3] = (byte)this.opcode(a2[a1] & 0xFF);
            /*SL:1435*/a4[a3 + 1] = 0;
            /*SL:1436*/a4[a3 + 2] = 8;
            /*SL:1437*/a4[a3 + 3] = -56;
            /*SL:1438*/ByteArray.write32bit(this.offset - 3, a4, a3 + 4);
        }
        
        int opcode(final int a1) {
            /*SL:1442*/if (a1 == 198) {
                /*SL:1443*/return 199;
            }
            /*SL:1444*/if (a1 == 199) {
                /*SL:1445*/return 198;
            }
            /*SL:1447*/if ((a1 - 153 & 0x1) == 0x0) {
                /*SL:1448*/return a1 + 1;
            }
            /*SL:1450*/return a1 - 1;
        }
    }
    
    static class Jump32 extends Branch
    {
        int offset;
        
        Jump32(final int a1, final int a2) {
            super(a1);
            this.offset = a2;
        }
        
        @Override
        void shift(final int a1, final int a2, final boolean a3) {
            /*SL:1464*/this.offset = Branch.shiftOffset(this.pos, this.offset, a1, a2, a3);
            /*SL:1465*/super.shift(a1, a2, a3);
        }
        
        @Override
        int write(final int a1, final byte[] a2, final int a3, final byte[] a4) {
            /*SL:1469*/a4[a3] = a2[a1];
            /*SL:1470*/ByteArray.write32bit(this.offset, a4, a3 + 1);
            /*SL:1471*/return 5;
        }
    }
    
    abstract static class Switcher extends Branch
    {
        int gap;
        int defaultByte;
        int[] offsets;
        Pointers pointers;
        
        Switcher(final int a1, final int a2, final int[] a3, final Pointers a4) {
            super(a1);
            this.gap = 3 - (a1 & 0x3);
            this.defaultByte = a2;
            this.offsets = a3;
            this.pointers = a4;
        }
        
        @Override
        void shift(final int a3, final int v1, final boolean v2) {
            final int v3 = /*EL:1489*/this.pos;
            /*SL:1490*/this.defaultByte = Branch.shiftOffset(v3, this.defaultByte, a3, v1, v2);
            /*SL:1492*/for (int v4 = this.offsets.length, a4 = 0; a4 < v4; ++a4) {
                /*SL:1493*/this.offsets[a4] = Branch.shiftOffset(v3, this.offsets[a4], a3, v1, v2);
            }
            /*SL:1495*/super.shift(a3, v1, v2);
        }
        
        @Override
        int gapChanged() {
            final int v0 = /*EL:1499*/3 - (this.pos & 0x3);
            /*SL:1500*/if (v0 > this.gap) {
                final int v = /*EL:1501*/v0 - this.gap;
                /*SL:1502*/this.gap = v0;
                /*SL:1503*/return v;
            }
            /*SL:1506*/return 0;
        }
        
        @Override
        int deltaSize() {
            /*SL:1510*/return this.gap - (3 - (this.orgPos & 0x3));
        }
        
        @Override
        int write(final int a1, final byte[] a2, int a3, final byte[] a4) throws BadBytecode {
            int v1 = /*EL:1514*/3 - (this.pos & 0x3);
            int v2 = /*EL:1515*/this.gap - v1;
            final int v3 = /*EL:1516*/5 + (3 - (this.orgPos & 0x3)) + this.tableSize();
            /*SL:1517*/if (v2 > 0) {
                /*SL:1518*/this.adjustOffsets(v3, v2);
            }
            /*SL:1520*/a4[a3++] = a2[a1];
            /*SL:1521*/while (v1-- > 0) {
                /*SL:1522*/a4[a3++] = 0;
            }
            /*SL:1524*/ByteArray.write32bit(this.defaultByte, a4, a3);
            final int v4 = /*EL:1525*/this.write2(a3 + 4, a4);
            /*SL:1526*/a3 += v4 + 4;
            /*SL:1527*/while (v2-- > 0) {
                /*SL:1528*/a4[a3++] = 0;
            }
            /*SL:1530*/return 5 + (3 - (this.orgPos & 0x3)) + v4;
        }
        
        abstract int write2(final int p0, final byte[] p1);
        
        abstract int tableSize();
        
        void adjustOffsets(final int v1, final int v2) throws BadBytecode {
            /*SL:1546*/this.pointers.shiftForSwitch(this.pos + v1, v2);
            /*SL:1547*/if (this.defaultByte == v1) {
                /*SL:1548*/this.defaultByte -= v2;
            }
            /*SL:1550*/for (int a1 = 0; a1 < this.offsets.length; ++a1) {
                /*SL:1551*/if (this.offsets[a1] == v1) {
                    final int[] offsets = /*EL:1552*/this.offsets;
                    final int n = a1;
                    offsets[n] -= v2;
                }
            }
        }
    }
    
    static class Table extends Switcher
    {
        int low;
        int high;
        
        Table(final int a1, final int a2, final int a3, final int a4, final int[] a5, final Pointers a6) {
            super(a1, a2, a5, a6);
            this.low = a3;
            this.high = a4;
        }
        
        @Override
        int write2(int v1, final byte[] v2) {
            /*SL:1566*/ByteArray.write32bit(this.low, v2, v1);
            /*SL:1567*/ByteArray.write32bit(this.high, v2, v1 + 4);
            final int v3 = /*EL:1568*/this.offsets.length;
            /*SL:1569*/v1 += 8;
            /*SL:1570*/for (int a1 = 0; a1 < v3; ++a1) {
                /*SL:1571*/ByteArray.write32bit(this.offsets[a1], v2, v1);
                /*SL:1572*/v1 += 4;
            }
            /*SL:1575*/return 8 + 4 * v3;
        }
        
        @Override
        int tableSize() {
            /*SL:1578*/return 8 + 4 * this.offsets.length;
        }
    }
    
    static class Lookup extends Switcher
    {
        int[] matches;
        
        Lookup(final int a1, final int a2, final int[] a3, final int[] a4, final Pointers a5) {
            super(a1, a2, a4, a5);
            this.matches = a3;
        }
        
        @Override
        int write2(int v1, final byte[] v2) {
            final int v3 = /*EL:1590*/this.matches.length;
            /*SL:1591*/ByteArray.write32bit(v3, v2, v1);
            /*SL:1592*/v1 += 4;
            /*SL:1593*/for (int a1 = 0; a1 < v3; ++a1) {
                /*SL:1594*/ByteArray.write32bit(this.matches[a1], v2, v1);
                /*SL:1595*/ByteArray.write32bit(this.offsets[a1], v2, v1 + 4);
                /*SL:1596*/v1 += 8;
            }
            /*SL:1599*/return 4 + 8 * v3;
        }
        
        @Override
        int tableSize() {
            /*SL:1602*/return 4 + 8 * this.matches.length;
        }
    }
}
