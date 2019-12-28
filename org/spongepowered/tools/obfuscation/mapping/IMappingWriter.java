package org.spongepowered.tools.obfuscation.mapping;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.ObfuscationType;

public interface IMappingWriter
{
    void write(String p0, ObfuscationType p1, IMappingConsumer.MappingSet<MappingField> p2, IMappingConsumer.MappingSet<MappingMethod> p3);
}
