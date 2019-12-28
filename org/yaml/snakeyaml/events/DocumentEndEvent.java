package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class DocumentEndEvent extends Event
{
    private final boolean explicit;
    
    public DocumentEndEvent(final Mark a1, final Mark a2, final boolean a3) {
        super(a1, a2);
        this.explicit = a3;
    }
    
    public boolean getExplicit() {
        /*SL:35*/return this.explicit;
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:40*/return ID.DocumentEnd == a1;
    }
}
