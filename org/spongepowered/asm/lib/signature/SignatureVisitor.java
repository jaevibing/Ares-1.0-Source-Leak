package org.spongepowered.asm.lib.signature;

public abstract class SignatureVisitor
{
    public static final char EXTENDS = '+';
    public static final char SUPER = '-';
    public static final char INSTANCEOF = '=';
    protected final int api;
    
    public SignatureVisitor(final int a1) {
        if (a1 != 262144 && a1 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = a1;
    }
    
    public void visitFormalTypeParameter(final String a1) {
    }
    
    public SignatureVisitor visitClassBound() {
        /*SL:109*/return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        /*SL:118*/return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        /*SL:128*/return this;
    }
    
    public SignatureVisitor visitInterface() {
        /*SL:137*/return this;
    }
    
    public SignatureVisitor visitParameterType() {
        /*SL:146*/return this;
    }
    
    public SignatureVisitor visitReturnType() {
        /*SL:155*/return this;
    }
    
    public SignatureVisitor visitExceptionType() {
        /*SL:164*/return this;
    }
    
    public void visitBaseType(final char a1) {
    }
    
    public void visitTypeVariable(final String a1) {
    }
    
    public SignatureVisitor visitArrayType() {
        /*SL:193*/return this;
    }
    
    public void visitClassType(final String a1) {
    }
    
    public void visitInnerClassType(final String a1) {
    }
    
    public void visitTypeArgument() {
    }
    
    public SignatureVisitor visitTypeArgument(final char a1) {
        /*SL:230*/return this;
    }
    
    public void visitEnd() {
    }
}
