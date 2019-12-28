package org.spongepowered.asm.lib.signature;

public class SignatureReader
{
    private final String signature;
    
    public SignatureReader(final String a1) {
        this.signature = a1;
    }
    
    public void accept(final SignatureVisitor v-2) {
        final String signature = /*EL:73*/this.signature;
        final int v0 = /*EL:74*/signature.length();
        int v;
        /*SL:78*/if (signature.charAt(0) == '<') {
            /*SL:79*/v = 2;
            char v2;
            /*SL:93*/do {
                final int a1 = signature.indexOf(58, v);
                v-2.visitFormalTypeParameter(signature.substring(v - 1, a1));
                v = a1 + 1;
                v2 = signature.charAt(v);
                if (v2 == 'L' || v2 == '[' || v2 == 'T') {
                    v = parseType(signature, v, v-2.visitClassBound());
                }
                while ((v2 = signature.charAt(v++)) == ':') {
                    v = parseType(signature, v, v-2.visitInterfaceBound());
                }
            } while (v2 != '>');
        }
        else {
            /*SL:95*/v = 0;
        }
        /*SL:98*/if (signature.charAt(v) == '(') {
            /*SL:99*/++v;
            /*SL:100*/while (signature.charAt(v) != ')') {
                /*SL:101*/v = parseType(signature, v, v-2.visitParameterType());
            }
            /*SL:104*/for (v = parseType(signature, v + 1, v-2.visitReturnType()); v < v0; /*SL:105*/v = parseType(signature, v + 1, v-2.visitExceptionType())) {}
        }
        else {
            /*SL:109*/for (v = parseType(signature, v, v-2.visitSuperclass()); v < v0; /*SL:110*/v = parseType(signature, v, v-2.visitInterface())) {}
        }
    }
    
    public void acceptType(final SignatureVisitor a1) {
        parseType(/*EL:130*/this.signature, 0, a1);
    }
    
    private static int parseType(final String v1, int v2, final SignatureVisitor v3) {
        char v4;
        /*SL:151*/switch (v4 = v1.charAt(v2++)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z': {
                /*SL:161*/v3.visitBaseType(v4);
                /*SL:162*/return v2;
            }
            case '[': {
                /*SL:165*/return parseType(v1, v2, v3.visitArrayType());
            }
            case 'T': {
                final int a1 = /*EL:168*/v1.indexOf(59, v2);
                /*SL:169*/v3.visitTypeVariable(v1.substring(v2, a1));
                /*SL:170*/return a1 + 1;
            }
            default: {
                int v5 = /*EL:173*/v2;
                boolean v6 = /*EL:174*/false;
                boolean v7 = /*EL:175*/false;
            Block_3:
                while (true) {
                    /*SL:177*/switch (v4 = v1.charAt(v2++)) {
                        case '.':
                        case ';': {
                            /*SL:180*/if (!v6) {
                                final String a2 = /*EL:181*/v1.substring(v5, v2 - 1);
                                /*SL:182*/if (v7) {
                                    /*SL:183*/v3.visitInnerClassType(a2);
                                }
                                else {
                                    /*SL:185*/v3.visitClassType(a2);
                                }
                            }
                            /*SL:188*/if (v4 == ';') {
                                break Block_3;
                            }
                            /*SL:192*/v5 = v2;
                            /*SL:193*/v6 = false;
                            /*SL:194*/v7 = true;
                            /*SL:195*/continue;
                        }
                        case '<': {
                            final String a3 = /*EL:198*/v1.substring(v5, v2 - 1);
                            /*SL:199*/if (v7) {
                                /*SL:200*/v3.visitInnerClassType(a3);
                            }
                            else {
                                /*SL:202*/v3.visitClassType(a3);
                            }
                            /*SL:204*/v6 = true;
                        Label_0368:
                            while (true) {
                                /*SL:206*/switch (v4 = v1.charAt(v2)) {
                                    case '>': {
                                        break Label_0368;
                                    }
                                    case '*': {
                                        /*SL:210*/++v2;
                                        /*SL:211*/v3.visitTypeArgument();
                                        /*SL:212*/continue;
                                    }
                                    case '+':
                                    case '-': {
                                        /*SL:215*/v2 = parseType(v1, v2 + 1, v3.visitTypeArgument(v4));
                                        /*SL:217*/continue;
                                    }
                                    default: {
                                        /*SL:219*/v2 = parseType(v1, v2, v3.visitTypeArgument('='));
                                        /*SL:221*/continue;
                                    }
                                }
                            }
                            continue;
                        }
                    }
                }
                v3.visitEnd();
                return v2;
            }
        }
    }
}
