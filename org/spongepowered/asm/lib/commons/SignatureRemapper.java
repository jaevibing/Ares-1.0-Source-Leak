package org.spongepowered.asm.lib.commons;

import java.util.Stack;
import org.spongepowered.asm.lib.signature.SignatureVisitor;

public class SignatureRemapper extends SignatureVisitor
{
    private final SignatureVisitor v;
    private final Remapper remapper;
    private Stack<String> classNames;
    
    public SignatureRemapper(final SignatureVisitor a1, final Remapper a2) {
        this(327680, a1, a2);
    }
    
    protected SignatureRemapper(final int a1, final SignatureVisitor a2, final Remapper a3) {
        super(a1);
        this.classNames = new Stack<String>();
        this.v = a2;
        this.remapper = a3;
    }
    
    public void visitClassType(final String a1) {
        /*SL:64*/this.classNames.push(a1);
        /*SL:65*/this.v.visitClassType(this.remapper.mapType(a1));
    }
    
    public void visitInnerClassType(final String a1) {
        final String v1 = /*EL:70*/this.classNames.pop();
        final String v2 = /*EL:71*/v1 + '$' + a1;
        /*SL:72*/this.classNames.push(v2);
        final String v3 = /*EL:73*/this.remapper.mapType(v1) + '$';
        final String v4 = /*EL:74*/this.remapper.mapType(v2);
        final int v5 = /*EL:75*/v4.startsWith(v3) ? v3.length() : /*EL:76*/(v4.lastIndexOf(36) + 1);
        /*SL:77*/this.v.visitInnerClassType(v4.substring(v5));
    }
    
    public void visitFormalTypeParameter(final String a1) {
        /*SL:82*/this.v.visitFormalTypeParameter(a1);
    }
    
    public void visitTypeVariable(final String a1) {
        /*SL:87*/this.v.visitTypeVariable(a1);
    }
    
    public SignatureVisitor visitArrayType() {
        /*SL:92*/this.v.visitArrayType();
        /*SL:93*/return this;
    }
    
    public void visitBaseType(final char a1) {
        /*SL:98*/this.v.visitBaseType(a1);
    }
    
    public SignatureVisitor visitClassBound() {
        /*SL:103*/this.v.visitClassBound();
        /*SL:104*/return this;
    }
    
    public SignatureVisitor visitExceptionType() {
        /*SL:109*/this.v.visitExceptionType();
        /*SL:110*/return this;
    }
    
    public SignatureVisitor visitInterface() {
        /*SL:115*/this.v.visitInterface();
        /*SL:116*/return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        /*SL:121*/this.v.visitInterfaceBound();
        /*SL:122*/return this;
    }
    
    public SignatureVisitor visitParameterType() {
        /*SL:127*/this.v.visitParameterType();
        /*SL:128*/return this;
    }
    
    public SignatureVisitor visitReturnType() {
        /*SL:133*/this.v.visitReturnType();
        /*SL:134*/return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        /*SL:139*/this.v.visitSuperclass();
        /*SL:140*/return this;
    }
    
    public void visitTypeArgument() {
        /*SL:145*/this.v.visitTypeArgument();
    }
    
    public SignatureVisitor visitTypeArgument(final char a1) {
        /*SL:150*/this.v.visitTypeArgument(a1);
        /*SL:151*/return this;
    }
    
    public void visitEnd() {
        /*SL:156*/this.v.visitEnd();
        /*SL:157*/this.classNames.pop();
    }
}
