package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Variable extends Symbol
{
    protected Declarator declarator;
    
    public Variable(final String a1, final Declarator a2) {
        super(a1);
        this.declarator = a2;
    }
    
    public Declarator getDeclarator() {
        /*SL:32*/return this.declarator;
    }
    
    @Override
    public String toString() {
        /*SL:35*/return this.identifier + ":" + this.declarator.getType();
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:38*/a1.atVariable(this);
    }
}
