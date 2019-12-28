package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class Event
{
    private final Mark startMark;
    private final Mark endMark;
    
    public Event(final Mark a1, final Mark a2) {
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
        /*SL:55*/return "";
    }
    
    public abstract boolean is(final ID p0);
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:65*/return a1 instanceof Event && /*EL:66*/this.toString().equals(a1.toString());
    }
    
    @Override
    public int hashCode() {
        /*SL:77*/return this.toString().hashCode();
    }
    
    public enum ID
    {
        Alias, 
        DocumentEnd, 
        DocumentStart, 
        MappingEnd, 
        MappingStart, 
        Scalar, 
        SequenceEnd, 
        SequenceStart, 
        StreamEnd, 
        StreamStart;
    }
}
