package org.spongepowered.asm.mixin.injection.modify;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.code.Injector;

public class ModifyVariableInjector extends Injector
{
    private final LocalVariableDiscriminator discriminator;
    
    public ModifyVariableInjector(final InjectionInfo a1, final LocalVariableDiscriminator a2) {
        super(a1);
        this.discriminator = a2;
    }
    
    @Override
    protected boolean findTargetNodes(final MethodNode a3, final InjectionPoint a4, final InsnList v1, final Collection<AbstractInsnNode> v2) {
        /*SL:107*/if (a4 instanceof ContextualInjectionPoint) {
            final Target a5 = /*EL:108*/this.info.getContext().getTargetMethod(a3);
            /*SL:109*/return ((ContextualInjectionPoint)a4).find(a5, v2);
        }
        /*SL:111*/return a4.find(a3.desc, v1, v2);
    }
    
    @Override
    protected void sanityCheck(final Target a1, final List<InjectionPoint> a2) {
        /*SL:121*/super.sanityCheck(a1, a2);
        /*SL:123*/if (a1.isStatic != this.isStatic) {
            /*SL:124*/throw new InvalidInjectionException(this.info, "'static' of variable modifier method does not match target in " + this);
        }
        final int v1 = /*EL:127*/this.discriminator.getOrdinal();
        /*SL:128*/if (v1 < -1) {
            /*SL:129*/throw new InvalidInjectionException(this.info, "Invalid ordinal " + v1 + " specified in " + this);
        }
        /*SL:132*/if (this.discriminator.getIndex() == 0 && !this.isStatic) {
            /*SL:133*/throw new InvalidInjectionException(this.info, "Invalid index 0 specified in non-static variable modifier " + this);
        }
    }
    
    @Override
    protected void inject(final Target v2, final InjectionNodes.InjectionNode v3) {
        /*SL:142*/if (v3.isReplaced()) {
            /*SL:143*/throw new InvalidInjectionException(this.info, "Variable modifier target for " + this + " was removed by another injector");
        }
        final Context v4 = /*EL:146*/new Context(this.returnType, this.discriminator.isArgsOnly(), v2, v3.getCurrentTarget());
        /*SL:148*/if (this.discriminator.printLVT()) {
            /*SL:149*/this.printLocals(v4);
        }
        final String v5 = /*EL:152*/Bytecode.getDescriptor(new Type[] { this.returnType }, this.returnType);
        /*SL:153*/if (!v5.equals(this.methodNode.desc)) {
            /*SL:154*/throw new InvalidInjectionException(this.info, "Variable modifier " + this + " has an invalid signature, expected " + v5 + " but found " + this.methodNode.desc);
        }
        try {
            final int a1 = /*EL:159*/this.discriminator.findLocal(v4);
            /*SL:160*/if (a1 > -1) {
                /*SL:161*/this.inject(v4, a1);
            }
        }
        catch (InvalidImplicitDiscriminatorException a2) {
            /*SL:164*/if (this.discriminator.printLVT()) {
                /*SL:165*/this.info.addCallbackInvocation(this.methodNode);
                /*SL:166*/return;
            }
            /*SL:168*/throw new InvalidInjectionException(this.info, "Implicit variable modifier injection failed in " + this, a2);
        }
        /*SL:171*/v2.insns.insertBefore(v4.node, v4.insns);
        /*SL:172*/v2.addToStack(this.isStatic ? 1 : 2);
    }
    
    private void printLocals(final Context a1) {
        final SignaturePrinter v1 = /*EL:179*/new SignaturePrinter(this.methodNode.name, this.returnType, this.methodArgs, new String[] { "var" });
        /*SL:180*/v1.setModifiers(this.methodNode);
        /*SL:182*/new PrettyPrinter().kvWidth(20).kv(/*EL:183*/"Target Class", (Object)this.classNode.name.replace('/', '.')).kv(/*EL:184*/"Target Method", (Object)a1.target.method.name).kv(/*EL:185*/"Callback Name", (Object)this.methodNode.name).kv(/*EL:186*/"Capture Type", /*EL:187*/(Object)SignaturePrinter.getTypeName(this.returnType, false)).kv("Instruction", "%s %s", a1.node.getClass().getSimpleName(), /*EL:188*/Bytecode.getOpcodeName(a1.node.getOpcode())).hr().kv("Match mode", (Object)(this.discriminator.isImplicit(a1) ? /*EL:189*/"IMPLICIT (match single)" : "EXPLICIT (match by criteria)")).kv("Match ordinal", (this.discriminator.getOrdinal() < /*EL:190*/0) ? "any" : this.discriminator.getOrdinal()).kv("Match index", (this.discriminator.getIndex() < /*EL:191*/a1.baseArgIndex) ? "any" : this.discriminator.getIndex()).kv("Match name(s)", this.discriminator.hasNames() ? /*EL:192*/this.discriminator.getNames() : "any").kv("Args only", this.discriminator.isArgsOnly()).hr().add(/*EL:193*/a1).print(System.err);
    }
    
    private void inject(final Context a1, final int a2) {
        /*SL:205*/if (!this.isStatic) {
            /*SL:206*/a1.insns.add(new VarInsnNode(25, 0));
        }
        /*SL:209*/a1.insns.add(new VarInsnNode(this.returnType.getOpcode(21), a2));
        /*SL:210*/this.invokeHandler(a1.insns);
        /*SL:211*/a1.insns.add(new VarInsnNode(this.returnType.getOpcode(54), a2));
    }
    
    static class Context extends LocalVariableDiscriminator.Context
    {
        final InsnList insns;
        
        public Context(final Type a1, final boolean a2, final Target a3, final AbstractInsnNode a4) {
            super(a1, a2, a3, a4);
            this.insns = new InsnList();
        }
    }
    
    abstract static class ContextualInjectionPoint extends InjectionPoint
    {
        protected final IMixinContext context;
        
        ContextualInjectionPoint(final IMixinContext a1) {
            this.context = a1;
        }
        
        @Override
        public boolean find(final String a1, final InsnList a2, final Collection<AbstractInsnNode> a3) {
            /*SL:84*/throw new InvalidInjectionException(this.context, this.getAtCode() + " injection point must be used in conjunction with @ModifyVariable");
        }
        
        abstract boolean find(final Target p0, final Collection<AbstractInsnNode> p1);
    }
}
