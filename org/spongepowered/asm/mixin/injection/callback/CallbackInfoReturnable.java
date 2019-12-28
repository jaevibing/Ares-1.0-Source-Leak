package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.lib.Type;

public class CallbackInfoReturnable<R> extends CallbackInfo
{
    private R returnValue;
    
    public CallbackInfoReturnable(final String a1, final boolean a2) {
        super(a1, a2);
        this.returnValue = null;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final R a3) {
        super(a1, a2);
        this.returnValue = a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final byte a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final char a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final double a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final float a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final int a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final long a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final short a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public CallbackInfoReturnable(final String a1, final boolean a2, final boolean a3) {
        super(a1, a2);
        this.returnValue = (R)a3;
    }
    
    public void setReturnValue(final R a1) throws CancellationException {
        /*SL:106*/super.cancel();
        /*SL:108*/this.returnValue = a1;
    }
    
    public R getReturnValue() {
        /*SL:112*/return this.returnValue;
    }
    
    public byte getReturnValueB() {
        /*SL:117*/if (this.returnValue == null) {
            return 0;
        }
        return (byte)this.returnValue;
    }
    
    public char getReturnValueC() {
        /*SL:118*/if (this.returnValue == null) {
            return '\0';
        }
        return (char)this.returnValue;
    }
    
    public double getReturnValueD() {
        /*SL:119*/if (this.returnValue == null) {
            return 0.0;
        }
        return (double)this.returnValue;
    }
    
    public float getReturnValueF() {
        /*SL:120*/if (this.returnValue == null) {
            return 0.0f;
        }
        return (float)this.returnValue;
    }
    
    public int getReturnValueI() {
        /*SL:121*/if (this.returnValue == null) {
            return 0;
        }
        return (int)this.returnValue;
    }
    
    public long getReturnValueJ() {
        /*SL:122*/if (this.returnValue == null) {
            return 0L;
        }
        return (long)this.returnValue;
    }
    
    public short getReturnValueS() {
        /*SL:123*/if (this.returnValue == null) {
            return 0;
        }
        return (short)this.returnValue;
    }
    
    public boolean getReturnValueZ() {
        /*SL:124*/return this.returnValue != null && (boolean)this.returnValue;
    }
    
    static String getReturnAccessor(final Type a1) {
        /*SL:128*/if (a1.getSort() == 10 || a1.getSort() == 9) {
            /*SL:129*/return "getReturnValue";
        }
        /*SL:132*/return String.format("getReturnValue%s", a1.getDescriptor());
    }
    
    static String getReturnDescriptor(final Type a1) {
        /*SL:136*/if (a1.getSort() == 10 || a1.getSort() == 9) {
            /*SL:137*/return String.format("()%s", "Ljava/lang/Object;");
        }
        /*SL:140*/return String.format("()%s", a1.getDescriptor());
    }
}
