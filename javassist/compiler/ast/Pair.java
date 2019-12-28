package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class Pair extends ASTree
{
    protected ASTree left;
    protected ASTree right;
    
    public Pair(final ASTree a1, final ASTree a2) {
        this.left = a1;
        this.right = a2;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:33*/a1.atPair(this);
    }
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:36*/new StringBuffer();
        /*SL:37*/v1.append("(<Pair> ");
        /*SL:38*/v1.append((this.left == null) ? "<null>" : this.left.toString());
        /*SL:39*/v1.append(" . ");
        /*SL:40*/v1.append((this.right == null) ? "<null>" : this.right.toString());
        /*SL:41*/v1.append(')');
        /*SL:42*/return v1.toString();
    }
    
    @Override
    public ASTree getLeft() {
        /*SL:45*/return this.left;
    }
    
    @Override
    public ASTree getRight() {
        /*SL:47*/return this.right;
    }
    
    @Override
    public void setLeft(final ASTree a1) {
        /*SL:49*/this.left = a1;
    }
    
    @Override
    public void setRight(final ASTree a1) {
        /*SL:51*/this.right = a1;
    }
}
