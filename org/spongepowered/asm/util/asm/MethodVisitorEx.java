package org.spongepowered.asm.util.asm;

import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodVisitorEx extends MethodVisitor
{
    public MethodVisitorEx(final MethodVisitor a1) {
        super(327680, a1);
    }
    
    public void visitConstant(final byte a1) {
        /*SL:47*/if (a1 > -2 && a1 < 6) {
            /*SL:48*/this.visitInsn(Bytecode.CONSTANTS_INT[a1 + 1]);
            /*SL:49*/return;
        }
        /*SL:51*/this.visitIntInsn(16, a1);
    }
}
