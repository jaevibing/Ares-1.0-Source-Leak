package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.TypePath;
import java.util.ArrayList;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.Attribute;
import java.util.List;
import org.spongepowered.asm.lib.FieldVisitor;

public class FieldNode extends FieldVisitor
{
    public int access;
    public String name;
    public String desc;
    public String signature;
    public Object value;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    
    public FieldNode(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        this(327680, a1, a2, a3, a4, a5);
        if (this.getClass() != FieldNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public FieldNode(final int a1, final int a2, final String a3, final String a4, final String a5, final Object a6) {
        super(a1);
        this.access = a2;
        this.name = a3;
        this.desc = a4;
        this.signature = a5;
        this.value = a6;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final AnnotationNode v1 = /*EL:194*/new AnnotationNode(a1);
        /*SL:195*/if (a2) {
            /*SL:196*/if (this.visibleAnnotations == null) {
                /*SL:197*/this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:199*/this.visibleAnnotations.add(v1);
        }
        else {
            /*SL:201*/if (this.invisibleAnnotations == null) {
                /*SL:202*/this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:204*/this.invisibleAnnotations.add(v1);
        }
        /*SL:206*/return v1;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final TypeAnnotationNode v1 = /*EL:212*/new TypeAnnotationNode(a1, a2, a3);
        /*SL:213*/if (a4) {
            /*SL:214*/if (this.visibleTypeAnnotations == null) {
                /*SL:215*/this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:217*/this.visibleTypeAnnotations.add(v1);
        }
        else {
            /*SL:219*/if (this.invisibleTypeAnnotations == null) {
                /*SL:220*/this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:222*/this.invisibleTypeAnnotations.add(v1);
        }
        /*SL:224*/return v1;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:229*/if (this.attrs == null) {
            /*SL:230*/this.attrs = new ArrayList<Attribute>(1);
        }
        /*SL:232*/this.attrs.add(a1);
    }
    
    public void visitEnd() {
    }
    
    public void check(final int a1) {
        /*SL:254*/if (a1 == 262144) {
            /*SL:255*/if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > /*EL:256*/0) {
                /*SL:257*/throw new RuntimeException();
            }
            /*SL:259*/if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > /*EL:260*/0) {
                /*SL:261*/throw new RuntimeException();
            }
        }
    }
    
    public void accept(final ClassVisitor v-3) {
        final FieldVisitor visitField = /*EL:273*/v-3.visitField(this.access, this.name, this.desc, this.signature, this.value);
        /*SL:274*/if (visitField == null) {
            /*SL:275*/return;
        }
        /*SL:279*/for (int n = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode a1 = /*EL:280*/this.visibleAnnotations.get(v0);
            /*SL:281*/a1.accept(visitField.visitAnnotation(a1.desc, true));
        }
        /*SL:284*/for (int n = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode v = /*EL:285*/this.invisibleAnnotations.get(v0);
            /*SL:286*/v.accept(visitField.visitAnnotation(v.desc, false));
        }
        /*SL:289*/for (int n = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v2 = /*EL:290*/this.visibleTypeAnnotations.get(v0);
            /*SL:291*/v2.accept(visitField.visitTypeAnnotation(v2.typeRef, v2.typePath, v2.desc, true));
        }
        /*SL:296*/for (int n = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v2 = /*EL:297*/this.invisibleTypeAnnotations.get(v0);
            /*SL:298*/v2.accept(visitField.visitTypeAnnotation(v2.typeRef, v2.typePath, v2.desc, false));
        }
        /*SL:302*/for (int n = (this.attrs == null) ? 0 : this.attrs.size(), v0 = 0; v0 < n; ++v0) {
            /*SL:303*/visitField.visitAttribute(this.attrs.get(v0));
        }
        /*SL:305*/visitField.visitEnd();
    }
}
