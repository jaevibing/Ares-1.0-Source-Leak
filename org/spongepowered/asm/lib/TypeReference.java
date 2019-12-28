package org.spongepowered.asm.lib;

public class TypeReference
{
    public static final int CLASS_TYPE_PARAMETER = 0;
    public static final int METHOD_TYPE_PARAMETER = 1;
    public static final int CLASS_EXTENDS = 16;
    public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
    public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
    public static final int FIELD = 19;
    public static final int METHOD_RETURN = 20;
    public static final int METHOD_RECEIVER = 21;
    public static final int METHOD_FORMAL_PARAMETER = 22;
    public static final int THROWS = 23;
    public static final int LOCAL_VARIABLE = 64;
    public static final int RESOURCE_VARIABLE = 65;
    public static final int EXCEPTION_PARAMETER = 66;
    public static final int INSTANCEOF = 67;
    public static final int NEW = 68;
    public static final int CONSTRUCTOR_REFERENCE = 69;
    public static final int METHOD_REFERENCE = 70;
    public static final int CAST = 71;
    public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
    public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
    public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
    public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
    private int value;
    
    public TypeReference(final int a1) {
        this.value = a1;
    }
    
    public static TypeReference newTypeReference(final int a1) {
        /*SL:207*/return new TypeReference(a1 << 24);
    }
    
    public static TypeReference newTypeParameterReference(final int a1, final int a2) {
        /*SL:222*/return new TypeReference(a1 << 24 | a2 << 16);
    }
    
    public static TypeReference newTypeParameterBoundReference(final int a1, final int a2, final int a3) {
        /*SL:241*/return new TypeReference(a1 << 24 | a2 << 16 | a3 << 8);
    }
    
    public static TypeReference newSuperTypeReference(int a1) {
        /*SL:255*/a1 &= 0xFFFF;
        /*SL:256*/return new TypeReference(0x10000000 | a1 << 8);
    }
    
    public static TypeReference newFormalParameterReference(final int a1) {
        /*SL:268*/return new TypeReference(0x16000000 | a1 << 16);
    }
    
    public static TypeReference newExceptionReference(final int a1) {
        /*SL:282*/return new TypeReference(0x17000000 | a1 << 8);
    }
    
    public static TypeReference newTryCatchReference(final int a1) {
        /*SL:296*/return new TypeReference(0x42000000 | a1 << 8);
    }
    
    public static TypeReference newTypeArgumentReference(final int a1, final int a2) {
        /*SL:320*/return new TypeReference(a1 << 24 | a2);
    }
    
    public int getSort() {
        /*SL:350*/return this.value >>> 24;
    }
    
    public int getTypeParameterIndex() {
        /*SL:364*/return (this.value & 0xFF0000) >> 16;
    }
    
    public int getTypeParameterBoundIndex() {
        /*SL:377*/return (this.value & 0xFF00) >> 8;
    }
    
    public int getSuperTypeIndex() {
        /*SL:390*/return (short)((this.value & 0xFFFF00) >> 8);
    }
    
    public int getFormalParameterIndex() {
        /*SL:401*/return (this.value & 0xFF0000) >> 16;
    }
    
    public int getExceptionIndex() {
        /*SL:412*/return (this.value & 0xFFFF00) >> 8;
    }
    
    public int getTryCatchBlockIndex() {
        /*SL:424*/return (this.value & 0xFFFF00) >> 8;
    }
    
    public int getTypeArgumentIndex() {
        /*SL:440*/return this.value & 0xFF;
    }
    
    public int getValue() {
        /*SL:450*/return this.value;
    }
}
