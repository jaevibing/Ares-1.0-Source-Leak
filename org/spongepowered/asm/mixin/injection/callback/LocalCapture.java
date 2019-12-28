package org.spongepowered.asm.mixin.injection.callback;

public enum LocalCapture
{
    NO_CAPTURE(false, false), 
    PRINT(false, true), 
    CAPTURE_FAILSOFT, 
    CAPTURE_FAILHARD, 
    CAPTURE_FAILEXCEPTION;
    
    private final boolean captureLocals;
    private final boolean printLocals;
    
    private LocalCapture(final int n) {
        this(true, false);
    }
    
    private LocalCapture(final boolean a1, final boolean a2) {
        this.captureLocals = a1;
        this.printLocals = a2;
    }
    
    boolean isCaptureLocals() {
        /*SL:85*/return this.captureLocals;
    }
    
    boolean isPrintLocals() {
        /*SL:89*/return this.printLocals;
    }
}
