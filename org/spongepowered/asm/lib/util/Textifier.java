package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.TypeReference;
import java.util.HashMap;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.lib.ClassVisitor;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import org.spongepowered.asm.lib.ClassReader;
import java.io.FileInputStream;
import org.spongepowered.asm.lib.Label;
import java.util.Map;

public class Textifier extends Printer
{
    public static final int INTERNAL_NAME = 0;
    public static final int FIELD_DESCRIPTOR = 1;
    public static final int FIELD_SIGNATURE = 2;
    public static final int METHOD_DESCRIPTOR = 3;
    public static final int METHOD_SIGNATURE = 4;
    public static final int CLASS_SIGNATURE = 5;
    public static final int TYPE_DECLARATION = 6;
    public static final int CLASS_DECLARATION = 7;
    public static final int PARAMETERS_DECLARATION = 8;
    public static final int HANDLE_DESCRIPTOR = 9;
    protected String tab;
    protected String tab2;
    protected String tab3;
    protected String ltab;
    protected Map<Label, String> labelNames;
    private int access;
    private int valueNumber;
    
    public Textifier() {
        this(327680);
        if (this.getClass() != Textifier.class) {
            throw new IllegalStateException();
        }
    }
    
    protected Textifier(final int a1) {
        super(a1);
        this.tab = "  ";
        this.tab2 = "    ";
        this.tab3 = "      ";
        this.ltab = "   ";
        this.valueNumber = 0;
    }
    
    public static void main(final String[] v1) throws Exception {
        int v2 = /*EL:185*/0;
        int v3 = /*EL:186*/2;
        boolean v4 = /*EL:188*/true;
        /*SL:189*/if (v1.length < 1 || v1.length > 2) {
            /*SL:190*/v4 = false;
        }
        /*SL:192*/if (v4 && "-debug".equals(v1[0])) {
            /*SL:193*/v2 = 1;
            /*SL:194*/v3 = 0;
            /*SL:195*/if (v1.length != 2) {
                /*SL:196*/v4 = false;
            }
        }
        /*SL:199*/if (!v4) {
            System.err.println(/*EL:200*/"Prints a disassembled view of the given class.");
            System.err.println(/*EL:202*/"Usage: Textifier [-debug] <fully qualified class name or class file name>");
            /*SL:204*/return;
        }
        final ClassReader v5;
        /*SL:207*/if (v1[v2].endsWith(".class") || v1[v2].indexOf(92) > -1 || v1[v2].indexOf(47) > /*EL:208*/-1) {
            final ClassReader a1 = /*EL:209*/new ClassReader(new FileInputStream(v1[v2]));
        }
        else {
            /*SL:211*/v5 = new ClassReader(v1[v2]);
        }
        /*SL:213*/v5.accept(new TraceClassVisitor(new PrintWriter(System.out)), v3);
    }
    
    public void visit(final int a5, final int a6, final String v1, final String v2, final String v3, final String[] v4) {
        /*SL:224*/this.access = a6;
        final int v5 = /*EL:225*/a5 & 0xFFFF;
        final int v6 = /*EL:226*/a5 >>> 16;
        /*SL:227*/this.buf.setLength(0);
        /*SL:228*/this.buf.append("// class version ").append(v5).append('.').append(v6).append(" (").append(/*EL:229*/a5).append(")\n");
        /*SL:230*/if ((a6 & 0x20000) != 0x0) {
            /*SL:231*/this.buf.append("// DEPRECATED\n");
        }
        /*SL:233*/this.buf.append("// access flags 0x").append(/*EL:234*/Integer.toHexString(a6).toUpperCase()).append('\n');
        /*SL:236*/this.appendDescriptor(5, v2);
        /*SL:237*/if (v2 != null) {
            final TraceSignatureVisitor a7 = /*EL:238*/new TraceSignatureVisitor(a6);
            final SignatureReader a8 = /*EL:239*/new SignatureReader(v2);
            /*SL:240*/a8.accept(a7);
            /*SL:241*/this.buf.append("// declaration: ").append(v1).append(a7.getDeclaration()).append(/*EL:242*/'\n');
        }
        /*SL:245*/this.appendAccess(a6 & 0xFFFFFFDF);
        /*SL:246*/if ((a6 & 0x2000) != 0x0) {
            /*SL:247*/this.buf.append("@interface ");
        }
        else/*SL:248*/ if ((a6 & 0x200) != 0x0) {
            /*SL:249*/this.buf.append("interface ");
        }
        else/*SL:250*/ if ((a6 & 0x4000) == 0x0) {
            /*SL:251*/this.buf.append("class ");
        }
        /*SL:253*/this.appendDescriptor(0, v1);
        /*SL:255*/if (v3 != null && !"java/lang/Object".equals(v3)) {
            /*SL:256*/this.buf.append(" extends ");
            /*SL:257*/this.appendDescriptor(0, v3);
            /*SL:258*/this.buf.append(' ');
        }
        /*SL:260*/if (v4 != null && v4.length > 0) {
            /*SL:261*/this.buf.append(" implements ");
            /*SL:262*/for (int a9 = 0; a9 < v4.length; ++a9) {
                /*SL:263*/this.appendDescriptor(0, v4[a9]);
                /*SL:264*/this.buf.append(' ');
            }
        }
        /*SL:267*/this.buf.append(" {\n\n");
        /*SL:269*/this.text.add(this.buf.toString());
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:274*/this.buf.setLength(0);
        /*SL:275*/if (a1 != null) {
            /*SL:276*/this.buf.append(this.tab).append("// compiled from: ").append(a1).append('\n');
        }
        /*SL:279*/if (a2 != null) {
            /*SL:280*/this.buf.append(this.tab).append("// debug info: ").append(a2).append('\n');
        }
        /*SL:283*/if (this.buf.length() > 0) {
            /*SL:284*/this.text.add(this.buf.toString());
        }
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:291*/this.buf.setLength(0);
        /*SL:292*/this.buf.append(this.tab).append("OUTERCLASS ");
        /*SL:293*/this.appendDescriptor(0, a1);
        /*SL:294*/this.buf.append(' ');
        /*SL:295*/if (a2 != null) {
            /*SL:296*/this.buf.append(a2).append(' ');
        }
        /*SL:298*/this.appendDescriptor(3, a3);
        /*SL:299*/this.buf.append('\n');
        /*SL:300*/this.text.add(this.buf.toString());
    }
    
