package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Set;

public class SourceValue implements Value
{
    public final int size;
    public final Set<AbstractInsnNode> insns;
    
    public SourceValue(final int a1) {
        this(a1, SmallSet.<AbstractInsnNode>emptySet());
    }
    
    public SourceValue(final int a1, final AbstractInsnNode a2) {
        this.size = a1;
        this.insns = new SmallSet<AbstractInsnNode>(a2, null);
    }
    
    public SourceValue(final int a1, final Set<AbstractInsnNode> a2) {
        this.size = a1;
        this.insns = a2;
    }
    
    public int getSize() {
        /*SL:81*/return this.size;
    }
    
    public boolean equals(final Object a1) {
        /*SL:86*/if (!(a1 instanceof SourceValue)) {
            /*SL:87*/return false;
        }
        final SourceValue v1 = /*EL:89*/(SourceValue)a1;
        /*SL:90*/return this.size == v1.size && this.insns.equals(v1.insns);
    }
    
    public int hashCode() {
        /*SL:95*/return this.insns.hashCode();
    }
}
