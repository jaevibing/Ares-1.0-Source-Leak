package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.lib.Type;

public class CallbackInfo implements Cancellable
{
    private final String name;
    private final boolean cancellable;
    private boolean cancelled;
    
    public CallbackInfo(final String a1, final boolean a2) {
        this.name = a1;
        this.cancellable = a2;
    }
    
    public String getId() {
        /*SL:74*/return this.name;
    }
    
    @Override
    public String toString() {
        /*SL:82*/return String.format("CallbackInfo[TYPE=%s,NAME=%s,CANCELLABLE=%s]", this.getClass().getSimpleName(), this.name, this.cancellable);
    }
    
    @Override
    public final boolean isCancellable() {
        /*SL:87*/return this.cancellable;
    }
    
    @Override
    public final boolean isCancelled() {
        /*SL:92*/return this.cancelled;
    }
    
    @Override
    public void cancel() throws CancellationException {
        /*SL:100*/if (!this.cancellable) {
            /*SL:101*/throw new CancellationException(String.format("The call %s is not cancellable.", this.name));
        }
        /*SL:104*/this.cancelled = true;
    }
    
    static String getCallInfoClassName() {
        /*SL:110*/return CallbackInfo.class.getName();
    }
    
    public static String getCallInfoClassName(final Type a1) {
        /*SL:122*/return (a1.equals(Type.VOID_TYPE) ? CallbackInfo.class.getName() : CallbackInfoReturnable.class.getName()).replace('.', '/');
    }
    
    static String getConstructorDescriptor(final Type a1) {
        /*SL:126*/if (a1.equals(Type.VOID_TYPE)) {
            /*SL:127*/return getConstructorDescriptor();
        }
        /*SL:130*/if (a1.getSort() == 10 || a1.getSort() == 9) {
            /*SL:131*/return String.format("(%sZ%s)V", "Ljava/lang/String;", "Ljava/lang/Object;");
        }
        /*SL:134*/return String.format("(%sZ%s)V", "Ljava/lang/String;", a1.getDescriptor());
    }
    
    static String getConstructorDescriptor() {
        /*SL:138*/return String.format("(%sZ)V", "Ljava/lang/String;");
    }
    
    static String getIsCancelledMethodName() {
        /*SL:142*/return "isCancelled";
    }
    
    static String getIsCancelledMethodSig() {
        /*SL:146*/return "()Z";
    }
}
