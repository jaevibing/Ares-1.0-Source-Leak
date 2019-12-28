package javassist.compiler;

import javassist.compiler.ast.ASTree;

public class NoFieldException extends CompileError
{
    private String fieldName;
    private ASTree expr;
    
    public NoFieldException(final String a1, final ASTree a2) {
        super("no such field: " + a1);
        this.fieldName = a1;
        this.expr = a2;
    }
    
    public String getField() {
        /*SL:35*/return this.fieldName;
    }
    
    public ASTree getExpr() {
        /*SL:39*/return this.expr;
    }
}
