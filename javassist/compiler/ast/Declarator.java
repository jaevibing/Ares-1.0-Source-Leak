package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Declarator extends ASTList implements TokenId
{
    protected int varType;
    protected int arrayDim;
    protected int localVar;
    protected String qualifiedClass;
    
    public Declarator(final int a1, final int a2) {
        super(null);
        this.varType = a1;
        this.arrayDim = a2;
        this.localVar = -1;
        this.qualifiedClass = null;
    }
    
    public Declarator(final ASTList a1, final int a2) {
        super(null);
        this.varType = 307;
        this.arrayDim = a2;
        this.localVar = -1;
        this.qualifiedClass = astToClassName(a1, '/');
    }
    
    public Declarator(final int a1, final String a2, final int a3, final int a4, final Symbol a5) {
        super(null);
        this.varType = a1;
        this.arrayDim = a3;
        this.localVar = a4;
        this.qualifiedClass = a2;
        this.setLeft(a5);
        ASTList.append(this, null);
    }
    
    public Declarator make(final Symbol a1, final int a2, final ASTree a3) {
        final Declarator v1 = /*EL:61*/new Declarator(this.varType, this.arrayDim + a2);
        /*SL:62*/v1.qualifiedClass = this.qualifiedClass;
        /*SL:63*/v1.setLeft(a1);
        /*SL:64*/ASTList.append(v1, a3);
        /*SL:65*/return v1;
    }
    
    public int getType() {
        /*SL:71*/return this.varType;
    }
    
    public int getArrayDim() {
        /*SL:73*/return this.arrayDim;
    }
    
    public void addArrayDim(final int a1) {
        /*SL:75*/this.arrayDim += a1;
    }
    
    public String getClassName() {
        /*SL:77*/return this.qualifiedClass;
    }
    
    public void setClassName(final String a1) {
        /*SL:79*/this.qualifiedClass = a1;
    }
    
    public Symbol getVariable() {
        /*SL:81*/return (Symbol)this.getLeft();
    }
    
    public void setVariable(final Symbol a1) {
        /*SL:83*/this.setLeft(a1);
    }
    
    public ASTree getInitializer() {
        final ASTList v1 = /*EL:86*/this.tail();
        /*SL:87*/if (v1 != null) {
            /*SL:88*/return v1.head();
        }
        /*SL:90*/return null;
    }
    
    public void setLocalVar(final int a1) {
        /*SL:93*/this.localVar = a1;
    }
    
    public int getLocalVar() {
        /*SL:95*/return this.localVar;
    }
    
    public String getTag() {
        /*SL:97*/return "decl";
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:100*/a1.atDeclarator(this);
    }
    
    public static String astToClassName(final ASTList a1, final char a2) {
        /*SL:104*/if (a1 == null) {
            /*SL:105*/return null;
        }
        final StringBuffer v1 = /*EL:107*/new StringBuffer();
        astToClassName(/*EL:108*/v1, a1, a2);
        /*SL:109*/return v1.toString();
    }
    
    private static void astToClassName(final StringBuffer a2, ASTList a3, final char v1) {
        while (true) {
            final ASTree a4 = /*EL:115*/a3.head();
            /*SL:116*/if (a4 instanceof Symbol) {
                /*SL:117*/a2.append(((Symbol)a4).get());
            }
            else/*SL:118*/ if (a4 instanceof ASTList) {
                astToClassName(/*EL:119*/a2, (ASTList)a4, v1);
            }
            /*SL:121*/a3 = a3.tail();
            /*SL:122*/if (a3 == null) {
                break;
            }
            /*SL:125*/a2.append(v1);
        }
    }
}
