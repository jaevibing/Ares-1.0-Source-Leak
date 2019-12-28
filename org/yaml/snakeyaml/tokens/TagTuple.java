package org.yaml.snakeyaml.tokens;

public final class TagTuple
{
    private final String handle;
    private final String suffix;
    
    public TagTuple(final String a1, final String a2) {
        if (a2 == null) {
            throw new NullPointerException("Suffix must be provided.");
        }
        this.handle = a1;
        this.suffix = a2;
    }
    
    public String getHandle() {
        /*SL:31*/return this.handle;
    }
    
    public String getSuffix() {
        /*SL:35*/return this.suffix;
    }
}
