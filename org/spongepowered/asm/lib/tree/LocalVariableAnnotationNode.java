package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.TypePath;
import java.util.List;

public class LocalVariableAnnotationNode extends TypeAnnotationNode
{
    public List<LabelNode> start;
    public List<LabelNode> end;
    public List<Integer> index;
    
    public LocalVariableAnnotationNode(final int a1, final TypePath a2, final LabelNode[] a3, final LabelNode[] a4, final int[] a5, final String a6) {
        this(327680, a1, a2, a3, a4, a5, a6);
    }
    
    public LocalVariableAnnotationNode(final int a3, final int a4, final TypePath a5, final LabelNode[] a6, final LabelNode[] a7, final int[] v1, final String v2) {
        super(a3, a4, a5, v2);
        (this.start = new ArrayList<LabelNode>(a6.length)).addAll(Arrays.<LabelNode>asList(a6));
        (this.end = new ArrayList<LabelNode>(a7.length)).addAll(Arrays.<LabelNode>asList(a7));
        this.index = new ArrayList<Integer>(v1.length);
        for (final int a8 : v1) {
            this.index.add(a8);
        }
    }
    
    public void accept(final MethodVisitor v1, final boolean v2) {
        final Label[] v3 = /*EL:146*/new Label[this.start.size()];
        final Label[] v4 = /*EL:147*/new Label[this.end.size()];
        final int[] v5 = /*EL:148*/new int[this.index.size()];
        /*SL:149*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:150*/v3[a1] = this.start.get(a1).getLabel();
            /*SL:151*/v4[a1] = this.end.get(a1).getLabel();
            /*SL:152*/v5[a1] = this.index.get(a1);
        }
        /*SL:154*/this.accept(v1.visitLocalVariableAnnotation(this.typeRef, this.typePath, v3, v4, v5, this.desc, true));
    }
}
