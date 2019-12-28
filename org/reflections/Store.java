package org.reflections;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.util.concurrent.ConcurrentHashMap;
import com.google.common.base.Supplier;
import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import com.google.common.collect.Multimap;
import java.util.Map;

public class Store
{
    private transient boolean concurrent;
    private final Map<String, Multimap<String, String>> storeMap;
    
    protected Store() {
        this.storeMap = new HashMap<String, Multimap<String, String>>();
        this.concurrent = false;
    }
    
    public Store(final Configuration a1) {
        this.storeMap = new HashMap<String, Multimap<String, String>>();
        this.concurrent = (a1.getExecutorService() != null);
    }
    
    public Set<String> keySet() {
        /*SL:34*/return this.storeMap.keySet();
    }
    
    public Multimap<String, String> getOrCreate(final String v2) {
        Multimap<String, String> v3 = /*EL:39*/this.storeMap.get(v2);
        /*SL:40*/if (v3 == null) {
            final SetMultimap<String, String> a1 = /*EL:42*/Multimaps.<String, String>newSetMultimap(new HashMap<String, Collection<String>>(), new Supplier<Set<String>>() {
                @Override
                public Set<String> get() {
                    /*SL:45*/return Sets.<String>newSetFromMap(new ConcurrentHashMap<String, Boolean>());
                }
            });
            /*SL:48*/v3 = (this.concurrent ? Multimaps.<String, String>synchronizedSetMultimap(a1) : a1);
            /*SL:49*/this.storeMap.put(v2, v3);
        }
        /*SL:51*/return v3;
    }
    
    public Multimap<String, String> get(final String a1) {
        final Multimap<String, String> v1 = /*EL:56*/this.storeMap.get(a1);
        /*SL:57*/if (v1 == null) {
            /*SL:58*/throw new ReflectionsException("Scanner " + a1 + " was not configured");
        }
        /*SL:60*/return v1;
    }
    
    public Iterable<String> get(final String a1, final String... a2) {
        /*SL:65*/return this.get(a1, Arrays.<String>asList(a2));
    }
    
    public Iterable<String> get(final String v1, final Iterable<String> v2) {
        final Multimap<String, String> v3 = /*EL:70*/this.get(v1);
        final IterableChain<String> v4 = /*EL:71*/new IterableChain<String>();
        /*SL:72*/for (final String a1 : v2) {
            /*SL:73*/((IterableChain<Object>)v4).addAll((Iterable)v3.get(a1));
        }
        /*SL:75*/return v4;
    }
    
    private Iterable<String> getAllIncluding(final String v1, final Iterable<String> v2, final IterableChain<String> v3) {
        /*SL:80*/((IterableChain<Object>)v3).addAll((Iterable)v2);
        /*SL:81*/for (Iterable<String> a2 : v2) {
            /*SL:82*/a2 = this.get(v1, a2);
            /*SL:83*/if (a2.iterator().hasNext()) {
                /*SL:84*/this.getAllIncluding(v1, a2, v3);
            }
        }
        /*SL:87*/return v3;
    }
    
    public Iterable<String> getAll(final String a1, final String a2) {
        /*SL:92*/return this.getAllIncluding(a1, this.get(a1, a2), new IterableChain<String>());
    }
    
    public Iterable<String> getAll(final String a1, final Iterable<String> a2) {
        /*SL:97*/return this.getAllIncluding(a1, this.get(a1, a2), new IterableChain<String>());
    }
    
    private static class IterableChain<T> implements Iterable<T>
    {
        private final List<Iterable<T>> chain;
        
        private IterableChain() {
            this.chain = (List<Iterable<T>>)Lists.<Object>newArrayList();
        }
        
        private void addAll(final Iterable<T> a1) {
            /*SL:103*/this.chain.add(a1);
        }
        
        @Override
        public Iterator<T> iterator() {
            /*SL:105*/return Iterables.<T>concat((Iterable<? extends Iterable<? extends T>>)this.chain).iterator();
        }
    }
}
