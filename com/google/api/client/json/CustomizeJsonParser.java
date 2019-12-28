package com.google.api.client.json;

import java.util.Collection;
import java.lang.reflect.Field;
import com.google.api.client.util.Beta;

@Beta
public class CustomizeJsonParser
{
    public boolean stopAt(final Object a1, final String a2) {
        /*SL:46*/return false;
    }
    
    public void handleUnrecognizedKey(final Object a1, final String a2) {
    }
    
    public Collection<Object> newInstanceForArray(final Object a1, final Field a2) {
        /*SL:60*/return null;
    }
    
    public Object newInstanceForObject(final Object a1, final Class<?> a2) {
        /*SL:68*/return null;
    }
}
