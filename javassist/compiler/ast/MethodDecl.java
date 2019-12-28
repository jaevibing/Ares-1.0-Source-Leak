package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class MethodDecl extends ASTList
{
    public static final String initName = "<init>";
    
    public MethodDecl(final ASTree a1, final ASTList a2) {
        super(a1, a2);
    }
    
    public boolean isConstructor() {
        final Symbol v1 = /*EL:29*/this.getReturn().getVariable();
        /*SL:30*/return v1 != null && "<init>".equals(v1.get());
    }
    
    public ASTList getModifiers() {
        /*SL:33*/return (ASTList)this.getLeft();
    }
    
    public Declarator getReturn() {
        /*SL:35*/return (Declarator)this.tail().head();
    }
    
    public ASTList getParams() {
        /*SL:37*/return (ASTList)this.sublist(2).head();
    }
    
    public ASTList getThrows() {
        /*SL:39*/return (ASTList)this.sublist(3).head();
    }
    
    public Stmnt getBody() {
        /*SL:41*/return (Stmnt)this.sublist(4).head();
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:44*/a1.atMethodDecl(this);
    }
}
