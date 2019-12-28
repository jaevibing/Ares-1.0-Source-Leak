package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.code.Injector;

public abstract class InvokeInjector extends Injector
{
    protected final String annotationType;
    
    public InvokeInjector(final InjectionInfo a1, final String a2) {
        super(a1);
        this.annotationType = a2;
    }
    
    @Override
    protected void sanityCheck(final Target a1, final List<InjectionPoint> a2) {
        /*SL:65*/super.sanityCheck(a1, a2);
        /*SL:66*/this.checkTarget(a1);
    }
    
    protected void checkTarget(final Target a1) {
        /*SL:75*/this.checkTargetModifiers(a1, true);
    }
    
    protected final void checkTargetModifiers(final Target a1, final boolean a2) {
        /*SL:87*/if (a2 && a1.isStatic != this.isStatic) {
            /*SL:88*/throw new InvalidInjectionException(this.info, "'static' modifier of handler method does not match target in " + this);
        }
        /*SL:89*/if (!a2 && !this.isStatic && a1.isStatic) {
            /*SL:90*/throw new InvalidInjectionException(this.info, "non-static callback method " + this + " targets a static method which is not supported");
        }
    }
    
    protected void checkTargetForNode(final Target v-3, final InjectionNodes.InjectionNode v-2) {
        /*SL:106*/if (v-3.isCtor) {
            final MethodInsnNode a1 = /*EL:107*/v-3.findSuperInitNode();
            final int a2 = /*EL:108*/v-3.indexOf(a1);
            final int v1 = /*EL:109*/v-3.indexOf(v-2.getCurrentTarget());
            /*SL:110*/if (v1 <= a2) {
                /*SL:111*/if (!this.isStatic) {
                    /*SL:112*/throw new InvalidInjectionException(this.info, "Pre-super " + this.annotationType + " invocation must be static in " + this);
                }
                /*SL:114*/return;
            }
        }
        /*SL:117*/this.checkTargetModifiers(v-3, true);
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        /*SL:127*/if (!(a2.getCurrentTarget() instanceof MethodInsnNode)) {
            /*SL:128*/throw new InvalidInjectionException(this.info, this.annotationType + " annotation on is targetting a non-method insn in " + a1 + " in " + this);
        }
        /*SL:132*/this.injectAtInvoke(a1, a2);
    }
    
    protected abstract void injectAtInvoke(final Target p0, final InjectionNodes.InjectionNode p1);
    
    protected AbstractInsnNode invokeHandlerWithArgs(final Type[] a1, final InsnList a2, final int[] a3) {
        /*SL:150*/return this.invokeHandlerWithArgs(a1, a2, a3, 0, a1.length);
    }
    
    protected AbstractInsnNode invokeHandlerWithArgs(final Type[] a1, final InsnList a2, final int[] a3, final int a4, final int a5) {
        /*SL:162*/if (!this.isStatic) {
            /*SL:163*/a2.add(new VarInsnNode(25, 0));
        }
        /*SL:165*/this.pushArgs(a1, a2, a3, a4, a5);
        /*SL:166*/return this.invokeHandler(a2);
    }
    
    protected int[] storeArgs(final Target a1, final Type[] a2, final InsnList a3, final int a4) {
        final int[] v1 = /*EL:180*/a1.generateArgMap(a2, a4);
        /*SL:181*/this.storeArgs(a2, a3, v1, a4, a2.length);
        /*SL:182*/return v1;
    }
    
    protected void storeArgs(final Type[] a3, final InsnList a4, final int[] a5, final int v1, final int v2) {
        /*SL:195*/for (int a6 = v2 - 1; a6 >= v1; --a6) {
            /*SL:196*/a4.add(new VarInsnNode(a3[a6].getOpcode(54), a5[a6]));
        }
    }
    
    protected void pushArgs(final Type[] a3, final InsnList a4, final int[] a5, final int v1, final int v2) {
        /*SL:209*/for (int a6 = v1; a6 < v2; ++a6) {
            /*SL:210*/a4.add(new VarInsnNode(a3[a6].getOpcode(21), a5[a6]));
        }
    }
}
