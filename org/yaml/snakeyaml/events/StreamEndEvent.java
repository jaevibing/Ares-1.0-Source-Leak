package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class StreamEndEvent extends Event
{
    public StreamEndEvent(final Mark a1, final Mark a2) {
        super(a1, a2);
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:38*/return ID.StreamEnd == a1;
    }
}
