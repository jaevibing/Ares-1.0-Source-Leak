package org.spongepowered.asm.lib.tree;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import org.spongepowered.asm.lib.MethodVisitor;

public class InsnList
{
    private int size;
    private AbstractInsnNode first;
    private AbstractInsnNode last;
    AbstractInsnNode[] cache;
    
    public int size() {
        /*SL:70*/return this.size;
    }
    
    public AbstractInsnNode getFirst() {
        /*SL:80*/return this.first;
    }
    
    public AbstractInsnNode getLast() {
        /*SL:90*/return this.last;
    }
    
    public AbstractInsnNode get(final int a1) {
        /*SL:106*/if (a1 < 0 || a1 >= this.size) {
            /*SL:107*/throw new IndexOutOfBoundsException();
        }
        /*SL:109*/if (this.cache == null) {
            /*SL:110*/this.cache = this.toArray();
        }
        /*SL:112*/return this.cache[a1];
    }
    
    public boolean contains(final AbstractInsnNode a1) {
        AbstractInsnNode v1;
        /*SL:126*/for (v1 = this.first; v1 != null && v1 != a1; /*SL:127*/v1 = v1.next) {}
        /*SL:129*/return v1 != null;
    }
    
    public int indexOf(final AbstractInsnNode a1) {
        /*SL:147*/if (this.cache == null) {
            /*SL:148*/this.cache = this.toArray();
        }
        /*SL:150*/return a1.index;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:161*/for (AbstractInsnNode v1 = this.first; v1 != null; /*SL:163*/v1 = v1.next) {
            v1.accept(a1);
        }
    }
    
    public ListIterator<AbstractInsnNode> iterator() {
        /*SL:173*/return this.iterator(0);
    }
    
    public ListIterator<AbstractInsnNode> iterator(final int a1) {
        /*SL:186*/return (ListIterator<AbstractInsnNode>)new InsnListIterator(a1);
    }
    
    public AbstractInsnNode[] toArray() {
        int v1 = /*EL:195*/0;
        AbstractInsnNode v2 = /*EL:196*/this.first;
        final AbstractInsnNode[] v3 = /*EL:197*/new AbstractInsnNode[this.size];
        /*SL:198*/while (v2 != null) {
            /*SL:199*/v3[v1] = v2;
            /*SL:200*/v2.index = v1++;
            /*SL:201*/v2 = v2.next;
        }
        /*SL:203*/return v3;
    }
    
    public void set(final AbstractInsnNode v1, final AbstractInsnNode v2) {
        final AbstractInsnNode v3 = /*EL:216*/v1.next;
        /*SL:217*/v2.next = v3;
        /*SL:218*/if (v3 != null) {
            /*SL:219*/v3.prev = v2;
        }
        else {
            /*SL:221*/this.last = v2;
        }
        final AbstractInsnNode v4 = /*EL:223*/v1.prev;
        /*SL:224*/v2.prev = v4;
        /*SL:225*/if (v4 != null) {
            /*SL:226*/v4.next = v2;
        }
        else {
            /*SL:228*/this.first = v2;
        }
        /*SL:230*/if (this.cache != null) {
            final int a1 = /*EL:231*/v1.index;
            /*SL:232*/this.cache[a1] = v2;
            /*SL:233*/v2.index = a1;
        }
        else {
            /*SL:235*/v2.index = 0;
        }
        /*SL:237*/v1.index = -1;
        /*SL:238*/v1.prev = null;
        /*SL:239*/v1.next = null;
    }
    
    public void add(final AbstractInsnNode a1) {
        /*SL:250*/++this.size;
        /*SL:251*/if (this.last == null) {
            /*SL:252*/this.first = a1;
            /*SL:253*/this.last = a1;
        }
        else {
            /*SL:255*/this.last.next = a1;
            /*SL:256*/a1.prev = this.last;
        }
        /*SL:258*/this.last = a1;
        /*SL:259*/this.cache = null;
        /*SL:260*/a1.index = 0;
    }
    
    public void add(final InsnList v2) {
        /*SL:271*/if (v2.size == 0) {
            /*SL:272*/return;
        }
        /*SL:274*/this.size += v2.size;
        /*SL:275*/if (this.last == null) {
            /*SL:276*/this.first = v2.first;
            /*SL:277*/this.last = v2.last;
        }
        else {
            final AbstractInsnNode a1 = /*EL:279*/v2.first;
            /*SL:280*/this.last.next = a1;
            /*SL:281*/a1.prev = this.last;
            /*SL:282*/this.last = v2.last;
        }
        /*SL:284*/this.cache = null;
        /*SL:285*/v2.removeAll(false);
    }
    
