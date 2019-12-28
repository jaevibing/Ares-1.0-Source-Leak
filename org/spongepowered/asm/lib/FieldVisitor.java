package org.spongepowered.asm.lib;

public abstract class FieldVisitor
{
    protected final int api;
    protected FieldVisitor fv;
    
    public FieldVisitor(final int a1) {
        this(a1, null);
    }
    
    public FieldVisitor(final int a1, final FieldVisitor a2) {
        if (a1 != 262144 && a1 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = a1;
        this.fv = a2;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:93*/if (this.fv != null) {
            /*SL:94*/return this.fv.visitAnnotation(a1, a2);
        }
        /*SL:96*/return null;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:119*/if (this.api < 327680) {
            /*SL:120*/throw new RuntimeException();
        }
        /*SL:122*/if (this.fv != null) {
            /*SL:123*/return this.fv.visitTypeAnnotation(a1, a2, a3, a4);
        }
        /*SL:125*/return null;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:135*/if (this.fv != null) {
            /*SL:136*/this.fv.visitAttribute(a1);
        }
    }
    
    public void visitEnd() {
        /*SL:146*/if (this.fv != null) {
            /*SL:147*/this.fv.visitEnd();
        }
    }
}
