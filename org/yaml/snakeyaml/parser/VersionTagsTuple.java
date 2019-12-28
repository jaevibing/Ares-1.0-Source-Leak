package org.yaml.snakeyaml.parser;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;

class VersionTagsTuple
{
    private DumperOptions.Version version;
    private Map<String, String> tags;
    
    public VersionTagsTuple(final DumperOptions.Version a1, final Map<String, String> a2) {
        this.version = a1;
        this.tags = a2;
    }
    
    public DumperOptions.Version getVersion() {
        /*SL:35*/return this.version;
    }
    
    public Map<String, String> getTags() {
        /*SL:39*/return this.tags;
    }
    
    @Override
    public String toString() {
        /*SL:44*/return String.format("VersionTagsTuple<%s, %s>", this.version, this.tags);
    }
}
