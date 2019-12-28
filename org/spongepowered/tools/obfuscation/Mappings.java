package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.Iterator;
import java.util.HashMap;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import java.util.Map;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;

class Mappings implements IMappingConsumer
{
    private final Map<ObfuscationType, MappingSet<MappingField>> fieldMappings;
    private final Map<ObfuscationType, MappingSet<MappingMethod>> methodMappings;
    private UniqueMappings unique;
    
    public Mappings() {
        this.fieldMappings = new HashMap<ObfuscationType, MappingSet<MappingField>>();
        this.methodMappings = new HashMap<ObfuscationType, MappingSet<MappingMethod>>();
        this.init();
    }
    
    private void init() {
        /*SL:156*/for (final ObfuscationType v1 : ObfuscationType.types()) {
            /*SL:157*/this.fieldMappings.put(v1, new MappingSet<MappingField>());
            /*SL:158*/this.methodMappings.put(v1, new MappingSet<MappingMethod>());
        }
    }
    
    public IMappingConsumer asUnique() {
        /*SL:163*/if (this.unique == null) {
            /*SL:164*/this.unique = new UniqueMappings(this);
        }
        /*SL:166*/return this.unique;
    }
    
    @Override
    public MappingSet<MappingField> getFieldMappings(final ObfuscationType a1) {
        final MappingSet<MappingField> v1 = /*EL:174*/this.fieldMappings.get(a1);
        /*SL:175*/return (v1 != null) ? v1 : new MappingSet<MappingField>();
    }
    
    @Override
    public MappingSet<MappingMethod> getMethodMappings(final ObfuscationType a1) {
        final MappingSet<MappingMethod> v1 = /*EL:183*/this.methodMappings.get(a1);
        /*SL:184*/return (v1 != null) ? v1 : new MappingSet<MappingMethod>();
    }
    
    @Override
    public void clear() {
        /*SL:192*/this.fieldMappings.clear();
        /*SL:193*/this.methodMappings.clear();
        /*SL:194*/if (this.unique != null) {
            /*SL:195*/this.unique.clearMaps();
        }
        /*SL:197*/this.init();
    }
    
    @Override
    public void addFieldMapping(final ObfuscationType a1, final MappingField a2, final MappingField a3) {
        MappingSet<MappingField> v1 = /*EL:202*/this.fieldMappings.get(a1);
        /*SL:203*/if (v1 == null) {
            /*SL:204*/v1 = new MappingSet<MappingField>();
            /*SL:205*/this.fieldMappings.put(a1, v1);
        }
        /*SL:207*/v1.add(new MappingSet.Pair<MappingField>((IMapping)a2, (IMapping)a3));
    }
    
    @Override
    public void addMethodMapping(final ObfuscationType a1, final MappingMethod a2, final MappingMethod a3) {
        MappingSet<MappingMethod> v1 = /*EL:212*/this.methodMappings.get(a1);
        /*SL:213*/if (v1 == null) {
            /*SL:214*/v1 = new MappingSet<MappingMethod>();
            /*SL:215*/this.methodMappings.put(a1, v1);
        }
        /*SL:217*/v1.add(new MappingSet.Pair<MappingMethod>((IMapping)a2, (IMapping)a3));
    }
    
    public static class MappingConflictException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        private final IMapping<?> oldMapping;
        private final IMapping<?> newMapping;
        
        public MappingConflictException(final IMapping<?> a1, final IMapping<?> a2) {
            this.oldMapping = a1;
            this.newMapping = a2;
        }
        
        public IMapping<?> getOld() {
            /*SL:56*/return this.oldMapping;
        }
        
        public IMapping<?> getNew() {
            /*SL:60*/return this.newMapping;
        }
    }
    
    static class UniqueMappings implements IMappingConsumer
    {
        private final IMappingConsumer mappings;
        private final Map<ObfuscationType, Map<MappingField, MappingField>> fields;
        private final Map<ObfuscationType, Map<MappingMethod, MappingMethod>> methods;
        
        public UniqueMappings(final IMappingConsumer a1) {
            this.fields = new HashMap<ObfuscationType, Map<MappingField, MappingField>>();
            this.methods = new HashMap<ObfuscationType, Map<MappingMethod, MappingMethod>>();
            this.mappings = a1;
        }
        
        @Override
        public void clear() {
            /*SL:84*/this.clearMaps();
            /*SL:85*/this.mappings.clear();
        }
        
        protected void clearMaps() {
            /*SL:89*/this.fields.clear();
            /*SL:90*/this.methods.clear();
        }
        
        @Override
        public void addFieldMapping(final ObfuscationType a1, final MappingField a2, final MappingField a3) {
            /*SL:95*/if (!this.checkForExistingMapping(a1, (IMapping)a2, (IMapping)a3, (Map)this.fields)) {
                /*SL:96*/this.mappings.addFieldMapping(a1, a2, a3);
            }
        }
        
        @Override
        public void addMethodMapping(final ObfuscationType a1, final MappingMethod a2, final MappingMethod a3) {
            /*SL:102*/if (!this.checkForExistingMapping(a1, (IMapping)a2, (IMapping)a3, (Map)this.methods)) {
                /*SL:103*/this.mappings.addMethodMapping(a1, a2, a3);
            }
        }
        
        private <TMapping extends java.lang.Object> boolean checkForExistingMapping(final ObfuscationType a1, final TMapping a2, final TMapping a3, final Map<ObfuscationType, Map<TMapping, TMapping>> a4) throws MappingConflictException {
            Map<TMapping, TMapping> v1 = /*EL:109*/a4.get(a1);
            /*SL:110*/if (v1 == null) {
                /*SL:111*/v1 = new HashMap<TMapping, TMapping>();
                /*SL:112*/a4.put(a1, v1);
            }
            final TMapping v2 = /*EL:114*/v1.get(a2);
            /*SL:115*/if (v2 == null) {
                /*SL:121*/v1.put(a2, a3);
                /*SL:122*/return false;
            }
            if (v2.equals((Object)a3)) {
                return true;
            }
            throw new MappingConflictException((IMapping<?>)v2, (IMapping<?>)a3);
        }
        
        @Override
        public MappingSet<MappingField> getFieldMappings(final ObfuscationType a1) {
            /*SL:127*/return this.mappings.getFieldMappings(a1);
        }
        
        @Override
        public MappingSet<MappingMethod> getMethodMappings(final ObfuscationType a1) {
            /*SL:132*/return this.mappings.getMethodMappings(a1);
        }
    }
}
