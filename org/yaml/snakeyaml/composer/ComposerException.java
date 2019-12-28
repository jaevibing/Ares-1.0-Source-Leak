package org.yaml.snakeyaml.composer;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ComposerException extends MarkedYAMLException
{
    private static final long serialVersionUID = 2146314636913113935L;
    
    protected ComposerException(final String a1, final Mark a2, final String a3, final Mark a4) {
        super(a1, a2, a3, a4);
    }
}
