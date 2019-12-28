package com.sun.jna;

public final class WString implements CharSequence, Comparable
{
    private String string;
    
    public WString(final String a1) {
        if (a1 == null) {
            throw new NullPointerException("String initializer must be non-null");
        }
        this.string = a1;
    }
    
    @Override
    public String toString() {
        /*SL:39*/return this.string;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:43*/return a1 instanceof WString && this.toString().equals(a1.toString());
    }
    
    @Override
    public int hashCode() {
        /*SL:47*/return this.toString().hashCode();
    }
    
    @Override
    public int compareTo(final Object a1) {
        /*SL:51*/return this.toString().compareTo(a1.toString());
    }
    
    @Override
    public int length() {
        /*SL:55*/return this.toString().length();
    }
    
    @Override
    public char charAt(final int a1) {
        /*SL:59*/return this.toString().charAt(a1);
    }
    
    @Override
    public CharSequence subSequence(final int a1, final int a2) {
        /*SL:63*/return this.toString().subSequence(a1, a2);
    }
}
