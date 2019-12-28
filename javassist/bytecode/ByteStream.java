package javassist.bytecode;

import java.io.IOException;
import java.io.OutputStream;

final class ByteStream extends OutputStream
{
    private byte[] buf;
    private int count;
    
    public ByteStream() {
        this(32);
    }
    
    public ByteStream(final int a1) {
        this.buf = new byte[a1];
        this.count = 0;
    }
    
    public int getPos() {
        /*SL:33*/return this.count;
    }
    
    public int size() {
        /*SL:34*/return this.count;
    }
    
    public void writeBlank(final int a1) {
        /*SL:37*/this.enlarge(a1);
        /*SL:38*/this.count += a1;
    }
    
    @Override
    public void write(final byte[] a1) {
        /*SL:42*/this.write(a1, 0, a1.length);
    }
    
    @Override
    public void write(final byte[] a1, final int a2, final int a3) {
        /*SL:46*/this.enlarge(a3);
        /*SL:47*/System.arraycopy(a1, a2, this.buf, this.count, a3);
        /*SL:48*/this.count += a3;
    }
    
    @Override
    public void write(final int a1) {
        /*SL:52*/this.enlarge(1);
        final int v1 = /*EL:53*/this.count;
        /*SL:54*/this.buf[v1] = (byte)a1;
        /*SL:55*/this.count = v1 + 1;
    }
    
    public void writeShort(final int a1) {
        /*SL:59*/this.enlarge(2);
        final int v1 = /*EL:60*/this.count;
        /*SL:61*/this.buf[v1] = (byte)(a1 >>> 8);
        /*SL:62*/this.buf[v1 + 1] = (byte)a1;
        /*SL:63*/this.count = v1 + 2;
    }
    
    public void writeInt(final int a1) {
        /*SL:67*/this.enlarge(4);
        final int v1 = /*EL:68*/this.count;
        /*SL:69*/this.buf[v1] = (byte)(a1 >>> 24);
        /*SL:70*/this.buf[v1 + 1] = (byte)(a1 >>> 16);
        /*SL:71*/this.buf[v1 + 2] = (byte)(a1 >>> 8);
        /*SL:72*/this.buf[v1 + 3] = (byte)a1;
        /*SL:73*/this.count = v1 + 4;
    }
    
    public void writeLong(final long a1) {
        /*SL:77*/this.enlarge(8);
        final int v1 = /*EL:78*/this.count;
        /*SL:79*/this.buf[v1] = (byte)(a1 >>> 56);
        /*SL:80*/this.buf[v1 + 1] = (byte)(a1 >>> 48);
        /*SL:81*/this.buf[v1 + 2] = (byte)(a1 >>> 40);
        /*SL:82*/this.buf[v1 + 3] = (byte)(a1 >>> 32);
        /*SL:83*/this.buf[v1 + 4] = (byte)(a1 >>> 24);
        /*SL:84*/this.buf[v1 + 5] = (byte)(a1 >>> 16);
        /*SL:85*/this.buf[v1 + 6] = (byte)(a1 >>> 8);
        /*SL:86*/this.buf[v1 + 7] = (byte)a1;
        /*SL:87*/this.count = v1 + 8;
    }
    
    public void writeFloat(final float a1) {
        /*SL:91*/this.writeInt(Float.floatToIntBits(a1));
    }
    
    public void writeDouble(final double a1) {
        /*SL:95*/this.writeLong(Double.doubleToLongBits(a1));
    }
    
    public void writeUTF(final String v-3) {
        final int length = /*EL:99*/v-3.length();
        int count = /*EL:100*/this.count;
        /*SL:101*/this.enlarge(length + 2);
        final byte[] v0 = /*EL:103*/this.buf;
        /*SL:104*/v0[count++] = (byte)(length >>> 8);
        /*SL:105*/v0[count++] = (byte)length;
        /*SL:106*/for (int v = 0; v < length; ++v) {
            final char a1 = /*EL:107*/v-3.charAt(v);
            /*SL:108*/if ('\u0001' > a1 || a1 > '\u007f') {
                /*SL:111*/this.writeUTF2(v-3, length, v);
                /*SL:112*/return;
            }
            v0[count++] = (byte)a1;
        }
        /*SL:116*/this.count = count;
    }
    
