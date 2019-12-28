package javassist.bytecode;

import javassist.CtMethod;
import java.io.PrintStream;

public class InstructionPrinter implements Opcode
{
    private static final String[] opcodes;
    private final PrintStream stream;
    
    public InstructionPrinter(final PrintStream a1) {
        this.stream = a1;
    }
    
    public static void print(final CtMethod a1, final PrintStream a2) {
        /*SL:43*/new InstructionPrinter(a2).print(a1);
    }
    
    public void print(final CtMethod v-5) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-5 */
        //     1: invokevirtual   javassist/CtMethod.getMethodInfo2:()Ljavassist/bytecode/MethodInfo;
        //     4: astore_2        /* v-4 */
        //     5: aload_2         /* v-4 */
        //     6: invokevirtual   javassist/bytecode/MethodInfo.getConstPool:()Ljavassist/bytecode/ConstPool;
        //     9: astore_3        /* v-3 */
        //    10: aload_2         /* v-4 */
        //    11: invokevirtual   javassist/bytecode/MethodInfo.getCodeAttribute:()Ljavassist/bytecode/CodeAttribute;
        //    14: astore          v-2
        //    16: aload           v-2
        //    18: ifnonnull       22
        //    21: return         
        //    22: aload           v-2
        //    24: invokevirtual   javassist/bytecode/CodeAttribute.iterator:()Ljavassist/bytecode/CodeIterator;
        //    27: astore          v-1
        //    29: aload           v-1
        //    31: invokevirtual   javassist/bytecode/CodeIterator.hasNext:()Z
        //    34: ifeq            100
        //    37: aload           v-1
        //    39: invokevirtual   javassist/bytecode/CodeIterator.next:()I
        //    42: istore          a1
        //    44: goto            59
        //    47: astore          v1
        //    49: new             Ljava/lang/RuntimeException;
        //    52: dup            
        //    53: aload           v1
        //    55: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //    58: athrow         
        //    59: aload_0         /* v-6 */
        //    60: getfield        javassist/bytecode/InstructionPrinter.stream:Ljava/io/PrintStream;
        //    63: new             Ljava/lang/StringBuilder;
        //    66: dup            
        //    67: invokespecial   java/lang/StringBuilder.<init>:()V
        //    70: iload           v0
        //    72: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    75: ldc             ": "
        //    77: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    80: aload           v-1
        //    82: iload           v0
        //    84: aload_3         /* v-3 */
        //    85: invokestatic    javassist/bytecode/InstructionPrinter.instructionString:(Ljavassist/bytecode/CodeIterator;ILjavassist/bytecode/ConstPool;)Ljava/lang/String;
        //    88: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    91: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    94: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //    97: goto            29
        //   100: return         
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ---------------------------------------
        //  44     3       6     a1    I
        //  49     10      7     v1    Ljavassist/bytecode/BadBytecode;
        //  59     38      6     v0    I
        //  0      101     0     v-6   Ljavassist/bytecode/InstructionPrinter;
        //  0      101     1     v-5   Ljavassist/CtMethod;
        //  5      96      2     v-4   Ljavassist/bytecode/MethodInfo;
        //  10     91      3     v-3   Ljavassist/bytecode/ConstPool;
        //  16     85      4     v-2   Ljavassist/bytecode/CodeAttribute;
        //  29     72      5     v-1   Ljavassist/bytecode/CodeIterator;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  37     44     47     59     Ljavassist/bytecode/BadBytecode;
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
    
