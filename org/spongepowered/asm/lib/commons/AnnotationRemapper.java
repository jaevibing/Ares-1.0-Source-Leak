package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.AnnotationVisitor;

public class AnnotationRemapper extends AnnotationVisitor
{
    protected final Remapper remapper;
    
    public AnnotationRemapper(final AnnotationVisitor a1, final Remapper a2) {
        this(327680, a1, a2);
    }
    
    protected AnnotationRemapper(final int a1, final AnnotationVisitor a2, final Remapper a3) {
        super(a1, a2);
        this.remapper = a3;
    }
    
    public void visit(final String a1, final Object a2) {
        /*SL:58*/this.av.visit(a1, this.remapper.mapValue(a2));
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:63*/this.av.visitEnum(a1, this.remapper.mapDesc(a2), a3);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        final AnnotationVisitor v1 = /*EL:68*/this.av.visitAnnotation(a1, this.remapper.mapDesc(a2));
        /*SL:69*/return (v1 == null) ? null : ((v1 == this.av) ? this : new AnnotationRemapper(v1, this.remapper));
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        final AnnotationVisitor v1 = /*EL:75*/this.av.visitArray(a1);
        /*SL:76*/return (v1 == null) ? null : ((v1 == this.av) ? this : new AnnotationRemapper(v1, this.remapper));
    }
}
