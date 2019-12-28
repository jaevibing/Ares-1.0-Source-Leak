package com.google.api.client.util;

import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.HashMap;

public final class Maps
{
    public static <K, V> HashMap<K, V> newHashMap() {
        /*SL:37*/return new HashMap<K, V>();
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        /*SL:42*/return new LinkedHashMap<K, V>();
    }
    
    public static <K extends java.lang.Object, V> TreeMap<K, V> newTreeMap() {
        /*SL:50*/return new TreeMap<K, V>();
    }
}
