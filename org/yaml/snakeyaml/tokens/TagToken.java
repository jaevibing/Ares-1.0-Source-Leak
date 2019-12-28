package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class TagToken extends Token
{
    private final TagTuple value;
    
    public TagToken(final TagTuple a1, final Mark a2, final Mark a3) {
        super(a2, a3);
        this.value = a1;
    }
    
    public TagTuple getValue() {
        /*SL:29*/return this.value;
    }
    
    @Override
    protected String getArguments() {
        /*SL:34*/return "value=[" + this.value.getHandle() + ", " + this.value.getSuffix() + "]";
    }
    
    @Override
    public ID getTokenId() {
        /*SL:39*/return ID.Tag;
    }
}
