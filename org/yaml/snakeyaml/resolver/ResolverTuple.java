package org.yaml.snakeyaml.resolver;

import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.Tag;

final class ResolverTuple
{
    private final Tag tag;
    private final Pattern regexp;
    
    public ResolverTuple(final Tag a1, final Pattern a2) {
        this.tag = a1;
        this.regexp = a2;
    }
    
    public Tag getTag() {
        /*SL:32*/return this.tag;
    }
    
    public Pattern getRegexp() {
        /*SL:36*/return this.regexp;
    }
    
    @Override
    public String toString() {
        /*SL:41*/return "Tuple tag=" + this.tag + " regexp=" + this.regexp;
    }
}
