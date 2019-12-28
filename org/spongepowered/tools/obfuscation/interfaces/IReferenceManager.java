package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.ObfuscationData;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;

public interface IReferenceManager
{
    void setAllowConflicts(boolean p0);
    
    boolean getAllowConflicts();
    
    void write();
    
    ReferenceMapper getMapper();
    
    void addMethodMapping(String p0, String p1, ObfuscationData<MappingMethod> p2);
    
    void addMethodMapping(String p0, String p1, MemberInfo p2, ObfuscationData<MappingMethod> p3);
    
    void addFieldMapping(String p0, String p1, MemberInfo p2, ObfuscationData<MappingField> p3);
    
    void addClassMapping(String p0, String p1, ObfuscationData<String> p2);
}
