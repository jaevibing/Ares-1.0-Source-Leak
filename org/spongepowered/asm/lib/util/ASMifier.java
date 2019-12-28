package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Type;
import java.util.HashMap;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.ClassVisitor;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import org.spongepowered.asm.lib.ClassReader;
import java.io.FileInputStream;
import org.spongepowered.asm.lib.Label;
import java.util.Map;

public class ASMifier extends Printer
{
    protected final String name;
    protected final int id;
    protected Map<Label, String> labelNames;
    private static final int ACCESS_CLASS = 262144;
    private static final int ACCESS_FIELD = 524288;
    private static final int ACCESS_INNER = 1048576;
    
    public ASMifier() {
        this(327680, "cw", 0);
        if (this.getClass() != ASMifier.class) {
            throw new IllegalStateException();
        }
    }
    
    protected ASMifier(final int a1, final String a2, final int a3) {
        super(a1);
        this.name = a2;
        this.id = a3;
    }
    
    public static void main(final String[] v1) throws Exception {
        int v2 = /*EL:129*/0;
        int v3 = /*EL:130*/2;
        boolean v4 = /*EL:132*/true;
        /*SL:133*/if (v1.length < 1 || v1.length > 2) {
            /*SL:134*/v4 = false;
        }
        /*SL:136*/if (v4 && "-debug".equals(v1[0])) {
            /*SL:137*/v2 = 1;
            /*SL:138*/v3 = 0;
            /*SL:139*/if (v1.length != 2) {
                /*SL:140*/v4 = false;
            }
        }
        /*SL:143*/if (!v4) {
            System.err.println(/*EL:144*/"Prints the ASM code to generate the given class.");
            System.err.println(/*EL:146*/"Usage: ASMifier [-debug] <fully qualified class name or class file name>");
            /*SL:148*/return;
        }
        final ClassReader v5;
        /*SL:151*/if (v1[v2].endsWith(".class") || v1[v2].indexOf(92) > -1 || v1[v2].indexOf(47) > /*EL:152*/-1) {
            final ClassReader a1 = /*EL:153*/new ClassReader(new FileInputStream(v1[v2]));
        }
        else {
            /*SL:155*/v5 = new ClassReader(v1[v2]);
        }
        /*SL:157*/v5.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), v3);
    }
    
    public void visit(final int a4, final int a5, final String a6, final String v1, final String v2, final String[] v3) {
        final int v4 = /*EL:170*/a6.lastIndexOf(47);
        final String v5;
        /*SL:171*/if (v4 != -1) {
            /*SL:174*/this.text.add("package asm." + a6.substring(0, v4).replace('/', '.') + ";\n");
            /*SL:176*/v5 = a6.substring(v4 + 1);
        }
        /*SL:178*/this.text.add("import java.util.*;\n");
        /*SL:179*/this.text.add("import org.objectweb.asm.*;\n");
        /*SL:180*/this.text.add("public class " + v5 + "Dump implements Opcodes {\n\n");
        /*SL:181*/this.text.add("public static byte[] dump () throws Exception {\n\n");
        /*SL:182*/this.text.add("ClassWriter cw = new ClassWriter(0);\n");
        /*SL:183*/this.text.add("FieldVisitor fv;\n");
        /*SL:184*/this.text.add("MethodVisitor mv;\n");
        /*SL:185*/this.text.add("AnnotationVisitor av0;\n\n");
        /*SL:187*/this.buf.setLength(0);
        /*SL:188*/this.buf.append("cw.visit(");
        /*SL:189*/switch (a4) {
            case 196653: {
                /*SL:191*/this.buf.append("V1_1");
                /*SL:192*/break;
            }
            case 46: {
                /*SL:194*/this.buf.append("V1_2");
                /*SL:195*/break;
            }
            case 47: {
                /*SL:197*/this.buf.append("V1_3");
                /*SL:198*/break;
            }
            case 48: {
                /*SL:200*/this.buf.append("V1_4");
                /*SL:201*/break;
            }
            case 49: {
                /*SL:203*/this.buf.append("V1_5");
                /*SL:204*/break;
            }
            case 50: {
                /*SL:206*/this.buf.append("V1_6");
                /*SL:207*/break;
            }
            case 51: {
                /*SL:209*/this.buf.append("V1_7");
                /*SL:210*/break;
            }
            default: {
                /*SL:212*/this.buf.append(a4);
                break;
            }
        }
        /*SL:215*/this.buf.append(", ");
        /*SL:216*/this.appendAccess(a5 | 0x40000);
        /*SL:217*/this.buf.append(", ");
        /*SL:218*/this.appendConstant(a6);
        /*SL:219*/this.buf.append(", ");
        /*SL:220*/this.appendConstant(v1);
        /*SL:221*/this.buf.append(", ");
        /*SL:222*/this.appendConstant(v2);
        /*SL:223*/this.buf.append(", ");
        /*SL:224*/if (v3 != null && v3.length > 0) {
            /*SL:225*/this.buf.append("new String[] {");
            /*SL:226*/for (int a7 = 0; a7 < v3.length; ++a7) {
                /*SL:227*/this.buf.append((a7 == 0) ? " " : ", ");
                /*SL:228*/this.appendConstant(v3[a7]);
            }
            /*SL:230*/this.buf.append(" }");
        }
        else {
            /*SL:232*/this.buf.append("null");
        }
        /*SL:234*/this.buf.append(");\n\n");
        /*SL:235*/this.text.add(this.buf.toString());
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:240*/this.buf.setLength(0);
        /*SL:241*/this.buf.append("cw.visitSource(");
        /*SL:242*/this.appendConstant(a1);
        /*SL:243*/this.buf.append(", ");
        /*SL:244*/this.appendConstant(a2);
        /*SL:245*/this.buf.append(");\n\n");
        /*SL:246*/this.text.add(this.buf.toString());
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:252*/this.buf.setLength(0);
        /*SL:253*/this.buf.append("cw.visitOuterClass(");
        /*SL:254*/this.appendConstant(a1);
        /*SL:255*/this.buf.append(", ");
        /*SL:256*/this.appendConstant(a2);
        /*SL:257*/this.buf.append(", ");
        /*SL:258*/this.appendConstant(a3);
        /*SL:259*/this.buf.append(");\n\n");
        /*SL:260*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitClassAnnotation(final String a1, final boolean a2) {
        /*SL:266*/return this.visitAnnotation(a1, a2);
    }
    
    public ASMifier visitClassTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:272*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public void visitClassAttribute(final Attribute a1) {
        /*SL:277*/this.visitAttribute(a1);
    }
    
    public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        /*SL:283*/this.buf.setLength(0);
        /*SL:284*/this.buf.append("cw.visitInnerClass(");
        /*SL:285*/this.appendConstant(a1);
        /*SL:286*/this.buf.append(", ");
        /*SL:287*/this.appendConstant(a2);
        /*SL:288*/this.buf.append(", ");
        /*SL:289*/this.appendConstant(a3);
        /*SL:290*/this.buf.append(", ");
        /*SL:291*/this.appendAccess(a4 | 0x100000);
        /*SL:292*/this.buf.append(");\n\n");
        /*SL:293*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        /*SL:299*/this.buf.setLength(0);
        /*SL:300*/this.buf.append("{\n");
        /*SL:301*/this.buf.append("fv = cw.visitField(");
        /*SL:302*/this.appendAccess(a1 | 0x80000);
        /*SL:303*/this.buf.append(", ");
        /*SL:304*/this.appendConstant(a2);
        /*SL:305*/this.buf.append(", ");
        /*SL:306*/this.appendConstant(a3);
        /*SL:307*/this.buf.append(", ");
        /*SL:308*/this.appendConstant(a4);
        /*SL:309*/this.buf.append(", ");
        /*SL:310*/this.appendConstant(a5);
        /*SL:311*/this.buf.append(");\n");
        /*SL:312*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:313*/this.createASMifier("fv", 0);
        /*SL:314*/this.text.add(v1.getText());
        /*SL:315*/this.text.add("}\n");
        /*SL:316*/return v1;
    }
    
    public ASMifier visitMethod(final int a3, final String a4, final String a5, final String v1, final String[] v2) {
        /*SL:322*/this.buf.setLength(0);
        /*SL:323*/this.buf.append("{\n");
        /*SL:324*/this.buf.append("mv = cw.visitMethod(");
        /*SL:325*/this.appendAccess(a3);
        /*SL:326*/this.buf.append(", ");
        /*SL:327*/this.appendConstant(a4);
        /*SL:328*/this.buf.append(", ");
        /*SL:329*/this.appendConstant(a5);
        /*SL:330*/this.buf.append(", ");
        /*SL:331*/this.appendConstant(v1);
        /*SL:332*/this.buf.append(", ");
        /*SL:333*/if (v2 != null && v2.length > 0) {
            /*SL:334*/this.buf.append("new String[] {");
            /*SL:335*/for (int a6 = 0; a6 < v2.length; ++a6) {
                /*SL:336*/this.buf.append((a6 == 0) ? " " : ", ");
                /*SL:337*/this.appendConstant(v2[a6]);
            }
            /*SL:339*/this.buf.append(" }");
        }
        else {
            /*SL:341*/this.buf.append("null");
        }
        /*SL:343*/this.buf.append(");\n");
        /*SL:344*/this.text.add(this.buf.toString());
        final ASMifier v3 = /*EL:345*/this.createASMifier("mv", 0);
        /*SL:346*/this.text.add(v3.getText());
        /*SL:347*/this.text.add("}\n");
        /*SL:348*/return v3;
    }
    
    public void visitClassEnd() {
        /*SL:353*/this.text.add("cw.visitEnd();\n\n");
        /*SL:354*/this.text.add("return cw.toByteArray();\n");
        /*SL:355*/this.text.add("}\n");
        /*SL:356*/this.text.add("}\n");
    }
    
    public void visit(final String a1, final Object a2) {
        /*SL:365*/this.buf.setLength(0);
        /*SL:366*/this.buf.append("av").append(this.id).append(".visit(");
        appendConstant(/*EL:367*/this.buf, a1);
        /*SL:368*/this.buf.append(", ");
        appendConstant(/*EL:369*/this.buf, a2);
        /*SL:370*/this.buf.append(");\n");
        /*SL:371*/this.text.add(this.buf.toString());
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:377*/this.buf.setLength(0);
        /*SL:378*/this.buf.append("av").append(this.id).append(".visitEnum(");
        appendConstant(/*EL:379*/this.buf, a1);
        /*SL:380*/this.buf.append(", ");
        appendConstant(/*EL:381*/this.buf, a2);
        /*SL:382*/this.buf.append(", ");
        appendConstant(/*EL:383*/this.buf, a3);
        /*SL:384*/this.buf.append(");\n");
        /*SL:385*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitAnnotation(final String a1, final String a2) {
        /*SL:390*/this.buf.setLength(0);
        /*SL:391*/this.buf.append("{\n");
        /*SL:392*/this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        /*SL:393*/this.buf.append(this.id).append(".visitAnnotation(");
        appendConstant(/*EL:394*/this.buf, a1);
        /*SL:395*/this.buf.append(", ");
        appendConstant(/*EL:396*/this.buf, a2);
        /*SL:397*/this.buf.append(");\n");
        /*SL:398*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:399*/this.createASMifier("av", this.id + 1);
        /*SL:400*/this.text.add(v1.getText());
        /*SL:401*/this.text.add("}\n");
        /*SL:402*/return v1;
    }
    
    public ASMifier visitArray(final String a1) {
        /*SL:407*/this.buf.setLength(0);
        /*SL:408*/this.buf.append("{\n");
        /*SL:409*/this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        /*SL:410*/this.buf.append(this.id).append(".visitArray(");
        appendConstant(/*EL:411*/this.buf, a1);
        /*SL:412*/this.buf.append(");\n");
        /*SL:413*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:414*/this.createASMifier("av", this.id + 1);
        /*SL:415*/this.text.add(v1.getText());
        /*SL:416*/this.text.add("}\n");
        /*SL:417*/return v1;
    }
    
    public void visitAnnotationEnd() {
        /*SL:422*/this.buf.setLength(0);
        /*SL:423*/this.buf.append("av").append(this.id).append(".visitEnd();\n");
        /*SL:424*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitFieldAnnotation(final String a1, final boolean a2) {
        /*SL:434*/return this.visitAnnotation(a1, a2);
    }
    
    public ASMifier visitFieldTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:440*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public void visitFieldAttribute(final Attribute a1) {
        /*SL:445*/this.visitAttribute(a1);
    }
    
    public void visitFieldEnd() {
        /*SL:450*/this.buf.setLength(0);
        /*SL:451*/this.buf.append(this.name).append(".visitEnd();\n");
        /*SL:452*/this.text.add(this.buf.toString());
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:461*/this.buf.setLength(0);
        /*SL:462*/this.buf.append(this.name).append(".visitParameter(");
        /*SL:463*/Printer.appendString(this.buf, a1);
        /*SL:464*/this.buf.append(", ");
        /*SL:465*/this.appendAccess(a2);
        /*SL:466*/this.text.add(this.buf.append(");\n").toString());
    }
    
    public ASMifier visitAnnotationDefault() {
        /*SL:471*/this.buf.setLength(0);
        /*SL:472*/this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitAnnotationDefault();\n");
        /*SL:474*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:475*/this.createASMifier("av", 0);
        /*SL:476*/this.text.add(v1.getText());
        /*SL:477*/this.text.add("}\n");
        /*SL:478*/return v1;
    }
    
    public ASMifier visitMethodAnnotation(final String a1, final boolean a2) {
        /*SL:484*/return this.visitAnnotation(a1, a2);
    }
    
    public ASMifier visitMethodTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:490*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public ASMifier visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        /*SL:496*/this.buf.setLength(0);
        /*SL:497*/this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitParameterAnnotation(").append(/*EL:498*/a1).append(", ");
        /*SL:500*/this.appendConstant(a2);
        /*SL:501*/this.buf.append(", ").append(a3).append(");\n");
        /*SL:502*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:503*/this.createASMifier("av", 0);
        /*SL:504*/this.text.add(v1.getText());
        /*SL:505*/this.text.add("}\n");
        /*SL:506*/return v1;
    }
    
    public void visitMethodAttribute(final Attribute a1) {
        /*SL:511*/this.visitAttribute(a1);
    }
    
    public void visitCode() {
        /*SL:516*/this.text.add(this.name + ".visitCode();\n");
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:522*/this.buf.setLength(0);
        /*SL:523*/switch (a1) {
            case -1:
            case 0: {
                /*SL:526*/this.declareFrameTypes(a2, a3);
                /*SL:527*/this.declareFrameTypes(a4, a5);
                /*SL:528*/if (a1 == -1) {
                    /*SL:529*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_NEW, ");
                }
                else {
                    /*SL:531*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_FULL, ");
                }
                /*SL:533*/this.buf.append(a2).append(", new Object[] {");
                /*SL:534*/this.appendFrameTypes(a2, a3);
                /*SL:535*/this.buf.append("}, ").append(a4).append(", new Object[] {");
                /*SL:536*/this.appendFrameTypes(a4, a5);
                /*SL:537*/this.buf.append('}');
                /*SL:538*/break;
            }
            case 1: {
                /*SL:540*/this.declareFrameTypes(a2, a3);
                /*SL:541*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_APPEND,").append(a2).append(/*EL:542*/", new Object[] {");
                /*SL:543*/this.appendFrameTypes(a2, a3);
                /*SL:544*/this.buf.append("}, 0, null");
                /*SL:545*/break;
            }
            case 2: {
                /*SL:547*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_CHOP,").append(a2).append(/*EL:548*/", null, 0, null");
                /*SL:549*/break;
            }
            case 3: {
                /*SL:551*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME, 0, null, 0, null");
                /*SL:553*/break;
            }
            case 4: {
                /*SL:555*/this.declareFrameTypes(1, a5);
                /*SL:556*/this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {");
                /*SL:558*/this.appendFrameTypes(1, a5);
                /*SL:559*/this.buf.append('}');
                break;
            }
        }
        /*SL:562*/this.buf.append(");\n");
        /*SL:563*/this.text.add(this.buf.toString());
    }
    
    public void visitInsn(final int a1) {
        /*SL:568*/this.buf.setLength(0);
        /*SL:569*/this.buf.append(this.name).append(".visitInsn(").append(ASMifier.OPCODES[a1]).append(");\n");
        /*SL:571*/this.text.add(this.buf.toString());
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:576*/this.buf.setLength(0);
        /*SL:577*/this.buf.append(this.name).append(".visitIntInsn(").append(/*EL:578*/ASMifier.OPCODES[a1]).append(/*EL:579*/", ").append(/*EL:580*/(a1 == 188) ? ASMifier.TYPES[a2] : /*EL:582*/Integer.toString(a2)).append(");\n");
        /*SL:583*/this.text.add(this.buf.toString());
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:588*/this.buf.setLength(0);
        /*SL:589*/this.buf.append(this.name).append(".visitVarInsn(").append(ASMifier.OPCODES[a1]).append(", ").append(/*EL:590*/a2).append(");\n");
        /*SL:591*/this.text.add(this.buf.toString());
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:596*/this.buf.setLength(0);
        /*SL:597*/this.buf.append(this.name).append(".visitTypeInsn(").append(ASMifier.OPCODES[a1]).append(", ");
        /*SL:599*/this.appendConstant(a2);
        /*SL:600*/this.buf.append(");\n");
        /*SL:601*/this.text.add(this.buf.toString());
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:607*/this.buf.setLength(0);
        /*SL:608*/this.buf.append(this.name).append(".visitFieldInsn(").append(ASMifier.OPCODES[a1]).append(/*EL:609*/", ");
        /*SL:610*/this.appendConstant(a2);
        /*SL:611*/this.buf.append(", ");
        /*SL:612*/this.appendConstant(a3);
        /*SL:613*/this.buf.append(", ");
        /*SL:614*/this.appendConstant(a4);
        /*SL:615*/this.buf.append(");\n");
        /*SL:616*/this.text.add(this.buf.toString());
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:623*/if (this.api >= 327680) {
            /*SL:624*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:625*/return;
        }
        /*SL:627*/this.doVisitMethodInsn(a1, a2, a3, a4, a1 == 185);
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:634*/if (this.api < 327680) {
            /*SL:635*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:636*/return;
        }
        /*SL:638*/this.doVisitMethodInsn(a1, a2, a3, a4, a5);
    }
    
    private void doVisitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:643*/this.buf.setLength(0);
        /*SL:644*/this.buf.append(this.name).append(".visitMethodInsn(").append(ASMifier.OPCODES[a1]).append(/*EL:645*/", ");
        /*SL:646*/this.appendConstant(a2);
        /*SL:647*/this.buf.append(", ");
        /*SL:648*/this.appendConstant(a3);
        /*SL:649*/this.buf.append(", ");
        /*SL:650*/this.appendConstant(a4);
        /*SL:651*/this.buf.append(", ");
        /*SL:652*/this.buf.append(a5 ? "true" : "false");
        /*SL:653*/this.buf.append(");\n");
        /*SL:654*/this.text.add(this.buf.toString());
    }
    
    public void visitInvokeDynamicInsn(final String a3, final String a4, final Handle v1, final Object... v2) {
        /*SL:660*/this.buf.setLength(0);
        /*SL:661*/this.buf.append(this.name).append(".visitInvokeDynamicInsn(");
        /*SL:662*/this.appendConstant(a3);
        /*SL:663*/this.buf.append(", ");
        /*SL:664*/this.appendConstant(a4);
        /*SL:665*/this.buf.append(", ");
        /*SL:666*/this.appendConstant(v1);
        /*SL:667*/this.buf.append(", new Object[]{");
        /*SL:668*/for (int a5 = 0; a5 < v2.length; ++a5) {
            /*SL:669*/this.appendConstant(v2[a5]);
            /*SL:670*/if (a5 != v2.length - 1) {
                /*SL:671*/this.buf.append(", ");
            }
        }
        /*SL:674*/this.buf.append("});\n");
        /*SL:675*/this.text.add(this.buf.toString());
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:680*/this.buf.setLength(0);
        /*SL:681*/this.declareLabel(a2);
        /*SL:682*/this.buf.append(this.name).append(".visitJumpInsn(").append(ASMifier.OPCODES[a1]).append(", ");
        /*SL:684*/this.appendLabel(a2);
        /*SL:685*/this.buf.append(");\n");
        /*SL:686*/this.text.add(this.buf.toString());
    }
    
    public void visitLabel(final Label a1) {
        /*SL:691*/this.buf.setLength(0);
        /*SL:692*/this.declareLabel(a1);
        /*SL:693*/this.buf.append(this.name).append(".visitLabel(");
        /*SL:694*/this.appendLabel(a1);
        /*SL:695*/this.buf.append(");\n");
        /*SL:696*/this.text.add(this.buf.toString());
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:701*/this.buf.setLength(0);
        /*SL:702*/this.buf.append(this.name).append(".visitLdcInsn(");
        /*SL:703*/this.appendConstant(a1);
        /*SL:704*/this.buf.append(");\n");
        /*SL:705*/this.text.add(this.buf.toString());
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:710*/this.buf.setLength(0);
        /*SL:711*/this.buf.append(this.name).append(".visitIincInsn(").append(a1).append(", ").append(a2).append(/*EL:712*/");\n");
        /*SL:713*/this.text.add(this.buf.toString());
    }
    
    public void visitTableSwitchInsn(final int a4, final int v1, final Label v2, final Label... v3) {
        /*SL:719*/this.buf.setLength(0);
        /*SL:720*/for (int a5 = 0; a5 < v3.length; ++a5) {
            /*SL:721*/this.declareLabel(v3[a5]);
        }
        /*SL:723*/this.declareLabel(v2);
        /*SL:725*/this.buf.append(this.name).append(".visitTableSwitchInsn(").append(a4).append(", ").append(/*EL:726*/v1).append(", ");
        /*SL:727*/this.appendLabel(v2);
        /*SL:728*/this.buf.append(", new Label[] {");
        /*SL:729*/for (int a6 = 0; a6 < v3.length; ++a6) {
            /*SL:730*/this.buf.append((a6 == 0) ? " " : ", ");
            /*SL:731*/this.appendLabel(v3[a6]);
        }
        /*SL:733*/this.buf.append(" });\n");
        /*SL:734*/this.text.add(this.buf.toString());
    }
    
    public void visitLookupSwitchInsn(final Label v2, final int[] v3, final Label[] v4) {
        /*SL:740*/this.buf.setLength(0);
        /*SL:741*/for (int a1 = 0; a1 < v4.length; ++a1) {
            /*SL:742*/this.declareLabel(v4[a1]);
        }
        /*SL:744*/this.declareLabel(v2);
        /*SL:746*/this.buf.append(this.name).append(".visitLookupSwitchInsn(");
        /*SL:747*/this.appendLabel(v2);
        /*SL:748*/this.buf.append(", new int[] {");
        /*SL:749*/for (int a2 = 0; a2 < v3.length; ++a2) {
            /*SL:750*/this.buf.append((a2 == 0) ? " " : ", ").append(v3[a2]);
        }
        /*SL:752*/this.buf.append(" }, new Label[] {");
        /*SL:753*/for (int a3 = 0; a3 < v4.length; ++a3) {
            /*SL:754*/this.buf.append((a3 == 0) ? " " : ", ");
            /*SL:755*/this.appendLabel(v4[a3]);
        }
        /*SL:757*/this.buf.append(" });\n");
        /*SL:758*/this.text.add(this.buf.toString());
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:763*/this.buf.setLength(0);
        /*SL:764*/this.buf.append(this.name).append(".visitMultiANewArrayInsn(");
        /*SL:765*/this.appendConstant(a1);
        /*SL:766*/this.buf.append(", ").append(a2).append(");\n");
        /*SL:767*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:773*/return this.visitTypeAnnotation("visitInsnAnnotation", a1, a2, a3, a4);
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:780*/this.buf.setLength(0);
        /*SL:781*/this.declareLabel(a1);
        /*SL:782*/this.declareLabel(a2);
        /*SL:783*/this.declareLabel(a3);
        /*SL:784*/this.buf.append(this.name).append(".visitTryCatchBlock(");
        /*SL:785*/this.appendLabel(a1);
        /*SL:786*/this.buf.append(", ");
        /*SL:787*/this.appendLabel(a2);
        /*SL:788*/this.buf.append(", ");
        /*SL:789*/this.appendLabel(a3);
        /*SL:790*/this.buf.append(", ");
        /*SL:791*/this.appendConstant(a4);
        /*SL:792*/this.buf.append(");\n");
        /*SL:793*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:799*/return this.visitTypeAnnotation("visitTryCatchAnnotation", a1, a2, a3, a4);
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:807*/this.buf.setLength(0);
        /*SL:808*/this.buf.append(this.name).append(".visitLocalVariable(");
        /*SL:809*/this.appendConstant(a1);
        /*SL:810*/this.buf.append(", ");
        /*SL:811*/this.appendConstant(a2);
        /*SL:812*/this.buf.append(", ");
        /*SL:813*/this.appendConstant(a3);
        /*SL:814*/this.buf.append(", ");
        /*SL:815*/this.appendLabel(a4);
        /*SL:816*/this.buf.append(", ");
        /*SL:817*/this.appendLabel(a5);
        /*SL:818*/this.buf.append(", ").append(a6).append(");\n");
        /*SL:819*/this.text.add(this.buf.toString());
    }
    
    public Printer visitLocalVariableAnnotation(final int a5, final TypePath a6, final Label[] a7, final Label[] v1, final int[] v2, final String v3, final boolean v4) {
        /*SL:826*/this.buf.setLength(0);
        /*SL:827*/this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitLocalVariableAnnotation(");
        /*SL:829*/this.buf.append(a5);
        /*SL:830*/if (a6 == null) {
            /*SL:831*/this.buf.append(", null, ");
        }
        else {
            /*SL:833*/this.buf.append(", TypePath.fromString(\"").append(a6).append("\"), ");
        }
        /*SL:835*/this.buf.append("new Label[] {");
        /*SL:836*/for (int a8 = 0; a8 < a7.length; ++a8) {
            /*SL:837*/this.buf.append((a8 == 0) ? " " : ", ");
            /*SL:838*/this.appendLabel(a7[a8]);
        }
        /*SL:840*/this.buf.append(" }, new Label[] {");
        /*SL:841*/for (int a9 = 0; a9 < v1.length; ++a9) {
            /*SL:842*/this.buf.append((a9 == 0) ? " " : ", ");
            /*SL:843*/this.appendLabel(v1[a9]);
        }
        /*SL:845*/this.buf.append(" }, new int[] {");
        /*SL:846*/for (int a10 = 0; a10 < v2.length; ++a10) {
            /*SL:847*/this.buf.append((a10 == 0) ? " " : ", ").append(v2[a10]);
        }
        /*SL:849*/this.buf.append(" }, ");
        /*SL:850*/this.appendConstant(v3);
        /*SL:851*/this.buf.append(", ").append(v4).append(");\n");
        /*SL:852*/this.text.add(this.buf.toString());
        final ASMifier v5 = /*EL:853*/this.createASMifier("av", 0);
        /*SL:854*/this.text.add(v5.getText());
        /*SL:855*/this.text.add("}\n");
        /*SL:856*/return v5;
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:861*/this.buf.setLength(0);
        /*SL:862*/this.buf.append(this.name).append(".visitLineNumber(").append(a1).append(", ");
        /*SL:863*/this.appendLabel(a2);
        /*SL:864*/this.buf.append(");\n");
        /*SL:865*/this.text.add(this.buf.toString());
    }
    
    public void visitMaxs(final int a1, final int a2) {
        /*SL:870*/this.buf.setLength(0);
        /*SL:871*/this.buf.append(this.name).append(".visitMaxs(").append(a1).append(", ").append(a2).append(/*EL:872*/");\n");
        /*SL:873*/this.text.add(this.buf.toString());
    }
    
    public void visitMethodEnd() {
        /*SL:878*/this.buf.setLength(0);
        /*SL:879*/this.buf.append(this.name).append(".visitEnd();\n");
        /*SL:880*/this.text.add(this.buf.toString());
    }
    
    public ASMifier visitAnnotation(final String a1, final boolean a2) {
        /*SL:888*/this.buf.setLength(0);
        /*SL:889*/this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitAnnotation(");
        /*SL:891*/this.appendConstant(a1);
        /*SL:892*/this.buf.append(", ").append(a2).append(");\n");
        /*SL:893*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:894*/this.createASMifier("av", 0);
        /*SL:895*/this.text.add(v1.getText());
        /*SL:896*/this.text.add("}\n");
        /*SL:897*/return v1;
    }
    
    public ASMifier visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:902*/return this.visitTypeAnnotation("visitTypeAnnotation", a1, a2, a3, a4);
    }
    
    public ASMifier visitTypeAnnotation(final String a1, final int a2, final TypePath a3, final String a4, final boolean a5) {
        /*SL:908*/this.buf.setLength(0);
        /*SL:909*/this.buf.append("{\n").append("av0 = ").append(this.name).append(".").append(a1).append(/*EL:910*/"(");
        /*SL:911*/this.buf.append(a2);
        /*SL:912*/if (a3 == null) {
            /*SL:913*/this.buf.append(", null, ");
        }
        else {
            /*SL:915*/this.buf.append(", TypePath.fromString(\"").append(a3).append("\"), ");
        }
        /*SL:917*/this.appendConstant(a4);
        /*SL:918*/this.buf.append(", ").append(a5).append(");\n");
        /*SL:919*/this.text.add(this.buf.toString());
        final ASMifier v1 = /*EL:920*/this.createASMifier("av", 0);
        /*SL:921*/this.text.add(v1.getText());
        /*SL:922*/this.text.add("}\n");
        /*SL:923*/return v1;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:927*/this.buf.setLength(0);
        /*SL:928*/this.buf.append("// ATTRIBUTE ").append(a1.type).append('\n');
        /*SL:929*/if (a1 instanceof ASMifiable) {
            /*SL:930*/if (this.labelNames == null) {
                /*SL:931*/this.labelNames = new HashMap<Label, String>();
            }
            /*SL:933*/this.buf.append("{\n");
            /*SL:934*/((ASMifiable)a1).asmify(this.buf, "attr", this.labelNames);
            /*SL:935*/this.buf.append(this.name).append(".visitAttribute(attr);\n");
            /*SL:936*/this.buf.append("}\n");
        }
        /*SL:938*/this.text.add(this.buf.toString());
    }
    
    protected ASMifier createASMifier(final String a1, final int a2) {
        /*SL:946*/return new ASMifier(327680, a1, a2);
    }
    
    void appendAccess(final int a1) {
        boolean v1 = /*EL:957*/true;
        /*SL:958*/if ((a1 & 0x1) != 0x0) {
            /*SL:959*/this.buf.append("ACC_PUBLIC");
            /*SL:960*/v1 = false;
        }
        /*SL:962*/if ((a1 & 0x2) != 0x0) {
            /*SL:963*/this.buf.append("ACC_PRIVATE");
            /*SL:964*/v1 = false;
        }
        /*SL:966*/if ((a1 & 0x4) != 0x0) {
            /*SL:967*/this.buf.append("ACC_PROTECTED");
            /*SL:968*/v1 = false;
        }
        /*SL:970*/if ((a1 & 0x10) != 0x0) {
            /*SL:971*/if (!v1) {
                /*SL:972*/this.buf.append(" + ");
            }
            /*SL:974*/this.buf.append("ACC_FINAL");
            /*SL:975*/v1 = false;
        }
        /*SL:977*/if ((a1 & 0x8) != 0x0) {
            /*SL:978*/if (!v1) {
                /*SL:979*/this.buf.append(" + ");
            }
            /*SL:981*/this.buf.append("ACC_STATIC");
            /*SL:982*/v1 = false;
        }
        /*SL:984*/if ((a1 & 0x20) != 0x0) {
            /*SL:985*/if (!v1) {
                /*SL:986*/this.buf.append(" + ");
            }
            /*SL:988*/if ((a1 & 0x40000) == 0x0) {
                /*SL:989*/this.buf.append("ACC_SYNCHRONIZED");
            }
            else {
                /*SL:991*/this.buf.append("ACC_SUPER");
            }
            /*SL:993*/v1 = false;
        }
        /*SL:995*/if ((a1 & 0x40) != 0x0 && (a1 & 0x80000) != 0x0) {
            /*SL:997*/if (!v1) {
                /*SL:998*/this.buf.append(" + ");
            }
            /*SL:1000*/this.buf.append("ACC_VOLATILE");
            /*SL:1001*/v1 = false;
        }
        /*SL:1003*/if ((a1 & 0x40) != 0x0 && (a1 & 0x40000) == 0x0 && (a1 & 0x80000) == 0x0) {
            /*SL:1005*/if (!v1) {
                /*SL:1006*/this.buf.append(" + ");
            }
            /*SL:1008*/this.buf.append("ACC_BRIDGE");
            /*SL:1009*/v1 = false;
        }
        /*SL:1011*/if ((a1 & 0x80) != 0x0 && (a1 & 0x40000) == 0x0 && (a1 & 0x80000) == 0x0) {
            /*SL:1013*/if (!v1) {
                /*SL:1014*/this.buf.append(" + ");
            }
            /*SL:1016*/this.buf.append("ACC_VARARGS");
            /*SL:1017*/v1 = false;
        }
        /*SL:1019*/if ((a1 & 0x80) != 0x0 && (a1 & 0x80000) != 0x0) {
            /*SL:1021*/if (!v1) {
                /*SL:1022*/this.buf.append(" + ");
            }
            /*SL:1024*/this.buf.append("ACC_TRANSIENT");
            /*SL:1025*/v1 = false;
        }
        /*SL:1027*/if ((a1 & 0x100) != 0x0 && (a1 & 0x40000) == 0x0 && (a1 & 0x80000) == 0x0) {
            /*SL:1029*/if (!v1) {
                /*SL:1030*/this.buf.append(" + ");
            }
            /*SL:1032*/this.buf.append("ACC_NATIVE");
            /*SL:1033*/v1 = false;
        }
        /*SL:1035*/if ((a1 & 0x4000) != 0x0 && ((a1 & 0x40000) != 0x0 || (a1 & 0x80000) != 0x0 || (a1 & 0x100000) != 0x0)) {
            /*SL:1038*/if (!v1) {
                /*SL:1039*/this.buf.append(" + ");
            }
            /*SL:1041*/this.buf.append("ACC_ENUM");
            /*SL:1042*/v1 = false;
        }
        /*SL:1044*/if ((a1 & 0x2000) != 0x0 && ((a1 & 0x40000) != 0x0 || (a1 & 0x100000) != 0x0)) {
            /*SL:1046*/if (!v1) {
                /*SL:1047*/this.buf.append(" + ");
            }
            /*SL:1049*/this.buf.append("ACC_ANNOTATION");
            /*SL:1050*/v1 = false;
        }
        /*SL:1052*/if ((a1 & 0x400) != 0x0) {
            /*SL:1053*/if (!v1) {
                /*SL:1054*/this.buf.append(" + ");
            }
            /*SL:1056*/this.buf.append("ACC_ABSTRACT");
            /*SL:1057*/v1 = false;
        }
        /*SL:1059*/if ((a1 & 0x200) != 0x0) {
            /*SL:1060*/if (!v1) {
                /*SL:1061*/this.buf.append(" + ");
            }
            /*SL:1063*/this.buf.append("ACC_INTERFACE");
            /*SL:1064*/v1 = false;
        }
        /*SL:1066*/if ((a1 & 0x800) != 0x0) {
            /*SL:1067*/if (!v1) {
                /*SL:1068*/this.buf.append(" + ");
            }
            /*SL:1070*/this.buf.append("ACC_STRICT");
            /*SL:1071*/v1 = false;
        }
        /*SL:1073*/if ((a1 & 0x1000) != 0x0) {
            /*SL:1074*/if (!v1) {
                /*SL:1075*/this.buf.append(" + ");
            }
            /*SL:1077*/this.buf.append("ACC_SYNTHETIC");
            /*SL:1078*/v1 = false;
        }
        /*SL:1080*/if ((a1 & 0x20000) != 0x0) {
            /*SL:1081*/if (!v1) {
                /*SL:1082*/this.buf.append(" + ");
            }
            /*SL:1084*/this.buf.append("ACC_DEPRECATED");
            /*SL:1085*/v1 = false;
        }
        /*SL:1087*/if ((a1 & 0x8000) != 0x0) {
            /*SL:1088*/if (!v1) {
                /*SL:1089*/this.buf.append(" + ");
            }
            /*SL:1091*/this.buf.append("ACC_MANDATED");
            /*SL:1092*/v1 = false;
        }
        /*SL:1094*/if (v1) {
            /*SL:1095*/this.buf.append('0');
        }
    }
    
    protected void appendConstant(final Object a1) {
        appendConstant(/*EL:1108*/this.buf, a1);
    }
    
    static void appendConstant(final StringBuffer v-2, final Object v-1) {
        /*SL:1122*/if (v-1 == null) {
            /*SL:1123*/v-2.append("null");
        }
        else/*SL:1124*/ if (v-1 instanceof String) {
            /*SL:1125*/Printer.appendString(v-2, (String)v-1);
        }
        else/*SL:1126*/ if (v-1 instanceof Type) {
            /*SL:1127*/v-2.append("Type.getType(\"");
            /*SL:1128*/v-2.append(((Type)v-1).getDescriptor());
            /*SL:1129*/v-2.append("\")");
        }
        else/*SL:1130*/ if (v-1 instanceof Handle) {
            /*SL:1131*/v-2.append("new Handle(");
            final Handle a1 = /*EL:1132*/(Handle)v-1;
            /*SL:1133*/v-2.append("Opcodes.").append(ASMifier.HANDLE_TAG[a1.getTag()]).append(", \"");
            /*SL:1135*/v-2.append(a1.getOwner()).append("\", \"");
            /*SL:1136*/v-2.append(a1.getName()).append("\", \"");
            /*SL:1137*/v-2.append(a1.getDesc()).append("\")");
        }
        else/*SL:1138*/ if (v-1 instanceof Byte) {
            /*SL:1139*/v-2.append("new Byte((byte)").append(v-1).append(')');
        }
        else/*SL:1140*/ if (v-1 instanceof Boolean) {
            /*SL:1141*/v-2.append(v-1 ? "Boolean.TRUE" : "Boolean.FALSE");
        }
        else/*SL:1143*/ if (v-1 instanceof Short) {
            /*SL:1144*/v-2.append("new Short((short)").append(v-1).append(')');
        }
        else/*SL:1145*/ if (v-1 instanceof Character) {
            final int a2 = /*EL:1146*/(char)v-1;
            /*SL:1147*/v-2.append("new Character((char)").append(a2).append(')');
        }
        else/*SL:1148*/ if (v-1 instanceof Integer) {
            /*SL:1149*/v-2.append("new Integer(").append(v-1).append(')');
        }
        else/*SL:1150*/ if (v-1 instanceof Float) {
            /*SL:1151*/v-2.append("new Float(\"").append(v-1).append("\")");
        }
        else/*SL:1152*/ if (v-1 instanceof Long) {
            /*SL:1153*/v-2.append("new Long(").append(v-1).append("L)");
        }
        else/*SL:1154*/ if (v-1 instanceof Double) {
            /*SL:1155*/v-2.append("new Double(\"").append(v-1).append("\")");
        }
        else/*SL:1156*/ if (v-1 instanceof byte[]) {
            final byte[] v0 = /*EL:1157*/(byte[])v-1;
            /*SL:1158*/v-2.append("new byte[] {");
            /*SL:1159*/for (int v = 0; v < v0.length; ++v) {
                /*SL:1160*/v-2.append((v == 0) ? "" : ",").append(v0[v]);
            }
            /*SL:1162*/v-2.append('}');
        }
        else/*SL:1163*/ if (v-1 instanceof boolean[]) {
            final boolean[] v2 = /*EL:1164*/(boolean[])v-1;
            /*SL:1165*/v-2.append("new boolean[] {");
            /*SL:1166*/for (int v = 0; v < v2.length; ++v) {
                /*SL:1167*/v-2.append((v == 0) ? "" : ",").append(v2[v]);
            }
            /*SL:1169*/v-2.append('}');
        }
        else/*SL:1170*/ if (v-1 instanceof short[]) {
            final short[] v3 = /*EL:1171*/(short[])v-1;
            /*SL:1172*/v-2.append("new short[] {");
            /*SL:1173*/for (int v = 0; v < v3.length; ++v) {
                /*SL:1174*/v-2.append((v == 0) ? "" : ",").append("(short)").append(v3[v]);
            }
            /*SL:1176*/v-2.append('}');
        }
        else/*SL:1177*/ if (v-1 instanceof char[]) {
            final char[] v4 = /*EL:1178*/(char[])v-1;
            /*SL:1179*/v-2.append("new char[] {");
            /*SL:1180*/for (int v = 0; v < v4.length; ++v) {
                /*SL:1181*/v-2.append((v == 0) ? "" : ",").append("(char)").append((int)v4[v]);
            }
            /*SL:1184*/v-2.append('}');
        }
        else/*SL:1185*/ if (v-1 instanceof int[]) {
            final int[] v5 = /*EL:1186*/(int[])v-1;
            /*SL:1187*/v-2.append("new int[] {");
            /*SL:1188*/for (int v = 0; v < v5.length; ++v) {
                /*SL:1189*/v-2.append((v == 0) ? "" : ",").append(v5[v]);
            }
            /*SL:1191*/v-2.append('}');
        }
        else/*SL:1192*/ if (v-1 instanceof long[]) {
            final long[] v6 = /*EL:1193*/(long[])v-1;
            /*SL:1194*/v-2.append("new long[] {");
            /*SL:1195*/for (int v = 0; v < v6.length; ++v) {
                /*SL:1196*/v-2.append((v == 0) ? "" : ",").append(v6[v]).append('L');
            }
            /*SL:1198*/v-2.append('}');
        }
        else/*SL:1199*/ if (v-1 instanceof float[]) {
            final float[] v7 = /*EL:1200*/(float[])v-1;
            /*SL:1201*/v-2.append("new float[] {");
            /*SL:1202*/for (int v = 0; v < v7.length; ++v) {
                /*SL:1203*/v-2.append((v == 0) ? "" : ",").append(v7[v]).append('f');
            }
            /*SL:1205*/v-2.append('}');
        }
        else/*SL:1206*/ if (v-1 instanceof double[]) {
            final double[] v8 = /*EL:1207*/(double[])v-1;
            /*SL:1208*/v-2.append("new double[] {");
            /*SL:1209*/for (int v = 0; v < v8.length; ++v) {
                /*SL:1210*/v-2.append((v == 0) ? "" : ",").append(v8[v]).append('d');
            }
            /*SL:1212*/v-2.append('}');
        }
    }
    
    private void declareFrameTypes(final int v1, final Object[] v2) {
        /*SL:1217*/for (int a1 = 0; a1 < v1; ++a1) {
            /*SL:1218*/if (v2[a1] instanceof Label) {
                /*SL:1219*/this.declareLabel((Label)v2[a1]);
            }
        }
    }
    
    private void appendFrameTypes(final int v1, final Object[] v2) {
        /*SL:1225*/for (int a1 = 0; a1 < v1; ++a1) {
            /*SL:1226*/if (a1 > 0) {
                /*SL:1227*/this.buf.append(", ");
            }
            /*SL:1229*/if (v2[a1] instanceof String) {
                /*SL:1230*/this.appendConstant(v2[a1]);
            }
            else/*SL:1231*/ if (v2[a1] instanceof Integer) {
                /*SL:1232*/switch ((int)v2[a1]) {
                    case 0: {
                        /*SL:1234*/this.buf.append("Opcodes.TOP");
                        /*SL:1235*/break;
                    }
                    case 1: {
                        /*SL:1237*/this.buf.append("Opcodes.INTEGER");
                        /*SL:1238*/break;
                    }
                    case 2: {
                        /*SL:1240*/this.buf.append("Opcodes.FLOAT");
                        /*SL:1241*/break;
                    }
                    case 3: {
                        /*SL:1243*/this.buf.append("Opcodes.DOUBLE");
                        /*SL:1244*/break;
                    }
                    case 4: {
                        /*SL:1246*/this.buf.append("Opcodes.LONG");
                        /*SL:1247*/break;
                    }
                    case 5: {
                        /*SL:1249*/this.buf.append("Opcodes.NULL");
                        /*SL:1250*/break;
                    }
                    case 6: {
                        /*SL:1252*/this.buf.append("Opcodes.UNINITIALIZED_THIS");
                        break;
                    }
                }
            }
            else {
                /*SL:1256*/this.appendLabel((Label)v2[a1]);
            }
        }
    }
    
    protected void declareLabel(final Label a1) {
        /*SL:1270*/if (this.labelNames == null) {
            /*SL:1271*/this.labelNames = new HashMap<Label, String>();
        }
        String v1 = /*EL:1273*/this.labelNames.get(a1);
        /*SL:1274*/if (v1 == null) {
            /*SL:1275*/v1 = "l" + this.labelNames.size();
            /*SL:1276*/this.labelNames.put(a1, v1);
            /*SL:1277*/this.buf.append("Label ").append(v1).append(" = new Label();\n");
        }
    }
    
    protected void appendLabel(final Label a1) {
        /*SL:1290*/this.buf.append(this.labelNames.get(a1));
    }
}
