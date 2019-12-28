package org.spongepowered.asm.mixin.transformer.ext;

import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IExtension
{
    boolean checkActive(MixinEnvironment p0);
    
    void preApply(ITargetClassContext p0);
    
    void postApply(ITargetClassContext p0);
    
    void export(MixinEnvironment p0, String p1, boolean p2, byte[] p3);
}
