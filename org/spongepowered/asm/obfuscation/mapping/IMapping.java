package org.spongepowered.asm.obfuscation.mapping;

public interface IMapping<TMapping>
{
    Type getType();
    
    TMapping move(String p0);
    
    TMapping remap(String p0);
    
    TMapping transform(String p0);
    
    TMapping copy();
    
    String getName();
    
    String getSimpleName();
    
    String getOwner();
    
    String getDesc();
    
    TMapping getSuper();
    
    String serialise();
    
    public enum Type
    {
        FIELD, 
        METHOD, 
        CLASS, 
        PACKAGE;
    }
}
