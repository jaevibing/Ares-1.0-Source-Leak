package com.sun.jna;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public interface Callback
{
    public static final String METHOD_NAME = "callback";
    public static final List<String> FORBIDDEN_NAMES = Collections.<String>unmodifiableList((List<? extends String>)Arrays.<? extends T>asList("hashCode", "equals", "toString"));
    
    public interface UncaughtExceptionHandler
    {
        void uncaughtException(Callback p0, Throwable p1);
    }
}
