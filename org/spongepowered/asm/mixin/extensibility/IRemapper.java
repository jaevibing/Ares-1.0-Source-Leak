package org.spongepowered.asm.mixin.extensibility;

public interface IRemapper
{
    String mapMethodName(String p0, String p1, String p2);
    
    String mapFieldName(String p0, String p1, String p2);
    
    String map(String p0);
    
    String unmap(String p0);
    
    String mapDesc(String p0);
    
    String unmapDesc(String p0);
}
