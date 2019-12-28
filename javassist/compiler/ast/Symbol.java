package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Symbol extends ASTree
{
    protected String identifier;
    
    public Symbol(final String a1) {
        this.identifier = a1;
    }
    
    public String get() {
        /*SL:31*/return this.identifier;
    }
    
    @Override
    public String toString() {
        /*SL:33*/return this.identifier;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:35*/a1.atSymbol(this);
    }
}
