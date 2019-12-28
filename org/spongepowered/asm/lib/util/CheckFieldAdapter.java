package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;

public class CheckFieldAdapter extends FieldVisitor
{
    private boolean end;
    
    public CheckFieldAdapter(final FieldVisitor a1) {
        this(327680, a1);
        if (this.getClass() != CheckFieldAdapter.class) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckFieldAdapter(final int a1, final FieldVisitor a2) {
        super(a1, a2);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:79*/this.checkEnd();
        /*SL:80*/CheckMethodAdapter.checkDesc(a1, false);
        /*SL:81*/return new CheckAnnotationAdapter(super.visitAnnotation(a1, a2));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:87*/this.checkEnd();
        final int v1 = /*EL:88*/a1 >>> 24;
        /*SL:89*/if (v1 != 19) {
            /*SL:90*/throw new IllegalArgumentException("Invalid type reference sort 0x" + /*EL:91*/Integer.toHexString(v1));
        }
        /*SL:93*/CheckClassAdapter.checkTypeRefAndPath(a1, a2);
        /*SL:94*/CheckMethodAdapter.checkDesc(a3, false);
        /*SL:95*/return new CheckAnnotationAdapter(super.visitTypeAnnotation(a1, a2, a3, a4));
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:101*/this.checkEnd();
        /*SL:102*/if (a1 == null) {
            /*SL:103*/throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        /*SL:106*/super.visitAttribute(a1);
    }
    
    public void visitEnd() {
        /*SL:111*/this.checkEnd();
        /*SL:112*/this.end = true;
        /*SL:113*/super.visitEnd();
    }
    
    private void checkEnd() {
        /*SL:117*/if (this.end) {
            /*SL:118*/throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
}
