package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class MappingEndEvent extends CollectionEndEvent
{
    public MappingEndEvent(final Mark a1, final Mark a2) {
        super(a1, a2);
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:33*/return ID.MappingEnd == a1;
    }
}
