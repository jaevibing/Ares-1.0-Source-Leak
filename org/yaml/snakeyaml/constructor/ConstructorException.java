package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ConstructorException extends MarkedYAMLException
{
    private static final long serialVersionUID = -8816339931365239910L;
    
    protected ConstructorException(final String a1, final Mark a2, final String a3, final Mark a4, final Throwable a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    protected ConstructorException(final String a1, final Mark a2, final String a3, final Mark a4) {
        this(a1, a2, a3, a4, (Throwable)null);
    }
}
