package com.fasterxml.jackson.core;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.OutputStream;

public interface SerializableString
{
    String getValue();
    
    int charLength();
    
    char[] asQuotedChars();
    
    byte[] asUnquotedUTF8();
    
    byte[] asQuotedUTF8();
    
    int appendQuotedUTF8(byte[] p0, int p1);
    
    int appendQuoted(char[] p0, int p1);
    
    int appendUnquotedUTF8(byte[] p0, int p1);
    
    int appendUnquoted(char[] p0, int p1);
    
    int writeQuotedUTF8(OutputStream p0) throws IOException;
    
    int writeUnquotedUTF8(OutputStream p0) throws IOException;
    
    int putQuotedUTF8(ByteBuffer p0) throws IOException;
    
    int putUnquotedUTF8(ByteBuffer p0) throws IOException;
}
