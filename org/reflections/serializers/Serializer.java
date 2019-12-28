package org.reflections.serializers;

import java.io.File;
import org.reflections.Reflections;
import java.io.InputStream;

public interface Serializer
{
    Reflections read(InputStream p0);
    
    File save(Reflections p0, String p1);
    
    String toString(Reflections p0);
}
