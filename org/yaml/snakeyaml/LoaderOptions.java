package org.yaml.snakeyaml;

public class LoaderOptions
{
    private boolean allowDuplicateKeys;
    
    public LoaderOptions() {
        this.allowDuplicateKeys = true;
    }
    
    public boolean isAllowDuplicateKeys() {
        /*SL:23*/return this.allowDuplicateKeys;
    }
    
    public void setAllowDuplicateKeys(final boolean a1) {
        /*SL:42*/this.allowDuplicateKeys = a1;
    }
}
