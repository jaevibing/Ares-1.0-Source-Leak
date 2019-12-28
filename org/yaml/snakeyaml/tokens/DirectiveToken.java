package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.error.Mark;
import java.util.List;

public final class DirectiveToken<T> extends Token
{
    private final String name;
    private final List<T> value;
    
    public DirectiveToken(final String a1, final List<T> a2, final Mark a3, final Mark a4) {
        super(a3, a4);
        this.name = a1;
        if (a2 != null && a2.size() != 2) {
            throw new YAMLException("Two strings must be provided instead of " + String.valueOf(a2.size()));
        }
        this.value = a2;
    }
    
    public String getName() {
        /*SL:38*/return this.name;
    }
    
    public List<T> getValue() {
        /*SL:42*/return this.value;
    }
    
    @Override
    protected String getArguments() {
        /*SL:47*/if (this.value != null) {
            /*SL:48*/return "name=" + this.name + ", value=[" + this.value.get(0) + ", " + this.value.get(1) + "]";
        }
        /*SL:50*/return "name=" + this.name;
    }
    
    @Override
    public ID getTokenId() {
        /*SL:56*/return ID.Directive;
    }
}
