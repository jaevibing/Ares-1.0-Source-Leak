package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class CompactData
{
    private String prefix;
    private List<String> arguments;
    private Map<String, String> properties;
    
    public CompactData(final String a1) {
        this.arguments = new ArrayList<String>();
        this.properties = new HashMap<String, String>();
        this.prefix = a1;
    }
    
    public String getPrefix() {
        /*SL:33*/return this.prefix;
    }
    
    public Map<String, String> getProperties() {
        /*SL:37*/return this.properties;
    }
    
    public List<String> getArguments() {
        /*SL:41*/return this.arguments;
    }
    
    @Override
    public String toString() {
        /*SL:46*/return "CompactData: " + this.prefix + " " + this.properties;
    }
}
