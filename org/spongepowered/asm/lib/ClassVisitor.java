package org.spongepowered.asm.lib;

public abstract class ClassVisitor
{
    protected final int api;
    protected ClassVisitor cv;
    
    public ClassVisitor(final int a1) {
        this(a1, null);
    }
    
    public ClassVisitor(final int a1, final ClassVisitor a2) {
        if (a1 != 262144 && a1 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = a1;
        this.cv = a2;
    }
    
    public void visit(final int a1, final int a2, final String a3, final String a4, final String a5, final String[] a6) {
        /*SL:112*/if (this.cv != null) {
            /*SL:113*/this.cv.visit(a1, a2, a3, a4, a5, a6);
        }
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:129*/if (this.cv != null) {
            /*SL:130*/this.cv.visitSource(a1, a2);
        }
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:150*/if (this.cv != null) {
            /*SL:151*/this.cv.visitOuterClass(a1, a2, a3);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:166*/if (this.cv != null) {
            /*SL:167*/return this.cv.visitAnnotation(a1, a2);
        }
        /*SL:169*/return null;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:196*/if (this.api < 327680) {
            /*SL:197*/throw new RuntimeException();
        }
        /*SL:199*/if (this.cv != null) {
            /*SL:200*/return this.cv.visitTypeAnnotation(a1, a2, a3, a4);
        }
        /*SL:202*/return null;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:212*/if (this.cv != null) {
            /*SL:213*/this.cv.visitAttribute(a1);
        }
    }
    
    public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        /*SL:237*/if (this.cv != null) {
            /*SL:238*/this.cv.visitInnerClass(a1, a2, a3, a4);
        }
    }
    
    public FieldVisitor visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        /*SL:271*/if (this.cv != null) {
            /*SL:272*/return this.cv.visitField(a1, a2, a3, a4, a5);
        }
        /*SL:274*/return null;
    }
    
    public MethodVisitor visitMethod(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
        /*SL:304*/if (this.cv != null) {
            /*SL:305*/return this.cv.visitMethod(a1, a2, a3, a4, a5);
        }
        /*SL:307*/return null;
    }
    
    public void visitEnd() {
        /*SL:316*/if (this.cv != null) {
            /*SL:317*/this.cv.visitEnd();
        }
    }
}
