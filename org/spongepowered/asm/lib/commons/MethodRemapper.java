package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodRemapper extends MethodVisitor
{
    protected final Remapper remapper;
    
    public MethodRemapper(final MethodVisitor a1, final Remapper a2) {
        this(327680, a1, a2);
    }
    
    protected MethodRemapper(final int a1, final MethodVisitor a2, final Remapper a3) {
        super(a1, a2);
        this.remapper = a3;
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        final AnnotationVisitor v1 = /*EL:61*/super.visitAnnotationDefault();
        /*SL:62*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final AnnotationVisitor v1 = /*EL:67*/super.visitAnnotation(this.remapper.mapDesc(a1), a2);
        /*SL:69*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final AnnotationVisitor v1 = /*EL:75*/super.visitTypeAnnotation(a1, a2, this.remapper.mapDesc(a3), /*EL:76*/a4);
        /*SL:77*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        final AnnotationVisitor v1 = /*EL:83*/super.visitParameterAnnotation(a1, this.remapper.mapDesc(a2), /*EL:84*/a3);
        /*SL:85*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:91*/super.visitFrame(a1, a2, this.remapEntries(a2, a3), a4, this.remapEntries(a4, a5));
    }
    
    private Object[] remapEntries(final int v-1, final Object[] v0) {
        /*SL:96*/for (int v = 0; v < v-1; ++v) {
            /*SL:97*/if (v0[v] instanceof String) {
                Object[] a2 = /*EL:98*/new Object[v-1];
                /*SL:99*/if (v > 0) {
                    /*SL:100*/System.arraycopy(v0, 0, a2, 0, v);
                }
                /*SL:106*/do {
                    a2 = v0[v];
                    a2[v++] = ((a2 instanceof String) ? this.remapper.mapType((String)a2) : a2);
                } while (v < v-1);
                /*SL:107*/return a2;
            }
        }
        /*SL:110*/return v0;
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:116*/super.visitFieldInsn(a1, this.remapper.mapType(a2), this.remapper.mapFieldName(a2, a3, a4), /*EL:117*/this.remapper.mapDesc(a4));
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:125*/if (this.api >= 327680) {
            /*SL:126*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:127*/return;
        }
        /*SL:129*/this.doVisitMethodInsn(a1, a2, a3, a4, a1 == 185);
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:136*/if (this.api < 327680) {
            /*SL:137*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:138*/return;
        }
        /*SL:140*/this.doVisitMethodInsn(a1, a2, a3, a4, a5);
    }
    
    private void doVisitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:151*/if (this.mv != null) {
            /*SL:152*/this.mv.visitMethodInsn(a1, this.remapper.mapType(a2), this.remapper.mapMethodName(a2, a3, a4), /*EL:153*/this.remapper.mapMethodDesc(a4), /*EL:154*/a5);
        }
    }
    
    public void visitInvokeDynamicInsn(final String a3, final String a4, final Handle v1, final Object... v2) {
        /*SL:161*/for (int a5 = 0; a5 < v2.length; ++a5) {
            /*SL:162*/v2[a5] = this.remapper.mapValue(v2[a5]);
        }
        /*SL:164*/super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(a3, a4), /*EL:165*/this.remapper.mapMethodDesc(a4), /*EL:166*/(Handle)this.remapper.mapValue(v1), v2);
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:172*/super.visitTypeInsn(a1, this.remapper.mapType(a2));
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:177*/super.visitLdcInsn(this.remapper.mapValue(a1));
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:182*/super.visitMultiANewArrayInsn(this.remapper.mapDesc(a1), a2);
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final AnnotationVisitor v1 = /*EL:188*/super.visitInsnAnnotation(a1, a2, this.remapper.mapDesc(a3), /*EL:189*/a4);
        /*SL:190*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:196*/super.visitTryCatchBlock(a1, a2, a3, (a4 == null) ? null : this.remapper.mapType(a4));
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final AnnotationVisitor v1 = /*EL:203*/super.visitTryCatchAnnotation(a1, a2, this.remapper.mapDesc(a3), /*EL:204*/a4);
        /*SL:205*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:211*/super.visitLocalVariable(a1, this.remapper.mapDesc(a2), this.remapper.mapSignature(a3, true), /*EL:212*/a4, a5, a6);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a1, final TypePath a2, final Label[] a3, final Label[] a4, final int[] a5, final String a6, final boolean a7) {
        final AnnotationVisitor v1 = /*EL:219*/super.visitLocalVariableAnnotation(a1, a2, a3, a4, a5, this.remapper.mapDesc(a6), /*EL:220*/a7);
        /*SL:221*/return (v1 == null) ? v1 : new AnnotationRemapper(v1, this.remapper);
    }
}
