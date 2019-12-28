package org.spongepowered.asm.lib;

public class Attribute
{
    public final String type;
    byte[] value;
    Attribute next;
    
    protected Attribute(final String a1) {
        this.type = a1;
    }
    
    public boolean isUnknown() {
        /*SL:72*/return true;
    }
    
    public boolean isCodeAttribute() {
        /*SL:81*/return false;
    }
    
    protected Label[] getLabels() {
        /*SL:91*/return null;
    }
    
    protected Attribute read(final ClassReader a1, final int a2, final int a3, final char[] a4, final int a5, final Label[] a6) {
        final Attribute v1 = /*EL:128*/new Attribute(this.type);
        /*SL:129*/v1.value = new byte[a3];
        /*SL:130*/System.arraycopy(a1.b, a2, v1.value, 0, a3);
        /*SL:131*/return v1;
    }
    
    protected ByteVector write(final ClassWriter a1, final byte[] a2, final int a3, final int a4, final int a5) {
        final ByteVector v1 = /*EL:161*/new ByteVector();
        /*SL:162*/v1.data = this.value;
        /*SL:163*/v1.length = this.value.length;
        /*SL:164*/return v1;
    }
    
    final int getCount() {
        int v1 = /*EL:173*/0;
        /*SL:175*/for (Attribute v2 = this; v2 != null; /*SL:177*/v2 = v2.next) {
            ++v1;
        }
        /*SL:179*/return v1;
    }
    
    final int getSize(final ClassWriter a1, final byte[] a2, final int a3, final int a4, final int a5) {
        Attribute v1 = /*EL:209*/this;
        int v2 = /*EL:210*/0;
        /*SL:211*/while (v1 != null) {
            /*SL:212*/a1.newUTF8(v1.type);
            /*SL:213*/v2 += v1.write(a1, a2, a3, a4, a5).length + 6;
            /*SL:214*/v1 = v1.next;
        }
        /*SL:216*/return v2;
    }
    
    final void put(final ClassWriter a3, final byte[] a4, final int a5, final int a6, final int v1, final ByteVector v2) {
        /*SL:248*/for (Attribute v3 = this; v3 != null; /*SL:252*/v3 = v3.next) {
            final ByteVector a7 = v3.write(a3, a4, a5, a6, v1);
            v2.putShort(a3.newUTF8(v3.type)).putInt(a7.length);
            v2.putByteArray(a7.data, 0, a7.length);
        }
    }
}
