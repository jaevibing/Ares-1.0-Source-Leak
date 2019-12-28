package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class ScannerException extends MarkedYAMLException
{
    private static final long serialVersionUID = 4782293188600445954L;
    
    public ScannerException(final String a1, final Mark a2, final String a3, final Mark a4, final String a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public ScannerException(final String a1, final Mark a2, final String a3, final Mark a4) {
        this(a1, a2, a3, a4, (String)null);
    }
}
