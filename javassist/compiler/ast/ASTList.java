package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ASTList extends ASTree
{
    private ASTree left;
    private ASTList right;
    
    public ASTList(final ASTree a1, final ASTList a2) {
        this.left = a1;
        this.right = a2;
    }
    
    public ASTList(final ASTree a1) {
        this.left = a1;
        this.right = null;
    }
    
    public static ASTList make(final ASTree a1, final ASTree a2, final ASTree a3) {
        /*SL:40*/return new ASTList(a1, new ASTList(a2, new ASTList(a3)));
    }
    
    @Override
    public ASTree getLeft() {
        /*SL:43*/return this.left;
    }
    
    @Override
    public ASTree getRight() {
        /*SL:45*/return this.right;
    }
    
    @Override
    public void setLeft(final ASTree a1) {
        /*SL:47*/this.left = a1;
    }
    
    @Override
    public void setRight(final ASTree a1) {
        /*SL:50*/this.right = (ASTList)a1;
    }
    
    public ASTree head() {
        /*SL:56*/return this.left;
    }
    
    public void setHead(final ASTree a1) {
        /*SL:59*/this.left = a1;
    }
    
    public ASTList tail() {
        /*SL:65*/return this.right;
    }
    
    public void setTail(final ASTList a1) {
        /*SL:68*/this.right = a1;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:71*/a1.atASTList(this);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = /*EL:74*/new StringBuffer();
        /*SL:75*/sb.append("(<");
        /*SL:76*/sb.append(this.getTag());
        /*SL:77*/sb.append('>');
        /*SL:79*/for (ASTList v0 = this; v0 != null; /*SL:83*/v0 = v0.right) {
            sb.append(' ');
            final ASTree v = v0.left;
            sb.append((v == null) ? "<null>" : v.toString());
        }
        /*SL:86*/sb.append(')');
        /*SL:87*/return sb.toString();
    }
    
    public int length() {
        /*SL:94*/return length(this);
    }
    
    public static int length(ASTList a1) {
        /*SL:98*/if (a1 == null) {
            /*SL:99*/return 0;
        }
        int v1;
        /*SL:102*/for (v1 = 0; a1 != null; /*SL:103*/a1 = a1.right, /*SL:104*/++v1) {}
        /*SL:107*/return v1;
    }
    
    public ASTList sublist(int a1) {
        ASTList v1 = /*EL:117*/this;
        /*SL:118*/while (a1-- > 0) {
            /*SL:119*/v1 = v1.right;
        }
        /*SL:121*/return v1;
    }
    
    public boolean subst(final ASTree v1, final ASTree v2) {
        /*SL:129*/for (ASTList a1 = this; a1 != null; a1 = a1.right) {
            /*SL:130*/if (a1.left == v2) {
                /*SL:131*/a1.left = v1;
                /*SL:132*/return true;
            }
        }
        /*SL:135*/return false;
    }
    
    public static ASTList append(final ASTList a1, final ASTree a2) {
        /*SL:142*/return concat(a1, new ASTList(a2));
    }
    
    public static ASTList concat(final ASTList a2, final ASTList v1) {
        /*SL:149*/if (a2 == null) {
            /*SL:150*/return v1;
        }
        ASTList a3;
        /*SL:153*/for (a3 = a2; a3.right != null; /*SL:154*/a3 = a3.right) {}
        /*SL:156*/a3.right = v1;
        /*SL:157*/return a2;
    }
}
