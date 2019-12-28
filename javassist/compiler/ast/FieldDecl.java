package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class FieldDecl extends ASTList
{
    public FieldDecl(final ASTree a1, final ASTList a2) {
        super(a1, a2);
    }
    
    public ASTList getModifiers() {
        /*SL:26*/return (ASTList)this.getLeft();
    }
    
    public Declarator getDeclarator() {
        /*SL:28*/return (Declarator)this.tail().head();
    }
    
    public ASTree getInit() {
        /*SL:30*/return this.sublist(2).head();
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:33*/a1.atFieldDecl(this);
    }
}
