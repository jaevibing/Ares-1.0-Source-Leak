package org.spongepowered.asm.lib.signature;

public class SignatureWriter extends SignatureVisitor
{
    private final StringBuilder buf;
    private boolean hasFormals;
    private boolean hasParameters;
    private int argumentStack;
    
    public SignatureWriter() {
        super(327680);
        this.buf = new StringBuilder();
    }
    
    public void visitFormalTypeParameter(final String a1) {
        /*SL:78*/if (!this.hasFormals) {
            /*SL:79*/this.hasFormals = true;
            /*SL:80*/this.buf.append('<');
        }
        /*SL:82*/this.buf.append(a1);
        /*SL:83*/this.buf.append(':');
    }
    
    public SignatureVisitor visitClassBound() {
        /*SL:88*/return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        /*SL:93*/this.buf.append(':');
        /*SL:94*/return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        /*SL:99*/this.endFormals();
        /*SL:100*/return this;
    }
    
    public SignatureVisitor visitInterface() {
        /*SL:105*/return this;
    }
    
    public SignatureVisitor visitParameterType() {
        /*SL:110*/this.endFormals();
        /*SL:111*/if (!this.hasParameters) {
            /*SL:112*/this.hasParameters = true;
            /*SL:113*/this.buf.append('(');
        }
        /*SL:115*/return this;
    }
    
    public SignatureVisitor visitReturnType() {
        /*SL:120*/this.endFormals();
        /*SL:121*/if (!this.hasParameters) {
            /*SL:122*/this.buf.append('(');
        }
        /*SL:124*/this.buf.append(')');
        /*SL:125*/return this;
    }
    
    public SignatureVisitor visitExceptionType() {
        /*SL:130*/this.buf.append('^');
        /*SL:131*/return this;
    }
    
    public void visitBaseType(final char a1) {
        /*SL:136*/this.buf.append(a1);
    }
    
    public void visitTypeVariable(final String a1) {
        /*SL:141*/this.buf.append('T');
        /*SL:142*/this.buf.append(a1);
        /*SL:143*/this.buf.append(';');
    }
    
    public SignatureVisitor visitArrayType() {
        /*SL:148*/this.buf.append('[');
        /*SL:149*/return this;
    }
    
    public void visitClassType(final String a1) {
        /*SL:154*/this.buf.append('L');
        /*SL:155*/this.buf.append(a1);
        /*SL:156*/this.argumentStack *= 2;
    }
    
    public void visitInnerClassType(final String a1) {
        /*SL:161*/this.endArguments();
        /*SL:162*/this.buf.append('.');
        /*SL:163*/this.buf.append(a1);
        /*SL:164*/this.argumentStack *= 2;
    }
    
    public void visitTypeArgument() {
        /*SL:169*/if (this.argumentStack % 2 == 0) {
            /*SL:170*/++this.argumentStack;
            /*SL:171*/this.buf.append('<');
        }
        /*SL:173*/this.buf.append('*');
    }
    
    public SignatureVisitor visitTypeArgument(final char a1) {
        /*SL:178*/if (this.argumentStack % 2 == 0) {
            /*SL:179*/++this.argumentStack;
            /*SL:180*/this.buf.append('<');
        }
        /*SL:182*/if (a1 != '=') {
            /*SL:183*/this.buf.append(a1);
        }
        /*SL:185*/return this;
    }
    
    public void visitEnd() {
        /*SL:190*/this.endArguments();
        /*SL:191*/this.buf.append(';');
    }
    
    public String toString() {
        /*SL:201*/return this.buf.toString();
    }
    
    private void endFormals() {
        /*SL:212*/if (this.hasFormals) {
            /*SL:213*/this.hasFormals = false;
            /*SL:214*/this.buf.append('>');
        }
    }
    
    private void endArguments() {
        /*SL:222*/if (this.argumentStack % 2 != 0) {
            /*SL:223*/this.buf.append('>');
        }
        /*SL:225*/this.argumentStack /= 2;
    }
}
