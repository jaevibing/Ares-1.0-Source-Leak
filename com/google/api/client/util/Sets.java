package com.google.api.client.util;

import java.util.TreeSet;
import java.util.HashSet;

public final class Sets
{
    public static <E> HashSet<E> newHashSet() {
        /*SL:36*/return new HashSet<E>();
    }
    
    public static <E extends java.lang.Object> TreeSet<E> newTreeSet() {
        /*SL:44*/return new TreeSet<E>();
    }
}
