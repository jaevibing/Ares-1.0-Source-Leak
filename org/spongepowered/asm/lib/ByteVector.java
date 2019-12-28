package org.spongepowered.asm.lib;

public class ByteVector
{
    byte[] data;
    int length;
    
    public ByteVector() {
        this.data = new byte[64];
    }
    
    public ByteVector(final int a1) {
        this.data = new byte[a1];
    }
    
    public ByteVector putByte(final int a1) {
        int v1 = /*EL:78*/this.length;
        /*SL:79*/if (v1 + 1 > this.data.length) {
            /*SL:80*/this.enlarge(1);
        }
        /*SL:82*/this.data[v1++] = (byte)a1;
        /*SL:83*/this.length = v1;
        /*SL:84*/return this;
    }
    
    ByteVector put11(final int a1, final int a2) {
        int v1 = /*EL:98*/this.length;
        /*SL:99*/if (v1 + 2 > this.data.length) {
            /*SL:100*/this.enlarge(2);
        }
        final byte[] v2 = /*EL:102*/this.data;
        /*SL:103*/v2[v1++] = (byte)a1;
        /*SL:104*/v2[v1++] = (byte)a2;
        /*SL:105*/this.length = v1;
        /*SL:106*/return this;
    }
    
    public ByteVector putShort(final int a1) {
        int v1 = /*EL:118*/this.length;
        /*SL:119*/if (v1 + 2 > this.data.length) {
            /*SL:120*/this.enlarge(2);
        }
        final byte[] v2 = /*EL:122*/this.data;
        /*SL:123*/v2[v1++] = (byte)(a1 >>> 8);
        /*SL:124*/v2[v1++] = (byte)a1;
        /*SL:125*/this.length = v1;
        /*SL:126*/return this;
    }
    
    ByteVector put12(final int a1, final int a2) {
        int v1 = /*EL:140*/this.length;
        /*SL:141*/if (v1 + 3 > this.data.length) {
            /*SL:142*/this.enlarge(3);
        }
        final byte[] v2 = /*EL:144*/this.data;
        /*SL:145*/v2[v1++] = (byte)a1;
        /*SL:146*/v2[v1++] = (byte)(a2 >>> 8);
        /*SL:147*/v2[v1++] = (byte)a2;
        /*SL:148*/this.length = v1;
        /*SL:149*/return this;
    }
    
    public ByteVector putInt(final int a1) {
        int v1 = /*EL:161*/this.length;
        /*SL:162*/if (v1 + 4 > this.data.length) {
            /*SL:163*/this.enlarge(4);
        }
        final byte[] v2 = /*EL:165*/this.data;
        /*SL:166*/v2[v1++] = (byte)(a1 >>> 24);
        /*SL:167*/v2[v1++] = (byte)(a1 >>> 16);
        /*SL:168*/v2[v1++] = (byte)(a1 >>> 8);
        /*SL:169*/v2[v1++] = (byte)a1;
        /*SL:170*/this.length = v1;
        /*SL:171*/return this;
    }
    
    public ByteVector putLong(final long a1) {
        int v1 = /*EL:183*/this.length;
        /*SL:184*/if (v1 + 8 > this.data.length) {
            /*SL:185*/this.enlarge(8);
        }
        final byte[] v2 = /*EL:187*/this.data;
        int v3 = /*EL:188*/(int)(a1 >>> 32);
        /*SL:189*/v2[v1++] = (byte)(v3 >>> 24);
        /*SL:190*/v2[v1++] = (byte)(v3 >>> 16);
        /*SL:191*/v2[v1++] = (byte)(v3 >>> 8);
        /*SL:192*/v2[v1++] = (byte)v3;
        /*SL:193*/v3 = (int)a1;
        /*SL:194*/v2[v1++] = (byte)(v3 >>> 24);
        /*SL:195*/v2[v1++] = (byte)(v3 >>> 16);
        /*SL:196*/v2[v1++] = (byte)(v3 >>> 8);
        /*SL:197*/v2[v1++] = (byte)v3;
        /*SL:198*/this.length = v1;
        /*SL:199*/return this;
    }
    
