package org.spongepowered.asm.mixin.injection.invoke.arg;

import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.util.asm.MethodVisitorEx;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.util.Bytecode;
import java.util.HashMap;
import com.google.common.collect.HashBiMap;
import java.util.Map;
import com.google.common.collect.BiMap;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

public final class ArgsClassGenerator implements IClassGenerator
{
    public static final String ARGS_NAME;
    public static final String ARGS_REF;
    public static final String GETTER_PREFIX = "$";
    private static final String CLASS_NAME_BASE = "org.spongepowered.asm.synthetic.args.Args$";
    private static final String OBJECT = "java/lang/Object";
    private static final String OBJECT_ARRAY = "[Ljava/lang/Object;";
    private static final String VALUES_FIELD = "values";
    private static final String CTOR_DESC = "([Ljava/lang/Object;)V";
    private static final String SET = "set";
    private static final String SET_DESC = "(ILjava/lang/Object;)V";
    private static final String SETALL = "setAll";
    private static final String SETALL_DESC = "([Ljava/lang/Object;)V";
    private static final String NPE = "java/lang/NullPointerException";
    private static final String NPE_CTOR_DESC = "(Ljava/lang/String;)V";
    private static final String AIOOBE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException";
    private static final String AIOOBE_CTOR_DESC = "(I)V";
    private static final String ACE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException";
    private static final String ACE_CTOR_DESC = "(IILjava/lang/String;)V";
    private int nextIndex;
    private final BiMap<String, String> classNames;
    private final Map<String, byte[]> classBytes;
    
    public ArgsClassGenerator() {
        this.nextIndex = 1;
        this.classNames = (BiMap<String, String>)HashBiMap.<Object, Object>create();
        this.classBytes = new HashMap<String, byte[]>();
    }
    
    public String getClassName(final String a1) {
        final String v1 = /*EL:115*/Bytecode.changeDescriptorReturnType(a1, "V");
        String v2 = /*EL:116*/this.classNames.get(v1);
        /*SL:117*/if (v2 == null) {
            /*SL:118*/v2 = String.format("%s%d", "org.spongepowered.asm.synthetic.args.Args$", this.nextIndex++);
            /*SL:119*/this.classNames.put(v1, v2);
        }
        /*SL:121*/return v2;
    }
    
    public String getClassRef(final String a1) {
        /*SL:134*/return this.getClassName(a1).replace('.', '/');
    }
    
    @Override
    public byte[] generate(final String a1) {
        /*SL:143*/return this.getBytes(a1);
    }
    
    public byte[] getBytes(final String v2) {
        byte[] v3 = /*EL:154*/this.classBytes.get(v2);
        /*SL:155*/if (v3 == null) {
            final String a1 = /*EL:156*/this.classNames.inverse().get(v2);
            /*SL:157*/if (a1 == null) {
                /*SL:158*/return null;
            }
            /*SL:160*/v3 = this.generateClass(v2, a1);
            /*SL:161*/this.classBytes.put(v2, v3);
        }
        /*SL:163*/return v3;
    }
    
