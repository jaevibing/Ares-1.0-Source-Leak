package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Stmnt extends ASTList implements TokenId
{
    protected int operatorId;
    
    public Stmnt(final int a1, final ASTree a2, final ASTList a3) {
        super(a2, a3);
        this.operatorId = a1;
    }
    
    public Stmnt(final int a1, final ASTree a2) {
        super(a2);
        this.operatorId = a1;
    }
    
    public Stmnt(final int a1) {
        this(a1, null);
    }
    
    public static Stmnt make(final int a1, final ASTree a2, final ASTree a3) {
        /*SL:43*/return new Stmnt(a1, a2, new ASTList(a3));
    }
    
    public static Stmnt make(final int a1, final ASTree a2, final ASTree a3, final ASTree a4) {
        /*SL:47*/return new Stmnt(a1, a2, new ASTList(a3, new ASTList(a4)));
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:50*/a1.atStmnt(this);
    }
    
    public int getOperator() {
        /*SL:52*/return this.operatorId;
    }
    
    @Override
    protected String getTag() {
        /*SL:55*/if (this.operatorId < 128) {
            /*SL:56*/return "stmnt:" + (char)this.operatorId;
        }
        /*SL:58*/return "stmnt:" + this.operatorId;
    }
}
