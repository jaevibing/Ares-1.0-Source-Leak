package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.YAMLException;

public class ReaderException extends YAMLException
{
    private static final long serialVersionUID = 8710781187529689083L;
    private final String name;
    private final int codePoint;
    private final int position;
    
    public ReaderException(final String a1, final int a2, final int a3, final String a4) {
        super(a4);
        this.name = a1;
        this.codePoint = a3;
        this.position = a2;
    }
    
    public String getName() {
        /*SL:34*/return this.name;
    }
    
    public int getCodePoint() {
        /*SL:38*/return this.codePoint;
    }
    
    public int getPosition() {
        /*SL:42*/return this.position;
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:47*/new String(Character.toChars(this.codePoint));
        /*SL:48*/return "unacceptable code point '" + v1 + "' (0x" + /*EL:49*/Integer.toHexString(this.codePoint).toUpperCase() + ") " + this.getMessage() + "\nin \"" + this.name + "\", position " + this.position;
    }
}
