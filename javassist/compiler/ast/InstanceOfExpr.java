package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class InstanceOfExpr extends CastExpr
{
    public InstanceOfExpr(final ASTList a1, final int a2, final ASTree a3) {
        super(a1, a2, a3);
    }
    
    public InstanceOfExpr(final int a1, final int a2, final ASTree a3) {
        super(a1, a2, a3);
    }
    
    @Override
    public String getTag() {
        /*SL:34*/return "instanceof:" + this.castType + ":" + this.arrayDim;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:38*/a1.atInstanceOfExpr(this);
    }
}
