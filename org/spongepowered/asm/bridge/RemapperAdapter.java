package org.spongepowered.asm.bridge;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.commons.Remapper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.util.ObfuscationUtil;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public abstract class RemapperAdapter implements IRemapper, ObfuscationUtil.IClassRemapper
{
    protected final Logger logger;
    protected final Remapper remapper;
    
    public RemapperAdapter(final Remapper a1) {
        this.logger = LogManager.getLogger("mixin");
        this.remapper = a1;
    }
    
    @Override
    public String toString() {
        /*SL:47*/return this.getClass().getSimpleName();
    }
    
    @Override
    public String mapMethodName(final String a1, final String a2, final String a3) {
        /*SL:52*/this.logger.debug("{} is remapping method {}{} for {}", new Object[] { this, a2, a3, a1 });
        final String v1 = /*EL:53*/this.remapper.mapMethodName(a1, a2, a3);
        /*SL:54*/if (!v1.equals(a2)) {
            /*SL:55*/return v1;
        }
        final String v2 = /*EL:57*/this.unmap(a1);
        final String v3 = /*EL:58*/this.unmapDesc(a3);
        /*SL:59*/this.logger.debug("{} is remapping obfuscated method {}{} for {}", new Object[] { this, a2, v3, v2 });
        /*SL:60*/return this.remapper.mapMethodName(v2, a2, v3);
    }
    
    @Override
    public String mapFieldName(final String a1, final String a2, final String a3) {
        /*SL:65*/this.logger.debug("{} is remapping field {}{} for {}", new Object[] { this, a2, a3, a1 });
        final String v1 = /*EL:66*/this.remapper.mapFieldName(a1, a2, a3);
        /*SL:67*/if (!v1.equals(a2)) {
            /*SL:68*/return v1;
        }
        final String v2 = /*EL:70*/this.unmap(a1);
        final String v3 = /*EL:71*/this.unmapDesc(a3);
        /*SL:72*/this.logger.debug("{} is remapping obfuscated field {}{} for {}", new Object[] { this, a2, v3, v2 });
        /*SL:73*/return this.remapper.mapFieldName(v2, a2, v3);
    }
    
    @Override
    public String map(final String a1) {
        /*SL:78*/this.logger.debug("{} is remapping class {}", new Object[] { this, a1 });
        /*SL:79*/return this.remapper.map(a1);
    }
    
    @Override
    public String unmap(final String a1) {
        /*SL:84*/return a1;
    }
    
    @Override
    public String mapDesc(final String a1) {
        /*SL:89*/return this.remapper.mapDesc(a1);
    }
    
    @Override
    public String unmapDesc(final String a1) {
        /*SL:94*/return ObfuscationUtil.unmapDescriptor(a1, this);
    }
}
