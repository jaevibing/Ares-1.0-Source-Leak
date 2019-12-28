package org.spongepowered.asm.util;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.ListIterator;
import org.spongepowered.asm.util.throwables.SyntheticBridgeException;
import java.util.ArrayList;
import java.util.List;
import com.google.common.primitives.Ints;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import com.google.common.base.Joiner;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.lang.reflect.Field;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.IntInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.util.TraceClassVisitor;
import java.io.PrintWriter;
import java.io.OutputStream;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.apache.logging.log4j.Logger;
import java.util.regex.Pattern;

public final class Bytecode
{
    public static final int[] CONSTANTS_INT;
    public static final int[] CONSTANTS_FLOAT;
    public static final int[] CONSTANTS_DOUBLE;
    public static final int[] CONSTANTS_LONG;
    public static final int[] CONSTANTS_ALL;
    private static final Object[] CONSTANTS_VALUES;
    private static final String[] CONSTANTS_TYPES;
    private static final String[] BOXING_TYPES;
    private static final String[] UNBOXING_METHODS;
    private static final Class<?>[] MERGEABLE_MIXIN_ANNOTATIONS;
    private static Pattern mergeableAnnotationPattern;
    private static final Logger logger;
    
    public static MethodNode findMethod(final ClassNode a2, final String a3, final String v1) {
        /*SL:229*/for (final MethodNode a4 : a2.methods) {
            /*SL:230*/if (a4.name.equals(a3) && a4.desc.equals(v1)) {
                /*SL:231*/return a4;
            }
        }
        /*SL:234*/return null;
    }
    
    public static AbstractInsnNode findInsn(final MethodNode a2, final int v1) {
        /*SL:245*/for (final AbstractInsnNode a3 : a2.instructions) {
            /*SL:248*/if (a3.getOpcode() == v1) {
                /*SL:249*/return a3;
            }
        }
        /*SL:252*/return null;
    }
    
    public static MethodInsnNode findSuperInit(final MethodNode v-2, final String v-1) {
        /*SL:265*/if (!"<init>".equals(v-2.name)) {
            /*SL:266*/return null;
        }
        int v0 = /*EL:269*/0;
        /*SL:270*/for (MethodInsnNode a2 : v-2.instructions) {
            /*SL:272*/if (a2 instanceof TypeInsnNode && a2.getOpcode() == 187) {
                /*SL:273*/++v0;
            }
            else {
                /*SL:274*/if (!(a2 instanceof MethodInsnNode) || a2.getOpcode() != 183) {
                    continue;
                }
                /*SL:275*/a2 = (MethodInsnNode)a2;
                /*SL:276*/if (!"<init>".equals(a2.name)) {
                    continue;
                }
                /*SL:277*/if (v0 > 0) {
                    /*SL:278*/--v0;
                }
                else {
                    /*SL:279*/if (a2.owner.equals(v-1)) {
                        /*SL:280*/return a2;
                    }
                    continue;
                }
            }
        }
        /*SL:285*/return null;
    }
    
    public static void textify(final ClassNode a1, final OutputStream a2) {
        /*SL:296*/a1.accept(new TraceClassVisitor(new PrintWriter(a2)));
    }
    
    public static void textify(final MethodNode a1, final OutputStream a2) {
        final TraceClassVisitor v1 = /*EL:307*/new TraceClassVisitor(new PrintWriter(a2));
        final MethodVisitor v2 = /*EL:308*/v1.visitMethod(a1.access, a1.name, a1.desc, a1.signature, a1.exceptions.<String>toArray(new String[0]));
        /*SL:310*/a1.accept(v2);
        /*SL:311*/v1.visitEnd();
    }
    
    public static void dumpClass(final ClassNode a1) {
        final ClassWriter v1 = /*EL:320*/new ClassWriter(3);
        /*SL:321*/a1.accept(v1);
        dumpClass(/*EL:322*/v1.toByteArray());
    }
    
