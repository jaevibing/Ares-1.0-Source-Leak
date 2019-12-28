package javassist.compiler.ast;

import javassist.compiler.CompileError;
import java.io.Serializable;

public abstract class ASTree implements Serializable
{
    public ASTree getLeft() {
        /*SL:28*/return null;
    }
    
    public ASTree getRight() {
        /*SL:30*/return null;
    }
    
    public void setLeft(final ASTree a1) {
    }
    
    public void setRight(final ASTree a1) {
    }
    
    public abstract void accept(final Visitor p0) throws CompileError;
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:44*/new StringBuffer();
        /*SL:45*/v1.append('<');
        /*SL:46*/v1.append(this.getTag());
        /*SL:47*/v1.append('>');
        /*SL:48*/return v1.toString();
    }
    
    protected String getTag() {
        final String v1 = /*EL:56*/this.getClass().getName();
        /*SL:57*/return v1.substring(v1.lastIndexOf(46) + 1);
    }
}
