package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.signature.SignatureVisitor;

public final class TraceSignatureVisitor extends SignatureVisitor
{
    private final StringBuilder declaration;
    private boolean isInterface;
    private boolean seenFormalParameter;
    private boolean seenInterfaceBound;
    private boolean seenParameter;
    private boolean seenInterface;
    private StringBuilder returnType;
    private StringBuilder exceptions;
    private int argumentStack;
    private int arrayStack;
    private String separator;
    
    public TraceSignatureVisitor(final int a1) {
        super(327680);
        this.separator = "";
        this.isInterface = ((a1 & 0x200) != 0x0);
        this.declaration = new StringBuilder();
    }
    
    private TraceSignatureVisitor(final StringBuilder a1) {
        super(327680);
        this.separator = "";
        this.declaration = a1;
    }
    
    public void visitFormalTypeParameter(final String a1) {
        /*SL:90*/this.declaration.append(this.seenFormalParameter ? ", " : "<").append(a1);
        /*SL:91*/this.seenFormalParameter = true;
        /*SL:92*/this.seenInterfaceBound = false;
    }
    
    public SignatureVisitor visitClassBound() {
        /*SL:97*/this.separator = " extends ";
        /*SL:98*/this.startType();
        /*SL:99*/return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        /*SL:104*/this.separator = (this.seenInterfaceBound ? ", " : " extends ");
        /*SL:105*/this.seenInterfaceBound = true;
        /*SL:106*/this.startType();
        /*SL:107*/return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        /*SL:112*/this.endFormals();
        /*SL:113*/this.separator = " extends ";
        /*SL:114*/this.startType();
        /*SL:115*/return this;
    }
    
    public SignatureVisitor visitInterface() {
        /*SL:120*/this.separator = (this.seenInterface ? ", " : (this.isInterface ? " extends " : " implements "));
        /*SL:122*/this.seenInterface = true;
        /*SL:123*/this.startType();
        /*SL:124*/return this;
    }
    
    public SignatureVisitor visitParameterType() {
        /*SL:129*/this.endFormals();
        /*SL:130*/if (this.seenParameter) {
            /*SL:131*/this.declaration.append(", ");
        }
        else {
            /*SL:133*/this.seenParameter = true;
            /*SL:134*/this.declaration.append('(');
        }
        /*SL:136*/this.startType();
        /*SL:137*/return this;
    }
    
    public SignatureVisitor visitReturnType() {
        /*SL:142*/this.endFormals();
        /*SL:143*/if (this.seenParameter) {
            /*SL:144*/this.seenParameter = false;
        }
        else {
            /*SL:146*/this.declaration.append('(');
        }
        /*SL:148*/this.declaration.append(')');
        /*SL:149*/this.returnType = new StringBuilder();
        /*SL:150*/return new TraceSignatureVisitor(this.returnType);
    }
    
    public SignatureVisitor visitExceptionType() {
        /*SL:155*/if (this.exceptions == null) {
            /*SL:156*/this.exceptions = new StringBuilder();
        }
        else {
            /*SL:158*/this.exceptions.append(", ");
        }
        /*SL:161*/return new TraceSignatureVisitor(this.exceptions);
    }
    
    public void visitBaseType(final char a1) {
        /*SL:166*/switch (a1) {
            case 'V': {
                /*SL:168*/this.declaration.append("void");
                /*SL:169*/break;
            }
            case 'B': {
                /*SL:171*/this.declaration.append("byte");
                /*SL:172*/break;
            }
            case 'J': {
                /*SL:174*/this.declaration.append("long");
                /*SL:175*/break;
            }
            case 'Z': {
                /*SL:177*/this.declaration.append("boolean");
                /*SL:178*/break;
            }
            case 'I': {
                /*SL:180*/this.declaration.append("int");
                /*SL:181*/break;
            }
            case 'S': {
                /*SL:183*/this.declaration.append("short");
                /*SL:184*/break;
            }
            case 'C': {
                /*SL:186*/this.declaration.append("char");
                /*SL:187*/break;
            }
            case 'F': {
                /*SL:189*/this.declaration.append("float");
                /*SL:190*/break;
            }
            default: {
                /*SL:193*/this.declaration.append("double");
                break;
            }
        }
        /*SL:196*/this.endType();
    }
    
