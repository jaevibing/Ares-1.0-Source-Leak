package org.spongepowered.tools.obfuscation.mapping.common;

import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import com.google.common.collect.BiMap;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;

public abstract class MappingProvider implements IMappingProvider
{
    protected final Messager messager;
    protected final Filer filer;
    protected final BiMap<String, String> packageMap;
    protected final BiMap<String, String> classMap;
    protected final BiMap<MappingField, MappingField> fieldMap;
    protected final BiMap<MappingMethod, MappingMethod> methodMap;
    
    public MappingProvider(final Messager a1, final Filer a2) {
        this.packageMap = (BiMap<String, String>)HashBiMap.<Object, Object>create();
        this.classMap = (BiMap<String, String>)HashBiMap.<Object, Object>create();
        this.fieldMap = (BiMap<MappingField, MappingField>)HashBiMap.<Object, Object>create();
        this.methodMap = (BiMap<MappingMethod, MappingMethod>)HashBiMap.<Object, Object>create();
        this.messager = a1;
        this.filer = a2;
    }
    
    @Override
    public void clear() {
        /*SL:57*/this.packageMap.clear();
        /*SL:58*/this.classMap.clear();
        /*SL:59*/this.fieldMap.clear();
        /*SL:60*/this.methodMap.clear();
    }
    
    @Override
    public boolean isEmpty() {
        /*SL:65*/return this.packageMap.isEmpty() && this.classMap.isEmpty() && this.fieldMap.isEmpty() && this.methodMap.isEmpty();
    }
    
    @Override
    public MappingMethod getMethodMapping(final MappingMethod a1) {
        /*SL:70*/return this.methodMap.get(a1);
    }
    
    @Override
    public MappingField getFieldMapping(final MappingField a1) {
        /*SL:75*/return this.fieldMap.get(a1);
    }
    
    @Override
    public String getClassMapping(final String a1) {
        /*SL:80*/return this.classMap.get(a1);
    }
    
    @Override
    public String getPackageMapping(final String a1) {
        /*SL:85*/return this.packageMap.get(a1);
    }
}
