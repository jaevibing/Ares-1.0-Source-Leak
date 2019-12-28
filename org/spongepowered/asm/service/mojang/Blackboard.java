package org.spongepowered.asm.service.mojang;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IGlobalPropertyService;

public class Blackboard implements IGlobalPropertyService
{
    @Override
    public final <T> T getProperty(final String a1) {
        /*SL:46*/return Launch.blackboard.get(a1);
    }
    
    @Override
    public final void setProperty(final String a1, final Object a2) {
        Launch.blackboard.put(/*EL:57*/a1, a2);
    }
    
    @Override
    public final <T> T getProperty(final String a1, final T a2) {
        final Object v1 = Launch.blackboard.get(/*EL:72*/a1);
        /*SL:73*/return (T)((v1 != null) ? v1 : a2);
    }
    
    @Override
    public final String getPropertyString(final String a1, final String a2) {
        final Object v1 = Launch.blackboard.get(/*EL:87*/a1);
        /*SL:88*/return (v1 != null) ? v1.toString() : a2;
    }
}