    public void visitTypeVariable(final String a1) {
        /*SL:201*/this.declaration.append(a1);
        /*SL:202*/this.endType();
    }
    
    public SignatureVisitor visitArrayType() {
        /*SL:207*/this.startType();
        /*SL:208*/this.arrayStack |= 0x1;
        /*SL:209*/return this;
    }
    
    public void visitClassType(final String v2) {
        /*SL:214*/if ("java/lang/Object".equals(v2)) {
            final boolean a1 = /*EL:220*/this.argumentStack % 2 != 0 || this.seenParameter;
            /*SL:221*/if (a1) {
                /*SL:222*/this.declaration.append(this.separator).append(v2.replace('/', '.'));
            }
        }
        else {
            /*SL:225*/this.declaration.append(this.separator).append(v2.replace('/', '.'));
        }
        /*SL:227*/this.separator = "";
        /*SL:228*/this.argumentStack *= 2;
    }
    
    public void visitInnerClassType(final String a1) {
        /*SL:233*/if (this.argumentStack % 2 != 0) {
            /*SL:234*/this.declaration.append('>');
        }
        /*SL:236*/this.argumentStack /= 2;
        /*SL:237*/this.declaration.append('.');
        /*SL:238*/this.declaration.append(this.separator).append(a1.replace('/', '.'));
        /*SL:239*/this.separator = "";
        /*SL:240*/this.argumentStack *= 2;
    }
    
    public void visitTypeArgument() {
        /*SL:245*/if (this.argumentStack % 2 == 0) {
            /*SL:246*/++this.argumentStack;
            /*SL:247*/this.declaration.append('<');
        }
        else {
            /*SL:249*/this.declaration.append(", ");
        }
        /*SL:251*/this.declaration.append('?');
    }
    
    public SignatureVisitor visitTypeArgument(final char a1) {
        /*SL:256*/if (this.argumentStack % 2 == 0) {
            /*SL:257*/++this.argumentStack;
            /*SL:258*/this.declaration.append('<');
        }
        else {
            /*SL:260*/this.declaration.append(", ");
        }
        /*SL:263*/if (a1 == '+') {
            /*SL:264*/this.declaration.append("? extends ");
        }
        else/*SL:265*/ if (a1 == '-') {
            /*SL:266*/this.declaration.append("? super ");
        }
        /*SL:269*/this.startType();
        /*SL:270*/return this;
    }
    
    public void visitEnd() {
        /*SL:275*/if (this.argumentStack % 2 != 0) {
            /*SL:276*/this.declaration.append('>');
        }
        /*SL:278*/this.argumentStack /= 2;
        /*SL:279*/this.endType();
    }
    
    public String getDeclaration() {
        /*SL:283*/return this.declaration.toString();
    }
    
    public String getReturnType() {
        /*SL:287*/return (this.returnType == null) ? null : this.returnType.toString();
    }
    
    public String getExceptions() {
        /*SL:291*/return (this.exceptions == null) ? null : this.exceptions.toString();
    }
    
    private void endFormals() {
        /*SL:297*/if (this.seenFormalParameter) {
            /*SL:298*/this.declaration.append('>');
            /*SL:299*/this.seenFormalParameter = false;
        }
    }
    
    private void startType() {
        /*SL:304*/this.arrayStack *= 2;
    }
    
    private void endType() {
        /*SL:308*/if (this.arrayStack % 2 == 0) {
            /*SL:309*/this.arrayStack /= 2;
        }
        else {
            /*SL:311*/while (this.arrayStack % 2 != 0) {
                /*SL:312*/this.arrayStack /= 2;
                /*SL:313*/this.declaration.append("[]");
            }
        }
    }
}
