package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class ExceptionsAttribute extends AttributeInfo
{
    public static final String tag = "Exceptions";
    
    ExceptionsAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    private ExceptionsAttribute(final ConstPool a1, final ExceptionsAttribute a2, final Map a3) {
        super(a1, "Exceptions");
        this.copyFrom(a2, a3);
    }
    
    public ExceptionsAttribute(final ConstPool a1) {
        super(a1, "Exceptions");
        final byte[] v1 = new byte[2];
        v1[0] = (v1[1] = 0);
        this.info = v1;
    }
    
    @Override
    public AttributeInfo copy(final ConstPool a1, final Map a2) {
        /*SL:71*/return new ExceptionsAttribute(a1, this, a2);
    }
    
    private void copyFrom(final ExceptionsAttribute v2, final Map v3) {
        final ConstPool v4 = /*EL:83*/v2.constPool;
        final ConstPool v5 = /*EL:84*/this.constPool;
        final byte[] v6 = /*EL:85*/v2.info;
        final int v7 = /*EL:86*/v6.length;
        final byte[] v8 = /*EL:87*/new byte[v7];
        /*SL:88*/v8[0] = v6[0];
        /*SL:89*/v8[1] = v6[1];
        /*SL:90*/for (int a2 = 2; a2 < v7; a2 += 2) {
            /*SL:91*/a2 = ByteArray.readU16bit(v6, a2);
            /*SL:92*/ByteArray.write16bit(v4.copy(a2, v5, v3), v8, a2);
        }
        /*SL:96*/this.info = v8;
    }
    
    public int[] getExceptionIndexes() {
        final byte[] info = /*EL:103*/this.info;
        final int length = /*EL:104*/info.length;
        /*SL:105*/if (length <= 2) {
            /*SL:106*/return null;
        }
        final int[] array = /*EL:108*/new int[length / 2 - 1];
        int v0 = /*EL:109*/0;
        /*SL:110*/for (int v = 2; v < length; v += 2) {
            /*SL:111*/array[v0++] = ((info[v] & 0xFF) << 8 | (info[v + 1] & 0xFF));
        }
        /*SL:113*/return array;
    }
    
    public String[] getExceptions() {
        final byte[] info = /*EL:120*/this.info;
        final int length = /*EL:121*/info.length;
        /*SL:122*/if (length <= 2) {
            /*SL:123*/return null;
        }
        final String[] array = /*EL:125*/new String[length / 2 - 1];
        int n = /*EL:126*/0;
        /*SL:127*/for (int v0 = 2; v0 < length; v0 += 2) {
            final int v = /*EL:128*/(info[v0] & 0xFF) << 8 | (info[v0 + 1] & 0xFF);
            /*SL:129*/array[n++] = this.constPool.getClassInfo(v);
        }
        /*SL:132*/return array;
    }
    
    public void setExceptionIndexes(final int[] v2) {
        final int v3 = /*EL:139*/v2.length;
        final byte[] v4 = /*EL:140*/new byte[v3 * 2 + 2];
        /*SL:141*/ByteArray.write16bit(v3, v4, 0);
        /*SL:142*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:143*/ByteArray.write16bit(v2[a1], v4, a1 * 2 + 2);
        }
        /*SL:145*/this.info = v4;
    }
    
    public void setExceptions(final String[] v2) {
        final int v3 = /*EL:152*/v2.length;
        final byte[] v4 = /*EL:153*/new byte[v3 * 2 + 2];
        /*SL:154*/ByteArray.write16bit(v3, v4, 0);
        /*SL:155*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:156*/ByteArray.write16bit(this.constPool.addClassInfo(v2[a1]), v4, a1 * 2 + 2);
        }
        /*SL:159*/this.info = v4;
    }
    
    public int tableLength() {
        /*SL:165*/return this.info.length / 2 - 1;
    }
    
    public int getException(final int a1) {
        final int v1 = /*EL:171*/a1 * 2 + 2;
        /*SL:172*/return (this.info[v1] & 0xFF) << 8 | (this.info[v1 + 1] & 0xFF);
    }
}
