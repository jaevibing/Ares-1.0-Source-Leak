package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class InnerClassesAttribute extends AttributeInfo
{
    public static final String tag = "InnerClasses";
    
    InnerClassesAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    private InnerClassesAttribute(final ConstPool a1, final byte[] a2) {
        super(a1, "InnerClasses", a2);
    }
    
    public InnerClassesAttribute(final ConstPool a1) {
        super(a1, "InnerClasses", new byte[2]);
        ByteArray.write16bit(0, this.get(), 0);
    }
    
    public int tableLength() {
        /*SL:55*/return ByteArray.readU16bit(this.get(), 0);
    }
    
    public int innerClassIndex(final int a1) {
        /*SL:61*/return ByteArray.readU16bit(this.get(), a1 * 8 + 2);
    }
    
    public String innerClass(final int a1) {
        final int v1 = /*EL:71*/this.innerClassIndex(a1);
        /*SL:72*/if (v1 == 0) {
            /*SL:73*/return null;
        }
        /*SL:75*/return this.constPool.getClassInfo(v1);
    }
    
    public void setInnerClassIndex(final int a1, final int a2) {
        /*SL:83*/ByteArray.write16bit(a2, this.get(), a1 * 8 + 2);
    }
    
    public int outerClassIndex(final int a1) {
        /*SL:90*/return ByteArray.readU16bit(this.get(), a1 * 8 + 4);
    }
    
    public String outerClass(final int a1) {
        final int v1 = /*EL:100*/this.outerClassIndex(a1);
        /*SL:101*/if (v1 == 0) {
            /*SL:102*/return null;
        }
        /*SL:104*/return this.constPool.getClassInfo(v1);
    }
    
    public void setOuterClassIndex(final int a1, final int a2) {
        /*SL:112*/ByteArray.write16bit(a2, this.get(), a1 * 8 + 4);
    }
    
    public int innerNameIndex(final int a1) {
        /*SL:119*/return ByteArray.readU16bit(this.get(), a1 * 8 + 6);
    }
    
    public String innerName(final int a1) {
        final int v1 = /*EL:129*/this.innerNameIndex(a1);
        /*SL:130*/if (v1 == 0) {
            /*SL:131*/return null;
        }
        /*SL:133*/return this.constPool.getUtf8Info(v1);
    }
    
    public void setInnerNameIndex(final int a1, final int a2) {
        /*SL:141*/ByteArray.write16bit(a2, this.get(), a1 * 8 + 6);
    }
    
    public int accessFlags(final int a1) {
        /*SL:148*/return ByteArray.readU16bit(this.get(), a1 * 8 + 8);
    }
    
    public void setAccessFlags(final int a1, final int a2) {
        /*SL:156*/ByteArray.write16bit(a2, this.get(), a1 * 8 + 8);
    }
    
    public void append(final String a1, final String a2, final String a3, final int a4) {
        final int v1 = /*EL:168*/this.constPool.addClassInfo(a1);
        final int v2 = /*EL:169*/this.constPool.addClassInfo(a2);
        final int v3 = /*EL:170*/this.constPool.addUtf8Info(a3);
        /*SL:171*/this.append(v1, v2, v3, a4);
    }
    
    public void append(final int a3, final int a4, final int v1, final int v2) {
        final byte[] v3 = /*EL:183*/this.get();
        final int v4 = /*EL:184*/v3.length;
        final byte[] v5 = /*EL:185*/new byte[v4 + 8];
        /*SL:186*/for (int a5 = 2; a5 < v4; ++a5) {
            /*SL:187*/v5[a5] = v3[a5];
        }
        final int v6 = /*EL:189*/ByteArray.readU16bit(v3, 0);
        /*SL:190*/ByteArray.write16bit(v6 + 1, v5, 0);
        /*SL:192*/ByteArray.write16bit(a3, v5, v4);
        /*SL:193*/ByteArray.write16bit(a4, v5, v4 + 2);
        /*SL:194*/ByteArray.write16bit(v1, v5, v4 + 4);
        /*SL:195*/ByteArray.write16bit(v2, v5, v4 + 6);
        /*SL:197*/this.set(v5);
    }
    
    public int remove(final int a1) {
        final byte[] v1 = /*EL:211*/this.get();
        final int v2 = /*EL:212*/v1.length;
        /*SL:213*/if (v2 < 10) {
            /*SL:214*/return 0;
        }
        final int v3 = /*EL:216*/ByteArray.readU16bit(v1, 0);
        final int v4 = /*EL:217*/2 + a1 * 8;
        /*SL:218*/if (v3 <= a1) {
            /*SL:219*/return v3;
        }
        final byte[] v5 = /*EL:221*/new byte[v2 - 8];
        /*SL:222*/ByteArray.write16bit(v3 - 1, v5, 0);
        int v6 = /*EL:223*/2;
        int v7 = 2;
        /*SL:224*/while (v6 < v2) {
            /*SL:225*/if (v6 == v4) {
                /*SL:226*/v6 += 8;
            }
            else {
                /*SL:228*/v5[v7++] = v1[v6++];
            }
        }
        /*SL:230*/this.set(v5);
        /*SL:231*/return v3 - 1;
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v-10, final Map v-9) {
        final byte[] value = /*EL:243*/this.get();
        final byte[] array = /*EL:244*/new byte[value.length];
        final ConstPool constPool = /*EL:245*/this.getConstPool();
        final InnerClassesAttribute innerClassesAttribute = /*EL:246*/new InnerClassesAttribute(v-10, array);
        final int u16bit = /*EL:247*/ByteArray.readU16bit(value, 0);
        /*SL:248*/ByteArray.write16bit(u16bit, array, 0);
        int n = /*EL:249*/2;
        /*SL:250*/for (int i = 0; i < u16bit; ++i) {
            int a1 = /*EL:251*/ByteArray.readU16bit(value, n);
            int a2 = /*EL:252*/ByteArray.readU16bit(value, n + 2);
            int v1 = /*EL:253*/ByteArray.readU16bit(value, n + 4);
            final int v2 = /*EL:254*/ByteArray.readU16bit(value, n + 6);
            /*SL:256*/if (a1 != 0) {
                /*SL:257*/a1 = constPool.copy(a1, v-10, v-9);
            }
            /*SL:259*/ByteArray.write16bit(a1, array, n);
            /*SL:261*/if (a2 != 0) {
                /*SL:262*/a2 = constPool.copy(a2, v-10, v-9);
            }
            /*SL:264*/ByteArray.write16bit(a2, array, n + 2);
            /*SL:266*/if (v1 != 0) {
                /*SL:267*/v1 = constPool.copy(v1, v-10, v-9);
            }
            /*SL:269*/ByteArray.write16bit(v1, array, n + 4);
            /*SL:270*/ByteArray.write16bit(v2, array, n + 6);
            /*SL:271*/n += 8;
        }
        /*SL:274*/return innerClassesAttribute;
    }
}
