package com.fasterxml.jackson.core.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Comparable<TypeReference<T>>
{
    protected final Type _type;
    
    protected TypeReference() {
        final Type v1 = this.getClass().getGenericSuperclass();
        if (v1 instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        this._type = ((ParameterizedType)v1).getActualTypeArguments()[0];
    }
    
    public Type getType() {
        /*SL:47*/return this._type;
    }
    
    @Override
    public int compareTo(final TypeReference<T> a1) {
        /*SL:55*/return 0;
    }
}
