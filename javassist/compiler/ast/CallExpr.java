package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.MemberResolver;

public class CallExpr extends Expr
{
    private MemberResolver.Method method;
    
    private CallExpr(final ASTree a1, final ASTList a2) {
        super(67, a1, a2);
        this.method = null;
    }
    
    public void setMethod(final MemberResolver.Method a1) {
        /*SL:35*/this.method = a1;
    }
    
    public MemberResolver.Method getMethod() {
        /*SL:39*/return this.method;
    }
    
    public static CallExpr makeCall(final ASTree a1, final ASTree a2) {
        /*SL:43*/return new CallExpr(a1, new ASTList(a2));
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:46*/a1.atCallExpr(this);
    }
}
