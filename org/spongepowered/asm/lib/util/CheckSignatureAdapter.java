package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.signature.SignatureVisitor;

public class CheckSignatureAdapter extends SignatureVisitor
{
    public static final int CLASS_SIGNATURE = 0;
    public static final int METHOD_SIGNATURE = 1;
    public static final int TYPE_SIGNATURE = 2;
    private static final int EMPTY = 1;
    private static final int FORMAL = 2;
    private static final int BOUND = 4;
    private static final int SUPER = 8;
    private static final int PARAM = 16;
    private static final int RETURN = 32;
    private static final int SIMPLE_TYPE = 64;
    private static final int CLASS_TYPE = 128;
    private static final int END = 256;
    private final int type;
    private int state;
    private boolean canBeVoid;
    private final SignatureVisitor sv;
    
    public CheckSignatureAdapter(final int a1, final SignatureVisitor a2) {
        this(327680, a1, a2);
    }
    
    protected CheckSignatureAdapter(final int a1, final int a2, final SignatureVisitor a3) {
        super(a1);
        this.type = a2;
        this.state = 1;
        this.sv = a3;
    }
    
    public void visitFormalTypeParameter(final String a1) {
        /*SL:145*/if (this.type == 2 || (this.state != 1 && this.state != 2 && this.state != 4)) {
            /*SL:147*/throw new IllegalStateException();
        }
        /*SL:149*/CheckMethodAdapter.checkIdentifier(a1, "formal type parameter");
        /*SL:150*/this.state = 2;
        /*SL:151*/if (this.sv != null) {
            /*SL:152*/this.sv.visitFormalTypeParameter(a1);
        }
    }
    
    public SignatureVisitor visitClassBound() {
        /*SL:158*/if (this.state != 2) {
            /*SL:159*/throw new IllegalStateException();
        }
        /*SL:161*/this.state = 4;
        final SignatureVisitor v1 = /*EL:162*/(this.sv == null) ? null : this.sv.visitClassBound();
        /*SL:163*/return new CheckSignatureAdapter(2, v1);
    }
    
    public SignatureVisitor visitInterfaceBound() {
        /*SL:168*/if (this.state != 2 && this.state != 4) {
            /*SL:169*/throw new IllegalArgumentException();
        }
        final SignatureVisitor v1 = /*EL:171*/(this.sv == null) ? null : this.sv.visitInterfaceBound();
        /*SL:172*/return new CheckSignatureAdapter(2, v1);
    }
    
    public SignatureVisitor visitSuperclass() {
        /*SL:179*/if (this.type != 0 || (this.state & 0x7) == 0x0) {
            /*SL:180*/throw new IllegalArgumentException();
        }
        /*SL:182*/this.state = 8;
        final SignatureVisitor v1 = /*EL:183*/(this.sv == null) ? null : this.sv.visitSuperclass();
        /*SL:184*/return new CheckSignatureAdapter(2, v1);
    }
    
    public SignatureVisitor visitInterface() {
        /*SL:189*/if (this.state != 8) {
            /*SL:190*/throw new IllegalStateException();
        }
        final SignatureVisitor v1 = /*EL:192*/(this.sv == null) ? null : this.sv.visitInterface();
        /*SL:193*/return new CheckSignatureAdapter(2, v1);
    }
    
    public SignatureVisitor visitParameterType() {
        /*SL:200*/if (this.type != 1 || (this.state & 0x17) == 0x0) {
            /*SL:202*/throw new IllegalArgumentException();
        }
        /*SL:204*/this.state = 16;
        final SignatureVisitor v1 = /*EL:205*/(this.sv == null) ? null : this.sv.visitParameterType();
        /*SL:206*/return new CheckSignatureAdapter(2, v1);
    }
    
    public SignatureVisitor visitReturnType() {
        /*SL:211*/if (this.type != 1 || (this.state & 0x17) == 0x0) {
            /*SL:213*/throw new IllegalArgumentException();
        }
        /*SL:215*/this.state = 32;
        final SignatureVisitor v1 = /*EL:216*/(this.sv == null) ? null : this.sv.visitReturnType();
        final CheckSignatureAdapter v2 = /*EL:217*/new CheckSignatureAdapter(2, v1);
        /*SL:218*/v2.canBeVoid = true;
        /*SL:219*/return v2;
    }
    
