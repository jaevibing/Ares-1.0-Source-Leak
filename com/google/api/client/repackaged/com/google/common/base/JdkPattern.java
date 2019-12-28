package com.google.api.client.repackaged.com.google.common.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@GwtIncompatible
final class JdkPattern extends CommonPattern implements Serializable
{
    private final Pattern pattern;
    private static final long serialVersionUID = 0L;
    
    JdkPattern(final Pattern a1) {
        this.pattern = Preconditions.<Pattern>checkNotNull(a1);
    }
    
    @Override
    CommonMatcher matcher(final CharSequence a1) {
        /*SL:35*/return new JdkMatcher(this.pattern.matcher(a1));
    }
    
    @Override
    String pattern() {
        /*SL:40*/return this.pattern.pattern();
    }
    
    @Override
    int flags() {
        /*SL:45*/return this.pattern.flags();
    }
    
    @Override
    public String toString() {
        /*SL:50*/return this.pattern.toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:55*/return this.pattern.hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:60*/return a1 instanceof JdkPattern && /*EL:63*/this.pattern.equals(((JdkPattern)a1).pattern);
    }
    
    private static final class JdkMatcher extends CommonMatcher
    {
        final Matcher matcher;
        
        JdkMatcher(final Matcher a1) {
            this.matcher = Preconditions.<Matcher>checkNotNull(a1);
        }
        
        @Override
        boolean matches() {
            /*SL:75*/return this.matcher.matches();
        }
        
        @Override
        boolean find() {
            /*SL:80*/return this.matcher.find();
        }
        
        @Override
        boolean find(final int a1) {
            /*SL:85*/return this.matcher.find(a1);
        }
        
        @Override
        String replaceAll(final String a1) {
            /*SL:90*/return this.matcher.replaceAll(a1);
        }
        
        @Override
        int end() {
            /*SL:95*/return this.matcher.end();
        }
        
        @Override
        int start() {
            /*SL:100*/return this.matcher.start();
        }
    }
}
