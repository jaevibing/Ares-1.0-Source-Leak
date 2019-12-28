package org.spongepowered.asm.lib;

final class AnnotationWriter extends AnnotationVisitor
{
    private final ClassWriter cw;
    private int size;
    private final boolean named;
    private final ByteVector bv;
    private final ByteVector parent;
    private final int offset;
    AnnotationWriter next;
    AnnotationWriter prev;
    
    AnnotationWriter(final ClassWriter a1, final boolean a2, final ByteVector a3, final ByteVector a4, final int a5) {
        super(327680);
        this.cw = a1;
        this.named = a2;
        this.bv = a3;
        this.parent = a4;
        this.offset = a5;
    }
    
    public void visit(final String v-1, final Object v0) {
        /*SL:121*/++this.size;
        /*SL:122*/if (this.named) {
            /*SL:123*/this.bv.putShort(this.cw.newUTF8(v-1));
        }
        /*SL:125*/if (v0 instanceof String) {
            /*SL:126*/this.bv.put12(115, this.cw.newUTF8((String)v0));
        }
        else/*SL:127*/ if (v0 instanceof Byte) {
            /*SL:128*/this.bv.put12(66, this.cw.newInteger((byte)v0).index);
        }
        else/*SL:129*/ if (v0 instanceof Boolean) {
            final int a1 = /*EL:130*/((boolean)v0) ? 1 : 0;
            /*SL:131*/this.bv.put12(90, this.cw.newInteger(a1).index);
        }
        else/*SL:132*/ if (v0 instanceof Character) {
            /*SL:133*/this.bv.put12(67, this.cw.newInteger((char)v0).index);
        }
        else/*SL:134*/ if (v0 instanceof Short) {
            /*SL:135*/this.bv.put12(83, this.cw.newInteger((short)v0).index);
        }
        else/*SL:136*/ if (v0 instanceof Type) {
            /*SL:137*/this.bv.put12(99, this.cw.newUTF8(((Type)v0).getDescriptor()));
        }
        else/*SL:138*/ if (v0 instanceof byte[]) {
            final byte[] v = /*EL:139*/(byte[])v0;
            /*SL:140*/this.bv.put12(91, v.length);
            /*SL:141*/for (int a2 = 0; a2 < v.length; ++a2) {
                /*SL:142*/this.bv.put12(66, this.cw.newInteger(v[a2]).index);
            }
        }
        else/*SL:144*/ if (v0 instanceof boolean[]) {
            final boolean[] v2 = /*EL:145*/(boolean[])v0;
            /*SL:146*/this.bv.put12(91, v2.length);
            /*SL:147*/for (int v3 = 0; v3 < v2.length; ++v3) {
                /*SL:148*/this.bv.put12(90, this.cw.newInteger(v2[v3] ? 1 : 0).index);
            }
        }
        else/*SL:150*/ if (v0 instanceof short[]) {
            final short[] v4 = /*EL:151*/(short[])v0;
            /*SL:152*/this.bv.put12(91, v4.length);
            /*SL:153*/for (int v3 = 0; v3 < v4.length; ++v3) {
                /*SL:154*/this.bv.put12(83, this.cw.newInteger(v4[v3]).index);
            }
        }
        else/*SL:156*/ if (v0 instanceof char[]) {
            final char[] v5 = /*EL:157*/(char[])v0;
            /*SL:158*/this.bv.put12(91, v5.length);
            /*SL:159*/for (int v3 = 0; v3 < v5.length; ++v3) {
                /*SL:160*/this.bv.put12(67, this.cw.newInteger(v5[v3]).index);
            }
        }
        else/*SL:162*/ if (v0 instanceof int[]) {
            final int[] v6 = /*EL:163*/(int[])v0;
            /*SL:164*/this.bv.put12(91, v6.length);
            /*SL:165*/for (int v3 = 0; v3 < v6.length; ++v3) {
                /*SL:166*/this.bv.put12(73, this.cw.newInteger(v6[v3]).index);
            }
        }
        else/*SL:168*/ if (v0 instanceof long[]) {
            final long[] v7 = /*EL:169*/(long[])v0;
            /*SL:170*/this.bv.put12(91, v7.length);
            /*SL:171*/for (int v3 = 0; v3 < v7.length; ++v3) {
                /*SL:172*/this.bv.put12(74, this.cw.newLong(v7[v3]).index);
            }
        }
        else/*SL:174*/ if (v0 instanceof float[]) {
            final float[] v8 = /*EL:175*/(float[])v0;
            /*SL:176*/this.bv.put12(91, v8.length);
            /*SL:177*/for (int v3 = 0; v3 < v8.length; ++v3) {
                /*SL:178*/this.bv.put12(70, this.cw.newFloat(v8[v3]).index);
            }
        }
        else/*SL:180*/ if (v0 instanceof double[]) {
            final double[] v9 = /*EL:181*/(double[])v0;
            /*SL:182*/this.bv.put12(91, v9.length);
            /*SL:183*/for (int v3 = 0; v3 < v9.length; ++v3) {
                /*SL:184*/this.bv.put12(68, this.cw.newDouble(v9[v3]).index);
            }
        }
        else {
            final Item v10 = /*EL:187*/this.cw.newConstItem(v0);
            /*SL:188*/this.bv.put12(".s.IFJDCS".charAt(v10.type), v10.index);
        }
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:195*/++this.size;
        /*SL:196*/if (this.named) {
            /*SL:197*/this.bv.putShort(this.cw.newUTF8(a1));
        }
        /*SL:199*/this.bv.put12(101, this.cw.newUTF8(a2)).putShort(this.cw.newUTF8(a3));
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        /*SL:205*/++this.size;
        /*SL:206*/if (this.named) {
            /*SL:207*/this.bv.putShort(this.cw.newUTF8(a1));
        }
        /*SL:210*/this.bv.put12(64, this.cw.newUTF8(a2)).putShort(0);
        /*SL:211*/return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        /*SL:216*/++this.size;
        /*SL:217*/if (this.named) {
            /*SL:218*/this.bv.putShort(this.cw.newUTF8(a1));
        }
        /*SL:221*/this.bv.put12(91, 0);
        /*SL:222*/return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
    }
    
