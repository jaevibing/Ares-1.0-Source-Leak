package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import java.util.Collection;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;

public interface IObfuscationEnvironment
{
    MappingMethod getObfMethod(MemberInfo p0);
    
    MappingMethod getObfMethod(MappingMethod p0);
    
    MappingMethod getObfMethod(MappingMethod p0, boolean p1);
    
    MappingField getObfField(MemberInfo p0);
    
    MappingField getObfField(MappingField p0);
    
    MappingField getObfField(MappingField p0, boolean p1);
    
    String getObfClass(String p0);
    
    MemberInfo remapDescriptor(MemberInfo p0);
    
    String remapDescriptor(String p0);
    
    void writeMappings(Collection<IMappingConsumer> p0);
}
