package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ArrayInit extends ASTList
{
    public ArrayInit(final ASTree a1) {
        super(a1);
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:29*/a1.atArrayInit(this);
    }
    
    public String getTag() {
        /*SL:31*/return "array";
    }
}
