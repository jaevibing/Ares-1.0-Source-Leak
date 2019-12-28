package com.google.api.client.util;

import java.io.Reader;
import java.lang.reflect.Type;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.InputStream;

public interface ObjectParser
{
     <T> T parseAndClose(InputStream p0, Charset p1, Class<T> p2) throws IOException;
    
    Object parseAndClose(InputStream p0, Charset p1, Type p2) throws IOException;
    
     <T> T parseAndClose(Reader p0, Class<T> p1) throws IOException;
    
    Object parseAndClose(Reader p0, Type p1) throws IOException;
}
