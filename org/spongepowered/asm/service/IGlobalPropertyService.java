package org.spongepowered.asm.service;

public interface IGlobalPropertyService
{
     <T> T getProperty(String p0);
    
    void setProperty(String p0, Object p1);
    
     <T> T getProperty(String p0, T p1);
    
    String getPropertyString(String p0, String p1);
}