    private byte[] generateClass(final String a1, final String a2) {
        final String v1 = /*EL:174*/a1.replace('.', '/');
        final Type[] v2 = /*EL:175*/Type.getArgumentTypes(a2);
        ClassVisitor v4;
        final ClassWriter v3 = /*EL:178*/(ClassWriter)(v4 = new ClassWriter(2));
        /*SL:179*/if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
            /*SL:180*/v4 = new CheckClassAdapter(v3);
        }
        /*SL:183*/v4.visit(50, 4129, v1, null, ArgsClassGenerator.ARGS_REF, null);
        /*SL:184*/v4.visitSource(a1.substring(a1.lastIndexOf(46) + 1) + ".java", null);
        /*SL:186*/this.generateCtor(v1, a2, v2, v4);
        /*SL:187*/this.generateToString(v1, a2, v2, v4);
        /*SL:188*/this.generateFactory(v1, a2, v2, v4);
        /*SL:189*/this.generateSetters(v1, a2, v2, v4);
        /*SL:190*/this.generateGetters(v1, a2, v2, v4);
        /*SL:192*/v4.visitEnd();
        /*SL:194*/return v3.toByteArray();
    }
    
    private void generateCtor(final String a1, final String a2, final Type[] a3, final ClassVisitor a4) {
        final MethodVisitor v1 = /*EL:207*/a4.visitMethod(2, "<init>", "([Ljava/lang/Object;)V", null, null);
        /*SL:208*/v1.visitCode();
        /*SL:209*/v1.visitVarInsn(25, 0);
        /*SL:210*/v1.visitVarInsn(25, 1);
        /*SL:211*/v1.visitMethodInsn(183, ArgsClassGenerator.ARGS_REF, "<init>", "([Ljava/lang/Object;)V", false);
        /*SL:212*/v1.visitInsn(177);
        /*SL:213*/v1.visitMaxs(2, 2);
        /*SL:214*/v1.visitEnd();
    }
    
    private void generateToString(final String a1, final String a2, final Type[] a3, final ClassVisitor a4) {
        final MethodVisitor v1 = /*EL:226*/a4.visitMethod(1, "toString", "()Ljava/lang/String;", null, null);
        /*SL:227*/v1.visitCode();
        /*SL:228*/v1.visitLdcInsn("Args" + getSignature(a3));
        /*SL:229*/v1.visitInsn(176);
        /*SL:230*/v1.visitMaxs(1, 1);
        /*SL:231*/v1.visitEnd();
    }
    
    private void generateFactory(final String a3, final String a4, final Type[] v1, final ClassVisitor v2) {
        final String v3 = /*EL:246*/Bytecode.changeDescriptorReturnType(a4, "L" + a3 + ";");
        final MethodVisitorEx v4 = /*EL:247*/new MethodVisitorEx(v2.visitMethod(9, "of", v3, null, null));
        /*SL:248*/v4.visitCode();
        /*SL:251*/v4.visitTypeInsn(187, a3);
        /*SL:252*/v4.visitInsn(89);
        /*SL:255*/v4.visitConstant((byte)v1.length);
        /*SL:256*/v4.visitTypeInsn(189, "java/lang/Object");
        byte v5 = /*EL:259*/0;
        /*SL:260*/for (final Type a5 : v1) {
            /*SL:261*/v4.visitInsn(89);
            /*SL:262*/v4.visitConstant(v5);
            /*SL:263*/v4.visitVarInsn(a5.getOpcode(21), v5);
            box(/*EL:264*/v4, a5);
            /*SL:265*/v4.visitInsn(83);
            /*SL:266*/v5 += (byte)a5.getSize();
        }
        /*SL:270*/v4.visitMethodInsn(183, a3, "<init>", "([Ljava/lang/Object;)V", false);
        /*SL:273*/v4.visitInsn(176);
        /*SL:275*/v4.visitMaxs(6, Bytecode.getArgsSize(v1));
        /*SL:276*/v4.visitEnd();
    }
    
    private void generateGetters(final String v2, final String v3, final Type[] v4, final ClassVisitor v5) {
        byte v6 = /*EL:291*/0;
        /*SL:292*/for (MethodVisitorEx a4 : v4) {
            final String a2 = /*EL:293*/"$" + v6;
            final String a3 = /*EL:294*/"()" + a4.getDescriptor();
            /*SL:295*/a4 = new MethodVisitorEx(v5.visitMethod(1, a2, a3, null, null));
            /*SL:296*/a4.visitCode();
            /*SL:299*/a4.visitVarInsn(25, 0);
            /*SL:300*/a4.visitFieldInsn(180, v2, "values", "[Ljava/lang/Object;");
            /*SL:301*/a4.visitConstant(v6);
            /*SL:302*/a4.visitInsn(50);
            unbox(/*EL:305*/a4, a4);
            /*SL:308*/a4.visitInsn(a4.getOpcode(172));
            /*SL:310*/a4.visitMaxs(2, 1);
            /*SL:311*/a4.visitEnd();
            /*SL:312*/++v6;
        }
    }
    
    private void generateSetters(final String a1, final String a2, final Type[] a3, final ClassVisitor a4) {
        /*SL:326*/this.generateIndexedSetter(a1, a2, a3, a4);
        /*SL:327*/this.generateMultiSetter(a1, a2, a3, a4);
    }
    
    private void generateIndexedSetter(final String v2, final String v3, final Type[] v4, final ClassVisitor v5) {
        final MethodVisitorEx v6 = /*EL:342*/new MethodVisitorEx(v5.visitMethod(1, "set", "(ILjava/lang/Object;)V", null, null));
        /*SL:344*/v6.visitCode();
        final Label v7 = /*EL:346*/new Label();
        final Label v8 = new Label();
        final Label[] v9 = /*EL:347*/new Label[v4.length];
        /*SL:348*/for (int a1 = 0; a1 < v9.length; ++a1) {
            /*SL:349*/v9[a1] = new Label();
        }
        /*SL:353*/v6.visitVarInsn(25, 0);
        /*SL:354*/v6.visitFieldInsn(180, v2, "values", "[Ljava/lang/Object;");
        /*SL:357*/for (byte a2 = 0; a2 < v4.length; ++a2) {
            /*SL:358*/v6.visitVarInsn(21, 1);
            /*SL:359*/v6.visitConstant(a2);
            /*SL:360*/v6.visitJumpInsn(159, v9[a2]);
        }
        throwAIOOBE(/*EL:364*/v6, 1);
        /*SL:369*/for (int a3 = 0; a3 < v4.length; ++a3) {
            final String a4 = /*EL:370*/Bytecode.getBoxingType(v4[a3]);
            /*SL:371*/v6.visitLabel(v9[a3]);
            /*SL:372*/v6.visitVarInsn(21, 1);
            /*SL:373*/v6.visitVarInsn(25, 2);
            /*SL:374*/v6.visitTypeInsn(192, (a4 != null) ? a4 : v4[a3].getInternalName());
            /*SL:375*/v6.visitJumpInsn(167, (a4 != null) ? v8 : v7);
        }
        /*SL:379*/v6.visitLabel(v8);
        /*SL:380*/v6.visitInsn(89);
        /*SL:381*/v6.visitJumpInsn(199, v7);
        throwNPE(/*EL:384*/v6, "Argument with primitive type cannot be set to NULL");
        /*SL:387*/v6.visitLabel(v7);
        /*SL:388*/v6.visitInsn(83);
        /*SL:389*/v6.visitInsn(177);
        /*SL:390*/v6.visitMaxs(6, 3);
        /*SL:391*/v6.visitEnd();
    }
    
    private void generateMultiSetter(final String a4, final String v1, final Type[] v2, final ClassVisitor v3) {
        final MethodVisitorEx v4 = /*EL:405*/new MethodVisitorEx(v3.visitMethod(1, "setAll", "([Ljava/lang/Object;)V", null, null));
        /*SL:407*/v4.visitCode();
        final Label v5 = /*EL:409*/new Label();
        final Label v6 = new Label();
        int v7 = /*EL:410*/6;
        /*SL:413*/v4.visitVarInsn(25, 1);
        /*SL:414*/v4.visitInsn(190);
        /*SL:415*/v4.visitInsn(89);
        /*SL:416*/v4.visitConstant((byte)v2.length);
        /*SL:419*/v4.visitJumpInsn(159, v5);
        /*SL:421*/v4.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException");
        /*SL:422*/v4.visitInsn(89);
        /*SL:423*/v4.visitInsn(93);
        /*SL:424*/v4.visitInsn(88);
        /*SL:425*/v4.visitConstant((byte)v2.length);
        /*SL:426*/v4.visitLdcInsn(getSignature(v2));
        /*SL:428*/v4.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException", "<init>", "(IILjava/lang/String;)V", false);
        /*SL:429*/v4.visitInsn(191);
        /*SL:431*/v4.visitLabel(v5);
        /*SL:432*/v4.visitInsn(87);
        /*SL:435*/v4.visitVarInsn(25, 0);
        /*SL:436*/v4.visitFieldInsn(180, a4, "values", "[Ljava/lang/Object;");
        /*SL:438*/for (byte a5 = 0; a5 < v2.length; ++a5) {
            /*SL:440*/v4.visitInsn(89);
            /*SL:441*/v4.visitConstant(a5);
            /*SL:444*/v4.visitVarInsn(25, 1);
            /*SL:445*/v4.visitConstant(a5);
            /*SL:446*/v4.visitInsn(50);
            final String a6 = /*EL:449*/Bytecode.getBoxingType(v2[a5]);
            /*SL:450*/v4.visitTypeInsn(192, (a6 != null) ? a6 : v2[a5].getInternalName());
            /*SL:453*/if (a6 != null) {
                /*SL:454*/v4.visitInsn(89);
                /*SL:455*/v4.visitJumpInsn(198, v6);
                /*SL:456*/v7 = 7;
            }
            /*SL:460*/v4.visitInsn(83);
        }
        /*SL:463*/v4.visitInsn(177);
        /*SL:465*/v4.visitLabel(v6);
        throwNPE(/*EL:466*/v4, "Argument with primitive type cannot be set to NULL");
        /*SL:467*/v4.visitInsn(177);
        /*SL:469*/v4.visitMaxs(v7, 2);
        /*SL:470*/v4.visitEnd();
    }
    
    private static void throwNPE(final MethodVisitorEx a1, final String a2) {
        /*SL:477*/a1.visitTypeInsn(187, "java/lang/NullPointerException");
        /*SL:478*/a1.visitInsn(89);
        /*SL:479*/a1.visitLdcInsn(a2);
        /*SL:480*/a1.visitMethodInsn(183, "java/lang/NullPointerException", "<init>", "(Ljava/lang/String;)V", false);
        /*SL:481*/a1.visitInsn(191);
    }
    
    private static void throwAIOOBE(final MethodVisitorEx a1, final int a2) {
        /*SL:489*/a1.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException");
        /*SL:490*/a1.visitInsn(89);
        /*SL:491*/a1.visitVarInsn(21, a2);
        /*SL:492*/a1.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException", "<init>", "(I)V", false);
        /*SL:493*/a1.visitInsn(191);
    }
    
    private static void box(final MethodVisitor a2, final Type v1) {
        final String v2 = /*EL:504*/Bytecode.getBoxingType(v1);
        /*SL:505*/if (v2 != null) {
            final String a3 = /*EL:506*/String.format("(%s)L%s;", v1.getDescriptor(), v2);
            /*SL:507*/a2.visitMethodInsn(184, v2, "valueOf", a3, false);
        }
    }
    
    private static void unbox(final MethodVisitor v1, final Type v2) {
        final String v3 = /*EL:518*/Bytecode.getBoxingType(v2);
        /*SL:519*/if (v3 != null) {
            final String a1 = /*EL:520*/Bytecode.getUnboxingMethod(v2);
            final String a2 = /*EL:521*/"()" + v2.getDescriptor();
            /*SL:522*/v1.visitTypeInsn(192, v3);
            /*SL:523*/v1.visitMethodInsn(182, v3, a1, a2, false);
        }
        else {
            /*SL:525*/v1.visitTypeInsn(192, v2.getInternalName());
        }
    }
    
    private static String getSignature(final Type[] a1) {
        /*SL:530*/return new SignaturePrinter("", null, a1).setFullyQualified(true).getFormattedArgs();
    }
    
    static {
        ARGS_NAME = Args.class.getName();
        ARGS_REF = ArgsClassGenerator.ARGS_NAME.replace('.', '/');
    }
}
