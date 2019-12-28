package org.spongepowered.tools.obfuscation.mcp;

import org.spongepowered.tools.obfuscation.mapping.mcp.MappingWriterSrg;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
import org.spongepowered.tools.obfuscation.mapping.mcp.MappingProviderSrg;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;

public class ObfuscationEnvironmentMCP extends ObfuscationEnvironment
{
    protected ObfuscationEnvironmentMCP(final ObfuscationType a1) {
        super(a1);
    }
    
    @Override
    protected IMappingProvider getMappingProvider(final Messager a1, final Filer a2) {
        /*SL:48*/return new MappingProviderSrg(a1, a2);
    }
    
    @Override
    protected IMappingWriter getMappingWriter(final Messager a1, final Filer a2) {
        /*SL:53*/return new MappingWriterSrg(a1, a2);
    }
}
