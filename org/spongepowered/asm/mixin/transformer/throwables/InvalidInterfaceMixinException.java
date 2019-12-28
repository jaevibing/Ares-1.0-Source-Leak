package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class InvalidInterfaceMixinException extends InvalidMixinException
{
    private static final long serialVersionUID = 2L;
    
    public InvalidInterfaceMixinException(final IMixinInfo a1, final String a2) {
        super(a1, a2);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext a1, final String a2) {
        super(a1, a2);
    }
    
    public InvalidInterfaceMixinException(final IMixinInfo a1, final Throwable a2) {
        super(a1, a2);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext a1, final Throwable a2) {
        super(a1, a2);
    }
    
    public InvalidInterfaceMixinException(final IMixinInfo a1, final String a2, final Throwable a3) {
        super(a1, a2, a3);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext a1, final String a2, final Throwable a3) {
        super(a1, a2, a3);
    }
}
