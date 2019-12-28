package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionCheckClass implements IExtension
{
    @Override
    public boolean checkActive(final MixinEnvironment a1) {
        /*SL:69*/return a1.getOption(MixinEnvironment.Option.DEBUG_VERIFY);
    }
    
    @Override
    public void preApply(final ITargetClassContext a1) {
    }
    
    @Override
    public void postApply(final ITargetClassContext v2) {
        try {
            /*SL:87*/v2.getClassNode().accept(new CheckClassAdapter(new MixinClassWriter(2)));
        }
        catch (RuntimeException a1) {
            /*SL:89*/throw new ValidationFailedException(a1.getMessage(), a1);
        }
    }
    
    @Override
    public void export(final MixinEnvironment a1, final String a2, final boolean a3, final byte[] a4) {
    }
    
    public static class ValidationFailedException extends MixinException
    {
        private static final long serialVersionUID = 1L;
        
        public ValidationFailedException(final String a1, final Throwable a2) {
            /*SL:100*/super(a1, a2);
        }
        
        public ValidationFailedException(final String a1) {
            super(a1);
        }
        
        public ValidationFailedException(final Throwable a1) {
            super(a1);
        }
    }
}