    public void insert(final AbstractInsnNode a1) {
        /*SL:296*/++this.size;
        /*SL:297*/if (this.first == null) {
            /*SL:298*/this.first = a1;
            /*SL:299*/this.last = a1;
        }
        else {
            /*SL:301*/this.first.prev = a1;
            /*SL:302*/a1.next = this.first;
        }
        /*SL:304*/this.first = a1;
        /*SL:305*/this.cache = null;
        /*SL:306*/a1.index = 0;
    }
    
    public void insert(final InsnList v2) {
        /*SL:317*/if (v2.size == 0) {
            /*SL:318*/return;
        }
        /*SL:320*/this.size += v2.size;
        /*SL:321*/if (this.first == null) {
            /*SL:322*/this.first = v2.first;
            /*SL:323*/this.last = v2.last;
        }
        else {
            final AbstractInsnNode a1 = /*EL:325*/v2.last;
            /*SL:326*/this.first.prev = a1;
            /*SL:327*/a1.next = this.first;
            /*SL:328*/this.first = v2.first;
        }
        /*SL:330*/this.cache = null;
        /*SL:331*/v2.removeAll(false);
    }
    
    public void insert(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:346*/++this.size;
        final AbstractInsnNode v1 = /*EL:347*/a1.next;
        /*SL:348*/if (v1 == null) {
            /*SL:349*/this.last = a2;
        }
        else {
            /*SL:351*/v1.prev = a2;
        }
        /*SL:353*/a1.next = a2;
        /*SL:354*/a2.next = v1;
        /*SL:355*/a2.prev = a1;
        /*SL:356*/this.cache = null;
        /*SL:357*/a2.index = 0;
    }
    
    public void insert(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:371*/if (a2.size == 0) {
            /*SL:372*/return;
        }
        /*SL:374*/this.size += a2.size;
        final AbstractInsnNode v1 = /*EL:375*/a2.first;
        final AbstractInsnNode v2 = /*EL:376*/a2.last;
        final AbstractInsnNode v3 = /*EL:377*/a1.next;
        /*SL:378*/if (v3 == null) {
            /*SL:379*/this.last = v2;
        }
        else {
            /*SL:381*/v3.prev = v2;
        }
        /*SL:383*/a1.next = v1;
        /*SL:384*/v2.next = v3;
        /*SL:385*/v1.prev = a1;
        /*SL:386*/this.cache = null;
        /*SL:387*/a2.removeAll(false);
    }
    
    public void insertBefore(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:402*/++this.size;
        final AbstractInsnNode v1 = /*EL:403*/a1.prev;
        /*SL:404*/if (v1 == null) {
            /*SL:405*/this.first = a2;
        }
        else {
            /*SL:407*/v1.next = a2;
        }
        /*SL:409*/a1.prev = a2;
        /*SL:410*/a2.next = a1;
        /*SL:411*/a2.prev = v1;
        /*SL:412*/this.cache = null;
        /*SL:413*/a2.index = 0;
    }
    
    public void insertBefore(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:428*/if (a2.size == 0) {
            /*SL:429*/return;
        }
        /*SL:431*/this.size += a2.size;
        final AbstractInsnNode v1 = /*EL:432*/a2.first;
        final AbstractInsnNode v2 = /*EL:433*/a2.last;
        final AbstractInsnNode v3 = /*EL:434*/a1.prev;
        /*SL:435*/if (v3 == null) {
            /*SL:436*/this.first = v1;
        }
        else {
            /*SL:438*/v3.next = v1;
        }
        /*SL:440*/a1.prev = v2;
        /*SL:441*/v2.next = a1;
        /*SL:442*/v1.prev = v3;
        /*SL:443*/this.cache = null;
        /*SL:444*/a2.removeAll(false);
    }
    
    public void remove(final AbstractInsnNode a1) {
        /*SL:454*/--this.size;
        final AbstractInsnNode v1 = /*EL:455*/a1.next;
        final AbstractInsnNode v2 = /*EL:456*/a1.prev;
        /*SL:457*/if (v1 == null) {
            /*SL:458*/if (v2 == null) {
                /*SL:459*/this.first = null;
                /*SL:460*/this.last = null;
            }
            else {
                /*SL:462*/v2.next = null;
                /*SL:463*/this.last = v2;
            }
        }
        else/*SL:466*/ if (v2 == null) {
            /*SL:467*/this.first = v1;
            /*SL:468*/v1.prev = null;
        }
        else {
            /*SL:470*/v2.next = v1;
            /*SL:471*/v1.prev = v2;
        }
        /*SL:474*/this.cache = null;
        /*SL:475*/a1.index = -1;
        /*SL:476*/a1.prev = null;
        /*SL:477*/a1.next = null;
    }
    
