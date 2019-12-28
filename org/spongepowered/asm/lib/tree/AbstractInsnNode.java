package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.AnnotationVisitor;
import java.util.ArrayList;
import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.List;

public abstract class AbstractInsnNode
{
    public static final int INSN = 0;
    public static final int INT_INSN = 1;
    public static final int VAR_INSN = 2;
    public static final int TYPE_INSN = 3;
    public static final int FIELD_INSN = 4;
    public static final int METHOD_INSN = 5;
    public static final int INVOKE_DYNAMIC_INSN = 6;
    public static final int JUMP_INSN = 7;
    public static final int LABEL = 8;
    public static final int LDC_INSN = 9;
    public static final int IINC_INSN = 10;
    public static final int TABLESWITCH_INSN = 11;
    public static final int LOOKUPSWITCH_INSN = 12;
    public static final int MULTIANEWARRAY_INSN = 13;
    public static final int FRAME = 14;
    public static final int LINE = 15;
    protected int opcode;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    AbstractInsnNode prev;
    AbstractInsnNode next;
    int index;
    
    protected AbstractInsnNode(final int a1) {
        this.opcode = a1;
        this.index = -1;
    }
    
    public int getOpcode() {
        /*SL:188*/return this.opcode;
    }
    
    public abstract int getType();
    
    public AbstractInsnNode getPrevious() {
        /*SL:207*/return this.prev;
    }
    
    public AbstractInsnNode getNext() {
        /*SL:218*/return this.next;
    }
    
    public abstract void accept(final MethodVisitor p0);
    
    protected final void acceptAnnotations(final MethodVisitor v-1) {
        /*SL:238*/for (int v0 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), v = 0; v < v0; ++v) {
            final TypeAnnotationNode a1 = /*EL:239*/this.visibleTypeAnnotations.get(v);
            /*SL:240*/a1.accept(v-1.visitInsnAnnotation(a1.typeRef, a1.typePath, a1.desc, true));
        }
        /*SL:245*/for (int v0 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), v = 0; v < v0; ++v) {
            final TypeAnnotationNode v2 = /*EL:246*/this.invisibleTypeAnnotations.get(v);
            /*SL:247*/v2.accept(v-1.visitInsnAnnotation(v2.typeRef, v2.typePath, v2.desc, false));
        }
    }
    
    public abstract AbstractInsnNode clone(final Map<LabelNode, LabelNode> p0);
    
    static LabelNode clone(final LabelNode a1, final Map<LabelNode, LabelNode> a2) {
        /*SL:274*/return a2.get(a1);
    }
    
    static LabelNode[] clone(final List<LabelNode> a2, final Map<LabelNode, LabelNode> v1) {
        final LabelNode[] v2 = /*EL:288*/new LabelNode[a2.size()];
        /*SL:289*/for (int a3 = 0; a3 < v2.length; ++a3) {
            /*SL:290*/v2[a3] = v1.get(a2.get(a3));
        }
        /*SL:292*/return v2;
    }
    
    protected final AbstractInsnNode cloneAnnotations(final AbstractInsnNode v-2) {
        /*SL:304*/if (v-2.visibleTypeAnnotations != null) {
            /*SL:305*/this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            /*SL:306*/for (int i = 0; i < v-2.visibleTypeAnnotations.size(); ++i) {
                final TypeAnnotationNode a1 = /*EL:307*/v-2.visibleTypeAnnotations.get(i);
                final TypeAnnotationNode v1 = /*EL:308*/new TypeAnnotationNode(a1.typeRef, a1.typePath, a1.desc);
                /*SL:310*/a1.accept(v1);
                /*SL:311*/this.visibleTypeAnnotations.add(v1);
            }
        }
        /*SL:314*/if (v-2.invisibleTypeAnnotations != null) {
            /*SL:315*/this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            /*SL:316*/for (int i = 0; i < v-2.invisibleTypeAnnotations.size(); ++i) {
                final TypeAnnotationNode v2 = /*EL:317*/v-2.invisibleTypeAnnotations.get(i);
                final TypeAnnotationNode v1 = /*EL:318*/new TypeAnnotationNode(v2.typeRef, v2.typePath, v2.desc);
                /*SL:320*/v2.accept(v1);
                /*SL:321*/this.invisibleTypeAnnotations.add(v1);
            }
        }
        /*SL:324*/return this;
    }
}
