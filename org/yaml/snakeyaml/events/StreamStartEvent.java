package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class StreamStartEvent extends Event
{
    public StreamStartEvent(final Mark a1, final Mark a2) {
        super(a1, a2);
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:39*/return ID.StreamStart == a1;
    }
}
