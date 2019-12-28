package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.AnnotationVisitor;

public final class TraceAnnotationVisitor extends AnnotationVisitor
{
    private final Printer p;
    
    public TraceAnnotationVisitor(final Printer a1) {
        this(null, a1);
    }
    
    public TraceAnnotationVisitor(final AnnotationVisitor a1, final Printer a2) {
        super(327680, a1);
        this.p = a2;
    }
    
    public void visit(final String a1, final Object a2) {
        /*SL:56*/this.p.visit(a1, a2);
        /*SL:57*/super.visit(a1, a2);
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:63*/this.p.visitEnum(a1, a2, a3);
        /*SL:64*/super.visitEnum(a1, a2, a3);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        final Printer v1 = /*EL:70*/this.p.visitAnnotation(a1, a2);
        final AnnotationVisitor v2 = /*EL:71*/(this.av == null) ? null : this.av.visitAnnotation(a1, a2);
        /*SL:73*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        final Printer v1 = /*EL:78*/this.p.visitArray(a1);
        final AnnotationVisitor v2 = /*EL:79*/(this.av == null) ? null : this.av.visitArray(a1);
        /*SL:81*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitEnd() {
        /*SL:86*/this.p.visitAnnotationEnd();
        /*SL:87*/super.visitEnd();
    }
}
