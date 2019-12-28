package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.util.Bytecode;
import java.util.Arrays;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class ModifyArgInjector extends InvokeInjector
{
    private final int index;
    private final boolean singleArgMode;
    
    public ModifyArgInjector(final InjectionInfo a1, final int a2) {
        super(a1, "@ModifyArg");
        this.index = a2;
        this.singleArgMode = (this.methodArgs.length == 1);
    }
    
    @Override
    protected void sanityCheck(final Target a1, final List<InjectionPoint> a2) {
        /*SL:76*/super.sanityCheck(a1, a2);
        /*SL:78*/if (this.singleArgMode && /*EL:79*/!this.methodArgs[0].equals(this.returnType)) {
            /*SL:80*/throw new InvalidInjectionException(this.info, "@ModifyArg return type on " + this + " must match the parameter type. ARG=" + this.methodArgs[0] + " RETURN=" + this.returnType);
        }
    }
    
    @Override
    protected void checkTarget(final Target a1) {
        /*SL:92*/if (!this.isStatic && a1.isStatic) {
            /*SL:93*/throw new InvalidInjectionException(this.info, "non-static callback method " + this + " targets a static method which is not supported");
        }
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        /*SL:99*/this.checkTargetForNode(a1, a2);
        /*SL:100*/super.inject(a1, a2);
    }
    
    @Override
    protected void injectAtInvoke(final Target a1, final InjectionNodes.InjectionNode a2) {
        final MethodInsnNode v1 = /*EL:108*/(MethodInsnNode)a2.getCurrentTarget();
        final Type[] v2 = /*EL:109*/Type.getArgumentTypes(v1.desc);
        final int v3 = /*EL:110*/this.findArgIndex(a1, v2);
        final InsnList v4 = /*EL:111*/new InsnList();
        int v5 = /*EL:112*/0;
        /*SL:114*/if (this.singleArgMode) {
            /*SL:115*/v5 = this.injectSingleArgHandler(a1, v2, v3, v4);
        }
        else {
            /*SL:117*/v5 = this.injectMultiArgHandler(a1, v2, v3, v4);
        }
        /*SL:120*/a1.insns.insertBefore(v1, v4);
        /*SL:121*/a1.addToLocals(v5);
        /*SL:122*/a1.addToStack(2 - (v5 - 1));
    }
    
    private int injectSingleArgHandler(final Target a1, final Type[] a2, final int a3, final InsnList a4) {
        final int[] v1 = /*EL:129*/this.storeArgs(a1, a2, a4, a3);
        /*SL:130*/this.invokeHandlerWithArgs(a2, a4, v1, a3, a3 + 1);
        /*SL:131*/this.pushArgs(a2, a4, v1, a3 + 1, a2.length);
        /*SL:132*/return v1[v1.length - 1] - a1.getMaxLocals() + a2[a2.length - 1].getSize();
    }
    
    private int injectMultiArgHandler(final Target a1, final Type[] a2, final int a3, final InsnList a4) {
        /*SL:139*/if (!Arrays.equals(a2, this.methodArgs)) {
            /*SL:140*/throw new InvalidInjectionException(this.info, "@ModifyArg method " + this + " targets a method with an invalid signature " + /*EL:141*/Bytecode.getDescriptor(a2) + ", expected " + Bytecode.getDescriptor(this.methodArgs));
        }
        final int[] v1 = /*EL:144*/this.storeArgs(a1, a2, a4, 0);
        /*SL:145*/this.pushArgs(a2, a4, v1, 0, a3);
        /*SL:146*/this.invokeHandlerWithArgs(a2, a4, v1, 0, a2.length);
        /*SL:147*/this.pushArgs(a2, a4, v1, a3 + 1, a2.length);
        /*SL:148*/return v1[v1.length - 1] - a1.getMaxLocals() + a2[a2.length - 1].getSize();
    }
    
    protected int findArgIndex(final Target v1, final Type[] v2) {
        /*SL:152*/if (this.index > -1) {
            /*SL:153*/if (this.index >= v2.length || !v2[this.index].equals(this.returnType)) {
                /*SL:154*/throw new InvalidInjectionException(this.info, "Specified index " + this.index + " for @ModifyArg is invalid for args " + /*EL:155*/Bytecode.getDescriptor(v2) + ", expected " + this.returnType + " on " + this);
            }
            /*SL:157*/return this.index;
        }
        else {
            int v3 = /*EL:160*/-1;
            /*SL:162*/for (int a1 = 0; a1 < v2.length; ++a1) {
                /*SL:163*/if (v2[a1].equals(this.returnType)) {
                    /*SL:167*/if (v3 != -1) {
                        /*SL:168*/throw new InvalidInjectionException(this.info, "Found duplicate args with index [" + v3 + ", " + a1 + "] matching type " + this.returnType + " for @ModifyArg target " + v1 + " in " + this + ". Please specify index of desired arg.");
                    }
                    /*SL:172*/v3 = a1;
                }
            }
            /*SL:175*/if (v3 == -1) {
                /*SL:176*/throw new InvalidInjectionException(this.info, "Could not find arg matching type " + this.returnType + " for @ModifyArg target " + v1 + " in " + this);
            }
            /*SL:180*/return v3;
        }
    }
}
