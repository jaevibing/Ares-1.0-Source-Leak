package org.spongepowered.asm.lib.tree.analysis;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Iterator;
import java.util.AbstractSet;

class SmallSet<E> extends AbstractSet<E> implements Iterator<E>
{
    E e1;
    E e2;
    
    static final <T> Set<T> emptySet() {
        /*SL:50*/return new SmallSet<T>(null, null);
    }
    
    SmallSet(final E a1, final E a2) {
        this.e1 = a1;
        this.e2 = a2;
    }
    
    public Iterator<E> iterator() {
        /*SL:64*/return new SmallSet(this.e1, this.e2);
    }
    
    public int size() {
        /*SL:69*/return (this.e1 == null) ? 0 : ((this.e2 == null) ? 1 : 2);
    }
    
    public boolean hasNext() {
        /*SL:77*/return this.e1 != null;
    }
    
    public E next() {
        /*SL:81*/if (this.e1 == null) {
            /*SL:82*/throw new NoSuchElementException();
        }
        final E v1 = /*EL:84*/this.e1;
        /*SL:85*/this.e1 = this.e2;
        /*SL:86*/this.e2 = null;
        /*SL:87*/return v1;
    }
    
    public void remove() {
    }
    
    Set<E> union(final SmallSet<E> a1) {
        /*SL:98*/if ((a1.e1 == this.e1 && a1.e2 == this.e2) || (a1.e1 == this.e2 && a1.e2 == this.e1)) {
            /*SL:99*/return this;
        }
        /*SL:101*/if (a1.e1 == null) {
            /*SL:102*/return this;
        }
        /*SL:104*/if (this.e1 == null) {
            /*SL:105*/return a1;
        }
        /*SL:107*/if (a1.e2 == null) {
            /*SL:108*/if (this.e2 == null) {
                /*SL:109*/return new SmallSet(this.e1, a1.e1);
            }
            /*SL:110*/if (a1.e1 == this.e1 || a1.e1 == this.e2) {
                /*SL:111*/return this;
            }
        }
        /*SL:114*/if (this.e2 == null && /*EL:118*/(this.e1 == a1.e1 || this.e1 == a1.e2)) {
            /*SL:119*/return a1;
        }
        final HashSet<E> v1 = /*EL:123*/new HashSet<E>(4);
        /*SL:124*/v1.add(this.e1);
        /*SL:125*/if (this.e2 != null) {
            /*SL:126*/v1.add(this.e2);
        }
        /*SL:128*/v1.add(a1.e1);
        /*SL:129*/if (a1.e2 != null) {
            /*SL:130*/v1.add(a1.e2);
        }
        /*SL:132*/return v1;
    }
}
