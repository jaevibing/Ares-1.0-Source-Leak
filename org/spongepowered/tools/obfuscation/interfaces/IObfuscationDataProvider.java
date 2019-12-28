package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.tools.obfuscation.ObfuscationData;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;

public interface IObfuscationDataProvider
{
     <T> ObfuscationData<T> getObfEntryRecursive(MemberInfo p0);
    
     <T> ObfuscationData<T> getObfEntry(MemberInfo p0);
    
     <T> ObfuscationData<T> getObfEntry(IMapping<T> p0);
    
    ObfuscationData<MappingMethod> getObfMethodRecursive(MemberInfo p0);
    
    ObfuscationData<MappingMethod> getObfMethod(MemberInfo p0);
    
    ObfuscationData<MappingMethod> getRemappedMethod(MemberInfo p0);
    
    ObfuscationData<MappingMethod> getObfMethod(MappingMethod p0);
    
    ObfuscationData<MappingMethod> getRemappedMethod(MappingMethod p0);
    
    ObfuscationData<MappingField> getObfFieldRecursive(MemberInfo p0);
    
    ObfuscationData<MappingField> getObfField(MemberInfo p0);
    
    ObfuscationData<MappingField> getObfField(MappingField p0);
    
    ObfuscationData<String> getObfClass(TypeHandle p0);
    
    ObfuscationData<String> getObfClass(String p0);
}
