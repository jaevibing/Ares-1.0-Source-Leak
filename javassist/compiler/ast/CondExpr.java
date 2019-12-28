package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class CondExpr extends ASTList
{
    public CondExpr(final ASTree a1, final ASTree a2, final ASTree a3) {
        super(a1, new ASTList(a2, new ASTList(a3)));
    }
    
    public ASTree condExpr() {
        /*SL:29*/return this.head();
    }
    
    public void setCond(final ASTree a1) {
        /*SL:31*/this.setHead(a1);
    }
    
    public ASTree thenExpr() {
        /*SL:33*/return this.tail().head();
    }
    
    public void setThen(final ASTree a1) {
        /*SL:35*/this.tail().setHead(a1);
    }
    
    public ASTree elseExpr() {
        /*SL:37*/return this.tail().tail().head();
    }
    
    public void setElse(final ASTree a1) {
        /*SL:39*/this.tail().tail().setHead(a1);
    }
    
    public String getTag() {
        /*SL:41*/return "?:";
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:43*/a1.atCondExpr(this);
    }
}
