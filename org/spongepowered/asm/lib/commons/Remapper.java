package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureWriter;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Type;

public abstract class Remapper
{
    public String mapDesc(final String v-1) {
        final Type v0 = /*EL:54*/Type.getType(v-1);
        /*SL:55*/switch (v0.getSort()) {
            case 9: {
                String v = /*EL:57*/this.mapDesc(v0.getElementType().getDescriptor());
                /*SL:58*/for (int a1 = 0; a1 < v0.getDimensions(); ++a1) {
                    /*SL:59*/v = '[' + v;
                }
                /*SL:61*/return v;
            }
            case 10: {
                final String v2 = /*EL:63*/this.map(v0.getInternalName());
                /*SL:64*/if (v2 != null) {
                    /*SL:65*/return 'L' + v2 + ';';
                }
                break;
            }
        }
        /*SL:68*/return v-1;
    }
    
    private Type mapType(final Type v0) {
        /*SL:72*/switch (v0.getSort()) {
            case 9: {
                String v = /*EL:74*/this.mapDesc(v0.getElementType().getDescriptor());
                /*SL:75*/for (int a1 = 0; a1 < v0.getDimensions(); ++a1) {
                    /*SL:76*/v = '[' + v;
                }
                /*SL:78*/return Type.getType(v);
            }
            case 10: {
                final String v = /*EL:80*/this.map(v0.getInternalName());
                /*SL:81*/return (v != null) ? Type.getObjectType(v) : v0;
            }
            case 11: {
                /*SL:83*/return Type.getMethodType(this.mapMethodDesc(v0.getDescriptor()));
            }
            default: {
                /*SL:85*/return v0;
            }
        }
    }
    
    public String mapType(final String a1) {
        /*SL:89*/if (a1 == null) {
            /*SL:90*/return null;
        }
        /*SL:92*/return this.mapType(Type.getObjectType(a1)).getInternalName();
    }
    
    public String[] mapTypes(final String[] v-4) {
        String[] array = /*EL:96*/null;
        boolean b = /*EL:97*/false;
        /*SL:98*/for (int i = 0; i < v-4.length; ++i) {
            final String a1 = /*EL:99*/v-4[i];
            final String v1 = /*EL:100*/this.map(a1);
            /*SL:101*/if (v1 != null && array == null) {
                /*SL:102*/array = new String[v-4.length];
                /*SL:103*/if (i > 0) {
                    /*SL:104*/System.arraycopy(v-4, 0, array, 0, i);
                }
                /*SL:106*/b = true;
            }
            /*SL:108*/if (b) {
                /*SL:109*/array[i] = ((v1 == null) ? a1 : v1);
            }
        }
        /*SL:112*/return b ? array : v-4;
    }
    
    public String mapMethodDesc(final String v2) {
        /*SL:116*/if ("()V".equals(v2)) {
            /*SL:117*/return v2;
        }
        final Type[] v3 = /*EL:120*/Type.getArgumentTypes(v2);
        final StringBuilder v4 = /*EL:121*/new StringBuilder("(");
        /*SL:122*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:123*/v4.append(this.mapDesc(v3[a1].getDescriptor()));
        }
        final Type v5 = /*EL:125*/Type.getReturnType(v2);
        /*SL:126*/if (v5 == Type.VOID_TYPE) {
            /*SL:127*/v4.append(")V");
            /*SL:128*/return v4.toString();
        }
        /*SL:130*/v4.append(')').append(this.mapDesc(v5.getDescriptor()));
        /*SL:131*/return v4.toString();
    }
    
    public Object mapValue(final Object v2) {
        /*SL:135*/if (v2 instanceof Type) {
            /*SL:136*/return this.mapType((Type)v2);
        }
        /*SL:138*/if (v2 instanceof Handle) {
            final Handle a1 = /*EL:139*/(Handle)v2;
            /*SL:140*/return /*EL:142*/new Handle(a1.getTag(), this.mapType(a1.getOwner()), this.mapMethodName(a1.getOwner(), a1.getName(), a1.getDesc()), this.mapMethodDesc(a1.getDesc()), a1.isInterface());
        }
        /*SL:144*/return v2;
    }
    
    public String mapSignature(final String a1, final boolean a2) {
        /*SL:157*/if (a1 == null) {
            /*SL:158*/return null;
        }
        final SignatureReader v1 = /*EL:160*/new SignatureReader(a1);
        final SignatureWriter v2 = /*EL:161*/new SignatureWriter();
        final SignatureVisitor v3 = /*EL:162*/this.createSignatureRemapper(v2);
        /*SL:163*/if (a2) {
            /*SL:164*/v1.acceptType(v3);
        }
        else {
            /*SL:166*/v1.accept(v3);
        }
        /*SL:168*/return v2.toString();
    }
    
    @Deprecated
    protected SignatureVisitor createRemappingSignatureAdapter(final SignatureVisitor a1) {
        /*SL:177*/return new SignatureRemapper(a1, this);
    }
    
    protected SignatureVisitor createSignatureRemapper(final SignatureVisitor a1) {
        /*SL:182*/return this.createRemappingSignatureAdapter(a1);
    }
    
    public String mapMethodName(final String a1, final String a2, final String a3) {
        /*SL:197*/return a2;
    }
    
    public String mapInvokeDynamicMethodName(final String a1, final String a2) {
        /*SL:210*/return a1;
    }
    
    public String mapFieldName(final String a1, final String a2, final String a3) {
        /*SL:225*/return a2;
    }
    
    public String map(final String a1) {
        /*SL:236*/return a1;
    }
}
