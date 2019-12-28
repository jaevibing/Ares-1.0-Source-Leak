package org.spongepowered.asm.lib;

public class TypePath
{
    public static final int ARRAY_ELEMENT = 0;
    public static final int INNER_TYPE = 1;
    public static final int WILDCARD_BOUND = 2;
    public static final int TYPE_ARGUMENT = 3;
    byte[] b;
    int offset;
    
    TypePath(final byte[] a1, final int a2) {
        this.b = a1;
        this.offset = a2;
    }
    
    public int getLength() {
        /*SL:95*/return this.b[this.offset];
    }
    
    public int getStep(final int a1) {
        /*SL:108*/return this.b[this.offset + 2 * a1 + 1];
    }
    
    public int getStepArgument(final int a1) {
        /*SL:122*/return this.b[this.offset + 2 * a1 + 2];
    }
    
    public static TypePath fromString(final String v-3) {
        /*SL:135*/if (v-3 == null || v-3.length() == 0) {
            /*SL:136*/return null;
        }
        final int length = /*EL:138*/v-3.length();
        final ByteVector byteVector = /*EL:139*/new ByteVector(length);
        /*SL:140*/byteVector.putByte(0);
        int v0 = /*EL:141*/0;
        while (v0 < length) {
            char v = /*EL:142*/v-3.charAt(v0++);
            /*SL:143*/if (v == '[') {
                /*SL:144*/byteVector.put11(0, 0);
            }
            else/*SL:145*/ if (v == '.') {
                /*SL:146*/byteVector.put11(1, 0);
            }
            else/*SL:147*/ if (v == '*') {
                /*SL:148*/byteVector.put11(2, 0);
            }
            else {
                /*SL:149*/if (v < '0' || v > '9') {
                    continue;
                }
                int a1 = /*EL:150*/v - '0';
                /*SL:151*/while (v0 < length && (v = v-3.charAt(v0)) >= '0' && v <= '9') {
                    /*SL:152*/a1 = a1 * 10 + v - 48;
                    /*SL:153*/++v0;
                }
                /*SL:155*/if (v0 < length && v-3.charAt(v0) == ';') {
                    /*SL:156*/++v0;
                }
                /*SL:158*/byteVector.put11(3, a1);
            }
        }
        /*SL:161*/byteVector.data[0] = (byte)(byteVector.length / 2);
        /*SL:162*/return new TypePath(byteVector.data, 0);
    }
    
    public String toString() {
        final int length = /*EL:174*/this.getLength();
        final StringBuilder v0 = /*EL:175*/new StringBuilder(length * 2);
        /*SL:176*/for (int v = 0; v < length; ++v) {
            /*SL:177*/switch (this.getStep(v)) {
                case 0: {
                    /*SL:179*/v0.append('[');
                    /*SL:180*/break;
                }
                case 1: {
                    /*SL:182*/v0.append('.');
                    /*SL:183*/break;
                }
                case 2: {
                    /*SL:185*/v0.append('*');
                    /*SL:186*/break;
                }
                case 3: {
                    /*SL:188*/v0.append(this.getStepArgument(v)).append(';');
                    /*SL:189*/break;
                }
                default: {
                    /*SL:191*/v0.append('_');
                    break;
                }
            }
        }
        /*SL:194*/return v0.toString();
    }
}
