package org.spongepowered.asm.mixin.refmap;

public interface IReferenceMapper
{
    boolean isDefault();
    
    String getResourceName();
    
    String getStatus();
    
    String getContext();
    
    void setContext(String p0);
    
    String remap(String p0, String p1);
    
    String remapWithContext(String p0, String p1, String p2);
}
