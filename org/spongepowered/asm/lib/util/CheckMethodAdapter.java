package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.Opcodes;
import java.util.Iterator;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.spongepowered.asm.lib.tree.analysis.Interpreter;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.spongepowered.asm.lib.tree.analysis.BasicVerifier;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.lib.Label;
import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class CheckMethodAdapter extends MethodVisitor
{
    public int version;
    private int access;
    private boolean startCode;
    private boolean endCode;
    private boolean endMethod;
    private int insnCount;
    private final Map<Label, Integer> labels;
    private Set<Label> usedLabels;
    private int expandedFrames;
    private int compressedFrames;
    private int lastFrame;
    private List<Label> handlers;
    private static final int[] TYPE;
    private static Field labelStatusField;
    
    public CheckMethodAdapter(final MethodVisitor a1) {
        this(a1, new HashMap<Label, Integer>());
    }
    
    public CheckMethodAdapter(final MethodVisitor a1, final Map<Label, Integer> a2) {
        this(327680, a1, a2);
        if (this.getClass() != CheckMethodAdapter.class) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckMethodAdapter(final int a1, final MethodVisitor a2, final Map<Label, Integer> a3) {
        super(a1, a2);
        this.lastFrame = -1;
        this.labels = a3;
        this.usedLabels = new HashSet<Label>();
        this.handlers = new ArrayList<Label>();
    }
    
    public CheckMethodAdapter(final int a1, final String a2, final String a3, final MethodVisitor a4, final Map<Label, Integer> a5) {
        this(new MethodNode(327680, a1, a2, a3, null, null) {
            public void visitEnd() {
                final Analyzer<BasicValue> v-6 = /*EL:450*/new Analyzer<BasicValue>(new BasicVerifier());
                try {
                    /*SL:453*/v-6.analyze("dummy", this);
                }
                catch (Exception v0) {
                    /*SL:455*/if (v0 instanceof IndexOutOfBoundsException && this.maxLocals == 0 && this.maxStack == 0) {
                        /*SL:457*/throw new RuntimeException("Data flow checking option requires valid, non zero maxLocals and maxStack values.");
                    }
                    /*SL:460*/v0.printStackTrace();
                    final StringWriter v = /*EL:461*/new StringWriter();
                    final PrintWriter v2 = /*EL:462*/new PrintWriter(v, true);
                    /*SL:463*/CheckClassAdapter.printAnalyzerResult(this, v-6, v2);
                    /*SL:464*/v2.close();
                    /*SL:465*/throw new RuntimeException(v0.getMessage() + ' ' + v.toString());
                }
                /*SL:468*/this.accept(a4);
            }
        }, a5);
        this.access = a1;
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:476*/if (a1 != null) {
            checkUnqualifiedName(/*EL:477*/this.version, a1, "name");
        }
        /*SL:479*/CheckClassAdapter.checkAccess(a2, 36880);
        /*SL:481*/super.visitParameter(a1, a2);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:487*/this.checkEndMethod();
        checkDesc(/*EL:488*/a1, false);
        /*SL:489*/return new CheckAnnotationAdapter(super.visitAnnotation(a1, a2));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:495*/this.checkEndMethod();
        final int v1 = /*EL:496*/a1 >>> 24;
        /*SL:497*/if (v1 != 1 && v1 != 18 && v1 != 20 && v1 != 21 && v1 != 22 && v1 != 23) {
            /*SL:503*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:504*/Integer.toHexString(v1));
        }
        /*SL:506*/CheckClassAdapter.checkTypeRefAndPath(a1, a2);
        checkDesc(/*EL:507*/a3, false);
        /*SL:508*/return new CheckAnnotationAdapter(super.visitTypeAnnotation(a1, a2, a3, a4));
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        /*SL:514*/this.checkEndMethod();
        /*SL:515*/return new CheckAnnotationAdapter(super.visitAnnotationDefault(), false);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        /*SL:521*/this.checkEndMethod();
        checkDesc(/*EL:522*/a2, false);
        /*SL:523*/return new CheckAnnotationAdapter(super.visitParameterAnnotation(a1, a2, a3));
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:529*/this.checkEndMethod();
        /*SL:530*/if (a1 == null) {
            /*SL:531*/throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        /*SL:534*/super.visitAttribute(a1);
    }
    
    public void visitCode() {
        /*SL:539*/if ((this.access & 0x400) != 0x0) {
            /*SL:540*/throw new RuntimeException("Abstract methods cannot have code");
        }
        /*SL:542*/this.startCode = true;
        /*SL:543*/super.visitCode();
    }
    
    public void visitFrame(final int v-5, final int v-4, final Object[] v-3, final int v-2, final Object[] v-1) {
        /*SL:549*/if (this.insnCount == this.lastFrame) {
            /*SL:550*/throw new IllegalStateException("At most one frame can be visited at a given code location.");
        }
        /*SL:553*/this.lastFrame = this.insnCount;
        int v1 = 0;
        final int v2;
        /*SL:556*/switch (v-5) {
            case -1:
            case 0: {
                final int a1 = /*EL:559*/Integer.MAX_VALUE;
                final int a2 = /*EL:560*/Integer.MAX_VALUE;
                /*SL:561*/break;
            }
            case 3: {
                final int a3 = /*EL:564*/0;
                final int a4 = /*EL:565*/0;
                /*SL:566*/break;
            }
            case 4: {
                final int a5 = /*EL:569*/0;
                /*SL:570*/v1 = 1;
                /*SL:571*/break;
            }
            case 1:
            case 2: {
                /*SL:575*/v2 = 3;
                /*SL:576*/v1 = 0;
                /*SL:577*/break;
            }
            default: {
                /*SL:580*/throw new IllegalArgumentException("Invalid frame type " + v-5);
            }
        }
        /*SL:583*/if (v-4 > v2) {
            /*SL:584*/throw new IllegalArgumentException("Invalid nLocal=" + v-4 + " for frame type " + v-5);
        }
        /*SL:587*/if (v-2 > v1) {
            /*SL:588*/throw new IllegalArgumentException("Invalid nStack=" + v-2 + " for frame type " + v-5);
        }
        /*SL:592*/if (v-5 != 2) {
            /*SL:593*/if (v-4 > 0 && (v-3 == null || v-3.length < v-4)) {
                /*SL:594*/throw new IllegalArgumentException("Array local[] is shorter than nLocal");
            }
            /*SL:597*/for (int v3 = 0; v3 < v-4; ++v3) {
                /*SL:598*/this.checkFrameValue(v-3[v3]);
            }
        }
        /*SL:601*/if (v-2 > 0 && (v-1 == null || v-1.length < v-2)) {
            /*SL:602*/throw new IllegalArgumentException("Array stack[] is shorter than nStack");
        }
        /*SL:605*/for (int v3 = 0; v3 < v-2; ++v3) {
            /*SL:606*/this.checkFrameValue(v-1[v3]);
        }
        /*SL:608*/if (v-5 == -1) {
            /*SL:609*/++this.expandedFrames;
        }
        else {
            /*SL:611*/++this.compressedFrames;
        }
        /*SL:613*/if (this.expandedFrames > 0 && this.compressedFrames > 0) {
            /*SL:614*/throw new RuntimeException("Expanded and compressed frames must not be mixed.");
        }
        /*SL:617*/super.visitFrame(v-5, v-4, v-3, v-2, v-1);
    }
    
    public void visitInsn(final int a1) {
        /*SL:622*/this.checkStartCode();
        /*SL:623*/this.checkEndCode();
        checkOpcode(/*EL:624*/a1, 0);
        /*SL:625*/super.visitInsn(a1);
        /*SL:626*/++this.insnCount;
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:631*/this.checkStartCode();
        /*SL:632*/this.checkEndCode();
        checkOpcode(/*EL:633*/a1, 1);
        /*SL:634*/switch (a1) {
            case 16: {
                checkSignedByte(/*EL:636*/a2, "Invalid operand");
                /*SL:637*/break;
            }
            case 17: {
                checkSignedShort(/*EL:639*/a2, "Invalid operand");
                /*SL:640*/break;
            }
            default: {
                /*SL:643*/if (a2 < 4 || a2 > 11) {
                    /*SL:644*/throw new IllegalArgumentException("Invalid operand (must be an array type code T_...): " + a2);
                }
                break;
            }
        }
        /*SL:649*/super.visitIntInsn(a1, a2);
        /*SL:650*/++this.insnCount;
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:655*/this.checkStartCode();
        /*SL:656*/this.checkEndCode();
        checkOpcode(/*EL:657*/a1, 2);
        checkUnsignedShort(/*EL:658*/a2, "Invalid variable index");
        /*SL:659*/super.visitVarInsn(a1, a2);
        /*SL:660*/++this.insnCount;
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:665*/this.checkStartCode();
        /*SL:666*/this.checkEndCode();
        checkOpcode(/*EL:667*/a1, 3);
        checkInternalName(/*EL:668*/a2, "type");
        /*SL:669*/if (a1 == 187 && a2.charAt(0) == '[') {
            /*SL:670*/throw new IllegalArgumentException("NEW cannot be used to create arrays: " + a2);
        }
        /*SL:673*/super.visitTypeInsn(a1, a2);
        /*SL:674*/++this.insnCount;
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:680*/this.checkStartCode();
        /*SL:681*/this.checkEndCode();
        checkOpcode(/*EL:682*/a1, 4);
        checkInternalName(/*EL:683*/a2, "owner");
        checkUnqualifiedName(/*EL:684*/this.version, a3, "name");
        checkDesc(/*EL:685*/a4, false);
        /*SL:686*/super.visitFieldInsn(a1, a2, a3, a4);
        /*SL:687*/++this.insnCount;
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:694*/if (this.api >= 327680) {
            /*SL:695*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:696*/return;
        }
        /*SL:698*/this.doVisitMethodInsn(a1, a2, a3, a4, a1 == 185);
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:705*/if (this.api < 327680) {
            /*SL:706*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:707*/return;
        }
        /*SL:709*/this.doVisitMethodInsn(a1, a2, a3, a4, a5);
    }
    
    private void doVisitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:714*/this.checkStartCode();
        /*SL:715*/this.checkEndCode();
        checkOpcode(/*EL:716*/a1, 5);
        /*SL:717*/if (a1 != 183 || !"<init>".equals(a3)) {
            checkMethodIdentifier(/*EL:718*/this.version, a3, "name");
        }
        checkInternalName(/*EL:720*/a2, "owner");
        checkMethodDesc(/*EL:721*/a4);
        /*SL:722*/if (a1 == 182 && a5) {
            /*SL:723*/throw new IllegalArgumentException("INVOKEVIRTUAL can't be used with interfaces");
        }
        /*SL:726*/if (a1 == 185 && !a5) {
            /*SL:727*/throw new IllegalArgumentException("INVOKEINTERFACE can't be used with classes");
        }
        /*SL:730*/if (a1 == 183 && a5 && (this.version & 0xFFFF) < 52) {
            /*SL:732*/throw new IllegalArgumentException("INVOKESPECIAL can't be used with interfaces prior to Java 8");
        }
        /*SL:740*/if (this.mv != null) {
            /*SL:741*/this.mv.visitMethodInsn(a1, a2, a3, a4, a5);
        }
        /*SL:743*/++this.insnCount;
    }
    
    public void visitInvokeDynamicInsn(final String a3, final String a4, final Handle v1, final Object... v2) {
        /*SL:749*/this.checkStartCode();
        /*SL:750*/this.checkEndCode();
        checkMethodIdentifier(/*EL:751*/this.version, a3, "name");
        checkMethodDesc(/*EL:752*/a4);
        /*SL:753*/if (v1.getTag() != 6 && v1.getTag() != /*EL:754*/8) {
            /*SL:755*/throw new IllegalArgumentException("invalid handle tag " + v1.getTag());
        }
        /*SL:758*/for (int a5 = 0; a5 < v2.length; ++a5) {
            /*SL:759*/this.checkLDCConstant(v2[a5]);
        }
        /*SL:761*/super.visitInvokeDynamicInsn(a3, a4, v1, v2);
        /*SL:762*/++this.insnCount;
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:767*/this.checkStartCode();
        /*SL:768*/this.checkEndCode();
        checkOpcode(/*EL:769*/a1, 6);
        /*SL:770*/this.checkLabel(a2, false, "label");
        checkNonDebugLabel(/*EL:771*/a2);
        /*SL:772*/super.visitJumpInsn(a1, a2);
        /*SL:773*/this.usedLabels.add(a2);
        /*SL:774*/++this.insnCount;
    }
    
    public void visitLabel(final Label a1) {
        /*SL:779*/this.checkStartCode();
        /*SL:780*/this.checkEndCode();
        /*SL:781*/this.checkLabel(a1, false, "label");
        /*SL:782*/if (this.labels.get(a1) != null) {
            /*SL:783*/throw new IllegalArgumentException("Already visited label");
        }
        /*SL:785*/this.labels.put(a1, this.insnCount);
        /*SL:786*/super.visitLabel(a1);
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:791*/this.checkStartCode();
        /*SL:792*/this.checkEndCode();
        /*SL:793*/this.checkLDCConstant(a1);
        /*SL:794*/super.visitLdcInsn(a1);
        /*SL:795*/++this.insnCount;
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:800*/this.checkStartCode();
        /*SL:801*/this.checkEndCode();
        checkUnsignedShort(/*EL:802*/a1, "Invalid variable index");
        checkSignedShort(/*EL:803*/a2, "Invalid increment");
        /*SL:804*/super.visitIincInsn(a1, a2);
        /*SL:805*/++this.insnCount;
    }
    
    public void visitTableSwitchInsn(final int a4, final int v1, final Label v2, final Label... v3) {
        /*SL:811*/this.checkStartCode();
        /*SL:812*/this.checkEndCode();
        /*SL:813*/if (v1 < a4) {
            /*SL:814*/throw new IllegalArgumentException("Max = " + v1 + " must be greater than or equal to min = " + a4);
        }
        /*SL:817*/this.checkLabel(v2, false, "default label");
        checkNonDebugLabel(/*EL:818*/v2);
        /*SL:819*/if (v3 == null || v3.length != v1 - a4 + 1) {
            /*SL:820*/throw new IllegalArgumentException("There must be max - min + 1 labels");
        }
        /*SL:823*/for (int a5 = 0; a5 < v3.length; ++a5) {
            /*SL:824*/this.checkLabel(v3[a5], false, "label at index " + a5);
            checkNonDebugLabel(/*EL:825*/v3[a5]);
        }
        /*SL:827*/super.visitTableSwitchInsn(a4, v1, v2, v3);
        /*SL:828*/for (int a6 = 0; a6 < v3.length; ++a6) {
            /*SL:829*/this.usedLabels.add(v3[a6]);
        }
        /*SL:831*/++this.insnCount;
    }
    
    public void visitLookupSwitchInsn(final Label v1, final int[] v2, final Label[] v3) {
        /*SL:837*/this.checkEndCode();
        /*SL:838*/this.checkStartCode();
        /*SL:839*/this.checkLabel(v1, false, "default label");
        checkNonDebugLabel(/*EL:840*/v1);
        /*SL:841*/if (v2 == null || v3 == null || v2.length != v3.length) {
            /*SL:842*/throw new IllegalArgumentException("There must be the same number of keys and labels");
        }
        /*SL:845*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:846*/this.checkLabel(v3[a1], false, "label at index " + a1);
            checkNonDebugLabel(/*EL:847*/v3[a1]);
        }
        /*SL:849*/super.visitLookupSwitchInsn(v1, v2, v3);
        /*SL:850*/this.usedLabels.add(v1);
        /*SL:851*/for (int a2 = 0; a2 < v3.length; ++a2) {
            /*SL:852*/this.usedLabels.add(v3[a2]);
        }
        /*SL:854*/++this.insnCount;
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:859*/this.checkStartCode();
        /*SL:860*/this.checkEndCode();
        checkDesc(/*EL:861*/a1, false);
        /*SL:862*/if (a1.charAt(0) != '[') {
            /*SL:863*/throw new IllegalArgumentException("Invalid descriptor (must be an array type descriptor): " + a1);
        }
        /*SL:867*/if (a2 < 1) {
            /*SL:868*/throw new IllegalArgumentException("Invalid dimensions (must be greater than 0): " + a2);
        }
        /*SL:871*/if (a2 > a1.lastIndexOf(91) + 1) {
            /*SL:872*/throw new IllegalArgumentException("Invalid dimensions (must not be greater than dims(desc)): " + a2);
        }
        /*SL:876*/super.visitMultiANewArrayInsn(a1, a2);
        /*SL:877*/++this.insnCount;
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:883*/this.checkStartCode();
        /*SL:884*/this.checkEndCode();
        final int v1 = /*EL:885*/a1 >>> 24;
        /*SL:886*/if (v1 != 67 && v1 != 68 && v1 != 69 && v1 != 70 && v1 != 71 && v1 != 72 && v1 != 73 && v1 != 74 && v1 != 75) {
            /*SL:894*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:895*/Integer.toHexString(v1));
        }
        /*SL:897*/CheckClassAdapter.checkTypeRefAndPath(a1, a2);
        checkDesc(/*EL:898*/a3, false);
        /*SL:899*/return new CheckAnnotationAdapter(super.visitInsnAnnotation(a1, a2, a3, a4));
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:906*/this.checkStartCode();
        /*SL:907*/this.checkEndCode();
        /*SL:908*/this.checkLabel(a1, false, "start label");
        /*SL:909*/this.checkLabel(a2, false, "end label");
        /*SL:910*/this.checkLabel(a3, false, "handler label");
        checkNonDebugLabel(/*EL:911*/a1);
        checkNonDebugLabel(/*EL:912*/a2);
        checkNonDebugLabel(/*EL:913*/a3);
        /*SL:914*/if (this.labels.get(a1) != null || this.labels.get(a2) != null || this.labels.get(a3) != /*EL:915*/null) {
            /*SL:916*/throw new IllegalStateException("Try catch blocks must be visited before their labels");
        }
        /*SL:919*/if (a4 != null) {
            checkInternalName(/*EL:920*/a4, "type");
        }
        /*SL:922*/super.visitTryCatchBlock(a1, a2, a3, a4);
        /*SL:923*/this.handlers.add(a1);
        /*SL:924*/this.handlers.add(a2);
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:930*/this.checkStartCode();
        /*SL:931*/this.checkEndCode();
        final int v1 = /*EL:932*/a1 >>> 24;
        /*SL:933*/if (v1 != 66) {
            /*SL:934*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:935*/Integer.toHexString(v1));
        }
        /*SL:937*/CheckClassAdapter.checkTypeRefAndPath(a1, a2);
        checkDesc(/*EL:938*/a3, false);
        /*SL:939*/return new CheckAnnotationAdapter(super.visitTryCatchAnnotation(a1, a2, a3, a4));
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:947*/this.checkStartCode();
        /*SL:948*/this.checkEndCode();
        checkUnqualifiedName(/*EL:949*/this.version, a1, "name");
        checkDesc(/*EL:950*/a2, false);
        /*SL:951*/this.checkLabel(a4, true, "start label");
        /*SL:952*/this.checkLabel(a5, true, "end label");
        checkUnsignedShort(/*EL:953*/a6, "Invalid variable index");
        final int v1 = /*EL:954*/this.labels.get(a4);
        final int v2 = /*EL:955*/this.labels.get(a5);
        /*SL:956*/if (v2 < v1) {
            /*SL:957*/throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
        }
        /*SL:960*/super.visitLocalVariable(a1, a2, a3, a4, a5, a6);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a5, final TypePath a6, final Label[] a7, final Label[] v1, final int[] v2, final String v3, final boolean v4) {
        /*SL:967*/this.checkStartCode();
        /*SL:968*/this.checkEndCode();
        final int v5 = /*EL:969*/a5 >>> 24;
        /*SL:970*/if (v5 != 64 && v5 != 65) {
            /*SL:972*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:973*/Integer.toHexString(v5));
        }
        /*SL:975*/CheckClassAdapter.checkTypeRefAndPath(a5, a6);
        checkDesc(/*EL:976*/v3, false);
        /*SL:977*/if (a7 == null || v1 == null || v2 == null || v1.length != a7.length || v2.length != a7.length) {
            /*SL:979*/throw new IllegalArgumentException("Invalid start, end and index arrays (must be non null and of identical length");
        }
        /*SL:982*/for (int a8 = 0; a8 < a7.length; ++a8) {
            /*SL:983*/this.checkLabel(a7[a8], true, "start label");
            /*SL:984*/this.checkLabel(v1[a8], true, "end label");
            checkUnsignedShort(/*EL:985*/v2[a8], "Invalid variable index");
            final int a9 = /*EL:986*/this.labels.get(a7[a8]);
            final int a10 = /*EL:987*/this.labels.get(v1[a8]);
            /*SL:988*/if (a10 < a9) {
                /*SL:989*/throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
            }
        }
        /*SL:993*/return super.visitLocalVariableAnnotation(a5, a6, a7, v1, v2, v3, v4);
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:999*/this.checkStartCode();
        /*SL:1000*/this.checkEndCode();
        checkUnsignedShort(/*EL:1001*/a1, "Invalid line number");
        /*SL:1002*/this.checkLabel(a2, true, "start label");
        /*SL:1003*/super.visitLineNumber(a1, a2);
    }
    
    public void visitMaxs(final int v-3, final int v-2) {
        /*SL:1008*/this.checkStartCode();
        /*SL:1009*/this.checkEndCode();
        /*SL:1010*/this.endCode = true;
        /*SL:1011*/for (final Label a1 : this.usedLabels) {
            /*SL:1012*/if (this.labels.get(a1) == null) {
                /*SL:1013*/throw new IllegalStateException("Undefined label used");
            }
        }
        int i = /*EL:1016*/0;
        while (i < this.handlers.size()) {
            final Integer a2 = /*EL:1017*/this.labels.get(this.handlers.get(i++));
            final Integer v1 = /*EL:1018*/this.labels.get(this.handlers.get(i++));
            /*SL:1019*/if (a2 == null || v1 == null) {
                /*SL:1020*/throw new IllegalStateException("Undefined try catch block labels");
            }
            /*SL:1023*/if (v1 <= a2) {
                /*SL:1024*/throw new IllegalStateException("Emty try catch block handler range");
            }
        }
        checkUnsignedShort(/*EL:1028*/v-3, "Invalid max stack");
        checkUnsignedShort(/*EL:1029*/v-2, "Invalid max locals");
        /*SL:1030*/super.visitMaxs(v-3, v-2);
    }
    
    public void visitEnd() {
        /*SL:1035*/this.checkEndMethod();
        /*SL:1036*/this.endMethod = true;
        /*SL:1037*/super.visitEnd();
    }
    
    void checkStartCode() {
        /*SL:1046*/if (!this.startCode) {
            /*SL:1047*/throw new IllegalStateException("Cannot visit instructions before visitCode has been called.");
        }
    }
    
    void checkEndCode() {
        /*SL:1056*/if (this.endCode) {
            /*SL:1057*/throw new IllegalStateException("Cannot visit instructions after visitMaxs has been called.");
        }
    }
    
    void checkEndMethod() {
        /*SL:1066*/if (this.endMethod) {
            /*SL:1067*/throw new IllegalStateException("Cannot visit elements after visitEnd has been called.");
        }
    }
    
    void checkFrameValue(final Object a1) {
        /*SL:1079*/if (a1 == Opcodes.TOP || a1 == Opcodes.INTEGER || a1 == Opcodes.FLOAT || a1 == Opcodes.LONG || a1 == Opcodes.DOUBLE || a1 == Opcodes.NULL || a1 == Opcodes.UNINITIALIZED_THIS) {
            /*SL:1083*/return;
        }
        /*SL:1085*/if (a1 instanceof String) {
            checkInternalName(/*EL:1086*/(String)a1, "Invalid stack frame value");
            /*SL:1087*/return;
        }
        /*SL:1089*/if (!(a1 instanceof Label)) {
            /*SL:1090*/throw new IllegalArgumentException("Invalid stack frame value: " + a1);
        }
        /*SL:1093*/this.usedLabels.add((Label)a1);
    }
    
    static void checkOpcode(final int a1, final int a2) {
        /*SL:1106*/if (a1 < 0 || a1 > 199 || CheckMethodAdapter.TYPE[a1] != a2) {
            /*SL:1107*/throw new IllegalArgumentException("Invalid opcode: " + a1);
        }
    }
    
    static void checkSignedByte(final int a1, final String a2) {
        /*SL:1120*/if (a1 < -128 || a1 > 127) {
            /*SL:1121*/throw new IllegalArgumentException(a2 + " (must be a signed byte): " + a1);
        }
    }
    
    static void checkSignedShort(final int a1, final String a2) {
        /*SL:1135*/if (a1 < -32768 || a1 > 32767) {
            /*SL:1136*/throw new IllegalArgumentException(a2 + " (must be a signed short): " + a1);
        }
    }
    
    static void checkUnsignedShort(final int a1, final String a2) {
        /*SL:1150*/if (a1 < 0 || a1 > 65535) {
            /*SL:1151*/throw new IllegalArgumentException(a2 + " (must be an unsigned short): " + a1);
        }
    }
    
    static void checkConstant(final Object a1) {
        /*SL:1164*/if (!(a1 instanceof Integer) && !(a1 instanceof Float) && !(a1 instanceof Long) && !(a1 instanceof Double) && !(a1 instanceof String)) {
            /*SL:1167*/throw new IllegalArgumentException("Invalid constant: " + a1);
        }
    }
    
    void checkLDCConstant(final Object v0) {
        /*SL:1172*/if (v0 instanceof Type) {
            final int a1 = /*EL:1173*/((Type)v0).getSort();
            /*SL:1174*/if (a1 != 10 && a1 != 9 && a1 != 11) {
                /*SL:1175*/throw new IllegalArgumentException("Illegal LDC constant value");
            }
            /*SL:1177*/if (a1 != 11 && (this.version & 0xFFFF) < 49) {
                /*SL:1178*/throw new IllegalArgumentException("ldc of a constant class requires at least version 1.5");
            }
            /*SL:1181*/if (a1 == 11 && (this.version & 0xFFFF) < 51) {
                /*SL:1182*/throw new IllegalArgumentException("ldc of a method type requires at least version 1.7");
            }
        }
        else/*SL:1185*/ if (v0 instanceof Handle) {
            /*SL:1186*/if ((this.version & 0xFFFF) < 51) {
                /*SL:1187*/throw new IllegalArgumentException("ldc of a handle requires at least version 1.7");
            }
            final int v = /*EL:1190*/((Handle)v0).getTag();
            /*SL:1191*/if (v < 1 || v > 9) {
                /*SL:1192*/throw new IllegalArgumentException("invalid handle tag " + v);
            }
        }
        else {
            checkConstant(/*EL:1195*/v0);
        }
    }
    
    static void checkUnqualifiedName(final int a2, final String a3, final String v1) {
        /*SL:1211*/if ((a2 & 0xFFFF) < 49) {
            checkIdentifier(/*EL:1212*/a3, v1);
        }
        else {
            /*SL:1214*/for (int a4 = 0; a4 < a3.length(); ++a4) {
                /*SL:1215*/if (".;[/".indexOf(a3.charAt(a4)) != -1) {
                    /*SL:1216*/throw new IllegalArgumentException("Invalid " + v1 + " (must be a valid unqualified name): " + a3);
                }
            }
        }
    }
    
    static void checkIdentifier(final String a1, final String a2) {
        checkIdentifier(/*EL:1232*/a1, 0, -1, a2);
    }
    
    static void checkIdentifier(final String a2, final int a3, final int a4, final String v1) {
        /*SL:1251*/if (a2 != null) {
            if (a4 == -1) {
                if (a2.length() <= a3) {
                    /*SL:1252*/throw new IllegalArgumentException("Invalid " + v1 + " (must not be null or empty)");
                }
            }
            else if (a4 <= a3) {
                throw new IllegalArgumentException("Invalid " + v1 + " (must not be null or empty)");
            }
            /*SL:1255*/if (!Character.isJavaIdentifierStart(a2.charAt(a3))) {
                /*SL:1256*/throw new IllegalArgumentException("Invalid " + v1 + " (must be a valid Java identifier): " + a2);
            }
            /*SL:1260*/for (int v2 = (a4 == -1) ? a2.length() : a4, a5 = a3 + 1; a5 < v2; ++a5) {
                /*SL:1261*/if (!Character.isJavaIdentifierPart(a2.charAt(a5))) {
                    /*SL:1262*/throw new IllegalArgumentException("Invalid " + v1 + " (must be a valid Java identifier): " + a2);
                }
            }
            /*SL:1266*/return;
        }
        throw new IllegalArgumentException("Invalid " + v1 + " (must not be null or empty)");
    }
    
    static void checkMethodIdentifier(final int a3, final String v1, final String v2) {
        /*SL:1280*/if (v1 == null || v1.length() == 0) {
            /*SL:1281*/throw new IllegalArgumentException("Invalid " + v2 + " (must not be null or empty)");
        }
        /*SL:1284*/if ((a3 & 0xFFFF) >= 49) {
            /*SL:1285*/for (int a4 = 0; a4 < v1.length(); ++a4) {
                /*SL:1286*/if (".;[/<>".indexOf(v1.charAt(a4)) != -1) {
                    /*SL:1287*/throw new IllegalArgumentException("Invalid " + v2 + " (must be a valid unqualified name): " + v1);
                }
            }
            /*SL:1291*/return;
        }
        /*SL:1293*/if (!Character.isJavaIdentifierStart(v1.charAt(0))) {
            /*SL:1294*/throw new IllegalArgumentException("Invalid " + v2 + " (must be a '<init>', '<clinit>' or a valid Java identifier): " + v1);
        }
        /*SL:1300*/for (int a5 = 1; a5 < v1.length(); ++a5) {
            /*SL:1301*/if (!Character.isJavaIdentifierPart(v1.charAt(a5))) {
                /*SL:1302*/throw new IllegalArgumentException("Invalid " + v2 + " (must be '<init>' or '<clinit>' or a valid Java identifier): " + v1);
            }
        }
    }
    
    static void checkInternalName(final String a1, final String a2) {
        /*SL:1320*/if (a1 == null || a1.length() == 0) {
            /*SL:1321*/throw new IllegalArgumentException("Invalid " + a2 + " (must not be null or empty)");
        }
        /*SL:1324*/if (a1.charAt(0) == '[') {
            checkDesc(/*EL:1325*/a1, false);
        }
        else {
            checkInternalName(/*EL:1327*/a1, 0, -1, a2);
        }
    }
    
    static void checkInternalName(final String a4, final int v1, final int v2, final String v3) {
        final int v4 = /*EL:1347*/(v2 == -1) ? a4.length() : v2;
        try {
            int a5 = /*EL:1349*/v1;
            int a6;
            /*SL:1358*/do {
                a6 = a4.indexOf(47, a5 + 1);
                if (a6 == -1 || a6 > v4) {
                    a6 = v4;
                }
                checkIdentifier(a4, a5, a6, null);
                a5 = a6 + 1;
            } while (a6 != v4);
        }
        catch (IllegalArgumentException a7) {
            /*SL:1360*/throw new IllegalArgumentException("Invalid " + v3 + " (must be a fully qualified class name in internal form): " + a4);
        }
    }
    
    static void checkDesc(final String a1, final boolean a2) {
        final int v1 = checkDesc(/*EL:1377*/a1, 0, a2);
        /*SL:1378*/if (v1 != a1.length()) {
            /*SL:1379*/throw new IllegalArgumentException("Invalid descriptor: " + a1);
        }
    }
    
    static int checkDesc(final String v1, final int v2, final boolean v3) {
        /*SL:1396*/if (v1 == null || v2 >= v1.length()) {
            /*SL:1397*/throw new IllegalArgumentException("Invalid type descriptor (must not be null or empty)");
        }
        /*SL:1401*/switch (v1.charAt(v2)) {
            case 'V': {
                /*SL:1403*/if (v3) {
                    /*SL:1404*/return v2 + 1;
                }
                /*SL:1406*/throw new IllegalArgumentException("Invalid descriptor: " + v1);
            }
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z': {
                /*SL:1417*/return v2 + 1;
            }
            case '[': {
                int a1;
                /*SL:1420*/for (a1 = v2 + 1; a1 < v1.length() && v1.charAt(a1) == '['; /*SL:1421*/++a1) {}
                /*SL:1423*/if (a1 < v1.length()) {
                    /*SL:1424*/return checkDesc(v1, a1, false);
                }
                /*SL:1426*/throw new IllegalArgumentException("Invalid descriptor: " + v1);
            }
            case 'L': {
                final int a2 = /*EL:1430*/v1.indexOf(59, v2);
                /*SL:1431*/if (a2 == -1 || a2 - v2 < 2) {
                    /*SL:1432*/throw new IllegalArgumentException("Invalid descriptor: " + v1);
                }
                try {
                    checkInternalName(/*EL:1436*/v1, v2 + 1, a2, null);
                }
                catch (IllegalArgumentException a3) {
                    /*SL:1438*/throw new IllegalArgumentException("Invalid descriptor: " + v1);
                }
                /*SL:1441*/return a2 + 1;
            }
            default: {
                /*SL:1443*/throw new IllegalArgumentException("Invalid descriptor: " + v1);
            }
        }
    }
    
    static void checkMethodDesc(final String a1) {
        /*SL:1454*/if (a1 == null || a1.length() == 0) {
            /*SL:1455*/throw new IllegalArgumentException("Invalid method descriptor (must not be null or empty)");
        }
        /*SL:1458*/if (a1.charAt(0) != '(' || a1.length() < 3) {
            /*SL:1459*/throw new IllegalArgumentException("Invalid descriptor: " + a1);
        }
        int v1 = /*EL:1461*/1;
        Label_0143: {
            /*SL:1462*/if (a1.charAt(v1) != ')') {
                /*SL:1464*/while (a1.charAt(v1) != 'V') {
                    /*SL:1468*/v1 = checkDesc(a1, v1, false);
                    /*SL:1469*/if (v1 >= a1.length() || a1.charAt(v1) == ')') {
                        break Label_0143;
                    }
                }
                throw new IllegalArgumentException("Invalid descriptor: " + a1);
            }
        }
        /*SL:1471*/v1 = checkDesc(a1, v1 + 1, true);
        /*SL:1472*/if (v1 != a1.length()) {
            /*SL:1473*/throw new IllegalArgumentException("Invalid descriptor: " + a1);
        }
    }
    
    void checkLabel(final Label a1, final boolean a2, final String a3) {
        /*SL:1490*/if (a1 == null) {
            /*SL:1491*/throw new IllegalArgumentException("Invalid " + a3 + " (must not be null)");
        }
        /*SL:1494*/if (a2 && this.labels.get(a1) == null) {
            /*SL:1495*/throw new IllegalArgumentException("Invalid " + a3 + " (must be visited first)");
        }
    }
    
    private static void checkNonDebugLabel(final Label v1) {
        final Field v2 = getLabelStatusField();
        int v3 = /*EL:1508*/0;
        try {
            /*SL:1510*/v3 = (int)((v2 == null) ? 0 : v2.get(v1));
        }
        catch (IllegalAccessException a1) {
            /*SL:1512*/throw new Error("Internal error");
        }
        /*SL:1514*/if ((v3 & 0x1) != 0x0) {
            /*SL:1515*/throw new IllegalArgumentException("Labels used for debug info cannot be reused for control flow");
        }
    }
    
    private static Field getLabelStatusField() {
        /*SL:1526*/if (CheckMethodAdapter.labelStatusField == null) {
            CheckMethodAdapter.labelStatusField = getLabelField(/*EL:1527*/"a");
            /*SL:1528*/if (CheckMethodAdapter.labelStatusField == null) {
                CheckMethodAdapter.labelStatusField = getLabelField(/*EL:1529*/"status");
            }
        }
        /*SL:1532*/return CheckMethodAdapter.labelStatusField;
    }
    
    private static Field getLabelField(final String v0) {
        try {
            final Field a1 = /*EL:1544*/Label.class.getDeclaredField(v0);
            /*SL:1545*/a1.setAccessible(true);
            /*SL:1546*/return a1;
        }
        catch (NoSuchFieldException v) {
            /*SL:1548*/return null;
        }
    }
    
    static {
        final String v0 = "BBBBBBBBBBBBBBBBCCIAADDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBDDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBJBBBBBBBBBBBBBBBBBBBBHHHHHHHHHHHHHHHHDKLBBBBBBFFFFGGGGAECEBBEEBBAMHHAA";
        TYPE = new int[v0.length()];
        for (int v = 0; v < CheckMethodAdapter.TYPE.length; ++v) {
            CheckMethodAdapter.TYPE[v] = v0.charAt(v) - 'A' - '\u0001';
        }
    }
}
