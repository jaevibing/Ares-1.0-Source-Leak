package org.spongepowered.asm.lib.tree;

import java.util.Iterator;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.Attribute;
import java.util.List;
import org.spongepowered.asm.lib.ClassVisitor;

public class ClassNode extends ClassVisitor
{
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public List<FieldNode> fields;
    public List<MethodNode> methods;
    
    public ClassNode() {
        this(327680);
        if (this.getClass() != ClassNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public ClassNode(final int a1) {
        super(a1);
        this.interfaces = new ArrayList<String>();
        this.innerClasses = new ArrayList<InnerClassNode>();
        this.fields = new ArrayList<FieldNode>();
        this.methods = new ArrayList<MethodNode>();
    }
    
    public void visit(final int a1, final int a2, final String a3, final String a4, final String a5, final String[] a6) {
        /*SL:224*/this.version = a1;
        /*SL:225*/this.access = a2;
        /*SL:226*/this.name = a3;
        /*SL:227*/this.signature = a4;
        /*SL:228*/this.superName = a5;
        /*SL:229*/if (a6 != null) {
            /*SL:230*/this.interfaces.addAll(Arrays.<String>asList(a6));
        }
    }
    
    public void visitSource(final String a1, final String a2) {
        /*SL:236*/this.sourceFile = a1;
        /*SL:237*/this.sourceDebug = a2;
    }
    
    public void visitOuterClass(final String a1, final String a2, final String a3) {
        /*SL:243*/this.outerClass = a1;
        /*SL:244*/this.outerMethod = a2;
        /*SL:245*/this.outerMethodDesc = a3;
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final AnnotationNode v1 = /*EL:251*/new AnnotationNode(a1);
        /*SL:252*/if (a2) {
            /*SL:253*/if (this.visibleAnnotations == null) {
                /*SL:254*/this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:256*/this.visibleAnnotations.add(v1);
        }
        else {
            /*SL:258*/if (this.invisibleAnnotations == null) {
                /*SL:259*/this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:261*/this.invisibleAnnotations.add(v1);
        }
        /*SL:263*/return v1;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final TypeAnnotationNode v1 = /*EL:269*/new TypeAnnotationNode(a1, a2, a3);
        /*SL:270*/if (a4) {
            /*SL:271*/if (this.visibleTypeAnnotations == null) {
                /*SL:272*/this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:274*/this.visibleTypeAnnotations.add(v1);
        }
        else {
            /*SL:276*/if (this.invisibleTypeAnnotations == null) {
                /*SL:277*/this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:279*/this.invisibleTypeAnnotations.add(v1);
        }
        /*SL:281*/return v1;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:286*/if (this.attrs == null) {
            /*SL:287*/this.attrs = new ArrayList<Attribute>(1);
        }
        /*SL:289*/this.attrs.add(a1);
    }
    
    public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
        final InnerClassNode v1 = /*EL:295*/new InnerClassNode(a1, a2, a3, a4);
        /*SL:297*/this.innerClasses.add(v1);
    }
    
    public FieldVisitor visitField(final int a1, final String a2, final String a3, final String a4, final Object a5) {
        final FieldNode v1 = /*EL:303*/new FieldNode(a1, a2, a3, a4, a5);
        /*SL:304*/this.fields.add(v1);
        /*SL:305*/return v1;
    }
    
    public MethodVisitor visitMethod(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
        final MethodNode v1 = /*EL:311*/new MethodNode(a1, a2, a3, a4, a5);
        /*SL:313*/this.methods.add(v1);
        /*SL:314*/return v1;
    }
    
    public void visitEnd() {
    }
    
    public void check(final int v-1) {
        /*SL:336*/if (v-1 == 262144) {
            /*SL:337*/if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > /*EL:338*/0) {
                /*SL:339*/throw new RuntimeException();
            }
            /*SL:341*/if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > /*EL:342*/0) {
                /*SL:343*/throw new RuntimeException();
            }
            /*SL:345*/for (final FieldNode a1 : this.fields) {
                /*SL:346*/a1.check(v-1);
            }
            /*SL:348*/for (final MethodNode v1 : this.methods) {
                /*SL:349*/v1.check(v-1);
            }
        }
    }
    
    public void accept(final ClassVisitor v-3) {
        final String[] a2 = /*EL:362*/new String[this.interfaces.size()];
        /*SL:363*/this.interfaces.<String>toArray(a2);
        /*SL:364*/v-3.visit(this.version, this.access, this.name, this.signature, this.superName, a2);
        /*SL:366*/if (this.sourceFile != null || this.sourceDebug != null) {
            /*SL:367*/v-3.visitSource(this.sourceFile, this.sourceDebug);
        }
        /*SL:370*/if (this.outerClass != null) {
            /*SL:371*/v-3.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }
        /*SL:376*/for (int n = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode a1 = /*EL:377*/this.visibleAnnotations.get(v0);
            /*SL:378*/a1.accept(v-3.visitAnnotation(a1.desc, true));
        }
        /*SL:381*/for (int n = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode v = /*EL:382*/this.invisibleAnnotations.get(v0);
            /*SL:383*/v.accept(v-3.visitAnnotation(v.desc, false));
        }
        /*SL:386*/for (int n = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v2 = /*EL:387*/this.visibleTypeAnnotations.get(v0);
            /*SL:388*/v2.accept(v-3.visitTypeAnnotation(v2.typeRef, v2.typePath, v2.desc, true));
        }
        /*SL:393*/for (int n = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v2 = /*EL:394*/this.invisibleTypeAnnotations.get(v0);
            /*SL:395*/v2.accept(v-3.visitTypeAnnotation(v2.typeRef, v2.typePath, v2.desc, false));
        }
        /*SL:399*/for (int n = (this.attrs == null) ? 0 : this.attrs.size(), v0 = 0; v0 < n; ++v0) {
            /*SL:400*/v-3.visitAttribute(this.attrs.get(v0));
        }
        /*SL:403*/for (int v0 = 0; v0 < this.innerClasses.size(); ++v0) {
            /*SL:404*/this.innerClasses.get(v0).accept(v-3);
        }
        /*SL:407*/for (int v0 = 0; v0 < this.fields.size(); ++v0) {
            /*SL:408*/this.fields.get(v0).accept(v-3);
        }
        /*SL:411*/for (int v0 = 0; v0 < this.methods.size(); ++v0) {
            /*SL:412*/this.methods.get(v0).accept(v-3);
        }
        /*SL:415*/v-3.visitEnd();
    }
}
