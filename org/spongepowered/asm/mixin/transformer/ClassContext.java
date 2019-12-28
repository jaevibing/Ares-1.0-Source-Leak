package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.struct.MemberRef;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.HashSet;
import java.util.Set;

abstract class ClassContext
{
    private final Set<ClassInfo.Method> upgradedMethods;
    
    ClassContext() {
        this.upgradedMethods = new HashSet<ClassInfo.Method>();
    }
    
    abstract String getClassRef();
    
    abstract ClassNode getClassNode();
    
    abstract ClassInfo getClassInfo();
    
    void addUpgradedMethod(final MethodNode a1) {
        final ClassInfo.Method v1 = /*EL:80*/this.getClassInfo().findMethod(a1);
        /*SL:81*/if (v1 == null) {
            /*SL:83*/throw new IllegalStateException("Meta method for " + a1.name + " not located in " + this);
        }
        /*SL:85*/this.upgradedMethods.add(v1);
    }
    
    protected void upgradeMethods() {
        /*SL:89*/for (final MethodNode v1 : this.getClassNode().methods) {
            /*SL:90*/this.upgradeMethod(v1);
        }
    }
    
    private void upgradeMethod(final MethodNode v-1) {
        /*SL:95*/for (final AbstractInsnNode v : v-1.instructions) {
            /*SL:97*/if (!(v instanceof MethodInsnNode)) {
                /*SL:98*/continue;
            }
            final MemberRef v2 = /*EL:101*/new MemberRef.Method((MethodInsnNode)v);
            /*SL:102*/if (!v2.getOwner().equals(this.getClassRef())) {
                continue;
            }
            final ClassInfo.Method a1 = /*EL:103*/this.getClassInfo().findMethod(v2.getName(), v2.getDesc(), 10);
            /*SL:104*/this.upgradeMethodRef(v-1, v2, a1);
        }
    }
    
    protected void upgradeMethodRef(final MethodNode a1, final MemberRef a2, final ClassInfo.Method a3) {
        /*SL:110*/if (a2.getOpcode() != 183) {
            /*SL:111*/return;
        }
        /*SL:114*/if (this.upgradedMethods.contains(a3)) {
            /*SL:115*/a2.setOpcode(182);
        }
    }
}
