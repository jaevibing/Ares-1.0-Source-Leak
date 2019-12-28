package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.throwables.MixinException;

public class InvalidMixinException extends MixinException
{
    private static final long serialVersionUID = 2L;
    private final IMixinInfo mixin;
    
    public InvalidMixinException(final IMixinInfo a1, final String a2) {
        super(a2);
        this.mixin = a1;
    }
    
    public InvalidMixinException(final IMixinContext a1, final String a2) {
        this(a1.getMixin(), a2);
    }
    
    public InvalidMixinException(final IMixinInfo a1, final Throwable a2) {
        super(a2);
        this.mixin = a1;
    }
    
    public InvalidMixinException(final IMixinContext a1, final Throwable a2) {
        this(a1.getMixin(), a2);
    }
    
    public InvalidMixinException(final IMixinInfo a1, final String a2, final Throwable a3) {
        super(a2, a3);
        this.mixin = a1;
    }
    
    public InvalidMixinException(final IMixinContext a1, final String a2, final Throwable a3) {
        super(a2, a3);
        this.mixin = a1.getMixin();
    }
    
    public IMixinInfo getMixin() {
        /*SL:69*/return this.mixin;
    }
}
