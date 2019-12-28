package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Expr extends ASTList implements TokenId
{
    protected int operatorId;
    
    Expr(final int a1, final ASTree a2, final ASTList a3) {
        super(a2, a3);
        this.operatorId = a1;
    }
    
    Expr(final int a1, final ASTree a2) {
        super(a2);
        this.operatorId = a1;
    }
    
    public static Expr make(final int a1, final ASTree a2, final ASTree a3) {
        /*SL:45*/return new Expr(a1, a2, new ASTList(a3));
    }
    
    public static Expr make(final int a1, final ASTree a2) {
        /*SL:49*/return new Expr(a1, a2);
    }
    
    public int getOperator() {
        /*SL:52*/return this.operatorId;
    }
    
    public void setOperator(final int a1) {
        /*SL:54*/this.operatorId = a1;
    }
    
    public ASTree oprand1() {
        /*SL:56*/return this.getLeft();
    }
    
    public void setOprand1(final ASTree a1) {
        /*SL:59*/this.setLeft(a1);
    }
    
    public ASTree oprand2() {
        /*SL:62*/return this.getRight().getLeft();
    }
    
    public void setOprand2(final ASTree a1) {
        /*SL:65*/this.getRight().setLeft(a1);
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:68*/a1.atExpr(this);
    }
    
    public String getName() {
        final int v1 = /*EL:71*/this.operatorId;
        /*SL:72*/if (v1 < 128) {
            /*SL:73*/return String.valueOf((char)v1);
        }
        /*SL:74*/if (350 <= v1 && v1 <= 371) {
            /*SL:75*/return Expr.opNames[v1 - 350];
        }
        /*SL:76*/if (v1 == 323) {
            /*SL:77*/return "instanceof";
        }
        /*SL:79*/return String.valueOf(v1);
    }
    
    @Override
    protected String getTag() {
        /*SL:83*/return "op:" + this.getName();
    }
}
