package org.spongepowered.tools.obfuscation.mapping;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import com.google.common.base.Objects;
import java.util.LinkedHashSet;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.ObfuscationType;

public interface IMappingConsumer
{
    void clear();
    
    void addFieldMapping(ObfuscationType p0, MappingField p1, MappingField p2);
    
    void addMethodMapping(ObfuscationType p0, MappingMethod p1, MappingMethod p2);
    
    MappingSet<MappingField> getFieldMappings(ObfuscationType p0);
    
    MappingSet<MappingMethod> getMethodMappings(ObfuscationType p0);
    
    public static class MappingSet<TMapping extends java.lang.Object> extends LinkedHashSet<Pair<TMapping>>
    {
        private static final long serialVersionUID = 1L;
        
        public static class Pair<TMapping extends java.lang.Object>
        {
            public final TMapping from;
            public final TMapping to;
            
            public Pair(TMapping a1, TMapping a2) {
                this.from = (IMapping)a1;
                this.to = (IMapping)a2;
            }
            
            @Override
            public boolean equals(Object a1) {
                Pair<TMapping> v1;
                /*SL:73*/if (!(a1 instanceof Pair)) {
                    /*SL:74*/return false;
                }
                /*SL:78*/v1 = (Pair)a1;
                /*SL:79*/return Objects.equal(this.from, v1.from) && Objects.equal(this.to, v1.to);
            }
            
            @Override
            public int hashCode() {
                /*SL:84*/return Objects.hashCode(this.from, this.to);
            }
            
            @Override
            public String toString() {
                /*SL:89*/return String.format("%s -> %s", this.from, this.to);
            }
        }
    }
}
