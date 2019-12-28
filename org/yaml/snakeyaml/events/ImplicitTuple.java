package org.yaml.snakeyaml.events;

public class ImplicitTuple
{
    private final boolean plain;
    private final boolean nonPlain;
    
    public ImplicitTuple(final boolean a1, final boolean a2) {
        this.plain = a1;
        this.nonPlain = a2;
    }
    
    public boolean canOmitTagInPlainScalar() {
        /*SL:39*/return this.plain;
    }
    
    public boolean canOmitTagInNonPlainScalar() {
        /*SL:47*/return this.nonPlain;
    }
    
    public boolean bothFalse() {
        /*SL:51*/return !this.plain && !this.nonPlain;
    }
    
    @Override
    public String toString() {
        /*SL:56*/return "implicit=[" + this.plain + ", " + this.nonPlain + "]";
    }
}