    public static String instructionString(final CodeIterator a1, final int a2, final ConstPool a3) {
        final int v1 = /*EL:74*/a1.byteAt(a2);
        /*SL:76*/if (v1 > InstructionPrinter.opcodes.length || v1 < 0) {
            /*SL:77*/throw new IllegalArgumentException("Invalid opcode, opcode: " + v1 + " pos: " + a2);
        }
        final String v2 = /*EL:79*/InstructionPrinter.opcodes[v1];
        /*SL:80*/switch (v1) {
            case 16: {
                /*SL:82*/return v2 + " " + a1.byteAt(a2 + 1);
            }
            case 17: {
                /*SL:84*/return v2 + " " + a1.s16bitAt(a2 + 1);
            }
            case 18: {
                /*SL:86*/return v2 + " " + ldc(a3, a1.byteAt(a2 + 1));
            }
            case 19:
            case 20: {
                /*SL:89*/return v2 + " " + ldc(a3, a1.u16bitAt(a2 + 1));
            }
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58: {
                /*SL:100*/return v2 + " " + a1.byteAt(a2 + 1);
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 198:
            case 199: {
                /*SL:117*/return v2 + " " + (a1.s16bitAt(a2 + 1) + a2);
            }
            case 132: {
                /*SL:119*/return v2 + " " + a1.byteAt(a2 + 1) + ", " + a1.signedByteAt(a2 + 2);
            }
            case 167:
            case 168: {
                /*SL:122*/return v2 + " " + (a1.s16bitAt(a2 + 1) + a2);
            }
            case 169: {
                /*SL:124*/return v2 + " " + a1.byteAt(a2 + 1);
            }
            case 170: {
                /*SL:126*/return tableSwitch(a1, a2);
            }
            case 171: {
                /*SL:128*/return lookupSwitch(a1, a2);
            }
            case 178:
            case 179:
            case 180:
            case 181: {
                /*SL:133*/return v2 + " " + fieldInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 182:
            case 183:
            case 184: {
                /*SL:137*/return v2 + " " + methodInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 185: {
                /*SL:139*/return v2 + " " + interfaceMethodInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 186: {
                /*SL:141*/return v2 + " " + a1.u16bitAt(a2 + 1);
            }
            case 187: {
                /*SL:143*/return v2 + " " + classInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 188: {
                /*SL:145*/return v2 + " " + arrayInfo(a1.byteAt(a2 + 1));
            }
            case 189:
            case 192: {
                /*SL:148*/return v2 + " " + classInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 196: {
                /*SL:150*/return wide(a1, a2);
            }
            case 197: {
                /*SL:152*/return v2 + " " + classInfo(a3, a1.u16bitAt(a2 + 1));
            }
            case 200:
            case 201: {
                /*SL:155*/return v2 + " " + (a1.s32bitAt(a2 + 1) + a2);
            }
            default: {
                /*SL:157*/return v2;
            }
        }
    }
    
    private static String wide(final CodeIterator a1, final int a2) {
        final int v1 = /*EL:163*/a1.byteAt(a2 + 1);
        final int v2 = /*EL:164*/a1.u16bitAt(a2 + 2);
        /*SL:165*/switch (v1) {
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 132:
            case 169: {
                /*SL:178*/return InstructionPrinter.opcodes[v1] + " " + v2;
            }
            default: {
                /*SL:180*/throw new RuntimeException("Invalid WIDE operand");
            }
        }
    }
    
    private static String arrayInfo(final int a1) {
        /*SL:186*/switch (a1) {
            case 4: {
                /*SL:188*/return "boolean";
            }
            case 5: {
                /*SL:190*/return "char";
            }
            case 8: {
                /*SL:192*/return "byte";
            }
            case 9: {
                /*SL:194*/return "short";
            }
            case 10: {
                /*SL:196*/return "int";
            }
            case 11: {
                /*SL:198*/return "long";
            }
            case 6: {
                /*SL:200*/return "float";
            }
            case 7: {
                /*SL:202*/return "double";
            }
            default: {
                /*SL:204*/throw new RuntimeException("Invalid array type");
            }
        }
    }
    
    private static String classInfo(final ConstPool a1, final int a2) {
        /*SL:210*/return "#" + a2 + " = Class " + a1.getClassInfo(a2);
    }
    
    private static String interfaceMethodInfo(final ConstPool a1, final int a2) {
        /*SL:218*/return "#" + a2 + " = Method " + a1.getInterfaceMethodrefClassName(a2) + "." + a1.getInterfaceMethodrefName(a2) + "(" + a1.getInterfaceMethodrefType(a2) + ")";
    }
    
    private static String methodInfo(final ConstPool a1, final int a2) {
        /*SL:225*/return "#" + a2 + " = Method " + a1.getMethodrefClassName(a2) + "." + a1.getMethodrefName(a2) + "(" + a1.getMethodrefType(a2) + ")";
    }
    
    private static String fieldInfo(final ConstPool a1, final int a2) {
        /*SL:233*/return "#" + a2 + " = Field " + a1.getFieldrefClassName(a2) + "." + a1.getFieldrefName(a2) + "(" + a1.getFieldrefType(a2) + ")";
    }
    
    private static String lookupSwitch(final CodeIterator v1, final int v2) {
        final StringBuffer v3 = /*EL:238*/new StringBuffer("lookupswitch {\n");
        int v4 = /*EL:239*/(v2 & 0xFFFFFFFC) + 4;
        /*SL:241*/v3.append("\t\tdefault: ").append(v2 + v1.s32bitAt(v4)).append("\n");
        /*SL:242*/v4 += 4;
        final int v5 = v1.s32bitAt(v4);
        final int n = /*EL:243*/v5 * 8;
        v4 += 4;
        /*SL:245*/for (int v6 = n + v4; v4 < v6; v4 += 8) {
            final int a1 = /*EL:246*/v1.s32bitAt(v4);
            final int a2 = /*EL:247*/v1.s32bitAt(v4 + 4) + v2;
            /*SL:248*/v3.append("\t\t").append(a1).append(": ").append(a2).append("\n");
        }
        /*SL:251*/v3.setCharAt(v3.length() - 1, '}');
        /*SL:252*/return v3.toString();
    }
    
    private static String tableSwitch(final CodeIterator v1, final int v2) {
        final StringBuffer v3 = /*EL:257*/new StringBuffer("tableswitch {\n");
        int v4 = /*EL:258*/(v2 & 0xFFFFFFFC) + 4;
        /*SL:260*/v3.append("\t\tdefault: ").append(v2 + v1.s32bitAt(v4)).append("\n");
        /*SL:261*/v4 += 4;
        final int v5 = v1.s32bitAt(v4);
        /*SL:262*/v4 += 4;
        final int v6 = v1.s32bitAt(v4);
        final int n = /*EL:263*/(v6 - v5 + 1) * 4;
        v4 += 4;
        /*SL:266*/for (int v7 = n + v4, a2 = v5; v4 < v7; v4 += 4, ++a2) {
            /*SL:267*/a2 = v1.s32bitAt(v4) + v2;
            /*SL:268*/v3.append("\t\t").append(a2).append(": ").append(a2).append("\n");
        }
        /*SL:271*/v3.setCharAt(v3.length() - 1, '}');
        /*SL:272*/return v3.toString();
    }
    
    private static String ldc(final ConstPool a1, final int a2) {
        final int v1 = /*EL:277*/a1.getTag(a2);
        /*SL:278*/switch (v1) {
            case 8: {
                /*SL:280*/return "#" + a2 + " = \"" + a1.getStringInfo(a2) + "\"";
            }
            case 3: {
                /*SL:282*/return "#" + a2 + " = int " + a1.getIntegerInfo(a2);
            }
            case 4: {
                /*SL:284*/return "#" + a2 + " = float " + a1.getFloatInfo(a2);
            }
            case 5: {
                /*SL:286*/return "#" + a2 + " = long " + a1.getLongInfo(a2);
            }
            case 6: {
                /*SL:288*/return "#" + a2 + " = int " + a1.getDoubleInfo(a2);
            }
            case 7: {
                /*SL:290*/return classInfo(a1, a2);
            }
            default: {
                /*SL:292*/throw new RuntimeException("bad LDC: " + v1);
            }
        }
    }
    
    static {
        opcodes = Mnemonic.OPCODE;
    }
}
