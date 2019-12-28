package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class LineNumberAttribute extends AttributeInfo
{
    public static final String tag = "LineNumberTable";
    
    LineNumberAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    private LineNumberAttribute(final ConstPool a1, final byte[] a2) {
        super(a1, "LineNumberTable", a2);
    }
    
    public int tableLength() {
        /*SL:47*/return ByteArray.readU16bit(this.info, 0);
    }
    
    public int startPc(final int a1) {
        /*SL:58*/return ByteArray.readU16bit(this.info, a1 * 4 + 2);
    }
    
    public int lineNumber(final int a1) {
        /*SL:69*/return ByteArray.readU16bit(this.info, a1 * 4 + 4);
    }
    
    public int toLineNumber(final int a1) {
        final int v1 = /*EL:78*/this.tableLength();
        int v2 = /*EL:79*/0;
        /*SL:80*/while (v2 < v1) {
            /*SL:81*/if (a1 < this.startPc(v2)) {
                /*SL:82*/if (v2 == 0) {
                    /*SL:83*/return this.lineNumber(0);
                }
                break;
            }
            else {
                ++v2;
            }
        }
        /*SL:87*/return this.lineNumber(v2 - 1);
    }
    
    public int toStartPc(final int v2) {
        /*SL:99*/for (int v3 = this.tableLength(), a1 = 0; a1 < v3; ++a1) {
            /*SL:100*/if (v2 == this.lineNumber(a1)) {
                /*SL:101*/return this.startPc(a1);
            }
        }
        /*SL:103*/return -1;
    }
    
    public Pc toNearPc(final int v-3) {
        final int tableLength = /*EL:130*/this.tableLength();
        int index = /*EL:131*/0;
        int v0 = /*EL:132*/0;
        /*SL:133*/if (tableLength > 0) {
            /*SL:134*/v0 = this.lineNumber(0) - v-3;
            /*SL:135*/index = this.startPc(0);
        }
        /*SL:138*/for (int v = 1; v < tableLength; ++v) {
            final int a1 = /*EL:139*/this.lineNumber(v) - v-3;
            /*SL:140*/if ((a1 < 0 && a1 > v0) || (a1 >= 0 && (a1 < v0 || v0 < 0))) {
                /*SL:142*/v0 = a1;
                /*SL:143*/index = this.startPc(v);
            }
        }
        final Pc v2 = /*EL:147*/new Pc();
        /*SL:148*/v2.index = index;
        /*SL:149*/v2.line = v-3 + v0;
        /*SL:150*/return v2;
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) {
        final byte[] v3 = /*EL:160*/this.info;
        final int v4 = /*EL:161*/v3.length;
        final byte[] v5 = /*EL:162*/new byte[v4];
        /*SL:163*/for (int a1 = 0; a1 < v4; ++a1) {
            /*SL:164*/v5[a1] = v3[a1];
        }
        final LineNumberAttribute v6 = /*EL:166*/new LineNumberAttribute(v1, v5);
        /*SL:167*/return v6;
    }
    
    void shiftPc(final int v2, final int v3, final boolean v4) {
        /*SL:175*/for (int v5 = this.tableLength(), a3 = 0; a3 < v5; ++a3) {
            final int a2 = /*EL:176*/a3 * 4 + 2;
            /*SL:177*/a3 = ByteArray.readU16bit(this.info, a2);
            /*SL:178*/if (a3 > v2 || (v4 && a3 == v2)) {
                /*SL:179*/ByteArray.write16bit(a3 + v3, this.info, a2);
            }
        }
    }
    
    public static class Pc
    {
        public int index;
        public int line;
    }
}
