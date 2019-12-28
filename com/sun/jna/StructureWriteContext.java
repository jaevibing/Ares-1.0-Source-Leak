package com.sun.jna;

import java.lang.reflect.Field;

public class StructureWriteContext extends ToNativeContext
{
    private Structure struct;
    private Field field;
    
    StructureWriteContext(final Structure a1, final Field a2) {
        this.struct = a1;
        this.field = a2;
    }
    
    public Structure getStructure() {
        /*SL:40*/return this.struct;
    }
    
    public Field getField() {
        /*SL:43*/return this.field;
    }
}
