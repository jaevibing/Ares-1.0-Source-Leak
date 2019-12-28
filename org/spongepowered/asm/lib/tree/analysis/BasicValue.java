package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.Type;

public class BasicValue implements Value
{
    public static final BasicValue UNINITIALIZED_VALUE;
    public static final BasicValue INT_VALUE;
    public static final BasicValue FLOAT_VALUE;
    public static final BasicValue LONG_VALUE;
    public static final BasicValue DOUBLE_VALUE;
    public static final BasicValue REFERENCE_VALUE;
    public static final BasicValue RETURNADDRESS_VALUE;
    private final Type type;
    
    public BasicValue(final Type a1) {
        this.type = a1;
    }
    
    public Type getType() {
        /*SL:67*/return this.type;
    }
    
    public int getSize() {
        /*SL:71*/return (this.type == Type.LONG_TYPE || this.type == Type.DOUBLE_TYPE) ? 2 : 1;
    }
    
    public boolean isReference() {
        /*SL:75*/return this.type != null && (this.type.getSort() == /*EL:76*/10 || this.type.getSort() == 9);
    }
    
    public boolean equals(final Object a1) {
        /*SL:81*/if (a1 == this) {
            /*SL:82*/return true;
        }
        /*SL:83*/if (!(a1 instanceof BasicValue)) {
            /*SL:90*/return false;
        }
        if (this.type == null) {
            return ((BasicValue)a1).type == null;
        }
        return this.type.equals(((BasicValue)a1).type);
    }
    
    public int hashCode() {
        /*SL:96*/return (this.type == null) ? 0 : this.type.hashCode();
    }
    
    public String toString() {
        /*SL:101*/if (this == BasicValue.UNINITIALIZED_VALUE) {
            /*SL:102*/return ".";
        }
        /*SL:103*/if (this == BasicValue.RETURNADDRESS_VALUE) {
            /*SL:104*/return "A";
        }
        /*SL:105*/if (this == BasicValue.REFERENCE_VALUE) {
            /*SL:106*/return "R";
        }
        /*SL:108*/return this.type.getDescriptor();
    }
    
    static {
        UNINITIALIZED_VALUE = new BasicValue(null);
        INT_VALUE = new BasicValue(Type.INT_TYPE);
        FLOAT_VALUE = new BasicValue(Type.FLOAT_TYPE);
        LONG_VALUE = new BasicValue(Type.LONG_TYPE);
        DOUBLE_VALUE = new BasicValue(Type.DOUBLE_TYPE);
        REFERENCE_VALUE = new BasicValue(Type.getObjectType("java/lang/Object"));
        RETURNADDRESS_VALUE = new BasicValue(Type.VOID_TYPE);
    }
}
