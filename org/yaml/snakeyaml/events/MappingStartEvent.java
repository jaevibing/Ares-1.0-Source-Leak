package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class MappingStartEvent extends CollectionStartEvent
{
    public MappingStartEvent(final String a1, final String a2, final boolean a3, final Mark a4, final Mark a5, final Boolean a6) {
        super(a1, a2, a3, a4, a5, a6);
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:42*/return ID.MappingStart == a1;
    }
}
