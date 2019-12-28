package org.spongepowered.tools.obfuscation.mapping;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import java.io.IOException;
import java.io.File;

public interface IMappingProvider
{
    void clear();
    
    boolean isEmpty();
    
    void read(File p0) throws IOException;
    
    MappingMethod getMethodMapping(MappingMethod p0);
    
    MappingField getFieldMapping(MappingField p0);
    
    String getClassMapping(String p0);
    
    String getPackageMapping(String p0);
}