    private void writeUTF2(final String v-5, final int v-4, final int v-3) {
        int n = /*EL:120*/v-4;
        /*SL:121*/for (int a2 = v-3; a2 < v-4; ++a2) {
            /*SL:122*/a2 = v-5.charAt(a2);
            /*SL:123*/if (a2 > 2047) {
                /*SL:124*/n += 2;
            }
            else/*SL:125*/ if (a2 == 0 || a2 > 127) {
                /*SL:126*/++n;
            }
        }
        /*SL:129*/if (n > 65535) {
            /*SL:130*/throw new RuntimeException("encoded string too long: " + v-4 + n + " bytes");
        }
        /*SL:133*/this.enlarge(n + 2);
        int count = /*EL:134*/this.count;
        final byte[] v0 = /*EL:135*/this.buf;
        /*SL:136*/v0[count] = (byte)(n >>> 8);
        /*SL:137*/v0[count + 1] = (byte)n;
        /*SL:138*/count += 2 + v-3;
        /*SL:139*/for (int v = v-3; v < v-4; ++v) {
            final int a3 = /*EL:140*/v-5.charAt(v);
            /*SL:141*/if (1 <= a3 && a3 <= 127) {
                /*SL:142*/v0[count++] = (byte)a3;
            }
            else/*SL:143*/ if (a3 > 2047) {
                /*SL:144*/v0[count] = (byte)(0xE0 | (a3 >> 12 & 0xF));
                /*SL:145*/v0[count + 1] = (byte)(0x80 | (a3 >> 6 & 0x3F));
                /*SL:146*/v0[count + 2] = (byte)(0x80 | (a3 & 0x3F));
                /*SL:147*/count += 3;
            }
            else {
                /*SL:150*/v0[count] = (byte)(0xC0 | (a3 >> 6 & 0x1F));
                /*SL:151*/v0[count + 1] = (byte)(0x80 | (a3 & 0x3F));
                /*SL:152*/count += 2;
            }
        }
        /*SL:156*/this.count = count;
    }
    
    public void write(final int a1, final int a2) {
        /*SL:160*/this.buf[a1] = (byte)a2;
    }
    
    public void writeShort(final int a1, final int a2) {
        /*SL:164*/this.buf[a1] = (byte)(a2 >>> 8);
        /*SL:165*/this.buf[a1 + 1] = (byte)a2;
    }
    
    public void writeInt(final int a1, final int a2) {
        /*SL:169*/this.buf[a1] = (byte)(a2 >>> 24);
        /*SL:170*/this.buf[a1 + 1] = (byte)(a2 >>> 16);
        /*SL:171*/this.buf[a1 + 2] = (byte)(a2 >>> 8);
        /*SL:172*/this.buf[a1 + 3] = (byte)a2;
    }
    
    public byte[] toByteArray() {
        final byte[] v1 = /*EL:176*/new byte[this.count];
        /*SL:177*/System.arraycopy(this.buf, 0, v1, 0, this.count);
        /*SL:178*/return v1;
    }
    
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:182*/a1.write(this.buf, 0, this.count);
    }
    
    public void enlarge(final int v-2) {
        final int n = /*EL:186*/this.count + v-2;
        /*SL:187*/if (n > this.buf.length) {
            final int a1 = /*EL:188*/this.buf.length << 1;
            final byte[] v1 = /*EL:189*/new byte[(a1 > n) ? a1 : n];
            /*SL:190*/System.arraycopy(this.buf, 0, v1, 0, this.count);
            /*SL:191*/this.buf = v1;
        }
    }
}