    public Textifier visitClassAnnotation(final String a1, final boolean a2) {
        /*SL:306*/this.text.add("\n");
        /*SL:307*/return this.visitAnnotation(a1, a2);
    }
    
    public Printer visitClassTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:313*/this.text.add("\n");
        /*SL:314*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public void visitClassAttribute(final Attribute a1) {
        /*SL:319*/this.text.add("\n");
        /*SL:320*/this.visitAttribute(a1);
    }
    
    public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        /*SL:326*/this.buf.setLength(0);
        /*SL:327*/this.buf.append(this.tab).append("// access flags 0x");
        /*SL:328*/this.buf.append(/*EL:329*/Integer.toHexString(a4 & 0xFFFFFFDF).toUpperCase()).append('\n');
        /*SL:331*/this.buf.append(this.tab);
        /*SL:332*/this.appendAccess(a4);
        /*SL:333*/this.buf.append("INNERCLASS ");
        /*SL:334*/this.appendDescriptor(0, a1);
        /*SL:335*/this.buf.append(' ');
        /*SL:336*/this.appendDescriptor(0, a2);
        /*SL:337*/this.buf.append(' ');
        /*SL:338*/this.appendDescriptor(0, a3);
        /*SL:339*/this.buf.append('\n');
        /*SL:340*/this.text.add(this.buf.toString());
    }
    
    public Textifier visitField(final int a4, final String a5, final String v1, final String v2, final Object v3) {
        /*SL:346*/this.buf.setLength(0);
        /*SL:347*/this.buf.append('\n');
        /*SL:348*/if ((a4 & 0x20000) != 0x0) {
            /*SL:349*/this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        /*SL:351*/this.buf.append(this.tab).append("// access flags 0x").append(/*EL:352*/Integer.toHexString(a4).toUpperCase()).append('\n');
        /*SL:353*/if (v2 != null) {
            /*SL:354*/this.buf.append(this.tab);
            /*SL:355*/this.appendDescriptor(2, v2);
            final TraceSignatureVisitor a6 = /*EL:357*/new TraceSignatureVisitor(0);
            final SignatureReader a7 = /*EL:358*/new SignatureReader(v2);
            /*SL:359*/a7.acceptType(a6);
            /*SL:360*/this.buf.append(this.tab).append("// declaration: ").append(a6.getDeclaration()).append(/*EL:361*/'\n');
        }
        /*SL:364*/this.buf.append(this.tab);
        /*SL:365*/this.appendAccess(a4);
        /*SL:367*/this.appendDescriptor(1, v1);
        /*SL:368*/this.buf.append(' ').append(a5);
        /*SL:369*/if (v3 != null) {
            /*SL:370*/this.buf.append(" = ");
            /*SL:371*/if (v3 instanceof String) {
                /*SL:372*/this.buf.append('\"').append(v3).append('\"');
            }
            else {
                /*SL:374*/this.buf.append(v3);
            }
        }
        /*SL:378*/this.buf.append('\n');
        /*SL:379*/this.text.add(this.buf.toString());
        final Textifier v4 = /*EL:381*/this.createTextifier();
        /*SL:382*/this.text.add(v4.getText());
        /*SL:383*/return v4;
    }
    
    public Textifier visitMethod(final int v-4, final String v-3, final String v-2, final String v-1, final String[] v0) {
        /*SL:389*/this.buf.setLength(0);
        /*SL:390*/this.buf.append('\n');
        /*SL:391*/if ((v-4 & 0x20000) != 0x0) {
            /*SL:392*/this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        /*SL:394*/this.buf.append(this.tab).append("// access flags 0x").append(/*EL:395*/Integer.toHexString(v-4).toUpperCase()).append('\n');
        /*SL:397*/if (v-1 != null) {
            /*SL:398*/this.buf.append(this.tab);
            /*SL:399*/this.appendDescriptor(4, v-1);
            final TraceSignatureVisitor a1 = /*EL:401*/new TraceSignatureVisitor(0);
            final SignatureReader a2 = /*EL:402*/new SignatureReader(v-1);
            /*SL:403*/a2.accept(a1);
            final String a3 = /*EL:404*/a1.getDeclaration();
            final String a4 = /*EL:405*/a1.getReturnType();
            final String a5 = /*EL:406*/a1.getExceptions();
            /*SL:408*/this.buf.append(this.tab).append("// declaration: ").append(a4).append(' ').append(/*EL:409*/v-3).append(a3);
            /*SL:410*/if (a5 != null) {
                /*SL:411*/this.buf.append(" throws ").append(a5);
            }
            /*SL:413*/this.buf.append('\n');
        }
        /*SL:416*/this.buf.append(this.tab);
        /*SL:417*/this.appendAccess(v-4 & 0xFFFFFFBF);
        /*SL:418*/if ((v-4 & 0x100) != 0x0) {
            /*SL:419*/this.buf.append("native ");
        }
        /*SL:421*/if ((v-4 & 0x80) != 0x0) {
            /*SL:422*/this.buf.append("varargs ");
        }
        /*SL:424*/if ((v-4 & 0x40) != 0x0) {
            /*SL:425*/this.buf.append("bridge ");
        }
        /*SL:427*/if ((this.access & 0x200) != 0x0 && (v-4 & 0x400) == 0x0 && (v-4 & 0x8) == 0x0) {
            /*SL:430*/this.buf.append("default ");
        }
        /*SL:433*/this.buf.append(v-3);
        /*SL:434*/this.appendDescriptor(3, v-2);
        /*SL:435*/if (v0 != null && v0.length > 0) {
            /*SL:436*/this.buf.append(" throws ");
            /*SL:437*/for (int v = 0; v < v0.length; ++v) {
                /*SL:438*/this.appendDescriptor(0, v0[v]);
                /*SL:439*/this.buf.append(' ');
            }
        }
        /*SL:443*/this.buf.append('\n');
        /*SL:444*/this.text.add(this.buf.toString());
        final Textifier v2 = /*EL:446*/this.createTextifier();
        /*SL:447*/this.text.add(v2.getText());
        /*SL:448*/return v2;
    }
    
    public void visitClassEnd() {
        /*SL:453*/this.text.add("}\n");
    }
    
    public void visit(final String v-2, final Object v-1) {
        /*SL:462*/this.buf.setLength(0);
        /*SL:463*/this.appendComa(this.valueNumber++);
        /*SL:465*/if (v-2 != null) {
            /*SL:466*/this.buf.append(v-2).append('=');
        }
        /*SL:469*/if (v-1 instanceof String) {
            /*SL:470*/this.visitString((String)v-1);
        }
        else/*SL:471*/ if (v-1 instanceof Type) {
            /*SL:472*/this.visitType((Type)v-1);
        }
        else/*SL:473*/ if (v-1 instanceof Byte) {
            /*SL:474*/this.visitByte((byte)v-1);
        }
        else/*SL:475*/ if (v-1 instanceof Boolean) {
            /*SL:476*/this.visitBoolean((boolean)v-1);
        }
        else/*SL:477*/ if (v-1 instanceof Short) {
            /*SL:478*/this.visitShort((short)v-1);
        }
        else/*SL:479*/ if (v-1 instanceof Character) {
            /*SL:480*/this.visitChar((char)v-1);
        }
        else/*SL:481*/ if (v-1 instanceof Integer) {
            /*SL:482*/this.visitInt((int)v-1);
        }
        else/*SL:483*/ if (v-1 instanceof Float) {
            /*SL:484*/this.visitFloat((float)v-1);
        }
        else/*SL:485*/ if (v-1 instanceof Long) {
            /*SL:486*/this.visitLong((long)v-1);
        }
        else/*SL:487*/ if (v-1 instanceof Double) {
            /*SL:488*/this.visitDouble((double)v-1);
        }
        else/*SL:489*/ if (v-1.getClass().isArray()) {
            /*SL:490*/this.buf.append('{');
            /*SL:491*/if (v-1 instanceof byte[]) {
                byte[] a2;
                int a2;
                /*SL:493*/for (a2 = (byte[])v-1, a2 = 0; a2 < a2.length; ++a2) {
                    /*SL:494*/this.appendComa(a2);
                    /*SL:495*/this.visitByte(a2[a2]);
                }
            }
            else/*SL:497*/ if (v-1 instanceof boolean[]) {
                final boolean[] v0 = /*EL:498*/(boolean[])v-1;
                /*SL:499*/for (int v = 0; v < v0.length; ++v) {
                    /*SL:500*/this.appendComa(v);
                    /*SL:501*/this.visitBoolean(v0[v]);
                }
            }
            else/*SL:503*/ if (v-1 instanceof short[]) {
                final short[] v2 = /*EL:504*/(short[])v-1;
                /*SL:505*/for (int v = 0; v < v2.length; ++v) {
                    /*SL:506*/this.appendComa(v);
                    /*SL:507*/this.visitShort(v2[v]);
                }
            }
            else/*SL:509*/ if (v-1 instanceof char[]) {
                final char[] v3 = /*EL:510*/(char[])v-1;
                /*SL:511*/for (int v = 0; v < v3.length; ++v) {
                    /*SL:512*/this.appendComa(v);
                    /*SL:513*/this.visitChar(v3[v]);
                }
            }
            else/*SL:515*/ if (v-1 instanceof int[]) {
                final int[] v4 = /*EL:516*/(int[])v-1;
                /*SL:517*/for (int v = 0; v < v4.length; ++v) {
                    /*SL:518*/this.appendComa(v);
                    /*SL:519*/this.visitInt(v4[v]);
                }
            }
            else/*SL:521*/ if (v-1 instanceof long[]) {
                final long[] v5 = /*EL:522*/(long[])v-1;
                /*SL:523*/for (int v = 0; v < v5.length; ++v) {
                    /*SL:524*/this.appendComa(v);
                    /*SL:525*/this.visitLong(v5[v]);
                }
            }
            else/*SL:527*/ if (v-1 instanceof float[]) {
                final float[] v6 = /*EL:528*/(float[])v-1;
                /*SL:529*/for (int v = 0; v < v6.length; ++v) {
                    /*SL:530*/this.appendComa(v);
                    /*SL:531*/this.visitFloat(v6[v]);
                }
            }
            else/*SL:533*/ if (v-1 instanceof double[]) {
                final double[] v7 = /*EL:534*/(double[])v-1;
                /*SL:535*/for (int v = 0; v < v7.length; ++v) {
                    /*SL:536*/this.appendComa(v);
                    /*SL:537*/this.visitDouble(v7[v]);
                }
            }
            /*SL:540*/this.buf.append('}');
        }
        /*SL:543*/this.text.add(this.buf.toString());
    }
    
    private void visitInt(final int a1) {
        /*SL:547*/this.buf.append(a1);
    }
    
    private void visitLong(final long a1) {
        /*SL:551*/this.buf.append(a1).append('L');
    }
    
    private void visitFloat(final float a1) {
        /*SL:555*/this.buf.append(a1).append('F');
    }
    
    private void visitDouble(final double a1) {
        /*SL:559*/this.buf.append(a1).append('D');
    }
    
    private void visitChar(final char a1) {
        /*SL:563*/this.buf.append("(char)").append((int)a1);
    }
    
    private void visitShort(final short a1) {
        /*SL:567*/this.buf.append("(short)").append(a1);
    }
    
    private void visitByte(final byte a1) {
        /*SL:571*/this.buf.append("(byte)").append(a1);
    }
    
    private void visitBoolean(final boolean a1) {
        /*SL:575*/this.buf.append(a1);
    }
    
    private void visitString(final String a1) {
        /*SL:579*/Printer.appendString(this.buf, a1);
    }
    
    private void visitType(final Type a1) {
        /*SL:583*/this.buf.append(a1.getClassName()).append(".class");
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:589*/this.buf.setLength(0);
        /*SL:590*/this.appendComa(this.valueNumber++);
        /*SL:591*/if (a1 != null) {
            /*SL:592*/this.buf.append(a1).append('=');
        }
        /*SL:594*/this.appendDescriptor(1, a2);
        /*SL:595*/this.buf.append('.').append(a3);
        /*SL:596*/this.text.add(this.buf.toString());
    }
    
    public Textifier visitAnnotation(final String a1, final String a2) {
        /*SL:601*/this.buf.setLength(0);
        /*SL:602*/this.appendComa(this.valueNumber++);
        /*SL:603*/if (a1 != null) {
            /*SL:604*/this.buf.append(a1).append('=');
        }
        /*SL:606*/this.buf.append('@');
        /*SL:607*/this.appendDescriptor(1, a2);
        /*SL:608*/this.buf.append('(');
        /*SL:609*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:610*/this.createTextifier();
        /*SL:611*/this.text.add(v1.getText());
        /*SL:612*/this.text.add(")");
        /*SL:613*/return v1;
    }
    
    public Textifier visitArray(final String a1) {
        /*SL:618*/this.buf.setLength(0);
        /*SL:619*/this.appendComa(this.valueNumber++);
        /*SL:620*/if (a1 != null) {
            /*SL:621*/this.buf.append(a1).append('=');
        }
        /*SL:623*/this.buf.append('{');
        /*SL:624*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:625*/this.createTextifier();
        /*SL:626*/this.text.add(v1.getText());
        /*SL:627*/this.text.add("}");
        /*SL:628*/return v1;
    }
    
    public void visitAnnotationEnd() {
    }
    
    public Textifier visitFieldAnnotation(final String a1, final boolean a2) {
        /*SL:642*/return this.visitAnnotation(a1, a2);
    }
    
    public Printer visitFieldTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:648*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public void visitFieldAttribute(final Attribute a1) {
        /*SL:653*/this.visitAttribute(a1);
    }
    
    public void visitFieldEnd() {
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:666*/this.buf.setLength(0);
        /*SL:667*/this.buf.append(this.tab2).append("// parameter ");
        /*SL:668*/this.appendAccess(a2);
        /*SL:669*/this.buf.append(' ').append((a1 == null) ? "<no name>" : a1).append('\n');
        /*SL:671*/this.text.add(this.buf.toString());
    }
    
    public Textifier visitAnnotationDefault() {
        /*SL:676*/this.text.add(this.tab2 + "default=");
        final Textifier v1 = /*EL:677*/this.createTextifier();
        /*SL:678*/this.text.add(v1.getText());
        /*SL:679*/this.text.add("\n");
        /*SL:680*/return v1;
    }
    
    public Textifier visitMethodAnnotation(final String a1, final boolean a2) {
        /*SL:686*/return this.visitAnnotation(a1, a2);
    }
    
    public Printer visitMethodTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:692*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public Textifier visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        /*SL:698*/this.buf.setLength(0);
        /*SL:699*/this.buf.append(this.tab2).append('@');
        /*SL:700*/this.appendDescriptor(1, a2);
        /*SL:701*/this.buf.append('(');
        /*SL:702*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:703*/this.createTextifier();
        /*SL:704*/this.text.add(v1.getText());
        /*SL:705*/this.text.add(a3 ? ") // parameter " : ") // invisible, parameter ");
        /*SL:706*/this.text.add(a1);
        /*SL:707*/this.text.add("\n");
        /*SL:708*/return v1;
    }
    
    public void visitMethodAttribute(final Attribute a1) {
        /*SL:713*/this.buf.setLength(0);
        /*SL:714*/this.buf.append(this.tab).append("ATTRIBUTE ");
        /*SL:715*/this.appendDescriptor(-1, a1.type);
        /*SL:717*/if (a1 instanceof Textifiable) {
            /*SL:718*/((Textifiable)a1).textify(this.buf, this.labelNames);
        }
        else {
            /*SL:720*/this.buf.append(" : unknown\n");
        }
        /*SL:723*/this.text.add(this.buf.toString());
    }
    
    public void visitCode() {
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:733*/this.buf.setLength(0);
        /*SL:734*/this.buf.append(this.ltab);
        /*SL:735*/this.buf.append("FRAME ");
        /*SL:736*/switch (a1) {
            case -1:
            case 0: {
                /*SL:739*/this.buf.append("FULL [");
                /*SL:740*/this.appendFrameTypes(a2, a3);
                /*SL:741*/this.buf.append("] [");
                /*SL:742*/this.appendFrameTypes(a4, a5);
                /*SL:743*/this.buf.append(']');
                /*SL:744*/break;
            }
            case 1: {
                /*SL:746*/this.buf.append("APPEND [");
                /*SL:747*/this.appendFrameTypes(a2, a3);
                /*SL:748*/this.buf.append(']');
                /*SL:749*/break;
            }
            case 2: {
                /*SL:751*/this.buf.append("CHOP ").append(a2);
                /*SL:752*/break;
            }
            case 3: {
                /*SL:754*/this.buf.append("SAME");
                /*SL:755*/break;
            }
            case 4: {
                /*SL:757*/this.buf.append("SAME1 ");
                /*SL:758*/this.appendFrameTypes(1, a5);
                break;
            }
        }
        /*SL:761*/this.buf.append('\n');
        /*SL:762*/this.text.add(this.buf.toString());
    }
    
    public void visitInsn(final int a1) {
        /*SL:767*/this.buf.setLength(0);
        /*SL:768*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append('\n');
        /*SL:769*/this.text.add(this.buf.toString());
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:774*/this.buf.setLength(0);
        /*SL:775*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(/*EL:776*/' ').append(/*EL:777*/(a1 == 188) ? Textifier.TYPES[a2] : /*EL:779*/Integer.toString(a2)).append('\n');
        /*SL:780*/this.text.add(this.buf.toString());
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:785*/this.buf.setLength(0);
        /*SL:786*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(' ').append(a2).append('\n');
        /*SL:788*/this.text.add(this.buf.toString());
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:793*/this.buf.setLength(0);
        /*SL:794*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(' ');
        /*SL:795*/this.appendDescriptor(0, a2);
        /*SL:796*/this.buf.append('\n');
        /*SL:797*/this.text.add(this.buf.toString());
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:803*/this.buf.setLength(0);
        /*SL:804*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(' ');
        /*SL:805*/this.appendDescriptor(0, a2);
        /*SL:806*/this.buf.append('.').append(a3).append(" : ");
        /*SL:807*/this.appendDescriptor(1, a4);
        /*SL:808*/this.buf.append('\n');
        /*SL:809*/this.text.add(this.buf.toString());
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:816*/if (this.api >= 327680) {
            /*SL:817*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:818*/return;
        }
        /*SL:820*/this.doVisitMethodInsn(a1, a2, a3, a4, a1 == 185);
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:827*/if (this.api < 327680) {
            /*SL:828*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:829*/return;
        }
        /*SL:831*/this.doVisitMethodInsn(a1, a2, a3, a4, a5);
    }
    
    private void doVisitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:836*/this.buf.setLength(0);
        /*SL:837*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(' ');
        /*SL:838*/this.appendDescriptor(0, a2);
        /*SL:839*/this.buf.append('.').append(a3).append(' ');
        /*SL:840*/this.appendDescriptor(3, a4);
        /*SL:841*/this.buf.append('\n');
        /*SL:842*/this.text.add(this.buf.toString());
    }
    
    public void visitInvokeDynamicInsn(final String v1, final String v2, final Handle v3, final Object... v4) {
        /*SL:848*/this.buf.setLength(0);
        /*SL:849*/this.buf.append(this.tab2).append("INVOKEDYNAMIC").append(' ');
        /*SL:850*/this.buf.append(v1);
        /*SL:851*/this.appendDescriptor(3, v2);
        /*SL:852*/this.buf.append(" [");
        /*SL:853*/this.buf.append('\n');
        /*SL:854*/this.buf.append(this.tab3);
        /*SL:855*/this.appendHandle(v3);
        /*SL:856*/this.buf.append('\n');
        /*SL:857*/this.buf.append(this.tab3).append("// arguments:");
        /*SL:858*/if (v4.length == 0) {
            /*SL:859*/this.buf.append(" none");
        }
        else {
            /*SL:861*/this.buf.append('\n');
            /*SL:862*/for (Type a3 = (Type)0; a3 < v4.length; ++a3) {
                /*SL:863*/this.buf.append(this.tab3);
                final Object a2 = /*EL:864*/v4[a3];
                /*SL:865*/if (a2 instanceof String) {
                    /*SL:866*/Printer.appendString(this.buf, (String)a2);
                }
                else/*SL:867*/ if (a2 instanceof Type) {
                    /*SL:868*/a3 = (Type)a2;
                    /*SL:869*/if (a3.getSort() == 11) {
                        /*SL:870*/this.appendDescriptor(3, a3.getDescriptor());
                    }
                    else {
                        /*SL:872*/this.buf.append(a3.getDescriptor()).append(".class");
                    }
                }
                else/*SL:874*/ if (a2 instanceof Handle) {
                    /*SL:875*/this.appendHandle((Handle)a2);
                }
                else {
                    /*SL:877*/this.buf.append(a2);
                }
                /*SL:879*/this.buf.append(", \n");
            }
            /*SL:881*/this.buf.setLength(this.buf.length() - 3);
        }
        /*SL:883*/this.buf.append('\n');
        /*SL:884*/this.buf.append(this.tab2).append("]\n");
        /*SL:885*/this.text.add(this.buf.toString());
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:890*/this.buf.setLength(0);
        /*SL:891*/this.buf.append(this.tab2).append(Textifier.OPCODES[a1]).append(' ');
        /*SL:892*/this.appendLabel(a2);
        /*SL:893*/this.buf.append('\n');
        /*SL:894*/this.text.add(this.buf.toString());
    }
    
    public void visitLabel(final Label a1) {
        /*SL:899*/this.buf.setLength(0);
        /*SL:900*/this.buf.append(this.ltab);
        /*SL:901*/this.appendLabel(a1);
        /*SL:902*/this.buf.append('\n');
        /*SL:903*/this.text.add(this.buf.toString());
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:908*/this.buf.setLength(0);
        /*SL:909*/this.buf.append(this.tab2).append("LDC ");
        /*SL:910*/if (a1 instanceof String) {
            /*SL:911*/Printer.appendString(this.buf, (String)a1);
        }
        else/*SL:912*/ if (a1 instanceof Type) {
            /*SL:913*/this.buf.append(((Type)a1).getDescriptor()).append(".class");
        }
        else {
            /*SL:915*/this.buf.append(a1);
        }
        /*SL:917*/this.buf.append('\n');
        /*SL:918*/this.text.add(this.buf.toString());
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:923*/this.buf.setLength(0);
        /*SL:924*/this.buf.append(this.tab2).append("IINC ").append(a1).append(' ').append(a2).append(/*EL:925*/'\n');
        /*SL:926*/this.text.add(this.buf.toString());
    }
    
    public void visitTableSwitchInsn(final int a3, final int a4, final Label v1, final Label... v2) {
        /*SL:932*/this.buf.setLength(0);
        /*SL:933*/this.buf.append(this.tab2).append("TABLESWITCH\n");
        /*SL:934*/for (int a5 = 0; a5 < v2.length; ++a5) {
            /*SL:935*/this.buf.append(this.tab3).append(a3 + a5).append(": ");
            /*SL:936*/this.appendLabel(v2[a5]);
            /*SL:937*/this.buf.append('\n');
        }
        /*SL:939*/this.buf.append(this.tab3).append("default: ");
        /*SL:940*/this.appendLabel(v1);
        /*SL:941*/this.buf.append('\n');
        /*SL:942*/this.text.add(this.buf.toString());
    }
    
    public void visitLookupSwitchInsn(final Label a3, final int[] v1, final Label[] v2) {
        /*SL:948*/this.buf.setLength(0);
        /*SL:949*/this.buf.append(this.tab2).append("LOOKUPSWITCH\n");
        /*SL:950*/for (int a4 = 0; a4 < v2.length; ++a4) {
            /*SL:951*/this.buf.append(this.tab3).append(v1[a4]).append(": ");
            /*SL:952*/this.appendLabel(v2[a4]);
            /*SL:953*/this.buf.append('\n');
        }
        /*SL:955*/this.buf.append(this.tab3).append("default: ");
        /*SL:956*/this.appendLabel(a3);
        /*SL:957*/this.buf.append('\n');
        /*SL:958*/this.text.add(this.buf.toString());
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:963*/this.buf.setLength(0);
        /*SL:964*/this.buf.append(this.tab2).append("MULTIANEWARRAY ");
        /*SL:965*/this.appendDescriptor(1, a1);
        /*SL:966*/this.buf.append(' ').append(a2).append('\n');
        /*SL:967*/this.text.add(this.buf.toString());
    }
    
    public Printer visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:973*/return this.visitTypeAnnotation(a1, a2, a3, a4);
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:979*/this.buf.setLength(0);
        /*SL:980*/this.buf.append(this.tab2).append("TRYCATCHBLOCK ");
        /*SL:981*/this.appendLabel(a1);
        /*SL:982*/this.buf.append(' ');
        /*SL:983*/this.appendLabel(a2);
        /*SL:984*/this.buf.append(' ');
        /*SL:985*/this.appendLabel(a3);
        /*SL:986*/this.buf.append(' ');
        /*SL:987*/this.appendDescriptor(0, a4);
        /*SL:988*/this.buf.append('\n');
        /*SL:989*/this.text.add(this.buf.toString());
    }
    
    public Printer visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:995*/this.buf.setLength(0);
        /*SL:996*/this.buf.append(this.tab2).append("TRYCATCHBLOCK @");
        /*SL:997*/this.appendDescriptor(1, a3);
        /*SL:998*/this.buf.append('(');
        /*SL:999*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:1000*/this.createTextifier();
        /*SL:1001*/this.text.add(v1.getText());
        /*SL:1002*/this.buf.setLength(0);
        /*SL:1003*/this.buf.append(") : ");
        /*SL:1004*/this.appendTypeReference(a1);
        /*SL:1005*/this.buf.append(", ").append(a2);
        /*SL:1006*/this.buf.append(a4 ? "\n" : " // invisible\n");
        /*SL:1007*/this.text.add(this.buf.toString());
        /*SL:1008*/return v1;
    }
    
    public void visitLocalVariable(final String a4, final String a5, final String a6, final Label v1, final Label v2, final int v3) {
        /*SL:1015*/this.buf.setLength(0);
        /*SL:1016*/this.buf.append(this.tab2).append("LOCALVARIABLE ").append(a4).append(' ');
        /*SL:1017*/this.appendDescriptor(1, a5);
        /*SL:1018*/this.buf.append(' ');
        /*SL:1019*/this.appendLabel(v1);
        /*SL:1020*/this.buf.append(' ');
        /*SL:1021*/this.appendLabel(v2);
        /*SL:1022*/this.buf.append(' ').append(v3).append('\n');
        /*SL:1024*/if (a6 != null) {
            /*SL:1025*/this.buf.append(this.tab2);
            /*SL:1026*/this.appendDescriptor(2, a6);
            final TraceSignatureVisitor a7 = /*EL:1028*/new TraceSignatureVisitor(0);
            final SignatureReader a8 = /*EL:1029*/new SignatureReader(a6);
            /*SL:1030*/a8.acceptType(a7);
            /*SL:1031*/this.buf.append(this.tab2).append("// declaration: ").append(a7.getDeclaration()).append(/*EL:1032*/'\n');
        }
        /*SL:1034*/this.text.add(this.buf.toString());
    }
    
    public Printer visitLocalVariableAnnotation(final int a3, final TypePath a4, final Label[] a5, final Label[] a6, final int[] a7, final String v1, final boolean v2) {
        /*SL:1041*/this.buf.setLength(0);
        /*SL:1042*/this.buf.append(this.tab2).append("LOCALVARIABLE @");
        /*SL:1043*/this.appendDescriptor(1, v1);
        /*SL:1044*/this.buf.append('(');
        /*SL:1045*/this.text.add(this.buf.toString());
        final Textifier v3 = /*EL:1046*/this.createTextifier();
        /*SL:1047*/this.text.add(v3.getText());
        /*SL:1048*/this.buf.setLength(0);
        /*SL:1049*/this.buf.append(") : ");
        /*SL:1050*/this.appendTypeReference(a3);
        /*SL:1051*/this.buf.append(", ").append(a4);
        /*SL:1052*/for (int a8 = 0; a8 < a5.length; ++a8) {
            /*SL:1053*/this.buf.append(" [ ");
            /*SL:1054*/this.appendLabel(a5[a8]);
            /*SL:1055*/this.buf.append(" - ");
            /*SL:1056*/this.appendLabel(a6[a8]);
            /*SL:1057*/this.buf.append(" - ").append(a7[a8]).append(" ]");
        }
        /*SL:1059*/this.buf.append(v2 ? "\n" : " // invisible\n");
        /*SL:1060*/this.text.add(this.buf.toString());
        /*SL:1061*/return v3;
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:1066*/this.buf.setLength(0);
        /*SL:1067*/this.buf.append(this.tab2).append("LINENUMBER ").append(a1).append(' ');
        /*SL:1068*/this.appendLabel(a2);
        /*SL:1069*/this.buf.append('\n');
        /*SL:1070*/this.text.add(this.buf.toString());
    }
    
    public void visitMaxs(final int a1, final int a2) {
        /*SL:1075*/this.buf.setLength(0);
        /*SL:1076*/this.buf.append(this.tab2).append("MAXSTACK = ").append(a1).append('\n');
        /*SL:1077*/this.text.add(this.buf.toString());
        /*SL:1079*/this.buf.setLength(0);
        /*SL:1080*/this.buf.append(this.tab2).append("MAXLOCALS = ").append(a2).append('\n');
        /*SL:1081*/this.text.add(this.buf.toString());
    }
    
    public void visitMethodEnd() {
    }
    
    public Textifier visitAnnotation(final String a1, final boolean a2) {
        /*SL:1102*/this.buf.setLength(0);
        /*SL:1103*/this.buf.append(this.tab).append('@');
        /*SL:1104*/this.appendDescriptor(1, a1);
        /*SL:1105*/this.buf.append('(');
        /*SL:1106*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:1107*/this.createTextifier();
        /*SL:1108*/this.text.add(v1.getText());
        /*SL:1109*/this.text.add(a2 ? ")\n" : ") // invisible\n");
        /*SL:1110*/return v1;
    }
    
    public Textifier visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:1130*/this.buf.setLength(0);
        /*SL:1131*/this.buf.append(this.tab).append('@');
        /*SL:1132*/this.appendDescriptor(1, a3);
        /*SL:1133*/this.buf.append('(');
        /*SL:1134*/this.text.add(this.buf.toString());
        final Textifier v1 = /*EL:1135*/this.createTextifier();
        /*SL:1136*/this.text.add(v1.getText());
        /*SL:1137*/this.buf.setLength(0);
        /*SL:1138*/this.buf.append(") : ");
        /*SL:1139*/this.appendTypeReference(a1);
        /*SL:1140*/this.buf.append(", ").append(a2);
        /*SL:1141*/this.buf.append(a4 ? "\n" : " // invisible\n");
        /*SL:1142*/this.text.add(this.buf.toString());
        /*SL:1143*/return v1;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:1153*/this.buf.setLength(0);
        /*SL:1154*/this.buf.append(this.tab).append("ATTRIBUTE ");
        /*SL:1155*/this.appendDescriptor(-1, a1.type);
        /*SL:1157*/if (a1 instanceof Textifiable) {
            /*SL:1158*/((Textifiable)a1).textify(this.buf, null);
        }
        else {
            /*SL:1160*/this.buf.append(" : unknown\n");
        }
        /*SL:1163*/this.text.add(this.buf.toString());
    }
    
    protected Textifier createTextifier() {
        /*SL:1176*/return new Textifier();
    }
    
    protected void appendDescriptor(final int a1, final String a2) {
        /*SL:1191*/if (a1 == 5 || a1 == 2 || a1 == 4) {
            /*SL:1193*/if (a2 != null) {
                /*SL:1194*/this.buf.append("// signature ").append(a2).append('\n');
            }
        }
        else {
            /*SL:1197*/this.buf.append(a2);
        }
    }
    
    protected void appendLabel(final Label a1) {
        /*SL:1209*/if (this.labelNames == null) {
            /*SL:1210*/this.labelNames = new HashMap<Label, String>();
        }
        String v1 = /*EL:1212*/this.labelNames.get(a1);
        /*SL:1213*/if (v1 == null) {
            /*SL:1214*/v1 = "L" + this.labelNames.size();
            /*SL:1215*/this.labelNames.put(a1, v1);
        }
        /*SL:1217*/this.buf.append(v1);
    }
    
    protected void appendHandle(final Handle a1) {
        final int v1 = /*EL:1227*/a1.getTag();
        /*SL:1228*/this.buf.append("// handle kind 0x").append(Integer.toHexString(v1)).append(" : ");
        boolean v2 = /*EL:1230*/false;
        /*SL:1231*/switch (v1) {
            case 1: {
                /*SL:1233*/this.buf.append("GETFIELD");
                /*SL:1234*/break;
            }
            case 2: {
                /*SL:1236*/this.buf.append("GETSTATIC");
                /*SL:1237*/break;
            }
            case 3: {
                /*SL:1239*/this.buf.append("PUTFIELD");
                /*SL:1240*/break;
            }
            case 4: {
                /*SL:1242*/this.buf.append("PUTSTATIC");
                /*SL:1243*/break;
            }
            case 9: {
                /*SL:1245*/this.buf.append("INVOKEINTERFACE");
                /*SL:1246*/v2 = true;
                /*SL:1247*/break;
            }
            case 7: {
                /*SL:1249*/this.buf.append("INVOKESPECIAL");
                /*SL:1250*/v2 = true;
                /*SL:1251*/break;
            }
            case 6: {
                /*SL:1253*/this.buf.append("INVOKESTATIC");
                /*SL:1254*/v2 = true;
                /*SL:1255*/break;
            }
            case 5: {
                /*SL:1257*/this.buf.append("INVOKEVIRTUAL");
                /*SL:1258*/v2 = true;
                /*SL:1259*/break;
            }
            case 8: {
                /*SL:1261*/this.buf.append("NEWINVOKESPECIAL");
                /*SL:1262*/v2 = true;
                break;
            }
        }
        /*SL:1265*/this.buf.append('\n');
        /*SL:1266*/this.buf.append(this.tab3);
        /*SL:1267*/this.appendDescriptor(0, a1.getOwner());
        /*SL:1268*/this.buf.append('.');
        /*SL:1269*/this.buf.append(a1.getName());
        /*SL:1270*/if (!v2) {
            /*SL:1271*/this.buf.append('(');
        }
        /*SL:1273*/this.appendDescriptor(9, a1.getDesc());
        /*SL:1274*/if (!v2) {
            /*SL:1275*/this.buf.append(')');
        }
    }
    
    private void appendAccess(final int a1) {
        /*SL:1287*/if ((a1 & 0x1) != 0x0) {
            /*SL:1288*/this.buf.append("public ");
        }
        /*SL:1290*/if ((a1 & 0x2) != 0x0) {
            /*SL:1291*/this.buf.append("private ");
        }
        /*SL:1293*/if ((a1 & 0x4) != 0x0) {
            /*SL:1294*/this.buf.append("protected ");
        }
        /*SL:1296*/if ((a1 & 0x10) != 0x0) {
            /*SL:1297*/this.buf.append("final ");
        }
        /*SL:1299*/if ((a1 & 0x8) != 0x0) {
            /*SL:1300*/this.buf.append("static ");
        }
        /*SL:1302*/if ((a1 & 0x20) != 0x0) {
            /*SL:1303*/this.buf.append("synchronized ");
        }
        /*SL:1305*/if ((a1 & 0x40) != 0x0) {
            /*SL:1306*/this.buf.append("volatile ");
        }
        /*SL:1308*/if ((a1 & 0x80) != 0x0) {
            /*SL:1309*/this.buf.append("transient ");
        }
        /*SL:1311*/if ((a1 & 0x400) != 0x0) {
            /*SL:1312*/this.buf.append("abstract ");
        }
        /*SL:1314*/if ((a1 & 0x800) != 0x0) {
            /*SL:1315*/this.buf.append("strictfp ");
        }
        /*SL:1317*/if ((a1 & 0x1000) != 0x0) {
            /*SL:1318*/this.buf.append("synthetic ");
        }
        /*SL:1320*/if ((a1 & 0x8000) != 0x0) {
            /*SL:1321*/this.buf.append("mandated ");
        }
        /*SL:1323*/if ((a1 & 0x4000) != 0x0) {
            /*SL:1324*/this.buf.append("enum ");
        }
    }
    
    private void appendComa(final int a1) {
        /*SL:1329*/if (a1 != 0) {
            /*SL:1330*/this.buf.append(", ");
        }
    }
    
    private void appendTypeReference(final int a1) {
        final TypeReference v1 = /*EL:1335*/new TypeReference(a1);
        /*SL:1336*/switch (v1.getSort()) {
            case 0: {
                /*SL:1338*/this.buf.append("CLASS_TYPE_PARAMETER ").append(v1.getTypeParameterIndex());
                /*SL:1340*/break;
            }
            case 1: {
                /*SL:1342*/this.buf.append("METHOD_TYPE_PARAMETER ").append(v1.getTypeParameterIndex());
                /*SL:1344*/break;
            }
            case 16: {
                /*SL:1346*/this.buf.append("CLASS_EXTENDS ").append(v1.getSuperTypeIndex());
                /*SL:1347*/break;
            }
            case 17: {
                /*SL:1349*/this.buf.append("CLASS_TYPE_PARAMETER_BOUND ").append(v1.getTypeParameterIndex()).append(/*EL:1350*/", ").append(v1.getTypeParameterBoundIndex());
                /*SL:1352*/break;
            }
            case 18: {
                /*SL:1354*/this.buf.append("METHOD_TYPE_PARAMETER_BOUND ").append(v1.getTypeParameterIndex()).append(/*EL:1355*/", ").append(v1.getTypeParameterBoundIndex());
                /*SL:1357*/break;
            }
            case 19: {
                /*SL:1359*/this.buf.append("FIELD");
                /*SL:1360*/break;
            }
            case 20: {
                /*SL:1362*/this.buf.append("METHOD_RETURN");
                /*SL:1363*/break;
            }
            case 21: {
                /*SL:1365*/this.buf.append("METHOD_RECEIVER");
                /*SL:1366*/break;
            }
            case 22: {
                /*SL:1368*/this.buf.append("METHOD_FORMAL_PARAMETER ").append(v1.getFormalParameterIndex());
                /*SL:1370*/break;
            }
            case 23: {
                /*SL:1372*/this.buf.append("THROWS ").append(v1.getExceptionIndex());
                /*SL:1373*/break;
            }
            case 64: {
                /*SL:1375*/this.buf.append("LOCAL_VARIABLE");
                /*SL:1376*/break;
            }
            case 65: {
                /*SL:1378*/this.buf.append("RESOURCE_VARIABLE");
                /*SL:1379*/break;
            }
            case 66: {
                /*SL:1381*/this.buf.append("EXCEPTION_PARAMETER ").append(v1.getTryCatchBlockIndex());
                /*SL:1383*/break;
            }
            case 67: {
                /*SL:1385*/this.buf.append("INSTANCEOF");
                /*SL:1386*/break;
            }
            case 68: {
                /*SL:1388*/this.buf.append("NEW");
                /*SL:1389*/break;
            }
            case 69: {
                /*SL:1391*/this.buf.append("CONSTRUCTOR_REFERENCE");
                /*SL:1392*/break;
            }
            case 70: {
                /*SL:1394*/this.buf.append("METHOD_REFERENCE");
                /*SL:1395*/break;
            }
            case 71: {
                /*SL:1397*/this.buf.append("CAST ").append(v1.getTypeArgumentIndex());
                /*SL:1398*/break;
            }
            case 72: {
                /*SL:1400*/this.buf.append("CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT ").append(v1.getTypeArgumentIndex());
                /*SL:1402*/break;
            }
            case 73: {
                /*SL:1404*/this.buf.append("METHOD_INVOCATION_TYPE_ARGUMENT ").append(v1.getTypeArgumentIndex());
                /*SL:1406*/break;
            }
            case 74: {
                /*SL:1408*/this.buf.append("CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT ").append(v1.getTypeArgumentIndex());
                /*SL:1410*/break;
            }
            case 75: {
                /*SL:1412*/this.buf.append("METHOD_REFERENCE_TYPE_ARGUMENT ").append(v1.getTypeArgumentIndex());
                break;
            }
        }
    }
    
    private void appendFrameTypes(final int v2, final Object[] v3) {
        /*SL:1419*/for (String a2 = (String)0; a2 < v2; ++a2) {
            /*SL:1420*/if (a2 > 0) {
                /*SL:1421*/this.buf.append(' ');
            }
            /*SL:1423*/if (v3[a2] instanceof String) {
                /*SL:1424*/a2 = (String)v3[a2];
                /*SL:1425*/if (a2.startsWith("[")) {
                    /*SL:1426*/this.appendDescriptor(1, a2);
                }
                else {
                    /*SL:1428*/this.appendDescriptor(0, a2);
                }
            }
            else/*SL:1430*/ if (v3[a2] instanceof Integer) {
                /*SL:1431*/switch ((int)v3[a2]) {
                    case 0: {
                        /*SL:1433*/this.appendDescriptor(1, "T");
                        /*SL:1434*/break;
                    }
                    case 1: {
                        /*SL:1436*/this.appendDescriptor(1, "I");
                        /*SL:1437*/break;
                    }
                    case 2: {
                        /*SL:1439*/this.appendDescriptor(1, "F");
                        /*SL:1440*/break;
                    }
                    case 3: {
                        /*SL:1442*/this.appendDescriptor(1, "D");
                        /*SL:1443*/break;
                    }
                    case 4: {
                        /*SL:1445*/this.appendDescriptor(1, "J");
                        /*SL:1446*/break;
                    }
                    case 5: {
                        /*SL:1448*/this.appendDescriptor(1, "N");
                        /*SL:1449*/break;
                    }
                    case 6: {
                        /*SL:1451*/this.appendDescriptor(1, "U");
                        break;
                    }
                }
            }
            else {
                /*SL:1455*/this.appendLabel((Label)v3[a2]);
            }
        }
    }
}
