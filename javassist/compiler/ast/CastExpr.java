package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class CastExpr extends ASTList implements TokenId
{
    protected int castType;
    protected int arrayDim;
    
    public CastExpr(final ASTList a1, final int a2, final ASTree a3) {
        super(a1, new ASTList(a3));
        this.castType = 307;
        this.arrayDim = a2;
    }
    
    public CastExpr(final int a1, final int a2, final ASTree a3) {
        super(null, new ASTList(a3));
        this.castType = a1;
        this.arrayDim = a2;
    }
    
    public int getType() {
        /*SL:43*/return this.castType;
    }
    
    public int getArrayDim() {
        /*SL:45*/return this.arrayDim;
    }
    
    public ASTList getClassName() {
        /*SL:47*/return (ASTList)this.getLeft();
    }
    
    public ASTree getOprand() {
        /*SL:49*/return this.getRight().getLeft();
    }
    
    public void setOprand(final ASTree a1) {
        /*SL:51*/this.getRight().setLeft(a1);
    }
    
    public String getTag() {
        /*SL:53*/return "cast:" + this.castType + ":" + this.arrayDim;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:55*/a1.atCastExpr(this);
    }
}
