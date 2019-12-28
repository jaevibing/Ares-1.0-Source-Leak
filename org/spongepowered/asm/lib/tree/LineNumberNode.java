package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class LineNumberNode extends AbstractInsnNode
{
    public int line;
    public LabelNode start;
    
    public LineNumberNode(final int a1, final LabelNode a2) {
        super(-1);
        this.line = a1;
        this.start = a2;
    }
    
    public int getType() {
        /*SL:72*/return 15;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:77*/a1.visitLineNumber(this.line, this.start.getLabel());
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:82*/return new LineNumberNode(this.line, AbstractInsnNode.clone(this.start, a1));
    }
}
