package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.error.Mark;

public abstract class Token
{
    private final Mark startMark;
    private final Mark endMark;
    
    public Token(final Mark a1, final Mark a2) {
        if (a1 == null || a2 == null) {
            throw new YAMLException("Token requires marks.");
        }
        this.startMark = a1;
        this.endMark = a2;
    }
    
    @Override
    public String toString() {
        /*SL:38*/return "<" + this.getClass().getName() + "(" + this.getArguments() + ")>";
    }
    
    public Mark getStartMark() {
        /*SL:42*/return this.startMark;
    }
    
    public Mark getEndMark() {
        /*SL:46*/return this.endMark;
    }
    
    protected String getArguments() {
        /*SL:54*/return "";
    }
    
    public abstract ID getTokenId();
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:70*/return a1 instanceof Token && /*EL:71*/this.toString().equals(a1.toString());
    }
    
    @Override
    public int hashCode() {
        /*SL:82*/return this.toString().hashCode();
    }
    
    public enum ID
    {
        Alias, 
        Anchor, 
        BlockEnd, 
        BlockEntry, 
        BlockMappingStart, 
        BlockSequenceStart, 
        Directive, 
        DocumentEnd, 
        DocumentStart, 
        FlowEntry, 
        FlowMappingEnd, 
        FlowMappingStart, 
        FlowSequenceEnd, 
        FlowSequenceStart, 
        Key, 
        Scalar, 
        StreamEnd, 
        StreamStart, 
        Tag, 
        Value, 
        Whitespace, 
        Comment, 
        Error;
    }
}
