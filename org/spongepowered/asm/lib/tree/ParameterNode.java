package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;

public class ParameterNode
{
    public String name;
    public int access;
    
    public ParameterNode(final String a1, final int a2) {
        this.name = a1;
        this.access = a2;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:74*/a1.visitParameter(this.name, this.access);
    }
}
