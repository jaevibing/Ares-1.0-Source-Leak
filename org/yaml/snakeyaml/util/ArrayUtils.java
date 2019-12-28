package org.yaml.snakeyaml.util;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class ArrayUtils
{
    public static <E> List<E> toUnmodifiableList(final E... a1) {
        /*SL:36*/return (a1.length == 0) ? Collections.<E>emptyList() : new UnmodifiableArrayList<E>(a1);
    }
    
    public static <E> List<E> toUnmodifiableCompositeList(final E[] v1, final E[] v2) {
        List<E> a1;
        /*SL:49*/if (v1.length == 0) {
            /*SL:50*/a1 = ArrayUtils.<E>toUnmodifiableList(v2);
        }
        else/*SL:51*/ if (v2.length == 0) {
            /*SL:52*/a1 = ArrayUtils.<E>toUnmodifiableList(v1);
        }
        else {
            /*SL:54*/a1 = new CompositeUnmodifiableArrayList<E>(v1, v2);
        }
        /*SL:56*/return a1;
    }
    
    private static class UnmodifiableArrayList<E> extends AbstractList<E>
    {
        private final E[] array;
        
        UnmodifiableArrayList(final E[] a1) {
            this.array = a1;
        }
        
        @Override
        public E get(final int a1) {
            /*SL:69*/if (a1 >= this.array.length) {
                /*SL:70*/throw new IndexOutOfBoundsException("Index: " + a1 + ", Size: " + this.size());
            }
            /*SL:72*/return (E)this.array[a1];
        }
        
        @Override
        public int size() {
            /*SL:77*/return this.array.length;
        }
    }
    
    private static class CompositeUnmodifiableArrayList<E> extends AbstractList<E>
    {
        private final E[] array1;
        private final E[] array2;
        
        CompositeUnmodifiableArrayList(final E[] a1, final E[] a2) {
            this.array1 = a1;
            this.array2 = a2;
        }
        
        @Override
        public E get(final int v0) {
            E a1;
            /*SL:94*/if (v0 < this.array1.length) {
                /*SL:95*/a1 = (E)this.array1[v0];
            }
            else {
                /*SL:96*/if (v0 - this.array1.length >= this.array2.length) {
                    /*SL:99*/throw new IndexOutOfBoundsException("Index: " + v0 + ", Size: " + this.size());
                }
                a1 = (E)this.array2[v0 - this.array1.length];
            }
            /*SL:101*/return a1;
        }
        
        @Override
        public int size() {
            /*SL:106*/return this.array1.length + this.array2.length;
        }
    }
}
