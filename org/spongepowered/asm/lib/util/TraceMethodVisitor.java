package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.MethodVisitor;

public final class TraceMethodVisitor extends MethodVisitor
{
    public final Printer p;
    
    public TraceMethodVisitor(final Printer a1) {
        this(null, a1);
    }
    
    public TraceMethodVisitor(final MethodVisitor a1, final Printer a2) {
        super(327680, a1);
        this.p = a2;
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:61*/this.p.visitParameter(a1, a2);
        /*SL:62*/super.visitParameter(a1, a2);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final Printer v1 = /*EL:68*/this.p.visitMethodAnnotation(a1, a2);
        final AnnotationVisitor v2 = /*EL:69*/(this.mv == null) ? null : this.mv.visitAnnotation(a1, a2);
        /*SL:71*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final Printer v1 = /*EL:77*/this.p.visitMethodTypeAnnotation(a1, a2, a3, a4);
        final AnnotationVisitor v2 = /*EL:79*/(this.mv == null) ? null : this.mv.visitTypeAnnotation(a1, a2, a3, a4);
        /*SL:81*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:86*/this.p.visitMethodAttribute(a1);
        /*SL:87*/super.visitAttribute(a1);
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        final Printer v1 = /*EL:92*/this.p.visitAnnotationDefault();
        final AnnotationVisitor v2 = /*EL:93*/(this.mv == null) ? null : this.mv.visitAnnotationDefault();
        /*SL:94*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        final Printer v1 = /*EL:100*/this.p.visitParameterAnnotation(a1, a2, a3);
        final AnnotationVisitor v2 = /*EL:101*/(this.mv == null) ? null : this.mv.visitParameterAnnotation(a1, a2, a3);
        /*SL:103*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitCode() {
        /*SL:108*/this.p.visitCode();
        /*SL:109*/super.visitCode();
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:115*/this.p.visitFrame(a1, a2, a3, a4, a5);
        /*SL:116*/super.visitFrame(a1, a2, a3, a4, a5);
    }
    
    public void visitInsn(final int a1) {
        /*SL:121*/this.p.visitInsn(a1);
        /*SL:122*/super.visitInsn(a1);
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:127*/this.p.visitIntInsn(a1, a2);
        /*SL:128*/super.visitIntInsn(a1, a2);
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:133*/this.p.visitVarInsn(a1, a2);
        /*SL:134*/super.visitVarInsn(a1, a2);
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:139*/this.p.visitTypeInsn(a1, a2);
        /*SL:140*/super.visitTypeInsn(a1, a2);
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:146*/this.p.visitFieldInsn(a1, a2, a3, a4);
        /*SL:147*/super.visitFieldInsn(a1, a2, a3, a4);
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:154*/if (this.api >= 327680) {
            /*SL:155*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:156*/return;
        }
        /*SL:158*/this.p.visitMethodInsn(a1, a2, a3, a4);
        /*SL:159*/if (this.mv != null) {
            /*SL:160*/this.mv.visitMethodInsn(a1, a2, a3, a4);
        }
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:167*/if (this.api < 327680) {
            /*SL:168*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:169*/return;
        }
        /*SL:171*/this.p.visitMethodInsn(a1, a2, a3, a4, a5);
        /*SL:172*/if (this.mv != null) {
            /*SL:173*/this.mv.visitMethodInsn(a1, a2, a3, a4, a5);
        }
    }
    
    public void visitInvokeDynamicInsn(final String a1, final String a2, final Handle a3, final Object... a4) {
        /*SL:180*/this.p.visitInvokeDynamicInsn(a1, a2, a3, a4);
        /*SL:181*/super.visitInvokeDynamicInsn(a1, a2, a3, a4);
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:186*/this.p.visitJumpInsn(a1, a2);
        /*SL:187*/super.visitJumpInsn(a1, a2);
    }
    
    public void visitLabel(final Label a1) {
        /*SL:192*/this.p.visitLabel(a1);
        /*SL:193*/super.visitLabel(a1);
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:198*/this.p.visitLdcInsn(a1);
        /*SL:199*/super.visitLdcInsn(a1);
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:204*/this.p.visitIincInsn(a1, a2);
        /*SL:205*/super.visitIincInsn(a1, a2);
    }
    
    public void visitTableSwitchInsn(final int a1, final int a2, final Label a3, final Label... a4) {
        /*SL:211*/this.p.visitTableSwitchInsn(a1, a2, a3, a4);
        /*SL:212*/super.visitTableSwitchInsn(a1, a2, a3, a4);
    }
    
    public void visitLookupSwitchInsn(final Label a1, final int[] a2, final Label[] a3) {
        /*SL:218*/this.p.visitLookupSwitchInsn(a1, a2, a3);
        /*SL:219*/super.visitLookupSwitchInsn(a1, a2, a3);
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:224*/this.p.visitMultiANewArrayInsn(a1, a2);
        /*SL:225*/super.visitMultiANewArrayInsn(a1, a2);
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final Printer v1 = /*EL:231*/this.p.visitInsnAnnotation(a1, a2, a3, a4);
        final AnnotationVisitor v2 = /*EL:233*/(this.mv == null) ? null : this.mv.visitInsnAnnotation(a1, a2, a3, a4);
        /*SL:235*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:241*/this.p.visitTryCatchBlock(a1, a2, a3, a4);
        /*SL:242*/super.visitTryCatchBlock(a1, a2, a3, a4);
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final Printer v1 = /*EL:248*/this.p.visitTryCatchAnnotation(a1, a2, a3, a4);
        final AnnotationVisitor v2 = /*EL:250*/(this.mv == null) ? null : this.mv.visitTryCatchAnnotation(a1, a2, a3, a4);
        /*SL:252*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:259*/this.p.visitLocalVariable(a1, a2, a3, a4, a5, a6);
        /*SL:260*/super.visitLocalVariable(a1, a2, a3, a4, a5, a6);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a1, final TypePath a2, final Label[] a3, final Label[] a4, final int[] a5, final String a6, final boolean a7) {
        final Printer v1 = /*EL:267*/this.p.visitLocalVariableAnnotation(a1, a2, a3, a4, a5, a6, a7);
        final AnnotationVisitor v2 = /*EL:269*/(this.mv == null) ? null : this.mv.visitLocalVariableAnnotation(a1, a2, a3, a4, a5, a6, a7);
        /*SL:272*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:277*/this.p.visitLineNumber(a1, a2);
        /*SL:278*/super.visitLineNumber(a1, a2);
    }
    
    public void visitMaxs(final int a1, final int a2) {
        /*SL:283*/this.p.visitMaxs(a1, a2);
        /*SL:284*/super.visitMaxs(a1, a2);
    }
    
    public void visitEnd() {
        /*SL:289*/this.p.visitMethodEnd();
        /*SL:290*/super.visitEnd();
    }
}
