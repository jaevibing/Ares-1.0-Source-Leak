package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import org.yaml.snakeyaml.events.Event;

public interface Emitable
{
    void emit(Event p0) throws IOException;
}
