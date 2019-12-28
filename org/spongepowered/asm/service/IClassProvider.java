package org.spongepowered.asm.service;

import java.net.URL;

public interface IClassProvider
{
    URL[] getClassPath();
    
    Class<?> findClass(String p0) throws ClassNotFoundException;
    
    Class<?> findClass(String p0, boolean p1) throws ClassNotFoundException;
    
    Class<?> findAgentClass(String p0, boolean p1) throws ClassNotFoundException;
}
