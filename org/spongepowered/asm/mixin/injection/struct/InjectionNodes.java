package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.util.Bytecode;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.ArrayList;

public class InjectionNodes extends ArrayList<InjectionNode>
{
    private static final long serialVersionUID = 1L;
    
    public InjectionNode add(final AbstractInsnNode a1) {
        InjectionNode v1 = /*EL:220*/this.get(a1);
        /*SL:221*/if (v1 == null) {
            /*SL:222*/v1 = new InjectionNode(a1);
            /*SL:223*/this.add(v1);
        }
        /*SL:225*/return v1;
    }
    
    public InjectionNode get(final AbstractInsnNode v2) {
        /*SL:236*/for (final InjectionNode a1 : this) {
            /*SL:237*/if (a1.matches(v2)) {
                /*SL:238*/return a1;
            }
        }
        /*SL:241*/return null;
    }
    
    public boolean contains(final AbstractInsnNode a1) {
        /*SL:251*/return this.get(a1) != null;
    }
    
    public void replace(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        final InjectionNode v1 = /*EL:262*/this.get(a1);
        /*SL:263*/if (v1 != null) {
            /*SL:264*/v1.replace(a2);
        }
    }
    
    public void remove(final AbstractInsnNode a1) {
        final InjectionNode v1 = /*EL:275*/this.get(a1);
        /*SL:276*/if (v1 != null) {
            /*SL:277*/v1.remove();
        }
    }
    
    public static class InjectionNode implements Comparable<InjectionNode>
    {
        private static int nextId;
        private final int id;
        private final AbstractInsnNode originalTarget;
        private AbstractInsnNode currentTarget;
        private Map<String, Object> decorations;
        
        public InjectionNode(final AbstractInsnNode a1) {
            this.originalTarget = a1;
            this.currentTarget = a1;
            /*SL:279*/this.id = InjectionNode.nextId++;
        }
        
        public int getId() {
            /*SL:96*/return this.id;
        }
        
        public AbstractInsnNode getOriginalTarget() {
            /*SL:103*/return this.originalTarget;
        }
        
        public AbstractInsnNode getCurrentTarget() {
            /*SL:111*/return this.currentTarget;
        }
        
        public InjectionNode replace(final AbstractInsnNode a1) {
            /*SL:120*/this.currentTarget = a1;
            /*SL:121*/return this;
        }
        
        public InjectionNode remove() {
            /*SL:128*/this.currentTarget = null;
            /*SL:129*/return this;
        }
        
        public boolean matches(final AbstractInsnNode a1) {
            /*SL:141*/return this.originalTarget == a1 || this.currentTarget == a1;
        }
        
        public boolean isReplaced() {
            /*SL:148*/return this.originalTarget != this.currentTarget;
        }
        
        public boolean isRemoved() {
            /*SL:155*/return this.currentTarget == null;
        }
        
        public <V> InjectionNode decorate(final String a1, final V a2) {
            /*SL:166*/if (this.decorations == null) {
                /*SL:167*/this.decorations = new HashMap<String, Object>();
            }
            /*SL:169*/this.decorations.put(a1, a2);
            /*SL:170*/return this;
        }
        
        public boolean hasDecoration(final String a1) {
            /*SL:180*/return this.decorations != null && this.decorations.get(a1) != null;
        }
        
        public <V> V getDecoration(final String a1) {
            /*SL:192*/return (V)((this.decorations == null) ? null : this.decorations.get(a1));
        }
        
        @Override
        public int compareTo(final InjectionNode a1) {
            /*SL:200*/return (a1 == null) ? Integer.MAX_VALUE : (this.hashCode() - a1.hashCode());
        }
        
        @Override
        public String toString() {
            /*SL:208*/return String.format("InjectionNode[%s]", Bytecode.describeNode(this.currentTarget).replaceAll("\\s+", " "));
        }
        
        static {
            InjectionNode.nextId = 0;
        }
    }
}
