package org.spongepowered.asm.mixin.injection.code;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;

class ReadOnlyInsnList extends InsnList
{
    private InsnList insnList;
    
    public ReadOnlyInsnList(final InsnList a1) {
        this.insnList = a1;
    }
    
    void dispose() {
        /*SL:46*/this.insnList = null;
    }
    
    @Override
    public final void set(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:58*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final AbstractInsnNode a1) {
        /*SL:69*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final InsnList a1) {
        /*SL:79*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode a1) {
        /*SL:90*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final InsnList a1) {
        /*SL:101*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:113*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:125*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:137*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:149*/throw new UnsupportedOperationException();
    }
    
    @Override
    public final void remove(final AbstractInsnNode a1) {
        /*SL:160*/throw new UnsupportedOperationException();
    }
    
    @Override
    public AbstractInsnNode[] toArray() {
        /*SL:170*/return this.insnList.toArray();
    }
    
    @Override
    public int size() {
        /*SL:180*/return this.insnList.size();
    }
    
    @Override
    public AbstractInsnNode getFirst() {
        /*SL:190*/return this.insnList.getFirst();
    }
    
    @Override
    public AbstractInsnNode getLast() {
        /*SL:200*/return this.insnList.getLast();
    }
    
    @Override
    public AbstractInsnNode get(final int a1) {
        /*SL:210*/return this.insnList.get(a1);
    }
    
    @Override
    public boolean contains(final AbstractInsnNode a1) {
        /*SL:221*/return this.insnList.contains(a1);
    }
    
    @Override
    public int indexOf(final AbstractInsnNode a1) {
        /*SL:232*/return this.insnList.indexOf(a1);
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator() {
        /*SL:242*/return this.insnList.iterator();
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator(final int a1) {
        /*SL:252*/return this.insnList.iterator(a1);
    }
    
    @Override
    public final void resetLabels() {
        /*SL:262*/this.insnList.resetLabels();
    }
}
