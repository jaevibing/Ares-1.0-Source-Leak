package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;

public class ModifyArgsInjector extends InvokeInjector
{
    private final ArgsClassGenerator argsClassGenerator;
    
    public ModifyArgsInjector(final InjectionInfo a1) {
        super(a1, "@ModifyArgs");
        this.argsClassGenerator = (ArgsClassGenerator)a1.getContext().getExtensions().getGenerator((Class)ArgsClassGenerator.class);
    }
    
    @Override
    protected void checkTarget(final Target a1) {
        /*SL:63*/this.checkTargetModifiers(a1, false);
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        /*SL:68*/this.checkTargetForNode(a1, a2);
        /*SL:69*/super.inject(a1, a2);
    }
    
    @Override
    protected void injectAtInvoke(final Target a1, final InjectionNodes.InjectionNode a2) {
        final MethodInsnNode v1 = /*EL:77*/(MethodInsnNode)a2.getCurrentTarget();
        final Type[] v2 = /*EL:79*/Type.getArgumentTypes(v1.desc);
        /*SL:80*/if (v2.length == 0) {
            /*SL:81*/throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " targets a method invocation " + v1.name + v1.desc + " with no arguments!");
        }
        final String v3 = /*EL:85*/this.argsClassGenerator.getClassRef(v1.desc);
        final boolean v4 = /*EL:86*/this.verifyTarget(a1);
        final InsnList v5 = /*EL:88*/new InsnList();
        /*SL:89*/a1.addToStack(1);
        /*SL:91*/this.packArgs(v5, v3, v1);
        /*SL:93*/if (v4) {
            /*SL:94*/a1.addToStack(Bytecode.getArgsSize(a1.arguments));
            /*SL:95*/Bytecode.loadArgs(a1.arguments, v5, a1.isStatic ? 0 : 1);
        }
        /*SL:98*/this.invokeHandler(v5);
        /*SL:99*/this.unpackArgs(v5, v3, v2);
        /*SL:101*/a1.insns.insertBefore(v1, v5);
    }
    
    private boolean verifyTarget(final Target v-2) {
        final String format = /*EL:105*/String.format("(L%s;)V", ArgsClassGenerator.ARGS_REF);
        /*SL:106*/if (this.methodNode.desc.equals(format)) {
            /*SL:117*/return false;
        }
        final String a1 = Bytecode.changeDescriptorReturnType(v-2.method.desc, "V");
        final String v1 = String.format("(L%s;%s", ArgsClassGenerator.ARGS_REF, a1.substring(1));
        if (this.methodNode.desc.equals(v1)) {
            return true;
        }
        throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " has an invalid signature " + this.methodNode.desc + ", expected " + format + " or " + v1);
    }
    
    private void packArgs(final InsnList a1, final String a2, final MethodInsnNode a3) {
        final String v1 = /*EL:121*/Bytecode.changeDescriptorReturnType(a3.desc, "L" + a2 + ";");
        /*SL:122*/a1.add(new MethodInsnNode(184, a2, "of", v1, false));
        /*SL:123*/a1.add(new InsnNode(89));
        /*SL:125*/if (!this.isStatic) {
            /*SL:126*/a1.add(new VarInsnNode(25, 0));
            /*SL:127*/a1.add(new InsnNode(95));
        }
    }
    
    private void unpackArgs(final InsnList a3, final String v1, final Type[] v2) {
        /*SL:132*/for (int a4 = 0; a4 < v2.length; ++a4) {
            /*SL:133*/if (a4 < v2.length - 1) {
                /*SL:134*/a3.add(new InsnNode(89));
            }
            /*SL:136*/a3.add(new MethodInsnNode(182, v1, "$" + a4, "()" + v2[a4].getDescriptor(), false));
            /*SL:137*/if (a4 < v2.length - 1) {
                /*SL:138*/if (v2[a4].getSize() == 1) {
                    /*SL:139*/a3.add(new InsnNode(95));
                }
                else {
                    /*SL:141*/a3.add(new InsnNode(93));
                    /*SL:142*/a3.add(new InsnNode(88));
                }
            }
        }
    }
}
