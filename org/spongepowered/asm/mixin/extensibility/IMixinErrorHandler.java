package org.spongepowered.asm.mixin.extensibility;

import org.apache.logging.log4j.Level;

public interface IMixinErrorHandler
{
    ErrorAction onPrepareError(IMixinConfig p0, Throwable p1, IMixinInfo p2, ErrorAction p3);
    
    ErrorAction onApplyError(String p0, Throwable p1, IMixinInfo p2, ErrorAction p3);
    
    public enum ErrorAction
    {
        NONE(Level.INFO), 
        WARN(Level.WARN), 
        ERROR(Level.FATAL);
        
        public final Level logLevel;
        
        private ErrorAction(Level a1) {
            this.logLevel = a1;
        }
    }
}
