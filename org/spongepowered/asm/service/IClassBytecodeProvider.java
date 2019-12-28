package org.spongepowered.asm.service;

import org.spongepowered.asm.lib.tree.ClassNode;
import java.io.IOException;

public interface IClassBytecodeProvider
{
    byte[] getClassBytes(String p0, String p1) throws IOException;
    
    byte[] getClassBytes(String p0, boolean p1) throws ClassNotFoundException, IOException;
    
    ClassNode getClassNode(String p0) throws ClassNotFoundException, IOException;
}
