package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class ScalarToken extends Token
{
    private final String value;
    private final boolean plain;
    private final char style;
    
    public ScalarToken(final String a1, final Mark a2, final Mark a3, final boolean a4) {
        this(a1, a4, a2, a3, '\0');
    }
    
    public ScalarToken(final String a1, final boolean a2, final Mark a3, final Mark a4, final char a5) {
        super(a3, a4);
        this.value = a1;
        this.plain = a2;
        this.style = a5;
    }
    
    public boolean getPlain() {
        /*SL:37*/return this.plain;
    }
    
    public String getValue() {
        /*SL:41*/return this.value;
    }
    
    public char getStyle() {
        /*SL:45*/return this.style;
    }
    
    @Override
    protected String getArguments() {
        /*SL:50*/return "value=" + this.value + ", plain=" + this.plain + ", style=" + this.style;
    }
    
    @Override
    public ID getTokenId() {
        /*SL:55*/return ID.Scalar;
    }
}
