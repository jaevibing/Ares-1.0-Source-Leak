package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class AliasEvent extends NodeEvent
{
    public AliasEvent(final String a1, final Mark a2, final Mark a3) {
        super(a1, a2, a3);
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:30*/return ID.Alias == a1;
    }
}
