package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Keyword extends ASTree
{
    protected int tokenId;
    
    public Keyword(final int a1) {
        this.tokenId = a1;
    }
    
    public int get() {
        /*SL:31*/return this.tokenId;
    }
    
    @Override
    public String toString() {
        /*SL:33*/return "id:" + this.tokenId;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:35*/a1.atKeyword(this);
    }
}
