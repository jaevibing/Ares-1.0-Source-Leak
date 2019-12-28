package org.spongepowered.asm.lib.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import java.util.HashMap;
import org.spongepowered.asm.lib.tree.analysis.Frame;
import org.spongepowered.asm.lib.tree.TryCatchBlockNode;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.lib.tree.analysis.Interpreter;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.spongepowered.asm.lib.tree.analysis.SimpleVerifier;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.ArrayList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import org.spongepowered.asm.lib.ClassReader;
import java.io.FileInputStream;
import org.spongepowered.asm.lib.Label;
import java.util.Map;
import org.spongepowered.asm.lib.ClassVisitor;

public class CheckClassAdapter extends ClassVisitor
{
    private int version;
    private boolean start;
    private boolean source;
    private boolean outer;
    private boolean end;
    private Map<Label, Integer> labels;
    private boolean checkDataFlow;
    
    public static void main(final String[] v1) throws Exception {
        /*SL:177*/if (v1.length != 1) {
            System.err.println(/*EL:178*/"Verifies the given class.");
            System.err.println(/*EL:179*/"Usage: CheckClassAdapter <fully qualified class name or class file name>");
            /*SL:181*/return;
        }
        final ClassReader v2;
        /*SL:184*/if (v1[0].endsWith(".class")) {
            final ClassReader a1 = /*EL:185*/new ClassReader(new FileInputStream(v1[0]));
        }
        else {
            /*SL:187*/v2 = new ClassReader(v1[0]);
        }
        verify(/*EL:190*/v2, false, new PrintWriter(System.err));
    }
    
    public static void verify(final ClassReader v-10, final ClassLoader v-9, final boolean v-8, final PrintWriter v-7) {
        final ClassNode a5 = /*EL:211*/new ClassNode();
        /*SL:212*/v-10.accept(new CheckClassAdapter(a5, false), 2);
        final Type a6 = /*EL:214*/(a5.superName == null) ? null : /*EL:215*/Type.getObjectType(a5.superName);
        final List<MethodNode> methods = /*EL:216*/a5.methods;
        final List<Type> a7 = /*EL:218*/new ArrayList<Type>();
        final Iterator<String> a1 = /*EL:219*/a5.interfaces.iterator();
        while (a1.hasNext()) {
            /*SL:220*/a7.add(Type.getObjectType(a1.next()));
        }
        /*SL:223*/for (int i = 0; i < methods.size(); ++i) {
            final MethodNode a2 = /*EL:224*/methods.get(i);
            final SimpleVerifier a3 = /*EL:225*/new SimpleVerifier(/*EL:226*/Type.getObjectType(a5.name), a6, a7, (a5.access & 0x200) != 0x0);
            final Analyzer<BasicValue> v1 = /*EL:228*/new Analyzer<BasicValue>(a3);
            /*SL:229*/if (v-9 != null) {
                /*SL:230*/a3.setClassLoader(v-9);
            }
            try {
                /*SL:233*/v1.analyze(a5.name, a2);
                /*SL:234*/if (!v-8) {
                    /*SL:235*/continue;
                }
            }
            catch (Exception a4) {
                /*SL:238*/a4.printStackTrace(v-7);
            }
            printAnalyzerResult(/*EL:240*/a2, v1, v-7);
        }
        /*SL:242*/v-7.flush();
    }
    
    public static void verify(final ClassReader a1, final boolean a2, final PrintWriter a3) {
        verify(/*EL:259*/a1, null, a2, a3);
    }
    
    static void printAnalyzerResult(final MethodNode v-7, final Analyzer<BasicValue> v-6, final PrintWriter v-5) {
        final Frame<BasicValue>[] frames = /*EL:264*/v-6.getFrames();
        final Textifier a4 = /*EL:265*/new Textifier();
        final TraceMethodVisitor v-8 = /*EL:266*/new TraceMethodVisitor(a4);
        /*SL:268*/v-5.println(v-7.name + v-7.desc);
        /*SL:269*/for (int i = 0; i < v-7.instructions.size(); ++i) {
            /*SL:270*/v-7.instructions.get(i).accept(v-8);
            StringBuilder a3 = /*EL:272*/new StringBuilder();
            final Frame<BasicValue> v1 = /*EL:273*/frames[i];
            /*SL:274*/if (v1 == null) {
                /*SL:275*/a3.append('?');
            }
            else {
                /*SL:277*/for (int a2 = 0; a2 < v1.getLocals(); ++a2) {
                    /*SL:278*/a3.append(getShortName(((BasicValue)v1.getLocal(a2)).toString())).append(' ');
                }
                /*SL:281*/a3.append(" : ");
                /*SL:282*/for (a3 = 0; a3 < v1.getStackSize(); ++a3) {
                    /*SL:283*/a3.append(getShortName(((BasicValue)v1.getStack(a3)).toString())).append(' ');
                }
            }
            /*SL:287*/while (a3.length() < v-7.maxStack + v-7.maxLocals + 1) {
                /*SL:288*/a3.append(' ');
            }
            /*SL:290*/v-5.print(Integer.toString(i + 100000).substring(1));
            /*SL:291*/v-5.print(" " + (Object)a3 + " : " + a4.text.get(a4.text.size() - 1));
        }
        /*SL:293*/for (int i = 0; i < v-7.tryCatchBlocks.size(); ++i) {
            /*SL:294*/v-7.tryCatchBlocks.get(i).accept(v-8);
            /*SL:295*/v-5.print(" " + a4.text.get(a4.text.size() - 1));
        }
        /*SL:297*/v-5.println();
    }
    
