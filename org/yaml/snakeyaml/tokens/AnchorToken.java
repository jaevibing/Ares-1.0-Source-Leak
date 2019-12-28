package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class AnchorToken extends Token
{
    private final String value;
    
    public AnchorToken(final String a1, final Mark a2, final Mark a3) {
        super(a2, a3);
        this.value = a1;
    }
    
    public String getValue() {
        /*SL:29*/return this.value;
    }
    
    @Override
    protected String getArguments() {
        /*SL:34*/return "value=" + this.value;
    }
    
    @Override
    public ID getTokenId() {
        /*SL:39*/return ID.Anchor;
    }
}