    public static void dumpClass(final byte[] a1) {
        final ClassReader v1 = /*EL:331*/new ClassReader(a1);
        /*SL:332*/CheckClassAdapter.verify(v1, true, new PrintWriter(System.out));
    }
    
    public static void printMethodWithOpcodeIndices(final MethodNode v1) {
        System.err.printf(/*EL:341*/"%s%s\n", v1.name, v1.desc);
        int v2 = /*EL:342*/0;
        final Iterator<AbstractInsnNode> a1 = /*EL:343*/v1.instructions.iterator();
        while (a1.hasNext()) {
            System.err.printf(/*EL:344*/"[%4d] %s\n", v2++, describeNode(a1.next()));
        }
    }
    
    public static void printMethod(final MethodNode v1) {
        System.err.printf(/*EL:354*/"%s%s\n", v1.name, v1.desc);
        final Iterator<AbstractInsnNode> a1 = /*EL:355*/v1.instructions.iterator();
        while (a1.hasNext()) {
            System.err.print(/*EL:356*/"  ");
            printNode(/*EL:357*/a1.next());
        }
    }
    
    public static void printNode(final AbstractInsnNode a1) {
        System.err.printf(/*EL:367*/"%s\n", describeNode(a1));
    }
    
    public static String describeNode(final AbstractInsnNode v-1) {
        /*SL:377*/if (v-1 == null) {
            /*SL:378*/return String.format("   %-14s ", "null");
        }
        /*SL:381*/if (v-1 instanceof LabelNode) {
            /*SL:382*/return String.format("[%s]", ((LabelNode)v-1).getLabel());
        }
        String v0 = /*EL:385*/String.format("   %-14s ", v-1.getClass().getSimpleName().replace("Node", ""));
        /*SL:386*/if (v-1 instanceof JumpInsnNode) {
            /*SL:387*/v0 += String.format("[%s] [%s]", getOpcodeName(v-1), ((JumpInsnNode)v-1).label.getLabel());
        }
        else/*SL:388*/ if (v-1 instanceof VarInsnNode) {
            /*SL:389*/v0 += String.format("[%s] %d", getOpcodeName(v-1), ((VarInsnNode)v-1).var);
        }
        else/*SL:390*/ if (v-1 instanceof MethodInsnNode) {
            final MethodInsnNode a1 = /*EL:391*/(MethodInsnNode)v-1;
            /*SL:392*/v0 += String.format("[%s] %s %s %s", getOpcodeName(v-1), a1.owner, a1.name, a1.desc);
        }
        else/*SL:393*/ if (v-1 instanceof FieldInsnNode) {
            final FieldInsnNode v = /*EL:394*/(FieldInsnNode)v-1;
            /*SL:395*/v0 += String.format("[%s] %s %s %s", getOpcodeName(v-1), v.owner, v.name, v.desc);
        }
        else/*SL:396*/ if (v-1 instanceof LineNumberNode) {
            final LineNumberNode v2 = /*EL:397*/(LineNumberNode)v-1;
            /*SL:398*/v0 += String.format("LINE=[%d] LABEL=[%s]", v2.line, v2.start.getLabel());
        }
        else/*SL:399*/ if (v-1 instanceof LdcInsnNode) {
            /*SL:400*/v0 += ((LdcInsnNode)v-1).cst;
        }
        else/*SL:401*/ if (v-1 instanceof IntInsnNode) {
            /*SL:402*/v0 += ((IntInsnNode)v-1).operand;
        }
        else/*SL:403*/ if (v-1 instanceof FrameNode) {
            /*SL:404*/v0 += String.format("[%s] ", getOpcodeName(((FrameNode)v-1).type, "H_INVOKEINTERFACE", -1));
        }
        else {
            /*SL:406*/v0 += String.format("[%s] ", getOpcodeName(v-1));
        }
        /*SL:408*/return v0;
    }
    
