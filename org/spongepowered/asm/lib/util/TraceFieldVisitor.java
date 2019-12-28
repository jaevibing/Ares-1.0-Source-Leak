package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;

public final class TraceFieldVisitor extends FieldVisitor
{
    public final Printer p;
    
    public TraceFieldVisitor(final Printer a1) {
        this(null, a1);
    }
    
    public TraceFieldVisitor(final FieldVisitor a1, final Printer a2) {
        super(327680, a1);
        this.p = a2;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final Printer v1 = /*EL:60*/this.p.visitFieldAnnotation(a1, a2);
        final AnnotationVisitor v2 = /*EL:61*/(this.fv == null) ? null : this.fv.visitAnnotation(a1, a2);
        /*SL:63*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final Printer v1 = /*EL:69*/this.p.visitFieldTypeAnnotation(a1, a2, a3, a4);
        final AnnotationVisitor v2 = /*EL:71*/(this.fv == null) ? null : this.fv.visitTypeAnnotation(a1, a2, a3, a4);
        /*SL:73*/return new TraceAnnotationVisitor(v2, v1);
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:78*/this.p.visitFieldAttribute(a1);
        /*SL:79*/super.visitAttribute(a1);
    }
    
    public void visitEnd() {
        /*SL:84*/this.p.visitFieldEnd();
        /*SL:85*/super.visitEnd();
    }
}
