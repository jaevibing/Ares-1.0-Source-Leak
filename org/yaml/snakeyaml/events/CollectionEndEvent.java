package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionEndEvent extends Event
{
    public CollectionEndEvent(final Mark a1, final Mark a2) {
        super(a1, a2);
    }
}
