package com.google.api.client.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public final class Lists
{
    public static <E> ArrayList<E> newArrayList() {
        /*SL:37*/return new ArrayList<E>();
    }
    
    public static <E> ArrayList<E> newArrayListWithCapacity(final int a1) {
        /*SL:51*/return new ArrayList<E>(a1);
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterable<? extends E> a1) {
        /*SL:61*/return (a1 instanceof Collection) ? new ArrayList<E>(/*EL:62*/Collections2.<? extends E>cast(a1)) : Lists.<E>newArrayList(a1.iterator());
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterator<? extends E> a1) {
        final ArrayList<E> v1 = /*EL:72*/Lists.<E>newArrayList();
        /*SL:73*/while (a1.hasNext()) {
            /*SL:74*/v1.add((E)a1.next());
        }
        /*SL:76*/return v1;
    }
}
