package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class StringL extends ASTree
{
    protected String text;
    
    public StringL(final String a1) {
        this.text = a1;
    }
    
    public String get() {
        /*SL:31*/return this.text;
    }
    
    @Override
    public String toString() {
        /*SL:33*/return "\"" + this.text + "\"";
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:35*/a1.atStringL(this);
    }
}
