package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodNode;

public class AccessorGeneratorMethodProxy extends AccessorGenerator
{
    private final MethodNode targetMethod;
    private final Type[] argTypes;
    private final Type returnType;
    private final boolean isInstanceMethod;
    
    public AccessorGeneratorMethodProxy(final AccessorInfo a1) {
        super(a1);
        this.targetMethod = a1.getTargetMethod();
        this.argTypes = a1.getArgTypes();
        this.returnType = a1.getReturnType();
        this.isInstanceMethod = !Bytecode.hasFlag(this.targetMethod, 8);
    }
    
    @Override
    public MethodNode generate() {
        final int v1 = /*EL:70*/Bytecode.getArgsSize(this.argTypes) + this.returnType.getSize() + (this.isInstanceMethod ? 1 : 0);
        final MethodNode v2 = /*EL:71*/this.createMethod(v1, v1);
        /*SL:72*/if (this.isInstanceMethod) {
            /*SL:73*/v2.instructions.add(new VarInsnNode(25, 0));
        }
        /*SL:75*/Bytecode.loadArgs(this.argTypes, v2.instructions, this.isInstanceMethod ? 1 : 0);
        final boolean v3 = /*EL:76*/Bytecode.hasFlag(this.targetMethod, 2);
        final int v4 = /*EL:77*/this.isInstanceMethod ? (v3 ? 183 : 182) : 184;
        /*SL:78*/v2.instructions.add(new MethodInsnNode(v4, this.info.getClassNode().name, this.targetMethod.name, this.targetMethod.desc, false));
        /*SL:79*/v2.instructions.add(new InsnNode(this.returnType.getOpcode(172)));
        /*SL:80*/return v2;
    }
}
