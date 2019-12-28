package org.spongepowered.asm.lib;

public abstract class AnnotationVisitor
{
    protected final int api;
    protected AnnotationVisitor av;
    
    public AnnotationVisitor(final int a1) {
        this(a1, null);
    }
    
    public AnnotationVisitor(final int a1, final AnnotationVisitor a2) {
        if (a1 != 262144 && a1 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = a1;
        this.av = a2;
    }
    
    public void visit(final String a1, final Object a2) {
        /*SL:99*/if (this.av != null) {
            /*SL:100*/this.av.visit(a1, a2);
        }
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:115*/if (this.av != null) {
            /*SL:116*/this.av.visitEnum(a1, a2, a3);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        /*SL:134*/if (this.av != null) {
            /*SL:135*/return this.av.visitAnnotation(a1, a2);
        }
        /*SL:137*/return null;
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        /*SL:155*/if (this.av != null) {
            /*SL:156*/return this.av.visitArray(a1);
        }
        /*SL:158*/return null;
    }
    
    public void visitEnd() {
        /*SL:165*/if (this.av != null) {
            /*SL:166*/this.av.visitEnd();
        }
    }
}
