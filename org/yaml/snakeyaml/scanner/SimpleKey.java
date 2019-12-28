package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.Mark;

final class SimpleKey
{
    private int tokenNumber;
    private boolean required;
    private int index;
    private int line;
    private int column;
    private Mark mark;
    
    public SimpleKey(final int a1, final boolean a2, final int a3, final int a4, final int a5, final Mark a6) {
        this.tokenNumber = a1;
        this.required = a2;
        this.index = a3;
        this.line = a4;
        this.column = a5;
        this.mark = a6;
    }
    
    public int getTokenNumber() {
        /*SL:46*/return this.tokenNumber;
    }
    
    public int getColumn() {
        /*SL:50*/return this.column;
    }
    
    public Mark getMark() {
        /*SL:54*/return this.mark;
    }
    
    public int getIndex() {
        /*SL:58*/return this.index;
    }
    
    public int getLine() {
        /*SL:62*/return this.line;
    }
    
    public boolean isRequired() {
        /*SL:66*/return this.required;
    }
    
    @Override
    public String toString() {
        /*SL:71*/return "SimpleKey - tokenNumber=" + this.tokenNumber + " required=" + this.required + " index=" + this.index + " line=" + this.line + " column=" + this.column;
    }
}
