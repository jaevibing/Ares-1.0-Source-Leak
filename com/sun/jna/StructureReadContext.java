package com.sun.jna;

import java.lang.reflect.Field;

public class StructureReadContext extends FromNativeContext
{
    private Structure structure;
    private Field field;
    
    StructureReadContext(final Structure a1, final Field a2) {
        super(a2.getType());
        this.structure = a1;
        this.field = a2;
    }
    
    public Structure getStructure() {
        /*SL:41*/return this.structure;
    }
    
    public Field getField() {
        /*SL:43*/return this.field;
    }
}
