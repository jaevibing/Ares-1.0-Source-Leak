package org.spongepowered.asm.mixin.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public final class Proxy implements IClassTransformer, ILegacyClassTransformer
{
    private static List<Proxy> proxies;
    private static MixinTransformer transformer;
    private boolean isActive;
    
    public Proxy() {
        this.isActive = true;
        for (final Proxy v1 : Proxy.proxies) {
            v1.isActive = false;
        }
        Proxy.proxies.add(this);
        LogManager.getLogger("mixin").debug("Adding new mixin transformer proxy #{}", new Object[] { Proxy.proxies.size() });
    }
    
    public byte[] transform(final String a1, final String a2, final byte[] a3) {
        /*SL:71*/if (this.isActive) {
            /*SL:72*/return Proxy.transformer.transformClassBytes(a1, a2, a3);
        }
        /*SL:75*/return a3;
    }
    
    public String getName() {
        /*SL:80*/return this.getClass().getName();
    }
    
    public boolean isDelegationExcluded() {
        /*SL:85*/return true;
    }
    
    public byte[] transformClassBytes(final String a1, final String a2, final byte[] a3) {
        /*SL:90*/if (this.isActive) {
            /*SL:91*/return Proxy.transformer.transformClassBytes(a1, a2, a3);
        }
        /*SL:94*/return a3;
    }
    
    static {
        Proxy.proxies = new ArrayList<Proxy>();
        Proxy.transformer = new MixinTransformer();
    }
}
