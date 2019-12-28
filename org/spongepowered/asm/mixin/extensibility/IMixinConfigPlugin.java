package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.List;
import java.util.Set;

public interface IMixinConfigPlugin
{
    void onLoad(String p0);
    
    String getRefMapperConfig();
    
    boolean shouldApplyMixin(String p0, String p1);
    
    void acceptTargets(Set<String> p0, Set<String> p1);
    
    List<String> getMixins();
    
    void preApply(String p0, ClassNode p1, String p2, IMixinInfo p3);
    
    void postApply(String p0, ClassNode p1, String p2, IMixinInfo p3);
}
