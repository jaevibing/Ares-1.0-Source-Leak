package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;

public final class DocumentStartEvent extends Event
{
    private final boolean explicit;
    private final DumperOptions.Version version;
    private final Map<String, String> tags;
    
    public DocumentStartEvent(final Mark a1, final Mark a2, final boolean a3, final DumperOptions.Version a4, final Map<String, String> a5) {
        super(a1, a2);
        this.explicit = a3;
        this.version = a4;
        this.tags = a5;
    }
    
    public boolean getExplicit() {
        /*SL:47*/return this.explicit;
    }
    
    public DumperOptions.Version getVersion() {
        /*SL:59*/return this.version;
    }
    
    public Map<String, String> getTags() {
        /*SL:69*/return this.tags;
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:74*/return ID.DocumentStart == a1;
    }
}
