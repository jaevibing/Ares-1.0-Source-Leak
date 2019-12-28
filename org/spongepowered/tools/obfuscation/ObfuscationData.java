package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class ObfuscationData<T> implements Iterable<ObfuscationType>
{
    private final Map<ObfuscationType, T> data;
    private final T defaultValue;
    
    public ObfuscationData() {
        this(null);
    }
    
    public ObfuscationData(final T a1) {
        this.data = new HashMap<ObfuscationType, T>();
        this.defaultValue = a1;
    }
    
    @Deprecated
    public void add(final ObfuscationType a1, final T a2) {
        /*SL:81*/this.put(a1, a2);
    }
    
    public void put(final ObfuscationType a1, final T a2) {
        /*SL:91*/this.data.put(a1, a2);
    }
    
    public boolean isEmpty() {
        /*SL:98*/return this.data.isEmpty();
    }
    
    public T get(final ObfuscationType a1) {
        final T v1 = /*EL:109*/this.data.get(a1);
        /*SL:110*/return (v1 != null) ? v1 : this.defaultValue;
    }
    
    @Override
    public Iterator<ObfuscationType> iterator() {
        /*SL:118*/return this.data.keySet().iterator();
    }
    
    @Override
    public String toString() {
        /*SL:126*/return String.format("ObfuscationData[%s,DEFAULT=%s]", this.listValues(), this.defaultValue);
    }
    
    public String values() {
        /*SL:135*/return "[" + this.listValues() + "]";
    }
    
    private String listValues() {
        final StringBuilder sb = /*EL:139*/new StringBuilder();
        boolean b = /*EL:140*/false;
        /*SL:141*/for (final ObfuscationType v1 : this.data.keySet()) {
            /*SL:142*/if (b) {
                /*SL:143*/sb.append(',');
            }
            /*SL:145*/sb.append(v1.getKey()).append('=').append(this.data.get(v1));
            /*SL:146*/b = true;
        }
        /*SL:148*/return sb.toString();
    }
}