    private static String getShortName(final String a1) {
        final int v1 = /*EL:301*/a1.lastIndexOf(47);
        int v2 = /*EL:302*/a1.length();
        /*SL:303*/if (a1.charAt(v2 - 1) == ';') {
            /*SL:304*/--v2;
        }
        /*SL:306*/return (v1 == -1) ? a1 : a1.substring(v1 + 1, v2);
    }
    
    public CheckClassAdapter(final ClassVisitor a1) {
        this(a1, true);
    }
    
    public CheckClassAdapter(final ClassVisitor a1, final boolean a2) {
        this(327680, a1, a2);
        if (this.getClass() != CheckClassAdapter.class) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckClassAdapter(final int a1, final ClassVisitor a2, final boolean a3) {
        super(a1, a2);
        this.labels = new HashMap<Label, Integer>();
        this.checkDataFlow = a3;
    }
    
    public void visit(final int a3, final int a4, final String a5, final String a6, final String v1, final String[] v2) {
        /*SL:372*/if (this.start) {
            /*SL:373*/throw new IllegalStateException("visit must be called only once");
        }
        /*SL:375*/this.start = true;
        /*SL:376*/this.checkState();
        checkAccess(/*EL:377*/a4, 423473);
        /*SL:382*/if (a5 == null || !a5.endsWith("package-info")) {
            /*SL:383*/CheckMethodAdapter.checkInternalName(a5, "class name");
        }
        /*SL:385*/if ("java/lang/Object".equals(a5)) {
            /*SL:386*/if (v1 != null) {
                /*SL:387*/throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        }
        else {
            /*SL:391*/CheckMethodAdapter.checkInternalName(v1, "super class name");
        }
        /*SL:393*/if (a6 != null) {
            checkClassSignature(/*EL:394*/a6);
        }
        /*SL:396*/if ((a4 & 0x200) != 0x0 && /*EL:397*/!"java/lang/Object".equals(v1)) {
            /*SL:398*/throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
        }
        /*SL:402*/if (v2 != null) {
            /*SL:403*/for (int a7 = 0; a7 < v2.length; ++a7) {
                /*SL:404*/CheckMethodAdapter.checkInternalName(v2[a7], "interface name at index " + a7);
            }
        }
        /*SL:409*/super.visit(this.version = a3, a4, a5, a6, v1, v2);
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:414*/this.checkState();
        /*SL:415*/if (this.source) {
            /*SL:416*/throw new IllegalStateException("visitSource can be called only once.");
        }
        /*SL:419*/this.source = true;
        /*SL:420*/super.visitSource(a1, a2);
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:426*/this.checkState();
        /*SL:427*/if (this.outer) {
            /*SL:428*/throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        /*SL:431*/this.outer = true;
        /*SL:432*/if (a1 == null) {
            /*SL:433*/throw new IllegalArgumentException("Illegal outer class owner");
        }
        /*SL:435*/if (a3 != null) {
            /*SL:436*/CheckMethodAdapter.checkMethodDesc(a3);
        }
        /*SL:438*/super.visitOuterClass(a1, a2, a3);
    }
    
    public void visitInnerClass(final String a3, final String a4, final String v1, final int v2) {
        /*SL:444*/this.checkState();
        /*SL:445*/CheckMethodAdapter.checkInternalName(a3, "class name");
        /*SL:446*/if (a4 != null) {
            /*SL:447*/CheckMethodAdapter.checkInternalName(a4, "outer class name");
        }
        /*SL:449*/if (v1 != null) {
            int a5;
            /*SL:451*/for (a5 = 0; a5 < v1.length() && /*EL:452*/Character.isDigit(v1.charAt(a5)); /*SL:453*/++a5) {}
            /*SL:455*/if (a5 == 0 || a5 < v1.length()) {
                /*SL:456*/CheckMethodAdapter.checkIdentifier(v1, a5, -1, "inner class name");
            }
        }
        checkAccess(/*EL:460*/v2, 30239);
        /*SL:465*/super.visitInnerClass(a3, a4, v1, v2);
    }
    
    public FieldVisitor visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        /*SL:471*/this.checkState();
        checkAccess(/*EL:472*/a1, 413919);
        /*SL:477*/CheckMethodAdapter.checkUnqualifiedName(this.version, a2, "field name");
        /*SL:478*/CheckMethodAdapter.checkDesc(a3, false);
        /*SL:479*/if (a4 != null) {
            checkFieldSignature(/*EL:480*/a4);
        }
        /*SL:482*/if (a5 != null) {
            /*SL:483*/CheckMethodAdapter.checkConstant(a5);
        }
        final FieldVisitor v1 = /*EL:486*/super.visitField(a1, a2, a3, a4, a5);
        /*SL:487*/return new CheckFieldAdapter(v1);
    }
    
    public MethodVisitor visitMethod(final int a4, final String a5, final String v1, final String v2, final String[] v3) {
        /*SL:493*/this.checkState();
        checkAccess(/*EL:494*/a4, 400895);
        /*SL:500*/if (!"<init>".equals(a5) && !"<clinit>".equals(a5)) {
            /*SL:501*/CheckMethodAdapter.checkMethodIdentifier(this.version, a5, "method name");
        }
        /*SL:504*/CheckMethodAdapter.checkMethodDesc(v1);
        /*SL:505*/if (v2 != null) {
            checkMethodSignature(/*EL:506*/v2);
        }
        /*SL:508*/if (v3 != null) {
            /*SL:509*/for (int a6 = 0; a6 < v3.length; ++a6) {
                /*SL:510*/CheckMethodAdapter.checkInternalName(v3[a6], "exception name at index " + a6);
            }
        }
        final CheckMethodAdapter v4;
        /*SL:515*/if (this.checkDataFlow) {
            final CheckMethodAdapter a7 = /*EL:516*/new CheckMethodAdapter(a4, a5, v1, super.visitMethod(a4, a5, v1, v2, v3), this.labels);
        }
        else {
            /*SL:519*/v4 = new CheckMethodAdapter(super.visitMethod(a4, a5, v1, v2, v3), this.labels);
        }
        /*SL:522*/v4.version = this.version;
        /*SL:523*/return v4;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:529*/this.checkState();
        /*SL:530*/CheckMethodAdapter.checkDesc(a1, false);
        /*SL:531*/return new CheckAnnotationAdapter(super.visitAnnotation(a1, a2));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:537*/this.checkState();
        final int v1 = /*EL:538*/a1 >>> 24;
        /*SL:539*/if (v1 != 0 && v1 != 17 && v1 != 16) {
            /*SL:542*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:543*/Integer.toHexString(v1));
        }
        checkTypeRefAndPath(/*EL:545*/a1, a2);
        /*SL:546*/CheckMethodAdapter.checkDesc(a3, false);
        /*SL:547*/return new CheckAnnotationAdapter(super.visitTypeAnnotation(a1, a2, a3, a4));
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:553*/this.checkState();
        /*SL:554*/if (a1 == null) {
            /*SL:555*/throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        /*SL:558*/super.visitAttribute(a1);
    }
    
    public void visitEnd() {
        /*SL:563*/this.checkState();
        /*SL:564*/this.end = true;
        /*SL:565*/super.visitEnd();
    }
    
    private void checkState() {
        /*SL:577*/if (!this.start) {
            /*SL:578*/throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        /*SL:581*/if (this.end) {
            /*SL:582*/throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }
    
    static void checkAccess(final int a1, final int a2) {
        /*SL:598*/if ((a1 & ~a2) != 0x0) {
            /*SL:599*/throw new IllegalArgumentException("Invalid access flags: " + a1);
        }
        final int v1 = /*EL:602*/((a1 & 0x1) != 0x0) ? 1 : 0;
        final int v2 = /*EL:603*/((a1 & 0x2) != 0x0) ? 1 : 0;
        final int v3 = /*EL:604*/((a1 & 0x4) != 0x0) ? 1 : 0;
        /*SL:605*/if (v1 + v2 + v3 > 1) {
            /*SL:606*/throw new IllegalArgumentException("public private and protected are mutually exclusive: " + a1);
        }
        final int v4 = /*EL:610*/((a1 & 0x10) != 0x0) ? 1 : 0;
        final int v5 = /*EL:611*/((a1 & 0x400) != 0x0) ? 1 : 0;
        /*SL:612*/if (v4 + v5 > 1) {
            /*SL:613*/throw new IllegalArgumentException("final and abstract are mutually exclusive: " + a1);
        }
    }
    
    public static void checkClassSignature(final String a1) {
        int v1 = /*EL:628*/0;
        /*SL:629*/if (getChar(a1, 0) == '<') {
            /*SL:630*/v1 = checkFormalTypeParameters(a1, v1);
        }
        /*SL:633*/for (v1 = checkClassTypeSignature(a1, v1); getChar(a1, v1) == 'L'; /*SL:634*/v1 = checkClassTypeSignature(a1, v1)) {}
        /*SL:636*/if (v1 != a1.length()) {
            /*SL:637*/throw new IllegalArgumentException(a1 + ": error at index " + v1);
        }
    }
    
    public static void checkMethodSignature(final String a1) {
        int v1 = /*EL:653*/0;
        /*SL:654*/if (getChar(a1, 0) == '<') {
            /*SL:655*/v1 = checkFormalTypeParameters(a1, v1);
        }
        /*SL:658*/for (v1 = checkChar('(', a1, v1); "ZCBSIFJDL[T".indexOf(getChar(a1, v1)) != -1; /*SL:659*/v1 = checkTypeSignature(a1, v1)) {}
        /*SL:661*/v1 = checkChar(')', a1, v1);
        /*SL:662*/if (getChar(a1, v1) == 'V') {
            /*SL:663*/++v1;
        }
        else {
            /*SL:665*/v1 = checkTypeSignature(a1, v1);
        }
        /*SL:667*/while (getChar(a1, v1) == '^') {
            /*SL:668*/++v1;
            /*SL:669*/if (getChar(a1, v1) == 'L') {
                /*SL:670*/v1 = checkClassTypeSignature(a1, v1);
            }
            else {
                /*SL:672*/v1 = checkTypeVariableSignature(a1, v1);
            }
        }
        /*SL:675*/if (v1 != a1.length()) {
            /*SL:676*/throw new IllegalArgumentException(a1 + ": error at index " + v1);
        }
    }
    
    public static void checkFieldSignature(final String a1) {
        final int v1 = checkFieldTypeSignature(/*EL:688*/a1, 0);
        /*SL:689*/if (v1 != a1.length()) {
            /*SL:690*/throw new IllegalArgumentException(a1 + ": error at index " + v1);
        }
    }
    
    static void checkTypeRefAndPath(final int v1, final TypePath v2) {
        int v3 = /*EL:706*/0;
        /*SL:707*/switch (v1 >>> 24) {
            case 0:
            case 1:
            case 22: {
                /*SL:711*/v3 = -65536;
                /*SL:712*/break;
            }
            case 19:
            case 20:
            case 21:
            case 64:
            case 65:
            case 67:
            case 68:
            case 69:
            case 70: {
                /*SL:722*/v3 = -16777216;
                /*SL:723*/break;
            }
            case 16:
            case 17:
            case 18:
            case 23:
            case 66: {
                /*SL:729*/v3 = -256;
                /*SL:730*/break;
            }
            case 71:
            case 72:
            case 73:
            case 74:
            case 75: {
                /*SL:736*/v3 = -16776961;
                /*SL:737*/break;
            }
            default: {
                /*SL:739*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:740*/Integer.toHexString(v1 >>> 24));
            }
        }
        /*SL:742*/if ((v1 & ~v3) != 0x0) {
            /*SL:743*/throw new IllegalArgumentException("Invalid type reference 0x" + /*EL:744*/Integer.toHexString(v1));
        }
        /*SL:746*/if (v2 != null) {
            /*SL:747*/for (int a2 = 0; a2 < v2.getLength(); ++a2) {
                /*SL:748*/a2 = v2.getStep(a2);
                /*SL:749*/if (a2 != 0 && a2 != 1 && a2 != 3 && a2 != 2) {
                    /*SL:753*/throw new IllegalArgumentException("Invalid type path step " + a2 + " in " + v2);
                }
                /*SL:756*/if (a2 != 3 && v2.getStepArgument(a2) != /*EL:757*/0) {
                    /*SL:758*/throw new IllegalArgumentException("Invalid type path step argument for step " + a2 + " in " + v2);
                }
            }
        }
    }
    
    private static int checkFormalTypeParameters(final String a1, int a2) {
        /*SL:781*/for (a2 = checkChar('<', a1, a2), a2 = checkFormalTypeParameter(a1, a2); getChar(a1, a2) != '>'; /*SL:782*/a2 = checkFormalTypeParameter(a1, a2)) {}
        /*SL:784*/return a2 + 1;
    }
    
    private static int checkFormalTypeParameter(final String a1, int a2) {
        /*SL:800*/a2 = checkIdentifier(a1, a2);
        /*SL:801*/a2 = checkChar(':', a1, a2);
        /*SL:802*/if ("L[T".indexOf(getChar(a1, a2)) != -1) {
            /*SL:803*/a2 = checkFieldTypeSignature(a1, a2);
        }
        /*SL:805*/while (getChar(a1, a2) == ':') {
            /*SL:806*/a2 = checkFieldTypeSignature(a1, a2 + 1);
        }
        /*SL:808*/return a2;
    }
    
    private static int checkFieldTypeSignature(final String a1, final int a2) {
        switch (getChar(/*EL:827*/a1, a2)) {
            case 'L': {
                /*SL:829*/return checkClassTypeSignature(a1, a2);
            }
            case '[': {
                /*SL:831*/return checkTypeSignature(a1, a2 + 1);
            }
            default: {
                /*SL:833*/return checkTypeVariableSignature(a1, a2);
            }
        }
    }
    
    private static int checkClassTypeSignature(final String a1, int a2) {
        /*SL:853*/for (a2 = checkChar('L', a1, a2), a2 = checkIdentifier(a1, a2); getChar(a1, a2) == '/'; /*SL:854*/a2 = checkIdentifier(a1, a2 + 1)) {}
        /*SL:856*/if (getChar(a1, a2) == '<') {
            /*SL:857*/a2 = checkTypeArguments(a1, a2);
        }
        /*SL:859*/while (getChar(a1, a2) == '.') {
            /*SL:860*/a2 = checkIdentifier(a1, a2 + 1);
            /*SL:861*/if (getChar(a1, a2) == '<') {
                /*SL:862*/a2 = checkTypeArguments(a1, a2);
            }
        }
        /*SL:865*/return checkChar(';', a1, a2);
    }
    
    private static int checkTypeArguments(final String a1, int a2) {
        /*SL:883*/for (a2 = checkChar('<', a1, a2), a2 = checkTypeArgument(a1, a2); getChar(a1, a2) != '>'; /*SL:884*/a2 = checkTypeArgument(a1, a2)) {}
        /*SL:886*/return a2 + 1;
    }
    
    private static int checkTypeArgument(final String a1, int a2) {
        final char v1 = getChar(/*EL:902*/a1, a2);
        /*SL:903*/if (v1 == '*') {
            /*SL:904*/return a2 + 1;
        }
        /*SL:905*/if (v1 == '+' || v1 == '-') {
            /*SL:906*/++a2;
        }
        /*SL:908*/return checkFieldTypeSignature(a1, a2);
    }
    
    private static int checkTypeVariableSignature(final String a1, int a2) {
        /*SL:925*/a2 = checkChar('T', a1, a2);
        /*SL:926*/a2 = checkIdentifier(a1, a2);
        /*SL:927*/return checkChar(';', a1, a2);
    }
    
    private static int checkTypeSignature(final String a1, final int a2) {
        switch (getChar(/*EL:943*/a1, a2)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z': {
                /*SL:952*/return a2 + 1;
            }
            default: {
                /*SL:954*/return checkFieldTypeSignature(a1, a2);
            }
        }
    }
    
    private static int checkIdentifier(final String a1, int a2) {
        /*SL:968*/if (!Character.isJavaIdentifierStart(getChar(a1, a2))) {
            /*SL:969*/throw new IllegalArgumentException(a1 + ": identifier expected at index " + a2);
        }
        /*SL:972*/++a2;
        /*SL:973*/while (Character.isJavaIdentifierPart(getChar(a1, a2))) {
            /*SL:974*/++a2;
        }
        /*SL:976*/return a2;
    }
    
    private static int checkChar(final char a1, final String a2, final int a3) {
        /*SL:989*/if (getChar(a2, a3) == a1) {
            /*SL:990*/return a3 + 1;
        }
        /*SL:992*/throw new IllegalArgumentException(a2 + ": '" + a1 + "' expected at index " + a3);
    }
    
    private static char getChar(final String a1, final int a2) {
        /*SL:1007*/return (a2 < a1.length()) ? a1.charAt(a2) : '\0';
    }
}
