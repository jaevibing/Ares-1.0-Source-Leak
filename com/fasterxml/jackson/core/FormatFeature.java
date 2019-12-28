package com.fasterxml.jackson.core;

public interface FormatFeature
{
    boolean enabledByDefault();
    
    int getMask();
    
    boolean enabledIn(int p0);
}