    void removeAll(final boolean v0) {
        /*SL:488*/if (v0) {
            AbstractInsnNode a1;
            /*SL:490*/for (AbstractInsnNode v = this.first; v != null; /*SL:495*/v = a1) {
                a1 = v.next;
                v.index = -1;
                v.prev = null;
                v.next = null;
            }
        }
        /*SL:498*/this.size = 0;
        /*SL:499*/this.first = null;
        /*SL:500*/this.last = null;
        /*SL:501*/this.cache = null;
    }
    
    public void clear() {
        /*SL:508*/this.removeAll(false);
    }
    
    public void resetLabels() {
        /*SL:518*/for (AbstractInsnNode v1 = this.first; v1 != null; /*SL:522*/v1 = v1.next) {
            if (v1 instanceof LabelNode) {
                ((LabelNode)v1).resetLabel();
            }
        }
    }
    
    private final class InsnListIterator implements ListIterator
    {
        AbstractInsnNode next;
        AbstractInsnNode prev;
        AbstractInsnNode remove;
        
        InsnListIterator(final int a1) {
            if (a1 == InsnList.this.size()) {
                this.next = null;
                this.prev = InsnList.this.getLast();
            }
            else {
                /*SL:524*/this.next = InsnList.this.get(a1);
                this.prev = this.next.prev;
            }
        }
        
        public boolean hasNext() {
            /*SL:547*/return this.next != null;
        }
        
        public Object next() {
            /*SL:551*/if (this.next == null) {
                /*SL:552*/throw new NoSuchElementException();
            }
            final AbstractInsnNode v1 = /*EL:554*/this.next;
            /*SL:555*/this.prev = v1;
            /*SL:556*/this.next = v1.next;
            /*SL:558*/return this.remove = v1;
        }
        
        public void remove() {
            /*SL:562*/if (this.remove != null) {
                /*SL:563*/if (this.remove == this.next) {
                    /*SL:564*/this.next = this.next.next;
                }
                else {
                    /*SL:566*/this.prev = this.prev.prev;
                }
                /*SL:568*/InsnList.this.remove(this.remove);
                /*SL:569*/this.remove = null;
                /*SL:573*/return;
            }
            throw new IllegalStateException();
        }
        
        public boolean hasPrevious() {
            /*SL:576*/return this.prev != null;
        }
        
        public Object previous() {
            final AbstractInsnNode v1 = /*EL:580*/this.prev;
            /*SL:581*/this.next = v1;
            /*SL:582*/this.prev = v1.prev;
            /*SL:584*/return this.remove = v1;
        }
        
        public int nextIndex() {
            /*SL:588*/if (this.next == null) {
                /*SL:589*/return InsnList.this.size();
            }
            /*SL:591*/if (InsnList.this.cache == null) {
                /*SL:592*/InsnList.this.cache = InsnList.this.toArray();
            }
            /*SL:594*/return this.next.index;
        }
        
        public int previousIndex() {
            /*SL:598*/if (this.prev == null) {
                /*SL:599*/return -1;
            }
            /*SL:601*/if (InsnList.this.cache == null) {
                /*SL:602*/InsnList.this.cache = InsnList.this.toArray();
            }
            /*SL:604*/return this.prev.index;
        }
        
        public void add(final Object a1) {
            /*SL:608*/if (this.next != null) {
                /*SL:609*/InsnList.this.insertBefore(this.next, (AbstractInsnNode)a1);
            }
            else/*SL:610*/ if (this.prev != null) {
                /*SL:611*/InsnList.this.insert(this.prev, (AbstractInsnNode)a1);
            }
            else {
                /*SL:613*/InsnList.this.add((AbstractInsnNode)a1);
            }
            /*SL:615*/this.prev = (AbstractInsnNode)a1;
            /*SL:616*/this.remove = null;
        }
        
        public void set(final Object a1) {
            /*SL:620*/if (this.remove != null) {
                /*SL:621*/InsnList.this.set(this.remove, (AbstractInsnNode)a1);
                /*SL:622*/if (this.remove == this.prev) {
                    /*SL:623*/this.prev = (AbstractInsnNode)a1;
                }
                else {
                    /*SL:625*/this.next = (AbstractInsnNode)a1;
                }
                /*SL:630*/return;
            }
            throw new IllegalStateException();
        }
    }
}
