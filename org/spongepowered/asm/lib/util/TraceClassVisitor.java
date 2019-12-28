package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import java.io.PrintWriter;
import org.spongepowered.asm.lib.ClassVisitor;

public final class TraceClassVisitor extends ClassVisitor
{
    private final PrintWriter pw;
    public final Printer p;
    
    public TraceClassVisitor(final PrintWriter a1) {
        this(null, a1);
    }
    
    public TraceClassVisitor(final ClassVisitor a1, final PrintWriter a2) {
        this(a1, new Textifier(), a2);
    }
    
    public TraceClassVisitor(final ClassVisitor a1, final Printer a2, final PrintWriter a3) {
        super(327680, a1);
        this.pw = a3;
        this.p = a2;
    }
    
    public void visit(final int a1, final int a2, final String a3, final String a4, final String a5, final String[] a6) {
        /*SL:143*/this.p.visit(a1, a2, a3, a4, a5, a6);
        /*SL:144*/super.visit(a1, a2, a3, a4, a5, a6);
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:149*/this.p.visitSource(a1, a2);
        /*SL:150*/super.visitSource(a1, a2);
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:156*/this.p.visitOuterClass(a1, a2, a3);
        /*SL:157*/super.visitOuterClass(a1, a2, a3);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final Printer v1 = /*EL:163*/this.p.visitClassAnnotation(a1, a2);
        final AnnotationVisitor v2 = /*EL:164*/(this.cv == null) ? null : this.cv.visitAnnotation(a1, a2);
        /*SL:166*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final Printer v1 = /*EL:172*/this.p.visitClassTypeAnnotation(a1, a2, a3, a4);
        final AnnotationVisitor v2 = /*EL:174*/(this.cv == null) ? null : this.cv.visitTypeAnnotation(a1, a2, a3, a4);
        /*SL:176*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:181*/this.p.visitClassAttribute(a1);
        /*SL:182*/super.visitAttribute(a1);
    }
    
    public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        /*SL:188*/this.p.visitInnerClass(a1, a2, a3, a4);
        /*SL:189*/super.visitInnerClass(a1, a2, a3, a4);
    }
    
    public FieldVisitor visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        final Printer v1 = /*EL:195*/this.p.visitField(a1, a2, a3, a4, a5);
        final FieldVisitor v2 = /*EL:196*/(this.cv == null) ? null : this.cv.visitField(a1, a2, a3, a4, a5);
        /*SL:198*/return new TraceFieldVisitor(v2, v1);
    }
    
    public MethodVisitor visitMethod(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
        final Printer v1 = /*EL:204*/this.p.visitMethod(a1, a2, a3, a4, a5);
        final MethodVisitor v2 = /*EL:206*/(this.cv == null) ? null : this.cv.visitMethod(a1, a2, a3, a4, a5);
        /*SL:208*/return new TraceMethodVisitor(v2, v1);
    }
    
    public void visitEnd() {
        /*SL:213*/this.p.visitClassEnd();
        /*SL:214*/if (this.pw != null) {
            /*SL:215*/this.p.print(this.pw);
            /*SL:216*/this.pw.flush();
        }
        /*SL:218*/super.visitEnd();
    }
}
