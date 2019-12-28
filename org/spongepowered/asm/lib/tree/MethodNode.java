package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.Attribute;
import java.util.List;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodNode extends MethodVisitor
{
    public int access;
    public String name;
    public String desc;
    public String signature;
    public List<String> exceptions;
    public List<ParameterNode> parameters;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public Object annotationDefault;
    public List<AnnotationNode>[] visibleParameterAnnotations;
    public List<AnnotationNode>[] invisibleParameterAnnotations;
    public InsnList instructions;
    public List<TryCatchBlockNode> tryCatchBlocks;
    public int maxStack;
    public int maxLocals;
    public List<LocalVariableNode> localVariables;
    public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;
    public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;
    private boolean visited;
    
    public MethodNode() {
        this(327680);
        if (this.getClass() != MethodNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public MethodNode(final int a1) {
        super(a1);
        this.instructions = new InsnList();
    }
    
    public MethodNode(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
        this(327680, a1, a2, a3, a4, a5);
        if (this.getClass() != MethodNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public MethodNode(final int a1, final int a2, final String a3, final String a4, final String a5, final String[] a6) {
        super(a1);
        this.access = a2;
        this.name = a3;
        this.desc = a4;
        this.signature = a5;
        this.exceptions = new ArrayList<String>((a6 == null) ? 0 : a6.length);
        final boolean v1 = (a2 & 0x400) != 0x0;
        if (!v1) {
            this.localVariables = new ArrayList<LocalVariableNode>(5);
        }
        this.tryCatchBlocks = new ArrayList<TryCatchBlockNode>();
        if (a6 != null) {
            this.exceptions.addAll(Arrays.<String>asList(a6));
        }
        this.instructions = new InsnList();
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:318*/if (this.parameters == null) {
            /*SL:319*/this.parameters = new ArrayList<ParameterNode>(5);
        }
        /*SL:321*/this.parameters.add(new ParameterNode(a1, a2));
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        /*SL:327*/return new AnnotationNode(new ArrayList<Object>(0) {
            public boolean add(final Object a1) {
                /*SL:330*/MethodNode.this.annotationDefault = a1;
                /*SL:331*/return super.add(a1);
            }
        });
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final AnnotationNode v1 = /*EL:339*/new AnnotationNode(a1);
        /*SL:340*/if (a2) {
            /*SL:341*/if (this.visibleAnnotations == null) {
                /*SL:342*/this.visibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:344*/this.visibleAnnotations.add(v1);
        }
        else {
            /*SL:346*/if (this.invisibleAnnotations == null) {
                /*SL:347*/this.invisibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            /*SL:349*/this.invisibleAnnotations.add(v1);
        }
        /*SL:351*/return v1;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final TypeAnnotationNode v1 = /*EL:357*/new TypeAnnotationNode(a1, a2, a3);
        /*SL:358*/if (a4) {
            /*SL:359*/if (this.visibleTypeAnnotations == null) {
                /*SL:360*/this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:362*/this.visibleTypeAnnotations.add(v1);
        }
        else {
            /*SL:364*/if (this.invisibleTypeAnnotations == null) {
                /*SL:365*/this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:367*/this.invisibleTypeAnnotations.add(v1);
        }
        /*SL:369*/return v1;
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int v1, final String v2, final boolean v3) {
        final AnnotationNode v4 = /*EL:376*/new AnnotationNode(v2);
        /*SL:377*/if (v3) {
            /*SL:378*/if (this.visibleParameterAnnotations == null) {
                final int a1 = /*EL:379*/Type.getArgumentTypes(this.desc).length;
                /*SL:380*/this.visibleParameterAnnotations = (List<AnnotationNode>[])new List[a1];
            }
            /*SL:382*/if (this.visibleParameterAnnotations[v1] == null) {
                /*SL:383*/this.visibleParameterAnnotations[v1] = new ArrayList<AnnotationNode>(1);
            }
            /*SL:386*/this.visibleParameterAnnotations[v1].add(v4);
        }
        else {
            /*SL:388*/if (this.invisibleParameterAnnotations == null) {
                final int a2 = /*EL:389*/Type.getArgumentTypes(this.desc).length;
                /*SL:390*/this.invisibleParameterAnnotations = (List<AnnotationNode>[])new List[a2];
            }
            /*SL:392*/if (this.invisibleParameterAnnotations[v1] == null) {
                /*SL:393*/this.invisibleParameterAnnotations[v1] = new ArrayList<AnnotationNode>(1);
            }
            /*SL:396*/this.invisibleParameterAnnotations[v1].add(v4);
        }
        /*SL:398*/return v4;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:403*/if (this.attrs == null) {
            /*SL:404*/this.attrs = new ArrayList<Attribute>(1);
        }
        /*SL:406*/this.attrs.add(a1);
    }
    
    public void visitCode() {
    }
    
    public void visitFrame(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        /*SL:416*/this.instructions.add(new FrameNode(/*EL:418*/a1, a2, (Object[])((a3 == null) ? null : this.getLabelNodes(a3)), a4, (Object[])((a5 == null) ? null : this.getLabelNodes(a5))));
    }
    
    public void visitInsn(final int a1) {
        /*SL:423*/this.instructions.add(new InsnNode(a1));
    }
    
    public void visitIntInsn(final int a1, final int a2) {
        /*SL:428*/this.instructions.add(new IntInsnNode(a1, a2));
    }
    
    public void visitVarInsn(final int a1, final int a2) {
        /*SL:433*/this.instructions.add(new VarInsnNode(a1, a2));
    }
    
    public void visitTypeInsn(final int a1, final String a2) {
        /*SL:438*/this.instructions.add(new TypeInsnNode(a1, a2));
    }
    
    public void visitFieldInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:444*/this.instructions.add(new FieldInsnNode(a1, a2, a3, a4));
    }
    
    @Deprecated
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4) {
        /*SL:451*/if (this.api >= 327680) {
            /*SL:452*/super.visitMethodInsn(a1, a2, a3, a4);
            /*SL:453*/return;
        }
        /*SL:455*/this.instructions.add(new MethodInsnNode(a1, a2, a3, a4));
    }
    
    public void visitMethodInsn(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:461*/if (this.api < 327680) {
            /*SL:462*/super.visitMethodInsn(a1, a2, a3, a4, a5);
            /*SL:463*/return;
        }
        /*SL:465*/this.instructions.add(new MethodInsnNode(a1, a2, a3, a4, a5));
    }
    
    public void visitInvokeDynamicInsn(final String a1, final String a2, final Handle a3, final Object... a4) {
        /*SL:471*/this.instructions.add(new InvokeDynamicInsnNode(a1, a2, a3, a4));
    }
    
    public void visitJumpInsn(final int a1, final Label a2) {
        /*SL:476*/this.instructions.add(new JumpInsnNode(a1, this.getLabelNode(a2)));
    }
    
    public void visitLabel(final Label a1) {
        /*SL:481*/this.instructions.add(this.getLabelNode(a1));
    }
    
    public void visitLdcInsn(final Object a1) {
        /*SL:486*/this.instructions.add(new LdcInsnNode(a1));
    }
    
    public void visitIincInsn(final int a1, final int a2) {
        /*SL:491*/this.instructions.add(new IincInsnNode(a1, a2));
    }
    
    public void visitTableSwitchInsn(final int a1, final int a2, final Label a3, final Label... a4) {
        /*SL:497*/this.instructions.add(/*EL:498*/new TableSwitchInsnNode(a1, a2, this.getLabelNode(a3), this.getLabelNodes(a4)));
    }
    
    public void visitLookupSwitchInsn(final Label a1, final int[] a2, final Label[] a3) {
        /*SL:504*/this.instructions.add(/*EL:505*/new LookupSwitchInsnNode(this.getLabelNode(a1), a2, this.getLabelNodes(a3)));
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:510*/this.instructions.add(new MultiANewArrayInsnNode(a1, a2));
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        AbstractInsnNode v1;
        /*SL:519*/for (v1 = this.instructions.getLast(); v1.getOpcode() == -1; /*SL:520*/v1 = v1.getPrevious()) {}
        final TypeAnnotationNode v2 = /*EL:523*/new TypeAnnotationNode(a1, a2, a3);
        /*SL:524*/if (a4) {
            /*SL:525*/if (v1.visibleTypeAnnotations == null) {
                /*SL:526*/v1.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:529*/v1.visibleTypeAnnotations.add(v2);
        }
        else {
            /*SL:531*/if (v1.invisibleTypeAnnotations == null) {
                /*SL:532*/v1.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:535*/v1.invisibleTypeAnnotations.add(v2);
        }
        /*SL:537*/return v2;
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:543*/this.tryCatchBlocks.add(/*EL:544*/new TryCatchBlockNode(this.getLabelNode(a1), this.getLabelNode(a2), this.getLabelNode(a3), a4));
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final TryCatchBlockNode v1 = /*EL:550*/this.tryCatchBlocks.get((a1 & 0xFFFF00) >> 8);
        final TypeAnnotationNode v2 = /*EL:551*/new TypeAnnotationNode(a1, a2, a3);
        /*SL:552*/if (a4) {
            /*SL:553*/if (v1.visibleTypeAnnotations == null) {
                /*SL:554*/v1.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:557*/v1.visibleTypeAnnotations.add(v2);
        }
        else {
            /*SL:559*/if (v1.invisibleTypeAnnotations == null) {
                /*SL:560*/v1.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>(1);
            }
            /*SL:563*/v1.invisibleTypeAnnotations.add(v2);
        }
        /*SL:565*/return v2;
    }
    
    public void visitLocalVariable(final String a1, final String a2, final String a3, final Label a4, final Label a5, final int a6) {
        /*SL:572*/this.localVariables.add(/*EL:573*/new LocalVariableNode(a1, a2, a3, this.getLabelNode(a4), this.getLabelNode(a5), a6));
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a1, final TypePath a2, final Label[] a3, final Label[] a4, final int[] a5, final String a6, final boolean a7) {
        final LocalVariableAnnotationNode v1 = /*EL:581*/new LocalVariableAnnotationNode(a1, a2, this.getLabelNodes(a3), this.getLabelNodes(a4), a5, a6);
        /*SL:583*/if (a7) {
            /*SL:584*/if (this.visibleLocalVariableAnnotations == null) {
                /*SL:585*/this.visibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
            }
            /*SL:588*/this.visibleLocalVariableAnnotations.add(v1);
        }
        else {
            /*SL:590*/if (this.invisibleLocalVariableAnnotations == null) {
                /*SL:591*/this.invisibleLocalVariableAnnotations = new ArrayList<LocalVariableAnnotationNode>(1);
            }
            /*SL:594*/this.invisibleLocalVariableAnnotations.add(v1);
        }
        /*SL:596*/return v1;
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:601*/this.instructions.add(new LineNumberNode(a1, this.getLabelNode(a2)));
    }
    
    public void visitMaxs(final int a1, final int a2) {
        /*SL:606*/this.maxStack = a1;
        /*SL:607*/this.maxLocals = a2;
    }
    
    public void visitEnd() {
    }
    
    protected LabelNode getLabelNode(final Label a1) {
        /*SL:625*/if (!(a1.info instanceof LabelNode)) {
            /*SL:626*/a1.info = new LabelNode();
        }
        /*SL:628*/return (LabelNode)a1.info;
    }
    
    private LabelNode[] getLabelNodes(final Label[] v2) {
        final LabelNode[] v3 = /*EL:632*/new LabelNode[v2.length];
        /*SL:633*/for (int a1 = 0; a1 < v2.length; ++a1) {
            /*SL:634*/v3[a1] = this.getLabelNode(v2[a1]);
        }
        /*SL:636*/return v3;
    }
    
    private Object[] getLabelNodes(final Object[] v-1) {
        final Object[] v0 = /*EL:640*/new Object[v-1.length];
        /*SL:641*/for (int v = 0; v < v-1.length; ++v) {
            Object a1 = /*EL:642*/v-1[v];
            /*SL:643*/if (a1 instanceof Label) {
                /*SL:644*/a1 = this.getLabelNode((Label)a1);
            }
            /*SL:646*/v0[v] = a1;
        }
        /*SL:648*/return v0;
    }
    
    public void check(final int v-1) {
        /*SL:666*/if (v-1 == 262144) {
            /*SL:667*/if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > /*EL:668*/0) {
                /*SL:669*/throw new RuntimeException();
            }
            /*SL:671*/if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > /*EL:672*/0) {
                /*SL:673*/throw new RuntimeException();
            }
            /*SL:676*/for (int v0 = (this.tryCatchBlocks == null) ? 0 : this.tryCatchBlocks.size(), v = 0; v < v0; ++v) {
                final TryCatchBlockNode a1 = /*EL:677*/this.tryCatchBlocks.get(v);
                /*SL:678*/if (a1.visibleTypeAnnotations != null && a1.visibleTypeAnnotations.size() > /*EL:679*/0) {
                    /*SL:680*/throw new RuntimeException();
                }
                /*SL:682*/if (a1.invisibleTypeAnnotations != null && a1.invisibleTypeAnnotations.size() > /*EL:683*/0) {
                    /*SL:684*/throw new RuntimeException();
                }
            }
            /*SL:687*/for (int v = 0; v < this.instructions.size(); ++v) {
                final AbstractInsnNode v2 = /*EL:688*/this.instructions.get(v);
                /*SL:689*/if (v2.visibleTypeAnnotations != null && v2.visibleTypeAnnotations.size() > /*EL:690*/0) {
                    /*SL:691*/throw new RuntimeException();
                }
                /*SL:693*/if (v2.invisibleTypeAnnotations != null && v2.invisibleTypeAnnotations.size() > /*EL:694*/0) {
                    /*SL:695*/throw new RuntimeException();
                }
                /*SL:697*/if (v2 instanceof MethodInsnNode) {
                    final boolean v3 = /*EL:698*/((MethodInsnNode)v2).itf;
                    /*SL:699*/if (v3 != (v2.opcode == 185)) {
                        /*SL:700*/throw new RuntimeException();
                    }
                }
            }
            /*SL:704*/if (this.visibleLocalVariableAnnotations != null && this.visibleLocalVariableAnnotations.size() > /*EL:705*/0) {
                /*SL:706*/throw new RuntimeException();
            }
            /*SL:708*/if (this.invisibleLocalVariableAnnotations != null && this.invisibleLocalVariableAnnotations.size() > /*EL:709*/0) {
                /*SL:710*/throw new RuntimeException();
            }
        }
    }
    
    public void accept(final ClassVisitor a1) {
        final String[] v1 = /*EL:722*/new String[this.exceptions.size()];
        /*SL:723*/this.exceptions.<String>toArray(v1);
        final MethodVisitor v2 = /*EL:724*/a1.visitMethod(this.access, this.name, this.desc, this.signature, v1);
        /*SL:726*/if (v2 != null) {
            /*SL:727*/this.accept(v2);
        }
    }
    
    public void accept(final MethodVisitor v-2) {
        /*SL:741*/for (int n = (this.parameters == null) ? 0 : this.parameters.size(), v0 = 0; v0 < n; ++v0) {
            final ParameterNode a1 = /*EL:742*/this.parameters.get(v0);
            /*SL:743*/v-2.visitParameter(a1.name, a1.access);
        }
        /*SL:746*/if (this.annotationDefault != null) {
            final AnnotationVisitor v = /*EL:747*/v-2.visitAnnotationDefault();
            /*SL:748*/AnnotationNode.accept(v, null, this.annotationDefault);
            /*SL:749*/if (v != null) {
                /*SL:750*/v.visitEnd();
            }
        }
        /*SL:754*/for (int n = (this.visibleAnnotations == null) ? 0 : this.visibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode v2 = /*EL:755*/this.visibleAnnotations.get(v0);
            /*SL:756*/v2.accept(v-2.visitAnnotation(v2.desc, true));
        }
        /*SL:759*/for (int n = (this.invisibleAnnotations == null) ? 0 : this.invisibleAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final AnnotationNode v2 = /*EL:760*/this.invisibleAnnotations.get(v0);
            /*SL:761*/v2.accept(v-2.visitAnnotation(v2.desc, false));
        }
        /*SL:764*/for (int n = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v3 = /*EL:765*/this.visibleTypeAnnotations.get(v0);
            /*SL:766*/v3.accept(v-2.visitTypeAnnotation(v3.typeRef, v3.typePath, v3.desc, true));
        }
        /*SL:771*/for (int n = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), v0 = 0; v0 < n; ++v0) {
            final TypeAnnotationNode v3 = /*EL:772*/this.invisibleTypeAnnotations.get(v0);
            /*SL:773*/v3.accept(v-2.visitTypeAnnotation(v3.typeRef, v3.typePath, v3.desc, false));
        }
        /*SL:778*/for (int n = (this.visibleParameterAnnotations == null) ? 0 : this.visibleParameterAnnotations.length, v0 = 0; v0 < n; ++v0) {
            final List<?> a2 = /*EL:779*/this.visibleParameterAnnotations[v0];
            /*SL:780*/if (a2 != null) {
                /*SL:783*/for (int v4 = 0; v4 < a2.size(); ++v4) {
                    final AnnotationNode v5 = /*EL:784*/(AnnotationNode)a2.get(v4);
                    /*SL:785*/v5.accept(v-2.visitParameterAnnotation(v0, v5.desc, true));
                }
            }
        }
        /*SL:790*/for (int n = (this.invisibleParameterAnnotations == null) ? 0 : this.invisibleParameterAnnotations.length, v0 = 0; v0 < n; ++v0) {
            final List<?> a2 = /*EL:791*/this.invisibleParameterAnnotations[v0];
            /*SL:792*/if (a2 != null) {
                /*SL:795*/for (int v4 = 0; v4 < a2.size(); ++v4) {
                    final AnnotationNode v5 = /*EL:796*/(AnnotationNode)a2.get(v4);
                    /*SL:797*/v5.accept(v-2.visitParameterAnnotation(v0, v5.desc, false));
                }
            }
        }
        /*SL:800*/if (this.visited) {
            /*SL:801*/this.instructions.resetLabels();
        }
        /*SL:804*/for (int n = (this.attrs == null) ? 0 : this.attrs.size(), v0 = 0; v0 < n; ++v0) {
            /*SL:805*/v-2.visitAttribute(this.attrs.get(v0));
        }
        /*SL:808*/if (this.instructions.size() > 0) {
            /*SL:809*/v-2.visitCode();
            /*SL:812*/for (int n = (this.tryCatchBlocks == null) ? 0 : this.tryCatchBlocks.size(), v0 = 0; v0 < n; ++v0) {
                /*SL:813*/this.tryCatchBlocks.get(v0).updateIndex(v0);
                /*SL:814*/this.tryCatchBlocks.get(v0).accept(v-2);
            }
            /*SL:817*/this.instructions.accept(v-2);
            /*SL:820*/for (int n = (this.localVariables == null) ? 0 : this.localVariables.size(), v0 = 0; v0 < n; ++v0) {
                /*SL:821*/this.localVariables.get(v0).accept(v-2);
            }
            /*SL:826*/for (int n = (this.visibleLocalVariableAnnotations == null) ? 0 : this.visibleLocalVariableAnnotations.size(), v0 = 0; v0 < n; ++v0) {
                /*SL:827*/this.visibleLocalVariableAnnotations.get(v0).accept(v-2, true);
            }
            /*SL:831*/for (int n = (this.invisibleLocalVariableAnnotations == null) ? 0 : this.invisibleLocalVariableAnnotations.size(), v0 = 0; v0 < n; ++v0) {
                /*SL:832*/this.invisibleLocalVariableAnnotations.get(v0).accept(v-2, false);
            }
            /*SL:835*/v-2.visitMaxs(this.maxStack, this.maxLocals);
            /*SL:836*/this.visited = true;
        }
        /*SL:838*/v-2.visitEnd();
    }
}
