package org.spongepowered.asm.mixin.transformer.ext;

public interface IHotSwap
{
    void registerMixinClass(String p0);
    
    void registerTargetClass(String p0, byte[] p1);
}