    public ByteVector putUTF8(final String v-3) {
        final int length = /*EL:211*/v-3.length();
        /*SL:212*/if (length > 65535) {
            /*SL:213*/throw new IllegalArgumentException();
        }
        int length2 = /*EL:215*/this.length;
        /*SL:216*/if (length2 + 2 + length > this.data.length) {
            /*SL:217*/this.enlarge(2 + length);
        }
        final byte[] v0 = /*EL:219*/this.data;
        /*SL:226*/v0[length2++] = (byte)(length >>> 8);
        /*SL:227*/v0[length2++] = (byte)length;
        /*SL:228*/for (int v = 0; v < length; ++v) {
            final char a1 = /*EL:229*/v-3.charAt(v);
            /*SL:230*/if (a1 < '\u0001' || a1 > '\u007f') {
                /*SL:233*/this.length = length2;
                /*SL:234*/return this.encodeUTF8(v-3, v, 65535);
            }
            v0[length2++] = (byte)a1;
        }
        /*SL:237*/this.length = length2;
        /*SL:238*/return this;
    }
    
    ByteVector encodeUTF8(final String v-7, final int v-6, final int v-5) {
        final int length = /*EL:259*/v-7.length();
        int n = /*EL:260*/v-6;
        /*SL:262*/for (char a2 = (char)v-6; a2 < length; ++a2) {
            /*SL:263*/a2 = v-7.charAt(a2);
            /*SL:264*/if (a2 >= '\u0001' && a2 <= '\u007f') {
                /*SL:265*/++n;
            }
            else/*SL:266*/ if (a2 > '\u07ff') {
                /*SL:267*/n += 3;
            }
            else {
                /*SL:269*/n += 2;
            }
        }
        /*SL:272*/if (n > v-5) {
            /*SL:273*/throw new IllegalArgumentException();
        }
        final int n2 = /*EL:275*/this.length - v-6 - 2;
        /*SL:276*/if (n2 >= 0) {
            /*SL:277*/this.data[n2] = (byte)(n >>> 8);
            /*SL:278*/this.data[n2 + 1] = (byte)n;
        }
        /*SL:280*/if (this.length + n - v-6 > this.data.length) {
            /*SL:281*/this.enlarge(n - v-6);
        }
        int v0 = /*EL:283*/this.length;
        /*SL:284*/for (int v = v-6; v < length; ++v) {
            final char a3 = /*EL:285*/v-7.charAt(v);
            /*SL:286*/if (a3 >= '\u0001' && a3 <= '\u007f') {
                /*SL:287*/this.data[v0++] = (byte)a3;
            }
            else/*SL:288*/ if (a3 > '\u07ff') {
                /*SL:289*/this.data[v0++] = (byte)('\u00e0' | (a3 >> 12 & '\u000f'));
                /*SL:290*/this.data[v0++] = (byte)('\u0080' | (a3 >> 6 & '?'));
                /*SL:291*/this.data[v0++] = (byte)('\u0080' | (a3 & '?'));
            }
            else {
                /*SL:293*/this.data[v0++] = (byte)('\u00c0' | (a3 >> 6 & '\u001f'));
                /*SL:294*/this.data[v0++] = (byte)('\u0080' | (a3 & '?'));
            }
        }
        /*SL:297*/this.length = v0;
        /*SL:298*/return this;
    }
    
    public ByteVector putByteArray(final byte[] a1, final int a2, final int a3) {
        /*SL:315*/if (this.length + a3 > this.data.length) {
            /*SL:316*/this.enlarge(a3);
        }
        /*SL:318*/if (a1 != null) {
            /*SL:319*/System.arraycopy(a1, a2, this.data, this.length, a3);
        }
        /*SL:321*/this.length += a3;
        /*SL:322*/return this;
    }
    
    private void enlarge(final int a1) {
        final int v1 = /*EL:333*/2 * this.data.length;
        final int v2 = /*EL:334*/this.length + a1;
        final byte[] v3 = /*EL:335*/new byte[(v1 > v2) ? v1 : v2];
        /*SL:336*/System.arraycopy(this.data, 0, v3, 0, this.length);
        /*SL:337*/this.data = v3;
    }
}
