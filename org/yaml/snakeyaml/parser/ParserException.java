package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ParserException extends MarkedYAMLException
{
    private static final long serialVersionUID = -2349253802798398038L;
    
    public ParserException(final String a1, final Mark a2, final String a3, final Mark a4) {
        super(a1, a2, a3, a4, null, null);
    }
}