    public void visitEnd() {
        /*SL:227*/if (this.parent != null) {
            final byte[] v1 = /*EL:228*/this.parent.data;
            /*SL:229*/v1[this.offset] = (byte)(this.size >>> 8);
            /*SL:230*/v1[this.offset + 1] = (byte)this.size;
        }
    }
    
    int getSize() {
        int v1 = /*EL:244*/0;
        /*SL:246*/for (AnnotationWriter v2 = this; v2 != null; /*SL:248*/v2 = v2.next) {
            v1 += v2.bv.length;
        }
        /*SL:250*/return v1;
    }
    
    void put(final ByteVector a1) {
        int v1 = /*EL:261*/0;
        int v2 = /*EL:262*/2;
        AnnotationWriter v3 = /*EL:263*/this;
        AnnotationWriter v4 = /*EL:264*/null;
        /*SL:265*/while (v3 != null) {
            /*SL:266*/++v1;
            /*SL:267*/v2 += v3.bv.length;
            /*SL:268*/v3.visitEnd();
            /*SL:269*/v3.prev = v4;
            /*SL:270*/v4 = v3;
            /*SL:271*/v3 = v3.next;
        }
        /*SL:273*/a1.putInt(v2);
        /*SL:274*/a1.putShort(v1);
        /*SL:276*/for (v3 = v4; v3 != null; /*SL:278*/v3 = v3.prev) {
            a1.putByteArray(v3.bv.data, 0, v3.bv.length);
        }
    }
    
    static void put(final AnnotationWriter[] v-6, final int v-5, final ByteVector v-4) {
        int a4 = /*EL:294*/1 + 2 * (v-6.length - v-5);
        /*SL:295*/for (int a1 = v-5; a1 < v-6.length; ++a1) {
            /*SL:296*/a4 += ((v-6[a1] == null) ? 0 : v-6[a1].getSize());
        }
        /*SL:298*/v-4.putInt(a4).putByte(v-6.length - v-5);
        /*SL:299*/for (int i = v-5; i < v-6.length; ++i) {
            AnnotationWriter a2 = /*EL:300*/v-6[i];
            AnnotationWriter a3 = /*EL:301*/null;
            int v1 = /*EL:302*/0;
            /*SL:303*/while (a2 != null) {
                /*SL:304*/++v1;
                /*SL:305*/a2.visitEnd();
                /*SL:306*/a2.prev = a3;
                /*SL:307*/a3 = a2;
                /*SL:308*/a2 = a2.next;
            }
            /*SL:310*/v-4.putShort(v1);
            /*SL:312*/for (a2 = a3; a2 != null; /*SL:314*/a2 = a2.prev) {
                v-4.putByteArray(a2.bv.data, 0, a2.bv.length);
            }
        }
    }
    
    static void putTarget(final int a2, final TypePath a3, final ByteVector v1) {
        /*SL:333*/switch (a2 >>> 24) {
            case 0:
            case 1:
            case 22: {
                /*SL:337*/v1.putShort(a2 >>> 16);
                /*SL:338*/break;
            }
            case 19:
            case 20:
            case 21: {
                /*SL:342*/v1.putByte(a2 >>> 24);
                /*SL:343*/break;
            }
            case 71:
            case 72:
            case 73:
            case 74:
            case 75: {
                /*SL:349*/v1.putInt(a2);
                /*SL:350*/break;
            }
            default: {
                /*SL:361*/v1.put12(a2 >>> 24, (a2 & 0xFFFF00) >> 8);
                break;
            }
        }
        /*SL:364*/if (a3 == null) {
            /*SL:365*/v1.putByte(0);
        }
        else {
            final int a4 = /*EL:367*/a3.b[a3.offset] * 2 + 1;
            /*SL:368*/v1.putByteArray(a3.b, a3.offset, a4);
        }
    }
}
