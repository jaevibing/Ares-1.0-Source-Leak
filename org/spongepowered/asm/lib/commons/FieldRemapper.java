package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;

public class FieldRemapper extends FieldVisitor
{
    private final Remapper remapper;
    
    public FieldRemapper(final FieldVisitor a1, final Remapper a2) {
        this(327680, a1, a2);
    }
    
    protected FieldRemapper(final int a1, final FieldVisitor a2, final Remapper a3) {
        super(a1, a2);
        this.remapper = a3;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final AnnotationVisitor v1 = /*EL:59*/this.fv.visitAnnotation(this.remapper.mapDesc(a1), a2);
        /*SL:61*/return (v1 == null) ? null : new AnnotationRemapper(v1, this.remapper);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final AnnotationVisitor v1 = /*EL:67*/super.visitTypeAnnotation(a1, a2, this.remapper.mapDesc(a3), /*EL:68*/a4);
        /*SL:69*/return (v1 == null) ? null : new AnnotationRemapper(v1, this.remapper);
    }
}
