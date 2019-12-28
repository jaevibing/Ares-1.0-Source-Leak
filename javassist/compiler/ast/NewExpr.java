package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class NewExpr extends ASTList implements TokenId
{
    protected boolean newArray;
    protected int arrayType;
    
    public NewExpr(final ASTList a1, final ASTList a2) {
        super(a1, new ASTList(a2));
        this.newArray = false;
        this.arrayType = 307;
    }
    
    public NewExpr(final int a1, final ASTList a2, final ArrayInit a3) {
        super(null, new ASTList(a2));
        this.newArray = true;
        this.arrayType = a1;
        if (a3 != null) {
            ASTList.append(this, a3);
        }
    }
    
    public static NewExpr makeObjectArray(final ASTList a1, final ASTList a2, final ArrayInit a3) {
        final NewExpr v1 = /*EL:45*/new NewExpr(a1, a2);
        /*SL:46*/v1.newArray = true;
        /*SL:47*/if (a3 != null) {
            /*SL:48*/ASTList.append(v1, a3);
        }
        /*SL:50*/return v1;
    }
    
    public boolean isArray() {
        /*SL:53*/return this.newArray;
    }
    
    public int getArrayType() {
        /*SL:57*/return this.arrayType;
    }
    
    public ASTList getClassName() {
        /*SL:59*/return (ASTList)this.getLeft();
    }
    
    public ASTList getArguments() {
        /*SL:61*/return (ASTList)this.getRight().getLeft();
    }
    
    public ASTList getArraySize() {
        /*SL:63*/return this.getArguments();
    }
    
    public ArrayInit getInitializer() {
        final ASTree v1 = /*EL:66*/this.getRight().getRight();
        /*SL:67*/if (v1 == null) {
            /*SL:68*/return null;
        }
        /*SL:70*/return (ArrayInit)v1.getLeft();
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:73*/a1.atNewExpr(this);
    }
    
    @Override
    protected String getTag() {
        /*SL:76*/return this.newArray ? "new[]" : "new";
    }
}