    public SignatureVisitor visitExceptionType() {
        /*SL:224*/if (this.state != 32) {
            /*SL:225*/throw new IllegalStateException();
        }
        final SignatureVisitor v1 = /*EL:227*/(this.sv == null) ? null : this.sv.visitExceptionType();
        /*SL:228*/return new CheckSignatureAdapter(2, v1);
    }
    
    public void visitBaseType(final char a1) {
        /*SL:235*/if (this.type != 2 || this.state != 1) {
            /*SL:236*/throw new IllegalStateException();
        }
        /*SL:238*/if (a1 == 'V') {
            /*SL:239*/if (!this.canBeVoid) {
                /*SL:240*/throw new IllegalArgumentException();
            }
        }
        else/*SL:243*/ if ("ZCBSIFJD".indexOf(a1) == -1) {
            /*SL:244*/throw new IllegalArgumentException();
        }
        /*SL:247*/this.state = 64;
        /*SL:248*/if (this.sv != null) {
            /*SL:249*/this.sv.visitBaseType(a1);
        }
    }
    
    public void visitTypeVariable(final String a1) {
        /*SL:255*/if (this.type != 2 || this.state != 1) {
            /*SL:256*/throw new IllegalStateException();
        }
        /*SL:258*/CheckMethodAdapter.checkIdentifier(a1, "type variable");
        /*SL:259*/this.state = 64;
        /*SL:260*/if (this.sv != null) {
            /*SL:261*/this.sv.visitTypeVariable(a1);
        }
    }
    
    public SignatureVisitor visitArrayType() {
        /*SL:267*/if (this.type != 2 || this.state != 1) {
            /*SL:268*/throw new IllegalStateException();
        }
        /*SL:270*/this.state = 64;
        final SignatureVisitor v1 = /*EL:271*/(this.sv == null) ? null : this.sv.visitArrayType();
        /*SL:272*/return new CheckSignatureAdapter(2, v1);
    }
    
    public void visitClassType(final String a1) {
        /*SL:277*/if (this.type != 2 || this.state != 1) {
            /*SL:278*/throw new IllegalStateException();
        }
        /*SL:280*/CheckMethodAdapter.checkInternalName(a1, "class name");
        /*SL:281*/this.state = 128;
        /*SL:282*/if (this.sv != null) {
            /*SL:283*/this.sv.visitClassType(a1);
        }
    }
    
    public void visitInnerClassType(final String a1) {
        /*SL:289*/if (this.state != 128) {
            /*SL:290*/throw new IllegalStateException();
        }
        /*SL:292*/CheckMethodAdapter.checkIdentifier(a1, "inner class name");
        /*SL:293*/if (this.sv != null) {
            /*SL:294*/this.sv.visitInnerClassType(a1);
        }
    }
    
    public void visitTypeArgument() {
        /*SL:300*/if (this.state != 128) {
            /*SL:301*/throw new IllegalStateException();
        }
        /*SL:303*/if (this.sv != null) {
            /*SL:304*/this.sv.visitTypeArgument();
        }
    }
    
    public SignatureVisitor visitTypeArgument(final char a1) {
        /*SL:310*/if (this.state != 128) {
            /*SL:311*/throw new IllegalStateException();
        }
        /*SL:313*/if ("+-=".indexOf(a1) == -1) {
            /*SL:314*/throw new IllegalArgumentException();
        }
        final SignatureVisitor v1 = /*EL:316*/(this.sv == null) ? null : this.sv.visitTypeArgument(a1);
        /*SL:317*/return new CheckSignatureAdapter(2, v1);
    }
    
    public void visitEnd() {
        /*SL:322*/if (this.state != 128) {
            /*SL:323*/throw new IllegalStateException();
        }
        /*SL:325*/this.state = 256;
        /*SL:326*/if (this.sv != null) {
            /*SL:327*/this.sv.visitEnd();
        }
    }
}
