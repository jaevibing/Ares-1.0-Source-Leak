package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.mixin.injection.code.Injector;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.util.Locals;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.mixin.injection.invoke.util.InsnFinder;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class ModifyConstantInjector extends RedirectInjector
{
    private static final int OPCODE_OFFSET = 6;
    
    public ModifyConstantInjector(final InjectionInfo a1) {
        super(a1, "@ModifyConstant");
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        /*SL:68*/if (!this.preInject(a2)) {
            /*SL:69*/return;
        }
        /*SL:72*/if (a2.isReplaced()) {
            /*SL:73*/throw new UnsupportedOperationException("Target failure for " + this.info);
        }
        final AbstractInsnNode v1 = /*EL:76*/a2.getCurrentTarget();
        /*SL:77*/if (v1 instanceof JumpInsnNode) {
            /*SL:78*/this.checkTargetModifiers(a1, false);
            /*SL:79*/this.injectExpandedConstantModifier(a1, (JumpInsnNode)v1);
            /*SL:80*/return;
        }
        /*SL:83*/if (Bytecode.isConstant(v1)) {
            /*SL:84*/this.checkTargetModifiers(a1, false);
            /*SL:85*/this.injectConstantModifier(a1, v1);
            /*SL:86*/return;
        }
        /*SL:89*/throw new InvalidInjectionException(this.info, this.annotationType + " annotation is targetting an invalid insn in " + a1 + " in " + this);
    }
    
    private void injectExpandedConstantModifier(final Target a1, final JumpInsnNode a2) {
        final int v1 = /*EL:100*/a2.getOpcode();
        /*SL:101*/if (v1 < 155 || v1 > 158) {
            /*SL:102*/throw new InvalidInjectionException(this.info, this.annotationType + " annotation selected an invalid opcode " + /*EL:103*/Bytecode.getOpcodeName(v1) + " in " + a1 + " in " + this);
        }
        final InsnList v2 = /*EL:106*/new InsnList();
        /*SL:107*/v2.add(new InsnNode(3));
        final AbstractInsnNode v3 = /*EL:108*/this.invokeConstantHandler(Type.getType("I"), a1, v2, v2);
        /*SL:109*/v2.add(new JumpInsnNode(v1 + 6, a2.label));
        /*SL:110*/a1.replaceNode(a2, v3, v2);
        /*SL:111*/a1.addToStack(1);
    }
    
    private void injectConstantModifier(final Target a1, final AbstractInsnNode a2) {
        final Type v1 = /*EL:115*/Bytecode.getConstantType(a2);
        /*SL:117*/if (v1.getSort() <= 5 && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            /*SL:118*/this.checkNarrowing(a1, a2, v1);
        }
        final InsnList v2 = /*EL:121*/new InsnList();
        final InsnList v3 = /*EL:122*/new InsnList();
        final AbstractInsnNode v4 = /*EL:123*/this.invokeConstantHandler(v1, a1, v2, v3);
        /*SL:124*/a1.wrapNode(a2, v4, v2, v3);
    }
    
    private AbstractInsnNode invokeConstantHandler(final Type a1, final Target a2, final InsnList a3, final InsnList a4) {
        final String v1 = /*EL:128*/Bytecode.generateDescriptor(a1, a1);
        final boolean v2 = /*EL:129*/this.checkDescriptor(v1, a2, "getter");
        /*SL:131*/if (!this.isStatic) {
            /*SL:132*/a3.insert(new VarInsnNode(25, 0));
            /*SL:133*/a2.addToStack(1);
        }
        /*SL:136*/if (v2) {
            /*SL:137*/this.pushArgs(a2.arguments, a4, a2.getArgIndices(), 0, a2.arguments.length);
            /*SL:138*/a2.addToStack(Bytecode.getArgsSize(a2.arguments));
        }
        /*SL:141*/return this.invokeHandler(a4);
    }
    
    private void checkNarrowing(final Target v-6, final AbstractInsnNode v-5, final Type v-4) {
        final AbstractInsnNode popInsn = /*EL:145*/new InsnFinder().findPopInsn(v-6, v-5);
        /*SL:147*/if (popInsn == null) {
            /*SL:148*/return;
        }
        /*SL:149*/if (popInsn instanceof FieldInsnNode) {
            final FieldInsnNode a1 = /*EL:150*/(FieldInsnNode)popInsn;
            final Type a2 = /*EL:151*/Type.getType(a1.desc);
            /*SL:152*/this.checkNarrowing(v-6, v-5, v-4, a2, v-6.indexOf(popInsn), String.format("%s %s %s.%s", /*EL:153*/Bytecode.getOpcodeName(popInsn), SignaturePrinter.getTypeName(a2, false), a1.owner.replace('/', '.'), a1.name));
        }
        else/*SL:154*/ if (popInsn.getOpcode() == 172) {
            /*SL:155*/this.checkNarrowing(v-6, v-5, v-4, v-6.returnType, v-6.indexOf(popInsn), "RETURN " + /*EL:156*/SignaturePrinter.getTypeName(v-6.returnType, false));
        }
        else/*SL:157*/ if (popInsn.getOpcode() == 54) {
            final int var = /*EL:158*/((VarInsnNode)popInsn).var;
            final LocalVariableNode localVariable = /*EL:159*/Locals.getLocalVariableAt(v-6.classNode, v-6.method, popInsn, var);
            /*SL:163*/if (localVariable != null && localVariable.desc != null) {
                final String a3 = /*EL:164*/(localVariable.name != null) ? localVariable.name : "unnamed";
                final Type v1 = /*EL:165*/Type.getType(localVariable.desc);
                /*SL:166*/this.checkNarrowing(v-6, v-5, v-4, v1, v-6.indexOf(popInsn), String.format("ISTORE[var=%d] %s %s", var, /*EL:167*/SignaturePrinter.getTypeName(v1, false), a3));
            }
        }
    }
    
    private void checkNarrowing(final Target a6, final AbstractInsnNode v1, final Type v2, final Type v3, final int v4, final String v5) {
        final int v6 = /*EL:173*/v2.getSort();
        final int v7 = /*EL:174*/v3.getSort();
        /*SL:175*/if (v7 < v6) {
            final String a7 = /*EL:176*/SignaturePrinter.getTypeName(v2, false);
            final String a8 = /*EL:177*/SignaturePrinter.getTypeName(v3, false);
            final String a9 = /*EL:178*/(v7 == 1) ? ". Implicit conversion to <boolean> can cause nondeterministic (JVM-specific) behaviour!" : "";
            final Level a10 = /*EL:179*/(v7 == 1) ? Level.ERROR : Level.WARN;
            Injector.logger.log(/*EL:180*/a10, "Narrowing conversion of <{}> to <{}> in {} target {} at opcode {} ({}){}", new Object[] { a7, a8, this.info, a6, v4, /*EL:181*/v5, a9 });
        }
    }
}
