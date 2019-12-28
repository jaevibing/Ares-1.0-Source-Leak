package com.ares.utils.throwables;

public class NoStackTraceThrowable extends Error
{
    public NoStackTraceThrowable() {
        this("");
    }
    
    public NoStackTraceThrowable(final String a1) {
        super(a1);
        this.setStackTrace(new StackTraceElement[0]);
    }
    
    @Override
    public String toString() {
        /*SL:22*/return "Go away john";
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        /*SL:26*/return this;
    }
}
