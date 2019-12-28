package org.spongepowered.asm.service.mojang;

import javax.annotation.Resource;
import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.service.ILegacyClassTransformer;

class LegacyTransformerHandle implements ILegacyClassTransformer
{
    private final IClassTransformer transformer;
    
    LegacyTransformerHandle(final IClassTransformer a1) {
        this.transformer = a1;
    }
    
    @Override
    public String getName() {
        /*SL:53*/return this.transformer.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        /*SL:62*/return this.transformer.getClass().<Resource>getAnnotation(Resource.class) != null;
    }
    
    @Override
    public byte[] transformClassBytes(final String a1, final String a2, final byte[] a3) {
        /*SL:71*/return this.transformer.transform(a1, a2, a3);
    }
}
