package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class LocalVariableAttribute extends AttributeInfo
{
    public static final String tag = "LocalVariableTable";
    public static final String typeTag = "LocalVariableTypeTable";
    
    public LocalVariableAttribute(final ConstPool a1) {
        super(a1, "LocalVariableTable", new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }
    
    public LocalVariableAttribute(final ConstPool a1, final String a2) {
        super(a1, a2, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }
    
    LocalVariableAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    LocalVariableAttribute(final ConstPool a1, final String a2, final byte[] a3) {
        super(a1, a2, a3);
    }
    
    public void addEntry(final int a3, final int a4, final int a5, final int v1, final int v2) {
        final int v3 = /*EL:82*/this.info.length;
        final byte[] v4 = /*EL:83*/new byte[v3 + 10];
        /*SL:84*/ByteArray.write16bit(this.tableLength() + 1, v4, 0);
        /*SL:85*/for (int a6 = 2; a6 < v3; ++a6) {
            /*SL:86*/v4[a6] = this.info[a6];
        }
        /*SL:88*/ByteArray.write16bit(a3, v4, v3);
        /*SL:89*/ByteArray.write16bit(a4, v4, v3 + 2);
        /*SL:90*/ByteArray.write16bit(a5, v4, v3 + 4);
        /*SL:91*/ByteArray.write16bit(v1, v4, v3 + 6);
        /*SL:92*/ByteArray.write16bit(v2, v4, v3 + 8);
        /*SL:93*/this.info = v4;
    }
    
    @Override
    void renameClass(final String v-5, final String v-4) {
        final ConstPool constPool = /*EL:97*/this.getConstPool();
        /*SL:99*/for (int tableLength = this.tableLength(), i = 0; i < tableLength; ++i) {
            int a2 = /*EL:100*/i * 10 + 2;
            final int v1 = /*EL:101*/ByteArray.readU16bit(this.info, a2 + 6);
            /*SL:102*/if (v1 != 0) {
                /*SL:103*/a2 = constPool.getUtf8Info(v1);
                /*SL:104*/a2 = this.renameEntry(a2, v-5, v-4);
                /*SL:105*/ByteArray.write16bit(constPool.addUtf8Info(a2), this.info, a2 + 6);
            }
        }
    }
    
    String renameEntry(final String a1, final String a2, final String a3) {
        /*SL:111*/return Descriptor.rename(a1, a2, a3);
    }
    
    @Override
    void renameClass(final Map v-3) {
        final ConstPool constPool = /*EL:115*/this.getConstPool();
        /*SL:117*/for (int tableLength = this.tableLength(), v0 = 0; v0 < tableLength; ++v0) {
            final int v = /*EL:118*/v0 * 10 + 2;
            final int v2 = /*EL:119*/ByteArray.readU16bit(this.info, v + 6);
            /*SL:120*/if (v2 != 0) {
                String a1 = /*EL:121*/constPool.getUtf8Info(v2);
                /*SL:122*/a1 = this.renameEntry(a1, v-3);
                /*SL:123*/ByteArray.write16bit(constPool.addUtf8Info(a1), this.info, v + 6);
            }
        }
    }
    
    String renameEntry(final String a1, final Map a2) {
        /*SL:129*/return Descriptor.rename(a1, a2);
    }
    
    public void shiftIndex(final int v2, final int v3) {
        /*SL:141*/for (int v4 = this.info.length, a2 = 2; a2 < v4; a2 += 10) {
            /*SL:142*/a2 = ByteArray.readU16bit(this.info, a2 + 8);
            /*SL:143*/if (a2 >= v2) {
                /*SL:144*/ByteArray.write16bit(a2 + v3, this.info, a2 + 8);
            }
        }
    }
    
    public int tableLength() {
        /*SL:153*/return ByteArray.readU16bit(this.info, 0);
    }
    
    public int startPc(final int a1) {
        /*SL:164*/return ByteArray.readU16bit(this.info, a1 * 10 + 2);
    }
    
    public int codeLength(final int a1) {
        /*SL:175*/return ByteArray.readU16bit(this.info, a1 * 10 + 4);
    }
    
    void shiftPc(final int v-3, final int v-2, final boolean v-1) {
        /*SL:183*/for (int v0 = this.tableLength(), v = 0; v < v0; ++v) {
            final int a1 = /*EL:184*/v * 10 + 2;
            final int a2 = /*EL:185*/ByteArray.readU16bit(this.info, a1);
            final int a3 = /*EL:186*/ByteArray.readU16bit(this.info, a1 + 2);
            /*SL:190*/if (a2 > v-3 || (v-1 && a2 == v-3 && a2 != 0)) {
                /*SL:191*/ByteArray.write16bit(a2 + v-2, this.info, a1);
            }
            else/*SL:192*/ if (a2 + a3 > v-3 || (v-1 && a2 + a3 == v-3)) {
                /*SL:193*/ByteArray.write16bit(a3 + v-2, this.info, a1 + 2);
            }
        }
    }
    
    public int nameIndex(final int a1) {
        /*SL:204*/return ByteArray.readU16bit(this.info, a1 * 10 + 6);
    }
    
    public String variableName(final int a1) {
        /*SL:214*/return this.getConstPool().getUtf8Info(this.nameIndex(a1));
    }
    
    public int descriptorIndex(final int a1) {
        /*SL:230*/return ByteArray.readU16bit(this.info, a1 * 10 + 8);
    }
    
    public int signatureIndex(final int a1) {
        /*SL:244*/return this.descriptorIndex(a1);
    }
    
    public String descriptor(final int a1) {
        /*SL:258*/return this.getConstPool().getUtf8Info(this.descriptorIndex(a1));
    }
    
    public String signature(final int a1) {
        /*SL:275*/return this.descriptor(a1);
    }
    
    public int index(final int a1) {
        /*SL:285*/return ByteArray.readU16bit(this.info, a1 * 10 + 10);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v-9, final Map v-8) {
        final byte[] value = /*EL:295*/this.get();
        final byte[] a3 = /*EL:296*/new byte[value.length];
        final ConstPool constPool = /*EL:297*/this.getConstPool();
        final LocalVariableAttribute thisAttr = /*EL:298*/this.makeThisAttr(v-9, a3);
        final int u16bit = /*EL:299*/ByteArray.readU16bit(value, 0);
        /*SL:300*/ByteArray.write16bit(u16bit, a3, 0);
        int n = /*EL:301*/2;
        /*SL:302*/for (int i = 0; i < u16bit; ++i) {
            int a2 = /*EL:303*/ByteArray.readU16bit(value, n);
            final int v1 = /*EL:304*/ByteArray.readU16bit(value, n + 2);
            int v2 = /*EL:305*/ByteArray.readU16bit(value, n + 4);
            int v3 = /*EL:306*/ByteArray.readU16bit(value, n + 6);
            final int v4 = /*EL:307*/ByteArray.readU16bit(value, n + 8);
            /*SL:309*/ByteArray.write16bit(a2, a3, n);
            /*SL:310*/ByteArray.write16bit(v1, a3, n + 2);
            /*SL:311*/if (v2 != 0) {
                /*SL:312*/v2 = constPool.copy(v2, v-9, null);
            }
            /*SL:314*/ByteArray.write16bit(v2, a3, n + 4);
            /*SL:316*/if (v3 != 0) {
                /*SL:317*/a2 = constPool.getUtf8Info(v3);
                /*SL:318*/a2 = Descriptor.rename(a2, v-8);
                /*SL:319*/v3 = v-9.addUtf8Info(a2);
            }
            /*SL:322*/ByteArray.write16bit(v3, a3, n + 6);
            /*SL:323*/ByteArray.write16bit(v4, a3, n + 8);
            /*SL:324*/n += 10;
        }
        /*SL:327*/return thisAttr;
    }
    
    LocalVariableAttribute makeThisAttr(final ConstPool a1, final byte[] a2) {
        /*SL:332*/return new LocalVariableAttribute(a1, "LocalVariableTable", a2);
    }
}
