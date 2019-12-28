package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.AnnotationVisitor;

public class CheckAnnotationAdapter extends AnnotationVisitor
{
    private final boolean named;
    private boolean end;
    
    public CheckAnnotationAdapter(final AnnotationVisitor a1) {
        this(a1, true);
    }
    
    CheckAnnotationAdapter(final AnnotationVisitor a1, final boolean a2) {
        super(327680, a1);
        this.named = a2;
    }
    
    public void visit(final String v1, final Object v2) {
        /*SL:58*/this.checkEnd();
        /*SL:59*/this.checkName(v1);
        /*SL:60*/if (!(v2 instanceof Byte) && !(v2 instanceof Boolean) && !(v2 instanceof Character) && !(v2 instanceof Short) && !(v2 instanceof Integer) && !(v2 instanceof Long) && !(v2 instanceof Float) && !(v2 instanceof Double) && !(v2 instanceof String) && !(v2 instanceof Type) && !(v2 instanceof byte[]) && !(v2 instanceof boolean[]) && !(v2 instanceof char[]) && !(v2 instanceof short[]) && !(v2 instanceof int[]) && !(v2 instanceof long[]) && !(v2 instanceof float[]) && !(v2 instanceof double[])) {
            /*SL:69*/throw new IllegalArgumentException("Invalid annotation value");
        }
        /*SL:71*/if (v2 instanceof Type) {
            final int a1 = /*EL:72*/((Type)v2).getSort();
            /*SL:73*/if (a1 == 11) {
                /*SL:74*/throw new IllegalArgumentException("Invalid annotation value");
            }
        }
        /*SL:77*/if (this.av != null) {
            /*SL:78*/this.av.visit(v1, v2);
        }
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:85*/this.checkEnd();
        /*SL:86*/this.checkName(a1);
        /*SL:87*/CheckMethodAdapter.checkDesc(a2, false);
        /*SL:88*/if (a3 == null) {
            /*SL:89*/throw new IllegalArgumentException("Invalid enum value");
        }
        /*SL:91*/if (this.av != null) {
            /*SL:92*/this.av.visitEnum(a1, a2, a3);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        /*SL:99*/this.checkEnd();
        /*SL:100*/this.checkName(a1);
        /*SL:101*/CheckMethodAdapter.checkDesc(a2, false);
        /*SL:102*/return new CheckAnnotationAdapter((this.av == null) ? null : this.av.visitAnnotation(a1, a2));
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        /*SL:108*/this.checkEnd();
        /*SL:109*/this.checkName(a1);
        /*SL:110*/return new CheckAnnotationAdapter((this.av == null) ? null : this.av.visitArray(a1), /*EL:111*/false);
    }
    
    public void visitEnd() {
        /*SL:116*/this.checkEnd();
        /*SL:117*/this.end = true;
        /*SL:118*/if (this.av != null) {
            /*SL:119*/this.av.visitEnd();
        }
    }
    
    private void checkEnd() {
        /*SL:124*/if (this.end) {
            /*SL:125*/throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
    
    private void checkName(final String a1) {
        /*SL:131*/if (this.named && a1 == null) {
            /*SL:132*/throw new IllegalArgumentException("Annotation value name must not be null");
        }
    }
}
