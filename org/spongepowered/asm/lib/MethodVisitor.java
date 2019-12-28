package org.spongepowered.asm.lib;

public abstract class MethodVisitor
{
    protected final int api;
    protected MethodVisitor mv;
    
    public MethodVisitor(final int a1) {
        this(a1, null);
    }
    
    public MethodVisitor(final int a1, final MethodVisitor a2) {
        if (a1 != 262144 && a1 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = a1;
        this.mv = a2;
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:114*/if (this.api < 327680) {
            /*SL:115*/throw new RuntimeException();
        }
        /*SL:117*/if (this.mv != null) {
            /*SL:118*/this.mv.visitParameter(a1, a2);
        }
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        /*SL:133*/if (this.mv != null) {
            /*SL:134*/return this.mv.visitAnnotationDefault();
        }
        /*SL:136*/return null;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        /*SL:150*/if (this.mv != null) {
            /*SL:151*/return this.mv.visitAnnotation(a1, a2);
        }
        /*SL:153*/return null;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:183*/if (this.api < 327680) {
            /*SL:184*/throw new RuntimeException();
        }
        /*SL:186*/if (this.mv != null) {
            /*SL:187*/return this.mv.visitTypeAnnotation(a1, a2, a3, a4);
        }
        /*SL:189*/return null;
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        /*SL:206*/if (this.mv != null) {
            /*SL:207*/return this.mv.visitParameterAnnotation(a1, a2, a3);
        }
        /*SL:209*/return null;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:219*/if (this.mv != null) {
            /*SL:220*/this.mv.visitAttribute(a1);
        }
    }
    
    public void visitCode() {
        /*SL:228*/if (this.mv != null) {
            /*SL:229*/this.mv.visitCode();
        }
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:310*/if (this.mv != null) {
            /*SL:311*/this.mv.visitFrame(a1, a2, a3, a4, a5);
        }
    }
    
    public void visitInsn(final int a1) {
        /*SL:340*/if (this.mv != null) {
            /*SL:341*/this.mv.visitInsn(a1);
        }
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:364*/if (this.mv != null) {
            /*SL:365*/this.mv.visitIntInsn(a1, a2);
        }
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:382*/if (this.mv != null) {
            /*SL:383*/this.mv.visitVarInsn(a1, a2);
        }
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:400*/if (this.mv != null) {
            /*SL:401*/this.mv.visitTypeInsn(a1, a2);
        }
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:422*/if (this.mv != null) {
            /*SL:423*/this.mv.visitFieldInsn(a1, a2, a3, a4);
        }
    }
    
    @Deprecated
    public void visitMethodInsn(final int a3, final String a4, final String v1, final String v2) {
        /*SL:446*/if (this.api >= 327680) {
            final boolean a5 = /*EL:447*/a3 == 185;
            /*SL:448*/this.visitMethodInsn(a3, a4, v1, v2, a5);
            /*SL:449*/return;
        }
        /*SL:451*/if (this.mv != null) {
            /*SL:452*/this.mv.visitMethodInsn(a3, a4, v1, v2);
        }
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:476*/if (this.api >= 327680) {
            /*SL:484*/if (this.mv != null) {
                /*SL:485*/this.mv.visitMethodInsn(a1, a2, a3, a4, a5);
            }
            /*SL:487*/return;
        }
        if (a5 != (a1 == 185)) {
            throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM 5");
        }
        this.visitMethodInsn(a1, a2, a3, a4);
    }
    
    public void visitInvokeDynamicInsn(final String a1, final String a2, final Handle a3, final Object... a4) {
        /*SL:507*/if (this.mv != null) {
            /*SL:508*/this.mv.visitInvokeDynamicInsn(a1, a2, a3, a4);
        }
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:527*/if (this.mv != null) {
            /*SL:528*/this.mv.visitJumpInsn(a1, a2);
        }
    }
    
    public void visitLabel(final Label a1) {
        /*SL:540*/if (this.mv != null) {
            /*SL:541*/this.mv.visitLabel(a1);
        }
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:594*/if (this.mv != null) {
            /*SL:595*/this.mv.visitLdcInsn(a1);
        }
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:608*/if (this.mv != null) {
            /*SL:609*/this.mv.visitIincInsn(a1, a2);
        }
    }
    
    public void visitTableSwitchInsn(final int a1, final int a2, final Label a3, final Label... a4) {
        /*SL:628*/if (this.mv != null) {
            /*SL:629*/this.mv.visitTableSwitchInsn(a1, a2, a3, a4);
        }
    }
    
    public void visitLookupSwitchInsn(final Label a1, final int[] a2, final Label[] a3) {
        /*SL:645*/if (this.mv != null) {
            /*SL:646*/this.mv.visitLookupSwitchInsn(a1, a2, a3);
        }
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:659*/if (this.mv != null) {
            /*SL:660*/this.mv.visitMultiANewArrayInsn(a1, a2);
        }
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:697*/if (this.api < 327680) {
            /*SL:698*/throw new RuntimeException();
        }
        /*SL:700*/if (this.mv != null) {
            /*SL:701*/return this.mv.visitInsnAnnotation(a1, a2, a3, a4);
        }
        /*SL:703*/return null;
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:729*/if (this.mv != null) {
            /*SL:730*/this.mv.visitTryCatchBlock(a1, a2, a3, a4);
        }
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        /*SL:757*/if (this.api < 327680) {
            /*SL:758*/throw new RuntimeException();
        }
        /*SL:760*/if (this.mv != null) {
            /*SL:761*/return this.mv.visitTryCatchAnnotation(a1, a2, a3, a4);
        }
        /*SL:763*/return null;
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:791*/if (this.mv != null) {
            /*SL:792*/this.mv.visitLocalVariable(a1, a2, a3, a4, a5, a6);
        }
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a1, final TypePath a2, final Label[] a3, final Label[] a4, final int[] a5, final String a6, final boolean a7) {
        /*SL:828*/if (this.api < 327680) {
            /*SL:829*/throw new RuntimeException();
        }
        /*SL:831*/if (this.mv != null) {
            /*SL:832*/return this.mv.visitLocalVariableAnnotation(a1, a2, a3, a4, a5, a6, a7);
        }
        /*SL:835*/return null;
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:851*/if (this.mv != null) {
            /*SL:852*/this.mv.visitLineNumber(a1, a2);
        }
    }
    
    public void visitMaxs(final int a1, final int a2) {
        /*SL:866*/if (this.mv != null) {
            /*SL:867*/this.mv.visitMaxs(a1, a2);
        }
    }
    
    public void visitEnd() {
        /*SL:877*/if (this.mv != null) {
            /*SL:878*/this.mv.visitEnd();
        }
    }
}