    public static String getOpcodeName(final AbstractInsnNode a1) {
        /*SL:420*/return (a1 != null) ? getOpcodeName(a1.getOpcode()) : "";
    }
    
    public static String getOpcodeName(final int a1) {
        /*SL:432*/return getOpcodeName(a1, "UNINITIALIZED_THIS", 1);
    }
    
    private static String getOpcodeName(final int a3, final String v1, final int v2) {
        /*SL:436*/if (a3 >= v2) {
            boolean a4 = /*EL:437*/false;
            try {
                /*SL:440*/for (final Field a5 : Opcodes.class.getDeclaredFields()) {
                    /*SL:441*/if (a4 || a5.getName().equals(v1)) {
                        /*SL:444*/a4 = true;
                        /*SL:445*/if (a5.getType() == Integer.TYPE && a5.getInt(null) == a3) {
                            /*SL:446*/return a5.getName();
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
        /*SL:454*/return (a3 >= 0) ? String.valueOf(a3) : "UNKNOWN";
    }
    
    public static boolean methodHasLineNumbers(final MethodNode v1) {
        final Iterator<AbstractInsnNode> a1 = /*EL:464*/v1.instructions.iterator();
        while (a1.hasNext()) {
            /*SL:465*/if (a1.next() instanceof LineNumberNode) {
                /*SL:466*/return true;
            }
        }
        /*SL:469*/return false;
    }
    
    public static boolean methodIsStatic(final MethodNode a1) {
        /*SL:479*/return (a1.access & 0x8) == 0x8;
    }
    
    public static boolean fieldIsStatic(final FieldNode a1) {
        /*SL:489*/return (a1.access & 0x8) == 0x8;
    }
    
    public static int getFirstNonArgLocalIndex(final MethodNode a1) {
        /*SL:503*/return getFirstNonArgLocalIndex(Type.getArgumentTypes(a1.desc), (a1.access & 0x8) == 0x0);
    }
    
    public static int getFirstNonArgLocalIndex(final Type[] a1, final boolean a2) {
        /*SL:519*/return getArgsSize(a1) + (a2 ? 1 : 0);
    }
    
    public static int getArgsSize(final Type[] v1) {
        int v2 = /*EL:530*/0;
        /*SL:532*/for (final Type a1 : v1) {
            /*SL:533*/v2 += a1.getSize();
        }
        /*SL:536*/return v2;
    }
    
    public static void loadArgs(final Type[] a1, final InsnList a2, final int a3) {
        loadArgs(/*EL:548*/a1, a2, a3, -1);
    }
    
    public static void loadArgs(final Type[] a1, final InsnList a2, final int a3, final int a4) {
        loadArgs(/*EL:561*/a1, a2, a3, a4, null);
    }
    
    public static void loadArgs(final Type[] a2, final InsnList a3, final int a4, final int a5, final Type[] v1) {
        int v2 = /*EL:575*/a4;
        int v3 = 0;
        /*SL:577*/for (final Type a6 : a2) {
            /*SL:578*/a3.add(new VarInsnNode(a6.getOpcode(21), v2));
            /*SL:579*/if (v1 != null && v3 < v1.length && v1[v3] != null) {
                /*SL:580*/a3.add(new TypeInsnNode(192, v1[v3].getInternalName()));
            }
            /*SL:582*/v2 += a6.getSize();
            /*SL:583*/if (a5 >= a4 && v2 >= a5) {
                /*SL:584*/return;
            }
            /*SL:586*/++v3;
        }
    }
    
    public static Map<LabelNode, LabelNode> cloneLabels(final InsnList v-1) {
        final Map<LabelNode, LabelNode> v0 = /*EL:599*/new HashMap<LabelNode, LabelNode>();
        /*SL:601*/for (final AbstractInsnNode a1 : v-1) {
            /*SL:603*/if (a1 instanceof LabelNode) {
                /*SL:604*/v0.put((LabelNode)a1, new LabelNode(((LabelNode)a1).getLabel()));
            }
        }
        /*SL:608*/return v0;
    }
    
    public static String generateDescriptor(final Object a2, final Object... v1) {
        final StringBuilder v2 = /*EL:621*/new StringBuilder().append('(');
        /*SL:623*/for (final Object a3 : v1) {
            /*SL:624*/v2.append(toDescriptor(a3));
        }
        /*SL:627*/return v2.append(')').append((a2 != null) ? toDescriptor(a2) : "V").toString();
    }
    
    private static String toDescriptor(final Object a1) {
        /*SL:637*/if (a1 instanceof String) {
            /*SL:638*/return (String)a1;
        }
        /*SL:639*/if (a1 instanceof Type) {
            /*SL:640*/return a1.toString();
        }
        /*SL:641*/if (a1 instanceof Class) {
            /*SL:642*/return Type.getDescriptor((Class<?>)a1);
        }
        /*SL:644*/return (a1 == null) ? "" : a1.toString();
    }
    
    public static String getDescriptor(final Type[] a1) {
        /*SL:655*/return "(" + Joiner.on("").join(a1) + ")";
    }
    
    public static String getDescriptor(final Type[] a1, final Type a2) {
        /*SL:666*/return getDescriptor(a1) + a2.toString();
    }
    
    public static String changeDescriptorReturnType(final String a1, final String a2) {
        /*SL:677*/if (a1 == null) {
            /*SL:678*/return null;
        }
        /*SL:679*/if (a2 == null) {
            /*SL:680*/return a1;
        }
        /*SL:682*/return a1.substring(0, a1.lastIndexOf(41) + 1) + a2;
    }
    
    public static String getSimpleName(final Class<? extends Annotation> a1) {
        /*SL:693*/return a1.getSimpleName();
    }
    
    public static String getSimpleName(final AnnotationNode a1) {
        /*SL:704*/return getSimpleName(a1.desc);
    }
    
    public static String getSimpleName(final String a1) {
        final int v1 = /*EL:714*/Math.max(a1.lastIndexOf(47), 0);
        /*SL:715*/return a1.substring(v1 + 1).replace(";", "");
    }
    
    public static boolean isConstant(final AbstractInsnNode a1) {
        /*SL:726*/return a1 != null && /*EL:729*/Ints.contains(Bytecode.CONSTANTS_ALL, a1.getOpcode());
    }
    
    public static Object getConstant(final AbstractInsnNode v1) {
        /*SL:741*/if (v1 == null) {
            /*SL:742*/return null;
        }
        /*SL:743*/if (v1 instanceof LdcInsnNode) {
            /*SL:744*/return ((LdcInsnNode)v1).cst;
        }
        /*SL:745*/if (!(v1 instanceof IntInsnNode)) {
            final int v2 = /*EL:753*/Ints.indexOf(Bytecode.CONSTANTS_ALL, v1.getOpcode());
            /*SL:754*/return (v2 < 0) ? null : Bytecode.CONSTANTS_VALUES[v2];
        }
        final int a1 = ((IntInsnNode)v1).operand;
        if (v1.getOpcode() == 16 || v1.getOpcode() == 17) {
            return a1;
        }
        throw new IllegalArgumentException("IntInsnNode with invalid opcode " + v1.getOpcode() + " in getConstant");
    }
    
    public static Type getConstantType(final AbstractInsnNode v1) {
        /*SL:765*/if (v1 == null) {
            /*SL:766*/return null;
        }
        /*SL:767*/if (!(v1 instanceof LdcInsnNode)) {
            final int v2 = /*EL:785*/Ints.indexOf(Bytecode.CONSTANTS_ALL, v1.getOpcode());
            /*SL:786*/return (v2 < 0) ? null : Type.getType(Bytecode.CONSTANTS_TYPES[v2]);
        }
        final Object a1 = ((LdcInsnNode)v1).cst;
        if (a1 instanceof Integer) {
            return Type.getType("I");
        }
        if (a1 instanceof Float) {
            return Type.getType("F");
        }
        if (a1 instanceof Long) {
            return Type.getType("J");
        }
        if (a1 instanceof Double) {
            return Type.getType("D");
        }
        if (a1 instanceof String) {
            return Type.getType("Ljava/lang/String;");
        }
        if (a1 instanceof Type) {
            return Type.getType("Ljava/lang/Class;");
        }
        throw new IllegalArgumentException("LdcInsnNode with invalid payload type " + a1.getClass() + " in getConstant");
    }
    
    public static boolean hasFlag(final ClassNode a1, final int a2) {
        /*SL:797*/return (a1.access & a2) == a2;
    }
    
    public static boolean hasFlag(final MethodNode a1, final int a2) {
        /*SL:808*/return (a1.access & a2) == a2;
    }
    
    public static boolean hasFlag(final FieldNode a1, final int a2) {
        /*SL:819*/return (a1.access & a2) == a2;
    }
    
    public static boolean compareFlags(final MethodNode a1, final MethodNode a2, final int a3) {
        /*SL:832*/return hasFlag(a1, a3) == hasFlag(a2, a3);
    }
    
    public static boolean compareFlags(final FieldNode a1, final FieldNode a2, final int a3) {
        /*SL:845*/return hasFlag(a1, a3) == hasFlag(a2, a3);
    }
    
    public static Visibility getVisibility(final MethodNode a1) {
        /*SL:863*/return getVisibility(a1.access & 0x7);
    }
    
    public static Visibility getVisibility(final FieldNode a1) {
        /*SL:881*/return getVisibility(a1.access & 0x7);
    }
    
    private static Visibility getVisibility(final int a1) {
        /*SL:899*/if ((a1 & 0x4) != 0x0) {
            /*SL:900*/return Visibility.PROTECTED;
        }
        /*SL:901*/if ((a1 & 0x2) != 0x0) {
            /*SL:902*/return Visibility.PRIVATE;
        }
        /*SL:903*/if ((a1 & 0x1) != 0x0) {
            /*SL:904*/return Visibility.PUBLIC;
        }
        /*SL:906*/return Visibility.PACKAGE;
    }
    
    public static void setVisibility(final MethodNode a1, final Visibility a2) {
        /*SL:917*/a1.access = setVisibility(a1.access, a2.access);
    }
    
    public static void setVisibility(final FieldNode a1, final Visibility a2) {
        /*SL:928*/a1.access = setVisibility(a1.access, a2.access);
    }
    
    public static void setVisibility(final MethodNode a1, final int a2) {
        /*SL:939*/a1.access = setVisibility(a1.access, a2);
    }
    
    public static void setVisibility(final FieldNode a1, final int a2) {
        /*SL:950*/a1.access = setVisibility(a1.access, a2);
    }
    
    private static int setVisibility(final int a1, final int a2) {
        /*SL:954*/return (a1 & 0xFFFFFFF8) | (a2 & 0x7);
    }
    
    public static int getMaxLineNumber(final ClassNode v1, final int v2, final int v3) {
        int v4 = /*EL:966*/0;
        /*SL:967*/for (AbstractInsnNode a3 : v1.methods) {
            final Iterator<AbstractInsnNode> a2 = /*EL:968*/a3.instructions.iterator();
            while (a2.hasNext()) {
                /*SL:969*/a3 = a2.next();
                /*SL:970*/if (a3 instanceof LineNumberNode) {
                    /*SL:971*/v4 = Math.max(v4, ((LineNumberNode)a3).line);
                }
            }
        }
        /*SL:975*/return Math.max(v2, v4 + v3);
    }
    
    public static String getBoxingType(final Type a1) {
        /*SL:986*/return (a1 == null) ? null : Bytecode.BOXING_TYPES[a1.getSort()];
    }
    
    public static String getUnboxingMethod(final Type a1) {
        /*SL:999*/return (a1 == null) ? null : Bytecode.UNBOXING_METHODS[a1.getSort()];
    }
    
    public static void mergeAnnotations(final ClassNode a1, final ClassNode a2) {
        /*SL:1014*/a2.visibleAnnotations = mergeAnnotations(a1.visibleAnnotations, a2.visibleAnnotations, "class", a1.name);
        /*SL:1015*/a2.invisibleAnnotations = mergeAnnotations(a1.invisibleAnnotations, a2.invisibleAnnotations, "class", a1.name);
    }
    
    public static void mergeAnnotations(final MethodNode a1, final MethodNode a2) {
        /*SL:1030*/a2.visibleAnnotations = mergeAnnotations(a1.visibleAnnotations, a2.visibleAnnotations, "method", a1.name);
        /*SL:1031*/a2.invisibleAnnotations = mergeAnnotations(a1.invisibleAnnotations, a2.invisibleAnnotations, "method", a1.name);
    }
    
    public static void mergeAnnotations(final FieldNode a1, final FieldNode a2) {
        /*SL:1046*/a2.visibleAnnotations = mergeAnnotations(a1.visibleAnnotations, a2.visibleAnnotations, "field", a1.name);
        /*SL:1047*/a2.invisibleAnnotations = mergeAnnotations(a1.invisibleAnnotations, a2.invisibleAnnotations, "field", a1.name);
    }
    
    private static List<AnnotationNode> mergeAnnotations(final List<AnnotationNode> a4, List<AnnotationNode> v1, final String v2, final String v3) {
        try {
            /*SL:1062*/if (a4 == null) {
                /*SL:1063*/return v1;
            }
            /*SL:1066*/if (v1 == null) {
                /*SL:1067*/v1 = new ArrayList<AnnotationNode>();
            }
            /*SL:1070*/for (final AnnotationNode a5 : a4) {
                /*SL:1071*/if (!isMergeableAnnotation(a5)) {
                    /*SL:1072*/continue;
                }
                final Iterator<AnnotationNode> a6 = /*EL:1075*/v1.iterator();
                while (a6.hasNext()) {
                    /*SL:1076*/if (a6.next().desc.equals(a5.desc)) {
                        /*SL:1077*/a6.remove();
                        break;
                    }
                }
                /*SL:1082*/v1.add(a5);
            }
        }
        catch (Exception a7) {
            Bytecode.logger.warn(/*EL:1085*/"Exception encountered whilst merging annotations for {} {}", new Object[] { v2, v3 });
        }
        /*SL:1088*/return v1;
    }
    
    private static boolean isMergeableAnnotation(final AnnotationNode a1) {
        /*SL:1092*/return !a1.desc.startsWith("L" + Constants.MIXIN_PACKAGE_REF) || Bytecode.mergeableAnnotationPattern.matcher(/*EL:1093*/a1.desc).matches();
    }
    
    private static Pattern getMergeableAnnotationPattern() {
        final StringBuilder v0 = /*EL:1099*/new StringBuilder("^L(");
        /*SL:1100*/for (int v = 0; v < Bytecode.MERGEABLE_MIXIN_ANNOTATIONS.length; ++v) {
            /*SL:1101*/if (v > 0) {
                /*SL:1102*/v0.append('|');
            }
            /*SL:1104*/v0.append(Bytecode.MERGEABLE_MIXIN_ANNOTATIONS[v].getName().replace('.', '/'));
        }
        /*SL:1106*/return Pattern.compile(v0.append(");$").toString());
    }
    
    public static void compareBridgeMethods(final MethodNode v-6, final MethodNode v-5) {
        final ListIterator<AbstractInsnNode> iterator = /*EL:1117*/v-6.instructions.iterator();
        final ListIterator<AbstractInsnNode> iterator2 = /*EL:1118*/v-5.instructions.iterator();
        int n = /*EL:1120*/0;
        /*SL:1121*/while (iterator.hasNext() && iterator2.hasNext()) {
            final AbstractInsnNode a3 = /*EL:1122*/iterator.next();
            final AbstractInsnNode v0 = /*EL:1123*/iterator2.next();
            /*SL:1124*/if (!(a3 instanceof LabelNode)) {
                /*SL:1128*/if (a3 instanceof MethodInsnNode) {
                    final MethodInsnNode a1 = /*EL:1129*/(MethodInsnNode)a3;
                    final MethodInsnNode a2 = /*EL:1130*/(MethodInsnNode)v0;
                    /*SL:1131*/if (!a1.name.equals(a2.name)) {
                        /*SL:1132*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_NAME, v-6.name, v-6.desc, n, a3, v0);
                    }
                    /*SL:1133*/if (!a1.desc.equals(a2.desc)) {
                        /*SL:1134*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_DESC, v-6.name, v-6.desc, n, a3, v0);
                    }
                }
                else {
                    /*SL:1136*/if (a3.getOpcode() != v0.getOpcode()) {
                        /*SL:1137*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INSN, v-6.name, v-6.desc, n, a3, v0);
                    }
                    /*SL:1138*/if (a3 instanceof VarInsnNode) {
                        final VarInsnNode v = /*EL:1139*/(VarInsnNode)a3;
                        final VarInsnNode v2 = /*EL:1140*/(VarInsnNode)v0;
                        /*SL:1141*/if (v.var != v2.var) {
                            /*SL:1142*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LOAD, v-6.name, v-6.desc, n, a3, v0);
                        }
                    }
                    else/*SL:1144*/ if (a3 instanceof TypeInsnNode) {
                        final TypeInsnNode v3 = /*EL:1145*/(TypeInsnNode)a3;
                        final TypeInsnNode v4 = /*EL:1146*/(TypeInsnNode)v0;
                        /*SL:1147*/if (v3.getOpcode() == 192 && !v3.desc.equals(v4.desc)) {
                            /*SL:1148*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_CAST, v-6.name, v-6.desc, n, a3, v0);
                        }
                    }
                }
            }
            ++n;
        }
        /*SL:1153*/if (iterator.hasNext() || iterator2.hasNext()) {
            /*SL:1154*/throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LENGTH, v-6.name, v-6.desc, n, null, null);
        }
    }
    
    static {
        CONSTANTS_INT = new int[] { 2, 3, 4, 5, 6, 7, 8 };
        CONSTANTS_FLOAT = new int[] { 11, 12, 13 };
        CONSTANTS_DOUBLE = new int[] { 14, 15 };
        CONSTANTS_LONG = new int[] { 9, 10 };
        CONSTANTS_ALL = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 };
        CONSTANTS_VALUES = new Object[] { null, -1, 0, 1, 2, 3, 4, 5, 0L, 1L, 0.0f, 1.0f, 2.0f, 0.0, 1.0 };
        CONSTANTS_TYPES = new String[] { null, "I", "I", "I", "I", "I", "I", "I", "J", "J", "F", "F", "F", "D", "D", "I", "I" };
        BOXING_TYPES = new String[] { null, "java/lang/Boolean", "java/lang/Character", "java/lang/Byte", "java/lang/Short", "java/lang/Integer", "java/lang/Float", "java/lang/Long", "java/lang/Double", null, null, null };
        UNBOXING_METHODS = new String[] { null, "booleanValue", "charValue", "byteValue", "shortValue", "intValue", "floatValue", "longValue", "doubleValue", null, null, null };
        MERGEABLE_MIXIN_ANNOTATIONS = new Class[] { Overwrite.class, Intrinsic.class, Final.class, Debug.class };
        Bytecode.mergeableAnnotationPattern = getMergeableAnnotationPattern();
        logger = LogManager.getLogger("mixin");
    }
    
    public enum Visibility
    {
        PRIVATE(2), 
        PROTECTED(4), 
        PACKAGE(0), 
        PUBLIC(1);
        
        static final int MASK = 7;
        final int access;
        
        private Visibility(final int a1) {
            this.access = a1;
        }
    }
}
