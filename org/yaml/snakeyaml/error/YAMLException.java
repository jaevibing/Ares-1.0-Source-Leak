package org.yaml.snakeyaml.error;

public class YAMLException extends RuntimeException
{
    private static final long serialVersionUID = -4738336175050337570L;
    
    public YAMLException(final String a1) {
        super(a1);
    }
    
    public YAMLException(final Throwable a1) {
        super(a1);
    }
    
    public YAMLException(final String a1, final Throwable a2) {
        super(a1, a2);
    }
}
