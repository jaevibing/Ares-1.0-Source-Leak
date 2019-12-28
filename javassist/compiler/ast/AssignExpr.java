package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class AssignExpr extends Expr
{
    private AssignExpr(final int a1, final ASTree a2, final ASTList a3) {
        super(a1, a2, a3);
    }
    
    public static AssignExpr makeAssign(final int a1, final ASTree a2, final ASTree a3) {
        /*SL:35*/return new AssignExpr(a1, a2, new ASTList(a3));
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:39*/a1.atAssignExpr(this);
    }
}
