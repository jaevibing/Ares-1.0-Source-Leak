package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Iterator;
import java.util.List;

public class TryCatchBlockNode
{
    public LabelNode start;
    public LabelNode end;
    public LabelNode handler;
    public String type;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    
    public TryCatchBlockNode(final LabelNode a1, final LabelNode a2, final LabelNode a3, final String a4) {
        this.start = a1;
        this.end = a2;
        this.handler = a3;
        this.type = a4;
    }
    
    public void updateIndex(final int v-2) {
        final int n = /*EL:116*/0x42000000 | v-2 << 8;
        /*SL:117*/if (this.visibleTypeAnnotations != null) {
            /*SL:118*/for (final TypeAnnotationNode a1 : this.visibleTypeAnnotations) {
                /*SL:119*/a1.typeRef = n;
            }
        }
        /*SL:122*/if (this.invisibleTypeAnnotations != null) {
            /*SL:123*/for (final TypeAnnotationNode v1 : this.invisibleTypeAnnotations) {
                /*SL:124*/v1.typeRef = n;
            }
        }
    }
    
    public void accept(final MethodVisitor v-1) {
        /*SL:136*/v-1.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), (this.handler == null) ? null : this.handler.getLabel(), /*EL:137*/this.type);
        /*SL:140*/for (int v0 = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), v = 0; v < v0; ++v) {
            final TypeAnnotationNode a1 = /*EL:141*/this.visibleTypeAnnotations.get(v);
            /*SL:142*/a1.accept(v-1.visitTryCatchAnnotation(a1.typeRef, a1.typePath, a1.desc, true));
        }
        /*SL:147*/for (int v0 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), v = 0; v < v0; ++v) {
            final TypeAnnotationNode v2 = /*EL:148*/this.invisibleTypeAnnotations.get(v);
            /*SL:149*/v2.accept(v-1.visitTryCatchAnnotation(v2.typeRef, v2.typePath, v2.desc, false));
        }
    }
}
